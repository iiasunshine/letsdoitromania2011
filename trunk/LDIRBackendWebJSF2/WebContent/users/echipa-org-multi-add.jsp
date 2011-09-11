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
					<div id="leftColumn">
					    <h3>
							<a href="${pageContext.servletContext.contextPath}/users/echipa-org-multi-editare.jsf">
								 <h:outputText value="» #{msg.echipa_org_multi_link}"/>
							</a>
						</h3>
                         <br/>
				        <h3>
                             <h:outputText value="#{msg.echipa_empty_list}"
                                              rendered="#{fn:length(orgBean.teamList) eq 0}"/>
                             <h:outputText value="#{msg.echipa_list_title}"
                                              rendered="#{fn:length(orgBean.teamList) gt 0}"/>
                         </h3>
                         <h:panelGroup rendered="#{fn:length(orgBean.teamList) gt 0}">
                           <a4j:repeat value="#{orgBean.teamList}" var="team">
                          		 <div class="entryLeft">
                          		 	    <h:outputText value="Echipa Nume = " />
                                        <strong><h:outputText value="#{team.teamName}"/></strong>
                                        <br/>
                                        <h:outputLink value="echipa-org-multi-add.jsf?teamId=#{team.teamId}">
                                            <h:outputText value="#{msg.details_view_link}" escape="false"/>
                                        </h:outputLink>
                          		 </div>
                           </a4j:repeat>
                         
                         </h:panelGroup>

					</div>
					<%-- right column --%>
				<div id="rightColumn">

 	<h:messages warnClass="registerMessageError" infoClass="registerMessageOk" />
 	
 				<h:panelGroup rendered="#{orgBean.teamId ne 0}">
 					<h:form>
 							<div class="label1">
                                <h1><h:outputText value="Editeaza echipa #{orgBean.teamName}"/></h1>
                            </div>
                            <br/>
 						<div class="label">
 						   <h:outputText value="Numele echipei"/><span class="important">*</span>
 						</div>
                        <h:inputText value="#{orgBean.teamName}" id="code" styleClass="formTextfield"/>
                                <br/>
                                <br/>                                
                                 <!-- BUTOANE -->
                                 
                                <div style="margin-left: 150px;">
                                    <h:commandButton action="#{orgBean.actionChangeNameTeam}"
                                                     value="Redenumeste echipa"
                                                     id="confirma1"
                                                     styleClass="formButton">
                                     <f:param name="teamId" value="#{orgBean.teamId}"/>
                                   </h:commandButton>
                                </div>
                                <br/>
                                <div style="margin-left: 150px;">
                                    <h:commandButton action="#{orgBean.actionDeleteTeam}"
                                                     value="Sterge echipa"
                                                     id="confirma2"
                                                     styleClass="formButton">
                                       <f:param name="teamId" value="#{orgBean.teamId}"/>
                                   </h:commandButton>   
                                </div>
                                <br/>
 								<div style="margin-left: 150px;">
                                    <h:commandButton action="echipa_mem_detalii.jsf"
                                                     value="Cod acces echipa"
                                                     id="confirma3"
                                                     styleClass="formButton">
                                         <f:param name="teamId" value="#{orgBean.teamId}"/>
                                     </h:commandButton>    
                                </div>
                                 <br/>
                                <div style="margin-left: 150px;">
                                    <h:commandButton action="echipa-mem-editare.jsf"
                                                     value="Modifica lista de membrii din echipa"
                                                     id="confirma4"
                                                     styleClass="formButton"
                                                     disabled="true"                                                  
                                                     >
                                       <f:param name="teamId" value="#{orgBean.teamId}"/>
                                   </h:commandButton>
                                <h:outputText value="Momentan inactiv"/>  
                                </div>
                                
                                 <br/>
                                 <h:panelGroup rendered="#{not orgBean.orgBool}">
                                <div style="margin-left: 150px;">
                                    <h:commandButton action="echipa-org-detalii.jsf"
                                                     value="Adauga organizatie in echipa"
                                                     id="confirma5"
                                                     styleClass="formButton">
                                       <f:param name="teamId" value="#{orgBean.teamId}"/>
                                   </h:commandButton>               
                                </div>
                                <br/>
                                </h:panelGroup>
                                <h:panelGroup rendered="#{orgBean.orgBool}">
                                <div style="margin-left: 150px;">
                                    <h:commandButton action="echipa-org-detalii.jsf"
                                                     value="Modifica datele organizatiei"
                                                     id="confirma5a"
                                                     styleClass="formButton">
               					       <f:param name="teamId" value="#{orgBean.teamId}"/>
                                   </h:commandButton>            
                                </div>
                                 <br/>
                                 </h:panelGroup>
                                 <h:panelGroup rendered="#{not orgBean.equipmentBool}">
                                 <div style="margin-left: 150px;">
                                    <h:commandButton action="echipa-equip-detalii.jsf"
                                                     value="Adauga dotari la echipa"
                                                     id="confirma6"
                                                     styleClass="formButton">
                      					<f:param name="teamId" value="#{orgBean.teamId}"/>
                                   </h:commandButton>  
                                </div>
                                <br/>
                                </h:panelGroup>
                                <h:panelGroup rendered="#{orgBean.equipmentBool}">
                                <div style="margin-left: 150px;">
                                    <h:commandButton action="echipa-equip-detalii.jsf"
                                                     value="Modifica dotarile echipei"
                                                     id="confirma6a"
                                                     styleClass="formButton">
                                         <f:param name="teamId" value="#{orgBean.teamId}"/>
                                   </h:commandButton> 
                                </div>
                                 <br/>
                                 </h:panelGroup>
                                <div style="margin-left: 150px;">
                                    <h:commandButton action="#{orgBean.actionTeam}"
                                                     value="Aloca mormane pentru curatenie"
                                                     id="confirma7"
                                                     styleClass="formButton"
                                                     disabled="true"
                                                     >
                                        <f:param name="teamId" value="#{orgBean.teamId}"/>
                                   </h:commandButton>              
                                <h:outputText value="Momentan inactiv"/>
                                </div>
                                 <br/>
                                <div style="margin-left: 150px;">
                                    <h:commandButton action="echipa-org-multi-editare.jsp"
                                                     value="Renunta"
                                                     id="confirma8"
                                                     styleClass="formButton"/>
                                </div>
 					
 					</h:form>
  				</h:panelGroup>
				</div>		
				</div>
			</div>

                <%-- page Bottom --%>
                <custom:page_bottom/>
            </center>
			
		</body>
	</html>


</f:view>