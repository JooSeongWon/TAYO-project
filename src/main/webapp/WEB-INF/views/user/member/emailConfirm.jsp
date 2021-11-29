<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html lang="ko">
<head>
    <c:import url="../template/head-meta.jsp"/>
    <%--타이틀, 각 페이지따라 적절히 수정 (ex, 타요 - 멤버 프로필)--%>
    <title>타요 - 메타버스 코딩공간</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/email-confirm-style.css">
</head>
<body>
<c:import url="../template/header.jsp"/>

<section class="all-shadow">
    <i class="fas fa-envelope-open-text"></i>
    <div class="description"><span>${email}</span>님 타요 회원이 되신걸 환영합니다! <br>회원가입 후 모든 서비스를 이용하실 수 있습니다.</div>
    <div class="tayo-button login-btn" onclick="location.href = '/login'">로그인</div>
</section>

<c:import url="../template/footer.jsp"/>
</body>
</html>