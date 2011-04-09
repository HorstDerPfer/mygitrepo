<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html:xhtml/>

<div id="divBearbeiterTabelle">
	<display:table id="currentBearbeiter"
		name="baumassnahme.bearbeiter"
		export="false"
		class="colored"
		style="margin-top:5px;max-width:800px"
		defaultsort="1">
	
		<display:column property="user.name" titleKey="bearbeiter.name" style="width:75px;" sortable="false" />
		<display:column property="user.firstName" titleKey="bearbeiter.vorname" sortable="false" />
		
		<display:column titleKey="bearbeiter.aktiv" sortable="false">
			<bean:define toScope="request" id="index" value="${currentBearbeiter.id}"/>
			<% String property1Name = "bearbeiter(" + index + ")"; %>
			<html:radio name="baumassnahmeForm" property="<%=property1Name %>" value="true" styleClass="checkbox" errorStyle="${errorStyle }"/>
			<bean:message key="bearbeiter.aktiv.true" />
			<c:if test="${currentUser.id==currentBearbeiter.user.id}">
				<html:radio name="baumassnahmeForm" property="<%=property1Name %>" value="false" styleClass="checkbox" errorStyle="${errorStyle }"/>
				<bean:message key="bearbeiter.aktiv.false" />
			</c:if>
			<c:if test="${currentUser.id!=currentBearbeiter.user.id}">
				<html:radio name="baumassnahmeForm" property="<%=property1Name %>" value="false" styleClass="checkbox" errorStyle="${errorStyle }" disabled="true"/>
				<span style="color: #999999"><bean:message key="bearbeiter.aktiv.false"/></span>
			</c:if>
			
		</display:column>
	
		<display:setProperty name="basic.empty.showtable" value="true" />
		<display:setProperty name="paging.banner.item_name"><bean:message key="bearbeiter" /></display:setProperty>
		<display:setProperty name="paging.banner.items_name"><bean:message key="bearbeiter" /></display:setProperty>
	</display:table>
</div>

