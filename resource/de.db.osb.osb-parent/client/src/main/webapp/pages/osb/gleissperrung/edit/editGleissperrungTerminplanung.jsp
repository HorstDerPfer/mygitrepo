<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>

<div class="box">
	<div class="label"><label for="zeitVon"><bean:message key="regelung.zeitVon" /></label></div>
	<div class="input">
		<html:text property="zeitVon" styleId="zeitVon" styleClass="datetime" errorStyle="${errorStyle}" />
		<img src="<c:url value='/static/img/calendar.gif' />" id="buttonZeitVon" />
	</div>
	<div class="label" style="margin-left:35px;width:79px;"><label for="zeitVonKoordiniert"><bean:message key="regelung.zeitVonKoordiniert.short" /></label></div>
	<div class="input">
		<html:text property="zeitVonKoordiniert" styleId="zeitVonKoordiniert" styleClass="datetime" errorStyle="${errorStyle}" />
		<img src="<c:url value='/static/img/calendar.gif' />" id="buttonZeitVonKoordiniert" />
	</div>
</div>
<div class="box">
	<div class="label"><label for="zeitBis"><bean:message key="regelung.zeitBis" /></label></div>
	<div class="input">
		<html:text property="zeitBis" styleId="zeitBis" styleClass="datetime" errorStyle="${errorStyle}" />
		<img src="<c:url value='/static/img/calendar.gif' />" id="buttonZeitBis" />
	</div>
	<div class="label" style="margin-left:35px;width:79px;"><label for="zeitBisKoordiniert"><bean:message key="regelung.zeitBisKoordiniert.short" /></label></div>
	<div class="input">
		<html:text property="zeitBisKoordiniert" styleId="zeitBisKoordiniert" styleClass="datetime" errorStyle="${errorStyle}" />
		<img src="<c:url value='/static/img/calendar.gif' />" id="buttonZeitBisKoordiniert" />
	</div>
</div>
<div class="box">
	<div class="label"><label for="durchgehend"><bean:message key="regelung.durchgehend" /></label></div>
	<div class="input"><html:checkbox property="durchgehend" styleId="durchgehend" styleClass="checkbox" /></div>
	<div class="label" style="margin-left:130px;width:79px;"><label for="schichtweise"><bean:message key="regelung.schichtweise" /></label></div>
	<div class="input"><html:checkbox property="schichtweise" styleId="schichtweise" styleClass="checkbox" /></div>
</div>
<div class="box">
	<div class="label"><bean:message key="vtr" /></div>
	<div class="input" style="width:38px;"><html:checkbox property="wtsMo" styleClass="checkbox" /><bean:message key="vtr.montag.short" /></div>
	<div class="input" style="width:37px;"><html:checkbox property="wtsDi" styleClass="checkbox" /><bean:message key="vtr.dienstag.short" /></div>
	<div class="input" style="width:37px;"><html:checkbox property="wtsMi" styleClass="checkbox" /><bean:message key="vtr.mittwoch.short" /></div>
	<div class="input" style="width:38px;"><html:checkbox property="wtsDo" styleClass="checkbox" /><bean:message key="vtr.donnerstag.short" /></div>
	<div class="input" style="width:37px;"><html:checkbox property="wtsFr" styleClass="checkbox" /><bean:message key="vtr.freitag.short" /></div>
	<div class="input" style="width:38px;"><html:checkbox property="wtsSa" styleClass="checkbox" /><bean:message key="vtr.samstag.short" /></div>
	<div class="input"><html:checkbox property="wtsSo" styleClass="checkbox" /><bean:message key="vtr.sonntag.short" /></div>
</div>

<script type="text/javascript">
	Calendar.setup({
		inputField  : "zeitVon",
		ifFormat    : "%d.%m.%Y %H:%M",
		button      : "buttonZeitVon",
		showsTime	: true,
		eventName	: "click"
	});
	Calendar.setup({
		inputField  : "zeitBis",
		ifFormat    : "%d.%m.%Y %H:%M",
		button      : "buttonZeitBis",
		showsTime	: true,
		eventName	: "click"
	});
	Calendar.setup({
		inputField  : "zeitVonKoordiniert",
		ifFormat    : "%d.%m.%Y %H:%M",
		button      : "buttonZeitVonKoordiniert",
		showsTime	: true,
		eventName	: "click"
	});
	Calendar.setup({
		inputField  : "zeitBisKoordiniert",
		ifFormat    : "%d.%m.%Y %H:%M",
		button      : "buttonZeitBisKoordiniert",
		showsTime	: true,
		eventName	: "click"
	});
</script>