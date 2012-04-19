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
						<h:panelGroup rendered="#{not teamBean.orgBool}">
							<h:outputText value="#{msg.team_add_group_title}" />
						</h:panelGroup>
						<h:panelGroup rendered="#{teamBean.orgBool}">
							<h:outputText value="#{msg.team_change_group_title}" />
						</h:panelGroup>
					</h1>

					<br /> <br />
					<h:messages warnClass="registerMessageError"
						infoClass="registerMessageOk" />
					<h:form styleClass="form"
						rendered="#{not fn:contains(facesContext.messageList[0].severity, 'INFO')}">
						<!-- NUME ORGANIZATIE -->
						<div class="label">
							<h:outputText value="#{msg.team_edit_group_name_label}" />
							<span class="important">*</span>
						</div>
						<h:inputText value="#{teamBean.organization.name}" id="nume_org"
							styleClass="formTextfield" />
						<br />
						<!-- TIP ORGANIZATIE -->
						<div class="label">
							<h:outputText value="#{msg.team_edit_group_type_label}" />
							<span class="important">*</span>
						</div>
						<h:selectOneMenu value="#{teamBean.tipOrganization}" id="type"
							styleClass="formDropdown">
							<f:selectItem itemLabel="Prieteni" itemValue="FRIENDS" />
							<f:selectItem itemLabel="Primarie" itemValue="CITY_HALL" />
							<f:selectItem itemLabel="Companie" itemValue="COMPANY" />
							<f:selectItem itemLabel="Punct de inregistrare"
								itemValue="REGISTRATION_POINT" />
							<f:selectItem itemLabel="Scoala" itemValue="SCHOOL" />
							<f:selectItem itemLabel="ONG" itemValue="ONG" />
							<f:selectItem itemLabel="Institutie" itemValue="INSTITUTIE" />
							<f:selectItem itemLabel="Altele" itemValue="ALTELE" />
						</h:selectOneMenu>
						<br />
						<!-- PARTICIPANTI -->
						<div class="label">
							<h:outputText value="#{msg.team_edit_group_members_label}" />
							<span class="important">*</span>
						</div>
						<h:inputText value="#{teamBean.organization.membersCount}"
							id="participanti" styleClass="formTextfield" />
						<br />


						<h:outputText value="#{msg.team_edit_group_location_label}" />

						<br />
						<!-- ADRESA -->

						<div class="label">
							<h:outputText value="#{msg.team_edit_group_address_label}" />
							
						</div>
						<h:inputText value="#{teamBean.organization.address}" id="adresa"
							styleClass="formTextfield" />
						<br />
						<!-- ORAS -->
						<div class="label">
							<h:outputText value="#{msg.register_city} " />
							
						</div>
						<h:inputText value="#{teamBean.organization.town}"
							id="oras_resedinta" styleClass="formTextfield" />
						<br />
						<!-- JUDET -->
						<div class="label">
							<h:outputText value="#{msg.register_area} " />
							
						</div>
						<h:selectOneMenu value="#{teamBean.organization.county}"
							id="judet_resedinta" styleClass="formDropdown">
							<f:selectItem itemLabel="Alege Judet" itemValue="" />
							<f:selectItems value="#{teamBean.countyItems}" />
						</h:selectOneMenu>
						<br />


						<h:outputText value="#{msg.team_edit_group_contact_label}" />

						<br />
						<!-- PRENUME PERSOANA -->
						<div class="label">
							<h:outputText value="#{msg.team_edit_group_firstname_label}" />
							
						</div>
						<h:inputText value="#{teamBean.organization.contactFirstname}"
							id="prenume" styleClass="formTextfield" />
						<br />
						<!-- NUME PERSOANA -->
						<div class="label">
							<h:outputText value="#{msg.team_edit_group_lastname_label}" />
							
						</div>
						<h:inputText value="#{teamBean.organization.contactLastname}"
							id="nume" styleClass="formTextfield" />
						<br />
						<!-- TELEFON -->
						<div class="label">
							<h:outputText value="#{msg.register_phone} " />
							
						</div>
						<h:inputText value="#{teamBean.organization.contactPhone}"
							id="telefon" styleClass="formTextfield" />
						<br />
						<!-- EMAIL PERSOANA -->
						<div class="label">
							<h:outputText value="#{msg.team_edit_group_email_label}" />
							
						</div>
						<h:inputText value="#{teamBean.organization.contactEmail}"
							id="email" styleClass="formTextfield" />
						<br />

						<br />
						<br />

						<!-- BUTOANE -->
						<h:panelGroup rendered="#{not teamBean.orgBool}">
							<div style="margin-left: 150px;">
								<h:commandButton action="#{teamBean.actionAddOrg}"
									value="#{msg.team_edit_group_record_button}" id="confirma"
									styleClass="formButton">
									<f:param name="teamId" value="#{orgBean.teamId}" />
								</h:commandButton>
								<input name="anuleaza" type="reset" class="formButton"
									value="Renunta" id="anuleaza" />
							</div>
						</h:panelGroup>

						<h:panelGroup rendered="#{teamBean.orgBool}">
							<div style="margin-left: 150px;">
								<h:commandButton action="#{teamBean.actionEditOrg}"
									value="#{msg.team_edit_group_change_button}" id="actualizeaza"
									styleClass="formButton">
									<f:param name="teamId" value="#{orgBean.teamId}" />
								</h:commandButton>
								<input name="anuleaza" type="reset" class="formButton"
									value="Renunta" id="anuleaza" />
							</div>
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