function add() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/hostWarnDiy/edit";
}

function addForHostname(hostname) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/hostWarnDiy/edit?hostname="+hostname;
}

function addBatch() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/hostWarnDiy/editBatch";
}

function searchByAccount() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/hostWarnDiy/list?account="+$("#account").val();
}

function viewHost(hostname) {
	if (hostname.indexOf("(") != -1) {
		hostname = hostname.substring(0, hostname.indexOf("("));
	}
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/systemInfo/systemInfoList?hostname="+hostname;
}

function view(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/hostWarnDiy/view?id="+id;
}

function edit(id){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/hostWarnDiy/edit?id="+id;
}

function del(id) {
	if(confirm('您确定要删除此记录吗？')) {
		window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/hostWarnDiy/del?id=" + id;
	}
}

function copyHostWarnDiy(){
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
		$("#modal-default3").modal("toggle");
	}
}

function ajaxSaveCopyHostWarnDiy() {
	$("#form4").ajaxSubmit(function(message) {
		window.location.href = window.location.href;
	});
}

function showMenuToAdmin() {
	$("#modal-default2").modal("toggle");
}

function ajaxSaveAdminMenus() {
	getMenuCheckValues();
	$("#form2").ajaxSubmit(function(message) {
		$("#modal-default2").modal("toggle");
		if(message=='error'){
			toastr.info("此功能需要升级到专业版才可使用，请联系我们升级");
		}else{
			toastr.success("设置成功，请重新登录。若需要重置，请重启server即可");
		}
	});
}

function getMenuCheckValues() {
	var zTreeObj = $.fn.zTree.getZTreeObj("treeMenu");
	var checkedNodes = zTreeObj.getCheckedNodes();
	var menuIdsStr = "";
	for(i in checkedNodes) {
		menuIdsStr+=checkedNodes[i].id+",";
	}
	$("#menuIds").val(","+menuIdsStr);
	return true;
}

function checkRepeat() {
	var hostname =  $("#hostname").val();
	if(''==hostname){
		return;
	}
	$.ajax({
		url: SERVER_SERVLET_CONTEXT_PATH + "/hostWarnDiy/checkRepeat?hostname=" + hostname,
		//data: {},
		type: "GET",
		//dataType: "json",
		success: function(data) {
			if('1'==data){
				toastr.error(hostname+"已经设置过自定义告警了");
			}
		}
	});
}

function viewServerFile() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/hostWarnDiy/viewServerFile";
}
