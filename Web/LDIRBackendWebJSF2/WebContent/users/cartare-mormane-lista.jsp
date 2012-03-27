<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:directive.include file="/WEB-INF/jspf/page-header.jspf" />
<jsp:directive.include file="/WEB-INF/jspf/login-checkpoint.jspf" />
<f:view>
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:directive.include file="/WEB-INF/jspf/page-meta.jspf" />
<title>Let's do it Romania</title>
<%--<script
	src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=${initParam['google.maps.key']}"
	type="text/javascript">
	
</script>--%>
</head>
<body>
	<center>
		<%-- page Top --%>
		<custom:page_top_login selected="mormane"
			role="${mormanManager.userDetails.role}" />

		<%-- page Content --%>
		<div id="pageContainer">
			<div id="content">
				<%-- Left Column --%>
				<div id="leftColumn" style="" >
					<%-- <h3>
						<a
							href="${pageContext.servletContext.contextPath}/users/cartare-mormane-editare.jsf">
							<h:outputText value="» #{msg.chart_add_morman_link}" />
						</a>
					</h3> --%>
					<h:panelGroup
						rendered="#{fn:length(mormanManager.myGarbageList) eq 0}">
						<h1>
							<h:outputText value="#{msg.chart_empty_list}" />
						</h1>
					</h:panelGroup>
					<h:panelGroup
						rendered="#{fn:length(mormanManager.myGarbageList) gt 0}">
						<h1>
							<h:outputText
								value="#{msg.chart_list_title}  (#{fn:length(mormanManager.myGarbageList)})" />
						</h1>
						<h:panelGroup
							rendered="#{fn:length(mormanManager.myGarbageList) gt 0}"
							style="#{fn:length(mormanManager.myGarbageList) gt 15? 'max-height: 610px; overflow: scroll; display: block;' : ''}">
							<a4j:repeat value="#{mormanManager.myGarbageList}"
								var="myGarbage">
								<div class="entryLeft">
									<h:outputText value="#{myGarbage.garbage.dispersed ? msg.zone : msg.morman}"/>
									<strong><h:outputText
											value="#{myGarbage.garbage.garbageId}" /></strong> <br />
									<h:outputLink
										value="cartare-mormane-detalii.jsf?garbageId=#{myGarbage.garbage.garbageId}">
										<h:outputText value="#{msg.details_view_link}" escape="false" />
									</h:outputLink>
								</div>
							</a4j:repeat>
						</h:panelGroup>
					</h:panelGroup>

				</div>


		<%-- page Content --%>
		<custom:edit_garbage role="${mormanManager.userDetails.role}"/>

				<%-- Right Column (harta) 
				<div id="rightColumn">
				<a4j:form>
						<m:map latitude="44.4317879" longitude="26.1015844" width="710px"
							height="600px" zoom="8" jsVariable="myMap">
							<m:mapControl name="GLargeMapControl3D" />
							<m:mapControl name="GMapTypeControl"
								position="G_ANCHOR_TOP_RIGHT" />
							<m:mapControl name="GScaleControl"
								position="G_ANCHOR_BOTTOM_RIGHT" />
							<a4j:repeat value="#{mormanManager.myGarbageList}" var="gunoi">
								<m:marker latitude="#{gunoi.coordYToString}"
									longitude="#{gunoi.coordXToString}"
									showInformationEvent="mouseover">
									<m:icon
										imageURL="http://app.letsdoitromania.ro:8080/LDIRBackendWebJSF2/icons/morman-rosu-20x20.png"
										width="20" height="20" />
									<m:htmlInformationWindow htmlText="#{gunoi.infoHtml}" />
								</m:marker>
							</a4j:repeat>
						</m:map>
					</a4j:form> 
					
				</div> --%>
			</div>
		</div>

		<%-- page Bottom --%>
		<custom:page_bottom />
	</center>
</body>
	</html>
</f:view>
