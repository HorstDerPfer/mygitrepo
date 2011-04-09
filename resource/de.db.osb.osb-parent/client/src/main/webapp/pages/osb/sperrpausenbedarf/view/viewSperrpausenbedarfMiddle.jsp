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
		<div class="label"><bean:message key="sperrpausenbedarf.betriebsstelle.von.koordiniert" /></div>
		<div class="show">
			<c:if test="${baumassnahme.betriebsstelleVonKoordiniert != null}">
				<bean:write name="baumassnahme" property="betriebsstelleVonKoordiniert.captionWithID" />
			</c:if>
		</div>
	</div>
	<div class="box">
		<div class="label"><bean:message key="sperrpausenbedarf.bauterminStart" />*</div>
		<div class="show center" style="width:90px;"><bean:write name="baumassnahme" property="bauterminStart" format="dd.MM.yyyy, HH:mm" /></div>
		<div class="label" style="margin-left:46px;width:100px;"><bean:message key="sperrpausenbedarf.bauterminEnde" />*</div>
		<div class="show center" style="width:90px;"><bean:write name="baumassnahme" property="bauterminEnde" format="dd.MM.yyyy, HH:mm" /></div>
	</div>
	<div class="box">
		<div class="label"><bean:message key="sperrpausenbedarf.bauterminStartKoordiniert" /></div>
		<div class="show center" style="width:90px;"><bean:write name="baumassnahme" property="bauterminStartKoordiniert" format="dd.MM.yyyy, HH:mm" /></div>
		<div class="label" style="margin-left:46px;width:100px;"><bean:message key="sperrpausenbedarf.bauterminEndeKoordiniert" /></div>
		<div class="show center" style="width:90px;"><bean:write name="baumassnahme" property="bauterminEndeKoordiniert" format="dd.MM.yyyy, HH:mm" /></div>
	</div>
	<div class="box" style="height:auto;">
		<div class="label"><bean:message key="sperrpausenbedarf.kommentar" /></div>
		<div class="show"><bean:write name="baumassnahme" property="kommentar" /></div>
	</div>
</div>
<div class="textcontent_right">
	<div class="box">
		<div class="label"><bean:message key="sperrpausenbedarf.betriebsstelle.bis.koordiniert" /></div>
		<div class="show">
			<c:if test="${baumassnahme.betriebsstelleBisKoordiniert != null}">
				<bean:write name="baumassnahme" property="betriebsstelleBisKoordiniert.captionWithID" />
			</c:if>
		</div>
	</div>
	<div class="box">
		<div class="label"><bean:message key="sperrpausenbedarf.durchgehend" /></div>
		<div class="show center" style="width:25px;">
			<c:choose>
				<c:when test="${baumassnahme.durchgehend == true}"><bean:message key="common.ja" /></c:when>
				<c:otherwise><bean:message key="common.nein" /></c:otherwise>
			</c:choose>
		</div>
		<div class="label" style="margin-left:141px;"><bean:message key="sperrpausenbedarf.schichtweise" /></div>
		<div class="show center" style="width:25px;">
			<c:choose>
				<c:when test="${baumassnahme.schichtweise == true}"><bean:message key="common.ja" /></c:when>
				<c:otherwise><bean:message key="common.nein" /></c:otherwise>
			</c:choose>
		</div>
	</div>
	<div class="box">
		<div class="label"><bean:message key="sperrpausenbedarf.verkehrstageregelung" />*</div>
		<div class="show">
			<c:if test="${baumassnahme.vtr != null}">
				<bean:write name="baumassnahme" property="vtr.caption" />
			</c:if>
		</div>
	</div>
	<div class="box" style="height:auto;">
		<div class="label"><bean:message key="sperrpausenbedarf.kommentarKoordination" /></div>
		<div class="show"><bean:write name="baumassnahme" property="kommentarKoordination" /></div>
	</div>
</div>

<hr/>
