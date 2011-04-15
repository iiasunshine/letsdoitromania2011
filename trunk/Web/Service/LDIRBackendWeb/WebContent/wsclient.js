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