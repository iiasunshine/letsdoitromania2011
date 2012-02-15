<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:directive.include file="/WEB-INF/jspf/page-header.jspf"/>
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
                <h:panelGroup rendered="#{sessionScope['USER_DETAILS'] eq null}">
                    <custom:page_top selected="view"/>
                </h:panelGroup>
                <h:panelGroup rendered="#{not (sessionScope['USER_DETAILS'] eq null)}">
                    <custom:page_top_login selected="view"/>
                </h:panelGroup>

                <%-- page Content --%>
                <div id="pageContainer">
                    <div id="content">
                        <%-- left column (empty) --%>
                        <div id="leftColumn">
                            <h3><h:outputText value="#{msg.map_select_county}"/></h3>
                            <h:selectOneMenu styleClass="formDropdownLeft" onchange="loadCountyGarbageOverlay(this.value);">
                                <f:selectItem itemLabel="Selecteaza Judet" itemValue=""/>
                                <f:selectItems value="#{mapViewBean.countyItems}"/>
                            </h:selectOneMenu>
                        </div>

                        <%-- righ column (harta cu gunoaie) --%>
                        <div id="rightColumn">
                            <a4j:form>
                                <m:map width="710px" height="600px" latitude="44.4317879" longitude="26.1015844" zoom="10" jsVariable="myMap"  >
                                    <m:mapControl name="GLargeMapControl3D"/>
                                    <m:mapControl name="GMapTypeControl" position="G_ANCHOR_TOP_RIGHT"/>
                                    <m:mapControl name="GScaleControl" position="G_ANCHOR_BOTTOM_RIGHT" />

                                    <!--a4j:repeat value="{mapViewBean.myGarbageList}" var="gunoi">
                                        <xm:marker  latitude="{gunoi.coordYToString}"
                                                   longitude="{gunoi.coordXToString}"
                                                   showInformationEvent="mouseover">
                                            <xm:icon imageURL="http://www.google.com/mapfiles/ms/micons/red-dot.png"
                                                    width="32"
                                                    height="32"/>
                                            <xm:htmlInformationWindow htmlText="{gunoi.infoHtml}"/>
                                        </xm:marker>
                                    </xa4j:repeat-->
                                </m:map>
                            </a4j:form>

                            <!--script type="text/javascript"  src="{pageContext.servletContext.contextPath}/layout/scripts/gmap.js"></script-->

                        </div>
                    </div>
                </div>

                <%-- page Bottom --%>
                <custom:page_bottom/>
            </center>
        </body>
    </html>
</f:view>
