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
<%@ taglib uri="/META-INF/util.tld" prefix="util"%>

<display:column title="" sortable="false" style="padding-right:0px;${blueColoredText}" media="html">
	<c:if test="${item.massnahme != null && item.massnahme.genehmiger != null}"><img src="<c:url value='/static/img/icon_lock.gif' />" title="${titleFixiert}" /></c:if>
	<c:if test="${item.deleted == true}"><img src="<c:url value='/static/img/error.png' />" title="${titleDeleted}" /></c:if>
</display:column>
<display:column style="${blueColoredText}" title="" media="excel">
	<c:if test="${item.massnahme != null && item.massnahme.genehmiger != null}">${titleFixiert}</c:if>
	<c:if test="${item.deleted == true}">${titleDeleted}</c:if>
</display:column>

<display:column style="${blueColoredText}" titleKey="regionalbereich.short" sortable="false" class="center">
	<c:if test="${item.massnahme != null && item.massnahme.regionalbereich != null}">
		<bean:write name="item" property="massnahme.regionalbereich.kuerzel" />
	</c:if>
</display:column>

<display:column style="${blueColoredText}" titleKey="gleissperrung.streckeRichtungskennzahl.br" headerClass="left" class="center" sortable="false" media="html">
	<c:choose>
		<c:when test="${item.vzgStrecke != null && item.richtungsKennzahl != null}"><bean:write name="item" property="vzgStrecke.nummer" />.<bean:write name="item" property="richtungsKennzahl" /></c:when>
		<c:when test="${item.vzgStrecke != null}"><bean:write name="item" property="vzgStrecke.nummer" /></c:when>
	</c:choose>
</display:column>
<display:column style="${blueColoredText}" titleKey="gleissperrung.streckeRichtungskennzahl" class="center" media="excel">
	<c:choose>
		<c:when test="${item.vzgStrecke != null && item.richtungsKennzahl != null}"><bean:write name="item" property="vzgStrecke.nummer" />.<bean:write name="item" property="richtungsKennzahl" /></c:when>
		<c:when test="${item.vzgStrecke != null}"><bean:write name="item" property="vzgStrecke.nummer" /></c:when>
	</c:choose>
</display:column>

<display:column style="${blueColoredText}" titleKey="gleissperrung.buendelMassnahmeGleissperrung.br" headerClass="center" class="center" sortable="false" media="html">
	<!-- Buendel -->
	<c:choose>
		<c:when test="${fn:length(item.buendel) > 0}">
			<c:forEach var="b" items="${item.buendel}">
				<span style="color:grey;"><bean:message key="buendel.shortcut" /></span>${b.buendelId}
				<c:if test="${b.ankermassnahmeArt != null}"><span style="color:red">&nbsp;A</span></c:if>
				<br/>
			</c:forEach>
		</c:when>
		<c:otherwise>-<br/></c:otherwise>
	</c:choose>
	<!-- Massnahmen -->
	<c:choose>
		<c:when test="${item.massnahme != null}">
			<span style="color:grey;"><bean:message key="massnahme.shortcut" /></span>${item.massnahme.massnahmeId}
			<c:if test="${item.massnahme.ankermassnahmeArt != null}"><span style="color:red">&nbsp;A</span></c:if>
			<br/>
		</c:when>
		<c:otherwise>-<br/></c:otherwise>
	</c:choose>
	<!-- Gleissperrung -->
	<bean:write name="item" property="lfdNr" />
</display:column>
<display:column style="${blueColoredText}" titleKey="buendel" media="excel">
	<c:if test="${fn:length(item.buendel) > 0}">
		<c:forEach var="b" items="${item.buendel}">
			${b.buendelId}<c:if test="${b.ankermassnahmeArt != null}"> A</c:if>
		</c:forEach>
	</c:if>
</display:column>
<display:column style="${blueColoredText}" titleKey="massnahme" media="excel">
	<c:if test="${item.massnahme != null}">
		${item.massnahme.massnahmeId}<c:if test="${item.massnahme.ankermassnahmeArt != null}"> A</c:if>
	</c:if>
</display:column>
<display:column style="${blueColoredText}" titleKey="gleissperrung" media="excel">
	<bean:write name="item" property="lfdNr" />
</display:column>

<display:column style="${blueColoredText}" titleKey="regelung.streckenabschnitt.br" sortable="false" media="html">
	<c:if test="${item.bstVon != null}"><bean:write name="item" property="bstVon.caption" /></c:if>
	<c:if test="${item.bstVon != null && item.bstBis != null}"> -<br/></c:if>
	<c:if test="${item.bstBis != null}"><bean:write name="item" property="bstBis.caption" /></c:if>
</display:column>
<display:column style="${blueColoredText}" titleKey="regelung.streckenabschnitt" media="excel">
	<c:if test="${item.bstVon != null}"><bean:write name="item" property="bstVon.caption" /></c:if>
	<c:if test="${item.bstVon != null && item.bstBis != null}"> - </c:if>
	<c:if test="${item.bstBis != null}"><bean:write name="item" property="bstBis.caption" /></c:if>
</display:column>

<display:column style="${blueColoredText}" titleKey="regelung.streckenabschnittKoordiniert.br" sortable="false" media="html">
	<c:set var="styleStreckenabschnittKoordiniert" value="" />
	<c:if test="${item.betriebsstellenUnterschiedlich}">
		<c:set var="styleStreckenabschnittKoordiniert" value="color:blue;" />
	</c:if>
	<span style="${styleStreckenabschnittKoordiniert}">
		<c:if test="${item.bstVonKoordiniert != null}"><bean:write name="item" property="bstVonKoordiniert.caption" /></c:if>
		<c:if test="${item.bstVonKoordiniert != null && item.bstBisKoordiniert != null}"> -<br/></c:if>
		<c:if test="${item.bstBisKoordiniert != null}"><bean:write name="item" property="bstBisKoordiniert.caption" /></c:if>
	</span>
</display:column>
<display:column style="${blueColoredText}" titleKey="regelung.streckenabschnittKoordiniert" media="excel">
	<c:if test="${item.bstVonKoordiniert != null}"><bean:write name="item" property="bstVonKoordiniert.caption" /></c:if>
	<c:if test="${item.bstVonKoordiniert != null && item.bstBisKoordiniert != null}"> - </c:if>
	<c:if test="${item.bstBisKoordiniert != null}"><bean:write name="item" property="bstBisKoordiniert.caption" /></c:if>
</display:column>

<display:column style="${blueColoredText}" titleKey="regelung.zeitraum.br" sortable="false" media="html">
	<c:if test="${item.zeitVon != null || item.zeitBis != null}">
		<c:choose>
			<c:when test="${item.durchgehend && item.schichtweise}">
				<bean:write name="item" property="zeitVon" format="dd.MM.yyyy" /><br/>
				<bean:write name="item" property="zeitBis" format="dd.MM.yyyy" />
			</c:when>
			<c:otherwise>
				<bean:write name="item" property="zeitVon" format="dd.MM.yyyy, HH:mm" /><br/>
				<bean:write name="item" property="zeitBis" format="dd.MM.yyyy, HH:mm" />
			</c:otherwise>
		</c:choose>
	
		<c:choose>
			<c:when test="${item.durchgehend}">&nbsp;<bean:message key="regelung.durchgehend.short" /></c:when>
			<c:when test="${!item.durchgehend && item.schichtweise}">&nbsp;<bean:message key="regelung.schichtweise.short" /></c:when>
		</c:choose>
	
		<c:if test="${!item.durchgehend}"><br/><bean:message key="regelung.unterbrochen.short" /></c:if>
		<c:if test="${item.schichtweise}">
			<br/>
			<bean:message key="regelung.schichtweise.je" />&nbsp;<bean:write name="item" property="zeitVon" format="HH:mm" /> -
			<bean:write name="item" property="zeitBis" format="HH:mm" />
			<c:if test="${item.vtr != null}">&nbsp;<bean:write name="item" property="vtr.vtr" /></c:if>
		</c:if>
	</c:if>
</display:column>
<display:column style="${blueColoredText}" titleKey="regelung.zeitVon" property="zeitVon" format="{0,date,dd.MM.yyyy HH:mm}" media="excel" />
<display:column style="${blueColoredText}" titleKey="regelung.zeitBis" property="zeitBis" format="{0,date,dd.MM.yyyy HH:mm}" media="excel" />
<display:column style="${blueColoredText}" titleKey="regelung.durchgehend.short" property="durchgehend" media="excel" />
<display:column style="${blueColoredText}" titleKey="regelung.schichtweise.short" property="schichtweise" media="excel" />
<display:column style="${blueColoredText}" titleKey="vtr.short" media="excel">
	<c:if test="${item.vtr != null}"><bean:write name="item" property="vtr.vtr" /></c:if>
</display:column>

<display:column style="${blueColoredText}" titleKey="regelung.zeitraumKoordiniert.br" sortable="false" media="html">
	<c:if test="${item.zeitVonKoordiniert != null || item.zeitBisKoordiniert != null}">
		<c:set var="styleZeitraumKoordiniert" value="" />
		<c:if test="${item.zeitraeumeUnterschiedlich}">
			<c:set var="styleZeitraumKoordiniert" value="color:blue;" />
		</c:if>
		<span style="${styleZeitraumKoordiniert}">
			<c:choose>
				<c:when test="${item.durchgehend && item.schichtweise}">
					<bean:write name="item" property="zeitVonKoordiniert" format="dd.MM.yyyy" /><br/>
					<bean:write name="item" property="zeitBisKoordiniert" format="dd.MM.yyyy" />
				</c:when>
				<c:otherwise>
					<bean:write name="item" property="zeitVonKoordiniert" format="dd.MM.yyyy, HH:mm" /><br/>
					<bean:write name="item" property="zeitBisKoordiniert" format="dd.MM.yyyy, HH:mm" />
				</c:otherwise>
			</c:choose>
		
			<c:choose>
				<c:when test="${item.durchgehend}">&nbsp;<bean:message key="regelung.durchgehend.short" /></c:when>
				<c:when test="${!item.durchgehend && item.schichtweise}">&nbsp;<bean:message key="regelung.schichtweise.short" /></c:when>
			</c:choose>
		
			<c:if test="${!item.durchgehend}"><br/><bean:message key="regelung.unterbrochen.short" /></c:if>
			<c:if test="${item.schichtweise}">
				<br/>
				<bean:message key="regelung.schichtweise.je" />&nbsp;<bean:write name="item" property="zeitVonKoordiniert" format="HH:mm" /> -
				<bean:write name="item" property="zeitBisKoordiniert" format="HH:mm" />
				<c:if test="${item.vtr != null}">&nbsp;<bean:write name="item" property="vtr.vtr" /></c:if>
			</c:if>
		</span>
	</c:if>
</display:column>
<display:column style="${blueColoredText}" titleKey="regelung.zeitVonKoordiniert" property="zeitVonKoordiniert" format="{0,date,dd.MM.yyyy HH:mm}" media="excel" />
<display:column style="${blueColoredText}" titleKey="regelung.zeitBisKoordiniert" property="zeitBisKoordiniert" format="{0,date,dd.MM.yyyy HH:mm}" media="excel" />

<display:column style="${blueColoredText}" titleKey="gleissperrung.sperrpausenbedarf.br" sortable="false" media="html">
	<c:choose>
		<c:when test="${!empty item.sperrpausenbedarfEsp}">
			<bean:message key="sperrpausenbedarf.art.ESP" />
		</c:when>
		<c:when test="${!empty item.sperrpausenbedarfTsp}">
			<bean:message key="sperrpausenbedarf.art.TSP" />
		</c:when>
		<c:when test="${!empty item.sperrpausenbedarfBfGl}">
			<bean:message key="sperrpausenbedarf.art.SPERR_BF_GL" />
		</c:when>
	</c:choose>
</display:column>
<display:column style="${blueColoredText}" titleKey="gleissperrung.sperrpausenbedarf" media="excel">
	<c:choose>
		<c:when test="${!empty item.sperrpausenbedarfEsp}">
			<bean:message key="sperrpausenbedarf.art.ESP" />
		</c:when>
		<c:when test="${!empty item.sperrpausenbedarfTsp}">
			<bean:message key="sperrpausenbedarf.art.TSP" />
		</c:when>
		<c:when test="${!empty item.sperrpausenbedarfBfGl}">
			<bean:message key="sperrpausenbedarf.art.SPERR_BF_GL" />
		</c:when>
	</c:choose>
</display:column>

<display:column style="${blueColoredText}" titleKey="common.unit.hours.short" class="right" headerClass="right" sortable="false">
	<c:choose>
		<c:when test="${!empty item.sperrpausenbedarfEsp}">
			<bean:write name="item" property="sperrpausenbedarfEsp" format="#,##0" />
		</c:when>
		<c:when test="${!empty item.sperrpausenbedarfTsp}">
			<bean:write name="item" property="sperrpausenbedarfTsp" format="#,##0" />
		</c:when>
		<c:when test="${!empty item.sperrpausenbedarf}">
			<bean:write name="item" property="sperrpausenbedarfBfGl" format="#,##0" />
		</c:when>
	</c:choose>
</display:column>

<display:column titleKey="buendel.br" class="center" headerClass="center" style="font-weight:bold;color:red;" sortable="false" media="html">
	<%-- Auf Buendel-Seite darf die Markierung nur gesetzt werden, wenn die Gleissperrung zum angezeigten Buendel gehoert #514 --%>
	<c:choose>
		<c:when test="${buendel != null}">
			<c:if test="${util:contains(item.buendel,buendel)}"><span title='<bean:message key="buendel" />'>&#1046;</span></c:if>
		</c:when>
		<c:otherwise>
			<c:if test="${fn:length(item.buendel) > 0}"><span title='<bean:message key="buendel" />'>&#1046;</span></c:if>
		</c:otherwise>
	</c:choose>
</display:column>
<display:column style="${blueColoredText}" titleKey="buendel" media="excel">
	<c:choose>
		<c:when test="${buendel != null}">
			<c:if test="${fn:contains(item.buendel,buendel)}"><bean:message key="buendel" /></c:if>
		</c:when>
		<c:otherwise>
			<c:if test="${fn:length(item.buendel) > 0}"><bean:message key="buendel" /></c:if>
		</c:otherwise>
	</c:choose>
</display:column>





<display:column titleKey="fahrplanregelung.short" class="center" headerClass="center" style="font-weight:bold;color:red;" sortable="false" media="html">
	<c:if test="${item.fahrplanregelung != null}">
		<html:link action="/osb/fahrplanregelung/view" title="${titleView}">
		<html:param name="fahrplanregelungId" value="${item.fahrplanregelung.id}" />
	 		<span title='<bean:message key="fahrplanregelung" />'>&#1288;</span>
	 	</html:link>
	</c:if>
</display:column>
<display:column style="${blueColoredText}" titleKey="fahrplanregelung.short" media="excel">
	<c:if test="${item.fahrplanregelung != null}"><bean:message key="fahrplanregelung.short" /></c:if>
</display:column>






<display:column style="text-align:right;width:15px;padding-left:0px;" sortable="false" media="html">
	<easy:hasAuthorization model="${item}" authorization="ROLE_GLEISSPERRUNG_LESEN">
 		<html:link action="/osb/viewGleissperrung" styleClass="show" title="${titleView}">
 			<html:param name="gleissperrungId" value="${item.id}" />
	 		&nbsp;
	 	</html:link>
 	</easy:hasAuthorization>
</display:column>

<display:column style="text-align:right;width:15px;" media="html">
	<easy:hasAuthorization model="${item}" authorization="ROLE_GLEISSPERRUNG_BEARBEITEN">
 		<html:link action="/osb/editGleissperrung" styleClass="edit" title="${titleEdit}">
 			<html:param name="gleissperrungId" value="${item.id}" />
 			&nbsp;
 		</html:link>
 	</easy:hasAuthorization>
</display:column>
