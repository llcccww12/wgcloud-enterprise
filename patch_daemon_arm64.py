#!/usr/bin/env python3
"""
Patch wgcloud-agent-release (aarch64) 让 DaemonTaskCheck 立即返回。
- 目标函数: agentGoProject/common.DaemonTaskCheck
- vaddr: 0x33bea0
- .text 段: file_off=0x1000, vaddr=0x11000 (LOAD vaddr=0x10000)
- 文件偏移 = 0x33bea0 - 0x10000 = 0x32bea0
- 原 4 条指令 (16B):
    f9400b90  ldr x16, [x28, #16]
    eb3063ff  cmp sp, x16
    54000789  b.ls 0x33bf98
    f81b0ffe  str x30, [sp, #-80]!
- 新 2 条指令 (8B):
    d65f03c0  ret
    d503201f  nop
"""
import sys, shutil, hashlib, subprocess

BIN = '/root/re-wgcloud-agent/arm64-pkg/agent-linux-arm64-v3.6.8/wgcloud-agent-release'
BAK = '/root/re-wgcloud-agent/arm64-pkg/wgcloud-agent-release.original'
OFFSET = 0x32bea0
OLD = bytes.fromhex('900b40f9ff6330eb')          # 8 字节(小端)
NEW = bytes.fromhex('c0035fd61f2003d5')          # 8 字节(小端)
assert len(OLD) == 8 == len(NEW), "size mismatch"

if not shutil.os.path.exists(BAK):
    shutil.copy2(BIN, BAK)
    print(f"  ✓ 已备份 -> {BAK}")

with open(BIN, 'rb') as f:
    f.seek(OFFSET)
    cur = f.read(8)
    if cur == OLD:
        print(f"  ✓ 字节校验通过: {cur.hex()}")
    elif cur == NEW:
        print(f"  ⚠ 字节已是 patched 状态: {cur.hex()}")
        sys.exit(0)
    else:
        print(f"  ✗ 字节不匹配! 期望 {OLD.hex()}, 实际 {cur.hex()}")
        sys.exit(1)

with open(BIN, 'rb+') as f:
    f.seek(OFFSET)
    f.write(NEW)

with open(BIN, 'rb') as f:
    f.seek(OFFSET)
    after = f.read(8)
print(f"  ✓ patch 完成: {after.hex()}")

print("\n=== 反汇编验证 ===")
subprocess.run(['aarch64-linux-gnu-objdump', '-d',
                '--start-address=0x33bea0',
                '--stop-address=0x33bed0',
                BIN], check=False)

def md5(path):
    h = hashlib.md5()
    with open(path, 'rb') as f:
        h.update(f.read())
    return h.hexdigest()
print("\n=== Hash ===")
print(f"  原: {md5(BAK)}")
print(f"  新: {md5(BIN)}")