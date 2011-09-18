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
                                <h1><h:outputText value="Inscriere in echipa"/></h1>
                            </div>
  						<h:messages warnClass="registerMessageError" infoClass="registerMessageOk" />
  						<h:form styleClass="form" rendered="#{not fn:contains(facesContext.messageList[0].severity, 'INFO')}">
  						        <!-- COD ECHIPA -->
  						        <div class="label"><h:outputText value="Codul echipei"/><span class="important">*</span></div>
                                <h:inputText value="#{teamBean.teamID}" id="code" styleClass="formTextfield"/>
                 
                                <br/>
                                <br/>
                                
                                 <!-- BUTOANE -->
                                <div style="margin-left: 150px;">
                                    <h:commandButton action="#{teamBean.actionAddToTeam}"
                                                     value="Inscrie-ma la echipa"
                                                     id="confirma"
                                                     styleClass="formButton"/>
                                    <input name="anuleaza" type="reset" class="formButton" value="Renunta" id="anuleaza" />
                                </div>
                                <br/>
                                <label id="warningPersonalData1">
									<h:outputText rendered="#{not userManager.profileView}" value="Pentru ca liderul sa vada datele tale de contact (telefon si email), va trebui sa iti dai acordul expres; pentru aceasta, modifica Profilul tau si selecteaza bifa \"Sunt de acord ca liderul echipei sa vada datele mele de contact\"" />
                                </label>
                                <label id="warningPersonalData2" class="important">
									<h:outputText rendered="#{userManager.profileView}" value="Liderul de echipa POATE sa vada emailul si telefonul tau. Pentru a le ascunde, modifica Profilul tau si deselecteaza bifa \"Sunt de acord ca liderul echipei sa vada datele mele de contact\"" />
                                </label>     
                                     
                                
                             
  						
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