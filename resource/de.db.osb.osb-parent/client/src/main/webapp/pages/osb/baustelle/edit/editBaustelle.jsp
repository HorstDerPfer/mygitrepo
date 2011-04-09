<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
<html:xhtml/>

<jsp:include page="/pages/main_head.jsp"/>
<jsp:include page="/pages/main_path.jsp"/>
<jsp:include page="/pages/main_menu.jsp"/>
			
<script type="text/javascript">
    openMainMenu('navLink_osb_workflow-baustelle');
</script>

<html:form action="/osb/baustelle/save?sp=true" acceptCharset="UTF-8" focus="name" >
	<div class="textcontent_head"><bean:message key="baustelle.name" />: ${baustelle.name}</div>
	<div class="textcontent">
		<div class="textcontent_left">
			<div class="box">
				<div class="label"><bean:message key="baustelle.lfdnr" /></div>
				<div class="show">${baustelle.lfdNr}</div>
			</div>
			<div class="box">
				<div class="label"><bean:message key="baustelle.name" /></div>
				<div class="input"><html:text property="name" styleId="name" /></div>
			</div>
		</div>
	</div>
	<div class="buttonBar">
		<html:link action="/back.do" styleClass="buttonBack"><bean:message key="button.back" /></html:link>
		
		<easy:hasAuthorization model="${baustelle}" authorization="ROLE_BAUSTELLE_BEARBEITEN">
			<html:link href="#" onclick="$('baustelleForm').submit();" styleClass="buttonSave">
				<bean:message key="button.save" />
			</html:link>
		</easy:hasAuthorization>
	</div>
	
	<br />
	
	<div class="textcontent_head"><bean:message key="menu.gleissperrung" /></div>
	<div class="textcontent center" id="tabDivGleissperrungen">
		<bean:define id="titleFixiert"><bean:message key="common.fixiert" /></bean:define>
		<bean:define id="titleDeleted"><bean:message key="common.deleted" /></bean:define>
		<bean:define id="titleView"><bean:message key="button.view" /></bean:define>
		<bean:define id="titleRemove"><bean:message key="button.remove" /></bean:define>
		<bean:define id="confirmText" toScope="page"><bean:message key="confirm.gleissperrung.remove" /></bean:define>

		<jsp:useBean id="urls" class="java.util.HashMap" />
	
		<display:table id="currentGleissperrung" 
			name="${baustelle.gleissperrungen}"
			export="false" 
			requestURI="${requestURI}" 
			pagesize="20" 
			sort="external"
			class="colored"
			decorator="db.training.easy.util.displaytag.decorators.AddRowLink">
	
			<%-- URL wird erzeugt, ggf. inkl. sessionId --%>
			<c:set target="${urls}" property="${currentGleissperrung.id}" value="#" />
	
			<easy:hasAuthorization model="${currentGleissperrung}" authorization="ROLE_GLEISSPERRUNG_LESEN">
				<c:set target="${urls}" property="${currentGleissperrung.id}">
					<c:url value="/osb/viewGleissperrung.do?gleissperrungId=${currentGleissperrung.id}" />
				</c:set>
			</easy:hasAuthorization>

			<display:column title="" sortable="false">
				<c:if test="${currentGleissperrung.massnahme != null && currentGleissperrung.massnahme.genehmiger != null}"><img src="<c:url value='/static/img/icon_lock.gif' />" title="${titleFixiert}" /></c:if>
				<c:if test="${currentGleissperrung.deleted == true}"><img src="<c:url value='/static/img/error.png' />" title="${titleDeleted}" /></c:if>
			</display:column>
			
			<display:column property="bstVon.caption" titleKey="regelung.bstVon" sortable="false" media="html" />
			<display:column property="bstBis.caption" titleKey="regelung.bstBis" sortable="false" media="html" />
			<display:column property="zeitVon" format="{0, date, dd.MM.yyyy HH:mm}" titleKey="regelung.zeitVon" sortable="false" media="html" />
			<display:column property="zeitBis" format="{0, date, dd.MM.yyyy HH:mm}" titleKey="regelung.zeitBis" sortable="false" media="html" />
			
			<display:column style="text-align:right;width:15px;" media="html">
				<easy:hasAuthorization model="${currentGleissperrung}" authorization="ROLE_GLEISSPERRUNG_LESEN">
			 		<html:link action="/osb/viewGleissperrung" styleClass="show" title="${titleView}">
			 			<html:param name="gleissperrungId" value="${currentGleissperrung.id}" />
			 			&nbsp;
			 		</html:link>
			 	</easy:hasAuthorization>
			</display:column>
			
			<display:column style="text-align:right;width:15px;" media="html">
				<html:link action="/osb/deleteGleissperrungFromBaustelle" styleClass="delete" onclick="return confirmLink(this.href, '${confirmText}');" title="${titleRemove}">
					<html:param name="baustelleId" value="${baustelle.id}" />
					<html:param name="gleissperrungId" value="${currentGleissperrung.id}" />
					&nbsp;
				</html:link>
			</display:column>
		</display:table>
		
		<c:if test="${baustelle.id != null && baustelle.id != 0}">
			<div class="buttonBar" style="width:90px;float:right;">
				<html:link action="/osb/addGleissperrungenToBaustelle.do" styleClass="buttonPlus" style="margin:0px;">
					<bean:message key="button.create" />
					<html:param name="baustelleId" value="${baustelle.id}" />
					<html:param name="sp" value="true" />
				</html:link>
			</div>
		</c:if>
	</div>
	
</html:form>

<jsp:include page="/pages/main_footer.jsp"/>