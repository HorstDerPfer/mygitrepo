<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

<html:xhtml/>

<jsp:include page="../../../main_head.jsp"/>
	<jsp:include page="../../../main_path.jsp"/>
		<jsp:include page="../../../main_menu.jsp"/>

			<%-- Öffnet Punkt in Startmenü --%>
			<script type="text/javascript">
			    openMainMenu('navLink_bob');
			</script>
			
			<html:form action="/zvfXmlImport" enctype="multipart/form-data" method="post">
				<html:hidden name="xmlImportForm" property="fileContent"/>
				<html:hidden name="xmlImportForm" property="baumassnahmeId"/>
				<html:hidden name="xmlImportForm" property="type"/>
				<html:hidden name="xmlImportForm" property="zvfId"/>
								
			 	<div class="textcontent_head">
				    <bean:message key="zvf.import.title" />
				</div>
				
				<div class="textcontent">
					<jsp:include page="../zvf/viewUebHeader.jsp"/>
					<jsp:include page="../zvf/viewUebMassnahme.jsp"/>
				</div>
			</html:form>
			
			<div class="buttonBar">
				<html:link href="#" onclick="document.forms[0].submit();" styleClass="buttonSave" styleId="buttonSave"><bean:message key="button.save" /></html:link>
				<html:link action="/zvfXmlImportPrepare" styleClass="buttonBack"><bean:message key="button.back" /></html:link>
			</div> 
		
		
<jsp:include page="../../../main_footer.jsp"/>