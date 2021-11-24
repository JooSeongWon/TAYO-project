<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="board__title tayo-under-line">${category}</div>
<i class="fas fa-pen-square board__insert-button"></i>
<i class="fas fa-times board__exit-button"></i>
<div class="post-title">
    <div class="post-title__no">번호</div>
    <c:if test="${category eq 'Work plan'}">
        <div class="post-title__date">일정</div>
    </c:if>
    <c:if test="${category ne 'Work plan'}">
    <div class="post-title__date">작성일</div>
    </c:if>
    <div class="post-title__title">제목</div>
    <div class="post-title__writer">작성자</div>
</div>
<div class="board__list tayo-scroll-bar">
    <c:forEach items="${boardList}" var="post" varStatus="i">
        <div class="post" onclick="displayPost(${post.id}<c:if test="${not post.read}">,true,${categoryId}</c:if>)" >
            <c:if test="${not post.read}">
                <div class="new-post">N</div>
            </c:if>
            <div class="post__no">${paging.totalCount - (post.rowNumber-1)}</div>
            <c:if test="${empty post.planDate}">
                <jsp:useBean id="now" class="java.util.Date"/>
                <fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="today"/>
                <fmt:formatDate value="${post.writeDate}" pattern="yyyy-MM-dd" var="wirteDate"/>

                <c:if test="${today eq wirteDate}">
                    <div class="post__date"><fmt:formatDate value="${post.writeDate}" pattern="HH:mm"/></div>
                </c:if>
                <c:if test="${today ne wirteDate}">
                    <div class="post__date"><fmt:formatDate value="${post.writeDate}" pattern="MM/dd"/></div>
                </c:if>
            </c:if>
            <c:if test="${not empty post.planDate}">
                <div class="post__date">${post.planDate}</div>
            </c:if>
            <div class="post__title">${post.title}<c:if test="${not empty post.commentsCnt}"><span>(${post.commentsCnt})</span></c:if></div>
            <div class="post__writer">
                <c:if test="${empty post.member.profile}">
                    <img src="/resources/img/no-profile.png" alt="프로필" class="writer__img">
                </c:if>
                <c:if test="${not empty post.member.profile}">
                    <img src="/img/${post.member.profile}" alt="프로필" class="writer__img">
                </c:if>
                <div class="writer__name">${post.member}</div>
            </div>
        </div>
    </c:forEach>
    <c:if test="${empty boardList}">
        <div style="text-align: center">게시글이 없습니다.</div>
    </c:if>
</div>
<div class="board__page">
    <c:if test="${paging.totalPage eq 0}">
        <div class="page__no current-page">1</div>
    </c:if>
    <c:if test="${paging.totalPage ne 0}">
        <i class="fas fa-chevron-left page__lt <c:if test="${paging.curPage >= 6}">active"
           onclick= "displayBoard(${categoryId}, ${paging.startPage - 1})</c:if>"></i>
        <c:forEach begin="${paging.startPage}" end="${paging.endPage}" var="i">
            <div class="page__no <c:if test="${i eq paging.curPage}">current-page</c:if>" <c:if test="${i ne paging.curPage}">onclick= "displayBoard(${categoryId}, ${i})"</c:if>>${i}</div>
        </c:forEach>
        <i class="fas fa-chevron-right page__gt <c:if test="${paging.totalPage > paging.endPage}">active"
           onclick= "displayBoard(${categoryId}, ${paging.endPage + 1})</c:if>"></i>
    </c:if>
</div>