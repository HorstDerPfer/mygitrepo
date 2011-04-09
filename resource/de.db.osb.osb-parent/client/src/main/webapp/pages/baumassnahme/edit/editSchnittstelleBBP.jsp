<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

<table class="colored" style="text-align:center;">
	<%-- Spaltenüberschriften --%>
	<thead>
		<tr>
			<th></th>
	    	<c:if test='${baumassnahme.art == "KS"}'>
				<th class="center"><bean:message key="baumassnahme.termine.studie" /></th>
			</c:if>
			<th class="center"><bean:message key="baumassnahme.termine.anforderungbbzr" /></th>
			<c:if test='${baumassnahme.art != "B"}'>
				<th class="center"><bean:message key="baumassnahme.termine.bbp.biueentwurf.nobr" /></th>
			</c:if>
			<th class="center"><bean:message key="baumassnahme.termine.bbp.zvfentwurf.nobr" /></th>
			<c:if test='${baumassnahme.art != "B"}'>
				<th class="center"><bean:message key="baumassnahme.termine.gesamtkonzeptbbzr" /></th>
			</c:if>
			<th class="center"><bean:message key="baumassnahme.termine.bbp.zvf" /></th>
		</tr>
	</thead>
	<tbody>
		<%-- Ist-Termine --%>
		<tr class="evenrow">
			<td>Ist</td>
			<%--Studie/Grobkonzept --%>
	    	<c:if test='${baumassnahme.art == "KS"}'>
				<td class="center"><html:text property="baubetriebsplanung[0]" styleId="bbpStudieGrobkonzept" styleClass="dateShort" maxlength="10" /></td>
			</c:if>
			<%--anforderungBBZR --%>
			<td class="center"><html:text property="baubetriebsplanung[1]" styleId="bbpAnforderungBbzr" styleClass="dateShort" maxlength="10" /></td>
			<%--biUeEntwurf --%>
			<c:if test='${baumassnahme.art != "B"}'>
				<td class="center">
					<c:set var="cssClass" value="dateShort" />
					<html:text property="baubetriebsplanung[2]" styleId="bbpBiUeEntwurf" styleClass="${cssClass}" maxlength="10" />
					<br />
					<html:checkbox name="baumassnahmeForm" property="baubetriebsplanungErforderlich[2]" styleId="chk_bbpBiUeEntwurf" styleClass="checkbox" titleKey="baumassnahme.termine.required" />
					<bean:message key="baumassnahme.termine.required.short" />
				</td>
			</c:if>
			<%--zvfEntwurf --%>
			<td class="center">
				<html:text property="baubetriebsplanung[3]" styleId="bbpZvfEntwurf" styleClass="dateShort" maxlength="10" />
				<br /><html:checkbox name="baumassnahmeForm" property="baubetriebsplanungErforderlich[3]" styleId="chk_bbpZvfEntwurf" styleClass="checkbox" titleKey="baumassnahme.termine.required" />
				<bean:message key="baumassnahme.termine.required.short" />
			</td>
			<c:if test='${baumassnahme.art != "B"}'>
				<%--gesamtKonzeptBBZR --%>
				<td class="center">
					<html:text property="baubetriebsplanung[5]" styleId="bbpGesamtKonzeptBBZR" styleClass="dateShort" maxlength="10" />
					<br /><html:checkbox name="baumassnahmeForm" property="baubetriebsplanungErforderlich[5]" styleId="chk_bbpGesamtKonzeptBBZR" styleClass="checkbox" titleKey="baumassnahme.termine.required" />
					<bean:message key="baumassnahme.termine.required.short" />
				</td>
			</c:if>
			<%--zvf --%>
			<td class="center">
				<html:text property="baubetriebsplanung[7]" styleId="bbpZvf" styleClass="dateShort" maxlength="10" />
				<br /><html:checkbox name="baumassnahmeForm" property="baubetriebsplanungErforderlich[7]" styleId="chk_bbpZvf" styleClass="checkbox" titleKey="baumassnahme.termine.required" />
				<bean:message key="baumassnahme.termine.required.short" />
			</td>
		</tr>
	</tbody>
</table>
