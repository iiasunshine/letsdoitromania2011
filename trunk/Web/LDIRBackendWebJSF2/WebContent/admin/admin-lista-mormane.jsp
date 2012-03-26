<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:directive.include file="/WEB-INF/jspf/page-header.jspf" />
<jsp:directive.include file="/WEB-INF/jspf/login-admin-checkpoint.jspf" />
<f:view>
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:directive.include file="/WEB-INF/jspf/page-meta.jspf" />
<title>Let's do it Romania</title>
</head>
<body>
	<center>
		<%-- page Top --%>
		<custom:page_top_login selected="lista_mormane"
			role="${adminGarbageManagerBean.userDetails.role}" />

		<%-- page Content --%>
		<div id="pageContainer">
			<div id="contentList">
				<jsp:directive.include file="/WEB-INF/jspf/popup-loading.jspf" />
				<jsp:directive.include
					file="/WEB-INF/jspf/popup-admin-img-gallery.jspf" />
				<jsp:directive.include
					file="/WEB-INF/jspf/popup-garbage-delete.jspf" />
				<jsp:directive.include
					file="/WEB-INF/jspf/popup-garbage-nominate.jspf" />
				<jsp:directive.include
					file="/WEB-INF/jspf/popup-garbage-toClean.jspf" />

				<%-- Left Column --%>
				<div id="leftColumn">
					<div class="labelLeft">
						<h1>Filtrare dupa:</h1>
					</div>
					<a4j:form>
						<div>
							<h3>
								Judet<br />
								<h:selectOneMenu value="#{adminGarbageManagerBean.countyId}">
									<f:selectItem itemLabel="Toate" itemValue="Toate" />
									<f:selectItems value="#{adminGarbageManagerBean.countyItems}" />
								</h:selectOneMenu>
							</h3>
						</div>
						<div>
							<h3>
								ID zona cartare<br />
								<h:inputText value="#{adminGarbageManagerBean.gridId}"
									onkeypress="return numbersonly(this, event, false);"
									style="width:75px;" />
							</h3>
						</div>
						<div>
							<h3>
								ID utilizator<br />
								<h:inputText value="#{adminGarbageManagerBean.userId}"
									onkeypress="return numbersonly(this, event, false);"
									style="width:75px;" />
							</h3>
						</div>
						<div>
							<h3>
								data introducerii <br />
								<rich:calendar value="#{adminGarbageManagerBean.addDate}" />
							</h3>
						</div>

						<a4j:commandButton
							actionListener="#{adminGarbageManagerBean.actionApplyFilter}"
							reRender="lista-gunoaie" styleClass="formButtonLeft"
							onclick="#{rich:component('popup-loading')}.show();"
							oncomplete="#{rich:component('popup-loading')}.hide();"
							value="Aplica filtrul" />
						<button type="reset" class="formButtonLeft">Anuleaza</button>
					</a4j:form>
				</div>

				<%-- Right Column (harta) --%>
				<div id="rightColumnList" style="min-height: 300px;">
					<a4j:outputPanel id="lista-gunoaie">
						<h:messages warnClass="registerMessageError"
							infoClass="registerMessageOk" />

						<a4j:form>
							<h1>
								<h:outputFormat value="Lista mormane({0})">
									<f:param
										value="#{fn:length(adminGarbageManagerBean.garbageList)}" />
								</h:outputFormat>
								<br /> <br />
								<h:commandButton
									actionListener="#{adminGarbageManagerBean.actionGenerateExcel}"
									rendered="#{fn:length(adminGarbageManagerBean.garbageList) gt 0}"
									styleClass="formButtonLeft" value="Export lista mormane" />
							</h1>
							<h:outputText
								value="Exporturile Excel contin mai multe campuri decat sunt afisate pe pagina web."
								rendered="#{fn:length(adminGarbageManagerBean.garbageList) gt 0}" />
							<br />
							<h:outputText
								value="Daca modifici un morman folosind butonul din tabelul de mai jos trebuie sa reincarci lista pentru a vedea modificarile."
								rendered="#{fn:length(adminGarbageManagerBean.garbageList) gt 0}" />
							<br />
							<br />

							<div id="listHeaderContainer">
								<div class="listHeader">ID</div>
								<div class="listHeader">Nume morman</div>
								<div class="listHeader">Judet</div>
								<div class="listHeaderLarge">Descriere</div>

								<div class="listHeader">Stare morman</div>
								<div class="listHeader">Dispersat</div>
								<div class="listHeader">Raza[m]</div>
								<div class="listHeader">Numar saci</div>
								<div class="listHeaderLarge">Galerie foto</div>
								<div class="listHeaderLarge">Optiuni</div>
							</div>

							<a4j:repeat value="#{adminGarbageManagerBean.garbageList}"
								var="garbage">
								<div id="listEntryContainer">
									<div class="listEntry">
										<h:outputText value="#{garbage.garbageId}" />
									</div>
									<div class="listEntry">
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
										<h:outputText value="#{garbage.status}" />
									</div>
									<div class="listEntry">
										<h:outputText value="#{garbage.dispersed ? 'DA' : 'NU'}" />
									</div>
									<div class="listEntry">
										<h:outputText value="#{garbage.radius}" />
									</div>
									<%--<div class="listEntryLarge">
                                                <rich:panel rendered="#{fn:length(garbage.bigComponentsDescription) gt 0}"
                                                            onmouseover="this.style.cursor='help'">
                                                    <h:outputText value="#{fn:substring(garbage.bigComponentsDescription, 0 , 30)} ..."
                                                                  rendered="#{fn:length(garbage.bigComponentsDescription) gt 30}"/>
                                                    <h:outputText value="#{garbage.bigComponentsDescription}"
                                                                  rendered="#{not (fn:length(garbage.bigComponentsDescription) gt 30)}"/>
                                                    <rich:toolTip >
                                                        <h:outputText value="#{garbage.bigComponentsDescription}"/>
                                                    </rich:toolTip>
                                                </rich:panel>
                                            </div> --%>
									<div class="listEntry">
										<h:outputText value="#{garbage.bagCount}" />
									</div>
									<%--div class="listEntryLarge">
                                                <h:outputText value="#{garbage.x}">
                                                    <f:convertNumber maxFractionDigits="6"/>
                                                </h:outputText>
                                                <h:outputText value="/"/>
                                                <h:outputText value="#{garbage.y}">
                                                    <f:convertNumber maxFractionDigits="6"/>
                                                </h:outputText>
                                            </div> --%>
									<div class="listEntryLarge">
										<a4j:commandLink
											actionListener="#{adminGarbageManagerBean.actionSelectGarbage}"
											rendered="#{fn:length(garbage.pictures) gt 0}"
											reRender="img_gallery" ajaxSingle="true"
											onclick="#{rich:component('popup-loading')}.show();"
											oncomplete="#{rich:component('popup-loading')}.hide(); #{rich:component('img_gallery')}.show();">
											<f:param name="garbageId" value="#{garbage.garbageId}" />
											<strong><h:outputText
													value="DESCHIDE (#{fn:length(garbage.pictures)})" /></strong>
										</a4j:commandLink>
									</div>


									<div class="listEntryLarge">
										<%-- Delete --%>
										<a4j:commandLink
											actionListener="#{adminGarbageManagerBean.actionSelectGarbage}"
											reRender="popup_garbage_delete" id="delete" ajaxSingle="true"
											oncomplete="#{rich:component('popup_garbage_delete')}.show();">
											<f:param name="garbageId" value="#{garbage.garbageId}" />
											<strong>STERGE</strong>
										</a4j:commandLink>

										<h:outputLink
											value="#{pageContext.servletContext.contextPath}/users/cartare-mormane-editare.jsf?garbageId=#{garbage.garbageId}">
											<h:outputText value="MODIFICA" escape="false" />
										</h:outputLink>
										<br />
										<h:panelGroup rendered="#{garbage.dispersed}">
											<%-- nominate to vote --%>
											<a4j:commandLink
												actionListener="#{adminGarbageManagerBean.actionSelectGarbage}"
												reRender="popup_garbage_nominate" id="popupLinkNominate"
												ajaxSingle="true"
												oncomplete="#{rich:component('popup_garbage_nominate')}.show();">
												<f:param name="garbageId" value="#{garbage.garbageId}" />
												<strong> <h:outputText value="De-nominalizeaza"
														rendered="#{garbage.toVote}" /> <h:outputText
														value="Nominalizeaza" rendered="#{not garbage.toVote}" />
												</strong>
											</a4j:commandLink>
											<br />
											<%-- nominate to clean --%>
											<a4j:commandLink
												actionListener="#{adminGarbageManagerBean.actionSelectGarbage}"
												reRender="popup_garbage_toClean" id="popupLinkToClean"
												ajaxSingle="true"
												oncomplete="#{rich:component('popup_garbage_toClean')}.show();">
												<f:param name="garbageId" value="#{garbage.garbageId}" />
												<strong> <h:outputText value="Nu se curata"
														rendered="#{garbage.toClean}" /> <h:outputText
														value="De curatat" rendered="#{not garbage.toClean}" />
												</strong>
											</a4j:commandLink>
										</h:panelGroup>
									</div>
								</div>
							</a4j:repeat>
						</a4j:form>

						<h:panelGroup rendered="#{adminGarbageManagerBean.noFilter}">
							<br />
							<br />
							<br />
							<h3>
								<h:outputText
									value="Aplicati cel cel putin un criteriu de filtrare!" />
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
