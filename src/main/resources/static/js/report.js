function searchByPara(reportType){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/report/list?reportType="+reportType;
}

function excelExport(id){
	window.open(SERVER_SERVLET_CONTEXT_PATH + "/report/chartExcel?id="+id);
}


function view(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/report/view?id="+id;
}


function showSendMail(hostId,hostRemark) {
	$("#modal-default").modal("toggle");
}


function ajaxSendMail() {
	$("#form2").ajaxSubmit(function(message) {
		if(message=='success'){
			toastr.success("发送成功");
		}else{
			toastr.error("发送错误: "+message);
		}
	});
	$("#modal-default").modal("toggle");
}
