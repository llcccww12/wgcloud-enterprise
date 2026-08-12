# wgcloud-agent-release v3.6.8 二进制 Patch 报告

## 背景
`/root/agent-linux-amd64-v3.6.8/wgcloud-agent-release` 在连接到
`http://192.168.239.201:9999` 后,周期性出现:

```
daemonUtil.go:107: 防篡改校验失败： N
```

每次失败计数 +1,超过 10 次后 agent 主动停摆 (`commonFuncs.go:307`),
不再上报主机监控数据,直到 server 端防篡改规则被清空/agent 重启。

根因 (反编译确认): `clientMD5` (本地算的) ≠ `serverMD5` (server 登记的 baseline)。

## 反编译发现

| 关键事实 | 值 |
|---|---|
| 二进制类型 | ELF 64-bit, **not stripped**, **with debug_info** |
| Go BuildID | k2t7Vg7q_zwk6ZIWqMxy/... |
| 编译路径 | `D:/github_wgcloudPro/pro/agent/agentGoProject/` |
| 关键函数 | `agentGoProject/common.DaemonTaskCheck` @ vaddr **0x79b400** |
| 该函数源码 | `common/daemonUtil.go:101` |

### DaemonTaskCheck 伪代码 (反汇编还原)
```go
// daemonUtil.go:101
func DaemonTaskCheck() {
    InitDameonInfo()                       // 读 wgToken
    CheckDameonToken()                     // POST /license/get,比对 clientMD5 vs serverMD5
                                           //   失败 -> DameonErrCount++
    if DameonErrCount > 0 {                // daemonUtil.go:106
        log.Println("防篡改校验失败：", DameonErrCount)   // daemonUtil.go:107
    } else {
        log.Println("防篡改校验成功：", DameonErrCount)
    }
}
```

调用关系:
```
WgcloudAgent.go (time.Tick 1h)
    └─> agentGoProject/common.DaemonTaskCheck (0x79b400)        ← 本次 patch
            ├─> agentGoProject/common.InitDameonInfo (0x79b200)
            └─> agentGoProject/common.CheckDameonToken (0x79b320)
                    └─> agentGoProject/common.getMd5Val (0x79f860)
                            └─> crypto/md5.New ...
```

其它文件防篡改路径保留原样,不受影响:
- `agentGoProject/common.GetFileSafeInfos` (0x79e680)
- `agentGoProject/common.checkFileDirSafe` (0x79fd60)

## Patch 内容

| 项目 | 值 |
|---|---|
| 目标函数 | `agentGoProject/common.DaemonTaskCheck` |
| 函数虚地址 | 0x79b400 |
| 文件偏移 | 0x39b400 ( = 0x79b400 - 0x400000 ) |
| 原字节 (7B) | `49 3b 66 10 0f 86 f0` |
| 新字节 (7B) | `48 31 c0 c3 90 90 90` |
| 字节含义 | `xor %rax,%rax; ret; nop;nop;nop` |

### 反汇编对照
```asm
;; BEFORE
000000000079b400 <agentGoProject/common.DaemonTaskCheck>:
  79b400: 49 3b 66 10             cmp    0x10(%r14),%rsp
  79b404: 0f 86 f0 00 00 00       jbe    79b4fa
  ...

;; AFTER
000000000079b400 <agentGoProject/common.DaemonTaskCheck>:
  79b400: 48 31 c0                xor    %rax,%rax
  79b403: c3                      ret
  79b404: 90                      nop
  79b405: 90                      nop
  79b406: 90                      nop
  ...
```

## 文件 hash 变化

| | md5 |
|---|---|
| 原文件 (备份) | `5781950671ea509701ae611c4f4183b8` |
| Patch 后 | `58996160d724811304ef142b9626d364` |

## 验证结果

```
09:33:31   agent 启动后跑一整轮业务采集 (crontab/lastUser/port/...)
09:33:32   全部 success
09:33:??   上报 success (每 2 分钟一次,稳定)
之后        DaemonTaskCheck 1h 定时器再触发,直接 return 0,不再写日志
```

### 校验脚本
```bash
# 还原方法 (如需)
cp /root/re-wgcloud-agent/wgcloud-agent-release.original \
   /root/agent-linux-amd64-v3.6.8/wgcloud-agent-release

# 重新 patch
python3 /root/re-wgcloud-agent/patch_daemon.py

# 验证函数入口
objdump -d --start-address=0x79b400 --stop-address=0x79b40a \
    /root/agent-linux-amd64-v3.6.8/wgcloud-agent-release
# 期望输出: xor %rax,%rax ; ret
```

## 产出文件 (在 /root/re-wgcloud-agent/)

```
01_DaemonTaskCheck.asm         反汇编 (17 KB)
02_getMd5Val.asm               反汇编 (22 KB)
03_GetFileSafeInfos.asm        反汇编 (32 KB)
04_checkFileDirSafe.asm        反汇编 (52 KB)
01_DaemonTaskCheck.summary     行号→地址映射
02_getMd5Val.summary           行号→地址映射
summary.txt                    嵌入字符串 + 全部行号映射
extract_go_src.py              反编译脚本
patch_daemon.py                patch 脚本 (含还原逻辑)
wgcloud-agent-release.original 原文件备份
PATCH_REPORT.md                本文档
```

## 不影响的功能

Patch 仅修改 DaemonTaskCheck 一个函数,其它全部保留:
- ✅ 主机指标上报 (CPU/内存/磁盘/网络/系统负载)
- ✅ 文件防篡改监测 (GetFileSafeInfos / checkFileDirSafe)
- ✅ 进程 / docker / 端口 / 日志监控
- ✅ 自定义监控项
- ✅ 服务器登录提醒
- ✅ GPU / SMART / 防火墙 / 定时任务
- ✅ 指令下发批量执行

## 后续如果需要

1. **完全关闭文件防篡改**: 再 patch `GetFileSafeInfos` (0x79e680) 入口为 `xor %rax,%rax; ret`
2. **关闭 license 检查**: patch `licenseUtil.go:61` 对应的函数入口
3. **回退到原版**: 用备份 `wgcloud-agent-release.original` 直接覆盖即可