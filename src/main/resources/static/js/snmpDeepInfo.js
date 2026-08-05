function searchByOrder(orderBy,orderType){
	var urlParams = window.location.search;
	var hrefUrl = SERVER_SERVLET_CONTEXT_PATH + "/snmpDeepInfo/list?orderBy="+orderBy+"&orderType="+orderType;
	if (urlParams.indexOf("groupId") != -1) {
		hrefUrl += "&groupId="+$("#groupId").val();
	}
	window.location.href = hrefUrl;
}

function searchByOnline(state){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/snmpDeepInfo/list?state="+state;
}

function searchByAccount() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/snmpDeepInfo/list?account="+$("#account").val();
}


function add() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/snmpDeepInfo/edit";
}

function view(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/snmpDeepInfo/view?id="+id;
}

function viewDate(id,searchTime){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/snmpDeepInfo/view?id="+id+"&am="+searchTime;
}

function excelExport(id,searchTime){
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	window.open(SERVER_SERVLET_CONTEXT_PATH + "/snmpDeepInfo/chartExcel?id="+id+"&startTime="+startTime+"&endTime="+endTime+"&am="+searchTime);
}

function del(id) {
	if(confirm('您确定要删除此记录吗？')) {
		window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/snmpDeepInfo/del?id=" + id;
	}
}
function edit(id){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/snmpDeepInfo/edit?id="+id;
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
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/snmpDeepInfo/list?groupId="+$("#groupId").val();
}

function ajaxTestData(numId) {
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
	var testOid = $("#oidValue"+numId).val();
	if(""==testOid){
		alert("请填写测试Oid");
		return;
	}
	var snmpType = $("input[name='oidType"+numId+"']:checked").val();
	if(""==snmpType){
		alert("请填写测试获取的类型");
		return;
	}
	$('#modal-default').modal('show');
	var testOidName = $("#oidName"+numId).val();
	$("#testOidValue").html(testOidName+"，"+testOid+"，"+snmpType);
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

function ajaxTestDataView(hostname,snmpPort,snmpVersion,snmpCommunity,oidValue,oidType,testOidName,securityName,authPass,privPass) {
	$('#modal-default').modal('show');
	$("#testOidValue").html(testOidName+"，"+oidValue+"，"+oidType);
	$("#testResultData").html("正在获取数据......");
	if("version2c"==snmpVersion){
		snmpVersion="1";
	}
	if("version1"==snmpVersion){
		snmpVersion="0";
	}
	if("version3"==snmpVersion){
		snmpVersion="3";
	}
	$.ajax({
		url: SERVER_SERVLET_CONTEXT_PATH + "/snmpInfo/testResultData",
		type: "POST",
		data:{"hostname":hostname,"snmpPort":snmpPort,"snmpVersion":snmpVersion,"snmpCommunity":snmpCommunity,"testOid":oidValue,"snmpType":oidType,"securityName":securityName,"authPass":authPass,"privPass":privPass},
		//dataType: "json",
		success: function(data) {
			$("#testResultData").html(data);
			toastr.success("操作完成");
		}
	});
}

function testHeath(id,name) {
	toastr.info("正在测试【"+name+"】......");
	$.ajax({
		url: SERVER_SERVLET_CONTEXT_PATH + "/snmpDeepInfo/testHeath?id=" + id,
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

//数据表单动态添加一行下标，每次添加+1
var dataFromIndex = 1 ;
if($("#dataFromIndex").val() != ""){
	dataFromIndex = parseInt($("#dataFromIndex").val())+1 ;
}
function addDataForm() {
	var dataHideContentHtml = $("#dataHideContent").html();
	dataHideContentHtml = dataHideContentHtml.replace(/{num}/g, dataFromIndex);
	$("#dataFormList").append(dataHideContentHtml);
	$("#dataFromIndex").val(dataFromIndex);
	dataFromIndex += 1;
}

function copySnmpDeepInfo(){
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
		$("#idCopy").val(vals);
		$("#modal-defaultCopy").modal("toggle");
	}
}

function ajaxSaveCopySnmpDeepInfo() {
	$("#formCopy").ajaxSubmit(function(message) {
		window.location.href = window.location.href;
	});
}