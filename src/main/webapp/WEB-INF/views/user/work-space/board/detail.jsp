<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="detail__title tayo-under-line">${board.title}</div>
<i class="fas fa-arrow-left board__back-button"></i>
<i class="fas fa-times board__exit-button"></i>
<div class="detail-date">
    <c:if test="${empty board.planDate}"><fmt:formatDate value="${board.writeDate}" pattern="yyyy-MM-dd HH:mm"/></c:if>
    <c:if test="${not empty board.planDate}">${board.planDate}</c:if>
</div>
<div class="detail-content tayo-scroll-bar">
    <c:if test="${not empty board.uploadFileId}">
        <img src="/img/${board.uploadFileId}" alt="사진" class="board-file">
    </c:if>
    ${board.content}
</div>
<div class="control-panel tayo-under-line">
    <c:if test="${loginMember.id eq board.member.id}">
        <div class="control-buttons">
            <i class="fas fa-edit control-edit-btn"></i>
            <i class="fas fa-trash-alt control-delete-btn"></i>
        </div>
    </c:if>
    <div class="control-profile">
        <c:if test="${empty board.member.profile}">
            <img src="/resources/img/no-profile.png" alt="프로필"
                 class="control-profile__img">
        </c:if>
        <c:if test="${not empty board.member.profile}">
            <img src="/img/${board.member.profile}" alt="프로필"
                 class="control-profile__img">
        </c:if>
        <div class="control-profile__name">${board.member}</div>
    </div>
</div>
<div class="comments-list tayo-scroll-bar tayo-under-line">
    <c:forEach items="${board.commentsList}" var="comments">
        <div class="comments">
            <div class="comments__content">${comments.content}</div>
            <div class="comments-info">
                <div class="comments-profile">
                    <c:if test="${empty comments.member.profile}">
                        <img src="/resources/img/no-profile.png" alt="프로필" class="comments-profile__img">
                    </c:if>
                    <c:if test="${not empty comments.member.profile}">
                        <img src="/img/${comments.member.profile}" alt="프로필" class="comments-profile__img">
                    </c:if>
                    <div class="comments-profile__name">${comments.member}</div>
                </div>
                <div class="comments-write-date">
                    <fmt:formatDate value="${comments.writeDate}" pattern="MM/dd HH:mm"/>
                </div>
            </div>
            <c:if test="${comments.member.id eq loginMember.id}">
                <i class="fas fa-trash-alt comments-delete-btn" onclick="deleteComments(${comments.id}, this)"></i>
            </c:if>
        </div>
    </c:forEach>
    <c:if test="${empty board.commentsList}">
        <div class="no-comments">댓글이 없습니다.</div>
    </c:if>
</div>
<div class="comments-input-panel">
    <input type="text" class="comments-input-box" placeholder="내용을 입력하세요." maxlength="100">
    <div class="comments-input-btn">작성</div>
</div>