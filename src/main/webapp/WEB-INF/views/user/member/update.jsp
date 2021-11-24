<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
    <c:import url="../template/head-meta.jsp"/>
    <%--타이틀, 각 페이지따라 적절히 수정 (ex, 타요 - 멤버 프로필)--%>
    <title>타요 - 메타버스 코딩공간</title>
    <style>

        #profile {
            display: flex;
            flex-direction: column;
            width: 400px;
            font-size: var(--font-medium);
        }

        .profile-field {
            display: flex;
            justify-content: center;
            margin-top: 16px;
            font-weight: bold;
        }

        .profile-field > i {
            margin-left: 24px;
        }

        h1 {
            text-align: center;
        }

        .avatar {
            width: 120px;
            height: 120px;
            border: 2px solid var(--color-light-grey);
            border-radius: 50%;
            margin: auto;
        }

        div[class$='update'] {
            display: none;
        }

        div[class$='update'].active {
            display: block;
        }

        div[class$='display'] {
            display: none;
        }

        div[class$='display'].active {
            display: block;
        }

        div.password__update.active {
            display: flex;
            flex-direction: column;
            width: 211px;
        }

        .fa-edit {
            cursor: pointer;
            line-height: 36px;
        }

        .fa-edit.active {
            color: var(--color-puple);
        }


    </style>
</head>
<body>
<c:import url="../template/header.jsp"/>

<%-- 각자의 내용 작성 all-shadow는 필요없을시 지우고 사용해도 무방 --%>
<section class="all-shadow">

    <h1 class="tayo-under-line">My profile</h1>

    <div id="profile">

        <c:if test="${empty member.profile}">
            <img class="avatar" src="${pageContext.request.contextPath}/resources/img/no-profile.png" alt="프로필 없음">
        </c:if>
        <c:if test="${not empty member.profile}">
            <img class="avatar" src="/img/${member.profile}" alt="프로필사진">
        </c:if>

        <div class="email profile-field">${member.email }</div>

        <div class="name profile-field">
            <div class="name__display active">${member.name }</div>
            <div class="name__update">
                <input type="text" class ="tayo-input" placeholder="input name">
            </div>
            <i class="far fa-edit" onclick="fnIconClick('name', this)"></i>
        </div>

        <div class="phone profile-field">
            <div class="phone__display active">${member.phone }</div>
            <div class="phone__update">
                <input type="text" class ="tayo-input" placeholder="input phone">
            </div>
            <i class="far fa-edit" onclick="fnIconClick('phone', this)"></i>
        </div>
		
		<!-- 비밀번호 쓰고 들어가기 위한 c:if 문법쓰기 -->
		
		<c:if test= "${not empty member.password }">
        <div class="password profile-field">
            <div class="password__display active">password</div>
            <div class="password__update">
                <input type="password" class ="tayo-input password__input" placeholder="input password">
                <input type="password" class ="tayo-input password__confirm" placeholder="confirm">
            </div>
            <i class="far fa-edit" onclick="fnIconClick('password', this)"></i>
        </div>
        </c:if>

    </div>

</section>

<script type="text/javascript">

    const name = {
        display: document.querySelector('.name__display'),
        update: document.querySelector('.name__update'),
        isModifying: false
    };
    const phone = {
        display: document.querySelector('.phone__display'),
        update: document.querySelector('.phone__update'),
        isModifying: false
    };
    
    const password = {
        display: document.querySelector('.password__display'),
        update: document.querySelector('.password__update'),
        isModifying: false
    };

    const profileField = {name, phone, password};

    //아이콘 클릭 시
    function fnIconClick(target, btn) {

        const input = profileField[target].update.querySelector('input');

        if(!profileField[target].isModifying) {
            if(target !== 'password') {
                input.placeholder = profileField[target].display.innerText;
            }
        } else {
            //유효성검사
            if(target === 'password') {
                const confirm = document.querySelector('.password__confirm');
                //수정안함
                if(input.value === '' && confirm.value === '') {
                    profileField[target].isModifying = !profileField[target].isModifying;
                    profileField[target].display.classList.toggle('active');
                    profileField[target].update.classList.toggle('active');
                    btn.classList.toggle('active');
                    return;
                }

                //수정할거임 
                if(!isValidation(target, input.value, confirm.value)) {
                    return;
                }

                confirm.value = '';
            } else {
                //수정안함
                if(input.value === '') {
                    profileField[target].isModifying = !profileField[target].isModifying;
                    profileField[target].display.classList.toggle('active');
                    profileField[target].update.classList.toggle('active');
                    btn.classList.toggle('active');
                    return;
                }
                if(!isValidation(target, input.value)) {
                    return;
                }
            }

            //유효성검사 통과 - 서버 전송
            $.ajax({
                type: 'POST',
                url: '/profile/update',
                data: {target, value:input.value},
                dataType: 'json',
                success: data => updateCallback(data, target, btn),
                error: () => {showModal('에러', '서버에러')}
            });

            return;
        }

        profileField[target].isModifying = !profileField[target].isModifying;
        profileField[target].display.classList.toggle('active');
        profileField[target].update.classList.toggle('active');
        btn.classList.toggle('active');
    }

    //유효성검사
    function isValidation(target, value, valueConfirm) {

        /*if(!value && value.length < 1) {
            showModal('입력값 확인', '값을 입력해주세요!');
            return false;
        }*/

        //찬혁님이랑 얘기해서 유효성검사 추가
        switch (target) {
            case 'name':fnValidationName(value);

                break;
            case 'phone':fnValidationPhone(value);

                break;
            case 'password':fnValidationPassword(value);

                break;
        }

        return true;
    }
    
    //이름 한글 영어 입력 체크
    let nameCheck = false;
    
    var fnValidationName = function (name) {
    	let nameError = 0; //초기화
    	
    	if(name != null) {
    		
    		//한글 체크
    		if(fnValidationNameKor(name)) {
    			console.log('1')
    		} else {
    			nameError++;
    			console.log('2')
    		}
    		//영어 체크
    		if(fnValidationNameEng(name)) {
    			console.log('3')
    		} else {
    			nameError++;
    			console.log('4')
    		}
    		
    		//길이 체크
    		if(!fnValidationNameLength(name)) {
    			console.log('5')
			} else {
				nameError++;
				console.log('6')
			}
    		console.log('7')
    	} else {
    		nameError++;
    		console.log('8')
    	}
    	console.log('9')
    	if(nameError < 1 ) {
    		nameCheck = true;
    		console.log('10')
    	}
    }
    
    //이름 한글체크
    var fnValidationNameKor = function(name) {
    	let result = false;
    	if(name.search(/^[가-힣]+$/) != -1 ) {
    		result = true;
    	}
    	return result;
    };
    
 	// 이름 영어체크
    var fnValidationNameEng = function(name) {
		let result = false;
		if(name.search(/[a-zA-Z]/ig)  != -1 ) {
			result = true;
		}
		return result;
	};
	
	//이름 길이체크
	var fnValidationNameLength = function(name) {
		let result = false;
		// 길이(8~20)
		if(!/^[가-힣a-zA-Z]{8,20}$/.test(name)) {
			result = true;
		} else {
			result = false;
		}

		return result;
	}
    
    // 전화번호 입력 체크
    let phoneCheck = false;
    
    var fnValidationPhone = function(phone) {
    	let phoneError = 0;
    	
    	if(phone != null ) {
    		if(fnValidationPhone(phone)) {
    			console.log('a')
    		} else {
    			phoneError++;
    			console.log('b')
    		}
    		
    	} else {
    		phoneError++;
    		console.log('c')
    	}
    	
    	if( phoneError < 1 ) {
    		phoneCheck = true;
    		console.log('d')
    	}
    }
    
    //전화번호 체크
    var fnValidationPhone = function(phone) {
    	let result = false;
    	if(phone.search(/^01([0]{3})-?([0-9]{3,4})-?([0-9]{4})$/) != -1 ) {
    		result = true;
    	}
    	return result;
    };
    
    
    // 비밀번호 영어 입력 체크
    let passwdCheck = false;
    
    
    var fnValidationPassword = function (password) {
    	let passwdError = 0 ; //초기화
    	
    	if(password != null) {
    		
    		// 영어 체크 
    		if(fnValidtaionPwdEng(password)) {
    		}else{
    			passwdError++;
    		}
    		
			//2-2.숫자포함
			if(fnValidtaionPwdNumber(password)) {
			}else{
				passwdError++;
			}
			//2-3.특수문자
			if(fnValidtaionPwdChar(password)) {
			}else{
				passwdError++;
			}
			//2-4.길이
			if(!fnValidationPwdLength(password)) {
			}else{
				passwdError++;
			}
    		
    		//
    	} else {
    		passwdError++;
    	}
    	
    	if(passwdError < 1) {
			passwdCheck = true;
		}
    	
    }
    
    // 비밀번호 영어체크
    var fnValidtaionPwdEng = function (password) {
		let result = false;
		if(password.search(/[a-zA-Z]/ig)  != -1 ){
			result = true;
		}
		return result;
	};
	
	// 비밀번호 특문 체크
	var fnValidtaionPwdChar  = function (password) {
		let result = false;
		if(password.search(/[!@#$%^&*()?_~]/g)  != -1 ) {
			result = true;
		}
		return result;
	};
	
	// 비밀번호 숫자 체크 
	var fnValidtaionPwdNumber = function (password) {
		let result = false;
		if(password.search(/[0-9]/g) != -1 ) {
			result = true;
		}
		return result;
	};
	
	// 비밀번호 길이 채크
	var fnValidationPwdLength = function (password) {
		let result;
		// 길이(8~20)
		if(!/^[a-zA-Z0-9!@#$%^&*()?_~]{8,20}$/.test(password)) {
			result = true;
		} else {
			result = false;
		}

		return result;
	};
	
    

    function updateCallback(data, target, btn) {
        if(!data.result) { //실패
            showModal('실패', data.message);
            return;
        }

        //성공
        const input = profileField[target].update.querySelector('input');
        if(target !== 'password') {
            profileField[target].display.innerText = input.value;
        }
        input.value = '';
        profileField[target].isModifying = !profileField[target].isModifying;
        profileField[target].display.classList.toggle('active');
        profileField[target].update.classList.toggle('active');
        btn.classList.toggle('active');

    }

</script>

<c:import url="../template/footer.jsp"/>
</body>
</html>