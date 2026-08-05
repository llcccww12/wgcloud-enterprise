function searchByOrder(orderBy,orderType){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dockerInfo/list?orderBy="+orderBy+"&orderType="+orderType+"&hostname="+$("#hostname").val();
}

function searchByOnline(state){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dockerInfo/list?state="+state;
}

function searchByAccount() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dockerInfo/list?account="+$("#account").val();
}

function add() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dockerInfo/edit";
}

function addBatch() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dockerInfo/editBatch";
}


function view(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dockerInfo/view?id="+id;
}

function edit(id){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dockerInfo/edit?id="+id;
}

function del(id) {
	if(confirm('您确定要删除此记录吗？')) {
		window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dockerInfo/del?id=" + id;
	}
}
function viewDate(id,searchTime){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dockerInfo/view?id="+id+"&am="+searchTime;
}

function excelExport(id,searchTime){
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	window.open(SERVER_SERVLET_CONTEXT_PATH + "/dockerInfo/chartExcel?id="+id+"&startTime="+startTime+"&endTime="+endTime+"&am="+searchTime);
}

function showSetGroupId() {
	var chk_value =[];
	$("input[name='todo2']:checkbox").each(function() {
		if($(this).is(':checked')) {
			chk_value.push($(this).val());
		}
	});
	if(chk_value.length == 0){
		alert("请先选择需要设置分组的docker");
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
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dockerInfo/list?groupId="+$("#groupId").val();
}

//数据表单动态添加一行下标，每次添加+1
var dataFromIndex = 0 ;
var appType = "1";

function addDataForm() {
	var dataHideContentHtml = $("#dataHideContent").html();
	dataHideContentHtml = dataHideContentHtml.replace(/{num}/g, dataFromIndex);
	if(appType=='2'){
		dataHideContentHtml = dataHideContentHtml.replace(/CONTAINER ID/g, "CONTAINER NAME");
		dataHideContentHtml = dataHideContentHtml.replace(/完整ID/g, "NAME");
	}
	$("#dataFormList").append(dataHideContentHtml);
	$("#dataFromIndex").val(dataFromIndex);
	dataFromIndex += 1;
}

function runStop(id) {
	if(confirm('你确定要停止该容器吗？')) {
		$.ajax({
			url: SERVER_SERVLET_CONTEXT_PATH + "/dockerInfo/run?id=" + id+"&containerRunAction=1",
			//data: {},
			type: "GET",
			//dataType: "json",
			success: function(data) {
				toastr.success("【停止】指令已下发成功，系统将在3分钟内执行完成");
				$("#"+id+"_state").attr("class","badge bg-warning");
				$("#"+id+"_state").html("正在停止");
			}
		});
	}
}

function runStart(id) {
	if(confirm('你确定要启动该容器吗？')) {
		$.ajax({
			url: SERVER_SERVLET_CONTEXT_PATH + "/dockerInfo/run?id=" + id+"&containerRunAction=2",
			//data: {},
			type: "GET",
			//dataType: "json",
			success: function(data) {
				toastr.success("【启动】指令已下发成功，系统将在3分钟内执行完成");
				$("#"+id+"_state").attr("class","badge bg-success");
				$("#"+id+"_state").html("正在启动");
			}
		});
	}
}

function runRestart(id) {
	if(confirm('你确定要重启该容器吗？')) {
		$.ajax({
			url: SERVER_SERVLET_CONTEXT_PATH + "/dockerInfo/run?id=" + id+"&containerRunAction=3",
			//data: {},
			type: "GET",
			//dataType: "json",
			success: function(data) {
				toastr.success("【重启】指令已下发成功，系统将在3分钟内执行完成");
				$("#"+id+"_state").attr("class","badge bg-success");
				$("#"+id+"_state").html("正在重启");
			}
		});
	}
}

function viewAllDocker(){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dockerInfo/hostList";
}