<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>

<div class="textcontent_head"><bean:message key="menu.auswertungen" /></div>
<div class="textcontent">
	<div class="textcontent_left">
		<div class="box">
			<div class="label"><bean:message key="baumassnahme.art" /></div>
			<div class="input">
				<html:select property="art">
					<html:option value=""><bean:message key="common.select.option" /></html:option>
					<logic:iterate id="currentArt" name="arten">
						<html:option value="${currentArt}">
							<bean:message key="baumassnahme.art.${currentArt}" />
						</html:option>
					</logic:iterate>
				</html:select>
			</div>
		</div>
		
		<div class="box">
			<div class="label"><bean:message key="baumassnahme.kigbaunr" /></div>
			<div class="input"><html:text property="kigBauNr" /></div>
		</div>
		
		<div class="box">				
			<div class="label"><bean:message key="baumassnahme.korridornr" /></div>
			<div class="input"><html:text property="korridorNr" /></div>
		</div>

		<div class="box">
			<div class="label"><bean:message key="baumassnahme.qsnr" /></div>
			<div class="input"><html:text property="qsNr" /></div>
		</div>
		
		<logic:present name="evuList">
			&nbsp;<br /><div class="box">
				<div class="label" style="width:150px;"><bean:message key="auswertungen.pevu" /></div>
				<div class="checkbox"><html:radio styleClass="checkbox" property="evu" value="pevu" /></div>
				<div class="label" style="width:150px;"><bean:message key="auswertungen.gevu" /></div>
				<div class="checkbox"><html:radio styleClass="checkbox" property="evu" value="gevu" /></div>
			</div>
		</logic:present>
		
		<fieldset style="margin-top:15px;">
			<legend><bean:message key="ueb.uebergabeblatt" />/<bean:message key="bbzr.bbzr" /></legend>
			
			<div class="box">
				<div class="label"><label for="zeitraumVerkehrstagVon"><bean:message key="zvf.verkehrstagvon" /></label></div>
				<div class="input">
					<html:text property="zeitraumVerkehrstagVon" styleId="zeitraumVerkehrstagVon" maxlength="10" styleClass="date" />
					<img src="<c:url value='/static/img/calendar.gif' />" id="buttonZeitraumVerkehrstagVon" />
				</div>
			</div>
			<div class="box">
				<div class="label"><label for="zeitraumVerkehrstagBis"><bean:message key="zvf.verkehrstagbis" /></label></div>
				<div class="input">
					<html:text property="zeitraumVerkehrstagBis" styleId="zeitraumVerkehrstagBis" maxlength="10" styleClass="date" />
					<img src="<c:url value='/static/img/calendar.gif' />" id="buttonZeitraumVerkehrstagBis" />
				</div>
			</div>
			
			<div class="box">
				<div class="label"><bean:message key="ueb.zug.qsks" /></div>
				<html:radio property="qsks" value="-1" styleClass="checkbox"><bean:message key="ueb.zug.qsks.all" /></html:radio>
				<html:radio property="qsks" value="0" styleClass="checkbox"><bean:message key="ueb.zug.qsks.0" /></html:radio>
				<html:radio property="qsks" value="1" styleClass="checkbox"><bean:message key="ueb.zug.qsks.1" /></html:radio>
				<html:radio property="qsks" value="2" styleClass="checkbox"><bean:message key="ueb.zug.qsks.2" /></html:radio>
			</div>
		</fieldset>
	</div>
	
	<div class="textcontent_right">
		<%-- Suche nach Baubeginn --%>
		<div class="box">
			<div class="label"><bean:message key="baumassnahme.baubeginndatum" /></div>
			<div class="input">
				<html:text property="beginnDatum" styleId="beginnDatum" styleClass="date" maxlength="10" />
				<img src="<c:url value='/static/img/calendar.gif' />" id="buttonBeginnDatum" />
			</div>
		</div>
		<div class="box">
			<div class="label"><bean:message key="baumassnahme.enddatum" /></div>
			<div class="input">
				<html:text property="endDatum" styleId="endDatum" styleClass="date" maxlength="10" />
				<img src="<c:url value='/static/img/calendar.gif' />" id="buttonEndDatum" />
			</div>
		</div>

		<hr/>	
		
		<%-- Suche nach Bauzeitraum --%>
		<div class="box">
			<div class="label"><bean:message key="baumassnahme.beginndatum" /></div>
			<div class="input">
				<html:text property="bauZeitraumVon" styleId="bauZeitraumVon" maxlength="10" styleClass="date" />
				<img src="<c:url value='/static/img/calendar.gif' />" id="buttonBauZeitraumVon" />
			</div>
		</div>
		<div class="box">
			<div class="label"><bean:message key="baumassnahme.enddatum" /></div>
			<div class="input">
				<html:text property="bauZeitraumBis" styleId="bauZeitraumBis" maxlength="10" styleClass="date" />
				<img src="<c:url value='/static/img/calendar.gif' />" id="buttonBauZeitraumBis" />
			</div>
		</div>
		<div class="box">
			<div class="label"></div>
			<div class="input">
				<html:multibox property="nurAktiv" styleClass="checkbox" value="true"></html:multibox>&nbsp;<bean:message key="baumassnahme.nuraktiv" />
			</div>
		</div>
		
		<logic:present name="regionalbereiche">
			<hr/>
			
			<div class="box">
				<div class="label">
					<bean:message key="baumassnahme.regionalbereichbm" />
					<img src="<c:url value='/static/img/indicator.gif' />" id="regionalbereichBMIndicator" style="display:none;" />
				</div>						
				<div class="input">
					<html:text property="regionalbereichBM" styleId="regionalbereichBM" maxlength="255" />
					<div id="regionalbereichBMSelect" class="autocomplete"></div>
				</div>
			</div>
		
			<div class="box">
				<bean:define id="urlBearbeitungsbereich" toScope="page"><c:url value="/refreshBearbeitungsbereichSearch.do" /></bean:define>
				<div class="label"><bean:message key="baumassnahme.regionalbereichfpl" /></div>						
				<div class="input">
					<html:select property="regionalbereichFpl" styleId="regionalBereichFpl" onchange="refreshBearbeitungsbereichSearch('${urlBearbeitungsbereich}', this.value, 'STATISTICS');">
						<html:option value=""><bean:message key="common.select.option" /></html:option>
						<html:optionsCollection name="regionalbereiche" label="name" value="id" />
					</html:select>
				</div>
			</div>
			
			<div id="divBearbeitungsbereichSearch"> 
				<div class="box">
					<div class="label"><label for="bearbeitungsbereich"><bean:message key="baumassnahme.bearbeitungsbereich" /></label></div>
					<div class="input">
						<html:select property="bearbeitungsbereich" styleId="bearbeitungsbereich">
							<html:option value=""><bean:message key="common.select.option" /></html:option>
							<logic:iterate id="currentBearbeitungsbereich" name="bearbeitungsbereiche" indexId="index">
								<html:option value="${currentBearbeitungsbereich.id}" >${currentBearbeitungsbereich.name}</html:option>
							</logic:iterate>
						</html:select>
					</div>
				</div>
			</div>
		</logic:present>
	</div>

	<fieldset style="clear:both;">
		<legend><bean:message key="baumassnahme.tab.jbb" /></legend>
		
		<jsp:include page="../controlling/suche.jsp" />
	</fieldset>
</div>
<div class="buttonBar">
	<input type="submit" class="hiddenSubmit" />
	<html:link styleClass="buttonCancel" action="${currentActionPath}">
		<bean:message key="button.reset" />
		<html:param name="reset" value="true" />
	</html:link>
	<html:link styleClass="buttonSearch" href="javascript:document.getElementById('StatisticsFilterForm').submit();">
		<bean:message key="auswertungen.update" />
	</html:link>
</div>

<script type="text/javascript">
  Calendar.setup(
    {
      inputField  : "beginnDatum",         // ID of the input field
      ifFormat    : "%d.%m.%Y",    // the date format
      button      : "buttonBeginnDatum",       // ID of the button
      eventName	  : "click"
    }
  );
  Calendar.setup(
    {
      inputField  : "endDatum",         // ID of the input field
      ifFormat    : "%d.%m.%Y",    // the date format
      button      : "buttonEndDatum",       // ID of the button
      eventName   : "click"
    }
  );
  Calendar.setup(
    {
      inputField  : "bauZeitraumVon",         // ID of the input field
      ifFormat    : "%d.%m.%Y",    // the date format
      button      : "buttonBauZeitraumVon",       // ID of the button
      eventName	  : "click"
    }
  );
  Calendar.setup(
    {
      inputField  : "bauZeitraumBis",         // ID of the input field
      ifFormat    : "%d.%m.%Y",    // the date format
      button      : "buttonBauZeitraumBis",       // ID of the button
      eventName	  : "click"
    }
  );
  Calendar.setup(
    {
      inputField  : "zeitraumVerkehrstagVon",         // ID of the input field
      ifFormat    : "%d.%m.%Y",    // the date format
      button      : "buttonZeitraumVerkehrstagVon",       // ID of the button
      eventName	  : "click"
    }
  );
  Calendar.setup(
    {
      inputField  : "zeitraumVerkehrstagBis",         // ID of the input field
      ifFormat    : "%d.%m.%Y",    // the date format
      button      : "buttonZeitraumVerkehrstagBis",       // ID of the button
      eventName	  : "click"
    }
  );
</script>
<script type="text/javascript">
	new Ajax.Autocompleter(
			"regionalbereichBM", 
			"regionalbereichBMSelect", 
			"<c:url value='/AutoCompleteRegionalbereichBM.view'/>", 
			{
				indicator: 'regionalbereichBMIndicator', 
				minChars: 1, 
				paramName: 'keyword'
			}
	);
</script>