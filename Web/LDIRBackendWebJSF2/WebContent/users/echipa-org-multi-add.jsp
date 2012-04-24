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
		<a4j:keepAlive beanName="orgBean" />
		<jsp:directive.include
			file="/WEB-INF/jspf/popup-team-set-astobeused.jspf" />
		<%-- page Content --%>
		<div id="pageContainer">
			<div id="content">
				<%-- left column --%>
				<div id="leftColumn">
					<custom:page_left_links role="${userManager.userDetails.role}" />
					<%--
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

					<a4j:outputPanel id="magicButton"
						rendered="#{fn:length(orgBean.teamList) gt 0}">
						<a4j:form>

							<a4j:repeat value="#{orgBean.teamList}" var="team">
								<div class="entryLeft">
									<h:outputText value="Echipa: " />
									<strong><h:outputText value="#{team.teamName}" /></strong> <br />
									<h:outputLink
										value="echipa-org-multi-add.jsf?teamId=#{team.teamId}">
										<h:outputText value="#{msg.team_view_more_info_label}"
											escape="false" />
									</h:outputLink>

									<a4j:commandLink actionListener="#{orgBean.actionSelectTeam}"
										id="keke" ajaxSingle="true">
										<f:param name="team" value="#{orgBean.teamId}" />
										<strong>Folosestedd echipa pentru alocare gunoaie</strong>
									</a4j:commandLink>


								</div>
							</a4j:repeat>
						</a4j:form>
					</a4j:outputPanel>

 --%>
				</div>

				<%-- right column --%>
				<div id="rightColumn">



					<a4j:outputPanel rendered="#{orgBean.teamId ne 0}"
						id="rightPanelTeamCommands">
						<h:messages warnClass="registerMessageError"
							infoClass="registerMessageOk" />
						<a4j:form>
							<h1>
								<h:outputText
									value="Editeaza echipa cu numele: #{orgBean.teamName}" />
							</h1>

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
								<h:commandButton action="#{orgBean.actionChangeNameTeam}"
									value="Redenumeste echipa" id="confirma1"
									styleClass="formButton">
									<f:param name="teamId" value="#{orgBean.teamId}" />
								</h:commandButton>
							</div>
							<br />
							<div style="margin-left: 150px;">
								<h:commandButton action="#{orgBean.actionDeleteTeam}"
									value="Sterge echipa" id="confirma2" styleClass="formButton">
									<f:param name="teamId" value="#{orgBean.teamId}" />
								</h:commandButton>
							</div>

							<br />
							
							
<%--
							<a4j:commandButton actionListener="#{orgBean.actionSelectTeam}"
								id="selectTeamForCleaningMainButton"
								reRender="rightPanelTeamCommands"
								value="Foloseste echipa pentru alocare gunoaie"
								styleClass="formButton">
								<f:param name="team" value="#{orgBean.teamId}" />
							</a4j:commandButton>--%>

							<a4j:commandLink actionListener="#{orgBean.actionSelectTeamToBeUsed}"
								reRender="popup_garbage_set_astobeused"
								id="popupLinkToActivateTeam" ajaxSingle="true"
								oncomplete="#{rich:component('popup_garbage_set_astobeused')}.show();">
								<h:outputText value="Selecteaza echipa pentru curatenie" escape="false" />

								<f:param name="team" value="#{orgBean.teamId}" />

							</a4j:commandLink>

							<br />
							<br />

							<div style="margin-left: 150px;">
								<h:commandButton action="echipa_mem_detalii.jsf"
									value="Codul de acces al echipei" id="confirma3"
									styleClass="formButton">
									<f:param name="teamId" value="#{orgBean.teamId}" />
								</h:commandButton>
							</div>
							<br />
							<div style="margin-left: 150px;">
								<h:commandButton action="echipa-mem-editare.jsf"
									value="Modifica lista de membrii din echipa" id="confirma4"
									styleClass="formButton">
									<f:param name="teamId" value="#{orgBean.teamId}" />
								</h:commandButton>
							</div>

							<br />
							<h:panelGroup rendered="#{not orgBean.orgBool}">
								<div style="margin-left: 150px;">
									<h:commandButton action="echipa-org-detalii.jsf"
										value="Adauga grup de voluntari in echipa" id="confirma5"
										styleClass="formButton">
										<f:param name="teamId" value="#{orgBean.teamId}" />
									</h:commandButton>
								</div>
								<br />
							</h:panelGroup>
							<h:panelGroup rendered="#{orgBean.orgBool}">
								<div style="margin-left: 150px;">
									<h:commandButton action="echipa-org-detalii.jsf"
										value="Modifica datele grupului din echipa" id="confirma5a"
										styleClass="formButton">
										<f:param name="teamId" value="#{orgBean.teamId}" />
									</h:commandButton>
								</div>
								<br />
							</h:panelGroup>
							<h:panelGroup rendered="#{not orgBean.equipmentBool}">
								<div style="margin-left: 150px;">
									<h:commandButton action="echipa-equip-detalii.jsf"
										value="Adauga dotari la echipa" id="confirma6"
										styleClass="formButton">
										<f:param name="teamId" value="#{orgBean.teamId}" />
									</h:commandButton>
								</div>
								<br />
							</h:panelGroup>
							<h:panelGroup rendered="#{orgBean.equipmentBool}">
								<div style="margin-left: 150px;">
									<h:commandButton action="echipa-equip-detalii.jsf"
										value="Modifica dotarile echipei" id="confirma6a"
										styleClass="formButton">
										<f:param name="teamId" value="#{orgBean.teamId}" />
									</h:commandButton>
								</div>
								<br />
							</h:panelGroup>

							<br />


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