<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="ko">
<head>
<c:import url="../template/head-meta.jsp" />
<%--타이틀, 각 페이지따라 적절히 수정 (ex, 타요 - 멤버 프로필)--%>
<title>타요 - 로그인</title>

    <script src="${pageContext.request.contextPath}/resources/js/login-script.js" defer></script>
    
</head>
<body>
	<c:import url="../template/header.jsp" />

	<%-- 각자의 내용 작성 fixed-center 나 all-shadow는 필요없을시 지우고 사용해도 무방 --%>
	<section class="all-shadow fixed-center">

		<div>
				<div class="form-group">
					<label for="email">EMAIL</label>
					<div>
						<input type="email" id="email" name="email" class="tayo-input"
							placeholder="Email">
					</div>
				</div>
				<br>
				<div class="form-group">
					<label for="password">Password</label>
					<div>
						<input type="password" id="password" name="password"
							class="tayo-input" placeholder="password" >
					</div>
				</div>
				<br>
				<div>
					<div style="margin: auto;">
						<button id="loginBtn" class="loginbtn" >Login</button>
						<button type="button" id="kakaoLoginBtn" 
						onclick="location.href ='https://kauth.kakao.com/oauth/authorize?client_id=d688ecbcd7678fc036df85dfda0efcf3&redirect_uri=https%3a%2f%2flocalhost%3a8443%2flogin%2fkakao&response_type=code'">kakao Login</button>
						<button id="join" class="joinBtn" onclick="location.href='/join'">회원가입</button>
					</div>
				</div>
		</div>


	</section>

	<c:import url="../template/footer.jsp" />
</body>
</html>


