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
    margin: 50px auto;

    background: aqua;
    overflow-y: scroll;
}

.no {
	float: right;
}
.child {
}
</style>
<script type="text/javascript">

	let currPage = 1;
	let totalPage = ${paging.totalPage};

	$(document).ready(function(){

// 		$("#test").click(function() {
// 			console.log("clicked")
// 			$.ajax({
// 						type:"post",
// 						url:"/notice/notice",
// 						data: {},
// 						dataType: 'json',
// 						success: function(data){
// 							$('.data').text(data)
// 						},
// 						error: function(){
// 							alert("error")
// 						}
// 					}
// 			)
// 		});


		const wrap = document.querySelector('.wrap'); //.wrap

		const options = {
			root: wrap,
			rootMargin : '0px',
			threshold: 1
		}; //wrap 옵션

		const observer = new IntersectionObserver(callback, options);

		function getNextNotices(){
			if(currPage >= totalPage) {
				return;
			}

			//페이징 받아오기 ajax
			$.ajax({
						type:"post",
						url:"/notice",
						data: {curPage: ++currPage},
						dataType: 'json',
						success: appendChildren,
						error: console.log
					}
			)

		}

		function appendChildren(noticeList) {
			for(let notice of noticeList){

				const child = document.createElement('div');
				child.classList.add('child');

				child.innerHTML = `<span class="date">\${notice.writeDate }</span>
									<span class="no" data-no="\${notice.id }">\${notice.id }</span><br>
									<div class="noticeBox">
									<p class="text"><textarea cols="95" rows="4" style="border: none">\${notice.content }</textarea></p>
									</div>`;
				wrap.appendChild(child);
			}

			setObserve();
		}


		function callback(entries){ //마지막공지가 사라지면 0.5초후
			if(entries[0].isIntersecting){
				observer.unobserve(entries[0].target);
				getNextNotices();
			}

		}

		function setObserve() {
			let lastChild = document.querySelector('.child:last-child'); //마지막 child
			observer.observe(lastChild);
		}


		setObserve();
	});

</script>

</head>
<body>
<c:import url="../template/header.jsp"/>

<section class="all-shadow">
    <h1 class="tayo-under-line" style="text-align: center">NOTICE</h1>
		<div class="wrap tayo-scroll-bar">
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

<%-- <c:import url="../notice/paging.jsp"/> --%>
<c:import url="../template/footer.jsp"/>
</body>
</html>