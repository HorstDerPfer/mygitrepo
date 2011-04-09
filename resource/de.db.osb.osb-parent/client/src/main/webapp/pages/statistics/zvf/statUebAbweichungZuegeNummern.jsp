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
				    openMainMenu('navLink_auswertung-statUebAbweichungZuegeNummern');
				</script>
				
				<html:form action="/statistics/statUebAbweichungZuegeNummern">
					<jsp:include page="../suche.jsp" />
				</html:form>
				<br />

				<div class="textcontent_head">
				    <bean:message key="auswertungen.abweichungZuegeNummernGesamt" />
				</div>
				<div class="textcontent" style="text-align:center;">
					<display:table
						id="currentRow" 
						name="reportBeanGesamt" 
						export="true"
						requestURI="/statistics/statUebAbweichungZuegeNummern.do" 
						pagesize="20" 
						sort="list"
						class="colored">
						
						<display:column property="label" titleKey="auswertungen.noTitle" sortable="false" style="width:150px"/>
						
						<display:column titleKey="auswertungen.anzahlUmgeleitet" media="html" >
							<logic:iterate id="currentZug" name="currentRow" property="zuegeUmgeleitet">
								<span id="tip_${currentRow.label}_${currentZug.zugnr}_${currentZug.verkehrstag}">
									<div id="tip_${currentRow.label}_${currentZug.zugnr}_${currentZug.verkehrstag}">${currentZug.zugnr}</div>
								</span>
								<div id="tooltip_${currentRow.label}_${currentZug.zugnr}_${currentZug.verkehrstag}" style="width:400px;visibility:hidden;">
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
											<th><bean:message key="ueb.zug.knotenzeiten" /></th>
											<td>
												<table>
													<thead>
														<tr>
															<th>
																<div style="text-align:center"><bean:message key="ueb.zug.knotenzeiten.bahnhof" /></div>
															</th>
															<th>
																<div style="text-align:center"><bean:message key="ueb.zug.knotenzeiten.haltart" /></div>
															</th>
															<th>
																<div style="text-align:center"><bean:message key="ueb.zug.knotenzeiten.an" /></div>
															</th>
															<th>
																<div style="text-align:center"><bean:message key="ueb.zug.knotenzeiten.ab" /></div>
															</th>
															<th>
																<div style="text-align:center"><bean:message key="ueb.zug.knotenzeiten.relativlage" /></div>
															</th>
														</tr>
													</thead>
													<tbody>
														<logic:iterate id="currentKnoten" name="currentZug" property="knotenzeiten" indexId="iKn">
															<tr>
																<td style="width:10%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="bahnhof"  /></div>
																</td>
																<td style="width:15%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="haltart"  /></div>
																</td>
																<td style="width:5%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="ankunft" format="HH:mm" /></div>
																</td>
																<td style="width:5%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="abfahrt" format="HH:mm" /></div>
																</td>
																<td style="width:15%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="relativlage"  /></div>
																</td>
															</tr>
														</logic:iterate>
													</tbody>
												</table>	
											</td>	
										</tr>
										<tr class="even">
											<th><bean:message key="ueb.zug.verspaetung" /></th>
											<td><bean:write name="currentZug" property="abweichung.verspaetung"/></td>
										</tr>
									</table>
								</div>
								<script type="text/javascript">
									new Tip('tip_${currentRow.label}_${currentZug.zugnr}_${currentZug.verkehrstag}',
										$('tooltip_${currentRow.label}_${currentZug.zugnr}_${currentZug.verkehrstag}'),
										{
											title: '<bean:message key="zvf.zuginfo" />'
										});
								</script>
							</logic:iterate>
						</display:column>
						
						<display:column titleKey="auswertungen.anzahlVerspaetet" media="html" >
							<logic:iterate id="currentZug" name="currentRow" property="zuegeVerspaetet">
								<span id="tip_${currentRow.label}_${currentZug.zugnr}_${currentZug.verkehrstag}">
									<div id="tip_${currentRow.label}_${currentZug.zugnr}_${currentZug.verkehrstag}">${currentZug.zugnr}</div>
								</span>
								<div id="tooltip_${currentRow.label}_${currentZug.zugnr}_${currentZug.verkehrstag}" style="width:400px;visibility:hidden;">
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
											<th><bean:message key="ueb.zug.knotenzeiten" /></th>
											<td>
												<table>
													<tbody>
														<logic:iterate id="currentKnoten" name="currentZug" property="knotenzeiten" indexId="iKn">
															<tr>
																<td style="width:10%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="bahnhof"  /></div>
																</td>
																<td style="width:15%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="haltart"  /></div>
																</td>
																<td style="width:5%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="ankunft" format="HH:mm" /></div>
																</td>
																<td style="width:5%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="abfahrt" format="HH:mm" /></div>
																</td>
																<td style="width:15%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="relativlage"  /></div>
																</td>
															</tr>
														</logic:iterate>
													</tbody>
												</table>	
											</td>	
										</tr>
										<tr class="even">
											<th><bean:message key="ueb.zug.verspaetung" /></th>
											<td><bean:write name="currentZug" property="abweichung.verspaetung"/></td>
										</tr>
									</table>
								</div>
								<script type="text/javascript">
									new Tip('tip_${currentRow.label}_${currentZug.zugnr}_${currentZug.verkehrstag}',
										$('tooltip_${currentRow.label}_${currentZug.zugnr}_${currentZug.verkehrstag}'),
										{
											title: '<bean:message key="zvf.zuginfo" />'
										});
								</script>
							</logic:iterate>
						</display:column>
						
						<display:column titleKey="auswertungen.anzahlVorPlan" media="html" >
							<logic:iterate id="currentZug" name="currentRow" property="zuegeVorPlan">
								<span id="tip_${currentRow.label}_${currentZug.zugnr}_${currentZug.verkehrstag}">
									<div id="tip_${currentRow.label}_${currentZug.zugnr}_${currentZug.verkehrstag}">${currentZug.zugnr}</div>
								</span>
								<div id="tooltip_${currentRow.label}_${currentZug.zugnr}_${currentZug.verkehrstag}" style="width:400px;visibility:hidden;">
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
											<th><bean:message key="ueb.zug.knotenzeiten" /></th>
											<td>
												<table>
													<tbody>
														<logic:iterate id="currentKnoten" name="currentZug" property="knotenzeiten" indexId="iKn">
															<tr>
																<td style="width:10%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="bahnhof"  /></div>
																</td>
																<td style="width:15%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="haltart"  /></div>
																</td>
																<td style="width:5%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="ankunft" format="HH:mm" /></div>
																</td>
																<td style="width:5%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="abfahrt" format="HH:mm" /></div>
																</td>
																<td style="width:15%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="relativlage"  /></div>
																</td>
															</tr>
														</logic:iterate>
													</tbody>
												</table>	
											</td>	
										</tr>
										<tr class="even">
											<th><bean:message key="ueb.zug.verspaetung" /></th>
											<td><bean:write name="currentZug" property="abweichung.verspaetung"/></td>
										</tr>
									</table>
								</div>
								<script type="text/javascript">
									new Tip('tip_${currentRow.label}_${currentZug.zugnr}_${currentZug.verkehrstag}',
										$('tooltip_${currentRow.label}_${currentZug.zugnr}_${currentZug.verkehrstag}'),
										{
											title: '<bean:message key="zvf.zuginfo" />'
										});
								</script>
							</logic:iterate>
						</display:column>
						
						
						<display:column titleKey="auswertungen.anzahlUmgeleitet" media="excel">
							<logic:iterate id="currentZug" name="currentRow" property="zuegeUmgeleitet">${currentZug.zugnr},</logic:iterate>
						</display:column>
						<display:column titleKey="auswertungen.anzahlVerspaetet" media="excel">
							<logic:iterate id="currentZug" name="currentRow" property="zuegeVerspaetet">${currentZug.zugnr},</logic:iterate>
						</display:column>
						<display:column titleKey="auswertungen.anzahlVorPlan" media="excel">
							<logic:iterate id="currentZug" name="currentRow" property="zuegeVorPlan">${currentZug.zugnr},</logic:iterate>
						</display:column>
					</display:table>
				</div>
				<br>
				
				
				<div class="textcontent_head">
				    <bean:message key="auswertungen.abweichungZuegeNummernEVU" />
				</div>
				<div class="textcontent" style="text-align:center;">
					<display:table
						id="currentRow2" 
						name="reportBeanEVU" 
						export="true"
						requestURI="/statistics/statUebAbweichungZuegeNummern.do" 
						pagesize="20" 
						sort="list"
						class="colored">
						
						<display:column property="label" titleKey="auswertungen.evu" sortable="true" style="width:150px"/>
						
						<display:column titleKey="auswertungen.anzahlUmgeleitet" media="html">
							<logic:iterate id="currentZug" name="currentRow2" property="zuegeUmgeleitet">
								<span id="tip_${currentRow2.label}_${currentZug.zugnr}_${currentZug.verkehrstag}_evu">
									<div id="tip_${currentRow2.label}_${currentZug.zugnr}_${currentZug.verkehrstag}_evu">
										<bean:write name="currentZug" property="zugnr"/>
									</div>
								</span>
								<div id="tooltip_${currentRow2.label}_${currentZug.zugnr}_${currentZug.verkehrstag}_evu" style="width:400px;visibility:hidden;">
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
											<th><bean:message key="ueb.zug.knotenzeiten" /></th>
											<td>
												<table>
													<tbody>
														<logic:iterate id="currentKnoten" name="currentZug" property="knotenzeiten" indexId="iKn">
															<tr>
																<td style="width:10%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="bahnhof"  /></div>
																</td>
																<td style="width:15%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="haltart"  /></div>
																</td>
																<td style="width:5%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="ankunft" format="HH:mm" /></div>
																</td>
																<td style="width:5%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="abfahrt" format="HH:mm" /></div>
																</td>
																<td style="width:15%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="relativlage"  /></div>
																</td>
															</tr>
														</logic:iterate>
													</tbody>
												</table>	
											</td>	
										</tr>
										<tr class="even">
											<th><bean:message key="ueb.zug.verspaetung" /></th>
											<td><bean:write name="currentZug" property="abweichung.verspaetung"/></td>
										</tr>
									</table>
								</div>
								<script type="text/javascript">
									new Tip('tip_${currentRow2.label}_${currentZug.zugnr}_${currentZug.verkehrstag}_evu',
										$('tooltip_${currentRow2.label}_${currentZug.zugnr}_${currentZug.verkehrstag}_evu'),
										{
											title: '<bean:message key="zvf.zuginfo" />'
										});
								</script>
							</logic:iterate>
						</display:column>
						
						<display:column titleKey="auswertungen.anzahlVerspaetet" media="html" >
							<logic:iterate id="currentZug" name="currentRow2" property="zuegeVerspaetet">
								<span id="tip_${currentRow2.label}_${currentZug.zugnr}_${currentZug.verkehrstag}_evu">
									<div id="tip_${currentRow2.label}_${currentZug.zugnr}_${currentZug.verkehrstag}_evu">${currentZug.zugnr}</div>
								</span>
								<div id="tooltip_${currentRow2.label}_${currentZug.zugnr}_${currentZug.verkehrstag}_evu" style="width:400px;visibility:hidden;">
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
											<th><bean:message key="ueb.zug.knotenzeiten" /></th>
											<td>
												<table>
													<tbody>
														<logic:iterate id="currentKnoten" name="currentZug" property="knotenzeiten" indexId="iKn">
															<tr>
																<td style="width:10%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="bahnhof"  /></div>
																</td>
																<td style="width:15%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="haltart"  /></div>
																</td>
																<td style="width:5%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="ankunft" format="HH:mm" /></div>
																</td>
																<td style="width:5%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="abfahrt" format="HH:mm" /></div>
																</td>
																<td style="width:15%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="relativlage"  /></div>
																</td>
															</tr>
														</logic:iterate>
													</tbody>
												</table>	
											</td>	
										</tr>
										<tr class="even">
											<th><bean:message key="ueb.zug.verspaetung" /></th>
											<td><bean:write name="currentZug" property="abweichung.verspaetung"/></td>
										</tr>
									</table>
								</div>
								<script type="text/javascript">
									new Tip('tip_${currentRow2.label}_${currentZug.zugnr}_${currentZug.verkehrstag}_evu',
										$('tooltip_${currentRow2.label}_${currentZug.zugnr}_${currentZug.verkehrstag}_evu'),
										{
											title: '<bean:message key="zvf.zuginfo" />'
										});
								</script>
							</logic:iterate>
						</display:column>
						
						<display:column titleKey="auswertungen.anzahlVorPlan"  media="html">
							<logic:iterate id="currentZug" name="currentRow2" property="zuegeVorPlan">
								<span id="tip_${currentRow2.label}_${currentZug.zugnr}_${currentZug.verkehrstag}_evu">
									<div id="tip_${currentRow2.label}_${currentZug.zugnr}_${currentZug.verkehrstag}_evu">${currentZug.zugnr}</div>
								</span>
								<div id="tooltip_${currentRow2.label}_${currentZug.zugnr}_${currentZug.verkehrstag}_evu" style="width:400px;visibility:hidden;">
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
											<th><bean:message key="ueb.zug.knotenzeiten" /></th>
											<td>
												<table>
													<tbody>
														<logic:iterate id="currentKnoten" name="currentZug" property="knotenzeiten" indexId="iKn">
															<tr>
																<td style="width:10%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="bahnhof"  /></div>
																</td>
																<td style="width:15%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="haltart"  /></div>
																</td>
																<td style="width:5%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="ankunft" format="HH:mm" /></div>
																</td>
																<td style="width:5%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="abfahrt" format="HH:mm" /></div>
																</td>
																<td style="width:15%">
																	<div style="text-align:center"><bean:write name="currentKnoten" property="relativlage"  /></div>
																</td>
															</tr>
														</logic:iterate>
													</tbody>
												</table>	
											</td>	
										</tr>
										<tr class="even">
											<th><bean:message key="ueb.zug.verspaetung" /></th>
											<td><bean:write name="currentZug" property="abweichung.verspaetung"/></td>
										</tr>
									</table>
								</div>
								<script type="text/javascript">
									new Tip('tip_${currentRow2.label}_${currentZug.zugnr}_${currentZug.verkehrstag}_evu',
										$('tooltip_${currentRow2.label}_${currentZug.zugnr}_${currentZug.verkehrstag}_evu'),
										{
											title: '<bean:message key="zvf.zuginfo" />'
										});
								</script>
							</logic:iterate>
						</display:column>
						
						
						<display:column titleKey="auswertungen.anzahlUmgeleitet" media="excel">
							<logic:iterate id="currentZug" name="currentRow2" property="zuegeUmgeleitet">${currentZug.zugnr},</logic:iterate>
						</display:column>
						<display:column titleKey="auswertungen.anzahlVerspaetet" media="excel">
							<logic:iterate id="currentZug" name="currentRow2" property="zuegeVerspaetet">${currentZug.zugnr},</logic:iterate>
						</display:column>
						<display:column titleKey="auswertungen.anzahlVorPlan" media="excel">
							<logic:iterate id="currentZug" name="currentRow2" property="zuegeVorPlan">${currentZug.zugnr},</logic:iterate>
						</display:column>
					</display:table>
				</div>
				
				
<jsp:include page="../../main_footer.jsp" />