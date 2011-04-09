<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

<div id="divAenderungsdokumentation">
	<c:if test="${tab=='Aenderungsdokumentation'}">
		<display:table
			id="currentAenderung" 
			name="baumassnahme.aenderungen" 
			export="false"
			requestURI="viewBaumassnahme.do" 
			pagesize="20" 
			sort="list"
			defaultsort="1"
			class="colored">
			
			<c:set var="aufwandTimeStringTitle"><bean:message key="baumassnahme.aufwand" />&nbsp;<bean:message key="common.unit.time.hhmm" /></c:set>
			
			<display:column property="aenderungsNr" titleKey="baumassnahme.aenderungsNr" sortable="false"/>
			<display:column property="grund.name" titleKey="baumassnahme.grund" sortable="false" style="width:70%;"/>
			<display:column property="aufwandTimeString" title="${aufwandTimeStringTitle}" sortable="false" style="text-align:right;" />

			<display:setProperty name="basic.empty.showtable" value="true" />
			<display:setProperty name="paging.banner.item_name"><bean:message key="baumassnahme.aenderung" /></display:setProperty>
			<display:setProperty name="paging.banner.items_name"><bean:message key="baumassnahme.aenderungen" /></display:setProperty>
		</display:table>
	</c:if>
</div>