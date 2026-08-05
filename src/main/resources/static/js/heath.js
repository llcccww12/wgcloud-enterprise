function searchByOrder(orderBy,orderType){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/heathMonitor/list?orderBy="+orderBy+"&orderType="+orderType;
}

function searchByOnline(){
	var state = $("#heathStatus").val();
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/heathMonitor/list?heathStatus="+state;
}

function searchByAccount() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/heathMonitor/list?account="+$("#account").val();
}

function add() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/heathMonitor/edit";
}

function view(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/heathMonitor/view?id="+id;
}


function targetUrl(url) {
	window.open(url);
}

function viewDate(id,searchTime){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/heathMonitor/view?id="+id+"&am="+searchTime;
}

function excelExport(id,searchTime){
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	window.open(SERVER_SERVLET_CONTEXT_PATH + "/heathMonitor/chartExcel?id="+id+"&startTime="+startTime+"&endTime="+endTime+"&am="+searchTime);
}

function del(id) {
	if(confirm('您确定要删除此记录吗？')) {
		window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/heathMonitor/del?id=" + id;
	}
}
function edit(id){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/heathMonitor/edit?id="+id;
}

function showPostStr() {
	$("#PostStrDiv").show();
}

function hidePostStr() {
	$("#PostStrDiv").hide();
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
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/heathMonitor/list?groupId="+$("#groupId").val();
}

function testHeath(id,name) {
	toastr.info("正在测试【"+name+"】......");
	$.ajax({
		url: SERVER_SERVLET_CONTEXT_PATH + "/heathMonitor/testHeath?id=" + id,
		//data: {},
		type: "GET",
		//dataType: "json",
		success: function(data) {
			if("success"==data){
				toastr.success("测试连接成功，请刷新页面查看数据");
			}else if("noPro"==data){
				toastr.info("你好，使用此功能需要升级到专业版");
			}else{
				toastr.error(data);
			}
		}
	});
}