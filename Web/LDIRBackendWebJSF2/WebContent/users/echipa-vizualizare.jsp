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
				<custom:page_left_links role="${userManager.userDetails.role}" />

				<%-- right column --%>
				<div id="rightColumn">


					<h1>
						<h:outputText value="Detaliile echipei tale" rendered="#{teamBean.managerBool}"/>
						<h:outputText value="Detaliile echipei din care faci parte" rendered="#{not teamBean.managerBool}"/>
					</h1>

					<br />

					<h:messages warnClass="registerMessageError"
						infoClass="registerMessageOk" />

					<h:panelGroup
						rendered="#{not fn:contains(facesContext.messageList[0].severity, 'INFO')}">
						<!-- NUME -->
						<!--  div class="label form"><h:outputText value="Nume Echipa:"/></div>
                            <div class="label">
                            	<strong><h:outputText value="#{teamBean.userTeam.teamName}"/></strong>
                            </div>
                             <br/> -->
						<!-- MANAGER -->
						<div class="label form">
							<h:outputText value="Numele coordonatorului de echipa:" />
						</div>
						<div class="label">
							<strong> <h:outputText
									value="#{teamBean.managerDetails.firstName}" />&nbsp; <h:outputText
									value="#{teamBean.managerDetails.lastName}" />
							</strong>
						</div>
						<br />
						<!-- LISTA VOLUNTARI -->
						<h:panelGroup
							rendered="#{fn:length(teamBean.volunteerMembers) gt 0}"
							style="#{fn:length(teamBean.volunteerMembers) gt 15? 'max-height: 610px; overflow: scroll; display: block;' : ''}">
							<div class="label form">
								<h:outputText value="Voluntarii din echipa:" />
							</div>
							<div class="label">
								<a4j:repeat value="#{teamBean.volunteerMembers}" var="volunteer">
									<strong> <h:outputText value="#{volunteer.firstName}" />&nbsp;
										<h:outputText value="#{volunteer.lastName}" />
									</strong>
									<br />
								</a4j:repeat>
							</div>
							<h:panelGroup rendered="#{teamBean.orgBool}">
								<div class="label form">
									<h:outputText value="Numele grupului inclus in echipa:" />
								</div>
								<strong> <h:outputText
										value="#{teamBean.organization.name}" />&nbsp;
								</strong>
							</h:panelGroup>
						</h:panelGroup>
						<!-- COD ACCESS -->
						<br />
						<div class="label form">
							<h:outputText value="Codul de identificare al echipei:" />
						</div>
						<div class="label">
							<strong> <h:outputText
									value="#{teamBean.userTeam.teamId}" />&nbsp;
							</strong>
						</div>
						<br />
						<!-- Dotari Echipa -->
						<div class="label form">
							<h:outputText value="Dotarile echipei cu materiale/echipamente:" />
						</div>
						<h:panelGroup rendered="#{teamBean.equipmentBool}">
							<div class="label">
								<strong>
									<div class="label">
										<h:outputText
											value="Saci: #{teamBean.bagsUnits}, Manusi: #{teamBean.glovesUnits}, GPS: #{teamBean.gpsUnits}, Transport: #{teamBean.transport}, Lopeti: #{teamBean.shovelUnits}, Alte echipamente: #{teamBean.toolsUnits}" />
									</div>
								</strong>
							</div>
						</h:panelGroup>
						<h:panelGroup rendered="#{not teamBean.equipmentBool}">
							<div class="label">
								<strong>
									<div class="label">
										<h:outputText
											value="Nu au fost mentionate de catre coordoantorul de echipa!" />
									</div>
								</strong>
							</div>
						</h:panelGroup>
						<br />
						<br />
						<br />
						<h:panelGroup rendered="#{not teamBean.managerBool}">
							<div class="form">
								<h2>
									<h:outputText
										value="Poti adauga membrii in echipa ta in doua moduri:" />
									<br /> <br /> <a
										href="${pageContext.servletContext.contextPath}/users/echipa-org-detalii.jsf">

										<h:outputText
											value="1) Adaugi un grup de voluntari in echipa ta. " />
									</a> <br />
									<br />
									<h:outputText
										value="2) Le spui prietenilor tai sa se inscrie la fel ca si tine. Apoi le comunici codul echipei tale [#{teamBean.userTeam.teamId}]" />
									<br />
									<h:outputText
										value="    Ei trebuie sa acceseze meniul [Echipa] -> [Inscriere in echipa] si sa introduca codul echipei tale furnizat mai sus." />
								</h2>
							</div>
						</h:panelGroup>
					</h:panelGroup>
				</div>

			</div>
		</div>

		<%-- page Bottom --%>
		<custom:page_bottom />
	</center>

</body>
	</html>


</f:view>