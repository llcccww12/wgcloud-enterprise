function searchByPara(state){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/redisMonitor/list?state="+state;
}

function view(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/redisMonitor/view?id="+id;
}