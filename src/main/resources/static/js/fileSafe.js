function searchByOrder(orderBy,orderType){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/fileSafe/list?orderBy="+orderBy+"&orderType="+orderType+"&hostname="+$("#hostname").val();
}

function searchByOnline(state){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/fileSafe/list?state="+state;
}

function searchByAccount() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/fileSafe/list?account="+$("#account").val();
}

function add() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/fileSafe/edit";
}

function addBatch() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/fileSafe/editBatch";
}


function edit(id){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/fileSafe/edit?id="+id;
}

function view(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/fileSafe/view?id="+id;
}

function del(id) {
	if(confirm('您确定要删除此记录吗？')) {
		window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/fileSafe/del?id=" + id;
	}
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

function showSetGroupId() {
	var chk_value =[];
	$("input[name='todo2']:checkbox").each(function() {
		if($(this).is(':checked')) {
			chk_value.push($(this).val());
		}
	});
	if(chk_value.length == 0){
		alert("请先选择需要设置标签的数据");
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
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/fileSafe/list?groupId="+$("#groupId").val();
}

function refreshFileSafeState(id) {
	$.ajax({
		url: SERVER_SERVLET_CONTEXT_PATH + "/fileSafe/refreshFileSafeState?id="+id,
		type: "POST",
		success: function(data) {
			toastr.success("【成功】系统将重新监控该文件夹下的所有文件");
			$("#"+id+"_fileName").attr("class","badge bg-primary");
		}
	});
}
