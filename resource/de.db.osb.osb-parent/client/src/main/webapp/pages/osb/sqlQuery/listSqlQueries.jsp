<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy" %>
<html:xhtml/>

<jsp:include page="../../main_head.jsp"/>
	<jsp:include page="../../main_path.jsp"/>
		<jsp:include page="../../main_menu.jsp"/>

			<%-- Öffnet Punkt in Startmenü --%>
			<script type="text/javascript">
			    openMainMenu('navLink_osb_daten-sqlquery');
			</script>
			
			<div class="textcontent_head">
			    <bean:message key="menu.osb.sqlQueries" />
			</div>

	<br/>

	<input type="hidden" id="activeTabDiv" name="activeTabDiv" value="<%out.print(request.getParameter("activeTabDiv")!=null?request.getParameter("activeTabDiv"):"MENGENGERUEST");%>">

	<html:link href="#" onclick="showTabDiv('MENGENGERUEST');$('activeTabDiv').value='MENGENGERUEST'" styleId="tabLinkMENGENGERUEST" styleClass="tab_act">
		<bean:message key="osb.sqlQueries.MENGENGERUEST"/>
	</html:link>
	<html:link href="#" onclick="showTabDiv('FORTSCHRITTSKENNZAHL');$('activeTabDiv').value='FORTSCHRITTSKENNZAHL'" styleId="tabLinkFORTSCHRITTSKENNZAHL" styleClass="tab_ina">
		<bean:message key="osb.sqlQueries.FORTSCHRITTSKENNZAHL"/>
	</html:link>
	<html:link href="#" onclick="showTabDiv('DATENAUSZUG');$('activeTabDiv').value='DATENAUSZUG'" styleId="tabLinkDATENAUSZUG" styleClass="tab_ina">
		<bean:message key="osb.sqlQueries.DATENAUSZUG"/>
	</html:link>

    <div class="textcontent" id="tabDivMENGENGERUEST">
		<jsp:include page="listSqlQueriesMengengeruest.jsp" />
	</div>
	<div class="textcontent" id="tabDivFORTSCHRITTSKENNZAHL" style="display:none">
		<jsp:include page="listSqlQueriesFortschrittskennzahlen.jsp" />
	</div>
	<div class="textcontent" id="tabDivDATENAUSZUG" style="display:none">
		<jsp:include page="listSqlQueriesDatenauszuege.jsp" />
	</div>

	<div class="buttonBar">
		<authz:authorize ifAnyGranted="ROLE_SQLQUERY_ANLEGEN_ALLE">
			<html:link href="#" onclick="location.href='editSqlQuery.do?sqlQueryId=0&cluster=' + $('activeTabDiv').value" styleClass="buttonAdd"><bean:message key="button.create" /></html:link>
		</authz:authorize>
	</div>

<script type="text/javascript">
	if ($('activeTabDiv').value != null && $('activeTabDiv').value != "")
		showTabDiv($('activeTabDiv').value,'','1');
</script>

<jsp:include page="../../main_footer.jsp"/>