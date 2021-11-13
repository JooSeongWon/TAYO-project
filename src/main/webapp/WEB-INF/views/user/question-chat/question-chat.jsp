<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="ko">
<head>
    <c:import url="../template/head-meta.jsp"/>
    <title>타요 - 1:1채팅문의</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/chat-style.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.24.0/moment.min.js"></script>

<script type="text/javascript" defer="defer">
$(document).ready(function() {

	//방번호
	const questionId = ${questionId};
	const memberId = ${sessionScope.loginMember.id};
	var memberName = "${memberName}";
	console.log(memberName);
	
	//소켓연결
	sock = new SockJS("/question/chat");
	sock.onopen = onOpen;
// 	sock.onmessage = onMessage;
	
	//소켓 연결시 사용자+방번호 보내기
	function onOpen() {
		let data = {
			"questionChatId": questionId,
			"memberId": memberId,
			"content" : "CHAT-OPEN"
		}
		let jsonData = JSON.stringify(data);
		sock.send(jsonData);
	}
	
	
	//버튼 이벤트 걸기
	const inputButton = document.querySelector('.chat-button');
	const inputMessage = document.querySelector('.chat-message');
	
	var now = new Date();
	//메세지 보내기
	inputButton.addEventListener('click', function() {
		
		let data = {
			"questionChatId": questionId,
			"name" : memberName,
			"content" : inputMessage.value,
			"memberId": memberId,
			"sendDate": now
		}
		let jsonData = JSON.stringify(data);
		sock.send(jsonData)
		CheckLR(data)
		inputMessage.value = '';
	});
	
	inputMessage.addEventListener('keydown', function(e) {
		if(e.key === 'Enter'){
			inputButton.click();
		}
	});
	
	//메세지 수신
	function onMessage(event) {
		const Message = event.data.split(",");
		CheckLR(Message);
	}
	
	const box = document.querySelector('.chat-box');
	
	//지난 메세지 불러오기
	$.ajax({
		url : "${contextPath}/question/service/" + questionId,
		data : {},
		type : "POST",
		dataType : "JSON",
		success : function(data) {
			for(var i = 0; i < data.object.length; i++){
				CheckLR(data.object[i]);
			}
		},
		error : function() {
			showModal('서버오류' , '페이지 새로 고침 또는 새 브라우저 창을 열어주세요.')
			console.log("joinChatRoomProc 메소드 실패");
		}
	});
	
	
	function CheckLR(data) {
		
		const LR = (data.memberId != "${sessionScope.loginMember.id}") ? "left" : "right";
 		appendMessageTag(LR, data.name, data.content, data.sendDate);
	}
	

	function appendMessageTag(R, name, content, sendDate) {
		let createMessageDiv = document.createElement('div');
		let createNameSpan = document.createElement('span');
		let createContentDiv = document.createElement('div');
		let createDateSpan = document.createElement('span');
		
        let sendDay = moment(sendDate).format('LT')

		if(now - sendDate > 86399){
			sendDay = moment(sendDate).format('LL')
		}
        
		createMessageDiv.classList.add("message");
		createContentDiv.setAttribute("class", "content");
		createDateSpan.setAttribute("class", "day");
		
        if( R == "right"){
        	createMessageDiv.classList.add("right");
        }
        
        
        createNameSpan.innerText = name;
        createContentDiv.innerText = content;
        createDateSpan.innerText = sendDay;
        
        
        document.getElementById("divChatData").appendChild(createMessageDiv);
        createMessageDiv.appendChild(createNameSpan);
        createMessageDiv.appendChild(createContentDiv);
        createMessageDiv.appendChild(createDateSpan);
        // 스크롤바 아래 고정
        box.scroll(0, box.scrollHeight);
    }
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
    <div id="divChatData"></div>
</div>
<div class="chat-input">
    <input type="text" class="chat-message" />
    <div class="tayo-button chat-button">send</div>
</div>

</section>

<c:import url="../template/footer.jsp"/>
</body>
</html>