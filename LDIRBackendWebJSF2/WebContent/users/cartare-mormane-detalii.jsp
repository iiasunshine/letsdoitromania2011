<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:directive.include file="/WEB-INF/jspf/page-header.jspf"/>
<jsp:directive.include file="/WEB-INF/jspf/login-checkpoint.jspf"/>
<c:if test="${mormanManager.myGarbage eq null}">
    <custom:page_redirect target="/users/cartare-mormane-lista.jsf"/>
</c:if>
<f:view>
    <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
            <jsp:directive.include file="/WEB-INF/jspf/page-meta.jspf"/>
            <title>Let's do it Romania</title>
            <script
                src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=${initParam['google.maps.key']}"
                type="text/javascript">
            </script>
        </head>
        <body onunload="GUnload()">
            <center>
                <%-- page Top --%>
                <custom:page_top_login selected="mormane"/>

                <%-- galerie imagini --%>
                <a4j:keepAlive beanName="mormanManager"/>
                <jsp:directive.include file="/WEB-INF/jspf/popup-img-gallery.jspf"/>

                <%-- page Content --%>
                <div id="pageContainer">
                    <div id="content">
                        <%-- Left Column (detalii morman) --%>
                        <div id="leftColumn">
                            <br />
                            <!-- mesaj eroare -->
                            <h:messages warnClass="registerMessageError" infoClass="registerMessageOk"/>

                            <h:panelGroup rendered="#{not (mormanManager.myGarbage.garbage.garbageId eq null)}">
                                <h1><h:outputText value="Acum vizualizezi"/></h1>
                                <br />
                                <h:outputText escape="false" value="#{msg.details_morman} "/>
                                <strong><h:outputText value="#{mormanManager.myGarbage.garbage.garbageId}"/></strong>
                                <br />
                                <br />
                                <h:outputText escape="false" value="#{msg.details_area} "/>
                                <strong><h:outputText value="#{mormanManager.myGarbage.garbage.chartedArea.name}"/></strong>
                                <br />
                                <br />
                                <h:outputText escape="false" value="#{msg.details_county} "/>
                                <strong><h:outputText value="#{mormanManager.myGarbage.garbage.county.name}"/></strong>
                                <br />
                                <br />
                                <h3><h:outputText escape="false" value="#{msg.details_description_title}"/></h3>
                                <h:outputText escape="false" value="#{msg.details_description} "/>
                                <strong><h:outputText value="#{mormanManager.myGarbage.garbage.description}"/></strong>
                                <br />
                                <br />
                                <h:outputText escape="false" value="#{msg.details_dispersed} "/>
                                <strong><h:outputText value="#{mormanManager.myGarbage.garbage.dispersed ? 'Da' : 'Nu'}"/></strong>
                                <br />
                                <br />
                                <h:outputText escape="false" value="#{msg.details_big_components} "/>
                                <strong><h:outputText value="#{mormanManager.myGarbage.garbage.bigComponentsDescription}"/></strong>
                                <br />
                                <br />
                                <h:outputText escape="false" value="#{msg.details_components} "/>
                                <br />
                                <h:outputText escape="false" value="#{msg.details_bags} "/>
                                <strong><h:outputText value="#{mormanManager.myGarbage.garbage.bagCount}"/></strong>
                                <br />
                                <h:outputText escape="false" value="#{msg.details_percent_plastic} "/>
                                <strong><h:outputText value="#{mormanManager.myGarbage.garbage.percentagePlastic}%"/></strong>
                                <br />
                                <h:outputText escape="false" value="#{msg.details_percent_glass} "/>
                                <strong><h:outputText value="#{mormanManager.myGarbage.garbage.percentageGlass}%"/></strong>
                                <br />
                                <h:outputText escape="false" value="#{msg.details_percent_metal }"/>
                                <strong><h:outputText value="#{mormanManager.myGarbage.garbage.percentageMetal}%"/></strong>
                                <br />
                                <h:outputText escape="false" value="#{msg.details_percent_waste }"/>
                                <strong><h:outputText value="#{mormanManager.myGarbage.garbage.percentageWaste}%"/></strong>
                                <br />
                                <br />
                                <h:outputText escape="false" value="#{msg.details_state} "/>
                                <strong><h:outputText value="#{mormanManager.myGarbage.garbage.status}"/></strong>
                                <br />
                                <br />
                                <h:outputText escape="false" value="#{msg.details_coords_latitude} "/>
                                <strong><h:outputText value="#{mormanManager.myGarbage.garbage.y}"/></strong>
                                <br />
                                <h:outputText escape="false" value="#{msg.details_coords_longitude} "/>
                                <strong><h:outputText value="#{mormanManager.myGarbage.garbage.x}"/></strong>
                                <br />
                                <br />
                                <h:outputText escape="false" value="#{msg.details_add_date }"/>
                                <strong>
                                    <h:outputText value="#{mormanManager.myGarbage.garbage.recordDate}">
                                        <f:convertDateTime pattern="dd MMM yyyy"/>
                                    </h:outputText>
                                </strong>
                                <br />
                                <br />

                                <a4j:form rendered="#{fn:length(mormanManager.thumbnails) > 0}">
                                    <h3><h:outputText escape="false" value="#{msg.details_photo_gallery}"/></h3>
                                    <a4j:commandLink actionListener="#{mormanManager.actionSetSelectedImage}"
                                                     reRender="selected_image"
                                                     oncomplete="#{rich:component('img_gallery')}.show();">
                                        <f:param name="imgIndex" value="0"/>
                                        <h:graphicImage value="#{pageContext.servletContext.contextPath}/#{mormanManager.thumbnails[0]}" 
                                                        width="85"
                                                        styleClass="galerie"/>
                                    </a4j:commandLink>
                                    <a4j:commandLink actionListener="#{mormanManager.actionSetSelectedImage}"
                                                     reRender="selected_image"
                                                     oncomplete="#{rich:component('img_gallery')}.show();"
                                                     rendered="#{fn:length(mormanManager.thumbnails) > 1}">
                                        <f:param name="imgIndex" value="1"/>
                                        <h:graphicImage value="#{pageContext.servletContext.contextPath}/#{mormanManager.thumbnails[1]}" 
                                                        width="85"
                                                        styleClass="galerie"/>
                                    </a4j:commandLink>

                                    <br/>
                                    <a4j:commandLink onclick="#{rich:component('img_gallery')}.show();"
                                                     rendered="#{fn:length(mormanManager.thumbnails) > 2}">
                                        <h:outputText escape="false" value="#{msg.details_all_photos}"/>
                                    </a4j:commandLink>
                                </a4j:form>
                                <br />
                                <br />
                                <h3>
                                    <h:outputLink value="cartare-mormane-editare.jsf?garbageId=#{mormanManager.myGarbage.garbage.garbageId}">
                                        <h:outputText escape="false" value="#{msg.details_modify_link}"/>
                                    </h:outputLink>
                                </h3>
                                <h3>
                                    <h:form>
                                        <h:commandLink action="#{mormanManager.actionDeleteMorman}">
                                            <f:param name="garbageId" value="#{mormanManager.myGarbage.garbage.garbageId}"/>
                                            <h:outputText value="#{msg.details_delete_link}" escape="false"/>
                                        </h:commandLink>
                                    </h:form>
                                </h3>
                                <h3>
                                    <h:outputLink value="cartare-mormane-lista.jsf">
                                        <h:outputText escape="false" value="#{msg.details_lista_link}"/>
                                    </h:outputLink>
                                </h3>
                            </h:panelGroup>
                        </div>

                        <%-- Right Column (harta) --%>
                        <div id="rightColumn">
                            <a4j:form>
                                <m:map latitude="#{mormanManager.myGarbage.coordYToString}"
                                       longitude="#{mormanManager.myGarbage.coordXToString}"
                                       width="710px"
                                       height="600px"
                                       zoom="9"
                                       jsVariable="myMap" >
                                    <m:mapControl name="GLargeMapControl3D"/>
                                    <m:mapControl name="GMapTypeControl" position="G_ANCHOR_TOP_RIGHT"/>
                                    <m:mapControl name="GScaleControl" position="G_ANCHOR_BOTTOM_RIGHT" />
                                    <a4j:repeat value="#{mormanManager.myGarbageList}" var="gunoi">
                                        <m:marker  latitude="#{gunoi.coordYToString}"
                                                   longitude="#{gunoi.coordXToString}"
                                                   showInformationEvent="mouseover"
                                                   rendered="#{not (gunoi.garbage.garbageId eq mormanManager.myGarbage.garbage.garbageId)}">
                                            <m:icon imageURL="http://app.letsdoitromania.ro:8080/LDIRBackendWebJSF2/icons/morman-rosu-20x20.png"
                                                    width="20"
                                                    height="20"/>
                                            <m:htmlInformationWindow htmlText="#{gunoi.infoHtml}"/>
                                        </m:marker>
                                    </a4j:repeat>
                                    <m:marker  latitude="#{mormanManager.myGarbage.coordYToString}"
                                               longitude="#{mormanManager.myGarbage.coordXToString}"
                                               showInformationEvent="mouseover">
                                        <m:icon imageURL="http://app.letsdoitromania.ro:8080/LDIRBackendWebJSF2/icons/morman-galben-20x20.png"
                                                width="20"
                                                height="20"/>
                                        <m:htmlInformationWindow htmlText="<strong>Morman curent (#{mormanManager.myGarbage.idToString})</strong><br/>#{msg.details_area} #{mormanManager.myGarbage.garbage.chartedArea.name} <br/>#{msg.details_county} #{mormanManager.myGarbage.garbage.county.name} <br/>"/>
                                    </m:marker>
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
