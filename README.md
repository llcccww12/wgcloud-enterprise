# wgcloud-agent v3.6.8 — 防篡改校验问题修复包

本仓库包含 wgcloud-agent v3.6.8 的二进制补丁、修复脚本与相关文档，
用于解决 agent 周期性触发「防篡改校验失败 → 停止上报数据」的问题。

## 文件清单

| 文件 | 说明 |
|---|---|
| `wgcloud-agent-v3.6.8-patched-clean.tar.gz` | amd64 已 patch 的安装包 |
| `wgcloud-agent-arm64-v3.6.8-patched.tar.gz` | arm64 已 patch 的安装包 |
| `patch_daemon.py` | amd64 patch 脚本（idempotent） |
| `patch_daemon_arm64.py` | arm64 patch 脚本（idempotent） |
| `install_agent.sh` | 一键部署脚本 |
| `HANDOVER.md` | 同事交接文档 |
| `DEPLOY_GUIDE.md` | 批量部署指南 |
| `PATCH_REPORT.md` | Patch 字节级细节报告 |
| `extract_go_src.py` | 反编译辅助脚本 |
| `01_*.asm` ~ `04_*.asm` | 关键函数的反汇编文件 |
| `*.summary` | 行号→地址映射 |

## 快速上手

### 直接用 patched 包
```bash
mkdir -p /opt/wgcloud-agent
tar -xzf wgcloud-agent-v3.6.8-patched-clean.tar.gz -C /opt/wgcloud-agent/ \
    --strip-components=1
sed -i 's|serverUrl=.*|serverUrl=http://<your-server>:9999|' \
    /opt/wgcloud-agent/agent-linux-amd64-v3.6.8/config/application.properties
/opt/wgcloud-agent/agent-linux-amd64-v3.6.8/start.sh
```

### 从原版 + patch 脚本
```bash
cd /opt/wgcloud-agent/agent-linux-amd64-v3.6.8
./stop.sh
python3 patch_daemon.py    # 或 patch_daemon_arm64.py
./start.sh
```

## 详细文档

阅读 [HANDOVER.md](HANDOVER.md) 获取完整原理与操作说明。
