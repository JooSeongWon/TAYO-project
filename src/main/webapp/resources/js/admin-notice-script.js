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
	 showModal('삭제', '공자사항을 삭제 하시겠습니까?', () => {
		 location.href = `/admin/notice/delete/${this.getAttribute('data-notice-id') }`;
     },()=>{});
});

