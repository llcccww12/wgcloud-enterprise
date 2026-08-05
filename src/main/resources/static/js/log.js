function searchByPara(state){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/log/list?state="+state;
}

function view(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/log/view?id="+id;
}

function resetParam(){
	$("#startTime").val("");
	$("#state").val("");
	$("#hostname").val("");
}

function exportExcel(){
	var currentParams = window.location.search;
	window.open(SERVER_SERVLET_CONTEXT_PATH + "/log/exportListExcel"+currentParams);
}