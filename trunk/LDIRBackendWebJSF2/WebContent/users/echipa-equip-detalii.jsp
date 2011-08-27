<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:directive.include file="/WEB-INF/jspf/page-header.jspf" />
<jsp:directive.include file="/WEB-INF/jspf/login-checkpoint.jspf"/>


<f:view>
	<html xmlns="http://www.w3.org/1999/xhtml">
		<head>
			<jsp:directive.include file="/WEB-INF/jspf/page-meta.jspf" />
			<title>Let's do it Romania</title>
		</head>
		<body>
			<center>
			   <%-- page Top --%>  
			   <custom:page_top_login selected="echipa_edit" role="${userManager.userDetails.role}"/>

			
			<%-- page Content --%>  
			<div id="pageContainer">
				<div id="content">
					<%-- left column --%>
					  <custom:page_left_links role="${userManager.userDetails.role}"/>

					<%-- right column --%>
					<div id="rightColumn">
							<div class="label1">
                                <h1><h:outputText value="Adauga dotari la echipa"/></h1>
                            </div>
                             <br/>
                             <br/>
                         <h:messages warnClass="registerMessageError" infoClass="registerMessageOk"/>
                              <h:form styleClass="form" rendered="#{not fn:contains(facesContext.messageList[0].severity, 'INFO')}">
                                <!-- NUMAR APARATE GPS -->
                                <div class="label"><h:outputText value="Numar Aparate GPS"/></div>
                                 <h:selectOneMenu value="#{teamBean.gpsUnits}" id="GPS" styleClass="formDropdown">
                                    <f:selectItem itemLabel="NONE" itemValue="-1"/>
                                     <f:selectItems value="#{teamBean.numbers}"/>
                                </h:selectOneMenu>
                                <br />
                                 <!-- TRANSPORT -->
                                <div class="label"><h:outputText value="Transport"/></div>
                                <h:selectOneMenu   value="#{teamBean.transport}" id="transport" styleClass="formDropdown">
                                	<f:selectItem itemLabel="Bicileta" itemValue="BICYCLE"/>	
                                    <f:selectItem itemLabel="Masina" itemValue="CAR"/>
                                    <f:selectItem itemLabel="Masina companiei" itemValue="ORGANIZATION_CAR"/>
                                    <f:selectItem itemLabel="Transport in public" itemValue="PUBLIC"/>
                                </h:selectOneMenu>
                                
                                <br />
                                 <!-- NUMAR SACI -->
                                <div class="label"><h:outputText value="Numar Saci"/></div>
                                 <h:selectOneMenu value="#{teamBean.bagsUnits}" id="saci" styleClass="formDropdown">
                                    <f:selectItem itemLabel="NONE" itemValue="-1"/>
                                    <f:selectItems value="#{teamBean.numSac}"/>
                                </h:selectOneMenu>
                                <br />
                                <!-- NUMAR MANUSI -->
                                <div class="label"><h:outputText value="Numar Manusi"/></div>
                                 <h:selectOneMenu value="#{teamBean.glovesUnits}" id="manusi" styleClass="formDropdown">
                                    <f:selectItem itemLabel="NONE" itemValue="-1"/>
                                    <f:selectItems value="#{teamBean.numbMan}"/>
                                </h:selectOneMenu>
                                <br />
                                <!-- NUMAR LOPETI -->
                                <div class="label"><h:outputText value="Numar Lopeti"/></div>
                                 <h:selectOneMenu value="#{teamBean.shovelUnits}" id="lopeti" styleClass="formDropdown">
                                    <f:selectItem itemLabel="NONE" itemValue="-1"/>
                                    <f:selectItems value="#{teamBean.numbers}"/>
                                </h:selectOneMenu>
                                <br />
                                <!-- UTILAJE -->
                                <div class="label"><h:outputText value="Utilaje de curatenie(basculanta, excavator sa)"/></div>
                                <h:inputText value="#{teamBean.toolsUnits}" id="utilaje" styleClass="formTextfield"/>
                              
                                <br />
                     			<br/>
                     			<br/>
                                <!-- BUTOANE -->
                                <div style="margin-left: 150px;">
                                    <h:commandButton action="#{teamBean.actionAddEquipment}"
                                                     value="Inregistreaza dotarile"
                                                     id="confirma"
                                                     styleClass="formButton"/>
                                    <input name="anuleaza" type="reset" class="formButton" value="Renunta" id="anuleaza" />
                                </div>
                            </h:form>

					</div>
					
				</div>
			</div>

                <%-- page Bottom --%>
                <custom:page_bottom/>
            </center>
			
		</body>
	</html>


</f:view>