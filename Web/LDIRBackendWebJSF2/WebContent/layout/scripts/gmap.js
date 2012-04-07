var countyGridsOverlay_old = null;
var countyGarbageOverlay_old = null;
var countyBorderOverlay_old = null;
var chartedGridsOverlay_old = null;

var bboxGarbageOverlay_old = null;

var startDrag=false;
var addEvent=false;
var xhr = new XMLHttpRequest();;
var votx = new XMLHttpRequest();;
var nomx = new XMLHttpRequest();;
var xmlfrombackend="";
var oldxml="";
//var exml = new EGeoXml("exml", myMap, ""); //for testing googlemapp on localhost
var exml = new EGeoXml("exml", myMap, null,{nozoom:true});

GEvent.addListener(exml, "parsed", function(){
	showhidemarkers("");
	});
var tid=-1;
//loadEvents()



function showhidemarkers(element)
{

	//element=document.getElementById("layers");
	 element0=document.getElementById("layers:0");
	 element1=document.getElementById("layers:1");
	 element2=document.getElementById("layers:2");
	 
	 for(var i=0;i<exml.gmarkers.length;i++)
		 {
		 	marker=exml.gmarkers[i];
		 	
		 	if(element0.checked==true)
		 		marker.show()
		 		
		 	if(element1.checked==true)
			 	if(marker.id!=undefined)
			 	{
				 		 	
				 	
			 		if(String(marker.id).indexOf("Zona:")>-1)
			 			marker.hide()
				 	if(String(marker.id).indexOf("Morman:")>-1)
				 		marker.show()
				 	
			 	}
			if(element2.checked==true)
			 	if(marker.id!=undefined)
			 	{
			 		if(marker.id.indexOf("Zona:")>-1)
			 			marker.show()
				 	if(marker.id.indexOf("Morman:")>-1)
			 	 		marker.hide()
			 	}
		 }		
}

function voteMorman(id){
	url=WS_URL+"/admin/admin-vote-from-map.jsf?garbageId="+id;
	tid=id;
	votx = new XMLHttpRequest();
	votx.onreadystatechange = processvot;
	//alert(url);
//	if(userRole!='anon')
//		votx.open("put", url, true, username, password);
//	else
//		votx.open("put", url, true);
	votx.open("GET",url,true)
	votx.setRequestHeader('Content-Type', 'text/html'); 
	votx.send();
	
}

function processvot(){
	if (votx.readyState == 4) {
	response=votx.responseText;
	element="infovot"+tid;
	//alert(element)
	if(response.indexOf("morman votat succes")>-1)
		{
			document.getElementById(element).style.display = 'block';
			document.getElementById(element).innerHTML ='Mormanul a fost votat cu succes';
		}
	if(response.indexOf("morman votat fail")>-1)
		if(response.indexOf("Ai depasit numarul de voturi")>-1)
			{
			document.getElementById(element).style.display = 'block';
			document.getElementById(element).innerHTML ='Ai votat deja mormanul in ultimele 24 de ore.';
			}
		else
			{
			document.getElementById(element).style.display = 'block';
			document.getElementById(element).innerHTML ='Eroare nedefinita.';
			}

	//alert(response)
//    public static final String MORMAN_VOTAT_SUCCES="morman votat succes";
//    public static final String MORMAN_VOTAT_FAIL="morman votat fail";
//
//    public static final String MORMAN_NOMINALIZAT_SUCCES="morman nominalizat succes";
//    public static final String MORMAN_NOMINALIZAT_FAIL="morman nominalizat fail";
//	9 127.0.0.1 [[[[morman votat]]]]
//  9 127.0.0.1 [[[[morman fail|Nu se mai poate vota. Ai depasit numarul de voturi/zona permis pe 24 ore cu acest user!|9 127.0.0.1]]]]
//		
	
	
	}
}



function nominalizeazaMorman(id){
	url=WS_URL+"/admin/admin-nominate-from-map.jsf?garbageId="+id;
	tid=id;
	votx = new XMLHttpRequest();
	votx.onreadystatechange = processvot;
	alert(url);
//	if(userRole!='anon')
//		votx.open("put", url, true, username, password);
//	else
//		votx.open("put", url, true);
	votx.open("GET",url,true)
	votx.setRequestHeader('Content-Type', 'text/html'); 
	votx.send();
}

function getXMLonlocal(url){
//for testing googlemaps on localhost	
xhr = new XMLHttpRequest();
xhr.onreadystatechange = process;
xhr.open("GET", url, true);
xhr.send();

}
function process(){
//for testing googlemaps on localhost
if (xhr.readyState == 4) {
	oldxml=xmlfrombackend;
    xmlfrombackend = xhr.responseText;

    if(oldxml!=xmlfrombackend){
    
    
    //alert(xmlfrombackend)
    if(exml==undefined)
    	exml = new EGeoXml("exml", myMap, null,{nozoom:true});
    else
    	for(var i=0;i<exml.gmarkers.length;i++) 
    		myMap.removeOverlay(exml.gmarkers[i]);
    exml.parseString(xmlfrombackend);
    showhidemarkers("");
    //alert(exml.gmarkers.length)
  }
}
}

 


function showlinks(element) {

	var departure = element.value;
	latoras=Number(departure.split(',')[0])
	lngoras=Number(departure.split(',')[1])
	
    //var bounds = new GLatLngBounds(new GLatLng(jsonObject.bottomRightY , jsonObject.topLeftX),
            //new GLatLng(jsonObject.topLeftY, jsonObject.bottomRightX));
        //myMap.setCenter(bounds.getCenter(), myMap.getBoundsZoomLevel(bounds));
        //myMap.setZoom(myMap.getBoundsZoomLevel(bounds));
        //myMap.panTo(bounds.getCenter());
	//46.3103035360546,23.7182172002125
	
	var oras = new google.maps.LatLng(latoras, lngoras);
	//alert(departure	)
	//alert(myMap)
	
	zoomLevel = myMap.getZoom();
    if(zoomLevel<10)
	 myMap.setZoom(10)	
	myMap.panTo(new google.maps.LatLng(latoras, lngoras));
	//alert(new google.maps.LatLng(latoras, lngoras))
	myMap.setCenter(new google.maps.LatLng(latoras, lngoras));
    bounds=myMap.getBounds();
    ne=bounds.getNorthEast();
    sw=bounds.getSouthWest();
    
    loadBBoxGarbageOverlay(ne,sw)   
    
    
}

function onBoundsChanged(){
	
	zoomLevel = myMap.getZoom();
    if(zoomLevel<10)
    	myMap.setZoom(10)
    bounds=myMap.getBounds();
    ne=bounds.getNorthEast();
    sw=bounds.getSouthWest();
    
    
     loadBBoxGarbageOverlay(ne,sw)
    // showhidemarkers();
    //<m:eventListener eventName="tilesloaded" jsFunction="zoomToArea('#{areaCleanManager.areaJsonBouns}')"/>
	//    startDrag=false;

}

function loadEvents()
{
//google.maps.event.addListener(myMap, 'bounds_changed', test);
GEvent.addListener(myMap, "dragend", onBoundsChanged);
GEvent.addListener(myMap, "zoomend", onBoundsChanged);
}

function loadBBoxGarbageOverlay(ne,sw){
    
 	
	//ne=points[0];
	//sw=points[1];
	//alert(userRole)
    neLat=ne.lat()
    neLng=ne.lng()
    swLat=sw.lat()
    swLng=sw.lng()
    
    topLeftX=swLng;
    topLeftY=neLat;
    bottomRightX=neLng;
    bottomRightY=swLat;

    
    var url = WS_URL;
    
    url += '/LDIRBackend/map/ws/garbages/';
    
    url += '?topLeftX='+Number(topLeftX).toString()
  
    url += '&topLeftY='+topLeftY
    url += '&bottomRightX='+bottomRightX
    url += '&bottomRightY='+bottomRightY;
    //var voteLinks=''
    //if(userRole=='ORGANIZER' || userRole=='ORGANIZER_MULTI' || userRole=='ADMIN')
    url += '&cb=' + escape('<a style="color: #4D751F;" href="/users/curatenie-morman-detalii.jsf?garbageId={{{ID}}}">&raquo; Detalii morman</a>xxxXXXxxx'+userRole);
    //alert(url)

    /* adaugare layer lista gunoaie din judetul selectat */
//    var bboxGarbageOverlay = new GGeoXml(url);
//    myMap.addOverlay(bboxGarbageOverlay);
//    if(bboxGarbageOverlay_old != null){
//        myMap.removeOverlay(bboxGarbageOverlay_old);
//    }
//    bboxGarbageOverlay_old = bboxGarbageOverlay;
    
    //FOR LOCALHOST TESTING
    getXMLonlocal(url);
}



function loadCountyGridsOverlay(value, team){
    if(value){
        var jsonObject = JSON.parse(value);
        var url = WS_URL;
        //url += '/LDIRBackend/map/ws/countySearch/chartedAreas/';
        //url += '?county='+jsonObject.name;
        //url += '&cb=' + escape('<a target="_self" style="color: #4D751F;" href="cartare-zone-vizualizare.jsf?areaId={{{ID}}}">&raquo; vizualizeaza</a>');

        url += '/LDIRBackend/map/ws/countySearch/team/'+team+'/chartedAreas?county='+jsonObject.name;
        url += '&cb=' + escape('<a target="_self" style="color: #4D751F;" href="cartare-zone-vizualizare.jsf?areaId={{{ID}}}">&raquo; Vizualizeaza</a>');


        /* adaugare griduri judet selectat si stergere cele vechi */
        var countyGridsOverlay = new GGeoXml(url);
        GEvent.addListener(countyGridsOverlay, 'load', function(){
            document.getElementById('popup-grid-loading').component.hide();
        });
        myMap.addOverlay(countyGridsOverlay);
        if(countyGridsOverlay_old != null){
            myMap.removeOverlay(countyGridsOverlay_old);
        }
        countyGridsOverlay_old = countyGridsOverlay;

        /* centrare harta pe judetul selactat */
        var bounds = new GLatLngBounds(new GLatLng(jsonObject.bottomRightY , jsonObject.topLeftX),
            new GLatLng(jsonObject.topLeftY, jsonObject.bottomRightX));
        //myMap.setCenter(bounds.getCenter(), myMap.getBoundsZoomLevel(bounds));
        myMap.setZoom(myMap.getBoundsZoomLevel(bounds));
        myMap.panTo(bounds.getCenter());
    }
}

function refreshOverlay(){
    //alert('action');
    if(chartedGridsOverlay_old){
        myMap.removeOverlay(chartedGridsOverlay_old);
    }

    if(countyGridsOverlay_old){
        myMap.removeOverlay(countyGridsOverlay_old);
    }

    if(chartedGridsOverlay_old){
        myMap.addOverlay(chartedGridsOverlay_old);
    }
    if(countyGridsOverlay_old){
        myMap.addOverlay(countyGridsOverlay_old);
    }
}

function loadChartedAreasOverlay(team, county){
    var url = WS_URL;
    
       
    /* adaugare griduri judet selectat si stergere cele vechi */
    var chartedGridsOverlay = new GGeoXml(url);
    myMap.addOverlay(chartedGridsOverlay);
    if(chartedGridsOverlay_old != null){
        myMap.removeOverlay(chartedGridsOverlay_old);
    }
    chartedGridsOverlay_old = chartedGridsOverlay;

/*GEvent.addListener(myMap, "zoomend", function(){
        refreshOverlay();
    });
    GEvent.addListener(myMap, "moveend", function(){
        refreshOverlay();
    });*/
}

function zoomToArea(jsonBounds){
    if(jsonBounds){
        var jsonObject = JSON.parse(jsonBounds);

        var bounds = new GLatLngBounds(new GLatLng(jsonObject.bottomRightY , jsonObject.topLeftX),
            new GLatLng(jsonObject.topLeftY, jsonObject.bottomRightX));
        myMap.setZoom(myMap.getBoundsZoomLevel(bounds));
        myMap.panTo(bounds.getCenter());
    }
}
function loadCountyGarbageDetailOverlay(value){
    if(value){
        var jsonObject = JSON.parse(value);

        var url = WS_URL;
        url += '/LDIRBackend/map/ws/countySearch/garbages/';
        url += '?county='+jsonObject.name.replace(' ','%20');
        url += '&cb=' + escape('<a target="_self" style="color: #4D751F;" href="curatenie-morman-detalii.jsf?garbageId={{{ID}}}">&raquo; Detalii / Aloca mormanul pentru echipa ta</a>');

        
        /* adaugare layer lista gunoaie din judetul selectat */
//        var countyGarbageOverlay = new GGeoXml(url);
//        myMap.addOverlay(countyGarbageOverlay);
//        if(countyGarbageOverlay_old != null){
//            myMap.removeOverlay(countyGarbageOverlay_old);
//        }
//        countyGarbageOverlay_old = countyGarbageOverlay;
        getXMLonlocal(url);
        
        /* adaugare layer contur judet */
        var points = new Array();
        for (var i=0; i<(jsonObject.border.length/2); i++){
            points[i]=new GLatLng(jsonObject.border[i*2], jsonObject.border[(i*2)+1]);
        }
        var countyBorderOverlay = new GPolyline(points);
        myMap.addOverlay(countyBorderOverlay);
        if(countyBorderOverlay_old != null){
            myMap.removeOverlay(countyBorderOverlay_old);
        }
        countyBorderOverlay_old = countyBorderOverlay;

        /* zoom pe judetul seletat */
        var bounds = new GLatLngBounds(new GLatLng(jsonObject.bottomRightY , jsonObject.topLeftX),
            new GLatLng(jsonObject.topLeftY, jsonObject.bottomRightX));
        myMap.setZoom(myMap.getBoundsZoomLevel(bounds));
        myMap.panTo(bounds.getCenter());
    }
}


function loadCountyGarbageOverlay(value){
	if(value!=null){
        var jsonObject = JSON.parse(value);
        
        var url = WS_URL;
        url += '/LDIRBackend/map/ws/countySearch/garbages/';
        url += '?county='+jsonObject.name.replace(' ','%20');
       // alert(url)
        /* adaugare layer lista gunoaie din judetul selectat */
        var countyGarbageOverlay = new GGeoXml(url);
        myMap.addOverlay(countyGarbageOverlay);
        if(countyGarbageOverlay_old != null){
            myMap.removeOverlay(countyGarbageOverlay_old);
        }
        countyGarbageOverlay_old = countyGarbageOverlay;

        /* adaugare layer contur judet */
        var points = new Array();
        for (var i=0; i<(jsonObject.border.length/2); i++){
            points[i]=new GLatLng(jsonObject.border[i*2], jsonObject.border[(i*2)+1]);
        }
        var countyBorderOverlay = new GPolyline(points);
        myMap.addOverlay(countyBorderOverlay);
        if(countyBorderOverlay_old != null){
            myMap.removeOverlay(countyBorderOverlay_old);
        }
        countyBorderOverlay_old = countyBorderOverlay;

        /* zoom pe judetul seletat */
        var bounds = new GLatLngBounds(new GLatLng(jsonObject.bottomRightY , jsonObject.topLeftX),
            new GLatLng(jsonObject.topLeftY, jsonObject.bottomRightX));
        myMap.setZoom(myMap.getBoundsZoomLevel(bounds));
        myMap.panTo(bounds.getCenter());
    }
}




function loadChartedAreasOverlayOld(value){
    //alert(value);
    if(value){
        var areas = JSON.parse(value);

        /* adaugare layer griduri selectate */
        for(var i=0; i<areas.length; i++){
            var jsonObject = areas[i];

            var points = new Array();
            for (var j=0; j<(jsonObject.border.length/2); j++){
                points[j]=new GLatLng(jsonObject.border[j*2], jsonObject.border[(j*2)+1]);
            }
            var bounds = new GLatLngBounds(new GLatLng(jsonObject.bottomRightY , jsonObject.topLeftX),
                new GLatLng(jsonObject.topLeftY, jsonObject.bottomRightX));

            var chartedAreaOverlay = new GPolygon(points, 'red', 2, 1, 'red', 0.25);

            /*GEvent.addListener(chartedAreaOverlay, "click", function(latlng){
                myMap.openInfoWindow(latlng, 'muie '+latlng.lat());
            });*/
            myMap.addOverlay(chartedAreaOverlay);
        }
    }
}

function loadChartedAreasOverlayOld2(team){
    var url = WS_URL;
    url += '/LDIRBackend/map/ws/team/'+team+'/chartedAreas';
    url += '?cb=' + escape('Zona Cartata<br/> <a target="_self" style="color: #4D751F;" href="cartare-zone-vizualizare.jsf?areaId={{{ID}}}">&raquo; vizualizeaza</a>');

    /* adaugare griduri judet selectat si stergere cele vechi */
    var chartedGridsOverlay = new GGeoXml(url);
    myMap.addOverlay(chartedGridsOverlay);
    if(chartedGridsOverlay_old != null){
        myMap.removeOverlay(chartedGridsOverlay_old);
    }
    chartedGridsOverlay_old = chartedGridsOverlay;

/*GEvent.addListener(myMap, "zoomend", function(){
        refreshOverlay();
    });
    GEvent.addListener(myMap, "moveend", function(){
        refreshOverlay();
    });*/
}


