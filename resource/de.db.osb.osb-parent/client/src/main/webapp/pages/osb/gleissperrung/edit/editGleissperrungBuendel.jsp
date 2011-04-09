<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>

<bean:define id="titleFixiert"><bean:message key="common.fixiert" /></bean:define>
<bean:define id="titleDeleted"><bean:message key="common.deleted" /></bean:define>
<bean:define id="titleView"><bean:message key="button.view" /></bean:define>

<jsp:useBean id="urls" class="java.util.HashMap" />

<display:table id="currentBuendel" 
	name="${gleissperrung.buendel}"
	export="true" 
	requestURI="${requestURI}" 
	pagesize="20" 
	sort="external"
	class="colored"
	decorator="db.training.easy.util.displaytag.decorators.AddRowLink">

	<%-- URL wird erzeugt, ggf. inkl. sessionId --%>
	<c:set target="${urls}" property="${currentBuendel.id}" value="#" />

	<easy:hasAuthorization model="${currentBuendel}" authorization="ROLE_BUENDEL_LESEN">
		<c:set target="${urls}" property="${currentBuendel.id}">
			<c:url value="/osb/viewBuendel.do?buendelId=${currentBuendel.id}" />
		</c:set>
	</easy:hasAuthorization>

	<display:column title="" sortable="true" sortProperty="fixiert">
		<c:if test="${currentBuendel.fixiert == true}"><img src="<c:url value='/static/img/icon_lock.gif' />" title="${titleDeleted}" /></c:if>
		<c:if test="${currentBuendel.deleted == true}"><img src="<c:url value='/static/img/error.png' />" title="${titleDeleted}" /></c:if>
	</display:column>
	
	<display:column property="buendelId" titleKey="buendel.id" sortable="true" />
	<display:column property="buendelName" titleKey="buendel.name" sortable="true" />
	<display:column property="regionalbereich.name" sortProperty="regionalbereich" titleKey="buendel.regionalbereich" sortable="true" />
	<display:column property="hauptStrecke.nummer" sortProperty="hauptStrecke" titleKey="buendel.hauptStrecke" sortable="true" />

	<display:column style="text-align:right;width:15px;" media="html">
		<easy:hasAuthorization model="${currentBuendel}" authorization="ROLE_BUENDEL_LESEN">
			<html:link action="/osb/viewBuendel" styleClass="show" title="${titleView}">
				<html:param name="buendelId" value="${currentBuendel.id}" />
				&nbsp;
			</html:link>
		</easy:hasAuthorization>
	</display:column>
</display:table>