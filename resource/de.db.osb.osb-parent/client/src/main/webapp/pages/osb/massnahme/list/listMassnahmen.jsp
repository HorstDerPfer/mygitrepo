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

<%-- Öffnet Punkt in Startmenü --%>
<script type="text/javascript">
    openMainMenu('navLink_osb-massnahme');
</script>

<div class="textcontent_head"><bean:message key="menu.sperrpausenbedarfe.list" /></div>
<div class="textcontent" style="text-align: center;">

	<jsp:useBean id="urls" class="java.util.HashMap" />

	<div id="sperrpausenliste">
		<display:table id="current" 
			name="massnahmen"
			export="true" 
			requestURI="${requestURI}"
			pagesize="20" 
			sort="list"
			class="colored"
			decorator="db.training.easy.util.displaytag.decorators.AddRowLink">
			
			<%-- URL wird erzeugt, ggf. inkl. sessionId --%>
			<c:set target="${urls}" property="${current.id}" value="#" />
			
			<easy:hasAuthorization model="${current}" authorization="ROLE_MASSNAHME_LESEN, ROLE_MASSNAHME_BEARBEITEN">
				<c:set target="${urls}" property="${current.id}">
					<c:url value="/osb/massnahme/view.do?massnahmeId=${current.id}" />
				</c:set>
			</easy:hasAuthorization>

			<display:column property="massnahmeId" titleKey="baumassnahme.mnid" sortable="false" />
			<display:column property="projektDefinitionDbBez" titleKey="sperrpausenbedarf.bezeichnung" sortable="true" />
			<display:column property="untergewerk" titleKey="sperrpausenbedarf.untergewerk.br" sortable="true" />
			<display:column property="regionalbereich.name" titleKey="baumassnahme.regionalbereich.short" sortProperty="regionalbereich" sortable="true" />
			<display:column style="text-align:right;width:15px;" media="html">
				<easy:hasAuthorization model="${current}" authorization="ROLE_MASSNAHME_BEARBEITEN">
					<html:link action="/osb/massnahme/edit.do?massnahmeId=${current.id}" styleClass="edit">&nbsp;</html:link>
				</easy:hasAuthorization>
			</display:column>

		</display:table>
	</div>
</div>
<div class="buttonBar">
	<html:link action="/osb/massnahme/edit" styleClass="buttonAdd">
		<bean:message key="button.create" />
		<html:param name="massnahmeId" value="0" />
	</html:link>
</div>

<jsp:include page="/pages/main_footer.jsp" />