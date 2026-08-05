function searchByOnline(state){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dbInfo/list?dbState="+state;
}

function searchByOrder(orderBy,orderType){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dbInfo/list?orderBy="+orderBy+"&orderType="+orderType;
}

function viewTables(dbInfoId){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dbTable/list?dbInfoId="+dbInfoId;
}

function view(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dbInfo/view?id="+id;
}

function edit(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dbInfo/edit?id="+id;
}

function searchByAccount() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dbInfo/list?account="+$("#account").val();
}

function add() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dbInfo/edit";
}

function del(id) {
	if(confirm('您确定要删除此记录吗？此操作，同时也将会删除该监测数据库下监测的数据表记录')) {
		window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dbInfo/del?id=" + id;
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
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dbInfo/list?groupId="+$("#groupId").val();
}

function testHeath(id,name) {
	toastr.info("正在测试【"+name+"】......");
	$.ajax({
		url: SERVER_SERVLET_CONTEXT_PATH + "/dbInfo/testHeath?id=" + id,
		//data: {},
		type: "GET",
		//dataType: "json",
		success: function(data) {
			if("success"==data){
				toastr.success("连接数据库成功，请刷新页面查看数据");
			}else if("noPro"==data){
				toastr.info("你好，使用此功能需要升级到专业版");
			}else{
				toastr.error(data);
			}
		}
	});
}