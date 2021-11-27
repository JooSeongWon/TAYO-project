'use strict'

const kakaologinBtn = document.querySelector('#kakaologinBtn');

/*클릭 이벤트 부여*/
kakaologinBtn.addEventListener('click', function() {
	
	const email = document.querySelector('#email').value;
	const name = document.querySelector('#name').value;
	const phone = document.querySelector('#phone').value;
	
	$.ajax({
		type: 'POST',
		url: '/kakao-join',
		data: {email: email, name: name, phone: phone},
		dataType: 'json',
		success: loginCallBack,
		error: errorCallBack
	})
	
});

//로그인에 성공하면 /로 이동
function loginCallBack(data) {
    if(data.result) {
    	showModal('TAYO', '회원가입이 완료되었습니다.', function() {
			location.href = "/login";
		})				
        return;
    }
    
    showModal('TAYO', '회원가입에 실패하였습니다.')
    
}

// 로그인에 실패하면 '연결오류' 출력
function errorCallBack(e) {
    showModal('실패', '서버오류');
}