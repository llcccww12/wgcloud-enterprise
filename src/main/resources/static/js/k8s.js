function viewContainer(id) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/k8sMonitor/viewContainer?id="+id;
}

function toPodListUrl(k8sName) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/k8sMonitor/list?dataType=pod&k8sName="+k8sName;
}

function toContainerListUrl(k8sName) {
	window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/k8sMonitor/list?dataType=container&k8sName="+k8sName;
}