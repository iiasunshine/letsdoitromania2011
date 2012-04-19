<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:directive.include file="/WEB-INF/jspf/page-header.jspf" />
<f:view>
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:directive.include file="/WEB-INF/jspf/page-meta.jspf" />
<title>Let's do it Romania</title>
</head>
<body>
	<center>
		<%-- page Top --%>
		<h:panelGroup rendered="#{sessionScope['USER_DETAILS'] eq null}">
			<custom:page_top selected="home" />
		</h:panelGroup>
		<h:panelGroup rendered="#{not (sessionScope['USER_DETAILS'] eq null)}">
			<custom:page_top_login selected="home"
				role="${sessionScope['USER_DETAILS'].role}"
				county="${sessionScope['USER_DETAILS'].county}" />
		</h:panelGroup>

		<%-- page Content --%>
		<div id="pageContainer">
			<div id="content">
				<div id="leftColumn"></div>

				<div id="rightColumn" style="height: 700px;">
					<br /> <br />
					<h:panelGroup rendered="#{sessionScope['USER_DETAILS'] eq null}">

						<h2>
							<h:outputText
								value="Bine ai venit pe aplicatia Let`s Do It, Romania! 2012"
								escape="false" />
							<br /> <br />
						</h2>

						<div id="homepage-grid">
							<ul>
								<li><a
									href="${pageContext.servletContext.contextPath}/user-register.jsf"><img
										src="/icons/01-inscriere-utilizator.jpg" width="230"
										height="230" /></a></li>

								<li><a
									href="${pageContext.servletContext.contextPath}/users/free-cartare-mormane-editare.jsf"><img
										src="/icons/02-adaugare-gunoi.jpg" width="230" height="230" /></a>
								</li>
								<li><a
									href="${pageContext.servletContext.contextPath}/free-garbage-vote.jsf"><img
										src="/icons/03-votare-zona.jpg" width="230" height="230" /></a></li>

								<li>
									<div class="button-login-logout">
										<a
											href="${pageContext.servletContext.contextPath}/user-login.jsf"><img
											src="/icons/10-login.jpg" width="230" height="110" /></a>
									</div>
								</li>
							</ul>
						</div>


					</h:panelGroup>


					<h:panelGroup
						rendered="#{not (sessionScope['USER_DETAILS'] eq null)}">

						<h2>
							<h:outputText
								value="Bine ai venit pe aplicatia Let`s Do It, Romania! 2012"
								escape="false" />
							<br /> <br />
						</h2>
						<div id="homepage-grid">
							<ul>
								<li><a
									href="${pageContext.servletContext.contextPath}/users/cartare-mormane-lista.jsf"><img
										src="/icons/02-adaugare-gunoi.jpg" width="230" height="230" /></a></li>
								<li><a
									href="${pageContext.servletContext.contextPath}/users/garbage-vote.jsf"><img
										src="/icons/03-votare-zona.jpg" width="230" height="230" /></a></li>
								<c:if test="${not (sessionScope['USER_DETAILS'].role eq 'VOLUNTEER')}">
									<li><a href="${pageContext.servletContext.contextPath}/admin/admin-lista-mormane.jsf"><img src="/icons/05-listare-gunoi.jpg"
											width="230" height="230" /></a></li>
									<li><a href="${pageContext.servletContext.contextPath}/admin/admin-lista-voluntari.jsf"><img src="/icons/06-listare-useri.jpg"
											width="230" height="230" /></a></li>
								</c:if>
								
								<li>
									<div class="button-profile">
										<a href="${pageContext.servletContext.contextPath}/users/user-vizualizare.jsf"><img src="/icons/09-profil.jpg" width="230"
											height="110" /></a>
									</div>
									<div class="button-login-logout">
										<a href="${pageContext.servletContext.contextPath}/logout.jsf"><img src="/icons/10-logout.jpg" width="230"
											height="110" /></a>
									</div>
								</li>
							</ul>
						</div>

					</h:panelGroup>
					<br /> <br /><br /> <br /><br /> <br /><br /> <br /><br /> <br />
				</div>
			</div>
		</div>

		<%-- page Bottom --%>
		<custom:page_bottom />
	</center>
</body>
	</html>
</f:view>
