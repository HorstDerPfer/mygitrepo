<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
		
									<bean:define toScope="page" id="evenOdd" value="${evenOdd=='oddrow'?'evenrow':'oddrow'}"/>

									<logic:iterate id="currentMassnahme" name="baumassnahme" property="bbzr.massnahmen" indexId="index">
										<logic:iterate id="currentZug" name="currentMassnahme" property="zug" indexId="i">
											<c:if test="${currentZug.abweichung.art ==abweichungstyp }">
												<div id="tooltip_${currentZug.id}" style="width:730px;">
													<table class="colored">
														<thead>
															<tr>
																<c:if test="${abweichungstyp==artUmleitung }">
																	<th>
																		<div style="text-align:center;"><bean:message key="ueb.zug.umleitungsstrecke" /></div>
																	</th>
																</c:if>

																<c:if test="${abweichungstyp==artUmleitung || abweichungstyp==artVerspaetung }">
																	<th>
																		<div style="text-align:center;"><bean:message key="ueb.zug.verspaetung" /></div>
																	</th>
																</c:if>

																<c:if test="${abweichungstyp==artAusfall }">
																	<th>
																		<div style="text-align:center;"><bean:message key="ueb.zug.ausfallab" /></div>
																	</th>
																	<th>
																		<div style="text-align:center;"><bean:message key="ueb.zug.ausfallbis" /></div>
																	</th>
																</c:if>

																<c:if test="${abweichungstyp==artVorplan }">
																	<th>
																		<div style="text-align:center;"><bean:message key="ueb.zug.zeitvorplan" /></div>
																	</th>
																	<th>
																		<div style="text-align:center;"><bean:message key="ueb.zug.vorplanab" /></div>
																	</th>
																</c:if>

																<c:if test="${abweichungstyp==artErsatzhalte }">
																	<th>
																		<table>
																			<thead>
																				<tr>
																					<th>
																						<div style="text-align:center;"><bean:message key="ueb.zug.ausfallverkehrshalt" /></div>
																					</th>
																					<th>
																						<div style="text-align:center;"><bean:message key="ueb.zug.moeglicherersatzhalt" /></div>
																					</th>
																				</tr>
																			</thead>
																		</table>
																	</th>
																</c:if>
																
																<c:if test="${abweichungstyp==artUmleitung }">
																	<th>
																		<div style="text-align:center;"><bean:message key="ueb.zug.qsks" /></div>
																	</th>
																</c:if>
																<th>
																	<div style="text-align:center;"><bean:message key="ueb.zug.bemerkung" /></div>
																</th> 
															</tr>
														</thead>
														<tbody>
															<tr class="${evenOdd}">
																<c:if test="${abweichungstyp==artUmleitung }">
																	<td>
																		<div style="text-align:center;"><bean:write name="currentZug" property="abweichung.umleitung" /></div>
																	</td>
																</c:if>

																<c:if test="${abweichungstyp==artUmleitung || abweichungstyp==artVerspaetung }">
																	<td>
																		<div style="text-align:center;"><bean:write name="currentZug" property="abweichung.verspaetung" /></div>
																	</td>
																</c:if>

																<c:if test="${abweichungstyp==artAusfall }">
																	<td>
																		<logic:notEmpty name="currentZug" property="abweichung.ausfallvon">
																			<div style="text-align:center;"><bean:write name="currentZug" property="abweichung.ausfallvon.langName" /></div>
																		</logic:notEmpty>
																	</td>
																	<td>
																		<logic:notEmpty name="currentZug" property="abweichung.ausfallbis">
																			<div style="text-align:center;"><bean:write name="currentZug" property="abweichung.ausfallbis.langName" /></div>
																		</logic:notEmpty>
																	</td>
																</c:if>

																<c:if test="${abweichungstyp==artVorplan }">
																	<td>
																		<div style="text-align:center;"><bean:write name="currentZug" property="abweichung.verspaetung" /></div>
																	</td>
																	<td>
																		<logic:notEmpty name="currentZug" property="abweichung.vorplanab">
																			<div style="text-align:center;"><bean:write name="currentZug" property="abweichung.vorplanab.langName" /></div>
																		</logic:notEmpty>
																	</td>
																</c:if>

																<c:if test="${abweichungstyp==artErsatzhalte }">
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
																						<td>
																							<logic:notEmpty name="currentHalt" property="ersatz">
																								<div style="text-align:center;"><bean:write name="currentHalt" property="ersatz.langName" /></div>
																							</logic:notEmpty>
																						</td>
																					</tr>
																				</logic:iterate>
																			</tbody>
																		</table>
																	</td>
																</c:if>

																<c:if test="${abweichungstyp==artUmleitung }">
																	<td>
																		<div style="text-align:center">
																			<logic:notEmpty name="currentZug" property="qs_ks">
																				<bean:message key="ueb.zug.qsks.${currentZug.qs_ks}" />
																			</logic:notEmpty>
																		</div>
																	</td>
																</c:if>

																<td>
																	<div style="text-align:center"><bean:write name="currentZug" property="bemerkung"  /></div>
																</td>
															</tr>
														</tbody>
													</table>
												</div>
												<script type="text/javascript">
													new Tip('tip_${currentZug.id}',
														$('tooltip_${currentZug.id}'),
														{
															title: '<bean:message key="ueb.zug.zugnrhead" arg0="${currentZug.zugnr}" />',
															viewport: true 
														}); 
												</script>
											</c:if>
										</logic:iterate>
									</logic:iterate>
									