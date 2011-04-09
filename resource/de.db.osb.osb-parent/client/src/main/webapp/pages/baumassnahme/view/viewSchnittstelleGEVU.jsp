<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
<%@ taglib uri="/META-INF/bob.tld" prefix="bob"  %>

				<table class="colored" style="text-align:center;">
					<%-- SpaltenÃ¼berschriften --%>
					<thead>
						<tr>
							<th>&nbsp;</th>
								<th>
									<div style="text-align:center;"><bean:message key="baumassnahme.termine.studie" /></div>
								</th>
								<th>
									<div style="text-align:center;"><bean:message key="baumassnahme.termine.zvfentwurf.nobr" /></div>
								</th>
								<th>
									<div style="text-align:center;"><bean:message key="baumassnahme.termine.stellungnahmeevu" /></div>
								</th>
								<th>
									<div style="text-align:center;"><bean:message key="baumassnahme.termine.zvf" /></div>
								</th>
								<th>
									<div style="text-align:center;"><bean:message key="baumassnahme.termine.masteruebergabeblattgv" /></div>
								</th>
								<th>
									<div style="text-align:center;"><bean:message key="baumassnahme.termine.uebergabeblattgv" /></div>
								</th>
								<th>
									<div style="text-align:center;"><bean:message key="baumassnahme.termine.fplo" /></div>
								</th>
								<th>
									<div style="text-align:center;"><bean:message key="baumassnahme.termine.eingabegfd_z" /></div>
								</th>
								<%-- 2 Leerspalten, damit gleiche Spalten bei PEVU und GEVU untereinander sind--%>
								<th style="width: 60px">
								</th>
								<th style="width: 60px">
								</th>
						</tr>
					</thead>
					<tbody>
						<%-- Soll-Termine --%>
						<tr class="activerow">
							<td><div  style="text-align:left">Solltermin&#58;</div></td>
							<logic:iterate id="currentTermin" name="baumassnahme" property="sollTermineGEVU" indexId="index">
								<td><div style="text-align:center">${currentTermin}</div></td>
							</logic:iterate>
							<td>
							</td>
							<td>
							</td>
						</tr>
						<%-- Ist-Termine --%>
						<logic:iterate id="currentEVU" name="baumassnahme" property="gevus" indexId="index">
							<bean:define toScope="page" id="styleClass" value="${styleClass=='evenrow'?'oddrow':'evenrow'}"/>
							<tr class="${styleClass}">
								<td>
									<div style="text-align:left;width:100px;">
										<bean:write name="currentEVU" property="evuGruppe.name" />
									</div>
								</td>
								
						    	<%-- Studie/Grobkonzept --%>
								<c:choose>
									<c:when test="${baumassnahme.art == 'KS'}">
										<td>
											<div style="text-align:center"><bean:write name="currentEVU" property="studieGrobkonzept" format="dd.MM.yyyy" /></div>
										</td>
									</c:when>
									<c:otherwise>
										<td>
											<div style="text-align:center">
												<bob:statusIcon status="NEUTRAL" />
											</div>
										</td>
									</c:otherwise>
								</c:choose>
								
								<%-- ZvF-Entwurf --%>
								<td>
									<div style="text-align:center">
										<bean:write name="currentEVU" property="zvfEntwurf" format="dd.MM.yyyy" />
										<div style="padding-left:10px">
											<bob:statusIcon status="${currentEVU.zvfEntwurfStatus}" />
											<c:if test="${currentEVU.zvfEntwurfStatus == 'COUNTDOWN14'}">
												${currentEVU.verbleibendeTage_zvfEntwurf}
											</c:if>
										</div>
									</div>
								</td>
								
								<%-- Stellungsnahme EVU --%>
								<td>
									<div style="text-align:center">
										<bean:write name="currentEVU" property="stellungnahmeEVU" format="dd.MM.yyyy" />
										<div style="padding-left:10px">
											<bob:statusIcon status="${currentEVU.stellungnahmeEVUStatus}" />
											<c:if test="${currentEVU.stellungnahmeEVUStatus == 'COUNTDOWN14'}">
												${currentEVU.verbleibendeTage_stellungnahmeEVU}
											</c:if>
										</div>
									</div>
								</td>
								
								<%-- ZvF --%>
								<td>
									<div style="text-align:center">
										<bean:write name="currentEVU" property="zvF" format="dd.MM.yyyy" />
										<div style="padding-left:10px">
											<bob:statusIcon status="${currentEVU.zvFStatus}" />
											<c:if test="${currentEVU.zvFStatus == 'COUNTDOWN14'}">
												${currentEVU.verbleibendeTage_zvF}
											</c:if>
										</div>
									</div>
								</td>
								
								<%-- Masterübergabeblatt GV --%>
								<c:choose>
									<c:when test="${baumassnahme.art == 'A' || baumassnahme.art == 'QS' || baumassnahme.art == 'KS'}">
										<td>
											<div style="text-align:center">
												<bean:write name="currentEVU" property="masterUebergabeblattGV" format="dd.MM.yyyy" />
												<div style="padding-left:10px">
													<bob:statusIcon status="${currentEVU.masterUebergabeblattGVStatus}" />
													<c:if test="${currentEVU.masterUebergabeblattGVErforderlich == true && currentEVU.masterUebergabeblattGVStatus == 'COUNTDOWN14'}">
														${currentEVU.verbleibendeTage_masterUebergabeblattGV}
													</c:if>
												</div>
											</div>
										</td>
									</c:when>
									<c:otherwise>
										<td>
											<div style="text-align:center">
												<bob:statusIcon status="NEUTRAL" />
											</div>
										</td>
									</c:otherwise>
								</c:choose>
								
								<%-- Übergabeblatt GV --%>
								<td>
									<div style="text-align:center;">
										<bean:write name="currentEVU" property="uebergabeblattGV" format="dd.MM.yyyy" />
										<div style="padding-left:10px">
											<bob:statusIcon status="${currentEVU.uebergabeblattGVStatus}" />
											<c:if test="${currentEVU.uebergabeblattGVErforderlich == true && currentEVU.uebergabeblattGVStatus == 'COUNTDOWN14'}">
												${currentEVU.verbleibendeTage_uebergabeblattGV}
											</c:if>
										</div>
									</div>
								</td>
								
								<%-- Fplo --%>
								<td>
									<div style="text-align:center;">
										<bean:write name="currentEVU" property="fplo" format="dd.MM.yyyy" />
										<div style="padding-left:10px">
											<bob:statusIcon status="${currentEVU.fploStatus}" />
											<c:if test="${currentEVU.fploStatus == 'COUNTDOWN14'}">
												${currentEVU.verbleibendeTage_fplo}
											</c:if>
										</div>
									</div>
								</td>
								
								<%-- Eingabe GFD_Z --%>
								<td>
									<div style="text-align:center;">
										<bean:write name="currentEVU" property="eingabeGFD_Z" format="dd.MM.yyyy" />
										<div style="padding-left:10px">
											<bob:statusIcon status="${currentEVU.eingabeGFD_ZStatus}" />
											<c:if test="${currentEVU.eingabeGFD_ZStatus == 'COUNTDOWN14'}">
												${currentEVU.verbleibendeTage_eingabeGFD_Z}
											</c:if>
										</div>
									</div>
								</td>
								<td>
								</td>
								<td>
								</td>
							</tr> 
						</logic:iterate> 
					</tbody>
				</table>
		    