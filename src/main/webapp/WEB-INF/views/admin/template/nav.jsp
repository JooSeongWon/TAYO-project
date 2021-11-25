<%--
  Created by IntelliJ IDEA.
  User: parkjungtae
  Date: 2021-11-09
  Time: 오전 11:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<nav>
    <ul class="menu-list">
        <li class="list__member"><a href="${pageContext.request.contextPath}/admin/members"><i class="fas fa-user"></i></a></li>
        <li class="list__chat"><a href="${pageContext.request.contextPath}/admin/question/service/open"><i class="fas fa-comments"></i></a></li>
        <li class="list__question"><a href="${pageContext.request.contextPath}/admin/question"><i class="fas fa-question-circle"></i></a></li>
        <li class="list__notice"><a href="${pageContext.request.contextPath}/admin/notice"><i class="fas fa-bell"></i></a></li>
    </ul>
</nav>
<a class="exit-admin" href="${pageContext.request.contextPath}/"><i class="fas fa-sign-out-alt"></i></a>
