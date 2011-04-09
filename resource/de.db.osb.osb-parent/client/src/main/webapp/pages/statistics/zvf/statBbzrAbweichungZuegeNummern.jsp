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
				    openMainMenu('navLink_auswertung-statBbzrAbweichungZuegeNummern');
				</script>
				
				<html:form action="/statistics/statBbzrAbweichungZuegeNummern">
					<jsp:include page="../suche.jsp" />
				</html:form>
				<br />

				<div class="textcontent_head">
				    <bean:message key="auswertungen.abweichungZuegeNummernGesamt" />
				</div>
				<div class="textcontent" style="text-align:center;">
					<display:table
						id="currentRow2" 
						name="reportBeanGesamt" 
						export="true"
						requestURI="/statistics/statBbzrAbweichungZuegeNummern.do" 
						pagesize="20" 
						sort="list"
						class="colored">
						
						<display:column property="label" titleKey="auswertungen.noTitle" sortable="false" style="width:150px"/>
						
						<display:column titleKey="auswertungen.anzahlUmgeleitet" media="html" >
							<logic:iterate id="currentZug" name="currentRow2" property="zuegeUmgeleitet">
								<span id="tip_${currentRow2.label}_${currentZug}">
									<div id="tip_${currentRow2.label}_${currentZug}">${currentZug}</div>
								</span>
								<%-- <div id="tooltip_${currentRow2.label}_${currentZug.zugnr}" style="width:400px;visibility:hidden;">
									<table class="colored">
										<tr class="odd">
											<th><bean:message key="ueb.zug.zugnr" /></th>
											<td><bean:write name="currentZug" property="zugnr" /></td>
										</tr>
										<tr class="even">
											<th><bean:message key="ueb.zug.datum" /></th>
											<td><bean:write name="currentZug" property="verkehrstag" format="dd.MM.yyyy"/></td>
										</tr>
										<tr class="odd">
											<th><bean:message key="ueb.zug.umleitungsstrecke" /></th>
											<td><bean:write name="currentZug" property="abweichung.umleitung" /></td>
										</tr>
									</table>
								</div>
								<script type="text/javascript">
									new Tip('tip_${currentRow2.label}_${currentZug.zugnr}',
										$('tooltip_${currentRow2.label}_${currentZug.zugnr}'),
										{
											title: '<bean:message key="zvf.zuginfo" />'
										});
								</script>--%>
							</logic:iterate>
						</display:column>
						
						<display:column titleKey="auswertungen.anzahlVerspaetet" media="html" >
							<logic:iterate id="currentZug" name="currentRow2" property="zuegeVerspaetet">
								<span id="tip_${currentRow2.label}_${currentZug}">
									<div id="tip_${currentRow2.label}_${currentZug}">${currentZug}</div>
								</span>
								<%--<div id="tooltip_${currentRow2.label}_${currentZug.zugnr}" style="width:400px;visibility:hidden;">
									<table class="colored">
										<tr class="odd">
											<th><bean:message key="ueb.zug.zugnr" /></th>
											<td><bean:write name="currentZug" property="zugnr" /></td>
										</tr>
										<tr class="even">
											<th><bean:message key="ueb.zug.datum" /></th>
											<td><bean:write name="currentZug" property="verkehrstag" format="dd.MM.yyyy"/></td>
										</tr>
										<tr class="odd">
											<th><bean:message key="ueb.zug.verspaetung" /></th>
											<td><bean:write name="currentZug" property="abweichung.verspaetung" /></td>
										</tr>
									</table>
								</div>
								<script type="text/javascript">
									new Tip('tip_${currentRow2.label}_${currentZug.zugnr}',
										$('tooltip_${currentRow2.label}_${currentZug.zugnr}'),
										{
											title: '<bean:message key="zvf.zuginfo" />'
										});
								</script>--%>
							</logic:iterate>
						</display:column>
						
						<display:column titleKey="auswertungen.anzahlVorPlan" media="html" >
							<logic:iterate id="currentZug" name="currentRow2" property="zuegeVorPlan">
								<span id="tip_${currentRow2.label}_${currentZug}">
									<div id="tip_${currentRow2.label}_${currentZug}">${currentZug}</div>
								</span>
								<%--<div id="tooltip_${currentRow2.label}_${currentZug.zugnr}" style="width:400px;visibility:hidden;">
									<table class="colored">
										<tr class="odd">
											<th><bean:message key="ueb.zug.zugnr" /></th>
											<td><bean:write name="currentZug" property="zugnr" /></td>
										</tr>
										<tr class="even">
											<th><bean:message key="ueb.zug.datum" /></th>
											<td><bean:write name="currentZug" property="verkehrstag" format="dd.MM.yyyy"/></td>
										</tr>
										<tr class="odd">
											<th><bean:message key="ueb.zug.zeitvorplan" /></th>
											<td><bean:write name="currentZug" property="abweichung.vorplanab.langName" /></td>
										</tr>
									</table>
								</div>
								<script type="text/javascript">
									new Tip('tip_${currentRow2.label}_${currentZug.zugnr}',
										$('tooltip_${currentRow2.label}_${currentZug.zugnr}'),
										{
											title: '<bean:message key="zvf.zuginfo" />'
										});
								</script>--%>
							</logic:iterate>
						</display:column>
						
						<display:column titleKey="auswertungen.anzahlAusfall" media="html" >
							<logic:iterate id="currentZug" name="currentRow2" property="zuegeAusfall">
								<span id="tip_${currentRow2.label}_${currentZug}">
									<div id="tip_${currentRow2.label}_${currentZug}">${currentZug}</div>
								</span>
								<%--<div id="tooltip_${currentRow2.label}_${currentZug.zugnr}" style="width:400px;visibility:hidden;">
									<table class="colored">
										<tr class="odd">
											<th><bean:message key="ueb.zug.zugnr" /></th>
											<td><bean:write name="currentZug" property="zugnr" /></td>
										</tr>
										<tr class="even">
											<th><bean:message key="ueb.zug.datum" /></th>
											<td><bean:write name="currentZug" property="verkehrstag" format="dd.MM.yyyy"/></td>
										</tr>
										<tr class="odd">
											<th><bean:message key="ueb.zug.ausfallab" /></th>
											<td><bean:write name="currentZug" property="abweichung.ausfallvon.langName" /></td>
										</tr>
										<tr class="even">
											<th><bean:message key="ueb.zug.ausfallbis" /></th>
											<td><bean:write name="currentZug" property="abweichung.ausfallbis.langName" /></td>
										</tr>
									</table>
								</div>
								<script type="text/javascript">
									new Tip('tip_${currentRow2.label}_${currentZug.zugnr}',
										$('tooltip_${currentRow2.label}_${currentZug.zugnr}'),
										{
											title: '<bean:message key="zvf.zuginfo" />'
										});
								</script>--%>
							</logic:iterate>
						</display:column>
						
						<display:column titleKey="auswertungen.anzahlAusfallVerkehrshalt" media="html" >
							<logic:iterate id="currentZug" name="currentRow2" property="zuegeAusfallVerkehrshalt">
								<span id="tip_${currentRow2.label}_${currentZug}">
									<div id="tip_${currentRow2.label}_${currentZug}">${currentZug}</div>
								</span>
								<%--<div id="tooltip_${currentRow2.label}_${currentZug.zugnr}" style="width:400px;visibility:hidden;">
									<table class="colored">
										<tr class="odd">
											<th><bean:message key="ueb.zug.zugnr" /></th>
											<td><bean:write name="currentZug" property="zugnr" /></td>
										</tr>
										<tr class="even">
											<th><bean:message key="ueb.zug.datum" /></th>
											<td><bean:write name="currentZug" property="verkehrstag" format="dd.MM.yyyy"/></td>
										</tr>
										<tr class="odd">
											<th><bean:message key="ueb.zug.ausfallverkehrshalt" /></th>
											<td>
												<table>
													<tbody>
														<logic:iterate id="currentHalt" name="currentZug" property="abweichung.halt" indexId="haltId">
															<tr>
																<td>
																	<logic:notEmpty name="currentHalt" property="ausfall">
																		<div style="text-align:center;"><bean:write name="currentHalt" property="ausfall.langName" /></div>
																	</logic:notEmpty>
																</td>
															</tr>
														</logic:iterate>
													</tbody>
												</table>
											</td>
										</tr>
									</table>
								</div>
								<script type="text/javascript">
									new Tip('tip_${currentRow2.label}_${currentZug.zugnr}',
										$('tooltip_${currentRow2.label}_${currentZug.zugnr}'),
										{
											title: '<bean:message key="zvf.zuginfo" />'
										});
								</script>--%>
							</logic:iterate>
						</display:column>
						
						<display:column titleKey="auswertungen.anzahlBedarfsplanGesperrt" media="html" >
							<logic:iterate id="currentZug" name="currentRow2" property="zuegeBedarfsplanGesperrt">
								<span id="tip_${currentRow2.label}_${currentZug}">
									<div id="tip_${currentRow2.label}_${currentZug}">${currentZug}</div>
								</span>
								<%--<div id="tooltip_${currentRow2.label}_${currentZug.zugnr}" style="width:400px;visibility:hidden;">
									<table class="colored">
										<tr class="odd">
											<th><bean:message key="ueb.zug.zugnr" /></th>
											<td><bean:write name="currentZug" property="zugnr" /></td>
										</tr>
										<tr class="even">
											<th><bean:message key="ueb.zug.datum" /></th>
											<td><bean:write name="currentZug" property="verkehrstag" format="dd.MM.yyyy"/></td>
										</tr>
									</table>
								</div>
								<script type="text/javascript">
									new Tip('tip_${currentRow2.label}_${currentZug.zugnr}',
										$('tooltip_${currentRow2.label}_${currentZug.zugnr}'),
										{
											title: '<bean:message key="zvf.zuginfo" />'
										});
								</script>--%>
							</logic:iterate>
						</display:column>
						
						<display:column titleKey="auswertungen.anzahlRegelungen" media="html" >
							<logic:iterate id="currentZug" name="currentRow2" property="zuegeRegelung">
								<span id="tip_${currentRow2.label}_${currentZug}">
									<div id="tip_${currentRow2.label}_${currentZug}">${currentZug}</div>
								</span>
								<%--<div id="tooltip_${currentRow2.label}_${currentZug.zugnr}" style="width:400px;visibility:hidden;">
									<table class="colored">
										<tr class="odd">
											<th><bean:message key="ueb.zug.zugnr" /></th>
											<td><bean:write name="currentZug" property="zugnr" /></td>
										</tr>
										<tr class="even">
											<th><bean:message key="ueb.zug.datum" /></th>
											<td><bean:write name="currentZug" property="verkehrstag" format="dd.MM.yyyy"/></td>
										</tr>
									</table>
								</div>
								<script type="text/javascript">
									new Tip('tip_${currentRow2.label}_${currentZug.zugnr}',
										$('tooltip_${currentRow2.label}_${currentZug.zugnr}'),
										{
											title: '<bean:message key="zvf.zuginfo" />'
										});
								</script>--%>
							</logic:iterate>
						</display:column>
						
						<display:column titleKey="auswertungen.anzahlUmgeleitet" media="excel">
							<logic:iterate id="currentZug" name="currentRow2" property="zuegeUmgeleitet">${currentZug},</logic:iterate>
						</display:column>
						<display:column titleKey="auswertungen.anzahlVerspaetet" media="excel">
							<logic:iterate id="currentZug" name="currentRow2" property="zuegeVerspaetet">${currentZug},</logic:iterate>
						</display:column>
						<display:column titleKey="auswertungen.anzahlVorPlan" media="excel">
							<logic:iterate id="currentZug" name="currentRow2" property="zuegeVorPlan">${currentZug},</logic:iterate>
						</display:column>
						<display:column titleKey="auswertungen.anzahlAusfall" media="excel">
							<logic:iterate id="currentZug" name="currentRow2" property="zuegeAusfall">${currentZug},</logic:iterate>
						</display:column>
						<display:column titleKey="auswertungen.anzahlAusfallVerkehrshalt" media="excel">
							<logic:iterate id="currentZug" name="currentRow2" property="zuegeAusfallVerkehrshalt">${currentZug},</logic:iterate>
						</display:column>
						<display:column titleKey="auswertungen.anzahlBedarfsplanGesperrt" media="excel">
							<logic:iterate id="currentZug" name="currentRow2" property="zuegeBedarfsplanGesperrt">${currentZug},</logic:iterate>
						</display:column>
						<display:column titleKey="auswertungen.anzahlRegelungen" media="excel">
							<logic:iterate id="currentZug" name="currentRow2" property="zuegeRegelung">${currentZug},</logic:iterate>
						</display:column>
					</display:table>
				</div>
				<br>
				
				
				<div class="textcontent_head">
				    <bean:message key="auswertungen.abweichungZuegeNummernEVU" />
				</div>
				<div class="textcontent" style="text-align:center;">
					<display:table
						id="currentRow" 
						name="reportBeanEVU" 
						export="true"
						requestURI="/statistics/statBbzrAbweichungZuegeNummern.do" 
						pagesize="20" 
						sort="list"
						class="colored">
						
						<display:column property="label" titleKey="auswertungen.evu" sortable="true" style="width:150px"/>
						
						<display:column titleKey="auswertungen.anzahlUmgeleitet" media="html">
							<logic:iterate id="currentZug" name="currentRow" property="zuegeUmgeleitet">
								<span id="tip_${currentRow.label}_${currentZug}_evu">
									<div id="tip_${currentRow.label}_${currentZug}_evu">
										${currentZug}
									</div>
								</span>
								<%--<div id="tooltip_${currentRow.label}_${currentZug.zugnr}_evu" style="width:400px;visibility:hidden;">
									<table class="colored">
										<tr class="odd">
											<th><bean:message key="ueb.zug.zugnr" /></th>
											<td><bean:write name="currentZug" property="zugnr" /></td>
										</tr>
										<tr class="even">
											<th><bean:message key="ueb.zug.datum" /></th>
											<td><bean:write name="currentZug" property="verkehrstag" format="dd.MM.yyyy"/></td>
										</tr>
										<tr class="odd">
											<th><bean:message key="ueb.zug.umleitungsstrecke" /></th>
											<td><bean:write name="currentZug" property="abweichung.umleitung" /></td>
										</tr>
									</table>
								</div>
								<script type="text/javascript">
									new Tip('tip_${currentRow.label}_${currentZug.zugnr}_evu',
										$('tooltip_${currentRow.label}_${currentZug.zugnr}_evu'),
										{
											title: '<bean:message key="zvf.zuginfo" />'
										});
								</script>--%>
							</logic:iterate>
						</display:column>
						
						<display:column titleKey="auswertungen.anzahlVerspaetet" media="html" >
							<logic:iterate id="currentZug" name="currentRow" property="zuegeVerspaetet">
								<span id="tip_${currentRow.label}_${currentZug}_evu">
									<div id="tip_${currentRow.label}_${currentZug}_evu">${currentZug}</div>
								</span>
								<%--<div id="tooltip_${currentRow.label}_${currentZug.zugnr}_evu" style="width:400px;visibility:hidden;">
									<table class="colored">
										<tr class="odd">
											<th><bean:message key="ueb.zug.zugnr" /></th>
											<td><bean:write name="currentZug" property="zugnr" /></td>
										</tr>
										<tr class="even">
											<th><bean:message key="ueb.zug.datum" /></th>
											<td><bean:write name="currentZug" property="verkehrstag" format="dd.MM.yyyy"/></td>
										</tr>
										<tr class="odd">
											<th><bean:message key="ueb.zug.verspaetung" /></th>
											<td><bean:write name="currentZug" property="abweichung.verspaetung" /></td>
										</tr>
									</table>
								</div>
								<script type="text/javascript">
									new Tip('tip_${currentRow.label}_${currentZug.zugnr}_evu',
										$('tooltip_${currentRow.label}_${currentZug.zugnr}_evu'),
										{
											title: '<bean:message key="zvf.zuginfo" />'
										});
								</script>--%>
							</logic:iterate>
						</display:column>
						
						<display:column titleKey="auswertungen.anzahlVorPlan"  media="html">
							<logic:iterate id="currentZug" name="currentRow" property="zuegeVorPlan">
								<span id="tip_${currentRow.label}_${currentZug}_evu">
									<div id="tip_${currentRow.label}_${currentZug}_evu">${currentZug}</div>
								</span>
								<%--<div id="tooltip_${currentRow.label}_${currentZug.zugnr}_evu" style="width:400px;visibility:hidden;">
									<table class="colored">
										<tr class="odd">
											<th><bean:message key="ueb.zug.zugnr" /></th>
											<td><bean:write name="currentZug" property="zugnr" /></td>
										</tr>
										<tr class="even">
											<th><bean:message key="ueb.zug.datum" /></th>
											<td><bean:write name="currentZug" property="verkehrstag" format="dd.MM.yyyy"/></td>
										</tr>
										<tr class="odd">
											<th><bean:message key="ueb.zug.zeitvorplan" /></th>
											<td><bean:write name="currentZug" property="abweichung.vorplanab.langName" /></td>
										</tr>
									</table>
								</div>
								<script type="text/javascript">
									new Tip('tip_${currentRow.label}_${currentZug.zugnr}_evu',
										$('tooltip_${currentRow.label}_${currentZug.zugnr}_evu'),
										{
											title: '<bean:message key="zvf.zuginfo" />'
										});
								</script>--%>
							</logic:iterate>
						</display:column>
						
						<display:column titleKey="auswertungen.anzahlAusfall"  media="html">
							<logic:iterate id="currentZug" name="currentRow" property="zuegeAusfall">
								<span id="tip_${currentRow.label}_${currentZug}_evu">
									<div id="tip_${currentRow.label}_${currentZug}_evu">${currentZug}</div>
								</span>
								<%--<div id="tooltip_${currentRow.label}_${currentZug.zugnr}_evu" style="width:400px;visibility:hidden;">
									<table class="colored">
										<tr class="odd">
											<th><bean:message key="ueb.zug.zugnr" /></th>
											<td><bean:write name="currentZug" property="zugnr" /></td>
										</tr>
										<tr class="even">
											<th><bean:message key="ueb.zug.datum" /></th>
											<td><bean:write name="currentZug" property="verkehrstag" format="dd.MM.yyyy"/></td>
										</tr>
										<tr class="odd">
											<th><bean:message key="ueb.zug.ausfallab" /></th>
											<td><bean:write name="currentZug" property="abweichung.ausfallvon.langName" /></td>
										</tr>
										<tr class="even">
											<th><bean:message key="ueb.zug.ausfallbis" /></th>
											<td><bean:write name="currentZug" property="abweichung.ausfallbis.langName" /></td>
										</tr>
									</table>
								</div>
								<script type="text/javascript">
									new Tip('tip_${currentRow.label}_${currentZug.zugnr}_evu',
										$('tooltip_${currentRow.label}_${currentZug.zugnr}_evu'),
										{
											title: '<bean:message key="zvf.zuginfo" />'
										});
								</script>--%>
							</logic:iterate>
						</display:column>
						
						<display:column titleKey="auswertungen.anzahlAusfallVerkehrshalt" media="html" >
							<logic:iterate id="currentZug" name="currentRow" property="zuegeAusfallVerkehrshalt">
								<span id="tip_${currentRow.label}_${currentZug}_evu">
									<div id="tip_${currentRow.label}_${currentZug}_evu">${currentZug}</div>
								</span>
								<%--<div id="tooltip_${currentRow.label}_${currentZug.zugnr}_evu" style="width:400px;visibility:hidden;">
									<table class="colored">
										<tr class="odd">
											<th><bean:message key="ueb.zug.zugnr" /></th>
											<td><bean:write name="currentZug" property="zugnr" /></td>
										</tr>
										<tr class="even">
											<th><bean:message key="ueb.zug.datum" /></th>
											<td><bean:write name="currentZug" property="verkehrstag" format="dd.MM.yyyy"/></td>
										</tr>
										<tr class="odd">
											<th><bean:message key="ueb.zug.ausfallverkehrshalt" /></th>
											<td>
												<table>
													<tbody>
														<logic:iterate id="currentHalt" name="currentZug" property="abweichung.halt" indexId="haltId">
															<tr>
																<td>
																	<logic:notEmpty name="currentHalt" property="ausfall">
																		<div style="text-align:center;"><bean:write name="currentHalt" property="ausfall.langName" /></div>
																	</logic:notEmpty>
																</td>
															</tr>
														</logic:iterate>
													</tbody>
												</table>
											</td>
										</tr>
									</table>
								</div>
								<script type="text/javascript">
									new Tip('tip_${currentRow.label}_${currentZug.zugnr}_evu',
										$('tooltip_${currentRow.label}_${currentZug.zugnr}_evu'),
										{
											title: '<bean:message key="zvf.zuginfo" />'
										});
								</script>--%>
							</logic:iterate>
						</display:column>
						
						<display:column titleKey="auswertungen.anzahlBedarfsplanGesperrt" media="html" >
							<logic:iterate id="currentZug" name="currentRow" property="zuegeBedarfsplanGesperrt">
								<span id="tip_${currentRow.label}_${currentZug}_evu">
									<div id="tip_${currentRow.label}_${currentZug}_evu">${currentZug}</div>
								</span>
								<%--<div id="tooltip_${currentRow.label}_${currentZug.zugnr}_evu" style="width:400px;visibility:hidden;">
									<table class="colored">
										<tr class="odd">
											<th><bean:message key="ueb.zug.zugnr" /></th>
											<td><bean:write name="currentZug" property="zugnr" /></td>
										</tr>
										<tr class="even">
											<th><bean:message key="ueb.zug.datum" /></th>
											<td><bean:write name="currentZug" property="verkehrstag" format="dd.MM.yyyy"/></td>
										</tr>
									</table>
								</div>
								<script type="text/javascript">
									new Tip('tip_${currentRow.label}_${currentZug.zugnr}_evu',
										$('tooltip_${currentRow.label}_${currentZug.zugnr}_evu'),
										{
											title: '<bean:message key="zvf.zuginfo" />'
										});
								</script>--%>
							</logic:iterate>
						</display:column>
						
						<display:column titleKey="auswertungen.anzahlRegelungen" media="html" >
							<logic:iterate id="currentZug" name="currentRow" property="zuegeRegelung">
								<span id="tip_${currentRow.label}_${currentZug}_evu">
									<div id="tip_${currentRow.label}_${currentZug}_evu">${currentZug}</div>
								</span>
								<%--<div id="tooltip_${currentRow.label}_${currentZug.zugnr}_evu" style="width:400px;visibility:hidden;">
									<table class="colored">
										<tr class="odd">
											<th><bean:message key="ueb.zug.zugnr" /></th>
											<td><bean:write name="currentZug" property="zugnr" /></td>
										</tr>
										<tr class="even">
											<th><bean:message key="ueb.zug.datum" /></th>
											<td><bean:write name="currentZug" property="verkehrstag" format="dd.MM.yyyy"/></td>
										</tr>
									</table>
								</div>
								<script type="text/javascript">
									new Tip('tip_${currentRow.label}_${currentZug.zugnr}_evu',
										$('tooltip_${currentRow.label}_${currentZug.zugnr}_evu'),
										{
											title: '<bean:message key="zvf.zuginfo" />'
										});
								</script>--%>
							</logic:iterate>
						</display:column>
						
						<display:column titleKey="auswertungen.anzahlUmgeleitet" media="excel">
							<logic:iterate id="currentZug" name="currentRow" property="zuegeUmgeleitet">${currentZug},</logic:iterate>
						</display:column>
						<display:column titleKey="auswertungen.anzahlVerspaetet" media="excel">
							<logic:iterate id="currentZug" name="currentRow" property="zuegeVerspaetet">${currentZug},</logic:iterate>
						</display:column>
						<display:column titleKey="auswertungen.anzahlVorPlan" media="excel">
							<logic:iterate id="currentZug" name="currentRow" property="zuegeVorPlan">${currentZug},</logic:iterate>
						</display:column>
						<display:column titleKey="auswertungen.anzahlAusfall" media="excel">
							<logic:iterate id="currentZug" name="currentRow" property="zuegeAusfall">${currentZug},</logic:iterate>
						</display:column>
						<display:column titleKey="auswertungen.anzahlAusfallVerkehrshalt" media="excel">
							<logic:iterate id="currentZug" name="currentRow" property="zuegeAusfallVerkehrshalt">${currentZug},</logic:iterate>
						</display:column>
						<display:column titleKey="auswertungen.anzahlBedarfsplanGesperrt" media="excel">
							<logic:iterate id="currentZug" name="currentRow" property="zuegeBedarfsplanGesperrt">${currentZug},</logic:iterate>
						</display:column>
						<display:column titleKey="auswertungen.anzahlRegelungen" media="excel">
							<logic:iterate id="currentZug" name="currentRow" property="zuegeRegelung">${currentZug},</logic:iterate>
						</display:column>
					</display:table>
				</div>
				
				<%-- Tooltips --%>
				

				
<jsp:include page="../../main_footer.jsp" />