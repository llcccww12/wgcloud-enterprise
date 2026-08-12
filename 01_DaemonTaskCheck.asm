============ 1. 反汇编 DaemonTaskCheck（daemonUtil.go:101）============

/root/agent-linux-amd64-v3.6.8/wgcloud-agent-release:     file format elf64-x86-64


Disassembly of section .text:

000000000079b400 <agentGoProject/common.DaemonTaskCheck>:
agentGoProject/common.DaemonTaskCheck():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/daemonUtil.go:101
  79b400:	49 3b 66 10          	cmp    0x10(%r14),%rsp
  79b404:	0f 86 f0 00 00 00    	jbe    79b4fa <agentGoProject/common.DaemonTaskCheck+0xfa>
  79b40a:	48 83 ec 48          	sub    $0x48,%rsp
  79b40e:	48 89 6c 24 40       	mov    %rbp,0x40(%rsp)
  79b413:	48 8d 6c 24 40       	lea    0x40(%rsp),%rbp
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/daemonUtil.go:102
  79b418:	48 8b 0d 01 8b 40 00 	mov    0x408b01(%rip),%rcx        # ba3f20 <agentGoProject/common.WG_TOKEN>
  79b41f:	48 8b 3d 02 8b 40 00 	mov    0x408b02(%rip),%rdi        # ba3f28 <agentGoProject/common.WG_TOKEN+0x8>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/daemonUtil.go:103
  79b426:	48 8b 05 e3 8a 40 00 	mov    0x408ae3(%rip),%rax        # ba3f10 <agentGoProject/common.SERVER_URL>
  79b42d:	48 8b 1d e4 8a 40 00 	mov    0x408ae4(%rip),%rbx        # ba3f18 <agentGoProject/common.SERVER_URL+0x8>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/daemonUtil.go:104
  79b434:	e8 c7 fd ff ff       	call   79b200 <agentGoProject/common.InitDameonInfo>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/daemonUtil.go:105
  79b439:	e8 e2 fe ff ff       	call   79b320 <agentGoProject/common.CheckDameonToken>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/daemonUtil.go:106
  79b43e:	48 83 3d fa 9b 43 00 	cmpq   $0x0,0x439bfa(%rip)        # bd5040 <agentGoProject/common.DameonErrCount>
  79b445:	00 
  79b446:	7e 55                	jle    79b49d <agentGoProject/common.DaemonTaskCheck+0x9d>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/daemonUtil.go:107
  79b448:	44 0f 11 7c 24 20    	movups %xmm15,0x20(%rsp)
  79b44e:	44 0f 11 7c 24 30    	movups %xmm15,0x30(%rsp)
  79b454:	48 8d 0d 45 68 05 00 	lea    0x56845(%rip),%rcx        # 7f1ca0 <type:*+0x2fca0>
  79b45b:	48 89 4c 24 20       	mov    %rcx,0x20(%rsp)
  79b460:	48 8d 0d 69 4a 17 00 	lea    0x174a69(%rip),%rcx        # 90fed0 <runtime.buildVersion.str+0xc30>
  79b467:	48 89 4c 24 28       	mov    %rcx,0x28(%rsp)
  79b46c:	48 8b 05 cd 9b 43 00 	mov    0x439bcd(%rip),%rax        # bd5040 <agentGoProject/common.DameonErrCount>
  79b473:	e8 08 00 c7 ff       	call   40b480 <runtime.convT64>
  79b478:	48 8d 0d e1 5f 05 00 	lea    0x55fe1(%rip),%rcx        # 7f1460 <type:*+0x2f460>
  79b47f:	48 89 4c 24 30       	mov    %rcx,0x30(%rsp)
  79b484:	48 89 44 24 38       	mov    %rax,0x38(%rsp)
  79b489:	48 8d 44 24 20       	lea    0x20(%rsp),%rax
  79b48e:	bb 02 00 00 00       	mov    $0x2,%ebx
  79b493:	48 89 d9             	mov    %rbx,%rcx
  79b496:	e8 85 1a df ff       	call   58cf20 <log.Println>
  79b49b:	eb 53                	jmp    79b4f0 <agentGoProject/common.DaemonTaskCheck+0xf0>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/daemonUtil.go:109
  79b49d:	44 0f 11 7c 24 20    	movups %xmm15,0x20(%rsp)
  79b4a3:	44 0f 11 7c 24 30    	movups %xmm15,0x30(%rsp)
  79b4a9:	48 8d 0d f0 67 05 00 	lea    0x567f0(%rip),%rcx        # 7f1ca0 <type:*+0x2fca0>
  79b4b0:	48 89 4c 24 20       	mov    %rcx,0x20(%rsp)
  79b4b5:	48 8d 0d 24 4a 17 00 	lea    0x174a24(%rip),%rcx        # 90fee0 <runtime.buildVersion.str+0xc40>
  79b4bc:	48 89 4c 24 28       	mov    %rcx,0x28(%rsp)
  79b4c1:	48 8b 05 78 9b 43 00 	mov    0x439b78(%rip),%rax        # bd5040 <agentGoProject/common.DameonErrCount>
  79b4c8:	e8 b3 ff c6 ff       	call   40b480 <runtime.convT64>
  79b4cd:	48 8d 0d 8c 5f 05 00 	lea    0x55f8c(%rip),%rcx        # 7f1460 <type:*+0x2f460>
  79b4d4:	48 89 4c 24 30       	mov    %rcx,0x30(%rsp)
  79b4d9:	48 89 44 24 38       	mov    %rax,0x38(%rsp)
  79b4de:	48 8d 44 24 20       	lea    0x20(%rsp),%rax
  79b4e3:	bb 02 00 00 00       	mov    $0x2,%ebx
  79b4e8:	48 89 d9             	mov    %rbx,%rcx
  79b4eb:	e8 30 1a df ff       	call   58cf20 <log.Println>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/daemonUtil.go:111
  79b4f0:	48 8b 6c 24 40       	mov    0x40(%rsp),%rbp
  79b4f5:	48 83 c4 48          	add    $0x48,%rsp
  79b4f9:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/daemonUtil.go:101
  79b4fa:	e8 21 9b cc ff       	call   465020 <runtime.morestack_noctxt.abi0>
  79b4ff:	90                   	nop
  79b500:	e9 fb fe ff ff       	jmp    79b400 <agentGoProject/common.DaemonTaskCheck>
  79b505:	cc                   	int3   
  79b506:	cc                   	int3   
  79b507:	cc                   	int3   
  79b508:	cc                   	int3   
  79b509:	cc                   	int3   
  79b50a:	cc                   	int3   
  79b50b:	cc                   	int3   
  79b50c:	cc                   	int3   
  79b50d:	cc                   	int3   
  79b50e:	cc                   	int3   
  79b50f:	cc                   	int3   
  79b510:	cc                   	int3   
  79b511:	cc                   	int3   
  79b512:	cc                   	int3   
  79b513:	cc                   	int3   
  79b514:	cc                   	int3   
  79b515:	cc                   	int3   
  79b516:	cc                   	int3   
  79b517:	cc                   	int3   
  79b518:	cc                   	int3   
  79b519:	cc                   	int3   
  79b51a:	cc                   	int3   
  79b51b:	cc                   	int3   
  79b51c:	cc                   	int3   
  79b51d:	cc                   	int3   
  79b51e:	cc                   	int3   
  79b51f:	cc                   	int3   

000000000079b520 <agentGoProject/common.GetDockerInfos>:
agentGoProject/common.GetDockerInfos():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:58
  79b520:	4c 8d a4 24 08 ff ff 	lea    -0xf8(%rsp),%r12
  79b527:	ff 
  79b528:	4d 3b 66 10          	cmp    0x10(%r14),%r12
  79b52c:	0f 86 46 06 00 00    	jbe    79bb78 <agentGoProject/common.GetDockerInfos+0x658>
  79b532:	48 81 ec 78 01 00 00 	sub    $0x178,%rsp
  79b539:	48 89 ac 24 70 01 00 	mov    %rbp,0x170(%rsp)
  79b540:	00 
  79b541:	48 8d ac 24 70 01 00 	lea    0x170(%rsp),%rbp
  79b548:	00 
  79b549:	44 0f 11 bc 24 58 01 	movups %xmm15,0x158(%rsp)
  79b550:	00 00 
  79b552:	44 0f 11 bc 24 60 01 	movups %xmm15,0x160(%rsp)
  79b559:	00 00 
  79b55b:	48 89 9c 24 88 01 00 	mov    %rbx,0x188(%rsp)
  79b562:	00 
  79b563:	48 89 84 24 80 01 00 	mov    %rax,0x180(%rsp)
  79b56a:	00 
  79b56b:	c6 44 24 5f 00       	movb   $0x0,0x5f(%rsp)
  79b570:	44 0f 11 bc 24 00 01 	movups %xmm15,0x100(%rsp)
  79b577:	00 00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:59
  79b579:	48 8d 0d 80 ba 10 00 	lea    0x10ba80(%rip),%rcx        # 8a7000 <go:func.*+0x920>
  79b580:	48 89 8c 24 68 01 00 	mov    %rcx,0x168(%rsp)
  79b587:	00 
  79b588:	c6 44 24 5f 01       	movb   $0x1,0x5f(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:69
  79b58d:	48 8b 0d 8c 89 40 00 	mov    0x40898c(%rip),%rcx        # ba3f20 <agentGoProject/common.WG_TOKEN>
  79b594:	48 89 8c 24 b8 00 00 	mov    %rcx,0xb8(%rsp)
  79b59b:	00 
  79b59c:	48 8b 15 85 89 40 00 	mov    0x408985(%rip),%rdx        # ba3f28 <agentGoProject/common.WG_TOKEN+0x8>
  79b5a3:	48 89 54 24 60       	mov    %rdx,0x60(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:70
  79b5a8:	48 8b 35 11 89 40 00 	mov    0x408911(%rip),%rsi        # ba3ec0 <agentGoProject/common.BIND_IP>
  79b5af:	48 89 b4 24 e0 00 00 	mov    %rsi,0xe0(%rsp)
  79b5b6:	00 
  79b5b7:	48 8b 3d 0a 89 40 00 	mov    0x40890a(%rip),%rdi        # ba3ec8 <agentGoProject/common.BIND_IP+0x8>
  79b5be:	48 89 bc 24 88 00 00 	mov    %rdi,0x88(%rsp)
  79b5c5:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:71
  79b5c6:	90                   	nop
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/commonFuncs.go:192
  79b5c7:	48 8d 05 92 cf 0c 00 	lea    0xccf92(%rip),%rax        # 868560 <type:*+0xa6560>
agentGoProject/common.GetHttpClient():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/commonFuncs.go:192
  79b5ce:	e8 ad 26 c7 ff       	call   40dc80 <runtime.newobject>
  79b5d3:	48 89 84 24 c0 00 00 	mov    %rax,0xc0(%rsp)
  79b5da:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/commonFuncs.go:193
  79b5db:	48 8d 05 3e b0 0c 00 	lea    0xcb03e(%rip),%rax        # 866620 <type:*+0xa4620>
  79b5e2:	e8 99 26 c7 ff       	call   40dc80 <runtime.newobject>
  79b5e7:	c6 80 a0 00 00 00 01 	movb   $0x1,0xa0(%rax)
  79b5ee:	83 3d 5b 9f 43 00 00 	cmpl   $0x0,0x439f5b(%rip)        # bd5550 <runtime.writeBarrier>
  79b5f5:	75 11                	jne    79b608 <agentGoProject/common.GetDockerInfos+0xe8>
  79b5f7:	48 8b 8c 24 c0 00 00 	mov    0xc0(%rsp),%rcx
  79b5fe:	00 
  79b5ff:	48 89 81 a0 00 00 00 	mov    %rax,0xa0(%rcx)
  79b606:	eb 20                	jmp    79b628 <agentGoProject/common.GetDockerInfos+0x108>
  79b608:	48 8b bc 24 c0 00 00 	mov    0xc0(%rsp),%rdi
  79b60f:	00 
  79b610:	48 8d 8f a0 00 00 00 	lea    0xa0(%rdi),%rcx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/commonFuncs.go:192
  79b617:	48 89 fa             	mov    %rdi,%rdx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/commonFuncs.go:193
  79b61a:	48 89 cf             	mov    %rcx,%rdi
  79b61d:	0f 1f 00             	nopl   (%rax)
  79b620:	e8 bb b9 cc ff       	call   466fe0 <runtime.gcWriteBarrier>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/commonFuncs.go:194
  79b625:	48 89 d1             	mov    %rdx,%rcx
  79b628:	48 c7 81 b8 00 00 00 	movq   $0x1e,0xb8(%rcx)
  79b62f:	1e 00 00 00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/commonFuncs.go:195
  79b633:	48 ba 00 58 47 f8 0d 	movabs $0xdf8475800,%rdx
  79b63a:	00 00 00 
  79b63d:	48 89 91 d0 00 00 00 	mov    %rdx,0xd0(%rcx)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/commonFuncs.go:199
  79b644:	48 8d 05 d5 75 0a 00 	lea    0xa75d5(%rip),%rax        # 842c20 <type:*+0x80c20>
  79b64b:	e8 30 26 c7 ff       	call   40dc80 <runtime.newobject>
  79b650:	48 89 84 24 e8 00 00 	mov    %rax,0xe8(%rsp)
  79b657:	00 
  79b658:	48 b9 00 c8 17 a8 04 	movabs $0x4a817c800,%rcx
  79b65f:	00 00 00 
  79b662:	48 89 48 28          	mov    %rcx,0x28(%rax)
  79b666:	48 8d 0d b3 69 17 00 	lea    0x1769b3(%rip),%rcx        # 912020 <go:itab.*net/http.Transport,net/http.RoundTripper>
  79b66d:	48 89 08             	mov    %rcx,(%rax)
  79b670:	83 3d d9 9e 43 00 00 	cmpl   $0x0,0x439ed9(%rip)        # bd5550 <runtime.writeBarrier>
  79b677:	75 0e                	jne    79b687 <agentGoProject/common.GetDockerInfos+0x167>
  79b679:	48 8b 94 24 c0 00 00 	mov    0xc0(%rsp),%rdx
  79b680:	00 
  79b681:	48 89 50 08          	mov    %rdx,0x8(%rax)
  79b685:	eb 11                	jmp    79b698 <agentGoProject/common.GetDockerInfos+0x178>
  79b687:	48 8d 78 08          	lea    0x8(%rax),%rdi
  79b68b:	48 8b 94 24 c0 00 00 	mov    0xc0(%rsp),%rdx
  79b692:	00 
  79b693:	e8 68 ba cc ff       	call   467100 <runtime.gcWriteBarrierDX>
  79b698:	48 8d 15 b5 81 0d 00 	lea    0xd81b5(%rip),%rdx        # 873854 <go:string.*+0x628c>
  79b69f:	48 89 14 24          	mov    %rdx,(%rsp)
  79b6a3:	48 c7 44 24 08 12 00 	movq   $0x12,0x8(%rsp)
  79b6aa:	00 00 
agentGoProject/common.GetDockerInfos():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:72
  79b6ac:	31 c0                	xor    %eax,%eax
  79b6ae:	48 8d 1d 44 5b 0d 00 	lea    0xd5b44(%rip),%rbx        # 8711f9 <go:string.*+0x3c31>
  79b6b5:	b9 0c 00 00 00       	mov    $0xc,%ecx
  79b6ba:	48 8b bc 24 b8 00 00 	mov    0xb8(%rsp),%rdi
  79b6c1:	00 
  79b6c2:	48 8b 74 24 60       	mov    0x60(%rsp),%rsi
  79b6c7:	4c 8d 05 45 64 0d 00 	lea    0xd6445(%rip),%r8        # 871b13 <go:string.*+0x454b>
  79b6ce:	41 b9 0e 00 00 00    	mov    $0xe,%r9d
  79b6d4:	4c 8b 94 24 e0 00 00 	mov    0xe0(%rsp),%r10
  79b6db:	00 
  79b6dc:	4c 8b 9c 24 88 00 00 	mov    0x88(%rsp),%r11
  79b6e3:	00 
  79b6e4:	e8 d7 4e cb ff       	call   4505c0 <runtime.concatstring5>
  79b6e9:	48 89 d9             	mov    %rbx,%rcx
  79b6ec:	48 89 c3             	mov    %rax,%rbx
  79b6ef:	31 c0                	xor    %eax,%eax
  79b6f1:	e8 ea 51 cb ff       	call   4508e0 <runtime.stringtoslicebyte>
./C:/Program Files/Go/src/bytes/buffer.go:463
  79b6f6:	48 89 84 24 d8 00 00 	mov    %rax,0xd8(%rsp)
  79b6fd:	00 
bytes.NewBuffer():
./C:/Program Files/Go/src/bytes/buffer.go:463
  79b6fe:	48 89 5c 24 70       	mov    %rbx,0x70(%rsp)
  79b703:	48 89 4c 24 78       	mov    %rcx,0x78(%rsp)
  79b708:	48 8d 05 11 58 09 00 	lea    0x95811(%rip),%rax        # 830f20 <type:*+0x6ef20>
  79b70f:	e8 6c 25 c7 ff       	call   40dc80 <runtime.newobject>
  79b714:	48 8b 54 24 70       	mov    0x70(%rsp),%rdx
  79b719:	48 89 50 08          	mov    %rdx,0x8(%rax)
  79b71d:	48 8b 54 24 78       	mov    0x78(%rsp),%rdx
  79b722:	48 89 50 10          	mov    %rdx,0x10(%rax)
  79b726:	83 3d 23 9e 43 00 00 	cmpl   $0x0,0x439e23(%rip)        # bd5550 <runtime.writeBarrier>
  79b72d:	75 0d                	jne    79b73c <agentGoProject/common.GetDockerInfos+0x21c>
  79b72f:	48 8b 94 24 d8 00 00 	mov    0xd8(%rsp),%rdx
  79b736:	00 
  79b737:	48 89 10             	mov    %rdx,(%rax)
  79b73a:	eb 10                	jmp    79b74c <agentGoProject/common.GetDockerInfos+0x22c>
  79b73c:	48 89 c7             	mov    %rax,%rdi
  79b73f:	48 8b 94 24 d8 00 00 	mov    0xd8(%rsp),%rdx
  79b746:	00 
  79b747:	e8 b4 b9 cc ff       	call   467100 <runtime.gcWriteBarrierDX>
agentGoProject/common.GetDockerInfos():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:73
  79b74c:	48 8b 9c 24 80 01 00 	mov    0x180(%rsp),%rbx
  79b753:	00 
  79b754:	48 8b 8c 24 88 01 00 	mov    0x188(%rsp),%rcx
  79b75b:	00 
  79b75c:	48 8d 3d 7c 71 0d 00 	lea    0xd717c(%rip),%rdi        # 8728df <go:string.*+0x5317>
  79b763:	be 10 00 00 00       	mov    $0x10,%esi
  79b768:	4c 8d 05 71 61 17 00 	lea    0x176171(%rip),%r8        # 9118e0 <go:itab.*bytes.Buffer,io.Reader>
  79b76f:	49 89 c1             	mov    %rax,%r9
  79b772:	48 8b 84 24 e8 00 00 	mov    0xe8(%rsp),%rax
  79b779:	00 
  79b77a:	e8 41 51 f5 ff       	call   6f08c0 <net/http.(*Client).Post>
  79b77f:	48 89 84 24 d0 00 00 	mov    %rax,0xd0(%rsp)
  79b786:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:74
  79b787:	48 85 db             	test   %rbx,%rbx
  79b78a:	74 63                	je     79b7ef <agentGoProject/common.GetDockerInfos+0x2cf>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:75
  79b78c:	44 0f 11 bc 24 38 01 	movups %xmm15,0x138(%rsp)
  79b793:	00 00 
  79b795:	44 0f 11 bc 24 48 01 	movups %xmm15,0x148(%rsp)
  79b79c:	00 00 
  79b79e:	48 8d 15 fb 64 05 00 	lea    0x564fb(%rip),%rdx        # 7f1ca0 <type:*+0x2fca0>
  79b7a5:	48 89 94 24 38 01 00 	mov    %rdx,0x138(%rsp)
  79b7ac:	00 
  79b7ad:	48 8d 35 3c 47 17 00 	lea    0x17473c(%rip),%rsi        # 90fef0 <runtime.buildVersion.str+0xc50>
  79b7b4:	48 89 b4 24 40 01 00 	mov    %rsi,0x140(%rsp)
  79b7bb:	00 
  79b7bc:	74 04                	je     79b7c2 <agentGoProject/common.GetDockerInfos+0x2a2>
  79b7be:	48 8b 5b 08          	mov    0x8(%rbx),%rbx
  79b7c2:	48 89 9c 24 48 01 00 	mov    %rbx,0x148(%rsp)
  79b7c9:	00 
  79b7ca:	48 89 8c 24 50 01 00 	mov    %rcx,0x150(%rsp)
  79b7d1:	00 
  79b7d2:	48 8d 84 24 38 01 00 	lea    0x138(%rsp),%rax
  79b7d9:	00 
  79b7da:	bb 02 00 00 00       	mov    $0x2,%ebx
  79b7df:	48 89 d9             	mov    %rbx,%rcx
  79b7e2:	e8 39 17 df ff       	call   58cf20 <log.Println>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:77
  79b7e7:	48 8b 84 24 d0 00 00 	mov    0xd0(%rsp),%rax
  79b7ee:	00 
  79b7ef:	48 8b 48 40          	mov    0x40(%rax),%rcx
  79b7f3:	84 01                	test   %al,(%rcx)
  79b7f5:	48 8b 50 48          	mov    0x48(%rax),%rdx
  79b7f9:	48 c7 84 24 20 01 00 	movq   $0x0,0x120(%rsp)
  79b800:	00 00 00 00 00 
  79b805:	44 0f 11 bc 24 28 01 	movups %xmm15,0x128(%rsp)
  79b80c:	00 00 
  79b80e:	48 8d 1d eb 03 00 00 	lea    0x3eb(%rip),%rbx        # 79bc00 <agentGoProject/common.GetDockerInfos.func2>
  79b815:	48 89 9c 24 20 01 00 	mov    %rbx,0x120(%rsp)
  79b81c:	00 
  79b81d:	48 89 8c 24 28 01 00 	mov    %rcx,0x128(%rsp)
  79b824:	00 
  79b825:	48 89 94 24 30 01 00 	mov    %rdx,0x130(%rsp)
  79b82c:	00 
  79b82d:	48 8d 8c 24 20 01 00 	lea    0x120(%rsp),%rcx
  79b834:	00 
  79b835:	48 89 8c 24 60 01 00 	mov    %rcx,0x160(%rsp)
  79b83c:	00 
  79b83d:	c6 44 24 5f 03       	movb   $0x3,0x5f(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:78
  79b842:	48 8d 05 97 4e 09 00 	lea    0x94e97(%rip),%rax        # 8306e0 <type:*+0x6e6e0>
  79b849:	e8 32 24 c7 ff       	call   40dc80 <runtime.newobject>
  79b84e:	48 89 84 24 f8 00 00 	mov    %rax,0xf8(%rsp)
  79b855:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:79
  79b856:	48 8b 8c 24 d0 00 00 	mov    0xd0(%rsp),%rcx
  79b85d:	00 
  79b85e:	48 8b 59 40          	mov    0x40(%rcx),%rbx
  79b862:	48 8b 49 48          	mov    0x48(%rcx),%rcx
  79b866:	48 89 8c 24 f0 00 00 	mov    %rcx,0xf0(%rsp)
  79b86d:	00 
  79b86e:	48 8d 05 eb 91 07 00 	lea    0x791eb(%rip),%rax        # 814a60 <type:*+0x52a60>
  79b875:	e8 e6 fd c6 ff       	call   40b660 <runtime.convI2I>
  79b87a:	48 8b 9c 24 f0 00 00 	mov    0xf0(%rsp),%rbx
  79b881:	00 
  79b882:	e8 79 7c cd ff       	call   473500 <io.ReadAll>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:80
  79b887:	48 89 84 24 c8 00 00 	mov    %rax,0xc8(%rsp)
  79b88e:	00 
  79b88f:	48 89 5c 24 68       	mov    %rbx,0x68(%rsp)
  79b894:	48 89 d9             	mov    %rbx,%rcx
  79b897:	48 89 c3             	mov    %rax,%rbx
  79b89a:	48 8d 84 24 98 00 00 	lea    0x98(%rsp),%rax
  79b8a1:	00 
  79b8a2:	e8 39 4e cb ff       	call   4506e0 <runtime.slicebytetostring>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:81
  79b8a7:	48 89 d9             	mov    %rbx,%rcx
  79b8aa:	48 89 c3             	mov    %rax,%rbx
  79b8ad:	31 c0                	xor    %eax,%eax
  79b8af:	e8 2c 50 cb ff       	call   4508e0 <runtime.stringtoslicebyte>
  79b8b4:	48 8d 3d c5 61 04 00 	lea    0x461c5(%rip),%rdi        # 7e1a80 <type:*+0x1fa80>
  79b8bb:	48 8b b4 24 f8 00 00 	mov    0xf8(%rsp),%rsi
  79b8c2:	00 
  79b8c3:	e8 98 4d d6 ff       	call   500660 <encoding/json.Unmarshal>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:82
  79b8c8:	48 8b 8c 24 f8 00 00 	mov    0xf8(%rsp),%rcx
  79b8cf:	00 
  79b8d0:	48 8b 11             	mov    (%rcx),%rdx
  79b8d3:	48 83 79 08 01       	cmpq   $0x1,0x8(%rcx)
  79b8d8:	75 55                	jne    79b92f <agentGoProject/common.GetDockerInfos+0x40f>
  79b8da:	80 3a 30             	cmpb   $0x30,(%rdx)
  79b8dd:	75 50                	jne    79b92f <agentGoProject/common.GetDockerInfos+0x40f>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:86
  79b8df:	90                   	nop
./C:/Program Files/Go/src/sync/mutex.go:83
  79b8e0:	31 c0                	xor    %eax,%eax
sync.(*Mutex).Lock():
./C:/Program Files/Go/src/sync/mutex.go:83
  79b8e2:	48 8d 15 6f 97 43 00 	lea    0x43976f(%rip),%rdx        # bd5058 <agentGoProject/common.dockerIdGuard>
  79b8e9:	bb 01 00 00 00       	mov    $0x1,%ebx
  79b8ee:	f0 0f b1 1a          	lock cmpxchg %ebx,(%rdx)
  79b8f2:	0f 94 c2             	sete   %dl
  79b8f5:	84 d2                	test   %dl,%dl
  79b8f7:	75 0c                	jne    79b905 <agentGoProject/common.GetDockerInfos+0x3e5>
./C:/Program Files/Go/src/sync/mutex.go:90
  79b8f9:	48 8d 05 58 97 43 00 	lea    0x439758(%rip),%rax        # bd5058 <agentGoProject/common.dockerIdGuard>
  79b900:	e8 3b 4e cd ff       	call   470740 <sync.(*Mutex).lockSlow>
agentGoProject/common.GetDockerInfos():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:87
  79b905:	e8 b6 2e c7 ff       	call   40e7c0 <runtime.makemap_small>
  79b90a:	83 3d 3f 9c 43 00 00 	cmpl   $0x0,0x439c3f(%rip)        # bd5550 <runtime.writeBarrier>
  79b911:	75 09                	jne    79b91c <agentGoProject/common.GetDockerInfos+0x3fc>
  79b913:	48 89 05 16 7e 40 00 	mov    %rax,0x407e16(%rip)        # ba3730 <agentGoProject/common.DockerIdMap>
  79b91a:	eb 0c                	jmp    79b928 <agentGoProject/common.GetDockerInfos+0x408>
  79b91c:	48 8d 3d 0d 7e 40 00 	lea    0x407e0d(%rip),%rdi        # ba3730 <agentGoProject/common.DockerIdMap>
  79b923:	e8 b8 b6 cc ff       	call   466fe0 <runtime.gcWriteBarrier>
  79b928:	31 c0                	xor    %eax,%eax
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:88
  79b92a:	e9 b9 00 00 00       	jmp    79b9e8 <agentGoProject/common.GetDockerInfos+0x4c8>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:83
  79b92f:	44 0f 11 bc 24 38 01 	movups %xmm15,0x138(%rsp)
  79b936:	00 00 
  79b938:	44 0f 11 bc 24 48 01 	movups %xmm15,0x148(%rsp)
  79b93f:	00 00 
  79b941:	48 8d 15 58 63 05 00 	lea    0x56358(%rip),%rdx        # 7f1ca0 <type:*+0x2fca0>
  79b948:	48 89 94 24 38 01 00 	mov    %rdx,0x138(%rsp)
  79b94f:	00 
  79b950:	48 8d 35 99 45 17 00 	lea    0x174599(%rip),%rsi        # 90fef0 <runtime.buildVersion.str+0xc50>
  79b957:	48 89 b4 24 40 01 00 	mov    %rsi,0x140(%rsp)
  79b95e:	00 
  79b95f:	48 8b 41 10          	mov    0x10(%rcx),%rax
  79b963:	48 8b 59 18          	mov    0x18(%rcx),%rbx
  79b967:	e8 94 fb c6 ff       	call   40b500 <runtime.convTstring>
  79b96c:	48 8d 0d 2d 63 05 00 	lea    0x5632d(%rip),%rcx        # 7f1ca0 <type:*+0x2fca0>
  79b973:	48 89 8c 24 48 01 00 	mov    %rcx,0x148(%rsp)
  79b97a:	00 
  79b97b:	48 89 84 24 50 01 00 	mov    %rax,0x150(%rsp)
  79b982:	00 
  79b983:	48 8d 84 24 38 01 00 	lea    0x138(%rsp),%rax
  79b98a:	00 
  79b98b:	bb 02 00 00 00       	mov    $0x2,%ebx
  79b990:	48 89 d9             	mov    %rbx,%rcx
  79b993:	e8 88 15 df ff       	call   58cf20 <log.Println>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:84
  79b998:	44 0f 11 bc 24 00 01 	movups %xmm15,0x100(%rsp)
  79b99f:	00 00 
  79b9a1:	c6 44 24 5f 01       	movb   $0x1,0x5f(%rsp)
  79b9a6:	48 8b 94 24 60 01 00 	mov    0x160(%rsp),%rdx
  79b9ad:	00 
  79b9ae:	48 8b 0a             	mov    (%rdx),%rcx
  79b9b1:	ff d1                	call   *%rcx
  79b9b3:	c6 44 24 5f 00       	movb   $0x0,0x5f(%rsp)
  79b9b8:	e8 03 fd 01 00       	call   7bb6c0 <agentGoProject/common.GetDockerInfos.func1>
  79b9bd:	48 8b 84 24 00 01 00 	mov    0x100(%rsp),%rax
  79b9c4:	00 
  79b9c5:	48 8b 9c 24 08 01 00 	mov    0x108(%rsp),%rbx
  79b9cc:	00 
  79b9cd:	48 8b ac 24 70 01 00 	mov    0x170(%rsp),%rbp
  79b9d4:	00 
  79b9d5:	48 81 c4 78 01 00 00 	add    $0x178,%rsp
  79b9dc:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:88
  79b9dd:	48 8b 84 24 80 00 00 	mov    0x80(%rsp),%rax
  79b9e4:	00 
  79b9e5:	48 ff c0             	inc    %rax
  79b9e8:	48 8b 94 24 f8 00 00 	mov    0xf8(%rsp),%rdx
  79b9ef:	00 
  79b9f0:	4c 8b 52 20          	mov    0x20(%rdx),%r10
  79b9f4:	48 39 42 28          	cmp    %rax,0x28(%rdx)
  79b9f8:	0f 8e a7 00 00 00    	jle    79baa5 <agentGoProject/common.GetDockerInfos+0x585>
  79b9fe:	48 89 84 24 80 00 00 	mov    %rax,0x80(%rsp)
  79ba05:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:89
  79ba06:	48 8d 14 40          	lea    (%rax,%rax,2),%rdx
  79ba0a:	48 c1 e2 04          	shl    $0x4,%rdx
  79ba0e:	49 8b 4c 12 08       	mov    0x8(%r10,%rdx,1),%rcx
  79ba13:	49 8b 1c 12          	mov    (%r10,%rdx,1),%rbx
  79ba17:	4d 8b 4c 12 28       	mov    0x28(%r10,%rdx,1),%r9
  79ba1c:	4d 8b 44 12 20       	mov    0x20(%r10,%rdx,1),%r8
  79ba21:	4d 8b 5c 12 10       	mov    0x10(%r10,%rdx,1),%r11
  79ba26:	4c 89 9c 24 f0 00 00 	mov    %r11,0xf0(%rsp)
  79ba2d:	00 
  79ba2e:	49 8b 54 12 18       	mov    0x18(%r10,%rdx,1),%rdx
  79ba33:	48 89 94 24 90 00 00 	mov    %rdx,0x90(%rsp)
  79ba3a:	00 
  79ba3b:	48 8d 3d f6 1c 17 00 	lea    0x171cf6(%rip),%rdi        # 90d738 <runtime.gcbits.*+0x2d8>
  79ba42:	be 01 00 00 00       	mov    $0x1,%esi
  79ba47:	31 c0                	xor    %eax,%eax
  79ba49:	e8 92 49 cb ff       	call   4503e0 <runtime.concatstring3>
  79ba4e:	48 8b 15 db 7c 40 00 	mov    0x407cdb(%rip),%rdx        # ba3730 <agentGoProject/common.DockerIdMap>
  79ba55:	48 89 c1             	mov    %rax,%rcx
  79ba58:	48 89 df             	mov    %rbx,%rdi
  79ba5b:	48 8d 05 9e f3 06 00 	lea    0x6f39e(%rip),%rax        # 80ae00 <type:*+0x48e00>
  79ba62:	48 89 d3             	mov    %rdx,%rbx
  79ba65:	e8 96 7a c7 ff       	call   413500 <runtime.mapassign_faststr>
  79ba6a:	48 8b 94 24 90 00 00 	mov    0x90(%rsp),%rdx
  79ba71:	00 
  79ba72:	48 89 50 08          	mov    %rdx,0x8(%rax)
  79ba76:	83 3d d3 9a 43 00 00 	cmpl   $0x0,0x439ad3(%rip)        # bd5550 <runtime.writeBarrier>
  79ba7d:	75 10                	jne    79ba8f <agentGoProject/common.GetDockerInfos+0x56f>
  79ba7f:	48 8b 94 24 f0 00 00 	mov    0xf0(%rsp),%rdx
  79ba86:	00 
  79ba87:	48 89 10             	mov    %rdx,(%rax)
  79ba8a:	e9 4e ff ff ff       	jmp    79b9dd <agentGoProject/common.GetDockerInfos+0x4bd>
  79ba8f:	48 89 c7             	mov    %rax,%rdi
  79ba92:	48 8b 94 24 f0 00 00 	mov    0xf0(%rsp),%rdx
  79ba99:	00 
  79ba9a:	e8 61 b6 cc ff       	call   467100 <runtime.gcWriteBarrierDX>
  79ba9f:	90                   	nop
  79baa0:	e9 38 ff ff ff       	jmp    79b9dd <agentGoProject/common.GetDockerInfos+0x4bd>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:91
  79baa5:	44 0f 11 bc 24 10 01 	movups %xmm15,0x110(%rsp)
  79baac:	00 00 
  79baae:	48 8d 15 eb 00 00 00 	lea    0xeb(%rip),%rdx        # 79bba0 <agentGoProject/common.GetDockerInfos.func3>
  79bab5:	48 89 94 24 10 01 00 	mov    %rdx,0x110(%rsp)
  79babc:	00 
  79babd:	48 8d 15 94 95 43 00 	lea    0x439594(%rip),%rdx        # bd5058 <agentGoProject/common.dockerIdGuard>
  79bac4:	48 89 94 24 18 01 00 	mov    %rdx,0x118(%rsp)
  79bacb:	00 
  79bacc:	48 8d 94 24 10 01 00 	lea    0x110(%rsp),%rdx
  79bad3:	00 
  79bad4:	48 89 94 24 58 01 00 	mov    %rdx,0x158(%rsp)
  79badb:	00 
  79badc:	c6 44 24 5f 07       	movb   $0x7,0x5f(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:92
  79bae1:	31 c0                	xor    %eax,%eax
  79bae3:	48 8b 9c 24 c8 00 00 	mov    0xc8(%rsp),%rbx
  79baea:	00 
  79baeb:	48 8b 4c 24 68       	mov    0x68(%rsp),%rcx
  79baf0:	e8 eb 4b cb ff       	call   4506e0 <runtime.slicebytetostring>
  79baf5:	48 89 84 24 00 01 00 	mov    %rax,0x100(%rsp)
  79bafc:	00 
  79bafd:	48 89 9c 24 08 01 00 	mov    %rbx,0x108(%rsp)
  79bb04:	00 
  79bb05:	c6 44 24 5f 03       	movb   $0x3,0x5f(%rsp)
  79bb0a:	48 8b 94 24 58 01 00 	mov    0x158(%rsp),%rdx
  79bb11:	00 
  79bb12:	48 8b 32             	mov    (%rdx),%rsi
  79bb15:	ff d6                	call   *%rsi
  79bb17:	c6 44 24 5f 01       	movb   $0x1,0x5f(%rsp)
  79bb1c:	48 8b 94 24 60 01 00 	mov    0x160(%rsp),%rdx
  79bb23:	00 
  79bb24:	48 8b 32             	mov    (%rdx),%rsi
  79bb27:	ff d6                	call   *%rsi
  79bb29:	c6 44 24 5f 00       	movb   $0x0,0x5f(%rsp)
  79bb2e:	e8 8d fb 01 00       	call   7bb6c0 <agentGoProject/common.GetDockerInfos.func1>
  79bb33:	48 8b 84 24 00 01 00 	mov    0x100(%rsp),%rax
  79bb3a:	00 
  79bb3b:	48 8b 9c 24 08 01 00 	mov    0x108(%rsp),%rbx
  79bb42:	00 
  79bb43:	48 8b ac 24 70 01 00 	mov    0x170(%rsp),%rbp
  79bb4a:	00 
  79bb4b:	48 81 c4 78 01 00 00 	add    $0x178,%rsp
  79bb52:	c3                   	ret    
  79bb53:	e8 c8 81 c9 ff       	call   433d20 <runtime.deferreturn>
  79bb58:	48 8b 84 24 00 01 00 	mov    0x100(%rsp),%rax
  79bb5f:	00 
  79bb60:	48 8b 9c 24 08 01 00 	mov    0x108(%rsp),%rbx
  79bb67:	00 
  79bb68:	48 8b ac 24 70 01 00 	mov    0x170(%rsp),%rbp
  79bb6f:	00 
  79bb70:	48 81 c4 78 01 00 00 	add    $0x178,%rsp
  79bb77:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:58
  79bb78:	48 89 44 24 08       	mov    %rax,0x8(%rsp)
  79bb7d:	48 89 5c 24 10       	mov    %rbx,0x10(%rsp)
  79bb82:	e8 99 94 cc ff       	call   465020 <runtime.morestack_noctxt.abi0>
  79bb87:	48 8b 44 24 08       	mov    0x8(%rsp),%rax
  79bb8c:	48 8b 5c 24 10       	mov    0x10(%rsp),%rbx
  79bb91:	e9 8a f9 ff ff       	jmp    79b520 <agentGoProject/common.GetDockerInfos>
  79bb96:	cc                   	int3   
  79bb97:	cc                   	int3   
  79bb98:	cc                   	int3   
  79bb99:	cc                   	int3   
  79bb9a:	cc                   	int3   
  79bb9b:	cc                   	int3   
  79bb9c:	cc                   	int3   
  79bb9d:	cc                   	int3   
  79bb9e:	cc                   	int3   
  79bb9f:	cc                   	int3   

000000000079bba0 <agentGoProject/common.GetDockerInfos.func3>:
agentGoProject/common.GetDockerInfos.func3():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:91
  79bba0:	49 3b 66 10          	cmp    0x10(%r14),%rsp
  79bba4:	76 2a                	jbe    79bbd0 <agentGoProject/common.GetDockerInfos.func3+0x30>
  79bba6:	48 83 ec 10          	sub    $0x10,%rsp
  79bbaa:	48 89 6c 24 08       	mov    %rbp,0x8(%rsp)
  79bbaf:	48 8d 6c 24 08       	lea    0x8(%rsp),%rbp
  79bbb4:	4d 8b 66 20          	mov    0x20(%r14),%r12
  79bbb8:	4d 85 e4             	test   %r12,%r12
  79bbbb:	75 1a                	jne    79bbd7 <agentGoProject/common.GetDockerInfos.func3+0x37>
  79bbbd:	48 8b 42 08          	mov    0x8(%rdx),%rax
  79bbc1:	e8 1a 4e cd ff       	call   4709e0 <sync.(*Mutex).Unlock>
  79bbc6:	48 8b 6c 24 08       	mov    0x8(%rsp),%rbp
  79bbcb:	48 83 c4 10          	add    $0x10,%rsp
  79bbcf:	c3                   	ret    
  79bbd0:	e8 ab 93 cc ff       	call   464f80 <runtime.morestack.abi0>
  79bbd5:	eb c9                	jmp    79bba0 <agentGoProject/common.GetDockerInfos.func3>
  79bbd7:	4c 8d 6c 24 18       	lea    0x18(%rsp),%r13
  79bbdc:	0f 1f 40 00          	nopl   0x0(%rax)
  79bbe0:	4d 39 2c 24          	cmp    %r13,(%r12)
  79bbe4:	75 d7                	jne    79bbbd <agentGoProject/common.GetDockerInfos.func3+0x1d>
  79bbe6:	49 89 24 24          	mov    %rsp,(%r12)
  79bbea:	eb d1                	jmp    79bbbd <agentGoProject/common.GetDockerInfos.func3+0x1d>
  79bbec:	cc                   	int3   
  79bbed:	cc                   	int3   
  79bbee:	cc                   	int3   
  79bbef:	cc                   	int3   
  79bbf0:	cc                   	int3   
  79bbf1:	cc                   	int3   
  79bbf2:	cc                   	int3   
  79bbf3:	cc                   	int3   
  79bbf4:	cc                   	int3   
  79bbf5:	cc                   	int3   
  79bbf6:	cc                   	int3   
  79bbf7:	cc                   	int3   
  79bbf8:	cc                   	int3   
  79bbf9:	cc                   	int3   
  79bbfa:	cc                   	int3   
  79bbfb:	cc                   	int3   
  79bbfc:	cc                   	int3   
  79bbfd:	cc                   	int3   
  79bbfe:	cc                   	int3   
  79bbff:	cc                   	int3   

000000000079bc00 <agentGoProject/common.GetDockerInfos.func2>:
agentGoProject/common.GetDockerInfos.func2():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:77
  79bc00:	49 3b 66 10          	cmp    0x10(%r14),%rsp
  79bc04:	76 2f                	jbe    79bc35 <agentGoProject/common.GetDockerInfos.func2+0x35>
  79bc06:	48 83 ec 10          	sub    $0x10,%rsp
  79bc0a:	48 89 6c 24 08       	mov    %rbp,0x8(%rsp)
  79bc0f:	48 8d 6c 24 08       	lea    0x8(%rsp),%rbp
  79bc14:	4d 8b 66 20          	mov    0x20(%r14),%r12
  79bc18:	4d 85 e4             	test   %r12,%r12
  79bc1b:	75 1f                	jne    79bc3c <agentGoProject/common.GetDockerInfos.func2+0x3c>
  79bc1d:	48 8b 4a 08          	mov    0x8(%rdx),%rcx
  79bc21:	48 8b 42 10          	mov    0x10(%rdx),%rax
  79bc25:	48 8b 49 18          	mov    0x18(%rcx),%rcx
  79bc29:	ff d1                	call   *%rcx
  79bc2b:	48 8b 6c 24 08       	mov    0x8(%rsp),%rbp
  79bc30:	48 83 c4 10          	add    $0x10,%rsp
  79bc34:	c3                   	ret    
  79bc35:	e8 46 93 cc ff       	call   464f80 <runtime.morestack.abi0>
  79bc3a:	eb c4                	jmp    79bc00 <agentGoProject/common.GetDockerInfos.func2>
  79bc3c:	4c 8d 6c 24 18       	lea    0x18(%rsp),%r13
  79bc41:	4d 39 2c 24          	cmp    %r13,(%r12)
  79bc45:	75 d6                	jne    79bc1d <agentGoProject/common.GetDockerInfos.func2+0x1d>
  79bc47:	49 89 24 24          	mov    %rsp,(%r12)
  79bc4b:	eb d0                	jmp    79bc1d <agentGoProject/common.GetDockerInfos.func2+0x1d>
  79bc4d:	cc                   	int3   
  79bc4e:	cc                   	int3   
  79bc4f:	cc                   	int3   
  79bc50:	cc                   	int3   
  79bc51:	cc                   	int3   
  79bc52:	cc                   	int3   
  79bc53:	cc                   	int3   
  79bc54:	cc                   	int3   
  79bc55:	cc                   	int3   
  79bc56:	cc                   	int3   
  79bc57:	cc                   	int3   
  79bc58:	cc                   	int3   
  79bc59:	cc                   	int3   
  79bc5a:	cc                   	int3   
  79bc5b:	cc                   	int3   
  79bc5c:	cc                   	int3   
  79bc5d:	cc                   	int3   
  79bc5e:	cc                   	int3   
  79bc5f:	cc                   	int3   

000000000079bc60 <agentGoProject/common.GatherDockers>:
agentGoProject/common.GatherDockers():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:99
  79bc60:	4c 8d a4 24 f0 fd ff 	lea    -0x210(%rsp),%r12
  79bc67:	ff 
  79bc68:	4d 3b 66 10          	cmp    0x10(%r14),%r12
  79bc6c:	0f 86 d4 07 00 00    	jbe    79c446 <agentGoProject/common.GatherDockers+0x7e6>
  79bc72:	48 81 ec 90 02 00 00 	sub    $0x290,%rsp
  79bc79:	48 89 ac 24 88 02 00 	mov    %rbp,0x288(%rsp)
  79bc80:	00 
  79bc81:	48 8d ac 24 88 02 00 	lea    0x288(%rsp),%rbp
  79bc88:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:100
  79bc89:	48 8b 0d a0 7a 40 00 	mov    0x407aa0(%rip),%rcx        # ba3730 <agentGoProject/common.DockerIdMap>
  79bc90:	48 85 c9             	test   %rcx,%rcx
  79bc93:	74 05                	je     79bc9a <agentGoProject/common.GatherDockers+0x3a>
  79bc95:	48 8b 09             	mov    (%rcx),%rcx
  79bc98:	eb 06                	jmp    79bca0 <agentGoProject/common.GatherDockers+0x40>
  79bc9a:	31 c9                	xor    %ecx,%ecx
  79bc9c:	0f 1f 40 00          	nopl   0x0(%rax)
  79bca0:	48 85 c9             	test   %rcx,%rcx
  79bca3:	0f 8e 00 01 00 00    	jle    79bda9 <agentGoProject/common.GatherDockers+0x149>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:99
  79bca9:	48 89 84 24 08 01 00 	mov    %rax,0x108(%rsp)
  79bcb0:	00 
  79bcb1:	48 89 9c 24 00 01 00 	mov    %rbx,0x100(%rsp)
  79bcb8:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:101
  79bcb9:	48 89 d8             	mov    %rbx,%rax
  79bcbc:	0f 1f 40 00          	nopl   0x0(%rax)
  79bcc0:	e8 bb 07 00 00       	call   79c480 <agentGoProject/common.GetDockerApiContainerList>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:106
  79bcc5:	48 85 db             	test   %rbx,%rbx
  79bcc8:	74 62                	je     79bd2c <agentGoProject/common.GatherDockers+0xcc>
  79bcca:	48 89 5c 24 60       	mov    %rbx,0x60(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:101
  79bccf:	48 89 4c 24 68       	mov    %rcx,0x68(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:120
  79bcd4:	48 89 84 24 f8 00 00 	mov    %rax,0xf8(%rsp)
  79bcdb:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:107
  79bcdc:	48 8b 1d 4d 7a 40 00 	mov    0x407a4d(%rip),%rbx        # ba3730 <agentGoProject/common.DockerIdMap>
  79bce3:	48 8d bc 24 48 01 00 	lea    0x148(%rsp),%rdi
  79bcea:	00 
  79bceb:	48 8d 7f e0          	lea    -0x20(%rdi),%rdi
  79bcef:	66 0f 1f 84 00 00 00 	nopw   0x0(%rax,%rax,1)
  79bcf6:	00 00 
  79bcf8:	0f 1f 84 00 00 00 00 	nopl   0x0(%rax,%rax,1)
  79bcff:	00 
  79bd00:	48 89 6c 24 f0       	mov    %rbp,-0x10(%rsp)
  79bd05:	48 8d 6c 24 f0       	lea    -0x10(%rsp),%rbp
  79bd0a:	e8 5c ba cc ff       	call   46776b <runtime.duffzero+0x14b>
  79bd0f:	48 8b 6d 00          	mov    0x0(%rbp),%rbp
  79bd13:	48 8d 05 e6 f0 06 00 	lea    0x6f0e6(%rip),%rax        # 80ae00 <type:*+0x48e00>
  79bd1a:	48 8d 8c 24 48 01 00 	lea    0x148(%rsp),%rcx
  79bd21:	00 
  79bd22:	e8 19 3f c7 ff       	call   40fc40 <runtime.mapiterinit>
  79bd27:	e9 39 01 00 00       	jmp    79be65 <agentGoProject/common.GatherDockers+0x205>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:142
  79bd2c:	44 0f 11 bc 24 18 01 	movups %xmm15,0x118(%rsp)
  79bd33:	00 00 
  79bd35:	48 8d 15 64 5f 05 00 	lea    0x55f64(%rip),%rdx        # 7f1ca0 <type:*+0x2fca0>
  79bd3c:	48 89 94 24 18 01 00 	mov    %rdx,0x118(%rsp)
  79bd43:	00 
  79bd44:	48 8d 35 75 35 17 00 	lea    0x173575(%rip),%rsi        # 90f2c0 <runtime.buildVersion.str+0x20>
  79bd4b:	48 89 b4 24 20 01 00 	mov    %rsi,0x120(%rsp)
  79bd52:	00 
  79bd53:	48 8d 84 24 18 01 00 	lea    0x118(%rsp),%rax
  79bd5a:	00 
  79bd5b:	bb 01 00 00 00       	mov    $0x1,%ebx
  79bd60:	48 89 d9             	mov    %rbx,%rcx
  79bd63:	e8 b8 11 df ff       	call   58cf20 <log.Println>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:143
  79bd68:	48 8b 1d c1 79 40 00 	mov    0x4079c1(%rip),%rbx        # ba3730 <agentGoProject/common.DockerIdMap>
  79bd6f:	48 8d bc 24 48 01 00 	lea    0x148(%rsp),%rdi
  79bd76:	00 
  79bd77:	48 8d 7f e0          	lea    -0x20(%rdi),%rdi
  79bd7b:	0f 1f 44 00 00       	nopl   0x0(%rax,%rax,1)
  79bd80:	48 89 6c 24 f0       	mov    %rbp,-0x10(%rsp)
  79bd85:	48 8d 6c 24 f0       	lea    -0x10(%rsp),%rbp
  79bd8a:	e8 dc b9 cc ff       	call   46776b <runtime.duffzero+0x14b>
  79bd8f:	48 8b 6d 00          	mov    0x0(%rbp),%rbp
  79bd93:	48 8d 05 66 f0 06 00 	lea    0x6f066(%rip),%rax        # 80ae00 <type:*+0x48e00>
  79bd9a:	48 8d 8c 24 48 01 00 	lea    0x148(%rsp),%rcx
  79bda1:	00 
  79bda2:	e8 99 3e c7 ff       	call   40fc40 <runtime.mapiterinit>
  79bda7:	eb 1d                	jmp    79bdc6 <agentGoProject/common.GatherDockers+0x166>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:149
  79bda9:	48 8b ac 24 88 02 00 	mov    0x288(%rsp),%rbp
  79bdb0:	00 
  79bdb1:	48 81 c4 90 02 00 00 	add    $0x290,%rsp
  79bdb8:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:143
  79bdb9:	48 8d 84 24 48 01 00 	lea    0x148(%rsp),%rax
  79bdc0:	00 
  79bdc1:	e8 fa 40 c7 ff       	call   40fec0 <runtime.mapiternext>
  79bdc6:	48 8b 94 24 48 01 00 	mov    0x148(%rsp),%rdx
  79bdcd:	00 
  79bdce:	48 85 d2             	test   %rdx,%rdx
  79bdd1:	74 d6                	je     79bda9 <agentGoProject/common.GatherDockers+0x149>
  79bdd3:	48 8b 02             	mov    (%rdx),%rax
  79bdd6:	48 8b 5a 08          	mov    0x8(%rdx),%rbx
./C:/Program Files/Go/src/strings/strings.go:305
  79bdda:	48 8d 0d 57 19 17 00 	lea    0x171957(%rip),%rcx        # 90d738 <runtime.gcbits.*+0x2d8>
strings.Split():
./C:/Program Files/Go/src/strings/strings.go:305
  79bde1:	bf 01 00 00 00       	mov    $0x1,%edi
  79bde6:	31 f6                	xor    %esi,%esi
  79bde8:	49 c7 c0 ff ff ff ff 	mov    $0xffffffffffffffff,%r8
  79bdef:	e8 4c c0 d5 ff       	call   4f7e40 <strings.genSplit>
agentGoProject/common.GatherDockers():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:144
  79bdf4:	48 85 db             	test   %rbx,%rbx
  79bdf7:	76 51                	jbe    79be4a <agentGoProject/common.GatherDockers+0x1ea>
  79bdf9:	48 8b 08             	mov    (%rax),%rcx
  79bdfc:	48 8b 78 08          	mov    0x8(%rax),%rdi
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:145
  79be00:	48 8d 05 99 ec 06 00 	lea    0x6ec99(%rip),%rax        # 80aaa0 <type:*+0x48aa0>
  79be07:	48 8b 9c 24 08 01 00 	mov    0x108(%rsp),%rbx
  79be0e:	00 
  79be0f:	e8 ec 76 c7 ff       	call   413500 <runtime.mapassign_faststr>
  79be14:	48 8d 15 85 5e 05 00 	lea    0x55e85(%rip),%rdx        # 7f1ca0 <type:*+0x2fca0>
  79be1b:	48 89 10             	mov    %rdx,(%rax)
  79be1e:	83 3d 2b 97 43 00 00 	cmpl   $0x0,0x43972b(%rip)        # bd5550 <runtime.writeBarrier>
  79be25:	75 0d                	jne    79be34 <agentGoProject/common.GatherDockers+0x1d4>
  79be27:	48 8d 0d e2 40 17 00 	lea    0x1740e2(%rip),%rcx        # 90ff10 <runtime.buildVersion.str+0xc70>
  79be2e:	48 89 48 08          	mov    %rcx,0x8(%rax)
  79be32:	eb 85                	jmp    79bdb9 <agentGoProject/common.GatherDockers+0x159>
  79be34:	48 8d 78 08          	lea    0x8(%rax),%rdi
  79be38:	48 8d 0d d1 40 17 00 	lea    0x1740d1(%rip),%rcx        # 90ff10 <runtime.buildVersion.str+0xc70>
  79be3f:	90                   	nop
  79be40:	e8 9b b2 cc ff       	call   4670e0 <runtime.gcWriteBarrierCX>
  79be45:	e9 6f ff ff ff       	jmp    79bdb9 <agentGoProject/common.GatherDockers+0x159>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:144
  79be4a:	31 c0                	xor    %eax,%eax
  79be4c:	48 89 c1             	mov    %rax,%rcx
  79be4f:	e8 2c b6 cc ff       	call   467480 <runtime.panicIndex>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:107
  79be54:	48 8d 84 24 48 01 00 	lea    0x148(%rsp),%rax
  79be5b:	00 
  79be5c:	0f 1f 40 00          	nopl   0x0(%rax)
  79be60:	e8 5b 40 c7 ff       	call   40fec0 <runtime.mapiternext>
  79be65:	48 8b 94 24 48 01 00 	mov    0x148(%rsp),%rdx
  79be6c:	00 
  79be6d:	48 85 d2             	test   %rdx,%rdx
  79be70:	0f 84 33 ff ff ff    	je     79bda9 <agentGoProject/common.GatherDockers+0x149>
  79be76:	4c 8b 8c 24 50 01 00 	mov    0x150(%rsp),%r9
  79be7d:	00 
  79be7e:	4d 8b 51 08          	mov    0x8(%r9),%r10
  79be82:	4c 89 54 24 30       	mov    %r10,0x30(%rsp)
  79be87:	4d 8b 09             	mov    (%r9),%r9
  79be8a:	4c 89 8c 24 c0 00 00 	mov    %r9,0xc0(%rsp)
  79be91:	00 
  79be92:	48 8b 02             	mov    (%rdx),%rax
  79be95:	48 89 84 24 d8 00 00 	mov    %rax,0xd8(%rsp)
  79be9c:	00 
  79be9d:	48 8b 5a 08          	mov    0x8(%rdx),%rbx
  79bea1:	48 89 5c 24 48       	mov    %rbx,0x48(%rsp)
./C:/Program Files/Go/src/strings/strings.go:305
  79bea6:	48 8d 0d 8b 18 17 00 	lea    0x17188b(%rip),%rcx        # 90d738 <runtime.gcbits.*+0x2d8>
strings.Split():
./C:/Program Files/Go/src/strings/strings.go:305
  79bead:	bf 01 00 00 00       	mov    $0x1,%edi
  79beb2:	31 f6                	xor    %esi,%esi
  79beb4:	49 c7 c0 ff ff ff ff 	mov    $0xffffffffffffffff,%r8
  79bebb:	0f 1f 44 00 00       	nopl   0x0(%rax,%rax,1)
  79bec0:	e8 7b bf d5 ff       	call   4f7e40 <strings.genSplit>
agentGoProject/common.GatherDockers():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:109
  79bec5:	48 85 db             	test   %rbx,%rbx
  79bec8:	0f 86 6b 05 00 00    	jbe    79c439 <agentGoProject/common.GatherDockers+0x7d9>
  79bece:	48 8b 10             	mov    (%rax),%rdx
  79bed1:	48 89 94 24 f0 00 00 	mov    %rdx,0xf0(%rsp)
  79bed8:	00 
  79bed9:	4c 8b 48 08          	mov    0x8(%rax),%r9
  79bedd:	4c 89 4c 24 58       	mov    %r9,0x58(%rsp)
./C:/Program Files/Go/src/strings/strings.go:305
  79bee2:	48 8b 84 24 d8 00 00 	mov    0xd8(%rsp),%rax
  79bee9:	00 
strings.Split():
./C:/Program Files/Go/src/strings/strings.go:305
  79beea:	48 8b 5c 24 48       	mov    0x48(%rsp),%rbx
  79beef:	48 8d 0d 42 18 17 00 	lea    0x171842(%rip),%rcx        # 90d738 <runtime.gcbits.*+0x2d8>
  79bef6:	bf 01 00 00 00       	mov    $0x1,%edi
  79befb:	31 f6                	xor    %esi,%esi
  79befd:	49 c7 c0 ff ff ff ff 	mov    $0xffffffffffffffff,%r8
  79bf04:	e8 37 bf d5 ff       	call   4f7e40 <strings.genSplit>
agentGoProject/common.GatherDockers():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:111
  79bf09:	48 83 fb 01          	cmp    $0x1,%rbx
  79bf0d:	0f 86 19 05 00 00    	jbe    79c42c <agentGoProject/common.GatherDockers+0x7cc>
  79bf13:	48 8b 50 18          	mov    0x18(%rax),%rdx
  79bf17:	4c 8b 48 10          	mov    0x10(%rax),%r9
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:112
  79bf1b:	48 8b 5c 24 30       	mov    0x30(%rsp),%rbx
  79bf20:	48 85 db             	test   %rbx,%rbx
  79bf23:	74 29                	je     79bf4e <agentGoProject/common.GatherDockers+0x2ee>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:111
  79bf25:	48 89 54 24 78       	mov    %rdx,0x78(%rsp)
  79bf2a:	4c 89 8c 24 e0 00 00 	mov    %r9,0xe0(%rsp)
  79bf31:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:120
  79bf32:	48 8b 8c 24 f8 00 00 	mov    0xf8(%rsp),%rcx
  79bf39:	00 
  79bf3a:	49 89 ca             	mov    %rcx,%r10
  79bf3d:	48 8b 7c 24 60       	mov    0x60(%rsp),%rdi
  79bf42:	31 c0                	xor    %eax,%eax
  79bf44:	31 f6                	xor    %esi,%esi
  79bf46:	45 31 c0             	xor    %r8d,%r8d
  79bf49:	e9 d2 00 00 00       	jmp    79c020 <agentGoProject/common.GatherDockers+0x3c0>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:113
  79bf4e:	44 0f 11 bc 24 28 01 	movups %xmm15,0x128(%rsp)
  79bf55:	00 00 
  79bf57:	44 0f 11 bc 24 38 01 	movups %xmm15,0x138(%rsp)
  79bf5e:	00 00 
  79bf60:	48 8d 0d 39 5d 05 00 	lea    0x55d39(%rip),%rcx        # 7f1ca0 <type:*+0x2fca0>
  79bf67:	48 89 8c 24 28 01 00 	mov    %rcx,0x128(%rsp)
  79bf6e:	00 
  79bf6f:	48 8d 15 8a 3f 17 00 	lea    0x173f8a(%rip),%rdx        # 90ff00 <runtime.buildVersion.str+0xc60>
  79bf76:	48 89 94 24 30 01 00 	mov    %rdx,0x130(%rsp)
  79bf7d:	00 
  79bf7e:	48 8b 84 24 c0 00 00 	mov    0xc0(%rsp),%rax
  79bf85:	00 
  79bf86:	e8 75 f5 c6 ff       	call   40b500 <runtime.convTstring>
  79bf8b:	48 8d 0d 0e 5d 05 00 	lea    0x55d0e(%rip),%rcx        # 7f1ca0 <type:*+0x2fca0>
  79bf92:	48 89 8c 24 38 01 00 	mov    %rcx,0x138(%rsp)
  79bf99:	00 
  79bf9a:	48 89 84 24 40 01 00 	mov    %rax,0x140(%rsp)
  79bfa1:	00 
  79bfa2:	48 8d 84 24 28 01 00 	lea    0x128(%rsp),%rax
  79bfa9:	00 
  79bfaa:	bb 02 00 00 00       	mov    $0x2,%ebx
  79bfaf:	48 89 d9             	mov    %rbx,%rcx
  79bfb2:	e8 69 0f df ff       	call   58cf20 <log.Println>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:114
  79bfb7:	48 8d 05 e2 ea 06 00 	lea    0x6eae2(%rip),%rax        # 80aaa0 <type:*+0x48aa0>
  79bfbe:	48 8b 9c 24 08 01 00 	mov    0x108(%rsp),%rbx
  79bfc5:	00 
  79bfc6:	48 8b 8c 24 f0 00 00 	mov    0xf0(%rsp),%rcx
  79bfcd:	00 
  79bfce:	48 8b 7c 24 58       	mov    0x58(%rsp),%rdi
  79bfd3:	e8 28 75 c7 ff       	call   413500 <runtime.mapassign_faststr>
  79bfd8:	48 8d 0d c1 5c 05 00 	lea    0x55cc1(%rip),%rcx        # 7f1ca0 <type:*+0x2fca0>
  79bfdf:	48 89 08             	mov    %rcx,(%rax)
  79bfe2:	83 3d 67 95 43 00 00 	cmpl   $0x0,0x439567(%rip)        # bd5550 <runtime.writeBarrier>
  79bfe9:	75 10                	jne    79bffb <agentGoProject/common.GatherDockers+0x39b>
  79bfeb:	48 8d 15 1e 3f 17 00 	lea    0x173f1e(%rip),%rdx        # 90ff10 <runtime.buildVersion.str+0xc70>
  79bff2:	48 89 50 08          	mov    %rdx,0x8(%rax)
  79bff6:	e9 59 fe ff ff       	jmp    79be54 <agentGoProject/common.GatherDockers+0x1f4>
  79bffb:	48 8d 78 08          	lea    0x8(%rax),%rdi
  79bfff:	48 8d 15 0a 3f 17 00 	lea    0x173f0a(%rip),%rdx        # 90ff10 <runtime.buildVersion.str+0xc70>
  79c006:	e8 f5 b0 cc ff       	call   467100 <runtime.gcWriteBarrierDX>
  79c00b:	e9 44 fe ff ff       	jmp    79be54 <agentGoProject/common.GatherDockers+0x1f4>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:120
  79c010:	48 81 c1 e0 00 00 00 	add    $0xe0,%rcx
  79c017:	48 ff c0             	inc    %rax
  79c01a:	4c 89 df             	mov    %r11,%rdi
  79c01d:	0f 1f 00             	nopl   (%rax)
  79c020:	48 39 c7             	cmp    %rax,%rdi
  79c023:	0f 8e 0c 02 00 00    	jle    79c235 <agentGoProject/common.GatherDockers+0x5d5>
  79c029:	48 89 44 24 70       	mov    %rax,0x70(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:129
  79c02e:	48 89 74 24 40       	mov    %rsi,0x40(%rsp)
  79c033:	4c 89 84 24 d0 00 00 	mov    %r8,0xd0(%rsp)
  79c03a:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:120
  79c03b:	48 89 8c 24 10 01 00 	mov    %rcx,0x110(%rsp)
  79c042:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:106
  79c043:	49 89 fb             	mov    %rdi,%r11
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:120
  79c046:	48 8d bc 24 a8 01 00 	lea    0x1a8(%rsp),%rdi
  79c04d:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:129
  79c04e:	49 89 f4             	mov    %rsi,%r12
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:120
  79c051:	48 89 ce             	mov    %rcx,%rsi
  79c054:	66 0f 1f 84 00 00 00 	nopw   0x0(%rax,%rax,1)
  79c05b:	00 00 
  79c05d:	0f 1f 00             	nopl   (%rax)
  79c060:	48 89 6c 24 f0       	mov    %rbp,-0x10(%rsp)
  79c065:	48 8d 6c 24 f0       	lea    -0x10(%rsp),%rbp
  79c06a:	e8 ed b9 cc ff       	call   467a5c <runtime.duffcopy+0x2bc>
  79c06f:	48 8b 6d 00          	mov    0x0(%rbp),%rbp
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:111
  79c073:	48 83 fa 01          	cmp    $0x1,%rdx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:121
  79c077:	0f 85 e5 00 00 00    	jne    79c162 <agentGoProject/common.GatherDockers+0x502>
  79c07d:	41 80 39 32          	cmpb   $0x32,(%r9)
  79c081:	74 07                	je     79c08a <agentGoProject/common.GatherDockers+0x42a>
  79c083:	31 f6                	xor    %esi,%esi
  79c085:	e9 da 00 00 00       	jmp    79c164 <agentGoProject/common.GatherDockers+0x504>
  79c08a:	48 8b 84 24 b8 01 00 	mov    0x1b8(%rsp),%rax
  79c091:	00 
  79c092:	48 8b 9c 24 c0 01 00 	mov    0x1c0(%rsp),%rbx
  79c099:	00 
  79c09a:	48 8b 8c 24 c8 01 00 	mov    0x1c8(%rsp),%rcx
  79c0a1:	00 
  79c0a2:	48 8d 3d 8f 16 17 00 	lea    0x17168f(%rip),%rdi        # 90d738 <runtime.gcbits.*+0x2d8>
  79c0a9:	be 01 00 00 00       	mov    $0x1,%esi
  79c0ae:	e8 2d c5 d5 ff       	call   4f85e0 <strings.Join>
  79c0b3:	48 89 d9             	mov    %rbx,%rcx
  79c0b6:	48 8d 3d 7b 16 17 00 	lea    0x17167b(%rip),%rdi        # 90d738 <runtime.gcbits.*+0x2d8>
  79c0bd:	be 01 00 00 00       	mov    $0x1,%esi
  79c0c2:	48 89 c3             	mov    %rax,%rbx
  79c0c5:	48 8d 84 24 a0 00 00 	lea    0xa0(%rsp),%rax
  79c0cc:	00 
  79c0cd:	e8 6e 42 cb ff       	call   450340 <runtime.concatstring2>
  79c0d2:	48 89 84 24 c8 00 00 	mov    %rax,0xc8(%rsp)
  79c0d9:	00 
  79c0da:	48 89 5c 24 38       	mov    %rbx,0x38(%rsp)
  79c0df:	48 8b 4c 24 30       	mov    0x30(%rsp),%rcx
  79c0e4:	48 8d 3d 4d 16 17 00 	lea    0x17164d(%rip),%rdi        # 90d738 <runtime.gcbits.*+0x2d8>
  79c0eb:	be 01 00 00 00       	mov    $0x1,%esi
  79c0f0:	48 8d 84 24 80 00 00 	lea    0x80(%rsp),%rax
  79c0f7:	00 
  79c0f8:	48 8b 9c 24 c0 00 00 	mov    0xc0(%rsp),%rbx
  79c0ff:	00 
  79c100:	e8 3b 42 cb ff       	call   450340 <runtime.concatstring2>
./C:/Program Files/Go/src/strings/strings.go:59
  79c105:	48 89 c1             	mov    %rax,%rcx
strings.Contains():
./C:/Program Files/Go/src/strings/strings.go:59
  79c108:	48 89 df             	mov    %rbx,%rdi
  79c10b:	48 8b 84 24 c8 00 00 	mov    0xc8(%rsp),%rax
  79c112:	00 
  79c113:	48 8b 5c 24 38       	mov    0x38(%rsp),%rbx
  79c118:	e8 43 f5 d5 ff       	call   4fb660 <strings.Index>
  79c11d:	48 85 c0             	test   %rax,%rax
  79c120:	0f 9d c2             	setge  %dl
agentGoProject/common.GatherDockers():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:120
  79c123:	48 8b 44 24 70       	mov    0x70(%rsp),%rax
  79c128:	48 8b 8c 24 10 01 00 	mov    0x110(%rsp),%rcx
  79c12f:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:124
  79c130:	48 8b 5c 24 30       	mov    0x30(%rsp),%rbx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:129
  79c135:	4c 8b 84 24 d0 00 00 	mov    0xd0(%rsp),%r8
  79c13c:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:124
  79c13d:	4c 8b 8c 24 e0 00 00 	mov    0xe0(%rsp),%r9
  79c144:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:137
  79c145:	4c 8b 94 24 f8 00 00 	mov    0xf8(%rsp),%r10
  79c14c:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:120
  79c14d:	4c 8b 5c 24 60       	mov    0x60(%rsp),%r11
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:129
  79c152:	4c 8b 64 24 40       	mov    0x40(%rsp),%r12
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:121
  79c157:	89 d6                	mov    %edx,%esi
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:111
  79c159:	48 8b 54 24 78       	mov    0x78(%rsp),%rdx
  79c15e:	66 90                	xchg   %ax,%ax
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:121
  79c160:	eb 02                	jmp    79c164 <agentGoProject/common.GatherDockers+0x504>
  79c162:	31 f6                	xor    %esi,%esi
  79c164:	40 84 f6             	test   %sil,%sil
  79c167:	74 1c                	je     79c185 <agentGoProject/common.GatherDockers+0x525>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:123
  79c169:	4c 8b a4 24 a8 01 00 	mov    0x1a8(%rsp),%r12
  79c170:	00 
  79c171:	4c 8b ac 24 b0 01 00 	mov    0x1b0(%rsp),%r13
  79c178:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:129
  79c179:	4c 89 ee             	mov    %r13,%rsi
  79c17c:	4d 89 e0             	mov    %r12,%r8
  79c17f:	90                   	nop
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:123
  79c180:	e9 8b fe ff ff       	jmp    79c010 <agentGoProject/common.GatherDockers+0x3b0>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:111
  79c185:	48 83 fa 01          	cmp    $0x1,%rdx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:124
  79c189:	0f 85 9e 00 00 00    	jne    79c22d <agentGoProject/common.GatherDockers+0x5cd>
  79c18f:	41 80 39 31          	cmpb   $0x31,(%r9)
  79c193:	0f 85 8c 00 00 00    	jne    79c225 <agentGoProject/common.GatherDockers+0x5c5>
  79c199:	4c 8b ac 24 a8 01 00 	mov    0x1a8(%rsp),%r13
  79c1a0:	00 
  79c1a1:	48 39 9c 24 b0 01 00 	cmp    %rbx,0x1b0(%rsp)
  79c1a8:	00 
  79c1a9:	74 04                	je     79c1af <agentGoProject/common.GatherDockers+0x54f>
  79c1ab:	31 f6                	xor    %esi,%esi
  79c1ad:	eb 51                	jmp    79c200 <agentGoProject/common.GatherDockers+0x5a0>
  79c1af:	48 8b 84 24 c0 00 00 	mov    0xc0(%rsp),%rax
  79c1b6:	00 
  79c1b7:	48 89 d9             	mov    %rbx,%rcx
  79c1ba:	4c 89 eb             	mov    %r13,%rbx
  79c1bd:	0f 1f 00             	nopl   (%rax)
  79c1c0:	e8 7b 72 c6 ff       	call   403440 <runtime.memequal>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:120
  79c1c5:	48 8b 8c 24 10 01 00 	mov    0x110(%rsp),%rcx
  79c1cc:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:111
  79c1cd:	48 8b 54 24 78       	mov    0x78(%rsp),%rdx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:124
  79c1d2:	48 8b 5c 24 30       	mov    0x30(%rsp),%rbx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:129
  79c1d7:	4c 8b 84 24 d0 00 00 	mov    0xd0(%rsp),%r8
  79c1de:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:121
  79c1df:	4c 8b 8c 24 e0 00 00 	mov    0xe0(%rsp),%r9
  79c1e6:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:137
  79c1e7:	4c 8b 94 24 f8 00 00 	mov    0xf8(%rsp),%r10
  79c1ee:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:120
  79c1ef:	4c 8b 5c 24 60       	mov    0x60(%rsp),%r11
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:129
  79c1f4:	4c 8b 64 24 40       	mov    0x40(%rsp),%r12
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:124
  79c1f9:	89 c6                	mov    %eax,%esi
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:120
  79c1fb:	48 8b 44 24 70       	mov    0x70(%rsp),%rax
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:124
  79c200:	40 84 f6             	test   %sil,%sil
  79c203:	74 15                	je     79c21a <agentGoProject/common.GatherDockers+0x5ba>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:126
  79c205:	4c 8b 84 24 a8 01 00 	mov    0x1a8(%rsp),%r8
  79c20c:	00 
  79c20d:	48 8b b4 24 b0 01 00 	mov    0x1b0(%rsp),%rsi
  79c214:	00 
  79c215:	e9 f6 fd ff ff       	jmp    79c010 <agentGoProject/common.GatherDockers+0x3b0>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:129
  79c21a:	4c 89 e6             	mov    %r12,%rsi
  79c21d:	0f 1f 00             	nopl   (%rax)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:124
  79c220:	e9 eb fd ff ff       	jmp    79c010 <agentGoProject/common.GatherDockers+0x3b0>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:129
  79c225:	4c 89 e6             	mov    %r12,%rsi
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:124
  79c228:	e9 e3 fd ff ff       	jmp    79c010 <agentGoProject/common.GatherDockers+0x3b0>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:129
  79c22d:	4c 89 e6             	mov    %r12,%rsi
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:124
  79c230:	e9 db fd ff ff       	jmp    79c010 <agentGoProject/common.GatherDockers+0x3b0>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:129
  79c235:	48 85 f6             	test   %rsi,%rsi
  79c238:	0f 85 c7 00 00 00    	jne    79c305 <agentGoProject/common.GatherDockers+0x6a5>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:130
  79c23e:	44 0f 11 bc 24 28 01 	movups %xmm15,0x128(%rsp)
  79c245:	00 00 
  79c247:	44 0f 11 bc 24 38 01 	movups %xmm15,0x138(%rsp)
  79c24e:	00 00 
  79c250:	48 8d 0d 49 5a 05 00 	lea    0x55a49(%rip),%rcx        # 7f1ca0 <type:*+0x2fca0>
  79c257:	48 89 8c 24 28 01 00 	mov    %rcx,0x128(%rsp)
  79c25e:	00 
  79c25f:	48 8d 15 ba 3c 17 00 	lea    0x173cba(%rip),%rdx        # 90ff20 <runtime.buildVersion.str+0xc80>
  79c266:	48 89 94 24 30 01 00 	mov    %rdx,0x130(%rsp)
  79c26d:	00 
  79c26e:	48 8b 84 24 c0 00 00 	mov    0xc0(%rsp),%rax
  79c275:	00 
  79c276:	e8 85 f2 c6 ff       	call   40b500 <runtime.convTstring>
  79c27b:	48 8d 0d 1e 5a 05 00 	lea    0x55a1e(%rip),%rcx        # 7f1ca0 <type:*+0x2fca0>
  79c282:	48 89 8c 24 38 01 00 	mov    %rcx,0x138(%rsp)
  79c289:	00 
  79c28a:	48 89 84 24 40 01 00 	mov    %rax,0x140(%rsp)
  79c291:	00 
  79c292:	48 8d 84 24 28 01 00 	lea    0x128(%rsp),%rax
  79c299:	00 
  79c29a:	bb 02 00 00 00       	mov    $0x2,%ebx
  79c29f:	48 89 d9             	mov    %rbx,%rcx
  79c2a2:	e8 79 0c df ff       	call   58cf20 <log.Println>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:131
  79c2a7:	48 8d 05 f2 e7 06 00 	lea    0x6e7f2(%rip),%rax        # 80aaa0 <type:*+0x48aa0>
  79c2ae:	48 8b 9c 24 08 01 00 	mov    0x108(%rsp),%rbx
  79c2b5:	00 
  79c2b6:	48 8b 8c 24 f0 00 00 	mov    0xf0(%rsp),%rcx
  79c2bd:	00 
  79c2be:	48 8b 7c 24 58       	mov    0x58(%rsp),%rdi
  79c2c3:	e8 38 72 c7 ff       	call   413500 <runtime.mapassign_faststr>
  79c2c8:	48 8d 0d d1 59 05 00 	lea    0x559d1(%rip),%rcx        # 7f1ca0 <type:*+0x2fca0>
  79c2cf:	48 89 08             	mov    %rcx,(%rax)
  79c2d2:	83 3d 77 92 43 00 00 	cmpl   $0x0,0x439277(%rip)        # bd5550 <runtime.writeBarrier>
  79c2d9:	75 10                	jne    79c2eb <agentGoProject/common.GatherDockers+0x68b>
  79c2db:	48 8d 15 2e 3c 17 00 	lea    0x173c2e(%rip),%rdx        # 90ff10 <runtime.buildVersion.str+0xc70>
  79c2e2:	48 89 50 08          	mov    %rdx,0x8(%rax)
  79c2e6:	e9 69 fb ff ff       	jmp    79be54 <agentGoProject/common.GatherDockers+0x1f4>
  79c2eb:	48 8d 78 08          	lea    0x8(%rax),%rdi
  79c2ef:	48 8d 15 1a 3c 17 00 	lea    0x173c1a(%rip),%rdx        # 90ff10 <runtime.buildVersion.str+0xc70>
  79c2f6:	e8 05 ae cc ff       	call   467100 <runtime.gcWriteBarrierDX>
  79c2fb:	0f 1f 44 00 00       	nopl   0x0(%rax,%rax,1)
  79c300:	e9 4f fb ff ff       	jmp    79be54 <agentGoProject/common.GatherDockers+0x1f4>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:137
  79c305:	4c 89 c0             	mov    %r8,%rax
  79c308:	48 89 f3             	mov    %rsi,%rbx
  79c30b:	4c 89 d1             	mov    %r10,%rcx
  79c30e:	48 8b 74 24 68       	mov    0x68(%rsp),%rsi
  79c313:	4c 8b 84 24 00 01 00 	mov    0x100(%rsp),%r8
  79c31a:	00 
  79c31b:	0f 1f 44 00 00       	nopl   0x0(%rax,%rax,1)
  79c320:	e8 5b 03 00 00       	call   79c680 <agentGoProject/common.GetDockerByContainerId>
  79c325:	48 89 84 24 e8 00 00 	mov    %rax,0xe8(%rsp)
  79c32c:	00 
  79c32d:	48 89 5c 24 50       	mov    %rbx,0x50(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:138
  79c332:	44 0f 11 bc 24 28 01 	movups %xmm15,0x128(%rsp)
  79c339:	00 00 
  79c33b:	44 0f 11 bc 24 38 01 	movups %xmm15,0x138(%rsp)
  79c342:	00 00 
  79c344:	48 8b 84 24 c0 00 00 	mov    0xc0(%rsp),%rax
  79c34b:	00 
  79c34c:	48 8b 5c 24 30       	mov    0x30(%rsp),%rbx
  79c351:	e8 aa f1 c6 ff       	call   40b500 <runtime.convTstring>
  79c356:	48 8d 15 43 59 05 00 	lea    0x55943(%rip),%rdx        # 7f1ca0 <type:*+0x2fca0>
  79c35d:	48 89 94 24 28 01 00 	mov    %rdx,0x128(%rsp)
  79c364:	00 
  79c365:	48 89 84 24 30 01 00 	mov    %rax,0x130(%rsp)
  79c36c:	00 
  79c36d:	48 8b 84 24 e8 00 00 	mov    0xe8(%rsp),%rax
  79c374:	00 
  79c375:	48 8b 5c 24 50       	mov    0x50(%rsp),%rbx
  79c37a:	e8 81 f1 c6 ff       	call   40b500 <runtime.convTstring>
  79c37f:	48 8d 15 1a 59 05 00 	lea    0x5591a(%rip),%rdx        # 7f1ca0 <type:*+0x2fca0>
  79c386:	48 89 94 24 38 01 00 	mov    %rdx,0x138(%rsp)
  79c38d:	00 
  79c38e:	48 89 84 24 40 01 00 	mov    %rax,0x140(%rsp)
  79c395:	00 
  79c396:	48 8d 05 4b ef 0d 00 	lea    0xdef4b(%rip),%rax        # 87b2e8 <go:string.*+0xdd20>
  79c39d:	bb 1f 00 00 00       	mov    $0x1f,%ebx
  79c3a2:	48 8d 8c 24 28 01 00 	lea    0x128(%rsp),%rcx
  79c3a9:	00 
  79c3aa:	bf 02 00 00 00       	mov    $0x2,%edi
  79c3af:	48 89 fe             	mov    %rdi,%rsi
  79c3b2:	e8 c9 0a df ff       	call   58ce80 <log.Printf>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:139
  79c3b7:	48 8b 84 24 e8 00 00 	mov    0xe8(%rsp),%rax
  79c3be:	00 
  79c3bf:	48 8b 5c 24 50       	mov    0x50(%rsp),%rbx
  79c3c4:	e8 37 f1 c6 ff       	call   40b500 <runtime.convTstring>
  79c3c9:	48 89 84 24 10 01 00 	mov    %rax,0x110(%rsp)
  79c3d0:	00 
  79c3d1:	48 8b 9c 24 08 01 00 	mov    0x108(%rsp),%rbx
  79c3d8:	00 
  79c3d9:	48 8b 8c 24 f0 00 00 	mov    0xf0(%rsp),%rcx
  79c3e0:	00 
  79c3e1:	48 8b 7c 24 58       	mov    0x58(%rsp),%rdi
  79c3e6:	48 8d 05 b3 e6 06 00 	lea    0x6e6b3(%rip),%rax        # 80aaa0 <type:*+0x48aa0>
  79c3ed:	e8 0e 71 c7 ff       	call   413500 <runtime.mapassign_faststr>
  79c3f2:	48 8d 15 a7 58 05 00 	lea    0x558a7(%rip),%rdx        # 7f1ca0 <type:*+0x2fca0>
  79c3f9:	48 89 10             	mov    %rdx,(%rax)
  79c3fc:	83 3d 4d 91 43 00 00 	cmpl   $0x0,0x43914d(%rip)        # bd5550 <runtime.writeBarrier>
  79c403:	75 11                	jne    79c416 <agentGoProject/common.GatherDockers+0x7b6>
  79c405:	48 8b 8c 24 10 01 00 	mov    0x110(%rsp),%rcx
  79c40c:	00 
  79c40d:	48 89 48 08          	mov    %rcx,0x8(%rax)
  79c411:	e9 3e fa ff ff       	jmp    79be54 <agentGoProject/common.GatherDockers+0x1f4>
  79c416:	48 8d 78 08          	lea    0x8(%rax),%rdi
  79c41a:	48 8b 8c 24 10 01 00 	mov    0x110(%rsp),%rcx
  79c421:	00 
  79c422:	e8 b9 ac cc ff       	call   4670e0 <runtime.gcWriteBarrierCX>
  79c427:	e9 28 fa ff ff       	jmp    79be54 <agentGoProject/common.GatherDockers+0x1f4>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:111
  79c42c:	b8 01 00 00 00       	mov    $0x1,%eax
  79c431:	48 89 d9             	mov    %rbx,%rcx
  79c434:	e8 47 b0 cc ff       	call   467480 <runtime.panicIndex>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:109
  79c439:	31 c0                	xor    %eax,%eax
  79c43b:	48 89 c1             	mov    %rax,%rcx
  79c43e:	66 90                	xchg   %ax,%ax
  79c440:	e8 3b b0 cc ff       	call   467480 <runtime.panicIndex>
  79c445:	90                   	nop
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:99
  79c446:	48 89 44 24 08       	mov    %rax,0x8(%rsp)
  79c44b:	48 89 5c 24 10       	mov    %rbx,0x10(%rsp)
  79c450:	e8 cb 8b cc ff       	call   465020 <runtime.morestack_noctxt.abi0>
  79c455:	48 8b 44 24 08       	mov    0x8(%rsp),%rax
  79c45a:	48 8b 5c 24 10       	mov    0x10(%rsp),%rbx
  79c45f:	90                   	nop
  79c460:	e9 fb f7 ff ff       	jmp    79bc60 <agentGoProject/common.GatherDockers>
  79c465:	cc                   	int3   
  79c466:	cc                   	int3   
  79c467:	cc                   	int3   
  79c468:	cc                   	int3   
  79c469:	cc                   	int3   
  79c46a:	cc                   	int3   
  79c46b:	cc                   	int3   
  79c46c:	cc                   	int3   
  79c46d:	cc                   	int3   
  79c46e:	cc                   	int3   
  79c46f:	cc                   	int3   
  79c470:	cc                   	int3   
  79c471:	cc                   	int3   
  79c472:	cc                   	int3   
  79c473:	cc                   	int3   
  79c474:	cc                   	int3   
  79c475:	cc                   	int3   
  79c476:	cc                   	int3   
  79c477:	cc                   	int3   
  79c478:	cc                   	int3   
  79c479:	cc                   	int3   
  79c47a:	cc                   	int3   
  79c47b:	cc                   	int3   
  79c47c:	cc                   	int3   
  79c47d:	cc                   	int3   
  79c47e:	cc                   	int3   
  79c47f:	cc                   	int3   

000000000079c480 <agentGoProject/common.GetDockerApiContainerList>:
agentGoProject/common.GetDockerApiContainerList():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:155
  79c480:	4c 8d 64 24 d0       	lea    -0x30(%rsp),%r12
  79c485:	4d 3b 66 10          	cmp    0x10(%r14),%r12
  79c489:	0f 86 63 01 00 00    	jbe    79c5f2 <agentGoProject/common.GetDockerApiContainerList+0x172>
  79c48f:	48 81 ec b0 00 00 00 	sub    $0xb0,%rsp
  79c496:	48 89 ac 24 a8 00 00 	mov    %rbp,0xa8(%rsp)
  79c49d:	00 
  79c49e:	48 8d ac 24 a8 00 00 	lea    0xa8(%rsp),%rbp
  79c4a5:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:157
  79c4a6:	48 8b 15 03 87 17 00 	mov    0x178703(%rip),%rdx        # 914bb0 <go:itab.sort.StringSlice,sort.Interface+0x30>
  79c4ad:	48 8b 1d 3c 82 40 00 	mov    0x40823c(%rip),%rbx        # ba46f0 <golang.org/x/net/context.background>
  79c4b4:	48 8b 0d 3d 82 40 00 	mov    0x40823d(%rip),%rcx        # ba46f8 <golang.org/x/net/context.background+0x8>
  79c4bb:	48 89 14 24          	mov    %rdx,(%rsp)
  79c4bf:	0f 10 05 f2 86 17 00 	movups 0x1786f2(%rip),%xmm0        # 914bb8 <go:itab.sort.StringSlice,sort.Interface+0x38>
  79c4c6:	0f 11 44 24 08       	movups %xmm0,0x8(%rsp)
  79c4cb:	0f 10 05 f6 86 17 00 	movups 0x1786f6(%rip),%xmm0        # 914bc8 <go:itab.sort.StringSlice,sort.Interface+0x48>
  79c4d2:	0f 11 44 24 18       	movups %xmm0,0x18(%rsp)
  79c4d7:	0f 10 05 fa 86 17 00 	movups 0x1786fa(%rip),%xmm0        # 914bd8 <go:itab.sort.StringSlice,sort.Interface+0x58>
  79c4de:	0f 11 44 24 28       	movups %xmm0,0x28(%rsp)
  79c4e3:	e8 98 9d fa ff       	call   746280 <github.com/docker/docker/client.(*Client).ContainerList>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:163
  79c4e8:	48 85 ff             	test   %rdi,%rdi
  79c4eb:	0f 84 f1 00 00 00    	je     79c5e2 <agentGoProject/common.GetDockerApiContainerList+0x162>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:169
  79c4f1:	48 89 4c 24 60       	mov    %rcx,0x60(%rsp)
  79c4f6:	48 89 44 24 78       	mov    %rax,0x78(%rsp)
  79c4fb:	48 89 5c 24 58       	mov    %rbx,0x58(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:163
  79c500:	48 89 7c 24 50       	mov    %rdi,0x50(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:164
  79c505:	44 0f 11 bc 24 88 00 	movups %xmm15,0x88(%rsp)
  79c50c:	00 00 
  79c50e:	44 0f 11 bc 24 98 00 	movups %xmm15,0x98(%rsp)
  79c515:	00 00 
  79c517:	48 8d 15 82 57 05 00 	lea    0x55782(%rip),%rdx        # 7f1ca0 <type:*+0x2fca0>
  79c51e:	48 89 94 24 88 00 00 	mov    %rdx,0x88(%rsp)
  79c525:	00 
  79c526:	48 8d 15 03 3a 17 00 	lea    0x173a03(%rip),%rdx        # 90ff30 <runtime.buildVersion.str+0xc90>
  79c52d:	48 89 94 24 90 00 00 	mov    %rdx,0x90(%rsp)
  79c534:	00 
  79c535:	74 06                	je     79c53d <agentGoProject/common.GetDockerApiContainerList+0xbd>
  79c537:	48 8b 57 08          	mov    0x8(%rdi),%rdx
  79c53b:	eb 03                	jmp    79c540 <agentGoProject/common.GetDockerApiContainerList+0xc0>
  79c53d:	48 89 fa             	mov    %rdi,%rdx
  79c540:	48 89 74 24 70       	mov    %rsi,0x70(%rsp)
  79c545:	48 89 94 24 98 00 00 	mov    %rdx,0x98(%rsp)
  79c54c:	00 
  79c54d:	48 89 b4 24 a0 00 00 	mov    %rsi,0xa0(%rsp)
  79c554:	00 
  79c555:	48 8d 84 24 88 00 00 	lea    0x88(%rsp),%rax
  79c55c:	00 
  79c55d:	bb 02 00 00 00       	mov    $0x2,%ebx
  79c562:	48 89 d9             	mov    %rbx,%rcx
  79c565:	e8 b6 09 df ff       	call   58cf20 <log.Println>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:166
  79c56a:	48 8b 54 24 50       	mov    0x50(%rsp),%rdx
  79c56f:	48 8b 52 18          	mov    0x18(%rdx),%rdx
  79c573:	48 8b 44 24 70       	mov    0x70(%rsp),%rax
  79c578:	ff d2                	call   *%rdx
  79c57a:	48 89 84 24 80 00 00 	mov    %rax,0x80(%rsp)
  79c581:	00 
  79c582:	48 89 5c 24 68       	mov    %rbx,0x68(%rsp)
  79c587:	48 8d 05 32 14 08 00 	lea    0x81432(%rip),%rax        # 81d9c0 <type:*+0x5b9c0>
  79c58e:	e8 ed 16 c7 ff       	call   40dc80 <runtime.newobject>
  79c593:	48 8d 15 86 00 00 00 	lea    0x86(%rip),%rdx        # 79c620 <agentGoProject/common.GetDockerApiContainerList.func1>
  79c59a:	48 89 10             	mov    %rdx,(%rax)
  79c59d:	48 8b 54 24 68       	mov    0x68(%rsp),%rdx
  79c5a2:	48 89 50 10          	mov    %rdx,0x10(%rax)
  79c5a6:	83 3d a3 8f 43 00 00 	cmpl   $0x0,0x438fa3(%rip)        # bd5550 <runtime.writeBarrier>
  79c5ad:	75 0e                	jne    79c5bd <agentGoProject/common.GetDockerApiContainerList+0x13d>
  79c5af:	48 8b 8c 24 80 00 00 	mov    0x80(%rsp),%rcx
  79c5b6:	00 
  79c5b7:	48 89 48 08          	mov    %rcx,0x8(%rax)
  79c5bb:	eb 11                	jmp    79c5ce <agentGoProject/common.GetDockerApiContainerList+0x14e>
  79c5bd:	48 8d 78 08          	lea    0x8(%rax),%rdi
  79c5c1:	48 8b 8c 24 80 00 00 	mov    0x80(%rsp),%rcx
  79c5c8:	00 
  79c5c9:	e8 12 ab cc ff       	call   4670e0 <runtime.gcWriteBarrierCX>
  79c5ce:	e8 2d 3d ca ff       	call   440300 <runtime.newproc>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:169
  79c5d3:	48 8b 44 24 78       	mov    0x78(%rsp),%rax
  79c5d8:	48 8b 4c 24 60       	mov    0x60(%rsp),%rcx
  79c5dd:	48 8b 5c 24 58       	mov    0x58(%rsp),%rbx
  79c5e2:	48 8b ac 24 a8 00 00 	mov    0xa8(%rsp),%rbp
  79c5e9:	00 
  79c5ea:	48 81 c4 b0 00 00 00 	add    $0xb0,%rsp
  79c5f1:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:155
  79c5f2:	48 89 44 24 08       	mov    %rax,0x8(%rsp)
  79c5f7:	e8 24 8a cc ff       	call   465020 <runtime.morestack_noctxt.abi0>
  79c5fc:	48 8b 44 24 08       	mov    0x8(%rsp),%rax
  79c601:	e9 7a fe ff ff       	jmp    79c480 <agentGoProject/common.GetDockerApiContainerList>
  79c606:	cc                   	int3   
  79c607:	cc                   	int3   
  79c608:	cc                   	int3   
  79c609:	cc                   	int3   
  79c60a:	cc                   	int3   
  79c60b:	cc                   	int3   
  79c60c:	cc                   	int3   
  79c60d:	cc                   	int3   
  79c60e:	cc                   	int3   
  79c60f:	cc                   	int3   
  79c610:	cc                   	int3   
  79c611:	cc                   	int3   
  79c612:	cc                   	int3   
  79c613:	cc                   	int3   
  79c614:	cc                   	int3   
  79c615:	cc                   	int3   
  79c616:	cc                   	int3   
  79c617:	cc                   	int3   
  79c618:	cc                   	int3   
  79c619:	cc                   	int3   
  79c61a:	cc                   	int3   
  79c61b:	cc                   	int3   
  79c61c:	cc                   	int3   
  79c61d:	cc                   	int3   
  79c61e:	cc                   	int3   
  79c61f:	cc                   	int3   

000000000079c620 <agentGoProject/common.GetDockerApiContainerList.func1>:
agentGoProject/common.GetDockerApiContainerList.func1():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:166
  79c620:	49 3b 66 10          	cmp    0x10(%r14),%rsp
  79c624:	76 2e                	jbe    79c654 <agentGoProject/common.GetDockerApiContainerList.func1+0x34>
  79c626:	48 83 ec 18          	sub    $0x18,%rsp
  79c62a:	48 89 6c 24 10       	mov    %rbp,0x10(%rsp)
  79c62f:	48 8d 6c 24 10       	lea    0x10(%rsp),%rbp
  79c634:	4d 8b 66 20          	mov    0x20(%r14),%r12
  79c638:	4d 85 e4             	test   %r12,%r12
  79c63b:	75 1e                	jne    79c65b <agentGoProject/common.GetDockerApiContainerList.func1+0x3b>
  79c63d:	48 8b 42 08          	mov    0x8(%rdx),%rax
  79c641:	48 8b 5a 10          	mov    0x10(%rdx),%rbx
  79c645:	e8 76 b0 ff ff       	call   7976c0 <agentGoProject/common.ExecShellDownMsgSendDocker>
  79c64a:	48 8b 6c 24 10       	mov    0x10(%rsp),%rbp
  79c64f:	48 83 c4 18          	add    $0x18,%rsp
  79c653:	c3                   	ret    
  79c654:	e8 27 89 cc ff       	call   464f80 <runtime.morestack.abi0>
  79c659:	eb c5                	jmp    79c620 <agentGoProject/common.GetDockerApiContainerList.func1>
  79c65b:	4c 8d 6c 24 20       	lea    0x20(%rsp),%r13
  79c660:	4d 39 2c 24          	cmp    %r13,(%r12)
  79c664:	75 d7                	jne    79c63d <agentGoProject/common.GetDockerApiContainerList.func1+0x1d>
  79c666:	49 89 24 24          	mov    %rsp,(%r12)
  79c66a:	eb d1                	jmp    79c63d <agentGoProject/common.GetDockerApiContainerList.func1+0x1d>
  79c66c:	cc                   	int3   
  79c66d:	cc                   	int3   
  79c66e:	cc                   	int3   
  79c66f:	cc                   	int3   
  79c670:	cc                   	int3   
  79c671:	cc                   	int3   
  79c672:	cc                   	int3   
  79c673:	cc                   	int3   
  79c674:	cc                   	int3   
  79c675:	cc                   	int3   
  79c676:	cc                   	int3   
  79c677:	cc                   	int3   
  79c678:	cc                   	int3   
  79c679:	cc                   	int3   
  79c67a:	cc                   	int3   
  79c67b:	cc                   	int3   
  79c67c:	cc                   	int3   
  79c67d:	cc                   	int3   
  79c67e:	cc                   	int3   
  79c67f:	cc                   	int3   

000000000079c680 <agentGoProject/common.GetDockerByContainerId>:
agentGoProject/common.GetDockerByContainerId():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:190
  79c680:	4c 8d a4 24 88 fd ff 	lea    -0x278(%rsp),%r12
  79c687:	ff 
  79c688:	4d 3b 66 10          	cmp    0x10(%r14),%r12
  79c68c:	0f 86 ca 07 00 00    	jbe    79ce5c <agentGoProject/common.GetDockerByContainerId+0x7dc>
  79c692:	48 81 ec f8 02 00 00 	sub    $0x2f8,%rsp
  79c699:	48 89 ac 24 f0 02 00 	mov    %rbp,0x2f0(%rsp)
  79c6a0:	00 
  79c6a1:	48 8d ac 24 f0 02 00 	lea    0x2f0(%rsp),%rbp
  79c6a8:	00 
  79c6a9:	49 c7 c5 00 00 00 00 	mov    $0x0,%r13
  79c6b0:	4c 89 ac 24 e8 02 00 	mov    %r13,0x2e8(%rsp)
  79c6b7:	00 
  79c6b8:	48 89 9c 24 08 03 00 	mov    %rbx,0x308(%rsp)
  79c6bf:	00 
  79c6c0:	48 89 bc 24 18 03 00 	mov    %rdi,0x318(%rsp)
  79c6c7:	00 
  79c6c8:	48 89 8c 24 10 03 00 	mov    %rcx,0x310(%rsp)
  79c6cf:	00 
  79c6d0:	48 89 84 24 00 03 00 	mov    %rax,0x300(%rsp)
  79c6d7:	00 
  79c6d8:	c6 44 24 3f 00       	movb   $0x0,0x3f(%rsp)
  79c6dd:	44 0f 11 bc 24 18 01 	movups %xmm15,0x118(%rsp)
  79c6e4:	00 00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:191
  79c6e6:	48 8d 15 0b a9 10 00 	lea    0x10a90b(%rip),%rdx        # 8a6ff8 <go:func.*+0x918>
  79c6ed:	48 89 94 24 e8 02 00 	mov    %rdx,0x2e8(%rsp)
  79c6f4:	00 
  79c6f5:	c6 44 24 3f 01       	movb   $0x1,0x3f(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:201
  79c6fa:	44 0f 11 bc 24 70 01 	movups %xmm15,0x170(%rsp)
  79c701:	00 00 
  79c703:	48 8d bc 24 78 01 00 	lea    0x178(%rsp),%rdi
  79c70a:	00 
  79c70b:	48 8d 7f d0          	lea    -0x30(%rdi),%rdi
  79c70f:	66 0f 1f 84 00 00 00 	nopw   0x0(%rax,%rax,1)
  79c716:	00 00 
  79c718:	0f 1f 84 00 00 00 00 	nopl   0x0(%rax,%rax,1)
  79c71f:	00 
  79c720:	48 89 6c 24 f0       	mov    %rbp,-0x10(%rsp)
  79c725:	48 8d 6c 24 f0       	lea    -0x10(%rsp),%rbp
  79c72a:	e8 2a b0 cc ff       	call   467759 <runtime.duffzero+0x139>
  79c72f:	48 8b 6d 00          	mov    0x0(%rbp),%rbp
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:190
  79c733:	48 89 c6             	mov    %rax,%rsi
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:202
  79c736:	4c 89 c0             	mov    %r8,%rax
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:190
  79c739:	48 89 df             	mov    %rbx,%rdi
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:202
  79c73c:	48 89 f3             	mov    %rsi,%rbx
  79c73f:	48 89 f9             	mov    %rdi,%rcx
  79c742:	e8 79 07 00 00       	call   79cec0 <agentGoProject/common.GetMemByContainerId>
  79c747:	f2 0f 11 84 24 80 00 	movsd  %xmm0,0x80(%rsp)
  79c74e:	00 00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:204
  79c750:	48 85 c0             	test   %rax,%rax
  79c753:	7c 0a                	jl     79c75f <agentGoProject/common.GetDockerByContainerId+0xdf>
  79c755:	0f 57 c9             	xorps  %xmm1,%xmm1
  79c758:	f2 48 0f 2a c8       	cvtsi2sd %rax,%xmm1
  79c75d:	eb 18                	jmp    79c777 <agentGoProject/common.GetDockerByContainerId+0xf7>
  79c75f:	48 89 c1             	mov    %rax,%rcx
  79c762:	83 e0 01             	and    $0x1,%eax
  79c765:	48 d1 e9             	shr    %rcx
  79c768:	48 09 c1             	or     %rax,%rcx
  79c76b:	0f 57 c9             	xorps  %xmm1,%xmm1
  79c76e:	f2 48 0f 2a c9       	cvtsi2sd %rcx,%xmm1
  79c773:	f2 0f 58 c9          	addsd  %xmm1,%xmm1
  79c777:	f2 0f 10 05 39 28 17 	movsd  0x172839(%rip),%xmm0        # 90efb8 <$f64.3f50000000000000>
  79c77e:	00 
  79c77f:	f2 0f 59 c8          	mulsd  %xmm0,%xmm1
  79c783:	f2 0f 59 c8          	mulsd  %xmm0,%xmm1
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:205
  79c787:	0f 10 c1             	movups %xmm1,%xmm0
  79c78a:	e8 11 93 ff ff       	call   795aa0 <agentGoProject/common.FloatRound>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:206
  79c78f:	f2 0f 11 84 24 b8 01 	movsd  %xmm0,0x1b8(%rsp)
  79c796:	00 00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:208
  79c798:	f2 0f 10 8c 24 80 00 	movsd  0x80(%rsp),%xmm1
  79c79f:	00 00 
  79c7a1:	f2 0f 11 8c 24 e0 01 	movsd  %xmm1,0x1e0(%rsp)
  79c7a8:	00 00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:229
  79c7aa:	48 8b 84 24 10 03 00 	mov    0x310(%rsp),%rax
  79c7b1:	00 
  79c7b2:	48 8b 8c 24 18 03 00 	mov    0x318(%rsp),%rcx
  79c7b9:	00 
  79c7ba:	48 8b 94 24 08 03 00 	mov    0x308(%rsp),%rdx
  79c7c1:	00 
  79c7c2:	31 db                	xor    %ebx,%ebx
  79c7c4:	eb 09                	jmp    79c7cf <agentGoProject/common.GetDockerByContainerId+0x14f>
  79c7c6:	48 05 e0 00 00 00    	add    $0xe0,%rax
  79c7cc:	48 ff c3             	inc    %rbx
  79c7cf:	48 39 d9             	cmp    %rbx,%rcx
  79c7d2:	0f 8e 3d 01 00 00    	jle    79c915 <agentGoProject/common.GetDockerByContainerId+0x295>
  79c7d8:	48 8d bc 24 08 02 00 	lea    0x208(%rsp),%rdi
  79c7df:	00 
  79c7e0:	48 89 c6             	mov    %rax,%rsi
  79c7e3:	48 89 6c 24 f0       	mov    %rbp,-0x10(%rsp)
  79c7e8:	48 8d 6c 24 f0       	lea    -0x10(%rsp),%rbp
  79c7ed:	e8 6a b2 cc ff       	call   467a5c <runtime.duffcopy+0x2bc>
  79c7f2:	48 8b 6d 00          	mov    0x0(%rbp),%rbp
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:230
  79c7f6:	4c 8b 84 24 08 02 00 	mov    0x208(%rsp),%r8
  79c7fd:	00 
  79c7fe:	66 90                	xchg   %ax,%ax
  79c800:	48 39 94 24 10 02 00 	cmp    %rdx,0x210(%rsp)
  79c807:	00 
  79c808:	75 bc                	jne    79c7c6 <agentGoProject/common.GetDockerByContainerId+0x146>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:229
  79c80a:	48 89 9c 24 b8 00 00 	mov    %rbx,0xb8(%rsp)
  79c811:	00 
  79c812:	48 89 84 24 10 01 00 	mov    %rax,0x110(%rsp)
  79c819:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:230
  79c81a:	48 8b 84 24 00 03 00 	mov    0x300(%rsp),%rax
  79c821:	00 
  79c822:	4c 89 c3             	mov    %r8,%rbx
  79c825:	48 89 d1             	mov    %rdx,%rcx
  79c828:	e8 13 6c c6 ff       	call   403440 <runtime.memequal>
  79c82d:	84 c0                	test   %al,%al
  79c82f:	75 25                	jne    79c856 <agentGoProject/common.GetDockerByContainerId+0x1d6>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:229
  79c831:	48 8b 84 24 10 01 00 	mov    0x110(%rsp),%rax
  79c838:	00 
  79c839:	48 8b 8c 24 18 03 00 	mov    0x318(%rsp),%rcx
  79c840:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:230
  79c841:	48 8b 94 24 08 03 00 	mov    0x308(%rsp),%rdx
  79c848:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:229
  79c849:	48 8b 9c 24 b8 00 00 	mov    0xb8(%rsp),%rbx
  79c850:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:230
  79c851:	e9 70 ff ff ff       	jmp    79c7c6 <agentGoProject/common.GetDockerByContainerId+0x146>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:233
  79c856:	48 8b 94 24 30 02 00 	mov    0x230(%rsp),%rdx
  79c85d:	00 
  79c85e:	48 89 94 24 d8 00 00 	mov    %rdx,0xd8(%rsp)
  79c865:	00 
  79c866:	4c 8b 84 24 38 02 00 	mov    0x238(%rsp),%r8
  79c86d:	00 
  79c86e:	4c 89 44 24 60       	mov    %r8,0x60(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:234
  79c873:	4c 8b 8c 24 50 02 00 	mov    0x250(%rsp),%r9
  79c87a:	00 
  79c87b:	4c 89 8c 24 00 01 00 	mov    %r9,0x100(%rsp)
  79c882:	00 
  79c883:	4c 8b 94 24 58 02 00 	mov    0x258(%rsp),%r10
  79c88a:	00 
  79c88b:	4c 89 94 24 90 00 00 	mov    %r10,0x90(%rsp)
  79c892:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:235
  79c893:	4c 8b 9c 24 98 02 00 	mov    0x298(%rsp),%r11
  79c89a:	00 
  79c89b:	4c 89 9c 24 f8 00 00 	mov    %r11,0xf8(%rsp)
  79c8a2:	00 
  79c8a3:	4c 8b a4 24 a0 02 00 	mov    0x2a0(%rsp),%r12
  79c8aa:	00 
  79c8ab:	4c 89 a4 24 88 00 00 	mov    %r12,0x88(%rsp)
  79c8b2:	00 
./C:/Program Files/Go/src/time/time.go:1406
  79c8b3:	90                   	nop
./C:/Program Files/Go/src/time/time.go:1124
  79c8b4:	48 bb 00 f7 91 77 0e 	movabs $0xe7791f700,%rbx
  79c8bb:	00 00 00 
time.unixTime():
./C:/Program Files/Go/src/time/time.go:1124
  79c8be:	48 03 9c 24 60 02 00 	add    0x260(%rsp),%rbx
  79c8c5:	00 
  79c8c6:	48 8b 0d c3 b4 3f 00 	mov    0x3fb4c3(%rip),%rcx        # b97d90 <time.Local>
agentGoProject/common.GetDockerByContainerId():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:236
  79c8cd:	31 c0                	xor    %eax,%eax
  79c8cf:	48 8d 3d ce 75 0d 00 	lea    0xd75ce(%rip),%rdi        # 873ea4 <go:string.*+0x68dc>
  79c8d6:	be 13 00 00 00       	mov    $0x13,%esi
  79c8db:	0f 1f 44 00 00       	nopl   0x0(%rax,%rax,1)
  79c8e0:	e8 9b 1c d2 ff       	call   4be580 <time.Time.Format>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:262
  79c8e5:	48 89 84 24 f0 00 00 	mov    %rax,0xf0(%rsp)
  79c8ec:	00 
  79c8ed:	48 89 5c 24 78       	mov    %rbx,0x78(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:237
  79c8f2:	48 8b 94 24 68 02 00 	mov    0x268(%rsp),%rdx
  79c8f9:	00 
  79c8fa:	4c 8b 84 24 70 02 00 	mov    0x270(%rsp),%r8
  79c901:	00 
  79c902:	4c 89 84 24 b8 00 00 	mov    %r8,0xb8(%rsp)
  79c909:	00 
  79c90a:	31 c9                	xor    %ecx,%ecx
  79c90c:	31 f6                	xor    %esi,%esi
  79c90e:	31 ff                	xor    %edi,%edi
  79c910:	e9 22 03 00 00       	jmp    79cc37 <agentGoProject/common.GetDockerByContainerId+0x5b7>
  79c915:	31 c0                	xor    %eax,%eax
  79c917:	31 c9                	xor    %ecx,%ecx
  79c919:	31 d2                	xor    %edx,%edx
  79c91b:	31 db                	xor    %ebx,%ebx
  79c91d:	31 f6                	xor    %esi,%esi
  79c91f:	31 ff                	xor    %edi,%edi
  79c921:	45 31 c0             	xor    %r8d,%r8d
  79c924:	45 31 c9             	xor    %r9d,%r9d
  79c927:	0f 57 c0             	xorps  %xmm0,%xmm0
  79c92a:	45 31 d2             	xor    %r10d,%r10d
  79c92d:	45 31 db             	xor    %r11d,%r11d
  79c930:	45 31 e4             	xor    %r12d,%r12d
  79c933:	45 31 ed             	xor    %r13d,%r13d
  79c936:	41 bf 06 00 00 00    	mov    $0x6,%r15d
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:270
  79c93c:	48 8d 05 02 1c 0d 00 	lea    0xd1c02(%rip),%rax        # 86e545 <go:string.*+0xf7d>
  79c943:	48 89 84 24 f8 00 00 	mov    %rax,0xf8(%rsp)
  79c94a:	00 
  79c94b:	31 c0                	xor    %eax,%eax
  79c94d:	4c 89 bc 24 88 00 00 	mov    %r15,0x88(%rsp)
  79c954:	00 
./C:/Program Files/Go/src/strings/strings.go:456
  79c955:	48 83 f8 01          	cmp    $0x1,%rax
strings.HasSuffix():
./C:/Program Files/Go/src/strings/strings.go:456
  79c959:	7d 1b                	jge    79c976 <agentGoProject/common.GetDockerByContainerId+0x2f6>
agentGoProject/common.GetDockerByContainerId():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:252
  79c95b:	48 89 84 24 98 00 00 	mov    %rax,0x98(%rsp)
  79c962:	00 
  79c963:	31 c0                	xor    %eax,%eax
  79c965:	88 44 24 3e          	mov    %al,0x3e(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:253
  79c969:	48 8b 84 24 98 00 00 	mov    0x98(%rsp),%rax
  79c970:	00 
strings.HasSuffix():
./C:/Program Files/Go/src/strings/strings.go:456
  79c971:	e9 d6 00 00 00       	jmp    79ca4c <agentGoProject/common.GetDockerByContainerId+0x3cc>
agentGoProject/common.GetDockerByContainerId():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:261
  79c976:	48 89 b4 24 90 00 00 	mov    %rsi,0x90(%rsp)
  79c97d:	00 
  79c97e:	48 89 bc 24 00 01 00 	mov    %rdi,0x100(%rsp)
  79c985:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:252
  79c986:	48 89 44 24 50       	mov    %rax,0x50(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:262
  79c98b:	4c 89 44 24 78       	mov    %r8,0x78(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:252
  79c990:	48 89 8c 24 c8 00 00 	mov    %rcx,0xc8(%rsp)
  79c997:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:264
  79c998:	4c 89 54 24 40       	mov    %r10,0x40(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:260
  79c99d:	48 89 54 24 60       	mov    %rdx,0x60(%rsp)
  79c9a2:	48 89 9c 24 d8 00 00 	mov    %rbx,0xd8(%rsp)
  79c9a9:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:266
  79c9aa:	4c 89 64 24 58       	mov    %r12,0x58(%rsp)
  79c9af:	4c 89 ac 24 d0 00 00 	mov    %r13,0xd0(%rsp)
  79c9b6:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:264
  79c9b7:	4c 89 9c 24 c0 00 00 	mov    %r11,0xc0(%rsp)
  79c9be:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:262
  79c9bf:	4c 89 8c 24 f0 00 00 	mov    %r9,0xf0(%rsp)
  79c9c6:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:263
  79c9c7:	f2 0f 11 44 24 48    	movsd  %xmm0,0x48(%rsp)
strings.HasSuffix():
./C:/Program Files/Go/src/strings/strings.go:456
  79c9cd:	48 8d 14 08          	lea    (%rax,%rcx,1),%rdx
  79c9d1:	48 8d 52 ff          	lea    -0x1(%rdx),%rdx
  79c9d5:	48 8d 1d 5c 0d 17 00 	lea    0x170d5c(%rip),%rbx        # 90d738 <runtime.gcbits.*+0x2d8>
  79c9dc:	48 89 d0             	mov    %rdx,%rax
  79c9df:	b9 01 00 00 00       	mov    $0x1,%ecx
  79c9e4:	e8 57 6a c6 ff       	call   403440 <runtime.memequal>
agentGoProject/common.GetDockerByContainerId():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:259
  79c9e9:	48 8b 8c 24 c8 00 00 	mov    0xc8(%rsp),%rcx
  79c9f0:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:260
  79c9f1:	48 8b 54 24 60       	mov    0x60(%rsp),%rdx
  79c9f6:	48 8b 9c 24 d8 00 00 	mov    0xd8(%rsp),%rbx
  79c9fd:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:261
  79c9fe:	48 8b b4 24 90 00 00 	mov    0x90(%rsp),%rsi
  79ca05:	00 
  79ca06:	48 8b bc 24 00 01 00 	mov    0x100(%rsp),%rdi
  79ca0d:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:262
  79ca0e:	4c 8b 44 24 78       	mov    0x78(%rsp),%r8
  79ca13:	4c 8b 8c 24 f0 00 00 	mov    0xf0(%rsp),%r9
  79ca1a:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:264
  79ca1b:	4c 8b 54 24 40       	mov    0x40(%rsp),%r10
  79ca20:	4c 8b 9c 24 c0 00 00 	mov    0xc0(%rsp),%r11
  79ca27:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:266
  79ca28:	4c 8b 64 24 58       	mov    0x58(%rsp),%r12
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:269
  79ca2d:	4c 8b ac 24 d0 00 00 	mov    0xd0(%rsp),%r13
  79ca34:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:270
  79ca35:	4c 8b bc 24 88 00 00 	mov    0x88(%rsp),%r15
  79ca3c:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:263
  79ca3d:	f2 0f 10 44 24 48    	movsd  0x48(%rsp),%xmm0
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:252
  79ca43:	88 44 24 3e          	mov    %al,0x3e(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:253
  79ca47:	48 8b 44 24 50       	mov    0x50(%rsp),%rax
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:252
  79ca4c:	44 0f b6 7c 24 3e    	movzbl 0x3e(%rsp),%r15d
  79ca52:	45 84 ff             	test   %r15b,%r15b
  79ca55:	74 13                	je     79ca6a <agentGoProject/common.GetDockerByContainerId+0x3ea>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:253
  79ca57:	4c 8d 78 ff          	lea    -0x1(%rax),%r15
  79ca5b:	0f 1f 44 00 00       	nopl   0x0(%rax,%rax,1)
  79ca60:	4c 39 f8             	cmp    %r15,%rax
  79ca63:	73 08                	jae    79ca6d <agentGoProject/common.GetDockerByContainerId+0x3ed>
  79ca65:	e9 8f 01 00 00       	jmp    79cbf9 <agentGoProject/common.GetDockerByContainerId+0x579>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:256
  79ca6a:	49 89 c7             	mov    %rax,%r15
  79ca6d:	49 81 ff c8 00 00 00 	cmp    $0xc8,%r15
  79ca74:	7e 06                	jle    79ca7c <agentGoProject/common.GetDockerByContainerId+0x3fc>
  79ca76:	41 bf c8 00 00 00    	mov    $0xc8,%r15d
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:259
  79ca7c:	48 89 8c 24 70 01 00 	mov    %rcx,0x170(%rsp)
  79ca83:	00 
  79ca84:	4c 89 bc 24 78 01 00 	mov    %r15,0x178(%rsp)
  79ca8b:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:260
  79ca8c:	48 89 9c 24 80 01 00 	mov    %rbx,0x180(%rsp)
  79ca93:	00 
  79ca94:	48 89 94 24 88 01 00 	mov    %rdx,0x188(%rsp)
  79ca9b:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:261
  79ca9c:	48 89 bc 24 90 01 00 	mov    %rdi,0x190(%rsp)
  79caa3:	00 
  79caa4:	48 89 b4 24 98 01 00 	mov    %rsi,0x198(%rsp)
  79caab:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:262
  79caac:	4c 89 8c 24 a0 01 00 	mov    %r9,0x1a0(%rsp)
  79cab3:	00 
  79cab4:	4c 89 84 24 a8 01 00 	mov    %r8,0x1a8(%rsp)
  79cabb:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:263
  79cabc:	f2 0f 11 84 24 b0 01 	movsd  %xmm0,0x1b0(%rsp)
  79cac3:	00 00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:264
  79cac5:	4c 89 9c 24 c0 01 00 	mov    %r11,0x1c0(%rsp)
  79cacc:	00 
  79cacd:	4c 89 94 24 c8 01 00 	mov    %r10,0x1c8(%rsp)
  79cad4:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:266
  79cad5:	49 81 fc 96 00 00 00 	cmp    $0x96,%r12
  79cadc:	7e 06                	jle    79cae4 <agentGoProject/common.GetDockerByContainerId+0x464>
  79cade:	41 bc 96 00 00 00    	mov    $0x96,%r12d
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:269
  79cae4:	4c 89 ac 24 f8 01 00 	mov    %r13,0x1f8(%rsp)
  79caeb:	00 
  79caec:	4c 89 a4 24 00 02 00 	mov    %r12,0x200(%rsp)
  79caf3:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:270
  79caf4:	48 8b 8c 24 f8 00 00 	mov    0xf8(%rsp),%rcx
  79cafb:	00 
  79cafc:	48 89 8c 24 d0 01 00 	mov    %rcx,0x1d0(%rsp)
  79cb03:	00 
  79cb04:	48 8b 8c 24 88 00 00 	mov    0x88(%rsp),%rcx
  79cb0b:	00 
  79cb0c:	48 89 8c 24 d8 01 00 	mov    %rcx,0x1d8(%rsp)
  79cb13:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:271
  79cb14:	48 8d 05 25 cb 0b 00 	lea    0xbcb25(%rip),%rax        # 859640 <type:*+0x97640>
  79cb1b:	48 8d 9c 24 70 01 00 	lea    0x170(%rsp),%rbx
  79cb22:	00 
  79cb23:	e8 58 e7 c6 ff       	call   40b280 <runtime.convT>
  79cb28:	48 89 c3             	mov    %rax,%rbx
  79cb2b:	48 8d 05 0e cb 0b 00 	lea    0xbcb0e(%rip),%rax        # 859640 <type:*+0x97640>
  79cb32:	e8 29 aa d6 ff       	call   507560 <encoding/json.Marshal>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:272
  79cb37:	48 85 ff             	test   %rdi,%rdi
  79cb3a:	74 76                	je     79cbb2 <agentGoProject/common.GetDockerByContainerId+0x532>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:275
  79cb3c:	48 89 84 24 e8 00 00 	mov    %rax,0xe8(%rsp)
  79cb43:	00 
  79cb44:	48 89 5c 24 70       	mov    %rbx,0x70(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:273
  79cb49:	44 0f 11 bc 24 28 01 	movups %xmm15,0x128(%rsp)
  79cb50:	00 00 
  79cb52:	44 0f 11 bc 24 38 01 	movups %xmm15,0x138(%rsp)
  79cb59:	00 00 
  79cb5b:	48 8d 15 3e 51 05 00 	lea    0x5513e(%rip),%rdx        # 7f1ca0 <type:*+0x2fca0>
  79cb62:	48 89 94 24 28 01 00 	mov    %rdx,0x128(%rsp)
  79cb69:	00 
  79cb6a:	48 8d 15 bf 3b 17 00 	lea    0x173bbf(%rip),%rdx        # 910730 <gcmPoly+0x200>
  79cb71:	48 89 94 24 30 01 00 	mov    %rdx,0x130(%rsp)
  79cb78:	00 
  79cb79:	74 04                	je     79cb7f <agentGoProject/common.GetDockerByContainerId+0x4ff>
  79cb7b:	48 8b 7f 08          	mov    0x8(%rdi),%rdi
  79cb7f:	48 89 bc 24 38 01 00 	mov    %rdi,0x138(%rsp)
  79cb86:	00 
  79cb87:	48 89 b4 24 40 01 00 	mov    %rsi,0x140(%rsp)
  79cb8e:	00 
  79cb8f:	48 8d 84 24 28 01 00 	lea    0x128(%rsp),%rax
  79cb96:	00 
  79cb97:	bb 02 00 00 00       	mov    $0x2,%ebx
  79cb9c:	48 89 d9             	mov    %rbx,%rcx
  79cb9f:	90                   	nop
  79cba0:	e8 7b 03 df ff       	call   58cf20 <log.Println>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:275
  79cba5:	48 8b 84 24 e8 00 00 	mov    0xe8(%rsp),%rax
  79cbac:	00 
  79cbad:	48 8b 5c 24 70       	mov    0x70(%rsp),%rbx
  79cbb2:	48 89 d9             	mov    %rbx,%rcx
  79cbb5:	48 89 c3             	mov    %rax,%rbx
  79cbb8:	31 c0                	xor    %eax,%eax
  79cbba:	e8 21 3b cb ff       	call   4506e0 <runtime.slicebytetostring>
  79cbbf:	48 89 84 24 18 01 00 	mov    %rax,0x118(%rsp)
  79cbc6:	00 
  79cbc7:	48 89 9c 24 20 01 00 	mov    %rbx,0x120(%rsp)
  79cbce:	00 
  79cbcf:	c6 44 24 3f 00       	movb   $0x0,0x3f(%rsp)
  79cbd4:	e8 87 ea 01 00       	call   7bb660 <agentGoProject/common.GetDockerByContainerId.func1>
  79cbd9:	48 8b 84 24 18 01 00 	mov    0x118(%rsp),%rax
  79cbe0:	00 
  79cbe1:	48 8b 9c 24 20 01 00 	mov    0x120(%rsp),%rbx
  79cbe8:	00 
  79cbe9:	48 8b ac 24 f0 02 00 	mov    0x2f0(%rsp),%rbp
  79cbf0:	00 
  79cbf1:	48 81 c4 f8 02 00 00 	add    $0x2f8,%rsp
  79cbf8:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:253
  79cbf9:	4c 89 f9             	mov    %r15,%rcx
  79cbfc:	48 89 c2             	mov    %rax,%rdx
  79cbff:	90                   	nop
  79cc00:	e8 bb a8 cc ff       	call   4674c0 <runtime.panicSliceAlen>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:237
  79cc05:	48 8b 94 24 10 01 00 	mov    0x110(%rsp),%rdx
  79cc0c:	00 
  79cc0d:	48 83 c2 28          	add    $0x28,%rdx
  79cc11:	48 8b 8c 24 a8 00 00 	mov    0xa8(%rsp),%rcx
  79cc18:	00 
  79cc19:	48 ff c1             	inc    %rcx
  79cc1c:	4c 8b 84 24 b8 00 00 	mov    0xb8(%rsp),%r8
  79cc23:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:239
  79cc24:	48 89 de             	mov    %rbx,%rsi
  79cc27:	48 89 c7             	mov    %rax,%rdi
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:262
  79cc2a:	48 8b 84 24 f0 00 00 	mov    0xf0(%rsp),%rax
  79cc31:	00 
  79cc32:	48 8b 5c 24 78       	mov    0x78(%rsp),%rbx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:239
  79cc37:	48 89 bc 24 c8 00 00 	mov    %rdi,0xc8(%rsp)
  79cc3e:	00 
  79cc3f:	48 89 b4 24 b0 00 00 	mov    %rsi,0xb0(%rsp)
  79cc46:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:237
  79cc47:	4c 39 c1             	cmp    %r8,%rcx
  79cc4a:	0f 8d 0d 01 00 00    	jge    79cd5d <agentGoProject/common.GetDockerByContainerId+0x6dd>
  79cc50:	48 89 8c 24 a8 00 00 	mov    %rcx,0xa8(%rsp)
  79cc57:	00 
  79cc58:	48 89 94 24 10 01 00 	mov    %rdx,0x110(%rsp)
  79cc5f:	00 
  79cc60:	48 8b 0a             	mov    (%rdx),%rcx
  79cc63:	48 89 8c 24 48 01 00 	mov    %rcx,0x148(%rsp)
  79cc6a:	00 
  79cc6b:	0f 10 42 08          	movups 0x8(%rdx),%xmm0
  79cc6f:	0f 11 84 24 50 01 00 	movups %xmm0,0x150(%rsp)
  79cc76:	00 
  79cc77:	0f 10 42 18          	movups 0x18(%rdx),%xmm0
  79cc7b:	0f 11 84 24 60 01 00 	movups %xmm0,0x160(%rsp)
  79cc82:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:238
  79cc83:	0f b7 84 24 5a 01 00 	movzwl 0x15a(%rsp),%eax
  79cc8a:	00 
  79cc8b:	bb 0a 00 00 00       	mov    $0xa,%ebx
  79cc90:	e8 eb 0d ce ff       	call   47da80 <strconv.FormatInt>
  79cc95:	48 89 84 24 08 01 00 	mov    %rax,0x108(%rsp)
  79cc9c:	00 
  79cc9d:	48 89 9c 24 a0 00 00 	mov    %rbx,0xa0(%rsp)
  79cca4:	00 
  79cca5:	0f b7 8c 24 58 01 00 	movzwl 0x158(%rsp),%ecx
  79ccac:	00 
  79ccad:	48 89 c8             	mov    %rcx,%rax
  79ccb0:	bb 0a 00 00 00       	mov    $0xa,%ebx
  79ccb5:	e8 c6 0d ce ff       	call   47da80 <strconv.FormatInt>
  79ccba:	48 8b 8c 24 a0 00 00 	mov    0xa0(%rsp),%rcx
  79ccc1:	00 
  79ccc2:	48 8d 3d e7 0a 17 00 	lea    0x170ae7(%rip),%rdi        # 90d7b0 <runtime.gcbits.*+0x350>
  79ccc9:	be 01 00 00 00       	mov    $0x1,%esi
  79ccce:	49 89 c0             	mov    %rax,%r8
  79ccd1:	49 89 d9             	mov    %rbx,%r9
  79ccd4:	31 c0                	xor    %eax,%eax
  79ccd6:	48 8b 9c 24 08 01 00 	mov    0x108(%rsp),%rbx
  79ccdd:	00 
  79ccde:	66 90                	xchg   %ax,%ax
  79cce0:	e8 fb 36 cb ff       	call   4503e0 <runtime.concatstring3>
  79cce5:	48 89 84 24 e0 00 00 	mov    %rax,0xe0(%rsp)
  79ccec:	00 
  79cced:	48 89 5c 24 68       	mov    %rbx,0x68(%rsp)
./C:/Program Files/Go/src/strings/strings.go:59
  79ccf2:	48 89 c1             	mov    %rax,%rcx
strings.Contains():
./C:/Program Files/Go/src/strings/strings.go:59
  79ccf5:	48 89 df             	mov    %rbx,%rdi
  79ccf8:	48 8b 84 24 c8 00 00 	mov    0xc8(%rsp),%rax
  79ccff:	00 
  79cd00:	48 8b 9c 24 b0 00 00 	mov    0xb0(%rsp),%rbx
  79cd07:	00 
  79cd08:	e8 53 e9 d5 ff       	call   4fb660 <strings.Index>
  79cd0d:	48 85 c0             	test   %rax,%rax
agentGoProject/common.GetDockerByContainerId():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:239
  79cd10:	7c 15                	jl     79cd27 <agentGoProject/common.GetDockerByContainerId+0x6a7>
  79cd12:	48 8b 9c 24 b0 00 00 	mov    0xb0(%rsp),%rbx
  79cd19:	00 
  79cd1a:	48 8b 84 24 c8 00 00 	mov    0xc8(%rsp),%rax
  79cd21:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:240
  79cd22:	e9 de fe ff ff       	jmp    79cc05 <agentGoProject/common.GetDockerByContainerId+0x585>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:242
  79cd27:	31 c0                	xor    %eax,%eax
  79cd29:	48 8b 9c 24 c8 00 00 	mov    0xc8(%rsp),%rbx
  79cd30:	00 
  79cd31:	48 8b 8c 24 b0 00 00 	mov    0xb0(%rsp),%rcx
  79cd38:	00 
  79cd39:	48 8b bc 24 e0 00 00 	mov    0xe0(%rsp),%rdi
  79cd40:	00 
  79cd41:	48 8b 74 24 68       	mov    0x68(%rsp),%rsi
  79cd46:	4c 8d 05 eb 09 17 00 	lea    0x1709eb(%rip),%r8        # 90d738 <runtime.gcbits.*+0x2d8>
  79cd4d:	41 b9 01 00 00 00    	mov    $0x1,%r9d
  79cd53:	e8 88 36 cb ff       	call   4503e0 <runtime.concatstring3>
  79cd58:	e9 a8 fe ff ff       	jmp    79cc05 <agentGoProject/common.GetDockerByContainerId+0x585>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:245
  79cd5d:	48 8b 84 24 88 02 00 	mov    0x288(%rsp),%rax
  79cd64:	00 
  79cd65:	0f 57 c0             	xorps  %xmm0,%xmm0
  79cd68:	f2 48 0f 2a c0       	cvtsi2sd %rax,%xmm0
  79cd6d:	f2 0f 10 0d 43 22 17 	movsd  0x172243(%rip),%xmm1        # 90efb8 <$f64.3f50000000000000>
  79cd74:	00 
  79cd75:	f2 0f 59 c1          	mulsd  %xmm1,%xmm0
  79cd79:	f2 0f 59 c1          	mulsd  %xmm1,%xmm0
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:246
  79cd7d:	0f 1f 00             	nopl   (%rax)
  79cd80:	e8 1b 8d ff ff       	call   795aa0 <agentGoProject/common.FloatRound>
  79cd85:	f2 0f 11 44 24 48    	movsd  %xmm0,0x48(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:247
  79cd8b:	48 8b 84 24 a8 02 00 	mov    0x2a8(%rsp),%rax
  79cd92:	00 
  79cd93:	48 89 84 24 c0 00 00 	mov    %rax,0xc0(%rsp)
  79cd9a:	00 
  79cd9b:	48 8b 8c 24 b0 02 00 	mov    0x2b0(%rsp),%rcx
  79cda2:	00 
  79cda3:	48 89 4c 24 40       	mov    %rcx,0x40(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:248
  79cda8:	48 8b 94 24 18 02 00 	mov    0x218(%rsp),%rdx
  79cdaf:	00 
  79cdb0:	48 8b 9c 24 20 02 00 	mov    0x220(%rsp),%rbx
  79cdb7:	00 
  79cdb8:	48 8b b4 24 28 02 00 	mov    0x228(%rsp),%rsi
  79cdbf:	00 
  79cdc0:	48 8d 3d 71 09 17 00 	lea    0x170971(%rip),%rdi        # 90d738 <runtime.gcbits.*+0x2d8>
  79cdc7:	48 89 d0             	mov    %rdx,%rax
  79cdca:	48 89 f1             	mov    %rsi,%rcx
  79cdcd:	be 01 00 00 00       	mov    $0x1,%esi
  79cdd2:	e8 09 b8 d5 ff       	call   4f85e0 <strings.Join>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:252
  79cdd7:	48 8b 8c 24 c8 00 00 	mov    0xc8(%rsp),%rcx
  79cdde:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:260
  79cddf:	48 8b 54 24 60       	mov    0x60(%rsp),%rdx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:261
  79cde4:	48 8b b4 24 90 00 00 	mov    0x90(%rsp),%rsi
  79cdeb:	00 
  79cdec:	48 8b bc 24 00 01 00 	mov    0x100(%rsp),%rdi
  79cdf3:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:262
  79cdf4:	4c 8b 44 24 78       	mov    0x78(%rsp),%r8
  79cdf9:	4c 8b 8c 24 f0 00 00 	mov    0xf0(%rsp),%r9
  79ce00:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:263
  79ce01:	f2 0f 10 44 24 48    	movsd  0x48(%rsp),%xmm0
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:264
  79ce07:	4c 8b 54 24 40       	mov    0x40(%rsp),%r10
  79ce0c:	4c 8b 9c 24 c0 00 00 	mov    0xc0(%rsp),%r11
  79ce13:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:266
  79ce14:	49 89 dc             	mov    %rbx,%r12
  79ce17:	49 89 c5             	mov    %rax,%r13
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:270
  79ce1a:	4c 8b bc 24 88 00 00 	mov    0x88(%rsp),%r15
  79ce21:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:252
  79ce22:	48 8b 84 24 b0 00 00 	mov    0xb0(%rsp),%rax
  79ce29:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:260
  79ce2a:	48 8b 9c 24 d8 00 00 	mov    0xd8(%rsp),%rbx
  79ce31:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:249
  79ce32:	e9 16 fb ff ff       	jmp    79c94d <agentGoProject/common.GetDockerByContainerId+0x2cd>
  79ce37:	e8 e4 6e c9 ff       	call   433d20 <runtime.deferreturn>
  79ce3c:	48 8b 84 24 18 01 00 	mov    0x118(%rsp),%rax
  79ce43:	00 
  79ce44:	48 8b 9c 24 20 01 00 	mov    0x120(%rsp),%rbx
  79ce4b:	00 
  79ce4c:	48 8b ac 24 f0 02 00 	mov    0x2f0(%rsp),%rbp
  79ce53:	00 
  79ce54:	48 81 c4 f8 02 00 00 	add    $0x2f8,%rsp
  79ce5b:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:190
  79ce5c:	48 89 44 24 08       	mov    %rax,0x8(%rsp)
  79ce61:	48 89 5c 24 10       	mov    %rbx,0x10(%rsp)
  79ce66:	48 89 4c 24 18       	mov    %rcx,0x18(%rsp)
  79ce6b:	48 89 7c 24 20       	mov    %rdi,0x20(%rsp)
  79ce70:	48 89 74 24 28       	mov    %rsi,0x28(%rsp)
  79ce75:	4c 89 44 24 30       	mov    %r8,0x30(%rsp)
  79ce7a:	e8 a1 81 cc ff       	call   465020 <runtime.morestack_noctxt.abi0>
  79ce7f:	48 8b 44 24 08       	mov    0x8(%rsp),%rax
  79ce84:	48 8b 5c 24 10       	mov    0x10(%rsp),%rbx
  79ce89:	48 8b 4c 24 18       	mov    0x18(%rsp),%rcx
  79ce8e:	48 8b 7c 24 20       	mov    0x20(%rsp),%rdi
  79ce93:	48 8b 74 24 28       	mov    0x28(%rsp),%rsi
  79ce98:	4c 8b 44 24 30       	mov    0x30(%rsp),%r8
  79ce9d:	0f 1f 00             	nopl   (%rax)
  79cea0:	e9 db f7 ff ff       	jmp    79c680 <agentGoProject/common.GetDockerByContainerId>
  79cea5:	cc                   	int3   
  79cea6:	cc                   	int3   
  79cea7:	cc                   	int3   
  79cea8:	cc                   	int3   
  79cea9:	cc                   	int3   
  79ceaa:	cc                   	int3   
  79ceab:	cc                   	int3   
  79ceac:	cc                   	int3   
  79cead:	cc                   	int3   
  79ceae:	cc                   	int3   
  79ceaf:	cc                   	int3   
  79ceb0:	cc                   	int3   
  79ceb1:	cc                   	int3   
  79ceb2:	cc                   	int3   
  79ceb3:	cc                   	int3   
  79ceb4:	cc                   	int3   
  79ceb5:	cc                   	int3   
  79ceb6:	cc                   	int3   
  79ceb7:	cc                   	int3   
  79ceb8:	cc                   	int3   
  79ceb9:	cc                   	int3   
  79ceba:	cc                   	int3   
  79cebb:	cc                   	int3   
  79cebc:	cc                   	int3   
  79cebd:	cc                   	int3   
  79cebe:	cc                   	int3   
  79cebf:	cc                   	int3   

000000000079cec0 <agentGoProject/common.GetMemByContainerId>:
agentGoProject/common.GetMemByContainerId():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:282
  79cec0:	4c 8d 64 24 c8       	lea    -0x38(%rsp),%r12
  79cec5:	4d 3b 66 10          	cmp    0x10(%r14),%r12
  79cec9:	0f 86 e8 03 00 00    	jbe    79d2b7 <agentGoProject/common.GetMemByContainerId+0x3f7>
  79cecf:	48 81 ec b8 00 00 00 	sub    $0xb8,%rsp
  79ced6:	48 89 ac 24 b0 00 00 	mov    %rbp,0xb0(%rsp)
  79cedd:	00 
  79cede:	48 8d ac 24 b0 00 00 	lea    0xb0(%rsp),%rbp
  79cee5:	00 
  79cee6:	48 89 9c 24 c8 00 00 	mov    %rbx,0xc8(%rsp)
  79ceed:	00 
  79ceee:	44 0f 11 bc 24 a0 00 	movups %xmm15,0xa0(%rsp)
  79cef5:	00 00 
  79cef7:	c6 44 24 37 00       	movb   $0x0,0x37(%rsp)
  79cefc:	48 c7 44 24 40 00 00 	movq   $0x0,0x40(%rsp)
  79cf03:	00 00 
  79cf05:	0f 57 c0             	xorps  %xmm0,%xmm0
  79cf08:	f2 0f 11 44 24 38    	movsd  %xmm0,0x38(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:283
  79cf0e:	48 8d 15 43 a1 10 00 	lea    0x10a143(%rip),%rdx        # 8a7058 <go:func.*+0x978>
  79cf15:	48 89 94 24 a8 00 00 	mov    %rdx,0xa8(%rsp)
  79cf1c:	00 
  79cf1d:	c6 44 24 37 01       	movb   $0x1,0x37(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:293
  79cf22:	48 8b 15 c7 77 40 00 	mov    0x4077c7(%rip),%rdx        # ba46f0 <golang.org/x/net/context.background>
  79cf29:	4c 8b 0d c8 77 40 00 	mov    0x4077c8(%rip),%r9        # ba46f8 <golang.org/x/net/context.background+0x8>
  79cf30:	48 89 df             	mov    %rbx,%rdi
  79cf33:	48 89 ce             	mov    %rcx,%rsi
  79cf36:	45 31 c0             	xor    %r8d,%r8d
  79cf39:	48 89 d3             	mov    %rdx,%rbx
  79cf3c:	4c 89 c9             	mov    %r9,%rcx
  79cf3f:	90                   	nop
  79cf40:	e8 3b 9c fa ff       	call   746b80 <github.com/docker/docker/client.(*Client).ContainerStats>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:294
  79cf45:	48 85 f6             	test   %rsi,%rsi
  79cf48:	0f 84 94 00 00 00    	je     79cfe2 <agentGoProject/common.GetMemByContainerId+0x122>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:295
  79cf4e:	44 0f 11 bc 24 80 00 	movups %xmm15,0x80(%rsp)
  79cf55:	00 00 
  79cf57:	44 0f 11 bc 24 90 00 	movups %xmm15,0x90(%rsp)
  79cf5e:	00 00 
  79cf60:	48 8d 15 39 4d 05 00 	lea    0x54d39(%rip),%rdx        # 7f1ca0 <type:*+0x2fca0>
  79cf67:	48 89 94 24 80 00 00 	mov    %rdx,0x80(%rsp)
  79cf6e:	00 
  79cf6f:	48 8d 15 ca 2f 17 00 	lea    0x172fca(%rip),%rdx        # 90ff40 <runtime.buildVersion.str+0xca0>
  79cf76:	48 89 94 24 88 00 00 	mov    %rdx,0x88(%rsp)
  79cf7d:	00 
  79cf7e:	66 90                	xchg   %ax,%ax
  79cf80:	74 04                	je     79cf86 <agentGoProject/common.GetMemByContainerId+0xc6>
  79cf82:	48 8b 76 08          	mov    0x8(%rsi),%rsi
  79cf86:	48 89 b4 24 90 00 00 	mov    %rsi,0x90(%rsp)
  79cf8d:	00 
  79cf8e:	4c 89 84 24 98 00 00 	mov    %r8,0x98(%rsp)
  79cf95:	00 
  79cf96:	48 8d 84 24 80 00 00 	lea    0x80(%rsp),%rax
  79cf9d:	00 
  79cf9e:	bb 02 00 00 00       	mov    $0x2,%ebx
  79cfa3:	48 89 d9             	mov    %rbx,%rcx
  79cfa6:	e8 75 ff de ff       	call   58cf20 <log.Println>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:296
  79cfab:	48 c7 44 24 40 00 00 	movq   $0x0,0x40(%rsp)
  79cfb2:	00 00 
  79cfb4:	0f 57 c0             	xorps  %xmm0,%xmm0
  79cfb7:	f2 0f 11 44 24 38    	movsd  %xmm0,0x38(%rsp)
  79cfbd:	c6 44 24 37 00       	movb   $0x0,0x37(%rsp)
  79cfc2:	e8 39 e6 01 00       	call   7bb600 <agentGoProject/common.GetMemByContainerId.func1>
  79cfc7:	48 8b 44 24 40       	mov    0x40(%rsp),%rax
  79cfcc:	f2 0f 10 44 24 38    	movsd  0x38(%rsp),%xmm0
  79cfd2:	48 8b ac 24 b0 00 00 	mov    0xb0(%rsp),%rbp
  79cfd9:	00 
  79cfda:	48 81 c4 b8 00 00 00 	add    $0xb8,%rsp
  79cfe1:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:298
  79cfe2:	48 89 5c 24 50       	mov    %rbx,0x50(%rsp)
  79cfe7:	48 89 44 24 58       	mov    %rax,0x58(%rsp)
  79cfec:	84 00                	test   %al,(%rax)
  79cfee:	44 0f 11 7c 24 68    	movups %xmm15,0x68(%rsp)
  79cff4:	48 c7 44 24 78 00 00 	movq   $0x0,0x78(%rsp)
  79cffb:	00 00 
  79cffd:	48 8d 0d dc 02 00 00 	lea    0x2dc(%rip),%rcx        # 79d2e0 <agentGoProject/common.GetMemByContainerId.func2>
  79d004:	48 89 4c 24 68       	mov    %rcx,0x68(%rsp)
  79d009:	48 89 44 24 70       	mov    %rax,0x70(%rsp)
  79d00e:	48 89 5c 24 78       	mov    %rbx,0x78(%rsp)
  79d013:	48 8d 4c 24 68       	lea    0x68(%rsp),%rcx
  79d018:	48 89 8c 24 a0 00 00 	mov    %rcx,0xa0(%rsp)
  79d01f:	00 
  79d020:	c6 44 24 37 03       	movb   $0x3,0x37(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:300
  79d025:	48 8d 05 d4 6f 04 00 	lea    0x46fd4(%rip),%rax        # 7e4000 <type:*+0x22000>
  79d02c:	e8 4f 0c c7 ff       	call   40dc80 <runtime.newobject>
  79d031:	48 89 44 24 60       	mov    %rax,0x60(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:301
  79d036:	48 8b 5c 24 58       	mov    0x58(%rsp),%rbx
  79d03b:	48 8d 05 1e 7a 07 00 	lea    0x77a1e(%rip),%rax        # 814a60 <type:*+0x52a60>
  79d042:	e8 19 e6 c6 ff       	call   40b660 <runtime.convI2I>
  79d047:	48 89 44 24 58       	mov    %rax,0x58(%rsp)
./C:/Program Files/Go/src/encoding/json/stream.go:32
  79d04c:	48 8d 05 0d 8c 0b 00 	lea    0xb8c0d(%rip),%rax        # 855c60 <type:*+0x93c60>
encoding/json.NewDecoder():
./C:/Program Files/Go/src/encoding/json/stream.go:32
  79d053:	e8 28 0c c7 ff       	call   40dc80 <runtime.newobject>
  79d058:	48 8b 4c 24 58       	mov    0x58(%rsp),%rcx
  79d05d:	48 89 08             	mov    %rcx,(%rax)
  79d060:	83 3d e9 84 43 00 00 	cmpl   $0x0,0x4384e9(%rip)        # bd5550 <runtime.writeBarrier>
  79d067:	75 0b                	jne    79d074 <agentGoProject/common.GetMemByContainerId+0x1b4>
  79d069:	48 8b 54 24 50       	mov    0x50(%rsp),%rdx
  79d06e:	48 89 50 08          	mov    %rdx,0x8(%rax)
  79d072:	eb 11                	jmp    79d085 <agentGoProject/common.GetMemByContainerId+0x1c5>
  79d074:	48 8d 78 08          	lea    0x8(%rax),%rdi
  79d078:	48 8b 54 24 50       	mov    0x50(%rsp),%rdx
  79d07d:	0f 1f 00             	nopl   (%rax)
  79d080:	e8 7b a0 cc ff       	call   467100 <runtime.gcWriteBarrierDX>
agentGoProject/common.GetMemByContainerId():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:302
  79d085:	48 8d 1d 74 43 04 00 	lea    0x44374(%rip),%rbx        # 7e1400 <type:*+0x1f400>
  79d08c:	48 8b 4c 24 60       	mov    0x60(%rsp),%rcx
  79d091:	e8 ea 60 d7 ff       	call   513180 <encoding/json.(*Decoder).Decode>
  79d096:	48 85 c0             	test   %rax,%rax
  79d099:	0f 84 a4 00 00 00    	je     79d143 <agentGoProject/common.GetMemByContainerId+0x283>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:303
  79d09f:	44 0f 11 bc 24 80 00 	movups %xmm15,0x80(%rsp)
  79d0a6:	00 00 
  79d0a8:	44 0f 11 bc 24 90 00 	movups %xmm15,0x90(%rsp)
  79d0af:	00 00 
  79d0b1:	48 8d 15 e8 4b 05 00 	lea    0x54be8(%rip),%rdx        # 7f1ca0 <type:*+0x2fca0>
  79d0b8:	48 89 94 24 80 00 00 	mov    %rdx,0x80(%rsp)
  79d0bf:	00 
  79d0c0:	48 8d 15 79 2e 17 00 	lea    0x172e79(%rip),%rdx        # 90ff40 <runtime.buildVersion.str+0xca0>
  79d0c7:	48 89 94 24 88 00 00 	mov    %rdx,0x88(%rsp)
  79d0ce:	00 
  79d0cf:	74 04                	je     79d0d5 <agentGoProject/common.GetMemByContainerId+0x215>
  79d0d1:	48 8b 40 08          	mov    0x8(%rax),%rax
  79d0d5:	48 89 84 24 90 00 00 	mov    %rax,0x90(%rsp)
  79d0dc:	00 
  79d0dd:	48 89 9c 24 98 00 00 	mov    %rbx,0x98(%rsp)
  79d0e4:	00 
  79d0e5:	48 8d 84 24 80 00 00 	lea    0x80(%rsp),%rax
  79d0ec:	00 
  79d0ed:	bb 02 00 00 00       	mov    $0x2,%ebx
  79d0f2:	48 89 d9             	mov    %rbx,%rcx
  79d0f5:	e8 26 fe de ff       	call   58cf20 <log.Println>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:304
  79d0fa:	48 c7 44 24 40 00 00 	movq   $0x0,0x40(%rsp)
  79d101:	00 00 
  79d103:	0f 57 c0             	xorps  %xmm0,%xmm0
  79d106:	f2 0f 11 44 24 38    	movsd  %xmm0,0x38(%rsp)
  79d10c:	c6 44 24 37 01       	movb   $0x1,0x37(%rsp)
  79d111:	48 8b 94 24 a0 00 00 	mov    0xa0(%rsp),%rdx
  79d118:	00 
  79d119:	48 8b 32             	mov    (%rdx),%rsi
  79d11c:	ff d6                	call   *%rsi
  79d11e:	c6 44 24 37 00       	movb   $0x0,0x37(%rsp)
  79d123:	e8 d8 e4 01 00       	call   7bb600 <agentGoProject/common.GetMemByContainerId.func1>
  79d128:	48 8b 44 24 40       	mov    0x40(%rsp),%rax
  79d12d:	f2 0f 10 44 24 38    	movsd  0x38(%rsp),%xmm0
  79d133:	48 8b ac 24 b0 00 00 	mov    0xb0(%rsp),%rbp
  79d13a:	00 
  79d13b:	48 81 c4 b8 00 00 00 	add    $0xb8,%rsp
  79d142:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:308
  79d143:	48 8b 44 24 60       	mov    0x60(%rsp),%rax
  79d148:	48 8b 00             	mov    (%rax),%rax
  79d14b:	48 83 b8 f8 01 00 00 	cmpq   $0x0,0x1f8(%rax)
  79d152:	00 
  79d153:	74 09                	je     79d15e <agentGoProject/common.GetMemByContainerId+0x29e>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:312
  79d155:	48 8b 88 d8 01 00 00 	mov    0x1d8(%rax),%rcx
  79d15c:	eb 02                	jmp    79d160 <agentGoProject/common.GetMemByContainerId+0x2a0>
  79d15e:	31 c9                	xor    %ecx,%ecx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:315
  79d160:	48 8b 98 28 01 00 00 	mov    0x128(%rax),%rbx
  79d167:	48 2b 98 80 01 00 00 	sub    0x180(%rax),%rbx
  79d16e:	48 85 db             	test   %rbx,%rbx
  79d171:	7c 0a                	jl     79d17d <agentGoProject/common.GetMemByContainerId+0x2bd>
  79d173:	0f 57 c9             	xorps  %xmm1,%xmm1
  79d176:	f2 48 0f 2a cb       	cvtsi2sd %rbx,%xmm1
  79d17b:	eb 18                	jmp    79d195 <agentGoProject/common.GetMemByContainerId+0x2d5>
  79d17d:	48 89 de             	mov    %rbx,%rsi
  79d180:	48 d1 eb             	shr    %rbx
  79d183:	83 e6 01             	and    $0x1,%esi
  79d186:	48 09 de             	or     %rbx,%rsi
  79d189:	0f 57 c9             	xorps  %xmm1,%xmm1
  79d18c:	f2 48 0f 2a ce       	cvtsi2sd %rsi,%xmm1
  79d191:	f2 0f 58 c9          	addsd  %xmm1,%xmm1
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:316
  79d195:	48 8b 98 58 01 00 00 	mov    0x158(%rax),%rbx
  79d19c:	48 2b 98 b0 01 00 00 	sub    0x1b0(%rax),%rbx
  79d1a3:	48 85 db             	test   %rbx,%rbx
  79d1a6:	7c 0a                	jl     79d1b2 <agentGoProject/common.GetMemByContainerId+0x2f2>
  79d1a8:	0f 57 d2             	xorps  %xmm2,%xmm2
  79d1ab:	f2 48 0f 2a d3       	cvtsi2sd %rbx,%xmm2
  79d1b0:	eb 18                	jmp    79d1ca <agentGoProject/common.GetMemByContainerId+0x30a>
  79d1b2:	48 89 de             	mov    %rbx,%rsi
  79d1b5:	48 d1 eb             	shr    %rbx
  79d1b8:	83 e6 01             	and    $0x1,%esi
  79d1bb:	48 09 de             	or     %rbx,%rsi
  79d1be:	0f 57 d2             	xorps  %xmm2,%xmm2
  79d1c1:	f2 48 0f 2a d6       	cvtsi2sd %rsi,%xmm2
  79d1c6:	f2 0f 58 d2          	addsd  %xmm2,%xmm2
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:317
  79d1ca:	0f 57 db             	xorps  %xmm3,%xmm3
  79d1cd:	66 0f 2e d3          	ucomisd %xmm3,%xmm2
  79d1d1:	75 44                	jne    79d217 <agentGoProject/common.GetMemByContainerId+0x357>
  79d1d3:	7a 42                	jp     79d217 <agentGoProject/common.GetMemByContainerId+0x357>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:318
  79d1d5:	48 89 4c 24 40       	mov    %rcx,0x40(%rsp)
  79d1da:	f2 0f 11 5c 24 38    	movsd  %xmm3,0x38(%rsp)
  79d1e0:	c6 44 24 37 01       	movb   $0x1,0x37(%rsp)
  79d1e5:	48 8b 94 24 a0 00 00 	mov    0xa0(%rsp),%rdx
  79d1ec:	00 
  79d1ed:	48 8b 02             	mov    (%rdx),%rax
  79d1f0:	ff d0                	call   *%rax
  79d1f2:	c6 44 24 37 00       	movb   $0x0,0x37(%rsp)
  79d1f7:	e8 04 e4 01 00       	call   7bb600 <agentGoProject/common.GetMemByContainerId.func1>
  79d1fc:	48 8b 44 24 40       	mov    0x40(%rsp),%rax
  79d201:	f2 0f 10 44 24 38    	movsd  0x38(%rsp),%xmm0
  79d207:	48 8b ac 24 b0 00 00 	mov    0xb0(%rsp),%rbp
  79d20e:	00 
  79d20f:	48 81 c4 b8 00 00 00 	add    $0xb8,%rsp
  79d216:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:326
  79d217:	48 89 4c 24 48       	mov    %rcx,0x48(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:320
  79d21c:	48 8b 80 38 01 00 00 	mov    0x138(%rax),%rax
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:321
  79d223:	48 85 c0             	test   %rax,%rax
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:324
  79d226:	b9 01 00 00 00       	mov    $0x1,%ecx
  79d22b:	48 0f 44 c1          	cmove  %rcx,%rax
  79d22f:	f2 0f 5e ca          	divsd  %xmm2,%xmm1
  79d233:	0f 57 c0             	xorps  %xmm0,%xmm0
  79d236:	f2 48 0f 2a c0       	cvtsi2sd %rax,%xmm0
  79d23b:	f2 0f 59 c1          	mulsd  %xmm1,%xmm0
  79d23f:	f2 0f 10 0d 79 1e 17 	movsd  0x171e79(%rip),%xmm1        # 90f0c0 <$f64.4059000000000000>
  79d246:	00 
  79d247:	f2 0f 59 c1          	mulsd  %xmm1,%xmm0
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:325
  79d24b:	e8 50 88 ff ff       	call   795aa0 <agentGoProject/common.FloatRound>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:326
  79d250:	48 8b 44 24 48       	mov    0x48(%rsp),%rax
  79d255:	48 89 44 24 40       	mov    %rax,0x40(%rsp)
  79d25a:	f2 0f 11 44 24 38    	movsd  %xmm0,0x38(%rsp)
  79d260:	c6 44 24 37 01       	movb   $0x1,0x37(%rsp)
  79d265:	48 8b 94 24 a0 00 00 	mov    0xa0(%rsp),%rdx
  79d26c:	00 
  79d26d:	48 8b 02             	mov    (%rdx),%rax
  79d270:	ff d0                	call   *%rax
  79d272:	c6 44 24 37 00       	movb   $0x0,0x37(%rsp)
  79d277:	e8 84 e3 01 00       	call   7bb600 <agentGoProject/common.GetMemByContainerId.func1>
  79d27c:	48 8b 44 24 40       	mov    0x40(%rsp),%rax
  79d281:	f2 0f 10 44 24 38    	movsd  0x38(%rsp),%xmm0
  79d287:	48 8b ac 24 b0 00 00 	mov    0xb0(%rsp),%rbp
  79d28e:	00 
  79d28f:	48 81 c4 b8 00 00 00 	add    $0xb8,%rsp
  79d296:	c3                   	ret    
  79d297:	e8 84 6a c9 ff       	call   433d20 <runtime.deferreturn>
  79d29c:	48 8b 44 24 40       	mov    0x40(%rsp),%rax
  79d2a1:	f2 0f 10 44 24 38    	movsd  0x38(%rsp),%xmm0
  79d2a7:	48 8b ac 24 b0 00 00 	mov    0xb0(%rsp),%rbp
  79d2ae:	00 
  79d2af:	48 81 c4 b8 00 00 00 	add    $0xb8,%rsp
  79d2b6:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:282
  79d2b7:	48 89 44 24 08       	mov    %rax,0x8(%rsp)
  79d2bc:	48 89 5c 24 10       	mov    %rbx,0x10(%rsp)
  79d2c1:	48 89 4c 24 18       	mov    %rcx,0x18(%rsp)
  79d2c6:	e8 55 7d cc ff       	call   465020 <runtime.morestack_noctxt.abi0>
  79d2cb:	48 8b 44 24 08       	mov    0x8(%rsp),%rax
  79d2d0:	48 8b 5c 24 10       	mov    0x10(%rsp),%rbx
  79d2d5:	48 8b 4c 24 18       	mov    0x18(%rsp),%rcx
  79d2da:	e9 e1 fb ff ff       	jmp    79cec0 <agentGoProject/common.GetMemByContainerId>
  79d2df:	cc                   	int3   

000000000079d2e0 <agentGoProject/common.GetMemByContainerId.func2>:
agentGoProject/common.GetMemByContainerId.func2():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:298
  79d2e0:	49 3b 66 10          	cmp    0x10(%r14),%rsp
  79d2e4:	76 2f                	jbe    79d315 <agentGoProject/common.GetMemByContainerId.func2+0x35>
  79d2e6:	48 83 ec 10          	sub    $0x10,%rsp
  79d2ea:	48 89 6c 24 08       	mov    %rbp,0x8(%rsp)
  79d2ef:	48 8d 6c 24 08       	lea    0x8(%rsp),%rbp
  79d2f4:	4d 8b 66 20          	mov    0x20(%r14),%r12
  79d2f8:	4d 85 e4             	test   %r12,%r12
  79d2fb:	75 1f                	jne    79d31c <agentGoProject/common.GetMemByContainerId.func2+0x3c>
  79d2fd:	48 8b 4a 08          	mov    0x8(%rdx),%rcx
  79d301:	48 8b 42 10          	mov    0x10(%rdx),%rax
  79d305:	48 8b 49 18          	mov    0x18(%rcx),%rcx
  79d309:	ff d1                	call   *%rcx
  79d30b:	48 8b 6c 24 08       	mov    0x8(%rsp),%rbp
  79d310:	48 83 c4 10          	add    $0x10,%rsp
  79d314:	c3                   	ret    
  79d315:	e8 66 7c cc ff       	call   464f80 <runtime.morestack.abi0>
  79d31a:	eb c4                	jmp    79d2e0 <agentGoProject/common.GetMemByContainerId.func2>
  79d31c:	4c 8d 6c 24 18       	lea    0x18(%rsp),%r13
  79d321:	4d 39 2c 24          	cmp    %r13,(%r12)
  79d325:	75 d6                	jne    79d2fd <agentGoProject/common.GetMemByContainerId.func2+0x1d>
  79d327:	49 89 24 24          	mov    %rsp,(%r12)
  79d32b:	eb d0                	jmp    79d2fd <agentGoProject/common.GetMemByContainerId.func2+0x1d>
  79d32d:	cc                   	int3   
  79d32e:	cc                   	int3   
  79d32f:	cc                   	int3   
  79d330:	cc                   	int3   
  79d331:	cc                   	int3   
  79d332:	cc                   	int3   
  79d333:	cc                   	int3   
  79d334:	cc                   	int3   
  79d335:	cc                   	int3   
  79d336:	cc                   	int3   
  79d337:	cc                   	int3   
  79d338:	cc                   	int3   
  79d339:	cc                   	int3   
  79d33a:	cc                   	int3   
  79d33b:	cc                   	int3   
  79d33c:	cc                   	int3   
  79d33d:	cc                   	int3   
  79d33e:	cc                   	int3   
  79d33f:	cc                   	int3   

000000000079d340 <agentGoProject/common.RunDockerScript>:
agentGoProject/common.RunDockerScript():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:333
  79d340:	4c 8d 64 24 90       	lea    -0x70(%rsp),%r12
  79d345:	4d 3b 66 10          	cmp    0x10(%r14),%r12
  79d349:	0f 86 9a 03 00 00    	jbe    79d6e9 <agentGoProject/common.RunDockerScript+0x3a9>
  79d34f:	48 81 ec f0 00 00 00 	sub    $0xf0,%rsp
  79d356:	48 89 ac 24 e8 00 00 	mov    %rbp,0xe8(%rsp)
  79d35d:	00 
  79d35e:	48 8d ac 24 e8 00 00 	lea    0xe8(%rsp),%rbp
  79d365:	00 
  79d366:	48 89 9c 24 00 01 00 	mov    %rbx,0x100(%rsp)
  79d36d:	00 
  79d36e:	48 89 84 24 f8 00 00 	mov    %rax,0xf8(%rsp)
  79d375:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:334
  79d376:	48 8d 0d fe 49 0d 00 	lea    0xd49fe(%rip),%rcx        # 871d7b <go:string.*+0x47b3>
./C:/Program Files/Go/src/strings/strings.go:305
  79d37d:	bf 0e 00 00 00       	mov    $0xe,%edi
strings.Split():
./C:/Program Files/Go/src/strings/strings.go:305
  79d382:	31 f6                	xor    %esi,%esi
  79d384:	49 c7 c0 ff ff ff ff 	mov    $0xffffffffffffffff,%r8
  79d38b:	e8 b0 aa d5 ff       	call   4f7e40 <strings.genSplit>
agentGoProject/common.RunDockerScript():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:334
  79d390:	48 85 db             	test   %rbx,%rbx
  79d393:	0f 86 45 03 00 00    	jbe    79d6de <agentGoProject/common.RunDockerScript+0x39e>
  79d399:	48 8b 50 08          	mov    0x8(%rax),%rdx
  79d39d:	48 89 54 24 78       	mov    %rdx,0x78(%rsp)
  79d3a2:	48 83 fa 01          	cmp    $0x1,%rdx
  79d3a6:	4c 8b 08             	mov    (%rax),%r9
  79d3a9:	4c 89 8c 24 a0 00 00 	mov    %r9,0xa0(%rsp)
  79d3b0:	00 
./C:/Program Files/Go/src/strings/strings.go:305
  79d3b1:	48 8b 84 24 f8 00 00 	mov    0xf8(%rsp),%rax
  79d3b8:	00 
strings.Split():
./C:/Program Files/Go/src/strings/strings.go:305
  79d3b9:	48 8b 9c 24 00 01 00 	mov    0x100(%rsp),%rbx
  79d3c0:	00 
  79d3c1:	48 8d 0d b3 49 0d 00 	lea    0xd49b3(%rip),%rcx        # 871d7b <go:string.*+0x47b3>
  79d3c8:	bf 0e 00 00 00       	mov    $0xe,%edi
  79d3cd:	31 f6                	xor    %esi,%esi
  79d3cf:	49 c7 c0 ff ff ff ff 	mov    $0xffffffffffffffff,%r8
  79d3d6:	e8 65 aa d5 ff       	call   4f7e40 <strings.genSplit>
  79d3db:	0f 1f 44 00 00       	nopl   0x0(%rax,%rax,1)
agentGoProject/common.RunDockerScript():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:335
  79d3e0:	48 83 fb 01          	cmp    $0x1,%rbx
  79d3e4:	0f 86 e7 02 00 00    	jbe    79d6d1 <agentGoProject/common.RunDockerScript+0x391>
  79d3ea:	48 8b 50 10          	mov    0x10(%rax),%rdx
  79d3ee:	4c 8b 40 18          	mov    0x18(%rax),%r8
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:334
  79d3f2:	4c 8b 4c 24 78       	mov    0x78(%rsp),%r9
  79d3f7:	49 83 f9 01          	cmp    $0x1,%r9
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:337
  79d3fb:	75 30                	jne    79d42d <agentGoProject/common.RunDockerScript+0xed>
  79d3fd:	4c 8b 94 24 a0 00 00 	mov    0xa0(%rsp),%r10
  79d404:	00 
  79d405:	41 80 3a 31          	cmpb   $0x31,(%r10)
  79d409:	75 12                	jne    79d41d <agentGoProject/common.RunDockerScript+0xdd>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:334
  79d40b:	49 83 f9 01          	cmp    $0x1,%r9
  79d40f:	b8 04 00 00 00       	mov    $0x4,%eax
  79d414:	48 8d 0d 1b 09 0d 00 	lea    0xd091b(%rip),%rcx        # 86dd36 <go:string.*+0x76e>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:337
  79d41b:	eb 22                	jmp    79d43f <agentGoProject/common.RunDockerScript+0xff>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:334
  79d41d:	49 83 f9 01          	cmp    $0x1,%r9
  79d421:	b8 00 00 00 00       	mov    $0x0,%eax
  79d426:	b9 00 00 00 00       	mov    $0x0,%ecx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:337
  79d42b:	eb 12                	jmp    79d43f <agentGoProject/common.RunDockerScript+0xff>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:340
  79d42d:	4c 8b 94 24 a0 00 00 	mov    0xa0(%rsp),%r10
  79d434:	00 
  79d435:	b8 00 00 00 00       	mov    $0x0,%eax
  79d43a:	b9 00 00 00 00       	mov    $0x0,%ecx
  79d43f:	90                   	nop
  79d440:	75 22                	jne    79d464 <agentGoProject/common.RunDockerScript+0x124>
  79d442:	41 80 3a 32          	cmpb   $0x32,(%r10)
  79d446:	75 18                	jne    79d460 <agentGoProject/common.RunDockerScript+0x120>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:334
  79d448:	49 83 f9 01          	cmp    $0x1,%r9
  79d44c:	b8 05 00 00 00       	mov    $0x5,%eax
  79d451:	48 8d 0d 13 0d 0d 00 	lea    0xd0d13(%rip),%rcx        # 86e16b <go:string.*+0xba3>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:340
  79d458:	eb 0a                	jmp    79d464 <agentGoProject/common.RunDockerScript+0x124>
  79d45a:	66 0f 1f 44 00 00    	nopw   0x0(%rax,%rax,1)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:334
  79d460:	49 83 f9 01          	cmp    $0x1,%r9
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:343
  79d464:	75 1a                	jne    79d480 <agentGoProject/common.RunDockerScript+0x140>
  79d466:	41 80 3a 33          	cmpb   $0x33,(%r10)
  79d46a:	75 14                	jne    79d480 <agentGoProject/common.RunDockerScript+0x140>
  79d46c:	b8 07 00 00 00       	mov    $0x7,%eax
  79d471:	48 8d 0d 6f 18 0d 00 	lea    0xd186f(%rip),%rcx        # 86ece7 <go:string.*+0x171f>
  79d478:	0f 1f 84 00 00 00 00 	nopl   0x0(%rax,%rax,1)
  79d47f:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:346
  79d480:	48 85 c0             	test   %rax,%rax
  79d483:	0f 84 38 02 00 00    	je     79d6c1 <agentGoProject/common.RunDockerScript+0x381>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:335
  79d489:	48 89 94 24 90 00 00 	mov    %rdx,0x90(%rsp)
  79d490:	00 
  79d491:	4c 89 44 24 68       	mov    %r8,0x68(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:346
  79d496:	48 89 8c 24 98 00 00 	mov    %rcx,0x98(%rsp)
  79d49d:	00 
  79d49e:	48 89 44 24 70       	mov    %rax,0x70(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:349
  79d4a3:	44 0f 11 bc 24 c8 00 	movups %xmm15,0xc8(%rsp)
  79d4aa:	00 00 
  79d4ac:	44 0f 11 bc 24 d8 00 	movups %xmm15,0xd8(%rsp)
  79d4b3:	00 00 
  79d4b5:	48 89 8c 24 c8 00 00 	mov    %rcx,0xc8(%rsp)
  79d4bc:	00 
  79d4bd:	48 89 84 24 d0 00 00 	mov    %rax,0xd0(%rsp)
  79d4c4:	00 
  79d4c5:	48 89 94 24 d8 00 00 	mov    %rdx,0xd8(%rsp)
  79d4cc:	00 
  79d4cd:	4c 89 84 24 e0 00 00 	mov    %r8,0xe0(%rsp)
  79d4d4:	00 
  79d4d5:	bb 06 00 00 00       	mov    $0x6,%ebx
  79d4da:	bf 02 00 00 00       	mov    $0x2,%edi
  79d4df:	48 89 fe             	mov    %rdi,%rsi
  79d4e2:	48 8d 05 32 10 0d 00 	lea    0xd1032(%rip),%rax        # 86e51b <go:string.*+0xf53>
  79d4e9:	48 8d 8c 24 c8 00 00 	lea    0xc8(%rsp),%rcx
  79d4f0:	00 
  79d4f1:	e8 8a cf fa ff       	call   74a480 <os/exec.Command>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:350
  79d4f6:	e8 e5 00 fb ff       	call   74d5e0 <os/exec.(*Cmd).Output>
  79d4fb:	0f 1f 44 00 00       	nopl   0x0(%rax,%rax,1)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:351
  79d500:	48 85 ff             	test   %rdi,%rdi
  79d503:	0f 84 d9 00 00 00    	je     79d5e2 <agentGoProject/common.RunDockerScript+0x2a2>
  79d509:	48 89 7c 24 60       	mov    %rdi,0x60(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:352
  79d50e:	48 89 b4 24 88 00 00 	mov    %rsi,0x88(%rsp)
  79d515:	00 
  79d516:	44 0f 11 bc 24 a8 00 	movups %xmm15,0xa8(%rsp)
  79d51d:	00 00 
  79d51f:	44 0f 11 bc 24 b8 00 	movups %xmm15,0xb8(%rsp)
  79d526:	00 00 
  79d528:	48 8d 15 b4 43 0d 00 	lea    0xd43b4(%rip),%rdx        # 8718e3 <go:string.*+0x431b>
  79d52f:	48 89 14 24          	mov    %rdx,(%rsp)
  79d533:	48 c7 44 24 08 0e 00 	movq   $0xe,0x8(%rsp)
  79d53a:	00 00 
  79d53c:	31 c0                	xor    %eax,%eax
  79d53e:	48 8d 1d fe 15 0d 00 	lea    0xd15fe(%rip),%rbx        # 86eb43 <go:string.*+0x157b>
  79d545:	b9 07 00 00 00       	mov    $0x7,%ecx
  79d54a:	48 8b bc 24 98 00 00 	mov    0x98(%rsp),%rdi
  79d551:	00 
  79d552:	48 8b 74 24 70       	mov    0x70(%rsp),%rsi
  79d557:	4c 8d 05 e2 1c 17 00 	lea    0x171ce2(%rip),%r8        # 90f240 <go:buildinfo.ref+0x30>
  79d55e:	41 b9 01 00 00 00    	mov    $0x1,%r9d
  79d564:	4c 8b 94 24 90 00 00 	mov    0x90(%rsp),%r10
  79d56b:	00 
  79d56c:	4c 8b 5c 24 68       	mov    0x68(%rsp),%r11
  79d571:	e8 4a 30 cb ff       	call   4505c0 <runtime.concatstring5>
  79d576:	e8 85 df c6 ff       	call   40b500 <runtime.convTstring>
  79d57b:	48 8d 15 1e 47 05 00 	lea    0x5471e(%rip),%rdx        # 7f1ca0 <type:*+0x2fca0>
  79d582:	48 89 94 24 a8 00 00 	mov    %rdx,0xa8(%rsp)
  79d589:	00 
  79d58a:	48 89 84 24 b0 00 00 	mov    %rax,0xb0(%rsp)
  79d591:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:351
  79d592:	48 8b 54 24 60       	mov    0x60(%rsp),%rdx
  79d597:	48 85 d2             	test   %rdx,%rdx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:352
  79d59a:	74 06                	je     79d5a2 <agentGoProject/common.RunDockerScript+0x262>
  79d59c:	48 8b 7a 08          	mov    0x8(%rdx),%rdi
  79d5a0:	eb 03                	jmp    79d5a5 <agentGoProject/common.RunDockerScript+0x265>
  79d5a2:	48 89 d7             	mov    %rdx,%rdi
  79d5a5:	48 89 bc 24 b8 00 00 	mov    %rdi,0xb8(%rsp)
  79d5ac:	00 
  79d5ad:	48 8b 94 24 88 00 00 	mov    0x88(%rsp),%rdx
  79d5b4:	00 
  79d5b5:	48 89 94 24 c0 00 00 	mov    %rdx,0xc0(%rsp)
  79d5bc:	00 
  79d5bd:	48 8d 84 24 a8 00 00 	lea    0xa8(%rsp),%rax
  79d5c4:	00 
  79d5c5:	bb 02 00 00 00       	mov    $0x2,%ebx
  79d5ca:	48 89 d9             	mov    %rbx,%rcx
  79d5cd:	e8 4e f9 de ff       	call   58cf20 <log.Println>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:353
  79d5d2:	48 8b ac 24 e8 00 00 	mov    0xe8(%rsp),%rbp
  79d5d9:	00 
  79d5da:	48 81 c4 f0 00 00 00 	add    $0xf0,%rsp
  79d5e1:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:355
  79d5e2:	48 89 84 24 80 00 00 	mov    %rax,0x80(%rsp)
  79d5e9:	00 
  79d5ea:	48 89 5c 24 58       	mov    %rbx,0x58(%rsp)
  79d5ef:	44 0f 11 bc 24 a8 00 	movups %xmm15,0xa8(%rsp)
  79d5f6:	00 00 
  79d5f8:	44 0f 11 bc 24 b8 00 	movups %xmm15,0xb8(%rsp)
  79d5ff:	00 00 
  79d601:	48 8d 15 cd 42 0d 00 	lea    0xd42cd(%rip),%rdx        # 8718d5 <go:string.*+0x430d>
  79d608:	48 89 14 24          	mov    %rdx,(%rsp)
  79d60c:	48 c7 44 24 08 0e 00 	movq   $0xe,0x8(%rsp)
  79d613:	00 00 
  79d615:	31 c0                	xor    %eax,%eax
  79d617:	48 8d 1d 25 15 0d 00 	lea    0xd1525(%rip),%rbx        # 86eb43 <go:string.*+0x157b>
  79d61e:	b9 07 00 00 00       	mov    $0x7,%ecx
  79d623:	48 8b bc 24 98 00 00 	mov    0x98(%rsp),%rdi
  79d62a:	00 
  79d62b:	48 8b 74 24 70       	mov    0x70(%rsp),%rsi
  79d630:	4c 8d 05 09 1c 17 00 	lea    0x171c09(%rip),%r8        # 90f240 <go:buildinfo.ref+0x30>
  79d637:	41 b9 01 00 00 00    	mov    $0x1,%r9d
  79d63d:	4c 8b 94 24 90 00 00 	mov    0x90(%rsp),%r10
  79d644:	00 
  79d645:	4c 8b 5c 24 68       	mov    0x68(%rsp),%r11
  79d64a:	e8 71 2f cb ff       	call   4505c0 <runtime.concatstring5>
  79d64f:	e8 ac de c6 ff       	call   40b500 <runtime.convTstring>
  79d654:	48 8d 15 45 46 05 00 	lea    0x54645(%rip),%rdx        # 7f1ca0 <type:*+0x2fca0>
  79d65b:	48 89 94 24 a8 00 00 	mov    %rdx,0xa8(%rsp)
  79d662:	00 
  79d663:	48 89 84 24 b0 00 00 	mov    %rax,0xb0(%rsp)
  79d66a:	00 
  79d66b:	31 c0                	xor    %eax,%eax
  79d66d:	48 8b 9c 24 80 00 00 	mov    0x80(%rsp),%rbx
  79d674:	00 
  79d675:	48 8b 4c 24 58       	mov    0x58(%rsp),%rcx
  79d67a:	e8 61 30 cb ff       	call   4506e0 <runtime.slicebytetostring>
  79d67f:	90                   	nop
  79d680:	e8 7b de c6 ff       	call   40b500 <runtime.convTstring>
  79d685:	48 8d 15 14 46 05 00 	lea    0x54614(%rip),%rdx        # 7f1ca0 <type:*+0x2fca0>
  79d68c:	48 89 94 24 b8 00 00 	mov    %rdx,0xb8(%rsp)
  79d693:	00 
  79d694:	48 89 84 24 c0 00 00 	mov    %rax,0xc0(%rsp)
  79d69b:	00 
  79d69c:	48 8d 84 24 a8 00 00 	lea    0xa8(%rsp),%rax
  79d6a3:	00 
  79d6a4:	bb 02 00 00 00       	mov    $0x2,%ebx
  79d6a9:	48 89 d9             	mov    %rbx,%rcx
  79d6ac:	e8 6f f8 de ff       	call   58cf20 <log.Println>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:356
  79d6b1:	48 8b ac 24 e8 00 00 	mov    0xe8(%rsp),%rbp
  79d6b8:	00 
  79d6b9:	48 81 c4 f0 00 00 00 	add    $0xf0,%rsp
  79d6c0:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:347
  79d6c1:	48 8b ac 24 e8 00 00 	mov    0xe8(%rsp),%rbp
  79d6c8:	00 
  79d6c9:	48 81 c4 f0 00 00 00 	add    $0xf0,%rsp
  79d6d0:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:335
  79d6d1:	b8 01 00 00 00       	mov    $0x1,%eax
  79d6d6:	48 89 d9             	mov    %rbx,%rcx
  79d6d9:	e8 a2 9d cc ff       	call   467480 <runtime.panicIndex>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:334
  79d6de:	31 c0                	xor    %eax,%eax
  79d6e0:	48 89 c1             	mov    %rax,%rcx
  79d6e3:	e8 98 9d cc ff       	call   467480 <runtime.panicIndex>
  79d6e8:	90                   	nop
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/dockerUtil.go:333
  79d6e9:	48 89 44 24 08       	mov    %rax,0x8(%rsp)
  79d6ee:	48 89 5c 24 10       	mov    %rbx,0x10(%rsp)
  79d6f3:	e8 28 79 cc ff       	call   465020 <runtime.morestack_noctxt.abi0>
  79d6f8:	48 8b 44 24 08       	mov    0x8(%rsp),%rax
  79d6fd:	48 8b 5c 24 10       	mov    0x10(%rsp),%rbx
  79d702:	e9 39 fc ff ff       	jmp    79d340 <agentGoProject/common.RunDockerScript>
  79d707:	cc                   	int3   
  79d708:	cc                   	int3   
  79d709:	cc                   	int3   
  79d70a:	cc                   	int3   
  79d70b:	cc                   	int3   
  79d70c:	cc                   	int3   
  79d70d:	cc                   	int3   
  79d70e:	cc                   	int3   
  79d70f:	cc                   	int3   
  79d710:	cc                   	int3   
  79d711:	cc                   	int3   
  79d712:	cc                   	int3   
  79d713:	cc                   	int3   
  79d714:	cc                   	int3   
  79d715:	cc                   	int3   
  79d716:	cc                   	int3   
  79d717:	cc                   	int3   
  79d718:	cc                   	int3   
  79d719:	cc                   	int3   
  79d71a:	cc                   	int3   
  79d71b:	cc                   	int3   
  79d71c:	cc                   	int3   
  79d71d:	cc                   	int3   
  79d71e:	cc                   	int3   
  79d71f:	cc                   	int3   

000000000079d720 <agentGoProject/common.LoadWindowsEvtxLog>:
agentGoProject/common.LoadWindowsEvtxLog():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:19
  79d720:	4c 8d a4 24 10 ff ff 	lea    -0xf0(%rsp),%r12
  79d727:	ff 
  79d728:	4d 3b 66 10          	cmp    0x10(%r14),%r12
  79d72c:	0f 86 3e 09 00 00    	jbe    79e070 <agentGoProject/common.LoadWindowsEvtxLog+0x950>
  79d732:	48 81 ec 70 01 00 00 	sub    $0x170,%rsp
  79d739:	48 89 ac 24 68 01 00 	mov    %rbp,0x168(%rsp)
  79d740:	00 
  79d741:	48 8d ac 24 68 01 00 	lea    0x168(%rsp),%rbp
  79d748:	00 
  79d749:	49 c7 c5 00 00 00 00 	mov    $0x0,%r13
  79d750:	4c 89 ac 24 60 01 00 	mov    %r13,0x160(%rsp)
  79d757:	00 
  79d758:	4c 89 84 24 b0 01 00 	mov    %r8,0x1b0(%rsp)
  79d75f:	00 
  79d760:	48 89 b4 24 a8 01 00 	mov    %rsi,0x1a8(%rsp)
  79d767:	00 
  79d768:	4c 89 94 24 c0 01 00 	mov    %r10,0x1c0(%rsp)
  79d76f:	00 
  79d770:	48 89 bc 24 a0 01 00 	mov    %rdi,0x1a0(%rsp)
  79d777:	00 
  79d778:	48 89 8c 24 98 01 00 	mov    %rcx,0x198(%rsp)
  79d77f:	00 
  79d780:	48 89 9c 24 90 01 00 	mov    %rbx,0x190(%rsp)
  79d787:	00 
  79d788:	48 89 84 24 88 01 00 	mov    %rax,0x188(%rsp)
  79d78f:	00 
  79d790:	4c 89 8c 24 b8 01 00 	mov    %r9,0x1b8(%rsp)
  79d797:	00 
  79d798:	c6 44 24 3f 00       	movb   $0x0,0x3f(%rsp)
  79d79d:	44 0f 11 bc 24 00 01 	movups %xmm15,0x100(%rsp)
  79d7a4:	00 00 
  79d7a6:	44 0f 11 bc 24 f0 00 	movups %xmm15,0xf0(%rsp)
  79d7ad:	00 00 
  79d7af:	48 c7 44 24 40 00 00 	movq   $0x0,0x40(%rsp)
  79d7b6:	00 00 
  79d7b8:	44 0f 11 bc 24 e0 00 	movups %xmm15,0xe0(%rsp)
  79d7bf:	00 00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:20
  79d7c1:	48 8d 15 b8 98 10 00 	lea    0x1098b8(%rip),%rdx        # 8a7080 <go:func.*+0x9a0>
  79d7c8:	48 89 94 24 60 01 00 	mov    %rdx,0x160(%rsp)
  79d7cf:	00 
  79d7d0:	c6 44 24 3f 01       	movb   $0x1,0x3f(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:27
  79d7d5:	e8 66 5d 00 00       	call   7a3540 <agentGoProject/common.getFileSize>
  79d7da:	48 89 44 24 78       	mov    %rax,0x78(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:29
  79d7df:	48 8b 9c 24 a0 01 00 	mov    0x1a0(%rsp),%rbx
  79d7e6:	00 
  79d7e7:	b9 0a 00 00 00       	mov    $0xa,%ecx
  79d7ec:	bf 40 00 00 00       	mov    $0x40,%edi
  79d7f1:	48 8b 84 24 98 01 00 	mov    0x198(%rsp),%rax
  79d7f8:	00 
  79d7f9:	e8 e2 b4 cd ff       	call   478ce0 <strconv.ParseInt>
  79d7fe:	48 89 44 24 58       	mov    %rax,0x58(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:31
  79d803:	48 8d 1d a4 67 0d 00 	lea    0xd67a4(%rip),%rbx        # 873fae <go:string.*+0x69e6>
  79d80a:	b9 13 00 00 00       	mov    $0x13,%ecx
  79d80f:	48 8b bc 24 88 01 00 	mov    0x188(%rsp),%rdi
  79d816:	00 
  79d817:	48 8b b4 24 90 01 00 	mov    0x190(%rsp),%rsi
  79d81e:	00 
  79d81f:	4c 8d 05 3f d7 0d 00 	lea    0xdd73f(%rip),%r8        # 87af65 <go:string.*+0xd99d>
  79d826:	41 b9 1f 00 00 00    	mov    $0x1f,%r9d
  79d82c:	31 c0                	xor    %eax,%eax
  79d82e:	e8 ad 2b cb ff       	call   4503e0 <runtime.concatstring3>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:33
  79d833:	44 0f 11 bc 24 40 01 	movups %xmm15,0x140(%rsp)
  79d83a:	00 00 
  79d83c:	44 0f 11 bc 24 50 01 	movups %xmm15,0x150(%rsp)
  79d843:	00 00 
  79d845:	48 8d 0d 30 16 0d 00 	lea    0xd1630(%rip),%rcx        # 86ee7c <go:string.*+0x18b4>
  79d84c:	48 89 8c 24 40 01 00 	mov    %rcx,0x140(%rsp)
  79d853:	00 
  79d854:	48 c7 84 24 48 01 00 	movq   $0x8,0x148(%rsp)
  79d85b:	00 08 00 00 00 
  79d860:	48 89 84 24 50 01 00 	mov    %rax,0x150(%rsp)
  79d867:	00 
  79d868:	48 89 9c 24 58 01 00 	mov    %rbx,0x158(%rsp)
  79d86f:	00 
  79d870:	48 8d 05 fe 29 0d 00 	lea    0xd29fe(%rip),%rax        # 870275 <go:string.*+0x2cad>
  79d877:	bb 0a 00 00 00       	mov    $0xa,%ebx
  79d87c:	48 8d 8c 24 40 01 00 	lea    0x140(%rsp),%rcx
  79d883:	00 
  79d884:	bf 02 00 00 00       	mov    $0x2,%edi
  79d889:	48 89 fe             	mov    %rdi,%rsi
  79d88c:	e8 ef cb fa ff       	call   74a480 <os/exec.Command>
  79d891:	48 89 84 24 c0 00 00 	mov    %rax,0xc0(%rsp)
  79d898:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:36
  79d899:	48 8d 05 80 36 09 00 	lea    0x93680(%rip),%rax        # 830f20 <type:*+0x6ef20>
  79d8a0:	e8 db 03 c7 ff       	call   40dc80 <runtime.newobject>
  79d8a5:	48 89 84 24 d8 00 00 	mov    %rax,0xd8(%rsp)
  79d8ac:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:37
  79d8ad:	48 8d 05 6c 36 09 00 	lea    0x9366c(%rip),%rax        # 830f20 <type:*+0x6ef20>
  79d8b4:	e8 c7 03 c7 ff       	call   40dc80 <runtime.newobject>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:38
  79d8b9:	48 8d 0d 40 40 17 00 	lea    0x174040(%rip),%rcx        # 911900 <go:itab.*bytes.Buffer,io.Writer>
  79d8c0:	48 8b bc 24 c0 00 00 	mov    0xc0(%rsp),%rdi
  79d8c7:	00 
  79d8c8:	48 89 4f 60          	mov    %rcx,0x60(%rdi)
  79d8cc:	83 3d 7d 7c 43 00 00 	cmpl   $0x0,0x437c7d(%rip)        # bd5550 <runtime.writeBarrier>
  79d8d3:	75 0e                	jne    79d8e3 <agentGoProject/common.LoadWindowsEvtxLog+0x1c3>
  79d8d5:	48 8b 94 24 d8 00 00 	mov    0xd8(%rsp),%rdx
  79d8dc:	00 
  79d8dd:	48 89 57 68          	mov    %rdx,0x68(%rdi)
  79d8e1:	eb 1d                	jmp    79d900 <agentGoProject/common.LoadWindowsEvtxLog+0x1e0>
  79d8e3:	48 8d 57 68          	lea    0x68(%rdi),%rdx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:33
  79d8e7:	48 89 fb             	mov    %rdi,%rbx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:38
  79d8ea:	48 89 d7             	mov    %rdx,%rdi
  79d8ed:	48 8b b4 24 d8 00 00 	mov    0xd8(%rsp),%rsi
  79d8f4:	00 
  79d8f5:	e8 46 98 cc ff       	call   467140 <runtime.gcWriteBarrierSI>
./C:/Program Files/Go/src/bytes/buffer.go:61
  79d8fa:	48 89 f2             	mov    %rsi,%rdx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:39
  79d8fd:	48 89 df             	mov    %rbx,%rdi
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:37
  79d900:	48 89 84 24 d0 00 00 	mov    %rax,0xd0(%rsp)
  79d907:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:39
  79d908:	48 89 4f 70          	mov    %rcx,0x70(%rdi)
  79d90c:	83 3d 3d 7c 43 00 00 	cmpl   $0x0,0x437c3d(%rip)        # bd5550 <runtime.writeBarrier>
  79d913:	75 06                	jne    79d91b <agentGoProject/common.LoadWindowsEvtxLog+0x1fb>
  79d915:	48 89 47 78          	mov    %rax,0x78(%rdi)
  79d919:	eb 12                	jmp    79d92d <agentGoProject/common.LoadWindowsEvtxLog+0x20d>
  79d91b:	48 8d 4f 78          	lea    0x78(%rdi),%rcx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:33
  79d91f:	48 89 fb             	mov    %rdi,%rbx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:39
  79d922:	48 89 cf             	mov    %rcx,%rdi
  79d925:	e8 b6 96 cc ff       	call   466fe0 <runtime.gcWriteBarrier>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:42
  79d92a:	48 89 df             	mov    %rbx,%rdi
  79d92d:	48 89 f8             	mov    %rdi,%rax
  79d930:	e8 eb e0 fa ff       	call   74ba20 <os/exec.(*Cmd).Run>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:43
  79d935:	48 85 c0             	test   %rax,%rax
  79d938:	0f 85 df 00 00 00    	jne    79da1d <agentGoProject/common.LoadWindowsEvtxLog+0x2fd>
bytes.(*Buffer).String():
./C:/Program Files/Go/src/bytes/buffer.go:61
  79d93e:	48 8b 94 24 d8 00 00 	mov    0xd8(%rsp),%rdx
  79d945:	00 
  79d946:	48 85 d2             	test   %rdx,%rdx
  79d949:	74 3c                	je     79d987 <agentGoProject/common.LoadWindowsEvtxLog+0x267>
./C:/Program Files/Go/src/bytes/buffer.go:65
  79d94b:	48 8b 4a 08          	mov    0x8(%rdx),%rcx
  79d94f:	48 8b 32             	mov    (%rdx),%rsi
  79d952:	48 8b 7a 10          	mov    0x10(%rdx),%rdi
  79d956:	48 8b 42 18          	mov    0x18(%rdx),%rax
  79d95a:	66 0f 1f 44 00 00    	nopw   0x0(%rax,%rax,1)
  79d960:	48 39 c1             	cmp    %rax,%rcx
  79d963:	0f 82 b7 06 00 00    	jb     79e020 <agentGoProject/common.LoadWindowsEvtxLog+0x900>
  79d969:	48 29 c1             	sub    %rax,%rcx
  79d96c:	48 89 c2             	mov    %rax,%rdx
  79d96f:	48 29 f8             	sub    %rdi,%rax
  79d972:	48 c1 f8 3f          	sar    $0x3f,%rax
  79d976:	48 21 c2             	and    %rax,%rdx
  79d979:	48 8d 1c 16          	lea    (%rsi,%rdx,1),%rbx
  79d97d:	31 c0                	xor    %eax,%eax
  79d97f:	90                   	nop
  79d980:	e8 5b 2d cb ff       	call   4506e0 <runtime.slicebytetostring>
agentGoProject/common.LoadWindowsEvtxLog():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:51
  79d985:	eb 0c                	jmp    79d993 <agentGoProject/common.LoadWindowsEvtxLog+0x273>
  79d987:	bb 05 00 00 00       	mov    $0x5,%ebx
  79d98c:	48 8d 05 e0 04 0d 00 	lea    0xd04e0(%rip),%rax        # 86de73 <go:string.*+0x8ab>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:53
  79d993:	48 8b 94 24 b0 01 00 	mov    0x1b0(%rsp),%rdx
  79d99a:	00 
  79d99b:	0f 1f 44 00 00       	nopl   0x0(%rax,%rax,1)
  79d9a0:	48 83 fa 03          	cmp    $0x3,%rdx
  79d9a4:	75 33                	jne    79d9d9 <agentGoProject/common.LoadWindowsEvtxLog+0x2b9>
  79d9a6:	48 8b 94 24 a8 01 00 	mov    0x1a8(%rsp),%rdx
  79d9ad:	00 
  79d9ae:	66 81 3a 47 42       	cmpw   $0x4247,(%rdx)
  79d9b3:	75 24                	jne    79d9d9 <agentGoProject/common.LoadWindowsEvtxLog+0x2b9>
  79d9b5:	80 7a 02 4b          	cmpb   $0x4b,0x2(%rdx)
  79d9b9:	75 1e                	jne    79d9d9 <agentGoProject/common.LoadWindowsEvtxLog+0x2b9>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:54
  79d9bb:	48 89 d9             	mov    %rbx,%rcx
  79d9be:	48 89 c3             	mov    %rax,%rbx
  79d9c1:	31 c0                	xor    %eax,%eax
  79d9c3:	e8 18 2f cb ff       	call   4508e0 <runtime.stringtoslicebyte>
  79d9c8:	48 8d 3d 2a fe 0c 00 	lea    0xcfe2a(%rip),%rdi        # 86d7f9 <go:string.*+0x231>
  79d9cf:	be 03 00 00 00       	mov    $0x3,%esi
  79d9d4:	e8 87 b6 00 00       	call   7a9060 <agentGoProject/common.ByteToString>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:62
  79d9d9:	48 89 5c 24 68       	mov    %rbx,0x68(%rsp)
  79d9de:	48 89 84 24 a8 00 00 	mov    %rax,0xa8(%rsp)
  79d9e5:	00 
./C:/Program Files/Go/src/strings/strings.go:305
  79d9e6:	48 8d 0d 53 fb 16 00 	lea    0x16fb53(%rip),%rcx        # 90d540 <runtime.gcbits.*+0xe0>
strings.Split():
./C:/Program Files/Go/src/strings/strings.go:305
  79d9ed:	bf 01 00 00 00       	mov    $0x1,%edi
  79d9f2:	31 f6                	xor    %esi,%esi
  79d9f4:	49 c7 c0 ff ff ff ff 	mov    $0xffffffffffffffff,%r8
  79d9fb:	0f 1f 44 00 00       	nopl   0x0(%rax,%rax,1)
  79da00:	e8 3b a4 d5 ff       	call   4f7e40 <strings.genSplit>
agentGoProject/common.LoadWindowsEvtxLog():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:68
  79da05:	48 89 84 24 98 00 00 	mov    %rax,0x98(%rsp)
  79da0c:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:64
  79da0d:	48 89 5c 24 48       	mov    %rbx,0x48(%rsp)
strings.Split():
./C:/Program Files/Go/src/strings/strings.go:305
  79da12:	31 c9                	xor    %ecx,%ecx
  79da14:	31 d2                	xor    %edx,%edx
  79da16:	31 f6                	xor    %esi,%esi
agentGoProject/common.LoadWindowsEvtxLog():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:64
  79da18:	e9 fb 01 00 00       	jmp    79dc18 <agentGoProject/common.LoadWindowsEvtxLog+0x4f8>
./C:/Program Files/Go/src/bytes/buffer.go:61
  79da1d:	48 8b 94 24 d0 00 00 	mov    0xd0(%rsp),%rdx
  79da24:	00 
bytes.(*Buffer).String():
./C:/Program Files/Go/src/bytes/buffer.go:61
  79da25:	48 85 d2             	test   %rdx,%rdx
  79da28:	74 3d                	je     79da67 <agentGoProject/common.LoadWindowsEvtxLog+0x347>
./C:/Program Files/Go/src/bytes/buffer.go:65
  79da2a:	48 8b 4a 08          	mov    0x8(%rdx),%rcx
  79da2e:	48 8b 32             	mov    (%rdx),%rsi
  79da31:	48 8b 7a 10          	mov    0x10(%rdx),%rdi
  79da35:	48 8b 42 18          	mov    0x18(%rdx),%rax
  79da39:	0f 1f 80 00 00 00 00 	nopl   0x0(%rax)
  79da40:	48 39 c1             	cmp    %rax,%rcx
  79da43:	0f 82 a9 01 00 00    	jb     79dbf2 <agentGoProject/common.LoadWindowsEvtxLog+0x4d2>
  79da49:	48 29 c1             	sub    %rax,%rcx
  79da4c:	48 89 c2             	mov    %rax,%rdx
  79da4f:	48 29 f8             	sub    %rdi,%rax
  79da52:	48 c1 f8 3f          	sar    $0x3f,%rax
  79da56:	48 21 c2             	and    %rax,%rdx
  79da59:	48 8d 1c 16          	lea    (%rsi,%rdx,1),%rbx
  79da5d:	31 c0                	xor    %eax,%eax
  79da5f:	90                   	nop
  79da60:	e8 7b 2c cb ff       	call   4506e0 <runtime.slicebytetostring>
agentGoProject/common.LoadWindowsEvtxLog():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:45
  79da65:	eb 0c                	jmp    79da73 <agentGoProject/common.LoadWindowsEvtxLog+0x353>
  79da67:	bb 05 00 00 00       	mov    $0x5,%ebx
  79da6c:	48 8d 05 00 04 0d 00 	lea    0xd0400(%rip),%rax        # 86de73 <go:string.*+0x8ab>
  79da73:	44 0f 11 bc 24 20 01 	movups %xmm15,0x120(%rsp)
  79da7a:	00 00 
  79da7c:	44 0f 11 bc 24 30 01 	movups %xmm15,0x130(%rsp)
  79da83:	00 00 
  79da85:	48 8d 0d 14 42 05 00 	lea    0x54214(%rip),%rcx        # 7f1ca0 <type:*+0x2fca0>
  79da8c:	48 89 8c 24 20 01 00 	mov    %rcx,0x120(%rsp)
  79da93:	00 
  79da94:	48 8d 15 c5 24 17 00 	lea    0x1724c5(%rip),%rdx        # 90ff60 <runtime.buildVersion.str+0xcc0>
  79da9b:	48 89 94 24 28 01 00 	mov    %rdx,0x128(%rsp)
  79daa2:	00 
  79daa3:	e8 58 da c6 ff       	call   40b500 <runtime.convTstring>
  79daa8:	48 8d 0d f1 41 05 00 	lea    0x541f1(%rip),%rcx        # 7f1ca0 <type:*+0x2fca0>
  79daaf:	48 89 8c 24 30 01 00 	mov    %rcx,0x130(%rsp)
  79dab6:	00 
  79dab7:	48 89 84 24 38 01 00 	mov    %rax,0x138(%rsp)
  79dabe:	00 
  79dabf:	48 8d 84 24 20 01 00 	lea    0x120(%rsp),%rax
  79dac6:	00 
  79dac7:	bb 02 00 00 00       	mov    $0x2,%ebx
  79dacc:	48 89 d9             	mov    %rbx,%rcx
  79dacf:	e8 4c f4 de ff       	call   58cf20 <log.Println>
bytes.(*Buffer).String():
./C:/Program Files/Go/src/bytes/buffer.go:61
  79dad4:	48 8b 8c 24 d0 00 00 	mov    0xd0(%rsp),%rcx
  79dadb:	00 
  79dadc:	0f 1f 40 00          	nopl   0x0(%rax)
  79dae0:	48 85 c9             	test   %rcx,%rcx
agentGoProject/common.LoadWindowsEvtxLog():
./C:/Program Files/Go/src/bytes/buffer.go:61
  79dae3:	74 38                	je     79db1d <agentGoProject/common.LoadWindowsEvtxLog+0x3fd>
bytes.(*Buffer).String():
./C:/Program Files/Go/src/bytes/buffer.go:65
  79dae5:	48 8b 51 08          	mov    0x8(%rcx),%rdx
  79dae9:	48 8b 31             	mov    (%rcx),%rsi
  79daec:	48 8b 79 10          	mov    0x10(%rcx),%rdi
  79daf0:	48 8b 41 18          	mov    0x18(%rcx),%rax
  79daf4:	48 39 c2             	cmp    %rax,%rdx
  79daf7:	0f 82 ed 00 00 00    	jb     79dbea <agentGoProject/common.LoadWindowsEvtxLog+0x4ca>
  79dafd:	48 29 c2             	sub    %rax,%rdx
  79db00:	49 89 c0             	mov    %rax,%r8
  79db03:	48 29 f8             	sub    %rdi,%rax
  79db06:	48 c1 f8 3f          	sar    $0x3f,%rax
  79db0a:	49 21 c0             	and    %rax,%r8
  79db0d:	4a 8d 1c 06          	lea    (%rsi,%r8,1),%rbx
  79db11:	31 c0                	xor    %eax,%eax
  79db13:	48 89 d1             	mov    %rdx,%rcx
  79db16:	e8 c5 2b cb ff       	call   4506e0 <runtime.slicebytetostring>
agentGoProject/common.LoadWindowsEvtxLog():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:47
  79db1b:	eb 0c                	jmp    79db29 <agentGoProject/common.LoadWindowsEvtxLog+0x409>
  79db1d:	bb 05 00 00 00       	mov    $0x5,%ebx
  79db22:	48 8d 05 4a 03 0d 00 	lea    0xd034a(%rip),%rax        # 86de73 <go:string.*+0x8ab>
  79db29:	48 89 5c 24 50       	mov    %rbx,0x50(%rsp)
  79db2e:	48 89 84 24 a0 00 00 	mov    %rax,0xa0(%rsp)
  79db35:	00 
  79db36:	48 8b 44 24 58       	mov    0x58(%rsp),%rax
  79db3b:	bb 0a 00 00 00       	mov    $0xa,%ebx
  79db40:	e8 3b ff cd ff       	call   47da80 <strconv.FormatInt>
  79db45:	48 8b 8c 24 a0 00 00 	mov    0xa0(%rsp),%rcx
  79db4c:	00 
  79db4d:	48 89 8c 24 00 01 00 	mov    %rcx,0x100(%rsp)
  79db54:	00 
  79db55:	48 8b 4c 24 50       	mov    0x50(%rsp),%rcx
  79db5a:	48 89 8c 24 08 01 00 	mov    %rcx,0x108(%rsp)
  79db61:	00 
  79db62:	48 89 84 24 f0 00 00 	mov    %rax,0xf0(%rsp)
  79db69:	00 
  79db6a:	48 89 9c 24 f8 00 00 	mov    %rbx,0xf8(%rsp)
  79db71:	00 
  79db72:	48 8b 4c 24 78       	mov    0x78(%rsp),%rcx
  79db77:	48 89 4c 24 40       	mov    %rcx,0x40(%rsp)
  79db7c:	48 8d 0d 48 fa 0c 00 	lea    0xcfa48(%rip),%rcx        # 86d5cb <go:string.*+0x3>
  79db83:	48 89 8c 24 e0 00 00 	mov    %rcx,0xe0(%rsp)
  79db8a:	00 
  79db8b:	48 c7 84 24 e8 00 00 	movq   $0x1,0xe8(%rsp)
  79db92:	00 01 00 00 00 
  79db97:	c6 44 24 3f 00       	movb   $0x0,0x3f(%rsp)
  79db9c:	0f 1f 40 00          	nopl   0x0(%rax)
  79dba0:	e8 fb d9 01 00       	call   7bb5a0 <agentGoProject/common.LoadWindowsEvtxLog.func1>
  79dba5:	48 8b 74 24 40       	mov    0x40(%rsp),%rsi
  79dbaa:	48 8b 84 24 00 01 00 	mov    0x100(%rsp),%rax
  79dbb1:	00 
  79dbb2:	4c 8b 84 24 e0 00 00 	mov    0xe0(%rsp),%r8
  79dbb9:	00 
  79dbba:	48 8b 8c 24 f0 00 00 	mov    0xf0(%rsp),%rcx
  79dbc1:	00 
  79dbc2:	48 8b bc 24 f8 00 00 	mov    0xf8(%rsp),%rdi
  79dbc9:	00 
  79dbca:	4c 8b 8c 24 e8 00 00 	mov    0xe8(%rsp),%r9
  79dbd1:	00 
  79dbd2:	48 8b 9c 24 08 01 00 	mov    0x108(%rsp),%rbx
  79dbd9:	00 
  79dbda:	48 8b ac 24 68 01 00 	mov    0x168(%rsp),%rbp
  79dbe1:	00 
  79dbe2:	48 81 c4 70 01 00 00 	add    $0x170,%rsp
  79dbe9:	c3                   	ret    
bytes.(*Buffer).String():
./C:/Program Files/Go/src/bytes/buffer.go:65
  79dbea:	48 89 d1             	mov    %rdx,%rcx
  79dbed:	e8 4e 99 cc ff       	call   467540 <runtime.panicSliceB>
  79dbf2:	e8 49 99 cc ff       	call   467540 <runtime.panicSliceB>
agentGoProject/common.LoadWindowsEvtxLog():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:64
  79dbf7:	4c 8b 44 24 70       	mov    0x70(%rsp),%r8
  79dbfc:	49 ff c0             	inc    %r8
  79dbff:	48 8b 5c 24 48       	mov    0x48(%rsp),%rbx
  79dc04:	4c 8b 8c 24 98 00 00 	mov    0x98(%rsp),%r9
  79dc0b:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:87
  79dc0c:	48 89 c2             	mov    %rax,%rdx
  79dc0f:	48 89 ce             	mov    %rcx,%rsi
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:68
  79dc12:	4c 89 c8             	mov    %r9,%rax
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:64
  79dc15:	4c 89 c1             	mov    %r8,%rcx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:87
  79dc18:	48 89 94 24 80 00 00 	mov    %rdx,0x80(%rsp)
  79dc1f:	00 
  79dc20:	48 89 b4 24 b0 00 00 	mov    %rsi,0xb0(%rsp)
  79dc27:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:64
  79dc28:	48 39 cb             	cmp    %rcx,%rbx
  79dc2b:	0f 8e 2d 01 00 00    	jle    79dd5e <agentGoProject/common.LoadWindowsEvtxLog+0x63e>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:65
  79dc31:	48 85 d2             	test   %rdx,%rdx
  79dc34:	0f 85 24 01 00 00    	jne    79dd5e <agentGoProject/common.LoadWindowsEvtxLog+0x63e>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:64
  79dc3a:	48 89 4c 24 70       	mov    %rcx,0x70(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:68
  79dc3f:	48 c1 e1 04          	shl    $0x4,%rcx
  79dc43:	48 89 8c 24 90 00 00 	mov    %rcx,0x90(%rsp)
  79dc4a:	00 
  79dc4b:	48 8b 34 01          	mov    (%rcx,%rax,1),%rsi
  79dc4f:	48 8b 5c 01 08       	mov    0x8(%rcx,%rax,1),%rbx
./C:/Program Files/Go/src/strings/strings.go:59
  79dc54:	bf 0b 00 00 00       	mov    $0xb,%edi
strings.Contains():
./C:/Program Files/Go/src/strings/strings.go:59
  79dc59:	48 89 f0             	mov    %rsi,%rax
  79dc5c:	48 8d 0d 79 2b 0d 00 	lea    0xd2b79(%rip),%rcx        # 8707dc <go:string.*+0x3214>
  79dc63:	e8 f8 d9 d5 ff       	call   4fb660 <strings.Index>
  79dc68:	48 85 c0             	test   %rax,%rax
agentGoProject/common.LoadWindowsEvtxLog():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:68
  79dc6b:	7d 18                	jge    79dc85 <agentGoProject/common.LoadWindowsEvtxLog+0x565>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:87
  79dc6d:	48 8b 84 24 80 00 00 	mov    0x80(%rsp),%rax
  79dc74:	00 
  79dc75:	48 8b 8c 24 b0 00 00 	mov    0xb0(%rsp),%rcx
  79dc7c:	00 
  79dc7d:	0f 1f 00             	nopl   (%rax)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:68
  79dc80:	e9 72 ff ff ff       	jmp    79dbf7 <agentGoProject/common.LoadWindowsEvtxLog+0x4d7>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:69
  79dc85:	48 8b 94 24 98 00 00 	mov    0x98(%rsp),%rdx
  79dc8c:	00 
  79dc8d:	4c 8b 94 24 90 00 00 	mov    0x90(%rsp),%r10
  79dc94:	00 
  79dc95:	49 8b 04 12          	mov    (%r10,%rdx,1),%rax
  79dc99:	49 8b 5c 12 08       	mov    0x8(%r10,%rdx,1),%rbx
./C:/Program Files/Go/src/strings/strings.go:1089
  79dc9e:	48 8d 0d 9b f8 16 00 	lea    0x16f89b(%rip),%rcx        # 90d540 <runtime.gcbits.*+0xe0>
strings.ReplaceAll():
./C:/Program Files/Go/src/strings/strings.go:1089
  79dca5:	bf 01 00 00 00       	mov    $0x1,%edi
  79dcaa:	31 f6                	xor    %esi,%esi
  79dcac:	45 31 c0             	xor    %r8d,%r8d
  79dcaf:	49 c7 c1 ff ff ff ff 	mov    $0xffffffffffffffff,%r9
  79dcb6:	e8 a5 cf d5 ff       	call   4fac60 <strings.Replace>
agentGoProject/common.LoadWindowsEvtxLog():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:70
  79dcbb:	90                   	nop
./C:/Program Files/Go/src/strings/strings.go:1089
  79dcbc:	48 8d 0d ad f8 16 00 	lea    0x16f8ad(%rip),%rcx        # 90d570 <runtime.gcbits.*+0x110>
strings.ReplaceAll():
./C:/Program Files/Go/src/strings/strings.go:1089
  79dcc3:	bf 01 00 00 00       	mov    $0x1,%edi
  79dcc8:	31 f6                	xor    %esi,%esi
  79dcca:	45 31 c0             	xor    %r8d,%r8d
  79dccd:	49 c7 c1 ff ff ff ff 	mov    $0xffffffffffffffff,%r9
  79dcd4:	e8 87 cf d5 ff       	call   4fac60 <strings.Replace>
  79dcd9:	48 89 84 24 b0 00 00 	mov    %rax,0xb0(%rsp)
  79dce0:	00 
  79dce1:	48 89 9c 24 80 00 00 	mov    %rbx,0x80(%rsp)
  79dce8:	00 
agentGoProject/common.LoadWindowsEvtxLog():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:72
  79dce9:	44 0f 11 bc 24 10 01 	movups %xmm15,0x110(%rsp)
  79dcf0:	00 00 
  79dcf2:	b9 19 00 00 00       	mov    $0x19,%ecx
  79dcf7:	48 89 c7             	mov    %rax,%rdi
  79dcfa:	48 89 de             	mov    %rbx,%rsi
  79dcfd:	4c 8d 05 74 42 0d 00 	lea    0xd4274(%rip),%r8        # 871f78 <go:string.*+0x49b0>
  79dd04:	41 b9 0f 00 00 00    	mov    $0xf,%r9d
  79dd0a:	31 c0                	xor    %eax,%eax
  79dd0c:	48 8d 1d bb 9f 0d 00 	lea    0xd9fbb(%rip),%rbx        # 877cce <go:string.*+0xa706>
  79dd13:	e8 c8 26 cb ff       	call   4503e0 <runtime.concatstring3>
  79dd18:	e8 e3 d7 c6 ff       	call   40b500 <runtime.convTstring>
  79dd1d:	48 8d 15 7c 3f 05 00 	lea    0x53f7c(%rip),%rdx        # 7f1ca0 <type:*+0x2fca0>
  79dd24:	48 89 94 24 10 01 00 	mov    %rdx,0x110(%rsp)
  79dd2b:	00 
  79dd2c:	48 89 84 24 18 01 00 	mov    %rax,0x118(%rsp)
  79dd33:	00 
  79dd34:	48 8d 84 24 10 01 00 	lea    0x110(%rsp),%rax
  79dd3b:	00 
  79dd3c:	bb 01 00 00 00       	mov    $0x1,%ebx
  79dd41:	48 89 d9             	mov    %rbx,%rcx
  79dd44:	e8 d7 f1 de ff       	call   58cf20 <log.Println>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:87
  79dd49:	48 8b 84 24 80 00 00 	mov    0x80(%rsp),%rax
  79dd50:	00 
  79dd51:	48 8b 8c 24 b0 00 00 	mov    0xb0(%rsp),%rcx
  79dd58:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:72
  79dd59:	e9 99 fe ff ff       	jmp    79dbf7 <agentGoProject/common.LoadWindowsEvtxLog+0x4d7>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:76
  79dd5e:	48 8b 1d fb 59 40 00 	mov    0x4059fb(%rip),%rbx        # ba3760 <agentGoProject/common.readLogEndMarkMap>
  79dd65:	48 8d 05 94 d0 06 00 	lea    0x6d094(%rip),%rax        # 80ae00 <type:*+0x48e00>
  79dd6c:	48 8b 8c 24 b8 01 00 	mov    0x1b8(%rsp),%rcx
  79dd73:	00 
  79dd74:	48 8b bc 24 c0 01 00 	mov    0x1c0(%rsp),%rdi
  79dd7b:	00 
  79dd7c:	0f 1f 40 00          	nopl   0x0(%rax)
  79dd80:	e8 5b 4f c7 ff       	call   412ce0 <runtime.mapaccess1_faststr>
  79dd85:	48 83 78 08 00       	cmpq   $0x0,0x8(%rax)
  79dd8a:	75 39                	jne    79ddc5 <agentGoProject/common.LoadWindowsEvtxLog+0x6a5>
./C:/Program Files/Go/src/strings/strings.go:1089
  79dd8c:	48 8b 84 24 a8 00 00 	mov    0xa8(%rsp),%rax
  79dd93:	00 
strings.ReplaceAll():
./C:/Program Files/Go/src/strings/strings.go:1089
  79dd94:	48 8b 5c 24 68       	mov    0x68(%rsp),%rbx
  79dd99:	48 8d 0d 63 f9 0c 00 	lea    0xcf963(%rip),%rcx        # 86d703 <go:string.*+0x13b>
  79dda0:	bf 02 00 00 00       	mov    $0x2,%edi
  79dda5:	48 8d 35 c2 00 0d 00 	lea    0xd00c2(%rip),%rsi        # 86de6e <go:string.*+0x8a6>
  79ddac:	41 b8 05 00 00 00    	mov    $0x5,%r8d
  79ddb2:	49 c7 c1 ff ff ff ff 	mov    $0xffffffffffffffff,%r9
  79ddb9:	e8 a2 ce d5 ff       	call   4fac60 <strings.Replace>
  79ddbe:	66 90                	xchg   %ax,%ax
agentGoProject/common.LoadWindowsEvtxLog():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:78
  79ddc0:	e9 c6 00 00 00       	jmp    79de8b <agentGoProject/common.LoadWindowsEvtxLog+0x76b>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:81
  79ddc5:	48 8b 1d 94 59 40 00 	mov    0x405994(%rip),%rbx        # ba3760 <agentGoProject/common.readLogEndMarkMap>
  79ddcc:	48 8d 05 2d d0 06 00 	lea    0x6d02d(%rip),%rax        # 80ae00 <type:*+0x48e00>
  79ddd3:	48 8b 8c 24 b8 01 00 	mov    0x1b8(%rsp),%rcx
  79ddda:	00 
  79dddb:	48 8b bc 24 c0 01 00 	mov    0x1c0(%rsp),%rdi
  79dde2:	00 
  79dde3:	e8 f8 4e c7 ff       	call   412ce0 <runtime.mapaccess1_faststr>
  79dde8:	48 8b 08             	mov    (%rax),%rcx
  79ddeb:	48 8b 78 08          	mov    0x8(%rax),%rdi
  79ddef:	48 8b 84 24 a8 00 00 	mov    0xa8(%rsp),%rax
  79ddf6:	00 
  79ddf7:	48 8b 5c 24 68       	mov    0x68(%rsp),%rbx
  79ddfc:	0f 1f 40 00          	nopl   0x0(%rax)
  79de00:	e8 5b d8 d5 ff       	call   4fb660 <strings.Index>
  79de05:	48 85 c0             	test   %rax,%rax
  79de08:	7f 06                	jg     79de10 <agentGoProject/common.LoadWindowsEvtxLog+0x6f0>
  79de0a:	31 db                	xor    %ebx,%ebx
  79de0c:	31 c0                	xor    %eax,%eax
  79de0e:	eb 7b                	jmp    79de8b <agentGoProject/common.LoadWindowsEvtxLog+0x76b>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:82
  79de10:	48 8b 1d 49 59 40 00 	mov    0x405949(%rip),%rbx        # ba3760 <agentGoProject/common.readLogEndMarkMap>
  79de17:	48 8d 05 e2 cf 06 00 	lea    0x6cfe2(%rip),%rax        # 80ae00 <type:*+0x48e00>
  79de1e:	48 8b 8c 24 b8 01 00 	mov    0x1b8(%rsp),%rcx
  79de25:	00 
  79de26:	48 8b bc 24 c0 01 00 	mov    0x1c0(%rsp),%rdi
  79de2d:	00 
  79de2e:	e8 ad 4e c7 ff       	call   412ce0 <runtime.mapaccess1_faststr>
  79de33:	48 8b 08             	mov    (%rax),%rcx
  79de36:	48 8b 78 08          	mov    0x8(%rax),%rdi
  79de3a:	48 8b 84 24 a8 00 00 	mov    0xa8(%rsp),%rax
  79de41:	00 
  79de42:	48 8b 5c 24 68       	mov    0x68(%rsp),%rbx
  79de47:	e8 14 d8 d5 ff       	call   4fb660 <strings.Index>
  79de4c:	48 8b 54 24 68       	mov    0x68(%rsp),%rdx
  79de51:	48 39 d0             	cmp    %rdx,%rax
  79de54:	0f 87 bd 01 00 00    	ja     79e017 <agentGoProject/common.LoadWindowsEvtxLog+0x8f7>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:83
  79de5a:	90                   	nop
./C:/Program Files/Go/src/strings/strings.go:1089
  79de5b:	48 89 c3             	mov    %rax,%rbx
strings.ReplaceAll():
./C:/Program Files/Go/src/strings/strings.go:1089
  79de5e:	48 8d 0d 9e f8 0c 00 	lea    0xcf89e(%rip),%rcx        # 86d703 <go:string.*+0x13b>
  79de65:	bf 02 00 00 00       	mov    $0x2,%edi
  79de6a:	48 8d 35 fd ff 0c 00 	lea    0xcfffd(%rip),%rsi        # 86de6e <go:string.*+0x8a6>
  79de71:	41 b8 05 00 00 00    	mov    $0x5,%r8d
  79de77:	49 c7 c1 ff ff ff ff 	mov    $0xffffffffffffffff,%r9
  79de7e:	48 8b 84 24 a8 00 00 	mov    0xa8(%rsp),%rax
  79de85:	00 
  79de86:	e8 d5 cd d5 ff       	call   4fac60 <strings.Replace>
agentGoProject/common.LoadWindowsEvtxLog():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:89
  79de8b:	48 89 9c 24 88 00 00 	mov    %rbx,0x88(%rsp)
  79de92:	00 
  79de93:	48 89 84 24 b8 00 00 	mov    %rax,0xb8(%rsp)
  79de9a:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:87
  79de9b:	48 8b 15 be 58 40 00 	mov    0x4058be(%rip),%rdx        # ba3760 <agentGoProject/common.readLogEndMarkMap>
  79dea2:	48 8b 8c 24 b8 01 00 	mov    0x1b8(%rsp),%rcx
  79dea9:	00 
  79deaa:	48 8b bc 24 c0 01 00 	mov    0x1c0(%rsp),%rdi
  79deb1:	00 
  79deb2:	48 8d 05 47 cf 06 00 	lea    0x6cf47(%rip),%rax        # 80ae00 <type:*+0x48e00>
  79deb9:	48 89 d3             	mov    %rdx,%rbx
  79debc:	0f 1f 40 00          	nopl   0x0(%rax)
  79dec0:	e8 3b 56 c7 ff       	call   413500 <runtime.mapassign_faststr>
  79dec5:	48 8b 94 24 80 00 00 	mov    0x80(%rsp),%rdx
  79decc:	00 
  79decd:	48 89 50 08          	mov    %rdx,0x8(%rax)
  79ded1:	83 3d 78 76 43 00 00 	cmpl   $0x0,0x437678(%rip)        # bd5550 <runtime.writeBarrier>
  79ded8:	75 0d                	jne    79dee7 <agentGoProject/common.LoadWindowsEvtxLog+0x7c7>
  79deda:	48 8b 94 24 b0 00 00 	mov    0xb0(%rsp),%rdx
  79dee1:	00 
  79dee2:	48 89 10             	mov    %rdx,(%rax)
  79dee5:	eb 10                	jmp    79def7 <agentGoProject/common.LoadWindowsEvtxLog+0x7d7>
  79dee7:	48 89 c7             	mov    %rax,%rdi
  79deea:	48 8b 94 24 b0 00 00 	mov    0xb0(%rsp),%rdx
  79def1:	00 
  79def2:	e8 09 92 cc ff       	call   467100 <runtime.gcWriteBarrierDX>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:89
  79def7:	48 8b 84 24 78 01 00 	mov    0x178(%rsp),%rax
  79defe:	00 
  79deff:	48 8b 9c 24 80 01 00 	mov    0x180(%rsp),%rbx
  79df06:	00 
  79df07:	31 c9                	xor    %ecx,%ecx
  79df09:	31 ff                	xor    %edi,%edi
  79df0b:	48 8b b4 24 b8 00 00 	mov    0xb8(%rsp),%rsi
  79df12:	00 
  79df13:	4c 8b 84 24 88 00 00 	mov    0x88(%rsp),%r8
  79df1a:	00 
  79df1b:	0f 1f 44 00 00       	nopl   0x0(%rax,%rax,1)
  79df20:	e8 bb 01 00 00       	call   79e0e0 <agentGoProject/common.contentHandleTimeAsc>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:92
  79df25:	48 89 84 24 b8 00 00 	mov    %rax,0xb8(%rsp)
  79df2c:	00 
  79df2d:	48 89 9c 24 88 00 00 	mov    %rbx,0x88(%rsp)
  79df34:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:89
  79df35:	48 89 7c 24 60       	mov    %rdi,0x60(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:91
  79df3a:	48 8b 54 24 58       	mov    0x58(%rsp),%rdx
  79df3f:	48 01 ca             	add    %rcx,%rdx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:92
  79df42:	48 89 d0             	mov    %rdx,%rax
  79df45:	bb 0a 00 00 00       	mov    $0xa,%ebx
  79df4a:	e8 31 fb cd ff       	call   47da80 <strconv.FormatInt>
  79df4f:	48 89 84 24 c8 00 00 	mov    %rax,0xc8(%rsp)
  79df56:	00 
  79df57:	48 89 9c 24 90 00 00 	mov    %rbx,0x90(%rsp)
  79df5e:	00 
  79df5f:	48 8b 44 24 60       	mov    0x60(%rsp),%rax
  79df64:	bb 0a 00 00 00       	mov    $0xa,%ebx
  79df69:	e8 12 fb cd ff       	call   47da80 <strconv.FormatInt>
  79df6e:	48 8b 94 24 b8 00 00 	mov    0xb8(%rsp),%rdx
  79df75:	00 
  79df76:	48 89 94 24 00 01 00 	mov    %rdx,0x100(%rsp)
  79df7d:	00 
  79df7e:	48 8b 94 24 88 00 00 	mov    0x88(%rsp),%rdx
  79df85:	00 
  79df86:	48 89 94 24 08 01 00 	mov    %rdx,0x108(%rsp)
  79df8d:	00 
  79df8e:	48 8b 94 24 c8 00 00 	mov    0xc8(%rsp),%rdx
  79df95:	00 
  79df96:	48 89 94 24 f0 00 00 	mov    %rdx,0xf0(%rsp)
  79df9d:	00 
  79df9e:	48 8b 94 24 90 00 00 	mov    0x90(%rsp),%rdx
  79dfa5:	00 
  79dfa6:	48 89 94 24 f8 00 00 	mov    %rdx,0xf8(%rsp)
  79dfad:	00 
  79dfae:	48 8b 54 24 78       	mov    0x78(%rsp),%rdx
  79dfb3:	48 89 54 24 40       	mov    %rdx,0x40(%rsp)
  79dfb8:	48 89 84 24 e0 00 00 	mov    %rax,0xe0(%rsp)
  79dfbf:	00 
  79dfc0:	48 89 9c 24 e8 00 00 	mov    %rbx,0xe8(%rsp)
  79dfc7:	00 
  79dfc8:	c6 44 24 3f 00       	movb   $0x0,0x3f(%rsp)
  79dfcd:	e8 ce d5 01 00       	call   7bb5a0 <agentGoProject/common.LoadWindowsEvtxLog.func1>
  79dfd2:	48 8b 74 24 40       	mov    0x40(%rsp),%rsi
  79dfd7:	48 8b 84 24 00 01 00 	mov    0x100(%rsp),%rax
  79dfde:	00 
  79dfdf:	4c 8b 84 24 e0 00 00 	mov    0xe0(%rsp),%r8
  79dfe6:	00 
  79dfe7:	48 8b 8c 24 f0 00 00 	mov    0xf0(%rsp),%rcx
  79dfee:	00 
  79dfef:	4c 8b 8c 24 e8 00 00 	mov    0xe8(%rsp),%r9
  79dff6:	00 
  79dff7:	48 8b 9c 24 08 01 00 	mov    0x108(%rsp),%rbx
  79dffe:	00 
  79dfff:	48 8b bc 24 f8 00 00 	mov    0xf8(%rsp),%rdi
  79e006:	00 
  79e007:	48 8b ac 24 68 01 00 	mov    0x168(%rsp),%rbp
  79e00e:	00 
  79e00f:	48 81 c4 70 01 00 00 	add    $0x170,%rsp
  79e016:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:82
  79e017:	48 89 c1             	mov    %rax,%rcx
  79e01a:	e8 a1 94 cc ff       	call   4674c0 <runtime.panicSliceAlen>
  79e01f:	90                   	nop
bytes.(*Buffer).String():
./C:/Program Files/Go/src/bytes/buffer.go:65
  79e020:	e8 1b 95 cc ff       	call   467540 <runtime.panicSliceB>
  79e025:	90                   	nop
  79e026:	e8 f5 5c c9 ff       	call   433d20 <runtime.deferreturn>
  79e02b:	48 8b 84 24 00 01 00 	mov    0x100(%rsp),%rax
  79e032:	00 
  79e033:	48 8b 9c 24 08 01 00 	mov    0x108(%rsp),%rbx
  79e03a:	00 
  79e03b:	48 8b 8c 24 f0 00 00 	mov    0xf0(%rsp),%rcx
  79e042:	00 
  79e043:	48 8b bc 24 f8 00 00 	mov    0xf8(%rsp),%rdi
  79e04a:	00 
  79e04b:	48 8b 74 24 40       	mov    0x40(%rsp),%rsi
  79e050:	4c 8b 84 24 e0 00 00 	mov    0xe0(%rsp),%r8
  79e057:	00 
  79e058:	4c 8b 8c 24 e8 00 00 	mov    0xe8(%rsp),%r9
  79e05f:	00 
  79e060:	48 8b ac 24 68 01 00 	mov    0x168(%rsp),%rbp
  79e067:	00 
  79e068:	48 81 c4 70 01 00 00 	add    $0x170,%rsp
  79e06f:	c3                   	ret    
agentGoProject/common.LoadWindowsEvtxLog():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:19
  79e070:	48 89 44 24 18       	mov    %rax,0x18(%rsp)
  79e075:	48 89 5c 24 20       	mov    %rbx,0x20(%rsp)
  79e07a:	48 89 4c 24 28       	mov    %rcx,0x28(%rsp)
  79e07f:	48 89 7c 24 30       	mov    %rdi,0x30(%rsp)
  79e084:	48 89 74 24 38       	mov    %rsi,0x38(%rsp)
  79e089:	4c 89 44 24 40       	mov    %r8,0x40(%rsp)
  79e08e:	4c 89 4c 24 48       	mov    %r9,0x48(%rsp)
  79e093:	4c 89 54 24 50       	mov    %r10,0x50(%rsp)
  79e098:	e8 83 6f cc ff       	call   465020 <runtime.morestack_noctxt.abi0>
  79e09d:	48 8b 44 24 18       	mov    0x18(%rsp),%rax
  79e0a2:	48 8b 5c 24 20       	mov    0x20(%rsp),%rbx
  79e0a7:	48 8b 4c 24 28       	mov    0x28(%rsp),%rcx
  79e0ac:	48 8b 7c 24 30       	mov    0x30(%rsp),%rdi
  79e0b1:	48 8b 74 24 38       	mov    0x38(%rsp),%rsi
  79e0b6:	4c 8b 44 24 40       	mov    0x40(%rsp),%r8
  79e0bb:	4c 8b 4c 24 48       	mov    0x48(%rsp),%r9
  79e0c0:	4c 8b 54 24 50       	mov    0x50(%rsp),%r10
  79e0c5:	e9 56 f6 ff ff       	jmp    79d720 <agentGoProject/common.LoadWindowsEvtxLog>
  79e0ca:	cc                   	int3   
  79e0cb:	cc                   	int3   
  79e0cc:	cc                   	int3   
  79e0cd:	cc                   	int3   
  79e0ce:	cc                   	int3   
  79e0cf:	cc                   	int3   
  79e0d0:	cc                   	int3   
  79e0d1:	cc                   	int3   
  79e0d2:	cc                   	int3   
  79e0d3:	cc                   	int3   
  79e0d4:	cc                   	int3   
  79e0d5:	cc                   	int3   
  79e0d6:	cc                   	int3   
  79e0d7:	cc                   	int3   
  79e0d8:	cc                   	int3   
  79e0d9:	cc                   	int3   
  79e0da:	cc                   	int3   
  79e0db:	cc                   	int3   
  79e0dc:	cc                   	int3   
  79e0dd:	cc                   	int3   
  79e0de:	cc                   	int3   
  79e0df:	cc                   	int3   

000000000079e0e0 <agentGoProject/common.contentHandleTimeAsc>:
agentGoProject/common.contentHandleTimeAsc():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:100
  79e0e0:	4c 8d 64 24 f0       	lea    -0x10(%rsp),%r12
  79e0e5:	4d 3b 66 10          	cmp    0x10(%r14),%r12
  79e0e9:	0f 86 1c 02 00 00    	jbe    79e30b <agentGoProject/common.contentHandleTimeAsc+0x22b>
  79e0ef:	48 81 ec 90 00 00 00 	sub    $0x90,%rsp
  79e0f6:	48 89 ac 24 88 00 00 	mov    %rbp,0x88(%rsp)
  79e0fd:	00 
  79e0fe:	48 8d ac 24 88 00 00 	lea    0x88(%rsp),%rbp
  79e105:	00 
  79e106:	48 89 84 24 98 00 00 	mov    %rax,0x98(%rsp)
  79e10d:	00 
  79e10e:	48 89 8c 24 a8 00 00 	mov    %rcx,0xa8(%rsp)
  79e115:	00 
  79e116:	48 89 b4 24 b8 00 00 	mov    %rsi,0xb8(%rsp)
  79e11d:	00 
  79e11e:	66 90                	xchg   %ax,%ax
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:101
  79e120:	4d 85 c0             	test   %r8,%r8
  79e123:	0f 84 ab 00 00 00    	je     79e1d4 <agentGoProject/common.contentHandleTimeAsc+0xf4>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:100
  79e129:	48 89 84 24 98 00 00 	mov    %rax,0x98(%rsp)
  79e130:	00 
  79e131:	48 89 9c 24 a0 00 00 	mov    %rbx,0xa0(%rsp)
  79e138:	00 
  79e139:	48 89 8c 24 a8 00 00 	mov    %rcx,0xa8(%rsp)
  79e140:	00 
  79e141:	48 89 bc 24 b0 00 00 	mov    %rdi,0xb0(%rsp)
  79e148:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:106
  79e149:	90                   	nop
./C:/Program Files/Go/src/strings/strings.go:1089
  79e14a:	48 89 f0             	mov    %rsi,%rax
strings.ReplaceAll():
./C:/Program Files/Go/src/strings/strings.go:1089
  79e14d:	4c 89 c3             	mov    %r8,%rbx
  79e150:	48 8d 0d 50 1c 0d 00 	lea    0xd1c50(%rip),%rcx        # 86fda7 <go:string.*+0x27df>
  79e157:	bf 0a 00 00 00       	mov    $0xa,%edi
  79e15c:	48 8d 35 0b fd 0c 00 	lea    0xcfd0b(%rip),%rsi        # 86de6e <go:string.*+0x8a6>
  79e163:	41 b8 05 00 00 00    	mov    $0x5,%r8d
  79e169:	49 c7 c1 ff ff ff ff 	mov    $0xffffffffffffffff,%r9
  79e170:	e8 eb ca d5 ff       	call   4fac60 <strings.Replace>
  79e175:	48 89 44 24 78       	mov    %rax,0x78(%rsp)
  79e17a:	48 89 5c 24 50       	mov    %rbx,0x50(%rsp)
agentGoProject/common.contentHandleTimeAsc():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:108
  79e17f:	90                   	nop
./C:/Program Files/Go/src/strings/strings.go:305
  79e180:	48 8d 0d e7 fc 0c 00 	lea    0xcfce7(%rip),%rcx        # 86de6e <go:string.*+0x8a6>
strings.Split():
./C:/Program Files/Go/src/strings/strings.go:305
  79e187:	bf 05 00 00 00       	mov    $0x5,%edi
  79e18c:	31 f6                	xor    %esi,%esi
  79e18e:	49 c7 c0 ff ff ff ff 	mov    $0xffffffffffffffff,%r8
  79e195:	e8 a6 9c d5 ff       	call   4f7e40 <strings.genSplit>
agentGoProject/common.contentHandleTimeAsc():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:125
  79e19a:	48 89 5c 24 48       	mov    %rbx,0x48(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:110
  79e19f:	90                   	nop
./C:/Program Files/Go/src/strings/strings.go:305
  79e1a0:	48 8b 44 24 78       	mov    0x78(%rsp),%rax
strings.Split():
./C:/Program Files/Go/src/strings/strings.go:305
  79e1a5:	48 8d 0d 30 26 0d 00 	lea    0xd2630(%rip),%rcx        # 8707dc <go:string.*+0x3214>
  79e1ac:	bf 0b 00 00 00       	mov    $0xb,%edi
  79e1b1:	31 f6                	xor    %esi,%esi
  79e1b3:	49 c7 c0 ff ff ff ff 	mov    $0xffffffffffffffff,%r8
  79e1ba:	48 8b 5c 24 50       	mov    0x50(%rsp),%rbx
  79e1bf:	90                   	nop
  79e1c0:	e8 7b 9c d5 ff       	call   4f7e40 <strings.genSplit>
agentGoProject/common.contentHandleTimeAsc():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:113
  79e1c5:	48 89 44 24 70       	mov    %rax,0x70(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:112
  79e1ca:	48 8d 53 ff          	lea    -0x1(%rbx),%rdx
  79e1ce:	31 c9                	xor    %ecx,%ecx
  79e1d0:	31 db                	xor    %ebx,%ebx
  79e1d2:	eb 1e                	jmp    79e1f2 <agentGoProject/common.contentHandleTimeAsc+0x112>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:102
  79e1d4:	31 c0                	xor    %eax,%eax
  79e1d6:	31 db                	xor    %ebx,%ebx
  79e1d8:	48 89 d9             	mov    %rbx,%rcx
  79e1db:	48 89 cf             	mov    %rcx,%rdi
  79e1de:	48 8b ac 24 88 00 00 	mov    0x88(%rsp),%rbp
  79e1e5:	00 
  79e1e6:	48 81 c4 90 00 00 00 	add    $0x90,%rsp
  79e1ed:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:112
  79e1ee:	49 8d 51 ff          	lea    -0x1(%r9),%rdx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:121
  79e1f2:	48 89 4c 24 58       	mov    %rcx,0x58(%rsp)
  79e1f7:	48 89 9c 24 80 00 00 	mov    %rbx,0x80(%rsp)
  79e1fe:	00 
  79e1ff:	90                   	nop
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:112
  79e200:	48 85 d2             	test   %rdx,%rdx
  79e203:	0f 8c bc 00 00 00    	jl     79e2c5 <agentGoProject/common.contentHandleTimeAsc+0x1e5>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:113
  79e209:	49 89 d1             	mov    %rdx,%r9
  79e20c:	48 c1 e2 04          	shl    $0x4,%rdx
  79e210:	4c 8b 44 02 08       	mov    0x8(%rdx,%rax,1),%r8
  79e215:	48 8b 34 02          	mov    (%rdx,%rax,1),%rsi
  79e219:	4d 85 c0             	test   %r8,%r8
  79e21c:	74 d0                	je     79e1ee <agentGoProject/common.contentHandleTimeAsc+0x10e>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:112
  79e21e:	4c 89 4c 24 68       	mov    %r9,0x68(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:113
  79e223:	48 89 54 24 60       	mov    %rdx,0x60(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:118
  79e228:	48 8b 84 24 98 00 00 	mov    0x98(%rsp),%rax
  79e22f:	00 
  79e230:	48 8b 9c 24 a0 00 00 	mov    0xa0(%rsp),%rbx
  79e237:	00 
  79e238:	48 8b 8c 24 a8 00 00 	mov    0xa8(%rsp),%rcx
  79e23f:	00 
  79e240:	48 8b bc 24 b0 00 00 	mov    0xb0(%rsp),%rdi
  79e247:	00 
  79e248:	e8 13 01 00 00       	call   79e360 <agentGoProject/common.checkHaveChars>
  79e24d:	48 85 db             	test   %rbx,%rbx
  79e250:	75 19                	jne    79e26b <agentGoProject/common.contentHandleTimeAsc+0x18b>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:113
  79e252:	48 8b 44 24 70       	mov    0x70(%rsp),%rax
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:112
  79e257:	4c 8b 4c 24 68       	mov    0x68(%rsp),%r9
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:121
  79e25c:	48 8b 4c 24 58       	mov    0x58(%rsp),%rcx
  79e261:	48 8b 9c 24 80 00 00 	mov    0x80(%rsp),%rbx
  79e268:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:119
  79e269:	eb 83                	jmp    79e1ee <agentGoProject/common.contentHandleTimeAsc+0x10e>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:121
  79e26b:	48 8b 54 24 70       	mov    0x70(%rsp),%rdx
  79e270:	4c 8b 64 24 60       	mov    0x60(%rsp),%r12
  79e275:	4d 8b 04 14          	mov    (%r12,%rdx,1),%r8
  79e279:	4d 8b 4c 14 08       	mov    0x8(%r12,%rdx,1),%r9
  79e27e:	31 c0                	xor    %eax,%eax
  79e280:	48 8b 9c 24 80 00 00 	mov    0x80(%rsp),%rbx
  79e287:	00 
  79e288:	48 8b 4c 24 58       	mov    0x58(%rsp),%rcx
  79e28d:	48 8d 3d 48 25 0d 00 	lea    0xd2548(%rip),%rdi        # 8707dc <go:string.*+0x3214>
  79e294:	be 0b 00 00 00       	mov    $0xb,%esi
  79e299:	4c 8d 15 ce fb 0c 00 	lea    0xcfbce(%rip),%r10        # 86de6e <go:string.*+0x8a6>
  79e2a0:	41 bb 05 00 00 00    	mov    $0x5,%r11d
  79e2a6:	e8 15 22 cb ff       	call   4504c0 <runtime.concatstring4>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:112
  79e2ab:	4c 8b 4c 24 68       	mov    0x68(%rsp),%r9
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:121
  79e2b0:	48 89 d9             	mov    %rbx,%rcx
  79e2b3:	48 89 c3             	mov    %rax,%rbx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:113
  79e2b6:	48 8b 44 24 70       	mov    0x70(%rsp),%rax
  79e2bb:	0f 1f 44 00 00       	nopl   0x0(%rax,%rax,1)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:121
  79e2c0:	e9 29 ff ff ff       	jmp    79e1ee <agentGoProject/common.contentHandleTimeAsc+0x10e>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:124
  79e2c5:	90                   	nop
./C:/Program Files/Go/src/strings/strings.go:305
  79e2c6:	48 89 d8             	mov    %rbx,%rax
strings.Split():
./C:/Program Files/Go/src/strings/strings.go:305
  79e2c9:	48 89 cb             	mov    %rcx,%rbx
  79e2cc:	48 8d 0d 9b fb 0c 00 	lea    0xcfb9b(%rip),%rcx        # 86de6e <go:string.*+0x8a6>
  79e2d3:	bf 05 00 00 00       	mov    $0x5,%edi
  79e2d8:	31 f6                	xor    %esi,%esi
  79e2da:	49 c7 c0 ff ff ff ff 	mov    $0xffffffffffffffff,%r8
  79e2e1:	e8 5a 9b d5 ff       	call   4f7e40 <strings.genSplit>
agentGoProject/common.contentHandleTimeAsc():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:125
  79e2e6:	48 8b 84 24 80 00 00 	mov    0x80(%rsp),%rax
  79e2ed:	00 
  79e2ee:	48 8b 4c 24 48       	mov    0x48(%rsp),%rcx
  79e2f3:	48 89 df             	mov    %rbx,%rdi
  79e2f6:	48 8b 5c 24 58       	mov    0x58(%rsp),%rbx
  79e2fb:	48 8b ac 24 88 00 00 	mov    0x88(%rsp),%rbp
  79e302:	00 
  79e303:	48 81 c4 90 00 00 00 	add    $0x90,%rsp
  79e30a:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:100
  79e30b:	48 89 44 24 08       	mov    %rax,0x8(%rsp)
  79e310:	48 89 5c 24 10       	mov    %rbx,0x10(%rsp)
  79e315:	48 89 4c 24 18       	mov    %rcx,0x18(%rsp)
  79e31a:	48 89 7c 24 20       	mov    %rdi,0x20(%rsp)
  79e31f:	48 89 74 24 28       	mov    %rsi,0x28(%rsp)
  79e324:	4c 89 44 24 30       	mov    %r8,0x30(%rsp)
  79e329:	e8 f2 6c cc ff       	call   465020 <runtime.morestack_noctxt.abi0>
  79e32e:	48 8b 44 24 08       	mov    0x8(%rsp),%rax
  79e333:	48 8b 5c 24 10       	mov    0x10(%rsp),%rbx
  79e338:	48 8b 4c 24 18       	mov    0x18(%rsp),%rcx
  79e33d:	48 8b 7c 24 20       	mov    0x20(%rsp),%rdi
  79e342:	48 8b 74 24 28       	mov    0x28(%rsp),%rsi
  79e347:	4c 8b 44 24 30       	mov    0x30(%rsp),%r8
  79e34c:	e9 8f fd ff ff       	jmp    79e0e0 <agentGoProject/common.contentHandleTimeAsc>
  79e351:	cc                   	int3   
  79e352:	cc                   	int3   
  79e353:	cc                   	int3   
  79e354:	cc                   	int3   
  79e355:	cc                   	int3   
  79e356:	cc                   	int3   
  79e357:	cc                   	int3   
  79e358:	cc                   	int3   
  79e359:	cc                   	int3   
  79e35a:	cc                   	int3   
  79e35b:	cc                   	int3   
  79e35c:	cc                   	int3   
  79e35d:	cc                   	int3   
  79e35e:	cc                   	int3   
  79e35f:	cc                   	int3   

000000000079e360 <agentGoProject/common.checkHaveChars>:
agentGoProject/common.checkHaveChars():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:132
  79e360:	4c 8d 64 24 f8       	lea    -0x8(%rsp),%r12
  79e365:	4d 3b 66 10          	cmp    0x10(%r14),%r12
  79e369:	0f 86 bb 02 00 00    	jbe    79e62a <agentGoProject/common.checkHaveChars+0x2ca>
  79e36f:	48 81 ec 88 00 00 00 	sub    $0x88,%rsp
  79e376:	48 89 ac 24 80 00 00 	mov    %rbp,0x80(%rsp)
  79e37d:	00 
  79e37e:	48 8d ac 24 80 00 00 	lea    0x80(%rsp),%rbp
  79e385:	00 
  79e386:	48 89 b4 24 b0 00 00 	mov    %rsi,0xb0(%rsp)
  79e38d:	00 
  79e38e:	48 89 bc 24 a8 00 00 	mov    %rdi,0xa8(%rsp)
  79e395:	00 
  79e396:	48 89 84 24 90 00 00 	mov    %rax,0x90(%rsp)
  79e39d:	00 
  79e39e:	48 89 9c 24 98 00 00 	mov    %rbx,0x98(%rsp)
  79e3a5:	00 
  79e3a6:	48 89 8c 24 a0 00 00 	mov    %rcx,0xa0(%rsp)
  79e3ad:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:134
  79e3ae:	49 c7 c1 ff ff ff ff 	mov    $0xffffffffffffffff,%r9
./C:/Program Files/Go/src/strings/strings.go:1089
  79e3b5:	48 89 f0             	mov    %rsi,%rax
strings.ReplaceAll():
./C:/Program Files/Go/src/strings/strings.go:1089
  79e3b8:	4c 89 c3             	mov    %r8,%rbx
  79e3bb:	48 8d 0d ac fa 0c 00 	lea    0xcfaac(%rip),%rcx        # 86de6e <go:string.*+0x8a6>
  79e3c2:	bf 05 00 00 00       	mov    $0x5,%edi
  79e3c7:	31 f6                	xor    %esi,%esi
  79e3c9:	45 31 c0             	xor    %r8d,%r8d
  79e3cc:	e8 8f c8 d5 ff       	call   4fac60 <strings.Replace>
agentGoProject/common.checkHaveChars():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:135
  79e3d1:	90                   	nop
./C:/Program Files/Go/src/strings/strings.go:1089
  79e3d2:	48 8d 0d 67 0e 17 00 	lea    0x170e67(%rip),%rcx        # 90f240 <go:buildinfo.ref+0x30>
strings.ReplaceAll():
./C:/Program Files/Go/src/strings/strings.go:1089
  79e3d9:	bf 01 00 00 00       	mov    $0x1,%edi
  79e3de:	31 f6                	xor    %esi,%esi
  79e3e0:	45 31 c0             	xor    %r8d,%r8d
  79e3e3:	49 c7 c1 ff ff ff ff 	mov    $0xffffffffffffffff,%r9
  79e3ea:	e8 71 c8 d5 ff       	call   4fac60 <strings.Replace>
agentGoProject/common.checkHaveChars():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:136
  79e3ef:	90                   	nop
./C:/Program Files/Go/src/strings/strings.go:1089
  79e3f0:	48 8d 0d 79 f1 16 00 	lea    0x16f179(%rip),%rcx        # 90d570 <runtime.gcbits.*+0x110>
strings.ReplaceAll():
./C:/Program Files/Go/src/strings/strings.go:1089
  79e3f7:	bf 01 00 00 00       	mov    $0x1,%edi
  79e3fc:	31 f6                	xor    %esi,%esi
  79e3fe:	45 31 c0             	xor    %r8d,%r8d
  79e401:	49 c7 c1 ff ff ff ff 	mov    $0xffffffffffffffff,%r9
  79e408:	e8 53 c8 d5 ff       	call   4fac60 <strings.Replace>
agentGoProject/common.checkHaveChars():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:138
  79e40d:	48 8b 94 24 98 00 00 	mov    0x98(%rsp),%rdx
  79e414:	00 
  79e415:	48 85 d2             	test   %rdx,%rdx
  79e418:	75 1d                	jne    79e437 <agentGoProject/common.checkHaveChars+0xd7>
  79e41a:	48 8b 8c 24 a8 00 00 	mov    0xa8(%rsp),%rcx
  79e421:	00 
  79e422:	48 85 c9             	test   %rcx,%rcx
  79e425:	75 10                	jne    79e437 <agentGoProject/common.checkHaveChars+0xd7>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:139
  79e427:	48 8b ac 24 80 00 00 	mov    0x80(%rsp),%rbp
  79e42e:	00 
  79e42f:	48 81 c4 88 00 00 00 	add    $0x88,%rsp
  79e436:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:141
  79e437:	e8 04 b7 d5 ff       	call   4f9b40 <strings.ToLower>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:161
  79e43c:	48 89 44 24 78       	mov    %rax,0x78(%rsp)
  79e441:	48 89 5c 24 58       	mov    %rbx,0x58(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:143
  79e446:	48 8b 84 24 90 00 00 	mov    0x90(%rsp),%rax
  79e44d:	00 
  79e44e:	48 8b 9c 24 98 00 00 	mov    0x98(%rsp),%rbx
  79e455:	00 
  79e456:	e8 e5 b6 d5 ff       	call   4f9b40 <strings.ToLower>
  79e45b:	48 89 5c 24 50       	mov    %rbx,0x50(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:144
  79e460:	90                   	nop
./C:/Program Files/Go/src/strings/strings.go:305
  79e461:	48 8d 0d d0 f2 16 00 	lea    0x16f2d0(%rip),%rcx        # 90d738 <runtime.gcbits.*+0x2d8>
strings.Split():
./C:/Program Files/Go/src/strings/strings.go:305
  79e468:	bf 01 00 00 00       	mov    $0x1,%edi
  79e46d:	31 f6                	xor    %esi,%esi
  79e46f:	49 c7 c0 ff ff ff ff 	mov    $0xffffffffffffffff,%r8
  79e476:	e8 c5 99 d5 ff       	call   4f7e40 <strings.genSplit>
agentGoProject/common.checkHaveChars():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:168
  79e47b:	48 89 44 24 70       	mov    %rax,0x70(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:167
  79e480:	48 89 5c 24 40       	mov    %rbx,0x40(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:146
  79e485:	48 8b 84 24 a0 00 00 	mov    0xa0(%rsp),%rax
  79e48c:	00 
  79e48d:	48 8b 9c 24 a8 00 00 	mov    0xa8(%rsp),%rbx
  79e494:	00 
  79e495:	e8 a6 b6 d5 ff       	call   4f9b40 <strings.ToLower>
  79e49a:	48 89 5c 24 48       	mov    %rbx,0x48(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:147
  79e49f:	90                   	nop
./C:/Program Files/Go/src/strings/strings.go:305
  79e4a0:	48 8d 0d 91 f2 16 00 	lea    0x16f291(%rip),%rcx        # 90d738 <runtime.gcbits.*+0x2d8>
strings.Split():
./C:/Program Files/Go/src/strings/strings.go:305
  79e4a7:	bf 01 00 00 00       	mov    $0x1,%edi
  79e4ac:	31 f6                	xor    %esi,%esi
  79e4ae:	49 c7 c0 ff ff ff ff 	mov    $0xffffffffffffffff,%r8
  79e4b5:	e8 86 99 d5 ff       	call   4f7e40 <strings.genSplit>
agentGoProject/common.checkHaveChars():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:149
  79e4ba:	48 8b 4c 24 50       	mov    0x50(%rsp),%rcx
  79e4bf:	90                   	nop
  79e4c0:	48 85 c9             	test   %rcx,%rcx
  79e4c3:	75 25                	jne    79e4ea <agentGoProject/common.checkHaveChars+0x18a>
  79e4c5:	48 8b 54 24 48       	mov    0x48(%rsp),%rdx
  79e4ca:	48 85 d2             	test   %rdx,%rdx
  79e4cd:	74 16                	je     79e4e5 <agentGoProject/common.checkHaveChars+0x185>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:152
  79e4cf:	48 89 5c 24 38       	mov    %rbx,0x38(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:153
  79e4d4:	48 89 44 24 68       	mov    %rax,0x68(%rsp)
  79e4d9:	31 c9                	xor    %ecx,%ecx
  79e4db:	0f 1f 44 00 00       	nopl   0x0(%rax,%rax,1)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:149
  79e4e0:	e9 c9 00 00 00       	jmp    79e5ae <agentGoProject/common.checkHaveChars+0x24e>
  79e4e5:	48 85 c9             	test   %rcx,%rcx
  79e4e8:	eb 05                	jmp    79e4ef <agentGoProject/common.checkHaveChars+0x18f>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:164
  79e4ea:	48 8b 54 24 48       	mov    0x48(%rsp),%rdx
  79e4ef:	74 22                	je     79e513 <agentGoProject/common.checkHaveChars+0x1b3>
  79e4f1:	48 85 d2             	test   %rdx,%rdx
  79e4f4:	74 0f                	je     79e505 <agentGoProject/common.checkHaveChars+0x1a5>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:149
  79e4f6:	48 85 c9             	test   %rcx,%rcx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:164
  79e4f9:	74 18                	je     79e513 <agentGoProject/common.checkHaveChars+0x1b3>
  79e4fb:	0f 1f 44 00 00       	nopl   0x0(%rax,%rax,1)
  79e500:	48 85 d2             	test   %rdx,%rdx
  79e503:	74 0e                	je     79e513 <agentGoProject/common.checkHaveChars+0x1b3>
  79e505:	48 8b 54 24 40       	mov    0x40(%rsp),%rdx
  79e50a:	48 8b 74 24 70       	mov    0x70(%rsp),%rsi
  79e50f:	31 c0                	xor    %eax,%eax
  79e511:	eb 1e                	jmp    79e531 <agentGoProject/common.checkHaveChars+0x1d1>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:178
  79e513:	48 8b 44 24 78       	mov    0x78(%rsp),%rax
  79e518:	48 8b 5c 24 58       	mov    0x58(%rsp),%rbx
  79e51d:	48 8b ac 24 80 00 00 	mov    0x80(%rsp),%rbp
  79e524:	00 
  79e525:	48 81 c4 88 00 00 00 	add    $0x88,%rsp
  79e52c:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:167
  79e52d:	49 8d 40 01          	lea    0x1(%r8),%rax
  79e531:	48 39 c2             	cmp    %rax,%rdx
  79e534:	7e 5a                	jle    79e590 <agentGoProject/common.checkHaveChars+0x230>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:168
  79e536:	49 89 c0             	mov    %rax,%r8
  79e539:	48 c1 e0 04          	shl    $0x4,%rax
  79e53d:	48 8b 7c 06 08       	mov    0x8(%rsi,%rax,1),%rdi
  79e542:	48 8b 0c 06          	mov    (%rsi,%rax,1),%rcx
  79e546:	48 85 ff             	test   %rdi,%rdi
  79e549:	75 04                	jne    79e54f <agentGoProject/common.checkHaveChars+0x1ef>
  79e54b:	31 c0                	xor    %eax,%eax
  79e54d:	eb 31                	jmp    79e580 <agentGoProject/common.checkHaveChars+0x220>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:167
  79e54f:	4c 89 44 24 60       	mov    %r8,0x60(%rsp)
./C:/Program Files/Go/src/strings/strings.go:59
  79e554:	48 8b 44 24 78       	mov    0x78(%rsp),%rax
strings.Contains():
./C:/Program Files/Go/src/strings/strings.go:59
  79e559:	48 8b 5c 24 58       	mov    0x58(%rsp),%rbx
  79e55e:	66 90                	xchg   %ax,%ax
  79e560:	e8 fb d0 d5 ff       	call   4fb660 <strings.Index>
  79e565:	48 85 c0             	test   %rax,%rax
  79e568:	0f 9d c2             	setge  %dl
agentGoProject/common.checkHaveChars():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:168
  79e56b:	48 8b 74 24 70       	mov    0x70(%rsp),%rsi
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:167
  79e570:	4c 8b 44 24 60       	mov    0x60(%rsp),%r8
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:168
  79e575:	89 d0                	mov    %edx,%eax
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:167
  79e577:	48 8b 54 24 40       	mov    0x40(%rsp),%rdx
  79e57c:	0f 1f 40 00          	nopl   0x0(%rax)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:168
  79e580:	84 c0                	test   %al,%al
  79e582:	74 a9                	je     79e52d <agentGoProject/common.checkHaveChars+0x1cd>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:176
  79e584:	48 8b 44 24 58       	mov    0x58(%rsp),%rax
  79e589:	48 8b 4c 24 78       	mov    0x78(%rsp),%rcx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:173
  79e58e:	eb 04                	jmp    79e594 <agentGoProject/common.checkHaveChars+0x234>
  79e590:	31 c0                	xor    %eax,%eax
  79e592:	31 c9                	xor    %ecx,%ecx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:176
  79e594:	48 89 c3             	mov    %rax,%rbx
  79e597:	48 89 c8             	mov    %rcx,%rax
  79e59a:	48 8b ac 24 80 00 00 	mov    0x80(%rsp),%rbp
  79e5a1:	00 
  79e5a2:	48 81 c4 88 00 00 00 	add    $0x88,%rsp
  79e5a9:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:152
  79e5aa:	48 8d 4a 01          	lea    0x1(%rdx),%rcx
  79e5ae:	48 39 cb             	cmp    %rcx,%rbx
  79e5b1:	7e 57                	jle    79e60a <agentGoProject/common.checkHaveChars+0x2aa>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:153
  79e5b3:	48 89 ca             	mov    %rcx,%rdx
  79e5b6:	48 c1 e1 04          	shl    $0x4,%rcx
  79e5ba:	48 8b 7c 08 08       	mov    0x8(%rax,%rcx,1),%rdi
  79e5bf:	48 8b 0c 08          	mov    (%rax,%rcx,1),%rcx
  79e5c3:	48 85 ff             	test   %rdi,%rdi
  79e5c6:	75 04                	jne    79e5cc <agentGoProject/common.checkHaveChars+0x26c>
  79e5c8:	31 c9                	xor    %ecx,%ecx
  79e5ca:	eb 34                	jmp    79e600 <agentGoProject/common.checkHaveChars+0x2a0>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:152
  79e5cc:	48 89 54 24 60       	mov    %rdx,0x60(%rsp)
./C:/Program Files/Go/src/strings/strings.go:59
  79e5d1:	48 8b 44 24 78       	mov    0x78(%rsp),%rax
strings.Contains():
./C:/Program Files/Go/src/strings/strings.go:59
  79e5d6:	48 8b 5c 24 58       	mov    0x58(%rsp),%rbx
  79e5db:	0f 1f 44 00 00       	nopl   0x0(%rax,%rax,1)
  79e5e0:	e8 7b d0 d5 ff       	call   4fb660 <strings.Index>
  79e5e5:	48 85 c0             	test   %rax,%rax
  79e5e8:	0f 9d c2             	setge  %dl
agentGoProject/common.checkHaveChars():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:153
  79e5eb:	48 8b 44 24 68       	mov    0x68(%rsp),%rax
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:152
  79e5f0:	48 8b 5c 24 38       	mov    0x38(%rsp),%rbx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:153
  79e5f5:	89 d1                	mov    %edx,%ecx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:152
  79e5f7:	48 8b 54 24 60       	mov    0x60(%rsp),%rdx
  79e5fc:	0f 1f 40 00          	nopl   0x0(%rax)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:153
  79e600:	84 c9                	test   %cl,%cl
  79e602:	74 a6                	je     79e5aa <agentGoProject/common.checkHaveChars+0x24a>
  79e604:	31 c0                	xor    %eax,%eax
  79e606:	31 c9                	xor    %ecx,%ecx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:158
  79e608:	eb 0a                	jmp    79e614 <agentGoProject/common.checkHaveChars+0x2b4>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:161
  79e60a:	48 8b 44 24 58       	mov    0x58(%rsp),%rax
  79e60f:	48 8b 4c 24 78       	mov    0x78(%rsp),%rcx
  79e614:	48 89 c3             	mov    %rax,%rbx
  79e617:	48 89 c8             	mov    %rcx,%rax
  79e61a:	48 8b ac 24 80 00 00 	mov    0x80(%rsp),%rbp
  79e621:	00 
  79e622:	48 81 c4 88 00 00 00 	add    $0x88,%rsp
  79e629:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/evtxLogUtil.go:132
  79e62a:	48 89 44 24 08       	mov    %rax,0x8(%rsp)
  79e62f:	48 89 5c 24 10       	mov    %rbx,0x10(%rsp)
  79e634:	48 89 4c 24 18       	mov    %rcx,0x18(%rsp)
  79e639:	48 89 7c 24 20       	mov    %rdi,0x20(%rsp)
  79e63e:	48 89 74 24 28       	mov    %rsi,0x28(%rsp)
  79e643:	4c 89 44 24 30       	mov    %r8,0x30(%rsp)
  79e648:	e8 d3 69 cc ff       	call   465020 <runtime.morestack_noctxt.abi0>
  79e64d:	48 8b 44 24 08       	mov    0x8(%rsp),%rax
  79e652:	48 8b 5c 24 10       	mov    0x10(%rsp),%rbx
  79e657:	48 8b 4c 24 18       	mov    0x18(%rsp),%rcx
  79e65c:	48 8b 7c 24 20       	mov    0x20(%rsp),%rdi
  79e661:	48 8b 74 24 28       	mov    0x28(%rsp),%rsi
  79e666:	4c 8b 44 24 30       	mov    0x30(%rsp),%r8
  79e66b:	e9 f0 fc ff ff       	jmp    79e360 <agentGoProject/common.checkHaveChars>
  79e670:	cc                   	int3   
  79e671:	cc                   	int3   
  79e672:	cc                   	int3   
  79e673:	cc                   	int3   
  79e674:	cc                   	int3   
  79e675:	cc                   	int3   
  79e676:	cc                   	int3   
  79e677:	cc                   	int3   
  79e678:	cc                   	int3   
  79e679:	cc                   	int3   
  79e67a:	cc                   	int3   
  79e67b:	cc                   	int3   
  79e67c:	cc                   	int3   
  79e67d:	cc                   	int3   
  79e67e:	cc                   	int3   
  79e67f:	cc                   	int3   

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

000000000079ee80 <agentGoProject/common.GetFileSafeInfos.func3>:
agentGoProject/common.GetFileSafeInfos.func3():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:96
  79ee80:	49 3b 66 10          	cmp    0x10(%r14),%rsp
  79ee84:	76 2a                	jbe    79eeb0 <agentGoProject/common.GetFileSafeInfos.func3+0x30>
  79ee86:	48 83 ec 10          	sub    $0x10,%rsp
  79ee8a:	48 89 6c 24 08       	mov    %rbp,0x8(%rsp)
  79ee8f:	48 8d 6c 24 08       	lea    0x8(%rsp),%rbp
  79ee94:	4d 8b 66 20          	mov    0x20(%r14),%r12
  79ee98:	4d 85 e4             	test   %r12,%r12
  79ee9b:	75 1a                	jne    79eeb7 <agentGoProject/common.GetFileSafeInfos.func3+0x37>
  79ee9d:	48 8b 42 08          	mov    0x8(%rdx),%rax
  79eea1:	e8 3a 1b cd ff       	call   4709e0 <sync.(*Mutex).Unlock>
  79eea6:	48 8b 6c 24 08       	mov    0x8(%rsp),%rbp
  79eeab:	48 83 c4 10          	add    $0x10,%rsp
  79eeaf:	c3                   	ret    
  79eeb0:	e8 cb 60 cc ff       	call   464f80 <runtime.morestack.abi0>
  79eeb5:	eb c9                	jmp    79ee80 <agentGoProject/common.GetFileSafeInfos.func3>
  79eeb7:	4c 8d 6c 24 18       	lea    0x18(%rsp),%r13
  79eebc:	0f 1f 40 00          	nopl   0x0(%rax)
  79eec0:	4d 39 2c 24          	cmp    %r13,(%r12)
  79eec4:	75 d7                	jne    79ee9d <agentGoProject/common.GetFileSafeInfos.func3+0x1d>
  79eec6:	49 89 24 24          	mov    %rsp,(%r12)
  79eeca:	eb d1                	jmp    79ee9d <agentGoProject/common.GetFileSafeInfos.func3+0x1d>
  79eecc:	cc                   	int3   
  79eecd:	cc                   	int3   
  79eece:	cc                   	int3   
  79eecf:	cc                   	int3   
  79eed0:	cc                   	int3   
  79eed1:	cc                   	int3   
  79eed2:	cc                   	int3   
  79eed3:	cc                   	int3   
  79eed4:	cc                   	int3   
  79eed5:	cc                   	int3   
  79eed6:	cc                   	int3   
  79eed7:	cc                   	int3   
  79eed8:	cc                   	int3   
  79eed9:	cc                   	int3   
  79eeda:	cc                   	int3   
  79eedb:	cc                   	int3   
  79eedc:	cc                   	int3   
  79eedd:	cc                   	int3   
  79eede:	cc                   	int3   
  79eedf:	cc                   	int3   

000000000079eee0 <agentGoProject/common.GetFileSafeInfos.func2>:
agentGoProject/common.GetFileSafeInfos.func2():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:76
  79eee0:	49 3b 66 10          	cmp    0x10(%r14),%rsp
  79eee4:	76 2f                	jbe    79ef15 <agentGoProject/common.GetFileSafeInfos.func2+0x35>
  79eee6:	48 83 ec 10          	sub    $0x10,%rsp
  79eeea:	48 89 6c 24 08       	mov    %rbp,0x8(%rsp)
  79eeef:	48 8d 6c 24 08       	lea    0x8(%rsp),%rbp
  79eef4:	4d 8b 66 20          	mov    0x20(%r14),%r12
  79eef8:	4d 85 e4             	test   %r12,%r12
  79eefb:	75 1f                	jne    79ef1c <agentGoProject/common.GetFileSafeInfos.func2+0x3c>
  79eefd:	48 8b 4a 08          	mov    0x8(%rdx),%rcx
  79ef01:	48 8b 42 10          	mov    0x10(%rdx),%rax
  79ef05:	48 8b 49 18          	mov    0x18(%rcx),%rcx
  79ef09:	ff d1                	call   *%rcx
  79ef0b:	48 8b 6c 24 08       	mov    0x8(%rsp),%rbp
  79ef10:	48 83 c4 10          	add    $0x10,%rsp
  79ef14:	c3                   	ret    
  79ef15:	e8 66 60 cc ff       	call   464f80 <runtime.morestack.abi0>
  79ef1a:	eb c4                	jmp    79eee0 <agentGoProject/common.GetFileSafeInfos.func2>
  79ef1c:	4c 8d 6c 24 18       	lea    0x18(%rsp),%r13
  79ef21:	4d 39 2c 24          	cmp    %r13,(%r12)
  79ef25:	75 d6                	jne    79eefd <agentGoProject/common.GetFileSafeInfos.func2+0x1d>
  79ef27:	49 89 24 24          	mov    %rsp,(%r12)
  79ef2b:	eb d0                	jmp    79eefd <agentGoProject/common.GetFileSafeInfos.func2+0x1d>
  79ef2d:	cc                   	int3   
  79ef2e:	cc                   	int3   
  79ef2f:	cc                   	int3   
  79ef30:	cc                   	int3   
  79ef31:	cc                   	int3   
  79ef32:	cc                   	int3   
  79ef33:	cc                   	int3   
  79ef34:	cc                   	int3   
  79ef35:	cc                   	int3   
  79ef36:	cc                   	int3   
  79ef37:	cc                   	int3   
  79ef38:	cc                   	int3   
  79ef39:	cc                   	int3   
  79ef3a:	cc                   	int3   
  79ef3b:	cc                   	int3   
  79ef3c:	cc                   	int3   
  79ef3d:	cc                   	int3   
  79ef3e:	cc                   	int3   
  79ef3f:	cc                   	int3   

000000000079ef40 <agentGoProject/common.CompareFileMd5>:
agentGoProject/common.CompareFileMd5():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:106
  79ef40:	4c 8d a4 24 f8 fe ff 	lea    -0x108(%rsp),%r12
  79ef47:	ff 
  79ef48:	4d 3b 66 10          	cmp    0x10(%r14),%r12
  79ef4c:	0f 86 e8 02 00 00    	jbe    79f23a <agentGoProject/common.CompareFileMd5+0x2fa>
  79ef52:	48 81 ec 88 01 00 00 	sub    $0x188,%rsp
  79ef59:	48 89 ac 24 80 01 00 	mov    %rbp,0x180(%rsp)
  79ef60:	00 
  79ef61:	48 8d ac 24 80 01 00 	lea    0x180(%rsp),%rbp
  79ef68:	00 
  79ef69:	49 c7 c5 00 00 00 00 	mov    $0x0,%r13
  79ef70:	4c 89 ac 24 78 01 00 	mov    %r13,0x178(%rsp)
  79ef77:	00 
  79ef78:	c6 44 24 1f 00       	movb   $0x0,0x1f(%rsp)
  79ef7d:	48 c7 44 24 20 00 00 	movq   $0x0,0x20(%rsp)
  79ef84:	00 00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:107
  79ef86:	48 8d 05 b3 7f 10 00 	lea    0x107fb3(%rip),%rax        # 8a6f40 <go:func.*+0x860>
  79ef8d:	48 89 84 24 78 01 00 	mov    %rax,0x178(%rsp)
  79ef94:	00 
  79ef95:	c6 44 24 1f 01       	movb   $0x1,0x1f(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:117
  79ef9a:	48 8b 05 97 47 40 00 	mov    0x404797(%rip),%rax        # ba3738 <agentGoProject/common.FileSafeList>
  79efa1:	48 89 44 24 38       	mov    %rax,0x38(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:118
  79efa6:	e8 15 f8 c6 ff       	call   40e7c0 <runtime.makemap_small>
./C:/Program Files/Go/src/container/list/list.go:66
  79efab:	48 8b 4c 24 38       	mov    0x38(%rsp),%rcx
container/list.(*List).Len():
./C:/Program Files/Go/src/container/list/list.go:66
  79efb0:	48 83 79 28 01       	cmpq   $0x1,0x28(%rcx)
agentGoProject/common.CompareFileMd5():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:119
  79efb5:	7c 2b                	jl     79efe2 <agentGoProject/common.CompareFileMd5+0xa2>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:118
  79efb7:	48 89 44 24 28       	mov    %rax,0x28(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:123
  79efbc:	48 8d 05 9d 6c 09 00 	lea    0x96c9d(%rip),%rax        # 835c60 <type:*+0x73c60>
  79efc3:	e8 b8 ec c6 ff       	call   40dc80 <runtime.newobject>
./C:/Program Files/Go/src/container/list/list.go:70
  79efc8:	48 8b 4c 24 38       	mov    0x38(%rsp),%rcx
container/list.(*List).Front():
./C:/Program Files/Go/src/container/list/list.go:70
  79efcd:	48 83 79 28 00       	cmpq   $0x0,0x28(%rcx)
  79efd2:	75 04                	jne    79efd8 <agentGoProject/common.CompareFileMd5+0x98>
  79efd4:	31 c9                	xor    %ecx,%ecx
agentGoProject/common.CompareFileMd5():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:125
  79efd6:	eb 03                	jmp    79efdb <agentGoProject/common.CompareFileMd5+0x9b>
container/list.(*List).Front():
./C:/Program Files/Go/src/container/list/list.go:73
  79efd8:	48 8b 09             	mov    (%rcx),%rcx
agentGoProject/common.CompareFileMd5():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:123
  79efdb:	48 89 44 24 50       	mov    %rax,0x50(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:125
  79efe0:	eb 27                	jmp    79f009 <agentGoProject/common.CompareFileMd5+0xc9>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:120
  79efe2:	48 89 44 24 20       	mov    %rax,0x20(%rsp)
  79efe7:	c6 44 24 1f 00       	movb   $0x0,0x1f(%rsp)
  79efec:	e8 ef c4 01 00       	call   7bb4e0 <agentGoProject/common.CompareFileMd5.func1>
  79eff1:	48 8b 44 24 20       	mov    0x20(%rsp),%rax
  79eff6:	48 8b ac 24 80 01 00 	mov    0x180(%rsp),%rbp
  79effd:	00 
  79effe:	48 81 c4 88 01 00 00 	add    $0x188,%rsp
  79f005:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:125
  79f006:	48 89 d1             	mov    %rdx,%rcx
  79f009:	48 85 c9             	test   %rcx,%rcx
  79f00c:	0f 84 de 01 00 00    	je     79f1f0 <agentGoProject/common.CompareFileMd5+0x2b0>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:126
  79f012:	48 8b 71 20          	mov    0x20(%rcx),%rsi
  79f016:	48 8b 51 18          	mov    0x18(%rcx),%rdx
  79f01a:	4c 8d 05 df d7 0a 00 	lea    0xad7df(%rip),%r8        # 84c800 <type:*+0x8a800>
  79f021:	4c 39 c2             	cmp    %r8,%rdx
  79f024:	75 2f                	jne    79f055 <agentGoProject/common.CompareFileMd5+0x115>
  79f026:	48 8d bc 24 b8 00 00 	lea    0xb8(%rsp),%rdi
  79f02d:	00 
  79f02e:	66 0f 1f 84 00 00 00 	nopw   0x0(%rax,%rax,1)
  79f035:	00 00 
  79f037:	66 0f 1f 84 00 00 00 	nopw   0x0(%rax,%rax,1)
  79f03e:	00 00 
  79f040:	48 89 6c 24 f0       	mov    %rbp,-0x10(%rsp)
  79f045:	48 8d 6c 24 f0       	lea    -0x10(%rsp),%rbp
  79f04a:	e8 7d 8a cc ff       	call   467acc <runtime.duffcopy+0x32c>
  79f04f:	48 8b 6d 00          	mov    0x0(%rbp),%rbp
  79f053:	eb 1f                	jmp    79f074 <agentGoProject/common.CompareFileMd5+0x134>
  79f055:	48 8d bc 24 b8 00 00 	lea    0xb8(%rsp),%rdi
  79f05c:	00 
  79f05d:	48 8d 7f e0          	lea    -0x20(%rdi),%rdi
  79f061:	48 89 6c 24 f0       	mov    %rbp,-0x10(%rsp)
  79f066:	48 8d 6c 24 f0       	lea    -0x10(%rsp),%rbp
  79f06b:	e8 fb 86 cc ff       	call   46776b <runtime.duffzero+0x14b>
  79f070:	48 8b 6d 00          	mov    0x0(%rbp),%rbp
  79f074:	48 8d 7c 24 58       	lea    0x58(%rsp),%rdi
  79f079:	48 8d b4 24 b8 00 00 	lea    0xb8(%rsp),%rsi
  79f080:	00 
  79f081:	48 89 6c 24 f0       	mov    %rbp,-0x10(%rsp)
  79f086:	48 8d 6c 24 f0       	lea    -0x10(%rsp),%rbp
  79f08b:	e8 3c 8a cc ff       	call   467acc <runtime.duffcopy+0x32c>
  79f090:	48 8b 6d 00          	mov    0x0(%rbp),%rbp
  79f094:	4c 39 c2             	cmp    %r8,%rdx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:127
  79f097:	0f 85 2f 01 00 00    	jne    79f1cc <agentGoProject/common.CompareFileMd5+0x28c>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:125
  79f09d:	48 89 4c 24 30       	mov    %rcx,0x30(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:129
  79f0a2:	bb 01 00 00 00       	mov    $0x1,%ebx
  79f0a7:	e8 34 31 cd ff       	call   4721e0 <sync.(*WaitGroup).Add>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:130
  79f0ac:	48 8d 05 8d db 08 00 	lea    0x8db8d(%rip),%rax        # 82cc40 <type:*+0x6ac40>
  79f0b3:	e8 c8 eb c6 ff       	call   40dc80 <runtime.newobject>
  79f0b8:	48 89 44 24 48       	mov    %rax,0x48(%rsp)
  79f0bd:	48 8d 0d 1c 02 00 00 	lea    0x21c(%rip),%rcx        # 79f2e0 <agentGoProject/common.CompareFileMd5.func2>
  79f0c4:	48 89 08             	mov    %rcx,(%rax)
  79f0c7:	83 3d 82 64 43 00 00 	cmpl   $0x0,0x436482(%rip)        # bd5550 <runtime.writeBarrier>
  79f0ce:	75 14                	jne    79f0e4 <agentGoProject/common.CompareFileMd5+0x1a4>
  79f0d0:	48 8b 54 24 50       	mov    0x50(%rsp),%rdx
  79f0d5:	48 89 50 08          	mov    %rdx,0x8(%rax)
  79f0d9:	48 8b 5c 24 28       	mov    0x28(%rsp),%rbx
  79f0de:	48 89 58 10          	mov    %rbx,0x10(%rax)
  79f0e2:	eb 21                	jmp    79f105 <agentGoProject/common.CompareFileMd5+0x1c5>
  79f0e4:	48 8d 78 08          	lea    0x8(%rax),%rdi
  79f0e8:	48 8b 54 24 50       	mov    0x50(%rsp),%rdx
  79f0ed:	e8 0e 80 cc ff       	call   467100 <runtime.gcWriteBarrierDX>
  79f0f2:	48 8d 78 10          	lea    0x10(%rax),%rdi
  79f0f6:	48 8b 5c 24 28       	mov    0x28(%rsp),%rbx
  79f0fb:	0f 1f 44 00 00       	nopl   0x0(%rax,%rax,1)
  79f100:	e8 1b 80 cc ff       	call   467120 <runtime.gcWriteBarrierBX>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:180
  79f105:	48 8d bc 24 18 01 00 	lea    0x118(%rsp),%rdi
  79f10c:	00 
  79f10d:	48 8d 74 24 58       	lea    0x58(%rsp),%rsi
  79f112:	66 0f 1f 84 00 00 00 	nopw   0x0(%rax,%rax,1)
  79f119:	00 00 
  79f11b:	0f 1f 44 00 00       	nopl   0x0(%rax,%rax,1)
  79f120:	48 89 6c 24 f0       	mov    %rbp,-0x10(%rsp)
  79f125:	48 8d 6c 24 f0       	lea    -0x10(%rsp),%rbp
  79f12a:	e8 9d 89 cc ff       	call   467acc <runtime.duffcopy+0x32c>
  79f12f:	48 8b 6d 00          	mov    0x0(%rbp),%rbp
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:130
  79f133:	48 8d 05 a6 d6 08 00 	lea    0x8d6a6(%rip),%rax        # 82c7e0 <type:*+0x6a7e0>
  79f13a:	e8 41 eb c6 ff       	call   40dc80 <runtime.newobject>
  79f13f:	48 8d 0d 1a 01 00 00 	lea    0x11a(%rip),%rcx        # 79f260 <agentGoProject/common.CompareFileMd5.func3>
  79f146:	48 89 08             	mov    %rcx,(%rax)
  79f149:	83 3d 00 64 43 00 00 	cmpl   $0x0,0x436400(%rip)        # bd5550 <runtime.writeBarrier>
  79f150:	75 0b                	jne    79f15d <agentGoProject/common.CompareFileMd5+0x21d>
  79f152:	48 8b 54 24 48       	mov    0x48(%rsp),%rdx
  79f157:	48 89 50 08          	mov    %rdx,0x8(%rax)
  79f15b:	eb 0e                	jmp    79f16b <agentGoProject/common.CompareFileMd5+0x22b>
  79f15d:	48 8d 78 08          	lea    0x8(%rax),%rdi
  79f161:	48 8b 54 24 48       	mov    0x48(%rsp),%rdx
  79f166:	e8 95 7f cc ff       	call   467100 <runtime.gcWriteBarrierDX>
  79f16b:	48 8d 78 10          	lea    0x10(%rax),%rdi
  79f16f:	83 3d da 63 43 00 00 	cmpl   $0x0,0x4363da(%rip)        # bd5550 <runtime.writeBarrier>
  79f176:	75 1d                	jne    79f195 <agentGoProject/common.CompareFileMd5+0x255>
  79f178:	48 8d b4 24 18 01 00 	lea    0x118(%rsp),%rsi
  79f17f:	00 
  79f180:	48 89 6c 24 f0       	mov    %rbp,-0x10(%rsp)
  79f185:	48 8d 6c 24 f0       	lea    -0x10(%rsp),%rbp
  79f18a:	e8 3d 89 cc ff       	call   467acc <runtime.duffcopy+0x32c>
  79f18f:	48 8b 6d 00          	mov    0x0(%rbp),%rbp
  79f193:	eb 21                	jmp    79f1b6 <agentGoProject/common.CompareFileMd5+0x276>
  79f195:	48 89 44 24 40       	mov    %rax,0x40(%rsp)
  79f19a:	48 8d 05 5f d6 0a 00 	lea    0xad65f(%rip),%rax        # 84c800 <type:*+0x8a800>
  79f1a1:	48 89 fb             	mov    %rdi,%rbx
  79f1a4:	48 8d 8c 24 18 01 00 	lea    0x118(%rsp),%rcx
  79f1ab:	00 
  79f1ac:	e8 af 4f c7 ff       	call   414160 <runtime.typedmemmove>
  79f1b1:	48 8b 44 24 40       	mov    0x40(%rsp),%rax
  79f1b6:	e8 45 11 ca ff       	call   440300 <runtime.newproc>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:184
  79f1bb:	48 8b 44 24 50       	mov    0x50(%rsp),%rax
./C:/Program Files/Go/src/container/list/list.go:32
  79f1c0:	48 8b 4c 24 30       	mov    0x30(%rsp),%rcx
container/list.(*Element).Next():
./C:/Program Files/Go/src/container/list/list.go:32
  79f1c5:	4c 8d 05 34 d6 0a 00 	lea    0xad634(%rip),%r8        # 84c800 <type:*+0x8a800>
  79f1cc:	48 8b 11             	mov    (%rcx),%rdx
  79f1cf:	48 8b 49 10          	mov    0x10(%rcx),%rcx
  79f1d3:	48 85 c9             	test   %rcx,%rcx
  79f1d6:	74 11                	je     79f1e9 <agentGoProject/common.CompareFileMd5+0x2a9>
  79f1d8:	0f 1f 84 00 00 00 00 	nopl   0x0(%rax,%rax,1)
  79f1df:	00 
  79f1e0:	48 39 d1             	cmp    %rdx,%rcx
  79f1e3:	0f 85 1d fe ff ff    	jne    79f006 <agentGoProject/common.CompareFileMd5+0xc6>
  79f1e9:	31 d2                	xor    %edx,%edx
agentGoProject/common.CompareFileMd5():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:125
  79f1eb:	e9 16 fe ff ff       	jmp    79f006 <agentGoProject/common.CompareFileMd5+0xc6>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:184
  79f1f0:	e8 6b 31 cd ff       	call   472360 <sync.(*WaitGroup).Wait>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:185
  79f1f5:	48 8b 4c 24 28       	mov    0x28(%rsp),%rcx
  79f1fa:	48 89 4c 24 20       	mov    %rcx,0x20(%rsp)
  79f1ff:	c6 44 24 1f 00       	movb   $0x0,0x1f(%rsp)
  79f204:	e8 d7 c2 01 00       	call   7bb4e0 <agentGoProject/common.CompareFileMd5.func1>
  79f209:	48 8b 44 24 20       	mov    0x20(%rsp),%rax
  79f20e:	48 8b ac 24 80 01 00 	mov    0x180(%rsp),%rbp
  79f215:	00 
  79f216:	48 81 c4 88 01 00 00 	add    $0x188,%rsp
  79f21d:	c3                   	ret    
  79f21e:	66 90                	xchg   %ax,%ax
  79f220:	e8 fb 4a c9 ff       	call   433d20 <runtime.deferreturn>
  79f225:	48 8b 44 24 20       	mov    0x20(%rsp),%rax
  79f22a:	48 8b ac 24 80 01 00 	mov    0x180(%rsp),%rbp
  79f231:	00 
  79f232:	48 81 c4 88 01 00 00 	add    $0x188,%rsp
  79f239:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:106
  79f23a:	e8 e1 5d cc ff       	call   465020 <runtime.morestack_noctxt.abi0>
  79f23f:	90                   	nop
  79f240:	e9 fb fc ff ff       	jmp    79ef40 <agentGoProject/common.CompareFileMd5>
  79f245:	cc                   	int3   
  79f246:	cc                   	int3   
  79f247:	cc                   	int3   
  79f248:	cc                   	int3   
  79f249:	cc                   	int3   
  79f24a:	cc                   	int3   
  79f24b:	cc                   	int3   
  79f24c:	cc                   	int3   
  79f24d:	cc                   	int3   
  79f24e:	cc                   	int3   
  79f24f:	cc                   	int3   
  79f250:	cc                   	int3   
  79f251:	cc                   	int3   
  79f252:	cc                   	int3   
  79f253:	cc                   	int3   
  79f254:	cc                   	int3   
  79f255:	cc                   	int3   
  79f256:	cc                   	int3   
  79f257:	cc                   	int3   
  79f258:	cc                   	int3   
  79f259:	cc                   	int3   
  79f25a:	cc                   	int3   
  79f25b:	cc                   	int3   
  79f25c:	cc                   	int3   
  79f25d:	cc                   	int3   
  79f25e:	cc                   	int3   
  79f25f:	cc                   	int3   

000000000079f260 <agentGoProject/common.CompareFileMd5.func3>:
agentGoProject/common.CompareFileMd5.func3():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:130
  79f260:	49 3b 66 10          	cmp    0x10(%r14),%rsp
  79f264:	76 46                	jbe    79f2ac <agentGoProject/common.CompareFileMd5.func3+0x4c>
  79f266:	48 83 ec 68          	sub    $0x68,%rsp
  79f26a:	48 89 6c 24 60       	mov    %rbp,0x60(%rsp)
  79f26f:	48 8d 6c 24 60       	lea    0x60(%rsp),%rbp
  79f274:	4d 8b 66 20          	mov    0x20(%r14),%r12
  79f278:	4d 85 e4             	test   %r12,%r12
  79f27b:	75 36                	jne    79f2b3 <agentGoProject/common.CompareFileMd5.func3+0x53>
  79f27d:	48 8d 72 10          	lea    0x10(%rdx),%rsi
  79f281:	48 8b 52 08          	mov    0x8(%rdx),%rdx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:180
  79f285:	48 8b 02             	mov    (%rdx),%rax
  79f288:	48 89 e7             	mov    %rsp,%rdi
  79f28b:	48 89 6c 24 f0       	mov    %rbp,-0x10(%rsp)
  79f290:	48 8d 6c 24 f0       	lea    -0x10(%rsp),%rbp
  79f295:	e8 32 88 cc ff       	call   467acc <runtime.duffcopy+0x32c>
  79f29a:	48 8b 6d 00          	mov    0x0(%rbp),%rbp
  79f29e:	66 90                	xchg   %ax,%ax
  79f2a0:	ff d0                	call   *%rax
  79f2a2:	48 8b 6c 24 60       	mov    0x60(%rsp),%rbp
  79f2a7:	48 83 c4 68          	add    $0x68,%rsp
  79f2ab:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:130
  79f2ac:	e8 cf 5c cc ff       	call   464f80 <runtime.morestack.abi0>
  79f2b1:	eb ad                	jmp    79f260 <agentGoProject/common.CompareFileMd5.func3>
  79f2b3:	4c 8d 6c 24 70       	lea    0x70(%rsp),%r13
  79f2b8:	4d 39 2c 24          	cmp    %r13,(%r12)
  79f2bc:	75 bf                	jne    79f27d <agentGoProject/common.CompareFileMd5.func3+0x1d>
  79f2be:	49 89 24 24          	mov    %rsp,(%r12)
  79f2c2:	eb b9                	jmp    79f27d <agentGoProject/common.CompareFileMd5.func3+0x1d>
  79f2c4:	cc                   	int3   
  79f2c5:	cc                   	int3   
  79f2c6:	cc                   	int3   
  79f2c7:	cc                   	int3   
  79f2c8:	cc                   	int3   
  79f2c9:	cc                   	int3   
  79f2ca:	cc                   	int3   
  79f2cb:	cc                   	int3   
  79f2cc:	cc                   	int3   
  79f2cd:	cc                   	int3   
  79f2ce:	cc                   	int3   
  79f2cf:	cc                   	int3   
  79f2d0:	cc                   	int3   
  79f2d1:	cc                   	int3   
  79f2d2:	cc                   	int3   
  79f2d3:	cc                   	int3   
  79f2d4:	cc                   	int3   
  79f2d5:	cc                   	int3   
  79f2d6:	cc                   	int3   
  79f2d7:	cc                   	int3   
  79f2d8:	cc                   	int3   
  79f2d9:	cc                   	int3   
  79f2da:	cc                   	int3   
  79f2db:	cc                   	int3   
  79f2dc:	cc                   	int3   
  79f2dd:	cc                   	int3   
  79f2de:	cc                   	int3   
  79f2df:	cc                   	int3   

000000000079f2e0 <agentGoProject/common.CompareFileMd5.func2>:
agentGoProject/common.CompareFileMd5.func2():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:130
  79f2e0:	4c 8d a4 24 58 ff ff 	lea    -0xa8(%rsp),%r12
  79f2e7:	ff 
  79f2e8:	4d 3b 66 10          	cmp    0x10(%r14),%r12
  79f2ec:	0f 86 ea 04 00 00    	jbe    79f7dc <agentGoProject/common.CompareFileMd5.func2+0x4fc>
  79f2f2:	48 81 ec 28 01 00 00 	sub    $0x128,%rsp
  79f2f9:	48 89 ac 24 20 01 00 	mov    %rbp,0x120(%rsp)
  79f300:	00 
  79f301:	48 8d ac 24 20 01 00 	lea    0x120(%rsp),%rbp
  79f308:	00 
  79f309:	49 c7 c5 00 00 00 00 	mov    $0x0,%r13
  79f310:	4c 89 ac 24 18 01 00 	mov    %r13,0x118(%rsp)
  79f317:	00 
  79f318:	c6 44 24 2f 00       	movb   $0x0,0x2f(%rsp)
  79f31d:	48 8b 4a 10          	mov    0x10(%rdx),%rcx
  79f321:	48 89 4c 24 60       	mov    %rcx,0x60(%rsp)
  79f326:	48 8b 52 08          	mov    0x8(%rdx),%rdx
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:132
  79f32a:	44 0f 11 bc 24 98 00 	movups %xmm15,0x98(%rsp)
  79f331:	00 00 
  79f333:	48 8d 35 c6 04 00 00 	lea    0x4c6(%rip),%rsi        # 79f800 <agentGoProject/common.CompareFileMd5.func2.1>
  79f33a:	48 89 b4 24 98 00 00 	mov    %rsi,0x98(%rsp)
  79f341:	00 
  79f342:	48 89 94 24 a0 00 00 	mov    %rdx,0xa0(%rsp)
  79f349:	00 
  79f34a:	48 8d 94 24 98 00 00 	lea    0x98(%rsp),%rdx
  79f351:	00 
  79f352:	48 89 94 24 18 01 00 	mov    %rdx,0x118(%rsp)
  79f359:	00 
  79f35a:	c6 44 24 2f 01       	movb   $0x1,0x2f(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:133
  79f35f:	48 8b 94 24 30 01 00 	mov    0x130(%rsp),%rdx
  79f366:	00 
  79f367:	48 89 54 24 70       	mov    %rdx,0x70(%rsp)
  79f36c:	48 8b b4 24 38 01 00 	mov    0x138(%rsp),%rsi
  79f373:	00 
  79f374:	48 89 74 24 38       	mov    %rsi,0x38(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:135
  79f379:	48 8b 84 24 40 01 00 	mov    0x140(%rsp),%rax
  79f380:	00 
  79f381:	48 89 84 24 80 00 00 	mov    %rax,0x80(%rsp)
  79f388:	00 
  79f389:	48 8b 9c 24 48 01 00 	mov    0x148(%rsp),%rbx
  79f390:	00 
  79f391:	48 89 5c 24 48       	mov    %rbx,0x48(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:137
  79f396:	e8 85 08 00 00       	call   79fc20 <agentGoProject/common.getFileModTime>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:139
  79f39b:	44 0f 11 bc 24 d8 00 	movups %xmm15,0xd8(%rsp)
  79f3a2:	00 00 
  79f3a4:	44 0f 11 bc 24 e8 00 	movups %xmm15,0xe8(%rsp)
  79f3ab:	00 00 
  79f3ad:	44 0f 11 bc 24 f8 00 	movups %xmm15,0xf8(%rsp)
  79f3b4:	00 00 
  79f3b6:	44 0f 11 bc 24 08 01 	movups %xmm15,0x108(%rsp)
  79f3bd:	00 00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:140
  79f3bf:	48 8b 4c 24 70       	mov    0x70(%rsp),%rcx
  79f3c4:	48 89 8c 24 d8 00 00 	mov    %rcx,0xd8(%rsp)
  79f3cb:	00 
  79f3cc:	48 8b 54 24 38       	mov    0x38(%rsp),%rdx
  79f3d1:	48 89 94 24 e0 00 00 	mov    %rdx,0xe0(%rsp)
  79f3d8:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:141
  79f3d9:	bb 0a 00 00 00       	mov    $0xa,%ebx
  79f3de:	66 90                	xchg   %ax,%ax
  79f3e0:	e8 9b e6 cd ff       	call   47da80 <strconv.FormatInt>
  79f3e5:	48 89 84 24 f8 00 00 	mov    %rax,0xf8(%rsp)
  79f3ec:	00 
  79f3ed:	48 89 9c 24 00 01 00 	mov    %rbx,0x100(%rsp)
  79f3f4:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:142
  79f3f5:	48 8b 8c 24 60 01 00 	mov    0x160(%rsp),%rcx
  79f3fc:	00 
  79f3fd:	48 83 bc 24 68 01 00 	cmpq   $0x1,0x168(%rsp)
  79f404:	00 01 
  79f406:	75 09                	jne    79f411 <agentGoProject/common.CompareFileMd5.func2+0x131>
  79f408:	80 39 31             	cmpb   $0x31,(%rcx)
  79f40b:	0f 84 14 01 00 00    	je     79f525 <agentGoProject/common.CompareFileMd5.func2+0x245>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:144
  79f411:	48 8b 8c 24 50 01 00 	mov    0x150(%rsp),%rcx
  79f418:	00 
  79f419:	48 8b bc 24 58 01 00 	mov    0x158(%rsp),%rdi
  79f420:	00 
  79f421:	48 8b 84 24 80 00 00 	mov    0x80(%rsp),%rax
  79f428:	00 
  79f429:	48 8b 5c 24 48       	mov    0x48(%rsp),%rbx
  79f42e:	e8 4d 22 00 00       	call   7a1680 <agentGoProject/common.hasChanged>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:145
  79f433:	48 85 db             	test   %rbx,%rbx
  79f436:	75 2d                	jne    79f465 <agentGoProject/common.CompareFileMd5.func2+0x185>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:152
  79f438:	48 8d 0d 11 e3 16 00 	lea    0x16e311(%rip),%rcx        # 90d750 <runtime.gcbits.*+0x2f0>
  79f43f:	48 89 8c 24 e8 00 00 	mov    %rcx,0xe8(%rsp)
  79f446:	00 
  79f447:	48 c7 84 24 f0 00 00 	movq   $0x1,0xf0(%rsp)
  79f44e:	00 01 00 00 00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:153
  79f453:	44 0f 11 bc 24 08 01 	movups %xmm15,0x108(%rsp)
  79f45a:	00 00 
  79f45c:	0f 1f 40 00          	nopl   0x0(%rax)
  79f460:	e9 41 02 00 00       	jmp    79f6a6 <agentGoProject/common.CompareFileMd5.func2+0x3c6>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:145
  79f465:	48 89 5c 24 50       	mov    %rbx,0x50(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:149
  79f46a:	48 89 84 24 88 00 00 	mov    %rax,0x88(%rsp)
  79f471:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:147
  79f472:	44 0f 11 bc 24 b8 00 	movups %xmm15,0xb8(%rsp)
  79f479:	00 00 
  79f47b:	44 0f 11 bc 24 c8 00 	movups %xmm15,0xc8(%rsp)
  79f482:	00 00 
  79f484:	48 8d 0d 15 28 05 00 	lea    0x52815(%rip),%rcx        # 7f1ca0 <type:*+0x2fca0>
  79f48b:	48 89 8c 24 b8 00 00 	mov    %rcx,0xb8(%rsp)
  79f492:	00 
  79f493:	48 8d 15 e6 0a 17 00 	lea    0x170ae6(%rip),%rdx        # 90ff80 <runtime.buildVersion.str+0xce0>
  79f49a:	48 89 94 24 c0 00 00 	mov    %rdx,0xc0(%rsp)
  79f4a1:	00 
  79f4a2:	48 8b 84 24 80 00 00 	mov    0x80(%rsp),%rax
  79f4a9:	00 
  79f4aa:	48 8b 5c 24 48       	mov    0x48(%rsp),%rbx
  79f4af:	e8 4c c0 c6 ff       	call   40b500 <runtime.convTstring>
  79f4b4:	48 8d 0d e5 27 05 00 	lea    0x527e5(%rip),%rcx        # 7f1ca0 <type:*+0x2fca0>
  79f4bb:	48 89 8c 24 c8 00 00 	mov    %rcx,0xc8(%rsp)
  79f4c2:	00 
  79f4c3:	48 89 84 24 d0 00 00 	mov    %rax,0xd0(%rsp)
  79f4ca:	00 
  79f4cb:	48 8d 84 24 b8 00 00 	lea    0xb8(%rsp),%rax
  79f4d2:	00 
  79f4d3:	bb 02 00 00 00       	mov    $0x2,%ebx
  79f4d8:	48 89 d9             	mov    %rbx,%rcx
  79f4db:	0f 1f 44 00 00       	nopl   0x0(%rax,%rax,1)
  79f4e0:	e8 3b da de ff       	call   58cf20 <log.Println>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:148
  79f4e5:	48 8d 0d 7c e2 16 00 	lea    0x16e27c(%rip),%rcx        # 90d768 <runtime.gcbits.*+0x308>
  79f4ec:	48 89 8c 24 e8 00 00 	mov    %rcx,0xe8(%rsp)
  79f4f3:	00 
  79f4f4:	48 c7 84 24 f0 00 00 	movq   $0x1,0xf0(%rsp)
  79f4fb:	00 01 00 00 00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:149
  79f500:	48 8b 8c 24 88 00 00 	mov    0x88(%rsp),%rcx
  79f507:	00 
  79f508:	48 89 8c 24 08 01 00 	mov    %rcx,0x108(%rsp)
  79f50f:	00 
  79f510:	48 8b 4c 24 50       	mov    0x50(%rsp),%rcx
  79f515:	48 89 8c 24 10 01 00 	mov    %rcx,0x110(%rsp)
  79f51c:	00 
  79f51d:	0f 1f 00             	nopl   (%rax)
  79f520:	e9 81 01 00 00       	jmp    79f6a6 <agentGoProject/common.CompareFileMd5.func2+0x3c6>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:157
  79f525:	48 8b 15 24 90 3f 00 	mov    0x3f9024(%rip),%rdx        # b98550 <agentGoProject/common.License_state>
  79f52c:	48 83 3d 24 90 3f 00 	cmpq   $0x1,0x3f9024(%rip)        # b98558 <agentGoProject/common.License_state+0x8>
  79f533:	01 
  79f534:	75 05                	jne    79f53b <agentGoProject/common.CompareFileMd5.func2+0x25b>
  79f536:	80 3a 31             	cmpb   $0x31,(%rdx)
  79f539:	74 5e                	je     79f599 <agentGoProject/common.CompareFileMd5.func2+0x2b9>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:158
  79f53b:	44 0f 11 bc 24 a8 00 	movups %xmm15,0xa8(%rsp)
  79f542:	00 00 
  79f544:	48 8d 15 55 27 05 00 	lea    0x52755(%rip),%rdx        # 7f1ca0 <type:*+0x2fca0>
  79f54b:	48 89 94 24 a8 00 00 	mov    %rdx,0xa8(%rsp)
  79f552:	00 
  79f553:	48 8d 15 36 0a 17 00 	lea    0x170a36(%rip),%rdx        # 90ff90 <runtime.buildVersion.str+0xcf0>
  79f55a:	48 89 94 24 b0 00 00 	mov    %rdx,0xb0(%rsp)
  79f561:	00 
  79f562:	48 8d 84 24 a8 00 00 	lea    0xa8(%rsp),%rax
  79f569:	00 
  79f56a:	bb 01 00 00 00       	mov    $0x1,%ebx
  79f56f:	48 89 d9             	mov    %rbx,%rcx
  79f572:	e8 a9 d9 de ff       	call   58cf20 <log.Println>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:159
  79f577:	c6 44 24 2f 00       	movb   $0x0,0x2f(%rsp)
  79f57c:	48 8b 94 24 18 01 00 	mov    0x118(%rsp),%rdx
  79f583:	00 
  79f584:	48 8b 32             	mov    (%rdx),%rsi
  79f587:	ff d6                	call   *%rsi
  79f589:	48 8b ac 24 20 01 00 	mov    0x120(%rsp),%rbp
  79f590:	00 
  79f591:	48 81 c4 28 01 00 00 	add    $0x128,%rsp
  79f598:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:162
  79f599:	48 8b 84 24 80 00 00 	mov    0x80(%rsp),%rax
  79f5a0:	00 
  79f5a1:	48 8b 5c 24 48       	mov    0x48(%rsp),%rbx
  79f5a6:	e8 b5 07 00 00       	call   79fd60 <agentGoProject/common.checkFileDirSafe>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:163
  79f5ab:	48 85 db             	test   %rbx,%rbx
  79f5ae:	75 29                	jne    79f5d9 <agentGoProject/common.CompareFileMd5.func2+0x2f9>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:170
  79f5b0:	48 8d 0d 99 e1 16 00 	lea    0x16e199(%rip),%rcx        # 90d750 <runtime.gcbits.*+0x2f0>
  79f5b7:	48 89 8c 24 e8 00 00 	mov    %rcx,0xe8(%rsp)
  79f5be:	00 
  79f5bf:	48 c7 84 24 f0 00 00 	movq   $0x1,0xf0(%rsp)
  79f5c6:	00 01 00 00 00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:171
  79f5cb:	44 0f 11 bc 24 08 01 	movups %xmm15,0x108(%rsp)
  79f5d2:	00 00 
  79f5d4:	e9 cd 00 00 00       	jmp    79f6a6 <agentGoProject/common.CompareFileMd5.func2+0x3c6>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:163
  79f5d9:	48 89 5c 24 30       	mov    %rbx,0x30(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:167
  79f5de:	48 89 44 24 68       	mov    %rax,0x68(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:164
  79f5e3:	44 0f 11 bc 24 b8 00 	movups %xmm15,0xb8(%rsp)
  79f5ea:	00 00 
  79f5ec:	44 0f 11 bc 24 c8 00 	movups %xmm15,0xc8(%rsp)
  79f5f3:	00 00 
  79f5f5:	31 c0                	xor    %eax,%eax
  79f5f7:	48 8b 9c 24 80 00 00 	mov    0x80(%rsp),%rbx
  79f5fe:	00 
  79f5ff:	48 8b 4c 24 48       	mov    0x48(%rsp),%rcx
  79f604:	48 8d 3d 15 7c 0e 00 	lea    0xe7c15(%rip),%rdi        # 887220 <go:string.*+0x19c58>
  79f60b:	be 37 00 00 00       	mov    $0x37,%esi
  79f610:	e8 2b 0d cb ff       	call   450340 <runtime.concatstring2>
  79f615:	e8 e6 be c6 ff       	call   40b500 <runtime.convTstring>
  79f61a:	48 8d 15 7f 26 05 00 	lea    0x5267f(%rip),%rdx        # 7f1ca0 <type:*+0x2fca0>
  79f621:	48 89 94 24 b8 00 00 	mov    %rdx,0xb8(%rsp)
  79f628:	00 
  79f629:	48 89 84 24 c0 00 00 	mov    %rax,0xc0(%rsp)
  79f630:	00 
  79f631:	48 8b 44 24 68       	mov    0x68(%rsp),%rax
  79f636:	48 8b 5c 24 30       	mov    0x30(%rsp),%rbx
  79f63b:	0f 1f 44 00 00       	nopl   0x0(%rax,%rax,1)
  79f640:	e8 bb be c6 ff       	call   40b500 <runtime.convTstring>
  79f645:	48 8d 15 54 26 05 00 	lea    0x52654(%rip),%rdx        # 7f1ca0 <type:*+0x2fca0>
  79f64c:	48 89 94 24 c8 00 00 	mov    %rdx,0xc8(%rsp)
  79f653:	00 
  79f654:	48 89 84 24 d0 00 00 	mov    %rax,0xd0(%rsp)
  79f65b:	00 
  79f65c:	48 8d 84 24 b8 00 00 	lea    0xb8(%rsp),%rax
  79f663:	00 
  79f664:	bb 02 00 00 00       	mov    $0x2,%ebx
  79f669:	48 89 d9             	mov    %rbx,%rcx
  79f66c:	e8 af d8 de ff       	call   58cf20 <log.Println>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:166
  79f671:	48 8d 15 f0 e0 16 00 	lea    0x16e0f0(%rip),%rdx        # 90d768 <runtime.gcbits.*+0x308>
  79f678:	48 89 94 24 e8 00 00 	mov    %rdx,0xe8(%rsp)
  79f67f:	00 
  79f680:	48 c7 84 24 f0 00 00 	movq   $0x1,0xf0(%rsp)
  79f687:	00 01 00 00 00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:167
  79f68c:	48 8b 54 24 68       	mov    0x68(%rsp),%rdx
  79f691:	48 89 94 24 08 01 00 	mov    %rdx,0x108(%rsp)
  79f698:	00 
  79f699:	48 8b 54 24 30       	mov    0x30(%rsp),%rdx
  79f69e:	48 89 94 24 10 01 00 	mov    %rdx,0x110(%rsp)
  79f6a5:	00 
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:175
  79f6a6:	48 8d 05 b3 07 0a 00 	lea    0xa07b3(%rip),%rax        # 83fe60 <type:*+0x7de60>
  79f6ad:	48 8d 9c 24 d8 00 00 	lea    0xd8(%rsp),%rbx
  79f6b4:	00 
  79f6b5:	e8 c6 bb c6 ff       	call   40b280 <runtime.convT>
  79f6ba:	48 89 c3             	mov    %rax,%rbx
  79f6bd:	48 8d 05 9c 07 0a 00 	lea    0xa079c(%rip),%rax        # 83fe60 <type:*+0x7de60>
  79f6c4:	e8 97 7e d6 ff       	call   507560 <encoding/json.Marshal>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:176
  79f6c9:	48 85 ff             	test   %rdi,%rdi
  79f6cc:	74 6f                	je     79f73d <agentGoProject/common.CompareFileMd5.func2+0x45d>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:179
  79f6ce:	48 89 44 24 78       	mov    %rax,0x78(%rsp)
  79f6d3:	48 89 5c 24 40       	mov    %rbx,0x40(%rsp)
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:177
  79f6d8:	44 0f 11 bc 24 b8 00 	movups %xmm15,0xb8(%rsp)
  79f6df:	00 00 
  79f6e1:	44 0f 11 bc 24 c8 00 	movups %xmm15,0xc8(%rsp)
  79f6e8:	00 00 
  79f6ea:	48 8d 15 af 25 05 00 	lea    0x525af(%rip),%rdx        # 7f1ca0 <type:*+0x2fca0>
  79f6f1:	48 89 94 24 b8 00 00 	mov    %rdx,0xb8(%rsp)
  79f6f8:	00 
  79f6f9:	48 8d 15 30 10 17 00 	lea    0x171030(%rip),%rdx        # 910730 <gcmPoly+0x200>
  79f700:	48 89 94 24 c0 00 00 	mov    %rdx,0xc0(%rsp)
  79f707:	00 
  79f708:	74 04                	je     79f70e <agentGoProject/common.CompareFileMd5.func2+0x42e>
  79f70a:	48 8b 7f 08          	mov    0x8(%rdi),%rdi
  79f70e:	48 89 bc 24 c8 00 00 	mov    %rdi,0xc8(%rsp)
  79f715:	00 
  79f716:	48 89 b4 24 d0 00 00 	mov    %rsi,0xd0(%rsp)
  79f71d:	00 
  79f71e:	48 8d 84 24 b8 00 00 	lea    0xb8(%rsp),%rax
  79f725:	00 
  79f726:	bb 02 00 00 00       	mov    $0x2,%ebx
  79f72b:	48 89 d9             	mov    %rbx,%rcx
  79f72e:	e8 ed d7 de ff       	call   58cf20 <log.Println>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:179
  79f733:	48 8b 44 24 78       	mov    0x78(%rsp),%rax
  79f738:	48 8b 5c 24 40       	mov    0x40(%rsp),%rbx
  79f73d:	48 89 d9             	mov    %rbx,%rcx
  79f740:	48 89 c3             	mov    %rax,%rbx
  79f743:	31 c0                	xor    %eax,%eax
  79f745:	e8 96 0f cb ff       	call   4506e0 <runtime.slicebytetostring>
  79f74a:	48 89 84 24 90 00 00 	mov    %rax,0x90(%rsp)
  79f751:	00 
  79f752:	48 89 5c 24 58       	mov    %rbx,0x58(%rsp)
  79f757:	48 8b 4c 24 70       	mov    0x70(%rsp),%rcx
  79f75c:	48 8b 7c 24 38       	mov    0x38(%rsp),%rdi
  79f761:	48 8d 05 98 b6 06 00 	lea    0x6b698(%rip),%rax        # 80ae00 <type:*+0x48e00>
  79f768:	48 8b 5c 24 60       	mov    0x60(%rsp),%rbx
  79f76d:	e8 8e 3d c7 ff       	call   413500 <runtime.mapassign_faststr>
  79f772:	48 8b 54 24 58       	mov    0x58(%rsp),%rdx
  79f777:	48 89 50 08          	mov    %rdx,0x8(%rax)
  79f77b:	83 3d ce 5d 43 00 00 	cmpl   $0x0,0x435dce(%rip)        # bd5550 <runtime.writeBarrier>
  79f782:	75 0d                	jne    79f791 <agentGoProject/common.CompareFileMd5.func2+0x4b1>
  79f784:	48 8b 8c 24 90 00 00 	mov    0x90(%rsp),%rcx
  79f78b:	00 
  79f78c:	48 89 08             	mov    %rcx,(%rax)
  79f78f:	eb 14                	jmp    79f7a5 <agentGoProject/common.CompareFileMd5.func2+0x4c5>
  79f791:	48 89 c7             	mov    %rax,%rdi
  79f794:	48 8b 84 24 90 00 00 	mov    0x90(%rsp),%rax
  79f79b:	00 
  79f79c:	0f 1f 40 00          	nopl   0x0(%rax)
  79f7a0:	e8 3b 78 cc ff       	call   466fe0 <runtime.gcWriteBarrier>
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:180
  79f7a5:	c6 44 24 2f 00       	movb   $0x0,0x2f(%rsp)
  79f7aa:	48 8b 94 24 18 01 00 	mov    0x118(%rsp),%rdx
  79f7b1:	00 
  79f7b2:	48 8b 02             	mov    (%rdx),%rax
  79f7b5:	ff d0                	call   *%rax
  79f7b7:	48 8b ac 24 20 01 00 	mov    0x120(%rsp),%rbp
  79f7be:	00 
  79f7bf:	48 81 c4 28 01 00 00 	add    $0x128,%rsp
  79f7c6:	c3                   	ret    
  79f7c7:	e8 54 45 c9 ff       	call   433d20 <runtime.deferreturn>
  79f7cc:	48 8b ac 24 20 01 00 	mov    0x120(%rsp),%rbp
  79f7d3:	00 
  79f7d4:	48 81 c4 28 01 00 00 	add    $0x128,%rsp
  79f7db:	c3                   	ret    
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:130
  79f7dc:	0f 1f 40 00          	nopl   0x0(%rax)
  79f7e0:	e8 9b 57 cc ff       	call   464f80 <runtime.morestack.abi0>
  79f7e5:	e9 f6 fa ff ff       	jmp    79f2e0 <agentGoProject/common.CompareFileMd5.func2>
  79f7ea:	cc                   	int3   
  79f7eb:	cc                   	int3   
  79f7ec:	cc                   	int3   
  79f7ed:	cc                   	int3   
  79f7ee:	cc                   	int3   
  79f7ef:	cc                   	int3   
  79f7f0:	cc                   	int3   
  79f7f1:	cc                   	int3   
  79f7f2:	cc                   	int3   
  79f7f3:	cc                   	int3   
  79f7f4:	cc                   	int3   
  79f7f5:	cc                   	int3   
  79f7f6:	cc                   	int3   
  79f7f7:	cc                   	int3   
  79f7f8:	cc                   	int3   
  79f7f9:	cc                   	int3   
  79f7fa:	cc                   	int3   
  79f7fb:	cc                   	int3   
  79f7fc:	cc                   	int3   
  79f7fd:	cc                   	int3   
  79f7fe:	cc                   	int3   
  79f7ff:	cc                   	int3   

000000000079f800 <agentGoProject/common.CompareFileMd5.func2.1>:
agentGoProject/common.CompareFileMd5.func2.1():
./D:/github_wgcloudPro/pro/agent/agentGoProject/common/fileSafeUtil.go:132
  79f800:	49 3b 66 10          	cmp    0x10(%r14),%rsp
  79f804:	76 2a                	jbe    79f830 <agentGoProject/common.CompareFileMd5.func2.1+0x30>
  79f806:	48 83 ec 10          	sub    $0x10,%rsp
  79f80a:	48 89 6c 24 08       	mov    %rbp,0x8(%rsp)
  79f80f:	48 8d 6c 24 08       	lea    0x8(%rsp),%rbp
  79f814:	4d 8b 66 20          	mov    0x20(%r14),%r12
  79f818:	4d 85 e4             	test   %r12,%r12
  79f81b:	75 1a                	jne    79f837 <agentGoProject/common.CompareFileMd5.func2.1+0x37>
  79f81d:	48 8b 42 08          	mov    0x8(%rdx),%rax
  79f821:	e8 da 2a cd ff       	call   472300 <sync.(*WaitGroup).Done>
  79f826:	48 8b 6c 24 08       	mov    0x8(%rsp),%rbp
  79f82b:	48 83 c4 10          	add    $0x10,%rsp
  79f82f:	c3                   	ret    
  79f830:	e8 4b 57 cc ff       	call   464f80 <runtime.morestack.abi0>
  79f835:	eb c9                	jmp    79f800 <agentGoProject/common.CompareFileMd5.func2.1>
  79f837:	4c 8d 6c 24 18       	lea    0x18(%rsp),%r13
  79f83c:	0f 1f 40 00          	nopl   0x0(%rax)
  79f840:	4d 39 2c 24          	cmp    %r13,(%r12)
  79f844:	75 d7                	jne    79f81d <agentGoProject/common.CompareFileMd5.func2.1+0x1d>
  79f846:	49 89 24 24          	mov    %rsp,(%r12)
  79f84a:	eb d1                	jmp    79f81d <agentGoProject/common.CompareFileMd5.func2.1+0x1d>
  79f84c:	cc                   	int3   
  79f84d:	cc                   	int3   
  79f84e:	cc                   	int3   
  79f84f:	cc                   	int3   
  79f850:	cc                   	int3   
  79f851:	cc                   	int3   
  79f852:	cc                   	int3   
  79f853:	cc                   	int3   
  79f854:	cc                   	int3   
  79f855:	cc                   	int3   
  79f856:	cc                   	int3   
  79f857:	cc                   	int3   
  79f858:	cc                   	int3   
  79f859:	cc                   	int3   
  79f85a:	cc                   	int3   
  79f85b:	cc                   	int3   
  79f85c:	cc                   	int3   
  79f85d:	cc                   	int3   
  79f85e:	cc                   	int3   
  79f85f:	cc                   	int3   
