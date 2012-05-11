<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:directive.include file="/WEB-INF/jspf/page-header.jspf" />
<f:view>
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:directive.include file="/WEB-INF/jspf/page-meta.jspf" />
<title>Let's do it Romania</title>
</head>
<body>
	<center>
		<%-- page Top --%>
		<h:panelGroup rendered="#{sessionScope['USER_DETAILS'] eq null}">
			<custom:page_top selected="mapview" />
		</h:panelGroup>
		<h:panelGroup rendered="#{not (sessionScope['USER_DETAILS'] eq null)}">
			<custom:page_top_login selected="home"
				role="${sessionScope['USER_DETAILS'].role}"
				county="${sessionScope['USER_DETAILS'].county}" />
		</h:panelGroup>

		<%-- page Content --%>
		<div id="pageContainer">
			<div id="content">
				<div id="leftColumn"></div>

				<div id="rightColumn" style="height: 700px;">
					<br /> <br />
					<h2>
							<h:outputText
								value="Bine ai venit pe aplicatia Let`s Do It, Romania! 2012"
								escape="false" />
							<br /> <br />
						</h2>
						<iframe width="100%" height="90%" src="https://docs.google.com/a/letsdoitromania.ro/spreadsheet/viewform?formkey=dDhUSGdHb3JwUzYxOXRIQ0JfSGF1Qnc6MQ"></iframe>

					<br /> 
					<br />
				</div>
			</div>
		</div>

		<%-- page Bottom --%>
		<custom:page_bottom />
	</center>
</body>
	</html>
</f:view>
