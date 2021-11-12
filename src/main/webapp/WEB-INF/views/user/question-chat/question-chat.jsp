<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="ko">
<head>
    <c:import url="../template/head-meta.jsp"/>
    <%--타이틀, 각 페이지따라 적절히 수정 (ex, 타요 - 멤버 프로필)--%>
    <title>타요 - 1:1채팅문의</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/chat-style.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.js"></script>


<script type="text/javascript">
$(document).ready(function() {
	
	sock = new SockJS("/question/chat");
	
	const board = document.querySelector('.chat-box');
	board.scroll(0, board.scrollHeight);
	
});

</script>






</head>
<body>
<c:import url="../template/header.jsp"/>

<section class="all-shadow">

<div class="con-box">
	<span class="con-box__description">customer service center</span>
	<i class="fas fa-sign-out-alt control-box__icon"></i>
</div>
<div class="chat-box">
    <div id="divChatData">
    	<c:forEach items="${mList }" var="msg">
    		<c:if test="${msg.memberId == member.id}">
    		<div class="MyChat">
    			<span>${msg.memberId }</span>
    			<div>${msg.content }</div>
    			<span><fmt:formatDate value="${msg.sendDate}" pattern="a h:mm"/></span>
    		</div>
    		</c:if>

    		<c:if test="${msg.memberId != member.id}">
	    	<div class="Chat" style="background-color: red">
    			<span>${msg.memberId }</span>
    			<div>${msg.content }</div>
    			<span><fmt:formatDate value="${msg.sendDate}" pattern="a h:mm"/></span>
    		</div>
    		</c:if>
    	
    	</c:forEach>
    </div>
</div>
<div class="chat-input">
    <input type="text" class="chat-message" id="message" onkeypress="if(event.keyCode==13){webSocket.sendChat();}" />
    <div class="tayo-button"id="btnSend">send</div>
</div>

</section>

<c:import url="../template/footer.jsp"/>
</body>
</html>