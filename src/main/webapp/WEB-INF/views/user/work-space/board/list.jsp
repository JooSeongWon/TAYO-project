<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:forEach items="${boardList}" var="post" varStatus="i">
    <div class="post" data-post-no="${post.id}">
        <c:if test="${not post.read}">
            <div class="new-post">N</div>
        </c:if>
        <div class="post__no">${paging.totalCount - (post.rowNumber-1)}</div>
        <c:if test="${empty post.planDate}">
            <jsp:useBean id="now" class="java.util.Date" />
            <fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="today" />
            <fmt:formatDate value="${post.writeDate}" pattern="yyyy-MM-dd" var="wirteDate" />

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
        <div class="post__title">${post.title}</div>
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
<div class="page">
    <c:forEach begin="${paging.startPage}" end="${paging.endPage}" var="i">
        <div class="page-no" data-page-no="${i}">${i}</div>
    </c:forEach>
</div>