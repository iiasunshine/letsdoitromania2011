<%--
    Document   : page_header
    Created on : May 25, 2011, 2:37:55 PM
    Author     : dan.grigore
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="selected"%>
<%@attribute name="role"%>

<div id="headerContainer">
    <div id="header">
        <div id="logo"><a href="#"><img src="${pageContext.servletContext.contextPath}/layout/images/logo.jpg" width="200" height="90" /></a></div>
        <div id="topAdvertisement"><a href="#"><img src="${pageContext.servletContext.contextPath}/layout/images/banner_leaderboard_728x90px.gif" width="728" height="90" /></a></div>
        <div id="topMenu">
            <ul>
                <li><a href="${pageContext.servletContext.contextPath}/index.jsf" class="${selected eq 'home' ? 'active' : ''}"><span>${msg.meniu_home}</span></a></li>
             <%-- removed from 2012 ldir  <li><a href="${pageContext.servletContext.contextPath}/users/cartare-zone-lista.jsf" class="${selected eq 'zone' ? 'active' : ''}"><span>${msg.meniu_zona_cartare}</span></a></li>--%>
                <li><a href="${pageContext.servletContext.contextPath}/users/cartare-mormane-lista.jsf" class="${selected eq 'mormane' ? 'active' : ''}"><span>${msg.meniu_cartare_mormane}</span></a></li>
                <c:if test="${not (role eq 'VOLUNTEER')}">
                    <li><a href="${pageContext.servletContext.contextPath}/admin/admin-lista-voluntari.jsf" class="${selected eq 'lista_utilizatori' ? 'active' : ''}"><span>${msg.meniu_lista_utilizatori}</span></a></li>
                    <li><a href="${pageContext.servletContext.contextPath}/admin/admin-lista-mormane.jsf" class="${selected eq 'lista_mormane' ? 'active' : ''}"><span>${msg.meniu_lista_mormane}</span></a></li>
                </c:if>
                <li><a href="${pageContext.servletContext.contextPath}/users/user-vizualizare.jsf" class="${selected eq 'user_edit' ? 'active': ''}"><span>${msg.meniu_user_edit}</span></a></li>
                 <c:if test="${(role eq 'ORGANIZER')or (role eq 'ORGANIZER_MULTI')}">
                	<li><a href="${pageContext.servletContext.contextPath}/users/echipa-org-multi-editare.jsf" class="${selected eq 'echipa_edit' ? 'active': ''}"><span>${msg.meniu_echipa_edit}</span></a></li>
                 </c:if>
                 <c:if test="${(role eq 'ADMIN') or (role eq 'ORGANIZER') or (role eq 'VOLUNTEER')}">
                	<li><a href="${pageContext.servletContext.contextPath}/users/echipa-vizualizare.jsf" class="${selected eq 'echipa_edit' ? 'active': ''}"><span>${msg.meniu_echipa_edit}</span></a></li>
                 </c:if>
                 <c:if test="${true}">
                 <li><a href="${pageContext.servletContext.contextPath}/users/curatenie-vizualizare.jsf" class="${selected eq 'curatenie' ? 'active': ''}"><span>${msg.meniu_curatenie_list}</span></a></li>
                 </c:if>
                <li><a href="${pageContext.servletContext.contextPath}/logout.jsf"><span>${msg.meniu_logout}</span></a></li>
            </ul>
        </div>
    </div>
</div>