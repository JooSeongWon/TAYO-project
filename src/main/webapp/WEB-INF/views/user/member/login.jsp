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
						<div id="loginBtn" class="tayo-button purple loginBtn" >Login</div>
						<div type="button" id="kakaoLoginBtn" class="tayo-button purple"
						onclick="location.href ='https://kauth.kakao.com/oauth/authorize?client_id=d688ecbcd7678fc036df85dfda0efcf3&redirect_uri=${redirectUri}'">kakao Login</div>
<!-- 						<button id="join" class="tayo-input joinBtn" onclick="location.href='/join'">회원가입</button> -->
						<div id="findpw" class="tayo-button purple findpw" onclick="location.href='/findpw'">비밀번호 찾기</div>
					</div>
				</div>
		</div>


	</section>

	<c:import url="../template/footer.jsp" />
</body>
</html>


