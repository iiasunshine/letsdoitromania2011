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

					<h:messages warnClass="registerMessageError"
						infoClass="registerMessageOk" />
					<br /> <br />
					<a4j:outputPanel id="lista-utilizatori"
						rendered="#{(fn:length(teamBean.volunteerMembers))gt 0}">

						<h1>
							<h:outputFormat
								value="Lista voluntarilor inregistrati in echipa ta ({0})">
								<f:param value="#{fn:length(teamBean.volunteerMembers)}" />
							</h:outputFormat>
							<br /> <br />

						</h1>

						<span class="important">Emailul si telefonul va fi afisat
							doar daca voluntarii si-au dat acordul .</span>
						<br />
						<br />
						<h:form>
							<div id="listHeaderContainer">
								<div class="listHeader">Nume</div>
								<div class="listHeader">Oras</div>
								<div class="listHeader">Judet</div>
								<div class="listHeader">Telefon</div>
								<div class="listHeader" style="width: 120px">Email</div>
								<div class="listHeader">Elimina din echipa</div>
							</div>

							<a4j:repeat value="#{teamBean.volunteerMembers}" var="user">
								<div id="listEntryContainer">
									<div class="listEntry">
										<h:outputText value="#{user.firstName}" />
									</div>
									<div class="listEntry">
										<h:outputText value="#{user.town}" />
									</div>
									<div class="listEntry">
										<h:outputText value="#{user.county}" />
									</div>
									<div class="listEntry">
										<h:outputText value="#{user.phone}"
											rendered="#{user.profileView}" />
									</div>
									<div class="listEntry"
										style="width: 120px; overflow: scroll; overflow-y: hidden">
										<h:outputText value="#{user.email}"
											rendered="#{user.profileView}" />
									</div>



									<div class="listEntry">
										<h:commandButton action="#{teamBean.actionWithdrawFromTeam}"
											value="Sterge" id="confirma" styleClass="formButton">
											<f:param name="memDeleteId" value="#{user.userId}" />

										</h:commandButton>
									</div>
								</div>
							</a4j:repeat>
							<br />
							<br />

						</h:form>

					</a4j:outputPanel>
					<a4j:outputPanel id="lista-organizatii" rendered="#{(fn:length(teamBean.organizationMembers)) gt 0}">

						<h1>
							<h:outputFormat value="Lista grupurilor ({0})">
								<f:param value="#{fn:length(teamBean.organizationMembers)}" />
							</h:outputFormat>
							<br /> <br />

						</h1>
						<h:form>
							<div id="listHeaderContainer">
								<div class="listHeader">Nume</div>
								<div class="listHeader">Oras</div>
								<div class="listHeader">Judet</div>
								<div class="listHeader">Numar membri</div>
								<div class="listHeader">Elimina grupul</div>
							</div>

							<a4j:repeat value="#{teamBean.organizationMembers}" var="org">
								<div id="listEntryContainer">
									<div class="listEntry">
										<h:outputText value="#{org.name}" />
									</div>
									<div class="listEntry">
										<h:outputText value="#{org.town}" />
									</div>
									<div class="listEntry">
										<h:outputText value="#{org.county}" />
									</div>
									<div class="listEntry">
										<h:outputText value="#{org.membersCount}" />
									</div>

									<div class="listEntry">
										<h:commandButton action="#{teamBean.actionDelOrgTeam}"
											value="Sterge" id="confirma" styleClass="formButton">
											<f:param name="orgDeleteId" value="#{org.organizationId}" />
										</h:commandButton>
									</div>

								</div>
							</a4j:repeat>
						</h:form>

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