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

<%--

Voraussetzung:
 - Eine Collection vom Typ db.training.bob.model.Baumassnahme muss "irgendwo" existieren

so werden die blauen Info-Button gemacht:
<span id="tip_${currentBaumassnahme.id}">
	<img src="<c:url value='static/img/icon_s_info_small.gif' />" id="tip_${currentBaumassnahme.id}" />
</span>

und so wird diese Datei eingebunden:
<jsp:directive.include file="tooltips.jsp" />

--%>
<logic:iterate id="currentBaumassnahme" name="baumassnahmen" property="list">
	<div id="tooltip_${currentBaumassnahme.id}" style="width:400px;display: none">
		<table class="colored">
			<tr class="odd">
				<th><bean:message key="baumassnahme.art" /></th>
				<td><bean:write name="currentBaumassnahme" property="art" /></td>
			</tr>
			<tr class="even">
				<th><bean:message key="baumassnahme.kigbau" /></th>
				<td><bean:message key="baumassnahme.kigbau.${currentBaumassnahme.kigBau}" /></td>
			</tr>
			<tr class="odd">
				<th><bean:message key="baumassnahme.streckebbp" /></th>
				<td><bean:write name="currentBaumassnahme" property="streckeBBP" /></td>
			</tr>
			<tr class="even">
				<th><bean:message key="baumassnahme.streckevzg" /></th>
				<td><bean:write name="currentBaumassnahme" property="streckeVZG" /></td>
			</tr>
			<tr class="odd">
				<th><bean:message key="baumassnahme.streckenabschnitt" /></th>
				<td><bean:write name="currentBaumassnahme" property="streckenAbschnitt" /></td>
			</tr>
			<tr class="even">
				<th><bean:message key="baumassnahme.artdermassnahme" /></th>
				<td><bean:write name="currentBaumassnahme" property="artDerMassnahme" /></td>
			</tr>
			<tr class="odd">
				<th><bean:message key="baumassnahme.betriebsweise" /></th>
				<td><bean:write name="currentBaumassnahme" property="betriebsweise" /></td>
			</tr>
			<tr class="even">
				<th><bean:message key="baumassnahme.zeitraum" /></th>
				<td>
					<bean:write name="currentBaumassnahme" property="beginnDatum" format="dd.MM.yyyy" />
					 - 
					<bean:write name="currentBaumassnahme" property="endDatum" format="dd.MM.yyyy" />
				</td>
			</tr>
			<tr class="odd">
				<th><bean:message key="baumassnahme.beginn" /></th>
				<td><bean:write name="currentBaumassnahme" property="beginnFuerTerminberechnung" format="dd.MM.yyyy" /></td>
			</tr>
			<tr class="even">
				<th><bean:message key="baumassnahme.bbpmassnahmen" /></th>
				<td>
					<ul class="arrow">
						<logic:iterate id="currentBBPMassnahme" name="currentBaumassnahme" property="bbpMassnahmen" indexId="index">
							<li>${currentBBPMassnahme.masId}</li>
						</logic:iterate>
					</ul>
				</td>
			</tr>
			<tr class="odd">
				<th><bean:message key="baumassnahme.regionalbereichfpl" /></th>
				<td>
					<logic:notEmpty name="currentBaumassnahme" property="regionalBereichFpl">
						<bean:write name="currentBaumassnahme" property="regionalBereichFpl.name" />
					</logic:notEmpty>
				</td>
			</tr>
			<tr class="even">
				<th><bean:message key="baumassnahme.bearbeitungsbereich" /></th>
				<td>
				<logic:notEmpty name="currentBaumassnahme" property="bearbeitungsbereich">
					<bean:write name="currentBaumassnahme" property="bearbeitungsbereich.name" />
				</logic:notEmpty>
				</td>
			</tr>
			<tr class="odd">
				<th><bean:message key="baumassnahme.fplonr" /></th>
				<td><bean:write name="currentBaumassnahme" property="fploNr" /></td>
			</tr>
			<tr class="even">
				<th><bean:message key="baumassnahme.vorgangsnr" /></th>
				<td><bean:write name="currentBaumassnahme" property="vorgangsNr" /></td>
			</tr>
			<tr class="odd">
				<th><bean:message key="baumassnahme.prioritaet" /></th>
				<td>
					<logic:notEmpty name="currentBaumassnahme" property="prioritaet">
						<bean:message key="baumassnahme.prioritaet.${currentBaumassnahme.prioritaet}" />
					</logic:notEmpty>
				</td>
			</tr>
		</table>
	</div>
</logic:iterate>


<script type="text/javascript">
	<logic:iterate id="currentBaumassnahme" name="baumassnahmen" property="list">
		var content = $('tooltip_${currentBaumassnahme.id}').cloneNode(true).show();
		new Tip('tip_${currentBaumassnahme.id}',
				content,
			{
				title: '<bean:message key="baumassnahme.stammdaten" />'
			});
	</logic:iterate>
</script>