<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy" %>
<html:xhtml/>

<jsp:include page="/pages/main_head.jsp" />
<jsp:include page="/pages/main_path.jsp" />
<jsp:include page="/pages/main_menu.jsp" />

<%-- Öffnet Punkt in Startmenü --%>
<script type="text/javascript">
    openMainMenu('navLink_osb_daten-import');
</script>

<html:form action="/importData" enctype="multipart/form-data">
	<div class="textcontent_head"><bean:message key="import.description.new.title" /></div>
	<div class="textcontent">
		<bean:message key="import.description.new" />
		<br/><br/>
		<div class="box">
			<div class="label"><label for="file"><bean:message key="import.upload.file" />*</label></div>
			<div class="input"><html:file property="file" style="width:249px;" /></div>
		</div>
		<h1 id="info" style="display:none;clear:both;margin-top:50px;text-align:center;color:red;"><bean:message key="import.wait" /></h1>
	</div>
	<div class="buttonBar">
		<easy:hasRole role="ADMINISTRATOR_ZENTRAL">
			<html:link href="#" onclick="$('info').style.display='block';$('importDataForm').submit();" styleClass="buttonAdd">
				<bean:message key="button.import" />
			</html:link>
		</easy:hasRole>
	</div>
</html:form>
	
<br/>
	
<html:form action="/downloadTemplate" enctype="multipart/form-data">
	<div class="textcontent_head"><bean:message key="import.downloadTemplate.title" /></div>
	<div class="textcontent">
		<bean:message key="import.template.info" />
		<br/><br/>
		<div class="box">
			<div class="label"><label for="file"><bean:message key="import.template.download" /></label></div>
			<div class="input">
				<html:select property="importTable" style="width:249px;">
					<logic:iterate id="current" name="importTableTypes">
						<html:option value="${current}"><bean:message key="import.tableType.${current}"/></html:option>
					</logic:iterate>
				</html:select>
			</div>
		</div>
		<h1 id="info" style="display:none;clear:both;margin-top:50px;text-align:center;color:red;"><bean:message key="import.wait" /></h1>
	</div>
	<div class="buttonBar">
		<easy:hasRole role="ADMINISTRATOR_ZENTRAL">
			<html:link href="#" onclick="$('downloadTemplateForm').submit();hideProgressbar();" styleClass="buttonAdd">
				<bean:message key="button.download" />
			</html:link>
		</easy:hasRole>
	</div>
</html:form>

<logic:notEmpty name="importMessages">
	<c:if test="${ (fn:length(importMessages) > 0) }">
		<script type="text/javascript">
			window.open("<c:url value='/viewImportMessages.jsp'/>","ImportMessages","dependent=yes,scrollbars=yes,height=350,width=741,left=228,top=258,resizable=yes");
		</script>
	</c:if>
</logic:notEmpty>

<jsp:include page="/pages/main_footer.jsp"/>