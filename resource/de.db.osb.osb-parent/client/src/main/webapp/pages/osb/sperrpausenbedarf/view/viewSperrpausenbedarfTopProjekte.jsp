<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
<html:xhtml/>

<bean:define id="titleDeleted"><bean:message key="common.deleted" /></bean:define>
<bean:define id="titleView"><bean:message key="button.view" /></bean:define>
<bean:define id="titleEdit"><bean:message key="button.edit" /></bean:define>

<bean:message key="topprojekte" />

<jsp:useBean id="urls" class="java.util.HashMap" />

<display:table id="currentProjekt" 
	name="baumassnahme.topProjekte"
	export="false" 
	requestURI="${requestURI}"
	pagesize="20" 
	sort="list" 
	class="colored"
	style="margin:3px 0 5px 0;"
	decorator="db.training.easy.util.displaytag.decorators.AddRowLink" >

	<%-- URL wird erzeugt, ggf. inkl. sessionId --%>
	<c:set target="${urls}" property="${currentProjekt.id}" value="#" />

	<easy:hasAuthorization model="${currentProjekt}" authorization="ROLE_GLEISSPERRUNG_LESEN">
		<c:set target="${urls}" property="${currentProjekt.id}">
			<c:url value="/osb/viewTopProjekt.do?topProjektId=${currentProjekt.id}" />
		</c:set>
	</easy:hasAuthorization>

	<display:column title="" sortable="true" sortProperty="deleted">
		<c:if test="${currentProjekt.deleted == true}"><img src="<c:url value='/static/img/error.png' />" title="${titleDeleted}" /></c:if>
	</display:column>
	
	<display:column property="sapProjektNummer" titleKey="topprojekt.sapprojektnummer" sortable="false" media="html" />
	<display:column property="baukosten" titleKey="topprojekt.baukosten" sortable="false" format="{0, number}" media="html" />
	<display:column property="name" titleKey="topprojekt.name" sortable="false" media="html" />
	
	<display:column style="text-align:right;width:15px;" media="html">
		<easy:hasAuthorization model="${currentProjekt}" authorization="ROLE_TOPPROJEKT_LESEN">
	 		<html:link action="/osb/viewTopProjekt" styleClass="show" title="${titleView}">
	 			<html:param name="topProjektId" value="${currentProjekt.id}" />
	 			&nbsp;
	 		</html:link>
	 	</easy:hasAuthorization>
	</display:column>

	<display:column style="text-align:right;width:15px;" media="html">
		<easy:hasAuthorization model="${currentProjekt}" authorization="ROLE_TOPPROJEKT_BEARBEITEN">
	 		<html:link action="/osb/editTopProjekt" styleClass="edit" title="${titleEdit}">
	 			<html:param name="topProjektId" value="${currentProjekt.id}" />
	 			&nbsp;
	 		</html:link>
	 	</easy:hasAuthorization>
	</display:column>
	
	<display:setProperty name="paging.banner.all_items_found" value="" />
</display:table>
