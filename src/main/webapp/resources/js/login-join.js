'use strict'

//입력받은 데이터를 json데이터 타입으로 /join POST에 전달
setModalParentNode(document.querySelector('.page-content'));

const joinBtn = document.querySelector('#joinBtn');
joinBtn.addEventListener('click', join);

function join() {
	const name = document.querySelector('#name').value;
    const email = document.querySelector('#email').value;
    const phone = document.querySelector('#phone').value;
    const password = document.querySelector('#password').value;

    $.ajax({
        type: 'POST',
        url: '/join',
        data: {name: name, email: email, phone: phone , password: password},
        dataType: 'html',
        success: joinCallBack,
        error: errorCallBack
    });
}


// 회원가입에 성공하면 /로 이동
function joinCallBack(data) {
	
//	showModal('환영합니다!', '회원가입 완료!!');
	
	location.href = "/";
	
}

//function goMain() {
//	location.href = "/";
//}

// 회원가입에 실패하면 실패 '요청을 처리할 수 없습니다.' 출력
function errorCallBack(e) {
    showModal('실패', '양식을 지켜주세요');
}




