<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:directive.include file="/WEB-INF/jspf/page-header.jspf"/>
<jsp:directive.include file="/WEB-INF/jspf/login-admin-checkpoint.jspf"/>
<f:view>
    <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
            <jsp:directive.include file="/WEB-INF/jspf/page-meta.jspf"/>
            <title>Let's do it Romania</title>
        </head>
        <body>
            <center>
                <%-- page Top --%>
                <custom:page_top_login selected="lista_mormane" role="${adminGarbageManagerBean.userDetails.role}"/>

                <%-- page Content --%>
                <div id="pageContainer">
                    <div id="contentList">
                        <jsp:directive.include file="/WEB-INF/jspf/popup-loading.jspf"/>
                        <jsp:directive.include file="/WEB-INF/jspf/popup-admin-img-gallery.jspf"/>

                        <%-- Left Column --%>
                        <div id="leftColumn">
                            <div class="labelLeft">
                                <h1>Filtrare dupa:</h1>
                            </div>
                            <a4j:form>
                                <div>
                                    <h3>Judet<br/>
                                        <h:selectOneMenu value="#{adminGarbageManagerBean.countyId}">
                                            <f:selectItem itemLabel="Toate" itemValue=""/>
                                            <f:selectItems value="#{adminGarbageManagerBean.countyItems}"/>
                                        </h:selectOneMenu>
                                    </h3>
                                </div>
                                <div>
                                    <h3>ID zona cartare<br/>
                                        <h:inputText value="#{adminGarbageManagerBean.gridId}"
                                                     onkeypress="return numbersonly(this, event, false);"
                                                     style="width:75px;"/>
                                    </h3>
                                </div>
                                <div>
                                    <h3>ID utilizator<br/>
                                        <h:inputText value="#{adminGarbageManagerBean.userId}"
                                                     onkeypress="return numbersonly(this, event, false);"
                                                     style="width:75px;"/>
                                    </h3>
                                </div>
                                <div>
                                    <h3>data introducerii <br/>
                                        <rich:calendar value="#{adminGarbageManagerBean.addDate}"/>
                                    </h3>
                                </div>

                                <a4j:commandButton actionListener="#{adminGarbageManagerBean.actionApplyFilter}"
                                                   reRender="lista-gunoaie"
                                                   styleClass="formButtonLeft"
                                                   onclick="#{rich:component('popup-loading')}.show();"
                                                   oncomplete="#{rich:component('popup-loading')}.hide();"
                                                   value="Aplica filtrul"/>
                                <button type="reset"
                                        class="formButtonLeft">Anuleaza</button>
                            </a4j:form>
                        </div>

                        <%-- Right Column (harta) --%>
                        <div id="rightColumnList" style="min-height: 300px;">
                            <a4j:outputPanel id="lista-gunoaie">
                                <h:messages warnClass="registerMessageError" infoClass="registerMessageOk"/>

                                <a4j:form>
                                    <h1>
                                        <h:outputFormat value="Lista mormane({0})">
                                            <f:param value="#{fn:length(adminGarbageManagerBean.garbageList)}"/>
                                        </h:outputFormat>
                                        <br/>
                                        <br/>
                                        <a4j:commandButton actionListener="#{adminGarbageManagerBean.actionGenerateExcel}"
                                                           rendered="#{fn:length(adminGarbageManagerBean.garbageList) gt 0}"
                                                           styleClass="formButtonLeft"
                                                           value="Export lista mormane"/>
                                    </h1>
                                    <div id="listHeaderContainer">
                                        <div class="listHeader">ID morman</div>
                                        <div class="listHeader">Judet</div>
                                        <div class="listHeader">ID Grid</div>
                                        <div class="listHeaderLarge">Descriere</div>
                                        <div class="listHeader">ID user</div>
                                        <div class="listHeader">Stare morman</div>
                                        <div class="listHeader">Gunoi dispersat</div>
                                        <div class="listHeaderLarge">Gunoi voluminos</div>
                                        <div class="listHeader">Numar saci</div>
                                        <div class="listHeaderLarge">Coordonate</div>
                                        <div class="listHeaderLarge">Galerie foto</div>
                                        <div class="listHeader">Optiuni</div>
                                    </div>

                                    <a4j:repeat value="#{adminGarbageManagerBean.garbageList}" var="garbage">
                                        <div id="listEntryContainer">
                                            <div class="listEntry"><h:outputText value="#{garbage.garbageId}"/></div>
                                            <div class="listEntry"><h:outputText value="#{garbage.county.name}"/></div>
                                            <div class="listEntry"><h:outputText value="#{garbage.chartedArea.areaId}"/></div>
                                            <div class="listEntryLarge">
                                                <rich:panel rendered="#{fn:length(garbage.description) gt 0}"
                                                            onmouseover="this.style.cursor='help'">
                                                    <h:outputText value="#{fn:substring(garbage.description, 0 , 30)} ..."
                                                                  rendered="#{fn:length(garbage.description) gt 30}"/>
                                                    <h:outputText value="#{garbage.description}"
                                                                  rendered="#{not (fn:length(garbage.description) gt 30)}"/>
                                                    <rich:toolTip >
                                                        <h:outputText value="#{garbage.description}"/>
                                                    </rich:toolTip>
                                                </rich:panel>
                                            </div>
                                            <div class="listEntry"><h:outputText value="#{garbage.insertedBy.userId}"/></div>
                                            <div class="listEntry"><h:outputText value="#{garbage.status}"/></div>
                                            <div class="listEntry"><h:outputText value="#{garbage.dispersed ? 'DA' : 'NU'}"/></div>
                                            <div class="listEntryLarge">
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
                                            </div>
                                            <div class="listEntry"><h:outputText value="#{garbage.bagCount}"/></div>
                                            <div class="listEntryLarge">
                                                <h:outputText value="#{garbage.x}">
                                                    <f:convertNumber maxFractionDigits="6"/>
                                                </h:outputText>
                                                <h:outputText value="/"/>
                                                <h:outputText value="#{garbage.y}">
                                                    <f:convertNumber maxFractionDigits="6"/>
                                                </h:outputText>
                                            </div>
                                            <div class="listEntryLarge">
                                                <a4j:commandLink actionListener="#{adminGarbageManagerBean.actionSelectGarbage}"
                                                                 rendered="#{fn:length(garbage.pictures) gt 0}"
                                                                 reRender="img_gallery"
                                                                 ajaxSingle="true"
                                                                 onclick="#{rich:component('popup-loading')}.show();"
                                                                 oncomplete="#{rich:component('popup-loading')}.hide(); #{rich:component('img_gallery')}.show();">
                                                    <f:param name="garbageId" value="#{garbage.garbageId}"/>
                                                    <strong><h:outputText value="DESCHIDE (#{fn:length(garbage.pictures)})"/></strong>
                                                </a4j:commandLink>
                                            </div>
                                            <div class="listEntry">
                                                <a4j:commandLink actionListener="#{adminGarbageManagerBean.actionDeleteGarbage}"
                                                                 reRender="lista-gunoaie"
                                                                 ajaxSingle="true"
                                                                 onclick="#{rich:component('popup-loading')}.show();"
                                                                 oncomplete="#{rich:component('popup-loading')}.hide();">
                                                    <f:param name="garbageId" value="#{garbage.garbageId}"/>
                                                    <strong>STERGE</strong>
                                                </a4j:commandLink>
                                            </div>
                                        </div>
                                    </a4j:repeat>
                                </a4j:form>

                                <h:panelGroup rendered="#{adminGarbageManagerBean.noFilter}">
                                    <br/>
                                    <br/>
                                    <br/>
                                    <h3>
                                        <h:outputText value="Aplicati cel cel putin un criteriu de filtrare!"/>
                                    </h3>
                                </h:panelGroup>
                            </a4j:outputPanel>
                        </div>

                    </div>
                </div>

                <%-- page Bottom --%>
                <custom:page_bottom/>
            </center>
        </body>
    </html>
</f:view>
