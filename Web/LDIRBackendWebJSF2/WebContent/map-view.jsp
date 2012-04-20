<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:directive.include file="/WEB-INF/jspf/page-header.jspf"/>
<f:view>
    <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
            <jsp:directive.include file="/WEB-INF/jspf/page-meta.jspf"/>
            <jsp:directive.include file="/WEB-INF/jspf/googlemaps-meta.jspf"/>
            <title>Let's do it Romania</title>
        </head>
        <body>
        <jsp:directive.include file="/WEB-INF/jspf/usermeta.jspf"/>
            <center>
                <%-- page Top --%>
                <h:panelGroup rendered="#{sessionScope['USER_DETAILS'] eq null}">
                    <custom:page_top selected="mapview"/>
                </h:panelGroup>
                <h:panelGroup rendered="#{not (sessionScope['USER_DETAILS'] eq null)}">
                    <custom:page_top_login selected="mapview"/>
                </h:panelGroup>

                <%-- page Content --%>
                <div id="pageContainer">
                    <div id="content">
                        <%-- left column (empty) --%>
                        <div id="leftColumn">
                           <custom:layers_options selected="mormaneDeVotat"/> 

                        </div>
                        <%-- righ column (harta cu gunoaie) --%>
                        <div id="rightColumn">
							<div id="map" style="width: 100%; height: 600px"></div>
                        </div>
                    </div>
                </div>

                <%-- page Bottom --%>
                <custom:page_bottom/>
            </center>
        </body>
    </html>
</f:view>
