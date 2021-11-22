<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
    <c:import url="user/template/head-meta.jsp"/>
    <title>타요 - 메타버스 코딩공간</title>
    <link rel="stylesheet" href="https://use.typekit.net/rnz2bks.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/home-style.css">
</head>
<body>
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
<div class="page">
<section>
	<div class="logo-box">
		<div class="logo-items">
			<img src="${pageContext.request.contextPath}/resources/img/logo2.png" alt="logo" class="logo">
		</div>
		<div class="input-box">
		<c:if test="${empty loginMember}">	
			<div class="input-items"><a href="${pageContext.request.contextPath}/login">login</a></div>
			<div class="input-items"><a href="${pageContext.request.contextPath}/join">sign up</a></div>
		</c:if>
		<c:if test="${not empty loginMember}">	
			<div class="input-items"><a href="${pageContext.request.contextPath}/work-spaces">start now</a></div>
		</c:if>
		</div>
	</div>
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