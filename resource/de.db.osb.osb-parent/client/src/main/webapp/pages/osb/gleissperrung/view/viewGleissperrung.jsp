<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
<html:xhtml/>

<jsp:include page="/pages/main_head.jsp"/>
<jsp:include page="/pages/main_path.jsp"/>
<jsp:include page="/pages/main_menu.jsp"/>
		
<script type="text/javascript">
    openMainMenu('navLink_osb_workflow-gleissperrung');
</script>

<jsp:useBean id="urls" class="java.util.HashMap" />

<div class="textcontent_head">
	<span style="float:left"><bean:message key="gleissperrung" /></span>
	<c:if test="${gleissperrung != null}">
		<c:if test="${gleissperrung.bezeichnung != null}">
			<span style="float:left">: ${gleissperrung.bezeichnung}</span>
		</c:if>
		<span style="float:right;">
			<c:if test="${gleissperrung.deleted == true}">
				<bean:message key="common.deleted" />&nbsp;-&nbsp;
			</c:if>
			<bean:message key="common.lastchangedate" />:&nbsp;
			<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${gleissperrung.lastChangeDate}" />
		</span>
	</c:if>
</div>

<div class="textcontent">
	<div class="textcontent_left">
		<div class="box">
			<div class="label"><bean:message key="gleissperrung.lfd" /></div>
			<div class="show">${gleissperrung.lfdNr}</div>
		</div>
		<div class="box">
			<div class="label"><bean:message key="regelung.fahrplanjahr" /></div>
			<div class="show" style="width:25px;">${gleissperrung.fahrplanjahr}</div>
		</div>
		<div class="box">
			<div class="label"><bean:message key="gleissperrung.betriebsweise" /></div>
			<div class="show">${gleissperrung.betriebsweise.name}</div>
		</div>
		<div class="box">
			<div class="label"><bean:message key="gleissperrung.sperrpausenbedarf" /></div>
			<div class="show" style="width:150px;"><bean:write name="gleissperrung" property="sperrpausenbedarf" /></div>
		</div>
		<div class="box">
			<div class="label"><bean:message key="gleissperrung.auswirkung" /></div>
			<div class="show">${gleissperrung.auswirkung}</div>
		</div>
		<div class="box">
			<div class="label"><bean:message key="gleissperrung.bauLue" /></div>
			<div class="show" style="width:30px;">${gleissperrung.bauLue}</div>
		</div>
		<div class="box" style="height:auto;">
			<div class="label"><bean:message key="regelung.kommentar" /></div>
			<div class="show" style="height:60px;overflow:auto;">${gleissperrung.kommentar}</div>
		</div>
	</div>
	
	<div class="textcontent_right">
		<div class="box">
			<div class="label"><bean:message key="regelung.vzgStrecke" /></div>
			<div class="show">${gleissperrung.vzgStrecke.nummer}</div>
		</div>
		<div class="box">
			<div class="label"><bean:message key="gleissperrung.richtungsKennzahl" /></div>
			<div class="show">
				<c:if test="${!empty gleissperrung.richtungsKennzahl}">
					<bean:message key="gleissperrung.richtungsKennzahl.${gleissperrung.richtungsKennzahl}" />
				</c:if>
			</div>
		</div>
		<div class="box">
			<div class="label"><bean:message key="regelung.bstVon" /></div>
			<div class="show"><c:if test="${gleissperrung.bstVon != null}">${gleissperrung.bstVon.caption}</c:if></div>
		</div>
		<div class="box">
			<div class="label"><bean:message key="regelung.bstBis" /></div>
			<div class="show"><c:if test="${gleissperrung.bstBis != null}">${gleissperrung.bstBis.caption}</c:if></div>
		</div>
		<div class="box">
			<div class="label"><bean:message key="gleissperrung.kmVon" /></div>
			<div class="show" style="width:80px;">
				<fmt:formatNumber value="${gleissperrung.kmVon}" minFractionDigits="2" maxFractionDigits="4"></fmt:formatNumber>
			</div>
		</div>
		<div class="box">
			<div class="label"><bean:message key="gleissperrung.kmBis" /></div>
			<div class="show" style="width:80px;">
				<fmt:formatNumber value="${gleissperrung.kmBis}" minFractionDigits="2" maxFractionDigits="4"></fmt:formatNumber>
			</div>
		</div>
		<div class="box">
			<div class="label"><bean:message key="gleissperrung.sigWeicheVon" /></div>
			<div class="show">${gleissperrung.sigWeicheVon}</div>
		</div>
		<div class="box">
			<div class="label"><bean:message key="gleissperrung.sigWeicheBis" /></div>
			<div class="show">${gleissperrung.sigWeicheBis}</div>
		</div>
		<div class="box">
			<div class="label"><bean:message key="regelung.bstVonKoordiniert" /></div>
			<div class="show"><c:if test="${gleissperrung.bstVonKoordiniert != null}">${gleissperrung.bstVonKoordiniert.caption}</c:if></div>
		</div>
		<div class="box">
			<div class="label"><bean:message key="regelung.bstBisKoordiniert" /></div>
			<div class="show"><c:if test="${gleissperrung.bstBisKoordiniert != null}">${gleissperrung.bstBisKoordiniert.caption}</c:if></div>
		</div>
	</div>
</div>
<div class="buttonBar">
	<html:link action="/back.do" styleClass="buttonBack"><bean:message key="button.back" /></html:link>
	
	<easy:hasAuthorization model="${gleissperrung}" authorization="ROLE_GLEISSPERRUNG_BEARBEITEN">
		<html:link action="/osb/editGleissperrung" styleClass="buttonEdit">
			<bean:message key="button.edit" />
			<html:param name="gleissperrungId" value="${gleissperrung.id}"></html:param>
		</html:link>
	</easy:hasAuthorization>
</div>
<br />

<%-- Karteireiter --------------------------------------------------------------------------------------------------- --%>
<input type="hidden" id="tab" name="tab"
	value="${tab == null ? 'Terminplanung' : tab}" />
<c:set var="backButtonLink" value="/osb/editBuendel.do?buendelId=${buendel.id}" />
<html:link href="#" onclick="javascript:showTabDiv('Terminplanung');setBackButtonLink('${backButtonLink}', 'Terminplanung');" styleId="tabLinkTerminplanung" styleClass="tab_act"><bean:message key="gleissperrung.tab.terminplanung" /></html:link>
<html:link href="#" onclick="javascript:showTabDiv('Baustelle');setBackButtonLink('${backButtonLink}', 'Baustelle');" styleId="tabLinkBaustelle" styleClass="tab_ina"><bean:message key="gleissperrung.tab.baustelle" /></html:link>
<html:link href="#" onclick="javascript:showTabDiv('Buendel');setBackButtonLink('${backButtonLink}', 'Buendel');" styleId="tabLinkBuendel" styleClass="tab_ina"><bean:message key="gleissperrung.tab.buendel" /></html:link>
<html:link href="#" onclick="javascript:showTabDiv('Massnahmen');setBackButtonLink('${backButtonLink}', 'Massnahmen');" styleId="tabLinkMassnahmen" styleClass="tab_ina"><bean:message key="gleissperrung.tab.massnahmen" /></html:link>

<div class="textcontent" id="tabDivTerminplanung" style="display: none;">
	<div class="textcontent_left">
		<div class="box">
			<div class="label"><bean:message key="regelung.zeitVon" /></div>
			<div class="show" style="width:85px;"><fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${gleissperrung.zeitVon}" /></div>
			<div class="label" style="margin-left:77px;width:79px;"><bean:message key="regelung.zeitVonKoordiniert.short" /></div>
			<div class="show" style="width:85px;"><fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${gleissperrung.zeitVonKoordiniert}" /></div>
		</div>
		<div class="box">
			<div class="label"><bean:message key="regelung.zeitBis" /></div>
			<div class="show" style="width:85px;"><fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${gleissperrung.zeitBis}" /></div>
			<div class="label" style="margin-left:77px;width:79px;"><bean:message key="regelung.zeitBisKoordiniert.short" /></div>
			<div class="show" style="width:85px;"><fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${gleissperrung.zeitBisKoordiniert}" /></div>
		</div>
		<div class="box">
			<div class="label"><bean:message key="regelung.durchgehend" /></div>
			<div class="show" style="width:30px;">
				<c:if test="${gleissperrung.durchgehend != null}">
					<c:if test="${gleissperrung.durchgehend == true}"><bean:message key="common.ja" /></c:if>
					<c:if test="${gleissperrung.durchgehend == false}"><bean:message key="common.nein" /></c:if>
				</c:if>
			</div>
			<div class="label" style="margin-left:132px;width:79px;"><bean:message key="regelung.schichtweise" /></div>
			<div class="show" style="width:30px;">
				<c:if test="${gleissperrung.schichtweise != null}">
					<c:if test="${gleissperrung.schichtweise == true}"><bean:message key="common.ja" /></c:if>
					<c:if test="${gleissperrung.schichtweise == false}"><bean:message key="common.nein" /></c:if>
				</c:if>
			</div>
		</div>
		<div class="box">
			<div class="label"><bean:message key="vtr" /></div>
			<div class="show">${gleissperrung.vtr.caption}</div>
		</div>
	</div>
</div>

<div class="textcontent center" id="tabDivBaustelle" style="display:none;">
	<jsp:include page="../edit/editGleissperrungBaustelle.jsp" />
</div>

<div class="textcontent center" id="tabDivBuendel" style="display:none;">
	<jsp:include page="../edit/editGleissperrungBuendel.jsp" />
</div>

<div class="textcontent center" id="tabDivMassnahmen" style="display:none;">
	<jsp:include page="../edit/editGleissperrungMassnahmen.jsp" />
</div>

<script type="text/javascript">
	function setBackButtonLink(link, tab) {
		var e = $('backButton');
		if(e != null) {
			e.href = link + "&tab=" + tab;
		}
	}
	showTabDiv("${ ((tab != null) ? tab : 'Terminplanung') }");
</script>

<jsp:include page="/pages/main_footer.jsp"/>