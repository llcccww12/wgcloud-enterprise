function searchByAccount() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/shellNoteInfo/list?account="+$("#account").val();
}

function add() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/shellNoteInfo/edit";
}


function view(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/shellNoteInfo/view?id="+id;
}

function edit(id){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/shellNoteInfo/edit?id="+id;
}

function del(id) {
	if(confirm('您确定要删除此记录吗？')) {
		window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/shellNoteInfo/del?id=" + id;
	}
}