<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>

<display:column title="" sortable="true" sortProperty="deleted">
	<c:if test="${currentUmleitung.deleted == true}"><img src="<c:url value='/static/img/error.png' />" title="${titleDeleted}" /></c:if>
</display:column>

<display:column property="name" titleKey="umleitung.titel.short" sortable="true" />
<display:column titleKey="osb.umleitung.relation" sortable="false">
	<logic:notEmpty name="currentUmleitung" property="relation">
		<bean:write name="currentUmleitung" property="relation" />
	</logic:notEmpty>
	<logic:empty name="currentUmleitung" property="relation">
		<i><bean:message key="osb.umleitung.relation.null" /></i>
	</logic:empty>
</display:column>

<display:column style="text-align:right;width:15px;" media="html">
	<easy:hasAuthorization model="${currentUmleitung}" authorization="ROLE_UMLEITUNG_BEARBEITEN">
		<html:link action="/editUmleitung" styleClass="edit" title="${titleEdit}">
			<html:param name="umleitungId" value="${currentUmleitung.id}" />
			&nbsp;
		</html:link>
	</easy:hasAuthorization>
</display:column>

<display:column style="text-align:right;width:15px;" media="html">
	<easy:hasAuthorization model="${currentUmleitung}" authorization="ROLE_UMLEITUNG_LESEN">
		<html:link action="/viewUmleitung" styleClass="show" title="${titleView}">
			<html:param name="umleitungId" value="${currentUmleitung.id}" />
			&nbsp;
		</html:link>
	</easy:hasAuthorization>
</display:column>

<display:column style="text-align:right;width:15px;" media="html">
	<easy:hasAuthorization model="${currentUmleitung}" authorization="ROLE_UMLEITUNG_LOESCHEN">
		<c:choose>
			<c:when test="${currentUmleitung.deleted == true}">
		 		<html:link action="/deleteUmleitung" styleClass="undo" title="${titleUndelete}">
					<html:param name="umleitungId" value="${currentUmleitung.id}" />
		 			<html:param name="delete" value="false" />
		 			&nbsp;
		 		</html:link>
			</c:when>
			<c:when test="${currentUmleitung.deleted == false}">
		 		<html:link action="/deleteUmleitung" styleClass="delete" onclick="return confirmLink(this.href, '${confirmText}');" title="${titleDelete}">
					<html:param name="umleitungId" value="${currentUmleitung.id}" />
		 			<html:param name="delete" value="true" />
		 			&nbsp;
		 		</html:link>
			</c:when>
		</c:choose>
 	</easy:hasAuthorization>
</display:column>
