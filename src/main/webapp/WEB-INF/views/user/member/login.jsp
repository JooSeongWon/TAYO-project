<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
    <c:import url="../template/head-meta.jsp"/>
    <%--타이틀, 각 페이지따라 적절히 수정 (ex, 타요 - 멤버 프로필)--%>
    <title>타요 - 로그인</title>
</head>
<body>
<c:import url="../template/header.jsp"/>

<%-- 각자의 내용 작성 fixed-center 나 all-shadow는 필요없을시 지우고 사용해도 무방 --%>
<section class="all-shadow fixed-center">

<div>
<form action="/member/login" method="post" >
<div class="form-group">
    <label for="email" >아이디 : </label>
    <div>
      <input type="text" id="email" name="email" placeholder="email">
    </div>
</div>
<div class="form-group">
    <label for="password" >패스워드 : </label>
    <div>
      <input type="text" id="password" name="password" placeholder="password">
    </div>
</div>
<div>
    <div>
      <button>로그인</button>
    </div>
</div>
</form>
</div>


</section>

<c:import url="../template/footer.jsp"/>
</body>
</html>