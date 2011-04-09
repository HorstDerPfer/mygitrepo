<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy" %>
<html:xhtml />

<br />

<%-- Karteireiter ----------------------------------------------%>
<c:if test="${activeTab == 'editMassnahme'}">
	<html:link href="#" styleClass="tab_act"><bean:message key="massnahme.tab.allgemein" /></html:link>
</c:if>
<c:if test="${activeTab != 'editMassnahme'}">
	<html:link action="/osb/massnahme/edit" styleClass="tab_ina">
		<bean:message key="massnahme.tab.allgemein" />
		<html:param name="massnahmeId" value="${massnahme.id}" />
	</html:link>
</c:if>

<c:if test="${activeTab == 'editMassnahmeGleissperrung'}">
    <html:link href="#" styleClass="tab_act"><bean:message key="massnahme.tab.gleissperrung" /></html:link>
</c:if>
<c:if test="${activeTab != 'editMassnahmeGleissperrung'}">
	<html:link action="/osb/massnahme/gleissperrung/edit" styleClass="tab_ina">
		<bean:message key="massnahme.tab.gleissperrung" />
		<html:param name="massnahmeId" value="${massnahme.id}" />
		<html:param name="method" value="gleissperrung" />
	</html:link>
</c:if>

<c:if test="${activeTab == 'editMassnahmeLangsamfahrstelle'}">
    <html:link href="#" styleClass="tab_act"><bean:message key="massnahme.tab.langsamfahrstelle" /></html:link>
</c:if>
<c:if test="${activeTab != 'editMassnahmeLangsamfahrstelle'}">
	<html:link action="/osb/massnahme/langsamfahrstelle/edit" styleClass="tab_ina">
		<bean:message key="massnahme.tab.langsamfahrstelle" />
		<html:param name="massnahmeId" value="${massnahme.id}" />
		<html:param name="method" value="langsamfahrstelle" />
	</html:link>
</c:if>

<c:if test="${activeTab == 'editMassnahmeOberleitung'}">
    <html:link href="#" styleClass="tab_act"><bean:message key="massnahme.tab.oberleitung" /></html:link>
</c:if>
<c:if test="${activeTab != 'editMassnahmeOberleitung'}">
	<html:link action="/osb/massnahme/oberleitung/edit" styleClass="tab_ina">
		<bean:message key="massnahme.tab.oberleitung" />
		<html:param name="massnahmeId" value="${massnahme.id}" />
		<html:param name="method" value="oberleitung" />
	</html:link>
</c:if>