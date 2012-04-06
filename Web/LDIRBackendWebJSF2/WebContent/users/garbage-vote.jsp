l<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:directive.include file="/WEB-INF/jspf/page-header.jspf" />
<jsp:directive.include file="/WEB-INF/jspf/login-checkpoint.jspf" />

<f:view>
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:directive.include file="/WEB-INF/jspf/page-meta.jspf" />
<jsp:directive.include file="/WEB-INF/jspf/googlemaps-meta.jspf"/>

<title>Let's do it Romania</title>
</head>
<body>
<jsp:directive.include file="/WEB-INF/jspf/usermeta.jspf"/>
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
					file="/WEB-INF/jspf/popup-vote-img-gallery.jspf" />
				<jsp:directive.include file="/WEB-INF/jspf/popup-garbage-vote.jspf" />

				<%-- Left Column --%>
				<div id="leftColumn">
					<div class="labelLeft">
						<h1>Selecteaza judetul:</h1>
					</div>
					<a4j:form>
						<div>
							<h3>
								Judet<br />
								<h:selectOneMenu
								onchange="document.getElementById('formjudete:butonchange').click()"
								value="#{voteGarbageManagerBean.countyId}">
									<f:selectItems value="#{voteGarbageManagerBean.countyItems}" />
								</h:selectOneMenu>
								
							</h3>
						</div>

						<a4j:commandButton id="butonchange"
							actionListener="#{voteGarbageManagerBean.actionApplyFilterAsList}"
							reRender="garbageVoteList" styleClass="formButtonLeft"
							onclick="#{rich:component('popup-loading')}.show()"
							oncomplete="#{rich:component('popup-loading')}.hide();"
							value="Vezi zonele in tabel" />
						<a4j:commandButton
							actionListener="#{voteGarbageManagerBean.actionApplyFilterAsMap}"
							styleClass="formButtonLeft"
							onclick="window.location = '/users/garbage-vote-map.jsf'"							
							value="Vezi zonele pe harta" />
						<button type="reset" class="formButtonLeft">Anuleaza</button>
					</a4j:form>
				</div>

				<%-- Right Column (harta) --%>
				<div id="rightColumnList" style="min-height: 300px;">

					<a4j:outputPanel id="garbageVoteList"
						rendered="#{voteGarbageManagerBean.showList}">
						<h:messages warnClass="registerMessageError"
							infoClass="registerMessageOk" errorClass="registerMessageError" />

						<a4j:form>
							<h1>
								<h:outputFormat value="Zonele cu gunoaie ce trebuiesc votate: ({0})">
									<f:param
										value="#{fn:length(voteGarbageManagerBean.garbageList)}" />
								</h:outputFormat>
								<br /> <br />

							</h1>

							<div id="listHeaderContainer">
								<div class="listHeader">ID</div>
								<div class="listHeaderLarge">Numele zonei cu gunoaie</div>
								<div class="listHeader">Judet</div>
								<div class="listHeaderLarge">Descriere</div>

								<div class="listHeader">Raza[m]</div>
								<div class="listHeader">Numar saci</div>
								<div class="listHeaderLarge">Galerie foto</div>
								<div class="listHeaderLarge">Optiuni</div>
							</div>

							<a4j:repeat value="#{voteGarbageManagerBean.garbageList}"
								var="garbage">
								<div id="listEntryContainer">
									<div class="listEntry">
										<h:outputText value="#{garbage.garbageId}" />
									</div>
									<div class="listEntryLarge">
										<h:outputText value="#{garbage.name}" />
									</div>
									<div class="listEntry">
										<h:outputText value="#{garbage.county.name}" />
									</div>
									<div class="listEntryLarge">
										<rich:panel rendered="#{fn:length(garbage.description) gt 0}"
											onmouseover="this.style.cursor='help'">
											<h:outputText
												value="#{fn:substring(garbage.description, 0 , 30)} ..."
												rendered="#{fn:length(garbage.description) gt 30}" />
											<h:outputText value="#{garbage.description}"
												rendered="#{not (fn:length(garbage.description) gt 30)}" />
											<rich:toolTip>
												<h:outputText value="#{garbage.description}" />
											</rich:toolTip>
										</rich:panel>
									</div>


									<div class="listEntry">
										<h:outputText value="#{garbage.radius}" />
									</div>

									<div class="listEntry">
										<h:outputText value="#{garbage.bagCount}" />
									</div>

									<div class="listEntryLarge">
										<a4j:commandLink
											actionListener="#{voteGarbageManagerBean.actionSelectGarbage}"
											rendered="#{fn:length(garbage.pictures) gt 0}"
											reRender="img_gallery" ajaxSingle="true"
											onclick="#{rich:component('popup-loading')}.show();"
											oncomplete="#{rich:component('popup-loading')}.hide(); #{rich:component('img_gallery')}.show();">
											<f:param name="garbageId" value="#{garbage.garbageId}" />
											<f:param name="ipAddress" value="#{request.remoteAddr}" />
											<strong><h:outputText
													value="DESCHIDE (#{fn:length(garbage.pictures)})" /></strong>
										</a4j:commandLink>
									</div>


									<div class="listEntryLarge">
										<%-- vote --%>
										<a4j:commandLink
											actionListener="#{voteGarbageManagerBean.actionSelectGarbage}"
											reRender="popup_garbage_vote" id="voteButton"
											ajaxSingle="true"
											oncomplete="#{rich:component('popup_garbage_vote')}.show();">
											<f:param name="garbageId" value="#{garbage.garbageId}" />
											<f:param name="ipAddress" value="#{request.remoteAddr}" />
											<strong> <h:outputText value="VOTEAZA" />
											</strong>
										</a4j:commandLink>

									</div>
								</div>
							</a4j:repeat>
						</a4j:form>

						<h:panelGroup rendered="#{voteGarbageManagerBean.noFilter}">
							<br />
							<br />
							<br />
							<h3>
								<h:outputText value="Selecteaza un judet pentru a afisa zonele cu gunoaie vota!" />
							</h3>
						</h:panelGroup>
					</a4j:outputPanel>

				</div>

			</div>
		</div>


		<%-- page Bottom --%>
		<custom:page_bottom />
	</center>
</body>
	</html>
</f:view>
