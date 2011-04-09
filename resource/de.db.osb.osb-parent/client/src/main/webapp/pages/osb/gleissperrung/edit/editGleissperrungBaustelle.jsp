<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>

<bean:define id="titleView"><bean:message key="button.view" /></bean:define>

<jsp:useBean id="urls" class="java.util.HashMap" />

<display:table id="baustelle" 
	name="${gleissperrung.baustellen}"
	export="true" 
	requestURI="${requestURI}" 
	pagesize="20" 
	sort="external"
	class="colored"
	decorator="db.training.easy.util.displaytag.decorators.AddRowLink">

	<%-- URL wird erzeugt, ggf. inkl. sessionId --%>
	<c:set target="${urls}" property="${baustelle.id}" value="#" />

	<easy:hasAuthorization model="${baustelle}" authorization="ROLE_BAUSTELLE_LESEN">
		<c:set target="${urls}" property="${baustelle.id}">
			<c:url value="/osb/baustelle/view.do?id=${baustelle.id}" />
		</c:set>
	</easy:hasAuthorization>
	
	<display:column property="name" titleKey="baustelle.name" sortable="true" />
	
	<display:column style="text-align:right;width:15px;" media="html">
		<easy:hasAuthorization model="${baustelle}" authorization="ROLE_BAUSTELLE_LESEN">
	 		<html:link action="/osb/baustelle/view" styleClass="show" title="${titleView}">
				<html:param name="id" value="${baustelle.id}" />
	 			&nbsp;
	 		</html:link>
	 	</easy:hasAuthorization>
	</display:column>
</display:table>