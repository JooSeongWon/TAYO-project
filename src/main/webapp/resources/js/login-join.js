'use strict'

//입력받은 데이터를 json데이터 타입으로 /join POST에 전달
setModalParentNode(document.querySelector('.page-content'));

const joinBtn = document.querySelector('#joinBtn');
joinBtn.addEventListener('click', join);

const body = document.body;

const lodingBack = {
	display: 'flex',
	justifyContent: 'center',
	alignItems: 'center',
	position: 'absolute',
	zIndex: '9998',
	width: '100%',
	height: '100%',
	backgroundColor: 'rgba(0,0,0,0.4)'		
}

const lodingImg = {
    zIndex: '9999',
    margin: 'auto'
}

const lodingBackDom = document.createElement('div');
const lodingImgDom = document.createElement('img');
lodingImgDom.setAttribute('src', '/resources/img/loading.gif');

lodingBackDom.appendChild(lodingImgDom)

$(lodingBackDom).css(lodingBack);
$(lodingImgDom).css(lodingImg);


function join() {
	const name = document.querySelector('#name').value;
    const email = document.querySelector('#email').value;
    const phone = document.querySelector('#phone').value;
    const password = document.querySelector('#password').value;
    
    body.appendChild(lodingBackDom);
    
    $.ajax({
        type: 'POST',
        url: '/join',
        data: {name: name, email: email, phone: phone , password: password},
        dataType: 'json',
        success: function(data) {
        	joinCallBack(data);
        	body.removeChild(lodingBackDom);
		},
        error: errorCallBack
    });
}


// 회원가입에 성공하면 /로 이동
function joinCallBack(data) {
	
	if(data.result){
		showModal("TAYO", "회원가입 완료!!" , function() {
			location.href = "/";
		})
	}
	else {
		showModal('TAYO', '회원가입 양식을 지켜주세요');
	}
	
}


// 회원가입에 실패하면 실패 '요청을 처리할 수 없습니다.' 출력
function errorCallBack(e) {
    showModal('TAYO', '서버오류');
}

// 유효성 검사

const name = document.querySelector('#name');
const email = document.querySelector('#email');
const phone = document.querySelector('#phone');
const password = document.querySelector('#password');

const joinField = {name, email, phone, password};

//유효성검사
function isValidation(target, value, valueConfirm) {

    let regex;

    switch (target) {
    
        case 'name':
            regex = /^[가-힣a-zA-Z0-9]{2,10}$/;
            return value.match(regex);
            
        case 'email':
        	regex = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/;
        	return value.match(regex);
        	
        case 'phone':
            regex = /^01[0-9]-?([0-9]{3,4})-?([0-9]{4})$/;
            return value.match(regex);
            
        case 'password':
            if(!valueConfirm) return false;
            if(value !== valueConfirm) return false;

            regex = /^[a-zA-Z0-9!@#$%^&*()?_~]{8,20}$/;
            return value.match(regex);
    }

    return false;
}

