<%-- 
    Document   : page_redirect
    Created on : Jun 10, 2011, 2:47:33 PM
    Author     : dan.grigore
--%>
<%@attribute name="target" %>
<%
            response.sendRedirect(request.getContextPath() + target);
            return;
%>