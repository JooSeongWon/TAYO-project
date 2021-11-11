<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
    <c:import url="../template/head-meta.jsp"/>
    
    <title>타요 - 공지사항</title>
<style type="text/css">

section {
    background-color: var(--color-white);
    margin-top: 20px;
    padding: 30px;
    width: 900px;
    height: 680px;
}

.noticeBox {
	width: 700px;
	hieght: 100px;
	margin-left: 40px;
	padding: 0px;
	background: #ffffff;
	border: 2px solid #ccc;
	text-align: center;
}

.date {
margin: 0px;
padding-left: 45px;
}

.text {
margin: 0px;
padding: 0px;
text-align: left;
}

.wrap {
    width: 800px;
    height: 550px;
    margin: 50ps auto;

    background: aqua;
    overflow-y: scroll;
}
</style>

<script type="text/javascript">

// $(document).ready(function(){
	
// 	$("#test").click(function() {
// 		console.log("clicked")
// 		$.ajax({
// 				type:"post",
// 				url:"/notice/notice",
// 				data: {},
// 				dataType: 'json',
// 				success: function(data){
// 					$('.data').test(data)
// 				},
// 				error: function(){
// 					alert("error")
// 				}
// 			}		
// 		)
// 	})
// })
</script>

</head>
<body>
<c:import url="../template/header.jsp"/>

<section class="all-shadow">
<button id="test">test</button>
    <h1 class="tayo-under-line" style="text-align: center">NOTICE</h3>
		<div class="wrap">
    <c:forEach items="${list }" var="notice">
			<span class="date">${notice.writeDate }</span><br>
			<div class="noticeBox">
			<p class="text"><textarea cols="95" rows="4" style="border: none">${notice.content }</textarea></p>
		</div> <!-- noticeBox -->
    </c:forEach>
		</div> <!-- wrap -->
</section>

<c:import url="../template/footer.jsp"/>
</body>
</html>