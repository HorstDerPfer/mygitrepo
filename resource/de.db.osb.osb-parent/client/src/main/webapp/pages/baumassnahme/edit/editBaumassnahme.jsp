<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
<html:xhtml/>

<jsp:include page="../../main_head.jsp"/>
<jsp:include page="../../main_path.jsp"/>
<jsp:include page="../../main_menu.jsp"/>
		
<%-- Öffnet Punkt in Startmenü --%>
<script type="text/javascript">
    openMainMenu('navLink_bob');
</script>

<%--<html:form action="/saveBaumassnahme?sp=true"> --%>
<html:form action="/saveBaumassnahme.do">

	<div class="textcontent_head"><bean:message key="baumassnahme.stammdaten" /></div>
	
	<easy:hasAuthorization model="${baumassnahme}" authorization="ROLE_BAUMASSNAHME_GRUNDDATEN_BEARBEITEN">
		<div class="textcontent" style="border-bottom:0px;">
			<jsp:include page="editBaumassnahmeStammdaten.jsp"/>
		</div>
	</easy:hasAuthorization>
	<easy:hasNotAuthorization model="${baumassnahme}" authorization="ROLE_BAUMASSNAHME_GRUNDDATEN_BEARBEITEN">
		<div class="textcontent" style="border-bottom:0px;">
			<jsp:include page="../view/viewBaumassnahmeStammdaten.jsp"/>
		</div>
	</easy:hasNotAuthorization>
		
	<html:hidden property="id" styleId="id" value="${baumassnahme.id }" />
	<html:hidden property="benchmarkOnly" />
	<html:hidden property="showZuegeZvf" />
	<html:hidden property="showZuegeUeb" />
	
	<div class="buttonBar">
		<%-- SPEICHERN-BUTTON --%>
		<easy:hasAuthorization model="${baumassnahme}" authorization="ROLE_BAUMASSNAHME_GRUNDDATEN_BEARBEITEN,ROLE_BAUMASSNAHME_BENCHMARK_BEARBEITEN, ROLE_FAVORIT_BEARBEITEN">
			<html:link href="#" onclick="$('baumassnahmeForm').submit();" styleClass="buttonSave" styleId="buttonSave"><bean:message key="button.save" /></html:link>
		</easy:hasAuthorization>
		<%-- ZURÜCK-BUTTON --%>
		<jsp:useBean id="backButtonParamMap" class="java.util.HashMap">
			<c:set target="${backButtonParamMap}" property="id" value="${baumassnahme.id}" />
			<c:set target="${backButtonParamMap}" property="tab" value="${tab == null ? 'JBB' : tab}" />
		</jsp:useBean>
		<html:link action="/viewBaumassnahme" name="backButtonParamMap" styleId="backButton" styleClass="buttonBack"><bean:message key="button.back" /></html:link>
		<%-- BBP-MASSNAHME-HINZUFÜGEN-BUTTON --%>
		<easy:hasAuthorization model="${baumassnahme}" authorization="ROLE_BAUMASSNAHME_GRUNDDATEN_BEARBEITEN">
			<html:link action="/xmlImport1" paramId="baumassnahmeId" paramName="baumassnahme" paramProperty="id" styleClass="buttonAdd"><bean:message key="button.bbp.add" /></html:link>
		</easy:hasAuthorization>
	</div>
	
	<br/>
	
	<%-- Karteireiter -----------------------------------------------------------------------------------------------------%>
	<c:set var="backButtonLink" value="viewBaumassnahme.do?id=${baumassnahme.id}"/>
	<input type="hidden" id="tab" name="tab" value="" />
	<easy:hasAuthorization model="${baumassnahme}" authorization="ROLE_BAUMASSNAHME_GRUNDDATEN_BEARBEITEN">
	    <html:link href="#" onclick="javascript:showTabDivBM('JBB');" styleId="tabLinkJBB" styleClass="tab_ina"><bean:message key="baumassnahme.tab.jbb" /></html:link>
    	<html:link href="#" onclick="javascript:showTabDivBM('BBP');" styleId="tabLinkBBP" styleClass="tab_ina"><bean:message key="baumassnahme.tab.bbp" /></html:link>
	    <html:link href="#" onclick="javascript:showTabDivBM('StatusAktivitaeten');" styleId="tabLinkStatusAktivitaeten" styleClass="tab_ina"><bean:message key="baumassnahme.tab.statusaktivitaeten" /></html:link>
	    <html:link href="#" onclick="javascript:showTabDivBM('VerzichtQTrasse');" styleId="tabLinkVerzichtQTrasse" styleClass="tab_ina"><bean:message key="baumassnahme.tab.verzichtqtrasse" /></html:link>
		<html:link href="#" onclick="javascript:showTabDivBM('AbstimmungNachbarbahn');" styleId="tabLinkAbstimmungNachbarbahn" styleClass="tab_ina"><bean:message key="baumassnahme.tab.abstimmungnachbarbahn" /></html:link>
	    <html:link href="#" onclick="javascript:showTabDivBM('Benchmark');" styleId="tabLinkBenchmark" styleClass="tab_ina"><bean:message key="baumassnahme.tab.benchmark" /></html:link>
	    <html:link href="#" onclick="javascript:showTabDivBM('Aenderungsdokumentation');" styleId="tabLinkAenderungsdokumentation" styleClass="tab_ina"><bean:message key="baumassnahme.tab.aenderungsdokumenation" /></html:link>			    
	    <html:link href="#" onclick="javascript:showTabDivBM('Kommentar');" styleId="tabLinkKommentar" styleClass="tab_ina"><bean:message key="baumassnahme.tab.kommentar" /></html:link>
	    <html:link href="#" onclick="javascript:showTabDivBM('EskalationAusfall');" styleId="tabLinkEskalationAusfall" styleClass="tab_ina"><bean:message key="baumassnahme.tab.eskalationausfall" /></html:link>
		<html:link href="#" onclick="javascript:showTabDivBM('Uebergabeblatt');" styleId="tabLinkUebergabeblatt" styleClass="tab_ina"><bean:message key="baumassnahme.tab.uebergabeblatt" /></html:link>
		<html:link href="#" onclick="javascript:showTabDivBM('Zvf');" styleId="tabLinkZvf" styleClass="tab_ina"><bean:message key="baumassnahme.tab.zvf" /></html:link>
		<html:link href="#" onclick="javascript:showTabDivBM('Fahrplan');" styleId="tabLinkFahrplan" styleClass="tab_ina"><bean:message key="baumassnahme.tab.fahrplan" /></html:link>
		<html:link href="#" onclick="javascript:showTabDivBM('Bearbeiter');" styleId="tabLinkBearbeiter" styleClass="tab_ina"><bean:message key="baumassnahme.tab.bearbeiter" /></html:link>
	</easy:hasAuthorization>
	<easy:hasNotAuthorization model="${baumassnahme}" authorization="ROLE_BAUMASSNAHME_GRUNDDATEN_BEARBEITEN">
	    <html:link href="#" onclick="javascript:showTabDivBM('Benchmark');" styleId="tabLinkBenchmark" styleClass="tab_ina"><bean:message key="baumassnahme.tab.benchmark" /></html:link>
	    <html:link href="#" onclick="javascript:showTabDivBM('EskalationAusfall');" styleId="tabLinkEskalationAusfall" styleClass="tab_ina"><bean:message key="baumassnahme.tab.eskalationausfall" /></html:link>
		<html:link href="#" onclick="javascript:showTabDivBM('Uebergabeblatt');" styleId="tabLinkUebergabeblatt" styleClass="tab_ina"><bean:message key="baumassnahme.tab.uebergabeblatt" /></html:link>
		<html:link href="#" onclick="javascript:showTabDivBM('Zvf');" styleId="tabLinkZvf" styleClass="tab_ina"><bean:message key="baumassnahme.tab.zvf" /></html:link>
		<html:link href="#" onclick="javascript:showTabDivBM('Bearbeiter');" styleId="tabLinkBearbeiter" styleClass="tab_ina"><bean:message key="baumassnahme.tab.bearbeiter" /></html:link>
	</easy:hasNotAuthorization>

	<div id="tabDivJBB" style="display:none;">
		<div class="textcontent" id="tabJBBStammdaten">
			<jsp:include page="editJbb.jsp"/>
		</div>
		<div class="buttonBar">
			<html:link href="#" onclick="$('baumassnahmeForm').submit();" styleClass="buttonSave" styleId="buttonSave"><bean:message key="button.save" /></html:link>
			<html:link action="/viewBaumassnahme" paramId="id" paramName="baumassnahme" paramProperty="id" styleClass="buttonBack"><bean:message key="button.back" /></html:link>
		</div>
		
		<br/>
		
		<div class="textcontent_head"><bean:message key="baumassnahme.schnittstellen" /></div>
		<div class="textcontent" id="schnittstellen">
			<fieldset>
				<legend><bean:message key="baumassnahme.termine.bbptitel" /></legend>
			
				<jsp:include page="editSchnittstelleBBP.jsp"/>
			</fieldset>

			<br>

			<fieldset>
				<legend><bean:message key="baumassnahme.termine.pevutitel" /></legend>
				
				<jsp:include page="editSchnittstellePersonenverkehrsEVU.jsp"/>
				<jsp:include page="editSchnittstellePersonenverkehrsEVUeingabe.jsp"/>
			</fieldset>
			
			<br>
			
			<fieldset>
				<legend><bean:message key="baumassnahme.termine.gevutitel" /></legend>
				
				<jsp:include page="editSchnittstelleGueterverkehrsEVU.jsp"/>
				<jsp:include page="editSchnittstelleGueterverkehrsEVUeingabe.jsp"/>
			</fieldset>
		</div>
	</div>
	
	<div class="textcontent" id="tabDivBBP" style="display:none;">
		<jsp:include page="editBBPList.jsp"/>
	</div>

	<div class="textcontent" id="tabDivStatusAktivitaeten" style="display:none;">
		<jsp:include page="editStatusAktivitaeten.jsp"/>
	</div>

	<div class="textcontent" id="tabDivEskalationAusfall" style="display:none;">
		<easy:hasAuthorization model="${baumassnahme}" authorization="ROLE_BAUMASSNAHME_GRUNDDATEN_BEARBEITEN">
			<jsp:include page="editEskalationAusfall.jsp"/>
		</easy:hasAuthorization>
		<easy:hasNotAuthorization model="${baumassnahme}" authorization="ROLE_BAUMASSNAHME_GRUNDDATEN_BEARBEITEN">
			<jsp:include page="../view/viewEskalationAusfall.jsp"/>
		</easy:hasNotAuthorization>
	</div>
			 
	<div class="textcontent" id="tabDivKommentar" style="display:none;">
		<jsp:include page="editKommentar.jsp"/>
	</div>

	<div class="textcontent" id="tabDivAenderungsdokumentation" style="display:none;">
		<jsp:include page="editAenderungsdokumentation.jsp"/>
		<jsp:include page="editAenderungEingabe.jsp"/>
	</div>

	<div class="textcontent" id="tabDivVerzichtQTrasse" style="display:none;">
		<jsp:include page="editVerzichtQTrasse.jsp"/>
	</div>

	<div class="textcontent" id="tabDivBenchmark" style="display:none;">
		<jsp:include page="../view/viewBenchmark.jsp"/>
	</div>
	
	<div class="textcontent" id="tabDivAbstimmungNachbarbahn" style="display:none;">
		<jsp:include page="editAbstimmungNachbarbahn.jsp"/>
	</div>

	<div id="tabDivUebergabeblatt" style="display:none;">
		<logic:empty name="baumassnahme" property="uebergabeblatt">
			<div class="textcontent">
				<p><bean:message key="info.ueb.none" /></p>
			</div>
			<easy:hasAuthorization model="${baumassnahme}" authorization="ROLE_BBZR_ANLEGEN">
				<div class="buttonBar">
					<jsp:useBean id="uebImportParamMap" class="java.util.HashMap">
						<c:set target="${uebImportParamMap}" property="baumassnahmeId" value="${baumassnahme.id}" />
						<c:set target="${uebImportParamMap}" property="type" value="UEB" />
						<c:set target="${uebImportParamMap}" property="zvfId" value="-1" />
					</jsp:useBean>
					<html:link action="/zvfXmlSelectFile" name="uebImportParamMap" styleClass="buttonNext" styleId="buttonAdd" ><bean:message key="button.import" /></html:link>
					<html:link action="/uebergabeblattManuell" styleClass="buttonNext" styleId="buttonAdd" >
						<html:param name="id" value="${baumassnahme.id}" />									
						<bean:message key="button.uebergabeblatt.manuell" />
					</html:link>
				</div>
			</easy:hasAuthorization>
		</logic:empty>
		<logic:notEmpty name="baumassnahme" property="uebergabeblatt">
			<easy:hasAuthorization model="${baumassnahme.uebergabeblatt}" authorization="ROLE_BBZR_BEARBEITEN">
				<jsp:include page="uebergabeblatt/editUebergabeblatt.jsp"/>
			</easy:hasAuthorization>
			<easy:hasNotAuthorization model="${baumassnahme.uebergabeblatt}" authorization="ROLE_BBZR_BEARBEITEN">
				<jsp:include page="../view/uebergabeblatt/viewUebergabeblatt.jsp"/>
			</easy:hasNotAuthorization>
		</logic:notEmpty>
	</div>
	
	<div id="tabDivZvf" style="display:none;">
		<logic:empty name="baumassnahme" property="aktuelleZvf">
			<div class="textcontent">
				<bean:message key="info.zvf.none" />
			</div>
			<easy:hasAuthorization model="${baumassnahme}" authorization="ROLE_BBZR_ANLEGEN">
				<div class="buttonBar">
					<jsp:useBean id="zvfImportParamMap" class="java.util.HashMap">
						<c:set target="${zvfImportParamMap}" property="baumassnahmeId" value="${baumassnahme.id}" />
						<c:set target="${zvfImportParamMap}" property="type" value="ZVF" />
						<c:set target="${zvfImportParamMap}" property="zvfId" value="-1" />
					</jsp:useBean>
					<html:link action="/zvfXmlSelectFile" name="zvfImportParamMap" styleClass="buttonNext" styleId="buttonAdd" ><bean:message key="button.import" /></html:link>
				</div>
			</easy:hasAuthorization>
		</logic:empty>
		<logic:notEmpty name="baumassnahme" property="aktuelleZvf">
			<easy:hasAuthorization model="${baumassnahme.aktuelleZvf}" authorization="ROLE_BBZR_BEARBEITEN">
				<jsp:include page="zvf/editZvf.jsp"/>
			</easy:hasAuthorization>
			<easy:hasNotAuthorization model="${baumassnahme.aktuelleZvf}" authorization="ROLE_BBZR_BEARBEITEN">
				<jsp:include page="../view/zvf/viewZvf.jsp"/>
			</easy:hasNotAuthorization>
		</logic:notEmpty>
	</div>

	<div id="tabDivFahrplan" style="display:none;">
		<div class="textcontent">
			<jsp:include page="../view/viewFahrplan.jsp"/>
			<br>
			<jsp:include page="../view/viewFahrplanDetails.jsp"/>
		</div>
		<div class="buttonBar">
			<html:link action="/importISAZugDaten" styleClass="buttonAdd">
				<html:param name="id" value="${baumassnahme.id }" />
				<html:param name="vorgangsnr" value="${baumassnahme.vorgangsNr }" />
				<html:param name="rbId" value="${baumassnahme.regionalBereichFpl.id }" />
				<bean:message key="button.importISA" />
			</html:link>
		</div>
	</div>
	
	<div class="textcontent" id="tabDivBearbeiter" style="display:none;">
		<easy:hasAuthorization model="${baumassnahme}" authorization="ROLE_FAVORIT_BEARBEITEN_ALLE">
			<jsp:include page="editBearbeiter.jsp" />
		</easy:hasAuthorization>
		<easy:hasNotAuthorization model="${baumassnahme}" authorization="ROLE_FAVORIT_BEARBEITEN_ALLE">
			<jsp:include page="../view/viewBearbeiter.jsp"/>
		</easy:hasNotAuthorization>
	</div>

	<div class="buttonBar">
		<%-- SAVE-BUTTON --%>
		<authz:authorize ifAnyGranted="ROLE_BAUMASSNAHME_GRUNDDATEN_BEARBEITEN_ALLE,ROLE_BAUMASSNAHME_GRUNDDATEN_BEARBEITEN_REGIONALBEREICH,ROLE_BAUMASSNAHME_BENCHMARK_BEARBEITEN_ALLE,ROLE_BAUMASSNAHME_BENCHMARK_BEARBEITEN_REGIONALBEREICH,ROLE_FAVORIT_BEARBEITEN_ALLE,ROLE_FAVORIT_BEARBEITEN_REGIONALBEREICH">
			<html:link href="#" onclick="$('baumassnahmeForm').submit();" styleClass="buttonSave" styleId="buttonSave"><bean:message key="button.save" /></html:link>
		</authz:authorize>
		<%-- ZURÜCK-BUTTON --%>
		<html:link action="/viewBaumassnahme" name="backButtonParamMap" styleId="backButton" styleClass="buttonBack"><bean:message key="button.back" /></html:link>
	</div>
</html:form>

<script src="<c:url value='/static/js/shortcut.js' />" type="text/javascript" ></script>

<script type="text/javascript">
	var focusedElement;
	
	// Aktivierung/Deaktivierung der Quality Gates mit CheckBox
	function toggleTextboxState(item) {
		if(item.id.substring(0, 4) != "chk_")
			return;
	
		var textbox = $(item.id.substring(4));
			
		if(item.checked) {
			textbox.className = 'dateShort';
			textbox.disabled = false;
		} else {
			textbox.className = 'dateShortDisabled';
			textbox.disabled = true;
		}
	}
	
	// Event-Handler hinzufügen, initialisieren
	$$('div#schnittstellen input.checkbox').toArray().each(function(item) {
		item.onclick = function() {
			toggleTextboxState(this);
		};
		toggleTextboxState(item);
	});
	
	function getToday() {
		var now = new Date();
		var days = "" + now.getDate();
		if(days.length == 1) days = "0" + days;
		var month = "" + (now.getMonth()+1);
		if(month.length == 1) month = "0" + month;
		var year = "" + (now.getFullYear());
		year = year.substring(2, 4);
		return days + "." + month + "." + year;
	}
	
	function getNameOfQualityGate(item) {
		if(item == null)
			return null;
		
		var id = item.id;
		if(id == null || id == "")
			return null;
				
		var str = "";
		if(id.indexOf("bbp")==0) str = id.substring(3, id.length);
		else if(id.indexOf("gevu")==0) str = id.substring(4, id.length);
		else if(id.indexOf("pevu")==0) str = id.substring(4, id.length);
		str = str.split("__")[0];
		
		return str;
	}

	// alle Eingabesteuerelemente schreiben ihre ID in 'focusedElement' wenn sie den Eingabefokus erhalten 
	$$('div#schnittstellen input.date', 'div#schnittstellen input.dateShort').toArray().each(function(item) {
		item.onfocus = function() {
			focusedElement = this.id;
		};
	});
	
	// Shortcuts für das DIV #schnittstellen registrieren
	shortcut.add("shift+f11", function() {
	/*
	 * F11 - setzt das Eingabefeld/den Meilenstein auf das aktuelle Datum
	 */
		if(focusedElement == null) return;
		var control = $(focusedElement);
		if(control == null) return;
		
		control.value = getToday();
		new Effect.Highlight(control,  { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });
		
	}, {target:$('schnittstellen'), propagate:false});

	shortcut.add("shift+f12", function() {
	/*
	 * F12 - setzt das Eingabefeld/den Meilenstein, sowie alle gleichen Meilensteine in allen EVU der Baumaßnahme auf das aktuelle Datum
	 */
		if(focusedElement == null) return;
		var control = $(focusedElement);
		if(control == null) return;
		
		var date = getToday();
		
		// alle input.date / input.dateShort für das selbe Quality Gate finden
		var selectedQualityGate = getNameOfQualityGate(control);
		
		var inputArray = $$('div#schnittstellen input.date', 'div#schnittstellen input.dateShort').toArray();
		for(var index = 0; index < inputArray.size(); ++index)
		{
			var item = inputArray[index];
			var itemName = getNameOfQualityGate(item);
			if(itemName != null && itemName == selectedQualityGate)
			{
				if(item == control || item.value == null || item.value == "") {
					new Effect.Highlight(item,  { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });
					item.value = date;
				}
			}
		} 
		
	}, {target:$('schnittstellen'), propagate:false});
	
	function setBackButtonLink(link, tab) {
		var e = $('backButton');
		if(e != null) {
			e.href = link + "&tab=" + tab;
		}
	}

	function loopSelected()
	{
	  var selObj = $('uebEmpfaenger');
	  var empfaengerString = "";
	  var i;
	  for (i=0; i<selObj.options.length; i++) {
	    if (selObj.options[i].selected) {
	    	empfaengerString=empfaengerString + selObj.options[i].value + ",";
	    }
	  }
	  refreshMailEmpfaengerValue('refreshMailEmpfaengerValue.do',empfaengerString,'${baumassnahme.id}');
	}
	
	<easy:hasAuthorization model="${baumassnahme}" authorization="ROLE_BAUMASSNAHME_GRUNDDATEN_BEARBEITEN">
		showTabDivBM("${ ((tab != null) ? tab : 'JBB') }");
		$('streckeBBP').focus();
	</easy:hasAuthorization>
	<easy:hasNotAuthorization model="${baumassnahme}" authorization="ROLE_BAUMASSNAHME_GRUNDDATEN_BEARBEITEN">
		showTabDivBM("${ ((tab != null && (tab == 'EskalationAusfall' || tab == 'Uebergabeblatt' || tab == 'Zvf' || tab == 'Bearbeiter')) ? tab : 'Benchmark') }");
	</easy:hasNotAuthorization>
</script>

<jsp:include page="../../main_footer.jsp"/>