'use strict'

//입력받은 email, password 데이터를 json데이터 타입으로 /login POST에 전달
setModalParentNode(document.querySelector('.page-content'));

const loginBtn = document.querySelector('#loginBtn');
loginBtn.addEventListener('click', login);

function login() {
    const email = document.querySelector('#email').value;
    const password = document.querySelector('#password').value;

    $.ajax({
        type: 'POST',
        url: '/login',
        data: {email: email, password: password},
        dataType: 'json',
        success: loginCallBack,
        error: errorCallBack
    });
}

// 로그인에 성공하면 /로 이동
function loginCallBack(data) {
    if(data.result) {
        location.href = '/';
        return;
    }

    showModal('실패', data.message);
}

// 로그인에 실패하면 '연결오류' 출력
function errorCallBack(e) {
    showModal('실패', '요청을 처리할 수 없습니다.');
}