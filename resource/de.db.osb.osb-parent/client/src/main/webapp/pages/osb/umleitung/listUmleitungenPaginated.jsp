<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>
<html:xhtml />

<jsp:include page="/pages/main_head.jsp" />
<jsp:include page="/pages/main_path.jsp" />
<jsp:include page="/pages/main_menu.jsp" />
	
<script type="text/javascript">
	openMainMenu('navLink_osb_workflow-umleitung');
</script>
			
<html:form action="/listUmleitungenSearch" method="post">
	<div class="textcontent_head"><bean:message key="menu.umleitungen.list" /></div>
	<div class="textcontent">
		<div class="textcontent_left">
			<div class="box">
				<div class="label"><label for="name"><bean:message key="umleitung.titel.short" /></label></div>
				<div class="input"><html:text property="name" styleId="name" /></div>
			</div>
			<div class="box">
				<div class="label">
					<label for="vzgStreckenNummer"><bean:message key="umleitung.vzgStreckenummer" /></label>
					<img src="<c:url value='/static/img/indicator.gif' />" id="streckeVzgIndicator" style="display:none;" />
				</div>
				<div class="input">
					<html:text property="vzgStreckenNummer" maxlength="4" styleClass="short" styleId="vzgStreckenNummer" />
					<div id="streckeVzgSelect" class="autocomplete"></div>
					<script type="text/javascript">
						var urlAutocomplete = "<c:url value='/AutoCompleteStreckeVzg.view'/>";
						new Ajax.Autocompleter("vzgStreckenNummer", "streckeVzgSelect", urlAutocomplete, { indicator: 'streckeVzgIndicator', minChars: 1, paramName: 'nummer' });
					</script>
				</div>
			</div>
		</div>
		
		<div class="textcontent_right">
			<div class="box">
				<div class="label">
					<label for="betriebsStelle"><bean:message key="umleitung.betriebsstelle" /></label>
					<img src="<c:url value='./static/img/indicator.gif' />" id="betriebsStelleIndicator" style="display: none;" />
				</div>
				<div class="input">
					<html:text property="betriebsStelle" styleId="betriebsStelle" maxlength="30" errorStyle="${errorStyle}" />
					<div id="betriebsStelleSelect" class="autocomplete"></div>
				</div>
			</div>
			
			<c:set var="urlAutocompleteBetriebsstelleWithID" scope="request"><c:url value='/autocompleteBetriebstelleWithID.view' /></c:set>
			<c:set var="urlAutocompleteVzgStrecke" scope="request"><c:url value='/AutoCompleteStreckeVzg.view' /></c:set>

			<script type="text/javascript">
				new Ajax.Autocompleter("betriebsStelle", "betriebsStelleSelect", "${urlAutocompleteBetriebsstelleWithID}", { indicator: 'betriebsStelleIndicator', minChars: 2, paramName: 'keyword' });
			</script>
			
			<div class="box">
				<div class="label"><label for="regionalbereichId"><bean:message key="buendel.regionalbereich" /></label></div>
				<div class="input">
					<html:select property="regionalbereichId" styleId="regionalbereichId">
						<html:option value="0"><bean:message key="common.select.option" /></html:option>
						<html:optionsCollection name="regionalbereichListe" value="id" label="name" />
					</html:select>
				</div>
			</div>
			
			<div class="box">
				<div class="label"><label for="pageSize"><bean:message key="common.zeilenNr" /></label></div>
				<div class="input">
					<html:select property="pageSize" styleId="pageSize">
						<html:option value="10">10</html:option>
						<html:option value="20">20</html:option>
						<html:option value="50">50</html:option>
						<html:option value="100">100</html:option>
					</html:select>
				</div>
			</div>
		</div>
		<input type="submit" class="hiddenSubmit" />
	</div>
	<div class="buttonBar">
		<html:link action="/listUmleitungenSearch" styleClass="buttonReload">
			<html:param name="reset">true</html:param>
			<bean:message key="button.reset" />
		</html:link>
		<authz:authorize ifAnyGranted="ROLE_UMLEITUNG_LESEN_ALLE, ROLE_UMLEITUNG_LESEN_REGIONALBEREICH">
			<html:link href="javascript:$('umleitungSearchForm').submit();" styleClass="buttonSearch"><bean:message key="button.search" /></html:link>
		</authz:authorize>
	</div>
	
	<br/>
	
	<div class="textcontent center">
		<bean:define id="titleDeleted"><bean:message key="common.deleted" /></bean:define>
		<bean:define id="titleView"><bean:message key="button.view" /></bean:define>
		<bean:define id="titleEdit"><bean:message key="button.edit" /></bean:define>
		<bean:define id="titleDelete"><bean:message key="button.delete" /></bean:define>
		<bean:define id="titleUndelete"><bean:message key="button.undelete" /></bean:define>
		<bean:define id="confirmText" toScope="page"><bean:message key="confirm.umleitungsweg.remove" /></bean:define>

		<jsp:useBean id="urls" class="java.util.HashMap"/>

		<display:table id="currentUmleitung"
			name="umleitungenList" 
			requestURI="/listUmleitungenSearch.do"
			pagesize="${umleitungenList.objectsPerPage}"
			sort="external"
			export="true"
			class="colored"
			decorator="db.training.easy.util.displaytag.decorators.AddRowLink">
		
			<%-- URL wird erzeugt, ggf. inkl. sessionId --%>
			<easy:hasAuthorization model="${currentUmleitung}" authorization="ROLE_UMLEITUNG_LESEN">
				<c:set target="${urls}" property="${currentUmleitung.id}">
					<c:url value="viewUmleitung.do?umleitungId=${currentUmleitung.id}" />
				</c:set>
			</easy:hasAuthorization>
					 
			<jsp:directive.include file="columnsUmleitungenPaginated.jsp" />
		
			<display:setProperty name="basic.empty.showtable" value="true" />
			<display:setProperty name="paging.banner.item_name"><bean:message key="menu.osb.umleitung" /></display:setProperty>
			<display:setProperty name="paging.banner.items_name"><bean:message key="menu.osb.umleitungen" /></display:setProperty>
		</display:table>
	</div>
	<div class="buttonBar">
		<authz:authorize ifAnyGranted="ROLE_UMLEITUNG_ANLEGEN_ALLE, ROLE_UMLEITUNG_ANLEGEN_REGIONALBEREICH">
			<html:link action="/editUmleitung.do?umleitungId=0" styleClass="buttonAdd"><bean:message key="button.umleitung.add" /></html:link>
		</authz:authorize>
	</div>
</html:form>

<jsp:include page="/pages/main_footer.jsp"/>