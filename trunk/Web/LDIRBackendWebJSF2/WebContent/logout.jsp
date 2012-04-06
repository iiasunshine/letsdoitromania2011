<%
			request.logout();
			session.removeAttribute("userSessionRecord");
            session.invalidate();
            response.sendRedirect("user-login.jsf");
%>
