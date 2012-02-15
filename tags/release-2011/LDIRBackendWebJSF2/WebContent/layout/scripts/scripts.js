/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function checkForWarningOnLoad(b){
	//el=document.getElementById('rightColumn');
	el=document.getElementById('formProfil:vizibleData');
    //alert(el);
	showWarning(el);

}
function showWarning(element){

	if (element.checked==true)
	  document.getElementById('warningPersonalData').style.display = 'block';
	  else
		  document.getElementById('warningPersonalData').style.display = 'none';
	
} 

function numbersonly(myfield, e, dec){
    var key;
    var keychar;

    if (window.event){
        key = window.event.keyCode;
    //alert('--1: '+key);
    }else if (e){
        key = e.which;
    //alert('--2: '+myfield.value);
    }else{
        return true;
    }
    keychar = String.fromCharCode(key);

    // control keys
    if ((key==null) || (key==0) || (key==8) ||
        (key==9) || (key==13) || (key==27)|| (key==118) ){
        return true;
    }

    // numbers
    else if ((("0123456789").indexOf(keychar) > -1)){
        return true;
    }

    // decimal point jump
    else if (dec && (keychar == ".")){
        myfield.form.elements[dec].focus();
        return false;
    }
    else{
        return false;
    }
}

function popup(mylink, windowname){
    if (! window.focus){
        return true;
    }
    var href;
    if (typeof(mylink) == 'string'){
        href=mylink;
    }else{
        href=mylink.href;
    }
    window.open(href, windowname, 'width=800,height=600,scrollbars=1,locationbar=0,menubar=0,toolbar=0,personalbar=0, navigationbar=0, addonbar=0');
    return false;
}

