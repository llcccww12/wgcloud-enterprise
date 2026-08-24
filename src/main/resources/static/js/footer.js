// 禁用官方远程 license 校验脚本（会向 wgstart.com 上报授权信息并弹出盗版提示）
function licenseHandle() {}
function checkVersion() {}
function showFreeContent() {}
$("#checkall").on("click",function(){
	if($('#checkall').is(':checked')) {
		$("input[name='todo2']:checkbox").each(function() {
			$(this).prop("checked", true);
		});
	}else{
		$("input[name='todo2']:checkbox").each(function() {
			$(this).prop("checked", false);
		});
	}
});
//批量删除
function delChecks(url) {
	var chk_value =[];
	$("input[name='todo2']:checkbox").each(function() {
		if($(this).is(':checked')) {
			chk_value.push($(this).val());
		}
	});
	if(chk_value.length == 0){
		alert("请先选择需要删除的数据");
		return;
	}
	if(confirm('您确定要删除所选数据吗？')) {
		var vals = chk_value.join(",");
		$.ajax(SERVER_SERVLET_CONTEXT_PATH + url,{
			type:"post",
			data:{"id":vals},
			success:function(data){
				window.location.href = window.location.href;
			}
		});
	}
}
//移除div
function removeDiv(divId) {
	$("#"+divId).remove();
}

//跳转到系统日志查询
function goHrefLogInfo(param) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/log/list?hostname="+param;
}

//跳转url
function hrefTopUrl(url){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + url;
}

//批量开始监控
function startMonitor(url) {
	var chk_value =[];
	$("input[name='todo2']:checkbox").each(function() {
		if($(this).is(':checked')) {
			chk_value.push($(this).val());
		}
	});
	if(chk_value.length == 0){
		alert("请先选择需要开始监控的数据");
		return;
	}
	if(confirm('您确定要开始监控吗？')) {
		var vals = chk_value.join(",");
		$.ajax(SERVER_SERVLET_CONTEXT_PATH + url,{
			type:"post",
			data:{"id":vals,"active":"1"},
			success:function(data){
				window.location.href = window.location.href;
			}
		});
	}
}

//批量停止监控
function stopMonitor(url) {
	var chk_value =[];
	$("input[name='todo2']:checkbox").each(function() {
		if($(this).is(':checked')) {
			chk_value.push($(this).val());
		}
	});
	if(chk_value.length == 0){
		alert("请先选择需要停止监控的数据");
		return;
	}
	if(confirm('您确定要停止监控吗？')) {
		var vals = chk_value.join(",");
		$.ajax(SERVER_SERVLET_CONTEXT_PATH + url,{
			type:"post",
			data:{"id":vals,"active":"2"},
			success:function(data){
				window.location.href = window.location.href;
			}
		});
	}
}

//返回
function goback() {
	history.back();
}

//切换菜单展开闭合状态
function swapMenuState(){
	$.ajax(SERVER_SERVLET_CONTEXT_PATH + "/login/ajaxSwapMenuState",{
		type:"post",
		success:function(data){
		}
	});
}

//打开新窗口
function openNewPage(url) {
	window.open(SERVER_SERVLET_CONTEXT_PATH + url);
}