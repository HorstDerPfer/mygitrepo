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
				    openMainMenu('navLink_osb_workflow-pakete');
				</script>
	
				<div class="textcontent_head"><bean:message key="paket.name" />: <bean:write name="paket" property="kurzname" /></div>
	
				<div class="textcontent" style="border-bottom:0px; height:50px;">
					<jsp:include page="viewPaketStammdaten.jsp"/>
				</div>
	
				<div class="textcontent center" style="border-bottom:0px;border-top:0px;">
					<jsp:include page="viewPaketMassnahmen.jsp"/>
				</div>
	
				<div class="buttonBar">
					<html:link action="/listPakete.do" styleClass="buttonBack"><bean:message key="button.back" /></html:link>
				</div>
				
	<jsp:include page="/pages/main_footer.jsp"/>
