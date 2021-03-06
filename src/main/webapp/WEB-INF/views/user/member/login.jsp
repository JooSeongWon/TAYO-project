<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
    <c:import url="../template/head-meta.jsp"/>
    <%--타이틀, 각 페이지따라 적절히 수정 (ex, 타요 - 멤버 프로필)--%>
    <title>타요 - 로그인</title>

    <script src="${pageContext.request.contextPath}/resources/js/login-script.js" defer></script>
    <link rel="stylesheet" href="/resources/css/login-style.css">

</head>
<body>
<c:import url="../template/header.jsp"/>

<%-- 각자의 내용 작성 fixed-center 나 all-shadow는 필요없을시 지우고 사용해도 무방 --%>
<section class="all-shadow">


    <input type="email" id="email" name="email" class="tayo-input"
           placeholder="USER EMAIL">

    <input type="password" id="password" name="password"
           class="tayo-input" placeholder="PASSWORD">


    <div id="loginBtn" class="tayo-button purple loginBtn">LOGIN</div>

    <div type="button" id="kakaoLoginBtn" class="tayo-button"
         onclick="location.href ='https://kauth.kakao.com/oauth/authorize?client_id=d688ecbcd7678fc036df85dfda0efcf3&redirect_uri=${redirectUri}'">
        KAKAO LOGIN
    </div>

    <div class="forget-password" onclick="location.href='/findpw'" >Forget password?</div>

    <div class="under-line"></div>

    <div class="join">Don't have an account? <span onclick="location.href='/consent'">Join now</span></div>

</section>

<c:import url="../template/footer.jsp"/>
</body>
</html>


