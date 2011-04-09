<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<html:xhtml/>

<jsp:include page="../../main_head.jsp"/>
<jsp:include page="../../main_path.jsp"/>
<jsp:include page="../../main_menu.jsp"/>

<script type="text/javascript">
	openMainMenu('navLink_bob');
</script>

<html:form action="/createBaumassnahme.do">
	<div class="textcontent_head">
		<bean:message key="baumassnahme.create" />
	</div>
	
	<div class="textcontent">
			<jsp:include page="editBaumassnahmeStammdaten.jsp"/>
	</div>
	
	<div class="buttonBar">
		<authz:authorize ifAnyGranted="ROLE_BAUMASSNAHME_GRUNDDATEN_BEARBEITEN_ALLE, ROLE_BAUMASSNAHME_GRUNDDATEN_BEARBEITEN_REGIONALBEREICH">
			<html:link href="#" onclick="$('baumassnahmeForm').submit();" styleClass="buttonSave" styleId="buttonSave">
				<bean:message key="button.save" />
			</html:link>
			<html:link action="/xmlImport1" styleClass="buttonBack">
				<bean:message key="button.back" />
				<html:param name="baumassnahmeId" value="0" />
			</html:link>
		</authz:authorize>
	</div>
</html:form>
		
<jsp:include page="../../main_footer.jsp"/>