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
	openMainMenu('navLink_osb_workflow-baustelle');
</script>

<div class="textcontent_head">
	<bean:message key="menu.gleissperrungenToBaustelle.add" />
</div>
<div class="textcontent" style="text-align: center;">
	<bean:define id="titleFixiert"><bean:message key="common.fixiert" /></bean:define>
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
			<%--<c:if test="${currentGleissperrung.massnahme != null && currentGleissperrung.massnahme.genehmiger != null}"><img src="<c:url value='/static/img/icon_lock.gif' />" title="${titleFixiert}" /></c:if>--%>
			<c:if test="${currentGleissperrung.deleted == true}"><img src="<c:url value='/static/img/error.png' />" title="${titleDeleted}" /></c:if>
		</display:column>

		<display:column property="bstVon.caption" titleKey="regelung.bstVon" sortable="false" media="html" />
		<display:column property="bstBis.caption" titleKey="regelung.bstBis" sortable="false" media="html" />
		<display:column property="zeitVon" format="{0, date, dd.MM.yyyy HH:mm}" titleKey="regelung.zeitVon" sortable="false" media="html" />
		<display:column property="zeitBis" format="{0, date, dd.MM.yyyy HH:mm}" titleKey="regelung.zeitBis" sortable="false" media="html" />
		
		<display:column style="text-align:right;width:15px;" media="html">
			<easy:hasAuthorization model="${item}" authorization="ROLE_GLEISSPERRUNG_LESEN">
		 		<html:link action="/osb/viewGleissperrung.do?gleissperrungId=${item.id}" styleClass="show">&nbsp;</html:link>
		 	</easy:hasAuthorization>
		</display:column>
	
		<display:column style="text-align:right;width:15px;" media="html">
			<html:link action="/osb/addGleissperrungToBaustelle.do?gleissperrungId=${currentGleissperrung.id}&baustelleId=${baustelle.id}"
				styleClass="plus">&nbsp;</html:link>
		</display:column>
	</display:table>
</div>
<div class="buttonBar">
	<html:link action="/osb/baustelle/edit.do?id=${baustelle.id}" styleClass="buttonBack"><bean:message key="button.back" /></html:link>
</div>

<jsp:include page="/pages/main_footer.jsp" />