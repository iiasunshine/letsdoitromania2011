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
					
							<div class="label">
                                <h1><h:outputText value="Detalii Echipa"/></h1>
                            </div>         
                            <br/>
                            
                            <h:messages warnClass="registerMessageError" infoClass="registerMessageOk"/>
                            
                            <h:panelGroup rendered="#{not fn:contains(facesContext.messageList[0].severity, 'INFO')}">
					         <!-- NUME -->
                            <!--  div class="label form"><h:outputText value="Nume Echipa:"/></div>
                            <div class="label">
                            	<strong><h:outputText value="#{teamBean.userTeam.teamName}"/></strong>
                            </div>
                             <br/> -->
                             <!-- MANAGER -->
                            <div class="label form"><h:outputText value="Nume Manager:"/></div>
                            <div class="label">
                              <strong>
                               <h:outputText value="#{teamBean.managerDetails.firstName}"/>&nbsp;
                               <h:outputText value="#{teamBean.managerDetails.lastName}"/>
                              </strong>
                            </div>
                             <br/>
                             <!-- LISTA VOLUNTARI -->
                                <h:panelGroup rendered="#{fn:length(teamBean.volunteerMembers) gt 0}"
                                              style="#{fn:length(teamBean.volunteerMembers) gt 15? 'max-height: 610px; overflow: scroll; display: block;' : ''}">
                                    <div class="label form"><h:outputText value="Membri in Echipa:"/> </div>
                                    <div class="label">
                                    	<a4j:repeat value="#{teamBean.volunteerMembers}" var="volunteer">
                                    	<strong>
                                            <h:outputText value="#{volunteer.firstName}"/>&nbsp;
                                            <h:outputText value="#{volunteer.lastName}"/>
                                        </strong>
                                            <br/>                                  
                                   		</a4j:repeat>                                   		
                                    </div>
                                <h:panelGroup rendered="#{teamBean.orgBool}">   
                                		<div class="label form"><h:outputText value=" Organizatia:"/> </div>
                                		<strong>
                               			 <h:outputText value="#{teamBean.organization.name}"/>&nbsp;
                              			</strong>
                               </h:panelGroup> 
                              </h:panelGroup>
                             <!-- COD ACCESS -->
                             <br/>
							<div class="label form"><h:outputText value="Cod Acces Echipa:"/></div>
                            <div class="label">
                              <strong>
                               <h:outputText value="#{teamBean.userTeam.teamId}"/>&nbsp;
                              </strong>
                            </div>
                            <br/>
                             <!-- Dotari Echipa -->
							<div class="label form"><h:outputText value="Dotari Echipa:"/></div>
                            <h:panelGroup rendered="#{teamBean.equipmentBool}">  
                            <div class="label">
                              <strong>
                               <div class="label"><h:outputText value="Saci: #{teamBean.bagsUnits}, Manusi: #{teamBean.glovesUnits}, GPS: #{teamBean.gpsUnits}, Transport: #{teamBean.transport}, Lopeti: #{teamBean.shovelUnits}, Alte echipamente: #{teamBean.toolsUnits}"/></div>
                              </strong>
                            </div>
                            </h:panelGroup>
                            <h:panelGroup rendered="#{not teamBean.equipmentBool}">  
                            <div class="label">
                              <strong>
                               <div class="label"><h:outputText value="Echipa NU are dotari!"/></div>
                              </strong>
                            </div>
                            </h:panelGroup>  
                            <br/>
							<br/>
                            <br/>   
                            <div class="form">
                            	<h:outputText value="Pentru a adauga membrii in echipa ta trebuie sa le comunici acestora codul de acces."/>
                            	<br/>
							 </div>
                             <br/>
                             <div class="form">
                            	<h:outputText value="Pentru ca o persoana sa devina membru in echipa ta trebuie sa fie inregistrata ca user pe acest site."/>
                            	<br/>
                            	<h:outputText value="Daca este inregistrata trebuie sa acceseze meniul [Echipa] -> [Inscriere in echipa] si sa introduca codul de access furnizat mai sus."/>
                            </div>   
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