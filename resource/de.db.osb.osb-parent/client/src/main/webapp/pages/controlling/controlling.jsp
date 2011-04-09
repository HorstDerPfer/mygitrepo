<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="db.training.bob.model.Status"%>
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
				    openMainMenu('navLink_controlling');
				</script>
				
				<html:form action="/controlling">
					<div class="textcontent_head">
					    <bean:message key="menu.controlling" />
					</div>

					<div class="textcontent">
						<div class="textcontent_left">
							<div class="box">
								<div class="label" style="width:140px;"><bean:message key="baumassnahme.regionalbereichfpl" /></div>
								<div class="input">
									<html:select property="regionalbereich" style="width:210px;">
										<html:option value="" key="common.select.option"></html:option>
										<html:optionsCollection name="regionalbereiche" label="name" value="id" />
									</html:select>
								</div>
							</div>
							
							<div class="box">
								<div class="label" style="width:140px;"><bean:message key="controlling.termin" /></div>
								<div class="input">
									<html:select property="meilenstein" style="width:210px;">
										<html:option key="common.select.option" value=""></html:option>
										<html:option key="baumassnahme.termine.anforderungbbzr" value="anforderungbbzr" />
										<html:option key="baumassnahme.termine.biueentwurf.nobr" value="biueentwurf" />
										<html:option key="baumassnahme.termine.zvfentwurf.nobr" value="zvfentwurf" />
										<html:option key="baumassnahme.termine.koordinationsergebnis.nobr" value="koordinationsergebnis" />
										<html:option key="baumassnahme.termine.stellungnahmeevu.nobr" value="stellungnahmeevu" />
										<html:option key="baumassnahme.termine.gesamtkonzeptbbzr.nobr" value="gesamtkonzeptbbzr" />
										<html:option key="baumassnahme.termine.biue" value="biue" />
										<html:option key="baumassnahme.termine.zvf" value="zvf" />
										<html:option key="baumassnahme.termine.masteruebergabeblattpv.nobr" value="masteruebergabeblattpv" />
										<html:option key="baumassnahme.termine.masteruebergabeblattgv.nobr" value="masteruebergabeblattgv" />
										<html:option key="baumassnahme.termine.uebergabeblattpv" value="uebergabeblattpv" />
										<html:option key="baumassnahme.termine.uebergabeblattgv" value="uebergabeblattgv" />
										<html:option key="baumassnahme.termine.fplo" value="fplo" />
										<html:option key="baumassnahme.termine.eingabegfd_z" value="eingabegfdz" />
									</html:select>
								</div>
							</div>
						</div>
						
						<div class="textcontent_right">
							<div class="box">
								<div class="label"><bean:message key="baumassnahme.beginndatum" /></div>
								<div class="input">
									<html:text property="beginnDatum" maxlength="10" styleClass="date" />
									<img src="<c:url value='/static/img/calendar.gif' />" id="buttonBeginnDatum" />
								</div>
							</div>
							<div class="box">
								<div class="label"><bean:message key="baumassnahme.enddatum" /></div>
								<div class="input">
									<html:text property="endDatum" maxlength="10" styleClass="date" />
									<img src="<c:url value='/static/img/calendar.gif' />" id="buttonEndDatum" />
								</div>
							</div>
						</div>
					</div>
					<div class="buttonBar">
						<input type="submit" class="hiddenSubmit" />
						<html:link styleClass="buttonSearch" href="javascript:document.getElementById('controllingFilterForm').submit();">
							<bean:message key="controlling.update" />
						</html:link>
					</div>
					
					<script type="text/javascript">
					  Calendar.setup(
					    {
					      inputField  : "beginnDatum",         // ID of the input field
					      ifFormat    : "%d.%m.%Y",    // the date format
					      button      : "buttonBeginnDatum",       // ID of the button
					      eventName	  : "click"
					    }
					  );
					  Calendar.setup(
					    {
					      inputField  : "endDatum",         // ID of the input field
					      ifFormat    : "%d.%m.%Y",    // the date format
					      button      : "buttonEndDatum",       // ID of the button
					      eventName   : "click"
					    }
					  );
					</script>
				</html:form>
				
				<br />

				<div class="textcontent_head">
				    <bean:message key="menu.baumassnahme.list" />
				</div>

				<div class="textcontent" style="text-align:center;">
				
					<jsp:useBean id="urls" class="java.util.HashMap"/>   
					<display:table
						id="currentBaumassnahme" 
						name="baumassnahmen" 
						export="false"
						requestURI="listBaumassnahmen.do?method=web" 
						pagesize="20" 
						sort="list"
						class="colored"
						decorator="db.training.easy.util.displaytag.decorators.AddRowLink">
					
						<%-- URL wird erzeugt, ggf. inkl. sessionId --%>
						<c:set target="${urls}" property="${currentBaumassnahme.id}" value="#" />
					
						<c:set target="${urls}" property="${currentBaumassnahme.id}">
							<c:url value="viewBaumassnahme.do?id=${currentBaumassnahme.id}" />
						</c:set>

						<display:column property="streckenAbschnitt" titleKey="baumassnahme.streckenabschnitt" sortable="true" />
						<display:column property="artDerMassnahme" titleKey="baumassnahme.artdermassnahme" sortable="true" />
						<display:column property="fploNr" titleKey="baumassnahme.fplonr" sortable="true" />
						<display:column property="bearbeitungsbereich.name" titleKey="baumassnahme.bearbeitungsbereich" sortable="true" />
						<display:column property="prioritaet.value" titleKey="baumassnahme.prioritaet" sortable="true" style="text-align:center;"/>
						<display:column property="art" titleKey="baumassnahme.art" sortable="true" style="text-align:center;"/>
					
						<display:setProperty name="basic.empty.showtable" value="true" />
						<display:setProperty name="paging.banner.item_name"><bean:message key="baumassnahme" /></display:setProperty>
						<display:setProperty name="paging.banner.items_name"><bean:message key="baumassnahmen" /></display:setProperty>
						
						<logic:equal name="ControllingFilterForm" property="meilenstein" value="anforderungbbzr">
							<logic:equal name="currentBaumassnahme" property="baubetriebsplanung.anforderungBBZRStatus" value="RED">
								<display:column titleKey="controlling.status" value="<img src='static/img/ampelred.gif' />" />
							</logic:equal>
							<logic:equal name="currentBaumassnahme" property="baubetriebsplanung.anforderungBBZRStatus" value="GREEN">
								<display:column titleKey="controlling.status" value="<img src='static/img/ampelgreen.gif' />" />
							</logic:equal>
						</logic:equal>
						
						<logic:equal name="ControllingFilterForm" property="meilenstein" value="biueentwurf">
							<logic:equal name="currentBaumassnahme" property="baubetriebsplanung.biUeEntwurfStatus" value="RED">
								<display:column titleKey="controlling.status" value="<img src='static/img/ampelred.gif' />" />
							</logic:equal>
							<logic:equal name="currentBaumassnahme" property="baubetriebsplanung.biUeEntwurfStatus" value="GREEN">
								<display:column titleKey="controlling.status" value="<img src='static/img/ampelgreen.gif'
 />" />
							</logic:equal>
						</logic:equal>
						
						<logic:equal name="ControllingFilterForm" property="meilenstein" value="zvfentwurf">
							<logic:equal name="currentBaumassnahme" property="baubetriebsplanung.zvfEntwurfStatus" value="RED">
								<display:column titleKey="controlling.status" value="<img src='static/img/ampelred.gif' />" />
							</logic:equal>
							<logic:equal name="currentBaumassnahme" property="baubetriebsplanung.bzvfEntwurfStatus" value="GREEN">
								<display:column titleKey="controlling.status" value="<img src='static/img/ampelgreen.gif' />" />
							</logic:equal>
						</logic:equal>
						
						<logic:equal name="ControllingFilterForm" property="meilenstein" value="koordinationsergebnis">
							<logic:equal name="currentBaumassnahme" property="baubetriebsplanung.koordinationsErgebnisStatus" value="RED">
								<display:column titleKey="controlling.status" value="<img src='static/img/ampelred.gif' />" />
							</logic:equal>
							<logic:equal name="currentBaumassnahme" property="baubetriebsplanung.koordinationsErgebnisStatus" value="GREEN">
								<display:column titleKey="controlling.status" value="<img src='static/img/ampelgreen.gif' />" />
							</logic:equal>
						</logic:equal>
						
						<logic:equal name="ControllingFilterForm" property="meilenstein" value="stellungnahmeevu">
							<bean:define id="status" type="db.training.bob.model.Status" value="RED" />
							<logic:iterate id="currentPevu" name="currentBaumassnahme" type="db.training.bob.model.TerminUebersichtPersonenverkehrsEVU" property="pevus">
								<%status =currentPevu.getStellungnahmeEVUStatus(); %>
							</logic:iterate>
							<logic:equal name="status"value="RED">
								<display:column titleKey="controlling.status" value="<img src='static/img/ampelred.gif' />" />
							</logic:equal>
							<logic:equal name="status" value="GREEN">
								<display:column titleKey="controlling.status" value="<img src='static/img/ampelgreen.gif' />" />
							</logic:equal>
							<%--<logic:equal name="currentPevu" property="stellungnahmeEVUStatus" value="RED">
								<display:column titleKey="controlling.status" value="<img src='static/img/ampelred.gif' />" />
							</logic:equal>
							<logic:equal name="currentPevu" property="stellungnahmeEVUStatus" value="GREEN">
								<display:column titleKey="controlling.status" value="<img src='static/img/ampelgreen.gif' />" />
							</logic:equal>--%>
						</logic:equal>
						
						<logic:equal name="ControllingFilterForm" property="meilenstein" value="gesamtkonzeptbbzr">
						<logic:equal name="currentBaumassnahme" property="baubetriebsplanung.gesamtKonzeptBBZRStatus" value="RED">
								<display:column titleKey="controlling.status" value="<img src='static/img/ampelred.gif' />" />
							</logic:equal>
							<logic:equal name="currentBaumassnahme" property="baubetriebsplanung.gesamtKonzeptBBZRStatus" value="GREEN">
								<display:column titleKey="controlling.status" value="<img src='static/img/ampelgreen.gif' />" />
							</logic:equal>
						</logic:equal>
						
						<logic:equal name="ControllingFilterForm" property="meilenstein" value="biue">
							<logic:equal name="currentBaumassnahme" property="baubetriebsplanung.biUeStatus" value="RED">
								<display:column titleKey="controlling.status" value="<img src='static/img/ampelred.gif' />" />
							</logic:equal>
							<logic:equal name="currentBaumassnahme" property="baubetriebsplanung.biUeStatus" value="GREEN">
								<display:column titleKey="controlling.status" value="<img src='static/img/ampelgreen.gif' />" />
							</logic:equal>
						</logic:equal>
						
						<logic:equal name="ControllingFilterForm" property="meilenstein" value="zvf">
							<logic:equal name="currentBaumassnahme" property="baubetriebsplanung.zvfStatus" value="RED">
								<display:column titleKey="controlling.status" value="<img src='static/img/ampelred.gif' />" />
							</logic:equal>
							<logic:equal name="currentBaumassnahme" property="baubetriebsplanung.zvfStatus" value="GREEN">
								<display:column titleKey="controlling.status" value="<img src='static/img/ampelgreen.gif' />" />
							</logic:equal>
						</logic:equal>
						
						<logic:equal name="ControllingFilterForm" property="meilenstein" value="masteruebergabeblattpv">
						</logic:equal>
						<logic:equal name="ControllingFilterForm" property="meilenstein" value="masteruebergabeblattgv">
						</logic:equal>
						<logic:equal name="ControllingFilterForm" property="meilenstein" value="uebergabeblattpv">
						</logic:equal>
						<logic:equal name="ControllingFilterForm" property="meilenstein" value="uebergabeblattgv">
						</logic:equal>
						<logic:equal name="ControllingFilterForm" property="meilenstein" value="fplo">
						</logic:equal>
						<logic:equal name="ControllingFilterForm" property="meilenstein" value="eingabegfdz">
						</logic:equal>
					</display:table>
				</div>
				
				
<jsp:include page="../main_footer.jsp" />