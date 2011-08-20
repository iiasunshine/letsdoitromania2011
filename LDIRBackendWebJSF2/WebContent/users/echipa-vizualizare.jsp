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
                            <div class="label form"><h:outputText value="Nume Echipa:"/></div>
                            <div class="label">
                            	<strong><h:outputText value="#{teamBean.userTeam.teamName}"/></strong>
                            </div>
                             <br/>
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
                                    <div class="label form"><h:outputText value="Lista Voluntari:"/> </div>
                                    <div class="label">
                                    	<a4j:repeat value="#{teamBean.volunteerMembers}" var="volunteer">
                                    	<strong>
                                            <h:outputText value="#{volunteer.firstName}"/>&nbsp;
                                            <h:outputText value="#{volunteer.lastName}"/>
                                        </strong>
                                            <br/>
                                  
                                   		</a4j:repeat>
                                    </div>
                                </h:panelGroup>
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