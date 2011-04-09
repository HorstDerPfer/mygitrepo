<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

				<div id="divZug">
					<div id="divZugTabelle">
						<bean:define id="urlZug" toScope="page"><c:url value="refreshZug.do" /></bean:define>
						<input type="text" name="bmId" value="${baumassnahme.id}" style="display: none">
						<input type="checkbox" onClick="check(this.form.checkZug)" class="checkbox"><bean:message key="zvf.import.alleauswaehlen"/>
						<fieldset>
							<logic:iterate id="currentMassnahme" name="baumassnahme" property="uebergabeblatt.massnahmen" indexId="index">
					    		<legend><bean:message key="ueb.zuege" /></legend>
								<table class="colored" style="text-align:center;" >
									<thead>										
										<tr>
											<th><div style="text-align:center;"><bean:message key="ueb.zug.lfdnr" /></div></th>
											<th></th>
											<th><div style="text-align:center;"><bean:message key="ueb.zug.zugdaten" /></div></th>
										</tr>
									</thead>
									<tbody>
										<logic:iterate id="currentZug" name="currentMassnahme" property="zug" indexId="i">
										<bean:define toScope="page" id="styleClassZug" value="${styleClassZug=='evenrow'?'oddrow':'evenrow'}"/>
											<%--Zugzeile --%>
											<tr class="${styleClassZug}">
												<td><div style="text-align:center">${ i+1}</div></td>
												<td><input type="checkbox" name="checkZug" value="${currentZug.id}" class="checkbox"></td>
												<td>
													<%--Zugdaten --%>
													<table width="100%">
														<%-- Daten --%>
														<tr><td>
															<table class="colored" style="text-align:center" >
																<thead>
																	<tr>
																		<th>
																			<div style="text-align:left;"><bean:message key="ueb.zug.datum" /></div>
																		</th>
																		<th>
																			<div style="text-align:left;"><bean:message key="ueb.zug.zuggattung" /></div>
																		</th>
																		<th>
																			<div style="text-align:left;"><bean:message key="ueb.zug.zugnr" /></div>
																		</th>
																		<th>
																			<div style="text-align:left;"><bean:message key="ueb.zug.abgangsbhf" /></div>
																		</th>
																		<th>
																			<div style="text-align:left;"><bean:message key="ueb.zug.zielbhf" /></div>
																		</th>
																		<th>
																			<div style="text-align:center;"><bean:message key="ueb.zug.richtung" /></div>
																		</th>
																	</tr>
																</thead>
																<tbody>
																	<tr>
																		<td style="border-bottom:1px solid #666666;">
																			<% String property1Name = "zugVerkehrstag(" + i + ")"; %>
																			<html:text name="baumassnahmeForm" property="<%=property1Name %>" styleClass="date" maxlength="10"/>
																		</td>
																		<td style="border-bottom:1px solid #666666;">
																			<% String property2Name = "zugZuggattung(" + i + ")"; %>
																			<html:text name="baumassnahmeForm" property="<%=property2Name %>" styleClass="date" maxlength="10"/>
																		</td>
																		<td style="border-bottom:1px solid #666666;">
																			<% String property3Name = "zugZugnr(" + i + ")"; %>
																			<html:text name="baumassnahmeForm" property="<%=property3Name %>" styleClass="date" maxlength="5"/>
																		</td>
																		<td style="border-bottom:1px solid #666666;">
																			<% String property4Name = "zugAbgangsbhf(" + i + ")"; %>
																			<html:text name="baumassnahmeForm" property="<%=property4Name %>" styleClass="auto"/>
																		</td>
																		<td style="border-bottom:1px solid #666666;">
																			<% String property5Name = "zugZielbhf(" + i + ")"; %>
																			<html:text name="baumassnahmeForm" property="<%=property5Name %>" styleClass="auto"/>
																		</td>
																		<td>
																			<div style="text-align:center;">													
																				<% String property5aName = "uebZugRichtung(" + i + ")"; %>
																				<div class="input">
																					<html:radio property="<%=property5aName %>" value="false" styleClass="checkbox" />
																					<bean:message key="ueb.zug.richtung.false" />
																					<html:radio property="<%=property5aName %>" value="true" styleClass="checkbox" />
																					<bean:message key="ueb.zug.richtung.true" />
																				</div>
																			</div>
																		</td>
																	</tr>
																</tbody>
															</table></td>
														</tr>
														<%-- Details --%>
														<tr><td>
															<table class="colored" style="text-align:center" >
																<thead>
																	<tr>
																		<th style="color:black;font-weight: normal;">
																			<div style="text-align:left;"><bean:message key="ueb.zug.tfz" /></div>
																		</th>
																		<th style="color:black;font-weight: normal;">
																			<div style="text-align:left;"><bean:message key="ueb.zug.last" /></div>
																		</th>
																		<th style="color:black;font-weight: normal;">
																			<div style="text-align:left;"><bean:message key="ueb.zug.mbr.kurz" /></div>
																		</th>
																		<th style="color:black;font-weight: normal;">
																			<div style="text-align:left;"><bean:message key="ueb.zug.zuglaenge" /></div>
																		</th>
																		<th style="color:black;font-weight: normal;">
																			<div style="text-align:left;"><bean:message key="ueb.zug.vmax" /></div>
																		</th>
																		<th style="color:black;font-weight: normal;">
																			<div style="text-align:left;"><bean:message key="ueb.zug.besonderheiten.kvprofil" /></div>
																		</th>
																		<th style="color:black;font-weight: normal;">
																			<div style="text-align:left;"><bean:message key="ueb.zug.besonderheiten.streckenklasse" /></div>
																		</th>
																	</tr>
																</thead>
																<tbody>
																	<tr class="${styleClass}">
																		<td>
																			<% String property6Name = "zugTfz(" + i + ")"; %>
																			<html:text name="baumassnahmeForm" property="<%=property6Name %>" styleClass="date"  maxlength="5"/>
																		</td>
																		<td>
																			<% String property7Name = "zugLast(" + i + ")"; %>
																			<html:text name="baumassnahmeForm" property="<%=property7Name %>" styleClass="date" />
																		</td>
																		<td>
																			<% String property8Name = "zugMbr(" + i + ")"; %>
																			<html:text name="baumassnahmeForm" property="<%=property8Name %>" styleClass="date"  maxlength="10"/>
																		</td>
																		<td>
																			<% String property9Name = "zugLaenge(" + i + ")"; %>
																			<html:text name="baumassnahmeForm" property="<%=property9Name %>" styleClass="date" />
																		</td>
																		<td>
																			<% String property10Name = "zugVmax(" + i + ")"; %>
																			<html:text name="baumassnahmeForm" property="<%=property10Name %>" styleClass="date" />
																		</td>
																		<td>
																			<% String property11Name = "zugKvProfil(" + i + ")"; %>
																			<html:text name="baumassnahmeForm" property="<%=property11Name %>" styleClass="date" />
																		</td>
																		<td>
																			<% String property12Name = "zugStreckenklasse(" + i + ")"; %>
																			<html:text name="baumassnahmeForm" property="<%=property12Name %>" styleClass="date" />
																		</td>
																	</tr>
																</tbody>
															</table></td>
														</tr>
														<%-- Details/Knotenzeiten --%>
														<tr>	
															<td>
															<table class="colored" style="text-align:center" >
																<thead>
																	<th style="color:black;font-weight: normal;">
																		<div style="text-align:left;"><bean:message key="ueb.zug.bemerkung" /></div>
																	</th>
																	<th colspan="3">
																		<div style="text-align:center;"><bean:message key="ueb.zug.qsks" /></div>
																	</th>
																	<th>
																	</th>
																	<th>
																	</th>
																	<th>
																	</th>
																</thead>
																<tbody>
																	<td>
																		<% String property14Name = "zugBemerkung(" + i + ")"; %>
																		<html:text name="baumassnahmeForm" property="<%=property14Name %>" styleClass="middle" />
																	</td>
																	<% String property15Name = "zugQsKs(" + i + ")"; %>
																	<td>
																		<html:radio property="<%=property15Name %>" value="0" styleClass="checkbox"><bean:message key="ueb.zug.qsks.short.0" /></html:radio>
																	</td>
																	<td>
																		<html:radio property="<%=property15Name %>" value="1" styleClass="checkbox"><bean:message key="ueb.zug.qsks.1" /></html:radio>
																	</td>
																	<td>
																		<html:radio property="<%=property15Name %>" value="2" styleClass="checkbox"><bean:message key="ueb.zug.qsks.2" /></html:radio>
																	</td>
																	<td>
																		<bean:define id="bmId" toScope="request" name="baumassnahme" property="id"></bean:define>
																		<div>
																			<html:link href="#" onclick="knotenzeitenBearbeiten();" styleClass="buttonSave" styleId="buttonSave">
																				<bean:message key="ueb.zug.knotenzeiten.edit" />
																			</html:link>
																			
																		</div>
																	</td>
																	<td>	
																		<div style="text-align:right">
																			<bean:message key="ueb.zug.teilbearbeitet" />
																			<% String property13Name = "zugBearbeitet(" + i + ")"; %>
																		</div>
																	</td>
																	<td>
																		<div style="text-align:left">
																			<html:checkbox name="baumassnahmeForm" property="<%=property13Name %>" styleClass="checkbox" />
																		</div>
																	</td>
																</tbody>
															</table>
															<td>
																<%--<bean:define id="confirmText" toScope="page"><bean:message key="confirm.zug.delete" /></bean:define>
																<html:link href="javascript:if(confirmLink(this.href, '${confirmText}')) removeZug('${urlZug}','${currentZug.id}');" styleClass="delete" titleKey="button.delete">&nbsp;</html:link>--%>
																<jsp:useBean id="zugParamMap" class="java.util.HashMap" scope="page">
																	<c:set target="${zugParamMap}" property="id" value="${baumassnahme.id}" />
																	<c:set target="${zugParamMap}" property="type" value="UEB" />
																</jsp:useBean>
																<c:set target="${zugParamMap}" property="zugId" value="${currentZug.id}" />
																<html:link action="/deleteZug" name="zugParamMap" paramId="" styleClass="delete" titleKey="button.delete">&nbsp;
																	<html:param name="showZuegeUeb" value="true" />
																</html:link>
															</td>
															<%--</td>--%>
														</tr>
													</table>
												</td>
											</tr>
										</logic:iterate>
									</tbody>
								</table>
							</logic:iterate>
						</fieldset>
					</div>
				</div>
				<script type="text/javascript" language="JavaScript">
					function knotenzeitenBearbeiten(){
						var actionString = document.forms[0].action;
						var newActionString = "";
						var y=actionString.split("/");
						newActionString = newActionString + "/" + y[1] + "/knotenzeitenBearbeiten.do";
						document.forms[0].action=newActionString;
						document.forms[0].submit();
					}
				</script>
				
				<script type="text/javascript" language="JavaScript">
					var checkflag = "false";
					
					function check(field) {
					if (checkflag == "false") {
					  for (i = 0; i < field.length; i++) {
					  field[i].checked = true;}
					  checkflag = "true"; }
					else {
					  for (i = 0; i < field.length; i++) {
					  field[i].checked = false; }
					  checkflag = "false"; }
					}
					
				</script>
				
				

						