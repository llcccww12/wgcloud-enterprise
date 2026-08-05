function searchByOrder(orderBy,orderType){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/fileWarnInfo/list?orderBy="+orderBy+"&orderType="+orderType+"&hostname="+$("#hostname").val();
}

function searchByAccount() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/fileWarnInfo/list?account="+$("#account").val();
}

function add() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/fileWarnInfo/edit";
}

function addBatch() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/fileWarnInfo/editBatch";
}


function stateList(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/fileWarnInfo/stateList?fileWarnId="+id;
}

function excelExport(id){
	window.open(SERVER_SERVLET_CONTEXT_PATH + "/fileWarnInfo/chartExcel?id="+id);
}

function view(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/fileWarnInfo/view?id="+id;
}

function stateView(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/fileWarnInfo/stateView?id="+id;
}

function edit(id){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/fileWarnInfo/edit?id="+id;
}

function del(id) {
	if(confirm('您确定要删除此记录吗？')) {
		window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/fileWarnInfo/del?id=" + id;
	}
}
