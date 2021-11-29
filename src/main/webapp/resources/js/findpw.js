'use strict'

//입력받은 데이터를 json데이터 타입으로 /join POST에 전달
setModalParentNode(document.querySelector('.page-content'));

const findBtn = document.querySelector('#findBtn');


$(function(){
	$("#findBtn").click(function(){

		if($("#email").val() == ""){
			showModal("TAYO", "이메일을 입력해주세요");
			return;
		}
		else if($("#phone").val() == ""){
			showModal("TAYO", "핸드폰번호를 입력해주세요");
			return;
		}
		
		$.ajax({
			url : "/findpw",
			type : "POST",
			data : {
				email : $("#email").val(),
				phone : $("#phone").val()
			},
			success : function(result) {
				showModal("TAYO", "이메일로 임시번호가 발급되었습니다." , function() {
					location.href = "/login";
				})				
			},
		})
	});
})