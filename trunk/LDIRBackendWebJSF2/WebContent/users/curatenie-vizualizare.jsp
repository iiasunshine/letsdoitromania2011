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
        <body>
            <center>
                <%-- page Top --%>
                <custom:page_top_login selected="curatenie" role="${areaCleanManager.userDetails.role}"/>

                <%-- page Content --%>
                <div id="pageContainer">
                    <div id="content">
                        <%-- Left Column --%>
                        <div id="leftColumn" style="">
                            <h3><h:outputText value="#{msg.map_select_county}"/></h3>
                            <h:form>
                            <h:selectOneMenu  value="#{areaCleanManager.country}" styleClass="formDropdownLeft">
                                <f:selectItem itemLabel="Selecteaza Judet" itemValue=""/>
                                <f:selectItems value="#{areaCleanManager.countyItems}"/>
                            </h:selectOneMenu>
                            <br/><br/>
                             <!-- BUTOANE -->
                                <div>
                                    <h:commandButton action="#{areaCleanManager.garbageFromCountry}"
                                                     value="Listeaza"
                                                     id="confirma"
                                                     styleClass="formButton"/>
                                </div>
                            
							</h:form>
							<br/>
						  <br/>

 						 <h1>
                             <h:outputText value="#{msg.echipa_empty_list}"
                                              rendered="#{fn:length(areaCleanManager.teamList) eq 0}"/>
                             <h:outputText value="#{msg.echipa_list_title}"
                                              rendered="#{fn:length(areaCleanManager.teamList) gt 0}"/>
                         </h1>
                         <h:panelGroup rendered="#{fn:length(areaCleanManager.teamList) gt 0}">
                           <a4j:repeat value="#{areaCleanManager.teamList}" var="team">
                          		 <div class="entryLeft">
                          		 	   <h3> <h:outputText value="Echipa Nume = " /> </h3>
                                        <strong><h:outputText value="#{team.teamName}"/></strong>
                                        
 							<h:panelGroup rendered="#{fn:length(team.garbages) eq 0}">
                                <h3><h:outputText value="#{msg.clean_empty_list}"/></h3>
                            </h:panelGroup>
                            <h:panelGroup rendered="#{fn:length(team.garbages) gt 0}">
                                <h3><h:outputText value="#{msg.map_list_morman}  (#{fn:length(team.garbages)})"/></h3>
                                <h:panelGroup rendered="#{fn:length(team.garbages) gt 0}"
                                              style="#{fn:length(team.garbages) gt 15? 'max-height: 610px; overflow: scroll; display: block;' : ''}">
                                              
                                    <a4j:repeat value="#{team.garbages}" var="myGarbage">
   
                                    </a4j:repeat>
                                </h:panelGroup>
                            </h:panelGroup>

                          		 </div>
                           </a4j:repeat>
                         
                         </h:panelGroup>
                         
                         <br/>
                             
                            
                        </div>

                        <%-- Right Column (harta) --%>
                        <div id="rightColumn">

                            <a4j:form>
                                <m:map latitude="44.4317879"
                                       longitude="26.1015844"
                                       width="710px"
                                       height="600px"
                                       zoom="10"
                                       jsVariable="myMap" >
                                    <m:mapControl name="GLargeMapControl3D"/>
                                    <m:mapControl name="GMapTypeControl" position="G_ANCHOR_TOP_RIGHT"/>
                                    <m:mapControl name="GScaleControl" position="G_ANCHOR_BOTTOM_RIGHT" />

                                    
                                    <a4j:repeat value="#{areaCleanManager.garbageList}" var="gunoi">
                                        <m:marker  latitude="#{gunoi.coordYToString}"
                                                   longitude="#{gunoi.coordXToString}"
                                                   showInformationEvent="mouseover">
                                            <m:icon imageURL="http://app.letsdoitromania.ro:8080/LDIRBackendWebJSF2/icons/morman-rosu-20x20.png"
                                                    width="20"
                                                    height="20"/>
                                            <m:htmlInformationWindow htmlText="#{gunoi.infoHtml}"/>
                                        </m:marker>
                                    </a4j:repeat>
                                    <!-- zoom si focus pe zona de cartare -->
                                    <m:eventListener eventName="tilesloaded" jsFunction="zoomToArea('#{areaCleanManager.areaJsonBouns}')"/>
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
