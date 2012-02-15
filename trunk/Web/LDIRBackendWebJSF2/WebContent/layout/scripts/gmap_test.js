function alerta(){
    alert('eveniment');
}

function clickHandler(overlay, latlng){
    if (latlng) {
        var myHtml = "The GPoint value is: " + myMap.fromLatLngToDivPixel(latlng) + " at zoom level " + myMap.getZoom();

        if(overlay){
            myHtml += "<br/> Title: "+overlay.getTitle();
        }

        myMap.openInfoWindow(latlng, myHtml);
    }
}

function rightClickHandler(point, element, overlay){
    var myHtml = "Right click: ";

    if(overlay){
        myHtml += "<br/> Title: "+overlay.getTitle();
    }

    alert(point.x);
    myMap.openInfoWindow(point, myHtml);
}


var geoXml_old = null;
var nv_lat_old = 0;
var nv_lng_old = 0;
var se_lat_old = 0;
var se_lng_old = 0;
function loadCountyGridsOverlay(value){
    //alert(value);
    if(value){

        var url = WS_URL;
        url += '/LDIRBackend/map/ws/countySearch/chartedAreas/';
        url += '?county='+value;
        url += '&cb=xxx';
        //alert(url);

        var countyGridsOverlay = new GGeoXml(url);
        myMap.addOverlay(countyGridsOverlay);
        if(countyGridsOverlay_old != null){
            myMap.removeOverlay(countyGridsOverlay_old);
        }
        countyGridsOverlay_old = countyGridsOverlay;
    }
}

function showCoordinates(){
    var zoom = myMap.getZoom();
    if(zoom < 10){
        return;
    }

    var bounds = myMap.getBounds();
    var sv = bounds.getSouthWest();
    var ne = bounds.getNorthEast();

    var sv_lat = sv.lat();
    var sv_lng = sv.lng();
    var ne_lat = ne.lat();
    var ne_lng = ne.lng();

    var nv_lat = ne_lat;
    var nv_lng = sv_lng;
    var se_lat = sv_lat;
    var se_lng = ne_lng;

    if(nv_lat > nv_lat_old || nv_lng < nv_lng_old || se_lat < se_lat_old || se_lng > se_lng_old ){
        if(zoom > 10){
            var dif_lat = nv_lat - se_lat;
            var dif_lng = se_lng - nv_lng;

            //var new_dif_lat = dif_lat*(Math.pow(2, zoom-10));
            var new_dif_lat = dif_lat*2;
            nv_lat += new_dif_lat/2;
            se_lat -= new_dif_lat/2;
            //var new_dif_lng = dif_lng*(Math.pow(2, zoom-10));
            var new_dif_lng = dif_lng*2;
            se_lng += new_dif_lng/2;
            nv_lng -= new_dif_lng/2;
        }

        var url = WS_URL;
        url += '/LDIRBackend/map/ws/chartedAreas/?'
        url += 'topLeftX='+nv_lng;
        url += '&topLeftY='+nv_lat;

        url += '&bottomRightX='+se_lng;
        url += '&bottomRightY='+se_lat;
        //alert(url);

        var geoXml = new GGeoXml(url);
        myMap.addOverlay(geoXml);
        if(geoXml_old != null){
            myMap.removeOverlay(geoXml_old);
        }
        geoXml_old = geoXml;
        
        nv_lat_old = nv_lat;
        nv_lng_old = nv_lng;
        se_lat_old = se_lat;
        se_lng_old = se_lng;
    }
}


