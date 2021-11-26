<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
    <c:import url="../template/head-meta.jsp"/>
    <%--타이틀, 각 페이지따라 적절히 수정 (ex, 타요 - 멤버 프로필)--%>
    <title>타요 - 메타버스 코딩공간</title>
<script>
	$(function(){
		$("#findBtn").click(function(){
			$.ajax({
				url : "/findpw",
				type : "POST",
				data : {
					email : $("#email").val(),
					phone : $("#phone").val()
				},
				success : function(result) {
					location.href ="/";					
				},
			})
		});
	})
</script>
</head>
<body>
<c:import url="../template/header.jsp"/>

<%-- 각자의 내용 작성 all-shadow는 필요없을시 지우고 사용해도 무방 --%>
<section class="all-shadow">

	<div class="container">
		<div class="">
			<div class="">
				<h3>비밀번호 찾기</h3>
			</div>
			<div>
				<p>
					<label>이메일</label>
					<input class="email" type="text" id="email" name="email" placeholder="회원가입한 이메일주소를 입력하세요" required>
				</p>
				<p>
					<label>Phone</label>
					<input class="phone" type="text" id="phone" name="phone" placeholder="회원가입한 핸드폰 번호를 입력하세요" required>
				</p>
				<p class="">
					<div type="button" id="findBtn" class="findBtn tayo-button purple">찾기</div>
					<div type="button" onclick="history.go(-1);" class="login tayo-button purple">로그인으로</div>
				</p>
			</div>
		</div>
	</div>
</section>

<c:import url="../template/footer.jsp"/>
</body>
</html>