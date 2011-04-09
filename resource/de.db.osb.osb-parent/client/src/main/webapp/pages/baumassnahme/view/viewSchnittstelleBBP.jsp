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
	<%-- Spaltenüberschriften --%>
	<thead>
		<tr>
			<th>&nbsp;</th>
			<th>
				<div style="text-align:center;"><bean:message key="baumassnahme.termine.studie" /></div>
			</th>
			
			<th>
				<div style="text-align:center;"><bean:message key="baumassnahme.termine.anforderungbbzr" /></div>
			</th>
			
			<c:if test="${baumassnahme.art != 'B'}">
				<th>
					<div style="text-align:center;"><bean:message key="baumassnahme.termine.bbp.biueentwurf.nobr" /></div>
				</th>
			</c:if>
			
			<th>
				<div style="text-align:center;"><bean:message key="baumassnahme.termine.bbp.zvfentwurf.nobr" /></div>
			</th>
			
			<c:if test="${baumassnahme.art != 'B'}">
				<th>
					<div style="text-align:center;"><bean:message key="baumassnahme.termine.gesamtkonzeptbbzr" /></div>
				</th>
			</c:if>
			
			<th>
				<div style="text-align:center;"><bean:message key="baumassnahme.termine.bbp.zvf" /></div>
			</th>
		</tr>
	</thead>
	<tbody>
		<%-- Soll-Termine --%>
		<tr class="activerow">
			<td><div>Solltermin&#58;</div></td>
			<logic:iterate id="currentTermin" name="baumassnahme" property="sollTermineBBP" indexId="index">
				<td><div style="text-align:center">${currentTermin}</div></td>
			</logic:iterate>
		</tr>
		<%-- Ist-Termine --%>
		<tr class="evenrow">
			<td><div style="width:100px;">Ist</div></td>
			
			<%-- Studie/Grobkonzept --%>
			<c:choose>
				<c:when test='${baumassnahme.art == "KS"}'>
					<td>
						<div style="text-align:center"><bean:write name="baumassnahme" property="baubetriebsplanung.studieGrobkonzept" format="dd.MM.yyyy" /></div>
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
			
			<%-- Anforderunge BBZR --%>
			<td>
				<div style="text-align:center">
					<bean:write name="baumassnahme" property="baubetriebsplanung.anforderungBBZR" format="dd.MM.yyyy" />
					<div style="padding-left:10px">
						<bob:statusIcon status="${baumassnahme.baubetriebsplanung.anforderungBBZRStatus}" />
						<c:if test="${baumassnahme.baubetriebsplanung.anforderungBBZRStatus == 'COUNTDOWN14'}">
							${baumassnahme.baubetriebsplanung.verbleibendeTage_AnforderungBBZR}
						</c:if>
					</div>
				</div>
			</td>
			
			<%-- Vorentwurf BiÜ/ZvF (ehemals BiÜ-Entwurf) --%>
			<c:if test="${baumassnahme.art != 'B'}">
				<td>
					<div style="text-align:center">
						<bean:write name="baumassnahme" property="baubetriebsplanung.biUeEntwurf" format="dd.MM.yyyy" />
						<div style="padding-left:10px">
							<bob:statusIcon status="${baumassnahme.baubetriebsplanung.biUeEntwurfStatus}" />
							<c:if test="${baumassnahme.baubetriebsplanung.biUeEntwurfStatus == 'COUNTDOWN14'}">
								${baumassnahme.baubetriebsplanung.verbleibendeTage_BiUeEntwurf}
							</c:if>
						</div>
					</div>
				</td>
			</c:if>
			
			<%-- BiÜ/ZvF-Ent (ehemals ZvF Entwurf) --%>
			<td>
				<div style="text-align:center">
					<bean:write name="baumassnahme" property="baubetriebsplanung.zvfEntwurf" format="dd.MM.yyyy" />
					<div style="padding-left:10px">
						<bob:statusIcon status="${baumassnahme.baubetriebsplanung.zvfEntwurfStatus}" />
						<c:if test="${baumassnahme.baubetriebsplanung.zvfEntwurfStatus == 'COUNTDOWN14'}">
							${baumassnahme.baubetriebsplanung.verbleibendeTage_ZvfEntwurf}
						</c:if>
					</div>
				</div>
			</td>
			
			<%-- Gesamtkonzept BBZR --%>
			<c:if test="${baumassnahme.art != 'B'}">
				<td>
					<div style="text-align:center">
						<bean:write name="baumassnahme" property="baubetriebsplanung.gesamtKonzeptBBZR" format="dd.MM.yyyy" />
						<div style="padding-left:10px">
							<bob:statusIcon status="${baumassnahme.baubetriebsplanung.gesamtKonzeptBBZRStatus}" />
							<c:if test="${baumassnahme.baubetriebsplanung.gesamtKonzeptBBZRStatus == 'COUNTDOWN14'}">
								${baumassnahme.baubetriebsplanung.verbleibendeTage_GesamtKonzeptBBZR}
							</c:if>
						</div>
					</div>
				</td>
			</c:if>
			
			<%-- BiÜ/ZvF (ehemals ZvF)--%>
			<td>
				<div style="text-align:center">
					<bean:write name="baumassnahme" property="baubetriebsplanung.zvf" format="dd.MM.yyyy" />
					<div style="padding-left:10px">
						<bob:statusIcon status="${baumassnahme.baubetriebsplanung.zvfStatus}" />
						<c:if test="${baumassnahme.baubetriebsplanung.zvfStatus == 'COUNTDOWN14'}">
							${baumassnahme.baubetriebsplanung.verbleibendeTage_Zvf}
						</c:if>
					</div>
				</div>
			</td>
		</tr>
	</tbody>
</table>

