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
                        <h3>Dupa inregistrare, pentru a participa la curatenia din 12 Mai 2012, poti sa:
                        <br/><br/>
                        1. Te loghezi in aplicatie si din meniul Curatenie iti aloci mormane pentru tine sau echipa ta </br>
                        <br/>
                        sau
                        </br>
                        <br>
                        2. Te duci, impreuna cu echipa, la cel mai apropiat punct de inregistrare in ziua de 12 mai 2012 incepand cu ora 08:00 pentru a primi informatii despre curatenie</br>
                        </h3>

                        </div>

                        <%-- right column --%>
                        <div id="rightColumn" style="min-height: 200px;">

                                <h2><h:outputText value="#{msg.register_title}"/></h2>


                            <!-- mesaj eroare -->
                            <h:messages warnClass="registerMessageError" infoClass="registerMessageOk"/>                            
                            <!--h:outputText value="{fn:contains(facesContext.messageList[0].severity, 'INFO')}"/-->

                            <!-- form inregistrare -->
                            <h:form styleClass="form" rendered="#{not fn:contains(facesContext.messageList[0].severity, 'INFO')}">
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
                                <br />
								
                                <!-- JUDET -->
                                <div class="label"><h:outputText value="#{msg.register_area} "/><span class="important">*</span></div>
                                <h:selectOneMenu value="#{registerBean.regiterUser.county}" id="judet_resedinta" styleClass="formDropdown">
                                    <f:selectItem itemLabel="Alege Judet" itemValue=""/>
                                    <f:selectItems value="#{registerBean.countyItems}"/>
                                </h:selectOneMenu>
                                <br />
                               
                                <!-- TERMENI -->
                                <br/>
                                <h:selectBooleanCheckbox value="#{registerBean.acceptTerms}" id="conditii"/>
                                <label class="formCheckbox">
                                    <h:outputText value="#{msg.register_terms} "/>
                                    <a href="termene-si-conditii.htm" 
                                       onclick="popup(this, 'Termene si conditii');return false;"
                                       style="color: #4D751F; ext-decoration: none;">conditiile</a>
                                </label>
                                <div class="label"><span class="important">*</span></div>
                                <br/>
                                
                                <!-- Accepta si alte informari -->
                                <h:selectBooleanCheckbox value="#{registerBean.acceptReceiveNotifications}" id="acceptReceiveNotifications"/>
                                <label class="formCheckbox">
                                    <h:outputText value="#{msg.register_acceptNotifications} "/>
                                </label>
       
                                <br />  
                                <!-- ANTISPAM -->
                                <br />
                                <div class="label"><h:outputText value="#{msg.register_antispam} "/><span class="important">*</span></div>
                                <h:inputText value="#{registerBean.antispam}" id="captcha_code" styleClass="formTextfield"/>
                                <br />
                                <span class="important" style="margin-left: 150px;"><h:outputText value="#{msg.register_antispam_info}"/></span>
                                <br />                                
                                <div class="formCaptchaImage"  style="margin-left: 150px;">
                                    <img src="${pageContext.servletContext.contextPath}/stickyimg" width="120" height="40" />
                                </div>
                                
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
