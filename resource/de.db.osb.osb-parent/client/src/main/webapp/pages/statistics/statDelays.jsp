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
				    openMainMenu('navLink_auswertung-statDelays');
				</script>
				
				<html:form action="/statistics/statDelays">
					<jsp:include page="suche.jsp" />
				</html:form>
				<br />

				<div class="textcontent_head">
				    <bean:message key="menu.auswertungen.delays" />
				</div>
				<div class="textcontent" style="text-align:center;">
					<jsp:useBean id="urls" class="java.util.HashMap"/>   
					<display:table
						id="currentRow" 
						name="delaysReport" 
						export="true"
						requestURI="/statistics/statDelays.do" 
						pagesize="20" 
						sort="list"
						class="colored"
						decorator="db.training.easy.util.displaytag.decorators.AddRowLink">
						
						<display:column property="label" titleKey="auswertungen.evukundengruppe" sortable="true" />
						<display:column titleKey="auswertungen.termin.zvfentwurf" property="zvFEntwurf" sortable="true" />
						<display:column titleKey="auswertungen.termin.stellungnahmeevu" property="stellungNameEVU" sortable="true" />
						<display:column titleKey="auswertungen.termin.zvf" property="zvF" sortable="true" />
						<logic:equal name="StatisticsFilterForm" property="evu" value="pevu">
							<display:column titleKey="auswertungen.termin.masteruebergabeblattpv.short" property="masterUebergabeblattPV" sortable="true" />
							<display:column titleKey="auswertungen.termin.uebergabeblattpv.short" property="uebergabeblattPV" sortable="true" />
						</logic:equal>
						<logic:equal name="StatisticsFilterForm" property="evu" value="gevu">
							<display:column titleKey="auswertungen.termin.masteruebergabeblattgv.short" property="masterUebergabeblattGV" sortable="true" />
							<display:column titleKey="auswertungen.termin.uebergabeblattgv.short" property="uebergabeblattGV" sortable="true" />
						</logic:equal>
						<display:column titleKey="auswertungen.termin.fplo" property="fplo" sortable="true" />
						<display:column titleKey="auswertungen.termin.eingabegfd_z" property="eingabeGFDZ" sortable="true" />
					</display:table>
				</div>
				
<jsp:include page="../main_footer.jsp" />