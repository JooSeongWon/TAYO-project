<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="ko">
<head>
<c:import url="../template/head-meta.jsp" />
<script src="${pageContext.request.contextPath}/resources/js/kakaologin.js" defer></script>
<%--타이틀, 각 페이지따라 적절히 수정 (ex, 타요 - 멤버 프로필)--%>
<title>타요 - 정보입력</title>


</head>
<body>
	<c:import url="../template/header.jsp" />

	<section class="all-shadow fixed-center">

		<label>Email </label><br> 
			<input type="email" name="email" id="email" value="${sessionScope.kakaoEmail}" class="tayo-input" readonly="readonly" /><br>
			
		<label>User Name </label><br> 
			<input type="text" name="name" id="name" class="tayo-input" placeholder="USER NAME" /><br><br> 
		
		<label>Phone</label><br> 
			<input type="text" name="phone" id="phone" class="tayo-input" placeholder="Phone" /><br><br>

		<div id="kakaologinBtn" class="tayo-button purple">정보입력</div>

	</section>

	<c:import url="../template/footer.jsp" />
</body>
</html>