<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>


<div id="divBearbeiter">
	<c:if test="${tab=='Bearbeiter' or currentUser != null}">
		
		<display:table id="currentBearbeiter"
			name="baumassnahme.bearbeiter"
			export="false"
			class="colored"
			requestURI="${requestURI}"
			style="margin-top:5px"
			defaultsort="1">
		
			<display:column property="user.name" titleKey="bearbeiter.name" style="width:75px;" sortable="true" />
			<display:column property="user.firstName" titleKey="bearbeiter.vorname" sortable="true"/>
			<display:column titleKey="bearbeiter.aktiv">
				<bean:message key="bearbeiter.aktiv.${currentBearbeiter.aktiv}" />
			</display:column>
		
			<display:setProperty name="basic.empty.showtable" value="true" />
			<display:setProperty name="paging.banner.item_name"><bean:message key="bearbeiter" /></display:setProperty>
			<display:setProperty name="paging.banner.items_name"><bean:message key="bearbeiter" /></display:setProperty>
		</display:table>
		
	</c:if>
</div>			
