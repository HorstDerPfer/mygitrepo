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
		name="massnahme.gleissperrungen"
		export="false" 
		requestURI="/osb/massnahme/gleissperrung/edit.do" 
		pagesize="20" 
		sort="list"
		class="colored"
		decorator="db.training.easy.util.displaytag.decorators.AddRowLink">
		
		<jsp:directive.include file="editMassnahmeRegelungColumns.jsp" />
		
		<display:column style="text-align:right;width:15px;" media="html">
			<html:link action="/osb/massnahme/gleissperrung/edit.do" styleClass="edit">&nbsp;
				<html:param name="massnahmeId" value="${massnahme.id}" />
				<html:param name="regelungId" value="${current.id}" />
				<html:param name="method" value="gleissperrung" />
			</html:link>
			<html:link action="/osb/massnahme/gleissperrung/delete.do" styleClass="delete">&nbsp;
				<html:param name="massnahmeId" value="${massnahme.id}" />
				<html:param name="regelungId" value="${current.id}" />
				<html:param name="method" value="gleissperrung" />
			</html:link>
		</display:column>

		<display:setProperty name="basic.empty.showtable" value="true" />
		<display:setProperty name="paging.banner.item_name"><bean:message key="gleissperrung" /></display:setProperty>
		<display:setProperty name="paging.banner.items_name"><bean:message key="gleissperrungen" /></display:setProperty>
	</display:table>
</div>
<div class="buttonBar">
	<html:link action="/osb/massnahme/gleissperrung/edit" styleClass="buttonAdd">
		<bean:message key="button.create" />
		<html:param name="massnahmeId" value="${massnahme.id}" />
		<html:param name="regelungId" value="0" />
		<html:param name="method" value="gleissperrung" />
	</html:link>
</div>

<c:if test="${regelungId != null}">
	<html:form action="/osb/massnahme/gleissperrung/save">
		<div class="textcontent" style="border-top:0px;">
			<jsp:directive.include file="editMassnahmeRegelungAllgemein.jsp" />

			<div class="textcontent_left">
				<div class="box">
					<div class="label"><label for="betriebsweiseId"><bean:message key="gleissperrung.betriebsweise" /></label></div>
					<div class="input">
						<html:select property="betriebsweiseId" styleId="betriebsweiseId" errorStyle="${errorStyle}">
							<html:option value="">(<bean:message key="common.select.option" />)</html:option>
							<html:optionsCollection name="betriebsweiseList" label="name" value="id" />
						</html:select>
					</div>
				</div>
				<div class="box">
					<div class="label"><label for="ergaenzungBetriebsweise"><bean:message key="gleissperrung.ergaenzungBetriebsweise" /></label></div>
					<div class="input"><html:text property="ergaenzungBetriebsweise" styleId="ergaenzungBetriebsweise" errorStyle="${errorStyle}" /></div>
				</div>
			</div>
			<div class="textcontent_right">
				<div class="box">
					<div class="label"><label for="geschwindigkeitVzg"><bean:message key="gleissperrung.geschwindigkeitVzg" /></label></div>
					<div class="input"><html:text property="geschwindigkeitVzg" styleId="geschwindigkeitVzg" style="width:50px;" errorStyle="width:50px;${errorStyle}" /></div>
				</div>
				<div class="box">
					<div class="label"><label for="fzvMusterzug"><bean:message key="gleissperrung.fzvMusterzug" /></label></div>
					<div class="input"><html:text property="fzvMusterzug" styleId="fzvMusterzug" style="width:50px;" errorStyle="width:50px;${errorStyle}" /></div>
					<div class="input" style="margin-left:90px;">
						<html:checkbox property="vorschlagLisba" styleId="vorschlagLisba" styleClass="checkbox" />
						<label for="vorschlagLisba"><bean:message key="gleissperrung.vorschlagLisba" /></label>
					</div>
				</div>
			</div>
			<div class="box">
				<div class="label"><label for="kommentar"><bean:message key="regelung.kommentar" /></label></div>
				<div class="input"><html:text property="kommentar" styleId="kommentar" styleClass="long" errorStyle="${errorStyle}" /></div>
			</div>
		</div>
		<div class="buttonBar">
			<html:link href="#" onclick="$('regelungGleissperrungForm').submit();" styleClass="buttonSave">
				<bean:message key="button.save" />
			</html:link>
		</div>
	</html:form>

	<script type="text/javascript">
		var dataChanged = false;
		initDataChangeListener('regelungGleissperrungForm');
		window.onbeforeunload = changeChecker;
		function changeChecker(){
			if (dataChanged)
				return '<bean:message key="common.confirmChange" />';
		}
	</script>
</c:if>

<jsp:include page="/pages/main_footer.jsp" />
