<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

<bean:define id="artUmleitung" toScope="page"><bean:message key="ueb.zug.abweichung.umleitung"/></bean:define>
<bean:define id="artVerspaetung" toScope="page"><bean:message key="ueb.zug.abweichung.verspaetung"/></bean:define>
<bean:define id="artAusfall" toScope="page"><bean:message key="ueb.zug.abweichung.ausfall"/></bean:define>
<bean:define id="artVorplan" toScope="page"><bean:message key="ueb.zug.abweichung.vorplan"/></bean:define>
<bean:define id="artGesperrt" toScope="page"><bean:message key="ueb.zug.abweichung.gesperrt"/></bean:define>
<bean:define id="artErsatzhalte" toScope="page"><bean:message key="ueb.zug.abweichung.ersatzhalte"/></bean:define>
<bean:define id="artRegelung" toScope="page"><bean:message key="ueb.zug.abweichung.regelung"/></bean:define>
<bean:define id="confirmText" toScope="page"><bean:message key="confirm.zug.delete" /></bean:define>
<bean:define id="urlZugBBZR" toScope="page"><c:url value="refreshZugZvf.do" /></bean:define>

<div id="divZugTabelleZvf">
	<logic:iterate id="currentMassnahme" name="baumassnahme" property="aktuelleZvf.massnahmen" indexId="index">
		<table class="colored" style="text-align:center;width:100%" >
			<thead>										
				<tr>
					<th><div style="text-align:center;"><bean:message key="ueb.zug.lfdnr" /></div></th>
					<th><div style="text-align:center;"><bean:message key="ueb.zug.zugdaten" /></div></th>
				</tr>
			</thead>
			<tbody>
				<logic:iterate id="currentZug" name="currentMassnahme" property="zug" indexId="i">
					<c:if test="${currentZug.abweichung.art ==abweichungstyp }">
						<%--Zugzeile --%>
						<bean:define toScope="request" id="zvfstyleClassZug" value="${zvfStyleClassZug=='evenrow'?'oddrow':'evenrow'}"/>
						<tr class="${zvfstyleClassZug}">
							<bean:define toScope="request" id="zvfnr" value="${zvfnr+1}"/>

							<td style="width:30px;" rowspan="2"><div style="text-align:center;">${zvfnr}</div></td>
							<%--<td style="width:30px;" rowspan="2"><div style="text-align:center;">${ i+1}</div></td>--%>
							<td>
								<%--Zugdatenzeile 1--%>
								<table class="colored" style="text-align:center;width:100%" >
									<thead>
										<tr>
											<th>
												<div style="text-align:left;"><bean:message key="ueb.zug.datum" /></div>
											</th>
											<c:if test="${abweichungstyp==artUmleitung }">
												<th>
													<div style="text-align:left;"><bean:message key="ueb.zug.tageswechsel" /></div>
												</th>
											</c:if>
											<th>
												<div style="text-align:left;"><bean:message key="ueb.zug.zuggattung" /></div>
											</th>
											<th>
												<div style="text-align:left;"><bean:message key="ueb.zug.zugnr" /></div>
											</th>
											<c:if test="${abweichungstyp==artUmleitung }">
												<th>
													<div style="text-align:left;"><bean:message key="ueb.zug.bedarf" /></div>
												</th>
											</c:if>
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
												<% String property1Name = "zvfZugVerkehrstag(" + i + ")"; %>
												<html:text name="baumassnahmeForm" property="<%=property1Name %>" styleClass="date" maxlength="10"/>
											</td>
											<c:if test="${abweichungstyp==artUmleitung }">
												<td style="border-bottom:1px solid #666666;">
													<% String property1aName = "zvfZugTageswechsel(" + i + ")"; %>
													<html:text name="baumassnahmeForm" property="<%=property1aName %>" styleClass="time" maxlength="2"/>
												</td>
											</c:if>
											<td style="border-bottom:1px solid #666666;">
												<% String property2Name = "zvfZugZuggattung(" + i + ")"; %>
												<html:text name="baumassnahmeForm" property="<%=property2Name %>" styleClass="date" maxlength="10"/>
											</td>
											<td style="border-bottom:1px solid #666666;">
												<% String property3Name = "zvfZugZugnr(" + i + ")"; %>
												<html:text name="baumassnahmeForm" property="<%=property3Name %>" styleClass="time" maxlength="5"/>
											</td>
											<c:if test="${abweichungstyp==artUmleitung }">
												<td style="border-bottom:1px solid #666666;width: 100%">
													<% String property3aName = "zvfZugBedarf(" + i + ")"; %>
													<div class="input" >
														<html:checkbox name="baumassnahmeForm" property="<%=property3aName %>" styleClass="checkbox" />
													</div>
												</td>
											</c:if>
											<td style="border-bottom:1px solid #666666;">
												<% String property4Name = "zvfZugAbgangsbhf(" + i + ")"; %>
												<html:text name="baumassnahmeForm" property="<%=property4Name %>" styleClass="auto"/>
											</td>
											<td style="border-bottom:1px solid #666666;">
												<% String property5Name = "zvfZugZielbhf(" + i + ")"; %>
												<html:text name="baumassnahmeForm" property="<%=property5Name %>" styleClass="auto"/>
											</td>
											<td>
												<div style="text-align:center;">													
													<% String property5aName = "zvfZugRichtung(" + i + ")"; %>
													<div class="input">
														<html:radio property="<%=property5aName %>" value="false" styleClass="checkbox" >
															<bean:message key="ueb.zug.richtung.false" />
														</html:radio>
														<html:radio property="<%=property5aName %>" value="true" styleClass="checkbox" >
															<bean:message key="ueb.zug.richtung.true" />
														</html:radio>
													</div>
												</div>
											</td>
										</tr>
									</tbody>
								</table>
							</td>
						</tr>
						<tr class="${zvfstyleClassZug}">
							<td>
								<%--Zugdatenzeile 2--%>
								<table  class="colored" style="text-align:center;width:100%">
									<thead>
										<tr>
											<c:if test="${abweichungstyp==artErsatzhalte }">
												<th><div style="text-align:left;"><bean:message key="ueb.zug.ausfallverkehrshaltLang" /></div></th>
												<th><div style="text-align:left;"><bean:message key="ueb.zug.moeglicherersatzhaltLang" /></div></th>
											</c:if>
											<c:if test="${abweichungstyp==artRegelung }">
												<th><div style="text-align:left;"><bean:message key="ueb.zug.regelungsart" /></div></th>
												<th><div style="text-align:left;"><bean:message key="ueb.zug.inbst" /></div></th>
												<th><div style="text-align:left;"><bean:message key="ueb.zug.text" /></div></th>
											</c:if>
											<c:if test="${abweichungstyp==artUmleitung }">
												<th><div style="text-align:left;"><bean:message key="ueb.zug.umleitungsstrecke" /></div></th>
											</c:if>
											<c:if test="${abweichungstyp==artUmleitung || abweichungstyp==artVerspaetung }">
												<th><div style="text-align:left;"><bean:message key="ueb.zug.verspaetung" /></div></th>
											</c:if>
											<c:if test="${abweichungstyp==artUmleitung }">
												<th><div style="text-align:center;"><bean:message key="ueb.zug.qsks" /></div></th>
											</c:if>
											<c:if test="${abweichungstyp==artAusfall }">
												<th><div style="text-align:left;"><bean:message key="ueb.zug.ausfallab" /></div></th>
												<th><div style="text-align:left;"><bean:message key="ueb.zug.ausfallbis" /></div></th>
											</c:if>
											<c:if test="${abweichungstyp==artVorplan }">
												<th><div style="text-align:left;"><bean:message key="ueb.zug.zeitvorplan" /></div></th>
												<th><div style="text-align:left;"><bean:message key="ueb.zug.vorplanab" /></div></th>
											</c:if>
											<th><div style="text-align:left;"><bean:message key="ueb.zug.bemerkung" /></div></th>
											<th></th>
										</tr>
									</thead>
									<%-- Daten --%>
									<tbody>
										<c:if test="${abweichungstyp==artErsatzhalte }">
											<td colspan="2">
												<table style="text-align:center;width:100%">
													<tbody>
														<logic:iterate id="currentHalt" name="currentZug" property="abweichung.halt" indexId="haltId">
															<tr class="${zvfstyleClassZug}">
																<td>
																	<% String property6Name = "zvfZugAusfallVerkehrshaltHalt(" + i + ", "+ haltId +" )"; %>
																	<html:text name="baumassnahmeForm" property="<%=property6Name %>" styleClass="auto"/>
																</td>
																<td>
																	<% String property7Name = "zvfZugMoeglicherErsatzhaltHalt(" + i + ", "+ haltId +" )"; %>
																		<html:text name="baumassnahmeForm" property="<%=property7Name %>" styleClass="auto"/>
																</td>
															</tr>
														</logic:iterate>																			
													</tbody>
												</table>
											</td>
										</c:if>
										<c:if test="${abweichungstyp==artRegelung }">
											<td colspan="3">
												<table style="text-align:center;width:100%">
													<tbody>
														<logic:iterate id="currentRegelung" name="currentZug" property="abweichung.regelungen" indexId="regId">
															<tr class="${zvfstyleClassZug}">
																<td>
																	<% String property15Name = "zvfZugRegelungsArt(" + i + ", "+ regId +" )"; %>
																	<html:text name="baumassnahmeForm" property="<%=property15Name %>" styleClass="auto"/>
																</td>
																<td>
																	<% String property16Name = "zvfZugRegelungGiltIn(" + i + ", "+ regId +" )"; %>
																		<html:text name="baumassnahmeForm" property="<%=property16Name %>" styleClass="auto"/>
																</td>
																<td>
																	<% String property17Name = "zvfZugRegelungText(" + i + ", "+ regId +" )"; %>
																		<html:text name="baumassnahmeForm" property="<%=property17Name %>" styleClass="auto"/>
																</td>
															</tr>
														</logic:iterate>																			
													</tbody>
												</table>
											</td>
										</c:if>
										<c:if test="${abweichungstyp==artUmleitung }">
											<td>
												<% String property8Name = "zvfZugUmleitungsstrecke(" + i + ")"; %>
												<html:text name="baumassnahmeForm" property="<%=property8Name %>" styleClass="auto"/>
											</td>
										</c:if>
										<c:if test="${abweichungstyp==artUmleitung || abweichungstyp==artVerspaetung }">
											<td>
												<% String property9Name = "zvfZugVerspaetung(" + i + ")"; %>
												<html:text name="baumassnahmeForm" property="<%=property9Name %>" styleClass="auto"/>
											</td>
										</c:if>
										<c:if test="${abweichungstyp==artUmleitung }">
											<td>
												<% String property9aName = "zvfZugQsKs(" + i + ")"; %>
												<html:radio property="<%=property9aName %>" value="0" styleClass="checkbox"><bean:message key="ueb.zug.qsks.0" /></html:radio>
												<html:radio property="<%=property9aName %>" value="1" styleClass="checkbox"><bean:message key="ueb.zug.qsks.1" /></html:radio>
												<html:radio property="<%=property9aName %>" value="2" styleClass="checkbox"><bean:message key="ueb.zug.qsks.2" /></html:radio>
											</td>
										</c:if>
										<c:if test="${abweichungstyp==artAusfall }">
											<td>
												<% String property10Name = "zvfZugAusfallAb(" + i + ")"; %>
												<html:text name="baumassnahmeForm" property="<%=property10Name %>" styleClass="auto"/>
											</td>
											<td>
												<% String property11Name = "zvfZugAusfallBis(" + i + ")"; %>
												<html:text name="baumassnahmeForm" property="<%=property11Name %>" styleClass="auto"/>
											</td>
										</c:if>
										<c:if test="${abweichungstyp==artVorplan }">
											<td>
												<% String property12Name = "zvfZugVerspaetung(" + i + ")"; %>
												<html:text name="baumassnahmeForm" property="<%=property12Name %>" styleClass="auto"/>
											</td>
											<td>
												<% String property13Name = "zvfZugVorplanAb(" + i + ")"; %>
												<html:text name="baumassnahmeForm" property="<%=property13Name %>" styleClass="auto"/>
											</td>
										</c:if>
										<td style="vertical-align: top">
											<% String property14Name = "zvfZugBemerkung(" + i + ")"; %>
											<html:text name="baumassnahmeForm" property="<%=property14Name %>" styleClass="auto"/>
										</td>
										<td style="text-align: right; vertical-align: bottom;">
											<jsp:useBean id="zugParamMap" class="java.util.HashMap" scope="page">
												<c:set target="${zugParamMap}" property="id" value="${baumassnahme.id}" />
												<c:set target="${zugParamMap}" property="type" value="ZVF" />
											</jsp:useBean>
											<c:set target="${zugParamMap}" property="zugId" value="${currentZug.id}" />
											<html:link action="/deleteZug" name="zugParamMap" paramId="" styleClass="delete" titleKey="button.delete">&nbsp;</html:link>
										</td>
									</tbody>
								</table>
							</td>
						</tr>
					</c:if>
				</logic:iterate>
			</tbody>
		</table>
	</logic:iterate>
</div>