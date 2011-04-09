<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>
<html:xhtml />

<jsp:include page="/pages/main_head.jsp" />
<jsp:include page="/pages/main_path.jsp" />
<jsp:include page="/pages/main_menu.jsp" />
			
<%-- Öffnet Punkt in Startmenü --%>
<script type="text/javascript">
    openMainMenu('navLink_osb_workflow-topprojekt');
</script>

<html:form action="/osb/listTopProjekte">
	<div class="textcontent_head"><bean:message key="common.filter" /></div>
	<div class="textcontent">
		<div class="textcontent_left">
			<div class="box">
				<div class="label"><bean:message key="topprojekt.sapprojektnummer" /></div>
				<div class="input">
					<html:text property="sapProjektNummer" styleId="sapProjektNummer" />
				</div>
			</div>
			<div class="box">
				<div class="label"><label for="datumVon"><bean:message key="common.letzteAenderungNach" /></label></div>
				<div class="input"><html:text property="datumVon" styleId="datumVon" styleClass="date" errorStyle="${errorStyle}" maxlength="10" /><img src="<c:url value='/static/img/calendar.gif' />" id="datumVonButton" alt="Kalender" class="calendar" /></div>
				<html:hidden property="datumBis" />
			</div>
<!--			<div class="box">-->
<!--				<div class="label"><label for="datumBis"><bean:message key="datumBis" /></label></div>-->
<!--				<div class="input"><html:text property="datumBis" styleId="datumBis" styleClass="date" errorStyle="${errorStyle}" maxlength="10" /><img src="<c:url value='/static/img/calendar.gif' />" id="datumBisButton" alt="Kalender" class="calendar" /></div>-->
<!--			</div>-->
		</div>
	</div>
	<div class="buttonBar">
		<input type="submit" class="hiddenSubmit" />
		<html:link action="/osb/listTopProjekte" styleClass="buttonReload">
			<html:param name="reset" value="true" />
			<bean:message key="button.reset" />
		</html:link>
		<html:link action="/osb/listTopProjekte" onclick="$('topProjektFilterForm').submit();return false;"
			styleClass="buttonSearch" styleId="buttonSearch">
			<bean:message key="button.search" />
		</html:link>
	</div>
</html:form>

<br/>

<div class="textcontent_head"><bean:message key="menu.topprojekte" /></div>
<div class="textcontent" style="text-align: center;">
	<bean:define id="titleDeleted"><bean:message key="common.deleted" /></bean:define>
	<bean:define id="titleView"><bean:message key="button.view" /></bean:define>
	<bean:define id="titleEdit"><bean:message key="button.edit" /></bean:define>
	<bean:define id="titleDelete"><bean:message key="button.delete" /></bean:define>
	<bean:define id="titleUndelete"><bean:message key="button.undelete" /></bean:define>
	<bean:define id="confirmText" toScope="page"><bean:message key="confirm.topprojekt.delete" /></bean:define>
	<bean:define id="blueColoredText">color:blue;</bean:define>

	<jsp:useBean id="urls" class="java.util.HashMap" />
	<display:table
		id="item"
		name="projekte"
		export="true"
		requestURI="/osb/listTopProjekte.do"
		pagesize="${projekte.objectsPerPage}"
		sort="external"
		class="colored"
		decorator="db.training.easy.util.displaytag.decorators.AddRowLink">

		<%-- URL wird erzeugt, ggf. inkl. sessionId --%>
		<c:set target="${urls}" property="${item.id}" value="#" />
		
		<%-- Überprüfung, ob Text blau gefärbt werden soll (LastChangeDate innerhalb DatumVon und Datum Bis --%>
		<c:if test="${item.lastChangeDate != null}">
			<bean:define id="lastChange"><fmt:formatDate pattern="yyyy.MM.dd" value="${item.lastChangeDate}" /></bean:define>
		</c:if>
		<c:if test="${item.lastChangeDate == null}">
			<bean:define id="lastChange">&nbsp;</bean:define>
		</c:if>
		<bean:define id="datumVon">${datumVonToCompare}</bean:define>
		<bean:define id="datumBis">${datumBisToCompare}</bean:define>
				
		<c:choose>
			<c:when test="${lastChange >= datumVon  && lastChange <= datumBis}">
				<bean:define id="blueColoredText">color:blue;</bean:define>
			</c:when>
			<c:otherwise>
				<bean:define id="blueColoredText">&nbsp;</bean:define>
			</c:otherwise>
		</c:choose>
		
		<easy:hasAuthorization model="${item}" authorization="ROLE_TOPPROJEKT_LESEN">
			<c:set target="${urls}" property="${item.id}">
				<c:url value="/osb/viewTopProjekt.do?topProjektId=${item.id}" />
			</c:set>
		</easy:hasAuthorization>

		<display:column title="" sortable="true"  sortProperty="deleted" style="${blueColoredText}">
			<c:if test="${item.deleted == true}"><img src="<c:url value='/static/img/error.png' />" title="${titleDeleted}" /></c:if>
		</display:column>
		
		<display:column property="anmelder.caption" titleKey="topprojekt.anmelder" sortable="true" sortProperty="anmelder" style="${blueColoredText}">
			
		</display:column>
		<display:column property="name" titleKey="topprojekt.name" sortable="true" style="${blueColoredText}" />
		<display:column titleKey="topprojekt.baukosten.br" sortProperty="baukosten" sortable="true" style="${blueColoredText}">
			<bean:write name="item" property="baukosten" format="#,##0.000" />
		</display:column>
		<display:column property="sapProjektNummer" titleKey="topprojekt.sapProjektNummer" sortable="true" style="${blueColoredText}" />
		<display:column property="regionalbereich.name" sortProperty="regionalbereich" titleKey="topprojekt.regionalbereich" sortable="true" style="${blueColoredText}" />

		<display:column style="text-align:right;width:15px;${blueColoredText}" media="html">
			<easy:hasAuthorization model="${item}" authorization="ROLE_TOPPROJEKT_LESEN">
		 		<html:link action="/osb/viewTopProjekt" styleClass="show" title="${titleView}">
		 			<html:param name="topProjektId" value="${item.id}" />
		 			&nbsp;
		 		</html:link>
		 	</easy:hasAuthorization>
		</display:column>

		<display:column style="text-align:right;width:15px;${blueColoredText}" media="html">
			<easy:hasAuthorization model="${item}" authorization="ROLE_TOPPROJEKT_BEARBEITEN">
		 		<html:link action="/osb/editTopProjekt" styleClass="edit" title="${titleEdit}">
		 			<html:param name="topProjektId" value="${item.id}" />
		 			&nbsp;
		 		</html:link>
		 	</easy:hasAuthorization>
		</display:column>

		<display:column style="text-align:right;width:15px;${blueColoredText}" media="html">
			<easy:hasAuthorization model="${item}" authorization="ROLE_TOPPROJEKT_LOESCHEN">
				<c:choose>
					<c:when test="${item.deleted == true}">
				 		<html:link action="/osb/deleteTopProjekt" styleClass="undo" title="${titleUndelete}">
				 			<html:param name="topProjektId" value="${item.id}" />
				 			<html:param name="delete" value="false" />
				 			&nbsp;
				 		</html:link>
					</c:when>
					<c:when test="${item.deleted == false}">
				 		<html:link action="/osb/deleteTopProjekt" styleClass="delete" onclick="return confirmLink(this.href, '${confirmText}');" title="${titleDelete}">
				 			<html:param name="topProjektId" value="${item.id}" />
				 			<html:param name="delete" value="true" />
				 			&nbsp;
				 		</html:link>
					</c:when>
				</c:choose>
		 	</easy:hasAuthorization>
		</display:column>
	</display:table>	
</div>

<c:choose>
	<c:when test="${!empty regionalrechtEnabled && regionalrechtEnabled == true}">
		<authz:authorize ifAnyGranted="ROLE_TOPPROJEKT_ANLEGEN_ALLE, ROLE_TOPPROJEKT_ANLEGEN_REGIONALBEREICH, ROLE_TOPPROJEKT_ANLEGEN_TEMPORAER">
			<div class="buttonBar">
				<html:link action="/osb/editTopProjekt" styleClass="buttonAdd">
					<html:param name="topProjektId"></html:param>
					<bean:message key="button.create" />
				</html:link>
			</div>
		</authz:authorize>
	</c:when>
	<c:otherwise>
		<authz:authorize ifAnyGranted="ROLE_TOPPROJEKT_ANLEGEN_ALLE, ROLE_TOPPROJEKT_ANLEGEN_REGIONALBEREICH">
			<div class="buttonBar">
				<html:link action="/osb/editTopProjekt" styleClass="buttonAdd">
					<html:param name="topProjektId"></html:param>
					<bean:message key="button.create" />
				</html:link>
			</div>
		</authz:authorize>
	</c:otherwise>
</c:choose>
<script type="text/javascript">
	Calendar.setup({ inputField: "datumVon", button: "datumVonButton" });
	//Calendar.setup({ inputField: "datumBis", button: "datumBisButton" });
</script>
<jsp:include page="/pages/main_footer.jsp" />