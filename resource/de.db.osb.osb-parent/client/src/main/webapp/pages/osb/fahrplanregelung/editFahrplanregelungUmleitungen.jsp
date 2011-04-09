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
<bean:define id="titleRemove"><bean:message key="button.remove" /></bean:define>
<bean:define id="confirmText" toScope="page"><bean:message key="confirm.umleitung.remove" /></bean:define>

<jsp:useBean id="urls" class="java.util.HashMap" />

<display:table id="current" 
	name="fahrplanregelung.umleitungFahrplanregelungLinks"
	export="false"
	requestURI="${requestURI}" 
	pagesize="20" 
	sort="list" 
	class="colored"
	decorator="db.training.easy.util.displaytag.decorators.AddRowLink">
	
	<c:if test="${current != null}">
		<c:set target="${urls}" property="${current.umleitung.id}">
			<c:url value="editUmleitung.do?umleitungId=${current.umleitung.id}" />
		</c:set>
	</c:if>

	<display:column title="" sortable="true" sortProperty="deleted">
		<c:if test="${current.umleitung.deleted == true}"><img src="<c:url value='/static/img/error.png' />" title="${titleDeleted}" /></c:if>
	</display:column>
	
	<display:column property="umleitung.name" titleKey="umleitung.titel.short" sortable="true" />
	<display:column property="umleitung.relation" titleKey="osb.umleitung.relation" sortable="false" />
	<display:column titleKey="umleitung.anzahl.richtung">
		${current.anzahlSGV} /
		${current.anzahlSPNV} /
		${current.anzahlSPFV}
	</display:column>
	<display:column titleKey="umleitung.anzahl.gegenrichtung">
		${current.anzahlSGVGegenRich} /
		${current.anzahlSPNVGegenRich} /
		${current.anzahlSPFVGegenRich}
	</display:column>
	
	<display:column style="text-align:right;width:15px;padding-left:0px;" sortable="false" media="html">
		<easy:hasAuthorization model="${current.umleitung}" authorization="ROLE_UMLEITUNG_LESEN">
	 		<html:link action="/viewUmleitung" styleClass="show" title="${titleView}">
			<html:param name="umleitungId" value="${current.umleitung.id}" />
			&nbsp;
		 	</html:link>
	 	</easy:hasAuthorization>
	</display:column>
		
	<display:column style="text-align:right;width:15px;" media="html">
		<easy:hasAuthorization model="${current.umleitung}" authorization="ROLE_UMLEITUNG_BEARBEITEN">
	 		<html:link action="/editUmleitung" styleClass="edit" title="${titleEdit}">
				<html:param name="umleitungId" value="${current.umleitung.id}" />
				&nbsp;
	 		</html:link>
	 	</easy:hasAuthorization>
	</display:column>

	<%--
	<c:if test="${fahrplanregelung.fixiert == false}">
		<display:column style="text-align:right;width:15px;" media="html">
			<easy:hasAuthorization model="${fahrplanregelung}" authorization="ROLE_FAHRPLANREGELUNG_BEARBEITEN">
				<html:link action="/deleteUmleitungFromFahrplanregelung" styleClass="delete" onclick="return confirmLink(this.href, '${confirmText}');" title="${titleRemove}">
					<html:param name="umleitungId" value="${current.umleitung.id}" />
					<html:param name="fahrplanregelungId" value="${fahrplanregelungForm.fahrplanregelungId}" />
					&nbsp;
				</html:link>
			</easy:hasAuthorization>
		</display:column>
	</c:if>
	--%>
					
	<display:setProperty name="basic.empty.showtable" value="true" />
	<display:setProperty name="paging.banner.item_name"><bean:message key="umleitung" /></display:setProperty>
	<display:setProperty name="paging.banner.items_name"><bean:message key="umleitungen" /></display:setProperty>
</display:table>
<%--
<c:if test="${fahrplanregelung.fixiert == false}">
	<easy:hasAuthorization model="${fahrplanregelung}" authorization="ROLE_FAHRPLANREGELUNG_BEARBEITEN">
		<div class="buttonBar" style="width:95px;float:right;">
			<html:link action="/addUmleitungenToFahrplanregelung" styleClass="buttonPlus" style="margin-left:0px;">
				<html:param name="fahrplanregelungId" value="${fahrplanregelung.fahrplanregelungId}" />
				<bean:message key="button.create" />
			</html:link>
		</div>
	</easy:hasAuthorization>
</c:if>
	--%>
