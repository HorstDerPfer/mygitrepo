<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>

<div class="textcontent_head">
    <bean:message key="menu.baumassnahme.list" />
</div>

<div class="textcontent" style="text-align:center;">

	<jsp:useBean id="urls" class="java.util.HashMap"/>
	
	<display:table
		id="currentBaumassnahme" 
		name="baumassnahmen" 
		export="false"
		requestURI="${requestURI}"
		pagesize="20"
		sort="external"
		class="colored"
		decorator="db.training.easy.util.displaytag.decorators.AddRowLink">
	
		<%-- URL wird erzeugt, ggf. inkl. sessionId --%>
		<c:set target="${urls}" property="${currentBaumassnahme.id}" value="#" />
	
		<c:set target="${urls}" property="${currentBaumassnahme.id}">
			<c:url value="viewBaumassnahme.do?id=${currentBaumassnahme.id}" />
		</c:set>
		
		<c:set var="ausfallStyle">
			<c:choose>
				<c:when test="${!empty currentBaumassnahme.ausfallDatum}">background-color:#F4F4F4;color:#4f4f4f;</c:when>
				<c:otherwise></c:otherwise>
			</c:choose>
		</c:set>
		<display:column property="streckenAbschnitt" titleKey="baumassnahme.streckenabschnitt" sortable="true" style="${ausfallStyle}" />
		<%-- Formatierung nicht aendern. Notwendig fuer Excel-Export.--%>
		<display:column titleKey="baumassnahme.zeitraum" sortable="true" sortProperty="beginnDatum" style="${ausfallStyle}"><bean:write name="currentBaumassnahme" property="beginnDatum" format="dd.MM.yyyy" /> - <bean:write name="currentBaumassnahme" property="endDatum" format="dd.MM.yyyy" /></display:column>
		<display:column property="streckeBBP" titleKey="baumassnahme.streckebbp.short" sortable="true" style="${ausfallStyle}" />
		<display:column property="streckeVZG" titleKey="baumassnahme.streckevzg.short" sortable="true" style="${ausfallStyle}" />
		<display:column property="vorgangsNr" titleKey="baumassnahme.vorgangsnr" sortable="true" style="${ausfallStyle}" />
		<display:column property="regionalBereichFpl.name" media="html" titleKey="baumassnahme.regionalbereichfpl.short" sortable="true" style="${ausfallStyle}" />
		<display:column property="regionalBereichFpl.name" media="excel" titleKey="baumassnahme.regionalbereichfpl.short.excel" sortable="true" />
		<display:column property="prioritaet.value" titleKey="baumassnahme.prioritaet" sortable="false" style="text-align:center;${ausfallStyle}"/>
		<display:column property="art" titleKey="baumassnahme.art.short" sortable="true" style="text-align:center;${ausfallStyle}"/>
		
		<display:column titleKey="common.details" media="html" style="text-align:center;${ausfallStyle}">
			<span id="tip_${currentBaumassnahme.id}">
				<img src="<c:url value='static/img/icon_s_info_small.gif' />" id="tip_${currentBaumassnahme.id}" />
			</span>
		</display:column>
		<display:setProperty name="basic.empty.showtable" value="true" />
		<display:setProperty name="paging.banner.item_name"><bean:message key="baumassnahme" /></display:setProperty>
		<display:setProperty name="paging.banner.items_name"><bean:message key="baumassnahmen" /></display:setProperty>
	</display:table>
</div>

<div class="buttonBar">
	<html:link action="/suche" target="_blank" onclick="$('baumassnahmeSearchForm').submit();" styleClass="buttonXls">
		<html:param name="method" value="xlsSummary" />
		<bean:message key="button.xls.summary" />
	</html:link>
	<html:link action="/suche" target="_blank" onclick="$('baumassnahmeSearchForm').submit();" styleClass="buttonXls">
		<html:param name="method" value="xls" />
		<bean:message key="button.xls" />
	</html:link>
</div>

<%-- Tooltips --%>
<jsp:directive.include file="tooltips.jsp" />