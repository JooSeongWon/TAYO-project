'use strict'

//입력받은 데이터를 json데이터 타입으로 /join POST에 전달
setModalParentNode(document.querySelector('.page-content'));

const findBtn = document.querySelector('#findBtn');


$(function(){
	$("#findBtn").click(function(){
		
	    const email = document.querySelector('#email').value;
	    const phone = document.querySelector('#phone').value;

	    if(!/^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/.test(email)) {
	    	showModal("TAYO", "이메일 형식을 지켜주세요");
	    	return;
	    }
	    
	    if(!/^01[0-9]-?([0-9]{3,4})-?([0-9]{4})$/.test(phone)) {
	    	showModal("TAYO", "핸드폰번호가 올바르지 않습니다");
	    	return;
	    }
		
		$.ajax({
			url : "/findpw",
			type : "POST",
			data : {
				email : $("#email").val(),
				phone : $("#phone").val()
			},
			success : function(data) {
				if(!data.result) {
					showModal("TAYO", data.message);
					return;
				}
				showModal("TAYO", "이메일로 임시번호가 발급되었습니다." , function() {
					location.href = "/login";
				})				
			},
		})
	});
})