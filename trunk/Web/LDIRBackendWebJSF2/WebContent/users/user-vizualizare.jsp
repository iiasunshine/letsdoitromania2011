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
                            <!-- NUME -->
                            <div class="label form"><h:outputText value="#{msg.register_name} "/></div>
                            <div class="label"><h:outputText value="#{userManager.userDetails.firstName}"/></div>
                             <br/>
                             <!-- PRENUME -->
                            <div class="label form"><h:outputText value="#{msg.register_surname} "/></div>
                            <div class="label"><h:outputText value="#{userManager.userDetails.lastName}"/></div>
                             <br/>
                             <!-- DATA NASTERE -->
                             <div class="label form"><h:outputText value="#{msg.register_birthday}"/></div>
                             <div class="label"><h:outputText value="#{userManager.userDetails.birthday}"/></div>
                      	    <br/>
                             <!-- MAIL -->
                             <div class="label form"><h:outputText value="#{msg.register_email} "/></div>
                             <div class="label"><h:outputText value="#{userManager.userDetails.email}"/></div>
                             <br/>

                             <!-- ORAS -->
                             <div class="label form"><h:outputText value="#{msg.register_city} "/></div>
                              <div class="label"><h:outputText value="#{userManager.userDetails.town}"/></div>
                             <br/>
                             <!-- JUDET -->
                             <div class="label form"><h:outputText value="#{msg.register_area} "/></div>
                              <div class="label"><h:outputText value="#{userManager.userDetails.county}"/></div>
                             <br/>
                             <!-- ACTIVITATI -->
                             <div class="label form"><h:outputText value="#{msg.register_activities} "/></div>
                             <h:selectBooleanCheckbox value="#{userManager.cartare}" id="cartare" disabled="true" rendered="false"/>
                             <label class="formCheckbox"><h:outputText value="#{msg.register_activities_cartare}" rendered="false"/></label>
  							 <h:selectBooleanCheckbox value="#{userManager.curatenie}" id="curatenie" disabled="true" />
                       		 <label class="formCheckbox"><h:outputText value="#{msg.register_activities_curatenie}"/></label>
                             <br/>
                             <!-- TELEFON -->
                             <div class="label form"><h:outputText value="#{msg.register_phone} "/></div>
                              <div class="label"><h:outputText value="#{userManager.userDetails.phone}"/></div>
                             <br/>
                             <!-- ID -->
                             <div class="label form"><h:outputText value="ID user: "/></div>
                              <div class="label"><h:outputText value="#{userManager.userDetails.userId}"/></div>
                             <br/>
                             <!-- Accepta vizualizare de date Lead -->
                                <h:selectBooleanCheckbox value="#{userManager.profileView}" id="vizibleData" disabled="true"/>
                                <label class="formCheckbox">
                                    <h:outputText value="#{msg.register_acceptVizibleData} "/>
                                </label>
                                <br/>
                             <!-- Accepta si alte informari -->
                                <h:selectBooleanCheckbox value="#{userManager.userDetails.acceptsMoreInfo}" id="acceptReceiveNotifications" disabled="true"/>
                                <label class="formCheckbox">
                                    <h:outputText value="#{msg.register_acceptNotifications} "/>
                                </label>
       
                                <br />  
                             
                             
                             
					</div>
				</div>
			</div>	  
	  	  
	  	    <%-- page Buttom --%>
	  	    <custom:page_bottom/>
	  	  </center>
	  	</body>
	</html>
</f:view>