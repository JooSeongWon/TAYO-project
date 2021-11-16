'use strict'

$("#wirteBtn").click(function() {
	$(location).attr("href", "/admin/question/write");
});

$("#btnWrite").click(function() {
	$("form").submit();
		console.log("clicked")
});

$("#cancel").click(function() {
	history.go(-1);
});

$("#btnUpdate").click(function() {
	$("form").submit();
});

