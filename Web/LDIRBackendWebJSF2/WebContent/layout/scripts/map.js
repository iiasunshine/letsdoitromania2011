var map;
var mgr;
var icons = {};
var markers = [];
var mormane=[] //all mormane, up to let's say 500
var newmormane=[] //the current ajax batch
var xhr = new XMLHttpRequest();
var votx = new XMLHttpRequest();;

var soloMormanId=-1;

var markerClusterer = null;
var imageUrl = 'http://chart.apis.google.com/chart?cht=mm&chs=24x32&' +
    'chco=FFFFFF,008CFF,000000&ext=.png';

var infoWindow = new google.maps.InfoWindow;

var dontAjax=false //to stop refreshing from clicking the markers


var styles = [[{
    url: '../images/people35.png',
    height: 35,
    width: 35,
    anchor: [16, 0],
    textColor: '#ff00ff',
    textSize: 10
  }, {
    url: '../images/people45.png',
    height: 45,
    width: 45,
    anchor: [24, 0],
    textColor: '#ff0000',
    textSize: 11
  }, {
    url: '../images/people55.png',
    height: 55,
    width: 55,
    anchor: [32, 0],
    textColor: '#ffffff',
    textSize: 12
  }], [{
    url: '../images/conv30.png',
    height: 27,
    width: 30,
    anchor: [3, 0],
    textColor: '#ff00ff',
    textSize: 10
  }, {
    url: '../images/conv40.png',
    height: 36,
    width: 40,
    anchor: [6, 0],
    textColor: '#ff0000',
    textSize: 11
  }, {
    url: '../images/conv50.png',
    width: 50,
    height: 45,
    anchor: [8, 0],
    textSize: 12
  }], [{
    url: '../images/heart30.png',
    height: 26,
    width: 30,
    anchor: [4, 0],
    textColor: '#ff00ff',
    textSize: 10
  }, {
    url: '../images/heart40.png',
    height: 35,
    width: 40,
    anchor: [8, 0],
    textColor: '#ff0000',
    textSize: 11
  }, {
    url: '../images/heart50.png',
    width: 50,
    height: 44,
    anchor: [12, 0],
    textSize: 12
  }]];

var layersOptions = {
		mormaneToate:true,
		mormane2010:false,
		mormane2011:false,
		mormane2012:false,
		mormaneCuratate:false,
		mormaneNecuratate:false,
		mormaneDeVotat:false,
		judet:""
}

function initLayersOptions(){
	
	layersOptions.mormaneToate=getElementByValue("mormaneToate").checked;
	layersOptions.mormane2010=getElementByValue("mormane2010").checked;
	layersOptions.mormane2011=getElementByValue("mormane2011").checked;
	layersOptions.mormane2012=getElementByValue("mormane2012").checked;
	layersOptions.mormaneCuratate=getElementByValue("mormaneCuratate").checked;
	layersOptions.mormaneNecuratate=getElementByValue("mormaneNecuratate").checked;
	layersOptions.mormaneDeVotat=getElementByValue("mormaneDeVotat").checked;
	
}

function getElementByValue(value){
	var btns = document.getElementsByTagName('input');
	for(i=0;i<btns.length;i++)
	{
	    if(btns[i].value==value)
	       return btns[i]
	}
}

function layersoptions(element){
	layersOptions[element.value]=document.getElementById(element.id).checked;
	if(element.value!="mormaneToate"&&layersOptions[element.value]==true)
		{
			layersOptions.mormaneToate=false;
			getElementByValue("mormaneToate").checked=false;
		}

	for(var p in layersOptions)
		{
			if(layersOptions.mormaneToate==true&&p!="mormaneToate"&&p!="judet")
				getElementByValue(p).checked=false;
		}
	
	onboundschange();
	//alert("layersOptions["+element.value+"]="+layersOptions[element.value]);
	//renderData(false);
}

function showMarker(morman){
	date=new Date(morman.recordDate);
	year=date.getFullYear();
	month=date.getMonth();
	status=morman.status; //CLEANED,IDENTIFIED
	
	toClean=(morman.toClean==="true");
	yearB=false;
	
	toVote=(morman.toVote==="true")&&layersOptions.mormaneDeVotat;
	cleaned=(status=="CLEANED")&&layersOptions.mormaneCuratate;
	notcleaned=(status=="IDENTIFIED")&&layersOptions.mormaneNecuratate;
	
	if(layersOptions.mormaneToate==true)
		return true;
	if(layersOptions.mormane2010==true)
		{
			if(year==2009&&month>8)yearB=true;
			if(year==2010&&month<9)yearB=true;
		}
	if(layersOptions.mormane2011==true)
	{
		if(year==2010&&month>8)yearB=true;
		if(year==2011&&month<9)yearB=true;
	}
	if(layersOptions.mormane2012==true)
	{
		if(year==2011&&month>8)yearB=true;
		if(year==2012&&month<9)yearB=true;
	}
	
	if(yearB&&cleaned&&notcleaned&&toVote==true)return true;
	if(yearB&&cleaned&&notcleaned==true)return true;
	if(yearB&&cleaned&&toVote==true)return true;
	if(yearB&&cleaned==true)return true;
	if(yearB&&notcleaned&&toVote==true)return true;
	if(yearB&&notcleaned==true)return true;
	
	if(yearB&&toVote==true)return true;
	
	if(notcleaned&&toVote==true)return true;
	
	if(notcleaned&&cleaned==true)return true; //almost as same as Toate 
	
	if(yearB&&!layersOptions.mormaneDeVotat
			&&!layersOptions.mormaneCuratate
			&&!layersOptions.mormaneNecuratate)return true;
	if(cleaned&&!layersOptions.mormaneDeVotat
			&&!yearB
			&&!layersOptions.mormaneNecuratate)return true;
	if(notcleaned&&!layersOptions.mormaneDeVotat
			&&!layersOptions.mormaneCuratate
			&&!yearB)return true;
	if(toVote&&!yearB
			&&!layersOptions.mormaneCuratate
			&&!layersOptions.mormaneNecuratate)return true;
	
	
	return false;
}


function getMormane(url){
//alert(url)
if(dontAjax==true)return;

xhr = new XMLHttpRequest();
xhr.onreadystatechange = processGetMormane;
//if(soloMormanId!=-1)
//	xhr.open("GET", url, true,userEmail,userPasswd);
//else 
//	xhr.open("GET", url, true);
xhr.open("POST",url,true)
xhr.setRequestHeader('Accept', 'application/json');
xhr.send();
if(document.getElementById("ajaxloader")!=null)
	document.getElementById("ajaxloader").style.display="block";
}

function processGetMormane(){
//	alert(xhr.readyState)
//	alert(xhr.responseText)
if (xhr.readyState == 4) {
		//oldxml=xmlfrombackend;
	    //xmlfrombackend = xhr.responseText;
		
		if(xhr.responseText!=null)
		{response=JSON.parse(xhr.responseText);
		if(response!=null)
			newmormane=response["garbage"];
		if(response!=null&&newmormane==undefined)
			newmormane=new Array(response);
		if(document.getElementById("ajaxloader")!=null)
			document.getElementById("ajaxloader").style.display="none";
		renderData();
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
	element="infovot";
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
	
}

var onMarkerClick = function() {
	dontAjax=true;
    var marker = this;
    var morman = marker.morman;
    var latLng = marker.getPosition();
    var content="";
    if(morman.toVote=="false")
    	content+="<p>Morman: "+morman.garbageId+"</p>"
    else content+="<p>Zona: "+morman.garbageId+"</p>";
    content+="<p>Descriere: "+morman.description+"</p>"
    content+="<p>Saci: "+morman.bagCount+"</p>"
    content+="<p></p>"
    content+="<p><a target=\"_self\" style=\"color: #4D751F;\" href=\""+WS_URL+"/users/curatenie-morman-detalii.jsf?garbageId="+morman.garbageId+"\">&raquo; Detalii morman</a></p>";
    
    if(morman.toVote=="true")
		content+="<span style=\"color: #4D751F;cursor: pointer\" onMouseOver=\"this.style.textDecoration='underline'\" onMouseOut=\"this.style.textDecoration='none'\" onclick=\"javascript:voteMorman("+morman.garbageId+")\">Voteaza</span>";
//	if(userRole=="ORGANIZER" || userRole=="ORGANIZER_MULTI" || userRole == "ADMIN")
//		content+=" | <span style=\"color: #4D751F;cursor: pointer\" onMouseOver=\"this.style.textDecoration='underline'\" onMouseOut=\"this.style.textDecoration='none'\" onclick=\"javascript:nominalizeazaMorman("+morman.garbageId+")\">"+nominalizeazaString+"</span>";
	content+="</p>\n";
	content+="<p id=\"infovot\" style=\"display:none\">YOU SHOULDN'T SEE THIS</p>\n";

	infoWindow.setContent(content);
    infoWindow.open(map, marker);
    
  };


function renderData(){
	
	
	console.log('cache length: '+mormane.length,"new data: "+newmormane.length)
	if(mormane.length+newmormane.length>1000)
	{
		for(var i=0;i<markers.length;i++)
		if(markers[i]!=undefined)
			markers[i].setMap(null);
		
		markers=[];
		clearClusters();
		for(var i=0;i<mormane.length;i++)
			{
				mormane[i].marker=null;
				mormane[i]=null
			}
		mormane=[];
	};
	
	for(var i=0;i<newmormane.length;i++)
		{
			var morman=newmormane[i];	
			alreadyonmap=false;
			
			for(var j=0;j<mormane.length;j++)
				if(mormane[j].garbageId==morman.garbageId)
					{
						alreadyonmap=true;
						//alert(mormane[j].garbageId+" "+morman.garbageId)
						break;
					}
			if(alreadyonmap==true)
				continue;
			
			//alert("x "+morman.garbageId)
			latitude=morman.y;
			longitude=morman.x;
			
		    var markerImage = new google.maps.MarkerImage(imageUrl,
		             new google.maps.Size(24, 32));
		    var infowindow = new google.maps.InfoWindow();

			var latLng = new google.maps.LatLng(latitude,longitude)
			
		    if(morman.toVote=="true"){
		        var marker1 = new MarkerWithLabel({
		            position: latLng,
		            draggable: false,
		            map: map,
		            labelContent: 'Voteaza!',
		            labelAnchor: new google.maps.Point(22, 0),
		            labelClass: "labels", // the CSS class for the label
		            labelStyle: {opacity: 0.75}
		          });
		    
		    //marker1.morman=undefined;
		    marker1.morman=morman;
		    morman.marker=marker1;
		    google.maps.event.addListener(marker1, 'click', onMarkerClick);
		    marker1.setVisible(false);
		    markers.push(marker1);
		    }
			if(morman.toVote=="false"){
			var marker = new google.maps.Marker({
		           position: latLng,
		           draggable: false,
		           map: map,
		           title:"Hello World!"
		          });			 
		    marker.morman=morman;
		    morman.marker=marker;
		    google.maps.event.addListener(marker, 'click', onMarkerClick);
		    marker.setVisible(false);
		    markers.push(marker);
			};
		    //marker.morman=undefined;
			
		mormane.push(newmormane[i])
		};
		
//    var zoom = parseInt(document.getElementById('zoom').value, 10);
//    var size = parseInt(document.getElementById('size').value, 10);
//    var style = parseInt(document.getElementById('style').value, 10);
//    zoom = zoom == -1 ? null : zoom;
//    size = size == -1 ? null : size;
//    style = style == -1 ? null: style;
	var zoom = -1;
    var size = -1;
    var style =-1;
    zoom = 15; 
    size = size == -1 ? null : size;
    style = style == -1 ? null: style;
    
   
   // if(layersOptions.mormaneToate==true)
    clearClusters();
    showhidemarkers();
    //MarkerClusterer.IMAGE_PATH = "/layout/images/m";
    markerClusterer = new MarkerClusterer(map, markers, {
      maxZoom: zoom,
      gridSize: size,
      styles: styles[style],
      ignoreHidden:true
    });
    
    newmormane=[];
}

function returnVisibleMarkers(){
	var whatMarkersAreVisible=[]
	for(var i=0;i<markers.length;i++)
		{
		    var marker=markers[i];
			if(showMarker(marker.morman)==true)
					whatMarkersAreVisible.push(marker)
		}
	return whatMarkersAreVisible;
	
}

function showhidemarkers()
{
	for(var i=0;i<markers.length;i++)
		{
		    var marker=markers[i];
			if(showMarker(marker.morman)==true)
		    	{
					if(marker.getVisible()!=true)
						marker.setVisible(true)
		    	}
		    else 
		    	if(marker.getVisible()!=false)
		    		marker.setVisible(false);
			
			//marker.setVisible(true)
		}
}

function clearClusters()
{
	if(markerClusterer!=null||markerClusterer!=undefined)
		markerClusterer.clearMarkers();

}

function showlinks(element) {

	var departure = element.value;
	latoras=Number(departure.split(',')[0])
	lngoras=Number(departure.split(',')[1])
	
	var oras = new google.maps.LatLng(latoras, lngoras);
	map.panTo(new google.maps.LatLng(latoras, lngoras));
	map.setCenter(new google.maps.LatLng(latoras, lngoras));
	onboundschange();
}


function centerOnCounty(value){
	if(value!=null){
        var jsonObject = JSON.parse(value);
	    var latLngBottom = new google.maps.LatLng(jsonObject.bottomRightY,jsonObject.bottomRightX)
	    var latLngTop = new google.maps.LatLng(jsonObject.topLeftY,jsonObject.topLeftX)
	
	    var bounds = new google.maps.LatLngBounds();
	    bounds.extend(latLngBottom);
	    bounds.extend(latLngTop);
	    map.fitBounds(bounds);
	    map.panTo(bounds.getCenter());
	};
}

function onboundschange(){
	  bounds=map.getBounds();
	  ne=bounds.getNorthEast();
	  sw=bounds.getSouthWest();
	  neLat=ne.lat()
		neLng=ne.lng()
		swLat=sw.lat()
		swLng=sw.lng()
		
		topLeftX=swLng;
		topLeftY=neLat;
		bottomRightX=neLng;
		bottomRightY=swLat;
		
		
		var url = WS_URL;
		
		//url = 'http://app.letsdoitromania.ro/LDIRBackend/map/ws/garbageList/';
		if(soloMormanId!=-1)
			url += '/LDIRBackend/ws/garbage/'+soloMormanId;
		else 
			url += '/LDIRBackend/map/ws/garbageList2/';
		url += '?topLeftX='+Number(topLeftX).toString()
		  
		url += '&topLeftY='+topLeftY
		url += '&bottomRightX='+bottomRightX
		url += '&bottomRightY='+bottomRightY;
	
	//alert(soloMormanId);
		
	getMormane(url);
	
	}

function load() {   
		
		var myOptions = {
		                zoom: 10,
		                center: new google.maps.LatLng(44.4317879, 26.1015844),
		                mapTypeId: google.maps.MapTypeId.ROADMAP
		              }
		map = new google.maps.Map(document.getElementById('map'), myOptions);
		//m = new MarkerManager(map);
//		google.maps.event.addListener(map, 'bounds_changed', function() {
//			onboundschange();
//		    });
		google.maps.event.addListener(map, 'idle', function() {
			onboundschange();
		    });
		google.maps.event.addListener(map, 'zoom_changed', function() {
//			  if(map.getZoom()<10)
//				  map.setZoom(10);
		  })
		google.maps.event.addListener(map, 'click', function() {
			infoWindow.close();
			dontAjax=false;
			onboundschange();
		});;
		
		initLayersOptions();	
	};	
	
function somefunction(mormanId){
	//alert(mormanId)
	soloMormanId=mormanId;
}

google.maps.event.addDomListener(window, 'load', load);