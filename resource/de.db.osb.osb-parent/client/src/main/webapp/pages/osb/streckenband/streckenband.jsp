<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<script type="text/javascript" src="<c:url value='/static/js/jquery/jquery-1.4.2.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/static/js/jquery/jquery-ui-1.8.custom.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/static/js/jquery/jquery.livequery.js'/>"></script>
<script type="text/javascript">
	var $j = jQuery.noConflict();
</script>

<!--  ANFANG: Query Styles und JS für Progress Bar -->
<link href="<c:url value='/static/css/jquery/jquery-ui-1.8.1.bahn.css'/>" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/static/js/ajaxprogress.js'/>"></script>
<script type="text/javascript">
	
	// Funktion wird sicherheitshalber aufgerufen, falls sich Seitengroesse zur vorhergehenden Seite veraendert hat
	function setBodyProperties(){
		var width = document.documentElement.clientWidth;
		if(width < 780){
			width = 780;
		}
		$('streckenband_Top').style.width = width - 351;
		$('streckenband_Body').style.width = width - 351;
		$('streckenband_ButtonBar').style.width = width - 39;

		var height = document.documentElement.clientHeight - 32;
		if(height < 540){
			height = 540;
		}
		$('streckenband_Left').style.height = height - 75;
		$('streckenband_Body').style.height = height - 57;
		$('streckenband_ButtonBar').style.top = height;
	}

	$j(window).ready(setBodyProperties);
	$j(window).bind('beforeunload', changeChecker);
</script>
<!--  ENDE Query Styles und JS für Progress Bar -->

<%-- Das Streckenband wird mit 4 DIV Containern dargestellt, vergl. Excel mit fixiertem Tabellenkopf. --%>
<c:set var="streckenband_top" value="0" />
<c:set var="streckenband_left" value="0" />
<c:set var="width_topLeft" value="350" />
<c:set var="height_topLeft" value="56" />
<c:set var="width_body" value="${sessionScope.clientWidth - width_topLeft - 1}" />
<c:set var="height_body" value="${sessionScope.clientHeight - height_topLeft - 33}" />
<c:set var="singleLineHeight" value="14" />

<style type="text/css">
	div.container {
		overflow:hidden;
		padding:0px;
	}
	
	div#streckenband_TopLeft {
		position:absolute;
		height:${height_topLeft}px;
		width:${width_topLeft}px;
		top:${streckenband_top}px;
		left:${streckenband_left}px;
		padding-right:0px;
		border: 1px solid black;
		border-width:1px 0px 0px 1px;
		z-index:5;
	}
	
	div#streckenband_Top {
		position:absolute;
		top:${streckenband_top}px;
		left:${streckenband_left + width_topLeft}px;
		height:${height_topLeft}px;
		width:${width_body}px;
		padding-bottom:0px;
		padding-left:0px;
		border:1px solid black;
		border-width:1px 1px 0px 0px;
		z-index:4;
		overflow-y:scroll;
	}
	
	div#streckenband_Left {
		position:absolute;
		top:${streckenband_top + height_topLeft}px;
		left:${streckenband_left}px;
		height: ${height_body - 17}px;
		width:${width_topLeft}px;
		padding-right:0px;
		padding-top:0px;
		border:1px solid black;
		border-width:0px 0px 1px 1px;
		z-index:3;
	}
	
	div#streckenband_Body {
		position:absolute;
		top:${streckenband_top + height_topLeft}px;
		left:${streckenband_left + width_topLeft}px;
		height: ${height_body}px;
		width: ${width_body}px;
		<%--<c:choose><c:when test="${!empty streckenband}">overflow:auto;</c:when><c:otherwise>overflow:scroll;</c:otherwise></c:choose>--%>
		overflow:scroll;
		padding-left:0px;
		padding-top:0px;
		border:1px solid black;
		border-width:0px 1px 1px 0px;
		z-index:2;
	}
	
	div#streckenband_ButtonBar {
		position:absolute;
		top:${height_body + 57}px;
		width:${sessionScope.clientWidth - 39}px;
		border-width:1px;
	}
	
	div.nodata {
		background-color: #cccccc;
		color: black;
		font-style: italic;
		height:100%;
	}

	table.colored {
		background-color: #cccccc;
		table-layout: fixed;
		overflow:hidden;
		
	}
	
	table.colored td.nodata {
		background-color: #F7EFDE;
		color: black;
		font-style: italic;
	}
	
	table.colored td {
		border-right: 1px solid;
		padding:2px 5px;
		border-color: Transparent;

	}
	
	table.colored th {
		border-right: 1px solid;
		padding:2px 5px;
		border-color: Transparent;
	}

table.colored .buendel_oddrow   {background-color:#c6bead;}
table.colored .buendel_evenrow  {background-color:#c5ae94;}
table.colored .activerow		{background-color:#EFB684;}

	.fixedSizeCell {
		/*width:50px;*/
		overflow:hidden;
		white-space: nowrap;
		font-family:Arial, Helvetica, sans-serif;
		text-decoration:none;
		padding:0px;
		margin:0px;
	}
	
	.header {
		color:white;
		font-weight:bold;
		line-height: 14px;
	}
	
	.bst { /* CSS Klasse für Betriebsstellen */
		width:50px;
	}
	
	.bstBlock {
		font-size:10px;
		vertical-align: middle;
		text-align: center;
	}
	
	.rowHeight {
		line-height:${singleLineHeight}px;
		white-space: normal;
		height:${singleLineHeight * 3}px;
	}
</style>

<c:set var="width_tableCell" value="width:50px;" />
<c:set var="width_DatumGleissperrung" value="width:90px;" />
<c:set var="width_lfdNr" value="width:50px;" />
<c:set var="width_gewerk" value="width:45px;" />
<c:set var="width_projektDefinitionDbBez" value="width:120px;" />
<c:set var="width_sperrzustand" value="width:90px;" />
<c:set var="width_buendel" value="width:90px;" />
<c:set var="width_dauersperrung" value="width:60px;" />
<c:set var="width_massnahmenstatus" value="width:65px;" />
<c:set var="width_bemerkung" value="width:130px;" />
<c:set var="width_ctrlVerschieben" value="width:70px;" />
<c:set var="width_ctrlBuendel" value="width:100px" />
<c:set var="titleDeleted"><bean:message key="common.deleted" /></c:set>

<c:if test="${streckenbandViewType != null}">
	<c:if test="${streckenbandViewType == 'VIEW'}">
		<bean:define id="urlAction"	value="/osb/streckenband/edit?method=createBuendel" />
		<bean:define id="colspan" value="12" />
	</c:if>
	<c:if test="${streckenbandViewType == 'PROCESSBUENDEL'}">
		<bean:define id="urlAction" value="/osb/saveAddGleissperrungToBuendel?sp=true" />
		<bean:define id="colspan" value="12" />
	</c:if>
</c:if>

<body>

<div id="saveDialog" title="<bean:message key="common.loading" />" style="display:none;">
	<p class="center">
		<bean:message key="common.wait" /><br /><br />
		<img src="<c:url value='/static/img/ajax-loader.gif'/>"  />
	</p>
</div>
<script type="text/javascript">
<%-- Laden-Dialog solange anzeigen, bis die Seite fertig geladen wurde --%>
showSaveDialog();
$j(window).ready(function() {
	hideSaveDialog = true;
	showSaveDialog();
});
</script>

<html:form action="${urlAction}">
	<c:if test="${buendel != null}">
		<input type="hidden" name="buendelId" value='<c:out value="${buendel.id}" />' id="InputBuendelId" />
	</c:if>
	<c:if test="${buendel == null}">
		<input type="hidden" name="buendelId" value='-1' id="InputBuendelId" />
	</c:if>
		
	<%-- oben links, enthält Überschriften, wird nicht gescrollt --%>
	<div class="container" id="streckenband_TopLeft">
		<table class="colored" style="border:0px;">
			<thead>
				<tr>
					<th style="${width_lfdNr}" rowspan="3"><div class="fixedSizeCell header" style="${width_lfdNr}">Lfd. Nr.</div></th>
					<th style="${width_gewerk}"><div class="fixedSizeCell header" style="${width_gewerk}">Titel</div></th>
					<th style="${width_projektDefinitionDbBez}"><div class="fixedSizeCell header" style="${width_projektDefinitionDbBez}">Projektdefinition</div></th>
					<th style="${width_DatumGleissperrung}" rowspan="3"><div class="fixedSizeCell header" style="${width_DatumGleissperrung}">Gleis-<br />sperrung<br />angezeigt</div></th>
				</tr>
				<tr>
					<th rowspan="2">
						<logic:present name="streckeVzg"><div class="fixedSizeCell header" style="${width_gewerk}">${streckeVzg.nummer}</div></logic:present>
					</th>
					<th><div class="fixedSizeCell header" style="${width_projektDefinitionDbBez}">&nbsp; DB Bez.</div></th>
				</tr>
				<tr><th>&nbsp;</th></tr>
			</thead>
		</table>
	</div>
	
	<%-- oben, enthält Überschriften und Betriebsstellen, scrollt links/rechts --%>
	<div class="container" id="streckenband_Top">
		<table class="colored" style="border:0px;">
			<thead>
				<%-- Betriebsstellen auflisten --%>
				<tr>
					<c:forEach var="item" items="${bstList}">
						<th style="${width_tableCell}"><div class="fixedSizeCell header bst">${item.betriebsstelle.kuerzel}</div></th>
					</c:forEach>
					
					<%-- Bündelinformationen --%>
					<th width="90px" style="border-right: 1px none;"><div style="border-right: 0px none; position:relative; width:180px;">Bündelspez. Informationen</div></th>
					<th width="90px" style="border-right: 1px none;"></th>
					<th width="60px"></th>
					<th style="${width_massnahmenstatus}" rowspan="3"><div class="fixedSizeCell header" style="${width_massnahmenstatus}">Maßnahmen-<br />Status</div></th>
					<th style="${width_bemerkung}" rowspan="3"><div class="fixedSizeCell header" style="${width_bemerkung}">Bemerkung</div></th>
					
					<%-- Verschieben --%>
					<c:if test="${streckenbandViewType == 'VIEW'}">
						<th style="${width_ctrlVerschieben}" rowspan="3"><div class="fixedSizeCell header" style="text-align:center;${width_ctrlVer}">verschieben</div></th>
					</c:if>
							
					<%-- Bündeln --%>
					<th style="${width_ctrlBuendel}" rowspan="3">
						<div class="fixedSizeCell header" style="${width_ctrlBuendel}">Bündeln</div>
					</th>
				</tr>
				<tr>
					<c:forEach var="item" items="${bstList}">
						<th style="${width_tableCell}"><div class="fixedSizeCell header bst" style="font-size:10px;">${item.betriebsstelle.name}</div></th>
					</c:forEach>
					
					<th style="width:100px;" rowspan="2"><div class="fixedSizeCell header">Sperr-<br />zustand</div></th>
					<th style="${width_buendel}" rowspan="2"><div class="fixedSizeCell header">Bündel</div></th>
					<th style="${width_dauersperrung}" rowspan="2"><div class="fixedSizeCell header">Dauer-<br />sperrung</div></th>
				</tr>
				<tr>
					<c:forEach var="item" items="${bstList}">
						<th style="border-left:1px solid black;"><div class="fixedSizeCell header bst" style="font-size:10px;">${item.km}</div></th>
					</c:forEach>
				</tr>
				
			</thead>
		</table>
	</div>
	
	<%-- links, enthält Informationen der Maßnahme, scrollt auf/ab --%>
	<div class="container" id="streckenband_Left">
		<c:choose>
			<c:when test="${!empty streckenband }">
				<table class="colored" style="border:0px;">
					<c:forEach var="row" items="${streckenband}">
						<c:set var="currentGleissperrung" value="${row.gleissperrung}" />
						<c:set var="currentMassnahme" value="${currentGleissperrung.massnahme}" />
						<c:choose>
							<c:when test="${!empty currentGleissperrung.buendel}">
								<c:set var="rowClass" value="${rowClass=='buendel_evenrow'?'buendel_oddrow':'buendel_evenrow'}" />
							</c:when>
							<c:otherwise>
								<c:set var="rowClass" value="${rowClass=='evenrow'?'oddrow':'evenrow'}" />
							</c:otherwise>
						</c:choose>
						
<%--						<tr class="${rowClass}" id="row_left_${currentGleissperrung.id}" onmouseover="registerTooltip('row_left_${currentGleissperrung.id}', ${currentGleissperrung.id});" onmouseout="removeTooltip();" onclick="markRow(this, ${currentGleissperrung.id});">--%>
						<c:set var="currentLineHeight" value="${(fn:length(currentGleissperrung.buendel)+1)*singleLineHeight}" />
						<tr class="${rowClass}" id="row_left_${currentGleissperrung.id}" onclick="markRow(this, ${currentGleissperrung.id});" style="height:${currentLineHeight}px">
							<%-- Lfd.Nr. --%>
							<td style="${width_lfdNr}"><div class="fixedSizeCell rowHeight" style="${width_lfdNr};">
									<c:if test="${streckenbandViewType == 'VIEW'}">
										<easy:hasAuthorization model="${currentGleissperrung}" authorization="ROLE_GLEISSPERRUNG_LESEN">
											<html:link action="/osb/viewGleissperrung.do?gleissperrungId=${currentGleissperrung.id}"
												styleClass="list">
												${currentGleissperrung.lfdNr}
											</html:link>
										</easy:hasAuthorization>
										<easy:hasNotAuthorization model="${currentGleissperrung}" authorization="ROLE_GLEISSPERRUNG_LESEN">
											${currentGleissperrung.lfdNr}
										</easy:hasNotAuthorization>
									</c:if>
									<c:if test="${streckenbandViewType == 'PROCESSBUENDEL'}">
											${currentGleissperrung.lfdNr}
									</c:if>
									<%--
										<br />
										<!-- Delete Image -->
										<c:if test="${currentGleissperrung.deleted == true}"><img src="<c:url value='/static/img/error.png' />" title="${titleDeleted}" /></c:if>
										<c:set var="imgIconWidth" value="13px" />
										<!-- durchgehend/schichtweises Arbeiten Images nach Feinkonzept BaBet_OSB S. 37 -->
										<c:if test="${currentGleissperrung.durchgehend != null && currentGleissperrung.schichtweise != null}">
											<c:choose>
												<c:when test="${currentGleissperrung.durchgehend == true && currentGleissperrung.schichtweise == false}">
													<img src="<c:url value='/static/img/baubetriebsplan/nichtErforderlich.png' />" width="${imgIconWidth}" height="${imgIconWidth}" title="<bean:message key='regelung.durchgehend' />" />
												</c:when>
												<c:when test="${currentGleissperrung.durchgehend == false && currentGleissperrung.schichtweise == false}">
													<img src="<c:url value='/static/img/baubetriebsplan/unterbrochen.png' />" width="${imgIconWidth}" height="${imgIconWidth}" title="<bean:message key='regelung.unterbrochen' />" />
												</c:when>
												<c:when test="${currentGleissperrung.durchgehend == true && currentGleissperrung.schichtweise == true}">
													<img src="<c:url value='/static/img/baubetriebsplan/schichten.png' />" width="${imgIconWidth}" height="${imgIconWidth}" title="<bean:message key='regelung.durchgehendeSchichten' />" />
												</c:when>
												<c:when test="${currentGleissperrung.durchgehend == false && currentGleissperrung.schichtweise == true}">
													<img src="<c:url value='/static/img/baubetriebsplan/schichten.png' />" width="${imgIconWidth}" height="${imgIconWidth}" title="<bean:message key='regelung.unterbrocheneSchichten' />" />
													<img src="<c:url value='/static/img/baubetriebsplan/unterbrochen.png' />" width="${imgIconWidth}" height="${imgIconWidth}" title="<bean:message key='regelung.unterbrocheneSchichten' />" />
												</c:when>
											</c:choose>	
										</c:if>
									--%>
								</div>
							</td>
							<td style="${width_gewerk}"><div class="fixedSizeCell rowHeight" style="${width_gewerk}">${currentMassnahme.gewerk}</div></td>
											
							<td style="${width_projektDefinitionDbBez}"><div class="fixedSizeCell rowHeight" style="${width_projektDefinitionDbBez }">${currentMassnahme.projektDefinitionDbBez}</div></td>
							<td style="${width_DatumGleissperrung}"><div class="fixedSizeCell rowHeight" style="${width_DatumGleissperrung}">   
								<c:if test="${currentGleissperrung.zeitVon != null}">
									<fmt:formatDate pattern="dd.MM.yy HH:mm" value="${currentGleissperrung.zeitVon}" />
										- 
									<fmt:formatDate pattern="dd.MM.yy HH:mm" value="${currentGleissperrung.zeitBis}" />
								</c:if>
			
								<c:if test="${currentGleissperrung.zeitVon == null}">
									<center><bean:message key="info.gleissperrung.NoData" /></center>
								</c:if>
							</div>
							</td>
						</tr>
					</c:forEach>
					<c:remove var="currentLineHeight" />
					<c:remove var="currentGleissperrung" />
					<c:remove var="currentMassnahme" />
					<c:remove var="rowClass" />
				</table>
			</c:when>
			<c:otherwise>
				<div class="nodata"></div>
			</c:otherwise>
		</c:choose>
	</div>
	
	<%-- "Rest", enthält das Streckenband, scrollt alles --%>
	<div class="container" id="streckenband_Body">
		<c:choose>
			<c:when test="${!empty streckenband }">
				<table class="colored" style="border:0px;">
					<c:forEach var="row" items="${streckenband}">
						<c:set var="currentGleissperrung" value="${row.gleissperrung}" />
						<c:set var="currentMassnahme" value="${currentGleissperrung.massnahme}" />
						<%-- gebündelte Gleissperrungen farblich unterscheiden --%>
						<c:choose>
							<c:when test="${!empty currentGleissperrung.buendel}">
								<c:set var="rowClass" value="${rowClass=='buendel_evenrow'?'buendel_oddrow':'buendel_evenrow'}" />
							</c:when>
							<c:otherwise>
								<c:set var="rowClass" value="${rowClass=='evenrow'?'oddrow':'evenrow'}" />
							</c:otherwise>
						</c:choose>
						
<%--						<tr class="${rowClass}" id="row_body_${currentGleissperrung.id }" onmouseover="registerTooltip('row_body_${currentGleissperrung.id }', ${currentGleissperrung.id});" onmouseout="removeTooltip();" onclick="markRow(this, ${currentGleissperrung.id});">--%>
						<c:set var="currentLineHeight" value="${(fn:length(currentGleissperrung.buendel)+1)*singleLineHeight}" />
						<tr class="${rowClass}" id="row_body_${currentGleissperrung.id}" onclick="markRow(this, ${currentGleissperrung.id});" style="height:${currentLineHeight}px">
							<%-- Balken zeichnen --%>
							<c:set var="startBstFound" value="false" />
							<c:set var="endBstFound" value="false" />
							
							<%-- Betriebsstellen vertauschen, damit das Streckenband richtig gezeichnet wird --%>
							<c:forEach var="bstVonLink" items="${currentGleissperrung.bstVon.strecken}">
								<c:if test="${bstVonLink.strecke == streckeVzg}">
									<c:forEach var="bstBisLink" items="${currentGleissperrung.bstBis.strecken}">
										<c:if test="${bstBisLink.strecke == streckeVzg}">
										<%--${bstBisLink} --%>
											<c:choose>
												<c:when test="${bstVonLink.km > bstBisLink.km}">
													<c:set var="bstVon" value="${currentGleissperrung.bstBis }" />
													<c:set var="bstBis" value="${currentGleissperrung.bstVon }" />
												</c:when>
												<c:otherwise>
													<c:set var="bstVon" value="${currentGleissperrung.bstVon }" />
													<c:set var="bstBis" value="${currentGleissperrung.bstBis }" />
												</c:otherwise>
											</c:choose>
										</c:if>
									</c:forEach>
								</c:if>
							</c:forEach>
							
							<c:forEach var="bstItem" items="${bstList}" varStatus="bstRowCounter">
								<c:choose>
									<%-- bst == von == bis  --%>
									<c:when test="${ bstItem.betriebsstelle == bstVon && bstItem.betriebsstelle == bstBis }">
										<c:set var="startBstFound" value="true" />
										<c:set var="endBstFound" value="true" />
										<td id="tip_${currentGleissperrung.id}"
											style="border-left: 1px solid #fafafa; border-right: 1px solid #fafafa; background-color: #AEE6E6;${width_tableCell}">
											<div class="fixedWidthCell rowHeight bst bstBlock"><%@ include file="./blockEspTspLabel.jsp"%></div>
										</td>
									</c:when>
									
									<%-- bst == von && bst != bis  --%>
									<c:when test="${ bstItem.betriebsstelle == bstVon && bstItem.betriebsstelle != bstBis }">
										<%-- bstVon ist in letzter Block --%>
										<c:if test="${ startBstFound == true }">
											<c:set var="startBstFound" value="true" />
											<c:set var="endBstFound" value="true" />
											<td id="tip_${currentGleissperrung.id}"
												style="border-right: 1px solid #fafafa; background-color: #AEE6E6;${width_tableCell}">
												<div class="fixedWidthCell rowHeight bst bstBlock">&nbsp;</div>
											</td>
										</c:if>
										
										<%-- bstVon ist in erster Block  --%>
										<c:if test="${ startBstFound == false }">
											<c:set var="startBstFound" value="true" />
											<c:set var="endBstFound" value="false" />
											<td id="tip_${currentGleissperrung.id}"
												style="border-left: 1px solid #fafafa; background-color: #AEE6E6;${width_tableCell}">
												<div class="fixedWidthCell rowHeight bst bstBlock"><%@ include file="./blockEspTspLabel.jsp"%></div>
											</td>
										</c:if>
									</c:when>
									
									<%-- bst != von && bst == bis  --%>
									<c:when test="${ bstItem.betriebsstelle != bstVon && bstItem.betriebsstelle == bstBis }">
									<%-- bstBis ist in letzter Block  --%>
										<c:if test="${ startBstFound == true }">
											<c:set var="startBstFound" value="true" />
											<c:set var="endBstFound" value="true" />
											<td id="tip_${currentGleissperrung.id}"
												style="border-right: 1px solid #fafafa; background-color: #AEE6E6;${width_tableCell}">
												<div class="fixedWidthCell rowHeight bst bstBlock">&nbsp;</div>
											</td>
										</c:if>
										
										<%-- bstBis ist in erster Block  --%>
										<c:if test="${ startBstFound == false }">
											<c:set var="startBstFound" value="true" />
											<c:set var="endBstFound" value="false" />
											<td id="tip_${currentGleissperrung.id}"
												style="border-left: 1px solid #fafafa; background-color: #AEE6E6;${width_tableCell}">
												<div class="fixedWidthCell rowHeight bst bstBlock"><%@ include file="./blockEspTspLabel.jsp"%></div>
											</td>
										</c:if>
										
									</c:when>
									<c:otherwise>
										<%-- mittlere Bloecke  --%>
										<c:if test="${ startBstFound == true && endBstFound == false}">
											<td style="background-color: #AEE6E6;${width_tableCell}" id="tip_${currentGleissperrung.id}">
												<div class="fixedWidthCell rowHeight bst bstBlock"></div>
											</td>
										</c:if>
		
										<%-- leer  --%>
										<c:if test="${ startBstFound == false && endBstFound == false}">
											<td style="${width_tableCell}"><div class="fixedWidthCell rowHeight bst bstBlock">&nbsp;</div></td>
										</c:if>
										<c:if test="${ startBstFound == true && endBstFound == true}">
											<td style="${width_tableCell}"><div class="fixedWidthCell rowHeight bst bstBlock"></div></td>
										</c:if>
									</c:otherwise>
								</c:choose>
							</c:forEach>
							
							<%-- Sperrzustand --%>
							<td style="${width_sperrzustand}"><div class="fixedSizeCell rowHeight">
								<logic:iterate id="currentBuendel" name="currentGleissperrung" property="buendel" indexId="index">
									<c:if test="${currentBuendel.betriebsweise != null}">
										${currentBuendel.betriebsweise.kuerzel }
										<c:if test="${ (fn:length(currentGleissperrung.buendel) > (index+1)) }">,</c:if>
									</c:if>
								</logic:iterate>
							</div></td>
							
							<%-- Bündel --%>
							<td style="${width_buendel}"><div class="fixedSizeCell rowHeight" style="height:${currentLineHeight}px">
								<logic:iterate id="currentBuendel" name="currentGleissperrung" property="buendel" indexId="index">
									<c:if test="${streckenbandViewType == 'VIEW'}">
										<html:link action="/osb/viewBuendel" styleClass="list">
											<html:param name="buendelId" value="${currentBuendel.id}" />
											<bean:write name="currentBuendel" property="buendelId" />
										</html:link>
									</c:if>
									<c:if test="${streckenbandViewType == 'PROCESSBUENDEL'}">
										<bean:write name="currentBuendel" property="buendelId" />
									</c:if>
									<c:if test="${currentBuendel.deleted == true}"><img src="<c:url value='/static/img/error.png' />" title="${titleDeleted}" /></c:if>
									<c:if test="${ (fn:length(currentGleissperrung.buendel) > (index+1)) }"><br /></c:if>
								</logic:iterate>
							</div></td>
							
							<%-- Dauersperrung --%>
							<td style="${width_dauersperrung}"><div class="fixedSizeCell rowHeight">
								<logic:iterate id="currentBuendel" name="currentGleissperrung" property="buendel" indexId="index">
									<bean:write name="currentBuendel" property="sperrdauerBuendel" format="#" />h
									<c:if test="${ (fn:length(currentGleissperrung.buendel) > (index+1)) }">,</c:if>
								</logic:iterate>
							</div></td>
							
							<%-- Maßnahmenstatus --%>
							<td style="${width_massnahmenstatus}"><div class="fixedSizeCell rowHeight">&nbsp;</div></td>
							
							<%-- Kommentar / Paket ID --%>
							<td style="${width_bemerkung}"><div style="${width_bemerkung}" class="fixedSizeCell rowHeight">
								${currentMassnahme.kommentar}
								<c:if test="${ !empty currentMassnahme && !empty currentMassnahme.paket }">
									(Paket-ID: ${currentMassnahme.paket.paketId})
								</c:if>
							</div></td>
							
							<%-- Schaltflächen zum Bewegen von Zeilen --%>
							<c:if test="${streckenbandViewType == 'VIEW'}">
								<td style="${width_ctrlVerschieben}">
									<div class="fixedSizeCell rowHeight" style="text-align:center;${width_ctrlVerschieben}">
										<easy:hasAuthorization model="${currentGleissperrung}" authorization="ROLE_MASSNAHME_BEARBEITEN">
											<c:if test="${streckenbandViewType == 'VIEW'}">
												<html:link action="/osb/streckenband/edit" styleClass="openAll">
													<html:param name="method" value="moveDown" />
													<html:param name="moveRowId" value="${row.id}" />
													<html:param name="vzgStreckeId" value="${streckeVzg.id}" />
													<html:param name="sp">true</html:param>
													&nbsp;
												</html:link>
												<html:link action="/osb/streckenband/edit" styleClass="closeAll">
													<html:param name="method" value="moveUp" />
													<html:param name="moveRowId" value="${row.id}" />
													<html:param name="vzgStreckeId" value="${streckeVzg.id}" />
													<html:param name="sp">true</html:param>
													&nbsp;
												</html:link>
											</c:if>
										</easy:hasAuthorization>
									</div>
								</td>
							</c:if>
							
							<%-- Steuerelemente zum Bündeln/Entbündeln von Gleissperrungen --%>
							<td style="${width_ctrlBuendel}"><div class="fixedSizeCell rowHeight" style="${width_ctrlBuendel};height:${currentLineHeight}px">
								<easy:hasAuthorization model="${currentGleissperrung}" authorization="ROLE_GLEISSPERRUNG_BEARBEITEN">
									<c:if test="${fn:length(currentGleissperrung.buendel) == 0}">
										<html:multibox property="gleissperrungenIds" value="${currentGleissperrung.id}" styleClass="checkbox"
											styleId="checkbox_${currentGleissperrung.id}" />
									</c:if>
									<c:if test="${fn:length(currentGleissperrung.buendel) > 0}">
										<c:choose>
											<c:when test="${streckenbandViewType == 'VIEW'}">
												<logic:iterate id="currentBuendel" name="currentGleissperrung" property="buendel" indexId="index">
													<logic:equal name="currentBuendel" property="fixiert" value="false">
														<easy:hasAuthorization model="${currentBuendel}" authorization="ROLE_BUENDEL_GLEISSPERRUNG_ZUORDNEN">
															<html:link action="/osb/streckenband/edit" styleClass="undo">
																<html:param name="method" value="removeFromBuendel" />
																<html:param name="gleissperrungId" value="${currentGleissperrung.id}" />
																<html:param name="buendelId" value="${currentBuendel.id}" />
																&nbsp;${currentBuendel.buendelId}
															</html:link>
														</easy:hasAuthorization>
													</logic:equal>
													<c:if test="${ (fn:length(currentGleissperrung.buendel) > (index+1)) }"><br /></c:if>
												</logic:iterate>
											</c:when>
											<c:otherwise>
											<html:multibox property="gleissperrungenIds" value="${currentGleissperrung.id}" styleClass="checkbox"
												styleId="checkbox_${currentGleissperrung.id}" />
											</c:otherwise>
										</c:choose>
									</c:if>
								</easy:hasAuthorization>
							</div></td>
						</tr>
					</c:forEach>
					
					<c:remove var="currentLineHeight" />
					<c:remove var="currentGleissperrung" />
					<c:remove var="currentMassnahme" />
					<c:remove var="rowClass" />
				</table>
			</c:when>
			<c:otherwise>
				<div class="nodata">keine Daten</div>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="buttonBar" id="streckenband_ButtonBar">
		<c:if test="${streckenbandViewType == 'VIEW'}">
			<html:link action="/osb/streckenband/search" styleClass="buttonBack"><bean:message key="streckenband.close" /></html:link>
		
			<html:link action="/osb/streckenband/edit" styleClass="buttonReload">
				<html:param name="method" value="reset" />
				<html:param name="vzgStreckeId" value="${streckeVzg.id}" />
				<bean:message key="button.reset" />
			</html:link>
		
			<c:choose>
				<c:when test="${isFilterApplied == null || isFilterApplied == false}">
					<c:set var="filterTitle"><bean:message key='streckenband.filter' /></c:set>	
					<html:link action="/osb/streckenband/search?streckeVZG=${streckeVzg.id}" styleClass="buttonSearch" onclick="Modalbox.show($('streckenbandFilterDialog'), {title: '${filterTitle}', inactiveFade:false});return false;">
						<bean:message key="streckenband.filter" />
					</html:link>
				</c:when>
				<c:otherwise>
					<html:link action="/osb/streckenband/show" styleClass="buttonCancel">
						<html:param name="vzgStreckeId" value="${streckeVzg.id}" />
						<html:param name="resetFilter" />
						<bean:message key="streckenband.filter.reset" />
					</html:link>
				</c:otherwise>
			</c:choose>
		
			<authz:authorize ifAnyGranted="ROLE_GLEISSPERRUNG_BEARBEITEN_ALLE, ROLE_GLEISSPERRUNG_BEARBEITEN_REGIONALBEREICH, ROLE_GLEISSPERRUNG_BEARBEITEN_TEMPORAER, ROLE_BUENDEL_ANLEGEN_ALLE, ROLE_BUENDEL_ANLEGEN_REGIONALBEREICH">
				<html:link href="#" onclick="$('streckenbandEditForm').submit()"
					styleClass="buttonAdd" styleId="buttonAdd">ausgewählte Gleissperrungen bündeln</html:link>
			</authz:authorize>
		</c:if>

		<c:if test="${streckenbandViewType == 'PROCESSBUENDEL'}">
			<html:link action="/back" styleClass="buttonBack">
				<bean:message key="button.back" />
			</html:link>
			
			<html:link action="/osb/streckenband/edit" styleClass="buttonReload">
				<html:param name="method" value="reset" />
				<html:param name="vzgStreckeId" value="${streckeVzg.id}" />
				<html:param name="buendelId" value="${buendel.id}" />
				<bean:message key="button.reset" />
			</html:link>
			
			<easy:hasAuthorization model="${buendel}" authorization="ROLE_BUENDEL_GLEISSPERRUNG_ZUORDNEN">
				<html:link href="#" onclick="$('streckenbandEditForm').submit()" styleClass="buttonSave">
					Gleissperrung zu Bündel hinzufügen
				</html:link>
			</easy:hasAuthorization>
		</c:if>
	</div>
</html:form>

<div ID="streckenbandFilterDialog" style="display:none;">
	<html:form action="/osb/streckenband/show">
		<table class="colored" style="width:460px;">
			<colgroup>
				<col width="10" />
				<col width="*" />
			</colgroup>
			<tr><th></th><th>Gewerk</th></tr>
			<tr><td><html:multibox property="gewerke" value="" styleClass="checkbox"></html:multibox></td><td>(leer)</td></tr>
			<c:forEach var="item" items="${gewerkeList}">
				<c:if test="${!empty item }">
					<tr><td><html:multibox property="gewerke" value="${item}" styleClass="checkbox"></html:multibox></td><td>${item}</td></tr>
				</c:if>
			</c:forEach>
		</table>
		
		<div class="buttonBar" style="width:438px;">
			<img src="<c:url value='/static/img/wait.gif' />" id="updateIndicator" style="display:none;width:15px;height:15px;margin:-5px;" />
			<html:hidden property="vzgStreckeId" />
			<html:link href="#" onclick="$('updateIndicator').show();Modalbox.deactivate();$('streckenbandEditForm').submit();return false;" styleClass="buttonSearch">
				suchen
			</html:link>
			
			<html:link action="/osb/streckenband/show" styleClass="buttonCancel" onclick="Modalbox.hide();return false;">
				schließen
			</html:link>
		</div>
	</html:form>
</div>

</body>

<%-- Script zum synchronisierten Scrollen der Streckenband DIVs --%>
<script type="text/javascript">
	function markRow(tr, id) {
		var checkbox = "checkbox_" + id; 
		var tr_left = $('row_left_'+id);
		var tr_body = $('row_body_'+id);
		
		<c:choose>
			<c:when test="${streckenbandViewType == 'VIEW'}">
				if(tr.className.indexOf("activerow") == -1){
					//tr.className += " activerow";
					tr_left.className += " activerow";
					tr_body.className += " activerow";
					$(checkbox).checked = true;
				}
				else{
					//tr.className = tr.className.replace(/ activerow/g, "");
					tr_left.className = tr_left.className.replace(/ activerow/g, "");
					tr_body.className = tr_body.className.replace(/ activerow/g, "");
					$(checkbox).checked = false;			
				}
			</c:when>
			<c:otherwise>
				if ($(checkbox) != null)
				{
					if(tr.className.indexOf("activerow") == -1){
						//tr.className += " activerow";
						tr_left.className += " activerow";
						tr_body.className += " activerow";
						$(checkbox).checked = true;
					}
					else{
						//tr.className = tr.className.replace(/ activerow/g, "");
						tr_left.className = tr_left.className.replace(/ activerow/g, "");
						tr_body.className = tr_body.className.replace(/ activerow/g, "");
						$(checkbox).checked = false;			
					}	
				}
			</c:otherwise>
		</c:choose>
	}
	
	function scrollHandler(args) {
	    $('streckenband_Top').scrollLeft = $('streckenband_Body').scrollLeft;
	    $('streckenband_Left').scrollTop = $('streckenband_Body').scrollTop;
	}
	
	function generateDetailPopup(element_id, id) {
		<%-- Protokollierung der action durch BackRequestProcessor unterdrücken (sp=true) --%>
		var action = "<c:url value='/osb/streckenband/refreshPopupDetails.do' />?sp=true&id="+id;
		
		new Tip(element_id, {
			  ajax: {
			    url: action,
				evalScripts: true
			  }
		});
	}
	
	<!-- Event.observe($('streckenband_Body'), 'scroll', scrollHandler ); -->

	$j('#streckenband_Body').scroll(scrollHandler);
	
	$$('div#streckenband_Body input.checkbox').toArray().each(function(item) {
		// Zeilen markieren, wenn Checkbox angekreuzt ist
<!--		if(item.checked) {-->
<!--			var id = item.id.replace(/checkbox_/g, "");-->
<!--			var row = $('row_left_'+id);-->
<!--			markRow(row, id);-->
<!--		}-->
		
		var id = item.id.replace(/checkbox_/g, "");
		var row = $('row_left_'+id);
		
		<c:choose>
			<c:when test="${streckenbandViewType == 'VIEW'}">
				if(item.checked == true) {
					markRow(row, id);
				}
			</c:when>
			<c:otherwise>
				if ( item.disabled == true) {
					item.className += " disabledrow";
					$('row_left_'+id).className += " disabledrow";
					$('row_body_'+id).className += " disabledrow";
				
			}
			else if(item.checked == true){
				markRow(row, id);
			}
			</c:otherwise>
		</c:choose>
		
	});

	$j(document).ready(function() {
		<c:forEach var="row" items="${streckenband}">
			$j('#row_left_${row.gleissperrung.id}').mouseenter(function() {generateDetailPopup('row_left_${row.gleissperrung.id}', ${row.gleissperrung.id});});
			$j('#row_body_${row.gleissperrung.id}').mouseenter(function() {generateDetailPopup('row_body_${row.gleissperrung.id}', ${row.gleissperrung.id});});
		</c:forEach>
	});
</script>