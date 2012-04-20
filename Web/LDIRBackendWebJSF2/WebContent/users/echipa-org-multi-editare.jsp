<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:directive.include file="/WEB-INF/jspf/page-header.jspf" />
<jsp:directive.include file="/WEB-INF/jspf/login-checkpoint.jspf" />


<f:view>
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:directive.include file="/WEB-INF/jspf/page-meta.jspf" />
<title>Let's do it Romania</title>
</head>
<body>
	<center>
		<%-- page Top --%>
		<custom:page_top_login selected="echipa_edit"
			role="${userManager.userDetails.role}" />


		<%-- page Content --%>
		<div id="pageContainer">
			<div id="content">
				<%-- left column --%>
				<div id="leftColumn">
					<h3>
						<a
							href="${pageContext.servletContext.contextPath}/users/echipa-org-multi-editare.jsf">
							<h:outputText value="» #{msg.echipa_org_multi_link}" />
						</a>
					</h3>
					<br />
					<h3>
						<h:outputText value="#{msg.echipa_empty_list}"
							rendered="#{fn:length(orgBean.teamList) eq 0}" />
						<h:outputText value="#{msg.echipa_list_title}"
							rendered="#{fn:length(orgBean.teamList) gt 0}" />
					</h3>
					<h:panelGroup rendered="#{fn:length(orgBean.teamList) gt 0}">
						<a4j:repeat value="#{orgBean.teamList}" var="team">
							<div class="entryLeft">
								<h:outputText value="Echipa Nume = " />
								<strong><h:outputText value="#{team.teamName}" /></strong> <br />
								<h:outputLink
									value="echipa-org-multi-add.jsf?teamId=#{team.teamId}">
									<h:outputText value="#{msg.details_view_link}" escape="false" />
								</h:outputLink>
							</div>
						</a4j:repeat>

					</h:panelGroup>

				</div>
				<%-- right column --%>
				<div id="rightColumn">

					<h:messages warnClass="registerMessageError"
						infoClass="registerMessageOk" />
					<h:form>
						<div class="label1">
							<h1>
								<h:outputText value="Adauga echipa noua:" />
							</h1>
						</div>
						<br />

						<div class="label">
							<h:outputText value="Numele echipei" />
							<span class="important">*</span>
						</div>
						<h:inputText value="#{orgBean.teamName}" id="code"
							styleClass="formTextfield" />

						<br />
						<br />
						<!-- BUTOANE -->
						<div style="margin-left: 150px;">
							<h:commandButton action="#{orgBean.actionTeam}"
								value="Adauga echipa" id="confirma" styleClass="formButton" />
							<input name="anuleaza" type="reset" class="formButton"
								value="Renunta" id="anuleaza" />
						</div>
						<br />

						<h:panelGroup rendered="#{fn:length(orgBean.teamAvalable) gt 1}">
							<h3>
								<h:outputText
									value="S-au gasit urmatoarele echipe care sa contina textul: '#{orgBean.teamName}'" />
							</h3>
							<br />
							<a4j:repeat value="#{orgBean.teamAvalable}" var="team">
								<div class="entryLeft">
									<h:outputText value="Echipa Nume = " />
									<strong><h:outputText value="#{team.teamName}" /></strong> <br />
								</div>
							</a4j:repeat>
							<h3>
								<h:outputText
									value="Alegeti numele exact al echipei din aceasta lista." />
							</h3>
						</h:panelGroup>

					</h:form>

				</div>
			</div>
		</div>

		<%-- page Bottom --%>
		<custom:page_bottom />
	</center>

</body>
	</html>


</f:view>