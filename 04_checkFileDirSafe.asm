============ 4. 反汇编 checkFileDirSafe（关键 hash 比较）============

/root/agent-linux-amd64-v3.6.8/wgcloud-agent-release:     file format elf64-x86-64


Disassembly of section .text:

000000000079fd60 <agentGoProject/common.checkFileDirSafe>:
agentGoProject/common.checkFileDirSafe():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:242
  79fd60:	4c 8d a4 24 08 ff ff 	lea    -0xf8(%rsp),%r12
  79fd67:	ff 
  79fd68:	4d 3b 66 10          	cmp    0x10(%r14),%r12
  79fd6c:	0f 86 09 07 00 00    	jbe    7a047b <agentGoProject/common.checkFileDirSafe+0x71b>
  79fd72:	48 81 ec 78 01 00 00 	sub    $0x178,%rsp
  79fd79:	48 89 ac 24 70 01 00 	mov    %rbp,0x170(%rsp)
  79fd80:	00 
  79fd81:	48 8d ac 24 70 01 00 	lea    0x170(%rsp),%rbp
  79fd88:	00 
  79fd89:	48 89 9c 24 88 01 00 	mov    %rbx,0x188(%rsp)
  79fd90:	00 
  79fd91:	48 89 84 24 80 01 00 	mov    %rax,0x180(%rsp)
  79fd98:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:245
  79fd99:	e8 c2 5d ff ff       	call   795b60 <agentGoProject/common.PathExists>
  79fd9e:	66 90                	xchg   %ax,%ax
  79fda0:	84 c0                	test   %al,%al
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:246
  79fda2:	0f 84 80 04 00 00    	je     7a0228 <agentGoProject/common.checkFileDirSafe+0x4c8>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:251
  79fda8:	48 8b 84 24 80 01 00 	mov    0x180(%rsp),%rax
  79fdaf:	00 
  79fdb0:	48 8b 9c 24 88 01 00 	mov    0x188(%rsp),%rbx
  79fdb7:	00 
  79fdb8:	e8 23 22 00 00       	call   7a1fe0 <agentGoProject/common.scanAllFile>
  79fdbd:	0f 1f 00             	nopl   (%rax)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:252
  79fdc0:	48 85 ff             	test   %rdi,%rdi
  79fdc3:	0f 84 af 00 00 00    	je     79fe78 <agentGoProject/common.checkFileDirSafe+0x118>
  79fdc9:	48 89 bc 24 90 00 00 	mov    %rdi,0x90(%rsp)
  79fdd0:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:253
  79fdd1:	44 0f 11 bc 24 f0 00 	movups %xmm15,0xf0(%rsp)
  79fdd8:	00 00 
  79fdda:	44 0f 11 bc 24 00 01 	movups %xmm15,0x100(%rsp)
  79fde1:	00 00 
  79fde3:	48 8d 15 b6 1e 05 00 	lea    0x51eb6(%rip),%rdx        # 7f1ca0 <type:*+0x2fca0>
  79fdea:	48 89 94 24 f0 00 00 	mov    %rdx,0xf0(%rsp)
  79fdf1:	00 
  79fdf2:	48 8d 15 b7 01 17 00 	lea    0x1701b7(%rip),%rdx        # 90ffb0 <runtime.buildVersion.str+0xd10>
  79fdf9:	48 89 94 24 f8 00 00 	mov    %rdx,0xf8(%rsp)
  79fe00:	00 
  79fe01:	74 06                	je     79fe09 <agentGoProject/common.checkFileDirSafe+0xa9>
  79fe03:	48 8b 57 08          	mov    0x8(%rdi),%rdx
  79fe07:	eb 03                	jmp    79fe0c <agentGoProject/common.checkFileDirSafe+0xac>
  79fe09:	48 89 fa             	mov    %rdi,%rdx
  79fe0c:	48 89 b4 24 d0 00 00 	mov    %rsi,0xd0(%rsp)
  79fe13:	00 
  79fe14:	48 89 94 24 00 01 00 	mov    %rdx,0x100(%rsp)
  79fe1b:	00 
  79fe1c:	48 89 b4 24 08 01 00 	mov    %rsi,0x108(%rsp)
  79fe23:	00 
  79fe24:	48 8d 84 24 f0 00 00 	lea    0xf0(%rsp),%rax
  79fe2b:	00 
  79fe2c:	bb 02 00 00 00       	mov    $0x2,%ebx
  79fe31:	48 89 d9             	mov    %rbx,%rcx
  79fe34:	e8 e7 d0 de ff       	call   58cf20 <log.Println>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:254
  79fe39:	48 8b 94 24 90 00 00 	mov    0x90(%rsp),%rdx
  79fe40:	00 
  79fe41:	48 8b 52 18          	mov    0x18(%rdx),%rdx
  79fe45:	48 8b 84 24 d0 00 00 	mov    0xd0(%rsp),%rax
  79fe4c:	00 
  79fe4d:	ff d2                	call   *%rdx
  79fe4f:	b9 12 00 00 00       	mov    $0x12,%ecx
  79fe54:	48 89 c7             	mov    %rax,%rdi
  79fe57:	48 89 de             	mov    %rbx,%rsi
  79fe5a:	31 c0                	xor    %eax,%eax
  79fe5c:	48 8d 1d ef 3e 0d 00 	lea    0xd3eef(%rip),%rbx        # 873d52 <go:string.*+0x678a>
  79fe63:	e8 d8 04 cb ff       	call   450340 <runtime.concatstring2>
  79fe68:	48 8b ac 24 70 01 00 	mov    0x170(%rsp),%rbp
  79fe6f:	00 
  79fe70:	48 81 c4 78 01 00 00 	add    $0x178,%rsp
  79fe77:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:251
  79fe78:	48 89 4c 24 78       	mov    %rcx,0x78(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:288
  79fe7d:	48 89 84 24 b8 00 00 	mov    %rax,0xb8(%rsp)
  79fe84:	00 
  79fe85:	48 89 5c 24 70       	mov    %rbx,0x70(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:258
  79fe8a:	48 8b 1d c7 38 40 00 	mov    0x4038c7(%rip),%rbx        # ba3758 <agentGoProject/common.fileSaveDirInfosMap>
  79fe91:	48 8d 05 68 af 06 00 	lea    0x6af68(%rip),%rax        # 80ae00 <type:*+0x48e00>
  79fe98:	48 8b 8c 24 80 01 00 	mov    0x180(%rsp),%rcx
  79fe9f:	00 
  79fea0:	48 8b bc 24 88 01 00 	mov    0x188(%rsp),%rdi
  79fea7:	00 
  79fea8:	e8 33 2e c7 ff       	call   412ce0 <runtime.mapaccess1_faststr>
  79fead:	48 83 78 08 00       	cmpq   $0x0,0x8(%rax)
  79feb2:	0f 84 35 03 00 00    	je     7a01ed <agentGoProject/common.checkFileDirSafe+0x48d>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:266
  79feb8:	48 8b 1d 99 38 40 00 	mov    0x403899(%rip),%rbx        # ba3758 <agentGoProject/common.fileSaveDirInfosMap>
  79febf:	48 8d 05 3a af 06 00 	lea    0x6af3a(%rip),%rax        # 80ae00 <type:*+0x48e00>
  79fec6:	48 8b 8c 24 80 01 00 	mov    0x180(%rsp),%rcx
  79fecd:	00 
  79fece:	48 8b bc 24 88 01 00 	mov    0x188(%rsp),%rdi
  79fed5:	00 
  79fed6:	e8 05 2e c7 ff       	call   412ce0 <runtime.mapaccess1_faststr>
  79fedb:	48 83 78 08 00       	cmpq   $0x0,0x8(%rax)
  79fee0:	75 09                	jne    79feeb <agentGoProject/common.checkFileDirSafe+0x18b>
  79fee2:	31 db                	xor    %ebx,%ebx
  79fee4:	31 c0                	xor    %eax,%eax
  79fee6:	e9 2d 02 00 00       	jmp    7a0118 <agentGoProject/common.checkFileDirSafe+0x3b8>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:268
  79feeb:	48 8b 44 24 70       	mov    0x70(%rsp),%rax
  79fef0:	bb 0a 00 00 00       	mov    $0xa,%ebx
  79fef5:	e8 86 db cd ff       	call   47da80 <strconv.FormatInt>
  79fefa:	48 89 84 24 e8 00 00 	mov    %rax,0xe8(%rsp)
  79ff01:	00 
  79ff02:	48 89 9c 24 a0 00 00 	mov    %rbx,0xa0(%rsp)
  79ff09:	00 
  79ff0a:	48 8b 0d 47 38 40 00 	mov    0x403847(%rip),%rcx        # ba3758 <agentGoProject/common.fileSaveDirInfosMap>
  79ff11:	48 8b bc 24 88 01 00 	mov    0x188(%rsp),%rdi
  79ff18:	00 
  79ff19:	48 8d 05 e0 ae 06 00 	lea    0x6aee0(%rip),%rax        # 80ae00 <type:*+0x48e00>
  79ff20:	48 89 cb             	mov    %rcx,%rbx
  79ff23:	48 8b 8c 24 80 01 00 	mov    0x180(%rsp),%rcx
  79ff2a:	00 
  79ff2b:	e8 b0 2d c7 ff       	call   412ce0 <runtime.mapaccess1_faststr>
  79ff30:	48 8b 48 08          	mov    0x8(%rax),%rcx
  79ff34:	48 8b 00             	mov    (%rax),%rax
  79ff37:	48 8b 94 24 a0 00 00 	mov    0xa0(%rsp),%rdx
  79ff3e:	00 
  79ff3f:	90                   	nop
  79ff40:	48 39 ca             	cmp    %rcx,%rdx
  79ff43:	75 1a                	jne    79ff5f <agentGoProject/common.checkFileDirSafe+0x1ff>
  79ff45:	48 8b 9c 24 e8 00 00 	mov    0xe8(%rsp),%rbx
  79ff4c:	00 
  79ff4d:	e8 ee 34 c6 ff       	call   403440 <runtime.memequal>
  79ff52:	84 c0                	test   %al,%al
  79ff54:	74 09                	je     79ff5f <agentGoProject/common.checkFileDirSafe+0x1ff>
  79ff56:	31 db                	xor    %ebx,%ebx
  79ff58:	31 c0                	xor    %eax,%eax
  79ff5a:	e9 b9 01 00 00       	jmp    7a0118 <agentGoProject/common.checkFileDirSafe+0x3b8>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:270
  79ff5f:	48 8b 1d f2 37 40 00 	mov    0x4037f2(%rip),%rbx        # ba3758 <agentGoProject/common.fileSaveDirInfosMap>
  79ff66:	48 8d 05 93 ae 06 00 	lea    0x6ae93(%rip),%rax        # 80ae00 <type:*+0x48e00>
  79ff6d:	48 8b 8c 24 80 01 00 	mov    0x180(%rsp),%rcx
  79ff74:	00 
  79ff75:	48 8b bc 24 88 01 00 	mov    0x188(%rsp),%rdi
  79ff7c:	00 
  79ff7d:	0f 1f 00             	nopl   (%rax)
  79ff80:	e8 5b 2d c7 ff       	call   412ce0 <runtime.mapaccess1_faststr>
  79ff85:	48 8b 10             	mov    (%rax),%rdx
  79ff88:	48 8b 58 08          	mov    0x8(%rax),%rbx
  79ff8c:	48 89 d0             	mov    %rdx,%rax
  79ff8f:	b9 0a 00 00 00       	mov    $0xa,%ecx
  79ff94:	bf 40 00 00 00       	mov    $0x40,%edi
  79ff99:	e8 42 8d cd ff       	call   478ce0 <strconv.ParseInt>
  79ff9e:	66 90                	xchg   %ax,%ax
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:271
  79ffa0:	48 85 db             	test   %rbx,%rbx
  79ffa3:	0f 85 95 01 00 00    	jne    7a013e <agentGoProject/common.checkFileDirSafe+0x3de>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:275
  79ffa9:	48 8b 4c 24 70       	mov    0x70(%rsp),%rcx
  79ffae:	48 29 c8             	sub    %rcx,%rax
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:276
  79ffb1:	0f 57 c0             	xorps  %xmm0,%xmm0
  79ffb4:	f2 48 0f 2a c0       	cvtsi2sd %rax,%xmm0
./C:/Program Files/Go/src/math/unsafe.go:23
  79ffb9:	66 48 0f 7e c2       	movq   %xmm0,%rdx
./C:/Program Files/Go/src/math/abs.go:14
  79ffbe:	48 0f ba f2 3f       	btr    $0x3f,%rdx
./C:/Program Files/Go/src/math/unsafe.go:29
  79ffc3:	66 48 0f 6e c2       	movq   %rdx,%xmm0
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:276
  79ffc8:	f2 48 0f 2c c0       	cvttsd2si %xmm0,%rax
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:277
  79ffcd:	bb 0a 00 00 00       	mov    $0xa,%ebx
  79ffd2:	e8 a9 da cd ff       	call   47da80 <strconv.FormatInt>
  79ffd7:	48 89 84 24 e8 00 00 	mov    %rax,0xe8(%rsp)
  79ffde:	00 
  79ffdf:	48 89 9c 24 a0 00 00 	mov    %rbx,0xa0(%rsp)
  79ffe6:	00 
  79ffe7:	48 8b 44 24 70       	mov    0x70(%rsp),%rax
  79ffec:	bb 0a 00 00 00       	mov    $0xa,%ebx
  79fff1:	e8 8a da cd ff       	call   47da80 <strconv.FormatInt>
  79fff6:	48 89 84 24 e0 00 00 	mov    %rax,0xe0(%rsp)
  79fffd:	00 
  79fffe:	48 89 9c 24 98 00 00 	mov    %rbx,0x98(%rsp)
  7a0005:	00 
  7a0006:	48 8d bc 24 10 01 00 	lea    0x110(%rsp),%rdi
  7a000d:	00 
  7a000e:	48 8d 7f e0          	lea    -0x20(%rdi),%rdi
  7a0012:	66 0f 1f 84 00 00 00 	nopw   0x0(%rax,%rax,1)
  7a0019:	00 00 
  7a001b:	0f 1f 44 00 00       	nopl   0x0(%rax,%rax,1)
  7a0020:	48 89 6c 24 f0       	mov    %rbp,-0x10(%rsp)
  7a0025:	48 8d 6c 24 f0       	lea    -0x10(%rsp),%rbp
  7a002a:	e8 3c 77 cc ff       	call   46776b <runtime.duffzero+0x14b>
  7a002f:	48 8b 6d 00          	mov    0x0(%rbp),%rbp
  7a0033:	48 8d 0d 4c 74 0d 00 	lea    0xd744c(%rip),%rcx        # 877486 <go:string.*+0x9ebe>
  7a003a:	48 89 8c 24 10 01 00 	mov    %rcx,0x110(%rsp)
  7a0041:	00 
  7a0042:	48 c7 84 24 18 01 00 	movq   $0x18,0x118(%rsp)
  7a0049:	00 18 00 00 00 
  7a004e:	48 8b 8c 24 e8 00 00 	mov    0xe8(%rsp),%rcx
  7a0055:	00 
  7a0056:	48 89 8c 24 20 01 00 	mov    %rcx,0x120(%rsp)
  7a005d:	00 
  7a005e:	48 8b 8c 24 a0 00 00 	mov    0xa0(%rsp),%rcx
  7a0065:	00 
  7a0066:	48 89 8c 24 28 01 00 	mov    %rcx,0x128(%rsp)
  7a006d:	00 
  7a006e:	48 8d 0d 59 74 0d 00 	lea    0xd7459(%rip),%rcx        # 8774ce <go:string.*+0x9f06>
  7a0075:	48 89 8c 24 30 01 00 	mov    %rcx,0x130(%rsp)
  7a007c:	00 
  7a007d:	48 c7 84 24 38 01 00 	movq   $0x18,0x138(%rsp)
  7a0084:	00 18 00 00 00 
  7a0089:	48 8b 0d c8 36 40 00 	mov    0x4036c8(%rip),%rcx        # ba3758 <agentGoProject/common.fileSaveDirInfosMap>
  7a0090:	48 8b bc 24 88 01 00 	mov    0x188(%rsp),%rdi
  7a0097:	00 
  7a0098:	48 8d 05 61 ad 06 00 	lea    0x6ad61(%rip),%rax        # 80ae00 <type:*+0x48e00>
  7a009f:	48 89 cb             	mov    %rcx,%rbx
  7a00a2:	48 8b 8c 24 80 01 00 	mov    0x180(%rsp),%rcx
  7a00a9:	00 
  7a00aa:	e8 31 2c c7 ff       	call   412ce0 <runtime.mapaccess1_faststr>
  7a00af:	48 8b 08             	mov    (%rax),%rcx
  7a00b2:	48 8b 50 08          	mov    0x8(%rax),%rdx
  7a00b6:	48 89 8c 24 40 01 00 	mov    %rcx,0x140(%rsp)
  7a00bd:	00 
  7a00be:	48 89 94 24 48 01 00 	mov    %rdx,0x148(%rsp)
  7a00c5:	00 
  7a00c6:	48 8d 0d 19 74 0d 00 	lea    0xd7419(%rip),%rcx        # 8774e6 <go:string.*+0x9f1e>
  7a00cd:	48 89 8c 24 50 01 00 	mov    %rcx,0x150(%rsp)
  7a00d4:	00 
  7a00d5:	48 c7 84 24 58 01 00 	movq   $0x18,0x158(%rsp)
  7a00dc:	00 18 00 00 00 
  7a00e1:	48 8b 8c 24 e0 00 00 	mov    0xe0(%rsp),%rcx
  7a00e8:	00 
  7a00e9:	48 89 8c 24 60 01 00 	mov    %rcx,0x160(%rsp)
  7a00f0:	00 
  7a00f1:	48 8b 8c 24 98 00 00 	mov    0x98(%rsp),%rcx
  7a00f8:	00 
  7a00f9:	48 89 8c 24 68 01 00 	mov    %rcx,0x168(%rsp)
  7a0100:	00 
  7a0101:	31 c0                	xor    %eax,%eax
  7a0103:	48 8d 9c 24 10 01 00 	lea    0x110(%rsp),%rbx
  7a010a:	00 
  7a010b:	b9 06 00 00 00       	mov    $0x6,%ecx
  7a0110:	48 89 cf             	mov    %rcx,%rdi
  7a0113:	e8 e8 ff ca ff       	call   450100 <runtime.concatstrings>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:306
  7a0118:	48 89 9c 24 80 00 00 	mov    %rbx,0x80(%rsp)
  7a011f:	00 
  7a0120:	48 89 84 24 c0 00 00 	mov    %rax,0xc0(%rsp)
  7a0127:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:288
  7a0128:	48 8b 94 24 b8 00 00 	mov    0xb8(%rsp),%rdx
  7a012f:	00 
  7a0130:	31 c9                	xor    %ecx,%ecx
  7a0132:	31 f6                	xor    %esi,%esi
  7a0134:	31 ff                	xor    %edi,%edi
  7a0136:	45 31 c0             	xor    %r8d,%r8d
  7a0139:	e9 a4 01 00 00       	jmp    7a02e2 <agentGoProject/common.checkFileDirSafe+0x582>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:271
  7a013e:	48 89 9c 24 88 00 00 	mov    %rbx,0x88(%rsp)
  7a0145:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:272
  7a0146:	44 0f 11 bc 24 f0 00 	movups %xmm15,0xf0(%rsp)
  7a014d:	00 00 
  7a014f:	44 0f 11 bc 24 00 01 	movups %xmm15,0x100(%rsp)
  7a0156:	00 00 
  7a0158:	48 8d 15 41 1b 05 00 	lea    0x51b41(%rip),%rdx        # 7f1ca0 <type:*+0x2fca0>
  7a015f:	48 89 94 24 f0 00 00 	mov    %rdx,0xf0(%rsp)
  7a0166:	00 
  7a0167:	48 8d 15 72 f1 16 00 	lea    0x16f172(%rip),%rdx        # 90f2e0 <runtime.buildVersion.str+0x40>
  7a016e:	48 89 94 24 f8 00 00 	mov    %rdx,0xf8(%rsp)
  7a0175:	00 
  7a0176:	74 06                	je     7a017e <agentGoProject/common.checkFileDirSafe+0x41e>
  7a0178:	48 8b 53 08          	mov    0x8(%rbx),%rdx
  7a017c:	eb 03                	jmp    7a0181 <agentGoProject/common.checkFileDirSafe+0x421>
  7a017e:	48 89 da             	mov    %rbx,%rdx
  7a0181:	48 89 8c 24 c8 00 00 	mov    %rcx,0xc8(%rsp)
  7a0188:	00 
  7a0189:	48 89 94 24 00 01 00 	mov    %rdx,0x100(%rsp)
  7a0190:	00 
  7a0191:	48 89 8c 24 08 01 00 	mov    %rcx,0x108(%rsp)
  7a0198:	00 
  7a0199:	48 8d 84 24 f0 00 00 	lea    0xf0(%rsp),%rax
  7a01a0:	00 
  7a01a1:	bb 02 00 00 00       	mov    $0x2,%ebx
  7a01a6:	48 89 d9             	mov    %rbx,%rcx
  7a01a9:	e8 72 cd de ff       	call   58cf20 <log.Println>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:273
  7a01ae:	48 8b 94 24 88 00 00 	mov    0x88(%rsp),%rdx
  7a01b5:	00 
  7a01b6:	48 8b 52 18          	mov    0x18(%rdx),%rdx
  7a01ba:	48 8b 84 24 c8 00 00 	mov    0xc8(%rsp),%rax
  7a01c1:	00 
  7a01c2:	ff d2                	call   *%rdx
  7a01c4:	b9 12 00 00 00       	mov    $0x12,%ecx
  7a01c9:	48 89 c7             	mov    %rax,%rdi
  7a01cc:	48 89 de             	mov    %rbx,%rsi
  7a01cf:	31 c0                	xor    %eax,%eax
  7a01d1:	48 8d 1d 7a 3b 0d 00 	lea    0xd3b7a(%rip),%rbx        # 873d52 <go:string.*+0x678a>
  7a01d8:	e8 63 01 cb ff       	call   450340 <runtime.concatstring2>
  7a01dd:	48 8b ac 24 70 01 00 	mov    0x170(%rsp),%rbp
  7a01e4:	00 
  7a01e5:	48 81 c4 78 01 00 00 	add    $0x178,%rsp
  7a01ec:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:260
  7a01ed:	48 8b 84 24 b8 00 00 	mov    0xb8(%rsp),%rax
  7a01f4:	00 
  7a01f5:	48 8b 5c 24 70       	mov    0x70(%rsp),%rbx
  7a01fa:	48 8b 4c 24 78       	mov    0x78(%rsp),%rcx
  7a01ff:	48 8b bc 24 80 01 00 	mov    0x180(%rsp),%rdi
  7a0206:	00 
  7a0207:	48 8b b4 24 88 01 00 	mov    0x188(%rsp),%rsi
  7a020e:	00 
  7a020f:	e8 0c 20 00 00       	call   7a2220 <agentGoProject/common.putAllMd5ForDir>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:262
  7a0214:	31 c0                	xor    %eax,%eax
  7a0216:	31 db                	xor    %ebx,%ebx
  7a0218:	48 8b ac 24 70 01 00 	mov    0x170(%rsp),%rbp
  7a021f:	00 
  7a0220:	48 81 c4 78 01 00 00 	add    $0x178,%rsp
  7a0227:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:247
  7a0228:	44 0f 11 bc 24 f0 00 	movups %xmm15,0xf0(%rsp)
  7a022f:	00 00 
  7a0231:	44 0f 11 bc 24 00 01 	movups %xmm15,0x100(%rsp)
  7a0238:	00 00 
  7a023a:	48 8d 0d 5f 1a 05 00 	lea    0x51a5f(%rip),%rcx        # 7f1ca0 <type:*+0x2fca0>
  7a0241:	48 89 8c 24 f0 00 00 	mov    %rcx,0xf0(%rsp)
  7a0248:	00 
  7a0249:	48 8d 15 50 fd 16 00 	lea    0x16fd50(%rip),%rdx        # 90ffa0 <runtime.buildVersion.str+0xd00>
  7a0250:	48 89 94 24 f8 00 00 	mov    %rdx,0xf8(%rsp)
  7a0257:	00 
  7a0258:	48 8b 84 24 80 01 00 	mov    0x180(%rsp),%rax
  7a025f:	00 
  7a0260:	48 8b 9c 24 88 01 00 	mov    0x188(%rsp),%rbx
  7a0267:	00 
  7a0268:	e8 93 b2 c6 ff       	call   40b500 <runtime.convTstring>
  7a026d:	48 8d 0d 2c 1a 05 00 	lea    0x51a2c(%rip),%rcx        # 7f1ca0 <type:*+0x2fca0>
  7a0274:	48 89 8c 24 00 01 00 	mov    %rcx,0x100(%rsp)
  7a027b:	00 
  7a027c:	48 89 84 24 08 01 00 	mov    %rax,0x108(%rsp)
  7a0283:	00 
  7a0284:	48 8d 84 24 f0 00 00 	lea    0xf0(%rsp),%rax
  7a028b:	00 
  7a028c:	bb 02 00 00 00       	mov    $0x2,%ebx
  7a0291:	48 89 d9             	mov    %rbx,%rcx
  7a0294:	e8 87 cc de ff       	call   58cf20 <log.Println>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:248
  7a0299:	48 8d 05 8e 3a 0d 00 	lea    0xd3a8e(%rip),%rax        # 873d2e <go:string.*+0x6766>
  7a02a0:	bb 12 00 00 00       	mov    $0x12,%ebx
  7a02a5:	48 8b ac 24 70 01 00 	mov    0x170(%rsp),%rbp
  7a02ac:	00 
  7a02ad:	48 81 c4 78 01 00 00 	add    $0x178,%rsp
  7a02b4:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:288
  7a02b5:	48 8b 94 24 d8 00 00 	mov    0xd8(%rsp),%rdx
  7a02bc:	00 
  7a02bd:	48 83 c2 10          	add    $0x10,%rdx
  7a02c1:	48 8b 8c 24 a0 00 00 	mov    0xa0(%rsp),%rcx
  7a02c8:	00 
  7a02c9:	48 ff c1             	inc    %rcx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:309
  7a02cc:	48 89 df             	mov    %rbx,%rdi
  7a02cf:	49 89 c0             	mov    %rax,%r8
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:312
  7a02d2:	48 8b 84 24 c0 00 00 	mov    0xc0(%rsp),%rax
  7a02d9:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:306
  7a02da:	48 8b 9c 24 80 00 00 	mov    0x80(%rsp),%rbx
  7a02e1:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:288
  7a02e2:	4c 8b 4c 24 70       	mov    0x70(%rsp),%r9
  7a02e7:	49 39 c9             	cmp    %rcx,%r9
  7a02ea:	0f 8e b0 00 00 00    	jle    7a03a0 <agentGoProject/common.checkFileDirSafe+0x640>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:309
  7a02f0:	48 89 7c 24 60       	mov    %rdi,0x60(%rsp)
  7a02f5:	4c 89 84 24 b0 00 00 	mov    %r8,0xb0(%rsp)
  7a02fc:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:288
  7a02fd:	48 89 8c 24 a0 00 00 	mov    %rcx,0xa0(%rsp)
  7a0304:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:293
  7a0305:	48 89 74 24 68       	mov    %rsi,0x68(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:288
  7a030a:	48 89 94 24 d8 00 00 	mov    %rdx,0xd8(%rsp)
  7a0311:	00 
  7a0312:	48 8b 02             	mov    (%rdx),%rax
  7a0315:	48 89 84 24 a8 00 00 	mov    %rax,0xa8(%rsp)
  7a031c:	00 
  7a031d:	48 8b 5a 08          	mov    0x8(%rdx),%rbx
  7a0321:	48 89 5c 24 58       	mov    %rbx,0x58(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:290
  7a0326:	e8 55 14 00 00       	call   7a1780 <agentGoProject/common.hasChangedForDir>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:291
  7a032b:	48 85 db             	test   %rbx,%rbx
  7a032e:	75 14                	jne    7a0344 <agentGoProject/common.checkFileDirSafe+0x5e4>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:298
  7a0330:	48 8b 74 24 68       	mov    0x68(%rsp),%rsi
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:309
  7a0335:	48 8b 5c 24 60       	mov    0x60(%rsp),%rbx
  7a033a:	48 8b 84 24 b0 00 00 	mov    0xb0(%rsp),%rax
  7a0341:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:291
  7a0342:	eb 3c                	jmp    7a0380 <agentGoProject/common.checkFileDirSafe+0x620>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:295
  7a0344:	31 c0                	xor    %eax,%eax
  7a0346:	48 8b 9c 24 b0 00 00 	mov    0xb0(%rsp),%rbx
  7a034d:	00 
  7a034e:	48 8b 4c 24 60       	mov    0x60(%rsp),%rcx
  7a0353:	48 8b bc 24 a8 00 00 	mov    0xa8(%rsp),%rdi
  7a035a:	00 
  7a035b:	48 8b 74 24 58       	mov    0x58(%rsp),%rsi
  7a0360:	4c 8d 05 5a d6 0c 00 	lea    0xcd65a(%rip),%r8        # 86d9c1 <go:string.*+0x3f9>
  7a0367:	41 b9 03 00 00 00    	mov    $0x3,%r9d
  7a036d:	e8 6e 00 cb ff       	call   4503e0 <runtime.concatstring3>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:293
  7a0372:	48 8b 74 24 68       	mov    0x68(%rsp),%rsi
  7a0377:	48 ff c6             	inc    %rsi
  7a037a:	66 0f 1f 44 00 00    	nopw   0x0(%rax,%rax,1)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:298
  7a0380:	48 83 fe 64          	cmp    $0x64,%rsi
  7a0384:	0f 8e 2b ff ff ff    	jle    7a02b5 <agentGoProject/common.checkFileDirSafe+0x555>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:309
  7a038a:	48 89 df             	mov    %rbx,%rdi
  7a038d:	49 89 c0             	mov    %rax,%r8
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:312
  7a0390:	48 8b 84 24 c0 00 00 	mov    0xc0(%rsp),%rax
  7a0397:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:306
  7a0398:	48 8b 9c 24 80 00 00 	mov    0x80(%rsp),%rbx
  7a039f:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:305
  7a03a0:	48 85 f6             	test   %rsi,%rsi
  7a03a3:	0f 8e 9e 00 00 00    	jle    7a0447 <agentGoProject/common.checkFileDirSafe+0x6e7>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:309
  7a03a9:	48 89 7c 24 60       	mov    %rdi,0x60(%rsp)
  7a03ae:	4c 89 84 24 b0 00 00 	mov    %r8,0xb0(%rsp)
  7a03b5:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:306
  7a03b6:	48 85 db             	test   %rbx,%rbx
  7a03b9:	74 23                	je     7a03de <agentGoProject/common.checkFileDirSafe+0x67e>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:305
  7a03bb:	48 89 74 24 68       	mov    %rsi,0x68(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:307
  7a03c0:	48 89 d9             	mov    %rbx,%rcx
  7a03c3:	48 8d 3d f7 d5 0c 00 	lea    0xcd5f7(%rip),%rdi        # 86d9c1 <go:string.*+0x3f9>
  7a03ca:	be 03 00 00 00       	mov    $0x3,%esi
  7a03cf:	48 89 c3             	mov    %rax,%rbx
  7a03d2:	31 c0                	xor    %eax,%eax
  7a03d4:	e8 67 ff ca ff       	call   450340 <runtime.concatstring2>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:309
  7a03d9:	48 8b 74 24 68       	mov    0x68(%rsp),%rsi
  7a03de:	48 89 9c 24 80 00 00 	mov    %rbx,0x80(%rsp)
  7a03e5:	00 
  7a03e6:	48 89 84 24 c0 00 00 	mov    %rax,0xc0(%rsp)
  7a03ed:	00 
  7a03ee:	48 89 f0             	mov    %rsi,%rax
  7a03f1:	bb 0a 00 00 00       	mov    $0xa,%ebx
  7a03f6:	e8 85 d6 cd ff       	call   47da80 <strconv.FormatInt>
  7a03fb:	48 8b 8c 24 b0 00 00 	mov    0xb0(%rsp),%rcx
  7a0402:	00 
  7a0403:	48 89 0c 24          	mov    %rcx,(%rsp)
  7a0407:	48 8b 4c 24 60       	mov    0x60(%rsp),%rcx
  7a040c:	48 89 4c 24 08       	mov    %rcx,0x8(%rsp)
  7a0411:	48 8b 8c 24 80 00 00 	mov    0x80(%rsp),%rcx
  7a0418:	00 
  7a0419:	48 8d 3d f5 54 0d 00 	lea    0xd54f5(%rip),%rdi        # 875915 <go:string.*+0x834d>
  7a0420:	be 15 00 00 00       	mov    $0x15,%esi
  7a0425:	49 89 c0             	mov    %rax,%r8
  7a0428:	49 89 d9             	mov    %rbx,%r9
  7a042b:	4c 8d 15 92 d5 0c 00 	lea    0xcd592(%rip),%r10        # 86d9c4 <go:string.*+0x3fc>
  7a0432:	41 bb 03 00 00 00    	mov    $0x3,%r11d
  7a0438:	31 c0                	xor    %eax,%eax
  7a043a:	48 8b 9c 24 c0 00 00 	mov    0xc0(%rsp),%rbx
  7a0441:	00 
  7a0442:	e8 79 01 cb ff       	call   4505c0 <runtime.concatstring5>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:312
  7a0447:	48 81 fb f4 01 00 00 	cmp    $0x1f4,%rbx
  7a044e:	7e 1b                	jle    7a046b <agentGoProject/common.checkFileDirSafe+0x70b>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:313
  7a0450:	48 89 c3             	mov    %rax,%rbx
  7a0453:	b9 f4 01 00 00       	mov    $0x1f4,%ecx
  7a0458:	48 8d 3d 3a de 0c 00 	lea    0xcde3a(%rip),%rdi        # 86e299 <go:string.*+0xcd1>
  7a045f:	be 06 00 00 00       	mov    $0x6,%esi
  7a0464:	31 c0                	xor    %eax,%eax
  7a0466:	e8 d5 fe ca ff       	call   450340 <runtime.concatstring2>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:315
  7a046b:	48 8b ac 24 70 01 00 	mov    0x170(%rsp),%rbp
  7a0472:	00 
  7a0473:	48 81 c4 78 01 00 00 	add    $0x178,%rsp
  7a047a:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:242
  7a047b:	48 89 44 24 08       	mov    %rax,0x8(%rsp)
  7a0480:	48 89 5c 24 10       	mov    %rbx,0x10(%rsp)
  7a0485:	e8 96 4b cc ff       	call   465020 <runtime.morestack_noctxt.abi0>
  7a048a:	48 8b 44 24 08       	mov    0x8(%rsp),%rax
  7a048f:	48 8b 5c 24 10       	mov    0x10(%rsp),%rbx
  7a0494:	e9 c7 f8 ff ff       	jmp    79fd60 <agentGoProject/common.checkFileDirSafe>
  7a0499:	cc                   	int3   
  7a049a:	cc                   	int3   
  7a049b:	cc                   	int3   
  7a049c:	cc                   	int3   
  7a049d:	cc                   	int3   
  7a049e:	cc                   	int3   
  7a049f:	cc                   	int3   

00000000007a04a0 <agentGoProject/common.refreshDirPathMd5>:
agentGoProject/common.refreshDirPathMd5():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:323
  7a04a0:	4c 8d a4 24 c0 fe ff 	lea    -0x140(%rsp),%r12
  7a04a7:	ff 
  7a04a8:	4d 3b 66 10          	cmp    0x10(%r14),%r12
  7a04ac:	0f 86 e4 03 00 00    	jbe    7a0896 <agentGoProject/common.refreshDirPathMd5+0x3f6>
  7a04b2:	48 81 ec c0 01 00 00 	sub    $0x1c0,%rsp
  7a04b9:	48 89 ac 24 b8 01 00 	mov    %rbp,0x1b8(%rsp)
  7a04c0:	00 
  7a04c1:	48 8d ac 24 b8 01 00 	lea    0x1b8(%rsp),%rbp
  7a04c8:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:324
  7a04c9:	48 8b 0d 68 32 40 00 	mov    0x403268(%rip),%rcx        # ba3738 <agentGoProject/common.FileSafeList>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:323
  7a04d0:	84 c0                	test   %al,%al
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:326
  7a04d2:	0f 85 c7 00 00 00    	jne    7a059f <agentGoProject/common.refreshDirPathMd5+0xff>
./C:/Program Files/Go/src/container/list/list.go:66
  7a04d8:	48 83 79 28 01       	cmpq   $0x1,0x28(%rcx)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:335
  7a04dd:	7c 08                	jl     7a04e7 <agentGoProject/common.refreshDirPathMd5+0x47>
./C:/Program Files/Go/src/container/list/list.go:73
  7a04df:	48 8b 09             	mov    (%rcx),%rcx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:342
  7a04e2:	e9 b9 01 00 00       	jmp    7a06a0 <agentGoProject/common.refreshDirPathMd5+0x200>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:336
  7a04e7:	90                   	nop
./C:/Program Files/Go/src/sync/mutex.go:83
  7a04e8:	31 c0                	xor    %eax,%eax
sync.(*Mutex).Lock():
./C:/Program Files/Go/src/sync/mutex.go:83
  7a04ea:	48 8d 0d 77 4b 43 00 	lea    0x434b77(%rip),%rcx        # bd5068 <agentGoProject/common.fileSaveDirInfosGuard>
  7a04f1:	ba 01 00 00 00       	mov    $0x1,%edx
  7a04f6:	f0 0f b1 11          	lock cmpxchg %edx,(%rcx)
  7a04fa:	0f 94 c1             	sete   %cl
  7a04fd:	0f 1f 00             	nopl   (%rax)
  7a0500:	84 c9                	test   %cl,%cl
  7a0502:	75 0c                	jne    7a0510 <agentGoProject/common.refreshDirPathMd5+0x70>
./C:/Program Files/Go/src/sync/mutex.go:90
  7a0504:	48 8d 05 5d 4b 43 00 	lea    0x434b5d(%rip),%rax        # bd5068 <agentGoProject/common.fileSaveDirInfosGuard>
  7a050b:	e8 30 02 cd ff       	call   470740 <sync.(*Mutex).lockSlow>
agentGoProject/common.refreshDirPathMd5():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:337
  7a0510:	e8 ab e2 c6 ff       	call   40e7c0 <runtime.makemap_small>
  7a0515:	83 3d 34 50 43 00 00 	cmpl   $0x0,0x435034(%rip)        # bd5550 <runtime.writeBarrier>
  7a051c:	75 09                	jne    7a0527 <agentGoProject/common.refreshDirPathMd5+0x87>
  7a051e:	48 89 05 33 32 40 00 	mov    %rax,0x403233(%rip)        # ba3758 <agentGoProject/common.fileSaveDirInfosMap>
  7a0525:	eb 0c                	jmp    7a0533 <agentGoProject/common.refreshDirPathMd5+0x93>
  7a0527:	48 8d 3d 2a 32 40 00 	lea    0x40322a(%rip),%rdi        # ba3758 <agentGoProject/common.fileSaveDirInfosMap>
  7a052e:	e8 ad 6a cc ff       	call   466fe0 <runtime.gcWriteBarrier>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:338
  7a0533:	44 0f 11 bc 24 a8 00 	movups %xmm15,0xa8(%rsp)
  7a053a:	00 00 
  7a053c:	48 8d 0d dd 03 00 00 	lea    0x3dd(%rip),%rcx        # 7a0920 <agentGoProject/common.refreshDirPathMd5.func2>
  7a0543:	48 89 8c 24 a8 00 00 	mov    %rcx,0xa8(%rsp)
  7a054a:	00 
  7a054b:	48 8d 0d 16 4b 43 00 	lea    0x434b16(%rip),%rcx        # bd5068 <agentGoProject/common.fileSaveDirInfosGuard>
  7a0552:	48 89 8c 24 b0 00 00 	mov    %rcx,0xb0(%rsp)
  7a0559:	00 
  7a055a:	48 8d 8c 24 a8 00 00 	lea    0xa8(%rsp),%rcx
  7a0561:	00 
  7a0562:	48 89 4c 24 30       	mov    %rcx,0x30(%rsp)
  7a0567:	48 8d 44 24 18       	lea    0x18(%rsp),%rax
  7a056c:	e8 8f 31 c9 ff       	call   433700 <runtime.deferprocStack>
  7a0571:	85 c0                	test   %eax,%eax
  7a0573:	75 15                	jne    7a058a <agentGoProject/common.refreshDirPathMd5+0xea>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:339
  7a0575:	e8 a6 37 c9 ff       	call   433d20 <runtime.deferreturn>
  7a057a:	48 8b ac 24 b8 01 00 	mov    0x1b8(%rsp),%rbp
  7a0581:	00 
  7a0582:	48 81 c4 c0 01 00 00 	add    $0x1c0,%rsp
  7a0589:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:338
  7a058a:	e8 91 37 c9 ff       	call   433d20 <runtime.deferreturn>
  7a058f:	48 8b ac 24 b8 01 00 	mov    0x1b8(%rsp),%rbp
  7a0596:	00 
  7a0597:	48 81 c4 c0 01 00 00 	add    $0x1c0,%rsp
  7a059e:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:328
  7a059f:	90                   	nop
./C:/Program Files/Go/src/sync/mutex.go:83
  7a05a0:	31 c0                	xor    %eax,%eax
sync.(*Mutex).Lock():
./C:/Program Files/Go/src/sync/mutex.go:83
  7a05a2:	48 8d 0d bf 4a 43 00 	lea    0x434abf(%rip),%rcx        # bd5068 <agentGoProject/common.fileSaveDirInfosGuard>
  7a05a9:	ba 01 00 00 00       	mov    $0x1,%edx
  7a05ae:	f0 0f b1 11          	lock cmpxchg %edx,(%rcx)
  7a05b2:	0f 94 c1             	sete   %cl
  7a05b5:	84 c9                	test   %cl,%cl
  7a05b7:	75 0c                	jne    7a05c5 <agentGoProject/common.refreshDirPathMd5+0x125>
./C:/Program Files/Go/src/sync/mutex.go:90
  7a05b9:	48 8d 05 a8 4a 43 00 	lea    0x434aa8(%rip),%rax        # bd5068 <agentGoProject/common.fileSaveDirInfosGuard>
  7a05c0:	e8 7b 01 cd ff       	call   470740 <sync.(*Mutex).lockSlow>
agentGoProject/common.refreshDirPathMd5():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:329
  7a05c5:	e8 f6 e1 c6 ff       	call   40e7c0 <runtime.makemap_small>
  7a05ca:	83 3d 7f 4f 43 00 00 	cmpl   $0x0,0x434f7f(%rip)        # bd5550 <runtime.writeBarrier>
  7a05d1:	75 09                	jne    7a05dc <agentGoProject/common.refreshDirPathMd5+0x13c>
  7a05d3:	48 89 05 7e 31 40 00 	mov    %rax,0x40317e(%rip)        # ba3758 <agentGoProject/common.fileSaveDirInfosMap>
  7a05da:	eb 0c                	jmp    7a05e8 <agentGoProject/common.refreshDirPathMd5+0x148>
  7a05dc:	48 8d 3d 75 31 40 00 	lea    0x403175(%rip),%rdi        # ba3758 <agentGoProject/common.fileSaveDirInfosMap>
  7a05e3:	e8 f8 69 cc ff       	call   466fe0 <runtime.gcWriteBarrier>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:330
  7a05e8:	44 0f 11 bc 24 b8 00 	movups %xmm15,0xb8(%rsp)
  7a05ef:	00 00 
  7a05f1:	48 8d 0d 88 03 00 00 	lea    0x388(%rip),%rcx        # 7a0980 <agentGoProject/common.refreshDirPathMd5.func1>
  7a05f8:	48 89 8c 24 b8 00 00 	mov    %rcx,0xb8(%rsp)
  7a05ff:	00 
  7a0600:	48 8d 0d 61 4a 43 00 	lea    0x434a61(%rip),%rcx        # bd5068 <agentGoProject/common.fileSaveDirInfosGuard>
  7a0607:	48 89 8c 24 c0 00 00 	mov    %rcx,0xc0(%rsp)
  7a060e:	00 
  7a060f:	48 8d 8c 24 b8 00 00 	lea    0xb8(%rsp),%rcx
  7a0616:	00 
  7a0617:	48 89 4c 24 78       	mov    %rcx,0x78(%rsp)
  7a061c:	48 8d 44 24 60       	lea    0x60(%rsp),%rax
  7a0621:	e8 da 30 c9 ff       	call   433700 <runtime.deferprocStack>
  7a0626:	85 c0                	test   %eax,%eax
  7a0628:	75 56                	jne    7a0680 <agentGoProject/common.refreshDirPathMd5+0x1e0>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:331
  7a062a:	44 0f 11 bc 24 c8 00 	movups %xmm15,0xc8(%rsp)
  7a0631:	00 00 
  7a0633:	48 8d 15 66 16 05 00 	lea    0x51666(%rip),%rdx        # 7f1ca0 <type:*+0x2fca0>
  7a063a:	48 89 94 24 c8 00 00 	mov    %rdx,0xc8(%rsp)
  7a0641:	00 
  7a0642:	48 8d 15 a7 ec 16 00 	lea    0x16eca7(%rip),%rdx        # 90f2f0 <runtime.buildVersion.str+0x50>
  7a0649:	48 89 94 24 d0 00 00 	mov    %rdx,0xd0(%rsp)
  7a0650:	00 
  7a0651:	48 8d 84 24 c8 00 00 	lea    0xc8(%rsp),%rax
  7a0658:	00 
  7a0659:	bb 01 00 00 00       	mov    $0x1,%ebx
  7a065e:	48 89 d9             	mov    %rbx,%rcx
  7a0661:	e8 ba c8 de ff       	call   58cf20 <log.Println>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:332
  7a0666:	e8 b5 36 c9 ff       	call   433d20 <runtime.deferreturn>
  7a066b:	48 8b ac 24 b8 01 00 	mov    0x1b8(%rsp),%rbp
  7a0672:	00 
  7a0673:	48 81 c4 c0 01 00 00 	add    $0x1c0,%rsp
  7a067a:	c3                   	ret    
  7a067b:	0f 1f 44 00 00       	nopl   0x0(%rax,%rax,1)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:330
  7a0680:	e8 9b 36 c9 ff       	call   433d20 <runtime.deferreturn>
  7a0685:	48 8b ac 24 b8 01 00 	mov    0x1b8(%rsp),%rbp
  7a068c:	00 
  7a068d:	48 81 c4 c0 01 00 00 	add    $0x1c0,%rsp
  7a0694:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:342
  7a0695:	48 89 d1             	mov    %rdx,%rcx
  7a0698:	0f 1f 84 00 00 00 00 	nopl   0x0(%rax,%rax,1)
  7a069f:	00 
  7a06a0:	48 85 c9             	test   %rcx,%rcx
  7a06a3:	0f 84 d8 01 00 00    	je     7a0881 <agentGoProject/common.refreshDirPathMd5+0x3e1>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:343
  7a06a9:	48 8b 71 20          	mov    0x20(%rcx),%rsi
  7a06ad:	48 8b 51 18          	mov    0x18(%rcx),%rdx
  7a06b1:	48 8d 1d 48 c1 0a 00 	lea    0xac148(%rip),%rbx        # 84c800 <type:*+0x8a800>
  7a06b8:	48 39 da             	cmp    %rbx,%rdx
  7a06bb:	75 1d                	jne    7a06da <agentGoProject/common.refreshDirPathMd5+0x23a>
  7a06bd:	48 8d bc 24 58 01 00 	lea    0x158(%rsp),%rdi
  7a06c4:	00 
  7a06c5:	48 89 6c 24 f0       	mov    %rbp,-0x10(%rsp)
  7a06ca:	48 8d 6c 24 f0       	lea    -0x10(%rsp),%rbp
  7a06cf:	e8 f8 73 cc ff       	call   467acc <runtime.duffcopy+0x32c>
  7a06d4:	48 8b 6d 00          	mov    0x0(%rbp),%rbp
  7a06d8:	eb 1f                	jmp    7a06f9 <agentGoProject/common.refreshDirPathMd5+0x259>
  7a06da:	48 8d bc 24 58 01 00 	lea    0x158(%rsp),%rdi
  7a06e1:	00 
  7a06e2:	48 8d 7f e0          	lea    -0x20(%rdi),%rdi
  7a06e6:	48 89 6c 24 f0       	mov    %rbp,-0x10(%rsp)
  7a06eb:	48 8d 6c 24 f0       	lea    -0x10(%rsp),%rbp
  7a06f0:	e8 76 70 cc ff       	call   46776b <runtime.duffzero+0x14b>
  7a06f5:	48 8b 6d 00          	mov    0x0(%rbp),%rbp
  7a06f9:	48 8d bc 24 f8 00 00 	lea    0xf8(%rsp),%rdi
  7a0700:	00 
  7a0701:	48 8d b4 24 58 01 00 	lea    0x158(%rsp),%rsi
  7a0708:	00 
  7a0709:	48 89 6c 24 f0       	mov    %rbp,-0x10(%rsp)
  7a070e:	48 8d 6c 24 f0       	lea    -0x10(%rsp),%rbp
  7a0713:	e8 b4 73 cc ff       	call   467acc <runtime.duffcopy+0x32c>
  7a0718:	48 8b 6d 00          	mov    0x0(%rbp),%rbp
  7a071c:	0f 1f 40 00          	nopl   0x0(%rax)
  7a0720:	48 39 da             	cmp    %rbx,%rdx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:344
  7a0723:	75 43                	jne    7a0768 <agentGoProject/common.refreshDirPathMd5+0x2c8>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:346
  7a0725:	48 8b 94 24 28 01 00 	mov    0x128(%rsp),%rdx
  7a072c:	00 
  7a072d:	48 83 bc 24 30 01 00 	cmpq   $0x1,0x130(%rsp)
  7a0734:	00 01 
  7a0736:	75 30                	jne    7a0768 <agentGoProject/common.refreshDirPathMd5+0x2c8>
  7a0738:	80 3a 31             	cmpb   $0x31,(%rdx)
  7a073b:	75 2b                	jne    7a0768 <agentGoProject/common.refreshDirPathMd5+0x2c8>
  7a073d:	48 8b 94 24 18 01 00 	mov    0x118(%rsp),%rdx
  7a0744:	00 
  7a0745:	48 83 bc 24 20 01 00 	cmpq   $0x7,0x120(%rsp)
  7a074c:	00 07 
  7a074e:	75 18                	jne    7a0768 <agentGoProject/common.refreshDirPathMd5+0x2c8>
  7a0750:	81 3a 72 65 66 72    	cmpl   $0x72666572,(%rdx)
  7a0756:	75 10                	jne    7a0768 <agentGoProject/common.refreshDirPathMd5+0x2c8>
  7a0758:	66 81 7a 04 65 73    	cmpw   $0x7365,0x4(%rdx)
  7a075e:	66 90                	xchg   %ax,%ax
  7a0760:	75 06                	jne    7a0768 <agentGoProject/common.refreshDirPathMd5+0x2c8>
  7a0762:	80 7a 06 68          	cmpb   $0x68,0x6(%rdx)
  7a0766:	74 1d                	je     7a0785 <agentGoProject/common.refreshDirPathMd5+0x2e5>
./C:/Program Files/Go/src/container/list/list.go:32
  7a0768:	48 8b 11             	mov    (%rcx),%rdx
container/list.(*Element).Next():
./C:/Program Files/Go/src/container/list/list.go:32
  7a076b:	48 8b 49 10          	mov    0x10(%rcx),%rcx
  7a076f:	48 85 c9             	test   %rcx,%rcx
  7a0772:	74 09                	je     7a077d <agentGoProject/common.refreshDirPathMd5+0x2dd>
  7a0774:	48 39 d1             	cmp    %rdx,%rcx
  7a0777:	0f 85 18 ff ff ff    	jne    7a0695 <agentGoProject/common.refreshDirPathMd5+0x1f5>
  7a077d:	31 d2                	xor    %edx,%edx
  7a077f:	90                   	nop
agentGoProject/common.refreshDirPathMd5():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:342
  7a0780:	e9 10 ff ff ff       	jmp    7a0695 <agentGoProject/common.refreshDirPathMd5+0x1f5>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:348
  7a0785:	90                   	nop
./C:/Program Files/Go/src/sync/mutex.go:83
  7a0786:	31 c0                	xor    %eax,%eax
sync.(*Mutex).Lock():
./C:/Program Files/Go/src/sync/mutex.go:83
  7a0788:	48 8d 0d d9 48 43 00 	lea    0x4348d9(%rip),%rcx        # bd5068 <agentGoProject/common.fileSaveDirInfosGuard>
  7a078f:	ba 01 00 00 00       	mov    $0x1,%edx
  7a0794:	f0 0f b1 11          	lock cmpxchg %edx,(%rcx)
  7a0798:	0f 94 c1             	sete   %cl
  7a079b:	84 c9                	test   %cl,%cl
  7a079d:	75 0c                	jne    7a07ab <agentGoProject/common.refreshDirPathMd5+0x30b>
./C:/Program Files/Go/src/sync/mutex.go:90
  7a079f:	48 8d 05 c2 48 43 00 	lea    0x4348c2(%rip),%rax        # bd5068 <agentGoProject/common.fileSaveDirInfosGuard>
  7a07a6:	e8 95 ff cc ff       	call   470740 <sync.(*Mutex).lockSlow>
agentGoProject/common.refreshDirPathMd5():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:349
  7a07ab:	e8 10 e0 c6 ff       	call   40e7c0 <runtime.makemap_small>
  7a07b0:	83 3d 99 4d 43 00 00 	cmpl   $0x0,0x434d99(%rip)        # bd5550 <runtime.writeBarrier>
  7a07b7:	75 09                	jne    7a07c2 <agentGoProject/common.refreshDirPathMd5+0x322>
  7a07b9:	48 89 05 98 2f 40 00 	mov    %rax,0x402f98(%rip)        # ba3758 <agentGoProject/common.fileSaveDirInfosMap>
  7a07c0:	eb 0c                	jmp    7a07ce <agentGoProject/common.refreshDirPathMd5+0x32e>
  7a07c2:	48 8d 3d 8f 2f 40 00 	lea    0x402f8f(%rip),%rdi        # ba3758 <agentGoProject/common.fileSaveDirInfosMap>
  7a07c9:	e8 12 68 cc ff       	call   466fe0 <runtime.gcWriteBarrier>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:350
  7a07ce:	48 8d 05 eb cf 07 00 	lea    0x7cfeb(%rip),%rax        # 81d7c0 <type:*+0x5b7c0>
  7a07d5:	e8 a6 d4 c6 ff       	call   40dc80 <runtime.newobject>
  7a07da:	48 8d 0d df 00 00 00 	lea    0xdf(%rip),%rcx        # 7a08c0 <agentGoProject/common.refreshDirPathMd5.func3>
  7a07e1:	48 89 08             	mov    %rcx,(%rax)
  7a07e4:	48 8d 0d 7d 48 43 00 	lea    0x43487d(%rip),%rcx        # bd5068 <agentGoProject/common.fileSaveDirInfosGuard>
  7a07eb:	48 89 48 08          	mov    %rcx,0x8(%rax)
  7a07ef:	e8 0c 2e c9 ff       	call   433600 <runtime.deferproc>
  7a07f4:	85 c0                	test   %eax,%eax
  7a07f6:	75 73                	jne    7a086b <agentGoProject/common.refreshDirPathMd5+0x3cb>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:351
  7a07f8:	44 0f 11 bc 24 d8 00 	movups %xmm15,0xd8(%rsp)
  7a07ff:	00 00 
  7a0801:	44 0f 11 bc 24 e8 00 	movups %xmm15,0xe8(%rsp)
  7a0808:	00 00 
  7a080a:	48 8d 0d 8f 14 05 00 	lea    0x5148f(%rip),%rcx        # 7f1ca0 <type:*+0x2fca0>
  7a0811:	48 89 8c 24 d8 00 00 	mov    %rcx,0xd8(%rsp)
  7a0818:	00 
  7a0819:	48 8d 15 e0 ea 16 00 	lea    0x16eae0(%rip),%rdx        # 90f300 <runtime.buildVersion.str+0x60>
  7a0820:	48 89 94 24 e0 00 00 	mov    %rdx,0xe0(%rsp)
  7a0827:	00 
  7a0828:	48 8b 84 24 08 01 00 	mov    0x108(%rsp),%rax
  7a082f:	00 
  7a0830:	48 8b 9c 24 10 01 00 	mov    0x110(%rsp),%rbx
  7a0837:	00 
  7a0838:	e8 c3 ac c6 ff       	call   40b500 <runtime.convTstring>
  7a083d:	48 8d 0d 5c 14 05 00 	lea    0x5145c(%rip),%rcx        # 7f1ca0 <type:*+0x2fca0>
  7a0844:	48 89 8c 24 e8 00 00 	mov    %rcx,0xe8(%rsp)
  7a084b:	00 
  7a084c:	48 89 84 24 f0 00 00 	mov    %rax,0xf0(%rsp)
  7a0853:	00 
  7a0854:	48 8d 84 24 d8 00 00 	lea    0xd8(%rsp),%rax
  7a085b:	00 
  7a085c:	bb 02 00 00 00       	mov    $0x2,%ebx
  7a0861:	48 89 d9             	mov    %rbx,%rcx
  7a0864:	e8 b7 c6 de ff       	call   58cf20 <log.Println>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:352
  7a0869:	eb 16                	jmp    7a0881 <agentGoProject/common.refreshDirPathMd5+0x3e1>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:350
  7a086b:	e8 b0 34 c9 ff       	call   433d20 <runtime.deferreturn>
  7a0870:	48 8b ac 24 b8 01 00 	mov    0x1b8(%rsp),%rbp
  7a0877:	00 
  7a0878:	48 81 c4 c0 01 00 00 	add    $0x1c0,%rsp
  7a087f:	90                   	nop
  7a0880:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:356
  7a0881:	e8 9a 34 c9 ff       	call   433d20 <runtime.deferreturn>
  7a0886:	48 8b ac 24 b8 01 00 	mov    0x1b8(%rsp),%rbp
  7a088d:	00 
  7a088e:	48 81 c4 c0 01 00 00 	add    $0x1c0,%rsp
  7a0895:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:323
  7a0896:	88 44 24 08          	mov    %al,0x8(%rsp)
  7a089a:	e8 81 47 cc ff       	call   465020 <runtime.morestack_noctxt.abi0>
  7a089f:	0f b6 44 24 08       	movzbl 0x8(%rsp),%eax
  7a08a4:	e9 f7 fb ff ff       	jmp    7a04a0 <agentGoProject/common.refreshDirPathMd5>
  7a08a9:	cc                   	int3   
  7a08aa:	cc                   	int3   
  7a08ab:	cc                   	int3   
  7a08ac:	cc                   	int3   
  7a08ad:	cc                   	int3   
  7a08ae:	cc                   	int3   
  7a08af:	cc                   	int3   
  7a08b0:	cc                   	int3   
  7a08b1:	cc                   	int3   
  7a08b2:	cc                   	int3   
  7a08b3:	cc                   	int3   
  7a08b4:	cc                   	int3   
  7a08b5:	cc                   	int3   
  7a08b6:	cc                   	int3   
  7a08b7:	cc                   	int3   
  7a08b8:	cc                   	int3   
  7a08b9:	cc                   	int3   
  7a08ba:	cc                   	int3   
  7a08bb:	cc                   	int3   
  7a08bc:	cc                   	int3   
  7a08bd:	cc                   	int3   
  7a08be:	cc                   	int3   
  7a08bf:	cc                   	int3   

00000000007a08c0 <agentGoProject/common.refreshDirPathMd5.func3>:
agentGoProject/common.refreshDirPathMd5.func3():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:350
  7a08c0:	49 3b 66 10          	cmp    0x10(%r14),%rsp
  7a08c4:	76 2a                	jbe    7a08f0 <agentGoProject/common.refreshDirPathMd5.func3+0x30>
  7a08c6:	48 83 ec 10          	sub    $0x10,%rsp
  7a08ca:	48 89 6c 24 08       	mov    %rbp,0x8(%rsp)
  7a08cf:	48 8d 6c 24 08       	lea    0x8(%rsp),%rbp
  7a08d4:	4d 8b 66 20          	mov    0x20(%r14),%r12
  7a08d8:	4d 85 e4             	test   %r12,%r12
  7a08db:	75 1a                	jne    7a08f7 <agentGoProject/common.refreshDirPathMd5.func3+0x37>
  7a08dd:	48 8b 42 08          	mov    0x8(%rdx),%rax
  7a08e1:	e8 fa 00 cd ff       	call   4709e0 <sync.(*Mutex).Unlock>
  7a08e6:	48 8b 6c 24 08       	mov    0x8(%rsp),%rbp
  7a08eb:	48 83 c4 10          	add    $0x10,%rsp
  7a08ef:	c3                   	ret    
  7a08f0:	e8 8b 46 cc ff       	call   464f80 <runtime.morestack.abi0>
  7a08f5:	eb c9                	jmp    7a08c0 <agentGoProject/common.refreshDirPathMd5.func3>
  7a08f7:	4c 8d 6c 24 18       	lea    0x18(%rsp),%r13
  7a08fc:	0f 1f 40 00          	nopl   0x0(%rax)
  7a0900:	4d 39 2c 24          	cmp    %r13,(%r12)
  7a0904:	75 d7                	jne    7a08dd <agentGoProject/common.refreshDirPathMd5.func3+0x1d>
  7a0906:	49 89 24 24          	mov    %rsp,(%r12)
  7a090a:	eb d1                	jmp    7a08dd <agentGoProject/common.refreshDirPathMd5.func3+0x1d>
  7a090c:	cc                   	int3   
  7a090d:	cc                   	int3   
  7a090e:	cc                   	int3   
  7a090f:	cc                   	int3   
  7a0910:	cc                   	int3   
  7a0911:	cc                   	int3   
  7a0912:	cc                   	int3   
  7a0913:	cc                   	int3   
  7a0914:	cc                   	int3   
  7a0915:	cc                   	int3   
  7a0916:	cc                   	int3   
  7a0917:	cc                   	int3   
  7a0918:	cc                   	int3   
  7a0919:	cc                   	int3   
  7a091a:	cc                   	int3   
  7a091b:	cc                   	int3   
  7a091c:	cc                   	int3   
  7a091d:	cc                   	int3   
  7a091e:	cc                   	int3   
  7a091f:	cc                   	int3   

00000000007a0920 <agentGoProject/common.refreshDirPathMd5.func2>:
agentGoProject/common.refreshDirPathMd5.func2():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:338
  7a0920:	49 3b 66 10          	cmp    0x10(%r14),%rsp
  7a0924:	76 2a                	jbe    7a0950 <agentGoProject/common.refreshDirPathMd5.func2+0x30>
  7a0926:	48 83 ec 10          	sub    $0x10,%rsp
  7a092a:	48 89 6c 24 08       	mov    %rbp,0x8(%rsp)
  7a092f:	48 8d 6c 24 08       	lea    0x8(%rsp),%rbp
  7a0934:	4d 8b 66 20          	mov    0x20(%r14),%r12
  7a0938:	4d 85 e4             	test   %r12,%r12
  7a093b:	75 1a                	jne    7a0957 <agentGoProject/common.refreshDirPathMd5.func2+0x37>
  7a093d:	48 8b 42 08          	mov    0x8(%rdx),%rax
  7a0941:	e8 9a 00 cd ff       	call   4709e0 <sync.(*Mutex).Unlock>
  7a0946:	48 8b 6c 24 08       	mov    0x8(%rsp),%rbp
  7a094b:	48 83 c4 10          	add    $0x10,%rsp
  7a094f:	c3                   	ret    
  7a0950:	e8 2b 46 cc ff       	call   464f80 <runtime.morestack.abi0>
  7a0955:	eb c9                	jmp    7a0920 <agentGoProject/common.refreshDirPathMd5.func2>
  7a0957:	4c 8d 6c 24 18       	lea    0x18(%rsp),%r13
  7a095c:	0f 1f 40 00          	nopl   0x0(%rax)
  7a0960:	4d 39 2c 24          	cmp    %r13,(%r12)
  7a0964:	75 d7                	jne    7a093d <agentGoProject/common.refreshDirPathMd5.func2+0x1d>
  7a0966:	49 89 24 24          	mov    %rsp,(%r12)
  7a096a:	eb d1                	jmp    7a093d <agentGoProject/common.refreshDirPathMd5.func2+0x1d>
  7a096c:	cc                   	int3   
  7a096d:	cc                   	int3   
  7a096e:	cc                   	int3   
  7a096f:	cc                   	int3   
  7a0970:	cc                   	int3   
  7a0971:	cc                   	int3   
  7a0972:	cc                   	int3   
  7a0973:	cc                   	int3   
  7a0974:	cc                   	int3   
  7a0975:	cc                   	int3   
  7a0976:	cc                   	int3   
  7a0977:	cc                   	int3   
  7a0978:	cc                   	int3   
  7a0979:	cc                   	int3   
  7a097a:	cc                   	int3   
  7a097b:	cc                   	int3   
  7a097c:	cc                   	int3   
  7a097d:	cc                   	int3   
  7a097e:	cc                   	int3   
  7a097f:	cc                   	int3   

00000000007a0980 <agentGoProject/common.refreshDirPathMd5.func1>:
agentGoProject/common.refreshDirPathMd5.func1():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:330
  7a0980:	49 3b 66 10          	cmp    0x10(%r14),%rsp
  7a0984:	76 2a                	jbe    7a09b0 <agentGoProject/common.refreshDirPathMd5.func1+0x30>
  7a0986:	48 83 ec 10          	sub    $0x10,%rsp
  7a098a:	48 89 6c 24 08       	mov    %rbp,0x8(%rsp)
  7a098f:	48 8d 6c 24 08       	lea    0x8(%rsp),%rbp
  7a0994:	4d 8b 66 20          	mov    0x20(%r14),%r12
  7a0998:	4d 85 e4             	test   %r12,%r12
  7a099b:	75 1a                	jne    7a09b7 <agentGoProject/common.refreshDirPathMd5.func1+0x37>
  7a099d:	48 8b 42 08          	mov    0x8(%rdx),%rax
  7a09a1:	e8 3a 00 cd ff       	call   4709e0 <sync.(*Mutex).Unlock>
  7a09a6:	48 8b 6c 24 08       	mov    0x8(%rsp),%rbp
  7a09ab:	48 83 c4 10          	add    $0x10,%rsp
  7a09af:	c3                   	ret    
  7a09b0:	e8 cb 45 cc ff       	call   464f80 <runtime.morestack.abi0>
  7a09b5:	eb c9                	jmp    7a0980 <agentGoProject/common.refreshDirPathMd5.func1>
  7a09b7:	4c 8d 6c 24 18       	lea    0x18(%rsp),%r13
  7a09bc:	0f 1f 40 00          	nopl   0x0(%rax)
  7a09c0:	4d 39 2c 24          	cmp    %r13,(%r12)
  7a09c4:	75 d7                	jne    7a099d <agentGoProject/common.refreshDirPathMd5.func1+0x1d>
  7a09c6:	49 89 24 24          	mov    %rsp,(%r12)
  7a09ca:	eb d1                	jmp    7a099d <agentGoProject/common.refreshDirPathMd5.func1+0x1d>
  7a09cc:	cc                   	int3   
  7a09cd:	cc                   	int3   
  7a09ce:	cc                   	int3   
  7a09cf:	cc                   	int3   
  7a09d0:	cc                   	int3   
  7a09d1:	cc                   	int3   
  7a09d2:	cc                   	int3   
  7a09d3:	cc                   	int3   
  7a09d4:	cc                   	int3   
  7a09d5:	cc                   	int3   
  7a09d6:	cc                   	int3   
  7a09d7:	cc                   	int3   
  7a09d8:	cc                   	int3   
  7a09d9:	cc                   	int3   
  7a09da:	cc                   	int3   
  7a09db:	cc                   	int3   
  7a09dc:	cc                   	int3   
  7a09dd:	cc                   	int3   
  7a09de:	cc                   	int3   
  7a09df:	cc                   	int3   
