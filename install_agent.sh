#!/bin/bash
# ====================================================================
# wgcloud-agent v3.6.8 一键安装脚本 (patched 版)
# 用法: ./install_agent.sh <server_url> [install_dir]
# 例:  ./install_agent.sh http://192.168.239.201:9999 /opt/wgcloud-agent
# ====================================================================
set -e

SERVER_URL="${1:-http://192.168.239.201:9999}"
INSTALL_DIR="${2:-/opt/wgcloud-agent}"
BIN_NAME="wgcloud-agent-release"
TARBALL_URL="https://your-internal-mirror/agent-linux-amd64-v3.6.8.tar.gz"

echo "==============================================="
echo "  wgcloud-agent 一键部署 (patched)"
echo "  server:    ${SERVER_URL}"
echo "  install:   ${INSTALL_DIR}"
echo "==============================================="

# 1. 创建目录
mkdir -p "${INSTALL_DIR}"
cd "${INSTALL_DIR}"

# 2. 如果二进制不存在,从 tarball 部署
if [ ! -f "${BIN_NAME}" ]; then
  echo "[1/4] 部署二进制..."
  if [ -f "/root/agent-linux-amd64-v3.6.8.tar.gz" ]; then
    tar -xzf /root/agent-linux-amd64-v3.6.8.tar.gz -C /tmp/
    cp -r /tmp/agent-linux-amd64-v3.6.8/* .
    rm -rf /tmp/agent-linux-amd64-v3.6.8
  else
    echo "  ERROR: 找不到 agent tarball (/root/agent-linux-amd64-v3.6.8.tar.gz)"
    exit 1
  fi
fi

# 3. 验证二进制
echo "[2/4] 验证二进制完整性..."
chmod +x ${BIN_NAME}
md5sum ${BIN_NAME}

# 4. 检查是否已经 patched
FIRST7=$(xxd -l 7 -p -s 0x39b400 ${BIN_NAME} 2>/dev/null)
if [ "$FIRST7" = "4831c0c3909090" ]; then
  echo "  ✓ 已 patched (DaemonTaskCheck -> return 0)"
elif [ "$FIRST7" = "493b66100f86f0" ]; then
  echo "  → 未 patched,正在打补丁..."
  printf '\x48\x31\xc0\xc3\x90\x90\x90' | dd of=${BIN_NAME} bs=1 seek=$((0x39b400)) count=7 conv=notrunc 2>/dev/null
  echo "  ✓ patch 完成"
  echo "  → 校验..."
  FIRST7_AFTER=$(xxd -l 7 -p -s 0x39b400 ${BIN_NAME} 2>/dev/null)
  if [ "$FIRST7_AFTER" = "4831c0c3909090" ]; then
    echo "  ✓ patch 验证通过"
  else
    echo "  ERROR: patch 失败,期望 4831c0c3909090,实际 $FIRST7_AFTER"
    exit 1
  fi
else
  echo "  ⚠ 未知二进制状态 ($FIRST7),跳过 patch"
fi

# 5. 写配置
echo "[3/4] 配置..."
cat > config/application.properties <<EOF
serverUrl=${SERVER_URL}
bindIp=
wgToken=wgcloud
submitSeconds=120
hostAttachSeconds=300
smartOn=no
shellToRun=yes
shellGetSeconds=300
exceptionProcess=yes
gatherAllProcess=yes
gatherAllDocker=no
lastLoginInfo=yes
gatherGpuCmd=nvidia-smi
gatherFireWallCmd=
gatherLike=
logDays=180
logCheckSeconds=600
customDataSeconds=600
netInterface=
portModel=2
portListenShell=
ifconfigShell=
EOF

# 6. 启动
echo "[4/4] 启动..."
./start.sh

sleep 3
echo ""
echo "==============================================="
echo "  部署完成!"
echo "  进程: $(ps -ef | grep wgcloud-agent-release | grep -v grep | awk '{print $2}')"
echo "  日志: tail -f ${INSTALL_DIR}/log/$(date +%Y-%m-%d).log"
echo "==============================================="