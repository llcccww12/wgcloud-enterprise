function view(id) {
    window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/appInfo/view?id="+id;
}

function del(id) {
    window.location.href = SERVER_SERVLET_CONTEXT_PATH + "/appInfo/del?id="+id;
}