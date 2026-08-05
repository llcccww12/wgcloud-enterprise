
function add() {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/accountInfo/edit";
}


function edit(id){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/accountInfo/edit?id="+id;
}

function editPasswd(id){
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/accountInfo/editPasswd?id="+id;
}

function del(id) {
	if(confirm('您确定要删除此记录吗？删除后，此账号下的资源将只对管理员可见，建议删除之前先将该账号的监控资源迁移到其他账号下')) {
		window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/accountInfo/del?id=" + id;
	}
}

function showMenuToAdmin() {
	$("#modal-default2").modal("toggle");
}

function ajaxSaveAdminMenus() {
	getMenuCheckValues();
	$("#form2").ajaxSubmit(function(message) {
		$("#modal-default2").modal("toggle");
		toastr.success("设置成功，请重新登录。若需要重置，请重启server即可");
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

function showPasswd(){
	var type = $("#passwd").attr("type");
	if(type=='text'){
		$("#eyeSwitch").attr("class","fa fa-eye");
		$("#passwd").attr("type","password");
	}else{
		$("#eyeSwitch").attr("class","fa fa-eye-slash");
		$("#passwd").attr("type","text");
	}
}
