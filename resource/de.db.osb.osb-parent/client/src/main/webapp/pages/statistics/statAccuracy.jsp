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
				    openMainMenu('navLink_auswertung-statAccuracy');
				</script>
				
				<html:form action="/statistics/statAccuracy">
					<jsp:include page="suche.jsp" />
				</html:form>
				<br />

				<logic:present name="simpleReportBean">
					<div class="textcontent_head">
					    <bean:message key="menu.auswertungen.meilensteineProMassnahme" />
					</div>
					<div class="textcontent" style="text-align:center;">
						<jsp:useBean id="urls" class="java.util.HashMap"/>   
						<display:table
							id="currentRow" 
							name="simpleReportBean" 
							export="true"
							requestURI="/statistics/statAccuracy.do" 
							pagesize="20" 
							sort="list"
							class="colored"
							decorator="db.training.easy.util.displaytag.decorators.AddRowLink">
							
							<%-- common.ja, common.nein --%>
							<display:column property="label" titleKey="auswertungen.milestone" sortable="true" />
							<display:column titleKey="auswertungen.ontime" property="onTime" sortable="true" />
						</display:table>
					</div>
				</logic:present>
				
				<logic:present name="complexReportBean">
					<div class="textcontent_head">
					    <bean:message key="menu.auswertungen.meilensteineMehrereMassnahmen" />
					</div>
					<div class="textcontent" style="text-align:center;">
						<%--<jsp:useBean id="urls" class="java.util.HashMap"/>--%>
						<display:table
							id="currentRow" 
							name="complexReportBean" 
							export="true"
							requestURI="/statistics/statAccuracy.do" 
							pagesize="20" 
							sort="list"
							class="colored"
							decorator="db.training.easy.util.displaytag.decorators.AddRowLink">

							<display:column property="label" titleKey="auswertungen.milestone" sortable="true" />
							<display:column titleKey="auswertungen.totalCount" property="totalCount" sortable="true" />
							<display:column titleKey="auswertungen.delayedCount" property="delayedCount" sortable="true" />
							<display:column titleKey="auswertungen.delayed20Days" property="delayed20DaysCount" sortable="true" />
							<display:column titleKey="auswertungen.delayed10Days" property="delayed10DaysCount" sortable="true" />
							<display:column titleKey="auswertungen.delayed5Days" property="delayed5DaysCount" sortable="true" />
							<display:column titleKey="auswertungen.delayedLess5Days" property="delayedLess5DaysCount" sortable="true" />
						</display:table>
					</div>
				</logic:present>
				
<jsp:include page="../main_footer.jsp" />