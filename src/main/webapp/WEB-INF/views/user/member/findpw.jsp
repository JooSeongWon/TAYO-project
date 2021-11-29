<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
    <c:import url="../template/head-meta.jsp"/>
    <%--타이틀, 각 페이지따라 적절히 수정 (ex, 타요 - 멤버 프로필)--%>
    <title>타요 - 비밀번호 찾기</title>
    <script src="${pageContext.request.contextPath}/resources/js/findpw.js" defer></script>
	<link rel="stylesheet" href="/resources/css/findpw-style.css">
	
</head>
<body>
<c:import url="../template/header.jsp"/>

<%-- 각자의 내용 작성 all-shadow는 필요없을시 지우고 사용해도 무방 --%>
<section class="all-shadow">

	<div class="container">
		<div class="container__input">
			<input class="tayo-input" type="text" id="email" name="email" placeholder="USER EMAIL" required>
			<p>Please enter your email</p>
			
			<input class="tayo-input" type="text" id="phone" name="phone" placeholder="USER PHONE" required>
			<p>Please enter your phone</p>
		</div>
		
		<div id="findBtn" class="findBtn tayo-button purple">Continue</div>
	</div>
</section>

<c:import url="../template/footer.jsp"/>
</body>
</html>