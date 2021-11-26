<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:if test="${empty board.planDate}">
    <div class="board__title tayo-under-line">게시글 수정</div>
</c:if>

<c:if test="${not empty board.planDate}">
    <div class="detail__title tayo-under-line">게시글 수정</div>
</c:if>
<i class="fas fa-arrow-left post__back-button"></i>
<i class="fas fa-times board__exit-button"></i>
<div id="form">
    <c:if test="${not empty board.planDate}">
        <div class="create__plan-date active">
            일정 시작
            <input id="create__plan-date-first" type="date" name="planDateStart" value="${board.planStartDate}"> -
            일정 마감
            <input id="create__plan-date-second" type="date" name="planDateEnd" value="${board.planEndDate}">
        </div>
    </c:if>
    <div class="create__header">
        <input type="text" id="create__post-title" maxlength="30" placeholder="제목을 입력하세요."
               name="title" value="${board.title}" spellcheck="false">
    </div>
    <textarea id="create__post-content" cols="100" rows="30" style="width: 780px; height: 330px;"
              name="content">${board.content}</textarea>
    <c:if test="${not empty board.uploadFileId}">
        <div class="update__post-old-file">기존 첨부파일 : ${board.uploadFileName}</div>
    </c:if>
    <input type="file" id="create__post-file" name="file" onchange="checkSize(this)"
           accept="image/png, image/gif, image/jpeg">
</div>
<div class="tayo-button create__accept-btn">수정</div>