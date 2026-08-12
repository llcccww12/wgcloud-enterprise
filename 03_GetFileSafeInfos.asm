============ 3. 反汇编 GetFileSafeInfos（fileSafeUtil.go 行号待查）============

/root/agent-linux-amd64-v3.6.8/wgcloud-agent-release:     file format elf64-x86-64


Disassembly of section .text:

000000000079e680 <agentGoProject/common.GetFileSafeInfos>:
agentGoProject/common.GetFileSafeInfos():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:56
  79e680:	4c 8d a4 24 e0 fe ff 	lea    -0x120(%rsp),%r12
  79e687:	ff 
  79e688:	4d 3b 66 10          	cmp    0x10(%r14),%r12
  79e68c:	0f 86 c2 07 00 00    	jbe    79ee54 <agentGoProject/common.GetFileSafeInfos+0x7d4>
  79e692:	48 81 ec a0 01 00 00 	sub    $0x1a0,%rsp
  79e699:	48 89 ac 24 98 01 00 	mov    %rbp,0x198(%rsp)
  79e6a0:	00 
  79e6a1:	48 8d ac 24 98 01 00 	lea    0x198(%rsp),%rbp
  79e6a8:	00 
  79e6a9:	44 0f 11 bc 24 80 01 	movups %xmm15,0x180(%rsp)
  79e6b0:	00 00 
  79e6b2:	44 0f 11 bc 24 88 01 	movups %xmm15,0x188(%rsp)
  79e6b9:	00 00 
  79e6bb:	48 89 9c 24 b0 01 00 	mov    %rbx,0x1b0(%rsp)
  79e6c2:	00 
  79e6c3:	48 89 84 24 a8 01 00 	mov    %rax,0x1a8(%rsp)
  79e6ca:	00 
  79e6cb:	c6 44 24 5f 00       	movb   $0x0,0x5f(%rsp)
  79e6d0:	44 0f 11 bc 24 28 01 	movups %xmm15,0x128(%rsp)
  79e6d7:	00 00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:57
  79e6d9:	48 8d 0d 28 89 10 00 	lea    0x108928(%rip),%rcx        # 8a7008 <go:func.*+0x928>
  79e6e0:	48 89 8c 24 90 01 00 	mov    %rcx,0x190(%rsp)
  79e6e7:	00 
  79e6e8:	c6 44 24 5f 01       	movb   $0x1,0x5f(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:67
  79e6ed:	48 8b 0d dc 57 40 00 	mov    0x4057dc(%rip),%rcx        # ba3ed0 <agentGoProject/common.HOST_INFO>
  79e6f4:	48 89 8c 24 f0 00 00 	mov    %rcx,0xf0(%rsp)
  79e6fb:	00 
  79e6fc:	48 8b 15 d5 57 40 00 	mov    0x4057d5(%rip),%rdx        # ba3ed8 <agentGoProject/common.HOST_INFO+0x8>
  79e703:	48 89 94 24 90 00 00 	mov    %rdx,0x90(%rsp)
  79e70a:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:68
  79e70b:	48 8b 35 0e 58 40 00 	mov    0x40580e(%rip),%rsi        # ba3f20 <agentGoProject/common.WG_TOKEN>
  79e712:	48 89 b4 24 b8 00 00 	mov    %rsi,0xb8(%rsp)
  79e719:	00 
  79e71a:	48 8b 3d 07 58 40 00 	mov    0x405807(%rip),%rdi        # ba3f28 <agentGoProject/common.WG_TOKEN+0x8>
  79e721:	48 89 7c 24 60       	mov    %rdi,0x60(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:69
  79e726:	4c 8b 05 93 57 40 00 	mov    0x405793(%rip),%r8        # ba3ec0 <agentGoProject/common.BIND_IP>
  79e72d:	4c 89 84 24 e8 00 00 	mov    %r8,0xe8(%rsp)
  79e734:	00 
  79e735:	4c 8b 0d 8c 57 40 00 	mov    0x40578c(%rip),%r9        # ba3ec8 <agentGoProject/common.BIND_IP+0x8>
  79e73c:	4c 89 8c 24 88 00 00 	mov    %r9,0x88(%rsp)
  79e743:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:70
  79e744:	90                   	nop
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/commonFuncs.go:192
  79e745:	48 8d 05 14 9e 0c 00 	lea    0xc9e14(%rip),%rax        # 868560 <type:*+0xa6560>
agentGoProject/common.GetHttpClient():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/commonFuncs.go:192
  79e74c:	e8 2f f5 c6 ff       	call   40dc80 <runtime.newobject>
  79e751:	48 89 84 24 c0 00 00 	mov    %rax,0xc0(%rsp)
  79e758:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/commonFuncs.go:193
  79e759:	48 8d 05 c0 7e 0c 00 	lea    0xc7ec0(%rip),%rax        # 866620 <type:*+0xa4620>
  79e760:	e8 1b f5 c6 ff       	call   40dc80 <runtime.newobject>
  79e765:	c6 80 a0 00 00 00 01 	movb   $0x1,0xa0(%rax)
  79e76c:	83 3d dd 6d 43 00 00 	cmpl   $0x0,0x436ddd(%rip)        # bd5550 <runtime.writeBarrier>
  79e773:	75 11                	jne    79e786 <agentGoProject/common.GetFileSafeInfos+0x106>
  79e775:	48 8b 8c 24 c0 00 00 	mov    0xc0(%rsp),%rcx
  79e77c:	00 
  79e77d:	48 89 81 a0 00 00 00 	mov    %rax,0xa0(%rcx)
  79e784:	eb 22                	jmp    79e7a8 <agentGoProject/common.GetFileSafeInfos+0x128>
  79e786:	48 8b bc 24 c0 00 00 	mov    0xc0(%rsp),%rdi
  79e78d:	00 
  79e78e:	48 8d 8f a0 00 00 00 	lea    0xa0(%rdi),%rcx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/commonFuncs.go:192
  79e795:	48 89 fa             	mov    %rdi,%rdx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/commonFuncs.go:193
  79e798:	48 89 cf             	mov    %rcx,%rdi
  79e79b:	0f 1f 44 00 00       	nopl   0x0(%rax,%rax,1)
  79e7a0:	e8 3b 88 cc ff       	call   466fe0 <runtime.gcWriteBarrier>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/commonFuncs.go:194
  79e7a5:	48 89 d1             	mov    %rdx,%rcx
  79e7a8:	48 c7 81 b8 00 00 00 	movq   $0x1e,0xb8(%rcx)
  79e7af:	1e 00 00 00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/commonFuncs.go:195
  79e7b3:	48 ba 00 58 47 f8 0d 	movabs $0xdf8475800,%rdx
  79e7ba:	00 00 00 
  79e7bd:	48 89 91 d0 00 00 00 	mov    %rdx,0xd0(%rcx)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/commonFuncs.go:199
  79e7c4:	48 8d 05 55 44 0a 00 	lea    0xa4455(%rip),%rax        # 842c20 <type:*+0x80c20>
  79e7cb:	e8 b0 f4 c6 ff       	call   40dc80 <runtime.newobject>
  79e7d0:	48 89 84 24 f8 00 00 	mov    %rax,0xf8(%rsp)
  79e7d7:	00 
  79e7d8:	48 b9 00 c8 17 a8 04 	movabs $0x4a817c800,%rcx
  79e7df:	00 00 00 
  79e7e2:	48 89 48 28          	mov    %rcx,0x28(%rax)
  79e7e6:	48 8d 0d 33 38 17 00 	lea    0x173833(%rip),%rcx        # 912020 <go:itab.*net/http.Transport,net/http.RoundTripper>
  79e7ed:	48 89 08             	mov    %rcx,(%rax)
  79e7f0:	83 3d 59 6d 43 00 00 	cmpl   $0x0,0x436d59(%rip)        # bd5550 <runtime.writeBarrier>
  79e7f7:	75 0e                	jne    79e807 <agentGoProject/common.GetFileSafeInfos+0x187>
  79e7f9:	48 8b 94 24 c0 00 00 	mov    0xc0(%rsp),%rdx
  79e800:	00 
  79e801:	48 89 50 08          	mov    %rdx,0x8(%rax)
  79e805:	eb 11                	jmp    79e818 <agentGoProject/common.GetFileSafeInfos+0x198>
  79e807:	48 8d 78 08          	lea    0x8(%rax),%rdi
  79e80b:	48 8b 94 24 c0 00 00 	mov    0xc0(%rsp),%rdx
  79e812:	00 
  79e813:	e8 e8 88 cc ff       	call   467100 <runtime.gcWriteBarrierDX>
  79e818:	48 8d 15 35 50 0d 00 	lea    0xd5035(%rip),%rdx        # 873854 <go:string.*+0x628c>
  79e81f:	48 89 14 24          	mov    %rdx,(%rsp)
  79e823:	48 c7 44 24 08 12 00 	movq   $0x12,0x8(%rsp)
  79e82a:	00 00 
agentGoProject/common.GetFileSafeInfos():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:71
  79e82c:	31 c0                	xor    %eax,%eax
  79e82e:	48 8d 1d c4 29 0d 00 	lea    0xd29c4(%rip),%rbx        # 8711f9 <go:string.*+0x3c31>
  79e835:	b9 0c 00 00 00       	mov    $0xc,%ecx
  79e83a:	48 8b bc 24 b8 00 00 	mov    0xb8(%rsp),%rdi
  79e841:	00 
  79e842:	48 8b 74 24 60       	mov    0x60(%rsp),%rsi
  79e847:	4c 8d 05 c5 32 0d 00 	lea    0xd32c5(%rip),%r8        # 871b13 <go:string.*+0x454b>
  79e84e:	41 b9 0e 00 00 00    	mov    $0xe,%r9d
  79e854:	4c 8b 94 24 e8 00 00 	mov    0xe8(%rsp),%r10
  79e85b:	00 
  79e85c:	4c 8b 9c 24 88 00 00 	mov    0x88(%rsp),%r11
  79e863:	00 
  79e864:	e8 57 1d cb ff       	call   4505c0 <runtime.concatstring5>
  79e869:	48 89 d9             	mov    %rbx,%rcx
  79e86c:	48 89 c3             	mov    %rax,%rbx
  79e86f:	31 c0                	xor    %eax,%eax
  79e871:	e8 6a 20 cb ff       	call   4508e0 <runtime.stringtoslicebyte>
./C:/Program Files/Go/src/bytes/buffer.go:463
  79e876:	48 89 84 24 e0 00 00 	mov    %rax,0xe0(%rsp)
  79e87d:	00 
bytes.NewBuffer():
./C:/Program Files/Go/src/bytes/buffer.go:463
  79e87e:	48 89 5c 24 70       	mov    %rbx,0x70(%rsp)
  79e883:	48 89 4c 24 78       	mov    %rcx,0x78(%rsp)
  79e888:	48 8d 05 91 26 09 00 	lea    0x92691(%rip),%rax        # 830f20 <type:*+0x6ef20>
  79e88f:	e8 ec f3 c6 ff       	call   40dc80 <runtime.newobject>
  79e894:	48 8b 54 24 70       	mov    0x70(%rsp),%rdx
  79e899:	48 89 50 08          	mov    %rdx,0x8(%rax)
  79e89d:	48 8b 54 24 78       	mov    0x78(%rsp),%rdx
  79e8a2:	48 89 50 10          	mov    %rdx,0x10(%rax)
  79e8a6:	83 3d a3 6c 43 00 00 	cmpl   $0x0,0x436ca3(%rip)        # bd5550 <runtime.writeBarrier>
  79e8ad:	75 0d                	jne    79e8bc <agentGoProject/common.GetFileSafeInfos+0x23c>
  79e8af:	48 8b 94 24 e0 00 00 	mov    0xe0(%rsp),%rdx
  79e8b6:	00 
  79e8b7:	48 89 10             	mov    %rdx,(%rax)
  79e8ba:	eb 10                	jmp    79e8cc <agentGoProject/common.GetFileSafeInfos+0x24c>
  79e8bc:	48 89 c7             	mov    %rax,%rdi
  79e8bf:	48 8b 94 24 e0 00 00 	mov    0xe0(%rsp),%rdx
  79e8c6:	00 
  79e8c7:	e8 34 88 cc ff       	call   467100 <runtime.gcWriteBarrierDX>
agentGoProject/common.GetFileSafeInfos():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:72
  79e8cc:	48 8b 9c 24 a8 01 00 	mov    0x1a8(%rsp),%rbx
  79e8d3:	00 
  79e8d4:	48 8b 8c 24 b0 01 00 	mov    0x1b0(%rsp),%rcx
  79e8db:	00 
  79e8dc:	48 8d 3d fc 3f 0d 00 	lea    0xd3ffc(%rip),%rdi        # 8728df <go:string.*+0x5317>
  79e8e3:	be 10 00 00 00       	mov    $0x10,%esi
  79e8e8:	4c 8d 05 f1 2f 17 00 	lea    0x172ff1(%rip),%r8        # 9118e0 <go:itab.*bytes.Buffer,io.Reader>
  79e8ef:	49 89 c1             	mov    %rax,%r9
  79e8f2:	48 8b 84 24 f8 00 00 	mov    0xf8(%rsp),%rax
  79e8f9:	00 
  79e8fa:	e8 c1 1f f5 ff       	call   6f08c0 <net/http.(*Client).Post>
  79e8ff:	48 89 84 24 d0 00 00 	mov    %rax,0xd0(%rsp)
  79e906:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:73
  79e907:	48 85 db             	test   %rbx,%rbx
  79e90a:	74 63                	je     79e96f <agentGoProject/common.GetFileSafeInfos+0x2ef>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:74
  79e90c:	44 0f 11 bc 24 60 01 	movups %xmm15,0x160(%rsp)
  79e913:	00 00 
  79e915:	44 0f 11 bc 24 70 01 	movups %xmm15,0x170(%rsp)
  79e91c:	00 00 
  79e91e:	48 8d 15 7b 33 05 00 	lea    0x5337b(%rip),%rdx        # 7f1ca0 <type:*+0x2fca0>
  79e925:	48 89 94 24 60 01 00 	mov    %rdx,0x160(%rsp)
  79e92c:	00 
  79e92d:	48 8d 35 3c 16 17 00 	lea    0x17163c(%rip),%rsi        # 90ff70 <runtime.buildVersion.str+0xcd0>
  79e934:	48 89 b4 24 68 01 00 	mov    %rsi,0x168(%rsp)
  79e93b:	00 
  79e93c:	74 04                	je     79e942 <agentGoProject/common.GetFileSafeInfos+0x2c2>
  79e93e:	48 8b 5b 08          	mov    0x8(%rbx),%rbx
  79e942:	48 89 9c 24 70 01 00 	mov    %rbx,0x170(%rsp)
  79e949:	00 
  79e94a:	48 89 8c 24 78 01 00 	mov    %rcx,0x178(%rsp)
  79e951:	00 
  79e952:	48 8d 84 24 60 01 00 	lea    0x160(%rsp),%rax
  79e959:	00 
  79e95a:	bb 02 00 00 00       	mov    $0x2,%ebx
  79e95f:	48 89 d9             	mov    %rbx,%rcx
  79e962:	e8 b9 e5 de ff       	call   58cf20 <log.Println>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:76
  79e967:	48 8b 84 24 d0 00 00 	mov    0xd0(%rsp),%rax
  79e96e:	00 
  79e96f:	48 8b 48 40          	mov    0x40(%rax),%rcx
  79e973:	84 01                	test   %al,(%rcx)
  79e975:	48 8b 50 48          	mov    0x48(%rax),%rdx
  79e979:	48 c7 84 24 48 01 00 	movq   $0x0,0x148(%rsp)
  79e980:	00 00 00 00 00 
  79e985:	44 0f 11 bc 24 50 01 	movups %xmm15,0x150(%rsp)
  79e98c:	00 00 
  79e98e:	48 8d 1d 4b 05 00 00 	lea    0x54b(%rip),%rbx        # 79eee0 <agentGoProject/common.GetFileSafeInfos.func2>
  79e995:	48 89 9c 24 48 01 00 	mov    %rbx,0x148(%rsp)
  79e99c:	00 
  79e99d:	48 89 8c 24 50 01 00 	mov    %rcx,0x150(%rsp)
  79e9a4:	00 
  79e9a5:	48 89 94 24 58 01 00 	mov    %rdx,0x158(%rsp)
  79e9ac:	00 
  79e9ad:	48 8d 8c 24 48 01 00 	lea    0x148(%rsp),%rcx
  79e9b4:	00 
  79e9b5:	48 89 8c 24 88 01 00 	mov    %rcx,0x188(%rsp)
  79e9bc:	00 
  79e9bd:	c6 44 24 5f 03       	movb   $0x3,0x5f(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:77
  79e9c2:	48 8d 05 97 1e 09 00 	lea    0x91e97(%rip),%rax        # 830860 <type:*+0x6e860>
  79e9c9:	e8 b2 f2 c6 ff       	call   40dc80 <runtime.newobject>
  79e9ce:	48 89 84 24 20 01 00 	mov    %rax,0x120(%rsp)
  79e9d5:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:78
  79e9d6:	48 8b 8c 24 d0 00 00 	mov    0xd0(%rsp),%rcx
  79e9dd:	00 
  79e9de:	48 8b 59 40          	mov    0x40(%rcx),%rbx
  79e9e2:	48 8b 49 48          	mov    0x48(%rcx),%rcx
  79e9e6:	48 89 8c 24 18 01 00 	mov    %rcx,0x118(%rsp)
  79e9ed:	00 
  79e9ee:	48 8d 05 6b 60 07 00 	lea    0x7606b(%rip),%rax        # 814a60 <type:*+0x52a60>
  79e9f5:	e8 66 cc c6 ff       	call   40b660 <runtime.convI2I>
  79e9fa:	48 8b 9c 24 18 01 00 	mov    0x118(%rsp),%rbx
  79ea01:	00 
  79ea02:	e8 f9 4a cd ff       	call   473500 <io.ReadAll>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:79
  79ea07:	48 89 84 24 c8 00 00 	mov    %rax,0xc8(%rsp)
  79ea0e:	00 
  79ea0f:	48 89 5c 24 68       	mov    %rbx,0x68(%rsp)
  79ea14:	48 89 d9             	mov    %rbx,%rcx
  79ea17:	48 89 c3             	mov    %rax,%rbx
  79ea1a:	48 8d 84 24 98 00 00 	lea    0x98(%rsp),%rax
  79ea21:	00 
  79ea22:	e8 b9 1c cb ff       	call   4506e0 <runtime.slicebytetostring>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:80
  79ea27:	48 89 d9             	mov    %rbx,%rcx
  79ea2a:	48 89 c3             	mov    %rax,%rbx
  79ea2d:	31 c0                	xor    %eax,%eax
  79ea2f:	e8 ac 1e cb ff       	call   4508e0 <runtime.stringtoslicebyte>
  79ea34:	48 8d 3d 85 31 04 00 	lea    0x43185(%rip),%rdi        # 7e1bc0 <type:*+0x1fbc0>
  79ea3b:	48 8b b4 24 20 01 00 	mov    0x120(%rsp),%rsi
  79ea42:	00 
  79ea43:	e8 18 1c d6 ff       	call   500660 <encoding/json.Unmarshal>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:81
  79ea48:	48 8b 8c 24 20 01 00 	mov    0x120(%rsp),%rcx
  79ea4f:	00 
  79ea50:	48 8b 11             	mov    (%rcx),%rdx
  79ea53:	48 83 79 08 01       	cmpq   $0x1,0x8(%rcx)
  79ea58:	75 7d                	jne    79ead7 <agentGoProject/common.GetFileSafeInfos+0x457>
  79ea5a:	80 3a 30             	cmpb   $0x30,(%rdx)
  79ea5d:	75 78                	jne    79ead7 <agentGoProject/common.GetFileSafeInfos+0x457>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:85
  79ea5f:	90                   	nop
./C:/Program Files/Go/src/sync/mutex.go:83
  79ea60:	31 c0                	xor    %eax,%eax
sync.(*Mutex).Lock():
./C:/Program Files/Go/src/sync/mutex.go:83
  79ea62:	48 8d 15 f7 65 43 00 	lea    0x4365f7(%rip),%rdx        # bd5060 <agentGoProject/common.fileSafeGuard>
  79ea69:	bb 01 00 00 00       	mov    $0x1,%ebx
  79ea6e:	f0 0f b1 1a          	lock cmpxchg %ebx,(%rdx)
  79ea72:	0f 94 c2             	sete   %dl
  79ea75:	84 d2                	test   %dl,%dl
  79ea77:	75 0c                	jne    79ea85 <agentGoProject/common.GetFileSafeInfos+0x405>
./C:/Program Files/Go/src/sync/mutex.go:90
  79ea79:	48 8d 05 e0 65 43 00 	lea    0x4365e0(%rip),%rax        # bd5060 <agentGoProject/common.fileSafeGuard>
  79ea80:	e8 bb 1c cd ff       	call   470740 <sync.(*Mutex).lockSlow>
agentGoProject/common.GetFileSafeInfos():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:86
  79ea85:	90                   	nop
./C:/Program Files/Go/src/container/list/list.go:62
  79ea86:	48 8d 05 53 53 08 00 	lea    0x85353(%rip),%rax        # 823de0 <type:*+0x61de0>
container/list.New():
./C:/Program Files/Go/src/container/list/list.go:62
  79ea8d:	e8 ee f1 c6 ff       	call   40dc80 <runtime.newobject>
agentGoProject/common.GetFileSafeInfos():
./C:/Program Files/Go/src/container/list/list.go:56
  79ea92:	48 8d 48 08          	lea    0x8(%rax),%rcx
container/list.(*List).Init():
./C:/Program Files/Go/src/container/list/list.go:55
  79ea96:	83 3d b3 6a 43 00 00 	cmpl   $0x0,0x436ab3(%rip)        # bd5550 <runtime.writeBarrier>
  79ea9d:	75 09                	jne    79eaa8 <agentGoProject/common.GetFileSafeInfos+0x428>
  79ea9f:	48 89 00             	mov    %rax,(%rax)
./C:/Program Files/Go/src/container/list/list.go:56
  79eaa2:	48 89 40 08          	mov    %rax,0x8(%rax)
./C:/Program Files/Go/src/container/list/list.go:55
  79eaa6:	eb 10                	jmp    79eab8 <agentGoProject/common.GetFileSafeInfos+0x438>
  79eaa8:	48 89 c7             	mov    %rax,%rdi
  79eaab:	e8 30 85 cc ff       	call   466fe0 <runtime.gcWriteBarrier>
./C:/Program Files/Go/src/container/list/list.go:56
  79eab0:	48 89 cf             	mov    %rcx,%rdi
  79eab3:	e8 28 85 cc ff       	call   466fe0 <runtime.gcWriteBarrier>
container/list.New():
./C:/Program Files/Go/src/container/list/list.go:62
  79eab8:	48 89 84 24 d8 00 00 	mov    %rax,0xd8(%rsp)
  79eabf:	00 
container/list.(*List).Init():
./C:/Program Files/Go/src/container/list/list.go:56
  79eac0:	48 89 8c 24 10 01 00 	mov    %rcx,0x110(%rsp)
  79eac7:	00 
./C:/Program Files/Go/src/container/list/list.go:57
  79eac8:	48 c7 40 28 00 00 00 	movq   $0x0,0x28(%rax)
  79eacf:	00 
  79ead0:	31 d2                	xor    %edx,%edx
agentGoProject/common.GetFileSafeInfos():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:87
  79ead2:	e9 cf 00 00 00       	jmp    79eba6 <agentGoProject/common.GetFileSafeInfos+0x526>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:82
  79ead7:	44 0f 11 bc 24 60 01 	movups %xmm15,0x160(%rsp)
  79eade:	00 00 
  79eae0:	44 0f 11 bc 24 70 01 	movups %xmm15,0x170(%rsp)
  79eae7:	00 00 
  79eae9:	48 8d 15 b0 31 05 00 	lea    0x531b0(%rip),%rdx        # 7f1ca0 <type:*+0x2fca0>
  79eaf0:	48 89 94 24 60 01 00 	mov    %rdx,0x160(%rsp)
  79eaf7:	00 
  79eaf8:	48 8d 35 71 14 17 00 	lea    0x171471(%rip),%rsi        # 90ff70 <runtime.buildVersion.str+0xcd0>
  79eaff:	48 89 b4 24 68 01 00 	mov    %rsi,0x168(%rsp)
  79eb06:	00 
  79eb07:	48 8b 41 10          	mov    0x10(%rcx),%rax
  79eb0b:	48 8b 59 18          	mov    0x18(%rcx),%rbx
  79eb0f:	e8 ec c9 c6 ff       	call   40b500 <runtime.convTstring>
  79eb14:	48 8d 0d 85 31 05 00 	lea    0x53185(%rip),%rcx        # 7f1ca0 <type:*+0x2fca0>
  79eb1b:	48 89 8c 24 70 01 00 	mov    %rcx,0x170(%rsp)
  79eb22:	00 
  79eb23:	48 89 84 24 78 01 00 	mov    %rax,0x178(%rsp)
  79eb2a:	00 
  79eb2b:	48 8d 84 24 60 01 00 	lea    0x160(%rsp),%rax
  79eb32:	00 
  79eb33:	bb 02 00 00 00       	mov    $0x2,%ebx
  79eb38:	48 89 d9             	mov    %rbx,%rcx
  79eb3b:	0f 1f 44 00 00       	nopl   0x0(%rax,%rax,1)
  79eb40:	e8 db e3 de ff       	call   58cf20 <log.Println>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:83
  79eb45:	44 0f 11 bc 24 28 01 	movups %xmm15,0x128(%rsp)
  79eb4c:	00 00 
  79eb4e:	c6 44 24 5f 01       	movb   $0x1,0x5f(%rsp)
  79eb53:	48 8b 94 24 88 01 00 	mov    0x188(%rsp),%rdx
  79eb5a:	00 
  79eb5b:	48 8b 0a             	mov    (%rdx),%rcx
  79eb5e:	66 90                	xchg   %ax,%ax
  79eb60:	ff d1                	call   *%rcx
  79eb62:	c6 44 24 5f 00       	movb   $0x0,0x5f(%rsp)
  79eb67:	e8 d4 c9 01 00       	call   7bb540 <agentGoProject/common.GetFileSafeInfos.func1>
  79eb6c:	48 8b 84 24 28 01 00 	mov    0x128(%rsp),%rax
  79eb73:	00 
  79eb74:	48 8b 9c 24 30 01 00 	mov    0x130(%rsp),%rbx
  79eb7b:	00 
  79eb7c:	48 8b ac 24 98 01 00 	mov    0x198(%rsp),%rbp
  79eb83:	00 
  79eb84:	48 81 c4 a0 01 00 00 	add    $0x1a0,%rsp
  79eb8b:	c3                   	ret    
./C:/Program Files/Go/src/container/list/list.go:98
  79eb8c:	48 ff 43 28          	incq   0x28(%rbx)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:87
  79eb90:	48 8b 94 24 80 00 00 	mov    0x80(%rsp),%rdx
  79eb97:	00 
  79eb98:	48 ff c2             	inc    %rdx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:91
  79eb9b:	48 89 d8             	mov    %rbx,%rax
./C:/Program Files/Go/src/container/list/list.go:56
  79eb9e:	48 8b 8c 24 10 01 00 	mov    0x110(%rsp),%rcx
  79eba5:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:87
  79eba6:	48 8b b4 24 20 01 00 	mov    0x120(%rsp),%rsi
  79ebad:	00 
  79ebae:	48 8b 7e 20          	mov    0x20(%rsi),%rdi
  79ebb2:	48 39 56 28          	cmp    %rdx,0x28(%rsi)
  79ebb6:	0f 8e 53 01 00 00    	jle    79ed0f <agentGoProject/common.GetFileSafeInfos+0x68f>
  79ebbc:	48 89 94 24 80 00 00 	mov    %rdx,0x80(%rsp)
  79ebc3:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:88
  79ebc4:	48 8d 0c 52          	lea    (%rdx,%rdx,2),%rcx
  79ebc8:	48 c1 e1 05          	shl    $0x5,%rcx
  79ebcc:	48 8d 1c 0f          	lea    (%rdi,%rcx,1),%rbx
  79ebd0:	48 8d 05 29 dc 0a 00 	lea    0xadc29(%rip),%rax        # 84c800 <type:*+0x8a800>
  79ebd7:	e8 a4 c6 c6 ff       	call   40b280 <runtime.convT>
  79ebdc:	48 89 84 24 08 01 00 	mov    %rax,0x108(%rsp)
  79ebe3:	00 
./C:/Program Files/Go/src/container/list/list.go:151
  79ebe4:	90                   	nop
./C:/Program Files/Go/src/container/list/list.go:86
  79ebe5:	48 8b bc 24 d8 00 00 	mov    0xd8(%rsp),%rdi
  79ebec:	00 
container/list.(*List).lazyInit():
./C:/Program Files/Go/src/container/list/list.go:86
  79ebed:	48 83 3f 00          	cmpq   $0x0,(%rdi)
  79ebf1:	74 05                	je     79ebf8 <agentGoProject/common.GetFileSafeInfos+0x578>
container/list.(*List).PushBack():
./C:/Program Files/Go/src/container/list/list.go:152
  79ebf3:	48 89 f9             	mov    %rdi,%rcx
container/list.(*List).lazyInit():
./C:/Program Files/Go/src/container/list/list.go:86
  79ebf6:	eb 3b                	jmp    79ec33 <agentGoProject/common.GetFileSafeInfos+0x5b3>
./C:/Program Files/Go/src/container/list/list.go:87
  79ebf8:	90                   	nop
container/list.(*List).Init():
./C:/Program Files/Go/src/container/list/list.go:55
  79ebf9:	83 3d 50 69 43 00 00 	cmpl   $0x0,0x436950(%rip)        # bd5550 <runtime.writeBarrier>
  79ec00:	75 14                	jne    79ec16 <agentGoProject/common.GetFileSafeInfos+0x596>
  79ec02:	48 89 3f             	mov    %rdi,(%rdi)
./C:/Program Files/Go/src/container/list/list.go:56
  79ec05:	48 89 7f 08          	mov    %rdi,0x8(%rdi)
./C:/Program Files/Go/src/container/list/list.go:57
  79ec09:	48 89 f9             	mov    %rdi,%rcx
./C:/Program Files/Go/src/container/list/list.go:56
  79ec0c:	48 8b bc 24 10 01 00 	mov    0x110(%rsp),%rdi
  79ec13:	00 
./C:/Program Files/Go/src/container/list/list.go:55
  79ec14:	eb 15                	jmp    79ec2b <agentGoProject/common.GetFileSafeInfos+0x5ab>
  79ec16:	48 89 f9             	mov    %rdi,%rcx
  79ec19:	e8 c2 84 cc ff       	call   4670e0 <runtime.gcWriteBarrierCX>
./C:/Program Files/Go/src/container/list/list.go:56
  79ec1e:	48 8b bc 24 10 01 00 	mov    0x110(%rsp),%rdi
  79ec25:	00 
  79ec26:	e8 b5 84 cc ff       	call   4670e0 <runtime.gcWriteBarrierCX>
./C:/Program Files/Go/src/container/list/list.go:57
  79ec2b:	48 c7 41 28 00 00 00 	movq   $0x0,0x28(%rcx)
  79ec32:	00 
container/list.(*List).PushBack():
./C:/Program Files/Go/src/container/list/list.go:152
  79ec33:	48 8b 51 08          	mov    0x8(%rcx),%rdx
  79ec37:	48 89 94 24 00 01 00 	mov    %rdx,0x100(%rsp)
  79ec3e:	00 
agentGoProject/common.GetFileSafeInfos():
./C:/Program Files/Go/src/container/list/list.go:104
  79ec3f:	48 8d 05 1a 18 0a 00 	lea    0xa181a(%rip),%rax        # 840460 <type:*+0x7e460>
container/list.(*List).insertValue():
./C:/Program Files/Go/src/container/list/list.go:104
  79ec46:	e8 35 f0 c6 ff       	call   40dc80 <runtime.newobject>
  79ec4b:	48 8d 0d ae db 0a 00 	lea    0xadbae(%rip),%rcx        # 84c800 <type:*+0x8a800>
  79ec52:	48 89 48 18          	mov    %rcx,0x18(%rax)
  79ec56:	83 3d f3 68 43 00 00 	cmpl   $0x0,0x4368f3(%rip)        # bd5550 <runtime.writeBarrier>
  79ec5d:	75 0e                	jne    79ec6d <agentGoProject/common.GetFileSafeInfos+0x5ed>
  79ec5f:	48 8b 94 24 08 01 00 	mov    0x108(%rsp),%rdx
  79ec66:	00 
  79ec67:	48 89 50 20          	mov    %rdx,0x20(%rax)
  79ec6b:	eb 11                	jmp    79ec7e <agentGoProject/common.GetFileSafeInfos+0x5fe>
  79ec6d:	48 8d 78 20          	lea    0x20(%rax),%rdi
  79ec71:	48 8b 94 24 08 01 00 	mov    0x108(%rsp),%rdx
  79ec78:	00 
  79ec79:	e8 82 84 cc ff       	call   467100 <runtime.gcWriteBarrierDX>
container/list.(*List).insert():
./C:/Program Files/Go/src/container/list/list.go:93
  79ec7e:	83 3d cb 68 43 00 00 	cmpl   $0x0,0x4368cb(%rip)        # bd5550 <runtime.writeBarrier>
  79ec85:	75 0e                	jne    79ec95 <agentGoProject/common.GetFileSafeInfos+0x615>
  79ec87:	48 8b 94 24 00 01 00 	mov    0x100(%rsp),%rdx
  79ec8e:	00 
  79ec8f:	48 89 50 08          	mov    %rdx,0x8(%rax)
  79ec93:	eb 11                	jmp    79eca6 <agentGoProject/common.GetFileSafeInfos+0x626>
  79ec95:	48 8d 78 08          	lea    0x8(%rax),%rdi
  79ec99:	48 8b 94 24 00 01 00 	mov    0x100(%rsp),%rdx
  79eca0:	00 
  79eca1:	e8 5a 84 cc ff       	call   467100 <runtime.gcWriteBarrierDX>
./C:/Program Files/Go/src/container/list/list.go:94
  79eca6:	48 8b 32             	mov    (%rdx),%rsi
  79eca9:	83 3d a0 68 43 00 00 	cmpl   $0x0,0x4368a0(%rip)        # bd5550 <runtime.writeBarrier>
  79ecb0:	75 08                	jne    79ecba <agentGoProject/common.GetFileSafeInfos+0x63a>
  79ecb2:	48 89 30             	mov    %rsi,(%rax)
./C:/Program Files/Go/src/container/list/list.go:95
  79ecb5:	48 89 02             	mov    %rax,(%rdx)
./C:/Program Files/Go/src/container/list/list.go:94
  79ecb8:	eb 13                	jmp    79eccd <agentGoProject/common.GetFileSafeInfos+0x64d>
  79ecba:	48 89 c7             	mov    %rax,%rdi
  79ecbd:	0f 1f 00             	nopl   (%rax)
  79ecc0:	e8 7b 84 cc ff       	call   467140 <runtime.gcWriteBarrierSI>
./C:/Program Files/Go/src/container/list/list.go:95
  79ecc5:	48 89 d7             	mov    %rdx,%rdi
  79ecc8:	e8 13 83 cc ff       	call   466fe0 <runtime.gcWriteBarrier>
./C:/Program Files/Go/src/container/list/list.go:96
  79eccd:	48 8b 38             	mov    (%rax),%rdi
  79ecd0:	84 07                	test   %al,(%rdi)
  79ecd2:	83 3d 77 68 43 00 00 	cmpl   $0x0,0x436877(%rip)        # bd5550 <runtime.writeBarrier>
  79ecd9:	75 15                	jne    79ecf0 <agentGoProject/common.GetFileSafeInfos+0x670>
  79ecdb:	48 89 47 08          	mov    %rax,0x8(%rdi)
./C:/Program Files/Go/src/container/list/list.go:97
  79ecdf:	48 8b 9c 24 d8 00 00 	mov    0xd8(%rsp),%rbx
  79ece6:	00 
  79ece7:	48 89 58 10          	mov    %rbx,0x10(%rax)
./C:/Program Files/Go/src/container/list/list.go:96
  79eceb:	e9 9c fe ff ff       	jmp    79eb8c <agentGoProject/common.GetFileSafeInfos+0x50c>
  79ecf0:	48 83 c7 08          	add    $0x8,%rdi
  79ecf4:	e8 e7 82 cc ff       	call   466fe0 <runtime.gcWriteBarrier>
./C:/Program Files/Go/src/container/list/list.go:97
  79ecf9:	48 8d 78 10          	lea    0x10(%rax),%rdi
  79ecfd:	48 8b 9c 24 d8 00 00 	mov    0xd8(%rsp),%rbx
  79ed04:	00 
  79ed05:	e8 16 84 cc ff       	call   467120 <runtime.gcWriteBarrierBX>
./C:/Program Files/Go/src/container/list/list.go:96
  79ed0a:	e9 7d fe ff ff       	jmp    79eb8c <agentGoProject/common.GetFileSafeInfos+0x50c>
agentGoProject/common.GetFileSafeInfos():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:91
  79ed0f:	48 8b 0d 22 4a 40 00 	mov    0x404a22(%rip),%rcx        # ba3738 <agentGoProject/common.FileSafeList>
  79ed16:	48 89 c3             	mov    %rax,%rbx
  79ed19:	48 89 c8             	mov    %rcx,%rax
  79ed1c:	0f 1f 40 00          	nopl   0x0(%rax)
  79ed20:	e8 9b 2f 00 00       	call   7a1cc0 <agentGoProject/common.compareList>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:93
  79ed25:	83 3d 24 68 43 00 00 	cmpl   $0x0,0x436824(%rip)        # bd5550 <runtime.writeBarrier>
  79ed2c:	75 11                	jne    79ed3f <agentGoProject/common.GetFileSafeInfos+0x6bf>
  79ed2e:	48 8b 8c 24 d8 00 00 	mov    0xd8(%rsp),%rcx
  79ed35:	00 
  79ed36:	48 89 0d fb 49 40 00 	mov    %rcx,0x4049fb(%rip)        # ba3738 <agentGoProject/common.FileSafeList>
  79ed3d:	eb 14                	jmp    79ed53 <agentGoProject/common.GetFileSafeInfos+0x6d3>
  79ed3f:	48 8d 3d f2 49 40 00 	lea    0x4049f2(%rip),%rdi        # ba3738 <agentGoProject/common.FileSafeList>
  79ed46:	48 8b 8c 24 d8 00 00 	mov    0xd8(%rsp),%rcx
  79ed4d:	00 
  79ed4e:	e8 8d 83 cc ff       	call   4670e0 <runtime.gcWriteBarrierCX>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:95
  79ed53:	e8 48 17 00 00       	call   7a04a0 <agentGoProject/common.refreshDirPathMd5>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:96
  79ed58:	44 0f 11 bc 24 38 01 	movups %xmm15,0x138(%rsp)
  79ed5f:	00 00 
  79ed61:	48 8d 0d 18 01 00 00 	lea    0x118(%rip),%rcx        # 79ee80 <agentGoProject/common.GetFileSafeInfos.func3>
  79ed68:	48 89 8c 24 38 01 00 	mov    %rcx,0x138(%rsp)
  79ed6f:	00 
  79ed70:	48 8d 0d e9 62 43 00 	lea    0x4362e9(%rip),%rcx        # bd5060 <agentGoProject/common.fileSafeGuard>
  79ed77:	48 89 8c 24 40 01 00 	mov    %rcx,0x140(%rsp)
  79ed7e:	00 
  79ed7f:	48 8d 8c 24 38 01 00 	lea    0x138(%rsp),%rcx
  79ed86:	00 
  79ed87:	48 89 8c 24 80 01 00 	mov    %rcx,0x180(%rsp)
  79ed8e:	00 
  79ed8f:	c6 44 24 5f 07       	movb   $0x7,0x5f(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:98
  79ed94:	48 8b 8c 24 20 01 00 	mov    0x120(%rsp),%rcx
  79ed9b:	00 
  79ed9c:	48 8b 41 20          	mov    0x20(%rcx),%rax
  79eda0:	48 8b 59 28          	mov    0x28(%rcx),%rbx
  79eda4:	48 8b 49 30          	mov    0x30(%rcx),%rcx
  79eda8:	48 8b bc 24 f0 00 00 	mov    0xf0(%rsp),%rdi
  79edaf:	00 
  79edb0:	48 8b b4 24 90 00 00 	mov    0x90(%rsp),%rsi
  79edb7:	00 
  79edb8:	e8 23 1c 00 00       	call   7a09e0 <agentGoProject/common.fileSafeDownHandle>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:99
  79edbd:	31 c0                	xor    %eax,%eax
  79edbf:	48 8b 9c 24 c8 00 00 	mov    0xc8(%rsp),%rbx
  79edc6:	00 
  79edc7:	48 8b 4c 24 68       	mov    0x68(%rsp),%rcx
  79edcc:	e8 0f 19 cb ff       	call   4506e0 <runtime.slicebytetostring>
  79edd1:	48 89 84 24 28 01 00 	mov    %rax,0x128(%rsp)
  79edd8:	00 
  79edd9:	48 89 9c 24 30 01 00 	mov    %rbx,0x130(%rsp)
  79ede0:	00 
  79ede1:	c6 44 24 5f 03       	movb   $0x3,0x5f(%rsp)
  79ede6:	48 8b 94 24 80 01 00 	mov    0x180(%rsp),%rdx
  79eded:	00 
  79edee:	48 8b 0a             	mov    (%rdx),%rcx
  79edf1:	ff d1                	call   *%rcx
  79edf3:	c6 44 24 5f 01       	movb   $0x1,0x5f(%rsp)
  79edf8:	48 8b 94 24 88 01 00 	mov    0x188(%rsp),%rdx
  79edff:	00 
  79ee00:	48 8b 0a             	mov    (%rdx),%rcx
  79ee03:	ff d1                	call   *%rcx
  79ee05:	c6 44 24 5f 00       	movb   $0x0,0x5f(%rsp)
  79ee0a:	e8 31 c7 01 00       	call   7bb540 <agentGoProject/common.GetFileSafeInfos.func1>
  79ee0f:	48 8b 84 24 28 01 00 	mov    0x128(%rsp),%rax
  79ee16:	00 
  79ee17:	48 8b 9c 24 30 01 00 	mov    0x130(%rsp),%rbx
  79ee1e:	00 
  79ee1f:	48 8b ac 24 98 01 00 	mov    0x198(%rsp),%rbp
  79ee26:	00 
  79ee27:	48 81 c4 a0 01 00 00 	add    $0x1a0,%rsp
  79ee2e:	c3                   	ret    
  79ee2f:	e8 ec 4e c9 ff       	call   433d20 <runtime.deferreturn>
  79ee34:	48 8b 84 24 28 01 00 	mov    0x128(%rsp),%rax
  79ee3b:	00 
  79ee3c:	48 8b 9c 24 30 01 00 	mov    0x130(%rsp),%rbx
  79ee43:	00 
  79ee44:	48 8b ac 24 98 01 00 	mov    0x198(%rsp),%rbp
  79ee4b:	00 
  79ee4c:	48 81 c4 a0 01 00 00 	add    $0x1a0,%rsp
  79ee53:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:56
  79ee54:	48 89 44 24 08       	mov    %rax,0x8(%rsp)
  79ee59:	48 89 5c 24 10       	mov    %rbx,0x10(%rsp)
  79ee5e:	66 90                	xchg   %ax,%ax
  79ee60:	e8 bb 61 cc ff       	call   465020 <runtime.morestack_noctxt.abi0>
  79ee65:	48 8b 44 24 08       	mov    0x8(%rsp),%rax
  79ee6a:	48 8b 5c 24 10       	mov    0x10(%rsp),%rbx
  79ee6f:	e9 0c f8 ff ff       	jmp    79e680 <agentGoProject/common.GetFileSafeInfos>
  79ee74:	cc                   	int3   
  79ee75:	cc                   	int3   
  79ee76:	cc                   	int3   
  79ee77:	cc                   	int3   
  79ee78:	cc                   	int3   
  79ee79:	cc                   	int3   
  79ee7a:	cc                   	int3   
  79ee7b:	cc                   	int3   
  79ee7c:	cc                   	int3   
  79ee7d:	cc                   	int3   
  79ee7e:	cc                   	int3   
  79ee7f:	cc                   	int3   
