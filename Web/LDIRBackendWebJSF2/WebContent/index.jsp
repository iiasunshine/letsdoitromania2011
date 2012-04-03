<%--
    Document   : logout
    Created on : 01.10.2010, 14:06:37
    Author     : Administrator
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="customfn" uri="/WEB-INF/tld/custom-fn.tld" %>
<%@taglib prefix="custom" tagdir="/WEB-INF/tags/" %>

<%@page contentType="text/html" pageEncoding="windows-1250"%>

<c:if test="${not (sessionScope['USER_DETAILS'] eq null)}">
<%
response.sendRedirect("home.jsf");%>
</c:if>
<c:if test="${(sessionScope['USER_DETAILS'] eq null)}">
<%
response.sendRedirect("user-login.jsf");
%>
</c:if>