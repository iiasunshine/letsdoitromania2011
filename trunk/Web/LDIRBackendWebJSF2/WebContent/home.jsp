<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:directive.include file="/WEB-INF/jspf/page-header.jspf" />
<f:view>
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:directive.include file="/WEB-INF/jspf/page-meta.jspf" />
<jsp:directive.include file="/WEB-INF/jspf/googlemaps-meta.jspf"/>
<title>Let's do it Romania</title>
</head>
<body>
	<center>
		<%-- page Top --%>
		<h:panelGroup rendered="#{sessionScope['USER_DETAILS'] eq null}">
			<custom:page_top selected="home" />
		</h:panelGroup>
		<h:panelGroup rendered="#{not (sessionScope['USER_DETAILS'] eq null)}">
			<custom:page_top_login selected="home"
				role="${sessionScope['USER_DETAILS'].role}"
				county="${sessionScope['USER_DETAILS'].county}" />
		</h:panelGroup>

		<%-- page Content --%>
		<div id="pageContainer">
		
		<h2>
						<h:outputText
							value="Bine ai venit pe aplicatia Let`s Do It, Romania! 2013"
							escape="false" />
						<br /> <br />	<br /> <br />
					</h2>
					
					 <div id="map_options" style="float:left;display:block">
					        
					&nbsp;&nbsp;<input id="layers0" type="checkbox" title="Layers" onchange="layersoptions(this)" value="mormaneToate" name="layers"></input><label for="layers0">Toti anii</label>	<input id="layers1" type="checkbox" title="Layers" onchange="layersoptions(this)" value="mormane2013" name="layers" checked="checked"></input><label for="layers1">2013</label> <input id="layers2" type="checkbox" title="Layers" onchange="layersoptions(this)" value="judet" name="layers"></input><label for="layers2">Judet: </label>

				     
					  <h:selectOneMenu styleClass="formDropdownLeft" onchange="centerOnCounty(this.value);">
                                <f:selectItem itemLabel="Selecteaza Judet" itemValue=""/>
                                <f:selectItems value="#{mapViewBean.countyItems}"/>
                            </h:selectOneMenu>
					 <input id="layers3" type="checkbox" title="Layers" onchange="layersoptions(this)" value="clustering" name="layers"></input><label for="layers3" checked="checked">Clustere</label> &nbsp;&nbsp;
				&nbsp;&nbsp;
					<div id="ajaxloader" style="float:right;display:block">							 
							 <h:graphicImage value="/layout/images/ajaxloader.gif" width="20px"></h:graphicImage><h:outputText value="  Loading..." style="font-style:italic" /><br/>
							
							 </div>
					
					 
					 
					 </div><br/>

                     <div id="mapDIV" style="width:90%;float:left;padding:10px; background-color: white; border: 1px solid rgb(171, 171, 171); box-shadow: 0px 4px 16px rgba(0, 0, 0, 0.2);margin-right:30px;margin-left:20px">
							<div id="map" style="width: 100%; height: 600px"></div>
                        </div>

					 <div id="scrollingPhotosDIV" style="height:600px;float:left;width:10%;padding:10px; background-color: white; border: 1px solid rgb(171, 171, 171); box-shadow: 0px 4px 16px rgba(0, 0, 0, 0.2);overflow-y:scroll;display:none;">
					  <div id="scrollPhotos" style="" ></div>
                      </div>


		

				<!-- 				<div id="rightColumn" style="height: 800px;"> -->
			
				<br /> <br />
				<h:panelGroup rendered="#{sessionScope['USER_DETAILS'] eq null}">

                        <%-- righ column (harta cu gunoaie) --%>
		            <div style="clear:both"></div>
					<div id="homepage-grid" style="margin-top:40px">
						<ul>
							<li><a
								href="${pageContext.servletContext.contextPath}/user-register.jsf"><img
									src="/icons/01-inscriere-utilizator.jpg" width="230"
									height="230" /></a></li>

							<li><a
								href="${pageContext.servletContext.contextPath}/users/free-cartare-mormane-editare.jsf"><img
									src="/icons/02-adaugare-gunoi.jpg" width="230" height="230" /></a>
							</li>
							<li><a href="http://www.letsdoitromania.ro/feedback-proiect"><img
									src="/icons/11-feedback.jpg" width="230" height="230" /></a></li>
							<li>
							<li>
								<div class="button-login-logout">
									<a
										href="${pageContext.servletContext.contextPath}/user-login.jsf"><img
										src="/icons/10-login.jpg" width="230" height="110" /></a>
								</div>
							</li>
						</ul>
					</div>

		
				</h:panelGroup>


				<h:panelGroup
					rendered="#{not (sessionScope['USER_DETAILS'] eq null)}">
			<div id="content">
			<div id="leftColumn></div>
				<div id="rightColumn>
					<h2>
						<h:outputText
							value="Bine ai venit pe aplicatia Let`s Do It, Romania! 2013"
							escape="false" />
						<br /> <br />
					</h2>
					<div id="homepage-grid">
						<ul>
							<li><a
								href="${pageContext.servletContext.contextPath}/users/cartare-mormane-lista.jsf"><img
									src="/icons/02-adaugare-gunoi.jpg" width="230" height="230" /></a></li>

	 						<c:if
								test="${not (sessionScope['USER_DETAILS'].role eq 'VOLUNTEER')}">
	<%--							<li><a
									href="${pageContext.servletContext.contextPath}/users/garbage-vote.jsf"><img
										src="/icons/03-votare-zona.jpg" width="230" height="230" /></a></li>
--%>
								<li><a
									href="${pageContext.servletContext.contextPath}/admin/admin-lista-mormane.jsf"><img
										src="/icons/05-listare-gunoi.jpg" width="230" height="230" /></a></li>
								<li><a
									href="${pageContext.servletContext.contextPath}/admin/admin-lista-voluntari.jsf"><img
										src="/icons/06-listare-useri.jpg" width="230" height="230" /></a></li>
							</c:if>
		<%-- 					
							<li><a href="${pageContext.servletContext.contextPath}/users/curatenie-vizualizare.jsf"><img
									src="/icons/04-alocare-gunoi.jpg" width="230" height="230" /></a></li>
								--%>								
							<li><a href="http://www.letsdoitromania.ro/feedback-proiect"><img
									src="/icons/11-feedback.jpg" width="230" height="230" /></a></li>
							<li><a
								href="${pageContext.servletContext.contextPath}/users/help-page-volunteer-cleaning.jsf"><img
									src="/icons/14-ajutor.jpg" width="230" height="230" /></a></li>
							<li>
								<div class="button-profile">
									<a
										href="${pageContext.servletContext.contextPath}/users/user-vizualizare.jsf"><img
										src="/icons/09-profil.jpg" width="230" height="110" /></a>
								</div>
								<div class="button-login-logout">
									<a href="${pageContext.servletContext.contextPath}/logout.jsf"><img
										src="/icons/10-logout.jpg" width="230" height="110" /></a>
								</div>
							</li>
						</ul>
					</div>
			</div>
				</h:panelGroup>
				<br /> <br /> <br /> <br /> <br /> <br />

		</div>
		</div>

		<%-- page Bottom --%>
		<custom:page_bottom />
	</center>
</body>
	</html>
</f:view>
