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

$(".btnDelete").click(function() {
	showModal('오류', '해당 요청을 처리할 수 없습니다.')

});

