<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:directive.include file="/WEB-INF/jspf/page-header.jspf" />
<jsp:directive.include file="/WEB-INF/jspf/login-admin-checkpoint.jspf" />
<f:view>
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:directive.include file="/WEB-INF/jspf/page-meta.jspf" />
<title>Let's do it Romania</title>
</head>
<body>
	<center>
		<%-- page Top --%>
		<custom:page_top_login selected="lista_utilizatori"
			role="${adminUserSessionBean.userDetails.role}" />

		<%-- page Content --%>
		<div id="pageContainer">
			<div id="contentList">
				<jsp:directive.include file="/WEB-INF/jspf/popup-loading.jspf" />
				<jsp:directive.include file="/WEB-INF/jspf/popup-user-details.jspf" />

				<%-- Left Column --%>
				<div id="leftColumn">
					<div class="labelLeft">
						<h1></h1>
					</div>
					<a4j:form>
						<a4j:commandButton
							actionListener="#{adminUserSessionBean.actionRefreshList}"
							reRender="online-users-list" styleClass="formButtonLeft"
							value="Refresh" />
					</a4j:form>
				</div>

				<%-- Right Column (user list) --%>
				<div id="rightColumnList" style="min-height: 300px;">
					<a4j:outputPanel id="online-users-list">
						<h:messages warnClass="registerMessageError"
							infoClass="registerMessageOk" />

						<a4j:form>
							<h1>
								<h:outputFormat value="Lista utilizatori online: ({0})">
									<f:param value="#{fn:length(adminUserSessionBean.userList)}" />
								</h:outputFormat>
							</h1>
							<br />
							<br />

							<div id="listHeaderContainer">
								<div class="listHeader">Nume</div>
								<div class="listHeader">Prenume</div>
								<div class="listHeaderLarge">E-mail</div>
								<div class="listHeader">Telefon</div>
								<div class="listHeader">Rol</div>
								<div class="listHeader">Oras</div>
								<div class="listHeader">Judet</div>

								<div class="listHeader">ID</div>

							</div>

							<a4j:repeat value="#{adminUserSessionBean.userList}" var="user">
								<div id="listEntryContainer">
									<div class="listEntry">
										<h:outputText value="#{user.user.lastName}" />
									</div>
									<div class="listEntry">
										<h:outputText value="#{user.user.firstName}" />
									</div>
									<div class="listEntryLarge">
										<a href="mailto:nume@domeniu.ro"><h:outputText
												value="#{user.user.email}" /></a>
									</div>
									<div class="listEntry">
										<h:outputText value="#{user.user.phone}" />
									</div>
									<div class="listEntry">
										<h:outputText value="#{user.user.role}" />
									</div>
									<div class="listEntry">
										<h:outputText value="#{user.user.town}" />
									</div>
									<div class="listEntry">
										<h:outputText value="#{user.user.county}" />
									</div>

									<div class="listEntry">
										<h:outputText value="#{user.user.userId}" />
									</div>
								</div>
							</a4j:repeat>
						</a4j:form>

					</a4j:outputPanel>
				</div>
			</div>
		</div>

		<%-- page Bottom --%>
		<custom:page_bottom />
	</center>
</body>
	</html>
</f:view>
