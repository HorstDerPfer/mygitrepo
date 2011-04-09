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

<c:choose>
	<%-- Anzeige fuer Administratoren --%>
	<c:when test="${isAdmin}">
		<div class="textcontent_left">
			<div class="box">
				<div class="label"><label for="betriebsstelleVonKoordiniertId"><bean:message key="sperrpausenbedarf.betriebsstelle.von.koordiniert" /></label></div>
				<div class="input">
					<html:select property="betriebsstelleVonKoordiniertId" styleId="betriebsstelleVonKoordiniertId" errorStyle="${errorStyle}">
						<html:option value="">(<bean:message key="common.select.option" />)</html:option>
						<html:optionsCollection name="betriebsstellen" value="id" label="caption"/>
					</html:select>
				</div>
			</div>
			<div class="box">
				<div class="label"><label for="bauterminStart"><bean:message key="sperrpausenbedarf.bauterminStart" />*</label></div>
				<div class="input" style="width:120px;">
					<html:text property="bauterminStart" styleId="bauterminStart" styleClass="datetime" maxlength="16" errorStyle="${errorStyle}" />
					<img src="<c:url value='/static/img/calendar.gif' />" id="buttonBauterminStart" />
					<script type="text/javascript">
						Calendar.setup({
							inputField  : "bauterminStart",
							ifFormat    : "%d.%m.%Y %H:%M",
							button      : "buttonBauterminStart",
							showsTime	: true,
							eventName	: "click"
						});
					</script>	
				</div>
				<div class="label" style="margin-left:20px;width:85px;"><label for="bauterminEnde"><bean:message key="sperrpausenbedarf.bauterminEnde" />*</label></div>
				<div class="input">
					<html:text property="bauterminEnde" styleId="bauterminEnde" styleClass="datetime" maxlength="16" errorStyle="${errorStyle}" />
					<img src="<c:url value='/static/img/calendar.gif' />" id="buttonBauterminEnde" />
					<script type="text/javascript">
						Calendar.setup({
							inputField  : "bauterminEnde",
							ifFormat    : "%d.%m.%Y %H:%M",
							button      : "buttonBauterminEnde",
							showsTime	: true,
							eventName	: "click"
						});
					</script>	
				</div>
			</div>
			<div class="box">
				<div class="label"><label for="bauterminStartKoordiniert"><bean:message key="sperrpausenbedarf.bauterminStartKoordiniert" /></label></div>
				<div class="input" style="width:120px;">
					<html:text property="bauterminStartKoordiniert" styleId="bauterminStartKoordiniert" styleClass="datetime" maxlength="16" errorStyle="${errorStyle}" />
					<img src="<c:url value='/static/img/calendar.gif' />" id="buttonBauterminStartKoordiniert" />
					<script type="text/javascript">
						Calendar.setup({
							inputField  : "bauterminStartKoordiniert",
							ifFormat    : "%d.%m.%Y %H:%M",
							button      : "buttonBauterminStartKoordiniert",
							showsTime	: true,
							eventName	: "click"
						});
					</script>	
				</div>
				<div class="label" style="margin-left:20px;width:85px;"><label for="bauterminEndeKoordiniert"><bean:message key="sperrpausenbedarf.bauterminEndeKoordiniert" /></label></div>
				<div class="input">
					<html:text property="bauterminEndeKoordiniert" styleId="bauterminEndeKoordiniert" styleClass="datetime" maxlength="16" errorStyle="${errorStyle}" />
					<img src="<c:url value='/static/img/calendar.gif' />" id="buttonBauterminEndeKoordiniert" />
					<script type="text/javascript">
						Calendar.setup({
							inputField  : "bauterminEndeKoordiniert",
							ifFormat    : "%d.%m.%Y %H:%M",
							button      : "buttonBauterminEndeKoordiniert",
							showsTime	: true,
							eventName	: "click"
						});
					</script>	
				</div>
			</div>
			<div class="box">
				<div class="label"><label for="kommentar"><bean:message key="sperrpausenbedarf.kommentar" /></label></div>
				<div class="input"><html:text property="kommentar" styleId="kommentar" maxlength="1000" errorStyle="${errorStyle}" /></div>
			</div>
		</div>
		<div class="textcontent_right">
			<div class="box">
				<div class="label"><label for="betriebsstelleBisKoordiniertId"><bean:message key="sperrpausenbedarf.betriebsstelle.bis.koordiniert" /></label></div>
				<div class="input">
					<html:select property="betriebsstelleBisKoordiniertId" styleId="betriebsstelleBisKoordiniertId" errorStyle="${errorStyle}">
						<html:option value="">(<bean:message key="common.select.option" />)</html:option>
						<html:optionsCollection name="betriebsstellen" value="id" label="caption"/>
					</html:select>
				</div>
			</div>
			<div class="box">
				<div class="label"><label for="durchgehend"><bean:message key="sperrpausenbedarf.durchgehend" /></label></div>
				<div class="input" style="width:25px;"><html:checkbox property="durchgehend" styleId="durchgehend" styleClass="checkbox" errorStyle="${errorStyle}" /></div>
				<div class="label" style="margin-left:100px;width:100px;"><label for="schichtweise"><bean:message key="sperrpausenbedarf.schichtweise" /></label></div>
				<div class="input" style="width:25px;"><html:checkbox property="schichtweise" styleId="schichtweise" styleClass="checkbox" errorStyle="${errorStyle}" /></div>
			</div>
			<div class="box">
				<div class="label"><bean:message key="sperrpausenbedarf.verkehrstageregelung" />*</div>
				<div class="input" style="width:38px;"><html:checkbox property="wtsMo" styleClass="checkbox" /><bean:message key="vtr.montag.short" /></div>
				<div class="input" style="width:37px;"><html:checkbox property="wtsDi" styleClass="checkbox" /><bean:message key="vtr.dienstag.short" /></div>
				<div class="input" style="width:37px;"><html:checkbox property="wtsMi" styleClass="checkbox" /><bean:message key="vtr.mittwoch.short" /></div>
				<div class="input" style="width:38px;"><html:checkbox property="wtsDo" styleClass="checkbox" /><bean:message key="vtr.donnerstag.short" /></div>
				<div class="input" style="width:37px;"><html:checkbox property="wtsFr" styleClass="checkbox" /><bean:message key="vtr.freitag.short" /></div>
				<div class="input" style="width:38px;"><html:checkbox property="wtsSa" styleClass="checkbox" /><bean:message key="vtr.samstag.short" /></div>
				<div class="input"><html:checkbox property="wtsSo" styleClass="checkbox" /><bean:message key="vtr.sonntag.short" /></div>
			</div>
			<div class="box">
				<div class="label"><label for="kommentarKoordination"><bean:message key="sperrpausenbedarf.kommentarKoordination" /></label></div>
				<div class="input"><html:text property="kommentarKoordination" styleId="kommentarKoordination" maxlength="1000" errorStyle="${errorStyle}" /></div>
			</div>
		</div>
	</c:when>

	<%-- Anzeige fuer Bearbeiter --%>
	<c:otherwise>
		<div class="textcontent_left">
			<div class="box">
				<div class="label"><label for="betriebsstelleVonKoordiniertId"><bean:message key="sperrpausenbedarf.betriebsstelle.von.koordiniert" /></label></div>
				<div class="input">
					<html:select property="betriebsstelleVonKoordiniertId" styleId="betriebsstelleVonKoordiniertId" errorStyle="${errorStyle}">
						<html:option value="">(<bean:message key="common.select.option" />)</html:option>
						<html:optionsCollection name="betriebsstellen" value="id" label="caption"/>
					</html:select>
				</div>
			</div>
			<div class="box">
				<div class="label"><bean:message key="sperrpausenbedarf.bauterminStart" />*</div>
				<div class="show center" style="width:90px;"><bean:write name="baumassnahme" property="bauterminStart" format="dd.MM.yyyy, HH:mm" /></div>
				<div class="label" style="margin-left:46px;width:100px;"><bean:message key="sperrpausenbedarf.bauterminEnde" />*</div>
				<div class="show center" style="width:90px;"><bean:write name="baumassnahme" property="bauterminEnde" format="dd.MM.yyyy, HH:mm" /></div>
			</div>
			<div class="box">
				<div class="label"><label for="bauterminStartKoordiniert"><bean:message key="sperrpausenbedarf.bauterminStartKoordiniert" /></label></div>
				<div class="input" style="width:120px;">
					<html:text property="bauterminStartKoordiniert" styleId="bauterminStartKoordiniert" styleClass="datetime" maxlength="16" errorStyle="${errorStyle}" />
					<img src="<c:url value='/static/img/calendar.gif' />" id="buttonBauterminStartKoordiniert" />
					<script type="text/javascript">
						Calendar.setup({
							inputField  : "bauterminStartKoordiniert",
							ifFormat    : "%d.%m.%Y %H:%M",
							button      : "buttonBauterminStartKoordiniert",
							showsTime	: true,
							eventName	: "click"
						});
					</script>	
				</div>
				<div class="label" style="margin-left:20px;width:85px;"><label for="bauterminEndeKoordiniert"><bean:message key="sperrpausenbedarf.bauterminEndeKoordiniert" /></label></div>
				<div class="input">
					<html:text property="bauterminEndeKoordiniert" styleId="bauterminEndeKoordiniert" styleClass="datetime" maxlength="16" errorStyle="${errorStyle}" />
					<img src="<c:url value='/static/img/calendar.gif' />" id="buttonBauterminEndeKoordiniert" />
					<script type="text/javascript">
						Calendar.setup({
							inputField  : "bauterminEndeKoordiniert",
							ifFormat    : "%d.%m.%Y %H:%M",
							button      : "buttonBauterminEndeKoordiniert",
							showsTime	: true,
							eventName	: "click"
						});
					</script>	
				</div>
			</div>
			<div class="box" style="height:auto;">
				<div class="label"><bean:message key="sperrpausenbedarf.kommentar" /></div>
				<div class="show"><bean:write name="baumassnahme" property="kommentar" /></div>
			</div>
		</div>
		<div class="textcontent_right">
			<div class="box">
				<div class="label"><label for="betriebsstelleBisKoordiniertId"><bean:message key="sperrpausenbedarf.betriebsstelle.bis.koordiniert" /></label></div>
				<div class="input">
					<html:select property="betriebsstelleBisKoordiniertId" styleId="betriebsstelleBisKoordiniertId" errorStyle="${errorStyle}">
						<html:option value="">(<bean:message key="common.select.option" />)</html:option>
						<html:optionsCollection name="betriebsstellen" value="id" label="caption"/>
					</html:select>
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
			<div class="box">
				<div class="label"><label for="kommentarKoordination"><bean:message key="sperrpausenbedarf.kommentarKoordination" /></label></div>
				<div class="input"><html:text property="kommentarKoordination" styleId="kommentarKoordination" maxlength="1000" errorStyle="${errorStyle}" /></div>
			</div>
		</div>
	</c:otherwise>
</c:choose>

<hr/>
