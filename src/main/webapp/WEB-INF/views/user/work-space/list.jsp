<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
    <c:import url="../template/head-meta.jsp"/>
    <%--타이틀, 각 페이지따라 적절히 수정 (ex, 타요 - 멤버 프로필)--%>
    <title>타요 - 가상공간</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/work-space-style.css ">
    <script src="${pageContext.request.contextPath}/resources/js/work-space-script.js"></script>
</head>
<body>
<c:import url="../template/header.jsp"/>

<%-- 각자의 내용 작성 fixed-center 나 all-shadow는 필요없을시 지우고 사용해도 무방 --%>
<section class="fixed-center work-spaces">

    <%-- 헤더 --%>
    <div class="work-spaces__header">
        <%-- 좌측 프로필 --%>
        <div class="header__left">
            <img src="${pageContext.request.contextPath}/resources/img/no-profile.png" alt="프로필 사진" class="left__profile-img">
            <div class="left__nick">박정태 #0119</div>
        </div>

        <%-- 우측 컨트롤 박스 --%>
        <div class="header__right">
            <input type="text" class="invitation-code-input" placeholder="invitation code" maxlength="50">
            <div class="invitation-code-submit tayo-button">admission</div>
        </div>
    </div>

    <%-- 바디 --%>
    <div class="all-shadow work-spaces__body">
        <h3><span class="tayo-under-line work-spaces__title"> MY WORK LIST </span></h3>
        <i class="fas fa-plus-circle add-work-space"></i>
        <div class="work-spaces__board">
            <%-- 가상공간 --%>
            <div class="work-spaces__list">
                <div class="work-spaces__item tayo-under-line">
                    <div class="item__head-count">
                        <img src="${pageContext.request.contextPath}/resources/img/no-profile.png" alt="인원표시 이미지" class="item__profile-img">
                        <div class="item__count">(5/5)</div>
                    </div>
                    <div class="item__title">KH 파이널 프로젝트 ! <span class="item__leader">L</span> </div>
                    <%--<i class="fas fa-cog"></i>--%>
                    <i class="fas fa-sign-out-alt"></i>
                </div>
            </div>
        </div>
    </div>

</section>

<c:import url="../template/footer.jsp"/>
</body>
</html>