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
//	$(location).attr("href", "/admin/question/");
});

//admin-question 수정버튼
//$(".fa-quora").click(function() {
//	$(location).attr("href", "/admin/question/update?question_no="+ $(this).attr("data-questionNo"));
//	console.log("q-clicked")
//
//})