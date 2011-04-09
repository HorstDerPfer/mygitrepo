<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

<logic:equal name="baumassnahme" property="kigBau" value="true">
	<div class="box">
		<div class="label">
			<bean:message key="baumassnahme.kigbaunr" />
			<img src="static/img/icon_s_info_small.gif" id="tipKigbauNr" />
		</div>
		<div id="tooltipKigbauNr"><bean:message key="baumassnahme.kigbau.tip" /></div>
		<div class="input"><html:text property="kigBauNr" styleId="kigBauNr" maxlength="164" styleClass="long"/></div>
	</div>

	<script type="text/javascript">
		new Tip($('tipKigbauNr'), $('tooltipKigbauNr'));
	</script>	
</logic:equal>

<logic:equal name="baumassnahme" property="art" value="KS">
	<div class="box">
		<div class="label"><bean:message key="baumassnahme.korridornr" /></div>
		<div class="input"><html:text property="korridorNr" styleId="korridorNr" maxlength="5"/></div>
	</div>
	<div class="box">
		<div class="label">
			<bean:message key="baumassnahme.korridorzeitfenster" />&nbsp;
			<img src="static/img/icon_s_info_small.gif" id="tipKorridorZf" />
		</div>
		<div id="tooltipKorridorZf"><bean:message key="baumassnahme.korridorzf.tip" /></div>
		<div class="input"><html:text property="korridorZeitfenster" styleId="korridorZeitfenster" maxlength="54" styleClass="long" /></div>
	</div>
	
	<script type="text/javascript">
		new Tip($('tipKorridorZf'), $('tooltipKorridorZf'));
	</script>	
</logic:equal>

<logic:equal name="baumassnahme" property="art" value="QS">
	<div class="box">
		<div class="label">
			<bean:message key="baumassnahme.qsnr" />
			<img src="static/img/icon_s_info_small.gif" id="tipQsNr" />
		</div>
		<div id="tooltipQsNr"><bean:message key="baumassnahme.qsnr.tip" /></div>
		<div class="input"><html:text property="qsNr" styleId="qsNr" maxlength="255" styleClass="long"/></div>
	</div>
	<div class="box">
		<div class="label"><label for="qsSPFV"><bean:message key="baumassnahme.qsspfv" /></label></div>
		<div class="input">
			<html:radio property="qsSPFV" value="true" styleClass="checkbox" />
			<bean:message key="baumassnahme.qsspfv.true" />
			<html:radio property="qsSPFV" value="false" styleClass="checkbox" />
			<bean:message key="baumassnahme.qsspfv.false" />
		</div>
	</div>
	
	<div class="box">
		<div class="label"><label for="qsSPNV"><bean:message key="baumassnahme.qsspnv" /></label></div>
		<div class="input">
			<html:radio property="qsSPNV" value="true" styleClass="checkbox" />
			<bean:message key="baumassnahme.qsspnv.true" />
			<html:radio property="qsSPNV" value="false" styleClass="checkbox" />
			<bean:message key="baumassnahme.qsspnv.false" />
		</div>
	</div>

	<div class="box">
		<div class="label"><label for="qsSGV"><bean:message key="baumassnahme.qssgv" /></label></div>
		<div class="input">
			<html:radio property="qsSGV" value="true" styleClass="checkbox" />
			<bean:message key="baumassnahme.qssgv.true" />
			<html:radio property="qsSGV" value="false" styleClass="checkbox" />
			<bean:message key="baumassnahme.qssgv.false" />
		</div>
	</div>
	
	<script type="text/javascript">
		new Tip($('tipQsNr'), $('tooltipQsNr'));
	</script>	
</logic:equal>
