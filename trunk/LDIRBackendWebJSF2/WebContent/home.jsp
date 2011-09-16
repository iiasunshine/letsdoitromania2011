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
                                    <h:outputText value="Bine ai venit pe aplicatia Let`s Do It, Romania!" escape="false"/>
                                    <br/>
                                    <br/>
                                    <h:outputText value="Aici ai posibilitatea sa:" escape="false"/>
                                    <br/>
                                    <h:outputText value="1. Te inscrii in baza noastra de date ca voluntar LDIR (butonul \"Inscrie-te ca voluntar\" de mai sus)" escape="false"/>
                                	<br/>
                                    <h:outputText value="2. Accesezi contul tau daca deja te-ai inscris (butonul \"Login\" de mai sus)" escape="false"/>
                                    <br/>
                                    <h:outputText value="3. Iti alegi o zona de cartare (butonul \"Zona de Cartare\" - apare dupa ce te-ai logat)" escape="false"/>
                                    <br/>
                                    <h:outputText value="4. Introduci mormanele pe care le-ai identificat pe teren (butonul \"Cartare Mormane\" - apare dupa ce te-ai logat)" escape="false"/>
                                    <br/>
                                    <h:outputText value="5. Iti gestionezi echipa (butonul \"Echipa\" - apare dupa ce te-ai logat)" escape="false"/>
                                    <br/>
                                    <h:outputText value="6. Te inscrii intr-o echipa, pe baza codului primit de la un voluntar inregistrat, care doreste sa isi creeze o echipa (butonul \"Echipa\" --&#62; \"Inscriere in echipa\" - apare dupa ce te-ai logat)" escape="false"/>
                                    <br/>
                                    <br/>                                    
                                    <h:outputText value="<a href=\"http://app.letsdoitromania.ro/LDIRBackendWebJSF2/docs/manualcartare.pdf\" target=\"_blank\">Descarca Manual Cartare 2011</a>" escape="false"/>
                                    <br/>
                                    <h:outputText value="<a href=\"http://app.letsdoitromania.ro/LDIRBackendWebJSF2/docs/fisademorman.pdf\" target=\"_blank\">Descarca Fisa de Morman 2011</a>" escape="false"/>
                                    <br/>
                                    <h:outputText value="<a href=\"http://app.letsdoitromania.ro/LDIRBackendWebJSF2/docs/prezentarecartare.pdf\" target=\"_blank\">Descarca Prezentare Cartare 2011</a>" escape="false"/>
                                    <br/>
                                    <h:outputText value="<a href=\"http://itunes.apple.com/us/app/lets-do-it-ro/id449110019\">Descarca aplicatia de cartare pentru iPhone</a>" escape="false"/>
                                    <br/>
                                    <br/>                                    
                                    <h:outputText value="<a href=\"http://app.letsdoitromania.ro/LDIRBackendWebJSF2/docs/prezentareinscriereechipevoluntari.pdf\" target=\"_blank\">Descarca Prezentarea Pasilor pentru Inscrierea pentru Ziua de Curatenie Nationala</a>" escape="false"/>
                               		<br/>                                       
                                    <h:outputText value="<a href=\"http://app.letsdoitromania.ro/LDIRBackendWebJSF2/docs/declaratieminori2011.pdf\" target=\"_blank\">Descarca Declaratia pentru Participarea Minorilor la Ziua de Curatenie Nationala</a>" escape="false"/>
                                    <br/>   
                                    <h:outputText value="<a href=\"http://app.letsdoitromania.ro/LDIRBackendWebJSF2/docs/normedesiguranta2011.pdf\" target=\"_blank\">Descarca Normele de Siguranta pentru Ziua de Curatenie Nationala </a>" escape="false"/>
                                    <br/>
                                    <br/>
                                    <h:outputText value="<a href=\"http://www.letsdoitromania.ro/wp-content/uploads/2011/07/Manual-Curatenie-2011.pdf\" target=\"_blank\">Downloadeaza Manual Pentru Ziua de Curatenie 24 Septembrie 2011</a>" escape="false"/>
									<br/>  
                                    
                                </h3>
                            </h:panelGroup>
                            <h:panelGroup rendered="#{sessionScope['USER_DETAILS'] eq null}">
                                <h3>
                                    <h:outputText value="Bine ai venit pe aplicatia Let`s Do It, Romania!" escape="false"/>
                                    <br/>
                                    <br/>
                                    <h:outputText value="Aici ai posibilitatea sa:" escape="false"/>
                                    <br/>
                                    <h:outputText value="1. Te inscrii in baza noastra de date ca voluntar LDIR (butonul \"Inscrie-te ca voluntar\" de mai sus)" escape="false"/>
                                	<br/>
                                    <h:outputText value="2. Accesezi contul tau daca deja te-ai inscris (butonul \"Login\" de mai sus)" escape="false"/>
                                    <br/>
                                    <h:outputText value="3. Iti alegi o zona de cartare (butonul \"Zona de Cartare\" - apare dupa ce te-ai logat)" escape="false"/>
                                    <br/>
                                    <h:outputText value="4. Introduci mormanele pe care le-ai identificat pe teren (butonul \"Cartare Mormane\" - apare dupa ce te-ai logat)" escape="false"/>
                                    <br/>
                                    <h:outputText value="5. Iti gestionezi echipa (butonul \"Echipa\" - apare dupa ce te-ai logat)" escape="false"/>
                                    <br/>
                                    <h:outputText value="6. Te inscrii intr-o echipa, pe baza codului primit de la un voluntar inregistrat, care doreste sa isi creeze o echipa (butonul \"Echipa\" --&#62; \"Inscriere in echipa\" - apare dupa ce te-ai logat)" escape="false"/>
                                    <br/>
                                    <br/>                                    
                                    <h:outputText value="<a href=\"http://app.letsdoitromania.ro/LDIRBackendWebJSF2/docs/manualcartare.pdf\" target=\"_blank\">Descarca Manual Cartare 2011</a>" escape="false"/>
                                    <br/>
                                    <h:outputText value="<a href=\"http://app.letsdoitromania.ro/LDIRBackendWebJSF2/docs/fisademorman.pdf\" target=\"_blank\">Descarca Fisa de Morman 2011</a>" escape="false"/>
                                    <br/>
                                    <h:outputText value="<a href=\"http://app.letsdoitromania.ro/LDIRBackendWebJSF2/docs/prezentarecartare.pdf\" target=\"_blank\">Descarca Prezentare Cartare 2011</a>" escape="false"/>
                                    <br/>
                                    <h:outputText value="<a href=\"http://itunes.apple.com/us/app/lets-do-it-ro/id449110019\">Descarca aplicatia de cartare pentru iPhone</a>" escape="false"/>
                                    <br/>
                                    <br/>                                    
                                    <h:outputText value="<a href=\"http://app.letsdoitromania.ro/LDIRBackendWebJSF2/docs/prezentareinscriereechipevoluntari.pdf\" target=\"_blank\">Descarca Prezentarea Pasilor pentru Inscrierea pentru Ziua de Curatenie Nationala</a>" escape="false"/>
                               		<br/>                                       
                                    <h:outputText value="<a href=\"http://app.letsdoitromania.ro/LDIRBackendWebJSF2/docs/declaratieminori2011.pdf\" target=\"_blank\">Descarca Declaratia pentru Participarea Minorilor la Ziua de Curatenie Nationala</a>" escape="false"/>
                                    <br/>   
                                    <h:outputText value="<a href=\"http://app.letsdoitromania.ro/LDIRBackendWebJSF2/docs/normedesiguranta2011.pdf\" target=\"_blank\">Descarca Normele de Siguranta pentru Ziua de Curatenie Nationala </a>" escape="false"/>
                                    <br/>
                                    <br/>
                                    <h:outputText value="<a href=\"http://www.letsdoitromania.ro/wp-content/uploads/2011/07/Manual-Curatenie-2011.pdf\" target=\"_blank\">Downloadeaza Manual Pentru Ziua de Curatenie 24 Septembrie 2011</a>" escape="false"/>
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
