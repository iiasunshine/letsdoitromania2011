var map;
var mgr;
var icons = {};
var markers = [];
var mormane=[] //all mormane, up to let's say 500
var newmormane=[] //the current ajax batch
var xhr = new XMLHttpRequest();
var votx = new XMLHttpRequest();;

var judetName=Array();
judetName[4]="ALBA";
judetName[5]="ARAD";
judetName[6]="ARGES";
judetName[7]="BACAU";
judetName[8]="BIHOR";
judetName[9]="BISTRITA-NASAUD";
judetName[10]="BOTOSANI";
judetName[11]="BRAILA";
judetName[12]="BRASOV";
judetName[13]="BUZAU";
judetName[14]="CALARASI";
judetName[15]="CARAS-SEVERIN";
judetName[16]="CLUJ";
judetName[17]="CONSTANTA";
judetName[18]="COVASNA";
judetName[19]="DAMBOVITA";
judetName[20]="DOLJ";
judetName[21]="GALATI";
judetName[22]="GIURGIU";
judetName[23]="GORJ";
judetName[24]="HARGHITA";
judetName[25]="HUNEDOARA";
judetName[26]="IALOMITA";
judetName[27]="IASI";
judetName[28]="MARAMURES";
judetName[29]="MEHEDINTI";
judetName[30]="MUNICIPIUL BUCURESTI";
judetName[31]="MURES";
judetName[32]="NEAMT";
judetName[33]="OLT";
judetName[34]="PRAHOVA";
judetName[35]="SALAJ";
judetName[36]="SATU MARE";
judetName[37]="SIBIU";
judetName[38]="SUCEAVA";
judetName[39]="TELEORMAN";
judetName[40]="TIMIS";
judetName[41]="TULCEA";
judetName[42]="VALCEA";
judetName[43]="VASLUI";
judetName[44]="VRANCEA";
judetName[45]="ILFOV";


//var soloMormanId=-1;
if (typeof soloMormanId === 'undefined') {
	var soloMormanId='';
}




var markerClusterer = null;
//var markerRed = 'http://chart.apis.google.com/chart?cht=mm&chs=24x32&' +
//    'chco=FFFFFF,008CFF,000000&ext=.png';
var markerCOMPLETELY = WS_URL+'/layout/images/COMPLETELY.png'; 
var markerPARTIALLY = WS_URL+'/layout/images/PARTIALLY.png';
var markerUNALLOCATED = WS_URL+'/layout/images/UNALLOCATED.png';
var markerCLEANED = WS_URL+'/layout/images/CLEANED.png';
//var markerIDENTIFIED = WS_URL+'/layout/images/IDENTIFIED.png';
var markerTOVOTE = WS_URL+'/layout/images/TOVOTE.png';
var markerGREY= WS_URL+'/layout/images/GREY.png';
var markerGREYCLEANED = WS_URL+'/layout/images/GREYCLEANED.png';

var infoWindow = new google.maps.InfoWindow;

var dontAjax=false //to stop refreshing from clicking the markers

var dontLoad=false //to stop refreshing from zooming to morman

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
		mormaneToate:false,
		mormane2010:false,
		mormane2011:false,
		mormane2012:false,
		mormane2013:false,
		mormaneCuratate:false,
		mormaneNecuratate:false,
		mormaneDeVotat:false,
		mormanedeCuratat:true,
		judet:""
};

function initLayersOptions(){
	
	if(getElementByValue("mormaneToate")!=undefined)
		layersOptions.mormaneToate=getElementByValue("mormaneToate").checked;
	if(getElementByValue("mormane2011")!=undefined)
		layersOptions.mormane2011=getElementByValue("mormane2011").checked;
	if(getElementByValue("mormane2012")!=undefined)
		layersOptions.mormane2012=getElementByValue("mormane2012").checked;
	if(getElementByValue("mormaneCuratate")!=undefined)
		layersOptions.mormaneCuratate=getElementByValue("mormaneCuratate").checked;
	if(getElementByValue("mormaneNecuratate")!=undefined)
		layersOptions.mormaneNecuratate=getElementByValue("mormaneNecuratate").checked;
	if(getElementByValue("mormaneDeVotat")!=undefined)
		layersOptions.mormaneDeVotat=getElementByValue("mormaneDeVotat").checked;
	if(getElementByValue("mormaneDeCuratat")!=undefined)
		layersOptions.mormaneDeCuratat=getElementByValue("mormaneDeCuratat").checked;


	if(getElementByValue("mormaneDeCuratatTotiAnii")!=undefined)
		layersOptions.mormaneToate=getElementByValue("mormaneDeCuratatTotiAnii").checked;
	if(getElementByValue("mormane2013")!=undefined)
		layersOptions.mormane2013=getElementByValue("mormane2013").checked;

	if(getElementByValue("clustering")!=undefined)
		getElementByValue("clustering").checked=true;
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

	if(getElementByValue("clustering")==element){
		if(getElementByValue("clustering").checked!=true)

		{clearClusters();
		for(var i=0;i<markers.length;i++)
		markers[i].setMap(map);
		return;};
      //for(var i=0;i<markers.length;i++)
		//markers[i].setMap(null);
	  markerClusterer = new MarkerClusterer(map, markers, {
      maxZoom: zoom,
      gridSize: size,
      styles: styles[style],
      ignoreHidden:true	
    }
	  );

	return;
	};
	layersOptions[element.value]=document.getElementById(element.id).checked;
	if(element.value=="mormaneToate")
		{getElementByValue("mormane2013").checked=false
		layersOptions["mormaneToate"]=true;};
	if(element.value=="mormane2013")
		{getElementByValue("mormaneToate").checked=false
		layersOptions["mormaneToate"]=false;};

	/*
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
	*/
	onboundschange();
	//alert("layersOptions["+element.value+"]="+layersOptions[element.value]);
	//renderData(false);
}

function showMarker(morman){
	
	if(soloMormanId!=-1&&soloMormanId!='')
		return true;
	
	date=new Date(morman.recordDate);
	cutoffDate=new Date("2012-09-29");

	judet=true;
	if(getElementByValue("judet").checked==true)
		if(layersOptions.judet!=judetName[parseInt(morman.county)])
			judet=false;

	if(layersOptions.mormane2013==true)
		if(date-cutoffDate>0 && judet==true) return true
	if(layersOptions.mormaneToate==true)
		if(judet==true) return true
	
	return false;




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
	
	if(getElementByValue("judet").checked==true)
		if(layersOptions.judet==judetName[parseInt(morman.county)])
			return true;
		else return false;


	if(toClean==true)
		return true;

	
	return false;
}


function getMormane(url){
//alert(url)

if (dontAjax==true)
{
var map=infoWindow.getMap();
dontAjax=(map === null || typeof map === "undefined");
}
if(dontAjax==true)return;

xhr = new XMLHttpRequest();
xhr.onreadystatechange = processGetMormane;
if(soloMormanId!=-1&&soloMormanId!='')
	xhr.open("GET", url, true,userEmail,userPasswd);
else 
	xhr.open("GET", url, true);
//xhr.open("POST",url,true)
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
    content+="<p><a target=\"_self\" style=\"color: #4D751F;\" href=\""+WS_URL+"/users/curatenie-morman-detalii.jsf?garbageId="+morman.garbageId+"\">&raquo; Click aici pentru detalii si ca sa ti-l aloci</a></p><br/>";
	content+='<div style="display:block; width:300px; height:130px">'

	if (morman.pictures!=null)
	{
		for(var j=0;j<morman.pictures.length;j++){
		 imgurl='http://app.letsdoitromania.ro/ImageLoaderServlet?garbageID='+morman.garbageId+'&imageIndex='+j+'&display=1';
	 	 content+='<a href='+imgurl+' data-lightbox="roadtrip"><img style="display:inline; padding-left:5px;width:110px" src='+imgurl+'></img></a>';
		}
	}
	if (morman.trashOutImageUrls!=null)
	{
		trashoutImg=morman.trashOutImageUrls.split(",");
		for (var j=0;j<trashoutImg.length;j++)
		{	

		imgurl=trashoutImg;
		content+='<a href='+imgurl+' data-lightbox="roadtrip"><img style="display:inline;padding-left:5px;width:110px" src='+imgurl+'></img></a>';
		}
	};
   	content+='</div>'
    if(false)
    if(morman.toVote=="true")
		content+="<span style=\"color: #4D751F;cursor: pointer\" onMouseOver=\"this.style.textDecoration='underline'\" onMouseOut=\"this.style.textDecoration='none'\" onclick=\"javascript:voteMorman("+morman.garbageId+")\">Voteaza</span>";
//	if(userRole=="ORGANIZER" || userRole=="ORGANIZER_MULTI" || userRole == "ADMIN")
//		content+=" | <span style=\"color: #4D751F;cursor: pointer\" onMouseOver=\"this.style.textDecoration='underline'\" onMouseOut=\"this.style.textDecoration='none'\" onclick=\"javascript:nominalizeazaMorman("+morman.garbageId+")\">"+nominalizeazaString+"</span>";
	content+="</p>\n";
	content+="<p id=\"infovot\" style=\"display:none\">YOU SHOULDN'T SEE THIS</p>\n";

	infoWindow.setContent(content);
    infoWindow.open(map, marker);
    
  };


function expandPhoto(id)
{


 
}

function renderData(){
	
	
	//console.log('cache length: '+mormane.length,"new data: "+newmormane.length)
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
			var imageUrl="";
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
			
			if(morman.allocatedStatus=="COMPLETELY")
				imageUrl=markerCOMPLETELY; 
			if(morman.allocatedStatus=="PARTIALLY")
				imageUrl=markerPARTIALLY; 
			if(morman.allocatedStatus=="UNALLOCATED")
				imageUrl=markerUNALLOCATED; 
			if(morman.allocatedStatus=="CLEANED")
				imageUrl=markerCLEANED; 
			mormanDate=new Date(morman.recordDate);
			cutoffDate=new Date("2012-09-29");
			if(morman.allocatedStatus=="CLEANED"&&(mormanDate-cutoffDate<0))
				imageUrl=markerGREYCLEANED; 
			if(morman.allocatedStatus!="CLEANED"&&(mormanDate-cutoffDate<0))
				imageUrl=markerGREY; 

			//if(morman.toVote=="true")
				//imageUrl=markerTOVOTE;
			
		    var markerImage = new google.maps.MarkerImage(imageUrl);
		    var infowindow = new google.maps.InfoWindow();

			var latLng = new google.maps.LatLng(latitude,longitude)
			
//		    if(morman.toVote=="true"){
//		        var marker1 = new MarkerWithLabel({
//		            position: latLng,
//		            draggable: false,
//		            map: map,
//		            labelContent: 'Voteaza!',
//		            labelAnchor: new google.maps.Point(22, 0),
//		            labelClass: "labels", // the CSS class for the label
//		            labelStyle: {opacity: 0.75}
//		          });
//		    
//		    //marker1.morman=undefined;
//		    marker1.morman=morman;
//		    morman.marker=marker1;
//		    google.maps.event.addListener(marker1, 'click', onMarkerClick);
//		    marker1.setVisible(false);
//		    markers.push(marker1);
//		    }
			//if(morman.toVote=="false"){
			var marker = new google.maps.Marker({
		           position: latLng,
		           icon:markerImage,
		           draggable: false,
		           map: map,
		           title:""
		          });			 
			
		    marker.morman=morman;
		    morman.marker=marker;
		    google.maps.event.addListener(marker, 'click', onMarkerClick);
		    marker.setVisible(false);
		    markers.push(marker);
			//};
		    //marker.morman=undefined;
		    if(soloMormanId!=-1&&soloMormanId!='')
		    if(morman.garbageId==soloMormanId)
		    {
		    	map.panTo(latLng);
		    	map.setCenter(latLng);
		    	map.setZoom(13);
		    }
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
    if(getElementByValue("clustering").checked==true)
		markerClusterer = new MarkerClusterer(map, markers, {
		  maxZoom: zoom,
		  gridSize: size,
		  styles: styles[style],
		  ignoreHidden:true
		});

  	poze=0;
	///if(dontLoad==false)
	if(false)
	{
		
		var elements = document.getElementsByClassName("photoMormanLista");
		parent=document.getElementById('scrollPhotos')
		for(var i=0; i<elements.length; i++) {
			parent.removeChild(elements[i]);
		}
	
	document.getElementById('scrollPhotos').innerHTML="";
	for(var i=0;i<mormane.length && poze<100;i++)
	if (showMarker(mormane[i])==true)
	if (mormane[i].pictures!=null)
		for (var j=0;j<mormane[i].pictures.length;j++)
		{
	
 		 parent.innerHTML += '<span class="photoMormanLista" onclick="zoomToGarbage('+i+')" style="display:block;padding:5px;"><img width="110px" src="http://app.letsdoitromania.ro/ImageLoaderServlet?garbageID='+mormane[i].garbageId+'&imageIndex='+j+'&display=1"></img></span>';
		 poze++;
		break;
		}
	if (mormane[i].trashOutImageUrls!=null)
		for (var j=0;j<mormane[i].trashOutImageUrls.length;j++)
		{
	
 		 parent.innerHTML += '<span class="photoMormanLista" onclick="zoomToGarbage('+i+')" style="display:block;padding:5px;"><img width="110px" src="'+mormane[i].trashOutImageUrls[j]+'"></img></span>';
		 poze++;
		break;
		}
		
	};

    
    newmormane=[];
}

function zoomToGarbage(value){
	if(value!=null){
        garbage=mormane[value];
		garbage.marker.setVisible(true);
		//"x":"25.85997","y":"44.37364"
		centerLatitude=garbage.y;
		centerLongitude=garbage.x;
		bottomRightY=parseFloat(garbage.y)-0.0005;
		bottomRightX=parseFloat(garbage.x)+0.0005;
		topLeftY=parseFloat(garbage.y)+0.0005;
		topLeftX=parseFloat(garbage.x)-0.0005;
		var latLngBottom = new google.maps.LatLng(bottomRightY,bottomRightX)
	    var latLngTop = new google.maps.LatLng(topLeftY,topLeftX)
	
	    var bounds = new google.maps.LatLngBounds();
	    bounds.extend(latLngBottom);
	    bounds.extend(latLngTop);
		dontLoad=true;
	    map.fitBounds(bounds);
		map.setZoom(18);
	    map.panTo(bounds.getCenter());

	};
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
		layersOptions.judet=jsonObject.name;
		getElementByValue("judet").checked="checked";
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
	  if (dontLoad==true)
	  {
		  //dontLoad=false;
		  //return;
	  }
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
		
		//url = 'http://localhost:8080/LDIRBackend/map/ws/garbageList/';
		if(soloMormanId!=-1&&soloMormanId!='')
			url += '/LDIRBackend/ws/garbage/'+soloMormanId;
		else 
			url += '/LDIRBackend/map/ws/garbageList/';
		url += '?topLeftX='+Number(topLeftX).toString()
		  
		url += '&topLeftY='+topLeftY
		url += '&bottomRightX='+bottomRightX
		url += '&bottomRightY='+bottomRightY;
	
	//alert(soloMormanId);
		
	getMormane(url);
	
	}

function load() {   
		
		var myOptions = {
		                zoom: 12,
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
			  if(map.getZoom()<17)
					dontLoad=false;
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
	//soloMormanId=mormanId;
}

google.maps.event.addDomListener(window, 'load', load);