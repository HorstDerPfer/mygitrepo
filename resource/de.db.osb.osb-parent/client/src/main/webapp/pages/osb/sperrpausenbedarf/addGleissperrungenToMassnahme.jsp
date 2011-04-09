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
	openMainMenu('navLink_osb_workflow-sperrbedarf');
</script>

<div class="textcontent_head">
	<bean:message key="menu.gleissperrungenToMassnahme.add" />
</div>
<div class="textcontent" style="text-align: center;">
	<bean:define id="titleDeleted"><bean:message key="common.deleted" /></bean:define>

	<jsp:useBean id="urls" class="java.util.HashMap" />

	<display:table id="currentGleissperrung" 
		name="gleissperrungen"
		export="false"
		requestURI="${requestURI}"
		pagesize="20"
		sort="list"
		class="colored"
		decorator="db.training.easy.util.displaytag.decorators.AddRowLink">

		<display:column title="" sortable="true" sortProperty="deleted">
			<c:if test="${currentGleissperrung.deleted == true}"><img src="<c:url value='/static/img/error.png' />" title="${titleDeleted}" /></c:if>
		</display:column>

		<display:column property="bstVon.caption" titleKey="regelung.bstVon" sortable="false" media="html" />
		<display:column property="bstBis.caption" titleKey="regelung.bstBis" sortable="false" media="html" />
		<display:column property="sperrpausenbedarf" titleKey="gleissperrung.sperrpausenbedarf" sortable="false" media="html" style="text-align:right;" />
		<display:column titleKey="common.unit" sortable="false" media="html"><bean:message key="common.unit.hours" /></display:column>
		
		<display:column style="text-align:right;width:15px;" media="html">
			<easy:hasAuthorization model="${currentGleissperrung}" authorization="ROLE_GLEISSPERRUNG_LESEN">
		 		<html:link action="/osb/viewGleissperrung" styleClass="show">
		 			<html:param name="gleissperrungId" value="${currentGleissperrung.id}" />
		 			&nbsp;
		 		</html:link>
		 	</easy:hasAuthorization>
		</display:column>
	
		<display:column style="text-align:right;width:15px;" media="html">
			<html:link action="/osb/toggleGleissperrungToMassnahme" styleClass="plus">
				<html:param name="gleissperrungId" value="${currentGleissperrung.id}" />
				<html:param name="sperrpausenbedarfId" value="${massnahme.id}" />
	 			<html:param name="action" value="add" />
				&nbsp;
			</html:link>
		</display:column>
	</display:table>
</div>
<div class="buttonBar">
	<html:link action="/osb/editSperrpausenbedarf" styleClass="buttonBack">
		<html:param name="sperrpausenbedarfId" value="${massnahme.id}" />
		<bean:message key="button.back" />
	</html:link>
</div>

<jsp:include page="/pages/main_footer.jsp" />