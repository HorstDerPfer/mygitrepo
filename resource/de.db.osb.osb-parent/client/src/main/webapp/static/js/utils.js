function submitter(myfield,e){
	var keycode;
	
	if (window.event) keycode = window.event.keyCode;
	else if (e) keycode = e.which;
	else return true;

	if (keycode == 13){
	   myfield.form.submit();
   		return false;
   	}
   	else return true;
}

function disableField(strField){
	var objField = document.getElementById(strField);
	
	if(objField.disabled == true){
		objField.disabled = false;
		objField.style.backgroundColor = "#ffffff";
	}
	else{
		objField.disabled = true;
		objField.style.backgroundColor = "#dddddd";
	}
}

function activateRow(tr){
	var styleClass = tr.className;
	tr.className = "activerow";
}

function changeRow(tr,link){
	var styleClass = tr.className;
	tr.className = "activerow";
	tr.onmouseout =  function(){tr.className = styleClass;};
	var tds = tr.getElementsByTagName("td");
	
	for (var i = 0; i < tds.length; i++){
		if(tds[i].getElementsByTagName("input").length==0 && tds[i].getElementsByTagName("a").length==0){
			tds[i].onclick = function(){location.href = link;};
			tds[i].style.cursor = "pointer";
		}
	}
}

function changeRowStyle(tr){
	var styleClass = tr.className;
	tr.className = "activerow";
	tr.onmouseout =  function(){tr.className = styleClass;};
	tr.style.cursor = "pointer";
}

function changeRowBlank(tr,link){
	var styleClass = tr.className;
	tr.className = "activerow";
	tr.onmouseout =  function(){tr.className = styleClass;};
	var tds = tr.getElementsByTagName("td");
	
	for (var i = 0; i < tds.length; i++){
		if(tds[i].getElementsByTagName("input").length==0 && tds[i].getElementsByTagName("a").length==0){
			tds[i].onclick = function(){window.open(link);};
			tds[i].style.cursor = "pointer";
		}
	}
}

function confirmLink(link, text) {
	var is_confirmed = confirm(text);
	if (is_confirmed)
		return true;
	else
		return false;
}

function confirmSubmit(form, text) {
	var is_confirmed = confirm(text);
	if (is_confirmed)
		form.submit();
	else
		return false;
}

function initDataChangeListener(formId) {
	$A($$('#'+formId+' input')).each(function(input) {
		Event.observe(input, 'change', function() { dataChanged = true; }); 
    });
    
    $A($$('#'+formId+' textarea')).each(function(input) {
		Event.observe(input, 'change', function() { dataChanged = true; }); 
    });
    
    $A($$('#'+formId+' select')).each(function(input) {
		Event.observe(input, 'change', function() { dataChanged = true; }); 
    });
}