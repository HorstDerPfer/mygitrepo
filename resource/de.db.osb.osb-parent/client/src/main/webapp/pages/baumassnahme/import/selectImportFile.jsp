<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

<html:xhtml/>

<jsp:include page="../../main_head.jsp"/>
	<jsp:include page="../../main_path.jsp"/>
		<jsp:include page="../../main_menu.jsp"/>

			<%-- Öffnet Punkt in Startmenü --%>
			<script type="text/javascript">
			    openMainMenu('navLink_bob_baumassnahme');
			</script>
			
			<html:form action="/xmlImport2" enctype="multipart/form-data">
				<html:hidden property="baumassnahmeId"/>
			 	<div class="textcontent_head">
				    <bean:message key="baumassnahme.xmlimport" />
				</div>
				<div class="textcontent">
					<bean:message key="baumassnahme.xml.selectfile" />
					<html:file property="xmlFile" style="width:100%;" />
				</div>
			</html:form>
			
			<div class="buttonBar">
				<html:link action="/createEmptyBaumassnahme" styleClass="buttonNext">
					<bean:message key="button.baumassnahme.add.ohneimport" />
					<html:param name="reset">true</html:param>
				</html:link>
				<html:link href="#" onclick="$j('#xmlImportForm').submit();" styleClass="buttonNext" styleId="buttonAdd" >
					<bean:message key="button.import" />
				</html:link>
				<html:link action="/suche" styleClass="buttonBack">
					<bean:message key="button.back" />
					<html:param name="method" value="web" />
				</html:link>
			</div> 
			
<jsp:include page="../../main_footer.jsp"/>