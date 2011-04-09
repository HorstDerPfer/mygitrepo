<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
<html:xhtml/>

<div class="textcontent_left">
	<div class="box">
		<div class="label"><label for="paketId"><bean:message key="paket" /></label></div>
		<div class="input">
			<html:select property="paketId" errorStyle="${errorStyle}" styleId="paketId">
				<html:option value=""><bean:message key="common.select.option" /></html:option>
				<html:optionsCollection name="pakete" value="id" label="paketId"/>
			</html:select>
		</div>
	</div>
	<div class="box">
		<div class="label"><label for="technischerPlatz"><bean:message key="sperrpausenbedarf.technischerPlatz" /></label></div>
		<div class="input"><html:text property="technischerPlatz" styleId="technischerPlatz" maxlength="255" errorStyle="${errorStyle}" /></div>
	</div>
	<div class="box">
		<div class="label"><label for="bauverfahren"><bean:message key="sperrpausenbedarf.bauverfahren" /></label></div>
		<div class="input">
			<html:select property="bauverfahren" errorStyle="${errorStyle}" styleId="bauverfahren">
				<html:option value=""><bean:message key="common.select.option" /></html:option>
				<logic:iterate id="currentBauverfahren" name="bauverfahren">
					<html:option value="${currentBauverfahren}">
						<bean:message key="sperrpausenbedarf.bauverfahren.${currentBauverfahren}" />
					</html:option>
				</logic:iterate>
			</html:select>
		</div>
	</div>
	<div class="box">
		<div class="label"><label for="pspElement"><bean:message key="sperrpausenbedarf.pspElement" /></label></div>
		<div class="input"><html:text property="pspElement" styleId="pspElement" maxlength="255" errorStyle="${errorStyle}" /></div>
	</div>
</div>
<div class="textcontent_right">
	<div class="box">
		<div class="label"><label for="gewerk"><bean:message key="sperrpausenbedarf.gewerk" />*</label></div>
		<div class="input"><html:text property="gewerk" styleId="gewerk" maxlength="255" errorStyle="${errorStyle}" /></div>
	</div>
	<div class="box">
		<div class="label"><label for="untergewerk"><bean:message key="sperrpausenbedarf.untergewerk" />*</label></div>
		<div class="input"><html:text property="untergewerk" styleId="untergewerk" maxlength="255" errorStyle="${errorStyle}" /></div>
	</div>
	<div class="box">
		<div class="label"><label for="weichenGleisnummerBfGleisen"><bean:message key="sperrpausenbedarf.weichenGleisnummerBfGleisen.short" /></label></div>
		<div class="input"><html:text property="weichenGleisnummerBfGleisen" styleId="weichenGleisnummerBfGleisen" maxlength="255" errorStyle="${errorStyle}" /></div>
	</div>
	<div class="box">
		<div class="label"><label for="weichenbauform"><bean:message key="sperrpausenbefarf.weichenbauform" /></label></div>
		<div class="input"><html:text property="weichenbauform" styleId="weichenbauform" maxlength="255" errorStyle="${errorStyle}" /></div>
	</div>
</div>

<hr/>

<div class="textcontent_left">
	<div class="box">
		<div class="label"><label for="bahnsteige"><bean:message key="sperrpausenbedarf.bahnsteige" /></label></div>
		<div class="input"><html:checkbox property="bahnsteige" styleId="bahnsteige" styleClass="checkbox" errorStyle="${errorStyle}" /></div>
	</div>
	<div class="box">
		<div class="label"><label for="einbauPss"><bean:message key="sperrpausenbedarf.einbauPss" /></label></div>
		<div class="input"><html:checkbox property="einbauPss" styleId="einbauPss" styleClass="checkbox" errorStyle="${errorStyle}" /></div>
	</div>
	<div class="box">
		<div class="label"><label for="kabelkanal"><bean:message key="sperrpausenbedarf.kabelkanal" /></label></div>
		<div class="input"><html:checkbox property="kabelkanal" styleId="kabelkanal" styleClass="checkbox" errorStyle="${errorStyle}" /></div>
	</div>
	<div class="box">
		<div class="label"><label for="oberleitungsAnpassung"><bean:message key="sperrpausenbedarf.oberleitungsAnpassung" /></label></div>
		<div class="input"><html:checkbox property="oberleitungsAnpassung" styleId="oberleitungsAnpassung" styleClass="checkbox" errorStyle="${errorStyle}" /></div>
	</div>
	<div class="box">
		<div class="label"><label for="lst"><bean:message key="sperrpausenbedarf.lst" /></label></div>
		<div class="input"><html:checkbox property="lst" styleId="lst" styleClass="checkbox" errorStyle="${errorStyle}" /></div>
	</div>
</div>
<div class="textcontent_right">
	<div class="box">
		<div class="label"><label for="geplanteNennleistung"><bean:message key="sperrpausenbedarf.geplanteNennleistung" /></label></div>
		<div class="input"><html:text property="geplanteNennleistung" styleId="geplanteNennleistung" styleClass="right" maxlength="6" style="width:50px;" errorStyle="width:50px;${errorStyle}" /></div>
	</div>
	<div class="box">
		<div class="label"><label for="notwendigeLaengePss"><bean:message key="sperrpausenbedarf.notwendigeLaengePss" /></label></div>
		<div class="input"><html:text property="notwendigeLaengePss" styleId="notwendigeLaengePss" styleClass="right" maxlength="6" style="width:50px;" errorStyle="width:50px;${errorStyle}" /></div>
	</div>
	<div class="box">
		<div class="label"><label for="umbaulaenge"><bean:message key="sperrpausenbedarf.umbaulaenge" /></label></div>
		<div class="input"><html:text property="umbaulaenge" styleId="umbaulaenge" styleClass="right" maxlength="6" style="width:50px;" errorStyle="width:50px;${errorStyle}" /></div>
	</div>
	<div class="box">
		<div class="label"><label for="tiefentwaesserungLage"><bean:message key="sperrpausenbedarf.tiefentwaesserung.lage" /></label></div>
		<div class="input">
			<html:select property="tiefentwaesserungLage" style="width:60px;" errorStyle="width:50px;${errorStyle}" styleId="tiefentwaesserungLage">
				<html:option value=""><bean:message key="common.ohne" /></html:option>
				<logic:iterate id="currentTL" name="tiefentwaesserungLagen">
					<html:option value="${currentTL}">
						<bean:message key="sperrpausenbedarf.tiefentwaesserung.lage.${currentTL}" />
					</html:option>
				</logic:iterate>
			</html:select>
		</div>
	</div>
</div>

<br/>