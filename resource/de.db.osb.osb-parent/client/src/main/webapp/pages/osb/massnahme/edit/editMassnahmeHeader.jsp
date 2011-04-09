<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<html:xhtml />

<div class="textcontent_head">
	<bean:message key="massnahme" />:
	<c:if test="${massnahme.phase != null}">
		<bean:write name="massnahme" property="phase.kuerzel" />&nbsp;
	</c:if>
	<c:if test="${massnahme.hauptStrecke != null}">
		<bean:write name="massnahme" property="hauptStrecke.nummer" />
	</c:if>
	<c:if test="${massnahme.betriebsstelleVon != null}">
		von
		<bean:write name="massnahme" property="betriebsstelleVon.name" />
	</c:if>
	<c:if test="${massnahme.betriebsstelleBis != null}">
		bis
		<bean:write name="massnahme" property="betriebsstelleBis.name" />
	</c:if>
</div>

<div class="textcontent">
	<div class="textcontent_left">
		<div class="box">
			<div class="label"><bean:message key="common.zeitraum" /></div>
			<div class="show">
				<c:if test="${massnahme.bauterminStart != null}">
					<bean:write name="massnahme" property="bauterminStart" format="dd.MM.yyyy HH:mm" />
				</c:if>
				<c:if test="${massnahme.bauterminStart != null && massnahme.bauterminEnde != null}">
					-
				</c:if>
				<c:if test="${massnahme.bauterminEnde != null}">
					<bean:write name="massnahme" property="bauterminEnde" format="dd.MM.yyyy HH:mm" />
				</c:if>
			</div>
		</div>
	</div>
	<div class="textcontent_right">
		<div class="box">
			<div class="label"><bean:message key="sperrpausenbedarf.arbeiten" /></div>
			<div class="show">
				<c:if test="${massnahme.arbeiten != null}">
					<bean:write name="massnahme" property="arbeiten.caption" />
				</c:if>
			</div>
		</div>
	</div>
</div>