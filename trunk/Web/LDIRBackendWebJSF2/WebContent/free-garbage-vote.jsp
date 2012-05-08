<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:directive.include file="/WEB-INF/jspf/page-header.jspf" />
<jsp:directive.include file="/WEB-INF/jspf/notlogin-checkpoint.jspf" />

<f:view>
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:directive.include file="/WEB-INF/jspf/page-meta.jspf" />
<jsp:directive.include file="/WEB-INF/jspf/googlemaps-meta.jspf" />

<title>Let's do it Romania</title>
</head>
<body>
	<jsp:directive.include file="/WEB-INF/jspf/usermeta.jspf" />
	<center>
		<%-- page Top --%>

		<custom:page_top selected="freeGarbageVote" />
		<%-- page Content --%>
		<div id="pageContainer">
			<div id="contentList">

				<%-- Left Column --%>
				<div id="leftColumn">
					
				</div>

				<%-- Right Column (harta) --%>
				<div id="rightColumnList" style="min-height: 300px;">
					<h1> Procesul de votare s-a incheiat. Fa-ti cont si participa la curatenia din 12 mai 2012. Pentru mai multe detalii acceseaza butonul AJUTOR dupa ce te-ai logat!</h1>
					
				</div>

			</div>
		</div>


		<%-- page Bottom --%>
		<custom:page_bottom />
	</center>
</body>
	</html>
</f:view>