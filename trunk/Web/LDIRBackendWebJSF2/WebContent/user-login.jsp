<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:directive.include file="/WEB-INF/jspf/page-header.jspf" />
<jsp:directive.include file="/WEB-INF/jspf/notlogin-checkpoint.jspf" />
<f:view>
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:directive.include file="/WEB-INF/jspf/page-meta.jspf" />
<title>Let's do it Romania</title>
</head>
<body>
	<center>
		<%-- page Top --%>
		<custom:page_top selected="login" />

		<%-- page Content --%>
		<div id="pageContainer">
			<div id="content">
				<div id="leftColumn">
					<h:panelGroup rendered="#{(sessionScope['USER_DETAILS'] eq null)}">
						<div class="labelLeft">
							<h1>
								<h:outputText value="#{msg.login_title}" />
							</h1>
						</div>
						<br />
						<!---form  name="frmLogin" class="form" method="post" action="j_security_check"-->
						<h:form styleClass="form">
							<div class="labelLeft">
								<h:outputText value="#{msg.login_mail}" />
							</div>
							<!--input id="email" type="text" name="j_username" class="formTextfieldLeft"/-->
							<h:inputText value="#{loginBean.loginMail}" id="email"
								styleClass="formTextfieldLeft" />
							<br />
							<div class="labelLeft">
								<h:outputText value="#{msg.login_passowrd}" />
							</div>
							<!--input id="parola" type="password" name="j_password" class="formTextfieldLeft"/-->
							<h:inputSecret value="#{loginBean.loginPassword}" id="parola"
								styleClass="formTextfieldLeft" />
							<br />
							<br />
							<div>
								<h:commandButton action="#{loginBean.actionLogin}"
									value="#{msg.login_button_submit}" id="reset"
									styleClass="formButton" />
								<!--input type="submit" name="Login" value="Login" class="formButton"/-->
								<input name="anuleaza" type="reset" class="formButton"
									value="Anuleaza" id="anuleaza" />
							</div>
						</h:form>
						<br />
						<br />
						<br />
						<h3>
							<%--	<h:outputLink value="user-register.jsf">
								<h:outputText value="#{msg.meniu_inscriere_voluntar}"
									escape="false" />
							</h:outputLink>--%>
							<br /> <br />
							<h:outputLink value="reset-password.jsf">
								<h:outputText value="#{msg.reset_link}" escape="false" />
							</h:outputLink>
						</h3>
					</h:panelGroup>
				</div>
				<div id="rightColumn" style="height: 600px;">
					<h:panelGroup rendered="#{(sessionScope['USER_DETAILS'] eq null)}">
						<h:messages warnClass="registerMessageError"
							infoClass="registerMessageOk" />
					</h:panelGroup>
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
							<li><a href="http://www.letsdoitromania.ro/feedback-proiect"><img
									src="/icons/11-feedback.jpg" width="230" height="230" /></a></li>
							<li>
						</ul>
					</div>
				</div>
			</div>
		</div>

		<%-- page Bottom --%>
		<custom:page_bottom />
	</center>
</body>
	</html>
</f:view>
