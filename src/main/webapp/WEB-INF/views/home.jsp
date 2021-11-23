<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport"
      content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<meta name="description" content="메타버스 가상 코딩공간 타요 입니다.">


<%-- 아이콘 --%>
<link rel="icon" type="image/png" href="${pageContext.request.contextPath}/resources/img/favicon.png"/>

<%-- 자바스크립트 --%>
<script src="${pageContext.request.contextPath}/resources/js/jquery-2.2.4.min.js" ></script>
<script src="${pageContext.request.contextPath}/resources/js/modal.js" defer></script>
<script src="${pageContext.request.contextPath}/resources/js/home.js" defer></script>

<%-- 스타일시트 --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/common-style.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/user-style.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/home-style.css">

<%-- fontawesome --%>
<script src="https://kit.fontawesome.com/0d232bdc2d.js" crossorigin="anonymous"></script>

    <title>타요 - 메타버스 코딩공간</title>
    
</head>
<body>
<div class="page-head">
<div class="top-class">
	<div style="height: 100px;"></div>
	<div class="wave-box">
		<div class="custom-shape-divider-bottom-1637501478">
    		<svg data-name="Layer 1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1200 120" preserveAspectRatio="none">
        		<path d="M985.66,92.83C906.67,72,823.78,31,743.84,14.19c-82.26-17.34-168.06-16.33-250.45.39-57.84,11.73-114,31.07-172,41.86A600.21,600.21,0,0,1,0,27.35V120H1200V95.8C1132.19,118.92,1055.71,111.31,985.66,92.83Z" class="shape-fill"></path>
    		</svg>
		</div>
	</div>
</div>
<section>
	<div class="logo-box">
		<div class="logo-items">
			<img src="${pageContext.request.contextPath}/resources/img/logo2.png" alt="logo" class="logo">
		</div>
		<div class="input-box">
		<c:if test="${empty loginMember}">
			<div class="input-items" onclick="location.href='${pageContext.request.contextPath}/login';"><a  href="${pageContext.request.contextPath}/login">login</a></div>
			<div class="input-items" onclick="location.href='${pageContext.request.contextPath}/join';"><a href="${pageContext.request.contextPath}/join">sign up</a></div>
		</c:if>
		<c:if test="${not empty loginMember}">	
			<div class="input-items" onclick="location.href='${pageContext.request.contextPath}/work-spaces';"><a href="${pageContext.request.contextPath}/work-spaces">start now</a></div>
		</c:if>
		</div>
	</div>
</section>
</div>
<div class="page">
<section>
	<div class="board-box">
		<div class="board-title">타요 소개 적어야함</div>
		<div class="board-list">
			<div class="board-items">
				<img src="${pageContext.request.contextPath}/resources/img/logo2.png" alt="logo" class="board-img">
			</div>
			<div class="board-content">
				이이?
			</div>
		</div>
		<div class="board-list">
			<div class="board-items">
				<img src="${pageContext.request.contextPath}/resources/img/logo2.png" alt="logo" class="board-img">
			</div>
			<div class="board-content">
				이이?
			</div>
		</div>
	</div>
</section>
<c:import url="user/template/footer.jsp"/>
</body>
</html>