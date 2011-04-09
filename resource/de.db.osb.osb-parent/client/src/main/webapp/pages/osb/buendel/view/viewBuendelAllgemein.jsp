<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>

<div class="textcontent_left">
	<div class="box">
		<div class="label">
			<bean:message key="buendel.id" />
			<c:catch var="ex">
				<c:set var="buendelId"><bean:write name="buendel" property="buendelId" /></c:set>
			</c:catch>
			<c:if test="${ex != null}">
				<c:set var="buendelId">---</c:set>
				&nbsp;
				<img src="<c:url value='/static/img/icon_s_info_small.gif'/>" id="tipBuendelId" style="text-align:right" />
				<script type="text/javascript">
					new Tip($('tipBuendelId'), '<bean:message key="buendel.id.tip" />');
				</script>
			</c:if>
		</div>
		<div class="show" >
			<c:out value="${buendelId}" />
			<c:remove var="buendelId" />
		</div>
	</div>

	<div class="box">
		<div class="label"><label for="buendelName"><bean:message key="buendel.name" />*</label></div>
		<div class="show"><bean:write name="buendel" property="buendelName" /></div>
	</div>

	<div class="box">
		<div class="label"><bean:message key="buendel.regionalbereich" /></div>
		<div class="show">
			<c:if test="${buendel.regionalbereich != null}">
				<bean:write name="buendel" property="regionalbereich.name" />
			</c:if>
		</div>
	</div>

	<div class="box">
		<div class="label">
			<bean:message key="sperrpausenbedarf.ankermassnahmeArt" />&nbsp;
			<img src="<c:url value="/static/img/icon_s_info_small.gif" />" title="<bean:message key="buendel.ankermassnahmeArt.info" />" />
		</div>
		<div class="show">
			<c:if test="${buendel.ankermassnahmeArt != null}">
				<bean:write name="buendel" property="ankermassnahmeArt.langname" />
			</c:if>
		</div>
	</div>

	<div class="box">
		<div class="label"><bean:message key="buendel.fixiert" /></div>
		<div class="show" style="width:22px;">
			<c:choose>
				<c:when test="${buendel.fixiert == true}"><bean:message key="common.ja" /></c:when>
				<c:otherwise><bean:message key="common.nein" /></c:otherwise>
			</c:choose>
		</div>
		<div class="label" style="margin-left:109px;"><bean:message key="buendel.fixierungsDatum" /></div>
		<div class="show center" style="width:60px;"><bean:write name="buendel" property="fixierungsDatum" format="dd.MM.yyyy" /></div>
	</div>
</div>

<div class="textcontent_right">
	<div class="box">
		<div class="label"><bean:message key="buendel.hauptStrecke" /></div>
		<div class="show">
			<c:if test="${buendel.hauptStrecke != null}">
				<bean:write name="buendel" property="hauptStrecke.caption" />
			</c:if>
		</div>
	</div>

	<div class="box">
		<div class="label"><bean:message key="buendel.startBahnhof" /></div>
		<div class="show">
			<c:if test="${buendel.startBahnhof != null}">
				<bean:write name="buendel" property="startBahnhof.caption" />
			</c:if>
		</div>
	</div>

	<div class="box">
		<div class="label"><bean:message key="buendel.endeBahnhof" /></div>
		<div class="show">
			<c:if test="${buendel.endeBahnhof != null}">
				<bean:write name="buendel" property="endeBahnhof.caption" />
			</c:if>
		</div>
	</div>

	<div class="box">
		<div class="label" style="padding-top:0px;"><bean:message key="buendel.weitereStrecken" /></div>
		<div class="show"><bean:write name="buendel" property="weitereStreckenCsv" /></div>
	</div>
	
	<div class="box">
		<div class="label"><label for="durchfuehrungsZeitraumStartKoordiniert"><bean:message key="buendel.durchfuehrungszeitraumStartKoordiniert" /></label></div>
		<div class="show center" style="width:60px;"><bean:write name="buendel" property="durchfuehrungsZeitraumStartKoordiniert" format="dd.MM.yyyy" /></div>
		<div class="label" style="margin-left:86px;width:120px;"><label for="durchfuehrungsZeitraumEndeKoordiniert"><bean:message key="buendel.durchfuehrungszeitraumEndeKoordiniert" /></label></div>
		<div class="show center" style="width:60px;"><bean:write name="buendel" property="durchfuehrungsZeitraumEndeKoordiniert" format="dd.MM.yyyy" /></div>
	</div>
</div>
