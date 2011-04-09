<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>

<h2><bean:message key="buendel.tab.gleissperrungen.legend.added" /></h2>

<bean:define id="titleFixiert"><bean:message key="common.fixiert" /></bean:define>
<bean:define id="titleDeleted"><bean:message key="common.deleted" /></bean:define>
<bean:define id="titleView"><bean:message key="button.view" /></bean:define>
<bean:define id="titleEdit"><bean:message key="button.edit" /></bean:define>

<jsp:useBean id="urls" class="java.util.HashMap" />

<display:table id="item" 
	name="buendel.gleissperrungenGesamt"
	export="false" 
	requestURI="/osb/viewBuendel.do"
	pagesize="20" 
	class="colored"
	decorator="db.training.easy.util.displaytag.decorators.AddRowLink">

	<%-- URL wird erzeugt, ggf. inkl. sessionId --%>
	<c:if test="${item != null}">
		<easy:hasAuthorization model="${item}" authorization="ROLE_GLEISSPERRUNG_LESEN">
			<c:set target="${urls}" property="${item.id}">
				<c:url value="/osb/viewGleissperrung.do?gleissperrungId=${item.id}" />
			</c:set>
		</easy:hasAuthorization>
	</c:if>

	<jsp:directive.include file="../../gleissperrung/listGleissperrungColumnDetails.jsp" />

	<display:setProperty name="basic.empty.showtable" value="true" />
	<display:setProperty name="paging.banner.item_name"><bean:message key="gleissperrung" /></display:setProperty>
	<display:setProperty name="paging.banner.items_name"><bean:message key="gleissperrungen" /></display:setProperty>
</display:table>
