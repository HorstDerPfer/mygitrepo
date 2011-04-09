<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>

<div class="textcontent_left">
	<html:hidden property="gleissperrungIds" styleId="gleissperrungIds" />

	<div class="box">
		<div class="label">
			<bean:message key="buendel.id" />
			<c:catch var="ex">
				<c:set var="buendelId"><bean:write name="buendel" property="buendelId" /></c:set>
			</c:catch>
			<c:if test="${ex != null}">
				<c:set var="buendelId">---</c:set>
				&nbsp;
				<img src="<c:url value='/static/img/icon_s_info_small.gif'/>" id="tipBuendelId" style="text-align:right" />
				<script type="text/javascript">
					new Tip($('tipBuendelId'), '<bean:message key="buendel.id.tip" />');
				</script>
			</c:if>
		</div>
		<div class="show" >
			<c:out value="${buendelId}" />
			<c:remove var="buendelId" />
		</div>
	</div>

	<div class="box">
		<div class="label"><label for="buendelName"><bean:message key="buendel.name" />*</label></div>
		<div class="input"><html:text property="buendelName" styleId="buendelName" maxlength="45" errorStyle="${errorStyle}" /></div>
	</div>

	<div class="box">
		<div class="label"><label for="regionalbereichId"><bean:message key="buendel.regionalbereich" />*</label></div>
		<authz:authorize ifAnyGranted="ROLE_BUENDEL_ANLEGEN_ALLE,ROLE_BUENDEL_BEARBEITEN_ALLE">
			<div class="input">
				<html:select property="regionalbereichId" styleId="regionalbereichId" errorStyle="${errorStyle}">
					<html:option value="0"><bean:message key="common.select.option" /></html:option>
					<html:optionsCollection name="regionalbereichListe" value="id" label="name" />
				</html:select>
			</div>
		</authz:authorize>
		<authz:authorize ifNotGranted="ROLE_BUENDEL_ANLEGEN_ALLE,ROLE_BUENDEL_BEARBEITEN_ALLE">
			<div class="show">
				<bean:write name="loginUser" property="regionalbereich.name" />
				<html:hidden property="regionabereichId" value="${loginUser.regionalbereich.id}" />
			</div>
		</authz:authorize>
	</div>

	<div class="box">
		<div class="label">
			<label for="ankermassnahmeArtId"><bean:message key="sperrpausenbedarf.ankermassnahmeArt" /></label>&nbsp;
			<img src="<c:url value="/static/img/icon_s_info_small.gif" />" title="<bean:message key="buendel.ankermassnahmeArt.info" />" />
		</div>
		<div class="input">
			<html:select property="ankermassnahmeArtId" styleId="ankermassnahmeArtId" errorStyle="${errorStyle}">
				<html:option value="">(<bean:message key="common.keine" />)</html:option>
				<html:optionsCollection name="ankermassnahmeArten" value="id" label="langname"/>
			</html:select>
		</div>
	</div>

	<div class="box">
		<div class="label"><label for="fixiert"><bean:message key="buendel.fixiert" /></label></div>
		<div class="input"><html:checkbox property="fixiert" styleId="fixiert" styleClass="checkbox" errorStyle="${errorStyle}" /></div>
		<div class="label" style="margin-left:114px;"><bean:message key="buendel.fixierungsDatum" /></div>
		<div class="show center" style="width:60px;"><bean:write name="buendel" property="fixierungsDatum" format="dd.MM.yyyy" /></div>
	</div>
</div>

<div class="textcontent_right">
	<div class="box">
		<div class="label"><label for="hauptStrecke"><bean:message key="buendel.hauptStrecke" />*</label></div>
		<div class="input"><html:text property="hauptStrecke" styleId="hauptStrecke" errorStyle="${errorStyle}" /></div>
	</div>

	<div class="box">
		<div class="label"><label for="startBahnhofId"><bean:message key="buendel.startBahnhof" /></label></div>
		<div class="input">
			<html:select property="startBahnhofId" styleId="startBahnhofId" errorStyle="${errorStyle}">
				<html:option value="">(<bean:message key="common.select.option" />)</html:option>
				<html:optionsCollection name="betriebsstellen" value="id" label="caption"/>
			</html:select>
		</div>
	</div>

	<div class="box">
		<div class="label"><label for="endeBahnhofId"><bean:message key="buendel.endeBahnhof" /></label></div>
		<div class="input">
			<html:select property="endeBahnhofId" styleId="endeBahnhofId" errorStyle="${errorStyle}">
				<html:option value="">(<bean:message key="common.select.option" />)</html:option>
				<html:optionsCollection name="betriebsstellen" value="id" label="caption"/>
			</html:select>
		</div>
	</div>

	<div class="box">
		<div class="label"><bean:message key="buendel.weitereStrecken" /></div>
		<div class="show"><bean:write name="buendel" property="weitereStreckenCsv" /></div>
	</div>

	<div class="box">
		<div class="label"><label for="durchfuehrungsZeitraumStartKoordiniert"><bean:message key="buendel.durchfuehrungszeitraumStartKoordiniert" /></label></div>
		<div class="input" style="width:120px;">
			<html:text property="durchfuehrungsZeitraumStartKoordiniert" styleId="durchfuehrungsZeitraumStartKoordiniert" styleClass="date" maxlength="10" errorStyle="${errorStyle}" />
			<img src="<c:url value='/static/img/calendar.gif' />" id="buttonDurchfuehrungsZeitraumStartKoordiniert" />
			<script type="text/javascript">
				Calendar.setup({
					inputField  : "durchfuehrungsZeitraumStartKoordiniert",
					ifFormat    : "%d.%m.%Y",
					button      : "buttonDurchfuehrungsZeitraumStartKoordiniert"
				});
			</script>	
		</div>
		<div class="label" style="margin-left:35px;width:100px;"><label for="durchfuehrungsZeitraumEndeKoordiniert"><bean:message key="buendel.durchfuehrungszeitraumEndeKoordiniert" /></label></div>
		<div class="input">
			<html:text property="durchfuehrungsZeitraumEndeKoordiniert" styleId="durchfuehrungsZeitraumEndeKoordiniert" styleClass="date" maxlength="10" errorStyle="${errorStyle}" />
			<img src="<c:url value='/static/img/calendar.gif' />" id="buttonDurchfuehrungsZeitraumEndeKoordiniert" />
			<script type="text/javascript">
				Calendar.setup({
					inputField  : "durchfuehrungsZeitraumEndeKoordiniert",
					ifFormat    : "%d.%m.%Y",
					button      : "buttonDurchfuehrungsZeitraumEndeKoordiniert"
				});
			</script>	
		</div>
	</div>
</div>

