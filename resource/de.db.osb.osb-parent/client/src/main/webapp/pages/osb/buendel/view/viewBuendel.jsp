<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>
<html:xhtml />

<jsp:include page="/pages/main_head.jsp" />
<jsp:include page="/pages/main_path.jsp" />
<jsp:include page="/pages/main_menu.jsp" />

<%-- Öffnet Punkt in Startmenü --%>
<script type="text/javascript">
    openMainMenu('navLink_osb_workflow-buendel');
</script>

<div class="textcontent_head">
	<span style="float:left"><bean:message key="buendel" /></span>
	<c:if test="${buendel != null}">
		<span style="float:right;">
			<c:if test="${buendel.deleted == true}">
				<bean:message key="common.deleted" />&nbsp;-&nbsp;
			</c:if>
			<bean:message key="common.lastchangedate" />:&nbsp;
			<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${buendel.lastChangeDate}" />
		</span>	
	</c:if>
</div>
<div class="textcontent">
	<jsp:include page="viewBuendelAllgemein.jsp" />
</div>

<div class="buttonBar">
	<html:link action="/back" styleClass="buttonBack"><bean:message key="button.back" /></html:link>

		<c:if test="${buendel != null && buendel.id != null}">
			<bean:define id="gruppenlfwUrl">
				<bean:message bundle="configResources" key="gruppenlfw.${session_fahrplanjahr}" arg0="${buendel.buendelId}"/>
			</bean:define>			
			<html:link href="#" onclick="javascript:window.open('${gruppenlfwUrl}')" styleClass="buttonFolder">
				<bean:message key="gruppenlfw" />
			</html:link>
		</c:if>

	<easy:hasAuthorization model="${buendel}" authorization="ROLE_BUENDEL_ANLEGEN">
		<bean:define id="confirmTextCopy" toScope="page"><bean:message key="confirm.buendel.copy" /></bean:define>
		<html:link action="/osb/copyBuendel" styleClass="buttonAdd" onclick="return confirmLink(this.href, '${confirmTextCopy}');">
			<html:param name="buendelId" value="${buendel.id}" />
			<bean:message key="button.copy" />
		</html:link>
	</easy:hasAuthorization>
	
	<easy:hasAuthorization model="${buendel}" authorization="ROLE_BUENDEL_BEARBEITEN">
		<html:link action="/osb/editBuendel" styleClass="buttonEdit">
			<html:param name="buendelId" value="${buendel.id }" />
			<bean:message key="button.edit" />
		</html:link>
	</easy:hasAuthorization>
</div>

<br/>

<%-- Karteireiter -----------------------------------------------------------------------------------------------------%>
<html:link href="#" onclick="javascript:showTabDiv('Gleissperrungen');" styleId="tabLinkGleissperrungen" styleClass="tab_act">
	<bean:message key="buendel.tab.gleissperrungen" />
</html:link>
<html:link href="#" onclick="javascript:showTabDiv('Buendelungsgrad');" styleId="tabLinkBuendelungsgrad" styleClass="tab_ina">
	<bean:message key="buendel.tab.buendelungsgrad" />
</html:link>
<html:link href="#" onclick="javascript:showTabDiv('Fahrplanregelungen');" styleId="tabLinkFahrplanregelungen" styleClass="tab_ina">
	<bean:message key="fahrplanregelungen" />
</html:link>

<div class="textcontent center" id="tabDivGleissperrungen">
	<jsp:include page="viewBuendelGleissperrungen.jsp" />
</div>

<div class="textcontent" id="tabDivBuendelungsgrad" style="display: none;">
	<jsp:include page="viewBuendelBuendelungsgrad.jsp" />
</div>

<div class="textcontent center" id="tabDivFahrplanregelungen" style="display: none;">
	<jsp:include page="../edit/editBuendelFahrplanregelungen.jsp" />
</div>

<jsp:include page="/pages/main_footer.jsp" />