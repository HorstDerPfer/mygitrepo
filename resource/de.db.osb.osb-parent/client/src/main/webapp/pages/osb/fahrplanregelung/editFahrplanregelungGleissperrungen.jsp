<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>

<jsp:useBean id="urls" class="java.util.HashMap" />

<bean:define id="confirmText" toScope="page"><bean:message key="confirm.fahrplanregelung.gleissperrung.remove" /></bean:define>
<bean:define id="titleRemove"><bean:message key="button.remove" /></bean:define>

<display:table id="gleissperrung" 
	name="${fahrplanregelung.gleissperrungen}"
	export="false" 
	requestURI="${requestURI}" 
	pagesize="20" 
	class="colored"
	decorator="db.training.easy.util.displaytag.decorators.AddRowLink">

	<%-- URL wird erzeugt, ggf. inkl. sessionId --%>
	<c:if test="${gleissperrung != null}">
		<easy:hasAuthorization model="${gleissperrung}" authorization="ROLE_GLEISSPERRUNG_LESEN">
			<c:set target="${urls}" property="${gleissperrung.id}">
				<c:url value="/osb/viewGleissperrung.do?gleissperrungId=${gleissperrung.id}" />
			</c:set>
		</easy:hasAuthorization>
	
		<bean:define id="item" name="gleissperrung" />
	</c:if>

	<jsp:directive.include file="../gleissperrung/listGleissperrungColumnDetails.jsp" />
	
	<display:column style="text-align:right;width:15px;" media="html">
		<easy:hasAuthorization model="${item}" authorization="ROLE_FAHRPLANREGELUNG_GLEISSPERRUNG_ZUORDNEN">
	 		<html:link action="/osb/fahrplanregelung/gleissperrung/remove" styleClass="delete" onclick="return confirmLink(this.href, '${confirmText}');" title="${titleRemove}">
	 			<html:param name="gleissperrungId" value="${item.id}" />
	 			<html:param name="fahrplanregelungId" value="${fahrplanregelung.id}" />
	 			<html:param name="delete" value="true" />
	 			&nbsp;
	 		</html:link>
	 	</easy:hasAuthorization>
	</display:column>
	
</display:table>
