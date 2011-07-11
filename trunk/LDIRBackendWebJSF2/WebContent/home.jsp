<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:directive.include file="/WEB-INF/jspf/page-header.jspf"/>
<f:view>
    <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
            <jsp:directive.include file="/WEB-INF/jspf/page-meta.jspf"/>
            <title>Let's do it Romania</title>
        </head>
        <body>
            <center>
                <%-- page Top --%>
                <h:panelGroup rendered="#{sessionScope['USER_DETAILS'] eq null}">
                    <custom:page_top selected="home"/>
                </h:panelGroup>
                <h:panelGroup rendered="#{not (sessionScope['USER_DETAILS'] eq null)}">
                    <custom:page_top_login selected="home"/>
                </h:panelGroup>

                <%-- page Content --%>
                <div id="pageContainer">
                    <div id="content">
                        <div id="leftColumn">
                        </div>

                        <div id="rightColumn" style="height: 250px;"">
                             <br/>
                            <br/>
                            <h:panelGroup rendered="#{not (sessionScope['USER_DETAILS'] eq null)}">
                                <h3>
                                    <h:outputFormat value="#{msg.home_text_1}" escape="false">
                                        <f:param value="<a href=\"users/cartare-zone-lista.jsf\">aici</a>"/>
                                    </h:outputFormat>
                                </h3>
                            </h:panelGroup>
                            <h:panelGroup rendered="#{sessionScope['USER_DETAILS'] eq null}">
                                <h3>
                                    <h:outputText value="Bine ai venit in Let`s Do It, Romania!"/>
                                    <br/>
                                    <br/>
                                    <h:outputText value="Pentru a putea participa la actiunile noastre, te rugam sa iti creezi un [<a href=\"user-register.jsp\">cont</a>]. " escape="false"/>
                                    <h:outputText value="Daca ai deja un cont creat, te rugam sa te loghezi."/>
                                    <br/>
                                    <br/>
                                    <h:outputText value="In acest moment, te poti inscrie pentru:<br/>- cartare<br/>- curatenie" escape="false"/>
                                </h3>
                                <br/>
                                <h:outputText value="Cartarea presupune determinarea mormanelor de deşeuri din arealele naturale şi stabilirea coordonatelor acestora cu ajutorul
                                              GPS-ului, informaţiile fiind ulterior încărcate într-un soft special. Rezultatul final al acestei acţiuni este realizarea hărţii
                                              deşeurilor din România, în baza căreia vor putea fi estimate resursele necesare pentru reuşita proiectului, inclusiv numărul
                                              de voluntari necesari în ziua de curăţenie. Tot pe baza acestei hărţi va fi solicitat sprijinul concret al autorităţilor,
                                              companiilor private şi al publicului. "
                                              style="color:#4D751F ; display: block;"/>
                            </h:panelGroup>
                        </div>
                    </div>
                </div>

                <%-- page Bottom --%>
                <custom:page_bottom/>
            </center>
        </body>
    </html>
</f:view>
