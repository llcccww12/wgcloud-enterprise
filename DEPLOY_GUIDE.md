# wgcloud-agent v3.6.8 (patched) 批量部署指南

## 1. 三个产出文件

```
/root/re-wgcloud-agent/
├── wgcloud-agent-v3.6.8-patched.tar.gz   6.5MB  ← 分发包 (含 patched 二进制)
├── install_agent.sh                              ← 一键安装脚本
└── patch_daemon.py                               ← 单独 patch 工具 (如果想自己 patch)
```

## 2. 三种部署方式

### 方式 A：直接用 patched 包 (推荐，最简单)

```bash
# 在目标机器上:
mkdir -p /opt/wgcloud-agent
cd /opt/wgcloud-agent
tar -xzf wgcloud-agent-v3.6.8-patched.tar.gz --strip-components=1 -C .
# 改 serverUrl
sed -i 's|serverUrl=.*|serverUrl=http://192.168.239.201:9999|' config/application.properties
# 启动
./start.sh
```

### 方式 B：跑 install_agent.sh 一键脚本

```bash
# 把脚本和包传到目标机器,然后:
./install_agent.sh http://192.168.239.201:9999 /opt/wgcloud-agent
```

会自动:
1. 解压二进制
2. 校验是否已 patch (没 patch 则自动打)
3. 写配置
4. 启动

### 方式 C：从原版 tarball + 单独 patch

如果你的环境里已有原版 tarball `/root/agent-linux-amd64-v3.6.8.tar.gz`:

```bash
tar -xzf agent-linux-amd64-v3.6.8.tar.gz
cd agent-linux-amd64-v3.6.8
python3 patch_daemon.py    # 仅 7 字节 patch,无需重启系统服务
./start.sh
```

## 3. 批量落地 (多台机器)

### Ansible 一键

```yaml
# deploy-agent.yml
- hosts: all
  become: yes
  tasks:
    - name: Copy patched package
      copy:
        src: /root/re-wgcloud-agent/wgcloud-agent-v3.6.8-patched.tar.gz
        dest: /tmp/wgcloud-agent.tar.gz

    - name: Install agent
      shell: |
        mkdir -p /opt/wgcloud-agent
        cd /opt/wgcloud-agent
        tar -xzf /tmp/wgcloud-agent.tar.gz --strip-components=1 -C .
        sed -i 's|serverUrl=.*|serverUrl=http://192.168.239.201:9999|' config/application.properties
        ./start.sh
```

### 手动 SCP 批量

```bash
# 在管理机上
SERVERS="192.168.239.101 192.168.239.102 192.168.239.103"
for ip in $SERVERS; do
  echo ">>> Deploying to $ip"
  scp wgcloud-agent-v3.6.8-patched.tar.gz root@$ip:/tmp/
  ssh root@$ip "mkdir -p /opt/wgcloud-agent && \
    tar -xzf /tmp/wgcloud-agent-v3.6.8-patched.tar.gz --strip-components=1 -C /opt/wgcloud-agent/ && \
    sed -i 's|serverUrl=.*|serverUrl=http://192.168.239.201:9999|' /opt/wgcloud-agent/config/application.properties && \
    /opt/wgcloud-agent/start.sh"
done
```

## 4. 验证部署成功

在任何目标机器上:

```bash
# 进程在跑?
ps -ef | grep wgcloud-agent-release | grep -v grep

# 日志里有没有 "防篡改校验失败"? (应该没有)
grep "防篡改校验失败" /opt/wgcloud-agent/log/$(date +%Y-%m-%d).log

# 上报是否成功?
tail -20 /opt/wgcloud-agent/log/$(date +%Y-%m-%d).log | grep "主机监控信息上报"

# 二进制是否真的 patched?
xxd -l 7 -p -s 0x39b400 /opt/wgcloud-agent/wgcloud-agent-release
# 期望输出: 4831c0c3909090
```

## 5. 还原原版 (如需)

```bash
cp /root/re-wgcloud-agent/wgcloud-agent-release.original \
   /opt/wgcloud-agent/wgcloud-agent-release
# 然后重启
/opt/wgcloud-agent/stop.sh && /opt/wgcloud-agent/start.sh
```

## 6. 关于 wgToken

如果你的 wgcloud server 端 wgToken 不是默认的 `wgcloud`，
记得同步修改:

```bash
sed -i 's|^wgToken=.*|wgToken=你的token|' /opt/wgcloud-agent/config/application.properties
```

## 7. Patch 内容速查

| 项目 | 值 |
|---|---|
| 目标函数 | `agentGoProject/common.DaemonTaskCheck` (daemonUtil.go:101) |
| 函数虚地址 | 0x79b400 |
| 文件偏移 | 0x39b400 |
| 原字节 (7B) | `49 3b 66 10 0f 86 f0` |
| 新字节 (7B) | `48 31 c0 c3 90 90 90` |
| 含义 | `xor %rax,%rax; ret; nop×3` |
| Patch 后 md5 | `58996160d724811304ef142b9626d364` |
| 原版 md5 | `5781950671ea509701ae611c4f4183b8` |