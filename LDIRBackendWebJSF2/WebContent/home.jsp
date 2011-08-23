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
                    <custom:page_top_login selected="home" role="${sessionScope['USER_DETAILS'].role}"/>
                </h:panelGroup>

                <%-- page Content --%>
                <div id="pageContainer">
                    <div id="content">
                        <div id="leftColumn">
                        </div>

                        <div id="rightColumn" style="height: 450px;">
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
                                    <h:outputText value="Bine ai venit la Let`s Do It, Romania!" escape="false"/>
                                    <br/>
                                    <br/>
                                    <h:outputText value="Daca ai ajuns pe aceasta pagina inseamna ca vrei sa faci parte din Echipa Nationala de Actiune si pentru asta dorim in primul rand sa te felicitam! Pe 24 septembrie 2011 ne-am propus sa fim cel putin 500.000, iar cu tine alaturi suntem cu un pas mai aproape de a atinge acest obiectiv." escape="false"/>
                                    <br/>
                                    <h:outputText value="Inscrierea in echipa se realizeaza prin intermediul formularului de <a href=\"user-register.jsf\"><b>aici</b></a>. Vei avea posibilitatea sa te inscrii pentru doua actiuni la fel de importante pentru proiect: cartarea și curatenia. Ca si anul trecut, cei inscrisi pentru ziua nationala de curatenie vor avea posiblitatea de a-si alege mormanele pe care sa le stranga pe 24 septembrie." escape="false"/>
                                	<br/>
                                    <h:outputText value="- Cartarea presupune determinarea mormanelor de deseuri din arealele naturale si stabilirea coordonatelor acestora cu ajutorul GPS-ului (orice aparat care poate furniza coordonate GPS: GPS masina, GPS de la telefon, GPS de la ceas samd), informatiile fiind ulterior introduse pe acest site. Rezultatul final al acestei actiuni este realizarea hartii deseurilor din Romania, in baza careia vor putea fi estimate resursele necesare pentru reusita proiectului. Tot pe baza acestei harti va fi solicitat sprijinul concret al autoritatilor, companiilor private si al publicului." escape="false"/>
                                    <br/>
                                    <br/>
                                    <h:outputText value="Procesul de cartare se desfasoara astfel:" escape="false"/>
                                    <br/>
                                    <h:outputText value="1. <a href=\"user-register.jsf\"><b>te inregistrezi pentru cartare.</b></a>" escape="false"/>
                                    <br/>
                                    <h:outputText value="2. dupa ce te loghezi, vei avea acces la harta Romaniei, impartita in zone de cartare. Optional, aici iti vei putea alege zone de cartare din judetul preferat si ti le vei putea aloca. Dupa alocarea unei zone, poti accesa un pachet de informatii necesare cartarii(harta zonei, detalii despre localizare, puncte de reper si manualul cartarii)" escape="false"/>
                                    <br/>
                                    <h:outputText value="3. mergi pe teren si identifici mormanele de gunoaie. Pentru aceasta, vei completa o fisa de morman, cu detalii relevante pentru ziua de curatenie: coordonatele GPS, ce tipuri de deseuri se afla in compozitia mormanului, in ce volum, daca exista elemente de dificultate in acces sau transportul deseurilor, precum si descrierea cat mai completa a punctelor de reper si a modului in care se face accesul" escape="false"/>
                                    <br/>
                                    <h:outputText value="4. introduci datele pentru fiecare morman cartat pe harta online, furnizand toate detaliile din fisa de morman" escape="false"/>
                                    <br/>
                                    <h:outputText value="In afara de modalitatea clasica de cartare, poti utiliza si aplicatia \"Let's Do It Ro\" pentru IOS (Iphone)." escape="false"/>
                                    <br/>
                                    <h:outputText value="Pentru intrebari legate de cartare, poti scrie la <a href=\"mailto:cartare@letsdoitromania.ro\" target=\"_blank\">cartare@letsdoitromania.ro</a>." escape="false"/>
                                    <br/>
                                    <h:outputText value="<a href=\"http://app.letsdoitromania.ro/LDIRBackendWebJSF2/docs/manualcartare.pdf\" target=\"_blank\">Downloadeaza Manual Cartare 2011</a>" escape="false"/>
                                    <br/>
                                    <h:outputText value="<a href=\"http://itunes.apple.com/us/app/lets-do-it-ro/id449110019\">Downloadeaza aplicatia de cartare pentru iPhone</a>" escape="false"/>
                                    <br/>
                               
                                </h3>
                                <br/>
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
