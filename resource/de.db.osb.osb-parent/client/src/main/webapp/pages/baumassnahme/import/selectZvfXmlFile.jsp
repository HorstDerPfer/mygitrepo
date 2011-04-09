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

<jsp:include page="../../main_head.jsp"/>
	<jsp:include page="../../main_path.jsp"/>
		<jsp:include page="../../main_menu.jsp"/>

			<%-- Öffnet Punkt in Startmenü --%>
			<script type="text/javascript">
			    openMainMenu('navLink_bob');
			</script>

			<html:form action="/zvfXmlImportPrepare" enctype="multipart/form-data" method="post">
				<html:hidden styleId="baumassnahmeId" property="baumassnahmeId"/>
				<html:hidden styleId="type" property="type"/>
				<html:hidden styleId="zvfId" property="zvfId"/>
					
			 	<div class="textcontent_head">
					<logic:equal name="type" value="UEB">
				    	<bean:message key="ueb.xmlimport" />
					</logic:equal>
					<logic:equal name="type" value="ZVF">
				    	<bean:message key="zvf.xmlimport" />
					</logic:equal>
				</div>
				
				<div class="textcontent">
					<bean:message key="baumassnahme.xml.selectfile" />
					<html:file property="xmlFile" style="width:100%;" accept="text/xml" />
				</div>
			</html:form>
			
			<div class="buttonBar">
				<html:link action="/back" styleClass="buttonBack">
					<bean:message key="button.back" />
					<html:param name="id" value="${baumassnahmeId}"></html:param>
				</html:link>
				<html:link href="#" onclick="document.forms[0].submit();" styleClass="buttonNext" styleId="buttonAdd" ><bean:message key="button.import" /></html:link>
			</div>

<jsp:include page="../../main_footer.jsp"/>