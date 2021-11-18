<%--
  Created by IntelliJ IDEA.
  User: parkjungtae
  Date: 2021-11-15
  Time: 오전 11:49
  To change this template use File | Settings | File Templates.
--%>
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

    <%-- fontawesome --%>
    <script src="https://kit.fontawesome.com/0d232bdc2d.js" crossorigin="anonymous"></script>

    <%-- 스타일시트 --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/common-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/user-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/work-space-style.css ">

    <%-- 자바스크립트 --%>
    <script src="${pageContext.request.contextPath}/resources/js/jquery-2.2.4.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/modal.js" defer></script>
    <script src="${pageContext.request.contextPath}/resources/js/sockjs.js" defer></script>
    <script src="${pageContext.request.contextPath}/resources/js/work-space-script.js" defer></script>

    <script>
        const roomId = ${roomId};
        const myId = ${loginMember.id};
        const myName = `${loginMember.name}`;
        <c:if test="${not empty loginMember.profile}">
        const myProfile = ${loginMember.profile};
        </c:if>
        <c:if test="${empty loginMember.profile}">
        const myProfile = null;
        </c:if>
    </script>

    <title>[${name}] - 타요 가상공간</title>
</head>
<body>
<section class="screen all-shadow" draggable="true">
    <%-- 내 캐릭터 --%>
    <div class="avatar user">
        <div class="img-wrap">
            <c:if test="${not empty loginMember.profile}">
                <img class="avatar__img" src="${pageContext.request.contextPath}/img/${loginMember.profile}"
                     alt="내 아바타">
            </c:if>
            <c:if test="${empty loginMember.profile}">
                <img class="avatar__img" src="${pageContext.request.contextPath}/resources/img/no-profile.png"
                     alt="내 아바타">
            </c:if>
            <i class="fas fa-microphone-slash mute active"></i>
            <i class="fas fa-video on-air"></i>
        </div>
        <div class="avatar__name">${loginMember.name}</div>
    </div>
    <div class="speech-bubble-wrap active user">
        <div class="speech-bubble"></div>
        <div class="arrow-down"></div>
    </div>
    <div class="range"></div>

    <%-- 게시판 네비게이션 바 버튼 --%>
    <i class="fas fa-chevron-right"></i>
    <%-- 나가기 버튼 --%>
    <i class="fas fa-reply exit"></i>
    <%-- 하단메뉴 --%>
    <div class="bottom-menu">
        <%-- 채팅 인풋박스 --%>
        <div class="chat-box">
            <label>
                <input type="text" id="chat" maxlength="30" placeholder="type message" spellcheck="false" tabindex="0">
                <i class="far fa-comment-dots"></i>
            </label>
            <%-- 대화기록창 열기, 닫기 --%>
            <i class="fas fa-arrow-up"></i>
            <%-- 대화 기록창 --%>
            <div class="chat-history">
                <div class="chat-history__title">${name}</div>
                <div class="chat-history__content tayo-scroll-bar">
                </div>
            </div>
        </div>

        <i class="fas fa-video" id="cam"></i>
        <i class="fas fa-desktop" id="screen"></i>
        <i class="fas fa-microphone" id="mice"></i>
    </div>
</section>
<%-- 내 비디오 화면 --%>
<div class="my-cam-wrap" draggable="true">
    <div class="my-cam-title"><i class="fas fa-play-circle"></i>&nbsp;ON AIR<i class="fas fa-expand-arrows-alt my-cam-full"></i></div>
    <video src="" class="my-cam" playsinline autoplay width="320" height="180" muted></video>
</div>
</body>
</html>