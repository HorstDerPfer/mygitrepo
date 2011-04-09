/* Funktion zum verstecken aller Karteireiter-Divs */
function hideTabDivs(){
    var arrDivs = document.getElementsByTagName("div");
    
    for (i=0; i<arrDivs.length; i++){
        if((arrDivs[i].id.indexOf("tabDiv") != -1)){
            arrDivs[i].style.display = "none";
        }
    }
    hideTabLinks();
}

/* Funktion zum inaktivsetzen aller Karteireiter */
function hideTabLinks(){
    var arrLinks  = document.getElementsByTagName("a");
    
    for (i=0; i<arrLinks.length; i++){
        if((arrLinks[i].id.indexOf("tabLink") != -1)){
            arrLinks[i].className = "tab_ina";
        }
    }
}

/*
Dies ist die Einstiegsfunktion zur Steuerung der Karteireiter. Sie setzt zunächst alle DIVs die von den
Karteireitern abhängig sind auf unsichtbar und setzt alle Karteireiter auf inaktiv. Danach wird der
angeklickte Karteireiter wieder auf aktiv gesetzt und dessen DIV angezeigt
*/
function showTabDiv(strId){
    hideTabDivs();
    if(Object.isElement($('tabLink'+strId))) {
    	$('tabLink'+strId).className = "tab_act";
    }
    if(Object.isElement($('tabDiv'+strId))) {
    	$('tabDiv'+strId).style.display = "block";
    }
}

/**
 * Steuert die Karteireiter in der Baumassnahmenadministration
 * @param tab
 * @return
 */
function showTabDivBM(tab) {
	if(Object.isElement($('tab'))) { 
		$('tab').value = tab;
	}
	showTabDiv(tab);
}

//Hängt der action des Formulares den Namen des aktiven Karteireiters an
function addParam(strId,strForm){
    var objForm = $(strForm);
    if(objForm.action.indexOf("?") != -1){
        objForm.action = objForm.action.substr(0,objForm.action.indexOf("?"));
    }
    objForm.action += "?tab="+strId;
}

/*
Wird verwendet um nach dem Speichern wieder den Richtigen Karteireiter angezeigt zu bekommen. Dazu wird
beim Speicher der Wert tab mit übergeben und die Funktion mit diesem Wert beim body onload aufgerufen.
Der Wert wird der action des Formulars in Funktion showTabDiv angehängt
*/
function activateTab(strId,strForm){
    if(strId){
        hideTabDivs();
        $("tabLink"+strId).className = "tab_act";
        $("tabDiv" +strId).style.display = "block";
        if(strForm) addParam(strId,strForm);
    }
}

function toggleAllBmRows(img){
	var url = img.src;
	var imgId = "img_toggle_";
	var rowId = "row_toggle_";

	if(img.src.indexOf("plus") > -1){
		url = url.replace(/plus/g, "minus");
		$A($$('tr.evenrow2', 'tr.oddrow2')).each(function(tableRow) {
			if(tableRow.id.indexOf(rowId) === 0){
				tableRow.show();
				var nr = tableRow.id.slice(11, tableRow.id.lastIndexOf("_"));
				$(imgId + nr).src = $(imgId + nr).src.replace(/plus/g, "minus");
			}
		});
	}
	if(img.src.indexOf("minus") > -1){
		url = url.replace(/minus/g, "plus");
		$A($$('tr.evenrow2', 'tr.oddrow2')).each(function(tableRow) {
			if(tableRow.id.indexOf(rowId) === 0){
				tableRow.hide();
				var nr = tableRow.id.slice(11, tableRow.id.lastIndexOf("_"));
				$(imgId + nr).src = $(imgId + nr).src.replace(/minus/g, "plus");
			}
		});
	}

	img.src = url;
}

function toggleBmRows(img, id){
	var rowid = "row_toggle_" + id;
	
	$A($$('tr.evenrow2', 'tr.oddrow2')).each(function(tableRow) {
		if(tableRow.id.indexOf(rowid) === 0 && tableRow.id.lastIndexOf("_") == rowid.length){
			tableRow.toggle();
		}
	});
	
	var url = img.src;
	if(img.src.indexOf("plus") > -1){
		url = url.replace(/plus/g, "minus");
	}
	if(img.src.indexOf("minus") > -1){
		url = url.replace(/minus/g, "plus");
	}

	img.src = url;
}