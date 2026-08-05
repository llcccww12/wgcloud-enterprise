function searchByOrder(orderBy,orderType){
	var urlParams = window.location.search;
	var hrefUrl = SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/systemInfoList?orderBy="+orderBy+"&orderType="+orderType;
	if (urlParams.indexOf("pageSize=20000") != -1) {
		hrefUrl += "&pageSize=20000";
	}
	if (urlParams.indexOf("groupId") != -1) {
		hrefUrl += "&groupId="+$("#groupId").val();
	}
	window.location.href = hrefUrl;
}

function searchByOnline(state){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/systemInfoList?state="+state;
}

function searchByOnlineDashView(state){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/systemInfoList?dashView=1&state="+state;
}


function searchByOrderDashView(orderBy,orderType){
	var urlParams = window.location.search;
	var hrefUrl = SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/systemInfoList?dashView=1&orderBy="+orderBy+"&orderType="+orderType;
	if (urlParams.indexOf("pageSize=20000") != -1) {
		hrefUrl += "&pageSize=20000";
	}
	if (urlParams.indexOf("groupId") != -1) {
		hrefUrl += "&groupId="+$("#groupId").val();
	}
	window.location.href = hrefUrl;
}

function searchAll(){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/systemInfoList?pageSize=20000";
}

function searchAllDashView(){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/systemInfoList?dashView=1&pageSize=20000";
}

function viewImage(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/dash/hostDraw?id="+id;
}

function viewAllProcess(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/viewAllProcess?id="+id;
}

function viewAllPort(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/viewAllPortInfo?id="+id;
}

function viewIfconfig(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/viewIfconfigInfo?id="+id;
}

function viewGpuInfo(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/viewImportInfo?id="+id;
}

function viewDashView(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/detail?dashView=1&id="+id;
}
function viewChartDashView(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/chart?dashView=1&id="+id;
}
function viewDatetDashView(id,searchTime){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/chart?dashView=1&id="+id+"&am="+searchTime;
}
function view(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/detail?id="+id;
}
function viewChart(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/chart?id="+id;
}

function del(id) {
	if(confirm('您确定要删除此记录吗？此操作只会删除主机，但主机下的监控资源及历史数据不会被删除')) {
		window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/del?id=" + id;
	}
}

function viewWebSSH(hostname) {
	window.open(SERVER_SERVLET_CONTEXT_PATH + "/ssh2/view?hostname="+hostname);
}


function viewDate(id,searchTime){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/chart?id="+id+"&am="+searchTime;
}

function excelExport(id,searchTime){
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	window.open(SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/chartExcel?id="+id+"&startTime="+startTime+"&endTime="+endTime+"&am="+searchTime);
}

function excelExportHostList(){
	var urlParams = window.location.search;
	var chk_value =[];
	$("input[name='todo2']:checkbox").each(function() {
		if($(this).is(':checked')) {
			chk_value.push($(this).val());
		}
	});
	if(chk_value.length > 0){
		window.open(SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/hostListExcel?id=" + chk_value.join(","));
	}else{
		window.open(SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/hostListExcel" + urlParams);
	}
}

function ajaxSaveRemark() {
	var idRemark = $("#id").val();
	$("#"+idRemark+"_remark").html($("#remark").val());
	$("#modal-default").modal("toggle");
	$("#form2").ajaxSubmit(function(message) {

	});
}


function setHostRemark(hostId,hostRemark) {
	$("#id").val(hostId);
	$("#remark").val(hostRemark);
}


function setWinConsole(hostId,winConsole) {
	$("#id2").val(hostId);
	$("#winConsole").val(winConsole);
}

function ajaxSaveWinConsole() {
	$("#form3").ajaxSubmit(function(message) {
		window.open (message);
		window.location.href = window.location.href;
	});
}

function viewWinConsole() {
    window.open ($("#winConsole").val());
    $("#modal-default2").modal("toggle");
}

function showSetGroupId() {
	var chk_value =[];
	$("input[name='todo2']:checkbox").each(function() {
		if($(this).is(':checked')) {
			chk_value.push($(this).val());
		}
	});
	if(chk_value.length == 0){
		alert("请先选择需要设置标签的主机");
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
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/systemInfoList?groupId="+$("#groupId").val();
}

function searchByAccount() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/systemInfoList?account="+$("#account").val();
}

function searchByGroupIdDashView() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/systemInfoList?dashView=1&groupId="+$("#groupId").val();
}

function ajaxSystemInfoList() {
	var urlParams = window.location.search;
	var dashView = "";
	if (urlParams.indexOf("dashView") != -1) {
		dashView = "?dashView=1";
	}
	$.ajax({
		url: SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/systemInfoListAjax"+dashView,
		type: "GET",
		dataType: "json",
		success: function(data) {
			for(i in data) {
			    var obj = $("#"+data[i].id+"_state").html();
                if(obj!=null && obj != undefined){
                    $("#"+data[i].id+"_state").html(data[i].hostname);
                    $("#"+data[i].id+"_memPer").html(data[i].image);
                    $("#"+data[i].id+"_cpuPer").html(data[i].hostnameExt);
                    $("#"+data[i].id+"_rxbyt").html(data[i].rxbyt);
                    $("#"+data[i].id+"_txbyt").html(data[i].txbyt);
					$("#"+data[i].id+"_fiveLoad").html(data[i].fiveLoad);
					$("#"+data[i].id+"_netConnections").html(data[i].netConnections);
					$("#"+data[i].id+"_procs").html(data[i].procs);
                    $("#"+data[i].id+"_createTime").html(data[i].remark);
                }
			}
		}
	});
}

function startTime(){
    if("" == $("#startTime").html()){
        $("#timeDiv").show();
        $("#startTime").html("10");
    } else {
        var sec = parseInt($("#startTime").html());
        sec = sec - 1;
        if (-1==sec){
            $("#startTime").html("10");
        }else{
            $("#startTime").html(sec);
        }
    }
}

var timer=null;
var timer2=null;
function startTask() {
	if(timer == null){
		$("#startTaskBtn").html("停止刷新");
		$("#startTaskBtn").addClass("btn-default");
		toastr.success("【自动刷新】启动成功，每隔10秒会自动刷新主机状态、内存%、CPU%、上下行传输速率、系统负载、连接数量、更新时间");
		timer = setInterval(function(){ajaxSystemInfoList()},10000);
        timer2 = setInterval(function(){ startTime()},1000);
	}else{
		toastr.success("【自动刷新】停止成功");
		$("#startTaskBtn").html("自动刷新");
		$("#startTaskBtn").removeClass("btn-default");
		clearInterval(timer);
		timer=null;
        clearInterval(timer2);
        timer2=null;
        $("#startTime").html("");
        $("#timeDiv").hide();
	}
}

//批量开始或停止主机监控
function startOrStopMonitor(id,active) {
	if("2"==active){
		if(confirm('您确定要停止监控吗？停止后，该主机的进程、端口、日志、docker等资源也将停止监控')) {
			$.ajax(SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/updateActive",{
				type:"post",
				data:{"id":id,"active":active},
				success:function(data){
					window.location.href = window.location.href;
				}
			});
		}
	}
	if("1"==active){
		if(confirm('您确定要开始监控吗？')) {
			$.ajax(SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/updateActive",{
				type:"post",
				data:{"id":id,"active":active},
				success:function(data){
					window.location.href = window.location.href;
				}
			});
		}
	}
}

//设置主机是否加入面板统计
function setCountBlock(id,active) {
	if("2"==active){
		if(confirm('您确定要在监控概要、大屏页面停止计算该主机的内存、cpu、磁盘等资源吗？')) {
			$.ajax(SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/updateCountBlock",{
				type:"post",
				data:{"id":id,"countBlock":active},
				success:function(data){
					window.location.href = window.location.href;
				}
			});
		}
	}
	if("1"==active){
		if(confirm('您确定要在监控概要、大屏页面恢复计算该主机的内存、cpu、磁盘等资源吗？')) {
			$.ajax(SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/updateCountBlock",{
				type:"post",
				data:{"id":id,"countBlock":active},
				success:function(data){
					window.location.href = window.location.href;
				}
			});
		}
	}
}

function setHostOrderNum(hostId,hostOrderNum) {
	$("#id6").val(hostId);
	$("#hostOrderNum").val(hostOrderNum);
}

//设置主机排序序号
function ajaxSaveOrderNum() {
	$("#form6").ajaxSubmit(function(message) {
		window.location.href = window.location.href;
	});
}

function viewDateForDisk(id,searchTime){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/detail?id="+id+"&am="+searchTime;
}