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
	  	     <custom:page_top_login selected="user_edit" role="${userManager.userDetails.role}"/>
	  	  
			<%-- page Content --%>  
			<div id="pageContainer">
				<div id="content">
					<%-- left column --%>
					<div id="leftColumn">
						<h3>
							<a href="${pageContext.servletContext.contextPath}/users/user-editare.jsf">
								 <h:outputText value="» #{msg.user_modify_link}"/>
							</a>
						</h3>
						<h3>
							<a href="${pageContext.servletContext.contextPath}/users/user-psw-editare.jsf">
								 <h:outputText value="» #{msg.user_change_psw_link}"/>
							</a>
						</h3>
					</div>
					
					<%-- right column --%>
					<div id="rightColumn" style="min-height: 200px;">
					        <div class="label">
                                <h1><h:outputText value="#{msg.user_title}"/></h1>
                            </div>
                             <br/>
                             
                              <h:messages warnClass="registerMessageError" infoClass="registerMessageOk"/>
                              <h:form styleClass="form" rendered="#{not fn:contains(facesContext.messageList[0].severity, 'INFO')}">
                                <!-- NUME -->
                                <div class="label"><h:outputText value="#{msg.register_name} "/><span class="important">*</span></div>
                                <h:inputText value="#{userManager.userDetails.firstName}" id="nume" styleClass="formTextfield"/>
                                <br />
                                <!-- PRENUME -->
                                <div class="label"><h:outputText value="#{msg.register_surname} "/><span class="important">*</span></div>
                                <h:inputText value="#{userManager.userDetails.lastName}" id="prenume" styleClass="formTextfield"/>
                                <br />
                                <!-- DATA NASTERE -->
                                <div class="label"><h:outputText value="#{msg.register_birthday} "/></div>
                             
                                 <h:selectOneMenu value="#{userManager.day}" id="zi_nastere" styleClass="formDate">
                                    <f:selectItem itemLabel="Zi" itemValue="0"/>
                                    <f:selectItems value="#{userManager.daysItems}"/>
                                </h:selectOneMenu>

                                <h:selectOneMenu value="#{userManager.month}" id="luna_nastere" styleClass="formDate">
                                    <f:selectItem itemLabel="Luna" itemValue="-1"/>
                                    <f:selectItem itemLabel="Ianuarie" itemValue="0"/>
                                    <f:selectItem itemLabel="Februarie" itemValue="1"/>
                                    <f:selectItem itemLabel="Martie" itemValue="2"/>
                                    <f:selectItem itemLabel="Aprilie" itemValue="3"/>
                                    <f:selectItem itemLabel="Mai" itemValue="4"/>
                                    <f:selectItem itemLabel="Iunie" itemValue="5"/>
                                    <f:selectItem itemLabel="Iulie" itemValue="6"/>
                                    <f:selectItem itemLabel="August" itemValue="7"/>
                                    <f:selectItem itemLabel="Septembrie" itemValue="8"/>
                                    <f:selectItem itemLabel="Octombrie" itemValue="9"/>
                                    <f:selectItem itemLabel="Noiembrie" itemValue="10"/>
                                    <f:selectItem itemLabel="Decembrie" itemValue="11"/>
                                </h:selectOneMenu>

                                <h:selectOneMenu value="#{userManager.year}" id="an_nastere" styleClass="formDate">
                              		<f:selectItem itemLabel="An" itemValue="-1"/>
                                    <f:selectItems value="#{userManager.yearsItems}"/>
                                </h:selectOneMenu>
                                <br/>
                                 <!-- ORAS -->
                                <div class="label"><h:outputText value="#{msg.register_city} "/></div>
                                <h:inputText value="#{userManager.userDetails.town}" id="oras_resedinta" styleClass="formTextfield"/>
                                <br />
                                <!-- JUDET -->
                                <div class="label"><h:outputText value="#{msg.register_area} "/></div>
                                <h:selectOneMenu value="#{userManager.userDetails.county}" id="judet_resedinta" styleClass="formDropdown">
                                    <f:selectItem itemLabel="Alege Judet" itemValue=""/>
                                    <f:selectItems value="#{userManager.countyItems}"/>
                                </h:selectOneMenu>
                                <br />
                                <!-- ACTIVITATI -->
                                <div class="label"><h:outputText value="#{msg.register_activities} "/><span class="important">*</span></div>
                                <h:selectBooleanCheckbox value="#{userManager.cartare}" id="cartare"/>
                                <label class="formCheckbox"><h:outputText value="#{msg.register_activities_cartare}"/></label>
  								<h:selectBooleanCheckbox value="#{userManager.curatenie}" id="curatenie"/>
                       			<label class="formCheckbox"><h:outputText value="#{msg.register_activities_curatenie}"/></label>
                                <br />
                                <!-- TELEFON -->
                                <div class="label"><h:outputText value="#{msg.register_phone} "/></div>
                                <h:inputText value="#{userManager.userDetails.phone}" id="telefon" styleClass="formTextfield"/>
                                <br/>
                                 <!-- Accepta si alte informari -->
                                <h:selectBooleanCheckbox value="#{userManager.acceptReceiveNotifications}" id="acceptReceiveNotifications"/>
                                <label class="formCheckbox">
                                    <h:outputText value="#{msg.register_acceptNotifications} "/>
                                </label>
                     			<br/>
                     			<br/>
                                <!-- BUTOANE -->
                                <div style="margin-left: 150px;">
                                    <h:commandButton action="#{userManager.actionEdit}"
                                                     value="#{msg.user_button_confirm}"
                                                     id="confirma"
                                                     styleClass="formButton"/>
                                    <input name="anuleaza" type="reset" class="formButton" value="Anuleaza modificarile" id="anuleaza" />
                                </div>
                            </h:form>
                             
					</div>
				</div>
			</div>	  
	  	  
	  	    <%-- page Buttom --%>
	  	    <custom:page_bottom/>
	  	  </center>
	  	</body>
	</html>
</f:view>