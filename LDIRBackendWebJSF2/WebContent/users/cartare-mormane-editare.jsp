<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:directive.include file="/WEB-INF/jspf/page-header.jspf"/>
<jsp:directive.include file="/WEB-INF/jspf/login-checkpoint.jspf"/>
<f:view>
    <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
            <jsp:directive.include file="/WEB-INF/jspf/page-meta.jspf"/>
            <title>Let's do it Romania</title>
        </head>
        <body>
            <center>
                <%-- page Top --%>
                <custom:page_top_login selected="mormane" role="${mormanManager.userDetails.role}"/>

                <%-- page Content --%>
                <div id="pageContainer">
                    <div id="content">
                        <%-- Left Column (lista mormane adaugate pana acum) --%>
                        <div id="leftColumn">
                            <h:panelGroup rendered="#{fn:length(mormanManager.myGarbageList) eq 0}">
                                <h1><h:outputText value="#{msg.chart_empty_list}"/></h1>
                            </h:panelGroup>
                            <h:panelGroup rendered="#{fn:length(mormanManager.myGarbageList) gt 0}">
                                <h1><h:outputText value="#{msg.chart_list_title} (#{fn:length(mormanManager.myGarbageList)})"/></h1>
                                <h:panelGroup rendered="#{fn:length(mormanManager.myGarbageList) gt 0}"
                                              style="#{fn:length(mormanManager.myGarbageList) gt 15 ? 'max-height: 610px; overflow: scroll; display: block;' : ''}">
                                <a4j:repeat value="#{mormanManager.myGarbageList}"
                                            var="myGarbage">
                                    <div class="entryLeft">
                                        <h:outputText value="Morman:"/>
                                        <strong><h:outputText value="#{myGarbage.garbage.garbageId}"/></strong>
                                        <br/>
                                        <h:outputLink value="cartare-mormane-detalii.jsf?garbageId=#{myGarbage.garbage.garbageId}">
                                            <h:outputText value="» vizualizeaza"/>
                                        </h:outputLink>
                                    </div>
                                </a4j:repeat>
                                </h:panelGroup>
                            </h:panelGroup>
                        </div>

                        <%-- Right Column (formular adaugare/editare morman) --%>
                        <div id="rightColumn">
                            <!-- mesaj eroare sau info -->
                            <h:messages warnClass="registerMessageError" errorClass="registerMessageError" infoClass="registerMessageOk"/>

                            <!-- titlu formular -->
                            <h1>
                                <h:outputText value="#{msg.chart_add_morman_link}"
                                              rendered="#{not (mormanManager.myGarbage.garbage.garbageId > 0)}"/>
                                <h:outputText value="#{msg.chart_modify_morman} #{mormanManager.myGarbage.garbage.garbageId}"
                                              rendered="#{mormanManager.myGarbage.garbage.garbageId > 0}"/>
                            </h1>
                            <a4j:keepAlive  beanName="mormanManager"/>
                            <h:form styleClass="form" id="form" enctype="multipart/form-data">
                                <!-- descriere -->
                                <div class="label"><h:outputText value="#{msg.chart_add_description}"/><span class="important">*</span></div>
                                <h:inputTextarea value="#{mormanManager.myGarbage.garbage.description}"
                                                 cols="45" rows="5"
                                                 id="descriere"
                                                 styleClass="formTextarea"
                                                 validatorMessage="#{msg.chart_err_description}">
                                    <f:validateLength minimum="0" maximum="1000"/>
                                </h:inputTextarea>
                                <br />
                                <br />

                                <!-- reprezentare coordonate (zecimale/grade) -->
                                <h:panelGroup id="coordonate">
                                    <div class="label"><h:outputText value="#{msg.chart_add_coord_types}"/></div>
                                    <h:selectBooleanCheckbox value="#{mormanManager.coord_zecimale}"
                                                             disabled="#{mormanManager.coord_zecimale}">
                                        <a4j:support event="onchange"
                                                     ajaxSingle="true"
                                                     reRender="form"/>
                                    </h:selectBooleanCheckbox>
                                    <h:outputText value="#{msg.chart_add_coord_decimals}"/>
                                    <h:selectBooleanCheckbox value="#{mormanManager.coord_grade}"
                                                             disabled="#{mormanManager.coord_grade}">
                                        <a4j:support event="onchange"
                                                     ajaxSingle="true"
                                                     reRender="form"/>
                                    </h:selectBooleanCheckbox>
                                    <h:outputText value="#{msg.chart_add_coord_degrees}"/>
                                    <br />

                                    <!-- coordonate (latitudine) -->
                                    <div class="label"><h:outputText value="#{msg.chart_add_latitudine}"/><span class="important">*</span></div>
                                    <h:panelGroup rendered="#{mormanManager.coord_zecimale}">
                                        <h:inputText value="#{mormanManager.latitudine}"
                                                     onkeypress="return numbersonly(this, event, true);"
                                                     styleClass="formTextfield"
                                                     id="coordonate-y-dec"
                                                     validatorMessage="#{msg.chart_err_latitude}"
                                                     converterMessage="#{msg.chart_err_latitude}">
                                            <f:validateDoubleRange minimum="43" maximum="49"/>
                                        </h:inputText>
                                    </h:panelGroup>
                                    <h:panelGroup rendered="#{mormanManager.coord_grade}">
                                        <h:inputText value="#{mormanManager.lat_grd}"
                                                     onkeypress="return numbersonly(this, event, false);"
                                                     styleClass="formTextfieldSmall"
                                                     id="coordonate-y-grd"
                                                     converterMessage="#{msg.chart_err_degrees}"/>
                                        <div class="labelSmall"><h:outputText value="°"/></div>
                                        <h:inputText value="#{mormanManager.lat_min}"
                                                     onkeypress="return numbersonly(this, event, false);"
                                                     styleClass="formTextfieldSmall"
                                                     id="coordonate-y-min"
                                                     converterMessage="#{msg.chart_err_degrees}"/>
                                        <div class="labelSmall"><h:outputText value="'"/></div>
                                        <h:inputText value="#{mormanManager.lat_sec}"
                                                     onkeypress="return numbersonly(this, event, true);"
                                                     styleClass="formTextfieldSmall"
                                                     id="coordonate-y-sec"
                                                     converterMessage="#{msg.chart_err_degrees}"/>
                                        <div class="labelSmall"><h:outputText value="\""/></div>
                                    </h:panelGroup>
                                    <br />

                                    <!-- coordonate (longitudine) -->
                                    <div class="label"><h:outputText value="#{msg.chart_add_longitudine}"/><span class="important">*</span></div>
                                    <h:panelGroup rendered="#{mormanManager.coord_zecimale}">
                                        <h:inputText value="#{mormanManager.longitudine}"
                                                     onkeypress="return numbersonly(this, event, true);"
                                                     styleClass="formTextfield"
                                                     id="coordonate-x-dec"
                                                     validatorMessage="#{msg.chart_err_longitude}"
                                                     converterMessage="#{msg.chart_err_longitude}">
                                            <f:validateDoubleRange minimum="20" maximum="30"/>
                                        </h:inputText>
                                    </h:panelGroup>
                                    <h:panelGroup rendered="#{mormanManager.coord_grade}">
                                        <h:inputText value="#{mormanManager.long_grd}"
                                                     onkeypress="return numbersonly(this, event, false);"
                                                     styleClass="formTextfieldSmall"
                                                     id="coordonate-x-grd"
                                                     converterMessage="#{msg.chart_err_degrees}"/>
                                        <div class="labelSmall"><h:outputText value="°"/></div>
                                        <h:inputText value="#{mormanManager.long_min}"
                                                     onkeypress="return numbersonly(this, event, false);"
                                                     styleClass="formTextfieldSmall"
                                                     id="coordonate-x-min"
                                                     converterMessage="#{msg.chart_err_degrees}"/>
                                        <div class="labelSmall"><h:outputText value="'"/></div>
                                        <h:inputText value="#{mormanManager.long_sec}"
                                                     onkeypress="return numbersonly(this, event, true);"
                                                     styleClass="formTextfieldSmall"
                                                     id="coordonate-x-sec"
                                                     converterMessage="#{msg.chart_err_degrees}"/>
                                        <div class="labelSmall"><h:outputText value="\""/></div>
                                    </h:panelGroup>
                                    <br />
                                </h:panelGroup>

                                <!-- gunoi dispersat -->
                                <div class="label"><h:outputText value="#{msg.chart_add_dispersat}"/></div>
                                <h:selectBooleanCheckbox value="#{mormanManager.myGarbage.garbage.dispersed}" styleClass="formDate" id="dispersat"/>
                                <br />

                                <!-- numar saci -->
                                <div class="label"><h:outputText value="#{msg.chart_add_bags_nr}"/></div>
                                <h:selectOneMenu value="#{mormanManager.myGarbage.garbage.bagCount}" styleClass="formDropdown" id="saci">
                                    <f:selectItems value="#{mormanManager.saciNrItems}"/>
                                </h:selectOneMenu>
                                <br />

                                <!-- descriere componente voluminoase -->
                                <div class="label"><h:outputText value="#{msg.chart_add_description2}"/></div>
                                <h:inputTextarea value="#{mormanManager.myGarbage.garbage.bigComponentsDescription}"
                                                 cols="45" rows="5"
                                                 styleClass="formTextarea"
                                                 id="descriere2"validatorMessage="#{msg.chart_err_description2}">
                                    <f:validateLength minimum="0" maximum="1000"/>
                                </h:inputTextarea>
                                <br />
                                <br />

                                <!-- procent plastic -->
                                <div class="label"><h:outputText value="#{msg.chart_add_plastic}"/></div>
                                <h:inputText value="#{mormanManager.myGarbage.garbage.percentagePlastic}"
                                             onkeypress="return numbersonly(this, event, false);"
                                             styleClass="formTextfield"
                                             id="plastic"
                                             validatorMessage="#{msg.chart_js_err_plastic}"
                                             converterMessage="#{msg.chart_js_err_plastic}">
                                    <f:validateLongRange maximum="100" minimum="0"/>
                                </h:inputText>

                                <br />

                                <!-- procent metal -->
                                <div class="label"><h:outputText value="#{msg.chart_add_metal}"/></div>
                                <h:inputText value="#{mormanManager.myGarbage.garbage.percentageMetal}"
                                             onkeypress="return numbersonly(this, event);"
                                             styleClass="formTextfield"
                                             id="metal"
                                             validatorMessage="#{msg.chart_js_err_metal}"
                                             converterMessage="#{msg.chart_js_err_metal}">
                                    <f:validateLongRange maximum="100" minimum="0"/>
                                </h:inputText>
                                <br />

                                <!-- procent sticla -->
                                <div class="label"><h:outputText value="#{msg.chart_add_sticla}"/></div>
                                <h:inputText value="#{mormanManager.myGarbage.garbage.percentageGlass}"
                                             onkeypress="return numbersonly(this, event);"
                                             styleClass="formTextfield"
                                             id="sticla"
                                             validatorMessage="#{msg.chart_js_err_glass}"
                                             converterMessage="#{msg.chart_js_err_glass}">
                                    <f:validateLongRange maximum="100" minimum="0"/>
                                </h:inputText>
                                <br />

                                <!-- procent nereciclabil -->
                                <div class="label"><h:outputText value="#{msg.chart_add_nereciclabile}"/></div>
                                <h:inputText value="#{mormanManager.myGarbage.garbage.percentageWaste}"
                                             onkeypress="return numbersonly(this, event);"
                                             styleClass="formTextfield"
                                             id="nereciclabil"
                                             validatorMessage="#{msg.chart_js_err_waste}"
                                             converterMessage="#{msg.chart_js_err_waste}">
                                    <f:validateLongRange maximum="100" minimum="0"/>
                                </h:inputText>

                                <!-- imagine 1 -->
                                <br />
                                <div class="label"><h:outputText value="#{msg.chart_add_image1}"/></div>
                                <t:inputFileUpload value="#{mormanManager.uploadedFile1}"
                                                   id="imagine1"
                                                   styleClass="formTextfield"
                                                   storage="file"/>
                                <!-- imagine 2 -->
                                <br />
                                <div class="label"><h:outputText value="#{msg.chart_add_image2}"/></div>
                                <t:inputFileUpload value="#{mormanManager.uploadedFile2}"
                                                   id="imagine2"
                                                   styleClass="formTextfield"
                                                   storage="file"/>
                                <!-- imagine 3 -->
                                <br />
                                <div class="label"><h:outputText value="#{msg.chart_add_image3}"/></div>
                                <t:inputFileUpload value="#{mormanManager.uploadedFile3}"
                                                   id="imagine3"
                                                   styleClass="formTextfield"
                                                   storage="file"/>


                                <!-- buton adaugare -->
                                <br />
                                <br />
                                <div style="margin: 0px 0px 0px 143px;">
                                    <h:commandButton styleClass="formButton"
                                                     value="#{mormanManager.myGarbage.garbage.garbageId > 0 ? msg.chart_modify_morman : msg.chart_add_morman_link}"
                                                     action="#{mormanManager.actionEditMorman}">
                                        <f:param name="garbageId" value="#{mormanManager.myGarbage.garbage.garbageId}"/>
                                    </h:commandButton>
                                </div>
                            </h:form>
                        </div>
                    </div>
                </div>

                <%-- page Bottom --%>
                <custom:page_bottom/>
            </center>
        </body>
    </html>
</f:view>
