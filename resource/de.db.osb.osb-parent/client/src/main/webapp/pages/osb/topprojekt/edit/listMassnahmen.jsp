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
<%@ taglib uri="/META-INF/util.tld" prefix="util"%>
<html:xhtml />

<jsp:include page="/pages/main_head.jsp" />
<jsp:include page="/pages/main_path.jsp" />
<jsp:include page="/pages/main_menu.jsp" />
			
<%-- Öffnet Punkt in Startmenü --%>
<script type="text/javascript">
    openMainMenu('navLink_osb_workflow-topprojekt');
</script>

<html:form action="/osb/topProjekt/massnahmen/edit.do?topProjektId=${topProjekt.id}">
	<div class="textcontent_head"><bean:message key="sperrpausenbedarf.titelSuche" /></div>
	<%-- select filter input form  --%>
	<div class="textcontent">
		<div class="textcontent_left">
			<div class="box">
				<div class="label">
					<label for="regionalbereichId"><bean:message key="sperrpausenbedarf.regionalbereich" /></label>
				</div>
				<div class="input">
					<html:select property="regionalbereichId" styleId="regionalbereich">
						<html:option value="0">
							<bean:message key="common.all" />
						</html:option>
						<html:optionsCollection name="regionalbereichListe" value="id"
							label="name" />
					</html:select>
				</div>
			</div>
			<div class="box">
				<div class="label">
					<label for="streckeVZG">
						<bean:message key="sperrpausenbedarf.hauptstreckenNummer" />
					</label>
					<img src="<c:url value='/static/img/indicator.gif' />" id="streckeVZGIndicator" style="display: none;" />
				</div>
				<div class="input">
					<html:text property="streckeVZG" styleId="streckeVZG" />
				</div>
				<div id="streckeVZGSelect" class="autocomplete"></div>
			</div>
	
			<div class="box">
				<div class="label">
					<label for="streckeVZG">
						<bean:message key="sperrpausenbedarf.sapProjektNummer" />
					</label>
				</div>
				<div class="input">
					<html:text property="sapProjektnummer" styleId="sapProjektnummer" />
				</div>
			</div>
		</div>
		<div class="textcontent_right">
			<div class="box">
				<div class="label"><label for="paket"><bean:message key="paket" /></label></div>
				<div class="input">
					<html:text property="paket" styleId="paket" styleClass="small" />
					<img src="<c:url value='/static/img/indicator.gif' />" id="paketIndicator" style="display: none;" />
				</div>
				<div id="paketSelect" class="autocomplete">
				</div>
			</div>
			<div class="box">
				<div class="label"><label for="gewerk"><bean:message key="sperrpausenbedarf.gewerk" /></label></div>
				<div class="input"><html:text property="gewerk" styleId="streckeBBP" maxlength="255"/></div>
			</div>
			<div class="box">
				<div class="label"><label for="untergewerk"><bean:message key="sperrpausenbedarf.untergewerk" /></label></div>
				<div class="input"><html:text property="untergewerk" styleId="streckeBBP" maxlength="255"/></div>
			</div>
		</div>
	</div>
	<div class="buttonBar">
		<input type="submit" class="hiddenSubmit" />
		<html:link href="javascript:document.getElementById('sperrpausenbedarfFilterForm').submit(); "
			styleClass="buttonSearch" styleId="buttonSearch">
			<bean:message key="button.search" />
		</html:link>
	</div>
</html:form>

<div class="textcontent_head" style="margin-top: 30px;"><bean:message key="topprojekt.sperrpausenbedarf.add" /></div>
<div class="textcontent" style="text-align: center;">
	<bean:define id="titleFixiert"><bean:message key="common.fixiert" /></bean:define>

	<jsp:useBean id="urls" class="java.util.HashMap" />

	<display:table id="currentMassnahme" 
		name="massnahmen" 
		export="false"
		requestURI="${requestURI}" 
		pagesize="20" 
		sort="list" 
		class="colored"
		decorator="db.training.easy.util.displaytag.decorators.AddRowLink">

		<jsp:directive.include file="../../sperrpausenbedarf/list/listSperrpausenbedarfeColumnDetails.jsp" />
		
		<display:column style="text-align:right;width:15px;" media="html">
			<c:if test="${not util:contains(topProjekt.massnahmen, currentMassnahme)}">
				<easy:hasAuthorization model="${currentMassnahme}" authorization="ROLE_MASSNAHME_BEARBEITEN">
			 		<html:link action="/osb/topProjekt/massnahmen/save" styleClass="plus">
			 			<html:param name="method" value="attach" />
			 			<html:param name="topProjektId" value="${topProjekt.id}" />
			 			<html:param name="massnahmeId" value="${currentMassnahme.id}" />
			 			&nbsp;
			 		</html:link>
			 	</easy:hasAuthorization>
		 	</c:if>
		</display:column>
	
		<display:column style="text-align:right;width:15px;" media="html">
			<c:if test="${util:contains(topProjekt.massnahmen, currentMassnahme)}">
				<easy:hasAuthorization model="${currentMassnahme}" authorization="ROLE_TOPPROJEKT_BEARBEITEN">
					<html:link action="/osb/topProjekt/massnahmen/save" styleClass="minus">
			 			<html:param name="method" value="detach" />
			 			<html:param name="topProjektId" value="${topProjekt.id}" />
			 			<html:param name="massnahmeId" value="${currentMassnahme.id}" />
			 			&nbsp;
			 		</html:link>
			 	</easy:hasAuthorization>
			 </c:if>
		</display:column>
	</display:table>
</div>
<div class="buttonBar">
	<html:link action="/osb/editTopProjekt" styleClass="buttonBack">
		<html:param name="topProjektId" value="${topProjekt.id}" />
		<bean:message key="button.back" />
	</html:link>
</div>

<script type="text/javascript">
	new Ajax.Autocompleter("streckeVZG", "streckeVZGSelect", "<c:url value='/AutoCompleteStreckeVzg.view' />", { indicator: 'streckeVZGIndicator', minChars: 1, paramName: 'nummer' });
	new Ajax.Autocompleter("paket", "paketSelect", "<c:url value='/AutocompletePaket.view' />", { indicator: 'paketIndicator', minChars: 1, paramName: 'keyword' });
</script>

<jsp:include page="/pages/main_footer.jsp" />