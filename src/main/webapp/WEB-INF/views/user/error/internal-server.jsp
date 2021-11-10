<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
    <c:import url="../template/head-meta.jsp"/>
    <%--타이틀, 각 페이지따라 적절히 수정 (ex, 타요 - 멤버 프로필)--%>
    <title>타요 - 에러</title>
</head>
<body>
<c:import url="../template/header.jsp"/>

<%-- 각자의 내용 작성 fixed-center 나 all-shadow는 필요없을시 지우고 사용해도 무방 --%>
<section class="all-shadow fixed-center">

    <%--아래내용 지우고 사용--%>
    <h3 class="tayo-under-line" style="text-align: center">Server Error</h3>
    <p style="text-align: center; margin-top: 30px">
        서비스에 불편을 드려 죄송합니다. <br>
        지속적인 에러 발생시 관리자에게 문의해주세요. <br> <br>
        <a href="${pageContext.request.contextPath}/" style="color: #a29bfe">home</a>
    </p>

</section>

<c:import url="../template/footer.jsp"/>
</body>
</html>