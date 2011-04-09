<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>
<html:xhtml />

	<jsp:include page="../main_head.jsp" />
		<jsp:include page="../main_path.jsp" />
			<jsp:include page="../main_menu.jsp" />
			
				<%-- Öffnet Punkt in Startmenü --%>
				<script type="text/javascript">
				    openMainMenu('navLink_auswertung-statEscalation');
				</script>
				
				<html:form action="/statistics/statEscalation">
					<jsp:include page="suche.jsp" />
				</html:form>
				<br />

				<div class="textcontent_head">
				    <bean:message key="menu.auswertungen.withEscalation" />
				</div>
				<div class="textcontent" style="text-align:center;">
					<jsp:useBean id="urls" class="java.util.HashMap"/>   
					<display:table
						id="currentRow" 
						name="reportBean" 
						export="true"
						requestURI="/statistics/statEscalation.do" 
						pagesize="20" 
						sort="list"
						class="colored"
						decorator="db.training.easy.util.displaytag.decorators.AddRowLink">
						
						<display:column property="label" titleKey="baumassnahme.regionalbereich" sortable="true" />
						<display:column property="totalCount" titleKey="auswertungen.changes.totalcount" sortable="true" />
						<display:column property="escalationCount" titleKey="auswertungen.escalations" sortable="true" />
						<display:column property="vetoCount" titleKey="auswertungen.vetos" sortable="true" />
					</display:table>
				</div>
				
<jsp:include page="../main_footer.jsp" />