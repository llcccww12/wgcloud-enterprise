
//切换主题
function switchTheme(themeSign){
	$.ajax({
		url: SERVER_SERVLET_CONTEXT_PATH + "/warnInfo/switchTheme?themeName="+themeSign,
		type: "POST",
		success: function(data) {
			if("success"==data){
				window.location.href = window.location.href;
			}else{
				toastr.info(data);
			}
		}
	});
}