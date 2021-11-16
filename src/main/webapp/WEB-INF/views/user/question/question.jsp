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
    width: 900px;
    height: 680px;
}

.wrap {
    width: 750px;
    height: 540px;
    margin: 50px auto;
    overflow-y: scroll;
}

.child {
	padding-left: 100px;
}
.fa-arrow-down {
	float: right;
	cursor: pointer;
	transition: all 400ms ease;
}

.fa-arrow-down.active {
	transform: rotate(0.5turn);
}

.answer {
	display: block;
	transition: all 400ms ease;
}

.answer.disabled {
	height: 0;
	font-size: 0;
	line-height: 0;
}
</style>
    
</head>
<body>
<c:import url="../template/header.jsp"/>

<section class="all-shadow">
<button id="chatBtn">이양</button>
	<h1 class="tayo-under-line" style="text-align: center">Popular articles</h1>
	<div class="wrap">
	<c:forEach items="${list }" var="question">
		<div class="child">
			<span class="question">${question.questionContent}</span>
			<i class="fas fa-arrow-down"></i><br>
			<span class="answer disabled">&nbsp;&nbsp;<i class="fas fa-arrow-right"></i>&nbsp;&nbsp;${question.answerContent }</span>
			<hr>	
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
	location.href = "${contextPath}/question/service/" + data;

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