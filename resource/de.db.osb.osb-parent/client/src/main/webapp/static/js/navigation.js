/****************************************
JavaScript-Funktionen für Hauptnavigation
****************************************/

function getAForDiv(divElement) {
	return document.getElementById("navLink" + divElement.id.slice(divElement.id.indexOf("_")));
}

function getDivForA(aElement) {
	return document.getElementById("navDiv" + aElement.id.slice(aElement.id.indexOf("_")));
}

//*******************************************************************************************************************
function openMainMenu(strId){
    var objLink = document.getElementById(strId);
    openNavDiv(objLink);
}

//*******************************************************************************************************************
//öffnet bzw aktiviert einen Navigationsmenüpunkt
function openNavDiv(objLink){
	var aElement;
	var divElement = getDivForA(objLink);
	
	// Navimenü zurücksetzen
	closeAllNavDivs();
	
	// Gewählte Ebene und alle darüber ausklappen (wenn vorhanden) und Klassen auf "navLink_o" setzen
	if(divElement == null) {
		divElement = objLink.parentNode;
		if(divElement.className == "navDiv") {
	   		aElement = getAForDiv(divElement);
			aElement.className = "navLink_o";
		}
	}
    divElement.style.display = "block";
    
    for(divElement = divElement.parentNode; divElement != null; divElement = divElement.parentNode) {
    	if(divElement.className == "navDiv") {
    		divElement.style.display = "block";
    		aElement = getAForDiv(divElement);
    		aElement.className = "navLink_o";
    	}
    }
  	
	// Gewähltes Element auf "selektiert" setzen
	if(objLink.className == "navLink_d")
		objLink.className = "navLink_s_d";
	else
		objLink.className = "navLink_s";
}

//*******************************************************************************************************************
// schließt alle Navigationsuntermenüs und setzt die Klassennamen (zurück)
function closeAllNavDivs(){
    var arrDivs = document.getElementsByTagName("div");
    
    for (i=0; i<arrDivs.length; i++){
        if((arrDivs[i].className == "navDiv")){
            arrDivs[i].style.display = "none";
        }
    }
    
    resetNavLinkClasses();
}

//*******************************************************************************************************************
// setzt alle Klassennamen (zurück)
function resetNavLinkClasses() {
	var hasChilds;
	var divElement;
    var childElement;
    var arrAs = document.getElementsByTagName("a");
    
    for (i=0; i<arrAs.length; i++) {
        if(arrAs[i].className.indexOf("navLink") == 0) {
	        hasChilds = false;
   	    	divElement = getDivForA(arrAs[i]);
   	    	if(divElement != null) {
	        	for(childElement = divElement.firstChild; childElement != null && !hasChilds; childElement = childElement.nextSibling) {
	        		if(childElement.nodeName == "A")
		        		hasChilds = true;
	        	}
				if(hasChilds) {
					arrAs[i].className = "navLink_d";
				} else {
					arrAs[i].className = "navLink";
				}
			} else {
				arrAs[i].className = "navLink";
			}
        }
    }
}