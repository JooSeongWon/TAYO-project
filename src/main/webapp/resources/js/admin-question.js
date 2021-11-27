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

$(".deleteBtn").click(function() {
	 showModal('삭제', '질문을 삭제 하시겠습니까?', () => {
		 location.href = `/admin/question/delete/${this.getAttribute('data-question-id') }`;
    },()=>{});
});