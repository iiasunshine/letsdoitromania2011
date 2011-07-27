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
                <custom:page_top_login selected="lista_utilizatori" role="${adminUsersManagerBean.userDetails.role}"/>

                <%-- page Content --%>
                <div id="pageContainer">
                    <div id="contentList">
                        <jsp:directive.include file="/WEB-INF/jspf/popup-loading.jspf"/>
                        <jsp:directive.include file="/WEB-INF/jspf/popup-user-details.jspf"/>

                        <%-- Left Column --%>
                        <div id="leftColumn">
                            <div class="labelLeft">
                                <h1>Filtrare dupa:</h1>
                            </div>
                            <a4j:form>
                                <div>
                                    <h3>Judet<br/>
                                        <h:selectOneMenu value="#{adminUsersManagerBean.selectedCounty}">
                                            <f:selectItem itemLabel="Toate" itemValue=""/>
                                            <f:selectItems value="#{adminUsersManagerBean.countyItems}"/>
                                        </h:selectOneMenu>
                                    </h3>
                                </div>
                                <div>
                                    <h3>Anul Nasterii<br/>
                                        <h:selectOneMenu value="#{adminUsersManagerBean.selectedBirthYear}">
                                            <f:selectItem itemLabel="Toti" itemValue=""/>
                                            <f:selectItems value="#{adminUsersManagerBean.yearsItems}"/>
                                        </h:selectOneMenu>
                                    </h3>
                                </div>
                                <div>
                                    <h3>Rol<br/>
                                        <h:selectOneMenu value="#{adminUsersManagerBean.selectedRole}">
                                            <f:selectItem itemLabel="Toate" itemValue=""/>
                                            <f:selectItem itemLabel="ADMIN" itemValue="ADMIN"/>
                                            <f:selectItem itemLabel="VOLUNTEER" itemValue="VOLUNTEER"/>
                                            <f:selectItem itemLabel="ORGANIZER" itemValue="ORGANIZER"/>
                                            <f:selectItem itemLabel="ORGANIZER_MULTI" itemValue="ORGANIZER_MULTI"/>
                                        </h:selectOneMenu>
                                    </h3>
                                </div>
                                <div>
                                    <h3>Numar mormane adaugate<br/>
                                        <label>min:  </label>
                                        <h:inputText value="#{adminUsersManagerBean.selectedMinGarbages}" 
                                                     onkeypress="return numbersonly(this, event, false);"
                                                     style="width:75px;"/>
                                        <br/>
                                        <label>max: </label>
                                        <h:inputText value="#{adminUsersManagerBean.selectedMaxGarbages}" 
                                                     onkeypress="return numbersonly(this, event, false);"
                                                     style="width:75px;"/>
                                    </h3>
                                </div>

                                <a4j:commandButton actionListener="#{adminUsersManagerBean.actionApplyFilter}"
                                                   reRender="lista-utilizatori"
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
                            <a4j:outputPanel id="lista-utilizatori">
                                <h:messages warnClass="registerMessageError" infoClass="registerMessageOk"/>

                                <a4j:form>
                                    <h1>
                                        <h:outputFormat value="Lista utilizatori ({0})">
                                            <f:param value="#{fn:length(adminUsersManagerBean.usersList)}"/>
                                        </h:outputFormat>
                                        <br/>
                                        <br/>
                                        <a4j:commandButton actionListener="#{adminUsersManagerBean.actionGenerateExcel}"
                                                           rendered="#{fn:length(adminUsersManagerBean.usersList) gt 0}"
                                                   styleClass="formButtonLeft"
                                                   value="Export lista utilizatori"/>
                                    </h1>
                                    <div id="listHeaderContainer">
                                        <div class="listHeader">Nume</div>
                                        <div class="listHeader">Prenume</div>
                                        <div class="listHeaderLarge">E-mail</div>
                                        <div class="listHeader">Telefon</div>
                                        <div class="listHeader">Rol</div>
                                        <div class="listHeader">Oras</div>
                                        <div class="listHeader">Judet</div>
                                        <div class="listHeaderLarge">Data inregistrarii</div>
                                        <div class="listHeader">ID</div>
                                        <div class="listHeaderLarge">Numar mormane introduse</div>
                                        <div class="listHeaderLarge">Numar zone selectate</div>
                                        <div class="listHeader">Optiuni</div>
                                    </div>

                                    <a4j:repeat value="#{adminUsersManagerBean.usersList}" var="user">
                                        <div id="listEntryContainer">
                                            <div class="listEntry"><h:outputText value="#{user.lastName}"/></div>
                                            <div class="listEntry"><h:outputText value="#{user.firstName}"/></div>
                                            <div class="listEntryLarge"><a href="mailto:nume@domeniu.ro"><h:outputText value="#{user.email}"/></a></div>
                                            <div class="listEntry"><h:outputText value="#{user.phone}"/></div>
                                            <div class="listEntry"><h:outputText value="#{user.role}"/></div>
                                            <div class="listEntry"><h:outputText value="#{user.town}"/></div>
                                            <div class="listEntry"><h:outputText value="#{user.county}"/></div>
                                            <div class="listEntryLarge"><h:outputText value="#{user.recordDate}"/></div>
                                            <div class="listEntry"><h:outputText value="#{user.userId}"/></div>
                                            <div class="listEntryLarge"><h:outputText value="#{fn:length(user.garbages)}"/></div>
                                            <div class="listEntryLarge"><h:outputText value="#{fn:length(user.memberOf.chartedAreas)}"/></div>
                                            <div class="listEntry">
                                                <a4j:commandLink actionListener="#{adminUsersManagerBean.actionSelectUser}"
                                                                 reRender="popup_user_details"
                                                                 ajaxSingle="true"
                                                                 oncomplete="#{rich:component('popup_user_details')}.show();">
                                                    <f:param name="userId" value="#{user.userId}"/>
                                                    <strong>EDITEAZA</strong>
                                                </a4j:commandLink>
                                            </div>
                                        </div>
                                    </a4j:repeat>
                                </a4j:form>

                                <h:panelGroup rendered="#{adminUsersManagerBean.noFilter}">
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
