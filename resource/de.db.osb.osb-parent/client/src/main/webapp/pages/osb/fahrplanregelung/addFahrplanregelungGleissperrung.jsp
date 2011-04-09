<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>
<html:xhtml />

<jsp:include page="/pages/main_head.jsp" />
<jsp:include page="/pages/main_path.jsp" />
<jsp:include page="/pages/main_menu.jsp" />

<%-- Öffnet Punkt in Startmenü --%>
<script type="text/javascript">
    openMainMenu('navLink_osb_workflow-fahrplanregelung');

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
<html:form action="/osb/fahrplanregelung/gleissperrung/edit">
	<div class="textcontent_head">
		<bean:message key="fahrplanregelung" />: ${fahrplanregelung.name}
	</div>
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
				<div class="input"><html:text property="streckeVZG" styleId="streckeVZG" errorStyle="${errorStyle}" /></div>
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
		<html:link action="/osb/fahrplanregelung/edit" styleClass="buttonBack">
			<bean:message key="button.back" />
			<html:param name="fahrplanregelungId" value="${fahrplanregelung.id}"></html:param>
		</html:link>
		<html:link action="/osb/fahrplanregelung/gleissperrung/edit" styleClass="buttonReload">
			<html:param name="reset" value="true" />
			<html:param name="fahrplanregelungId" value="${fahrplanregelung.id}" />
			<bean:message key="button.reset" />
		</html:link>
		<html:link href="#" onclick="$('fahrplanregelungGleissperrungFilterForm').submit();" styleClass="buttonSearch">
			<bean:message key="button.search" />
		</html:link>
	</div>
</html:form>

<html:form action="/osb/fahrplanregelung/gleissperrung/save" >
	<html:hidden property="fahrplanregelungId" value="${fahrplanregelung.id}"></html:hidden>
	<br />
	<div class="textcontent_head">
		<bean:message key="fahrplanregelung.gleissperrung.add" />
	</div>
	<div class="textcontent" style="text-align: center;">
		<jsp:useBean id="urls" class="java.util.HashMap" />
		
		<display:table id="item"
			name="${gleissperrungen}"
			requestURI="/osb/fahrplanregelung/gleissperrung/edit.do"
			pagesize="20"
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
			
			<jsp:directive.include file="../gleissperrung/listGleissperrungColumnDetails.jsp" />
		
			<display:column media="html" sortable="false">
				<easy:hasAuthorization model="${item}" authorization="ROLE_FAHRPLANREGELUNG_GLEISSPERRUNG_ZUORDNEN">
					<c:if test="${item != null && item.fahrplanregelung == null}">
						<html:multibox property="gleissperrungenIds" value="${item.id}" styleClass="checkbox" styleId="checkbox_${item.id}" />
					</c:if>
				</easy:hasAuthorization>
			</display:column>
		</display:table>
		
	</div>

	<div class="buttonBar">
		<easy:hasAuthorization model="${fahrplanregelung}" authorization="ROLE_FAHRPLANREGELUNG_GLEISSPERRUNG_ZUORDNEN">
			<html:link href="#" onclick="$('fahrplanregelungGleissperrungSaveForm').submit();" styleClass="buttonPlus">
				<bean:message key="button.create" />
			</html:link>
		</easy:hasAuthorization>
	</div>
</html:form>



<jsp:include page="/pages/main_footer.jsp" />