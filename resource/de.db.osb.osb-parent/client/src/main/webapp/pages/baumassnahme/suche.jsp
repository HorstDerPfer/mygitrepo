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
<html:xhtml />

<jsp:include page="../main_head.jsp" />
<jsp:include page="../main_path.jsp" />
<jsp:include page="../main_menu.jsp" />

<%-- Öffnet Punkt in Startmenü --%>
<script type="text/javascript">
    openMainMenu('navLink_bob_suche');

    function setDisplaytagPagingLinks() {
    	var pageLinks = $j('span.pagelinks a:not([class]^="arrow")');
    	var maxPages = 1;
    	$j(pageLinks).each(function(index, element) {$j(element).attr("onclick", "$j('#page').val("+parseInt($j(element).text())+");$j('#baumassnahmeSearchForm').submit();return false;");maxPages++;});
    	
        var currentPage = parseInt($j('span.pagelinks strong').text());
        var prevPage = (currentPage==1)?1:currentPage-1;
        var nextPage = (currentPage==maxPages)?currentPage:currentPage+1;
		
		$j('span.pagelinks a[class^="arrowFirst"]').each(function(index, element){$j(element).attr("onclick", "$j('#page').val(1);$j('#baumassnahmeSearchForm').submit();return false;");});
		$j('span.pagelinks a[class^="arrowLast"]').each(function(index, element){$j(element).attr("onclick", "$j('#page').val("+(maxPages)+");$j('#baumassnahmeSearchForm').submit();return false;");});
		$j('span.pagelinks a[class^="arrowLeft"]').each(function(index, element){$j(element).attr("onclick", "$j('#page').val("+prevPage+");$j('#baumassnahmeSearchForm').submit();return false;");});
		$j('span.pagelinks a[class^="arrowRight"]').each(function(index, element){$j(element).attr("onclick", "$j('#page').val("+nextPage+");$j('#baumassnahmeSearchForm').submit();return false;");});
	}
    $j(window).ready(setDisplaytagPagingLinks);
</script>

<div class="textcontent_head"><bean:message key="menu.suche" /></div>
<html:form action="/suche?method=web">
	<html:hidden property="sortDirection" />
	<html:hidden property="sortColumn" styleId="sortColumn" />
	
	<div class="textcontent">
		<div class="textcontent_left">
			<fieldset>
				<legend><bean:message key="baumassnahme.search.regionalbereich" /></legend>
				
				<%--
				<div class="box">
					<div class="label"><bean:message key="baumassnahme.fplonr" /></div>
					<div class="input"><html:text property="fploNr" /></div>
				</div>
				--%>
				 
				<div class="box">
					<div class="label"><bean:message key="baumassnahme.favoriten" /></div>		
					<html:select property="bearbeiter" styleId="bearbeiter" >
						<html:option value=""><bean:message key="baumassnahme.select.option.alle" /></html:option>
						<html:optionsCollection name="bearbeiter" label="nameAndFirstname" value="id" />
					</html:select>				
				</div>
				
				<logic:present name="regionalbereiche">
					<div class="box">
						<bean:define id="urlBearbeitungsbereich" toScope="page"><c:url value="refreshBearbeitungsbereichSearch.do" /></bean:define>
						<div class="label"><bean:message key="baumassnahme.regionalbereichfpl" /></div>						
						<div class="input">
							<html:select property="regionalbereichFpl" styleId="regionalBereichFpl" onchange="refreshBearbeitungsbereichSearch('${urlBearbeitungsbereich}', this.value, 'BAUMASSNAHMESEARCH');">
								<html:option value=""><bean:message key="common.select.option" /></html:option>
								<html:optionsCollection name="regionalbereiche" label="name" value="id" />
							</html:select>
						</div>
					</div>
					<jsp:include page="edit/editBearbeitungsbereichSearch.jsp"></jsp:include>
				</logic:present>
			</fieldset>

			<fieldset>
				<legend><bean:message key="baumassnahme.search.zeitraum" /></legend>
				
				<div class="box">
					<div class="label"><bean:message key="baumassnahme.fahrplanjahr" /></div>
					<div class="input">
						<html:select property="fahrplanjahr">
							<html:option value="">(<bean:message key="common.all" />)</html:option>
							<c:forEach var="fplj" items="${availableFahrplanjahre}">
								<html:option value="${fplj}">${fplj}</html:option>
							</c:forEach>
						</html:select>
					</div>
				</div>

				<%-- Suche nach Baubeginn --%>
				<div class="box">
					<div class="label"><bean:message key="baumassnahme.baubeginndatum" /></div>
					<div class="input" style="width:150px;">
						<html:text property="beginnDatum" styleId="beginnDatum" maxlength="10" styleClass="date" />
						<img src="<c:url value='/static/img/calendar.gif' />" id="buttonBeginnDatum" />
					</div>
					<div class="label" style="width:75px;"><bean:message key="baumassnahme.enddatum" /></div>
					<div class="input">
						<html:text property="endDatum" styleId="endDatum" maxlength="10" styleClass="date" />
						<img src="<c:url value='/static/img/calendar.gif' />" id="buttonEndDatum" />
					</div>
				</div>
				
				<%-- Suche nach Bauzeitraum --%>
				<div class="box">
					<div class="label"><bean:message key="baumassnahme.beginndatum" /></div>
					<div class="input" style="width:150px;">
						<html:text property="bauZeitraumVon" styleId="bauZeitraumVon" maxlength="10" styleClass="date" />
						<img src="<c:url value='/static/img/calendar.gif' />" id="buttonBauZeitraumVon" />
					</div>
					<div class="label" style="width:75px;"><bean:message key="baumassnahme.enddatum" /></div>
					<div class="input">
						<html:text property="bauZeitraumBis" styleId="bauZeitraumBis" maxlength="10" styleClass="date" />
						<img src="<c:url value='/static/img/calendar.gif' />" id="buttonBauZeitraumBis" />
					</div>
				</div>
	
				<div class="box">
					<div class="label"></div>
					<div class="input">
						<html:multibox property="nurAktiv" styleClass="checkbox" value="true"></html:multibox>
						<bean:message key="baumassnahme.nuraktiv" />
					</div>
				</div>
			</fieldset>
			
			<fieldset>
				<legend><bean:message key="baumassnahme.search.strecke" /></legend>
				
				<div class="box">
					<div class="label"><bean:message key="baumassnahme.streckebbp" /></div>
					<div class="input" style="width:130px;"><html:text property="streckeBBP" style="width:80px;" /></div>
					<div class="label" style="width:95px;"><bean:message key="baumassnahme.streckevzg" /></div>
					<div class="input"><html:text property="streckeVZG" style="width:80px;" /></div>
				</div>
				
				<div class="box">
					<div class="label"><bean:message key="baumassnahme.streckenabschnitt" /></div>
					<div class="input"><html:text property="streckenAbschnitt" /></div>
				</div>
			</fieldset>
		</div>
		<div class="textcontent_right">
			<fieldset>
				<legend><bean:message key="baumassnahme.art" /></legend>

					<div class="box" style="margin:6px 0 4px 0;">
						<div class="input"><html:checkbox property="artKs" value="true" styleClass="checkbox" /></div>
						<div class="label" style="width:115px;"><bean:message key="baumassnahme.art.KS" /></div>
						<div class="input"><html:checkbox property="artQs" value="true" styleClass="checkbox" /></div>
						<div class="label" style="width:115px;"><bean:message key="baumassnahme.art.QS" /></div>
						<div class="input"><html:checkbox property="artA" value="true" styleClass="checkbox" /></div>
						<div class="label" style="width:110px;"><bean:message key="baumassnahme.art.A" /></div>
						<div class="input"><html:checkbox property="artB" value="true" styleClass="checkbox" /></div>
						<div class="label" style="width:15px;"><bean:message key="baumassnahme.art.B" /></div>
					</div>
			</fieldset>
		
			<fieldset>
				<legend><bean:message key="baumassnahme.search.nummernUndIds" /></legend>
			
				<div class="box">
					<div class="label"><bean:message key="baumassnahme.vorgangsnr" /></div>
					<div class="input"><html:text property="vorgangsNummer" /></div>
				</div>
				
				<hr/>
				
				<div class="box">
					<div class="label"><bean:message key="baumassnahme.korridornr" /></div>
					<div class="input" style="width:140px;"><html:text property="korridorNr" maxlength="5" style="width:30px;" /></div>
					<div class="label" style="width:105px;"><bean:message key="baumassnahme.korridorzeitfenster" /></div>
					<div class="input"><html:text property="korridorZeitfenster" maxlength="10"  style="width:60px;" /></div>
				</div>

				<div class="box">
					<div class="label"><bean:message key="baumassnahme.qsnr" /></div>
					<div class="input"><html:text property="qsNr" /></div>
				</div>

				<div class="box">
					<div class="label"><bean:message key="baumassnahme.kigbaunr" /></div>						
					<%--<div class="show">
						%--<logic:iterate id="currentKigBauNr" name="baumassnahme" property="kigBauNr" indexId="index">
							${currentKigBauNr}
							<c:if test="${ (fn:length(baumassnahme.kigBauNr) > (index+1)) }">,</c:if>
						</logic:iterate>--%
					</div>--%>
					<div class="input"><html:text property="kigBauNr" /></div>
				</div>
				
				<div class="box">
					<div class="label"><bean:message key="baumassnahme.bbpmassnahme" /></div>
					<div class="input"><html:text property="masId" /></div>
				</div>
			</fieldset>
			
			<fieldset>
				<legend><bean:message key="baumassnahme.search.ausfaelleUndAenderungen" /></legend>
				
				<div class="box">
					<div class="label"><bean:message key="baumassnahme.search.ausfaelleUndAenderungen.ausfaelle" /></div>
					<div class="input">
						<html:select property="ausfaelle">
							<html:option value=""><bean:message key="common.egal" /></html:option>
							<html:option value="true"><bean:message key="common.mit" /></html:option>
							<html:option value="false"><bean:message key="common.ohne" /></html:option>
						</html:select>
					</div>
				</div>

				<div class="box">
					<div class="label"><bean:message key="baumassnahme.search.ausfaelleUndAenderungen.aenderungen" /></div>
					<div class="input">
						<html:select property="aenderungen">
							<html:option value=""><bean:message key="common.egal" /></html:option>
							<html:option value="true"><bean:message key="common.mit" /></html:option>
							<html:option value="false"><bean:message key="common.ohne" /></html:option>
						</html:select>
					</div>
				</div>
			</fieldset>
		</div>

		<fieldset style="margin-top:10px;">
			<legend><bean:message key="controlling.title" /></legend>
	
			<jsp:include page="../controlling/suche.jsp" />
			<html:multibox property="onlyOpenMilestones" styleClass="checkbox" value="true" />nur unbearbeitete Meilensteine anzeigen
		</fieldset>
		
		<div class="textcontent_left">
			<div class="label" style=""><bean:message key="common.view" /></div>
			<div class="input">
				<html:radio property="viewMode" styleClass="checkbox" value="listView" onchange="$('sortColumn').value='';" />Liste<br />
				<html:radio property="viewMode" styleClass="checkbox" value="milestoneView" onchange="$('sortColumn').value='';" />Meilensteine<br />
			</div>
		</div>
	</div>
	<div class="buttonBar">
		<html:hidden property="page" styleId="page" />
		<script type="text/javascript">
			function change_action(method) {
				
				var action = '<c:url value="/suche.do" />';
				action += "?method=";
				if(method == null) {
					return false;
				} else if(method == "web") {
					action = action + "web";
				}else if(method == "xlsSummary") {
					action = action + "xlsSummary";
				}else if(method == "saveConfig") {
					action = action + "saveConfig";
				}else if(method == "loadConfig") {
					action = action + "loadConfig";
				} else if(method == "xls") {
					action = action + "xls";
				}
				
				var backup_action = $('baumassnahmeSearchForm').action;
				$('baumassnahmeSearchForm').action = action;
				$('baumassnahmeSearchForm').submit();
				$('baumassnahmeSearchForm').action = backup_action;
				return false;
			}
		</script>
		<input type="submit" class="hiddenSubmit" />
		<html:link action="/suche" onclick="return change_action('saveConfig');" styleClass="buttonSave">
			<bean:message key="baumassnahme.search.save" />
			<html:param name="method" value="saveConfig" />
		</html:link>
		<html:link action="/suche" onclick="return change_action('loadConfig');" styleClass="buttonReload">
			<bean:message key="baumassnahme.search.load" />
			<html:param name="method" value="loadConfig" />
		</html:link>
		<html:link styleClass="buttonReload" action="/suche">
			<bean:message key="button.reset" />
			<html:param name="reset" value="true" />
			<html:param name="method" value="web" />
		</html:link>
		<html:link styleClass="buttonSearch" href="#" onclick="$j('#page').val(0);$j('#baumassnahmeSearchForm').submit();">
			<bean:message key="baumassnahme.search" />
		</html:link>
	</div>
</html:form>


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
</script>

<br />
<br />

<logic:empty name="baumassnahmeSearchForm" property="viewMode">
	<c:import url="baumassnahmenliste.jsp">
		<c:param name="requestURI" value="suche.do?method=web" />
	</c:import>
</logic:empty>

<logic:equal name="baumassnahmeSearchForm" property="viewMode" value="listView">
	<c:import url="baumassnahmenliste.jsp">
		<c:param name="requestURI" value="suche.do?method=web" />
	</c:import>
</logic:equal>

<logic:equal name="baumassnahmeSearchForm" property="viewMode" value="milestoneView">
	<jsp:include page="../controlling/controllingView.jsp"/>
</logic:equal>

<jsp:include page="../main_footer.jsp" />				