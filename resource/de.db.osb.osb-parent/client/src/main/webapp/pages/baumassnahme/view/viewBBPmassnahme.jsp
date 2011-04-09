<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="textcontent">
	<br/>
		<bean:define id="urlBBP" toScope="page"><c:url value="refreshBBP.do" /></bean:define>
		<table class="colored"  id="currentBBP">
			<tr>
				<th><img src="<c:url value='/static/img/button_s_plus.gif' />" onclick="toggleAllBmRows(this);" /></th>
				<th><bean:message key="bbpmassnahme.masid.regid" /></th>
				<th><bean:message key="bbpmassnahme.massnahmenart" /></th>
				<th><bean:message key="bbpmassnahme.streckebbp.streckevzg" /></th>
				<th><bean:message key="bbpmassnahme.bstvon" /></th>
				<th><bean:message key="bbpmassnahme.bstbis" /></th>
				<th><bean:message key="bbpmassnahme.bauzeitraum.sperrart" /></th>
				<th><bean:message key="bbpmassnahme.betriebsweise" /></th>
				<th><bean:message key="bbpmassnahme.arbeiten.bemerkungen" /></th>
				<th><bean:message key="bbpmassnahme.kigbau.lisbanr" /></th>
				<th><bean:message key="baumassnahme.xml.fuerterminberechnung" /></th>
			</tr>
		<logic:empty name="baumassnahme" property="bbpMassnahmen">
			<tr>
				<td style="text-align:center;" colspan="11"><bean:message key="common.noData" /> </td>
			</tr>
		</logic:empty>
		<logic:notEmpty name="baumassnahme" property="bbpMassnahmen">
			<logic:iterate id="current" name="baumassnahme" property="bbpMassnahmen">
				<bean:define toScope="page" id="rowClass" value="${rowClass=='evenrow'?'oddrow':'evenrow'}"/>
				<bean:define toScope="page" id="rowClass2" value="evenrow2" />
				
				<tr class="${rowClass}" id="rowid_${current.id}">
					<td><img id="img_toggle_${current.id}" src="<c:url value='/static/img/button_s_plus.gif' />" onclick="toggleBmRows(this, '${current.id}');" /></td>
					<td><bean:write name="current" property="masId" /></td>
					<td>&nbsp;</td>
					<td>
						<bean:write name="current" property="streckeBBP" />
						&nbsp;<br />
						<bean:write name="current" property="streckeVZG" />
					</td>
					<td><bean:write name="current" property="bstVonLang" /></td>
					<td><bean:write name="current" property="bstBisLang" /></td>
					<td>
						<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${current.beginn}" />
						- <br />
						<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${current.ende}" />
					</td>
					<td>&nbsp;</td>
					<td><bean:write name="current" property="arbeiten" /></td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				
				<logic:iterate id="currentRegelung" name="current" property="regelungen">
					<bean:define toScope="page" id="rowClass2" value="${rowClass2=='evenrow2'?'oddrow2':'evenrow2'}"/>
					
					<tr class="${rowClass2}" id="row_toggle_${current.id}_${currentRegelung.id}" style="display:none;">
					
						<td>&nbsp;</td>
						<td>
							<bean:define id="laenge" value="${fn:length(currentRegelung.regelungId)}"></bean:define>
							<c:out value="${fn:substring(currentRegelung.regelungId,laenge-5,laenge)}"/>
						</td>
						<td><bean:write name="currentRegelung" property="bplArtText" /></td>
						<td>
							<bean:write name="currentRegelung" property="streckeBBP" />
							<br />
							<bean:write name="currentRegelung" property="streckeVZG" />
						</td>
						<td><bean:write name="currentRegelung" property="betriebsStelleVon" /></td>
						<td><bean:write name="currentRegelung" property="betriebsStelleBis" /></td>
						<td>
							<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${currentRegelung.beginn}" />
							- <br />
							<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${currentRegelung.ende}" />
							<br />
							<bean:write name="currentRegelung" property="sperrKz" />
						</td>
						<td><bean:write name="currentRegelung" property="betriebsweise" /></td>
						<td><bean:write name="currentRegelung" property="bemerkungenBpl" /></td>
						<td><bean:write name="currentRegelung" property="lisbaNr" /></td>
						<td>
							<c:if test="${currentRegelung.beginnFuerTerminberechnung == true}">
								X
							</c:if>
							<%--<span id="tip_${currentRegelung.id}">
								<img src="<c:url value='static/img/icon_s_info_small.gif' />" id="tip_${currentRegelung.id}" />
							</span>--%>
						</td>
					</tr>
				</logic:iterate>
			</logic:iterate>
		</logic:notEmpty>
		</table>
</div>
<%--
<logic:iterate id="current" name="baumassnahme" property="bbpMassnahmen">
	<logic:iterate id="currentRegelung" name="current" property="regelungen">
		<div id="tooltip_${currentRegelung.id}" style="width:400px;display: none;">
			<table class="colored">
				<tr class="odd">
					<th><bean:message key="bbpmassnahme.regid" /></th>
					<td><bean:write name="currentRegelung" property="regelungId" /></td>
				</tr>
				<tr class="even">
					<th><bean:message key="bbpmassnahme.bstvon" /></th>
					<td><bean:write name="currentRegelung" property="betriebsStelleVon" /></td>
				</tr>
				<tr class="odd">
					<th><bean:message key="bbpmassnahme.bstbis" /></th>
					<td><bean:write name="currentRegelung" property="betriebsStelleBis" /></td>
				</tr>
				<tr class="even">
					<th><bean:message key="bbpmassnahme.streckebbp" /></th>
					<td><bean:write name="currentRegelung" property="streckeBBP" /></td>
				</tr>
				<tr class="odd">
					<th><bean:message key="bbpmassnahme.streckevzg" /></th>
					<td><bean:write name="currentRegelung" property="streckeVZG" /></td>
				</tr>
				<tr class="even">
					<th><bean:message key="bbpmassnahme.beginn" /></th>
					<td><bean:write name="currentRegelung" property="beginn" format="dd.MM.yyyy HH:mm" /></td>
				</tr>
				<tr class="odd">
					<th><bean:message key="bbpmassnahme.ende" /></th>
					<td><bean:write name="currentRegelung" property="ende" format="dd.MM.yyyy HH:mm" /></td>
				</tr>
				<tr class="even">
					<th><bean:message key="bbpmassnahme.lisba" /></th>
					<td><bean:write name="currentRegelung" property="lisbaNr" /></td>
				</tr>
				<tr class="odd">
					<th><bean:message key="bbpmassnahme.regelvts" /></th>
					<td><bean:write name="currentRegelung" property="regelVTS" /></td>
				</tr>
				<tr class="even">
					<th><bean:message key="bbpmassnahme.betriebsweise" /></th>
					<td><bean:write name="currentRegelung" property="betriebsweise" /></td>
				</tr>
				<tr class="odd">
					<th><bean:message key="bbpmassnahme.bplarttext" /></th>
					<td><bean:write name="currentRegelung" property="bplArtText" /></td>
				</tr>
				<tr class="even">
					<th><bean:message key="bbpmassnahme.sperrkz" /></th>
					<td><bean:write name="currentRegelung" property="sperrKz" /></td>
				</tr>
			</table>
		</div>
	</logic:iterate>
</logic:iterate>
<script type="text/javascript">
<logic:iterate id="current" name="baumassnahme" property="bbpMassnahmen">
	<logic:iterate id="currentRegelung" name="current" property="regelungen">
		var content = $('tooltip_${currentRegelung.id}').cloneNode(true).show();
		new Tip('tip_${currentRegelung.id}',
				content,
			{
				title: '<bean:message key="bbpmassnahme.stammdatenregelung" arg0="${currentRegelung.regelungId}" />'
			});
	</logic:iterate>
</logic:iterate>
</script> --%>
