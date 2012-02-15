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
                            <h3><h:outputText rendered="false" value="#{msg.map_select_county}"/></h3>
                            <h:form>
                            <h:selectOneMenu  rendered="false" value="#{areaCleanManager.country}" styleClass="formDropdownLeft">
                                <f:selectItem itemLabel="Selecteaza Judet" itemValue=""/>
                                <f:selectItems value="#{areaCleanManager.countyItems}"/>
                            </h:selectOneMenu>
                            <br/><br/>
                            <h3><h:outputText value="Alege cel mai apropiat oras"/></h3>
                            
                            <h:selectOneMenu styleClass="formDropdownLeft" onchange="showlinks(this);">
                                <f:selectItem itemLabel="Selecteaza Localitate" itemValue=""/>
								<f:selectItem itemValue="46.3103035360546,23.7182172002125" itemLabel="Aiud"/>
								<f:selectItem itemValue="46.0694759364561,23.5736700475653" itemLabel="Alba Iulia"/>
								<f:selectItem itemValue="43.9696678454095,25.332721448277" itemLabel="Alexandria"/>
								<f:selectItem itemValue="46.1891293538884,21.3127831247694" itemLabel="Arad"/>
								<f:selectItem itemValue="46.5665132000984,26.9113387603545" itemLabel="Bacău"/>
								<f:selectItem itemValue="47.6557929062524,23.5832089566312" itemLabel="Baia Mare"/>
								<f:selectItem itemValue="44.3544210306152,24.0977514092562" itemLabel="Balş"/>
								<f:selectItem itemValue="46.2257913442237,27.6706991873965" itemLabel="Bârlad"/>
								<f:selectItem itemValue="44.0209153600287,23.3459415876305" itemLabel="Băileşti"/>
								<f:selectItem itemValue="47.1320012176196,24.4935924194196" itemLabel="Bistrița"/>
								<f:selectItem itemValue="47.6551406045343,24.663016022893" itemLabel="Borşa"/>
								<f:selectItem itemValue="47.739843264896,26.6715618800112" itemLabel="Botoşani"/>
								<f:selectItem itemValue="45.6517059902193,25.6011289211613" itemLabel="Braşov"/>
								<f:selectItem itemValue="45.2718962760987,27.97499622024" itemLabel="Brăila"/>
								<f:selectItem itemValue="44.4325,26.103889" itemLabel="Bucureşti"/>
								<f:selectItem itemValue="46.7203049853696,26.6997025637237" itemLabel="Buhuşi"/>
								<f:selectItem itemValue="45.1504554541058,26.8213576650806" itemLabel="Buzău"/>
								<f:selectItem itemValue="46.5441700673624,23.8847182951222" itemLabel="Câmpia Turzii"/>
								<f:selectItem itemValue="45.1261916069452,25.734955325629" itemLabel="Câmpina"/>
								<f:selectItem itemValue="45.277588271195,25.0511791038778" itemLabel="Câmpulung"/>
								<f:selectItem itemValue="47.5298125614299,25.5597481954397" itemLabel="Câmpulung Moldovenesc"/>
								<f:selectItem itemValue="44.1127338687156,24.3508850979109" itemLabel="Caracal"/>
								<f:selectItem itemValue="45.411968235466,22.2185151503899" itemLabel="Caransebeş"/>
								<f:selectItem itemValue="47.6857601901834,22.467416788701" itemLabel="Carei"/>
								<f:selectItem itemValue="44.1958004712324,27.3365013166047" itemLabel="Călăraşi"/>
								<f:selectItem itemValue="44.3433062871586,28.0369446620649" itemLabel="Cernavodă"/>
								<f:selectItem itemValue="46.7779139025907,23.6051175719926" itemLabel="Cluj-Napoca"/>
								<f:selectItem itemValue="45.6994426791675,25.4477819220605" itemLabel="Codlea"/>
								<f:selectItem itemValue="46.4215649618654,26.4431962238024" itemLabel="Comăneşti"/>
								<f:selectItem itemValue="44.1756295872839,28.6278237194653" itemLabel="Constanța"/>
								<f:selectItem itemValue="43.7766174532215,24.5040840935536" itemLabel="Corabia"/>
								<f:selectItem itemValue="44.3185460894783,23.8032646645854" itemLabel="Craiova"/>
								<f:selectItem itemValue="45.8362887475687,23.3740956280605" itemLabel="Cugir"/>
								<f:selectItem itemValue="45.1423279046406,24.6781171955985" itemLabel="Curtea de Argeş"/>
								<f:selectItem itemValue="47.1420139438406,23.8769377979213" itemLabel="Dej"/>
								<f:selectItem itemValue="45.8795617551267,22.9048539517642" itemLabel="Deva"/>
								<f:selectItem itemValue="47.9529134283811,26.4009603849245" itemLabel="Dorohoi"/>
								<f:selectItem itemValue="44.6605386781759,24.2601012520219" itemLabel="Drăgăşani"/>
								<f:selectItem itemValue="44.6344181530031,22.6600345816975" itemLabel="Drobeta-Turnu Severin"/>
								<f:selectItem itemValue="45.8409762993704,24.9734764871545" itemLabel="Făgăraş"/>
								<f:selectItem itemValue="47.4609225754717,26.2984238590685" itemLabel="Fălticeni"/>
								<f:selectItem itemValue="44.4195957916529,27.8253582487364" itemLabel="Feteşti-Gară"/>
								<f:selectItem itemValue="45.6998785727708,27.1843148109934" itemLabel="Focşani"/>
								<f:selectItem itemValue="45.4407833217965,28.0411806998036" itemLabel="Galați"/>
								<f:selectItem itemValue="46.7216784193434,25.6055747758852" itemLabel="Gheorgheni"/>
								<f:selectItem itemValue="47.0306677048026,23.9115328851326" itemLabel="Gherla"/>
								<f:selectItem itemValue="43.8905051195169,25.9660905227562" itemLabel="Giurgiu"/>
								<f:selectItem itemValue="46.6731972451966,28.0595243247817" itemLabel="Huşi"/>
								<f:selectItem itemValue="45.7525424734363,22.9023690872688" itemLabel="Hunedoara"/>
								<f:selectItem itemValue="47.1618401742699,27.5845076897174" itemLabel="Iaşi"/>
								<f:selectItem itemValue="45.6856961015978,21.9073441522523" itemLabel="Lugoj"/>
								<f:selectItem itemValue="45.3565359394899,23.2216223283391" itemLabel="Lupeni"/>
								<f:selectItem itemValue="43.8176623876889,28.5801286963902" itemLabel="Mangalia"/>
								<f:selectItem itemValue="44.2478715145439,28.2725807028537" itemLabel="Medgidia"/>
								<f:selectItem itemValue="46.1620500199661,24.3583405722444" itemLabel="Mediaş"/>
								<f:selectItem itemValue="46.3633087565411,25.8072985229316" itemLabel="Miercurea Ciuc"/>
								<f:selectItem itemValue="44.9590496346341,24.9419832831355" itemLabel="Mioveni"/>
								<f:selectItem itemValue="46.4750047150835,26.4927835757206" itemLabel="Moineşti"/>
								<f:selectItem itemValue="44.9832314043473,25.6426808293116" itemLabel="Moreni"/>
								<f:selectItem itemValue="44.8031115264727,22.974141346593" itemLabel="Motru"/>
								<f:selectItem itemValue="44.3206799135807,28.6114876157018" itemLabel="Năvodari"/>
								<f:selectItem itemValue="46.3064615598485,25.2955061813589" itemLabel="Odorheiu Secuiesc"/>
								<f:selectItem itemValue="44.0920350113515,26.6418256735846" itemLabel="Oltenița"/>
								<f:selectItem itemValue="46.2482731292601,26.7661570146722" itemLabel="Oneşti"/>
								<f:selectItem itemValue="47.0535263115449,21.9363283638003" itemLabel="Oradea"/>
								<f:selectItem itemValue="45.8384087526808,23.1988493594747" itemLabel="Orăştie"/>
								<f:selectItem itemValue="47.2534006972286,26.7235945054475" itemLabel="Paşcani"/>
								<f:selectItem itemValue="45.4496930180669,23.3900966431379" itemLabel="Petrila"/>
								<f:selectItem itemValue="45.4192510503934,23.3684728915202" itemLabel="Petroşani"/>
								<f:selectItem itemValue="46.9342503168442,26.3700632958137" itemLabel="Piatra-Neamț"/>
								<f:selectItem itemValue="44.8585506504426,24.8763380237587" itemLabel="Piteşti"/>
								<f:selectItem itemValue="44.9428113058871,26.021157404645" itemLabel="Ploieşti"/>
								<f:selectItem itemValue="45.3832429948148,27.0516238552949" itemLabel="Râmnicu Sărat"/>
								<f:selectItem itemValue="45.1062558963198,24.3710774893546" itemLabel="Râmnicu Vâlcea"/>
								<f:selectItem itemValue="47.8508962330248,25.9156991399115" itemLabel="Rădăuți"/>
								<f:selectItem itemValue="46.7799642320703,24.7019381548709" itemLabel="Reghin"/>
								<f:selectItem itemValue="45.2948614155575,21.9028333926154" itemLabel="Reşița"/>
								<f:selectItem itemValue="46.929726071952,26.9367807070858" itemLabel="Roman"/>
								<f:selectItem itemValue="44.1136815358066,24.9872214183517" itemLabel="Rosiori de Vede"/>
								<f:selectItem itemValue="46.8028328313294,21.6641514778907" itemLabel="Salonta"/>
								<f:selectItem itemValue="47.7955854206523,22.8759506238845" itemLabel="Satu Mare"/>
								<f:selectItem itemValue="45.6177238840657,25.6988969145622" itemLabel="Săcele"/>
								<f:selectItem itemValue="45.957934188967,23.5671278874138" itemLabel="Sebeş"/>
								<f:selectItem itemValue="45.8646621301704,25.7880513679512" itemLabel="Sfântu  Gheorghe"/>
								<f:selectItem itemValue="45.7931385203533,24.152133984469" itemLabel="Sibiu"/>
								<f:selectItem itemValue="47.9288904656214,23.8953851997279" itemLabel="Sighetu Marmației"/>
								<f:selectItem itemValue="46.2214134172428,24.7927944806714" itemLabel="Sighişoara"/>
								<f:selectItem itemValue="44.4305524731605,24.3610508626085" itemLabel="Slatina"/>
								<f:selectItem itemValue="44.5626881599902,27.3759204756949" itemLabel="Slobozia"/>
								<f:selectItem itemValue="47.674574933648,26.2810883755589" itemLabel="Suceava"/>
								<f:selectItem itemValue="44.929119527058,25.4586973498359" itemLabel="Târgovişte"/>
								<f:selectItem itemValue="45.0360689150007,23.2756081120375" itemLabel="Târgu Jiu"/>
								<f:selectItem itemValue="46.5405061758216,24.5597813245638" itemLabel="Târgu Mureş"/>
								<f:selectItem itemValue="45.9996942800373,26.1347129237213" itemLabel="Târgu Secuiesc"/>
								<f:selectItem itemValue="46.3319138163826,24.2966181719319" itemLabel="Târnăveni"/>
								<f:selectItem itemValue="45.8515456609387,27.4282558975573" itemLabel="Tecuci"/>
								<f:selectItem itemValue="45.7564070278526,21.2297396512115" itemLabel="Timişoara"/>
								<f:selectItem itemValue="45.1797467504774,28.7864238824664" itemLabel="Tulcea"/>
								<f:selectItem itemValue="46.5745618015618,23.785738623348" itemLabel="Turda"/>
								<f:selectItem itemValue="43.7464570329024,24.868928686102" itemLabel="Turnu Măgurele"/>
								<f:selectItem itemValue="46.6411569681853,27.7305459771744" itemLabel="Vaslui"/>
								<f:selectItem itemValue="44.4902279060705,26.1843855740225" itemLabel="Voluntari"/>
								<f:selectItem itemValue="45.376458975131,23.2934946844464" itemLabel="Vulcan"/>
								<f:selectItem itemValue="47.179304651952,23.0575498383573" itemLabel="Zalău"/>
								<f:selectItem itemValue="45.5609288621234,25.3178721504729" itemLabel="Zărneşti"/>


                            </h:selectOneMenu>
                            <br/><br/>
                             <!-- BUTOANE -->
                                <div>
                                    <h:commandButton action="#{areaCleanManager.garbageFromCountry}"
                                                     value="Listeaza"
                                                     id="confirma"
                                                     rendered="false"
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
                          		 	   <br/>
                          		 	   <h3><h:outputText value="Echipa: #{team.teamName}" /></h3>
								<h:form>
                                        <h:commandLink action="#{areaCleanManager.actionSelectTeam}">
                                            <f:param name="team" value="#{team.teamId}"/>
                                            <h:outputText value="Aloca pt aceasta echipa" escape="false"/>
                                        </h:commandLink>
                                </h:form>	 	   
                            <span class=important><h:outputText value="<strong>Echipa selectata.</strong><br/>" escape="false" rendered="#{areaCleanManager.teamSelected.teamId eq team.teamId}"/></span>
 							<h:outputText value="Membrii: #{team.countMembers()}" escape="false"/></span><br/>
							<h:outputText value="Putere de curatare: #{team.getBagsEnrolled()} / #{team.getCleaningPower()*team.countMembers()} saci" escape="false"/></span><br/>
 							<h:panelGroup rendered="#{fn:length(team.garbages) eq 0}">
                                <h:outputText value="#{msg.clean_empty_list}"/>
                            </h:panelGroup>
                            <h:panelGroup rendered="#{fn:length(team.garbages) gt 0}">
                                <h3><h:outputText value="#{msg.map_list_morman}  (#{fn:length(team.garbages)})"/></h3>
                                <h:panelGroup rendered="#{fn:length(team.garbages) gt 0}"
                                              style="#{fn:length(team.garbages) gt 15? 'max-height: 610px; overflow: scroll; display: block;' : ''}">
                                              
                                    <a4j:repeat value="#{team.garbages}" var="myGarbage">
   										<h:outputText escape="false" value="#{msg.details_morman} "/>                                		
                                		 <h:outputLink value="curatenie-morman-detalii.jsf?garbageId=#{myGarbage.garbageId}">
                                                <h:outputText value="#{myGarbage.garbageId}" escape="false"/>
                                            </h:outputLink>
                                		<br/>
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
                            <!-- mesaj eroare sau info -->
                            <h:messages warnClass="registerMessageError" errorClass="registerMessageError" infoClass="registerMessageOk"/>

                            <a4j:form>
                                <m:map latitude="#{areaCleanManager.currentLat}"
                                       longitude="#{areaCleanManager.currentLng}"
                                       width="710px"
                                       height="600px"
                                       zoom="11"
                                       jsVariable="myMap" 
                                       
                                       >
                                    
                                    <m:mapControl name="GLargeMapControl3D"/>
                                    <m:mapControl name="GMapTypeControl" position="G_ANCHOR_TOP_RIGHT"/>
                                    <m:mapControl name="GScaleControl" position="G_ANCHOR_BOTTOM_RIGHT" />
									
                                    <!-- 
                                    <a4j:repeat value="#{areaCleanManager.garbageList}" var="gunoi">
                                        <m:marker  latitude="#{gunoi.coordYToString}"
                                                   longitude="#{gunoi.coordXToString}"
                                                   showInformationEvent="mouseover">
                                            <m:icon imageURL="http://app.letsdoitromania.ro:8080/LDIRBackendWebJSF2/icons/morman-rosu-20x20.png"
                                                    width="20"
                                                    height="20"/>
                                            <m:htmlInformationWindow htmlText="#{gunoi.infoHtml}"/>
                                        </m:marker>
                                    </a4j:repeat>-->
                                    <!-- zoom si focus pe zona de cartare -->
                                    <m:eventListener eventName="bounds_changed" jsFunction="onBoundsChanged" />     
                                    <m:eventListener eventName="bounds_changed" jsFunction="loadEvents()" />
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
