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
    const confirm = document.querySelector('#confirm').value;
    
    // 유효성 검사
 
    if(!/^[가-힣a-zA-Z0-9]{2,10}$/.test(name)) {
    	showModal("TAYO", "이름은 한글/영문/숫자 2자에서 10자 사이로 입력해주세요");
    	return;
    }
    
    
    if(!/^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/.test(email)) {
    	showModal("TAYO", "이메일 형식을 지켜주세요");
    	return;
    }
    
    if(!/^01[0-9]-?([0-9]{3,4})-?([0-9]{4})$/.test(phone)) {
    	showModal("TAYO", "핸드폰번호가 올바르지 않습니다");
    	return;
    }
    
    if(!/^[a-zA-Z0-9!@#$%^&*()?_~]{8,20}$/.test(password)) {
    	showModal("TAYO", "비밀번호는 영문/숫자/특수문자(8-20자)를 기입해주세요");
    	return;
    }
    if(password !== confirm) {
    	showModal("TAYO", "비밀번호가 일치하지 않습니다.");
    	return;
    }
    
    
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
		showModal("TAYO", "이메일로 인증코드가 발급되었습니다." , function() {
			location.href = "/login";
		})
	}
	else {
		showModal('TAYO', data.message);
	}
	
}


// 회원가입에 실패하면 실패 '요청을 처리할 수 없습니다.' 출력
function errorCallBack(e) {
	body.removeChild(lodingBackDom);
    showModal('TAYO', '서버오류');
}


 
   



