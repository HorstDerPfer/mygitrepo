<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>

<display:column style="${blueColoredText}" title="" sortable="true">
	<c:if test="${currentBaumassnahme.genehmiger != null}"><img src="<c:url value='/static/img/icon_lock.gif' />" title="${titleFixiert}" /></c:if>
</display:column>

<display:column style="${blueColoredText}" property="massnahmeId" titleKey="baumassnahme.mnid" sortable="false" />
<display:column style="${blueColoredText}" property="projektDefinitionDbBez" titleKey="sperrpausenbedarf.bezeichnung" sortable="true" />
<display:column style="${blueColoredText}" property="untergewerk" titleKey="sperrpausenbedarf.untergewerk.br" sortable="true" />
<display:column style="${blueColoredText}" property="regionalbereich.name" titleKey="baumassnahme.regionalbereich.short" sortProperty="regionalbereich" sortable="true" />
<%--
<display:column property="buendelStrecke.nummer" titleKey="sperrpausenbedarf.buendelStreckenNummer.short.br" sortable="true" />
<display:column property="anmelder" titleKey="sperrpausenbedarf.anmelder" sortable="true" media="html excel" />
<display:column titleKey="sperrpausenbedarf.baukosten.br" sortable="true" media="html" style="text-align:right;">
	<bean:write name="currentBaumassnahme" property="baukosten2011BhhIem" format="#,##0.000000" />
</display:column>
<display:column property="baukosten2011BhhIem" titleKey="sperrpausenbedarf.baukosten" sortable="false" media="excel" style="text-align:right;" />
<display:column property="sperrpausenBedarfEsp" titleKey="sperrpausenbedarf.sperrpausenbedarfEsp.short.br" sortable="true" media="html excel" style="text-align:right;" />
--%>

<display:column style="text-align:right;width:15px;" media="html">
	<easy:hasAuthorization model="${currentBaumassnahme}" authorization="ROLE_MASSNAHME_LESEN">
		<html:link action="/osb/viewSperrpausenbedarf" styleClass="show" title="${titleView}">
			<html:param name="sperrpausenbedarfId" value="${currentBaumassnahme.id}" />
			&nbsp;
		</html:link>
	</easy:hasAuthorization>
</display:column>

<display:column style="text-align:right;width:15px;" media="html">
	<easy:hasAuthorization model="${currentBaumassnahme}" authorization="ROLE_MASSNAHME_BEARBEITEN">
		<html:link action="/osb/editSperrpausenbedarf" styleClass="edit" title="${titleEdit}">
			<html:param name="sperrpausenbedarfId" value="${currentBaumassnahme.id}" />
			&nbsp;
		</html:link>
	</easy:hasAuthorization>
</display:column>

<!-- --------------------- -->
<!-- nur fuer Excel-Export -->

<display:column property="id" titleKey="sperrpausenbedarf.id" media="excel" />
<display:column property="bahnsteige" titleKey="sperrpausenbedarf.bahnsteige" media="excel" />
<%--
<display:column property="bauLaGeschwindigkeit" titleKey="sperrpausenbedarf.baulageschwindigkeit" media="excel" />
--%>
<display:column property="bauterminEnde" format="{0,date,dd.MM.yyyy}" titleKey="sperrpausenbedarf.bauterminEnde" media="excel" />
<display:column property="bauterminStart" format="{0,date,dd.MM.yyyy}" titleKey="sperrpausenbedarf.bauterminStart" media="excel" />
<display:column property="zeilenNummer" titleKey="sperrpausenbedarf.zeilennummer" media="excel" />
<display:column property="gewerk" titleKey="sperrpausenbedarf.gewerk" media="excel" />
<display:column property="pspElement" titleKey="sperrpausenbedarf.pspelement" media="excel" />
<%--
<display:column property="sapProjektNummer" titleKey="sperrpausenbedarf.sapprojektnummer" media="excel" />
--%>
<display:column property="hauptStrecke.nummer" titleKey="sperrpausenbedarf.hauptstrecke" media="excel" />
<display:column titleKey="sperrpausenbedarf.weiterestrecken" media="excel">
	<logic:iterate id="currentStrecke" name="currentBaumassnahme" property="weitereStrecken" indexId="index">
		${currentStrecke.nummer}
		<%--<c:if test="${(fn:length(currentBaumassnahme.weitereStrecken) > (index+1))}">,</c:if>--%>
	</logic:iterate>
</display:column>

<display:column property="betriebsstelleVon.kuerzel" titleKey="sperrpausenbedarf.betriebsstelle.von" media="excel" />
<display:column property="betriebsstelleBis.kuerzel" titleKey="sperrpausenbedarf.betriebsstelle.bis" media="excel" />
<%--
<display:column property="betriebsstelleVonBuendel.kuerzel" titleKey="sperrpausenbedarf.betriebsstelle.von.buendel" media="excel" />
<display:column property="betriebsstelleBisBuendel.kuerzel" titleKey="sperrpausenbedarf.betriebsstelle.bis.buendel" media="excel" />
--%>
<display:column property="kmVon" titleKey="sperrpausenbedarf.kmVon" media="excel" />
<display:column property="kmBis" titleKey="sperrpausenbedarf.kmBis" media="excel" />
<%--
<display:column property="richtungsKennzahl" titleKey="sperrpausenbedarf.richtungsKennzahl" media="excel" />
--%>
<display:column property="weichenGleisnummerBfGleisen" titleKey="sperrpausenbedarf.weichenGleisnummerBfGleisen" media="excel" />
<display:column property="weichenbauform" titleKey="sperrpausenbedarf.weichenbauform" media="excel" />
<display:column property="umbaulaenge" titleKey="sperrpausenbedarf.umbaulaenge" media="excel" />
<display:column property="einbauPss" titleKey="sperrpausenbedarf.einbauPss" media="excel" />
<display:column property="notwendigeLaengePss" titleKey="sperrpausenbedarf.notwendigeLaengePss" media="excel" />
<display:column property="tiefentwaesserungLage" titleKey="sperrpausenbedarf.tiefentwaesserung.lage" media="excel" />
<display:column property="oberleitungsAnpassung" titleKey="sperrpausenbedarf.oberleitungsanpassung" media="excel" />
<display:column property="kabelkanal" titleKey="sperrpausenbedarf.kabelkanal" media="excel" />
<display:column property="lst" titleKey="sperrpausenbedarf.lst" media="excel" />
<display:column property="bauverfahren" titleKey="sperrpausenbedarf.bauverfahren" media="excel" />
<display:column property="geplanteNennleistung" titleKey="sperrpausenbedarf.geplanteNennleistung" media="excel" />
<%--
<display:column property="sperrpausenBedarfTotalsperung" titleKey="sperrpausenbedarf.sperrpausenbedarf.totalsperung" media="excel" />
<display:column property="typSicherungsleistung" titleKey="sperrpausenbedarf.typsicherungsleistung" media="excel" />
<display:column property="sperrpausenBedarfEsp" titleKey="sperrpausenbedarf.sperrpausenbedarf.esp" media="excel" />
--%>
<display:column property="paket.paketId" titleKey="paket" media="excel" />
<display:column property="technischerPlatz" titleKey="sperrpausenbedarf.technischerplatz" media="excel" />
<display:column property="kommentar" titleKey="sperrpausenbedarf.kommentar" media="excel" />
<display:column property="massnahmeBehandlung" titleKey="sperrpausenbedarf.massnahme.behandlung" media="excel" />
<display:column property="urspruenglichesPlanungsjahr" titleKey="sperrpausenbedarf.planungsjahr.urspruenglich" media="excel" />
<display:column property="geaendertesPlanungsjahr" titleKey="sperrpausenbedarf.planungsjahr.geaendert" media="excel" />
<%--
<display:column property="buendel" titleKey="sperrpausenbedarf.buendel" media="excel" />
<display:column titleKey="sperrpausenbedarf.buendel" media="excel">
	<logic:iterate id="currentBuendel" name="currentBaumassnahme" property="buendel" indexId="index">
		${currentBuendel.id}
		<c:if test="${(fn:length(currentBaumassnahme.weitereStrecken) > (index+1))}">,</c:if>
	</logic:iterate>
</display:column>
--%>
<display:column property="kommentarlNPP1" titleKey="sperrpausenbedarf.kommentar.lnpp1" media="excel" />

