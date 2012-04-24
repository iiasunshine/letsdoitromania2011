<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:directive.include file="/WEB-INF/jspf/page-header.jspf" />
<jsp:directive.include file="/WEB-INF/jspf/login-checkpoint.jspf" />
<f:view>
<%--	<c:if test="${mormanManager.myGarbage eq null}">
		<custom:page_redirect target="/users/cartare-mormane-lista.jsf" />
	</c:if>--%>
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<jsp:directive.include file="/WEB-INF/jspf/googlemaps-meta.jspf" />
<jsp:directive.include file="/WEB-INF/jspf/page-meta.jspf" />
<jsp:directive.include file="/WEB-INF/jspf/usermeta.jspf" />

<title>Let's do it Romania</title>
</head>
<%--<body
	onload="somefunction(${mormanManager.myGarbage.garbage.getGarbageId()})">--%>
<body>
	<center>
		<%-- page Top --%>


		<h:panelGroup
			rendered="#{mormanManager.userDetails.role eq 'VOLUNTEER'}">
			<custom:page_top_login selected="curatenie" role="VOLUNTEER" />
		</h:panelGroup>
		<h:panelGroup
			rendered="#{not (mormanManager.userDetails.role eq 'VOLUNTEER')}">
			<custom:page_top_login selected="curatenie" role="ADMIN" />
		</h:panelGroup>

		<%-- galerie imagini --%>

		<a4j:keepAlive beanName="mormanManager" />

		<jsp:directive.include file="/WEB-INF/jspf/popup-img-gallery.jspf" />
		<jsp:directive.include
			file="/WEB-INF/jspf/popup-garbage-set-cleaned.jspf" />
		<jsp:directive.include
			file="/WEB-INF/jspf/popup-garbage-set-allocated.jspf" />

		<%-- page Content --%>
		<div id="pageContainer">
			<div id="content">
				<%-- Left Column (detalii morman) --%>
				<div id="leftColumn">
					<a4j:outputPanel id="detalii_Morman">
						<br />
						<!-- mesaj eroare -->


						<a4j:form>
							<h:panelGroup
								rendered="#{not (mormanManager.myGarbage.garbage.garbageId eq null)}">
								<h:outputText escape="false" value="Echipa selectata: " />
								<span class="important"><strong><h:outputText
											value="#{areaCleanManager.teamSelected.teamName}" /></strong></span>
								<br />
								<h:outputText
									value="A alocat: #{areaCleanManager.teamSelected.getBagsEnrolled()}  saci"
									escape="false" />
								<br />
								<h:outputText
									value="Numar maxim: #{areaCleanManager.teamSelected.getCleaningPower()*areaCleanManager.teamSelected.countMembers()} saci"
									escape="false" />
								<br />
								<br />
								<br />
								<br />
								<h2>
									<h:outputText value="Acum vizualizezi" />
								</h2>
								<h:outputText escape="false" value="#{msg.details_morman} " />
								<strong><h:outputText
										value="#{mormanManager.myGarbage.garbage.garbageId}" /></strong>
								<br />
								<br />
								<h:outputText escape="false" value="#{msg.details_county} " />
								<strong><h:outputText
										value="#{mormanManager.garbageSimplu.county.name}" /></strong>
								<br />
								<br />
								<h:outputText escape="false" value="Saci alocati: " />
								<strong><h:outputText
										value="#{mormanManager.enrollBags} / #{mormanManager.myGarbage.garbage.bagCount}" /></strong>
								<br />
								<br />

								<h3>
									<h:outputText escape="false"
										value="#{msg.details_description_title}" />
								</h3>
								<h:outputText escape="false" value="#{msg.details_description} " />
								<strong><h:outputText
										value="#{mormanManager.myGarbage.garbage.description}" /></strong>
								<br />
								<br />
								<h:outputText escape="false" value="#{msg.details_dispersed} " />
								<strong><h:outputText
										value="#{mormanManager.myGarbage.garbage.dispersed ? 'Da' : 'Nu'}" /></strong>
								<br />
								<br />
								<h:outputText escape="false"
									value="#{msg.details_big_components} " />
								<strong><h:outputText
										value="#{mormanManager.myGarbage.garbage.bigComponentsDescription}" /></strong>
								<br />
								<br />
								<h:outputText escape="false" value="#{msg.details_components} " />
								<br />
								<h:outputText escape="false" value="#{msg.details_bags} " />
								<strong><h:outputText
										value="#{mormanManager.myGarbage.garbage.bagCount}" /></strong>
								<br />
								<h:outputText escape="false"
									value="#{msg.details_percent_plastic} " />
								<strong><h:outputText
										value="#{mormanManager.myGarbage.garbage.percentagePlastic}%" /></strong>
								<br />
								<h:outputText escape="false"
									value="#{msg.details_percent_glass} " />
								<strong><h:outputText
										value="#{mormanManager.myGarbage.garbage.percentageGlass}%" /></strong>
								<br />
								<h:outputText escape="false"
									value="#{msg.details_percent_metal }" />
								<strong><h:outputText
										value="#{mormanManager.myGarbage.garbage.percentageMetal}%" /></strong>
								<br />
								<h:outputText escape="false"
									value="#{msg.details_percent_waste }" />
								<strong><h:outputText
										value="#{mormanManager.myGarbage.garbage.percentageWaste}%" /></strong>
								<br />
								<br />
								<h:outputText escape="false" id="GarbageStatusLabel"
									value="Starea gunoiului: #{mormanManager.myGarbage.garbage.status}" />
								<br />
								<%----<h:form
							rendered="#{mormanManager.userDetails.role eq 'ADMIN' or mormanManager.userDetails.role eq 'ORGANIZER_MULTI' or mormanManager.userDetails.role eq 'ORGANIZER' or areaCleanManager.teamSelected.isGarbageIdinTeam(mormanManager.myGarbage.garbage.garbageId)}">
							<h:commandLink action="#{mormanManager.actionChangeStare}"
								rerender="GarbageStatusLabel">
								<f:param name="garbageId"
									value="#{mormanManager.myGarbage.garbage.garbageId}" />
								<h:outputText value="Marcheaza ca morman curatat" escape="false"
									rendered="#{not (mormanManager.cleaned)}" />
								<h:outputText value="Marcheaza ca morman necuratat"
									escape="false" rendered="#{mormanManager.cleaned}" />
							</h:commandLink>
						</h:form> --%>

								<a4j:commandLink
									rendered="#{mormanManager.userDetails.role eq 'ADMIN' or mormanManager.userDetails.role eq 'ORGANIZER_MULTI' or mormanManager.userDetails.role eq 'ORGANIZER'}"
									actionListener="#{mormanManager.actionSelectGarbage}"
									reRender="popup_garbage_set_cleaned" id="popupLinkToClean"
									ajaxSingle="true"
									oncomplete="#{rich:component('popup_garbage_set_cleaned')}.show();">
									<strong> <h:outputText
											value="Marcheaza ca morman curatat" escape="false"
											rendered="#{not (mormanManager.cleaned)}" /> <h:outputText
											value="Marcheaza ca morman necuratat" escape="false"
											rendered="#{mormanManager.cleaned}" />
									</strong>
								</a4j:commandLink>

								<br />
								<br />
								<h:outputText escape="false"
									value="#{msg.details_coords_latitude} " />
								<strong><h:outputText
										value="#{mormanManager.myGarbage.garbage.y}" /></strong>
								<br />
								<h:outputText escape="false"
									value="#{msg.details_coords_longitude} " />
								<strong><h:outputText
										value="#{mormanManager.myGarbage.garbage.x}" /></strong>
								<br />
								<br />
								<h:outputText escape="false" value="#{msg.details_add_date }" />
								<strong> <h:outputText
										value="#{mormanManager.myGarbage.garbage.recordDate}">
										<f:convertDateTime pattern="dd MMM yyyy" />
									</h:outputText>
								</strong>
								<br />
								<br />

								<a4j:form rendered="#{fn:length(mormanManager.thumbnails) > 0}">
									<h3>
										<h:outputText escape="false"
											value="#{msg.details_photo_gallery}" />
									</h3>
									<a4j:commandLink
										actionListener="#{mormanManager.actionSetSelectedImage}"
										reRender="selected_image"
										oncomplete="#{rich:component('img_gallery')}.show();">
										<f:param name="imgIndex" value="0" />
										<h:graphicImage value="#{mormanManager.thumbnails[0]}"
											width="85" styleClass="galerie" />
									</a4j:commandLink>
									<a4j:commandLink
										actionListener="#{mormanManager.actionSetSelectedImage}"
										reRender="selected_image"
										oncomplete="#{rich:component('img_gallery')}.show();"
										rendered="#{fn:length(mormanManager.thumbnails) > 1}">
										<f:param name="imgIndex" value="1" />
										<h:graphicImage value="#{mormanManager.thumbnails[1]}"
											width="85" styleClass="galerie" />
									</a4j:commandLink>

									<br />
									<a4j:commandLink
										onclick="#{rich:component('img_gallery')}.show();"
										rendered="#{fn:length(mormanManager.thumbnails) > 2}">
										<h:outputText escape="false" value="#{msg.details_all_photos}" />
									</a4j:commandLink>
								</a4j:form>
								<br />
								<br />
						<%--		<h3>
									<h:outputLink value="pachetmorman.jsf">
										<f:param name="garbageId"
											value="#{mormanManager.myGarbage.garbage.garbageId}" />
										<h:outputText escape="false" value="» Pachet pentru print" />
									</h:outputLink>
								</h3>--%>
								<%--
								<h3>
									<h:form rendered="true">
										
										<h:commandLink action="#{mormanManager.actionAssignMorman}"
											id="assignActionID">
											<f:param name="addGarbageId"
												value="#{mormanManager.myGarbage.garbage.garbageId}" />
											<h:outputText value="#{msg.details_assign_link}"
												escape="false" />
										</h:commandLink>
									</h:form>
									<h:form rendered="#{mormanManager.mormanAlocat}">
										<h:commandLink
											action="#{mormanManager.actionRemoveMormanFromTeam}">
											<f:param name="removeGarbageId"
												value="#{mormanManager.myGarbage.garbage.garbageId}" />
											<h:outputText value="» Dez-aloca morman" escape="false" />
										</h:commandLink>
									</h:form>
								</h3>--%>


								<a4j:commandLink
									actionListener="#{mormanManager.actionSelectGarbageForAllocate}"
									reRender="popup_garbage_set_allocated" id="popupLinkToAllocate"
									ajaxSingle="true"
									oncomplete="#{rich:component('popup_garbage_set_allocated')}.show();"
									>
									<strong> <h:outputText value="» Aloca gunoiul"
											escape="false" rendered="#{not (mormanManager.mormanAlocat)}" />
										<h:outputText value="» Renunta la gunoi" escape="false"
											rendered="#{mormanManager.mormanAlocat}" /> <f:param
											name="garbageId"
											value="#{mormanManager.myGarbage.garbage.garbageId}" /> <f:param
											name="TEAM_SELECTED"
											value="#{areaCleanManager.teamSelected.teamId}" />
									</strong>
								</a4j:commandLink>

								<br />
								<h3>
									<h:outputLink value="curatenie-vizualizare.jsf">
										<h:outputText escape="false" value="#{msg.details_lista_link}" />
									</h:outputLink>
								</h3>

							</h:panelGroup>

						</a4j:form>
					</a4j:outputPanel>
				</div>

				<%-- Right Column (harta) --%>
				<div id="rightColumn">
					<a4j:outputPanel id="hartaAlocari">
						<h:messages warnClass="registerMessageError"
							infoClass="registerMessageOk" />
						<br />
						<div id="map" style="width: 100%; height: 600px"></div>
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
