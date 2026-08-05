function searchByOnline(state){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dbTable/list?state="+state;
}
function searchByOrder(orderBy,orderType){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dbTable/list?orderBy="+orderBy+"&orderType="+orderType+"&dbInfoId="+$("#dbInfoId").val();
}

function searchByAccount() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dbTable/list?account="+$("#account").val();
}

function view(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dbTable/edit?id="+id;
}

function viewChart(id){
    window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dbTable/viewChart?id="+id;
}

function searchByDb(){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dbTable/list?dbInfoId="+$("#dbInfoId").val();
}


function add() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dbTable/edit";
}

function del(id) {
	if(confirm('您确定要删除此记录吗？此操作只会删除此处的数据记录')) {
		window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dbTable/del?id=" + id;
	}
}

function viewDate(id,searchTime){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dbTable/viewChart?id="+id+"&am="+searchTime;
}

function excelExport(id,searchTime){
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	window.open(SERVER_SERVLET_CONTEXT_PATH + "/dbTable/chartExcel?id="+id+"&startTime="+startTime+"&endTime="+endTime+"&am="+searchTime);
}

function testHeath(id,name) {
	toastr.info("正在测试【"+name+"】......");
	$.ajax({
		url: SERVER_SERVLET_CONTEXT_PATH + "/dbTable/testHeath?id=" + id,
		//data: {},
		type: "GET",
		//dataType: "json",
		success: function(data) {
			if(data.indexOf("success")>-1){
				toastr.success("SQL执行完成，执行结果"+data.replace("success",""));
			}else if("noPro"==data){
				toastr.info("你好，使用此功能需要升级到专业版");
			}else{
				toastr.error(data);
			}
		}
	});
}