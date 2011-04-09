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
	openMainMenu('navLink_osb_workflow-buendel');
</script>

<div class="textcontent_head">
	<bean:message key="menu.fahrplanregelungenToBuendel.add" />
</div>

<html:form action="/addFahrplanregelungenToBuendel?buendelId=${buendel.id}">
	<div class="textcontent">
		<div class="textcontent_left">
			<div class="box">
				<div class="label"><label for="name"><bean:message key="fahrplanregelung.name" /></label></div>
				<div class="input"><html:text property="name" styleId="name" /></div>
			</div>
			<div class="box">
				<div class="label"><label for="nummer"><bean:message key="fahrplanregelung.nummer" /></label></div>
				<div class="input"><html:text property="nummer" styleId="nummer" /></div>
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
		</div>

		<input type="submit" class="hiddenSubmit" />					
	</div>
	<div class="buttonBar">
		<html:link action="/addFahrplanregelungenToBuendel" styleClass="buttonReload">
			<html:param name="buendelId" value="${buendel.id}" />
			<html:param name="reset" value="true" />
			<bean:message key="button.reset" />
		</html:link>
		<html:link href="#" onclick="$('fahrplanregelungSearchForm').submit();" styleClass="buttonSearch">
			<bean:message key="button.search" />
		</html:link>
	</div>
</html:form>

<br />

<div class="textcontent" style="text-align: center;">
	<jsp:useBean id="urls" class="java.util.HashMap" />
	
	<display:table id="currentFahrplanregelung" 
		name="fahrplanregelungenList" 
		export="false"
		requestURI="${requestURI}" 
		pagesize="${pageSize}" 
		sort="list" 
		class="colored"
		decorator="db.training.easy.util.displaytag.decorators.AddRowLink">
	
		<display:column property="fahrplanregelungId" titleKey="fahrplanregelung.nummer" sortable="true" />
		<display:column property="name" titleKey="fahrplanregelung.name" sortable="true" />
		<display:column property="betriebsstelleVon.caption" titleKey="fahrplanregelung.betriebsstelleVon" sortable="true" />
		<display:column property="betriebsstelleBis.caption" titleKey="fahrplanregelung.betriebsstelleBis" sortable="true" />
		<display:column style="text-align:right;width:15px;" media="html">
			<html:link action="/addFahrplanregelungToBuendel" styleClass="plus">
				<html:param name="buendelId" value="${buendel.id}" />
				<html:param name="fahrplanregelungId" value="${currentFahrplanregelung.id}" />
				&nbsp;
			</html:link>
		</display:column>
	</display:table>
</div>
<div class="buttonBar">
	<html:link action="/osb/editBuendel" styleClass="buttonBack">
		<html:param name="buendelId" value="${buendel.id}" />
		<bean:message key="button.back" />
	</html:link>
</div>


<jsp:include page="/pages/main_footer.jsp" />