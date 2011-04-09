<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>
<html:xhtml />

<jsp:include page="/pages/main_head.jsp" />
<jsp:include page="/pages/main_path.jsp" />
<jsp:include page="/pages/main_menu.jsp" />
			
<div class="textcontent_head">
	<logic:present name="buendelId">
		<script type="text/javascript">
			openMainMenu('navLink_osb_workflow-buendel');
		</script>
		<bean:message key="menu.fahrplanregelungen.list.buendel" />
	</logic:present>
	<logic:notPresent name="buendelId">
		<script type="text/javascript">
			openMainMenu('navLink_osb_workflow-fahrplanregelung');
		</script>
		<bean:message key="fahrplanregelungen" />
	</logic:notPresent>
</div>
	
<html:form action="/listFahrplanregelungenSearch" method="post">
	<div class="textcontent">
		<div class="textcontent_left">
			<div class="box">
				<div class="label"><label for="name"><bean:message key="fahrplanregelung.name" /></label></div>
				<div class="input"><html:text property="name" styleId="name" /></div>
			</div>
			<div class="box">
				<div class="label"><label for="nummer"><bean:message key="fahrplanregelung.nummer" /></label></div>
				<div class="input"><html:text property="nummer" styleId="nummer" /></div>
			</div>
			<div class="box">
				<div class="label"><label for="datumVon"><bean:message key="common.letzteAenderungNach" /></label></div>
				<div class="input"><html:text property="datumVon" styleId="datumVon" styleClass="date" errorStyle="${errorStyle}" maxlength="10" /><img src="<c:url value='/static/img/calendar.gif' />" id="datumVonButton" alt="Kalender" class="calendar" /></div>
				<html:hidden property="datumBis" />
			</div>
		</div>
		
		<div class="textcontent_right">
			<div class="label"><label for="regionalbereichId"><bean:message key="buendel.regionalbereich" /></label></div>
			<div class="input">
				<html:select property="regionalbereichId" styleId="regionalbereichId">
					<html:option value="0"><bean:message key="common.select.option" /></html:option>
					<html:optionsCollection name="regionalbereichListe" value="id" label="name" />
				</html:select>
			</div>

			<%-- 
			<div class="box">
				<div class="label"><bean:message key="fahrplanregelung.aufnahmeNfp" /></div>
				<div class="input">
					<html:radio property="aufnahmeNfp" value="false" styleClass="checkbox" />&nbsp;nein&nbsp;
					<html:radio property="aufnahmeNfp" value="true" styleClass="checkbox" />&nbsp;ja
				</div>
			</div>
			<div class="box">
				<div class="label"><bean:message key="fahrplanregelung.behandlungKs" /></div>
				<div class="input">
					<html:radio property="behandlungKBBT" value="false" styleClass="checkbox" />&nbsp;nein&nbsp;
					<html:radio property="behandlungKBBT" value="true" styleClass="checkbox" />&nbsp;ja
				</div>
			</div>
			 --%>
			 
			 <div class="box">
				<div class="label"><label for="pageSize"><bean:message key="common.zeilenNr" /></label></div>
				<div class="input">
					<html:select property="pageSize" styleId="pageSize">
						<html:option value="0"><bean:message key="common.select.option" /></html:option>
						<html:option value="10">10</html:option>
						<html:option value="20">20</html:option>
						<html:option value="50">50</html:option>
						<html:option value="100">100</html:option>
					</html:select>
				</div>
			</div>
		</div>
	</div>
	<div class="buttonBar">
		<input type="submit" class="hiddenSubmit" />

		<authz:authorize ifAnyGranted="ROLE_FAHRPLANREGELUNG_ANLEGEN_ALLE, ROLE_FAHRPLANREGELUNG_ANLEGEN_REGIONALBEREICH">
			<html:link action="/osb/fahrplanregelung/edit.do" styleClass="buttonAdd">
				<html:param name="fahrplanregelungId" value="0"></html:param>
				<html:param name="buendelId" value="${buendelId}"></html:param>
				<html:param name="reset"></html:param>
				<bean:message key="button.create" />
			</html:link>
		</authz:authorize>

		<html:link action="/listFahrplanregelungenSearch" styleClass="buttonReload">
			<html:param name="reset">true</html:param>
			<bean:message key="button.reset" />
		</html:link>
		<html:link href="javascript:$('fahrplanregelungSearchForm').submit();" styleClass="buttonSearch"><bean:message key="button.search" /></html:link>
	</div>
</html:form>

<logic:present name="fahrplanregelungenList">
	<br/> 
	<div class="textcontent center">
		<bean:define id="titleFixiert"><bean:message key="common.fixiert" /></bean:define>
		<bean:define id="titleDeleted"><bean:message key="common.deleted" /></bean:define>
		<bean:define id="titleView"><bean:message key="button.view" /></bean:define>
		<bean:define id="titleEdit"><bean:message key="button.edit" /></bean:define>
		<bean:define id="titleDelete"><bean:message key="button.delete" /></bean:define>
		<bean:define id="titleUndelete"><bean:message key="button.undelete" /></bean:define>
		<bean:define id="confirmText" toScope="page"><bean:message key="confirm.fahrplanregelung.delete" /></bean:define>
		<bean:define id="blueColoredText">color:blue;</bean:define>
	
		<jsp:useBean id="urls" class="java.util.HashMap"/>

		<display:table id="currentFahrplanregelung"
			 name="fahrplanregelungenList" 
			 requestURI="/osb/fahrplanregelung/list.do"
			 pagesize="${fahrplanregelungenList.objectsPerPage}"
			 sort="external"
			 class="colored"
			 defaultsort="2"
			 defaultorder="ascending"
			 export="true"
			 decorator="db.training.easy.util.displaytag.decorators.AddRowLink">

			<%-- URL wird erzeugt, ggf. inkl. sessionId --%>
			<c:set target="${urls}" property="${currentFahrplanregelung.id}" value="#" />
			
			<%-- Überprüfung, ob Text blau gefärbt werden soll (LastChangeDate innerhalb DatumVon und Datum Bis --%>
			<c:if test="${currentFahrplanregelung.lastChangeDate != null}">
				<bean:define id="lastChange"><fmt:formatDate pattern="yyyy.MM.dd" value="${currentFahrplanregelung.lastChangeDate}" /></bean:define>
			</c:if>
			<c:if test="${currentFahrplanregelung.lastChangeDate == null}">
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
			
			<c:set target="${urls}" property="${currentFahrplanregelung.id}">
				<c:url value="/osb/fahrplanregelung/view.do?fahrplanregelungId=${currentFahrplanregelung.id}" />
			</c:set>

			<jsp:directive.include file="columnsFahrplanregelungenPaginated.jsp" />

			<display:setProperty name="basic.empty.showtable" value="true" />
			<display:setProperty name="paging.banner.item_name"><bean:message key="fahrplanregelung" /></display:setProperty>
			<display:setProperty name="paging.banner.items_name"><bean:message key="fahrplanregelungen" /></display:setProperty>
		</display:table>
	</div>
</logic:present>

<script type="text/javascript">
	Calendar.setup({ inputField: "datumVon", button: "datumVonButton" });
	//Calendar.setup({ inputField: "datumBis", button: "datumBisButton" });
</script>

<jsp:include page="/pages/main_footer.jsp"/>