<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

				<div id="divStreckeZvf">
					<div id="divStreckeTabelle">
						<br>
						<fieldset>
							<%-- Tabelle Streckenabschnitte--%>
					    	<legend><bean:message key="ueb.betroffenerbereich" /></legend>
							<table class="colored" style="text-align:center;" >
								<thead>
									<tr>
										<th>
											<div style="text-align:center;"><bean:message key="ueb.strecke.vzg.kurz" /></div>
										</th>
										<th>
											<div style="text-align:center;"><bean:message key="ueb.strecke.massnahme" /></div>
										</th>
										<th>
											<div style="text-align:center;"><bean:message key="ueb.strecke.zeitraum" /></div>
										</th>
										<th>
											<div style="text-align:center;"><bean:message key="ueb.strecke.unterbrochen.kurz" /></div>
										</th>
										<th>
											<div style="text-align:center;"><bean:message key="ueb.strecke.grund" /></div>
										</th>
										<th>
											<div style="text-align:center;"><bean:message key="ueb.strecke.betriebsweise" /></div>
										</th>
										<th></th>
									</tr>
								</thead>
								<tbody>
									<logic:iterate id="currentMassnahme" name="baumassnahme" property="aktuelleZvf.massnahmen" indexId="index">
										<logic:iterate id="currentStrecke" name="currentMassnahme" property="strecke" indexId="ind">
											<bean:define toScope="page" id="styleClass" value="${styleClass=='evenrow'?'oddrow':'evenrow'}"/>
											<tr class="${styleClass}">
												<td style="width: 50px;">
													<div style="text-align:center;">
														<% String property1Name = "zvfStreckeVZG(" + ind + ")"; %>
														<html:text name="baumassnahmeForm" property="<%=property1Name %>" styleClass="year" maxlength="5" titleKey="ueb.tip.vzg"/>
													</div>
												</td>
												<td>
													<div style="text-align:center;">
														<% String property2Name = "zvfStreckeMassnahme(" + ind + ")"; %>
														<html:text name="baumassnahmeForm" property="<%=property2Name %>" style="width:200px;"/>
													</div>
												</td>
												<td>
													<span style="text-align:right;">
														<bean:message key="ueb.strecke.zeitvon" />
														<% String property3Name = "zvfStreckeBaubeginn(" + ind + ")"; %>
														<html:text name="baumassnahmeForm" property="<%=property3Name %>" styleClass="datetime" maxlength="16" style="margin-left:5px;"/>
													</span>
													<span style="text-align:right;">
														<bean:message key="ueb.strecke.zeitbis" />
														<% String property4Name = "zvfStreckeBauende(" + ind + ")"; %>
														<html:text name="baumassnahmeForm" property="<%=property4Name %>" styleClass="datetime" maxlength="16" style="margin-left:5px;"/>
													</span>
												</td>
												<td style="width: 50px;">
													<div style="text-align:center;">													
														<% String property5Name = "zvfStreckeUnterbrochen(" + ind + ")"; %>
														<html:checkbox name="baumassnahmeForm" property="<%=property5Name %>" styleClass="checkbox" />
													</div>
												</td>
												<td>
													<div style="text-align:center;">													
														<% String property6Name = "zvfStreckeGrund(" + ind + ")"; %>
														<html:text name="baumassnahmeForm" property="<%=property6Name %>" style="width:140px;"/>
													</div>
												</td>
												<td>
													<div style="text-align:center;">													
														<% String property7Name = "zvfStreckeBetriebsweise(" + ind + ")"; %>
														<html:text name="baumassnahmeForm" property="<%=property7Name %>" style="width:140px;"/>
													</div>
												</td>
												<td>
													<div style="text-align:center;">													
														<bean:define id="confirmText" toScope="page"><bean:message key="confirm.strecke.delete" /></bean:define>
														<jsp:useBean id="addStreckeParamMap" class="java.util.HashMap">
															<c:set target="${addStreckeParamMap}" property="id" value="${baumassnahme.id}" />
															<c:set target="${addStreckeParamMap}" property="type" value="ZVF" />
														</jsp:useBean>
														<c:set target="${addStreckeParamMap}" property="streckeId" value="${currentStrecke.id}" />
														<html:link action="/refreshStrecke" name="addStreckeParamMap" styleClass="delete" titleKey="button.delete">&nbsp;</html:link>
													</div>
												</td>
											</tr>
										</logic:iterate>
									</logic:iterate>
								</tbody>
							</table>
						</fieldset>
					</div>
				</div>
				