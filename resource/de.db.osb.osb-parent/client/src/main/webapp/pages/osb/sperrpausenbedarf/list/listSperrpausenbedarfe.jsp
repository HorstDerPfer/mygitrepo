<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>
<html:xhtml />

<jsp:include page="/pages/main_head.jsp" />
<jsp:include page="/pages/main_path.jsp" />
<jsp:include page="/pages/main_menu.jsp" />

<%-- Öffnet Punkt in Startmenü --%>
<script type="text/javascript">
    openMainMenu('navLink_osb_workflow-sperrbedarf');
</script>

<script type="text/javascript">
	// jquery pagination der Liste
	$j(function(){
		// dummy function to Fix Safari Browser
		var callback = callback || function() {
		};

		$j('.pagelinks a').livequery('click', function(){
			var self = this;
			jQuery.ajax({
						url: self.href,
						type: 'GET',
						complete: function(res, status) {
							// If successful, inject the HTML into all the matched elements
							if (status == "success") {
								var area = $j('<div />').append(res.responseText.replace(/<script(.|\s)*?\/script>/g, ""))

									// Locate the specified elements
										.find('#sperrpausenliste');
								$j('#sperrpausenliste').html(area.html());
							}

							// Add delay to account for Safari's delay in globalEval
							setTimeout(function() {
								$j(self).each(callback, [res.responseText, status, res]);
							}, 13);
						}
					});

			return false;
		});
	});
</script>

<%-- select filter input form  --%>
<html:form action="/listSperrpausenbedarfe">
	<div class="textcontent_head"><bean:message key="sperrpausenbedarf.titelSuche" /></div>
	<div class="textcontent">
		<div class="textcontent_left">
			<div class="box">
				<div class="label"><label for="regionalbereichId"><bean:message key="sperrpausenbedarf.regionalbereich" /></label></div>
				<div class="input">
					<html:select property="regionalbereichId" styleId="regionalbereich">
						<html:option value="0"><bean:message key="common.all" /></html:option>
						<html:optionsCollection name="regionalbereichListe" value="id" label="name" />
					</html:select>
				</div>
			</div>
			<div class="box">
				<div class="label"><label for="streckeVZG"><bean:message key="sperrpausenbedarf.hauptstreckenNummer" /></label></div>
				<div class="input">
					<html:text property="streckeVZG" styleId="streckeVZG" />
					<img src="<c:url value='./static/img/indicator.gif' />" id="streckeVZGIndicator" style="display: none;" />
					<div id="streckeVZGSelect" class="autocomplete"></div>
				</div>
			</div>
			<div class="box">
				<div class="label"><label for="streckeVZG"><bean:message key="sperrpausenbedarf.sapProjektNummer" /></label></div>
				<div class="input"><html:text property="sapProjektnummer" styleId="sapProjektnummer" styleClass="small" /></div>
			</div>
			<div class="box">
				<div class="label"><label for="datumVon"><bean:message key="common.letzteAenderungNach" /></label></div>
				<div class="input"><html:text property="datumVon" styleId="datumVon" styleClass="date" errorStyle="${errorStyle}" maxlength="10" /><img src="<c:url value='/static/img/calendar.gif' />" id="datumVonButton" alt="Kalender" class="calendar" /></div>
				<html:hidden property="datumBis" />
			</div>
	
			<!--<div class="box">-->
			<!--	<div class="label"><label for="anmelder"><bean:message key="sperrpausenbedarf.anmelder" /></label></div>-->
			<!--	<div class="input"><html:text property="anmelder" styleId="anmelder" maxlength="255"/></div>-->
			<!--</div>-->
		</div>
		<div class="textcontent_right">
			<div class="box">
				<div class="label"><label for="paket"><bean:message key="paket" /></label></div>
				<div class="input">
					<html:text property="paket" styleId="paket" styleClass="small" />
					<img src="<c:url value='./static/img/indicator.gif' />" id="paketIndicator" style="display: none;" />
				</div>
				<div id="paketSelect" class="autocomplete"></div>
			</div>
			<div class="box">
				<div class="label"><label for="gewerk"><bean:message key="sperrpausenbedarf.gewerk" /></label></div>
				<div class="input"><html:text property="gewerk" styleId="streckeBBP" maxlength="255"/></div>
			</div>
			<div class="box">
				<div class="label"><label for="untergewerk"><bean:message key="sperrpausenbedarf.untergewerk" /></label></div>
				<div class="input"><html:text property="untergewerk" styleId="streckeBBP" maxlength="255"/></div>
			</div>
			<div class="box">
				<div class="label"><label for="pageSize"><bean:message key="common.zeilenNr" /></label></div>
				<div class="input">
					<html:select property="pageSize" styleId="pageSize">
						<html:option value="0"><bean:message key="common.select.option" /></html:option>
						<html:option value="10">10</html:option>
						<html:option value="20">20</html:option>
						<html:option value="50">50</html:option>
						<html:option value="100">100</html:option>
					</html:select>
				</div>
			</div>
		</div>
	</div>
	<div class="buttonBar">
		<html:link action="/listSperrpausenbedarfe" styleClass="buttonReload">
			<html:param name="reset" value="true" />
			<bean:message key="button.reset" />
		</html:link>
		<input type="submit" class="hiddenSubmit" />
		<html:link href="javascript:document.getElementById('sperrpausenbedarfFilterForm').submit(); " styleClass="buttonSearch" styleId="buttonSearch">
			<bean:message key="button.search" />
		</html:link>
	</div>
</html:form>

<br/>

<div class="textcontent_head"><bean:message key="menu.sperrpausenbedarfe.list" /> ${order}</div>
<div class="textcontent" style="text-align: center;">

	<bean:define id="titleFixiert"><bean:message key="common.fixiert" /></bean:define>
	<bean:define id="titleView"><bean:message key="button.view" /></bean:define>
	<bean:define id="titleEdit"><bean:message key="button.edit" /></bean:define>
	<bean:define id="blueColoredText">color:blue;</bean:define>
	<jsp:useBean id="urls" class="java.util.HashMap" />
	<!-- listSperrpausenbedarfe.do -->

	<div id="sperrpausenliste">
		<display:table id="currentBaumassnahme" 
			name="baumassnahmen"
			export="true" 
			requestURI="${requestURI}" 
			pagesize="${baumassnahmen.objectsPerPage}" 
			sort="external"
			class="colored"
			decorator="db.training.easy.util.displaytag.decorators.AddRowLink">
			
			<%-- URL wird erzeugt, ggf. inkl. sessionId --%>
			<c:set target="${urls}" property="${currentBaumassnahme.id}" value="#" />
			
			
			<%-- Überprüfung, ob Text blau gefärbt werden soll (LastChangeDate innerhalb DatumVon und Datum Bis --%>
			<c:if test="${currentBaumassnahme.lastChangeDate != null}">
				<bean:define id="lastChange"><fmt:formatDate pattern="yyyy.MM.dd" value="${currentBaumassnahme.lastChangeDate}" /></bean:define>
			</c:if>
			<c:if test="${currentBaumassnahme.lastChangeDate == null}">
				<bean:define id="lastChange">&nbsp;</bean:define>
			</c:if>
			<bean:define id="datumVon">${datumVonToCompare}</bean:define>
			<bean:define id="datumBis">${datumBisToCompare}</bean:define>
					
			<c:choose>
				<c:when test="${lastChange >= datumVon  && lastChange <= datumBis}">
					<bean:define id="blueColoredText">color:blue;</bean:define>
				</c:when>
				<c:otherwise>
					<bean:define id="blueColoredText">&nbsp;</bean:define>
				</c:otherwise>
			</c:choose>
			
			<easy:hasAuthorization model="${currentBaumassnahme}" authorization="ROLE_MASSNAHME_LESEN, ROLE_MASSNAHME_BEARBEITEN">
				<c:set target="${urls}" property="${currentBaumassnahme.id}">
					<c:url value="/osb/viewSperrpausenbedarf.do?sperrpausenbedarfId=${currentBaumassnahme.id}" />
				</c:set>
			</easy:hasAuthorization>
			
			<jsp:directive.include file="listSperrpausenbedarfeColumnDetails.jsp" />
		</display:table>
	</div>
</div>

<c:choose>
	<c:when test="${!empty regionalrechtEnabled && regionalrechtEnabled == true}">
		<authz:authorize ifAnyGranted="ROLE_MASSNAHME_ANLEGEN_ALLE, ROLE_MASSNAHME_ANLEGEN_REGIONALBEREICH, ROLE_MASSNAHME_ANLEGEN_TEMPORAER">
			<div class="buttonBar">
				<html:link action="/osb/editSperrpausenbedarf" styleClass="buttonAdd">
					<html:param name="sperrpausenbedarfId" value="0" />
					<bean:message key="button.create" />
				</html:link>
			</div>
		</authz:authorize>
	</c:when>
	<c:otherwise>
		<authz:authorize ifAnyGranted="ROLE_MASSNAHME_ANLEGEN_ALLE, ROLE_MASSNAHME_ANLEGEN_REGIONALBEREICH">
			<div class="buttonBar">
				<html:link action="/osb/editSperrpausenbedarf" styleClass="buttonAdd">
					<html:param name="sperrpausenbedarfId" value="0" />
					<bean:message key="button.create" />
				</html:link>
			</div>
		</authz:authorize>
	</c:otherwise>
</c:choose>

<c:set var="urlAutocompleteVzgStrecke" scope="request"><c:url value='/AutoCompleteStreckeVzg.view' /></c:set>
<c:set var="urlAutocompletePaket" scope="request"><c:url value='/AutocompletePaket.view' /></c:set>

<script type="text/javascript">
	new Ajax.Autocompleter("streckeVZG", "streckeVZGSelect", "${urlAutocompleteVzgStrecke}", { indicator: 'streckeVZGIndicator', minChars: 1, paramName: 'nummer' });
	new Ajax.Autocompleter("paket", "paketSelect", "${urlAutocompletePaket}", { indicator: 'paketIndicator', minChars: 1, paramName: 'keyword' });
</script>

<script type="text/javascript">
	Calendar.setup({ inputField: "datumVon", button: "datumVonButton" });
	//Calendar.setup({ inputField: "datumBis", button: "datumBisButton" });
</script>

<jsp:include page="/pages/main_footer.jsp" />