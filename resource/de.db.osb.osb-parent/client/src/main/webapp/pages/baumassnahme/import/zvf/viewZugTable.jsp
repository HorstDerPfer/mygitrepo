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

<logic:iterate id="currentMassnahme" name="zvfFile" property="massnahmen" indexId="index">
	<logic:iterate id="currentZug" name="currentMassnahme" property="zug" indexId="i">
		<c:if test="${currentZug.abweichung.art ==abweichungstyp }">
			<bean:define toScope="page" id="styleClass" value="${styleClass=='evenrow'?'oddrow':'evenrow'}"/>
			<tr class="${styleClass}">
				<bean:define toScope="request" id="bbzrnr" value="${bbzrnr+1}"/>
				<td>
					<input type="checkbox" name="checkZug" value="${i}" class="checkbox">
				</td>
				<td>
					<div style="text-align:center">${bbzrnr}</div>
				</td>
				<td>
					<div style="text-align:center"><bean:write name="currentZug" property="verkehrstag" format="dd.MM.yyyy" /></div>
				</td>
				<c:if test="${abweichungstyp==artUmleitung }">
					<td>
						<div style="text-align:center"><bean:write name="currentZug" property="tageswechsel"  /></div>
					</td>
				</c:if>
				<td>
					<div style="text-align:center"><bean:write name="currentZug" property="zugbez"  /></div>
				</td>
				<td>
					<div style="text-align:center"><bean:write name="currentZug" property="zugnr"  /></div>
				</td>
				<c:if test="${abweichungstyp==artUmleitung }">
					<td>
						<div style="text-align:center">
							<logic:notEmpty name="currentZug" property="bedarf">
								<bean:message key="ueb.zug.bedarf.${currentZug.bedarf}" />
							</logic:notEmpty>
						</div>
					</td>
				</c:if>

				<td>
					<div style="text-align:center">
						<logic:notEmpty name="currentZug" property="regelweg.abgangsbahnhof">
							<bean:write name="currentZug" property="regelweg.abgangsbahnhof.langName"  />
						</logic:notEmpty>
					</div>
				</td>
				<td>
					<div style="text-align:center">
						<logic:notEmpty name="currentZug" property="regelweg.zielbahnhof">
							<bean:write name="currentZug" property="regelweg.zielbahnhof.langName"  />
						</logic:notEmpty>
					</div>
				</td>
				
				<c:if test="${abweichungstyp==artUmleitung }">
					<td>
						<div style="text-align:center;"><bean:write name="currentZug" property="abweichung.umleitung" /></div>
					</td>
					<td>
						<div style="text-align:center;"><bean:write name="currentZug" property="abweichung.verspaetung" /></div>
					</td>
					<td>
						<div style="text-align:center">
							<logic:notEmpty name="currentZug" property="qs_ks">
								<bean:message key="ueb.zug.qsks.${currentZug.qs_ks}" />
							</logic:notEmpty>
						</div>
					</td>
				</c:if>
				
				<c:if test="${abweichungstyp==artVerspaetung }">
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
					<td align="center">
						<table>
							<tbody>
								<logic:iterate id="currentHalt" name="currentZug" property="abweichung.halt" indexId="haltId">
									<tr>
										<td>
											<logic:notEmpty name="currentHalt" property="ausfall">
												<logic:notEmpty name="currentHalt" property="ausfall.langName">	
													<div style="text-align:center;"><bean:write name="currentHalt" property="ausfall.langName" /></div>
												</logic:notEmpty>
											</logic:notEmpty>
											<logic:empty name="currentHalt" property="ausfall.langName">
												<div>&#160;</div>
											</logic:empty>
										</td>
									</tr>
								</logic:iterate>
							</tbody>
						</table>
					</td>
					<td align="center">
						<table>
							<tbody>
								<logic:iterate id="currentHalt" name="currentZug" property="abweichung.halt" indexId="haltId">
									<tr>
										<td>
											<logic:notEmpty name="currentHalt" property="ersatz">
												<logic:notEmpty name="currentHalt" property="ersatz.langName">
													<div style="text-align:center;"><bean:write name="currentHalt" property="ersatz.langName" /></div>
												</logic:notEmpty>
											</logic:notEmpty>
											<logic:empty name="currentHalt" property="ersatz">
												<div>&#160;</div>
											</logic:empty>
										</td>
									</tr>
								</logic:iterate>
							</tbody>
						</table>
					</td>
				</c:if>
				
				<c:if test="${abweichungstyp==artRegelung }">
					<td align="center">
						<table>
							<tbody>
								<logic:iterate id="currentRegelung" name="currentZug" property="abweichung.regelungen" indexId="regId">
									<tr>
										<td>
											<div style="text-align:center;"><bean:write name="currentRegelung" property="art" /></div>
										</td>
									</tr>
								</logic:iterate>
							</tbody>
						</table>
					</td>
					<td align="center">
						<table>
							<tbody>
								<logic:iterate id="currentRegelung" name="currentZug" property="abweichung.regelungen" indexId="regId">
									<tr>
										<td>
											<logic:notEmpty name="currentRegelung" property="giltIn">
												<logic:notEmpty name="currentRegelung" property="giltIn.langName">
													<div style="text-align:center;"><bean:write name="currentRegelung" property="giltIn.langName" /></div>
												</logic:notEmpty>
											</logic:notEmpty>
											<logic:empty name="currentRegelung" property="giltIn">
												<div>&#160;</div>
											</logic:empty>
										</td>
									</tr>
								</logic:iterate>
							</tbody>
						</table>
					</td>
				</c:if>

				<c:if test="${abweichungstyp!=artUmleitung }">
					<td>
						<div style="text-align:center"><bean:write name="currentZug" property="bemerkung"  /></div>
					</td>
				</c:if>
				
				
				<%-- Zugdetails im Popup --%>
				<c:if test="${abweichungstyp==artUmleitung }">
					<td style="text-align:center;">
						<c:if test="${countZuege<=1000 }">
							<span id="tipp_${i}">
								<img src="<c:url value='static/img/icon_s_info_small.gif' />" id="tipp_${i}" />
							</span>
						</c:if>
					</td>
				</c:if>
			</tr>
		</c:if>
	</logic:iterate>
</logic:iterate>
