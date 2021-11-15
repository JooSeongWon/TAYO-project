<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="ko">
<head>
    <c:import url="../template/head-meta.jsp"/>
<%--     <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/work-space-list-style.css "> --%>
    <%--타이틀, 각 페이지따라 적절히 수정 (ex, 관리자 - 회원관리)--%>
    <title>관리자</title>
</head>
<body>
<c:import url="../template/nav.jsp"/>
<%-- 각자의 내용 작성 --%>
<section>

<div class="container">

<h1>회원관리</h1>
<hr>

<table>
<thead>
	<tr>
		<th style="width: 10%;">No.</th>
		<th style="width: 45%;">Name</th>
		<th style="width: 20%;">Email</th>
		<th style="width: 10%;">Phone</th>
		<th style="width: 15%;">가입일</th>
		<th style="width: 15%;">권한</th>
		<th style="width: 15%;">회원제재</th>
	</tr>
</thead>
<tbody>
<c:forEach items="${list }" var="member">
	<tr>
		<td>${member.id }</td>
		<td><a href="/admin/view?memberId=${member.id }">${member.name }</a></td>
		<td>${member.email }</td>
		<td>${member.phone }</td>
		<td><fmt:formatDate value="${member.createDate }" pattern="yy-MM-dd"/></td>
<%-- 		<td><fmt:formatDate value="${member.createDate }" pattern="yy-MM-dd HH:mm:ss"/></td> --%>
		<td>${member.grade }</td>
		<td>${member.ban }</td>
	</tr>
</c:forEach>
</tbody>
</table>
</div>

<c:import url="/WEB-INF/views/admin/member/paging.jsp"></c:import>

</section>
</body>
</html>