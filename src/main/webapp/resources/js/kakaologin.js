'use strict'

//content중앙에 모달창 띄우기
setModalParentNode(document.querySelector('.page-content'));

const kakaologinBtn = document.querySelector('#kakaologinBtn');

/*클릭 이벤트 부여*/
kakaologinBtn.addEventListener('click', function() {

	const email = document.querySelector('#email').value;
	const name = document.querySelector('#name').value;
	const phone = document.querySelector('#phone').value;

    // 유효성 검사
	
    if(!/^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/.test(email)) {
    	showModal("TAYO", "이메일 형식을 지켜주세요");
    	return;
    }	 
	
    if(!/^[가-힣a-zA-Z0-9]{2,10}$/.test(name)) {
    	showModal("TAYO", "이름은 한글/영문/숫자 2자에서 10자 사이로 입력해주세요");
    	return;
    }
       
    if(!/^01[0-9]-?([0-9]{3,4})-?([0-9]{4})$/.test(phone)) {
    	showModal("TAYO", "핸드폰번호가 올바르지 않습니다");
    	return;
    }

	
	$.ajax({
		type: 'POST',
		url: '/kakao-join',
		data: {email: email, name: name, phone: phone},
		dataType: 'HTML',
		success: joinCallBack,
		error: errorCallBack
	})

});

//로그인에 성공하면 /로 이동
function joinCallBack(result) {
	
	console.log(result)
	
	if(result) {
		showModal('TAYO', '회원가입이 완료되었습니다.', function() {
			
			location.href = '/login';
			
		})				
		return;
	} else {
		
		showModal('TAYO', '회원가입에 실패하였습니다.')
		
	}


}

//로그인에 실패하면 '연결오류' 출력
function errorCallBack(e) {

	showModal('실패', '서버오류');
	
}
