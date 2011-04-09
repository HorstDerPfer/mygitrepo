<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

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
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>
						<div style="text-align:left;"><bean:write name="zug" property="verkehrstag" format="dd.MM.yyyy" /></div>
					</td>
					<td>
						<div style="text-align:left;"><bean:write name="zug" property="zugbez"  /></div>
					</td>
					<td>
						<div style="text-align:left;"><bean:write name="zug" property="zugnr"  /></div>
					</td>
					<td>
						<div style="text-align:left;">
							<logic:notEmpty name="zug" property="regelweg.abgangsbahnhof">
								<bean:write name="zug" property="regelweg.abgangsbahnhof.langName"  />
							</logic:notEmpty>
						</div>
					</td>
					<td>
						<div style="text-align:left;">
							<logic:notEmpty name="zug" property="regelweg.zielbahnhof">
								<bean:write name="zug" property="regelweg.zielbahnhof.langName"  />
							</logic:notEmpty>
						</div>
					</td>
				</tr>
			</tbody>
		</table></td>
	</tr>
	<%-- Details --%>
	<tr><td>
		<table class="colored" style="text-align:left;" >
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
					<th style="color:black;font-weight: normal;">
						<div style="text-align:left;"><bean:message key="ueb.zug.qsks" /></div>
					</th>
					<th style="color:black;font-weight: normal;">
						<div style="text-align:left;"><bean:message key="ueb.zug.bemerkung" /></div>
					</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>
						<div style="text-align:left;"><bean:write name="zug" property="zugdetails.tfz.tfz"  /></div>
					</td>
					<td>
						<div style="text-align:left;"><bean:write name="zug" property="zugdetails.last.last"  /></div>
					</td>
					<td>
						<div style="text-align:left;"><bean:write name="zug" property="zugdetails.brems"  /></div>
					</td>
					<td>
						<div style="text-align:left;"><bean:write name="zug" property="zugdetails.laenge"  /></div>
					</td>
					<td>
						<div style="text-align:left;"><bean:write name="zug" property="zugdetails.vmax"  /></div>
					</td>
					<td>
						<div style="text-align:left;"><bean:write name="zug" property="kvProfil"  /></div>
					</td>
					<td>
						<div style="text-align:left;"><bean:write name="zug" property="streckenKlasse"  /></div>
					</td>
					<td>
						<div style="text-align:left"><bean:message key="ueb.zug.qsks.${zug.qs_ks}" /></div>
					</td>
					<td>
						<div style="text-align:left;"><bean:write name="zug" property="bemerkung"  /></div>
					</td>
				</tr>
			</tbody>
		</table>
	</td></tr>
</table>