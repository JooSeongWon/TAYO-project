<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
    <c:import url="../template/head-meta.jsp"/>
    <%--타이틀, 각 페이지따라 적절히 수정 (ex, 타요 - 멤버 프로필)--%>
    <title>타요 - 메타버스 코딩공간</title>
</head>
<body>
<c:import url="../template/header.jsp"/>

<%-- 각자의 내용 작성 all-shadow는 필요없을시 지우고 사용해도 무방 --%>
<section class="all-shadow">

    <%--아래내용 지우고 사용--%>
    <h3 class="tayo-under-line" style="text-align: center">테스트 페이지</h3>
    <p style="text-align: center">업로드 경로 : ${path}</p>
    <c:if test="${empty sessionScope.loginMember}">
        <p style="text-align: center">비로그인 상태</p>
    </c:if>
    <c:if test="${not empty sessionScope.loginMember}">
        <p style="text-align: center">유저네임 : ${sessionScope.loginMember}</p>
        <p style="text-align: center">테스트 유저 이메일 : ${sessionScope.loginMember.email}</p>
    </c:if>
    <p style="text-align: center">테스트 경로 : question</p>
    <div class="tayo-button">타요버튼</div>
    <div class="tayo-button purple">타요버튼 퍼플</div>
    <input type="text" class="tayo-input" placeholder="hold text here">

</section>

<c:import url="../template/footer.jsp"/>
</body>
</html>