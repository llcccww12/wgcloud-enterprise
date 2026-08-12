#!/usr/bin/env python3
"""
Patch wgcloud-agent-release 让 DaemonTaskCheck 立即返回。
目的: 关闭 daemon 路径的 md5 校验,避免 agent 反复因为 serverMD5 != clientMD5 停摆。

函数位置:
  - agentGoProject/common.DaemonTaskCheck @ vaddr 0x79b400
  - .text 段 vaddr=0x401000 file_off=0x1000
  - 文件偏移 = 0x79b400 - 0x400000 = 0x39b400

Patch 内容 (7字节 -> 7字节):
  原: 49 3b 66 10 0f 86 f0   (cmp + jbe = Go runtime stack check)
  新: 48 c7 c0 00 00 00 00   (mov $0,%rax)
     c3                       (ret)
  含义: 函数一进来就返回 0,什么都不做

备份策略: 先复制为 .original, 失败可还原
"""
import sys, shutil, hashlib, subprocess

BIN = '/root/agent-linux-amd64-v3.6.8/wgcloud-agent-release'
BAK = '/root/re-wgcloud-agent/wgcloud-agent-release.original'
OFFSET = 0x39b400
OLD = bytes.fromhex('493b66100f86f0')                       # 7 字节
# 新字节: xor %rax,%rax(3) + ret(1) + nop(3) = 7 字节
NEW = bytes.fromhex('4831c0c390909090'[:14])               # 取前 7 字节
assert len(OLD) == 7 == len(NEW), f"size mismatch OLD={len(OLD)} NEW={len(NEW)}"

if not shutil.os.path.exists(BAK):
    shutil.copy2(BIN, BAK)
    print(f"  ✓ 已备份原文件 -> {BAK}")

# 1. 校验当前字节匹配
with open(BIN, 'rb') as f:
    f.seek(OFFSET)
    cur = f.read(7)
    if cur == OLD:
        print(f"  ✓ 字节校验通过: {cur.hex()}")
    elif cur == NEW:
        print(f"  ⚠ 字节已是 patch 状态: {cur.hex()}")
        sys.exit(0)
    else:
        print(f"  ✗ 字节不匹配! 期望 {OLD.hex()}, 实际 {cur.hex()}")
        sys.exit(1)

# 2. 改写
with open(BIN, 'rb+') as f:
    f.seek(OFFSET)
    f.write(NEW)

# 3. 验证
with open(BIN, 'rb') as f:
    f.seek(OFFSET)
    after = f.read(7)
print(f"  ✓ patch 完成, 新字节: {after.hex()}")

# 4. 显示新反汇编
print("\n=== 反汇编验证 ===")
subprocess.run(['objdump', '-d',
                f'--start-address=0x79b400',
                f'--stop-address=0x79b420',
                BIN], check=False)

# 5. 显示新旧 hash
print("\n=== Hash 校验 ===")
def md5(path):
    h = hashlib.md5()
    with open(path, 'rb') as f:
        h.update(f.read())
    return h.hexdigest()
print(f"  原文件 md5: {md5(BAK)}")
print(f"  现文件 md5: {md5(BIN)}")