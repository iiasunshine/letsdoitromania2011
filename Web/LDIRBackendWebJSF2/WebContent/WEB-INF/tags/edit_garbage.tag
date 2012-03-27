<%-- 
	Edit garbage for everybody

--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib prefix="a4j" uri="http://richfaces.org/a4j"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@tag isELIgnored="false" body-content="tagdependent"%>
<%@attribute name="role"%>



<%-- Left Column (lista mormane adaugate pana acum) 
		<div id="leftColumn">
		<h:panelGroup
				rendered="#{not (sessionScope['USER_DETAILS'] eq null)}">
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
							value="#{msg.chart_list_title} (#{fn:length(mormanManager.myGarbageList)})" />
					</h1>
					<h:panelGroup
						rendered="#{fn:length(mormanManager.myGarbageList) gt 0}"
						style="#{fn:length(mormanManager.myGarbageList) gt 15 ? 'max-height: 610px; overflow: scroll; display: block;' : ''}">
						<a4j:repeat value="#{mormanManager.myGarbageList}" var="myGarbage">
							<div class="entryLeft">
								<h:outputText value="Morman:" />
								<strong><h:outputText
										value="#{myGarbage.garbage.garbageId}" /></strong> <br />
								<c:if
									test="${(role eq 'ORGANIZER')or (role eq 'ORGANIZER_MULTI')}">
									<h:outputLink
										value="cartare-mormane-detalii.jsf?garbageId=#{myGarbage.garbage.garbageId}">
										<h:outputText value="» vizualizeaza" />
									</h:outputLink>
								</c:if>
								<c:if test="${not (role eq 'VOLUNTEER')}">
									<h:outputLink
										value="cartare-mormane-detalii.jsf?garbageId=#{myGarbage.garbage.garbageId}">
										<h:outputText value="» vizualizeaza" />
									</h:outputLink>
								</c:if>
							</div>
						</a4j:repeat>
					</h:panelGroup>
				</h:panelGroup>
			</h:panelGroup> 
		</div>
--%>

<%-- Right Column (formular adaugare/editare morman) --%>
<div id="rightColumn">
	<!-- mesaj eroare sau info -->
	<a4j:keepAlive beanName="mormanManager" />
	<h:messages warnClass="registerMessageError"
		errorClass="registerMessageError" infoClass="registerMessageOk" />

	<!-- titlu formular -->
	<h1>
		<h:outputText value="#{msg.chart_add_morman_link}"
			rendered="#{not (mormanManager.myGarbage.garbage.garbageId > 0)}" />
		<h:outputText
			value="#{msg.chart_modify_morman} #{mormanManager.myGarbage.garbage.garbageId}"
			rendered="#{mormanManager.myGarbage.garbage.garbageId > 0}" />
	</h1>
	<h:outputText value="Campurile cu " />
	<span class="important">*</span>
	<h:outputText value=" sunt obligatorii" />
	<br />
	<h:outputText
		value="Suma procentelor plastic, metal, sticla si nereciclabile trebuie sa fie 100" />

	<h:form styleClass="form" id="form" enctype="multipart/form-data">
		<br />
		<br />

		<!-- gunoi dispersat -->

		<div class="label">
			<h:outputText value="#{msg.chart_add_dispersat}" />
		</div>
		<h:selectBooleanCheckbox
			value="#{mormanManager.myGarbage.garbage.dispersed}"
			styleClass="formDate" id="dispersat">
			<a4j:support event="onclick" ajaxSingle="true" reRender="radius,name" />
		</h:selectBooleanCheckbox>
		<br />

		<!--  cat de dispersat: -->
		<div class="label">
			<h:outputText value="#{msg.chart_add_radius}" />
		</div>
		<h:inputText value="#{mormanManager.myGarbage.garbage.radius}"
			styleClass="formTextfield" id="radius"
			validatorMessage="#{msg.chart_err_radius}"
			converterMessage="#{msg.chart_err_radius}"
			disabled="#{mormanManager.radiusDisabled}">
			<f:validateLongRange maximum="10000" minimum="1" />
		</h:inputText>
		<br />
		<br />

		<c:if
			test="${(role eq 'ADMIN') or (role eq 'ORGANIZER')or (role eq 'ORGANIZER_MULTI') }">
			<!--  garbage name -->
			<div class="label">
				<h:outputText value="#{msg.chart_add_name}" />
			</div>


			<h:inputText value="#{mormanManager.myGarbage.garbage.name}"
				id="name" styleClass="formTextfieldLeft" />
			<br />
			<br />

		</c:if>
		<c:if
			test="${(role eq 'ADMIN') or (role eq 'ORGANIZER')or (role eq 'ORGANIZER_MULTI') and (mormanManager.myGarbage.garbage.garbageId > 0) }">
			<!-- nominalizat si de curatat -->

			<h:commandButton styleClass="formButton" id="toVoteButton"
				value="#{mormanManager.myGarbage.garbage.toVote ? msg.chart_add_removeNominate : msg.chart_add_nominate}"
				action="#{mormanManager.actionToVote}">
				<a4j:support event="onclick" ajaxSingle="true"
					reRender="toVoteButton" />
			</h:commandButton>

			<h:commandButton styleClass="formButton" id="toCleanButton"
				value="#{mormanManager.myGarbage.garbage.toClean ? msg.chart_add_removeToClean : msg.chart_add_toClean}"
				action="#{mormanManager.actionToClean}">
				<a4j:support event="onclick" ajaxSingle="true"
					reRender="toCleanButton" />
			</h:commandButton>
			<br />
			<br />

		</c:if>

		<!-- reprezentare coordonate (zecimale/grade) -->
		<h:panelGroup id="coordonate">
			<div class="label">
				<h:outputText value="#{msg.chart_add_coord_types}" />
			</div>
			<h:selectBooleanCheckbox value="#{mormanManager.coord_zecimale}"
				disabled="#{mormanManager.coord_zecimale}">
				<a4j:support event="onchange" ajaxSingle="true" reRender="form" />
			</h:selectBooleanCheckbox>
			<h:outputText value="#{msg.chart_add_coord_decimals}" />
			<h:selectBooleanCheckbox value="#{mormanManager.coord_grade}"
				disabled="#{mormanManager.coord_grade}">
				<a4j:support event="onchange" ajaxSingle="true" reRender="form" />
			</h:selectBooleanCheckbox>
			<h:outputText value="#{msg.chart_add_coord_degrees}" />
			<br />
			<br />




			<!-- coordonate (latitudine) -->
			<div class="label">
				<h:outputText value="#{msg.chart_add_latitudine}" />
				<span class="important">*</span>
			</div>
			<h:panelGroup rendered="#{mormanManager.coord_zecimale}">
				<h:inputText value="#{mormanManager.latitudine}"
					onkeypress="return numbersonly(this, event, true);"
					styleClass="formTextfield" id="coordonate-y-dec"
					validatorMessage="#{msg.chart_err_latitude}"
					converterMessage="#{msg.chart_err_latitude}">
					<f:validateDoubleRange minimum="43.0" maximum="49.0" />
				</h:inputText>
			</h:panelGroup>
			<h:panelGroup rendered="#{mormanManager.coord_grade}">
				<h:inputText value="#{mormanManager.lat_grd}"
					onkeypress="return numbersonly(this, event, false);"
					styleClass="formTextfieldSmall" id="coordonate-y-grd"
					converterMessage="#{msg.chart_err_degrees}">
					<f:validateLongRange minimum="43" maximum="49" />
				</h:inputText>
				<div class="label">
					<h:outputText value="°" />
				</div>
				<br />
				<div class="label">
					<h:outputText value="minute" />
				</div>
				<h:inputText value="#{mormanManager.lat_min}"
					onkeypress="return numbersonly(this, event, false);"
					styleClass="formTextfieldSmall" id="coordonate-y-min"
					converterMessage="#{msg.chart_err_degrees}" />
				<div class="label">
					<h:outputText value="'" />
				</div>
				<br />
				<div class="label">
					<h:outputText value="secunde" />
				</div>
				<h:inputText value="#{mormanManager.lat_sec}"
					onkeypress="return numbersonly(this, event, true);"
					styleClass="formTextfieldSmall" id="coordonate-y-sec"
					converterMessage="#{msg.chart_err_degrees}" />
				<div class="label">
					<h:outputText value="\"" />
				</div>
			</h:panelGroup>
			<br />

			<!-- coordonate (longitudine) -->
			<div class="label">
				<h:outputText value="#{msg.chart_add_longitudine}" />
				<span class="important">*</span>
			</div>
			<h:panelGroup rendered="#{mormanManager.coord_zecimale}">
				<h:inputText value="#{mormanManager.longitudine}"
					onkeypress="return numbersonly(this, event, true);"
					styleClass="formTextfield" id="coordonate-x-dec"
					validatorMessage="#{msg.chart_err_longitude}"
					converterMessage="#{msg.chart_err_longitude}">
					<f:validateDoubleRange minimum="20" maximum="30" />
				</h:inputText>
			</h:panelGroup>
			<h:panelGroup rendered="#{mormanManager.coord_grade}">
				<h:inputText value="#{mormanManager.long_grd}"
					onkeypress="return numbersonly(this, event, false);"
					styleClass="formTextfieldSmall" id="coordonate-x-grd"
					converterMessage="#{msg.chart_err_degrees}" />
				<div class="label">
					<h:outputText value="°" />
				</div>
				<br />
				<div class="label">
					<h:outputText value="minute" />
				</div>
				<h:inputText value="#{mormanManager.long_min}"
					onkeypress="return numbersonly(this, event, false);"
					styleClass="formTextfieldSmall" id="coordonate-x-min"
					converterMessage="#{msg.chart_err_degrees}" />
				<div class="label">
					<h:outputText value="'" />
				</div>
				<br />
				<div class="label">
					<h:outputText value="secunde" />
				</div>
				<h:inputText value="#{mormanManager.long_sec}"
					onkeypress="return numbersonly(this, event, true);"
					styleClass="formTextfieldSmall" id="coordonate-x-sec"
					converterMessage="#{msg.chart_err_degrees}" />
			</h:panelGroup>
			<div class="label">
				<h:outputText value="\"" />
			</div>
			<br />
		</h:panelGroup>


		<!-- numar saci -->
		<div class="label">
			<h:outputText value="#{msg.chart_add_bags_nr}" />
		</div>
		<h:selectOneMenu value="#{mormanManager.myGarbage.garbage.bagCount}"
			styleClass="formDropdown" id="saci">
			<f:selectItems value="#{mormanManager.saciNrItems}" />
		</h:selectOneMenu>
		<br />


		<!-- procent plastic -->
		<div class="label">
			<h:outputText value="#{msg.chart_add_plastic}" />
		</div>
		<h:inputText
			value="#{mormanManager.myGarbage.garbage.percentagePlastic}"
			onkeypress="return numbersonly(this, event, false);"
			styleClass="formTextfield" id="plastic"
			validatorMessage="#{msg.chart_js_err_plastic}"
			converterMessage="#{msg.chart_js_err_plastic}">
			<f:validateLongRange maximum="100" minimum="0" />
		</h:inputText>

		<br />

		<!-- procent metal -->
		<div class="label">
			<h:outputText value="#{msg.chart_add_metal}" />
		</div>
		<h:inputText
			value="#{mormanManager.myGarbage.garbage.percentageMetal}"
			onkeypress="return numbersonly(this, event);"
			styleClass="formTextfield" id="metal"
			validatorMessage="#{msg.chart_js_err_metal}"
			converterMessage="#{msg.chart_js_err_metal}">
			<f:validateLongRange maximum="100" minimum="0" />
		</h:inputText>
		<br />

		<!-- procent sticla -->
		<div class="label">
			<h:outputText value="#{msg.chart_add_sticla}" />
		</div>
		<h:inputText
			value="#{mormanManager.myGarbage.garbage.percentageGlass}"
			onkeypress="return numbersonly(this, event);"
			styleClass="formTextfield" id="sticla"
			validatorMessage="#{msg.chart_js_err_glass}"
			converterMessage="#{msg.chart_js_err_glass}">
			<f:validateLongRange maximum="100" minimum="0" />
		</h:inputText>
		<br />

		<!-- procent nereciclabil -->
		<div class="label">
			<h:outputText value="#{msg.chart_add_nereciclabile}" />
		</div>
		<h:inputText
			value="#{mormanManager.myGarbage.garbage.percentageWaste}"
			onkeypress="return numbersonly(this, event);"
			styleClass="formTextfield" id="nereciclabil"
			validatorMessage="#{msg.chart_js_err_waste}"
			converterMessage="#{msg.chart_js_err_waste}">
			<f:validateLongRange maximum="100" minimum="0" />
		</h:inputText>
		<br />
		<br />

		<!-- descriere componente voluminoase -->
		<div class="label">
			<h:outputText value="#{msg.chart_add_description2}" />
		</div>
		<h:inputTextarea
			value="#{mormanManager.myGarbage.garbage.bigComponentsDescription}"
			cols="45" rows="5" styleClass="formTextarea" id="descriere2"
			validatorMessage="#{msg.chart_err_description2}">
			<f:validateLength minimum="0" maximum="1000" />
		</h:inputTextarea>
		<div class="labelSmall">
			<h:outputText value="Maxim 1000 de caractere." />
		</div>
		<br />
		<br />
		<!-- descriere -->
		<div class="label">
			<h:outputText value="#{msg.chart_add_description}" />
			<span class="important">*</span>
		</div>
		<h:inputTextarea
			value="#{mormanManager.myGarbage.garbage.description}" cols="45"
			rows="5" id="descriere" styleClass="formTextarea"
			validatorMessage="#{msg.chart_err_description}">
			<f:validateLength minimum="0" maximum="1000" />
		</h:inputTextarea>
		<div class="labelSmall">
			<h:outputText value="Maxim 1000 de caractere." />
		</div>
		<br />
		<br />


		<!-- imagine 1 -->
		<br />
		<div class="label">
			<h:outputText value="#{msg.chart_add_image1}" />
		</div>
		<t:inputFileUpload value="#{mormanManager.uploadedFile1}"
			id="imagine1" styleClass="formTextfield" storage="file" />
		<!-- imagine 2 -->
		<br />
		<div class="label">
			<h:outputText value="#{msg.chart_add_image2}" />
		</div>
		<t:inputFileUpload value="#{mormanManager.uploadedFile2}"
			id="imagine2" styleClass="formTextfield" storage="file" />
		<!-- imagine 3 -->
		<br />
		<div class="label">
			<h:outputText value="#{msg.chart_add_image3}" />
		</div>
		<t:inputFileUpload value="#{mormanManager.uploadedFile3}"
			id="imagine3" styleClass="formTextfield" storage="file" />


		<!-- buton adaugare -->
		<br />
		<br />
		<div style="margin: 0px 0px 0px 143px;">
			<h:commandButton styleClass="formButton"
				value="#{mormanManager.myGarbage.garbage.garbageId > 0 ? msg.chart_modify_morman : msg.chart_add_morman_link}"
				action="#{mormanManager.actionEditMorman}">
				<f:param name="garbageId"
					value="#{mormanManager.myGarbage.garbage.garbageId}" />
			</h:commandButton>
		</div>
	</h:form>
</div>

