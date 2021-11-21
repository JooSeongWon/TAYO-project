<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
    <c:import url="../template/head-meta.jsp"/>
    <%--타이틀, 각 페이지따라 적절히 수정 (ex, 타요 - 멤버 프로필)--%>
    <title>타요 - 메타버스 코딩공간</title>
    <script src="${pageContext.request.contextPath}/resources/js/profile.js" defer></script>
</head>
<body>
<c:import url="../template/header.jsp"/>

<%-- 각자의 내용 작성 all-shadow는 필요없을시 지우고 사용해도 무방 --%>
<section class="all-shadow">

<h1>My profile</h1>
<hr>

<div id="profile">

${info.savedName}<br><br>

${info.email }<br><br>

<div id="name">
${info.name }
&nbsp;<i class="far fa-edit" onclick="fnIconClick('nameupdate')"></i>

<div class="nameupdate" ></div>
<br>

</div>

<div id="phone">
${info.phone }
&nbsp;<i class="far fa-edit" onclick="fnIconClick('phoneupdate')"></i>

<div class="phoneupdate" ></div>
<br>

</div>

<div id="passoword">
${info.password }&nbsp;<i class="far fa-edit" onclick="fnIconClick('passwordupdate')"></i>

<div class="passwordupdate" ></div>

</div>

</div>

</section>

<script type="text/javascript">

//아이콘 클릭 시 
var fnIconClick = function(result) {
	
	if ($("."+ result).html() == '') {
		var html = '';
		html += '<input type="text" >';
		
		$("." + result).append(html);
		
	}else{
		// 초기화
		$("."+ result).html('');
	}
	
}


$(document).ready(function() {
	
	//const profile = document.querySelectorAll('#profile');
	//const btn = document.querySelectorAll('.far fa-edit');
	
	
	
})

</script>

<c:import url="../template/footer.jsp"/>
</body>
</html>