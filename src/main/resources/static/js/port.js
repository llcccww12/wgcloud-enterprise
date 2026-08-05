function searchByOrder(orderBy,orderType){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/portInfo/list?orderBy="+orderBy+"&orderType="+orderType+"&hostname="+$("#hostname").val();
}

function searchByOnline(state){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/portInfo/list?state="+state;
}

function searchByAccount() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/portInfo/list?account="+$("#account").val();
}

function add() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/portInfo/edit";
}

function view(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/portInfo/view?id="+id;
}

function addBatch() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/portInfo/editBatch";
}


function edit(id){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/portInfo/edit?id="+id;
}

function del(id) {
	if(confirm('您确定要删除此记录吗？')) {
		window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/portInfo/del?id=" + id;
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
		alert("请先选择需要设置分组的端口");
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
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/portInfo/list?groupId="+$("#groupId").val();
}


//数据表单动态添加一行下标，每次添加+1
var dataFromIndex = 0 ;

function addDataForm() {
	var dataHideContentHtml = $("#dataHideContent").html();
	dataHideContentHtml = dataHideContentHtml.replace(/{num}/g, dataFromIndex);
	$("#dataFormList").append(dataHideContentHtml);
	$("#dataFromIndex").val(dataFromIndex);
	dataFromIndex += 1;
}