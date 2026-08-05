
function add() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/hostGroup/edit";
}

function searchByAccount() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/hostGroup/list?account="+$("#account").val();
}

function edit(id){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/hostGroup/edit?id="+id;
}

function del(id) {
	if(confirm('您确定要删除此记录吗？删除此标签后，此标签下的资源将恢复到无标签状态，其他无影响')) {
		window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/hostGroup/del?id=" + id;
	}
}