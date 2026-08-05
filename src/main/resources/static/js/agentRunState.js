function searchByOrder(orderBy,orderType){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/agentRunState/list?orderBy="+orderBy+"&orderType="+orderType+"&hostname="+$("#hostname").val();
}

function searchByOnline(state){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/agentRunState/list?state="+state;
}

function searchByAccount() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/agentRunState/list?account="+$("#account").val();
}

function del(id) {
	if(confirm('您确定要删除此记录吗？')) {
		window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/agentRunState/del?id=" + id;
	}
}

function view(hostname) {
	if(hostname!="" && hostname.indexOf("(")>0){
		hostname = hostname.substring(0,hostname.indexOf("("));
	}
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/systemInfoList?hostname="+hostname;
}

function searchByGroupId() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/agentRunState/list?groupId="+$("#groupId").val();
}

function exportExcel(){
	window.open(SERVER_SERVLET_CONTEXT_PATH + "/agentRunState/exportListExcel");
}

function ajaxSaveBindIp() {
	var idRemark = $("#id").val();
	var oldValue = $("#"+idRemark+"_hostname").html();
	$("#"+idRemark+"_hostname").html($("#bindIp").val());
	$("#modal-default").modal("toggle");
	$("#form").ajaxSubmit(function(message) {
		toastr.success("开始下发指令，新主机IP【"+$("#bindIp").val()+"】将在几分钟内修改完成，同时旧主机IP【"+oldValue+"】会显示下线，有时间将其删除即可");
	});
}


function setHostBindIp(hostId,bindIp) {
	if(confirm('您确定要修改此主机agent的配置参数【bindIp】吗？修改后，主机管理列表中的旧主机IP会显示下线，新设置的主机bindIp会出现在列表中')) {
		$("#id").val(hostId);
		$("#bindIp").val(bindIp);
	}
}

function modifyAgentConfig() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/agentRunState/toModifyAgentPage";
}