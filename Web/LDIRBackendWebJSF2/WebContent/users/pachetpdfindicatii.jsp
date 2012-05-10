<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:directive.include file="/WEB-INF/jspf/page-header.jspf"/>
<f:view>
    <html xmlns="http://www.w3.org/1999/xhtml">
       
<f:verbatim>
<title>Pachetul de morman pentru mormanul:</f:verbatim><h:outputText value="#{mormanManager.garbageSimplu.garbageId}" /> <f:verbatim></title>

<style type="text/css">
<!--
body {
	font: 100% Verdana, Arial, Helvetica, sans-serif;
	background: #666666;
	margin: 0; /* it's good practice to zero the margin and padding of the body element to account for differing browser defaults */
	padding: 0;
	text-align: center; /* this centers the container in IE 5* browsers. The text is then set to the left aligned default in the #container selector */
	color: #000000;
}

.oneColElsCtrHdr #container {
	width: 60em;  /* this width will create a container that will fit in an 800px browser window if text is left at browser default font sizes */
	background: #FFFFFF;
	margin: 0 auto; /* the auto margins (in conjunction with a width) center the page */
	border: 1px solid #000000;
	text-align: left; /* this overrides the text-align: center on the body element. */
}
.oneColElsCtrHdr #header { 
	background: #DDDDDD; 
	padding: 0 10px 0 10px;  /* this padding matches the left alignment of the elements in the divs that appear beneath it. If an image is used in the #header instead of text, you may want to remove the padding. */
} 
.oneColElsCtrHdr #header h1 {
	margin: 0; /* zeroing the margin of the last element in the #header div will avoid margin collapse - an unexplainable space between divs. If the div has a border around it, this is not necessary as that also avoids the margin collapse */
	padding: 10px 0; /* using padding instead of margin will allow you to keep the element away from the edges of the div */
}
.oneColElsCtrHdr #mainContent {
	padding: 0 20px; /* remember that padding is the space inside the div box and margin is the space outside the div box */
	background: #FFFFFF;
}
.oneColElsCtrHdr #footer { 
	padding: 0 10px; /* this padding matches the left alignment of the elements in the divs that appear above it. */
	background:#DDDDDD;
} 
.oneColElsCtrHdr #footer p {
	margin: 0; /* zeroing the margins of the first element in the footer will avoid the possibility of margin collapse - a space between divs */
	padding: 10px 0; /* padding on this element will create space, just as the the margin would have, without the margin collapse issue */
}
.thinborder { border: 1px solid #000; }
.style2 {color: #CC0000}
/* Greyscale
Table Design by Scott Boyle, Two Plus Four
www.twoplusfour.co.uk
----------------------------------------------- */

table {border-collapse: collapse;
border: 2px solid #000;
font: normal 80%/140% arial, helvetica, sans-serif;
color: #555;
background: #fff;}

td, th {border: 1px dotted #bbb;
padding: .5em;}

caption {padding: 0 0 .5em 0;
text-align: left;
font-size: 1.4em;
font-weight: bold;
text-transform: uppercase;
color: #333;
background: transparent;}

/* =links
----------------------------------------------- */

table a {padding: 1px;
text-decoration: none;
font-weight: bold;
background: transparent;}

table a:link {border-bottom: 1px dashed #ddd;
color: #000;}

table a:visited {border-bottom: 1px dashed #ccc;
text-decoration: line-through;
color: #808080;}

table a:hover {border-bottom: 1px dashed #bbb;
color: #666;}

/* =head =foot
----------------------------------------------- */

thead th, tfoot th {border: 2px solid #000;
text-align: left;
font-size: 1.2em;
font-weight: bold;
color: #333;
background: transparent;}

tfoot td {border: 2px solid #000;}

/* =body
----------------------------------------------- */

tbody th, tbody td {vertical-align: top;
text-align: left;}

tbody th {white-space: nowrap;}

.odd {background: #fcfcfc;}

tbody tr:hover {background: #fafafa;}
.style4 {
	font-size: 9pt;
	color: #666666;
	font-weight: bold;
}
.style5 {
	font-size: 9pt;
	color: #666666;
	font-weight: bold;
}
-->
</style>
<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
<script type="text/javascript">
	function showlinks(element) {

		// Grid center coordonates, format: lat, lng
		var gridgoogle = </f:verbatim><h:outputText value="\'#{mormanManager.latitudine},#{mormanManager.longitudine}\'" /><f:verbatim>
		latgrid=Number(gridgoogle.split(',')[1])
		lnggrid=Number(gridgoogle.split(',')[0])
		var gridbing =  </f:verbatim><h:outputText value="\'#{mormanManager.latitudine}_#{mormanManager.longitudine}\'" /><f:verbatim>

		var departure = element.value;
		latoras=Number(departure.split(',')[1])
		lngoras=Number(departure.split(',')[0])
		bingoras=String(lngoras)+'_'+String(latoras)
		bingCenter=String((lngoras+lnggrid)/2)+","+String((latoras+latgrid)/2)
	
		googlelink=	'http://maps.google.com/maps?f=d&source=s_d&saddr=[oras]&daddr=[gridgoogle]'

		googlelink=googlelink.replace('[gridgoogle]',gridgoogle);
		googlelink=googlelink.replace('[oras]',departure);
		//http://www.bing.com/maps/default.aspx?v=2&rtp=pos.44.4325_26.103889~pos.44.37659_25.86513
		binglink='http://www.bing.com/maps/default.aspx?v=2&rtp=pos.[oras]~pos.[gridbing]&mode=D&rtop=0~0~0~&cmw=797&cmh=617&u=0&style=u;'

		binglink='http://www.bing.com/maps/default.aspx?v=2&rtp=pos.[oras]~pos.[gridbing]'

		
		//departure=departure.replace(' ','_')
		//binglink=binglink.replace('[bingCenter]',bingCenter);
		binglink=binglink.replace('[gridbing]',gridbing);
		//departure=departure.replace(',','_')
		binglink=binglink.replace('[oras]',bingoras);

		yahoolink='http://maps.yahoo.com/#mvt=m&q1=[oras]&q2=[gridgoogle]'
		yahoolink=yahoolink.replace('[gridgoogle]',gridgoogle);
		yahoolink=yahoolink.replace('[oras]',departure);
	
		document.getElementById('yahoo-link').href = yahoolink;
		document.getElementById('bing-link').href = binglink;
		document.getElementById('google-link').href = googlelink;
		//document.getElementById('dirFrame').src = yahoolink;



	}
 
  var directionDisplay;
  var directionsService = new google.maps.DirectionsService();
  var map;

  function initialize() {
	  
    directionsDisplay = new google.maps.DirectionsRenderer();
    var chicago = new google.maps.LatLng(44.416667,26.1);
    
	var morman= new google.maps.LatLng(</f:verbatim><h:outputText value="#{mormanManager.latitudine},#{mormanManager.longitudine}" /><f:verbatim>);
    var myOptions = {
      zoom:7,
	  preserveViewport:false,
      mapTypeId: google.maps.MapTypeId.ROADMAP,
	  center:chicago
     }

    map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);

  var marker = new google.maps.Marker({
      position: morman, 
      map: map, 
      title: </f:verbatim><h:outputText value="\'MORMAN #{mormanManager.garbageSimplu.garbageId}\'" /><f:verbatim>
  }); 
  	map.setCenter(morman);
  	map.setZoom(14);
  	
    directionsDisplay.setMap(map);
  }
  
  function calcRoute(element) {	
    var start = </f:verbatim><h:outputText value="\'#{mormanManager.latitudine},#{mormanManager.longitudine}\'" /><f:verbatim>
	
    var end = element.value;
    var request = {
        origin:start, 
        destination:end,
        travelMode: google.maps.DirectionsTravelMode.DRIVING
    };
    directionsService.route(request, function(response, status) {
      if (status == google.maps.DirectionsStatus.OK) {
        directionsDisplay.setDirections(response);
      }
    });
    showlinks(element);
  }
</script>

</head>

<body class="oneColElsCtrHdr" onLoad="initialize()">
<div id="container">
  <div id="header">
    <h1>Morman  </f:verbatim><h:outputText value="#{mormanManager.garbageSimplu.garbageId}" /><f:verbatim></h1>
  <div id="mainContent">
    <h3>1. Date Generale</h3>
    <p><strong>Judeţul</strong>:  </f:verbatim><h:outputText value="#{mormanManager.garbageSimplu.county.name}" /><f:verbatim><br />
      <strong>Comuna:</strong>  </f:verbatim><h:outputText value="#{mormanManager.garbageSimplu.town.name}" /><f:verbatim><br />
      Zona de cartare: </f:verbatim><h:outputText value="#{mormanManager.garbageSimplu.chartedArea.name}" /> <f:verbatim><br />
      <br />
      Situarea Geografica (in grade zecimale şi grade minute secunde): <br />
    </p>
    <table cellspacing="0" cellpadding="0">
      <col width="64" span="3" />
      <tr height="17">
        <td height="17" width="100"><div align="center"></div></td>
        <td width="149"><div align="center">Latitudine</div></td>
        <td width="166"><div align="center">Longitudine</div></td>
      </tr>
      <tr height="36">
        <td rowspan="2" height="73"><div align="center">Coordonate</div></td>
        <td align="right"><div align="center"> </f:verbatim><h:outputText value="#{mormanManager.latitudine}" /><f:verbatim></div></td>
        <td align="right"><div align="center"> </f:verbatim><h:outputText value="#{mormanManager.longitudine}" /><f:verbatim></div></td>
      </tr>
      <tr height="37">
        <td height="37" align="right"><div align="center"></f:verbatim><h:outputText value="#{mormanManager.lat_grd}° #{mormanManager.lat_min}\' #{mormanManager.lat_sec}\"" /><f:verbatim></div></td>
        <td align="right"><div align="center"></f:verbatim><h:outputText value="#{mormanManager.long_grd}° #{mormanManager.long_min}\' #{mormanManager.long_sec}\"" /><f:verbatim></div></td>
      </tr>
    </table>
    <p><strong>Descriere</strong>:<br />
     </f:verbatim><h:outputText value="#{mormanManager.garbageSimplu.description}" /><f:verbatim></p>
    <p><strong>Numar saci: </f:verbatim><h:outputText value="#{mormanManager.garbageSimplu.bagCount}" /><f:verbatim></strong></p>
    <p><strong>Componenta gunoi:</strong><br />
      </f:verbatim><h:outputText rendered="#{not (mormanManager.garbageSimplu.percentagePlastic eq 0)}" value="» Procent plastic: #{mormanManager.garbageSimplu.percentagePlastic}%<br />" escape="false"/><f:verbatim>
      </f:verbatim><h:outputText rendered="#{not (mormanManager.garbageSimplu.percentageGlass eq 0)}" value="» Procent sticla: #{mormanManager.garbageSimplu.percentageGlass}%<br />" escape="false"/><f:verbatim>
      </f:verbatim><h:outputText rendered="#{not (mormanManager.garbageSimplu.percentageMetal eq 0)}" value="» Procent metale: #{mormanManager.garbageSimplu.percentageMetal}%<br />" escape="false" /><f:verbatim>
      </f:verbatim><h:outputText rendered="#{not (mormanManager.garbageSimplu.percentageWaste eq 0)}" value="» Procent nereciclabile: #{mormanManager.garbageSimplu.percentageWaste}%<br />" escape="false" /><f:verbatim>
      </f:verbatim><h:outputText rendered="#{not (mormanManager.garbageSimplu.description.length() eq 0)}" value="» Descriere componente mari:  #{mormanManager.garbageSimplu.bigComponentsDescription}" /><f:verbatim><br />
    </p>   
    <p>Locatia mormanului:</p> 
    <div id="map_canvas" style="width:56em; height:600px"></div>    
    <p>&nbsp;</p>
    
    <!-- end #mainContent --></div>
    
  <div id="footer">
    <p>&nbsp;</p>
  <!-- end #footer --></div>
<!-- end #container --></div>
</body>
</html>
</f:verbatim>
   
    </html>
</f:view>
