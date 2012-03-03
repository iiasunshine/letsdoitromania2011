<%
			request.logout();
            session.invalidate();
            response.sendRedirect("user-login.jsf");
%>
