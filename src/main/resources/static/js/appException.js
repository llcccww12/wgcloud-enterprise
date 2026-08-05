function searchByOrder(orderBy,orderType){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/appExceptionInfo/list?orderBy="+orderBy+"&orderType="+orderType+"&hostname="+$("#hostname").val();
}

function searchByOnline(state){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/appExceptionInfo/list?state="+state;
}

function searchByAccount() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/appExceptionInfo/list?account="+$("#account").val();
}

function del(id) {
	if(confirm('您确定要删除此记录吗？此操作只会删除此处的数据记录，不会删除主机上的进程')) {
		window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/appExceptionInfo/del?id=" + id;
	}
}

function ajaxCancelProcessId(id) {
	if(confirm('你确定要结束该进程吗？')) {
		$.ajax({
			url: SERVER_SERVLET_CONTEXT_PATH + "/appExceptionInfo/cancelProcessAjax?id="+id,
			type: "GET",
			success: function(data) {
				$("#"+id+"_state").attr("class","badge bg-warning");
				$("#"+id+"_state").html("正在结束");
				toastr.success("系统将结束该进程，请稍后");
			}
		});
	}
}

function view(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/appExceptionInfo/view?id="+id;
}
