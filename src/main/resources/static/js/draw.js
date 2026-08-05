function searchByOrder(orderBy,orderType){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dash/hostDrawList?orderBy="+orderBy+"&orderType="+orderType;
}

function searchByOnlineDashView(state){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dash/hostDrawList?state="+state;
}


function viewChart(id) {
	window.location.href= SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/chart?id="+id;
}

function view(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dash/hostDraw?id="+id;
}

function viewDocker(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dockerInfo/view?id="+id;
}

function viewAppInfo(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/appInfo/view?id="+id;
}

function viewCustomInfo(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/customInfo/view?id="+id;
}

function viewFileSafeInfo(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/fileSafe/view?id="+id;
}

function viewTaskJobInfo(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/taskJobInfo/view?id="+id;
}

function viewApps(hostname){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/appInfo/list?hostname="+hostname;
}

function viewDockers(hostname){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dockerInfo/list?hostname="+hostname;
}

function viewPorts(hostname){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/portInfo/list?hostname="+hostname;
}

function viewFileWarn(hostname){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/fileWarnInfo/list?hostname="+hostname;
}

function stateList(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/fileWarnInfo/stateList?fileWarnId="+id;
}

function viewFileWarnInfo(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/fileWarnInfo/view?id="+id;
}
