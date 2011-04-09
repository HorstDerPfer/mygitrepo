<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>
<html:xhtml />

<jsp:include page="/pages/main_head.jsp" />
<jsp:include page="/pages/main_path.jsp" />
<jsp:include page="/pages/main_menu.jsp" />
			

<%-- Öffnet Punkt in Startmenü --%>
<script type="text/javascript">
    openMainMenu('navLink_osb_workflow-fahrplanregelung');
</script>
<script type="text/javascript">
	function setUmleitungId(nummer) {
		var inputUmleitungId = document.getElementById('umleitungId');
		inputUmleitungId.value=nummer;
	}
</script>

<div class="textcontent_head">
	<bean:message key="menu.umleitungenToFahrplanregelung.add" />
</div>

<html:form action="/addUmleitungToFahrplanregelung">
	<html:hidden property="fahrplanregelungId" styleId="fahrplanregelungId" />
	<html:hidden property="umleitungId" styleId="umleitungId"/>

	<div class="textcontent" style="height:100px;border-bottom:0px;" >
		<div class="textcontent_left" >
			<div class="box">
				<div class="label" style="width:180px;"><label for="anzahlSGV"><bean:message key="umleitungenToFahrplanregelung.anzahlSGV" /></label></div>
				<div class="input"><html:text property="anzahlSGV" styleId="anzahlSGV" styleClass="small" errorStyle="${errorStyle}" /></div>
			</div>
		
			<div class="box">
				<div class="label" style="width:180px;"><label for="anzahlSPNV"><bean:message key="umleitungenToFahrplanregelung.anzahlSPNV" /></label></div>
				<div class="input"><html:text property="anzahlSPNV" styleId="anzahlSPNV" styleClass="small" errorStyle="${errorStyle}" /></div>
			</div>
		
			<div class="box">
				<div class="label" style="width:180px;"><label for="anzahlSPFV"><bean:message key="umleitungenToFahrplanregelung.anzahlSPFV" /></label></div>
				<div class="input"><html:text property="anzahlSPFV" styleId="anzahlSPFV" styleClass="small" errorStyle="${errorStyle}" /></div>
			</div>
		</div>
		<div class="textcontent_right" >
			<div class="box">
				<div class="label" style="width:180px;"><label for="anzahlSGVGegenRich"><bean:message key="umleitungenToFahrplanregelung.anzahlSGVGegenRich" /></label></div>
				<div class="input"><html:text property="anzahlSGVGegenRich" styleId="anzahlSGVGegenRich" styleClass="small" errorStyle="${errorStyle}" /></div>
			</div>
		
			<div class="box">
				<div class="label" style="width:180px;"><label for="anzahlSPNVGegenRich"><bean:message key="umleitungenToFahrplanregelung.anzahlSPNVGegenRich" /></label></div>
				<div class="input"><html:text property="anzahlSPNVGegenRich" styleId="anzahlSPNVGegenRich" styleClass="small" errorStyle="${errorStyle}" /></div>
			</div>
		
			<div class="box" >
				<div class="label" style="width:180px;"><label for="anzahlSPFVGegenRich"><bean:message key="umleitungenToFahrplanregelung.anzahlSPFVGegenRich" /></label></div>
				<div class="input"><html:text property="anzahlSPFVGegenRich" styleId="anzahlSPFVGegenRich" styleClass="small" errorStyle="${errorStyle}" /></div>
			</div>
		</div>
	</div>
	
	<div class="textcontent" style="text-align: center;border-top:0px;">
		<jsp:useBean id="urls" class="java.util.HashMap" />
	
		<display:table id="currentUmleitung" 
			name="umleitungen" 
			export="false"
			requestURI="${requestURI}" 
			pagesize="20" 
			sort="external" 
			class="colored"
			decorator="db.training.easy.util.displaytag.decorators.AddRowLink">
	
			<%-- URL wird erzeugt, ggf. inkl. sessionId --%>
			<c:set target="${urls}" property="${currentUmleitung.id}">
				<c:url value="viewUmleitung.do?umleitungId=${currentUmleitung.id}" />
			</c:set>
	
			<display:column property="name" titleKey="umleitung.titel.short" sortable="true" media="html" />
			<display:column property="relation" titleKey="osb.umleitung.relation" sortable="false" />
			<display:column media="html" sortable="false">
				<input type="radio" name="radio_box" styleClass="checkbox" styleId="radio_${currentUmleitung.id}"
	    			<c:if test="${umleitungId == currentUmleitung.id}">checked="checked"</c:if>
	        		style="width:10px;border:0px" onclick="setUmleitungId(${currentUmleitung.id});" />
			</display:column>
			
			<display:setProperty name="paging.banner.all_items_found" value="" />
		</display:table>
	</div>
</html:form>

<div class="buttonBar">
	<html:link action="/osb/fahrplanregelung/edit" styleClass="buttonBack">
		<bean:message key="button.back" />
		<html:param name="fahrplanregelungId" value="${fahrplanregelungForm.fahrplanregelungId}"></html:param>
	</html:link>
	<easy:hasAuthorization model="${fahrplanregelung}" authorization="ROLE_FAHRPLANREGELUNG_BEARBEITEN">
		<html:link href="javascript:document.getElementById('umleitungenToFahrplanregelungForm').submit();" styleClass="buttonPlus"><bean:message key="button.create" /></html:link>
	</easy:hasAuthorization>
</div>

<jsp:include page="/pages/main_footer.jsp" />