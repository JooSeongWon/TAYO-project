<%--
  Created by IntelliJ IDEA.
  User: parkjungtae
  Date: 2021-11-09
  Time: 오전 11:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header>
    <c:if test="${not empty loginMember}">
        <i class="fas fa-bars bars-button"></i>
        <ul class="header-nav">
            <li class="header-nav__item <c:if test="${not empty cafe}">active</c:if>"><a href="${pageContext.request.contextPath}/work-spaces">Workspace</a></li>
            <li class="header-nav__item <c:if test="${not empty profile}">active</c:if>"><a href="${pageContext.request.contextPath}/profile">Profile</a></li>
            <li class="header-nav__item <c:if test="${not empty notice}">active</c:if>"><a href="${pageContext.request.contextPath}/notice">Notice</a></li>
            <li class="header-nav__item <c:if test="${not empty question}">active</c:if>"><a href="${pageContext.request.contextPath}/question">Question</a></li>
            <c:if test="${loginMember.isAdmin()}">
                <li class="header-nav__item"><a href="${pageContext.request.contextPath}/admin">Admin</a></li>
            </c:if>
        </ul>
    </c:if>
    <c:if test="${empty loginMember}">
        <div style="width: 10px"></div>
    </c:if>

    <div class="control-box">
        <i class="fas fa-home control-box__icon home"></i>
        <c:if test="${not empty loginMember}">
            <div class="control-box__icon exit">
                <i class="fas fa-door-open exit__icon"></i>
                <span class="exit__description">sign out</span>
            </div>
        </c:if>
    </div>
</header>
<div class="page-content">
