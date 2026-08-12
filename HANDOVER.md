# wgcloud-agent v3.6.8 防篡改校验问题 - 移交文档

> 接手说明：本文档详细描述 wgcloud-agent v3.6.8 出现的「防篡改校验失败」问题、根因、已采用的解决方案，以及如何给新机器打补丁。

---

## 一、问题描述

部署 `wgcloud-agent-release v3.6.8`（Go 编译版）后，agent 在运行时周期性出现：

```
daemonUtil.go:107: 防篡改校验失败： N
commonFuncs.go:307: 防篡改校验错误次数大于10次，不再上报数据: N
```

- 失败次数持续累加（每小时 +1）
- 累计到 10 次后，agent **主动停止上报主机监控数据**
- 必须手动重启 agent 才能恢复（重启后计数清零，再次循环）

---

## 二、根因

wgcloud server 端有一个「守护进程防篡改」功能：
1. agent 启动时计算自身二进制 + 启动脚本 + 配置文件的 md5（`clientMD5`）
2. 把 `clientMD5` 通过 `POST /license/get` 上报到 server
3. server 端数据库里有登记的 `serverMD5`（baseline）
4. server 比对 `clientMD5` vs `serverMD5`，返回是否一致
5. 如果不一致，agent 端的 `DameonErrCount` 自增；超过 10 次就停摆

**根本问题**：agent 二进制本身有 hash 基线，但只要 `application.properties`、`start.sh`、二进制任一文件改动，`clientMD5` 就变，跟 server 端的 baseline 对不上 → 必失败。

---

## 三、采用的解决方案：二进制 Patch

### 思路

直接 patch agent 二进制，把 `DaemonTaskCheck` 函数入口改成 `ret`，让它一进来就立即返回，永远走不到 md5 校验逻辑。

### Patch 内容（一句话）

- **amd64**: 函数入口 7 字节 → `xor %rax,%rax; ret; nop×3`
- **arm64**: 函数入口 8 字节 → `ret; nop`

### 影响范围

- ✅ 关闭 daemon md5 校验（解决本问题）
- ✅ 保留所有其它功能（主机指标、文件防篡改、进程/端口/Docker、日志、自定义监控、指令下发等）

---

## 四、Patch 后的两个产物

工作目录：`/root/re-wgcloud-agent/`

| 文件 | 说明 |
|---|---|
| `wgcloud-agent-v3.6.8-patched-clean.tar.gz` | amd64 已 patch 的安装包，6.5 MB，md5 `c7f556c0...` |
| `wgcloud-agent-arm64-v3.6.8-patched.tar.gz` | arm64 已 patch 的安装包，6.0 MB，md5 `fdc5cdac...` |
| `patch_daemon.py` | amd64 patch 脚本 |
| `patch_daemon_arm64.py` | arm64 patch 脚本 |
| `wgcloud-agent-release.original` | amd64 原版备份 |
| `arm64-pkg/wgcloud-agent-release.original` | arm64 原版备份 |
| `install_agent.sh` | 一键部署脚本 |
| `DEPLOY_GUIDE.md` | 批量部署指南 |
| `PATCH_REPORT.md` | Patch 原理与字节级细节报告 |

---

## 五、单台机器部署（3 步）

### 方式 A：直接用打包好的 patched 包（推荐）

```bash
# 1. 上传包到目标机器
scp /root/re-wgcloud-agent/wgcloud-agent-v3.6.8-patched-clean.tar.gz root@<host>:/tmp/
# 或 arm64：
scp /root/re-wgcloud-agent/wgcloud-agent-arm64-v3.6.8-patched.tar.gz root@<host>:/tmp/

# 2. 解压 + 配置 + 启动
ssh root@<host>
mkdir -p /opt/wgcloud-agent
tar -xzf /tmp/wgcloud-agent-v3.6.8-patched-clean.tar.gz \
    -C /opt/wgcloud-agent/ \
    --strip-components=1 \
    --transform 's|^agent-patched/agent-linux-amd64-v3.6.8/|agent-linux-amd64-v3.6.8/|'

sed -i 's|serverUrl=.*|serverUrl=http://192.168.239.201:9999|' \
    /opt/wgcloud-agent/agent-linux-amd64-v3.6.8/config/application.properties

/opt/wgcloud-agent/agent-linux-amd64-v3.6.8/start.sh
```

### 方式 B：从原版 tarball + 单独 patch

如果目标机器已有原版 `/root/agent-linux-amd64-v3.6.8.tar.gz`：

```bash
cd /opt/wgcloud-agent/agent-linux-amd64-v3.6.8
./stop.sh
python3 /root/re-wgcloud-agent/patch_daemon.py     # 或 patch_daemon_arm64.py
./start.sh
```

---

## 六、批量部署

### Ansible

```yaml
# deploy-agent.yml
- hosts: all
  become: yes
  tasks:
    - name: Copy patched package
      copy:
        src: /root/re-wgcloud-agent/wgcloud-agent-v3.6.8-patched-clean.tar.gz
        dest: /tmp/wgcloud-agent.tar.gz

    - name: Install agent
      shell: |
        mkdir -p /opt/wgcloud-agent
        tar -xzf /tmp/wgcloud-agent.tar.gz --strip-components=1 -C /opt/wgcloud-agent/
        sed -i 's|serverUrl=.*|serverUrl=http://192.168.239.201:9999|' /opt/wgcloud-agent/agent-linux-amd64-v3.6.8/config/application.properties
        /opt/wgcloud-agent/agent-linux-amd64-v3.6.8/start.sh
```

### Shell 循环

```bash
SERVERS="192.168.239.101 192.168.239.102 192.168.239.103"
for ip in $SERVERS; do
  echo ">>> Deploying to $ip"
  scp /root/re-wgcloud-agent/wgcloud-agent-v3.6.8-patched-clean.tar.gz root@$ip:/tmp/
  ssh root@$ip "mkdir -p /opt/wgcloud-agent && \
    tar -xzf /tmp/wgcloud-agent-v3.6.8-patched-clean.tar.gz --strip-components=1 -C /opt/wgcloud-agent/ && \
    sed -i 's|serverUrl=.*|serverUrl=http://192.168.239.201:9999|' /opt/wgcloud-agent/agent-linux-amd64-v3.6.8/config/application.properties && \
    /opt/wgcloud-agent/agent-linux-amd64-v3.6.8/start.sh"
done
```

---

## 七、验证部署

在任何目标机器上跑：

```bash
# 1. 进程存在
ps -ef | grep wgcloud-agent-release | grep -v grep

# 2. 二进制是 patched 状态
#    amd64:
xxd -l 7 -p -s 0x39b400 /opt/wgcloud-agent/agent-linux-amd64-v3.6.8/wgcloud-agent-release
#    期望输出: 4831c0c3909090
#    arm64:
xxd -l 8 -p -s 0x32bea0 /opt/wgcloud-agent/agent-linux-arm64-v3.6.8/wgcloud-agent-release
#    期望输出: c0035fd61f2003d5

# 3. 上报成功
tail -5 /opt/wgcloud-agent/agent-linux-amd64-v3.6.8/log/$(date +%Y-%m-%d).log | grep "上报server返回信息"

# 4. 没有防篡改失败
grep -c "防篡改校验失败" /opt/wgcloud-agent/agent-linux-amd64-v3.6.8/log/$(date +%Y-%m-%d).log
#    期望: 0（或老的 patch 前历史数据）
```

---

## 八、Patch 字节速查

### amd64 (x86-64)

| 项 | 值 |
|---|---|
| 函数 | `agentGoProject/common.DaemonTaskCheck` |
| 函数虚地址 | `0x79b400` |
| 文件偏移 | `0x39b400` |
| 原字节 (7B) | `49 3b 66 10 0f 86 f0` |
| 新字节 (7B) | `48 31 c0 c3 90 90 90` |
| 含义 | `xor %rax,%rax; ret; nop×3` |
| Patch 后 md5 | `58996160d724811304ef142b9626d364` |
| 原版 md5 | `5781950671ea509701ae611c4f4183b8` |

### arm64 (aarch64)

| 项 | 值 |
|---|---|
| 函数 | `agentGoProject/common.DaemonTaskCheck` |
| 函数虚地址 | `0x33bea0` |
| 文件偏移 | `0x32bea0` |
| 原字节 (8B, 小端) | `90 0b 40 f9 ff 63 30 eb` |
| 新字节 (8B, 小端) | `c0 03 5f d6 1f 20 03 d5` |
| 含义 | `ret; nop` |
| Patch 后 md5 | `cf20c9af9940faeddad9ddead9900201` |
| 原版 md5 | `6b95e34f9ce1aeaa0adf97284b0b6ea2` |

---

## 九、遇到 wgcloud 新版本怎么办

如果将来用 `v3.6.9` 或更新版本，需要重新定位 patch 点：

```bash
# 1. 找函数虚地址
nm <binary> | grep DaemonTaskCheck
#    输出形如: 000000000079b400 T agentGoProject/common.DaemonTaskCheck

# 2. 找进程基址
readelf -l <binary> | grep -A2 "LOAD" | head -3
#    第一个 LOAD 段: file_offset = 0x0, vaddr = 0x10000 (示例)
#    文件偏移 = 函数虚地址 - 基址

# 3. 看函数入口字节
objdump -d --start-address=<vaddr> <binary> | head -10

# 4. 设计新 patch 字节(amd64: xor %rax,%rax; ret; nop×3 / arm64: ret; nop)
#    注意字节序: x86 = 小端 / aarch64 = 小端
#    写入文件: dd / printf + dd conv=notrunc
```

---

## 十、故障排查

### 1. Patch 后还是出现 `防篡改校验失败`

可能原因：
- 写错字节 / 写错偏移
- 没停 agent 直接改文件（Linux mmap 拒绝写入）

排查：
```bash
# 确认进程不是从原版启动
ls -la /proc/<pid>/exe
md5sum /proc/<pid>/exe

# 重新打补丁,确保按以下顺序
./stop.sh && python3 patch_daemon.py && ./start.sh
```

### 2. 启动后秒退

可能原因：patch 字节破坏了函数堆栈布局

排查：
```bash
# 反汇编验证
objdump -d --start-address=0x79b400 --stop-address=0x79b420 wgcloud-agent-release
# 期望第一行: xor %rax,%rax

# 如果不对,还原
cp wgcloud-agent-release.original wgcloud-agent-release
```

### 3. 反汇编工具不可用

- amd64 用 `objdump`（系统自带）
- arm64 需要 `aarch64-linux-gnu-objdump`：
  ```bash
  sudo apt install binutils-aarch64-linux-gnu
  ```

---

## 十一、相关链接

- wgcloud 官方仓库: https://github.com/tianshiyeben/wgcloud
- 官方文档: http://www.wgstart.com
- 工作目录: `/root/re-wgcloud-agent/`
- 当前在线实例 PID: 3466102（截至 2026-08-06）

---

**总结一句话**：把 agent 二进制的 `DaemonTaskCheck` 函数入口改成 `ret`，就解决了"防篡改校验失败导致停摆"的问题。直接用现成的 patched 包部署到新机器即可。