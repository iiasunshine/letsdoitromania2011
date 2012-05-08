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
		<custom:page_top_login selected="home"
			role="${userManager.userDetails.role}" />

		<%-- page Content --%>
		<div id="pageContainer">
			<div id="content">
				<%-- left column --%>
				<div id="leftColumn"></div>

				<%-- right column --%>
				<div id="rightColumn">
					<img
						src="http://www.letsdoitromania.ro/wp-content/uploads/2012/05/app-voluntari-help-slide3.jpg"
						width="640" height="480" /> <br /> <img
						src="http://www.letsdoitromania.ro/wp-content/uploads/2012/05/app-voluntari-help-slide4.jpg"
						width="640" height="480" /> <br /> <img
						src="http://www.letsdoitromania.ro/wp-content/uploads/2012/05/app-voluntari-help-slide5.jpg"
						width="640" height="480" /> <br /> <img
						src="http://www.letsdoitromania.ro/wp-content/uploads/2012/05/app-voluntari-help-slide6.jpg"
						width="640" height="480" /> <br /> <img
						src="http://www.letsdoitromania.ro/wp-content/uploads/2012/05/app-voluntari-help-slide7.jpg"
						width="640" height="480" /> <br />
				</div>
			</div>
		</div>

		<%-- page Buttom --%>
		<custom:page_bottom />
	</center>
</body>
	</html>
</f:view>