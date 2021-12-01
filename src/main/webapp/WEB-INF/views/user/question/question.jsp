<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
    <c:import url="../template/head-meta.jsp"/>
    <title>타요 - 자주묻는 질문</title>
   
<style type="text/css">

section {
    background-color: var(--color-white);
    margin-top: 20px;
    padding: 30px;
    width: 850px;
    height: 680px;
}

.wrap {
    width: 650px;
    height: 540px;
    margin: 0px auto;
    overflow-y: scroll;
}

.child {
/* 	margin: 0 auto; */
	padding: 30px;
}
.fa-arrow-down {
	float: right;
	cursor: pointer;
    transition: all 200ms ease; 	
}

.fa-arrow-down.active {
	transform: rotate(0.5turn);
}

.question{
	font-size: var(--font-small);
	padding: 8px;
}

.answer {
	display: flex;
    transition: all 600ms ease; 
	background-color: var(--color-puple);
	font-size: var(--font-small);
	overflow: hidden;
	height: 120px;
	justify-content: center;
	align-items: center;
	color: var(--color-white);
	font-weight: bold;
}

.answer.disabled {
	height: 0;
}

.tayo-under-line {
    padding-bottom: 6px;
    margin: 0 auto;
    margin-bottom: 40px;
    width: 350px;
}

#chatBtn {
	float: right;
	border-radius: 4px;
    color: var(--color-white);
    background: var(--color-dark-grey);
}

#q{
    color: var(--color-blue);
}

#a{
    color: var(--color-red);
}

hr{
margin: 8px;
}

p{
margin: 32px;
}

</style>
    
</head>
<body>
<c:import url="../template/header.jsp"/>

<section class="all-shadow">
	<button id="chatBtn">1:1 문의</button>
	<h1 class="tayo-under-line" style="text-align: center">Popular articles</h1>
	<div class="wrap tayo-scroll-bar">
	<c:forEach items="${list }" var="question">
		<div class="child">
			<i id="q" class="fab fa-quora"></i>
				<span class="question">${question.questionContent}</span>
				
			<i class="fas fa-arrow-down"></i> <br> <hr>
				<span class="answer disabled">

<!-- 			<i id="a" class="fas fa-font"></i> -->
					<p>${question.answerContent }</p>
				</span>
				
		</div> <!-- child -->
	</c:forEach>
	</div> <!-- wrap -->
</section>

<script type="text/javascript">
$(document).ready(function(){
	
	const aList = document.querySelectorAll('.answer');
	const btnList = document.querySelectorAll('.fa-arrow-down');

	for(let i = 0; i < btnList.length; i++) {
		btnList[i].addEventListener('click', () => {
			aList[i].classList.toggle('disabled');
			btnList[i].classList.toggle('active');
		});
	}

	$("#chatBtn").click( function() {
		console.log("Btn")

	$.ajax({
	url : "${contextPath}/question/service/open",
	data : {},
	type : "POST",
	dataType : "JSON",
	success : function(data) {
		console.log(data)
		location.href = "${contextPath}/question/service";
	},
	error : function() {
		console.log("openChat 메소드 실패");
	}
	});
	});
	

})
</script>


<c:import url="../template/footer.jsp"/>
</body>
</html>