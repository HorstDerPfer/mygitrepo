<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>

<bean:define id="titleFixiert"><bean:message key="common.fixiert" /></bean:define>
<bean:define id="titleView"><bean:message key="button.view" /></bean:define>

<table class="colored">
	<tr>
		<th></th>
		<th><bean:message key="baumassnahme.mnid" /></th>
		<th><bean:message key="sperrpausenbedarf.projektDefinitionDbBez" /></th>
		<th><bean:message key="sperrpausenbedarf.untergewerk" /></th>
		<th><bean:message key="baumassnahme.regionalbereich" /></th>
		<th></th>
	</tr>
	<c:choose>
		<c:when test="${gleissperrung.massnahme != null}">
			<tr class="evenrow">
				<td><c:if test="${gleissperrung.massnahme != null && gleissperrung.massnahme.genehmiger != null}"><img src="<c:url value='/static/img/icon_lock.gif' />" title="${titleFixiert}" /></c:if></td>
				<td><bean:write name="gleissperrung" property="massnahme.massnahmeId" /></td>
				<td><bean:write name="gleissperrung" property="massnahme.projektDefinitionDbBez" /></td>
				<td><bean:write name="gleissperrung" property="massnahme.untergewerk" /></td>
				<td><bean:write name="gleissperrung" property="massnahme.regionalbereich.name" /></td>
				<td style="text-align:right;width:15px;">
					<easy:hasAuthorization model="${gleissperrung.massnahme}" authorization="ROLE_MASSNAHME_LESEN">
				 		<html:link action="/osb/viewSperrpausenbedarf" styleClass="show" title="${titleView}">
							<html:param name="sperrpausenbedarfId" value="${gleissperrung.massnahme.id}" />
				 			&nbsp;
				 		</html:link>
				 	</easy:hasAuthorization>
				</td>
			</tr>
		</c:when>
		<c:otherwise>
			<tr class="evenrow">
				<td class="center" colspan="5"><bean:message key="common.noData" /></td>
			</tr>
		</c:otherwise>
	</c:choose>
</table>
