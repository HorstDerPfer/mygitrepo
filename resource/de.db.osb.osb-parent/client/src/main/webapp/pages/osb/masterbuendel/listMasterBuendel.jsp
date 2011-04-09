<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>
<html:xhtml />

<jsp:include page="../../main_head.jsp" />
<jsp:include page="../../main_path.jsp" />
<jsp:include page="../../main_menu.jsp" />

<%-- Öffnet Punkt in Startmenü --%>
<script type="text/javascript">
			    openMainMenu('navLink_osb-masterbuendel');
			</script>

<div class="textcontent_head">
	<bean:message key="menu.masterbuendel.list" />
</div>

<div class="textcontent" style="text-align: center;">

	<jsp:useBean id="urls" class="java.util.HashMap" />

	<display:table id="currentMasterBuendel" name="masterBuendel" export="false"
		requestURI="listMasterBuendel.do" pagesize="20" sort="list" class="colored"
		decorator="db.training.easy.util.displaytag.decorators.AddRowLink">

		<%-- URL wird erzeugt, ggf. inkl. sessionId --%>
		<c:set target="${urls}" property="${currentMasterBuendel.id}" value="#" />

		<c:if test="${currentMasterBuendel != null}">
			<c:set target="${urls}" property="${currentMasterBuendel.id}">
				<c:url value="viewMasterBuendel.do?masterBuendelId=${currentMasterBuendel.id}" />
			</c:set>
		</c:if>

		<display:column property="id" titleKey="masterbuendel.id" sortable="true" />
		<display:column property="id" titleKey="masterbuendel.name" sortable="true" />
		<display:column style="width:20px;" class="right">
			<html:link action="/editMasterBuendel" paramId="masterBuendelId" paramName="currentMasterBuendel"
				paramProperty="id" styleClass="edit" titleKey="button.edit">&nbsp;</html:link>
		</display:column>

		<display:setProperty name="basic.empty.showtable" value="true" />
		<display:setProperty name="paging.banner.item_name">
			<bean:message key="user" />
		</display:setProperty>
		<display:setProperty name="paging.banner.items_name">
			<bean:message key="user" />
		</display:setProperty>
	</display:table>

</div>

<div class="buttonBar">
	<bean:define id="masterBuendelId" value="0" />
	<html:link action="/editMasterBuendel" paramId="masterBuendelId" paramName="masterBuendelId"
		styleClass="buttonAdd">
		<bean:message key="button.masterbuendel.add" />
	</html:link>
</div>

<jsp:include page="../../main_footer.jsp" />