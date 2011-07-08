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
                <custom:page_top selected="login"/>

                <%-- page Content --%>
                <div id="pageContainer">
                    <div id="content">
                        <%-- left column --%>
                        <div id="leftColumn">

                        </div>

                        <%-- right column --%>
                        <div id="rightColumn" style="height: 250px;">
                            <div class="label">
                                <h1>Parola Noua</h1>
                            </div>

                            <!-- mesaj eroare -->
                            <h:messages warnClass="registerMessageError" infoClass="registerMessageOk"/>

                            <!-- fomular parola noua -->
                            <h:form styleClass="form"  rendered="#{not fn:contains(facesContext.messageList[0].severity, 'INFO')}">
                                <!-- PAROLA -->
                                <div class="label"><h:outputText value="#{msg.register_password} "/><span class="important">*</span></div>
                                <h:inputSecret value="#{resetBean.password}" id="parola" styleClass="formTextfield"/>
                                <br />
                                <!-- CONFIRMARE PAROLA -->
                                <div class="label"><h:outputText value="#{msg.register_password_confirm} "/><span class="important">*</span></div>
                                <h:inputSecret value="#{resetBean.passwordConfirm}" id="confirmare_parola" styleClass="formTextfield"/>
                                <br/>
                                <br/>
                                <!-- BUTOANE -->
                                <div style="margin-left: 140px;">
                                    <h:commandButton action="#{resetBean.actionNewPassword}"
                                                     value="#{msg.newpass_button_new_pass}"
                                                     id="confirma"
                                                     styleClass="formButton"/>
                                    <input name="anuleaza" type="reset" class="formButton" value="Anuleaza" id="anuleaza" />
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
