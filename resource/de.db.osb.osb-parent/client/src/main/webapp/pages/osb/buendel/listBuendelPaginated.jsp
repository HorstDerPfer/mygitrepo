<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>
<html:xhtml />

<jsp:include page="/pages/main_head.jsp" />
<jsp:include page="/pages/main_path.jsp" />
<jsp:include page="/pages/main_menu.jsp" />

<%-- Öffnet Punkt in Startmenü --%>
<script type="text/javascript">
   openMainMenu('navLink_osb_workflow-buendel');
</script>

<html:form action="/osb/listBuendelSearch" method="post">
	<div class="textcontent_head"><bean:message key="menu.buendel.list" /></div>
	<div class="textcontent">
		<div class="textcontent_left">
			<div class="box">
				<div class="label"><label for="buendelKennung"><bean:message key="buendel.id" /></label></div>
				<div class="input"><html:text property="buendelKennung" styleId="buendelKennung" maxlength="45" /></div>
			</div>
			<div class="box">
				<div class="label"><label for="buendelName"><bean:message key="buendel.name" /></label></div>
				<div class="input"><html:text property="searchBuendelName" styleId="searchBuendelName" maxlength="45" /></div>
			</div>
			<div class="box">
				<div class="label">
					<label><bean:message key="buendel.hauptStrecke" /></label>
					<img src="<c:url value='/static/img/indicator.gif' />" id="streckeVzgIndicator" style="display:none;" />
				</div>
				<div class="input">
					<html:text property="hauptStreckeNummer" maxlength="4" styleClass="short" styleId="hauptStreckeNummer" />
					<div id="streckeVzgSelect" class="autocomplete"></div>
					
					<script type="text/javascript">
						var urlAutocomplete = "<c:url value='/AutoCompleteStreckeVzg.view'/>";
						new Ajax.Autocompleter("hauptStreckeNummer", "streckeVzgSelect", urlAutocomplete, { indicator: 'streckeVzgIndicator', minChars: 1, paramName: 'nummer' });
					</script>
				</div>
			</div>
			<div class="box">
				<div class="label">
					<label for="betriebsstelleVon"><bean:message key="fahrplanregelung.betriebsstelleVon" /></label>
					<img src="<c:url value='./static/img/indicator.gif' />" id="betriebsstelleVonIndicator" style="display:none;" />
				</div>
				<div class="input">
					<html:text property="betriebsstelleVon" styleId="betriebsstelleVon" maxlength="30" errorStyle="${errorStyle}" />
					<div id="betriebsstelleVonSelect" class="autocomplete"></div>
				</div>
			</div>
			<div class="box">
				<div class="label">
					<label for="betriebsstelleBis"><bean:message key="fahrplanregelung.betriebsstelleBis" /></label>
					<img src="<c:url value='./static/img/indicator.gif' />" id="betriebsstelleBisIndicator" style="display:none;" />
				</div>
				<div class="input">
					<html:text property="betriebsstelleBis" styleId="betriebsstelleBis" maxlength="30" errorStyle="${errorStyle}" />
					<div id="betriebsstelleBisSelect" class="autocomplete"></div>
				</div>
			</div>
		</div>
		
		<div class="textcontent_right">
			<div class="box">
				<div class="label"><label for="regionalbereich"><bean:message key="buendel.regionalbereich" /></label></div>
				<div class="input">
					<html:select property="regionalbereichId" styleId="art">
						<html:option value="0"><bean:message key="common.select.option" /></html:option>
						<html:optionsCollection name="regionalbereichListe" value="id" label="name" />
					</html:select>
				</div>
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
			<div class="box">
				<div class="label"><label for="datumVon"><bean:message key="common.letzteAenderungNach" /></label></div>
				<div class="input"><html:text property="datumVon" styleId="datumVon" styleClass="date" errorStyle="${errorStyle}" maxlength="10" /><img src="<c:url value='/static/img/calendar.gif' />" id="datumVonButton" alt="Kalender" class="calendar" /></div>
				<html:hidden property="datumBis" />
			</div>			
		</div>
	</div>
	
	<div class="buttonBar">
		<input type="submit" class="hiddenSubmit" />					
		<html:link action="/osb/listBuendelSearch" styleClass="buttonReload" style="margin:0px;">
			<html:param name="reset">true</html:param>
			<bean:message key="button.reset" />
		</html:link>
		<html:link href="javascript:document.getElementById('buendelSearchForm').submit();" styleClass="buttonSearch"><bean:message key="button.search" /></html:link>
	</div>
</html:form>

<logic:present name="buendelList">
	<br/>
	
	<div class="textcontent center">
		<bean:define id="titleFixiert"><bean:message key="common.fixiert" /></bean:define>
		<bean:define id="titleDeleted"><bean:message key="common.deleted" /></bean:define>
		<bean:define id="titleView"><bean:message key="button.view" /></bean:define>
		<bean:define id="titleEdit"><bean:message key="button.edit" /></bean:define>
		<bean:define id="titleDelete"><bean:message key="button.delete" /></bean:define>
		<bean:define id="titleUndelete"><bean:message key="button.undelete" /></bean:define>
		<bean:define id="confirmText" toScope="page"><bean:message key="confirm.buendel.delete" /></bean:define>
		<bean:define id="blueColoredText">color:blue;</bean:define>
		

		<jsp:useBean id="urls" class="java.util.HashMap"/>
		
		<%-- requestURI="${currentAction}" --%>
		
		<display:table 
		 	id="currentBuendel"
		 	name="buendelList" 
		 	requestURI="/osb/listBuendel.do"
		 	pagesize="${buendelList.objectsPerPage}"
		 	sort="external"
		 	defaultsort="1"
		 	class="colored"
		 	export="true"
			decorator="db.training.easy.util.displaytag.decorators.AddRowLink">

			<%-- URL wird erzeugt, ggf. inkl. sessionId --%>
			<c:set target="${urls}" property="${currentBuendel.id}" value="#" />
			
			<%-- Überprüfung, ob Text blau gefärbt werden soll (LastChangeDate innerhalb DatumVon und Datum Bis --%>
			<c:if test="${currentBuendel.lastChangeDate != null}">
				<bean:define id="lastChange"><fmt:formatDate pattern="yyyy.MM.dd" value="${currentBuendel.lastChangeDate}" /></bean:define>
			</c:if>
			<c:if test="${currentBuendel.lastChangeDate == null}">
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
			
			<easy:hasAuthorization model="${currentBuendel}" authorization="ROLE_BUENDEL_LESEN">
				<c:set target="${urls}" property="${currentBuendel.id}">
					<c:url value="/osb/viewBuendel.do?buendelId=${currentBuendel.id}" />
				</c:set>
			</easy:hasAuthorization>

			<jsp:directive.include file="columnsBuendelPaginated.jsp" />

			<display:setProperty name="basic.empty.showtable" value="true" />
		</display:table>
	</div>
</logic:present>

<c:set var="urlAutocomplete" scope="request"><c:url value='/autocompleteOrtsbezeichnungen.view'/></c:set>

<script type="text/javascript">
	new Ajax.Autocompleter("betriebsstelleVon", "betriebsstelleVonSelect", "${urlAutocomplete}", { indicator: 'betriebsstelleVonIndicator', minChars: 2, paramName: 'keyword' });
	new Ajax.Autocompleter("betriebsstelleBis", "betriebsstelleBisSelect", "${urlAutocomplete}", { indicator: 'betriebsstelleBisIndicator', minChars: 2, paramName: 'keyword' });					  
</script>	

<script type="text/javascript">
	Calendar.setup({ inputField: "datumVon", button: "datumVonButton" });
	//Calendar.setup({ inputField: "datumBis", button: "datumBisButton" });
</script>

<jsp:include page="/pages/main_footer.jsp"/>