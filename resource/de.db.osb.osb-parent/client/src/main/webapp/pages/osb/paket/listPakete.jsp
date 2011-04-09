<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>
<html:xhtml />

	<jsp:include page="/pages/main_head.jsp" />
		<jsp:include page="/pages/main_path.jsp" />
			<jsp:include page="/pages/main_menu.jsp" />
			
				<%-- Öffnet Punkt in Startmenü --%>
				<script type="text/javascript">
				    openMainMenu('navLink_osb_workflow-pakete');
				</script>
				
				<c:import url="paketlist.jsp">
					<c:param name="requestURI" value="listPakete.do" />
				</c:import>

<jsp:include page="/pages/main_footer.jsp" />
