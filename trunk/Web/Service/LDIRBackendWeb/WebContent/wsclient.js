function createXMLHttpRequest() {
	try {
		return new XMLHttpRequest();
	} catch (e) {
	}
	try {
		return new ActiveXObject("Msxml2.XMLHTTP");
	} catch (e) {
	}
	return new ActiveXObject("Microsoft.XMLHTTP");
}

function sendRequest(method, url, callback, object, expect_reply, user, pass) {
	var req = createXMLHttpRequest();
	if (typeof (user) != 'undefined' && typeof (pass) != 'underfined')
		req.open(method, url, true, user, pass);
	else
		req.open(method, url, true);
	req.onreadystatechange = callback;
	if (typeof (object) != 'undefined' && object != null)
		req.setRequestHeader('Content-Type', 'application/json');
	else
		object = null;
	if (typeof (expect_reply) != 'undefined' && expect_reply)
		req.setRequestHeader('Accept', 'application/json');
	req.send(object);
	return req;
}

function sendRequestString(method, url, callback, object, user, pass) {
	var req = createXMLHttpRequest();
	if (typeof (user) != 'undefined' && typeof (pass) != 'underfined')
		req.open(method, url, true, user, pass);
	else
		req.open(method, url, true);
	req.onreadystatechange = callback;
	if (typeof (object) != 'undefined' && object != null)
		req.setRequestHeader('Content-Type', 'application/json');
	else
		object = null;
	req.send(object);
	return req;
}

var gen_form_reference;
var gen_req;

var generic_select;
var generic_type;
var generic_field_id;
var generic_field_name;

var generic_edit_id;

var generic_err_callback = null;
var generic_ok_callback = null;


function generic_load_callback()
{
	if (gen_req.readyState != 4)
		return;
	if (gen_req.status != 200) {
		alert('Unable to get data: ' + fetchorg_req.status);
		if (generic_err_callback != null) {
			var tmp = generic_err_callback;
			generic_err_callback = null;
			tmp();
		}
		return;
	}
	server_data = JSON.parse(gen_req.responseText);
	var el, i = 0, rForm = document.getElementById(gen_form_reference);
	while (el = rForm.elements[i++]) 
		el.value = server_data[el.name];
	if (generic_ok_callback != null) {
		var tmp = generic_eok_callback;
		generic_ok_callback = null;
		tmp();
	}
}


function generic_select_callback()
{
	if (gen_req.readyState != 4)
		return;
	if (gen_req.status != 200) {
		alert('Unable to get data: ' + gen_req.status);
		if (generic_err_callback != null) {
			var tmp = generic_err_callback;
			generic_err_callback = null;
			tmp();
		}
		return;
	}
	reply_string = gen_req.responseText;
	objects = JSON.parse(reply_string);
	
	selectObj = document.getElementById(generic_select);
	selectObj.options.length = 0;
	
	obj_array = objects[generic_type];
	
	if (typeof(obj_array[generic_field_id]) != 'undefined') {
		selectObj.options[selectObj.options.length] = 
            new Option(obj_array[generic_field_name], obj_array[generic_field_id], false, false);
		if (generic_ok_callback != null) {
			var tmp = generic_eok_callback;
			generic_ok_callback = null;
			tmp();
		}
		return;
	}
	
	for (idx in obj_array) 
		selectObj.options[selectObj.options.length] = 
	    	new Option(obj_array[idx][generic_field_name], obj_array[idx][generic_field_id], false, false);
	if (generic_ok_callback != null) {
		var tmp = generic_eok_callback;
		generic_ok_callback = null;
		tmp();
	}
}


function generic_insert_callback()
{
	if (gen_req.readyState != 4)
		return;
	if (gen_req.status != 200) {
		alert('Unable to insert data: ' + gen_req.status);
		if (generic_err_callback != null) {
			var tmp = generic_err_callback;
			generic_err_callback = null;
			tmp();
		}
		return;
	}
	alert('Data ok!');
	if (gen_form_reference != null)
		document.getElementById(gen_form_reference).reset();
	if (generic_ok_callback != null) {
		var tmp = generic_eok_callback;
		generic_ok_callback = null;
		tmp();
	}
}


function json_from_form(form_name)
{
	gen_form_reference = form_name;
	var org = new Object();
	var el, i = 0, rForm = document.getElementById(gen_form_reference);
	while (el = rForm.elements[i++]) 
		org[el.name] = el.value;
	return JSON.stringify(org);
}
