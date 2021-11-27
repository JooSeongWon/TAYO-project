<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html lang="ko">
<head>
    <c:import url="../template/head-meta.jsp"/>
    <%--타이틀, 각 페이지따라 적절히 수정 (ex, 타요 - 멤버 프로필)--%>
    <title>타요 - 메타버스 코딩공간</title>
</head>
<body>
<c:import url="../template/header.jsp"/>

<section class="all-shadow">

  <script type="text/javascript">
    var userEmail = '${email}';

    alert(userEmail + '님 회원가입을 축하합니다. 이제 로그인이 가능 합니다.');
    
    window.open('', '_self', ''); 
    
    self.location = '/login';
  </script>
  	
</section>

<c:import url="../template/footer.jsp"/>
</body>
</html>