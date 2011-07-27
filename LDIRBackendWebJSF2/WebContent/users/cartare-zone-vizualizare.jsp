<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:directive.include file="/WEB-INF/jspf/page-header.jspf"/>
<jsp:directive.include file="/WEB-INF/jspf/login-checkpoint.jspf"/>
<c:if test="${areaManager.seletedArea eq null}">
    <custom:page_redirect target="/users/cartare-zone-lista.jsf"/>
</c:if>
<f:view>
    <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
            <jsp:directive.include file="/WEB-INF/jspf/page-meta.jspf"/>
            <jsp:directive.include file="/WEB-INF/jspf/googlemaps-meta.jspf"/>
            <title>Let's do it Romania</title>
        </head>
        <body onunload="GUnload()">
            <center>
                <%-- page Top --%>
                <custom:page_top_login selected="zone" role="${areaManager.userDetails.role}"/>

                <%-- page Content --%>
                <div id="pageContainer">
                    <div id="content">
                        <%-- Left Column (detalii zona) --%>
                        <div id="leftColumn">
                            <h1>
                                <h:outputText value="#{msg.area_details_title}"/>
                            </h1>
                            <br/>
                            <br/>
                            <!-- area Name -->
                            <h:outputFormat value="#{msg.area_label_id} " escape="false">
                                <f:param value="<strong>#{areaManager.seletedArea.name}</strong>"/>
                            </h:outputFormat>
                            <br/>
                            <!-- county -->
                            <h:outputFormat value="#{msg.area_label_county} " escape="false">
                                <f:param value="<strong>#{areaManager.seletedArea.county}</strong>"/>
                            </h:outputFormat>
                            <br/>
                            <br/>
                            <!-- numar voluntari -->
                            <h:outputFormat value="#{msg.area_label_volunteers} " 
                                            rendered="#{not (fn:length(areaManager.areaTeams) eq 1)}"
                                            escape="false">
                                <f:param value="<strong>#{fn:length(areaManager.areaTeams)} voluntari</strong>"/>
                            </h:outputFormat>
                            <h:outputFormat value="#{msg.area_label_volunteer} "
                                            rendered="#{fn:length(areaManager.areaTeams) eq 1}"
                                            escape="false">
                                <f:param value="<strong>Un voluntar</strong>"/>
                            </h:outputFormat>
                            <br/>
                            <br/>
                            <!-- suprafata zonei -->
                            <h:outputFormat value="#{msg.area_label_area} " escape="false"/>
                            <strong>
                                <h:outputText value="#{areaManager.seletedArea.perimeter}" escape="false">
                                    <f:convertNumber maxFractionDigits="1"/>
                                </h:outputText>
                                <h:outputText value=" Km&sup2;" escape="false"/>
                            </strong>
                            <br/>
                            <br/>
                            <!-- numar mormane zona -->
                            <h:outputFormat value="#{msg.area_label_garbage}" 
                                            rendered="#{not (fn:length(areaManager.areaGarbages) eq 1)}"
                                            escape="false">
                                <f:param value="<strong>#{fn:length(areaManager.areaGarbages)} mormane</strong>"/>
                            </h:outputFormat>
                            <h:outputFormat value="#{msg.area_label_garbage}"
                                            rendered="#{fn:length(areaManager.areaGarbages) eq 1}"
                                            escape="false">
                                <f:param value="<strong>un morman</strong>"/>
                            </h:outputFormat>
                            <br/>
                            <br/>
                            <!-- scor zona -->
                            <h:outputFormat value="#{msg.area_label_score} " escape="false">
                                <f:param value="<strong>#{areaManager.seletedArea.score}</strong>"/>
                            </h:outputFormat>
                            <br/>
                            <br/>
                            <h:form>
                                <h:outputLink value="#{pageContext.servletContext.contextPath}/gridHtml/#{areaManager.seletedArea.name}_pachet_cartare.html">
                                    <h3><h:outputText value="» Pachet zona cartare"/></h3>
                                </h:outputLink>
                            </h:form>
                                    <!-- link assign/remove -->
                            <h:form rendered="#{not areaManager.assigned}">
                                <h:commandLink action="#{areaManager.actionAssignArea}">
                                    <f:param name="addAreaId" value="#{areaManager.seletedArea.areaId}"/>
                                    <f:param name="addAreaName" value="#{areaManager.seletedArea.name}"/>
                                    <f:param name="addAreaCounty" value="#{areaManager.seletedArea.county}"/>
                                    <h3><h:outputText value="» #{msg.area_link_add}"/></h3>
                                </h:commandLink>
                            </h:form>
                            <h:form  rendered="#{areaManager.assigned}">
                                <h:commandLink action="#{areaManager.actionRemoveArea}">
                                    <f:param name="removeAreaId" value="#{areaManager.seletedArea.areaId}"/>
                                    <f:param name="removeAreaName" value="#{areaManager.seletedArea.name}"/>
                                    <f:param name="removeAreaCounty" value="#{areaManager.seletedArea.county}"/>
                                    <h3><h:outputText value="» #{msg.area_link_remove}"/></h3>
                                </h:commandLink>
                            </h:form>
                            
                            
                        <!-- charted area percent 
                           <h:form  rendered="#{areaManager.assigned}">
                                <h:commandLink action="#{areaManager.actionSetChartedArea}">
                                    <f:param name="cpAreaId" value="#{areaManager.seletedArea.areaId}"/>
                                    <f:param name="cpPercentageCompleted" value="#{areaManager.seletedArea.percentageCompleted}"/>
                                    <h3><h:outputText value="» #{msg.area_grid_charted}"/></h3>
                                </h:commandLink>
                            </h:form> 
                            <h:selectOneMenu value="#{areaManager.seletedArea.percentageCompleted}" id="percentCharted" styleClass="formDate">
                                    <f:selectItem itemLabel="0%" itemValue="0"/>
                                    <f:selectItem itemLabel="50%" itemValue="50"/>
                                    <f:selectItem itemLabel="100%" itemValue="100"/>
                            </h:selectOneMenu>

                                <br />
                                -->
						</div>
                        
                        <%-- Right Column (harta) --%>
                        <div id="rightColumn">
                            <!-- mesaj eroare sau info -->
                            <h:messages warnClass="registerMessageError" errorClass="registerMessageError" infoClass="registerMessageOk"/>

                            <!-- afisare harta -->
                            <a4j:form>
                                <m:map width="710px" height="600px" latitude="44.4317879" longitude="26.1015844" zoom="8" jsVariable="myMap" type="G_HYBRID_MAP">
                                    <m:mapControl name="GLargeMapControl3D"/>
                                    <m:mapControl name="GMapTypeControl" position="G_ANCHOR_TOP_RIGHT"/>
                                    <m:mapControl name="GScaleControl" position="G_ANCHOR_BOTTOM_RIGHT" />

                                    <!-- zona de cartare -->
                                    <m:polyline lineWidth="2">
                                        <a4j:repeat value="#{areaManager.seletedArea.polyline}" var="point">
                                            <m:point latitude="'#{point.y}'" longitude="'#{point.x}'"/>
                                        </a4j:repeat>
                                    </m:polyline>

                                    <!-- gunoaie din zona de cartare -->
                                    <a4j:repeat value="#{areaManager.areaGarbages}" var="garbage">
                                        <m:marker  latitude="'#{garbage.y}'"
                                                   draggable="true"
                                                   longitude="'#{garbage.x}'">
                                            <m:icon imageURL="http://app.letsdoitromania.ro:8080/LDIRBackendWebJSF2/icons/morman-rosu-20x20.png"
                                                    width="20"
                                                    height="20"/>
                                        </m:marker>
                                    </a4j:repeat>

                                    <!-- zoom si focus pe zona de cartare -->
                                    <m:eventListener eventName="tilesloaded" jsFunction="zoomToArea('#{areaManager.areaJsonBouns}')"/>
                                </m:map>
                            </a4j:form>
                        </div>
                    </div>
                </div>

                <%-- page Bottom --%>
                <custom:page_bottom/>
            </center>
        </body>
    </html>
</f:view>
