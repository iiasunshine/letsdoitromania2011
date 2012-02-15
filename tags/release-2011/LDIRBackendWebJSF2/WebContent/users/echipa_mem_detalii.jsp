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
					    <!-- COD ACCESS -->
							<div class="label">
                                <h1><h:outputText value="Cod Access"/></h1>
                            </div>
                            <br/>   
                            <div class="form">
                            	<h:outputText value="Pentru a adauga membrii in echipa ta trebuie sa le comunici acestora codul de acces. Codul de acces este:"/>
                            	<br/>
                            	<strong><h:outputText value="#{teamBean.userTeam.teamId}"/></strong>
                            </div>
                             <br/>
                             <div class="form">
                            	<h:outputText value="Pentru ca o persoana sa devina membru in echipa ta trebuie sa fie inregistrata ca user pe acest site. Daca este inregistrata trebuie sa acceseze meniul [Echipa] -> [Inscriere in echipa] si sa introduca codul de access furnizat mai sus."/>
                            	
                            </div>
					</div>
					
				</div>
			</div>

                <%-- page Bottom --%>
                <custom:page_bottom/>
            </center>
			
		</body>
	</html>


</f:view>