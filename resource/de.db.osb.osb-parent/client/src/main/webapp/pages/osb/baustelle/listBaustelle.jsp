<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>
<html:xhtml />

<jsp:include page="/pages/main_head.jsp" />
<jsp:include page="/pages/main_path.jsp" />
<jsp:include page="/pages/main_menu.jsp" />
			
<%-- Öffnet Punkt in Startmenü --%>
<script type="text/javascript">
    openMainMenu('navLink_osb_workflow-baustelle');
</script>

<div class="textcontent_head">
	<bean:message key="menu.baustelle" />
</div>

<div class="textcontent" style="text-align: center;">
	<jsp:useBean id="urls" class="java.util.HashMap" />

	<display:table
		id="item"
		name="baustellen"
		export="true"
		requestURI="${requestURI}"
		pagesize="${baustellen.objectsPerPage}"
		sort="external"
		class="colored"
		decorator="db.training.easy.util.displaytag.decorators.AddRowLink">

		<%-- URL wird erzeugt, ggf. inkl. sessionId --%>
		<c:set target="${urls}" property="${item.id}" value="#" />

		<easy:hasAuthorization model="${item}" authorization="ROLE_BAUSTELLE_LESEN">
			<c:set target="${urls}" property="${item.id}">
				<c:url value="/osb/baustelle/view.do?id=${item.id}" />
			</c:set>
		</easy:hasAuthorization>
		
		<display:column property="name" titleKey="baustelle.name" sortable="true" />

		<display:column titleKey="baustelle.anzahl.gleissperrungen" sortable="false">
			${ fn:length(item.gleissperrungen) }
		</display:column>

		<display:column style="text-align:right;width:15px;" media="html">
			<easy:hasAuthorization model="${item}" authorization="ROLE_BAUSTELLE_LESEN">
		 		<html:link action="/osb/baustelle/view" styleClass="show">
					<html:param name="id" value="${item.id}" />
		 			&nbsp;
		 		</html:link>
		 	</easy:hasAuthorization>
		</display:column>

		<display:column style="text-align:right;width:15px;" media="html">
			<easy:hasAuthorization model="${item}" authorization="ROLE_BAUSTELLE_BEARBEITEN">
		 		<html:link action="/osb/baustelle/edit" styleClass="edit">
					<html:param name="id" value="${item.id}" />
		 			&nbsp;
		 		</html:link>
		 	</easy:hasAuthorization>
		</display:column>
	</display:table>
	
</div>
<authz:authorize ifAnyGranted="ROLE_GLEISSPERRUNG_ANLEGEN_ALLE, ROLE_GLEISSPERRUNG_ANLEGEN_REGIONALBEREICH">
	<div class="buttonBar">
		<html:link action="/osb/baustelle/edit" styleClass="buttonAdd">
			<html:param name="id"></html:param>
			<bean:message key="button.create" />
		</html:link>
	</div>
</authz:authorize>

<jsp:include page="/pages/main_footer.jsp" />