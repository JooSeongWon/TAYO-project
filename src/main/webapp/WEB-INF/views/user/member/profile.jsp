<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
    <c:import url="../template/head-meta.jsp"/>
    <%--타이틀, 각 페이지따라 적절히 수정 (ex, 타요 - 멤버 프로필)--%>
    <title>타요 - 메타버스 코딩공간</title>
    
    <style>
    	h1 {
            text-align: center;
        }
    </style>
    
</head>
<body>
<c:import url="../template/header.jsp"/>

<%-- 각자의 내용 작성 all-shadow는 필요없을시 지우고 사용해도 무방 --%>
<section class="all-shadow">
	
	<h1>비밀번호 확인</h1>
	<div class="password">
		<div class="check__password">
			<input type="password" id="password-input" class="tayo-input" placeholder="input password">
			<button class="passwordBtn">확인</button>		
		</div>
	</div>
</section>

<script type="text/javascript">

const passwordBtn = document.querySelector('.passwordBtn');
const passwordInput = document.querySelector('#password-input');
console.log(passwordBtn);

passwordBtn.addEventListener('click', function() {
	
	const password = passwordInput.value;
	
	if(!password || password === '') {
		showModal('실패', '비밀번호를 입력하세요.');
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