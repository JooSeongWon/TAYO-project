<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<script src="${pageContext.request.contextPath}/resources/js/jquery-2.2.4.min.js" ></script>

<script>
$(document).ready(function(){
	$("#chatBtn").click( function() {
					console.log("Btn")

		$.ajax({
			url : "${contextPath}/question/service/open",
			data : {},
			type : "POST",
			dataType : "JSON",
			success : function(data) {
				console.log(data)
				location.href = "${contextPath}/question/service/" + data;

			},
			error : function() {
				console.log("openChat 메소드 실패");
			}
		});
	});
});
</script>

</head>
<body>

<button id="chatBtn">이양</button>

</body>
</html>