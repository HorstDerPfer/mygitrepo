<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="d" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html:xhtml/>
    
<div id="master_limited">
	<div id="master_navigation">
    
    	<bean:define id="resetTrue" value="true"></bean:define>
		
        <div class="navTitle">
			<bean:message bundle="configResources" key="application.title" />&nbsp;<bean:message bundle="configResources" key="application.version" />
        </div>

		<!-- BOB -->
		<authz:authorize ifAllGranted="ROLE_BOB_USER">
			<!--  Menue-Hauptpunkt -->
		    <div class="navGroup">
		    	<html:link href="#" styleId="navLink_bob" onclick="openNavDiv(this);" styleClass="navLink">
		    		<bean:message key="menu.bob" />
		    	</html:link>
		    	<div id="navDiv_bob" class="navDiv">
			    	<html:link action="/suche" styleId="navLink_bob_suche" styleClass="navLink">
		    			<bean:message key="menu.suche" /> / <bean:message key="menu.controlling" />
		    			<html:param name="method" value="web" />
		    		</html:link>
		    		<%-- alle Baumassnahmen auflisten
		    		<html:link action="/listBaumassnahmen" styleId="navLink_bob_baumassnahme" styleClass="navLink">
	    				<bean:message key="menu.baumassnahme" />
	    				<html:param name="method" value="web" />
	    			</html:link>--%>
	    			
	    			<authz:authorize ifAnyGranted="ROLE_BAUMASSNAHME_GRUNDDATEN_BEARBEITEN_ALLE,ROLE_BAUMASSNAHME_GRUNDDATEN_BEARBEITEN_REGIONALBEREICH">
						<bean:define id="baumassnahmeId" value="0"></bean:define>
						<html:link action="/xmlImport1" styleId="navLink_bob_baumassnahme" styleClass="navLink" paramId="baumassnahmeId" paramName="baumassnahmeId"><bean:message key="button.baumassnahme.add" /></html:link>
					</authz:authorize>
		    	</div>
		    </div>
		    
		    <%--<authz:authorize ifAnyGranted="ROLE_AUSWERTUNG_ALLGEMEIN_ALLE,ROLE_AUSWERTUNG_ALLGEMEIN_REGIONALBEREICH">
			    <div class="navGroup">
			    	<html:link action="/controlling" paramId="reset" paramName="resetTrue" styleId="navLink_controlling" styleClass="navLink"><bean:message key="menu.controlling" /></html:link>
			    </div>
		    </authz:authorize>--%>
		    
		    <authz:authorize ifAnyGranted="ROLE_AUSWERTUNG_ALLGEMEIN_ALLE,ROLE_AUSWERTUNG_ALLGEMEIN_REGIONALBEREICH">
			    <div class="navGroup">
		    		<html:link href="#" styleId="navLink_auswertung" onclick="openNavDiv(this);" styleClass="navLink"><bean:message key="menu.auswertungen" /></html:link>
		    		<div id="navDiv_auswertung" class="navDiv">
						<authz:authorize ifAnyGranted="ROLE_AUSWERTUNG_ALLGEMEIN_ALLE,ROLE_AUSWERTUNG_ALLGEMEIN_REGIONALBEREICH">
						   	<html:link action="/statistics/statMethodsPerLocation" paramId="reset" paramName="resetTrue" styleId="navLink_auswertung-statMethodsPerLocation" onclick="openNavDiv(this);" styleClass="navLink"><bean:message key="menu.auswertungen.massnahmenProRegionalbereich" /></html:link>
						   	<%--<html:link action="/admin/listUsers" styleId="navLink_admin-users" onclick="openNavDiv(this);" styleClass="navLink" style="color:#ff0000;" ><bean:message key="menu.auswertungen.meilensteineProMassnahme" /></html:link>
						   	<html:link action="/admin/listUsers" styleId="navLink_admin-users" onclick="openNavDiv(this);" styleClass="navLink" style="color:#ff0000;" ><bean:message key="menu.auswertungen.meilensteineMehrereMassnahmen" /></html:link>--%>
						   	<html:link action="/statistics/statAccuracy" paramId="reset" paramName="resetTrue" styleId="navLink_auswertung-statAccuracy" onclick="openNavDiv(this);" styleClass="navLink" ><bean:message key="menu.auswertungen.accuracy" /></html:link>
						   	<html:link action="/statistics/statDelays" paramId="reset" paramName="resetTrue" styleId="navLink_auswertung-statDelays" onclick="openNavDiv(this);" styleClass="navLink" ><bean:message key="menu.auswertungen.delays" /></html:link>
						   	<html:link action="/statistics/statQualitaetstrasse" paramId="reset" paramName="resetTrue" styleId="navLink_auswertung-statQualitaetstrasse" onclick="openNavDiv(this);" styleClass="navLink" ><bean:message key="menu.auswertungen.qualitaetstrasse" /></html:link>
						   	<html:link action="/statistics/statChanges" paramId="reset" paramName="resetTrue" styleId="navLink_auswertung-statChanges" onclick="openNavDiv(this);" styleClass="navLink" ><bean:message key="menu.auswertungen.anzahlGeaenderteMassnahmen" /></html:link>
						   	<html:link action="/statistics/statChanges2" paramId="reset" paramName="resetTrue" styleId="navLink_auswertung-statChanges2" onclick="openNavDiv(this);" styleClass="navLink" ><bean:message key="menu.auswertungen.anzahlAenderungsgruende" /></html:link>
						   	<html:link action="/statistics/statPerformance" paramId="reset" paramName="resetTrue" styleId="navLink_auswertung-statPerformance" onclick="openNavDiv(this);" styleClass="navLink" ><bean:message key="menu.auswertungen.performanceProRegionalbereich" /></html:link>
						   	<html:link action="/statistics/statEscalation" paramId="reset" paramName="resetTrue" styleId="navLink_auswertung-statEscalation" onclick="openNavDiv(this);" styleClass="navLink" ><bean:message key="menu.auswertungen.withEscalation" /></html:link>
						   	<html:link action="/statistics/statAusfall" paramId="reset" paramName="resetTrue" styleId="navLink_auswertung-statAusfall" onclick="openNavDiv(this);" styleClass="navLink" ><bean:message key="menu.auswertungen.failures" /></html:link>
						   	<html:link action="/statistics/statAusfall2" paramId="reset" paramName="resetTrue" styleId="navLink_auswertung-statAusfall2" onclick="openNavDiv(this);" styleClass="navLink" ><bean:message key="menu.auswertungen.anzahlAusfallgruende" /></html:link>
						   	<html:link action="/statistics/statUebKsQsZuege" paramId="reset" paramName="resetTrue" styleId="navLink_auswertung-statUebKsQsZuege" onclick="openNavDiv(this);" styleClass="navLink" ><bean:message key="menu.auswertungen.anzahlQsKsZuege" /></html:link>
						   	<html:link action="/statistics/statUebVerspaetungsminutenZuege" paramId="reset" paramName="resetTrue" styleId="navLink_auswertung-statUebVerspaetungsminutenZuege" onclick="openNavDiv(this);" styleClass="navLink" ><bean:message key="menu.auswertungen.anzahlVerspaetungsminutenZuege" /></html:link>
						   	<html:link action="/statistics/statUebAbweichungZuegeNummern" paramId="reset" paramName="resetTrue" styleId="navLink_auswertung-statUebAbweichungZuegeNummern" onclick="openNavDiv(this);" styleClass="navLink" ><bean:message key="menu.auswertungen.auswirkungZuegeNummern" /></html:link>
						   	<html:link action="/statistics/statBbzrVerspaetungsminutenZuege" paramId="reset" paramName="resetTrue" styleId="navLink_auswertung-statBbzrVerspaetungsminutenZuege" onclick="openNavDiv(this);" styleClass="navLink" ><bean:message key="menu.auswertungen.anzahlBBzrVerspaetungsminutenZuege" /></html:link>
						   	<html:link action="/statistics/statBbzrAbweichungAnzahlZuege" paramId="reset" paramName="resetTrue" styleId="navLink_auswertung-statBbzrAbweichungAnzahlZuege" onclick="openNavDiv(this);" styleClass="navLink" ><bean:message key="menu.auswertungen.anzahlAbweichungZuege" /></html:link>
						   	<html:link action="/statistics/statBbzrAbweichungZuegeNummern" paramId="reset" paramName="resetTrue" styleId="navLink_auswertung-statBbzrAbweichungZuegeNummern" onclick="openNavDiv(this);" styleClass="navLink" ><bean:message key="menu.auswertungen.abweichungZuegeNummern" /></html:link>
					   	</authz:authorize>
					</div>
			    </div>
		    </authz:authorize>
		</authz:authorize>
	
		<!-- OSB -->
		<authz:authorize ifAllGranted="ROLE_OSB_USER">
			<easy:hasRole role="ADMINISTRATOR_ZENTRAL, ADMINISTRATOR_REGIONAL, BEARBEITER_ZENTRAL, BEARBEITER_REGIONAL, LESER_ZENTRAL, LESER_REGIONAL">
				<div class="navGroup">
					<html:link href="#" styleId="navLink_osb_workflow" onclick="openNavDiv(this);" styleClass="navLink"><bean:message key="menu.osb.workflow" /></html:link>
					<div class="navDiv" id="navDiv_osb_workflow">
						<html:link action="/osb/streckenband/search" onclick="openNavDiv(this);" styleId="navDiv_osb_workflow-streckenband" styleClass="navLink"><bean:message key="menu.osb.streckenband" /></html:link>
						<html:link action="/osb/listTopProjekte" styleId="navLink_osb_workflow-topprojekt" styleClass="navLink"><bean:message key="menu.topprojekte" /></html:link>
						<html:link action="/osb/baustelle/list" styleId="navLink_osb_workflow-baustelle" styleClass="navLink"><bean:message key="menu.baustelle" /></html:link>
						<html:link action="/listSperrpausenbedarfe" styleId="navLink_osb_workflow-sperrbedarf" styleClass="navLink"><bean:message key="menu.sperrpausenbedarfe" /></html:link>
						<%--<html:link action="/osb/massnahme/list.do" styleId="navLink_osb-massnahme" styleClass="navLink"><bean:message key="menu.sperrpausenbedarfe" /> NEU</html:link>--%>
						<html:link action="/osb/listGleissperrung" styleId="navLink_osb_workflow-gleissperrung" styleClass="navLink"><bean:message key="menu.gleissperrung" /></html:link>
						<html:link action="/listPakete" styleId="navLink_osb_workflow-pakete" styleClass="navLink"><bean:message key="menu.pakete" /></html:link>
						<html:link action="/osb/listBuendel?page=1&pagesize=20" styleId="navLink_osb_workflow-buendel" styleClass="navLink"><bean:message key="menu.buendel" />
							<html:param name="page">1</html:param>
							<html:param name="pagesize">20</html:param>
						</html:link>
						<html:link action="/osb/fahrplanregelung/list" styleId="navLink_osb_workflow-fahrplanregelung" styleClass="navLink"><bean:message key="menu.osb.fahrplanregelungen" />
							<html:param name="page">1</html:param>
							<html:param name="pagesize">20</html:param>
						</html:link>
						<html:link action="/listUmleitungen" styleId="navLink_osb_workflow-umleitung" styleClass="navLink"><bean:message key="menu.osb.umleitungen" /></html:link>
						<%--<html:link action="/osb/baubetriebsplan.do" styleId="navLink_osb_baubetriebsplan" styleClass="navLink"><bean:message key="menu.osb.baubetriebsplan" /></html:link>--%>
						<%--<html:link action="/listMasterBuendel" styleId="navLink_osb_workflow-masterbuendel" styleClass="navLink"><bean:message key="menu.osb.masterbuendel" /></html:link>--%>
						<%--<html:link action="/listKorridore" styleId="navLink_osb_workflow-korridore" styleClass="navLink"><bean:message key="menu.osb.korridor" /></html:link>--%>
					</div>
				</div>
			</easy:hasRole>
			
			<easy:hasRole role="ADMINISTRATOR_ZENTRAL, ADMINISTRATOR_REGIONAL, AUSWERTER_ZENTRAL, AUSWERTER_REGIONAL">
				<div class="navGroup">
					<html:link href="#" styleId="navLink_osb_daten" onclick="openNavDiv(this);" styleClass="navLink"><bean:message key="menu.osb.daten" /></html:link>
					<div class="navDiv" id="navDiv_osb_daten">
						<easy:hasRole role="ADMINISTRATOR_ZENTRAL, ADMINISTRATOR_REGIONAL, AUSWERTER_ZENTRAL, AUSWERTER_REGIONAL">
							<html:link action="/listSqlQueries" styleId="navLink_osb_daten-sqlquery" styleClass="navLink"><bean:message key="menu.osb.sqlQueries" /></html:link>
						</easy:hasRole>
						<authz:authorize ifAllGranted="ROLE_BENUTZER_ANLEGEN_ALLE, ROLE_BAUMASSNAHME_GRUNDDATEN_BEARBEITEN_ALLE, ROLE_AUSWERTUNG_ALLGEMEIN_ALLE">
							<div class="navGroup" style="margin-bottom: 0px; border-bottom: 0px;">
								<html:link href="#" styleId="navLink_osb_stammdaten" onclick="openNavDiv(this);" styleClass="navLink"><bean:message key="menu.osb.stammdaten" /></html:link>
								<div class="navDiv" id="navDiv_osb_stammdaten">
								   	<html:link action="/admin/listEvu.do" styleId="navLink_osb_stammdaten-evuList" onclick="openNavDiv(this);" styleClass="navLink">
								   		<html:param name="method" value="list"></html:param>
								   		<bean:message key="menu.admin.evu" />
								   	</html:link>
								   	<html:link action="/admin/listGrund.do" styleId="navLink_osb_stammdaten-grundList" onclick="openNavDiv(this);" styleClass="navLink">
								   		<html:param name="method" value="list"></html:param>
								   		<bean:message key="menu.admin.grund" />
								   	</html:link>
								   	<html:link action="/admin/listNachbarbahn.do" styleId="navLink_osb_stammdaten-nachbarbahnList" onclick="openNavDiv(this);" styleClass="navLink">
								   		<html:param name="method" value="list"></html:param>
								   		<bean:message key="menu.admin.nachbarbahn" />
								   	</html:link>
								   	<html:link action="/admin/listFplBearbeitungsbereich.do" styleId="navLink_osb_stammdaten-fplbearbeitungsbereichList" onclick="openNavDiv(this);" styleClass="navLink">
								   		<html:param name="method" value="list"></html:param>
								   		<bean:message key="menu.admin.fplbearbeitungsbereich" />
								   	</html:link>
								</div>
							</div>
						</authz:authorize>
						<easy:hasRole role="ADMINISTRATOR_ZENTRAL">
			    			<html:link action="/importData" styleId="navLink_osb_daten-import" styleClass="navLink">
			    				<bean:message key="import.description.new.title" />
			    				<html:param name="reset" value="true"></html:param>
			    			</html:link>
						</easy:hasRole>
					</div>
				</div>
			</easy:hasRole>
		</authz:authorize>
		
		
		<authz:authorize ifAnyGranted="ROLE_BENUTZER_LESEN_ALLE,ROLE_BENUTZER_LESEN_REGIONALBEREICH">
		    <div class="navGroup">
			   	<html:link href="#" styleId="navLink_admin" onclick="openNavDiv(this);" styleClass="navLink" ><bean:message key="menu.admin" /></html:link>
				<div id="navDiv_admin" class="navDiv">
					<authz:authorize ifAnyGranted="ROLE_BENUTZER_LESEN_ALLE,ROLE_BENUTZER_LESEN_REGIONALBEREICH">
					   	<html:link action="/admin/listUsers" styleId="navLink_admin-users" onclick="openNavDiv(this);" styleClass="navLink" ><bean:message key="menu.admin.user" /></html:link>
				   	</authz:authorize>
					<authz:authorize ifAllGranted="ROLE_BOB_USER">
					   	<authz:authorize ifAllGranted="ROLE_BENUTZER_ANLEGEN_ALLE, ROLE_BAUMASSNAHME_GRUNDDATEN_BEARBEITEN_ALLE, ROLE_AUSWERTUNG_ALLGEMEIN_ALLE">
						   	<html:link action="/admin/listEvu.do" styleId="navLink_admin-evuList" onclick="openNavDiv(this);" styleClass="navLink">
						   		<html:param name="method" value="list"></html:param>
						   		<bean:message key="menu.admin.evu" />
						   	</html:link>
						   	<html:link action="/admin/listGrund.do" styleId="navLink_admin-grundList" onclick="openNavDiv(this);" styleClass="navLink">
						   		<html:param name="method" value="list"></html:param>
						   		<bean:message key="menu.admin.grund" />
						   	</html:link>
						   	<html:link action="/admin/listNachbarbahn.do" styleId="navLink_admin-nachbarbahnList" onclick="openNavDiv(this);" styleClass="navLink">
						   		<html:param name="method" value="list"></html:param>
						   		<bean:message key="menu.admin.nachbarbahn" />
						   	</html:link>
						   	<html:link action="/admin/listFplBearbeitungsbereich.do" styleId="navLink_admin-fplbearbeitungsbereichList" onclick="openNavDiv(this);" styleClass="navLink">
						   		<html:param name="method" value="list"></html:param>
						   		<bean:message key="menu.admin.fplbearbeitungsbereich" />
						   	</html:link>
					   	</authz:authorize>
					   	<easy:hasRole role="ADMINISTRATOR_ZENTRAL">
						   	<html:link action="/admin/listMeilensteine" styleId="navLink_admin-meilensteine" onclick="openNavDiv(this);" styleClass="navLink" >
						   		<html:param name="method" value="list"></html:param>
						   		<bean:message key="menu.admin.meilensteine" />
						   	</html:link>
					   	</easy:hasRole>
					</authz:authorize>
				</div>
			</div>
		</authz:authorize>

	    <div class="navGroup">
			<html:link action="/editUserProfile" styleId="navLink_userprofile" styleClass="navLink"><bean:message key="menu.profile" /></html:link>
	    </div>
	    
	    <div class="navGroup">
	    	<html:link action="/contact" styleId="navLink_contact" styleClass="navLink"><bean:message key="menu.contact" /></html:link>
	    </div>

	    <div class="navGroup">
    		<a href="<c:url value='/j_acegi_logout'/>" class="navLogout"><bean:message key="menu.logout" /></a>
	    </div>
    </div>
    
	<div id="master_content"><!-- wird in main_footer.jsp geschlossen -->
	    <c:if test="${hideErrorMessages == null}">
		    <html:errors/>
		    <html:messages id="message" message="true" header="success.messages.header" footer="success.messages.footer">
		        <li><bean:write name="message"/></li>
		    </html:messages>
	    </c:if>