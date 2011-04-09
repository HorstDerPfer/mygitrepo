function getGevuId(text, li) {
	$('gevuId').value = li.id;
}

function getPevuId(text, li) {
	$('pevuId').value = li.id;
}

/**
 * Fuegt eine weitere Zeile in die EVU-Liste ein
 * 
 * @param target
 *            gültige Werte: pevu, gevu
 * @param evuId
 *            eindeutige Nummer des EVU
 * @param caption
 *            Beschriftung der Zeile (zB.: EVU + Kundennr.)
 * @param art
 *            Art der Baumaßnahme: {A, B, KS, QS}
 * @param delTitleStr
 *            title-Text fuer Loesch-Button
 * @param ajaxUrl
 *            URL fuer AJAX-Aufruf
 */
function insertEVURow(target, evuId, caption, art, requiredMsg, delTitleStr) {

	var ctrl;
	var isGEVU = (target.indexOf('gevu') != -1);
	var isPEVU = (target.indexOf('pevu') != -1)
	if (isGEVU) {
		ctrl = $('gevuLinkedIds');
		$('gevuId').value = "";
	} else if (isPEVU) {
		ctrl = $('pevuLinkedIds');
		$('pevuId').value = "";
	}

	// prüfen, ob evuId schon verknüpft wurde
	// JA: abbrechen
	// NEIN: Id in Liste speichern
	if (ctrl != null) {
		if (ctrl.value.split(",").indexOf(evuId) != -1) {
			if (isGEVU)
				$('gevuError').show();
			else if (isPEVU)
				$('pevuError').show();
			return;
		}
		ctrl.value += evuId;
		ctrl.value += ",";
	}

	// welchen Style verwenden?
	var styleClass = ($(target).rows.length % 2 == 0) ? 'evenrow' : 'oddrow';

	// Template in Abhängigkeit der Maßnahmenart auswählen
	var strTemplate;

	switch (art) {
	case 'a', 'A':
		if (isGEVU) {
			strTemplate = '<tr id="#{type}__#{evuId}" class="#{styleClass}">';
			strTemplate += '<td style="overflow:hidden;max-width:120px;min-width:80px;"><div class="box" style="text-align:left">#{caption}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}ZvfEntwurf(#{evuId})" id="#{type}ZvfEntwurf__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsZvFEntwurfErforderlich(#{evuId})" id="chk_#{type}ZvfEntwurf__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}StellungnahmeEVU(#{evuId})" id="#{type}StellungnahmeEVU__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsStellungnahmeEVUErforderlich(#{evuId})" id="chk_#{type}StellungnahmeEVU__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}ZvF(#{evuId})" id="#{type}Zvf__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsZvFErforderlich(#{evuId})" id="chk_#{type}Zvf__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}MasterUebergabeblattGV(#{evuId})" id="#{type}MasterUebergabeblattGV__#{evuId}" class="dateDisabled" maxlength="10" disabled="" /><br /><input type="checkbox" name="#{type}IsMasterUebergabeblattGVErforderlich(#{evuId})" id="chk_#{type}MasterUebergabeblattGV__#{evuId}" class="checkbox" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}UebergabeblattGV(#{evuId})" id="#{type}UebergabeblattGV__#{evuId}" class="dateDisabled" maxlength="10" disabled="" /><br /><input type="checkbox" name="#{type}IsUebergabeblattGVErforderlich(#{evuId})" id="chk_#{type}UebergabeblattGV__#{evuId}" class="checkbox" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}Fplo(#{evuId})" id="#{type}Fplo__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsFploErforderlich(#{evuId})" id="chk_#{type}Fplo__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}EingabeGFD_Z(#{evuId})" id="#{type}EingabeGFD_Z__#{evuId}" class="dateDisabled" maxlength="10" disabled="" /><br /><input type="checkbox" name="#{type}IsEingabeGFD_ZErforderlich(#{evuId})" id="chk_#{type}EingabeGFD_Z__#{evuId}" class="checkbox" />#{requiredMessage}</div></td>';
			strTemplate += '<td><a href="#" onclick="if(confirmLink(this.href, \'#{confirmText}\')) removeEvuRow(\'#{targetId}\', \'#{evuId}\');return false;" class="delete">&nbsp;</a></td>';
		} else if (isPEVU) {
			strTemplate = '<tr id="#{type}__#{evuId}" class="#{styleClass}">';
			strTemplate += '<td style="overflow:hidden;max-width:120px;min-width:80px;"><div class="box" style="text-align:left">#{caption}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}ZvfEntwurf(#{evuId})" id="#{type}ZvfEntwurf__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsZvfEntwurfErforderlich(#{evuId})" id="chk_#{type}ZvfEntwurf__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}StellungnahmeEVU(#{evuId})" id="#{type}StellungnahmeEVU__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsStellungnahmeEVUErforderlich(#{evuId})" id="chk_#{type}StellungnahmeEVU__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}ZvF(#{evuId})" id="#{type}Zvf__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsZvFErforderlich(#{evuId})" id="chk_#{type}Zvf__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}MasterUebergabeblattPV(#{evuId})" id="#{type}MasterUebergabeblattPV__#{evuId}" class="dateDisabled" maxlength="10" disabled="" /><br /><input type="checkbox" name="#{type}IsMasterUebergabeblattPVErforderlich(#{evuId})" id="chk_#{type}MasterUebergabeblattPV__#{evuId}" class="checkbox" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}UebergabeblattPV(#{evuId})" id="#{type}UebergabeblattPV__#{evuId}" class="dateDisabled" maxlength="10" disabled="" /><br /><input type="checkbox" name="#{type}IsUebergabeblattPVErforderlich(#{evuId})" id="chk_#{type}UebergabeblattPV__#{evuId}" class="checkbox" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}Fplo(#{evuId})" id="#{type}Fplo__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsFploErforderlich(#{evuId})" id="chk_#{type}Fplo__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}EingabeGFD_Z(#{evuId})" id="#{type}EingabeGFD_Z__#{evuId}" class="dateDisabled" maxlength="10" disabled="" /><br /><input type="checkbox" name="#{type}IsEingabeGFD_ZErforderlich(#{evuId})" id="chk_#{type}EingabeGFD_Z__#{evuId}" class="checkbox" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="radio" name="pevuAusfaelleSEV(#{evuId})" value="true" class="checkbox" />Ja</div><div class="box"><input type="radio" name="pevuAusfaelleSEV(#{evuId})" value="false" class="checkbox" />Nein</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}BKonzeptEVU(#{evuId})" id="#{type}BKonzeptEVU__#{evuId}" class="dateShort" maxlength="10" /><br /><input type="checkbox" name="#{type}IsBKonzeptEVUErforderlich(#{evuId})" id="chk_#{type}BKonzeptEVU__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><a href="#" onclick="if(confirmLink(this.href, \'#{confirmText}\')) removeEvuRow(\'#{targetId}\', \'#{evuId}\');return false;" class="delete">&nbsp;</a></td>';
		}
		break;

	case 'b', 'B':
		if (isGEVU) {
			strTemplate = '<tr id="#{type}__#{evuId}" class="#{styleClass}">';
			strTemplate += '<td style="overflow:hidden;max-width:120px;min-width:80px;"><div class="box" style="word-break:break-all;text-align:left">#{caption}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}ZvfEntwurf(#{evuId})" id="#{type}ZvfEntwurf__#{evuId}" class="dateShort" maxlength="10" /><br /><input type="checkbox" name="#{type}IsZvFEntwurfErforderlich(#{evuId})" id="chk_#{type}ZvfEntwurf__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}StellungnahmeEVU(#{evuId})" id="#{type}StellungnahmeEVU__#{evuId}" class="dateDisabled" maxlength="10" disabled="" /><br /><input type="checkbox" name="#{type}IsStellungnahmeEVUErforderlich(#{evuId})" id="chk_#{type}StellungnahmeEVU__#{evuId}" class="checkbox" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}ZvF(#{evuId})" id="#{type}Zvf__#{evuId}" class="dateDisabled" maxlength="10" disabled="" /><br /><input type="checkbox" name="#{type}IsZvFErforderlich(#{evuId})" id="chk_#{type}Zvf__#{evuId}" class="checkbox" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}UebergabeblattGV(#{evuId})" id="#{type}UebergabeblattGV__#{evuId}" class="dateDisabled" maxlength="10" disabled="" /><br /><input type="checkbox" name="#{type}IsUebergabeblattGVErforderlich(#{evuId})" id="chk_#{type}UebergabeblattGV__#{evuId}" class="checkbox" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}Fplo(#{evuId})" id="#{type}Fplo__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsFploErforderlich(#{evuId})" id="chk_#{type}Fplo__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}EingabeGFD_Z(#{evuId})" id="#{type}EingabeGFD_Z__#{evuId}" class="dateDisabled" maxlength="10" disabled="" /><br /><input type="checkbox" name="#{type}IsEingabeGFD_ZErforderlich(#{evuId})" id="chk_#{type}EingabeGFD_Z__#{evuId}" class="checkbox" />#{requiredMessage}</div></td>';
			strTemplate += '<td><a href="#" onclick="if(confirmLink(this.href, \'#{confirmText}\')) removeEvuRow(\'#{targetId}\', \'#{evuId}\');return false;" class="delete">&nbsp;</a></td>';
			strTemplate += '</tr>';
		} else if (isPEVU) {
			strTemplate = '<tr id="#{type}__#{evuId}" class="#{styleClass}">';
			strTemplate += '<td style="overflow:hidden;max-width:120px;min-width:80px;"><div class="box" style="word-break:break-all;text-align:left">#{caption}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}ZvfEntwurf(#{evuId})" id="#{type}ZvfEntwurf__#{evuId}" class="dateShort" maxlength="10" /><br /><input type="checkbox" name="#{type}IsZvfEntwurfErforderlich(#{evuId})" id="chk_#{type}ZvfEntwurf__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}StellungnahmeEVU(#{evuId})" id="#{type}StellungnahmeEVU__#{evuId}" class="dateDisabled" maxlength="10" disabled="" /><br /><input type="checkbox" name="#{type}IsStellungnahmeEVUErforderlich(#{evuId})" id="chk_#{type}StellungnahmeEVU__#{evuId}" class="checkbox" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}ZvF(#{evuId})" id="#{type}Zvf__#{evuId}" class="dateDisabled" maxlength="10" disabled="" /><br /><input type="checkbox" name="#{type}IsZvFErforderlich(#{evuId})" id="chk_#{type}Zvf__#{evuId}" class="checkbox" />#{requiredMessage}</div></td>';
			// strTemplate += '<td><div class="box"><input type="text" value=""
			// name="#{type}MasterUebergabeblattPV(#{evuId})"
			// id="#{type}MasterUebergabeblattPV__#{evuId}" class="dateDisabled"
			// maxlength="10" disabled="" /><br /><input type="checkbox"
			// name="#{type}IsMasterUebergabeblattPVErforderlich(#{evuId})"
			// id="chk_#{type}MasterUebergabeblattPV__#{evuId}" class="checkbox"
			// />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}UebergabeblattPV(#{evuId})" id="#{type}UebergabeblattPV__#{evuId}" class="dateDisabled" maxlength="10" disabled="" /><br /><input type="checkbox" name="#{type}IsUebergabeblattPVErforderlich(#{evuId})" id="chk_#{type}UebergabeblattPV__#{evuId}" class="checkbox" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}Fplo(#{evuId})" id="#{type}Fplo__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsFploErforderlich(#{evuId})" id="chk_#{type}Fplo__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}EingabeGFD_Z(#{evuId})" id="#{type}EingabeGFD_Z__#{evuId}" class="dateDisabled" maxlength="10" disabled="" /><br /><input type="checkbox" name="#{type}IsEingabeGFD_ZErforderlich(#{evuId})" id="chk_#{type}EingabeGFD_Z__#{evuId}" class="checkbox" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="radio" name="pevuAusfaelleSEV(#{evuId})" value="true" class="checkbox" />Ja</div><div class="box"><input type="radio" name="pevuAusfaelleSEV(#{evuId})" value="false" class="checkbox" />Nein</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}BKonzeptEVU(#{evuId})" id="#{type}BKonzeptEVU__#{evuId}" class="dateDisabled" maxlength="10" disabled="" /><br /><input type="checkbox" name="#{type}IsBKonzeptEVUErforderlich(#{evuId})" id="chk_#{type}BKonzeptEVU__#{evuId}" class="checkbox" />#{requiredMessage}</div></td>';
			strTemplate += '<td><a href="#" onclick="if(confirmLink(this.href, \'#{confirmText}\')) removeEvuRow(\'#{targetId}\', \'#{evuId}\');return false;" class="delete">&nbsp;</a></td>';
			strTemplate += '</tr>';
		}
		break;

	case 'ks', 'KS':
		if (isGEVU) {
			strTemplate = '<tr id="#{type}__#{evuId}" class="#{styleClass}">';
			strTemplate += '<td style="overflow:hidden;max-width:120px;min-width:80px;"><div class="box" style="text-align:left">#{caption}</div></td>';
			strTemplate += '<td><input type="text" value="" name="#{type}StudieGrobkonzept(#{evuId})" id="#{type}StudieGrobkonzept__#{evuId}" class="dateShort" maxlength="10"/></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}ZvfEntwurf(#{evuId})" id="#{type}ZvfEntwurf__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsZvFEntwurfErforderlich(#{evuId})" id="chk_#{type}ZvfEntwurf__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}StellungnahmeEVU(#{evuId})" id="#{type}StellungnahmeEVU__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsStellungnahmeEVUErforderlich(#{evuId})" id="chk_#{type}StellungnahmeEVU__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}ZvF(#{evuId})" id="#{type}Zvf__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsZvFErforderlich(#{evuId})" id="chk_#{type}Zvf__#{evuId}" class="checkbox" titleKey="baumassnahme.termine.required" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}MasterUebergabeblattGV(#{evuId})" id="#{type}MasterUebergabeblattGV__#{evuId}" class="dateDisabled" maxlength="10" disabled="" /><br /><input type="checkbox" name="#{type}IsMasterUebergabeblattGVErforderlich(#{evuId})" id="chk_#{type}MasterUebergabeblattGV__#{evuId}" class="checkbox" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}UebergabeblattGV(#{evuId})" id="#{type}UebergabeblattGV__#{evuId}" class="dateDisabled" maxlength="10" disabled="" /><br /><input type="checkbox" name="#{type}IsUebergabeblattGVErforderlich(#{evuId})" id="chk_#{type}UebergabeblattGV__#{evuId}" class="checkbox" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}Fplo(#{evuId})" id="#{type}Fplo__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsFploErforderlich(#{evuId})" id="chk_#{type}Fplo__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}EingabeGFD_Z(#{evuId})" id="#{type}EingabeGFD_Z__#{evuId}" class="dateDisabled" maxlength="10" disabled="" /><br /><input type="checkbox" name="#{type}IsEingabeGFD_ZErforderlich(#{evuId})" id="chk_#{type}EingabeGFD_Z__#{evuId}" class="checkbox" />#{requiredMessage}</div></td>';
			strTemplate += '<td><a href="#" onclick="if(confirmLink(this.href, \'#{confirmText}\')) removeEvuRow(\'#{targetId}\', \'#{evuId}\');return false;" class="delete">&nbsp;</a></td>';
			strTemplate += '</tr>';
		} else if (isPEVU) {
			strTemplate = '<tr id="#{type}__#{evuId}" class="#{styleClass}">';
			strTemplate += '<td style="overflow:hidden;max-width:120px;min-width:80px;"><div class="box" style="text-align:left">#{caption}</div></td>';
			strTemplate += '<td><input type="text" value="" name="#{type}StudieGrobkonzept(#{evuId})" id="#{type}StudieGrobkonzept__#{evuId}" class="dateShort" maxlength="10"/></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}ZvfEntwurf(#{evuId})" id="#{type}ZvfEntwurf__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsZvfEntwurfErforderlich(#{evuId})" id="chk_#{type}ZvfEntwurf__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}StellungnahmeEVU(#{evuId})" id="#{type}StellungnahmeEVU__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsStellungnahmeEVUErforderlich(#{evuId})" id="chk_#{type}StellungnahmeEVU__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}ZvF(#{evuId})" id="#{type}Zvf__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsZvFErforderlich(#{evuId})" id="chk_#{type}Zvf__#{evuId}" class="checkbox" titleKey="baumassnahme.termine.required" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}MasterUebergabeblattPV(#{evuId})" id="#{type}MasterUebergabeblattPV__#{evuId}" class="dateDisabled" maxlength="10" disabled="" /><br /><input type="checkbox" name="#{type}IsMasterUebergabeblattPVErforderlich(#{evuId})" id="chk_#{type}MasterUebergabeblattPV__#{evuId}" class="checkbox" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}UebergabeblattPV(#{evuId})" id="#{type}UebergabeblattPV__#{evuId}" class="dateShort" maxlength="10" /><br /><input type="checkbox" name="#{type}IsUebergabeblattPVErforderlich(#{evuId})" id="chk_#{type}UebergabeblattPV__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}Fplo(#{evuId})" id="#{type}Fplo__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsFploErforderlich(#{evuId})" id="chk_#{type}Fplo__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}EingabeGFD_Z(#{evuId})" id="#{type}EingabeGFD_Z__#{evuId}" class="dateShort" maxlength="10" /><br /><input type="checkbox" name="#{type}IsEingabeGFD_ZErforderlich(#{evuId})" id="chk_#{type}EingabeGFD_Z__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="radio" name="pevuAusfaelleSEV(#{evuId})" value="true" class="checkbox" />Ja</div><div class="box"><input type="radio" name="pevuAusfaelleSEV(#{evuId})" value="false" class="checkbox" />Nein</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}BKonzeptEVU(#{evuId})" id="#{type}BKonzeptEVU__#{evuId}" class="dateShort" maxlength="10" /><br /><input type="checkbox" name="#{type}IsBKonzeptEVUErforderlich(#{evuId})" id="chk_#{type}BKonzeptEVU__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><a href="#" onclick="if(confirmLink(this.href, \'#{confirmText}\')) removeEvuRow(\'#{targetId}\', \'#{evuId}\');return false;" class="delete">&nbsp;</a></td>';
			strTemplate += '</tr>';
		}
		break;

	case 'qs', 'QS':
		if (isGEVU) {
			strTemplate = '<tr id="#{type}__#{evuId}" class="#{styleClass}">';
			strTemplate += '<td style="overflow:hidden;max-width:120px;min-width:80px;"><div class="box" style="text-align:left">#{caption}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}ZvfEntwurf(#{evuId})" id="#{type}ZvfEntwurf__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsZvFEntwurfErforderlich(#{evuId})" id="chk_#{type}ZvfEntwurf__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}StellungnahmeEVU(#{evuId})" id="#{type}StellungnahmeEVU__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsStellungnahmeEVUErforderlich(#{evuId})" id="chk_#{type}StellungnahmeEVU__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}ZvF(#{evuId})" id="#{type}Zvf__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsZvFErforderlich(#{evuId})" id="chk_#{type}Zvf__#{evuId}" class="checkbox" titleKey="baumassnahme.termine.required" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}MasterUebergabeblattGV(#{evuId})" id="#{type}MasterUebergabeblattGV__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsMasterUebergabeblattGVErforderlich(#{evuId})" id="chk_#{type}MasterUebergabeblattGV__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}UebergabeblattGV(#{evuId})" id="#{type}UebergabeblattGV__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsUebergabeblattGVErforderlich(#{evuId})" id="chk_#{type}UebergabeblattGV__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}Fplo(#{evuId})" id="#{type}Fplo__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsFploErforderlich(#{evuId})" id="chk_#{type}Fplo__#{evuId}" class="checkbox" checked="checked" value="on" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}EingabeGFD_Z(#{evuId})" id="#{type}EingabeGFD_Z__#{evuId}" class="dateShort" maxlength="10" /><br /><input type="checkbox" name="#{type}IsEingabeGFD_ZErforderlich(#{evuId})" id="chk_#{type}EingabeGFD_Z__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><a href="#" onclick="if(confirmLink(this.href, \'#{confirmText}\')) removeEvuRow(\'#{targetId}\', \'#{evuId}\');return false;" class="delete">&nbsp;</a></td>';
			strTemplate += '</tr>';
		} else if (isPEVU) {
			strTemplate = '<tr id="#{type}__#{evuId}" class="#{styleClass}">';
			strTemplate += '<td style="overflow:hidden;max-width:120px;min-width:80px;"><div class="box" style="text-align:left">#{caption}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}ZvfEntwurf(#{evuId})" id="#{type}ZvfEntwurf__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsZvfEntwurfErforderlich(#{evuId})" id="chk_#{type}ZvfEntwurf__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}StellungnahmeEVU(#{evuId})" id="#{type}StellungnahmeEVU__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsStellungnahmeEVUErforderlich(#{evuId})" id="chk_#{type}StellungnahmeEVU__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}ZvF(#{evuId})" id="#{type}Zvf__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsZvFErforderlich(#{evuId})" id="chk_#{type}Zvf__#{evuId}" class="checkbox" titleKey="baumassnahme.termine.required" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}MasterUebergabeblattPV(#{evuId})" id="#{type}MasterUebergabeblattPV__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsMasterUebergabeblattPVErforderlich(#{evuId})" id="chk_#{type}MasterUebergabeblattPV__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}UebergabeblattPV(#{evuId})" id="#{type}UebergabeblattPV__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsUebergabeblattPVErforderlich(#{evuId})" id="chk_#{type}UebergabeblattPV__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}Fplo(#{evuId})" id="#{type}Fplo__#{evuId}" class="dateShort" maxlength="10"/><br /><input type="checkbox" name="#{type}IsFploErforderlich(#{evuId})" id="chk_#{type}Fplo__#{evuId}" class="checkbox" checked="checked" value="on" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}EingabeGFD_Z(#{evuId})" id="#{type}EingabeGFD_Z__#{evuId}" class="dateShort" maxlength="10" /><br /><input type="checkbox" name="#{type}IsEingabeGFD_ZErforderlich(#{evuId})" id="chk_#{type}EingabeGFD_Z__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><div class="box"><input type="radio" name="pevuAusfaelleSEV(#{evuId})" value="true" class="checkbox" />Ja</div><div class="box"><input type="radio" name="pevuAusfaelleSEV(#{evuId})" value="false" class="checkbox" />Nein</div></td>';
			strTemplate += '<td><div class="box"><input type="text" value="" name="#{type}BKonzeptEVU(#{evuId})" id="#{type}BKonzeptEVU__#{evuId}" class="dateShort" maxlength="10" /><br /><input type="checkbox" name="#{type}IsBKonzeptEVUErforderlich(#{evuId})" id="chk_#{type}BKonzeptEVU__#{evuId}" class="checkbox" checked="checked" value="on" />#{requiredMessage}</div></td>';
			strTemplate += '<td><a href="#" onclick="if(confirmLink(this.href, \'#{confirmText}\')) removeEvuRow(\'#{targetId}\', \'#{evuId}\');return false;" class="delete">&nbsp;</a></td>';
			strTemplate += '</tr>';
		}
		break;

	default:
		break;
	}

	var template = new Template(strTemplate);
	var evuType = (isGEVU) ? 'gevu' : 'pevu';
	var values = {
		type :evuType,
		caption :caption,
		evuId :evuId,
		requiredMessage :requiredMsg,
		confirmText :delTitleStr,
		styleClass :styleClass,
		targetId :target
	};
	$(target).insert(template.evaluate(values));

	// Fehlernachricht ausblenden
	$('gevuError').hide();
	$('pevuError').hide();
	
	// Skript für Checkboxen ausführen
	refreshEVUEvents();
}

function removeEvuRow(target, evuId) {
	var table = $(target);
	if (table == null)
		return;

	var rowId;

	if (target.indexOf('gevu') != -1) {
		rowId = 'gevu__' + evuId;

		// Id aus 'linkedEvuList' löschen
		var list = $('gevuLinkedIds');
		list.value = list.value.replace("" + evuId + ",", "");
	} else if (target.indexOf('pevu') != -1) {
		rowId = 'pevu__' + evuId;

		// Id aus 'linkedEvuList' löschen
		var list = $('pevuLinkedIds');
		list.value = list.value.replace("" + evuId + ",", "");
	} else
		return;

	// Tabellenzeile löschen
	$(rowId).remove();

	// Fehlernachricht ausblenden
	$('gevuError').hide();
	$('pevuError').hide();

	// Skript für Checkboxen ausführen
	refreshEVUEvents();
}
