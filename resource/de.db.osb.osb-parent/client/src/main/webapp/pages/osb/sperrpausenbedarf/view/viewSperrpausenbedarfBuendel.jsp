<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>

<bean:define id="titleDeleted"><bean:message key="common.deleted" /></bean:define>
<bean:define id="titleView"><bean:message key="button.view" /></bean:define>
<bean:define id="titleEdit"><bean:message key="button.edit" /></bean:define>

<bean:message key="buendel" />

<jsp:useBean id="urls" class="java.util.HashMap" />

<display:table id="currentBuendel" 
	name="${baumassnahme.gleissperrungsBuendel}"
	export="false" 
	requestURI="${requestURI}" 
	pagesize="20" 
	sort="external"
	class="colored"
	style="margin:3px 0 5px 0;"
	decorator="db.training.easy.util.displaytag.decorators.AddRowLink">

	<%-- URL wird erzeugt, ggf. inkl. sessionId --%>
	<c:set target="${urls}" property="${currentBuendel.id}" value="#" />
	
	<easy:hasAuthorization model="${currentBuendel}" authorization="ROLE_BUENDEL_LESEN">
		<c:set target="${urls}" property="${currentBuendel.id}">
			<c:url value="/osb/viewBuendel.do?buendelId=${currentBuendel.id}" />
		</c:set>
	</easy:hasAuthorization>
	
	<display:column property="buendelId" titleKey="buendel.id" sortable="true" />
	<display:column property="buendelName" titleKey="buendel.name" sortable="true" />
	<display:column property="regionalbereich.name" sortProperty="regionalbereich" titleKey="buendel.regionalbereich" sortable="true" />

	<display:column style="text-align:right;width:15px;" media="html">
		<easy:hasAuthorization model="${currentBuendel}" authorization="ROLE_BUENDEL_LESEN">
			<html:link action="/osb/viewBuendel" styleClass="show" title="${titleView}">
				<html:param name="buendelId" value="${currentBuendel.id}" />
				&nbsp;
			</html:link>
		</easy:hasAuthorization>
	</display:column>
	
	<display:column style="text-align:right;width:15px;" media="html">
		<easy:hasAuthorization model="${currentBuendel}" authorization="ROLE_BUENDEL_BEARBEITEN">
			<html:link action="/osb/editBuendel" styleClass="edit" title="${titleEdit}">
				<html:param name="buendelId" value="${currentBuendel.id}" />
				&nbsp;
			</html:link>
		</easy:hasAuthorization>
	</display:column>

</display:table>
