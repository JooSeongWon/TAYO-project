<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
    <c:import url="../template/head-meta.jsp"/>
    <%--타이틀, 각 페이지따라 적절히 수정 (ex, 타요 - 멤버 프로필)--%>
    <title>타요 - 1:1채팅문의</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/chat-style.css">
</head>
<body>
<c:import url="../template/header.jsp"/>

<%-- 각자의 내용 작성 fixed-center 나 all-shadow는 필요없을시 지우고 사용해도 무방 --%>
<section class="all-shadow fixed-center">

<div class="con-box">
	<span class="con-box__description">customer service center</span>
	<i class="fas fa-sign-out-alt control-box__icon"></i>
</div>
<div class="chat-box">
    <div id="divChatData"></div>
</div>
<div class="chat-input">
    <input type="text" class="chat-message" id="message" onkeypress="if(event.keyCode==13){webSocket.sendChat();}" />
    <div class="tayo-button"id="btnSend">send</div>
</div>

</section>

<c:import url="../template/footer.jsp"/>
</body>
</html>