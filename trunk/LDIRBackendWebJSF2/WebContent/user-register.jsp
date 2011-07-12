<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:directive.include file="/WEB-INF/jspf/page-header.jspf"/>
<jsp:directive.include file="/WEB-INF/jspf/notlogin-checkpoint.jspf"/>
<f:view>
    <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
            <jsp:directive.include file="/WEB-INF/jspf/page-meta.jspf"/>
            <title>Let's do it Romania</title>
        </head>
        <body>
            <center>
                <%-- page Top --%>
                <custom:page_top selected="register"/>

                <%-- page Content --%>
                <div id="pageContainer">
                    <div id="content">
                        <%-- left column --%>
                        <div id="leftColumn">

                        </div>

                        <%-- right column --%>
                        <div id="rightColumn" style="min-height: 200px;">
                            <div class="label">
                                <h1><h:outputText value="#{msg.register_title}"/></h1>
                            </div>

                            <!-- mesaj eroare -->
                            <h:messages warnClass="registerMessageError" infoClass="registerMessageOk"/>
                            <!--h:outputText value="{fn:contains(facesContext.messageList[0].severity, 'INFO')}"/-->

                            <!-- form inregistrare -->
                            <h:form styleClass="form" rendered="#{not fn:contains(facesContext.messageList[0].severity, 'INFO')}">
                                <!-- NUME -->
                                <div class="label"><h:outputText value="#{msg.register_name} "/><span class="important">*</span></div>
                                <h:inputText value="#{registerBean.regiterUser.lastName}" id="nume" styleClass="formTextfield"/>
                                <br />
                                <!-- PRENUME -->
                                <div class="label"><h:outputText value="#{msg.register_surname} "/><span class="important">*</span></div>
                                <h:inputText value="#{registerBean.regiterUser.firstName}" id="prenume" styleClass="formTextfield"/>
                                <br />
                                <!-- MAIL -->
                                <div class="label"><h:outputText value="#{msg.register_email} "/><span class="important">*</span></div>
                                <h:inputText value="#{registerBean.regiterUser.email}" id="email" styleClass="formTextfield"/>
                                <br />
                                <!-- PAROLA -->
                                <div class="label"><h:outputText value="#{msg.register_password} "/><span class="important">*</span></div>
                                <h:inputSecret value="#{registerBean.regiterUser.passwd}" id="parola" styleClass="formTextfield"/>
                                <br />
                                <!-- CONFIRMARE PAROLA -->
                                <div class="label"><h:outputText value="#{msg.register_password_confirm} "/><span class="important">*</span></div>
                                <h:inputSecret value="#{registerBean.passwordConfirm}" id="confirmare_parola" styleClass="formTextfield"/>
                                <!-- DATA NASTERE -->
                                <br />
                                <div class="label"><h:outputText value="#{msg.register_birthday} "/></div>
                                <h:selectOneMenu value="#{registerBean.day}" id="zi_nastere" styleClass="formDate">
                                    <f:selectItem itemLabel="Zi" itemValue=""/>
                                    <f:selectItems value="#{registerBean.daysItems}"/>
                                </h:selectOneMenu>

                                <h:selectOneMenu value="#{registerBean.month}" id="luna_nastere" styleClass="formDate">
                                    <f:selectItem itemLabel="Luna" itemValue=""/>
                                    <f:selectItem itemLabel="Ianuarie" itemValue="1"/>
                                    <f:selectItem itemLabel="Februarie" itemValue="2"/>
                                    <f:selectItem itemLabel="Martie" itemValue="3"/>
                                    <f:selectItem itemLabel="Aprilie" itemValue="4"/>
                                    <f:selectItem itemLabel="Mai" itemValue="5"/>
                                    <f:selectItem itemLabel="Iunie" itemValue="6"/>
                                    <f:selectItem itemLabel="Iulie" itemValue="7"/>
                                    <f:selectItem itemLabel="August" itemValue="8"/>
                                    <f:selectItem itemLabel="Septembrie" itemValue="9"/>
                                    <f:selectItem itemLabel="Octombrie" itemValue="10"/>
                                    <f:selectItem itemLabel="Noiembrie" itemValue="11"/>
                                    <f:selectItem itemLabel="Decembrie" itemValue="12"/>
                                </h:selectOneMenu>

                                <h:selectOneMenu value="#{registerBean.year}" id="an_nastere" styleClass="formDate">
                                    <f:selectItem itemLabel="An" itemValue=""/>
                                    <f:selectItems value="#{registerBean.yearsItems}"/>
                                </h:selectOneMenu>
                                <br />
                                <!-- ORAS -->
                                <div class="label"><h:outputText value="#{msg.register_city} "/></div>
                                <h:inputText value="#{registerBean.regiterUser.town}" id="oras_resedinta" styleClass="formTextfield"/>
                                <br />
                                <!-- JUDET -->
                                <div class="label"><h:outputText value="#{msg.register_area} "/></div>
                                <h:selectOneMenu value="#{registerBean.regiterUser.county}" id="judet_resedinta" styleClass="formDropdown">
                                    <f:selectItem itemLabel="Alege Judet" itemValue=""/>
                                    <f:selectItems value="#{registerBean.countyItems}"/>
                                </h:selectOneMenu>
                                <br />
                                <!-- ACTIVITATI -->
                                <div class="label"><h:outputText value="#{msg.register_activities} "/><span class="important">*</span></div>
                                <h:selectBooleanCheckbox value="#{registerBean.cartare}" id="cartare"/>
                                <label class="formCheckbox"><h:outputText value="#{msg.register_activities_cartare}   "/></label>
                                <h:selectBooleanCheckbox value="#{registerBean.curatenie}" id="curatenie"/>
                                <label class="formCheckbox"><h:outputText value="#{msg.register_activities_curatenie}"/></label>
                                <br />
                                <!-- TELEFON -->
                                <div class="label"><h:outputText value="#{msg.register_phone} "/></div>
                                <h:inputText value="#{registerBean.regiterUser.phone}" id="telefon" styleClass="formTextfield"/>
                                <br />
                                <!-- TERMENI -->
                                <h:selectBooleanCheckbox value="#{registerBean.acceptTerms}" id="conditii"/>
                                <label class="formCheckbox">
                                    <h:outputText value="#{msg.register_terms} "/>
                                    <a href="termene-si-conditii.htm" 
                                       onclick="popup(this, 'Termene si conditii');return false;"
                                       style="color: #4D751F; ext-decoration: none;">conditiile</a>
                                </label>
                                <div class="label"><span class="important">*</span></div>
                                <br />
                                <!-- Accepta si alte informari -->
                                <h:selectBooleanCheckbox value="#{registerBean.acceptReceiveNotifications}" id="acceptReceiveNotifications"/>
                                <label class="formCheckbox">
                                    <h:outputText value="#{msg.register_acceptNotifications} "/>
                                </label>
                                <div class="label"><span class="important">*</span></div>
                                <br />                                
                                <!-- ANTISPAM -->
                                <div class="label"><h:outputText value="#{msg.register_antispam} "/><span class="important">*</span></div>
                                <h:inputText value="#{registerBean.antispam}" id="captcha_code" styleClass="formTextfield"/>
                                <br />
                                <span class="important" style="margin-left: 150px;"><h:outputText value="#{msg.register_antispam_info}"/></span>
                                <div class="formCaptchaImage"  style="margin-left: 150px;">
                                    <img src="${pageContext.servletContext.contextPath}/stickyimg" width="120" height="40" />
                                </div>
                                <br />
                                <!-- BUTOANE -->
                                <div style="margin-left: 150px;">
                                    <h:commandButton action="#{registerBean.actionRegister}"
                                                     value="#{msg.register_button_confirm}"
                                                     id="confirma"
                                                     styleClass="formButton"/>
                                    <input name="anuleaza" type="reset" class="formButton" value="Anuleaza inregistrarea" id="anuleaza" />
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
