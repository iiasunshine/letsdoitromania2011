<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:directive.include file="/WEB-INF/jspf/page-header.jspf" />
<jsp:directive.include file="/WEB-INF/jspf/login-checkpoint.jspf" />

<f:view>
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:directive.include file="/WEB-INF/jspf/page-meta.jspf" />
<jsp:directive.include file="/WEB-INF/jspf/usermeta.jspf"/>
<jsp:directive.include file="/WEB-INF/jspf/googlemaps-meta.jspf"/>

<title>Let's do it Romania</title>
</head>
<body>

	<center>
		<%-- page Top --%>
		<%-- 	<custom:page_top selected="lista_mormane"
			role="${voteGarbageManagerBean.userDetails.role}" /> --%>
		<custom:page_top_login selected="garbageVote" role="${voteGarbageManagerBean.userDetails.role}"/>
		<%-- page Content --%>
		<div id="pageContainer">
			<div id="contentList">

				<jsp:directive.include file="/WEB-INF/jspf/popup-loading.jspf" />
				<jsp:directive.include
					file="/WEB-INF/jspf/popup-admin-img-gallery.jspf" />
				<jsp:directive.include file="/WEB-INF/jspf/popup-garbage-vote.jspf" />

				<%-- Left Column --%>
				<div id="leftColumn">
					<div class="labelLeft">
						<h1>Selecteaza judetul:</h1>
					</div>
					<a4j:form>
						<div>
							
						</div>

						<a4j:commandButton
							actionListener="#{voteGarbageManagerBean.actionApplyFilterAsList}"
							styleClass="formButtonLeft"
							onclick="window.location = '/users/garbage-vote.jsf'"
							value="Vezi zonele in tabel" />
						<button type="reset" class="formButtonLeft">Anuleaza</button>
					</a4j:form>
					
					 <custom:layers_options/> 
				</div>

				<%-- Right Column (harta) --%>
				<div id="rightColumn">
					<div id="map" style="width: 100%; height: 600px"></div>
				</div>

			</div>
		</div>


		<%-- page Bottom --%>
		<custom:page_bottom />
	</center>
</body>
	</html>
</f:view>
