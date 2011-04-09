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

	<jsp:include page="../../main_head.jsp" />
		<jsp:include page="../../main_path.jsp" />
			<jsp:include page="../../main_menu.jsp" />
			
				<%-- Öffnet Punkt in Startmenü --%>
				<script type="text/javascript">
				    openMainMenu('navLink_auswertung-statUebVerspaetungsminutenZuege');
				</script>
				
				<html:form action="/statistics/statUebVerspaetungsminutenZuege">
					<jsp:include page="../suche.jsp" />
				</html:form>
				<br />

				<div class="textcontent_head">
				    <bean:message key="auswertungen.anzahlVerspaetunsminutenGesamt" />
				</div>
				<div class="textcontent" style="text-align:center;">
					<display:table
						id="currentRow" 
						name="reportBeanGesamt" 
						export="true"
						requestURI="/statistics/statUebVerspaetungsminutenZuege.do" 
						pagesize="20" 
						sort="list"
						class="colored">
						
						<display:column property="label" titleKey="auswertungen.noTitle" sortable="false" style="width:200px"/>
						<display:column property="anzahl" titleKey="auswertungen.anzahlVerspaetunsminuten" sortable="true" />
					</display:table>
				</div>
				<br>
				
				<div class="textcontent_head">
				    <bean:message key="auswertungen.anzahlVerspaetunsminutenEVUKundengruppe" />
				</div>
				<div class="textcontent" style="text-align:center;">
					<display:table
						id="currentRow2" 
						name="reportBeanEVU" 
						export="true"
						requestURI="/statistics/statUebVerspaetungsminutenZuege.do" 
						pagesize="20" 
						sort="list"
						class="colored">
						
						<display:column property="label" titleKey="auswertungen.evukundengruppe" sortable="true" style="width:200px"/>
						<display:column property="anzahl" titleKey="auswertungen.anzahlVerspaetunsminuten" sortable="true" />
					</display:table>
				</div>
				
<jsp:include page="../../main_footer.jsp" />