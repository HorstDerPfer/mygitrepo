<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>

<bean:define id="titleDeleted"><bean:message key="common.deleted" /></bean:define>
<bean:define id="titleView"><bean:message key="button.view" /></bean:define>
<bean:define id="titleEdit"><bean:message key="button.edit" /></bean:define>
<bean:define id="titleRemove"><bean:message key="button.remove" /></bean:define>
<bean:define id="confirmText" toScope="page"><bean:message key="confirm.fahrplanregelung.remove" /></bean:define>

<jsp:useBean id="urls" class="java.util.HashMap" />

<display:table id="currentFahrplanregelung" 
	name="buendel.fahrplanregelungen"
	export="false"
	requestURI="${requestURI}" 
	pagesize="20" 
	sort="list" 
	class="colored"
	decorator="db.training.easy.util.displaytag.decorators.AddRowLink">

	<%-- URL wird erzeugt, ggf. inkl. sessionId --%>
	<c:if test="${action != null && action != 'create'}">
		<easy:hasAuthorization model="${currentFahrplanregelung}" authorization="ROLE_FAHRPLANREGELUNG_LESEN">
			<c:set target="${urls}" property="${currentFahrplanregelung.id}">
				<c:url value="/osb/fahrplanregelung/view.do?fahrplanregelungId=${currentFahrplanregelung.id}" />
			</c:set>
		</easy:hasAuthorization>
	</c:if>

	<display:column title="" sortable="true" sortProperty="fixiert">
		<c:if test="${currentFahrplanregelung.fixiert == true}"><img src="<c:url value='/static/img/icon_lock.gif' />" title="${titleFixiert}" /></c:if>
		<c:if test="${currentFahrplanregelung.deleted == true}"><img src="<c:url value='/static/img/error.png' />" title="${titleDeleted}" /></c:if>
	</display:column>

	<display:column property="fahrplanregelungId" titleKey="fahrplanregelung.nummer" sortable="true" />
	<display:column property="name" titleKey="fahrplanregelung.name" sortable="true" />
	<display:column property="betriebsstelleVon.caption" titleKey="fahrplanregelung.betriebsstelleVon" sortable="true" />
	<display:column property="betriebsstelleBis.caption" titleKey="fahrplanregelung.betriebsstelleBis" sortable="true" />

	<display:column style="text-align:right;width:15px;" media="html">
		<easy:hasAuthorization model="${currentFahrplanregelung}" authorization="ROLE_FAHRPLANREGELUNG_LESEN">
			<html:link action="/osb/fahrplanregelung/view" styleClass="show" title="${titleView}">
				<html:param name="fahrplanregelungId">${currentFahrplanregelung.id}</html:param>
				&nbsp;
			</html:link>
		</easy:hasAuthorization>
	</display:column>

	<c:if test="${buendelForm.buendelId != null && buendelForm.buendelId != 0}">
		<display:column style="text-align:right;width:15px;" media="html">
			<easy:hasAuthorization model="${currentFahrplanregelung}" authorization="ROLE_FAHRPLANREGELUNG_BEARBEITEN">
				<html:link action="/osb/fahrplanregelung/edit" styleClass="edit" title="${titleEdit}">
					<html:param name="fahrplanregelungId">${currentFahrplanregelung.id}</html:param>
					&nbsp;
				</html:link>
			</easy:hasAuthorization>
		</display:column>
	
		<display:column style="text-align:right;width:15px;" media="html">
			<easy:hasAuthorization model="${buendel}" authorization="ROLE_BUENDEL_FAHRPLANREGELUNG_LOESCHEN">
				<html:link action="/deleteFahrplanregelungFromBuendel" styleClass="delete" onclick="return confirmLink(this.href, '${confirmText}');" title="${titleRemove}">
					<html:param name="buendelId" value="${buendelForm.buendelId}" />
					<html:param name="fahrplanregelungId" value="${currentFahrplanregelung.id}" />
					&nbsp;
				</html:link>
			</easy:hasAuthorization>
		</display:column>
	</c:if>
</display:table>

<c:if test="${buendelForm.buendelId != null && buendelForm.buendelId != 0}">
	<easy:hasAuthorization model="${buendel}" authorization="ROLE_BUENDEL_FAHRPLANREGELUNG_ANLEGEN">
		<div class="buttonBar" style="width:100px;float:right;padding-left:0px;">
			<html:link action="/addFahrplanregelungenToBuendel" styleClass="buttonPlus">
				<html:param name="buendelId" value="${buendelForm.buendelId}" />
				<bean:message key="button.create" />
			</html:link>
		</div>
	</easy:hasAuthorization>
</c:if>
