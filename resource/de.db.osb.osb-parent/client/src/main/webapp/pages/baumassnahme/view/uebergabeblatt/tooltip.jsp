<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>

<logic:iterate id="currentZug" name="uebZuege" indexId="index2">
<div id="tooltip_${currentZug.id}" style="width:750px;">
	<table class="colored">
		<thead>
			<tr>
				<th colspan="5">
					<div style="text-align:center;"><bean:message key="ueb.zug.knotenzeiten" /></div>
				</th>
				<th rowspan="2">
					<div style="text-align:center;"><bean:message key="ueb.zug.tfz" /></div>
				</th>
				<th rowspan="2">
					<div style="text-align:center;"><bean:message key="ueb.zug.last" /></div>
				</th>
				<th rowspan="2">
					<div style="text-align:center;"><bean:message key="ueb.zug.mbr" /></div>
				</th>
				<th rowspan="2">
					<div style="text-align:center;"><bean:message key="ueb.zug.zuglaenge" /></div>
				</th>
				<th rowspan="2">
					<div style="text-align:center;"><bean:message key="ueb.zug.vmax" /></div>
				</th>
				<th colspan="2">
					<div style="text-align:center;"><bean:message key="ueb.zug.besonderheiten" /></div>
				</th>
				<th rowspan="2">
					<div style="text-align:center;"><bean:message key="ueb.zug.bemerkung" /></div>
				</th> 
			</tr>
			<tr>
				<th style="width:10%">
					<div style="text-align:center"><bean:message key="ueb.zug.knotenzeiten.bahnhof" /></div>
				</th>
				<th style="width:15%">
					<div style="text-align:center"><bean:message key="ueb.zug.knotenzeiten.haltart" /></div>
				</th>
				<th style="width:5%">
					<div style="text-align:center"><bean:message key="ueb.zug.knotenzeiten.an" /></div>
				</th>
				<th style="width:5%">
					<div style="text-align:center"><bean:message key="ueb.zug.knotenzeiten.ab" /></div>
				</th>
				<th style="width:15%">
					<div style="text-align:center"><bean:message key="ueb.zug.knotenzeiten.relativlage" /></div>
				</th>
				<th>
					<div style="text-align:center;"><bean:message key="ueb.zug.besonderheiten.kvprofil" /></div>
				</th>
				<th>
					<div style="text-align:center;"><bean:message key="ueb.zug.besonderheiten.streckenklasse" /></div>
				</th>
			</tr>
		</thead>
		<tbody>
			<tr class="${evenOdd}">
				<td colspan="5">
					<table>
						<tbody>
							<logic:iterate id="currentKnoten" name="currentZug" property="knotenzeiten" indexId="iKn">
								<tr>
									<td style="width:10%">
										<div style="text-align:center"><bean:write name="currentKnoten" property="bahnhof"  /></div>
									</td>
									<td style="width:15%">
										<div style="text-align:center"><bean:write name="currentKnoten" property="haltart"  /></div>
									</td>
									<td style="width:5%">
										<div style="text-align:center"><bean:write name="currentKnoten" property="ankunft" format="HH:mm" /></div>
									</td>
									<td style="width:5%">
										<div style="text-align:center"><bean:write name="currentKnoten" property="abfahrt" format="HH:mm" /></div>
									</td>
									<td style="width:15%">
										<div style="text-align:center"><bean:write name="currentKnoten" property="relativlage"  /></div>
									</td>
								</tr>
							</logic:iterate>
						</tbody>
					</table>													
				</td>
				<td>
					<div style="text-align:center"><bean:write name="currentZug" property="zugdetails.tfz.tfz"  /></div>
				</td>
				<td>
					<div style="text-align:center"><bean:write name="currentZug" property="zugdetails.last.last"  /></div>
				</td>
				<td>
					<div style="text-align:center"><bean:write name="currentZug" property="zugdetails.brems"  /></div>
				</td>
				<td>
					<div style="text-align:center"><bean:write name="currentZug" property="zugdetails.laenge"  /></div>
				</td>
				<td>
					<div style="text-align:center"><bean:write name="currentZug" property="zugdetails.vmax"  /></div>
				</td>
				<td>
					<div style="text-align:center"><bean:write name="currentZug" property="kvProfil"  /></div>
				</td>
				<td>
					<div style="text-align:center"><bean:write name="currentZug" property="streckenKlasse"  /></div>
				</td>
				<td>
					<div style="text-align:center"><bean:write name="currentZug" property="bemerkung"  /></div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
</logic:iterate>

<script type="text/javascript">
	var content = $('tooltip_${currentZug.id}').cloneNode(true).show();
	new Tip('tip_${currentZug.id}',
		content,
		{
			title: '<bean:message key="ueb.zug.zugnrhead" arg0="${currentZug.zugnr}" />'
		});
</script>