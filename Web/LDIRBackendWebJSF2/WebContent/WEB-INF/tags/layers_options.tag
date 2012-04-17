<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib prefix="a4j" uri="http://richfaces.org/a4j"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@tag isELIgnored="false" body-content="tagdependent"%>
<h3><h:outputText value="#{msg.map_select_county}"/></h3>
                            <h:selectOneMenu styleClass="formDropdownLeft" onchange="centerOnCounty(this.value);">
                                <f:selectItem itemLabel="Selecteaza Judet" itemValue=""/>
                                <f:selectItems value="#{mapViewBean.countyItems}"/>
                            </h:selectOneMenu>
                            <br/>
                            <br/>
                             <h:selectManyCheckbox id="layers" 
								  value="mormaneToate" disabled="false" layout="pageDirection"
								  title="Layers"
								  onchange="layersoptions(this)">
								<f:selectItem id="mormaneToate" itemLabel="Toate" itemValue="mormaneToate" />
								<f:selectItem id="mormane2010" itemLabel="2010" itemValue="mormane2010" itemDisabled="true" />
								<f:selectItem id="mormane2011" itemLabel="2011" itemValue="mormane2011" />
								<f:selectItem id="mormane2012" itemLabel="2012" itemValue="mormane2012" />
								<f:selectItem id="mormaneCuratate" itemLabel="Curatate" itemValue="mormaneCuratate" />
								<f:selectItem id="mormaneNecuratate" itemLabel="Ne-curatate" itemValue="mormaneNecuratate" />
								<f:selectItem id="mormaneDeVotat" itemLabel="Propuse pentru curatenie" itemValue="mormaneDeVotat" />
							 </h:selectManyCheckbox><br/>
							 <h:outputText value="Legenda: "/><br/><br/>
							 <h:graphicImage value="/layout/images/m1.png" width="20px"></h:graphicImage><h:outputText value=" 1-10 mormane"/><br/>
							 <h:graphicImage value="/layout/images/m2.png" width="20px"></h:graphicImage><h:outputText value=" 10-100 mormane"/><br/>
							 <h:graphicImage value="/layout/images/m3.png" width="20px"></h:graphicImage><h:outputText value=" 100-1000 mormane"/><br/>							 
							<h:graphicImage value="/layout/images/m5.png" width="20px"></h:graphicImage><h:outputText value=" 1000+ mormane"/><br/>
							<br/>							
							 <h:graphicImage value="http://maps.gstatic.com/mapfiles/markers/marker_sprite.png" width="30px"></h:graphicImage><h:outputText value=" Morman"/><br/>
							 <br/>
							 <div id="ajaxloader" style="display:none">							 
							 <h:graphicImage value="/layout/images/ajaxloader.gif" width="20px"></h:graphicImage><h:outputText value="  Loading..." style="font-style:italic" /><br/>
							 </div>
							 
