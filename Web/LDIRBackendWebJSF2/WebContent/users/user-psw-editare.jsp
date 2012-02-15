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
						<b>
						<h3>
							<a href="${pageContext.servletContext.contextPath}/users/user-psw-editare.jsf">
								 <h:outputText value="» #{msg.user_change_psw_link}"/>
							</a>
						</h3>
						</b>
					</div>
					
					<%-- right column --%>
					<div id="rightColumn" style="min-height: 200px;">
					        <div class="label1">
                                <h1><h:outputText value="Schimba Parola"/></h1>
                            </div>
                             <br/>
                          <h:messages warnClass="registerMessageError" infoClass="registerMessageOk"/>
                          
                          <h:form styleClass="form" rendered="#{not fn:contains(facesContext.messageList[0].severity, 'INFO')}">    
                            <!-- PAROLA CURENTA-->
                            <div class="label form"><h:outputText value="#{msg.user_curent_password} "/><span class="important">*</span></div>
                             <h:inputSecret value="#{userManager.passwordCurent}" id="parola_curenta" styleClass="formTextfield"/>
                            <br />
                            <!-- PAROLA NOUA-->
                            <div class="label form"><h:outputText value="#{msg.user_new_password} "/><span class="important">*</span></div>
                            <h:inputSecret value="#{userManager.password}" id="parola" styleClass="formTextfield"/>
                            <br />
                            <!-- CONFIRMARE PAROLA NOUA-->
                            <div class="label form"><h:outputText value="#{msg.user_new_confirm_password} "/><span class="important">*</span></div>
                            <h:inputSecret value="#{userManager.passwordConfirm}" id="confirmare_parola" styleClass="formTextfield"/>
                            <br/>
                            <br/>
                           	<!-- BUTOANE -->
                            <div style="margin-left: 150px;">
                            <h:commandButton action="#{userManager.actionPassword}"
                                 value="#{msg.user_button_psw_confirm}"
                                 id="confirma"
                                 styleClass="formButton"/>
                                 <input name="anuleaza" type="reset" class="formButton" value="Renunta" id="anuleaza" />
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