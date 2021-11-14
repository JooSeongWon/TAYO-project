<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
    <c:import url="../template/head-meta.jsp"/>
    <%--타이틀, 각 페이지따라 적절히 수정 (ex, 타요 - 멤버 프로필)--%>
    <title>타요 - 가상공간</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/work-space-list-style.css ">
    <script src="${pageContext.request.contextPath}/resources/js/work-space-list-script.js" defer></script>
</head>
<body>
<c:import url="../template/header.jsp"/>

<%-- 각자의 내용 작성 all-shadow는 필요없을시 지우고 사용해도 무방 --%>
<section class="work-spaces">

    <%-- 헤더 --%>
    <div class="work-spaces__header">
        <%-- 좌측 프로필 --%>
        <div class="header__left">
            <c:if test="${empty sessionScope.loginMember.profile}">
                <img src="${pageContext.request.contextPath}/resources/img/no-profile.png" alt="사진 없음"
                     class="left__profile-img">
            </c:if>
            <c:if test="${not empty sessionScope.loginMember.profile}">
                <img src="${pageContext.request.contextPath}/img/${sessionScope.loginMember.profile}" alt="프로필 사진"
                     class="left__profile-img">
            </c:if>
            <div class="left__nick">${sessionScope.loginMember}</div>
        </div>

        <%-- 우측 컨트롤 박스 --%>
        <div class="header__right">
            <input type="text" class="invitation-code-input" placeholder="invitation code" maxlength="12">
            <div class="invitation-code-submit tayo-button">admission</div>
        </div>
    </div>

    <%-- 바디 --%>
    <div class="all-shadow work-spaces__body">
        <h3><span class="tayo-under-line work-spaces__title"> MY WORK LIST </span></h3>
        <i class="fas fa-plus-circle add-work-space"
           <c:if test="${workSpaceList ne null and workSpaceList.size() > 6}">style="padding-right: 30px" </c:if>></i>
        <div class="work-spaces__board tayo-scroll-bar">
            <%-- 가상공간 --%>
            <div class="work-spaces__list">

                <c:if test="${empty workSpaceList}">
                    <div class="work-spaces__item tayo-under-line">
                        <div class="no-item">
                            가상공간이 없습니다.
                        </div>
                    </div>
                </c:if>

                <c:forEach items="${workSpaceList}" var="item">
                    <div class="work-spaces__item tayo-under-line">
                        <div class="item__head-count">
                            <img src="${pageContext.request.contextPath}/resources/img/no-profile.png" alt="인원표시 이미지"
                                 class="item__profile-img">
                            <div class="item__count">(0/${item.headCount})</div>
                        </div>

                        <div class="item__title">
                                ${item.name}
                            <c:if test="${item.isOwner(sessionScope.loginMember.id)}">
                                <span class="item__leader">L</span>
                            </c:if>
                        </div>

                        <c:if test="${item.isOwner(sessionScope.loginMember.id)}">
                            <i class="fas fa-cog" data-workspaceId="${item.id}"></i>
                        </c:if>
                        <c:if test="${not item.isOwner(sessionScope.loginMember.id)}">
                            <i class="fas fa-sign-out-alt" data-workspaceId="${item.id}"></i>
                        </c:if>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>

</section>

<c:import url="../template/footer.jsp"/>
</body>
</html>