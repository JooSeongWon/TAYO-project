<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
    <c:import url="../template/head-meta.jsp"/>

	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin-question.css"/>
    <script src="${pageContext.request.contextPath}/resources/js/admin-question.js" defer></script>
    <title>관리자 - 자주묻는 질문</title>
</head>
<body>
<c:import url="../template/nav.jsp"/>
<%-- 각자의 내용 작성 --%>
<section>
	<h1>자주묻는 질문 </h1>
	<button id="wirteBtn">작성</button>
	
	<div class="wrap tayo-scroll-bar">
	  <c:forEach items="${list }" var="question">
		
		<a class="updateBtn" href="${pageContext.request.contextPath }/admin/question/update/${question.id}"><i id="updateBtn" class="fas fa-edit fa-2x"></i></a>
		<div class="questionBox">
			<span class="question">
				<i id="q" class="fab fa-quora fa-2x"></i>
					<span>${question.questionContent }</span>
			</span>
		</div> <!-- questionBox -->
		
		<a class="deleteBtn" href="${pageContext.request.contextPath }/admin/question/delete/${question.id}"><i id="deleteBtn" class="fas fa-eraser fa-2x"></i></a>
		<div class="questionBox">
			<span class="answer">
				<i id="a" class="fas fa-font fa-2x"></i>
					<span>${question.answerContent }</span>
			</span>
		</div>	<!-- questionBox -->
		
		<div class="interval"></div>
	  </c:forEach>
	</div> <!-- wrap -->
	
</section>
</body>
</html>