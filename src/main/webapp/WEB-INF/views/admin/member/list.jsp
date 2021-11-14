<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
    <c:import url="../template/head-meta.jsp"/>
    <%--타이틀, 각 페이지따라 적절히 수정 (ex, 관리자 - 회원관리)--%>
    <title>관리자</title>
</head>
<body>
<c:import url="../template/nav.jsp"/>
<%-- 각자의 내용 작성 --%>
<section>
<h1>회원관리</h1>

<%-- <c:forEach items="${list }" var="member"> --%>
<!-- 	<tr> -->
<%-- 		<td>${member.memberId }</td> --%>
<%-- 		<td><a href="/board/view?boardNo=${member.memberId }">${board.title }</a></td> --%>
<%-- 		<td>${board.writerNick }</td> --%>
<%-- 		<td>${board.hit }</td> --%>
<%-- 		<td><fmt:formatDate value="${board.writeDate }" pattern="yy-MM-dd HH:mm:ss"/></td> --%>
<!-- 	</tr> -->
<%-- </c:forEach> --%>

</section>
</body>
</html>