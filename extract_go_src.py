#!/usr/bin/env python3
"""
从 objdump 反汇编还原 Go 源码骨架。
objdump --line-numbers 的格式:
   ./D:/.../xxx.go:107
     79b447:  48 89 ...        mov ...
下一行汇编继承上一行的行号。
"""
import re, sys, os, subprocess
from collections import defaultdict

GO_RE = re.compile(r'\./?([\w./\-:]+?\.go):(\d+)')
ASM_RE = re.compile(r'^\s*([0-9a-f]+):')

def parse(path):
    """返回 { (file, line) -> [addr, addr, ...] }"""
    srcmap = defaultdict(list)
    cur_src = None
    with open(path) as f:
        for ln in f:
            g = GO_RE.search(ln)
            if g:
                cur_src = (g.group(1), int(g.group(2)))
                continue
            m = ASM_RE.match(ln)
            if m and cur_src:
                srcmap[cur_src].append(m.group(1))
    return srcmap

def strings_keywords(bin_path, kws):
    r = subprocess.run(['strings', '-n', '6', bin_path],
                       capture_output=True, text=True)
    out = []
    for s in r.stdout.splitlines():
        for k in kws:
            if k in s:
                out.append(s.strip())
                break
    return sorted(set(out))

def dump_func(name, asm_path, keywords):
    print(f'\n========== {name} ==========')
    print(f'  汇编文件: {asm_path}')
    srcmap = parse(asm_path)
    if not srcmap:
        print('  ⚠ 没解析到 .go 行号'); return
    by_file = defaultdict(list)
    for (f, ln), addrs in srcmap.items():
        by_file[f].append((ln, addrs))
    for f, items in by_file.items():
        items.sort()
        print(f'\n  --- {f} ---')
        print(f'    覆盖行号: L{items[0][0]} ~ L{items[-1][0]} (共 {len(items)} 行)')
        # 全部打印(行号 -> 入口地址)
        for ln, addrs in items:
            print(f'    L{ln:<4} -> {addrs[0]}  (共 {len(addrs)} 条指令)')
    # 字符串
    s = strings_keywords('/root/agent-linux-amd64-v3.6.8/wgcloud-agent-release', keywords)
    if s:
        print(f'\n  --- 嵌入字符串({len(s)}) ---')
        for x in s:
            print(f'    "{x[:120]}"')

if __name__ == '__main__':
    KW = ['防篡改','校验','失败','md5','MD5','文件防篡改','wgcloud','bindIp',
          'wgToken','serverUrl','license','License','daemon','Daemon',
          '请求server','不再上报','帮助说明','docs83','请查看','返回信息',
          '返回结果','getMd5Val','licenseInfo','LicenseInfo']
    cases = [
        ('DaemonTaskCheck  (daemonUtil.go:101)', '01_DaemonTaskCheck.asm', KW),
        ('getMd5Val        (fileSafeUtil.go:192)', '02_getMd5Val.asm', KW),
        ('GetFileSafeInfos', '03_GetFileSafeInfos.asm', KW),
        ('checkFileDirSafe', '04_checkFileDirSafe.asm', KW),
    ]
    for name, p, k in cases:
        dump_func(name, p, k)