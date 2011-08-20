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

                        	  <a4j:outputPanel id="lista-utilizatori">
                                <a4j:form>
                                    <h1>
                                        <h:outputFormat value="Lista membri ({0})">
                                            <f:param value="#{fn:length(teamBean.volunteerMembers)}"/>
                                        </h:outputFormat>
                                        <br/>
                                        <br/>

                                    </h1>
                                    <h:form>
                                    <div id="listHeaderContainer">
                                        <div class="listHeader">Nume</div>
                                        <div class="listHeader">Oras</div>
                                        <div class="listHeader">Judet</div>
                                        <div class="listHeader">Tipul membrului</div>   
                                        <div class="listHeader">Numar membri</div>
                                        <div class="listHeader">Sterge membru</div>
                                    </div>

                                    <a4j:repeat value="#{teamBean.volunteerMembers}" var="user">
                                        <div id="listEntryContainer">
                                            <div class="listEntry"><h:outputText value="#{user.firstName}"/></div>
                                            <div class="listEntry"><h:outputText value="#{user.town}"/></div>
                                            <div class="listEntry"><h:outputText value="#{user.county}"/></div>
                                            <div class="listEntry"><h:outputText value="#{user.role}"/></div>
                                            <div class="listEntry"><h:outputText value="#{fn:length(user.managedTeams)}"/></div>
                                      
                                            <div class="listEntry"><h:commandButton action="#{teamBean.actionAddOrg}"
                                                     value="Sterge"
                                                     id="confirma"
                                                     styleClass="formButton"/>
                                             </div>        
                                        </div>
                                    </a4j:repeat>
                                    </h:form>
                                </a4j:form>
                            </a4j:outputPanel>
                            <a4j:outputPanel id="lista-organizatii">
                                <a4j:form>
                                    <h1>
                                        <h:outputFormat value="Lista Organizatii ({0})">
                                            <f:param value="#{fn:length(teamBean.organizationMembers)}"/>
                                        </h:outputFormat>
                                        <br/>
                                        <br/>

                                    </h1>
                                    <h:form>
                                    <div id="listHeaderContainer">
                                        <div class="listHeader">Nume</div>
                                        <div class="listHeader">Oras</div>
                                        <div class="listHeader">Judet</div>
                                        <div class="listHeader">Tipul membrului</div>   
                                        <div class="listHeader">Numar membri</div>
                                        <div class="listHeader">Sterge organizatie</div>
                                    </div>

                                    <a4j:repeat value="#{teamBean.organizationMembers}" var="org">
                                        <div id="listEntryContainer">
                                            <div class="listEntry"><h:outputText value="#{org.name}"/></div>
                                            <div class="listEntry"><h:outputText value="#{org.town}"/></div>
                                            <div class="listEntry"><h:outputText value="#{org.county}"/></div>
                                            <div class="listEntry"><h:outputText value="#{org.type}"/></div>
                                            <div class="listEntry"><h:outputText value="0"/></div>
                                      
                                            <div class="listEntry"><h:commandButton action="#{teamBean.actionAddOrg}"
                                                     value="Sterge"
                                                     id="confirma"
                                                     styleClass="formButton"/>
                                             </div>        
                                        </div>
                                    </a4j:repeat>
                                    </h:form>
                                </a4j:form>
                            </a4j:outputPanel>


					</div>
					
				</div>
			</div>

                <%-- page Bottom --%>
                <custom:page_bottom/>
            </center>
			
		</body>
	</html>


</f:view>