var hideSaveDialog = false;
var hideAllDialogs = false;

function showSaveDialog(){
	if(hideSaveDialog == false){
		$j("#saveDialog").dialog({
			modal: true,
			position: 'center',
			autoOpen: false
		});
		$j("#saveDialog").dialog("open");
	} else {
		/* hideSaveDialog ist immer nur zuer einmaligen Verwendung */
		/* und muss somit immer wieder zurueckgesetzt werden*/
		$j("#saveDialog").dialog("close");
		hideSaveDialog = false;
	}
}
/* Wird im IE6 benoetigt, da sonst showDialog doppelt ausgefuehrt wird, da IE6 das win */
var onBeforeUnloadFired = false;
function ResetOnBeforeUnloadFired() { 
    onBeforeUnloadFired = false; 
}

function changeChecker(){
	if(!hideAllDialogs){
		if(!onBeforeUnloadFired){
			onBeforeUnloadFired = true;
			//if (dataChanged)
				//return '<bean:message key="common.confirmChange" />';
			//else
				showSaveDialog();
		}
		window.setTimeout("ResetOnBeforeUnloadFired()", 1000);
	} else
		hideAllDialogs = false;
}


function hideProgressbar() {
	hideSaveDialog = true;
	showSaveDialog();
}