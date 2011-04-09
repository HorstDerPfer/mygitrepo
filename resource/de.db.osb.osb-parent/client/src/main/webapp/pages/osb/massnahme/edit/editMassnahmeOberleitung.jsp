<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>
<html:xhtml/>

<jsp:include page="/pages/main_head.jsp" />
<jsp:include page="/pages/main_path.jsp" />
<jsp:include page="/pages/main_menu.jsp" />

<script type="text/javascript">
	openMainMenu('navLink_osb-massnahme');
</script>

<jsp:include page="editMassnahmeHeader.jsp" />
<jsp:include page="editMassnahmeTabs.jsp" />

<div class="textcontent center">
	<display:table id="current" 
		name="massnahme.oberleitungen"
		export="false" 
		requestURI="/osb/massnahme/oberleitung/edit.do" 
		pagesize="20" 
		sort="list"
		class="colored"
		decorator="db.training.easy.util.displaytag.decorators.AddRowLink">
		
		<jsp:directive.include file="editMassnahmeRegelungColumns.jsp" />
					
		<display:column style="text-align:right;width:15px;" media="html">
			<html:link action="/osb/massnahme/oberleitung/edit.do" styleClass="edit">&nbsp;
				<html:param name="massnahmeId" value="${massnahme.id}" />
				<html:param name="regelungId" value="${current.id}" />
				<html:param name="method" value="oberleitung" />
			</html:link>
			<html:link action="/osb/massnahme/oberleitung/delete.do" styleClass="delete">&nbsp;
				<html:param name="massnahmeId" value="${massnahme.id}" />
				<html:param name="regelungId" value="${current.id}" />
				<html:param name="method" value="oberleitung" />
			</html:link>
		</display:column>
		
		<display:setProperty name="basic.empty.showtable" value="true" />
		<display:setProperty name="paging.banner.item_name"><bean:message key="oberleitung" /></display:setProperty>
		<display:setProperty name="paging.banner.items_name"><bean:message key="oberleitungen" /></display:setProperty>
	</display:table>
</div>
<div class="buttonBar">
	<html:link action="/osb/massnahme/oberleitung/edit" styleClass="buttonAdd">
		<bean:message key="button.create" />
		<html:param name="massnahmeId" value="${massnahme.id}" />
		<html:param name="regelungId" value="0" />
		<html:param name="method" value="oberleitung" />
	</html:link>
</div>

<c:if test="${regelungId != null}">
	<html:form action="/osb/massnahme/oberleitung/save">
		<div class="textcontent" style="border-top:0px;">
			<jsp:directive.include file="editMassnahmeRegelungAllgemein.jsp" />
			<div class="box">
				<div class="label"><label for="schaltgruppen"><bean:message key="oberleitung.schaltgruppen" />*</label></div>
				<div class="input"><html:text property="schaltgruppen" styleId="schaltgruppen" styleClass="long" errorStyle="${errorStyle}" /></div>
			</div>
			<div class="textcontent_left">
				<div class="box">
					<div class="label"><label for="sigWeicheVon"><bean:message key="oberleitung.sigWeicheVon" /></label></div>
					<div class="input"><html:text property="sigWeicheVon" styleId="sigWeicheVon" errorStyle="${errorStyle}" /></div>
				</div>
			</div>
			<div class="textcontent_right">
				<div class="box">
					<div class="label"><label for="sigWeicheBis"><bean:message key="oberleitung.sigWeicheBis" /></label></div>
					<div class="input"><html:text property="sigWeicheBis" styleId="sigWeicheBis" errorStyle="${errorStyle}" /></div>
				</div>
			</div>
			<div class="box">
				<div class="label"><label for="kommentar"><bean:message key="regelung.kommentar" /></label></div>
				<div class="input"><html:text property="kommentar" styleId="kommentar" styleClass="long" errorStyle="${errorStyle}" /></div>
			</div>
		</div>
		<div class="buttonBar">
			<html:link href="#" onclick="$('regelungOberleitungForm').submit();" styleClass="buttonSave">
				<bean:message key="button.save" />
			</html:link>
		</div>
	</html:form>
	
	<script type="text/javascript">
		var dataChanged = false;
		initDataChangeListener('regelungOberleitungForm');
		window.onbeforeunload = changeChecker;
		function changeChecker(){
			if (dataChanged)
				return '<bean:message key="common.confirmChange" />';
		}
	</script>
</c:if>

<jsp:include page="/pages/main_footer.jsp" />
