function searchByPara(reportType){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/report/list?reportType="+reportType;
}

function excelExport(id){
	window.open(SERVER_SERVLET_CONTEXT_PATH + "/report/chartExcel?id="+id);
}


function excelExportHostList(){
	window.open(SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/hostListExcel");
}

function excelExportHostMacList(){
	window.open(SERVER_SERVLET_CONTEXT_PATH + "/largeModel/hostMacListExcel");
}

function excelExportNetworkList(){
	window.open(SERVER_SERVLET_CONTEXT_PATH + "/largeModel/hostNetworkNameListExcel");
}

function excelExportCpuTemperList(){
	window.open(SERVER_SERVLET_CONTEXT_PATH + "/largeModel/hostCpuTemperListExcel");
}

function excelExportDiskPerList(){
	window.open(SERVER_SERVLET_CONTEXT_PATH + "/largeModel/hostDiskPerListExcel");
}

function excelExportDiskIoStateList(){
	window.open(SERVER_SERVLET_CONTEXT_PATH + "/largeModel/hostDiskIoStateListExcel");
}

function zipDownLoad() {
	var chk_value =[];
	$("input[name='todo2']:checkbox").each(function() {
		if($(this).is(':checked')) {
			chk_value.push($(this).val());
		}
	});
	if(chk_value.length == 0){
		alert("请先选择需要下载的记录");
		return;
	}
	var ids = chk_value.join(",");
	window.open(SERVER_SERVLET_CONTEXT_PATH + "/largeModel/zipDownload?id="+ids);
}
