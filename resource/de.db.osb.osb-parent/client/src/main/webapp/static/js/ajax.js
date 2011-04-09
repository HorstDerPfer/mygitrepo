function refreshJbb(url, id, tab, ajax) {
	new Ajax.Updater('divJbb', url, {
		parameters : {
			id :id,
			tab :tab,
			ajax :ajax
		}
	});
}

function refreshBbp(url, id, tab, ajax) {
	new Ajax.Updater('divBbp', url, {
		parameters : {
			id :id,
			tab :tab,
			ajax :ajax
		}
	});
}

function refreshStatusAktivitaeten(url, id, tab, ajax) {
	new Ajax.Updater('divStatusAktivitaeten', url, {
		parameters : {
			id :id,
			tab :tab,
			ajax :ajax
		}
	});
}

function refreshVerzichtQTrasse(url, id, tab, ajax) {
	new Ajax.Updater('divVerzichtQTrasse', url, {
		parameters : {
			id :id,
			tab :tab,
			ajax :ajax
		}
	});
}

function refreshAbstimmungNachbarbahn(url, id, tab, ajax) {
	new Ajax.Updater('divAbstimmungNachbarbahn', url, {
		parameters : {
			id :id,
			tab :tab,
			ajax :ajax
		}
	});
}

function refreshBenchmark(url, id, tab, ajax) {
	new Ajax.Updater('divBenchmark', url, {
		parameters : {
			id :id,
			tab :tab,
			ajax :ajax
		}
	});
}

function refreshKommentar(url, id, tab, ajax) {
	new Ajax.Updater('divKommentar', url, {
		parameters : {
			id :id,
			tab :tab,
			ajax :ajax
		}
	});
}

function refreshAenderungsdokumentation(url, id, tab, ajax) {
	new Ajax.Updater('divAenderungsdokumentation', url, {
		parameters : {
			id :id,
			tab :tab,
			ajax :ajax
		}
	});
}

function refreshEskalationAusfall(url, id, tab, ajax) {
	new Ajax.Updater('divEskalationAusfall', url, {
		parameters : {
			id :id,
			tab :tab,
			ajax :ajax
		}
	});
}

function refreshUebergabeblatt(url, id, showZuege, tab, ajax) {
	new Ajax.Updater('divUeb', url, {
		parameters : {
			id :id,
			showZuegeUeb :showZuege,
			tab :tab,
			ajax :ajax
		}
	});
}

function refreshZvf1(url, id, tab, showZuege, ajax) {
	new Ajax.Updater('divZvf', url, {
		parameters : {
			id :id,
			tab :tab,
			showZuegeZvf :false,
			ajax :ajax
		}
	});
}

function refreshZvf2(url, id, zvfId, tab, showZuege, ajax) {
	new Ajax.Updater('divZvf', url, {
		parameters : {
			id :id,
			showzvf :zvfId,
			tab :tab,
			showZuegeZvf :showZuege,
			ajax :ajax
		}
	});
}

function refreshFahrplan(url, id, tab, ajax) {
	new Ajax.Updater('divFahrplan', url, {
		parameters : {
			id :id,
			tab :tab,
			ajax :ajax
		}
	});
}

function refreshBearbeiter(url, id, tab, ajax) {
	new Ajax.Updater('divBearbeiter', url, {
		parameters : {
			id :id,
			tab :tab,
			ajax :ajax
		}
	});
}


function refreshUebergabeblattZuege(url, id, showTab, showZuege) {
	new Ajax.Updater('divUebZuege', url, {
		parameters : {
			id :id,
			showTabUebergabegblatt :showTab,
			showZuegeUeb :showZuege
		},
		evalScripts: true
	});
}

function refreshAddMeilenstein(url, art, schnittstelle){
	new Ajax.Updater('divAddMeilenstein', url, {
		parameters : {
			art :art,
			schnittstelle :schnittstelle
		}
	})
}

function addBearbeiter(url, id, userId) {
	new Ajax.Updater('divBearbeiterTabelle', url, {
		parameters : {
			id :id,
			userId :userId
		}
	});
}


/* -------- */
function removeBearbeiter(url, id) {
	new Ajax.Updater('divBearbeiter', url, {
		parameters : {
			id :$('id').value,
			bearbeiterId :id
		}
	});
}
function refreshAenderung(url) {
	new Ajax.Updater('divAenderung', url, {
		parameters : {
			id :$('id').value
		}
	});
}

function addAenderung(url) {
	new Ajax.Updater('divAenderung', url, {
		parameters : {
			id :$('id').value,
			grund :$('grund').value,
			aufwand :$('aufwand').value
		}
	});
}

function removeAenderung(url, id) {
	new Ajax.Updater('divAenderung', url, {
		parameters : {
			id :$('id').value,
			aenderungId :id
		}
	});
}
function refreshGEVU(url) {
	new Ajax.Updater('divGEVU', url, {
		parameters : {
			id :$('id').value
		}
	});
}

function addGEVU(url) {
	new Ajax.Updater('divGEVU', url, {
		parameters : {
			id :$('id').value,
			// gevu: $('gevu').value
			gevuKundengruppe :$('gevuKundengruppe').value
		},
		onComplete: refreshEVUEvents
	});
}

function removeGEVU(url, id) {
	new Ajax.Updater('divGEVU', url, {
		parameters : {
			id :$('id').value,
			termineGEVUId :id
		},
		onComplete: refreshEVUEvents
	});
}
function refreshPEVU(url) {
	new Ajax.Updater('divPEVU', url, {
		parameters : {
			id :$('id').value
		}
	});
}

function refreshEVUEvents() {
	// Skript aus editBaumassnahme.jsp (362-366)
	$$('div#schnittstellen input.checkbox').toArray().each(function(item) {
		if(item.onclick == null) {
			item.onclick = function() {
				toggleTextboxState(this);
			};
		}
		toggleTextboxState(item);
	});
				
	$$('div#schnittstellen input.date', 'div#schnittstellen input.dateShort').toArray().each(function(item) {
		if(item.onfocus == null) {
			item.onfocus = function() {
				focusedElement = this.id;
			};
		}
	});
}

function addPEVU(url) {
	new Ajax.Updater('divPEVU', url, {
		parameters : {
			id :$('id').value,
			// evu: $('evu').value
			evuNr :$('grpName').value
		},
		onComplete: refreshEVUEvents
	});
}

function removePEVU(url, id) {
	new Ajax.Updater('divPEVU', url, {
		parameters : {
			id :$('id').value,
			terminePEVUId :id
		},
		onComplete: refreshEVUEvents
	});
}

function refreshBearbeitungsbereich(url, regionalBereichFpl, type) {
	new Ajax.Updater('divBearbeitungsbereich', url, {
		parameters : {
			regionalBereichFpl :regionalBereichFpl,
			forwardType :type
		}
	});

}
function refreshBearbeitungsbereichSearch(url, regionalBereichFpl, type) {
	new Ajax.Updater('divBearbeitungsbereichSearch', url, {
		parameters : {
		regionalBereichFpl :regionalBereichFpl,
		forwardType :type
	}
	});
	
}

function refreshBearbeitungsbereichUser(url, regionalBereichFpl, type) {
	new Ajax.Updater('divBearbeitungsbereichUser', url, {
		parameters : {
		regionalBereichFpl :regionalBereichFpl,
		forwardType :type
	}
	});
	
}

function refreshMailEmpfaenger(url, regionalBereich) {
	var userId = document.getElementById("uebEmpfaenger").value;

	new Ajax.Updater('divMailEmpfaenger', url, {
		parameters : {
			/* id: $('id').value, */
			regionalBereich :regionalBereich,
			userId :userId
		}
	});

}

function refreshMailEmpfaengerValue(url, empfaenger, bmId) {
	new Ajax.Updater('divMailEmpfaenger', url, {
		parameters : {
			/* id: $('id').value, */
			empfaenger :empfaenger,
			bmId :bmId
		}
	});

}

function refreshBBP(url) {
	new Ajax.Updater('divBBP', url, {
		parameters : {
			id :$('id').value
		}
	});
}

function addBBP(url) {
	new Ajax.Updater('divBBP', url, {
		parameters : {
			id :$('id').value,
			masId :$('masId').value
		}
	});
}

function removeBBP(url, id) {
	new Ajax.Updater('divBBP', url, {
		parameters : {
			id :$('id').value,
			bbpId :id
		}
	});
}

function refreshEmpfaenger(url, type) {
	new Ajax.Updater('divEmpfaenger', url, {
		parameters : {
			id :$('id').value,
			type :type
		}
	});
}

function removeEmpfaenger(url, id, type) {
	new Ajax.Updater('divEmpfaenger', url, {
		parameters : {
			id :$('id').value,
			empfaenger :id,
			type :type
		}
	});
}

function addEmpfaenger(url, type) {
	new Ajax.Updater('divEmpfaenger', url, {
		parameters : {
			id :$('id').value,
			empfaengerName :$('empfaengerName').value,
			type :type
		}
	});
}

function refreshEmpfaengerBBZR(url, type) {
	new Ajax.Updater('divEmpfaengerBBZR', url, {
		parameters : {
			id :$('id').value,
			type :type
		}
	});
}

function removeEmpfaengerBBZR(url, id, type) {
	new Ajax.Updater('divEmpfaengerBBZR', url, {
		parameters : {
			id :$('id').value,
			empfaenger :id,
			type :type
		}
	});
}

function addEmpfaengerBBZR(url, type) {
	new Ajax.Updater('divEmpfaengerBBZR', url, {
		parameters : {
			id :$('id').value,
			empfaengerName :$('empfaengerNameBBZR').value,
			type :type
		}
	});
}

function refreshKnotenzeiten(url) {
	new Ajax.Updater('divKnotenzeiten', url, {
		parameters : {
			id :$('id').value
		}
	});
}

function addKnotenzeiten(url, zugId) {
	new Ajax.Updater('divKnotenzeiten', url, {
		parameters : {
			zugId :zugId
		}
	});
}

function removeKnotenzeiten(url, id, zugId) {
	new Ajax.Updater('divKnotenzeiten', url, {
		parameters : {
			knotenzeit :id,
			zugId :zugId
		}
	});
}

function removeZug(url, id) {
	new Ajax.Updater('divZug', url, {
		parameters : {
			id :$('id').value,
			zugId :id
		}
	});
}

function refreshZug(url) {
	new Ajax.Updater('divZug', url, {
		parameters : {
			id :$('id').value
		}
	});
}

function addZug(url) {
	new Ajax.Updater('divZug', url, {
		parameters : {
			id :$('id').value
		}
	});
}

function removeStrecke(url, id, type) {
	new Ajax.Updater('divStrecke', url, {
		parameters : {
			id :$('id').value,
			streckeId :id,
			type :type
		}
	});
}

function addStrecke(url, type) {
	new Ajax.Updater('divStrecke', url, {
		parameters : {
			id :$('id').value,
			type :type
		}
	});
}

function refreshStrecke(url, type) {
	new Ajax.Updater('divStrecke', url, {
		parameters : {
			id :$('id').value,
			type :type
		}
	});
}

function removeStreckeBBZR(url, id, type) {
	new Ajax.Updater('divStreckeBBZR', url, {
		parameters : {
			id :$('id').value,
			streckeId :id,
			type :type
		}
	});
}

function addStreckeBBZR(url, type) {
	new Ajax.Updater('divStreckeBBZR', url, {
		parameters : {
			id :$('id').value,
			type :type
		}
	});
}

function refreshFahrplanDetails(url, id) {
	new Ajax.Updater('divFahrplanDetails', url, {
		parameters : {
			id :id
		}
	});
}

function refreshBuendelVzgStrecken(url, buendelId) {
	$('vzgStreckenIndicator').show();
	new Ajax.Updater('vzgStreckenAlle', url, {
		parameters : {
			buendelId : buendelId
		},
		onComplete : function(){
			$('vzgStreckenIndicator').hide();
			$('vzgStreckenAlle').show();
			$('vzgStrecken').hide();
		}
	});
}