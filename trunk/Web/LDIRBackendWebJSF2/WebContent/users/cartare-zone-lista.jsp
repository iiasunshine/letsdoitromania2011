<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:directive.include file="/WEB-INF/jspf/page-header.jspf"/>
<jsp:directive.include file="/WEB-INF/jspf/login-checkpoint.jspf"/>
<f:view>
    <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
            <jsp:directive.include file="/WEB-INF/jspf/page-meta.jspf"/>
            <jsp:directive.include file="/WEB-INF/jspf/googlemaps-meta.jspf"/>
            <title>Let's do it Romania</title>
        </head>
        <body onunload="GUnload()">
            <center>
                <%-- POPUPURI --%>
                <jsp:directive.include file="/WEB-INF/jspf/popup-grid-loading.jspf"/>

                <%-- page Top --%>
                <custom:page_top_login selected="zone" role="${areaManager.userDetails.role}"/>

                <%-- page Content --%>
                <div id="pageContainer">
                    <div id="content">
                        <%-- Left Column (lista zone atribuite) --%>
                        <div id="leftColumn">
                            <h1>
                                <h:outputText value="#{msg.area_empty_list}"
                                              rendered="#{fn:length(areaManager.chartedAreasList) eq 0}"/>
                                <h:outputText value="#{msg.area_list_title}"
                                              rendered="#{fn:length(areaManager.chartedAreasList) gt 0}"/>
                            </h1>
                            <!--h3>
                            <xh:outputText value="{areaManager.info}" escape="false"/>
                            </h3-->

                            <h:panelGroup rendered="#{fn:length(areaManager.chartedAreasList) gt 0}">
                                <a4j:repeat value="#{areaManager.chartedAreasList}"
                                            var="chartedArea">
                                    <div class="entryLeft">
                                        <h:outputText value="Zona: " />
                                        <strong><h:outputText value="#{chartedArea.name}"/></strong>
                                        <br/>
                                        <h:outputLink value="cartare-zone-vizualizare.jsf?areaId=#{chartedArea.areaId}">
                                            <h:outputText value="#{msg.details_view_link}" escape="false"/>
                                        </h:outputLink>
                                    </div>
                                </a4j:repeat>
                            </h:panelGroup>

                            <h3><h:outputText value="#{msg.area_select_intsuctions}"/></h3>
                            <h:selectOneMenu styleClass="formDropdownLeft"
                                             value="#{areaManager.dummyCounty}"
                                             onchange="#{rich:component('popup-grid-loading')}.show(); loadCountyGridsOverlay(this.value, #{areaManager.userTeam.teamId});">
                                <f:selectItem itemLabel="Selecteaza Judet" itemValue=""/>
                                <f:selectItems value="#{areaManager.countyItems}"/>
                            </h:selectOneMenu>
                        </div>

                        <%-- Right Column (harta) --%>
                        <div id="rightColumn">
                            <!-- mesaj eroare sau info -->
                            <h:messages warnClass="registerMessageError" errorClass="registerMessageError" infoClass="registerMessageOk"/>

                            <a4j:form>
                                <m:map width="710px" height="600px" latitude="44.4317879" longitude="26.1015844" zoom="8" jsVariable="myMap" >
                                    <m:mapControl name="GLargeMapControl3D"/>
                                    <m:mapControl name="GMapTypeControl" position="G_ANCHOR_TOP_RIGHT"/>
                                    <m:mapControl name="GScaleControl" position="G_ANCHOR_BOTTOM_RIGHT" />
                                    <m:eventListener eventName="tilesloaded" 
                                                     jsFunction="loadCountyGridsOverlay('#{areaManager.selectedCounty}', #{areaManager.userTeam.teamId})"/>
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
