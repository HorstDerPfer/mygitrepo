<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
			
<bean:define id="titleFixiert"><bean:message key="common.fixiert" /></bean:define>
<jsp:useBean id="urls" class="java.util.HashMap"/>
  
<display:table
	id="currentMassnahme" 
	name="paket.massnahmen" 
	export="true"
	requestURI="${requestURI}"
	pagesize="20" 
	sort="list"
	class="colored"
	decorator="db.training.easy.util.displaytag.decorators.AddRowLink">

	<%-- URL wird erzeugt, ggf. inkl. sessionId --%>
	<c:set target="${urls}" property="${currentMassnahme.id}" value="#" />

	<easy:hasAuthorization model="${currentMassnahme}" authorization="ROLE_MASSNAHME_LESEN">
		<c:set target="${urls}" property="${currentMassnahme.id}">
			<c:url value="/osb/viewSperrpausenbedarf.do?sperrpausenbedarfId=${currentMassnahme.id}" />
		</c:set>
	</easy:hasAuthorization>
	
	<display:column title="" sortable="true">
		<c:if test="${currentMassnahme.genehmiger != null}"><img src="<c:url value='/static/img/icon_lock.gif' />" title="${titleFixiert}" /></c:if>
	</display:column>

	<display:column property="id" titleKey="paket.id" sortable="true" />
	<display:column property="gewerk" titleKey="paket.gewerk" sortable="true" />
	<display:column property="untergewerk" titleKey="sperrpausenbedarf.untergewerk" sortable="true" media="html excel" />
	<display:column property="projektDefinitionDbBez" titleKey="sperrpausenbedarf.projektDefinitionDbBez.br" sortable="true" media="html excel" />
	<display:column property="hauptStrecke.nummer" titleKey="sperrpausenbedarf.hauptStrecke" sortable="true" media="html excel" />
	
	<display:column style="text-align:right;width:15px;" media="html">
		<easy:hasAuthorization model="${currentMassnahme}" authorization="ROLE_MASSNAHME_LESEN">
	 		<html:link action="/osb/viewSperrpausenbedarf" styleClass="show">
	 			<html:param name="sperrpausenbedarfId" value="${currentMassnahme.id}" />
	 			&nbsp;
	 		</html:link>
	 	</easy:hasAuthorization>
	</display:column>
	
	<display:setProperty name="basic.empty.showtable" value="true" />
	<display:setProperty name="paging.banner.item_name"><bean:message key="baumassnahme" /></display:setProperty>
	<display:setProperty name="paging.banner.items_name"><bean:message key="baumassnahmen" /></display:setProperty>
</display:table>
