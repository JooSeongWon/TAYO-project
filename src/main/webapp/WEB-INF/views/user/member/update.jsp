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

		.input-file {
			display: none;
		}
		
		.profile-img {
			position: relative;
			text-align: center;
			margin: 0 auto;
		}
		
		.input-file-button {
			width: 35px;
			height: 35px;
			background-color: var(--color-puple);
			border-radius: 50%;
			text-align: center;
			display: flex;
        	justify-content: center;
        	align-items: center;
        	cursor: pointer;
        	
        	position: absolute;
        	top: 85px; 
        	left: 89px;
		}
		
		.fas .fa-camera {
			width: 50%;
			height: 50%;
			margin: auto;
		}
		
		.fa-camera{
			color: var(--color-white);
		}
    </style>
</head>
<body>
<c:import url="../template/header.jsp"/>

<%-- 각자의 내용 작성 all-shadow는 필요없을시 지우고 사용해도 무방 --%>
<section class="all-shadow">

    <h1 class="tayo-under-line">My profile</h1>

    <div id="profile">
    	
		<div class="profile-img">
        	<c:if test="${empty member.profile}">
            	<img class="avatar" src="${pageContext.request.contextPath}/resources/img/no-profile.png" alt="프로필 없음">
        	</c:if>
        	<c:if test="${not empty member.profile}">
            	<img class="avatar" src="/img/${member.profile}" alt="프로필사진">
        	</c:if>
        
			<div class="input-file-button" >
  				<i class="fas fa-camera"></i>
			</div>
		</div>
		<input type="file" id="input-file" class="input-file" accept="image/*"/>
		
		
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
	const inputfilebtn = document.querySelector('.input-file-button')
	const inputfile = document.querySelector('.input-file')
	
	inputfilebtn.addEventListener("click", function(e) {
		inputfile.click()
	});
	
	inputfile.addEventListener("change", function(e) {
		let fileForm = /(.*?)\.(jpg|jpeg|png|gif|bmp|pdf)$/;
		var elem = inputfile.value;

        if(!elem.match(fileForm) ) {
	        showModal("TAYO", "이미지 파일이 아닙니다 :(")
        }else{
        	let formData = new FormData();
        	let file = inputfile.files[0];
        	
        	formData.append("uploadFile" , file);
        	
        	
        	console.log(file);
//         	'${loginMember.id}'
        	$.ajax({ 
        		type: "POST", 
        		enctype: 'multipart/form-data',
        		url: '/profile/fileupload',
        		data: formData,
                dataType: 'json',
        		processData: false,
        		contentType: false,
        		success: function(data){
        			if(!data.result) {
                        showModal('실패', data.message);
                        return;
                    }

                    //성공
                    location.reload();
        		},
        		error: () => showModal('실패', '요청을 처리할 수 없습니다.')
        	});
        }
    });
	
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
                    showModal('실패', '입력값이 바르지 않습니다.');
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
                    showModal('실패', '입력값이 바르지 않습니다.');
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

        let regex;

        //찬혁님이랑 얘기해서 유효성검사 추가
        switch (target) {
            case 'name':
                regex = /^[가-힣a-zA-Z0-9]{2,10}$/;
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