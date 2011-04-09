<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
<%@ taglib uri="/META-INF/bob.tld" prefix="bob"  %>

				<table class="colored">
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
									<div style="text-align:center;"><bean:message key="baumassnahme.termine.masteruebergabeblattpv" /></div>
								</th>
								<th>
									<div style="text-align:center;"><bean:message key="baumassnahme.termine.uebergabeblattpv" /></div>
								</th>
								<th>
									<div style="text-align:center;"><bean:message key="baumassnahme.termine.fplo" /></div>
								</th>
								<th>
									<div style="text-align:center;"><bean:message key="baumassnahme.termine.eingabegfd_z" /></div>
								</th>
								<th style="width: 60px">
									<div style="text-align:center;"><bean:message key="baumassnahme.termine.ausfaellesev" /></div>
								</th>
								<th style="width: 60px">
									<div style="text-align:center;"><bean:message key="baumassnahme.termine.bkonzeptevu" /></div>
								</th>
								<%--<th>
									<div style="text-align:center;"><bean:message key="common.more" /></div>
								</th>--%>
						</tr>
					</thead>
					<tbody>
						<%-- Soll-Termine --%>
						<tr class="activerow">
							<td><div  style="text-align:left">Solltermin&#58;</div></td>
							<logic:iterate id="currentTermin" name="baumassnahme" property="sollTerminePEVU" indexId="index">
								<c:if test="${index < 10}">
									<td><div style="text-align:center">${currentTermin}</div></td>
								</c:if>
								<%--<c:if test="${index == 8 }">
									<td>&nbsp;</td>
								</c:if>--%>
							</logic:iterate>
						</tr>
						<%-- Ist-Termine --%>
						<logic:iterate id="currentEVU" name="baumassnahme" property="pevus" indexId="index">
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
								
								<%-- Masterübergabeblatt PV --%>
								<c:choose>
									<c:when test="${baumassnahme.art == 'A' || baumassnahme.art == 'QS' || baumassnahme.art == 'KS'}">
										<td>
											<div style="text-align:center">
												<bean:write name="currentEVU" property="masterUebergabeblattPV" format="dd.MM.yyyy" />
												<div style="padding-left:10px">
													<bob:statusIcon status="${currentEVU.masterUebergabeblattPVStatus}" />
													<c:if test="${currentEVU.masterUebergabeblattPVErforderlich == true && currentEVU.masterUebergabeblattPVStatus == 'COUNTDOWN14'}">
														${currentEVU.verbleibendeTage_masterUebergabeblattPV}
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
								
								<%-- Übergabeblatt PV --%>
								<td>
									<div style="text-align:center;">
										<bean:write name="currentEVU" property="uebergabeblattPV" format="dd.MM.yyyy" />
										<div style="padding-left:10px">
											<bob:statusIcon status="${currentEVU.uebergabeblattPVStatus}" />
											<c:if test="${currentEVU.uebergabeblattPVErforderlich == true && currentEVU.uebergabeblattPVStatus == 'COUNTDOWN14'}">
												${currentEVU.verbleibendeTage_uebergabeblattPV}
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
								
								<%-- Ausfälle/SEV und B-Konzept EVU im Popup 
								<td style="text-align:center;">
									<span id="tip_${currentEVU.id}">
										<img src="<c:url value='static/img/icon_s_info_small.gif' />" id="tip_${currentEVU.id}" />
									</span>
								</td>--%>
								<td>
									<div style="text-align:center;">
										<bean:message key="baumassnahme.termine.ausfaellesev.${currentEVU.ausfaelleSEV}" />
									</div>
								</td>
								<td>
									<div style="text-align:center;">
										<bean:write name="currentEVU" property="BKonzeptEVU" format="dd.MM.yyyy" />
										<div style="padding-left:10px">
											<bob:statusIcon status="${currentEVU.BKonzeptEVUStatus }" />
											<c:if test="${currentEVU.BKonzeptEVUStatus == 'COUNTDOWN14'}">
												${currentEVU.verbleibendeTage_bKonzeptEVU}
											</c:if>
										</div>
									</div>					
								</td>
							</tr>								
						</logic:iterate>
					</tbody>
				</table>