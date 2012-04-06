<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:directive.include file="/WEB-INF/jspf/page-header.jspf" />
<jsp:directive.include file="/WEB-INF/jspf/login-checkpoint.jspf" />

<f:view>
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:directive.include file="/WEB-INF/jspf/page-meta.jspf" />
<jsp:directive.include file="/WEB-INF/jspf/googlemaps-meta.jspf"/>

<title>Let's do it Romania</title>
</head>
<body>
<jsp:directive.include file="/WEB-INF/jspf/usermeta.jspf"/>
	<center>
		<%-- page Top --%>
		<%-- 	<custom:page_top selected="lista_mormane"
			role="${voteGarbageManagerBean.userDetails.role}" /> --%>
		<custom:page_top_login selected="garbageVote" role="${voteGarbageManagerBean.userDetails.role}"/>
		<%-- page Content --%>
		<div id="pageContainer">
			<div id="contentList">

				<jsp:directive.include file="/WEB-INF/jspf/popup-loading.jspf" />
				<jsp:directive.include
					file="/WEB-INF/jspf/popup-admin-img-gallery.jspf" />
				<jsp:directive.include file="/WEB-INF/jspf/popup-garbage-vote.jspf" />

				<%-- Left Column --%>
				<div id="leftColumn">
					<div class="labelLeft">
						<h1>Selecteaza judetul:</h1>
					</div>
					<a4j:form>
						<div>
							<h3>
								Judet<br />
								<h:selectOneMenu rendered="true"
									onchange="loadCountyGarbageOverlay(this.value);">
									<f:selectItems value="#{mapViewBean.countyItems}" />
								</h:selectOneMenu>								
							</h3>
						</div>

						<a4j:commandButton
							actionListener="#{voteGarbageManagerBean.actionApplyFilterAsList}"
							styleClass="formButtonLeft"
							onclick="window.location = '/users/garbage-vote.jsf'"
							value="Vezi zonele in tabel" />
						<button type="reset" class="formButtonLeft">Anuleaza</button>
					</a4j:form>
				</div>

				<%-- Right Column (harta) --%>
				<div id="rightColumnList" style="min-height: 300px;">
					<h:selectOneRadio id="layers" value="toate" onchange="showhidemarkers(this)">
					  <f:selectItem id="item0" itemLabel="Toate" itemValue="toate" />
					  <f:selectItem id="item1" itemLabel="Mormane" itemValue="mormane" />
					  <f:selectItem id="item2" itemLabel="Zone de votat" itemValue="zonedevotat" />
					</h:selectOneRadio>
						 <a4j:form rendered="true">
                                <m:map width="750px" height="650px" latitude="44.4317879" longitude="26.1015844" zoom="10" jsVariable="myMap"  >
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
                                    <m:eventListener eventName="bounds_changed" jsFunction="onBoundsChanged" />     
                                    <m:eventListener eventName="bounds_changed" jsFunction="loadEvents()" />
                                    <m:eventListener eventName="load" jsFunction="onBoundsChanged()" />
                                </m:map>
                            </a4j:form>
					
				</div>

			</div>
		</div>


		<%-- page Bottom --%>
		<custom:page_bottom />
	</center>
</body>
	</html>
</f:view>
