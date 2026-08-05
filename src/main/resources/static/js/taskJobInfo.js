function searchByOrder(orderBy,orderType){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/taskJobInfo/list?orderBy="+orderBy+"&orderType="+orderType+"&hostname="+$("#hostname").val();
}

function searchByOnline(state){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/taskJobInfo/list?state="+state;
}

function searchByAccount() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/taskJobInfo/list?account="+$("#account").val();
}

function add() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/taskJobInfo/edit";
}

function addBatch() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/taskJobInfo/editBatch";
}


function edit(id){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/taskJobInfo/edit?id="+id;
}

function view(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/taskJobInfo/view?id="+id;
}

function del(id) {
	if(confirm('您确定要删除此记录吗？')) {
		window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/taskJobInfo/del?id=" + id;
	}
}

function showSetGroupId() {
	var chk_value =[];
	$("input[name='todo2']:checkbox").each(function() {
		if($(this).is(':checked')) {
			chk_value.push($(this).val());
		}
	});
	if(chk_value.length == 0){
		alert("请先选择需要设置分组的数据");
		return;
	}
	$("#id3").val(chk_value.join(","));
	$("#modal-default3").modal("toggle");
}

function ajaxSaveGroupId() {
	$("#form4").ajaxSubmit(function(message) {
		window.location.href = window.location.href;
	});
}

function searchByGroupId() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/taskJobInfo/list?groupId="+$("#groupId").val();
}