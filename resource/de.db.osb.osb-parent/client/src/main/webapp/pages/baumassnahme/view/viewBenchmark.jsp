<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
		
<div id="divBenchmark">
	<c:if test="${tab=='Benchmark'}">
		<table class="colored" style="text-align:center;">
			<%-- Spaltenueberschriften --%>
			<thead>
				<tr>
					<th rowspan=2 style="border-right-style:solid;text-align:left;">
						<bean:message key="baumassnahme.regionalbereich" />
					</th>
					<th colspan=3 style="text-align:center;">
						<bean:message key="baumassnahme.biue" />
					</th>
					<th rowspan=2 style="text-align:center;border-left-style:solid;">
						<bean:message key="baumassnahme.zvf" />
						<%--KID2 --%>
						<div style="text-align:center;"><bean:message key="baumassnahme.veroeffentlichtetrassen" /></div>
					</th>
				</tr>
				<tr>
					<th>
						<%--KID1 --%>
						<div style="text-align:center;"><bean:message key="baumassnahme.geregeltetrassen" /></div>
					</th>
					<th>
						<%--KID4 --%>
						<div style="text-align:center;"><bean:message key="baumassnahme.ueberarbeitetetrassen" /></div>
					</th>
					<th>
						<%--KID3 --%>
						<div style="text-align:center;"><bean:message key="baumassnahme.erstelltebiue" /></div>
					</th>
				</tr>
			</thead>
			<tbody>
				<%-- berechtigte Benutzer duerfen alle Daten lesen, nicht berechtigte Benutzer haben Zugriff auf ihre "eigenen" Daten --%>
				<logic:iterate id="currentBenchmark" name="baumassnahme" property="benchmark" indexId="index">
					<easy:hasAuthorization model="${currentBenchmark.key}" authorization="ROLE_BAUMASSNAHME_BENCHMARK_LESEN">
						<c:if test="${currentBenchmark.key.id == baumassnahme.regionalBereichFpl.id}">
							<bean:define toScope="page" id="styleClass" value="${styleClass=='evenrow'?'oddrow':'evenrow'}"/>
							<tr class="${styleClass}">
								<td>
									<div style="text-align:left"><bean:write name="currentBenchmark" property="key.name" /></div>
								</td>
								<td>
									<div style="text-align:center"><bean:write name="currentBenchmark" property="value.geregelteTrassenBiUeE" /></div>
								</td>
								<td>
									<div style="text-align:center"><bean:write name="currentBenchmark" property="value.ueberarbeiteteTrassenBiUe" /></div>
								</td>
								<td>
									<div style="text-align:center"><bean:write name="currentBenchmark" property="value.erstellteBiUe" /></div>
								</td>
								<td>
									<div style="text-align:center"><bean:write name="currentBenchmark" property="value.veroeffentlichteTrassenZvF" /></div>
								</td>
							</tr>
						</c:if>
					</easy:hasAuthorization>
				</logic:iterate>
			</tbody>
		</table>
	</c:if>
</div>