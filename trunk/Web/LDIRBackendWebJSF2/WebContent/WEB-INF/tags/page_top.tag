<%-- 
    Document   : page_header
    Created on : May 25, 2011, 2:37:55 PM
    Author     : dan.grigore
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@tag isELIgnored="false" body-content="tagdependent"%>
<%@attribute name="selected" %>

<div id="headerContainer">
    <div id="header">
        <div id="logo"><a href="#"><img src="${pageContext.servletContext.contextPath}/layout/images/logo.jpg" width="200" height="90" /></a></div>
        <div id="topAdvertisement"><a href="#"><img src="${pageContext.servletContext.contextPath}/layout/images/banner_leaderboard_728x90px.gif" width="728" height="90" /></a></div>
        <div id="topMenu">
            <ul>
                <li><a href="${pageContext.servletContext.contextPath}/index.jsf"
                       class="${selected eq 'home' ? 'active' : ''}"><span>${msg.meniu_home}</span></a></li>
              <%--   <li><a href="${pageContext.servletContext.contextPath}/user-login.jsf"
                       class="${selected eq 'login' ? 'active' : ''}"><span>${msg.meniu_login}</span></a></li>
                <li><a href="${pageContext.servletContext.contextPath}/user-register.jsf"
                       class="${selected eq 'register' ? 'active' : ''}"><span>${msg.meniu_inscriere_voluntar}</span></a></li> --%>
                <li><a href="${pageContext.servletContext.contextPath}/users/free-cartare-mormane-editare.jsf" class="${selected eq 'mormane' ? 'active' : ''}"><span>${msg.meniu_cartare_mormane}</span></a></li>
                <li><a href="${pageContext.servletContext.contextPath}/map-view.jsf" 
                       class="${selected eq 'view' ? 'active' : ''}"><span>${msg.meniu_harta}</span></a></li>
            </ul>
        </div>
    </div>
</div>