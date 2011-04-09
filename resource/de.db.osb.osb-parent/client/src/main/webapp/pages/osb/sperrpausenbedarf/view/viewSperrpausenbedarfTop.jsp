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
		<div class="label"><bean:message key="sperrpausenbedarf.arbeiten" />*</div>
		<div class="show">
			<c:if test="${baumassnahme.arbeiten != null}">
				<bean:write name="baumassnahme" property="arbeiten.name" />
			</c:if>
		</div>
	</div>
</div>
<div class="textcontent_right">
	<div class="box">
		<div class="label"><bean:message key="sperrpausenbedarf.arbeitenKommentar" /></div>
		<div class="show"><bean:write name="baumassnahme" property="arbeitenKommentar" /></div>
	</div>
</div>

<hr/>

<div class="textcontent_left">
	<div class="box">
		<div class="label"><bean:message key="sperrpausenbedarf.hauptStrecke" />*</div>
		<div class="show">
			<c:if test="${baumassnahme.hauptStrecke != null}">
				<bean:write name="baumassnahme" property="hauptStrecke.caption" />
			</c:if>
		</div>
	</div>
	<div class="box">
		<div class="label"><bean:message key="sperrpausenbedarf.richtungsKennzahl" />*</div>
		<div class="show"><bean:write name="baumassnahme" property="richtungsKennzahl" /></div>
	</div>
	<div class="box">
		<div class="label"><bean:message key="sperrpausenbedarf.betriebsstelle.von" />*</div>
		<div class="show">
			<c:if test="${baumassnahme.betriebsstelleVon != null}">
				<bean:write name="baumassnahme" property="betriebsstelleVon.captionWithID" />
			</c:if>
		</div>
	</div>
</div>
<div class="textcontent_right">
	<div class="box">
		<div class="label"><bean:message key="sperrpausenbedarf.kmVon" />*</div>
		<div class="show right" style="width:50px;"><fmt:formatNumber value="${baumassnahme.kmVon}" minFractionDigits="3" maxFractionDigits="4" /></div>
	</div>
	<div class="box">
		<div class="label"><bean:message key="sperrpausenbedarf.kmBis" />*</div>
		<div class="show right" style="width:50px;"><fmt:formatNumber value="${baumassnahme.kmBis}" minFractionDigits="3" maxFractionDigits="4" /></div>
	</div>
	<div class="box">
		<div class="label"><bean:message key="sperrpausenbedarf.betriebsstelle.bis" />*</div>
		<div class="show">
			<c:if test="${baumassnahme.betriebsstelleBis != null}">
				<bean:write name="baumassnahme" property="betriebsstelleBis.captionWithID" />
			</c:if>
		</div>
	</div>
</div>
