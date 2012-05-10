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
	
	var orase=new Array();
	orase["BUCURESTI"]="44.4325,26.103889"
	orase["AIUD"]="46.31030354,23.7182172"
	orase["AIUD"]="46.3103035360546,23.7182172002125"
	orase["ALBA-IULIA"]="46.0694759364561,23.5736700475653"
	orase["ALEXANDRIA"]="43.9696678454095,25.332721448277"
	orase["ARAD"]="46.1891293538884,21.3127831247694"
	orase["BACAU"]="46.5665132000984,26.9113387603545"
	orase["BAIA-MARE"]="47.6557929062524,23.5832089566312"
	orase["BALS"]="44.3544210306152,24.0977514092562"
	orase["BARLAD"]="46.2257913442237,27.6706991873965"
	orase["BAILESTI"]="44.0209153600287,23.3459415876305"
	orase["BISTRITA"]="47.1320012176196,24.4935924194196"
	orase["BORSA"]="47.6551406045343,24.663016022893"
	orase["BOTOSANI"]="47.739843264896,26.6715618800112"
	orase["BRASOV"]="45.6517059902193,25.6011289211613"
	orase["BRAILA"]="45.2718962760987,27.97499622024"
	orase["BUHUSI"]="46.7203049853696,26.6997025637237"
	orase["BUZAU"]="45.1504554541058,26.8213576650806"
	orase["CAMPIA-TURZII"]="46.5441700673624,23.8847182951222"
	orase["CAMPINA"]="45.1261916069452,25.734955325629"
	orase["CAMPULUNG"]="45.277588271195,25.0511791038778"
	orase["CAMPULUNG-MOLDOVENESC"]="47.5298125614299,25.5597481954397"
	orase["CARACAL"]="44.1127338687156,24.3508850979109"
	orase["CARANSEBES"]="45.411968235466,22.2185151503899"
	orase["CAREI"]="47.6857601901834,22.467416788701"
	orase["CALARASI"]="44.1958004712324,27.3365013166047"
	orase["CERNAVODA"]="44.3433062871586,28.0369446620649"
	orase["CLUJ-NAPOCA"]="46.7779139025907,23.6051175719926"
	orase["CODLEA"]="45.6994426791675,25.4477819220605"
	orase["COMANESTI"]="46.4215649618654,26.4431962238024"
	orase["CONSTANTA"]="44.1756295872839,28.6278237194653"
	orase["CORABIA"]="43.7766174532215,24.5040840935536"
	orase["CRAIOVA"]="44.3185460894783,23.8032646645854"
	orase["CUGIR"]="45.8362887475687,23.3740956280605"
	orase["CURTEA-DE-ARGES"]="45.1423279046406,24.6781171955985"
	orase["DEJ"]="47.1420139438406,23.8769377979213"
	orase["DEVA"]="45.8795617551267,22.9048539517642"
	orase["DOROHOI"]="47.9529134283811,26.4009603849245"
	orase["DRAGASANI"]="44.6605386781759,24.2601012520219"
	orase["DROBETA-TURNU-SEVERIN"]="44.6344181530031,22.6600345816975"
	orase["FAGARAS"]="45.8409762993704,24.9734764871545"
	orase["FALTICENI"]="47.4609225754717,26.2984238590685"
	orase["FETESTI-GARA"]="44.4195957916529,27.8253582487364"
	orase["FOCSANI"]="45.6998785727708,27.1843148109934"
	orase["GALATI"]="45.4407833217965,28.0411806998036"
	orase["GHEORGHENI"]="46.7216784193434,25.6055747758852"
	orase["GHERLA"]="47.0306677048026,23.9115328851326"
	orase["GIURGIU"]="43.8905051195169,25.9660905227562"
	orase["HUSI"]="46.6731972451966,28.0595243247817"
	orase["HUNEDOARA"]="45.7525424734363,22.9023690872688"
	orase["IASI"]="47.1618401742699,27.5845076897174"
	orase["LUGOJ"]="45.6856961015978,21.9073441522523"
	orase["LUPENI"]="45.3565359394899,23.2216223283391"
	orase["MANGALIA"]="43.8176623876889,28.5801286963902"
	orase["MEDGIDIA"]="44.2478715145439,28.2725807028537"
	orase["MEDIAS"]="46.1620500199661,24.3583405722444"
	orase["MIERCUREA-CIUC"]="46.3633087565411,25.8072985229316"
	orase["MIOVENI"]="44.9590496346341,24.9419832831355"
	orase["MOINESTI"]="46.4750047150835,26.4927835757206"
	orase["MORENI"]="44.9832314043473,25.6426808293116"
	orase["MOTRU"]="44.8031115264727,22.974141346593"
	orase["NAVODARI"]="44.3206799135807,28.6114876157018"
	orase["ODORHEIU-SECUIESC"]="46.3064615598485,25.2955061813589"
	orase["OLTENITA"]="44.0920350113515,26.6418256735846"
	orase["ONESTI"]="46.2482731292601,26.7661570146722"
	orase["ORADEA"]="47.0535263115449,21.9363283638003"
	orase["ORASTIE"]="45.8384087526808,23.1988493594747"
	orase["PASCANI"]="47.2534006972286,26.7235945054475"
	orase["PETRILA"]="45.4496930180669,23.3900966431379"
	orase["PETROSANI"]="45.4192510503934,23.3684728915202"
	orase["PIATRA-NEAMT"]="46.9342503168442,26.3700632958137"
	orase["PITESTI"]="44.8585506504426,24.8763380237587"
	orase["PLOIESTI"]="44.9428113058871,26.021157404645"
	orase["RAMNICU-SARAT"]="45.3832429948148,27.0516238552949"
	orase["RAMNICU-VALCEA"]="45.1062558963198,24.3710774893546"
	orase["RADAUTI"]="47.8508962330248,25.9156991399115"
	orase["REGHIN"]="46.7799642320703,24.7019381548709"
	orase["RESITA"]="45.2948614155575,21.9028333926154"
	orase["ROMAN"]="46.929726071952,26.9367807070858"
	orase["ROSIORI-DE-VEDE"]="44.1136815358066,24.9872214183517"
	orase["SALONTA"]="46.8028328313294,21.6641514778907"
	orase["SATU-MARE"]="47.7955854206523,22.8759506238845"
	orase["SACELE"]="45.6177238840657,25.6988969145622"
	orase["SEBES"]="45.957934188967,23.5671278874138"
	orase["SFANTU-GHEORGHE"]="45.8646621301704,25.7880513679512"
	orase["SIBIU"]="45.7931385203533,24.152133984469"
	orase["SIGHETU-MARMATIEI"]="47.9288904656214,23.8953851997279"
	orase["SIGHISOARA"]="46.2214134172428,24.7927944806714"
	orase["SLATINA"]="44.4305524731605,24.3610508626085"
	orase["SLOBOZIA"]="44.5626881599902,27.3759204756949"
	orase["SUCEAVA"]="47.674574933648,26.2810883755589"
	orase["TARGOVISTE"]="44.929119527058,25.4586973498359"
	orase["TARGU-JIU"]="45.0360689150007,23.2756081120375"
	orase["TARGU-MURES"]="46.5405061758216,24.5597813245638"
	orase["TARGU-SECUIESC"]="45.9996942800373,26.1347129237213"
	orase["TARNAVENI"]="46.3319138163826,24.2966181719319"
	orase["TECUCI"]="45.8515456609387,27.4282558975573"
	orase["TIMISOARA"]="45.7564070278526,21.2297396512115"
	orase["TULCEA"]="45.1797467504774,28.7864238824664"
	orase["TURDA"]="46.5745618015618,23.785738623348"
	orase["TURNU-MAGURELE"]="43.7464570329024,24.868928686102"
	orase["VASLUI"]="46.6411569681853,27.7305459771744"
	orase["VOLUNTARI"]="44.4902279060705,26.1843855740225"
	orase["VULCAN"]="45.376458975131,23.2934946844464"
	orase["ZALAU"]="47.179304651952,23.0575498383573"
	orase["ZARNESTI"]="45.5609288621234,25.3178721504729"

	var orasPornire="</f:verbatim><h:outputText value='#{param["orasPornire"]}'/><f:verbatim>";
	if(orasPornire==''||orasPornire=='null'||orasPornire==undefined)
		orasPornire='BUCURESTI';
	
	orasPornire=orasPornire.toUpperCase();
	
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
  	map.setZoom(16);
  	
  	directionsDisplay.setMap(map);
    calcRoute()
    //showLinks(document.getElementById('orase'))
  }
  
  function calcRoute() {
	  
	  
	
		
    var start = </f:verbatim><h:outputText value="\'#{mormanManager.latitudine},#{mormanManager.longitudine}\'" /><f:verbatim>
	
    var end = orase[orasPornire];
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
    //showlinks(element);
	return;

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
