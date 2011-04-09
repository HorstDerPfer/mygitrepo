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
				    openMainMenu('navLink_auswertung-statPerformance');
				</script>
				
				<html:form action="/statistics/statPerformance">
					<jsp:include page="suche.jsp" />
				</html:form>
				<br />

				<div class="textcontent_head">
				    <bean:message key="menu.auswertungen.performanceProRegionalbereich" />
				</div>
				<div class="textcontent" style="text-align:center;">
					<jsp:useBean id="urls" class="java.util.HashMap"/>   
					<display:table
						id="currentRow" 
						name="reportBean" 
						export="true"
						requestURI="/statistics/statPerformance.do" 
						pagesize="20" 
						sort="list"
						class="colored"
						decorator="db.training.easy.util.displaytag.decorators.AddRowLink">
						
						<display:column property="label" titleKey="baumassnahme.regionalbereich" sortable="true" />
						<display:column property="geregelteTrassen" media="html" titleKey="baumassnahme.geregeltetrassen" sortable="true" />
						<display:column property="geregelteTrassen" media="excel" titleKey="baumassnahme.geregeltetrassen.excel" sortable="true" />
						<display:column property="ueberarbeiteteTrassen" media="html" titleKey="baumassnahme.ueberarbeitetetrassen" sortable="true" />
						<display:column property="ueberarbeiteteTrassen" media="excel" titleKey="baumassnahme.ueberarbeitetetrassen.excel" sortable="true" />
						<display:column property="anzahlBiUe" media="html" titleKey="baumassnahme.erstelltebiue" sortable="true" />
						<display:column property="anzahlBiUe" media="excel" titleKey="baumassnahme.erstelltebiue.excel" sortable="true" />
						<display:column property="veroeffentlichteTrassen" media="html" titleKey="baumassnahme.veroeffentlichtetrassen" sortable="true" />
						<display:column property="veroeffentlichteTrassen" media="excel" titleKey="baumassnahme.veroeffentlichtetrassen.excel" sortable="true" />
					</display:table>
				</div>
				
<jsp:include page="../main_footer.jsp" />