var countyGridsOverlay_old = null;
var countyGarbageOverlay_old = null;
var countyBorderOverlay_old = null;
var chartedGridsOverlay_old = null;

function loadCountyGridsOverlay(value, team){
    if(value){
        var jsonObject = JSON.parse(value);
        var url = WS_URL;
        //url += '/LDIRBackend/map/ws/countySearch/chartedAreas/';
        //url += '?county='+jsonObject.name;
        //url += '&cb=' + escape('<a target="_self" style="color: #4D751F;" href="cartare-zone-vizualizare.jsf?areaId={{{ID}}}">&raquo; vizualizeaza</a>');

        url += '/LDIRBackend/map/ws/countySearch/team/'+team+'/chartedAreas?county='+jsonObject.name;
        url += '&cb=' + escape('<a target="_self" style="color: #4D751F;" href="cartare-zone-vizualizare.jsf?areaId={{{ID}}}">&raquo; vizualizeaza</a>');


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
        url += '&cb=' + escape('<a target="_self" style="color: #4D751F;" href="curatenie-morman-detalii.jsf?garbageId={{{ID}}}">&raquo; vizualizeaza(asigneaza morman)</a>');

        
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


function loadCountyGarbageOverlay(value){
    if(value){
        var jsonObject = JSON.parse(value);

        var url = WS_URL;
        url += '/LDIRBackend/map/ws/countySearch/garbages/';
        url += '?county='+jsonObject.name.replace(' ','%20');
        
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





