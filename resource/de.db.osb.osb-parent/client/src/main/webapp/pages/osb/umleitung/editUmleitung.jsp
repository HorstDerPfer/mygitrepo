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

<script type="text/javascript">
	function submitSaveUmleitung() {
		var saveFlagObj = document.getElementById('saveFlag');
		saveFlagObj.value=1;
		document.getElementById('umleitungForm').submit();
	}
	
	function submitAddUmleitungsWeg() {
		var saveFlagObj = document.getElementById('saveFlag');
		saveFlagObj.value=0;
		document.getElementById('umleitungForm').submit();
	}
</script>

<html:form action="/saveUmleitung?sp=true">
	<div class="textcontent_head">
		<span style="float:left">
			<logic:equal name="umleitungForm" property="umleitungId" value="0"><bean:message key="umleitung.create" /></logic:equal>
			<logic:notEqual name="umleitungForm" property="umleitungId" value="0"><bean:message key="umleitung.edit" /></logic:notEqual>
		</span>
		<c:if test="${umleitung != null && umleitung.deleted == true}">
			<span style="float:right;"><bean:message key="common.deleted" /></span>
		</c:if>
	</div>
	<div class="textcontent">
		<html:hidden property="saveFlag" styleId="saveFlag" />
		
		<div class="textcontent_left" >
			<div class="box">
				<div class="label"><label for="umleitungName"><bean:message key="umleitung.titel.short" /></label></div>
				<div class="input">
					<logic:equal name="umleitungForm" property="umleitungId" value="0">
						<html:text property="umleitungName" styleId="umleitungName" styleClass="small" errorStyle="${errorStyle}" />
					</logic:equal>
					<logic:notEqual name="umleitungForm" property="umleitungId" value="0">
						<html:text property="umleitungName" styleId="umleitungName" styleClass="small" disabled="true" />
					</logic:notEqual>
				</div>
			</div>
	
			<div class="box">
				<div class="label"><label for="freieKapaRichtung"><bean:message key="umleitung.freieKapaRichtung" /></label></div>
				<div class="input"><html:text property="freieKapaRichtung" styleId="freieKapaRichtung" styleClass="small" errorStyle="${errorStyle}" /></div>
			</div>
	
			<div class="box">
				<div class="label"><label for="freieKapaGegenrichtung"><bean:message key="umleitung.freieKapaGegenrichtung" /></label></div>
				<div class="input"><html:text property="freieKapaGegenrichtung" styleId="freieKapaGegenrichtung" styleClass="small" errorStyle="${errorStyle}" /></div>
			</div>
		</div>
		<div class="textcontent_right">
			<logic:present name="umleitung">
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
			</logic:present>				
		</div>
	</div>
	<div class="buttonBar">
		<html:link action="/back" styleClass="buttonBack"><bean:message key="button.back" /></html:link>

		<logic:present name="umleitung">
			<easy:hasAuthorization model="${umleitung}" authorization="ROLE_UMLEITUNG_LOESCHEN">
				<c:choose>
					<c:when test="${umleitung.deleted == true}">
						<html:link action="/deleteUmleitung" styleClass="buttonReload">
							<html:param name="umleitungId" value="${umleitung.id}" />
							<html:param name="delete" value="false" />
							<bean:message key="button.undelete" />
						</html:link>
					</c:when>
					<c:when test="${umleitung.deleted == false}">
						<bean:define id="confirmText" toScope="page"><bean:message key="confirm.umleitung.delete" /></bean:define>
						<html:link action="/deleteUmleitung" styleClass="buttonDelete" onclick="return confirmLink(this.href, '${confirmText}');">
							<html:param name="umleitungId" value="${umleitung.id}" />
							<html:param name="delete" value="true" />
							<bean:message key="button.delete" />
						</html:link>
					</c:when>
				</c:choose>
			</easy:hasAuthorization>
			
			<easy:hasAuthorization model="${umleitung}" authorization="ROLE_UMLEITUNG_BEARBEITEN">
				<html:link href="javascript:submitSaveUmleitung();" styleClass="buttonSave"><bean:message key="button.save" /></html:link>
			</easy:hasAuthorization>
		</logic:present>
		
		<logic:notPresent name="umleitung">
			<html:link href="javascript:submitSaveUmleitung();" styleClass="buttonSave"><bean:message key="button.save" /></html:link>
		</logic:notPresent>
	</div>
	
	<br />
	
	<div class="textcontent_head"><bean:message key="osb.umleitungswege" /></div>
	<div class="textcontent" >
		<bean:define id="titleRemove"><bean:message key="button.remove" /></bean:define>
		<bean:define id="confirmText" toScope="page"><bean:message key="confirm.umleitungsweg.remove" /></bean:define>

		<jsp:useBean id="urls" class="java.util.HashMap" />
	
		<display:table
			id="current" 
			name="umleitung.umleitungswegeSorted"
			export="false" 
			requestURI="${requestURI}"
			pagesize="20" 
			sort="list" class="colored"
			decorator="db.training.easy.util.displaytag.decorators.AddRowLink">
	
			<display:column property="vzgStrecke.nummer" titleKey="umleitung.vzgStrecke" sortable="false" />
			<display:column property="betriebsstelleVon.caption" titleKey="umleitung.betriebsstelleVon" sortable="false" />
			<display:column property="betriebsstelleBis.caption" titleKey="umleitung.betriebsstelleBis" sortable="false" />
			
			<display:column style="text-align:right;width:15px;" media="html">
				<easy:hasAuthorization model="${current}" authorization="ROLE_UMLEITUNGSWEG_LOESCHEN">
					<html:link action="/deleteUmleitungsweg" styleClass="delete" onclick="return confirmLink(this.href, '${confirmText}');" title="${titleRemove}">
						<html:param name="umleitungId" value="${umleitung.id}" />
						<html:param name="umleitungswegId" value="${current.id}" />
						&nbsp;
					</html:link>
				</easy:hasAuthorization>
			</display:column>
			
			<display:setProperty name="paging.banner.all_items_found" value="" />
		</display:table>
	</div>
		
	<authz:authorize ifAnyGranted="ROLE_UMLEITUNG_ANLEGEN_ALLE,ROLE_UMLEITUNG_ANLEGEN_REGIONALBEREICH,ROLE_UMLEITUNG_BEARBEITEN_ALLE,ROLE_UMLEITUNG_BEARBEITEN_REGIONALBEREICH,ROLE_UMLEITUNGSWEG_ANLEGEN_ALLE,ROLE_UMLEITUNGSWEG_ANLEGEN_REGIONALBEREICH">
		<br/>
		
		<div class="textcontent_head"><bean:message key="osb.umleitungsweg.add" /></div>
		<div class="textcontent">
			<div class="box">
				<div class="label"><label for="vzgStrecke"><bean:message key="umleitung.vzgStrecke" /></label></div>
				<div class="input">
					<html:text property="vzgStrecke" styleId="vzgStrecke" errorStyle="${errorStyle}" />
					<div id="vzgStreckeSelect" class="autocomplete"></div>
				</div>
				<img src="<c:url value='./static/img/indicator.gif' />" id="vzgStreckeIndicator" style="display:none;" />
			</div>
			
			<div class="box">
				<div class="label"><label for="betriebsstelleVon"><bean:message key="umleitung.betriebsstelleVon" /></label></div>
				<div class="input">
					<html:text property="betriebsstelleVon" styleId="betriebsstelleVon" maxlength="30" errorStyle="${errorStyle}" />
					<div id="betriebsstelleVonSelect" class="autocomplete"></div>
				</div>
				<img src="<c:url value='./static/img/indicator.gif' />" id="betriebsstelleVonIndicator" style="display:none;" />
			</div>
			
			<div class="box">
				<div class="label"><label for="betriebsstelleBis"><bean:message key="umleitung.betriebsstelleBis" /></label></div>
				<div class="input">
					<html:text property="betriebsstelleBis" styleId="betriebsstelleBis" maxlength="30" errorStyle="${errorStyle}" />
					<div id="betriebsstelleBisSelect" class="autocomplete"></div>
				</div>
				<img src="<c:url value='./static/img/indicator.gif' />" id="betriebsstelleBisIndicator" style="display:none;" />
			</div>
		</div>
	
		<div class="buttonBar">
			<html:link href="javascript:submitAddUmleitungsWeg();" styleClass="buttonPlus"><bean:message key="button.create" /></html:link>
		</div>
	</authz:authorize>
</html:form>

<c:set var="urlAutocompleteVzgStrecke" scope="request"><c:url value='/AutoCompleteStreckeVzg.view'/></c:set>
<c:set var="urlAutocompleteBetriebsstelle" scope="request"><c:url value='/autocompleteOrtsbezeichnungen.view'/></c:set>
<script type="text/javascript">
	new Ajax.Autocompleter("vzgStrecke", "vzgStreckeSelect", "${urlAutocompleteVzgStrecke}", { indicator: 'vzgStreckeIndicator', minChars: 2, paramName: 'nummer' });
	new Ajax.Autocompleter("betriebsstelleVon", "betriebsstelleVonSelect", "${urlAutocompleteBetriebsstelle}", { indicator: 'betriebsstelleVonIndicator', minChars: 2, paramName: 'keyword' });
	new Ajax.Autocompleter("betriebsstelleBis", "betriebsstelleBisSelect", "${urlAutocompleteBetriebsstelle}", { indicator: 'betriebsstelleBisIndicator', minChars: 2, paramName: 'keyword' });					  
</script>

<jsp:include page="../../main_footer.jsp"/>