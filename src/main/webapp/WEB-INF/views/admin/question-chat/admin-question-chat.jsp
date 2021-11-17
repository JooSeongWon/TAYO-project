<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
    <c:import url="../template/head-meta.jsp"/>
    <title>1:1 문의</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/adminchat-style.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.24.0/moment.min.js"></script>


<script type="text/javascript" defer="defer">
$(document).ready(function() {
	
	const memberId = ${sessionScope.loginMember.id};
	var now = new Date();
		
	getList();
	
	function getList() {
		//채팅방 리스트 불러오기
		$.ajax({
			url : "${contextPath}/admin/question/service/list",
			data : {},
			type : "POST",
			dataType : "JSON",
			success : function(data) {
				for(var i = 0; i < data.object.length; i++){
					appendList(data.object[i]);
					console.log(data.object[i].MEMBER_ID)
				}
			},
			error : function(e) {
				showModal('서버오류' , '페이지 새로 고침 또는 새 브라우저 창을 열어주세요.')
				console.log("getList 메소드 실패");
			}
		});
	}
	
	function appendList(list) {
		let questionChatId = list.QUESTION_CHAT_ID;
		
		let createListDiv = document.createElement('div');
		let createListItemDiv = document.createElement('div');
		let createImgDiv = document.createElement('div');
		let createImg = document.createElement('img');
		let createNameSpan = document.createElement('span');
		let createListDateSpan = document.createElement('span');
		
        const today = moment(now).format('YYYY-MM-DD')
        const before = moment(list.SEND_DATE).format('YYYY-MM-DD')

        let sendDay = moment(list.SEND_DATE).format('LL')
		if(moment(before).isSame(today)){
			sendDay = moment(list.SEND_DATE).format('LT')
		}
        
        
        createListDiv.setAttribute("class", "list");
        createListDiv.setAttribute("id", questionChatId);
        createListItemDiv.setAttribute("class", "list-div");
        createImgDiv.setAttribute("class", "img");
        createImg.setAttribute("class", "thumb");
        createNameSpan.setAttribute("class", "name");
        createListDateSpan.setAttribute("class", "list-date");
        
        if(list.FILE_ID == null){
        	createImg.src = '${pageContext.request.contextPath}' +'/resources/img/no-profile.png';
        } else {
        	createImg.src = '${pageContext.request.contextPath}' +'/img/' + list.FILE_ID ;
		}
        createNameSpan.innerText = list.NAME;
        createListDateSpan.innerText = sendDay;
        
        document.getElementById("divchatList").appendChild(createListDiv);
        createListDiv.appendChild(createListItemDiv);
        createListItemDiv.appendChild(createImgDiv);
        createImgDiv.appendChild(createImg);
        createListItemDiv.appendChild(createNameSpan);
        createListItemDiv.appendChild(createListDateSpan);
        createListDiv.onclick = function() {
        	questionId = $(this).attr('id');
			
			getMessage(questionId);
		}
	}
	
	
	const box = document.querySelector('.chat-box');
	const divChatData = document.querySelector('#divChatData');
	
	function getMessage(questionId) {
		//현재 채팅창 비우기
		
		while (divChatData.hasChildNodes()){ 
			divChatData.removeChild( divChatData.firstChild );
			sock.close()
		}
		
		//지난 메세지 불러오기
		$.ajax({
			url : "${contextPath}" + questionId,
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
		connect()
	}
	
	let sock;
	
	//소켓연결
	function connect() {
		sock = new SockJS("/question/chat");
		
		sock.onopen = onOpen;
		sock.onmessage = onMessage;	
	}
	
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
	
	//메세지 수신
	function onMessage(event) {
		const Message = JSON.parse(event.data);
		
		console.log(Message.admin)
		console.log(Message.admin.FILE)
		
		let data = {
			"NAME" : Message.msg.name,
			"CONTENT" : Message.msg.content,
			"sendDate" : Message.msg.sendDate,
			"memberId" : Message.msg.memberId,
			"GRADE" : Message.admin.GRADE,
			"FILE_ID" : Message.admin.FILE
		}
		
		CheckLR(data);
	}	
	
		
	//채팅방 버튼 이벤트 걸기
	const inputButton = document.querySelector('.chat-button');
	const inputMessage = document.querySelector('.chat-message');	
	
	//메세지 보내기
	inputButton.addEventListener('click', function() {
				
		let data = {
			"questionChatId": questionId,
			"content" : inputMessage.value,
			"memberId": memberId,
			"sendDate": now
		}
		let jsonData = JSON.stringify(data);
		sock.send(jsonData)
		inputMessage.value = '';
	});
	
	inputMessage.addEventListener('keydown', function(e) {
		if(e.key === 'Enter'){
			inputButton.click();
		}
	});

	
	function CheckLR(data) {
		const LR = (data.GRADE != "A") ? "left" : "right";
 		appendMessageTag(LR, data);
	}
	
	function appendMessageTag(R, data) {
		let createMessageDiv = document.createElement('div');
		let createProfileDiv = document.createElement('div');
		let createImgDiv = document.createElement('div');
		let createImg = document.createElement('img');
		
		let createNameSpan = document.createElement('span');
		let createContentDiv = document.createElement('div');
		let createDaySpan = document.createElement('span');
		
        const today = moment(now).format('YYYY-MM-DD')
        const before = moment(data.SEND_DATE).format('YYYY-MM-DD')

        let sendDay = moment(data.SEND_DATE).format('LL')
		if(moment(before).isSame(today)){
			sendDay = moment(data.SEND_DATE).format('LT')
		}
        
        
        if(data.FILE_ID == null){
        	createImg.src = '${pageContext.request.contextPath}' +'/resources/img/no-profile.png';
        } else {
        	createImg.src = '${pageContext.request.contextPath}' +'/img/' + data.FILE_ID ;
        	
		}
        
		createMessageDiv.classList.add("message");
		createProfileDiv.classList.add("profile");
		createImgDiv.setAttribute("class", "img");
		createImg.setAttribute("class", "thumb");
		createNameSpan.setAttribute("class", "name");
		createContentDiv.setAttribute("class", "message");
		createDaySpan.setAttribute("class", "day");
		
		createContentDiv.setAttribute("class", "content");
		createDaySpan.setAttribute("class", "day");
		
        if( R == "right"){
        	createImg.src = '${pageContext.request.contextPath}' +'/resources/img/admin-profile.png';
			createMessageDiv.classList.add("messageR");
			createProfileDiv.classList.add("right");
        }
        
        
        createNameSpan.innerText = data.NAME;
        createContentDiv.innerText = data.CONTENT;
        createDaySpan.innerText = sendDay;
        
        
        document.getElementById("divChatData").appendChild(createMessageDiv);
        createMessageDiv.appendChild(createProfileDiv);
        createProfileDiv.appendChild(createImgDiv);
        createImgDiv.appendChild(createImg);
        createProfileDiv.appendChild(createNameSpan);
        createMessageDiv.appendChild(createContentDiv);
        createMessageDiv.appendChild(createDaySpan);
        
        // 스크롤바 아래 고정
        box.scroll(0, box.scrollHeight);
    }
	
});
</script>
    
</head>
<body>
<c:import url="../template/nav.jsp"/>

<section>
	<div class="chat">
		<div class="chat-list">
			<div id="divchatList"></div>
			
		</div>
		<div class="chat-room">
			<div class="chat-box">
				<div id="divChatData"></div>
            
			</div>
			<div class="chat-input">
				<input type="text" class="chat-message" />
				<div class="tayo-button chat-button">send</div>
			</div>
		</div>
	</div>
</section>


</body>
</html>