'use strict'
$("#wirteBtn").click(function() {
	$(location).attr("href", "/admin/notice/write");
});

$("#btnWrite").click(function() {
	$("form").submit();
});

$("#btnUpdate").click(function() {
	$("form").submit();
});

$("#cancel").click(function() {
	history.go(-1);
});
