<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib prefix="a4j" uri="http://richfaces.org/a4j"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@tag isELIgnored="false" body-content="tagdependent"%>
<%@attribute name="selected"%>

<h3><h:outputText value="Selecteaza un judet pentru vedea gunoaiele ce se pot curata in 2012"/></h3>
                            <h:selectOneMenu styleClass="formDropdownLeft" onchange="centerOnCounty(this.value);">
                                <f:selectItem itemLabel="Selecteaza Judet" itemValue=""/>
                                <f:selectItems value="#{mapViewBean.countyItems}"/>
                            </h:selectOneMenu>
                            <br/>
                            <br/>
                              
                            <c:if test="${not (selected eq 'mormaneDeVotat')}">
							<h:selectManyCheckbox id="layers"
                             value="mormaneDeCuratat"
                             disabled="false" layout="pageDirection"
								  title="Layers"
						onchange="layersoptions(this)">
								
								<f:selectItem id="mormaneDeCuratat" itemLabel="Mormane de curatat 2012" itemValue="mormaneDeCuratat" itemDisabled="true"/>
						</h:selectManyCheckbox><br/>
							   
							</c:if> 
							 <c:if test="${selected eq 'mormaneDeVotat'}">
								   <h:selectManyCheckbox id="layers"
                             value="mormaneDeCuratat"
                             disabled="false" layout="pageDirection"
								  title="Layers"
						onchange="layersoptions(this)">
								<f:selectItem id="mormaneDeCuratat" itemLabel="Mormane de curatat 2012" itemValue="mormaneDeCuratat" itemDisabled="true"/>
								 </h:selectManyCheckbox><br/>
							</c:if>

								  
							 <h:outputText value="Legenda: "/><br/><br/>
							 <h:graphicImage value="/layout/images/m1.png" width="20px"></h:graphicImage><h:outputText value=" 1-10 gunoaie"/><br/>
							 <h:graphicImage value="/layout/images/m2.png" width="20px"></h:graphicImage><h:outputText value=" 10-100 gunoaie"/><br/>
							 <h:graphicImage value="/layout/images/m3.png" width="20px"></h:graphicImage><h:outputText value=" 100-1000 gunoaie"/><br/>							 
							<h:graphicImage value="/layout/images/m4.png" width="20px"></h:graphicImage><h:outputText value=" 1000+ gunoaie"/><br/>
							<br/>			
							 <h:graphicImage value="http://app.letsdoitromania.ro/layout/images/UNALLOCATED.png" width="30px"/><h:outputText value="Gunoi identificat"/><br/>
							 <h:graphicImage value="http://app.letsdoitromania.ro/layout/images/PARTIALLY.png" width="30px"/><h:outputText value=" Gunoi alocat partial"/><br/>
							 <h:graphicImage value="http://app.letsdoitromania.ro/layout/images/COMPLETELY.png" width="30px"/><h:outputText value=" Gunoi alocat complet"/><br/>	
							 <h:graphicImage value="http://app.letsdoitromania.ro/layout/images/CLEANED.png" width="30px"/><h:outputText value=" Gunoi curatat"/><br/>	
							 <br/>
							 <div id="ajaxloader" style="display:none">							 
							 <h:graphicImage value="/layout/images/ajaxloader.gif" width="20px"></h:graphicImage><h:outputText value="  Loading..." style="font-style:italic" /><br/>
							 </div>
							 
