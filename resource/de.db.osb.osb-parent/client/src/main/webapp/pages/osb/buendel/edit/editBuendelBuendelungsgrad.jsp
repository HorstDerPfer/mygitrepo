<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>

<fieldset>
	<legend><bean:message key="buendel.tab.buendelungsgrad.legend.ausgangslage" /></legend>

	<div class="textcontent_left">
		<div class="box">
			<div class="label"><bean:message key="buendel.summeSperrzeitEsp" /></div>
			<div class="show right" style="width:65px;"><bean:write name="buendel" property="summeSperrpausenbedarfEsp" format="#,##0" /></div>
			<div class="label">&nbsp;<bean:message key="common.unit.hours" /></div>
		</div>
		<div class="box">
			<div class="label"><bean:message key="buendel.summeSperrzeitTsp" /></div>
			<div class="show right" style="width:65px;"><bean:write name="buendel" property="summeSperrpausenbedarfTsp" format="#,##0" /></div>
			<div class="label">&nbsp;<bean:message key="common.unit.hours" /></div>
		</div>
		<div class="box">
			<div class="label"><bean:message key="buendel.summeSperrzeitBfGl" /></div>
			<div class="show right" style="width:65px;"><bean:write name="buendel" property="summeSperrpausenbedarfBfGl" format="#,##0" /></div>
			<div class="label">&nbsp;<bean:message key="common.unit.hours" /></div>
		</div>
		<div class="box">
			<div class="label"><label for="eiuVks"><bean:message key="buendel.eiuVks" /></label></div>
			<div class="input"><html:text property="eiuVks" styleId="eiuVks" styleClass="right" maxlength="9" style="width:61px;" errorStyle="width:61px;${errorStyle}" /></div>
			<div class="label">&nbsp;<bean:message key="common.unit.hours" /></div>
		</div>
		<div class="box">
			<div class="label"><bean:message key="buendel.summeSperrzeit" /></div>
			<div class="show right" style="width:65px;"><bean:write name="buendel" property="summeSperrpausenbedarfGesamt" format="#,##0" /></div>
			<div class="label">&nbsp;<bean:message key="common.unit.hours" /></div>
		</div>
	</div>

	<div class="textcontent_right">
		<div class="box" style="margin-top:104px;">
			<div class="label"><label for="baukostenVorBuendelung"><bean:message key="buendel.baukostenVorBuendelung" /></label></div>
			<div class="input"><html:text property="baukostenVorBuendelung" styleId="baukostenVorBuendelung" styleClass="right" maxlength="15" style="width:101px;" errorStyle="width:101px;${errorStyle}" /></div>
			<div class="label">&nbsp;<bean:message key="common.unit.eur.thousand" /></div>
		</div>
	</div>
</fieldset>

<br />

<fieldset>
	<legend><bean:message key="buendel.tab.buendelungsgrad.legend.ergebnis" /></legend>

	<div class="textcontent_left">
		<div class="box">
			<div class="label"><label for="sperrzeitbedarfBuendel"><bean:message key="buendel.sperrzeitbedarfBuendel" /></label></div>
			<div class="input"><html:text property="sperrzeitbedarfBuendel" styleId="sperrzeitbedarfBuendel" styleClass="right" maxlength="9" style="width:61px;" errorStyle="width:61px;${errorStyle}" /></div>
			<div class="label">&nbsp;<bean:message key="common.unit.hours" /></div>
		</div>
		<div class="box">
			<div class="label"><label for="sperrzeitErsparnis"><bean:message key="buendel.sperrzeitErsparnis" /></label></div>
			<div class="input"><html:text property="sperrzeitErsparnis" styleId="sperrzeitErsparnis" styleClass="right" maxlength="9" style="width:61px;" errorStyle="width:61px;${errorStyle}" /></div>
			<div class="label">&nbsp;<bean:message key="common.unit.hours" /></div>
		</div>
	</div>
	<div class="textcontent_right">
		<div class="box">
			<div class="label"><label for="baukostenNachBuendelung"><bean:message key="buendel.baukostenNachBuendelung" /></label></div>
			<div class="input"><html:text property="baukostenNachBuendelung" styleId="baukostenNachBuendelung" styleClass="right" maxlength="15" style="width:101px;" errorStyle="width:101px;${errorStyle}" /></div>
			<div class="label">&nbsp;<bean:message key="common.unit.eur.thousand" /></div>
		</div>
		<div class="box">
			<div class="label"><label for="baukostenErsparnis"><bean:message key="buendel.baukostenErsparnis" /></label></div>
			<div class="input"><html:text property="baukostenErsparnis" styleId="baukostenErsparnis" styleClass="right" maxlength="15" style="width:101px;" errorStyle="width:101px;${errorStyle}" /></div>
			<div class="label">&nbsp;<bean:message key="common.unit.eur.thousand" /></div>
		</div>
	</div>	
</fieldset>
