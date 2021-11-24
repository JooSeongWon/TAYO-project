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
	</tr>
</thead>
<tbody>

<c:forEach items="${list }" var="member">
	<tr>
		<td>${member.id }</td>
		<td>${member.name }</td>
		<td>${member.email }</td>
		<td>${member.phone }</td>
		<td><fmt:formatDate value="${member.createDate }" pattern="yy-MM-dd"/></td>
		<td>
			<c:if test="${loginMember.grade eq 'S'.charAt(0)}"> 
				<select id="${member.id }" class="grade">
					<option value="N" <c:if test="${fn:contains(member.grade,'N')}">selected="selected"</c:if>>일반회원</option>
					<option value="A" <c:if test="${fn:contains(member.grade,'A')}">selected="selected"</c:if>>관리자</option>
					<option value="S" <c:if test="${fn:contains(member.grade,'S')}">selected="selected"</c:if>>최고관리자</option>
				</select>
			</c:if>
			
			<c:if test="${loginMember.grade eq 'A'.charAt(0)}">
				<c:if test="${fn:contains(member.grade,'N')}">일반회원</c:if>
				<c:if test="${fn:contains(member.grade,'A')}">관리자</c:if>
				<c:if test="${fn:contains(member.grade,'S')}">최고관리자</c:if>
			</c:if>
		</td>
		
		<td>
			<select id="${member.id }" class="ban">
				<option value="N" <c:if test="${fn:contains(member.ban, 'N')}">selected="selected"</c:if>>정상</option>
				<option value="Y" <c:if test="${fn:contains(member.ban, 'Y')}">selected="selected"</c:if>>이용정지</option>
			</select>
		</td>
	</tr>

		
</c:forEach>
</tbody>
</table>

<c:import url="/WEB-INF/views/admin/member/paging.jsp"></c:import>

<div class="form-inline text-center">
	<i class="fas fa-search"></i>
	<input class="form-control" type="text" id="search" value="${param.search }" placeholder="Name" onkeyup="enterkey()"/>
	<button id="btnSearch">검색</button>
</div>

</section>
</body>
</html>