<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>

<div class="textcontent_left">
	<div class="box">
		<div class="label"><bean:message key="paket" /></div>
		<div class="show">
			<c:if test="${baumassnahme.paket != null}">
				<bean:write name="baumassnahme" property="paket.paketId" />
			</c:if>
		</div>
	</div>
	<div class="box">
		<div class="label"><bean:message key="sperrpausenbedarf.technischerPlatz" /></div>
		<div class="show"><bean:write name="baumassnahme" property="technischerPlatz" /></div>
	</div>
	<div class="box">
		<div class="label"><bean:message key="sperrpausenbedarf.bauverfahren" /></div>
		<div class="show"><bean:write name="baumassnahme" property="bauverfahren" /></div>
	</div>
	<div class="box">
		<div class="label"><bean:message key="sperrpausenbedarf.pspElement" /></div>
		<div class="show"><bean:write name="baumassnahme" property="pspElement" /></div>
	</div>
</div>
<div class="textcontent_right">
	<div class="box">
		<div class="label"><bean:message key="sperrpausenbedarf.gewerk" />*</div>
		<div class="show"><bean:write name="baumassnahme" property="gewerk" /></div>
	</div>
	<div class="box">
		<div class="label"><bean:message key="sperrpausenbedarf.untergewerk" />*</div>
		<div class="show"><bean:write name="baumassnahme" property="untergewerk" /></div>
	</div>
	<div class="box">
		<div class="label"><bean:message key="sperrpausenbedarf.weichenGleisnummerBfGleisen.short" /></div>
		<div class="show"><bean:write name="baumassnahme" property="weichenGleisnummerBfGleisen" /></div>
	</div>
	<div class="box">
		<div class="label"><bean:message key="sperrpausenbefarf.weichenbauform" /></div>
		<div class="show"><bean:write name="baumassnahme" property="weichenbauform" /></div>
	</div>
</div>

<hr/>

<div class="textcontent_left">
	<div class="box">
		<div class="label"><bean:message key="sperrpausenbedarf.bahnsteige" /></div>
		<div class="show" style="width:25px;">
			<c:choose>
				<c:when test="${baumassnahme.bahnsteige == true}"><bean:message key="sperrpausenbedarf.bahnsteige.true" /></c:when>
				<c:otherwise><bean:message key="sperrpausenbedarf.bahnsteige.false" /></c:otherwise>
			</c:choose>
		</div>
	</div>
	<div class="box">
		<div class="label"><bean:message key="sperrpausenbedarf.einbauPss" /></div>
		<div class="show" style="width:25px;">
			<c:choose>
				<c:when test="${baumassnahme.einbauPss == true}"><bean:message key="sperrpausenbedarf.einbauPss.true" /></c:when>
				<c:otherwise><bean:message key="sperrpausenbedarf.einbauPss.false" /></c:otherwise>
			</c:choose>
		</div>
	</div>
	<div class="box">
		<div class="label"><bean:message key="sperrpausenbedarf.kabelkanal" /></div>
		<div class="show" style="width:25px;">
			<c:choose>
				<c:when test="${baumassnahme.kabelkanal == true}"><bean:message key="sperrpausenbedarf.kabelkanal.true" /></c:when>
				<c:otherwise><bean:message key="sperrpausenbedarf.kabelkanal.false" /></c:otherwise>
			</c:choose>
		</div>
	</div>
	<div class="box">
		<div class="label"><bean:message key="sperrpausenbedarf.oberleitungsAnpassung" /></div>
		<div class="show" style="width:25px;">
			<c:choose>
				<c:when test="${baumassnahme.oberleitungsAnpassung == true}"><bean:message key="sperrpausenbedarf.oberleitungsAnpassung.true" /></c:when>
				<c:otherwise><bean:message key="sperrpausenbedarf.oberleitungsAnpassung.false" /></c:otherwise>
			</c:choose>
		</div>
	</div>
	<div class="box">
		<div class="label"><bean:message key="sperrpausenbedarf.lst" /></div>
		<div class="show" style="width:25px;">
			<c:choose>
				<c:when test="${baumassnahme.lst == true}"><bean:message key="sperrpausenbedarf.lst.true" /></c:when>
				<c:otherwise><bean:message key="sperrpausenbedarf.lst.false" /></c:otherwise>
			</c:choose>
		</div>
	</div>
</div>
<div class="textcontent_right">
	<div class="box">
		<div class="label"><bean:message key="sperrpausenbedarf.geplanteNennleistung" /></div>
		<div class="show right" style="width:50px;"><fmt:formatNumber value="${baumassnahme.geplanteNennleistung}" /></div>
	</div>
	<div class="box">
		<div class="label"><bean:message key="sperrpausenbedarf.notwendigeLaengePss" /></div>
		<div class="show right" style="width:50px;"><fmt:formatNumber value="${baumassnahme.notwendigeLaengePss}" /></div>
	</div>
	<div class="box">
		<div class="label"><bean:message key="sperrpausenbedarf.umbaulaenge" /></div>
		<div class="show right" style="width:50px;"><fmt:formatNumber value="${baumassnahme.umbaulaenge}" /></div>
	</div>
	<div class="box">
		<div class="label"><bean:message key="sperrpausenbedarf.tiefentwaesserung.lage" /></div>
		<div class="show" style="width:50px;">
			<c:choose>
				<c:when test="${tiefentwaesserung.lage != null}">
					<bean:message key="tiefentwaesserung.lage.${tiefentwaesserung.lage}" />
				</c:when>
				<c:otherwise><bean:message key="common.ohne" /></c:otherwise>
			</c:choose>
		</div>
	</div>
</div>

<br/>