function searchByOrder(orderBy,orderType){
	var urlParams = window.location.search;
	var hrefUrl = SERVER_SERVLET_CONTEXT_PATH + "/snmpInfo/list?orderBy="+orderBy+"&orderType="+orderType;
	if (urlParams.indexOf("groupId") != -1) {
		hrefUrl += "&groupId="+$("#groupId").val();
	}
	window.location.href = hrefUrl;
}

function searchByOnline(state){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/snmpInfo/list?state="+state;
}

function searchByAccount() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/snmpInfo/list?account="+$("#account").val();
}


function add() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/snmpInfo/edit";
}

function view(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/snmpInfo/view?id="+id;
}

function viewDate(id,searchTime){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/snmpInfo/view?id="+id+"&am="+searchTime;
}

function excelExport(id,searchTime){
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	window.open(SERVER_SERVLET_CONTEXT_PATH + "/snmpInfo/chartExcel?id="+id+"&startTime="+startTime+"&endTime="+endTime+"&am="+searchTime);
}

function del(id) {
	if(confirm('您确定要删除此记录吗？')) {
		window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/snmpInfo/del?id=" + id;
	}
}
function edit(id){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/snmpInfo/edit?id="+id;
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
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/snmpInfo/list?groupId="+$("#groupId").val();
}

function ajaxTestData() {
	var hostname = $("#hostname").val();
	if(""==hostname){
		alert("请填写设备IP");
		return;
	}
	var snmpPort = $("#snmpPort").val();
	if(""==snmpPort){
		alert("请填写SNMP端口");
		return;
	}
	var snmpVersion = $("input[name='snmpVersion']:checked").val();
	if(""==snmpVersion){
		alert("请填写SNMP版本");
		return;
	}
	var snmpCommunity = $("#snmpCommunity").val();
	if(""==snmpCommunity){
		alert("请填写SNMP团体名称（community）");
		return;
	}
	var testOid = $("#testOid").val();
	if(""==testOid){
		alert("请填写测试Oid");
		return;
	}
	var snmpType = $("input[name='snmpType']:checked").val();
	if(""==snmpType){
		alert("请填写测试获取的类型");
		return;
	}
	var securityName = $("#securityName").val();
	var authPass = $("#authPass").val();
	var privPass = $("#privPass").val();
	$("#testResultData").html("正在获取数据......");
	$.ajax({
		url: SERVER_SERVLET_CONTEXT_PATH + "/snmpInfo/testResultData",
		type: "POST",
		data:{"hostname":hostname,"snmpPort":snmpPort,"snmpVersion":snmpVersion,"snmpCommunity":snmpCommunity,"testOid":testOid,"snmpType":snmpType,"securityName":securityName,"authPass":authPass,"privPass":privPass},
		//dataType: "json",
		success: function(data) {
			$("#testResultData").html(data);
		}
	});
}

function testHeath(id,name) {
	toastr.info("正在测试【"+name+"】......");
	$.ajax({
		url: SERVER_SERVLET_CONTEXT_PATH + "/snmpInfo/testHeath?id=" + id,
		//data: {},
		type: "GET",
		//dataType: "json",
		success: function(data) {
			if("success"==data){
				toastr.success("SNMP检测成功，请刷新页面查看数据");
			}else if("noPro"==data){
				toastr.info("你好，使用此功能需要升级到专业版");
			}else{
				toastr.error(data);
			}
		}
	});
}