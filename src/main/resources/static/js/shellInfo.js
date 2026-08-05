function searchByOnline(state){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/shellInfo/list?state="+state;
}

function searchByAccount() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/shellInfo/list?account="+$("#account").val();
}

function add() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/shellInfo/edit";
}


function view(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/shellInfo/view?id="+id;
}

function edit(id){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/shellInfo/edit?id="+id;
}

function del(id) {
	if(confirm('您确定要删除此记录吗？删除同时会取消该指令下发')) {
		window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/shellInfo/del?id=" + id;
	}
}

function restartShell(id) {
	if(confirm('您确定要重新下发指令吗？')) {
		$.ajax({
			url: SERVER_SERVLET_CONTEXT_PATH + "/shellInfo/restart?id=" + id,
			//data: {},
			type: "GET",
			//dataType: "json",
			success: function(data) {
				toastr.success("【成功】已重新下发指令");
				$("#"+id).html('<span class="badge bg-primary">已重新下发</span>');
			}
		});
	}
}

function cancelShell(id) {
	if(confirm('你确定要取消指令下发吗？')) {
		$.ajax({
			url: SERVER_SERVLET_CONTEXT_PATH + "/shellInfo/cancel?id=" + id,
			//data: {},
			type: "GET",
			//dataType: "json",
			success: function(data) {
				toastr.success("【成功】已取消尚未下发的指令，已下发成功的指令将会继续执行");
				$("#"+id).attr("class","badge bg-warning");
				$("#"+id).html("已取消");
			}
		});
	}
}

function stateView(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/shellInfo/stateView?id="+id;
}
