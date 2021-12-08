<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
    <c:import url="../template/head-meta.jsp"/>
    <%--타이틀, 각 페이지따라 적절히 수정 (ex, 타요 - 멤버 프로필)--%>
    <title>타요 - 회원가입</title>

    <script src="${pageContext.request.contextPath}/resources/js/login-join.js" defer></script>
    <link rel="stylesheet" href="/resources/css/join-style.css">
</head>
<body>
<c:import url="../template/header.jsp"/>

<%-- 각자의 내용 작성 fixed-center 나 all-shadow는 필요없을시 지우고 사용해도 무방 --%>
<section class="all-shadow">

<input type="text" id="name" name="name" class="tayo-input" placeholder="USER NAME" />

<input type="email" id="email" name="email" class="tayo-input" placeholder="Email" />

<input type="text" id="phone" name="phone" class="tayo-input" placeholder="Phone" />

<input type="password" id="password" name="password" class="tayo-input" placeholder="Password"/>

<input type="password" id="confirm" name="password" class="tayo-input" placeholder="Confirm"/>

<div id="joinBtn" class="tayo-button purple">Sign up</div>

</section>

<c:import url="../template/footer.jsp"/>
</body>
</html>