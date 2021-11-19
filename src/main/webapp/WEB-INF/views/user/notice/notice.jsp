<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    width: 850px;
    height: 680px;
}

.noticeBox {
	width: 700px;
 	margin-left: 40px;
	margin-bottom: 30px;
	background: #ffffff;
	border: 2px solid #ccc;
	text-align: center;
}

.date {
padding-left: 45px;
font-weight: bold;
}


.wrap {
    width: 800px;
    height: 540px;
    margin: 50px auto;
    background: #ffffff;
    overflow-y: scroll;
	-ms-overflow-style: none;
}

.wrap::-webkit-scrollbar {
	display: none;
	width: 0 !important;
}

.no {
	float: right;
}

.tayo-under-line {
    border-bottom: 2px solid var(-color-light-grey);
    padding-bottom: 6px;
    margin: 0 auto;
    width: 350px;
}
</style>
<script type="text/javascript">

	let currPage = 1;
	let totalPage = ${paging.totalPage};

	$(document).ready(function(){

		const wrap = document.querySelector('.wrap'); //.wrap

		const options = {
			root: wrap,
			rootMargin : '0px',
			threshold: 0.5
		}; //wrap 옵션

		const observer = new IntersectionObserver(callback, options);

		function getNextNotices(){
			//현재 페이지가 전체페이지랑 같거나 클때 스탑
			if(currPage >= totalPage) {
				return;
			}

			//페이징 받아오기 ajax
			//다음페이지 불러오기
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

		//다음 페이지로 부를 공지사항 정보들
		function appendChildren(noticeList) {
			for(let notice of noticeList){
				
				//div태그인 child 생성
				const child = document.createElement('div');

				//child에 class="child" 추가
				child.classList.add('child');

				//child에 HTML(공지사항)정보 추가
				child.innerHTML = `<span class="date">\${notice.writeDate }</span>
									<div class="noticeBox">
									<p class="text">\${notice.content }</p>
									</div>`;
				//wrap에 .child 추가
				wrap.appendChild(child);
			}

			setObserve();
		}


		function callback(entries){ 
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
				
				<span class="date"><fmt:formatDate value="${notice.writeDate}" pattern="yyyy-MM-dd"/></span>
				<div class="noticeBox">
					<p class="text">${notice.content }</p>
				</div> <!-- noticeBox -->
				
			</div> <!-- child -->

  		  </c:forEach>
		</div> <!-- wrap -->
</section>

<%-- <c:import url="../notice/paging.jsp"/> --%>
<c:import url="../template/footer.jsp"/>
</body>
</html>