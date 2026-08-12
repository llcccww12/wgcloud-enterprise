============ 2. 反汇编 getMd5Val（fileSafeUtil.go:192）============

/root/agent-linux-amd64-v3.6.8/wgcloud-agent-release:     file format elf64-x86-64


Disassembly of section .text:

000000000079f860 <agentGoProject/common.getMd5Val>:
agentGoProject/common.getMd5Val():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:192
  79f860:	4c 8d 64 24 c8       	lea    -0x38(%rsp),%r12
  79f865:	4d 3b 66 10          	cmp    0x10(%r14),%r12
  79f869:	0f 86 23 03 00 00    	jbe    79fb92 <agentGoProject/common.getMd5Val+0x332>
  79f86f:	48 81 ec b8 00 00 00 	sub    $0xb8,%rsp
  79f876:	48 89 ac 24 b0 00 00 	mov    %rbp,0xb0(%rsp)
  79f87d:	00 
  79f87e:	48 8d ac 24 b0 00 00 	lea    0xb0(%rsp),%rbp
  79f885:	00 
  79f886:	49 c7 c5 00 00 00 00 	mov    $0x0,%r13
  79f88d:	4c 89 ac 24 a8 00 00 	mov    %r13,0xa8(%rsp)
  79f894:	00 
  79f895:	48 89 9c 24 c8 00 00 	mov    %rbx,0xc8(%rsp)
  79f89c:	00 
  79f89d:	48 89 84 24 c0 00 00 	mov    %rax,0xc0(%rsp)
  79f8a4:	00 
  79f8a5:	c6 44 24 3f 00       	movb   $0x0,0x3f(%rsp)
  79f8aa:	44 0f 11 7c 24 68    	movups %xmm15,0x68(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:193
  79f8b0:	e8 ab 62 ff ff       	call   795b60 <agentGoProject/common.PathExists>
  79f8b5:	84 c0                	test   %al,%al
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:194
  79f8b7:	0f 84 84 01 00 00    	je     79fa41 <agentGoProject/common.getMd5Val+0x1e1>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:199
  79f8bd:	48 8b 84 24 c0 00 00 	mov    0xc0(%rsp),%rax
  79f8c4:	00 
  79f8c5:	48 8b 9c 24 c8 00 00 	mov    0xc8(%rsp),%rbx
  79f8cc:	00 
  79f8cd:	e8 2e 62 ff ff       	call   795b00 <agentGoProject/common.IsDir>
  79f8d2:	84 c0                	test   %al,%al
  79f8d4:	0f 85 3d 01 00 00    	jne    79fa17 <agentGoProject/common.getMd5Val+0x1b7>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:202
  79f8da:	90                   	nop
./C:/Program Files/Go/src/os/file.go:306
  79f8db:	48 8b 84 24 c0 00 00 	mov    0xc0(%rsp),%rax
  79f8e2:	00 
os.Open():
./C:/Program Files/Go/src/os/file.go:306
  79f8e3:	48 8b 9c 24 c8 00 00 	mov    0xc8(%rsp),%rbx
  79f8ea:	00 
  79f8eb:	31 c9                	xor    %ecx,%ecx
  79f8ed:	31 ff                	xor    %edi,%edi
  79f8ef:	e8 ec 42 d4 ff       	call   4e3be0 <os.OpenFile>
agentGoProject/common.getMd5Val():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:203
  79f8f4:	44 0f 11 7c 24 78    	movups %xmm15,0x78(%rsp)
  79f8fa:	48 8d 15 bf 02 00 00 	lea    0x2bf(%rip),%rdx        # 79fbc0 <agentGoProject/common.getMd5Val.func1>
  79f901:	48 89 54 24 78       	mov    %rdx,0x78(%rsp)
  79f906:	48 89 84 24 80 00 00 	mov    %rax,0x80(%rsp)
  79f90d:	00 
  79f90e:	48 8d 54 24 78       	lea    0x78(%rsp),%rdx
  79f913:	48 89 94 24 a8 00 00 	mov    %rdx,0xa8(%rsp)
  79f91a:	00 
  79f91b:	c6 44 24 3f 01       	movb   $0x1,0x3f(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:204
  79f920:	48 85 db             	test   %rbx,%rbx
  79f923:	0f 85 bc 00 00 00    	jne    79f9e5 <agentGoProject/common.getMd5Val+0x185>
os.Open():
./C:/Program Files/Go/src/os/file.go:306
  79f929:	48 89 44 24 50       	mov    %rax,0x50(%rsp)
agentGoProject/common.getMd5Val():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:205
  79f92e:	90                   	nop
./C:/Program Files/Go/src/crypto/md5/md5.go:102
  79f92f:	48 8d 05 2a 14 0a 00 	lea    0xa142a(%rip),%rax        # 840d60 <type:*+0x7ed60>
crypto/md5.New():
./C:/Program Files/Go/src/crypto/md5/md5.go:102
  79f936:	e8 45 e3 c6 ff       	call   40dc80 <runtime.newobject>
  79f93b:	48 89 44 24 60       	mov    %rax,0x60(%rsp)
./C:/Program Files/Go/src/crypto/md5/md5.go:103
  79f940:	90                   	nop
agentGoProject/common.getMd5Val():
./C:/Program Files/Go/src/crypto/md5/md5.go:47
  79f941:	48 b9 01 23 45 67 89 	movabs $0xefcdab8967452301,%rcx
  79f948:	ab cd ef 
crypto/md5.(*digest).Reset():
./C:/Program Files/Go/src/crypto/md5/md5.go:47
  79f94b:	48 89 08             	mov    %rcx,(%rax)
./C:/Program Files/Go/src/crypto/md5/md5.go:49
  79f94e:	48 b9 fe dc ba 98 76 	movabs $0x1032547698badcfe,%rcx
  79f955:	54 32 10 
  79f958:	48 89 48 08          	mov    %rcx,0x8(%rax)
./C:/Program Files/Go/src/crypto/md5/md5.go:51
  79f95c:	44 0f 11 78 50       	movups %xmm15,0x50(%rax)
agentGoProject/common.getMd5Val():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:206
  79f961:	48 8d 1d 40 57 17 00 	lea    0x175740(%rip),%rbx        # 9150a8 <go:itab.*crypto/md5.digest,hash.Hash>
  79f968:	48 8d 05 f1 53 07 00 	lea    0x753f1(%rip),%rax        # 814d60 <type:*+0x52d60>
  79f96f:	e8 ec bc c6 ff       	call   40b660 <runtime.convI2I>
./C:/Program Files/Go/src/io/io.go:386
  79f974:	48 8b 5c 24 60       	mov    0x60(%rsp),%rbx
io.Copy():
./C:/Program Files/Go/src/io/io.go:386
  79f979:	48 8d 0d 80 28 17 00 	lea    0x172880(%rip),%rcx        # 912200 <go:itab.*os.File,io.Reader>
  79f980:	48 8b 7c 24 50       	mov    0x50(%rsp),%rdi
  79f985:	31 f6                	xor    %esi,%esi
  79f987:	45 31 c0             	xor    %r8d,%r8d
  79f98a:	4d 89 c1             	mov    %r8,%r9
  79f98d:	e8 2e 32 cd ff       	call   472bc0 <io.copyBuffer>
agentGoProject/common.getMd5Val():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:207
  79f992:	48 8b 44 24 60       	mov    0x60(%rsp),%rax
  79f997:	31 db                	xor    %ebx,%ebx
  79f999:	31 c9                	xor    %ecx,%ecx
  79f99b:	48 89 cf             	mov    %rcx,%rdi
  79f99e:	66 90                	xchg   %ax,%ax
  79f9a0:	e8 fb 7c ec ff       	call   6676a0 <crypto/md5.(*digest).Sum>
./C:/Program Files/Go/src/encoding/hex/hex.go:46
  79f9a5:	48 89 44 24 58       	mov    %rax,0x58(%rsp)
./C:/Program Files/Go/src/encoding/hex/hex.go:107
  79f9aa:	48 89 5c 24 40       	mov    %rbx,0x40(%rsp)
./C:/Program Files/Go/src/encoding/hex/hex.go:38
  79f9af:	48 89 d9             	mov    %rbx,%rcx
encoding/hex.EncodedLen():
./C:/Program Files/Go/src/encoding/hex/hex.go:38
  79f9b2:	48 d1 e1             	shl    %rcx
  79f9b5:	48 89 4c 24 48       	mov    %rcx,0x48(%rsp)
encoding/hex.EncodeToString():
./C:/Program Files/Go/src/encoding/hex/hex.go:107
  79f9ba:	48 8d 05 df 24 05 00 	lea    0x524df(%rip),%rax        # 7f1ea0 <type:*+0x2fea0>
  79f9c1:	48 89 cb             	mov    %rcx,%rbx
  79f9c4:	e8 77 ca ca ff       	call   44c440 <runtime.makeslice>
./C:/Program Files/Go/src/encoding/hex/hex.go:108
  79f9c9:	90                   	nop
encoding/hex.Encode():
./C:/Program Files/Go/src/encoding/hex/hex.go:46
  79f9ca:	48 8b 4c 24 40       	mov    0x40(%rsp),%rcx
  79f9cf:	48 8b 54 24 48       	mov    0x48(%rsp),%rdx
  79f9d4:	48 8b 5c 24 58       	mov    0x58(%rsp),%rbx
  79f9d9:	31 f6                	xor    %esi,%esi
  79f9db:	31 ff                	xor    %edi,%edi
  79f9dd:	0f 1f 00             	nopl   (%rax)
  79f9e0:	e9 f3 00 00 00       	jmp    79fad8 <agentGoProject/common.getMd5Val+0x278>
agentGoProject/common.getMd5Val():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:210
  79f9e5:	44 0f 11 7c 24 68    	movups %xmm15,0x68(%rsp)
  79f9eb:	c6 44 24 3f 00       	movb   $0x0,0x3f(%rsp)
  79f9f0:	48 8b 94 24 a8 00 00 	mov    0xa8(%rsp),%rdx
  79f9f7:	00 
  79f9f8:	48 8b 02             	mov    (%rdx),%rax
  79f9fb:	ff d0                	call   *%rax
  79f9fd:	48 8b 5c 24 70       	mov    0x70(%rsp),%rbx
  79fa02:	48 8b 44 24 68       	mov    0x68(%rsp),%rax
  79fa07:	48 8b ac 24 b0 00 00 	mov    0xb0(%rsp),%rbp
  79fa0e:	00 
  79fa0f:	48 81 c4 b8 00 00 00 	add    $0xb8,%rsp
  79fa16:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:200
  79fa17:	48 8b 84 24 c0 00 00 	mov    0xc0(%rsp),%rax
  79fa1e:	00 
  79fa1f:	48 89 44 24 68       	mov    %rax,0x68(%rsp)
  79fa24:	48 8b 9c 24 c8 00 00 	mov    0xc8(%rsp),%rbx
  79fa2b:	00 
  79fa2c:	48 89 5c 24 70       	mov    %rbx,0x70(%rsp)
  79fa31:	48 8b ac 24 b0 00 00 	mov    0xb0(%rsp),%rbp
  79fa38:	00 
  79fa39:	48 81 c4 b8 00 00 00 	add    $0xb8,%rsp
  79fa40:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:195
  79fa41:	44 0f 11 bc 24 88 00 	movups %xmm15,0x88(%rsp)
  79fa48:	00 00 
  79fa4a:	44 0f 11 bc 24 98 00 	movups %xmm15,0x98(%rsp)
  79fa51:	00 00 
  79fa53:	48 8d 0d 46 22 05 00 	lea    0x52246(%rip),%rcx        # 7f1ca0 <type:*+0x2fca0>
  79fa5a:	48 89 8c 24 88 00 00 	mov    %rcx,0x88(%rsp)
  79fa61:	00 
  79fa62:	48 8d 15 37 05 17 00 	lea    0x170537(%rip),%rdx        # 90ffa0 <runtime.buildVersion.str+0xd00>
  79fa69:	48 89 94 24 90 00 00 	mov    %rdx,0x90(%rsp)
  79fa70:	00 
  79fa71:	48 8b 84 24 c0 00 00 	mov    0xc0(%rsp),%rax
  79fa78:	00 
  79fa79:	48 8b 9c 24 c8 00 00 	mov    0xc8(%rsp),%rbx
  79fa80:	00 
  79fa81:	e8 7a ba c6 ff       	call   40b500 <runtime.convTstring>
  79fa86:	48 8d 0d 13 22 05 00 	lea    0x52213(%rip),%rcx        # 7f1ca0 <type:*+0x2fca0>
  79fa8d:	48 89 8c 24 98 00 00 	mov    %rcx,0x98(%rsp)
  79fa94:	00 
  79fa95:	48 89 84 24 a0 00 00 	mov    %rax,0xa0(%rsp)
  79fa9c:	00 
  79fa9d:	48 8d 84 24 88 00 00 	lea    0x88(%rsp),%rax
  79faa4:	00 
  79faa5:	bb 02 00 00 00       	mov    $0x2,%ebx
  79faaa:	48 89 d9             	mov    %rbx,%rcx
  79faad:	e8 6e d4 de ff       	call   58cf20 <log.Println>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:196
  79fab2:	44 0f 11 7c 24 68    	movups %xmm15,0x68(%rsp)
  79fab8:	31 c0                	xor    %eax,%eax
  79faba:	31 db                	xor    %ebx,%ebx
  79fabc:	48 8b ac 24 b0 00 00 	mov    0xb0(%rsp),%rbp
  79fac3:	00 
  79fac4:	48 81 c4 b8 00 00 00 	add    $0xb8,%rsp
  79facb:	c3                   	ret    
encoding/hex.Encode():
./C:/Program Files/Go/src/encoding/hex/hex.go:48
  79facc:	44 88 4c 07 01       	mov    %r9b,0x1(%rdi,%rax,1)
./C:/Program Files/Go/src/encoding/hex/hex.go:46
  79fad1:	48 ff c6             	inc    %rsi
./C:/Program Files/Go/src/encoding/hex/hex.go:49
  79fad4:	48 83 c7 02          	add    $0x2,%rdi
./C:/Program Files/Go/src/encoding/hex/hex.go:46
  79fad8:	48 39 f1             	cmp    %rsi,%rcx
  79fadb:	7e 39                	jle    79fb16 <agentGoProject/common.getMd5Val+0x2b6>
  79fadd:	44 0f b6 04 33       	movzbl (%rbx,%rsi,1),%r8d
./C:/Program Files/Go/src/encoding/hex/hex.go:47
  79fae2:	45 89 c1             	mov    %r8d,%r9d
  79fae5:	41 c0 e8 04          	shr    $0x4,%r8b
  79fae9:	45 0f b6 c0          	movzbl %r8b,%r8d
  79faed:	4c 8d 15 8b 2b 0d 00 	lea    0xd2b8b(%rip),%r10        # 87267f <go:string.*+0x50b7>
  79faf4:	47 0f b6 04 02       	movzbl (%r10,%r8,1),%r8d
  79faf9:	48 39 d7             	cmp    %rdx,%rdi
  79fafc:	73 69                	jae    79fb67 <agentGoProject/common.getMd5Val+0x307>
  79fafe:	44 88 04 38          	mov    %r8b,(%rax,%rdi,1)
./C:/Program Files/Go/src/encoding/hex/hex.go:48
  79fb02:	4c 8d 47 01          	lea    0x1(%rdi),%r8
  79fb06:	41 83 e1 0f          	and    $0xf,%r9d
  79fb0a:	47 0f b6 0c 11       	movzbl (%r9,%r10,1),%r9d
  79fb0f:	4c 39 c2             	cmp    %r8,%rdx
  79fb12:	77 b8                	ja     79facc <agentGoProject/common.getMd5Val+0x26c>
  79fb14:	eb 46                	jmp    79fb5c <agentGoProject/common.getMd5Val+0x2fc>
encoding/hex.EncodeToString():
./C:/Program Files/Go/src/encoding/hex/hex.go:109
  79fb16:	48 89 c3             	mov    %rax,%rbx
  79fb19:	48 89 d1             	mov    %rdx,%rcx
  79fb1c:	31 c0                	xor    %eax,%eax
  79fb1e:	66 90                	xchg   %ax,%ax
  79fb20:	e8 bb 0b cb ff       	call   4506e0 <runtime.slicebytetostring>
agentGoProject/common.getMd5Val():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:208
  79fb25:	48 89 44 24 68       	mov    %rax,0x68(%rsp)
  79fb2a:	48 89 5c 24 70       	mov    %rbx,0x70(%rsp)
  79fb2f:	c6 44 24 3f 00       	movb   $0x0,0x3f(%rsp)
  79fb34:	48 8b 94 24 a8 00 00 	mov    0xa8(%rsp),%rdx
  79fb3b:	00 
  79fb3c:	48 8b 32             	mov    (%rdx),%rsi
  79fb3f:	90                   	nop
  79fb40:	ff d6                	call   *%rsi
  79fb42:	48 8b 44 24 68       	mov    0x68(%rsp),%rax
  79fb47:	48 8b 5c 24 70       	mov    0x70(%rsp),%rbx
  79fb4c:	48 8b ac 24 b0 00 00 	mov    0xb0(%rsp),%rbp
  79fb53:	00 
  79fb54:	48 81 c4 b8 00 00 00 	add    $0xb8,%rsp
  79fb5b:	c3                   	ret    
encoding/hex.Encode():
./C:/Program Files/Go/src/encoding/hex/hex.go:48
  79fb5c:	4c 89 c0             	mov    %r8,%rax
  79fb5f:	48 89 d1             	mov    %rdx,%rcx
  79fb62:	e8 19 79 cc ff       	call   467480 <runtime.panicIndex>
./C:/Program Files/Go/src/encoding/hex/hex.go:47
  79fb67:	48 89 f8             	mov    %rdi,%rax
  79fb6a:	48 89 d1             	mov    %rdx,%rcx
  79fb6d:	e8 0e 79 cc ff       	call   467480 <runtime.panicIndex>
  79fb72:	90                   	nop
  79fb73:	e8 a8 41 c9 ff       	call   433d20 <runtime.deferreturn>
  79fb78:	48 8b 44 24 68       	mov    0x68(%rsp),%rax
  79fb7d:	48 8b 5c 24 70       	mov    0x70(%rsp),%rbx
  79fb82:	48 8b ac 24 b0 00 00 	mov    0xb0(%rsp),%rbp
  79fb89:	00 
  79fb8a:	48 81 c4 b8 00 00 00 	add    $0xb8,%rsp
  79fb91:	c3                   	ret    
agentGoProject/common.getMd5Val():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:192
  79fb92:	48 89 44 24 08       	mov    %rax,0x8(%rsp)
  79fb97:	48 89 5c 24 10       	mov    %rbx,0x10(%rsp)
  79fb9c:	0f 1f 40 00          	nopl   0x0(%rax)
  79fba0:	e8 7b 54 cc ff       	call   465020 <runtime.morestack_noctxt.abi0>
  79fba5:	48 8b 44 24 08       	mov    0x8(%rsp),%rax
  79fbaa:	48 8b 5c 24 10       	mov    0x10(%rsp),%rbx
  79fbaf:	e9 ac fc ff ff       	jmp    79f860 <agentGoProject/common.getMd5Val>
  79fbb4:	cc                   	int3   
  79fbb5:	cc                   	int3   
  79fbb6:	cc                   	int3   
  79fbb7:	cc                   	int3   
  79fbb8:	cc                   	int3   
  79fbb9:	cc                   	int3   
  79fbba:	cc                   	int3   
  79fbbb:	cc                   	int3   
  79fbbc:	cc                   	int3   
  79fbbd:	cc                   	int3   
  79fbbe:	cc                   	int3   
  79fbbf:	cc                   	int3   

000000000079fbc0 <agentGoProject/common.getMd5Val.func1>:
agentGoProject/common.getMd5Val.func1():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:203
  79fbc0:	49 3b 66 10          	cmp    0x10(%r14),%rsp
  79fbc4:	76 2a                	jbe    79fbf0 <agentGoProject/common.getMd5Val.func1+0x30>
  79fbc6:	48 83 ec 10          	sub    $0x10,%rsp
  79fbca:	48 89 6c 24 08       	mov    %rbp,0x8(%rsp)
  79fbcf:	48 8d 6c 24 08       	lea    0x8(%rsp),%rbp
  79fbd4:	4d 8b 66 20          	mov    0x20(%r14),%r12
  79fbd8:	4d 85 e4             	test   %r12,%r12
  79fbdb:	75 1a                	jne    79fbf7 <agentGoProject/common.getMd5Val.func1+0x37>
  79fbdd:	48 8b 42 08          	mov    0x8(%rdx),%rax
  79fbe1:	e8 3a 47 d4 ff       	call   4e4320 <os.(*File).Close>
  79fbe6:	48 8b 6c 24 08       	mov    0x8(%rsp),%rbp
  79fbeb:	48 83 c4 10          	add    $0x10,%rsp
  79fbef:	c3                   	ret    
  79fbf0:	e8 8b 53 cc ff       	call   464f80 <runtime.morestack.abi0>
  79fbf5:	eb c9                	jmp    79fbc0 <agentGoProject/common.getMd5Val.func1>
  79fbf7:	4c 8d 6c 24 18       	lea    0x18(%rsp),%r13
  79fbfc:	0f 1f 40 00          	nopl   0x0(%rax)
  79fc00:	4d 39 2c 24          	cmp    %r13,(%r12)
  79fc04:	75 d7                	jne    79fbdd <agentGoProject/common.getMd5Val.func1+0x1d>
  79fc06:	49 89 24 24          	mov    %rsp,(%r12)
  79fc0a:	eb d1                	jmp    79fbdd <agentGoProject/common.getMd5Val.func1+0x1d>
  79fc0c:	cc                   	int3   
  79fc0d:	cc                   	int3   
  79fc0e:	cc                   	int3   
  79fc0f:	cc                   	int3   
  79fc10:	cc                   	int3   
  79fc11:	cc                   	int3   
  79fc12:	cc                   	int3   
  79fc13:	cc                   	int3   
  79fc14:	cc                   	int3   
  79fc15:	cc                   	int3   
  79fc16:	cc                   	int3   
  79fc17:	cc                   	int3   
  79fc18:	cc                   	int3   
  79fc19:	cc                   	int3   
  79fc1a:	cc                   	int3   
  79fc1b:	cc                   	int3   
  79fc1c:	cc                   	int3   
  79fc1d:	cc                   	int3   
  79fc1e:	cc                   	int3   
  79fc1f:	cc                   	int3   

000000000079fc20 <agentGoProject/common.getFileModTime>:
agentGoProject/common.getFileModTime():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:217
  79fc20:	49 3b 66 10          	cmp    0x10(%r14),%rsp
  79fc24:	0f 86 fc 00 00 00    	jbe    79fd26 <agentGoProject/common.getFileModTime+0x106>
  79fc2a:	48 83 ec 58          	sub    $0x58,%rsp
  79fc2e:	48 89 6c 24 50       	mov    %rbp,0x50(%rsp)
  79fc33:	48 8d 6c 24 50       	lea    0x50(%rsp),%rbp
  79fc38:	48 89 5c 24 68       	mov    %rbx,0x68(%rsp)
  79fc3d:	48 89 44 24 60       	mov    %rax,0x60(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:218
  79fc42:	e8 19 5f ff ff       	call   795b60 <agentGoProject/common.PathExists>
  79fc47:	84 c0                	test   %al,%al
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:219
  79fc49:	74 76                	je     79fcc1 <agentGoProject/common.getFileModTime+0xa1>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:223
  79fc4b:	48 8b 44 24 60       	mov    0x60(%rsp),%rax
  79fc50:	48 8b 5c 24 68       	mov    0x68(%rsp),%rbx
  79fc55:	e8 26 7d d4 ff       	call   4e7980 <os.Stat>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:224
  79fc5a:	48 85 c9             	test   %rcx,%rcx
  79fc5d:	75 52                	jne    79fcb1 <agentGoProject/common.getFileModTime+0x91>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:225
  79fc5f:	48 8b 48 20          	mov    0x20(%rax),%rcx
  79fc63:	48 89 d8             	mov    %rbx,%rax
  79fc66:	ff d1                	call   *%rcx
  79fc68:	48 89 44 24 18       	mov    %rax,0x18(%rsp)
  79fc6d:	48 89 5c 24 20       	mov    %rbx,0x20(%rsp)
  79fc72:	48 89 4c 24 28       	mov    %rcx,0x28(%rsp)
./C:/Program Files/Go/src/time/time.go:1193
  79fc77:	90                   	nop
./C:/Program Files/Go/src/time/time.go:171
  79fc78:	48 8b 4c 24 18       	mov    0x18(%rsp),%rcx
time.(*Time).sec():
./C:/Program Files/Go/src/time/time.go:171
  79fc7d:	48 0f ba e1 3f       	bt     $0x3f,%rcx
  79fc82:	73 15                	jae    79fc99 <agentGoProject/common.getFileModTime+0x79>
./C:/Program Files/Go/src/time/time.go:172
  79fc84:	48 d1 e1             	shl    %rcx
  79fc87:	48 c1 e9 1f          	shr    $0x1f,%rcx
  79fc8b:	48 ba 80 7f b1 d7 0d 	movabs $0xdd7b17f80,%rdx
  79fc92:	00 00 00 
  79fc95:	48 8d 1c 0a          	lea    (%rdx,%rcx,1),%rbx
agentGoProject/common.getFileModTime():
./C:/Program Files/Go/src/time/time.go:178
  79fc99:	48 b9 00 09 6e 88 f1 	movabs $0xfffffff1886e0900,%rcx
  79fca0:	ff ff ff 
time.(*Time).unixSec():
./C:/Program Files/Go/src/time/time.go:178
  79fca3:	48 8d 04 19          	lea    (%rcx,%rbx,1),%rax
agentGoProject/common.getFileModTime():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:225
  79fca7:	48 8b 6c 24 50       	mov    0x50(%rsp),%rbp
  79fcac:	48 83 c4 58          	add    $0x58,%rsp
  79fcb0:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:227
  79fcb1:	b8 20 5c c2 02       	mov    $0x2c25c20,%eax
  79fcb6:	48 8b 6c 24 50       	mov    0x50(%rsp),%rbp
  79fcbb:	48 83 c4 58          	add    $0x58,%rsp
  79fcbf:	90                   	nop
  79fcc0:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:220
  79fcc1:	44 0f 11 7c 24 30    	movups %xmm15,0x30(%rsp)
  79fcc7:	44 0f 11 7c 24 40    	movups %xmm15,0x40(%rsp)
  79fccd:	48 8d 0d cc 1f 05 00 	lea    0x51fcc(%rip),%rcx        # 7f1ca0 <type:*+0x2fca0>
  79fcd4:	48 89 4c 24 30       	mov    %rcx,0x30(%rsp)
  79fcd9:	48 8d 15 c0 02 17 00 	lea    0x1702c0(%rip),%rdx        # 90ffa0 <runtime.buildVersion.str+0xd00>
  79fce0:	48 89 54 24 38       	mov    %rdx,0x38(%rsp)
  79fce5:	48 8b 44 24 60       	mov    0x60(%rsp),%rax
  79fcea:	48 8b 5c 24 68       	mov    0x68(%rsp),%rbx
  79fcef:	e8 0c b8 c6 ff       	call   40b500 <runtime.convTstring>
  79fcf4:	48 8d 0d a5 1f 05 00 	lea    0x51fa5(%rip),%rcx        # 7f1ca0 <type:*+0x2fca0>
  79fcfb:	48 89 4c 24 40       	mov    %rcx,0x40(%rsp)
  79fd00:	48 89 44 24 48       	mov    %rax,0x48(%rsp)
  79fd05:	48 8d 44 24 30       	lea    0x30(%rsp),%rax
  79fd0a:	bb 02 00 00 00       	mov    $0x2,%ebx
  79fd0f:	48 89 d9             	mov    %rbx,%rcx
  79fd12:	e8 09 d2 de ff       	call   58cf20 <log.Println>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:221
  79fd17:	b8 20 5c c2 02       	mov    $0x2c25c20,%eax
  79fd1c:	48 8b 6c 24 50       	mov    0x50(%rsp),%rbp
  79fd21:	48 83 c4 58          	add    $0x58,%rsp
  79fd25:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:217
  79fd26:	48 89 44 24 08       	mov    %rax,0x8(%rsp)
  79fd2b:	48 89 5c 24 10       	mov    %rbx,0x10(%rsp)
  79fd30:	e8 eb 52 cc ff       	call   465020 <runtime.morestack_noctxt.abi0>
  79fd35:	48 8b 44 24 08       	mov    0x8(%rsp),%rax
  79fd3a:	48 8b 5c 24 10       	mov    0x10(%rsp),%rbx
  79fd3f:	90                   	nop
  79fd40:	e9 db fe ff ff       	jmp    79fc20 <agentGoProject/common.getFileModTime>
  79fd45:	cc                   	int3   
  79fd46:	cc                   	int3   
  79fd47:	cc                   	int3   
  79fd48:	cc                   	int3   
  79fd49:	cc                   	int3   
  79fd4a:	cc                   	int3   
  79fd4b:	cc                   	int3   
  79fd4c:	cc                   	int3   
  79fd4d:	cc                   	int3   
  79fd4e:	cc                   	int3   
  79fd4f:	cc                   	int3   
  79fd50:	cc                   	int3   
  79fd51:	cc                   	int3   
  79fd52:	cc                   	int3   
  79fd53:	cc                   	int3   
  79fd54:	cc                   	int3   
  79fd55:	cc                   	int3   
  79fd56:	cc                   	int3   
  79fd57:	cc                   	int3   
  79fd58:	cc                   	int3   
  79fd59:	cc                   	int3   
  79fd5a:	cc                   	int3   
  79fd5b:	cc                   	int3   
  79fd5c:	cc                   	int3   
  79fd5d:	cc                   	int3   
  79fd5e:	cc                   	int3   
  79fd5f:	cc                   	int3   
