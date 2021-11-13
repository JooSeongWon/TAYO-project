<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
    <c:import url="../template/head-meta.jsp"/>
    <%--타이틀, 각 페이지따라 적절히 수정 (ex, 타요 - 멤버 프로필)--%>
    <title>타요 - 회원가입</title>

    
</head>
<body>
<c:import url="../template/header.jsp"/>

<%-- 각자의 내용 작성 fixed-center 나 all-shadow는 필요없을시 지우고 사용해도 무방 --%>
<section class="all-shadow fixed-center">

<form action="/join" method="post">

<label>USER NAME </label><br>
<input type="text" name="name" class="tayo-input" placeholder="USER NAME" /><br><br>

<label>Email </label><br>
<input type="text" name="email" class="tayo-input" placeholder="Email" /><br><br>

<label>Phone</label><br>
<input type="text" name="phone" class="tayo-input" placeholder="Phone" /><br><br>

<label>Password</label><br>
<input type="password" name="password" class="tayo-input" placeholder="Password" /><br><br>

<label>Confirm</label><br>
<input type="password" name="passwoed" class="tayo-input" placeholder="Password" /><br><br>

<button>회원가입</button>

</form>


</section>

<c:import url="../template/footer.jsp"/>
</body>
</html>