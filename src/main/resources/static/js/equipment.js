function add() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/equipment/edit";
}

function searchByAccount() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/equipment/list?account="+$("#account").val();
}

function view(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/equipment/view?id="+id;
}

function edit(id){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/equipment/edit?id="+id;
}

function del(id) {
	if(confirm('您确定要删除此记录吗？')) {
		window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/equipment/del?id=" + id;
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
		alert("请先选择需要设置标签的进程");
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
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/equipment/list?groupId="+$("#groupId").val();
}

function excelExportList(){
	var urlParams = window.location.search;
	window.open(SERVER_SERVLET_CONTEXT_PATH + "/equipment/exportListExcel" + urlParams);
}

function copyEquipment(){
	var chk_value =[];
	$("input[name='todo2']:checkbox").each(function() {
		if($(this).is(':checked')) {
			chk_value.push($(this).val());
		}
	});
	if(chk_value.length == 0){
		alert("请先选择需要复制的数据");
		return;
	}
	if(chk_value.length > 1){
		alert("只能选择一条数据");
		return;
	}
	if(confirm('您确定要复制所选数据吗？')) {
		var vals = chk_value.join(",");
		window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/equipment/copyEquipment?id="+vals;
	}
}
