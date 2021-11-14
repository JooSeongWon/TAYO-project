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
    height: 540px;
    margin: 50ps auto;

    background: aqua;
    overflow-y: scroll;
}

.no {
	float: right;
}
.child {
}
</style>

</head>
<body>
<c:import url="../template/header.jsp"/>

<section class="all-shadow">
<button id="test">test</button>
    <h1 class="tayo-under-line" style="text-align: center">NOTICE</h3>
		<div class="wrap">
  		  <c:forEach items="${list }" var="notice">
			<div class="child">
			<span class="date">${notice.writeDate }</span>
			<span class="no" data-no="${notice.id }">${notice.id }</span><br>
			<div class="noticeBox">
			<p class="text"><textarea cols="95" rows="4" style="border: none">${notice.content }</textarea></p>
			</div> <!-- noticeBox -->
			</div> <!-- child -->
  		  </c:forEach>
		</div> <!-- wrap -->
</section>
<script type="text/javascript">

$(document).ready(function(){
	
	$("#test").click(function() {
		console.log("clicked")
		$.ajax({
				type:"post",
				url:"/notice/notice",
				data: {},
				dataType: 'json',
				success: function(data){
					$('.data').text(data)
				},
				error: function(){
					alert("error")
				}
			}		
		)
	})
})
const wrap = document.querySelector('.wrap'); //.wrap 

const options = {
    root: wrap,
    rootMargin : '0px',
    threshold: 1
}; //wrap 옵션

const observer = new IntersectionObserver(callback, options);

addChildrean();

function addChildrean(){

    for(let i = 0; i < 15; i++){ 
        const child = document.createElement('div'); //child(div)
        child.classList.add('child'); //childList생성

        wrap.appendChild(child); //wrap맨밑에 child추가

        child.innerText = i+1; //수 세기
     }

    let lastChild = document.querySelector('.child:last-child') //마지막 child
    observer.observe(lastChild); 
}




function callback(entries){ //마지막공지가 사라지면 0.5초후
    if(entries[0].isIntersecting){
        observer.unobserve(entries[0].target);
        setTimeout(addChildrean, 500);
    }

}
</script>
<c:import url="../notice/paging.jsp"/>
<c:import url="../template/footer.jsp"/>
</body>
</html>