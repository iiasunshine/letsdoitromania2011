<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:directive.include file="/WEB-INF/jspf/page-header.jspf" />
<jsp:directive.include file="/WEB-INF/jspf/notlogin-checkpoint.jspf" />

<f:view>
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:directive.include file="/WEB-INF/jspf/page-meta.jspf" />
<title>Let's do it Romania</title>
</head>
<body>
	<center>
		<%-- page Top --%>

		<custom:page_top selected="mormane"/>

		<%-- page Content --%>
		<custom:edit_garbage/>
		
		
							<h:selectBooleanCheckbox
						value="#{mormanManager.myGarbage.garbage.toVote}" styleClass="formDate"
						id="dispersat" />
						
						
		<%-- page Bottom --%>
		<custom:page_bottom />
	</center>
</body>
	</html>
</f:view>
