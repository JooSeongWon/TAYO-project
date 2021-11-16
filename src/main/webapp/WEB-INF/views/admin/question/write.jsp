<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
    <c:import url="../template/head-meta.jsp"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin-question.css"/>
    <script src="${pageContext.request.contextPath}/resources/js/admin-question.js" defer></script>
    <title>관리자 - 자주묻는 질문 글쓰기</title>
</head>
<body>
<c:import url="../template/nav.jsp"/>
<section>
<h1>자주묻는 질문 작성</h1>
<hr>

<form action="/admin/question/write" method="post">
<!-- <div class="container"> -->
	<div>
		<label for="question">질문</label>
		<input type="text" id="questuon" name="questionContent">
	</div>
	<div>
		<label for="answer">답변</label>
		<textarea rows="5" id="answer" name="answerContent"></textarea>
	</div>
</form>
	
	<div>
	<button id="btnWrite">작성</button>
	<button id="cancel">취소</button>
	</div>
<!-- </div> .container -->

</section>
</body>
</html>