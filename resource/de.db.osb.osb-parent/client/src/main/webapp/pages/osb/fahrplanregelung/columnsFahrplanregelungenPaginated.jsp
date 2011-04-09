<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>

<display:column title="" sortable="true" sortProperty="fixiert">
	<c:if test="${currentFahrplanregelung.fixiert == true}"><img src="<c:url value='/static/img/icon_lock.gif' />" title="${titleFixiert}" /></c:if>
	<c:if test="${currentFahrplanregelung.deleted == true}"><img src="<c:url value='/static/img/error.png' />" title="${titleDeleted}" /></c:if>
</display:column>

<display:column style="${blueColoredText}" property="fahrplanregelungId" titleKey="fahrplanregelung.nummer" sortable="false" />

<display:column style="${blueColoredText}" sortProperty="betriebsstelleVon" titleKey="fahrplanregelung.streckenabschnitt" sortable="true">
	${currentFahrplanregelung.betriebsstelleVon.caption} - ${currentFahrplanregelung.betriebsstelleBis.caption}
</display:column>

<display:column  titleKey="common.zeitraum" sortable="true" style="width:130px;${blueColoredText}" sortProperty="planStart">
	<fmt:formatDate pattern="dd.MM.yyyy" value="${currentFahrplanregelung.planStart}" />
	- 
	<fmt:formatDate pattern="dd.MM.yyyy" value="${currentFahrplanregelung.planEnde}" />
	<br />
	${currentFahrplanregelung.verkehrstage}
</display:column>

<display:column style="${blueColoredText}" property="name" titleKey="fahrplanregelung.name" sortable="true" />
<display:column style="${blueColoredText}" property="betriebsweise" titleKey="baumassnahme.betriebsweise" sortable="true" />


<display:column style="text-align:right;width:15px;" media="html">
	<easy:hasAuthorization model="${currentFahrplanregelung}" authorization="ROLE_FAHRPLANREGELUNG_LESEN">
		<html:link action="/osb/fahrplanregelung/view" styleClass="show" title="${titleView}">
			<html:param name="fahrplanregelungId" value="${currentFahrplanregelung.id}" />
			&nbsp;
		</html:link>
	</easy:hasAuthorization>
</display:column>
<display:column style="text-align:right;width:15px;" media="html">
	<easy:hasAuthorization model="${currentFahrplanregelung}" authorization="ROLE_FAHRPLANREGELUNG_BEARBEITEN">
		<html:link action="/osb/fahrplanregelung/edit" styleClass="edit" title="${titleEdit}">
			<html:param name="fahrplanregelungId" value="${currentFahrplanregelung.id}" />
			&nbsp;
		</html:link>
	</easy:hasAuthorization>
</display:column>
<display:column style="text-align:right;width:15px;" media="html">
	<easy:hasAuthorization model="${currentFahrplanregelung}" authorization="ROLE_FAHRPLANREGELUNG_LOESCHEN">
		<c:choose>
			<c:when test="${currentFahrplanregelung.deleted == true}">
		 		<html:link action="/osb/fahrplanregelung/delete" styleClass="undo" title="${titleUndelete}">
					<html:param name="fahrplanregelungId" value="${currentFahrplanregelung.id}" />
		 			<html:param name="delete" value="false" />
		 			&nbsp;
		 		</html:link>
			</c:when>
			<c:when test="${currentFahrplanregelung.deleted == false}">
		 		<html:link action="/osb/fahrplanregelung/delete" styleClass="delete" onclick="return confirmLink(this.href, '${confirmText}');" title="${titleDelete}">
					<html:param name="fahrplanregelungId" value="${currentFahrplanregelung.id}" />
		 			<html:param name="delete" value="true" />
		 			&nbsp;
		 		</html:link>
			</c:when>
		</c:choose>
 	</easy:hasAuthorization>
</display:column>