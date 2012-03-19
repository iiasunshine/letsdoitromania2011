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
							<h:outputLink value="reset-password.jsf">
								<h:outputText value="#{msg.reset_link}" escape="false" />
							</h:outputLink>
							<br/>
							<br/>
								<h:outputLink value="user-register.jsf">
								<h:outputText value="#{msg.meniu_inscriere_voluntar}" escape="false" />
							</h:outputLink>						
						</h3>
					</h:panelGroup>
				</div>
				<div id="rightColumn" style="height: 200px;">
					<h:panelGroup rendered="#{(sessionScope['USER_DETAILS'] eq null)}">
						<h:messages warnClass="registerMessageError"
							infoClass="registerMessageOk" />
					</h:panelGroup>
					<h3>
						<h:outputText
							value="Bine ai venit pe aplicatia Let`s Do It, Romania!"
							escape="false" />
						<br />
					</h3>

				</div>
			</div>
		</div>

		<%-- page Bottom --%>
		<custom:page_bottom />
	</center>
</body>
	</html>
</f:view>
