<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!doctype html>
<html lang="ko">
<head>
    <c:import url="../template/head-meta.jsp"/>
   
   	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin-members-style.css"/>
    <script src="${pageContext.request.contextPath}/resources/js/admin-members-script.js" defer></script>
   
    <title>관리자 - 회원관리</title>
</head>
<%-- 각자의 내용 작성 --%>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>

<body>
<c:import url="../template/nav.jsp"/>

<section class="container text-center">

<!-- <div class="container text-center"> -->

<h1>회원관리</h1>

<hr>

<table class=" table text-center">
<thead> 
	<tr>
		<th style="width: 10%;">No.</th>
		<th style="width: 10%;">Name</th>
		<th style="width: 25%;">Email</th>
		<th style="width: 20%;">Phone</th>
		<th style="width: 15%;">가입일</th>
		<th style="width: 10%;">권한</th>
		<th style="width: 10%; text-align: auto;">회원제재</th>
		<th><button class="change">변경</button></th>
	</tr>
</thead>
<tbody>

<form action="${pageContext.request.contextPath}/admin/members/${member.id}" method="post">
<c:forEach items="${list }" var="member">
	<tr>
		<td>${member.id }</td>
		<td>${member.name }</td>
		<td>${member.email }</td>
		<td>${member.phone }</td>
		<td><fmt:formatDate value="${member.createDate }" pattern="yy-MM-dd"/></td>
<%-- 		<td><fmt:formatDate value="${member.createDate }" pattern="yy-MM-dd HH:mm:ss"/></td> --%>

<%-- 		<td>${member.grade }</td> --%>
<%-- 		<td>${member.ban }</td> --%>
		<td>
		<select name="grade">
			<option value="N">일반회원</option>
			<option value="A">관리자</option>
			<option value="S">최고관리자</option>
		</select>
			
			<c:choose>
				<c:when test="${fn:contains(member.grade,'N')}">
					<option value="N" selected>N</option>
				</c:when>
				
				<c:when test="${fn:contains(member.grade,'A')}">
					<option value="A" selected>A</option>
				</c:when>

				<c:when test="${fn:contains(member.grade,'S')}">
					<option value="S" selected>S</option>
				</c:when>
			</c:choose>

		</td>



<!-- 		<td> -->
<!-- 			<select name="grade"> -->
<!-- 				<option value="N"> -->
<%-- 					<c:choose> --%>
<%-- 						<c:when test="${member.grade eq 'N' }"> --%>
<!-- 							<option value="N" selected>일반회원</option> -->
<%-- 						</c:when> --%>
<%-- 					</c:choose> --%>
<!-- 				</option> -->
<!-- 			</select> -->
<!-- 		</td> -->
	
<!-- 		<td> -->
<%-- 			<c:choose> --%>
<%-- 				<c:when test="${member.grade eq 'N'}">일반회원</c:when> --%>
<%-- 				<c:when test="${member.grade eq 'A'}">관리자</c:when> --%>
<%-- 				<c:when test="${member.grade eq 'S'}">최고관리자</c:when> --%>
<%-- 			</c:choose> --%>
			
<!-- 		</td> -->
		
		<td>${member.ban }</td>
	</tr>
</c:forEach>
</form>
</tbody>
</table>

<!-- <div class="float-right"> -->
<!-- 	<button>변경</button> -->
<!-- </div> -->

<c:import url="/WEB-INF/views/admin/member/paging.jsp"></c:import>

</section>
</body>
</html>