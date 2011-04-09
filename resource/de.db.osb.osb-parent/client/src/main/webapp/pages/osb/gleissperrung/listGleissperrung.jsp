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
    openMainMenu('navLink_osb_workflow-gleissperrung');

	$j(function() {
		// VZG-Strecke
		$j("#streckeVZG").autocomplete({
			source:
				function(request, response) {
					$j.ajax({
						url:"<c:url value='/AutoCompleteStreckeVzg.view'/>",
						datatype: "html",
						data: {
							<%-- Parameter, die bei AJAX Request übergeben werden --%>
							nummer: request.term
						},
						success: function(data) {
							<%-- Ergebnis des Requests in Autocomplete-Response-Objekt umbauen --%>
							<%-- erwartet: <ul><li>Wert</li>...</ul> --%>
							response($j.map($j("li", data), function(item) {
								var t = $j(item).text();
								return {label: t, value:t};
							}));
							clearBetriebsstellen();
						},
						error: function(){
							clearBetriebsstellen();
						}
					});
				},
			minLength: 2
		});
		// Betriebsstelle-Von
		$j("#betriebsstelleVon").autocomplete({
			source:
				function(request, response) {
					$j.ajax({
						url:"<c:url value='/autocompleteOrtsbezeichnungen.view'/>",
						datatype: "html",
						data: {
							<%-- Parameter, die bei AJAX Request übergeben werden --%>
							keyword: request.term,
							vzgstrecke: $('streckeVZG').value
						},
						success: function(data) {
							<%-- Ergebnis des Requests in Autocomplete-Response-Objekt umbauen --%>
							<%-- erwartet: <ul><li>Wert</li>...</ul> --%>
							response($j.map($j("li", data), function(item) {
								var t = $j(item).text();
								return {label: t, value:t};
							}));
						}
					});
				},
			minLength: 2
		});
		// Betriebsstelle-Bis
		$j("#betriebsstelleBis").autocomplete({
			source:
				function(request, response) {
					$j.ajax({
						url:"<c:url value='/autocompleteOrtsbezeichnungen.view'/>",
						datatype: "html",
						data: {
							<%-- Parameter, die bei AJAX Request übergeben werden --%>
							keyword: request.term,
							vzgstrecke: $('streckeVZG').value
						},
						success: function(data) {
							<%-- Ergebnis des Requests in Autocomplete-Response-Objekt umbauen --%>
							<%-- erwartet: <ul><li>Wert</li>...</ul> --%>
							response($j.map($j("li", data), function(item) {
								var t = $j(item).text();
								return {label: t, value:t};
							}));
						}
					});
				},
			minLength: 2
		});
	});

	function clearBetriebsstellen(){
		if($("betriebsstelleVon").value.length > 0){
			$("betriebsstelleVon").value = "";
			new Effect.Highlight('betriebsstelleVon', { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });
		}

		if($("betriebsstelleBis").value.length > 0){
			$("betriebsstelleBis").value = "";
			new Effect.Highlight('betriebsstelleBis', { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });
		}
	}
</script>

<%-- select filter input form  --%>
<html:form action="/osb/listGleissperrungExecute">
	<div class="textcontent_head"><bean:message key="gleissperrung.titelSuche" /></div>
	<div class="textcontent">
		<div class="textcontent_left">
			<div class="box">
				<div class="label"><label for="regionalbereichId"><bean:message key="sperrpausenbedarf.regionalbereich" /></label></div>
				<div class="input">
					<html:select property="regionalbereichId" styleId="regionalbereich">
						<html:option value="0"><bean:message key="common.all" /></html:option>
		<html:optionsCollection name="regionalbereichListe" value="id"
			label="name" />
	</html:select>
				</div>
			</div>
			<div class="box">
				<div class="label"><label for="streckeVZG"><bean:message key="sperrpausenbedarf.hauptstreckenNummer" /></label></div>
				<div class="input"><html:text property="streckeVZG" styleId="streckeVZG" errorStyle="${errorStyle}" /></div>
			</div>
			<div class="box">
				<div class="label"><label for="datumVon"><bean:message key="common.letzteAenderungNach" /></label></div>
				<div class="input"><html:text property="datumVon" styleId="datumVon" styleClass="date" errorStyle="${errorStyle}" maxlength="10" /><img src="<c:url value='/static/img/calendar.gif' />" id="datumVonButton" alt="Kalender" class="calendar" /></div>
				<html:hidden property="datumBis" />
			</div>
		</div>
		<div class="textcontent_right">
			<div class="box">
				<div class="label"><label for="betriebsstelleVon"><bean:message key="fahrplanregelung.betriebsstelleVon" /></label></div>
				<div class="input"><html:text property="betriebsstelleVon" styleId="betriebsstelleVon" maxlength="30" errorStyle="${errorStyle}" /></div>
			</div>
			<div class="box">
				<div class="label"><label for="betriebsstelleBis"><bean:message key="fahrplanregelung.betriebsstelleBis" /></label></div>
				<div class="input"><html:text property="betriebsstelleBis" styleId="betriebsstelleBis" maxlength="30" errorStyle="${errorStyle}" /></div>
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
		<input type="submit" class="hiddenSubmit" />
		
		<html:link action="/osb/listGleissperrung" styleClass="buttonReload">
			<html:param name="reset" value="true" />
			<bean:message key="button.reset" />
		</html:link>
		<html:link href="#" onclick="$('gleissperrungFilterForm').submit();" styleClass="buttonSearch">
			<bean:message key="button.search" />
		</html:link>
	</div>
</html:form>

<br/>

<div class="textcontent_head"><bean:message key="menu.gleissperrung" /></div>

<div class="textcontent" style="text-align: center;">
	<bean:define id="titleDeleted"><bean:message key="common.deleted" /></bean:define>
	<bean:define id="titleView"><bean:message key="button.view" /></bean:define>
	<bean:define id="titleEdit"><bean:message key="button.edit" /></bean:define>
	<bean:define id="titleDelete"><bean:message key="button.delete" /></bean:define>
	<bean:define id="titleUndelete"><bean:message key="button.undelete" /></bean:define>
	<bean:define id="confirmText" toScope="page"><bean:message key="confirm.gleissperrung.delete" /></bean:define>
	<bean:define id="blueColoredText">color:blue;</bean:define>

	<jsp:useBean id="urls" class="java.util.HashMap" />

	<display:table id="item"
		name="gleissperrungen"
		export="true"
		requestURI="/osb/listGleissperrung.do"
		pagesize="${gleissperrungen.objectsPerPage}"
		sort="external"
		class="colored"
		decorator="db.training.easy.util.displaytag.decorators.AddRowLink">

		<%-- URL wird erzeugt, ggf. inkl. sessionId --%>
		<c:if test="${item != null}">
			<easy:hasAuthorization model="${item}" authorization="ROLE_GLEISSPERRUNG_LESEN">
				<c:set target="${urls}" property="${item.id}">
					<c:url value="/osb/viewGleissperrung.do?gleissperrungId=${item.id}" />
				</c:set>
			</easy:hasAuthorization>
		</c:if>
		
		<%-- Überprüfung, ob Text blau gefärbt werden soll (LastChangeDate innerhalb DatumVon und Datum Bis --%>
		<c:if test="${item.lastChangeDate != null}">
			<bean:define id="lastChange"><fmt:formatDate pattern="yyyy.MM.dd" value="${item.lastChangeDate}" /></bean:define>
		</c:if>
		<c:if test="${item.lastChangeDate == null}">
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
		
		<jsp:directive.include file="listGleissperrungColumnDetails.jsp" />
	
		<display:column style="text-align:right;width:15px;" media="html">
			<easy:hasAuthorization model="${item}" authorization="ROLE_GLEISSPERRUNG_LOESCHEN">
				<c:choose>
					<c:when test="${item.deleted == true}">
				 		<html:link action="/osb/deleteGleissperrung" styleClass="undo" title="${titleUndelete}">
				 			<html:param name="gleissperrungId" value="${item.id}" />
				 			<html:param name="delete" value="false" />
				 			&nbsp;
				 		</html:link>
					</c:when>
					<c:when test="${item.deleted == false}">
				 		<html:link action="/osb/deleteGleissperrung" styleClass="delete" onclick="return confirmLink(this.href, '${confirmText}');" title="${titleDelete}">
				 			<html:param name="gleissperrungId" value="${item.id}" />
				 			<html:param name="delete" value="true" />
				 			&nbsp;
				 		</html:link>
					</c:when>
				</c:choose>
		 	</easy:hasAuthorization>
		</display:column>
	</display:table>
</div>

<c:choose>
	<c:when test="${!empty regionalrechtEnabled && regionalrechtEnabled == true}">
		<authz:authorize ifAnyGranted="ROLE_GLEISSPERRUNG_ANLEGEN_ALLE, ROLE_GLEISSPERRUNG_ANLEGEN_REGIONALBEREICH, ROLE_GLEISSPERRUNG_ANLEGEN_TEMPORAER">
			<div class="buttonBar">
				<html:link action="/osb/editGleissperrung.do" styleClass="buttonAdd">
					<html:param name="gleissperrungId" value="0" />
					<bean:message key="button.create" />
				</html:link>
			</div>
		</authz:authorize>
	</c:when>
	<c:otherwise>
		<authz:authorize ifAnyGranted="ROLE_GLEISSPERRUNG_ANLEGEN_ALLE, ROLE_GLEISSPERRUNG_ANLEGEN_REGIONALBEREICH">
			<div class="buttonBar">
				<html:link action="/osb/editGleissperrung.do" styleClass="buttonAdd">
					<html:param name="gleissperrungId" value="0" />
					<bean:message key="button.create" />
				</html:link>
			</div>
		</authz:authorize>
	</c:otherwise>
</c:choose>

<script type="text/javascript">
	Calendar.setup({ inputField: "datumVon", button: "datumVonButton" });
	//Calendar.setup({ inputField: "datumBis", button: "datumBisButton" });
</script>

<jsp:include page="/pages/main_footer.jsp" />