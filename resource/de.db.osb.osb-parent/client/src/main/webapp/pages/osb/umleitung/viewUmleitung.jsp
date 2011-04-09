<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
<html:xhtml/>

<jsp:include page="../../main_head.jsp"/>
<jsp:include page="../../main_path.jsp"/>
<jsp:include page="../../main_menu.jsp"/>

<%-- Öffnet Punkt in Startmenü --%>
<script type="text/javascript">
    openMainMenu('navLink_osb_workflow-umleitung');
</script>		

<div class="textcontent_head">
	<span style="float:left"><bean:message key="umleitung.edit" /></span>
	<c:if test="${umleitung != null && umleitung.deleted == true}">
		<span style="float:right;"><bean:message key="common.deleted" /></span>
	</c:if>
</div>
<div class="textcontent">
	<html:hidden name="umleitungForm" property="saveFlag" styleId="saveFlag" />

	<div class="textcontent_left">
		<div class="box">
			<div class="label"><label for="umleitungName"><bean:message key="umleitung.titel.short" /></label></div>
			<div class="show">${umleitungForm.umleitungName}</div>
		</div>
	
		<div class="box">
			<div class="label"><label for="freieKapaRichtung"><bean:message key="umleitung.freieKapaRichtung" /></label></div>
			<div class="show">${umleitungForm.freieKapaRichtung}</div>
		</div>
	
		<div class="box">
			<div class="label"><label for="freieKapaGegenrichtung"><bean:message key="umleitung.freieKapaGegenrichtung" /></label></div>
			<div class="show">${umleitungForm.freieKapaGegenrichtung}</div>
		</div>
	</div>
	<div class="textcontent_right">
		<div class="box">
			<div class="label">
				<label>
					<bean:message key="umleitung.gueltigVon" />
					<img src="<c:url value='static/img/icon_s_info_small.gif' />" title="<bean:message key="umleitung.gueltigInfo" />" />	
				</label>
			</div>
			<div class="show" style="width:55px"><bean:write name="umleitung" property="gueltigVon" bundle="configResources" formatKey="dateFormat.long" /></div>
		</div>
		<div class="box">
			<div class="label">
				<label>
					<bean:message key="umleitung.gueltigBis" />
					<img src="<c:url value='static/img/icon_s_info_small.gif' />" title="<bean:message key="umleitung.gueltigInfo" />" />	
				</label>
			</div>
			<div class="show" style="width:55px"><bean:write name="umleitung" property="gueltigBis" bundle="configResources" formatKey="dateFormat.long" /></div>
		</div>
	</div>
</div>

<br />

<div class="textcontent_head"><bean:message key="osb.umleitungswege" /></div>
<div class="textcontent" style="text-align:center">
	<jsp:useBean id="urls" class="java.util.HashMap" />

	<display:table id="current" 
		name="umleitung.umleitungswegeSorted"
		export="false" 
		requestURI="${requestURI}"
		pagesize="20" 
		sort="list" 
		class="colored"
		decorator="db.training.easy.util.displaytag.decorators.AddRowLink">

		<display:column property="vzgStrecke.nummer" titleKey="umleitung.vzgStrecke" sortable="false" />
		<display:column property="betriebsstelleVon.caption" titleKey="umleitung.betriebsstelleVon" sortable="false" />
		<display:column property="betriebsstelleBis.caption" titleKey="umleitung.betriebsstelleBis" sortable="false" />

		<display:setProperty name="paging.banner.all_items_found" value="" />
	</display:table>
</div>

<div class="buttonBar">
	<html:link action="/back" styleClass="buttonBack"><bean:message key="button.back" /></html:link>
	
	<logic:notEqual name="umleitungForm" property="umleitungId" value="0">
		<easy:hasAuthorization model="${umleitung}" authorization="ROLE_UMLEITUNG_BEARBEITEN">
			<html:link action="editUmleitung" styleClass="buttonEdit">
				<bean:message key="button.edit" />
				<html:param name="umleitungId" value="${umleitung.id}" />
			</html:link>
		</easy:hasAuthorization>
	</logic:notEqual>
</div>

<jsp:include page="../../main_footer.jsp"/>