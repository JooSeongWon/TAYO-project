<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="board__title tayo-under-line">게시글 작성</div>
<i class="fas fa-arrow-left board__back-button"></i>
<i class="fas fa-times board__exit-button"></i>
<form action="" method="post" enctype="multipart/form-data" id="form">
    <div class="create__plan-date">
        일정 시작
        <input id="create__plan-date-first" type="date" name="planDateStart"> -
        일정 마감
        <input id="create__plan-date-second" type="date" name="planDateEnd">
    </div>
    <div class="create__header">
        <select id="create__post-category" name="category">
            <option value="1">Issue</option>
            <option value="2">Work Plan</option>
            <option value="3">QnA</option>
        </select>
        <input type="text" id="create__post-title" maxlength="30" placeholder="제목을 입력하세요." name="title" spellcheck="false">
    </div>
    <textarea id="create__post-content" cols="100" rows="30" style="width: 780px; height: 330px;" name="content"></textarea>
    <input type="file" id="create__post-file" name="file" onchange="checkSize(this)" accept="image/png, image/gif, image/jpeg">
</form>
<div class="tayo-button create__accept-btn">작성</div>