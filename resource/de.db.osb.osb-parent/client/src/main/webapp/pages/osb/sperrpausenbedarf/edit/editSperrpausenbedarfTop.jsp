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
		<div class="label"><label for="arbeitenId"><bean:message key="sperrpausenbedarf.arbeiten" />*</label></div>
		<div class="input">
			<html:select property="arbeitenId" errorStyle="${errorStyle}" styleId="arbeitenId">
				<html:option value=""><bean:message key="common.select.option" /></html:option>
				<html:optionsCollection name="arbeitstypen" value="id" label="name"/>
			</html:select>
		</div>
	</div>
</div>
<div class="textcontent_right">
	<div class="box">
		<div class="label"><label for="arbeitenKommentar"><bean:message key="sperrpausenbedarf.arbeitenKommentar" /></label></div>
		<div class="input"><html:text property="arbeitenKommentar" styleId="arbeitenKommentar" maxlength="255" errorStyle="${errorStyle}" /></div>
	</div>
</div>

<hr/>

<div class="textcontent_left">
	<div class="box">
		<div class="label"><label for="hauptStrecke"><bean:message key="sperrpausenbedarf.hauptStrecke" />*</label></div>
		<div class="input"><html:text property="hauptStrecke" styleId="hauptStrecke" maxlength="255" errorStyle="${errorStyle}" /></div>
	</div>
	<div class="box">
		<div class="label"><label for="richtungsKennzahl"><bean:message key="sperrpausenbedarf.richtungsKennzahl" />*</label></div>
		<div class="input">
			<html:select property="richtungsKennzahl" styleId="richtungsKennzahl" errorStyle="${errorStyle}">
				<html:option value="">(<bean:message key="common.select.option" />)</html:option>
				<html:option value="0"><bean:message key="gleissperrung.richtungsKennzahl.0" /></html:option>
				<html:option value="1"><bean:message key="gleissperrung.richtungsKennzahl.1" /></html:option>
				<html:option value="2"><bean:message key="gleissperrung.richtungsKennzahl.2" /></html:option>
			</html:select>
		</div>
	</div>
	<div class="box">
		<div class="label"><label for="betriebsstelleVonId"><bean:message key="sperrpausenbedarf.betriebsstelle.von" />*</label></div>
		<div class="input">
			<html:select property="betriebsstelleVonId" styleId="betriebsstelleVonId" errorStyle="${errorStyle}">
				<html:option value="">(<bean:message key="common.select.option" />)</html:option>
				<html:optionsCollection name="betriebsstellen" value="id" label="caption"/>
			</html:select>
		</div>
	</div>
</div>
<div class="textcontent_right">
	<div class="box">
		<div class="label"><label for="kmVon"><bean:message key="sperrpausenbedarf.kmVon" />*</label></div>
		<div class="input"><html:text property="kmVon" styleId="kmVon" styleClass="right" maxlength="7" style="width:50px;" errorStyle="width:50px;${errorStyle}" /></div>
	</div>
	<div class="box">
		<div class="label"><label for="kmBis"><bean:message key="sperrpausenbedarf.kmBis" />*</label></div>
		<div class="input"><html:text property="kmBis" styleId="kmBis" styleClass="right" maxlength="7" style="width:50px;" errorStyle="width:50px;${errorStyle}" /></div>
	</div>
	<div class="box">
		<div class="label"><label for="betriebsstelleBisId"><bean:message key="sperrpausenbedarf.betriebsstelle.bis" />*</label></div>
		<div class="input">
			<html:select property="betriebsstelleBisId" styleId="betriebsstelleBisId" errorStyle="${errorStyle}">
				<html:option value="">(<bean:message key="common.select.option" />)</html:option>
				<html:optionsCollection name="betriebsstellen" value="id" label="caption"/>
			</html:select>
		</div>
	</div>
</div>
