<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
    <c:import url="../template/head-meta.jsp"/>
 	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin-notice-style.css"/>
    <script src="${pageContext.request.contextPath}/resources/js/admin-notice-script.js" defer></script>
    <title>관리자 - 공지사항 글쓰기</title>
</head>
<body>
<c:import url="../template/nav.jsp"/>
<section>
<h1>공지사항 작성</h1>
<hr>

<form action="/admin/notice/write" method="post">
	<div>
		<label for="content">내용</label>
		<textarea rows="5" id="content" name="content"></textarea>
	</div>
</form>
	
	<div>
	<button id="btnWrite">작성</button>
	<button id="cancel">취소</button>
	</div>

</section>
</body>
</html>