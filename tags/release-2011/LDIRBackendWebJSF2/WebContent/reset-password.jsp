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
                            <div class="labelLeft">
                                <h1><h:outputText value="#{msg.reset_title}"/></h1>
                            </div>
                            <br />
                            <h:form styleClass="form" >
                                <div class="labelLeft"><h:outputText value="#{msg.reset_mail}"/></div>
                                <!--input id="email" type="text" name="j_username" class="formTextfieldLeft"/-->
                                <h:inputText value="#{resetBean.loginMail}"
                                             id="email"
                                             styleClass="formTextfieldLeft"/>
                                <br />
                                <br />
                                <div>
                                    <h:commandButton action="#{resetBean.actionReset}"
                                                     value="#{msg.reset_button_submit}"
                                                     id="login"
                                                     styleClass="formButton"/>
                                    <input name="anuleaza" type="reset" class="formButton" value="Anuleaza" id="anuleaza" />
                                </div>
                            </h:form>
                                <br/>
                            <br/>
                            <br/>
                            <h3>
                                <h:outputLink value="user-login.jsf"><h:outputText value="#{msg.login_link}" escape="false"/></h:outputLink>
                            </h3>
                        </div>

                        <%-- right column --%>
                        <div id="rightColumn" style="height: 200px;">
                            <h:messages warnClass="registerMessageError" infoClass="registerMessageOk"/>
                        </div>
                    </div>
                </div>

                <%-- page Bottom --%>
                <custom:page_bottom/>
            </center>
        </body>
    </html>
</f:view>
