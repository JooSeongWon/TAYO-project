<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
    <c:import url="../template/head-meta.jsp"/>
    <%--타이틀, 각 페이지따라 적절히 수정 (ex, 타요 - 멤버 프로필)--%>
    <title>타요 - 메타버스 코딩공간</title>
    
<style>
section {
	display: flex;
	flex-direction: column;
	align-items: center;
	
	width: 40%;
	max-width: 420px;
}

.avatar {
    width: 75px;
    height: 75px;
    border: 2px solid var(--color-light-grey);
    border-radius: 50%;
}
.profile-img {
	position: relative;
	text-align: center;
	margin: 20px 0px;
}

.tayo-under-line {
	font-size: 18px;
	text-align: center;
	width: 90%;
	border-bottom: 2px solid var(--color-light-grey);
	
}

.tayo-input {
	width: 70%;
	margin: 0px 0px 30px 0px;
	border-bottom: 2px solid var(--color-light-grey);
}

.passwordBtn {
	margin: 40px 0px;
	width: 68%;
	height: 31px;
}

.user__name {
	font-size: 19px;
	font-weight: bold;
}

.description{
	font-size: 14px;
	margin: 30px 0px;
}

</style>

</head>
<body>
<c:import url="../template/header.jsp"/>
<%-- 각자의 내용 작성 all-shadow는 필요없을시 지우고 사용해도 무방 --%>
<section class="all-shadow">
	
	<div class="tayo-under-line">My profile</div>
	<div class="profile-img">
		<c:if test="${empty member.profile}">
           	<img class="avatar" src="${pageContext.request.contextPath}/resources/img/no-profile.png" alt="프로필 없음">
       	</c:if>
       	<c:if test="${not empty member.profile}">
           	<img class="avatar" src="/img/${member.profile}" alt="프로필사진">
       	</c:if>
	</div>
	<div class="user__name">${loginMember }</div>
	<div class="description">본인 확인을 위해 비밀번호를 입력해주세요.</div>
	<input type="password" id="password-input" class="tayo-input" placeholder="PASSWORD">
	<div class="tayo-button passwordBtn">Continue</div>		

</section>

<script type="text/javascript">

const passwordBtn = document.querySelector('.passwordBtn');
const passwordInput = document.querySelector('#password-input');
console.log(passwordBtn);

passwordBtn.addEventListener('click', function() {
	
	const password = passwordInput.value;
	
	if(!password || password === '') {
		showModal('TAYO', '비밀번호를 입력하세요.');
		return;
	}
	
	$.ajax({
		type: 'POST',
		url: '/profile',
		data: {password},
		dataType: 'json',
		success: (data) => {
			if(!data.result) {
				showModal('실패', data.message);
				return;
			}
			location.href = '/profile/update';
			
		},
		error: console.log
	})
	
	
});

	/*엔터 이벤트 부여*/
passwordInput.addEventListener('keydown', function(e) {

	if(e.key === 'Enter'){
		passwordBtn.click();
	}
		
});

</script>
<c:import url="../template/footer.jsp"/>

</body>
</html>