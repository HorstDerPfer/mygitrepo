<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>

<display:column title="" sortable="true" sortProperty="fixiert">
	<c:if test="${currentBuendel.fixiert == true}"><img src="<c:url value='/static/img/icon_lock.gif' />" title="${titleFixiert}" /></c:if>
	<c:if test="${currentBuendel.deleted == true}"><img src="<c:url value='/static/img/error.png' />" title="${titleDeleted}" /></c:if>
</display:column>

<display:column style="${blueColoredText}" property="buendelId" titleKey="buendel.id" sortable="true" />
<display:column style="${blueColoredText}" property="buendelName" titleKey="buendel.name" sortable="true" />
<display:column style="${blueColoredText}" property="regionalbereich.name" sortProperty="regionalbereich" titleKey="buendel.regionalbereich" sortable="true" />

<logic:present name="buendel" property="hauptStrecke">
	<display:column style="${blueColoredText}" property="hauptStrecke.nummer" sortProperty="hauptStrecke" titleKey="buendel.hauptStrecke" sortable="true" />
</logic:present>
<logic:notPresent name="buendel" property="hauptStrecke">
	<display:column style="${blueColoredText}" property="hauptStrecke.nummer" sortProperty="hauptStrecke" titleKey="buendel.hauptStrecke" sortable="true" />
</logic:notPresent>

<display:column style="text-align:right;width:15px;" media="html">
	<easy:hasAuthorization model="${currentBuendel}" authorization="ROLE_BUENDEL_LESEN">
		<html:link action="/osb/viewBuendel" styleClass="show" title="${titleView}">
			<html:param name="buendelId" value="${currentBuendel.id}" />
			&nbsp;
		</html:link>
	</easy:hasAuthorization>
</display:column>

<display:column style="text-align:right;width:15px;" media="html">
	<easy:hasAuthorization model="${currentBuendel}" authorization="ROLE_BUENDEL_BEARBEITEN">
		<html:link action="/osb/editBuendel" styleClass="edit" title="${titleEdit}">
			<html:param name="buendelId" value="${currentBuendel.id}" />
			&nbsp;
		</html:link>
	</easy:hasAuthorization>
</display:column>

<display:column style="text-align:right;width:15px;" media="html">
	<easy:hasAuthorization model="${currentBuendel}" authorization="ROLE_BUENDEL_LOESCHEN">
		<c:choose>
			<c:when test="${currentBuendel.deleted == true}">
		 		<html:link action="/osb/deleteBuendel" styleClass="undo" title="${titleUndelete}">
		 			<html:param name="buendelId" value="${currentBuendel.id}" />
		 			<html:param name="delete" value="false" />
		 			&nbsp;
		 		</html:link>
			</c:when>
			<c:when test="${currentBuendel.deleted == false}">
		 		<html:link action="/osb/deleteBuendel" styleClass="delete" onclick="return confirmLink(this.href, '${confirmText}');" title="${titleDelete}">
		 			<html:param name="buendelId" value="${currentBuendel.id}" />
		 			<html:param name="delete" value="true" />
		 			&nbsp;
		 		</html:link>
			</c:when>
		</c:choose>
 	</easy:hasAuthorization>
</display:column>
