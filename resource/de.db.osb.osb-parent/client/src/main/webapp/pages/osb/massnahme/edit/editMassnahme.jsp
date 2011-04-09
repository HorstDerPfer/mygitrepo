<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>
<html:xhtml/>

<jsp:include page="/pages/main_head.jsp" />
<jsp:include page="/pages/main_path.jsp" />
<jsp:include page="/pages/main_menu.jsp" />

<script type="text/javascript">
	openMainMenu('navLink_osb-massnahme');

	<%-- JQuery UI Autocomplete für Strecke registrieren --%>
	$j(function() {
		$j("#strecke").autocomplete({
			source:
				function(request, response) {
					$j.ajax({
						url:"<c:url value='/AutoCompleteStreckeVzg.view'/>",
						datatype: "html",
						data: {
							<%-- Parameter, die bei AJAX Request übergeben werden --%>
							nummer: request.term,							
							regionalbereichId : "${loginUser.regionalbereich.id}"
						},
						success: function(data) {
							<%-- Ergebnis des Requests in Autocomplete-Response-Objekt umbauen --%>
							<%-- erwartet: <ul><li>Wert</li>...</ul> --%>
							response($j.map($j("li", data), function(item) {
								var t = $j(item).text();
								return {label: t, value:t};
							}));
						}
					});
				},
			minLength: 2 ,
			select: 
				function(){
					var vzgNr = $j("#strecke").val();
					var bsVon = $j("#betriebsstelleVonId");

					var defaultValue = "";
					var defaultMessage = '(<bean:message key="common.select.option" />)';

					bsVon.removeOption(/.*/);
					bsVon.addOption(defaultValue,defaultMessage);
					bsVon.ajaxAddOption("<c:url value='/AutoCompleteBetriebstellenByStrecke.view'/>",
						{ "vzgStrecke" : vzgNr, "onlyZugmeldestellen" : true },
						false,
						function(){
							var bsBis = $j("#betriebsstelleBisId");

							bsBis.removeOption(/.*/);
							bsBis.addOption(defaultValue,defaultMessage);
							$j(this).copyOptions(bsBis,"all");
							bsBis.selectOptions(defaultValue);
							new Effect.Highlight('betriebsstelleBisId', { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });
							
							var topProjekt = $j("#topprojektId");
							topProjekt.removeOption(/.*/);
							topProjekt.addOption(defaultValue,defaultMessage);
							topProjekt.ajaxAddOption("<c:url value='/AutoCompleteTopProjekteByStrecke.view'/>",
								{ "vzgStreckeNummer" : vzgNr, "fahrplanjahr" : $j("#fahrplanjahr").val() }, false);
							topProjekt.selectOptions(defaultValue);
							new Effect.Highlight('topprojektId', { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });
						}
					);
					bsVon.selectOptions(defaultValue);
					new Effect.Highlight('betriebsstelleVonId', { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });

					var bsArb = $j("#arbeitenOrtId");
					bsArb.removeOption(/.*/);
					bsArb.addOption(defaultValue,defaultMessage);
					bsArb.ajaxAddOption("<c:url value='/AutoCompleteBetriebstellenByStrecke.view'/>",
						{ "vzgStrecke" : vzgNr, "onlyZugmeldestellen" : false },
						false,
						function(){
						}
					);
					bsArb.selectOptions(defaultValue);
					new Effect.Highlight('arbeitenOrtId', { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });
				}
		});
	});
</script>

<c:choose>
	<c:when test="${massnahme != null}">
		<jsp:include page="editMassnahmeHeader.jsp" />
		<jsp:include page="editMassnahmeTabs.jsp" />
	</c:when>
	<c:otherwise>
		<div class="textcontent_head">	<bean:message key="massnahme" /></div>
	</c:otherwise>
</c:choose>

<html:form action="/osb/massnahme/save">
	<html:hidden property="massnahmeId" />
	<html:hidden property="regionalbereichId" />
	
	<div class="textcontent">
		<div class="box">
			<div class="input">
				<c:choose>
					<c:when test="${entwurfDisabled == true}">
						<input type="checkbox" name="entwurfTemp" class="checkbox" disabled="disabled" />
						<html:hidden property="entwurf" />
						<bean:message key="sperrpausenbedarf.entwurf" />
					</c:when>
					<c:otherwise>
						<html:checkbox styleId="entwurf" property="entwurf" styleClass="checkbox" errorStyle="${errorStyle}" />
						<label for="entwurf"><span style="color:red;"><bean:message key="sperrpausenbedarf.entwurf" /></span></label>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="input" style="margin-left:45px;">
				<c:choose>
					<c:when test="${studieDisabled == true}">
						<c:choose>
							<c:when test="${massnahmeForm.studie == true}"><input type="checkbox" name="studieTemp" class="checkbox" disabled="disabled" checked="checked" /></c:when>
							<c:otherwise><input type="checkbox" name="studieTemp" class="checkbox" disabled="disabled" /></c:otherwise>
						</c:choose>
						<html:hidden property="studie" />
					</c:when>
					<c:otherwise>
						<html:checkbox styleId="studie" property="studie" styleClass="checkbox" errorStyle="${errorStyle}" />
					</c:otherwise>
				</c:choose>
				<label for="studie"><bean:message key="sperrpausenbedarf.studie" /></label>
			</div>
		
			<div class="label" style="width:60px;margin-left:90px;"><label for="finanztypId"><bean:message key="sperrpausenbedarf.finanztyp" />*</label></div>
			<div class="input">
				<html:select property="finanztypId" styleId="finanztypId" style="width:165px;" errorStyle="${errorStyle}">
					<html:option value="">(<bean:message key="common.select.option" />)</html:option>
					<html:optionsCollection name="finanztypList" label="name" value="id" />
				</html:select>
			</div>
		
			<div class="input" style="margin-left:110px;">
				<html:checkbox styleId="geaendert" property="geaendert" styleClass="checkbox" errorStyle="${errorStyle}" />
				<label for="geaendert"><bean:message key="sperrpausenbedarf.geaendert.short" /></label>
			</div>
		
			<div class="input" style="margin-left:40px;">
				<html:checkbox styleId="gravierendeAenderung" property="gravierendeAenderung" styleClass="checkbox" errorStyle="${errorStyle}" />
				<label for="gravierendeAenderung"><bean:message key="sperrpausenbedarf.gravierendeAenderung.short" /></label>
			</div>
		</div>
		
		<hr/>
		<%--<bean:message key="sperrpausenbedarf.tab.oertlichkeit"/>--%>
		
		<div class="box">
			<div class="label"><label for="phaseId"><bean:message key="sperrpausenbedarf.phase" />*</label></div>
			<div class="input">
				<html:select property="phaseId" styleId="phaseId" errorStyle="${errorStyle}" onchange="initializeGenehmigungsanforderung();">
					<html:option value="">(<bean:message key="common.select.option" />)</html:option>
					<html:optionsCollection name="phaseList" label="caption" value="id" />
				</html:select>
			</div>
			<div class="input" style="margin-left:30px;">
				<html:hidden styleId="genehmigungsanforderung" property="genehmigungsanforderung" />
				<input type="checkbox" class="checkbox" name="genehmigungsanforderungTemp" id="genehmigungsanforderungTemp" onchange="setHiddenGenehmigungsanforderung(this);" />
				<label for="genehmigungsanforderung"><bean:message key="sperrpausenbedarf.genehmigungsanforderung.short" /></label>
			</div>
			<div class="input" style="margin-left:30px;">
				<html:checkbox styleId="bbei" property="bbei" styleClass="checkbox" errorStyle="${errorStyle}"></html:checkbox>
				<label for="bbei"><bean:message key="sperrpausenbedarf.bbei" /></label>
			</div>
		</div>

		<div class="box">
			<div class="label"><label for="strecke"><bean:message key="sperrpausenbedarf.hauptStrecke" />*</label></div>
			<div class="input"><html:text property="strecke" styleId="strecke" styleClass="long" errorStyle="${errorStyle}"></html:text></div>
		</div>
		
		<div class="textcontent_left">
			<div class="box">
				<div class="label"><label for="betriebsstelleVonId"><bean:message key="sperrpausenbedarf.betriebsstelle.von" />*</label></div>
				<div class="input">
					<html:select property="betriebsstelleVonId" styleId="betriebsstelleVonId" errorStyle="${errorStyle}">
						<html:option value="">(<bean:message key="common.select.option" />)</html:option>
						<html:optionsCollection name="betriebsstellen" value="id" label="caption"/>
					</html:select>
				</div>
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
		
		<div class="textcontent_right">
			<div class="box">
				<div class="label"><label for="kmVon"><bean:message key="sperrpausenbedarf.kmVon" /></label></div>
				<div class="input"><html:text property="kmVon" styleId="kmVon" styleClass="small" errorStyle="${errorStyle}" /></div>
			</div>
			<div class="box">
				<div class="label"><label for="kmBis"><bean:message key="sperrpausenbedarf.kmBis" /></label></div>
				<div class="input"><html:text property="kmBis" styleId="kmBis" styleClass="small" errorStyle="${errorStyle}" /></div>
			</div>
		</div>
			
		<div class="box" style="margin-bottom:30px;">
			<div class="label"></div>
			<div class="label" style="width:90px;">
				<html:radio property="richtungskennzahl" value="1" styleClass="checkbox" errorStyle="${errorStyle}"><bean:message key="sperrpausenbedarf.richtungsKennzahl.1" /></html:radio>
			</div>
			<div class="label" style="width:125px;">
				<html:radio property="richtungskennzahl" value="2" styleClass="checkbox" errorStyle="${errorStyle}"><bean:message key="sperrpausenbedarf.richtungsKennzahl.2" /></html:radio>
			</div>
			<div class="label" style="width:150px;">
				<html:radio property="richtungskennzahl" value="0" styleClass="checkbox" errorStyle="${errorStyle}"><bean:message key="sperrpausenbedarf.richtungsKennzahl.0" /></html:radio>
			</div>
		</div>
		
		<%--<bean:message key="common.zeitraum" />--%>
		
		<div class="textcontent_left">
			<div class="box">
				<div class="label"><label for="beginn"><bean:message key="sperrpausenbedarf.bauterminStart" />*</label></div>
				<div class="input">
					<html:text property="beginn" styleId="beginn" styleClass="datetime" maxlength="16" errorStyle="${errorStyle}" />
					<img src="<c:url value='/static/img/calendar.gif' />" id="buttonBeginn" />
				</div>
				<div class="input" style="margin-left:40px;">
					<html:checkbox styleId="durchgehend" property="durchgehend" styleClass="checkbox" errorStyle="${errorStyle}"></html:checkbox>
					<label for="durchgehend"><bean:message key="sperrpausenbedarf.durchgehend.short" />*</label>
				</div>
			</div>		
			
			<div class="box">
				<div class="label"><label for="ende"><bean:message key="sperrpausenbedarf.bauterminEnde" />*</label></div>
				<div class="input">
					<html:text property="ende" styleId="ende" styleClass="datetime" errorStyle="${errorStyle}" />
					<img src="<c:url value='/static/img/calendar.gif' />" id="buttonEnde" />
				</div>
				<div class="input" style="margin-left:40px;">
					<html:checkbox styleId="schichtweise" property="schichtweise" styleClass="checkbox" errorStyle="${errorStyle}"></html:checkbox>
					<label for="schichtweise"><bean:message key="sperrpausenbedarf.schichtweise.short" />*</label>
				</div>
			</div>
		</div>
		
		<div class="textcontent_right">
			<div class="box">
				<div class="label"><label for="fahrplanjahr"><bean:message key="common.fahrplanjahr" />*</label></div>
				<div class="input"><html:text property="fahrplanjahr" styleId="fahrplanjahr" style="width:25px;" maxlength="4" errorStyle="${errorStyle}" /></div>
			</div>
			<div class="box">
				<div class="label" style="width:95px;"><bean:message key="vtr.short" /></div>
				<%--
				<div class="input">
					<html:select property="vtrId" styleId="vtrId" errorStyle="${errorStyle}" style="width:130px;">
						<html:option value="">(<bean:message key="common.select.option" />)</html:option>
						<html:optionsCollection name="vtrList" label="caption" value="id" />
					</html:select>
				</div>
				--%>
				<div class="input" style="width:37px;"><html:checkbox property="wtsMo" styleClass="checkbox" /><bean:message key="vtr.montag.short" /></div>
				<div class="input" style="width:35px;"><html:checkbox property="wtsDi" styleClass="checkbox" /><bean:message key="vtr.dienstag.short" /></div>
				<div class="input" style="width:36px;"><html:checkbox property="wtsMi" styleClass="checkbox" /><bean:message key="vtr.mittwoch.short" /></div>
				<div class="input" style="width:37px;"><html:checkbox property="wtsDo" styleClass="checkbox" /><bean:message key="vtr.donnerstag.short" /></div>
				<div class="input" style="width:36px;"><html:checkbox property="wtsFr" styleClass="checkbox" /><bean:message key="vtr.freitag.short" /></div>
				<div class="input" style="width:37px;"><html:checkbox property="wtsSa" styleClass="checkbox" /><bean:message key="vtr.samstag.short" /></div>
				<div class="input"><html:checkbox property="wtsSo" styleClass="checkbox" /><bean:message key="vtr.sonntag.short" /></div>
			</div>
		</div>

		<div class="textcontent_left">
			<div class="box">
				<div class="label"><label for="topprojektId"><bean:message key="sperrpausenbedarf.sapProjektNummer.short" />*</label></div>
				<div class="input">
					<html:select property="topprojektId" styleId="topprojektId" errorStyle="${errorStyle}">
						<html:option value="">(<bean:message key="common.select.option" />)</html:option>
						<html:optionsCollection name="topProjekte" value="id" label="caption" />
					</html:select>
				</div>
			</div>
		</div>
		
		<div class="textcontent_right">
			<div class="box">
				<div class="label"><label for="technischerPlatz"><bean:message key="sperrpausenbedarf.technischerPlatz" /></label></div>
				<div class="input"><html:text property="technischerPlatz" styleId="technischerPlatz" maxlength="255" errorStyle="${errorStyle}" /></div>
			</div>
		</div>

		<hr/>
		<%--<bean:message key="sperrpausenbedarf.anmeldung" />--%>
			
		<div class="textcontent_left">
			<div class="box">
				<div class="label"><label for="anmelderId"><bean:message key="sperrpausenbedarf.anmelder" />*</label></div>
				<div class="input">
					<html:select property="anmelderId" styleId="anmelderId" errorStyle="${errorStyle}">
						<html:option value="">(<bean:message key="common.select.option" />)</html:option>
						<html:optionsCollection name="anmelderList" label="name" value="id" />
					</html:select>
				</div>
			</div>
		</div>
		
		<div class="textcontent_right">
			<div class="box">
				<div class="label"><label for="datumAnmeldung"><bean:message key="sperrpausenbedarf.anmeldedatum" />*</label></div>
				<div class="input">
					<html:text property="datumAnmeldung" styleId="datumAnmeldung" styleClass="date" maxlength="10" errorStyle="${errorStyle}" />
					<img src="<c:url value='/static/img/calendar.gif' />" id="buttonDatumAnmeldung" />
				</div>
			</div>
		</div>
		
		<div class="box">
			<div class="label"><label for="kommentarAnmelder"><bean:message key="sperrpausenbedarf.kommentar" /></label></div>
			<div class="input"><html:text property="kommentarAnmelder" styleId="kommentarAnmelder" styleClass="long" maxlength="255" errorStyle="${errorStyle}" /></div>
		</div>
	
		<div class="box">
			<div class="label"><label for="ergaenzungAnmelder"><bean:message key="sperrpausenbedarf.ergaenzungAnmelder" /></label></div>
			<div class="input"><html:text property="ergaenzungAnmelder" styleId="ergaenzungAnmelder" styleClass="long" maxlength="32" errorStyle="${errorStyle}" /></div>
		</div>

		<hr/>

		<%--<bean:message key="sperrpausenbedarf.arbeiten" />--%>
		
		<div class="textcontent_left">
			<div class="box">
				<div class="label"><label for="arbeitenId"><bean:message key="sperrpausenbedarf.arbeiten" />*</label></div>
				<div class="input">
					<html:select property="arbeitenId" styleId="arbeitenId" errorStyle="${errorStyle}">
						<html:option value="">(<bean:message key="common.select.option" />)</html:option>
						<html:optionsCollection name="arbeitstypList" label="caption" value="id" />
					</html:select>
				</div>
			</div>
		</div>

		<div class="textcontent_right">
			<div class="box">
				<div class="label"><label for="arbeitenOrtId"><bean:message key="sperrpausenbedarf.arbeitenOrt" /></label></div>
				<div class="input">
					<html:select property="arbeitenOrtId" styleId="arbeitenOrtId" errorStyle="${errorStyle}">
						<html:option value="">(<bean:message key="common.select.option" />)</html:option>
						<html:optionsCollection name="betriebsstellenArbeiten" value="id" label="caption"/>
					</html:select>
				</div>
			</div>
		</div>
		
		<div class="box">
			<div class="label"><label for="arbeitenKommentar"><bean:message key="sperrpausenbedarf.arbeitenKommentar" /></label></div>
			<div class="input"><html:text property="arbeitenKommentar" styleId="arbeitenKommentar" maxlength="32" styleClass="long" errorStyle="${errorStyle}" /></div>
		</div>

		<hr/>
		
		<%--<bean:message key="sperrpausenbedarf.folgeNichtausfuehrung" />--%>
		
		<div class="textcontent_left">
			<div class="box">
				<div class="label"><label for="folgeNichtausfuehrungId"><bean:message key="sperrpausenbedarf.folgeNichtausfuehrung" />*</label></div>
				<div class="input">
					<html:select property="folgeNichtausfuehrungId" styleId="folgeNichtausfuehrungId" errorStyle="${errorStyle}">
						<html:option value="">(<bean:message key="common.select.option" />)</html:option>
						<html:optionsCollection name="folgeNichtausfuehrungList" label="name" value="id" />
					</html:select>
				</div>
			</div>
			
			<div class="box">
				<div class="label"><label for="folgeNichtausfuehrungBeginn"><bean:message key="sperrpausenbedarf.folgeNichtausfuehrungBeginn" /></label></div>
				<div class="input">
					<html:text property="folgeNichtausfuehrungBeginn" styleId="folgeNichtausfuehrungBeginn" styleClass="date" maxlength="10" errorStyle="${errorStyle}" />
					<img src="<c:url value='/static/img/calendar.gif' />" id="buttonFolgeNichtausfuehrungBeginn" />
				</div>
			</div>
			
			<div class="box">
				<div class="label"><label for="folgeNichtausfuehrungFzv"><bean:message key="sperrpausenbedarf.folgeNichtausfuehrungFzv" /></label></div>
				<div class="input"><html:text property="folgeNichtausfuehrungFzv" styleId="folgeNichtausfuehrungFzv" styleClass="small" errorStyle="${errorStyle}" /></div>
			</div>
		
			<div class="box">
				<div class="label"><label for="folgeNichtausfuehrungGeschwindigkeitLa"><bean:message key="sperrpausenbedarf.folgeNichtausfuehrungGeschwindigkeitLa" /></label></div>
				<div class="input"><html:text property="folgeNichtausfuehrungGeschwindigkeitLa" styleId="folgeNichtausfuehrungGeschwindigkeitLa" styleClass="small" errorStyle="${errorStyle}" /></div>
			</div>
		</div>
		
		<div class="textcontent_right">
			<div class="box">
				<div class="label"><label for="grossmaschineId"><bean:message key="sperrpausenbedarf.grossmaschine" /></label></div>
				<div class="input">
					<html:select property="grossmaschineId" styleId="grossmaschineId" errorStyle="${errorStyle}">
						<html:option value="">(<bean:message key="common.select.option" />)</html:option>
						<html:optionsCollection name="grossmaschineList" label="name" value="id" />
					</html:select>
				</div>
			</div>
			
			<div class="box">
				<div class="label"><label for="statusBbzr"><bean:message key="sperrpausenbedarf.statusbbzr.short" />*</label></div>
				<div class="input">
					<html:select property="statusBbzr" styleId="statusBbzr" errorStyle="${errorStyle}">
						<html:option value="">(<bean:message key="common.select.option" />)</html:option>
						<c:forEach var="statusBbzrItem" items="${statusBbzrList}">
							<html:option value="${statusBbzrItem}">
								<bean:message key="statusbbzr.${statusBbzrItem}" />
							</html:option>
						</c:forEach>
					</html:select>
				</div>
			</div>
			
			<div class="box">
				<div class="label"><label for="hervorhebFarbe"><bean:message key="sperrpausenbedarf.hervorhebfarbe" /></label></div>
				<div class="input"><html:select property="hervorhebFarbe" styleId="hervorhebFarbe" styleClass="small" errorStyle="${errorStyle}" /></div>
			</div>
			
			<div class="box">
				<div class="input">
					<html:checkbox styleId="laEintrag406" property="laEintrag406" styleClass="checkbox" errorStyle="${errorStyle}"></html:checkbox>
					<label for="laEintrag406"><bean:message key="sperrpausenbedarf.laEintrag406.short" /></label>
				</div>
				<div class="input" style="margin-left:30px;">
					<html:checkbox styleId="lueHinweis" property="lueHinweis" styleClass="checkbox" errorStyle="${errorStyle}"></html:checkbox>
					<label for="lueHinweis"><bean:message key="sperrpausenbedarf.lueHinweis.short" /></label>
				</div>
			</div>
		</div>
		
		<hr/>

		<div class="box">
			<div class="label"><label for="vorbedingungId"><bean:message key="sperrpausenbedarf.vorbedingung" /></label></div>
			<div class="input">
				<html:select property="vorbedingungId" styleId="vorbedingungId" errorStyle="${errorStyle}" styleClass="long">
					<html:option value="">(<bean:message key="common.select.option" />)</html:option>
				</html:select>
			</div>
		</div>
		
		<div class="box">
			<div class="label"><label for="unterdeckungId"><bean:message key="sperrpausenbedarf.unterdeckung" /></label></div>
			<div class="input">
				<html:select property="unterdeckungId" styleId="unterdeckungId" errorStyle="${errorStyle}" styleClass="long">
					<html:option value="">(<bean:message key="common.select.option" />)</html:option>
				</html:select>
			</div>
		</div>
		
		<div class="box">
			<div class="label"><label for="ankermassnahmeId"><bean:message key="sperrpausenbedarf.ankermassnahme.short" /></label></div>
			<div class="input">
				<html:select property="ankermassnahmeId" styleId="ankermassnahmeId" errorStyle="${errorStyle}" styleClass="long">
					<html:option value="">(<bean:message key="common.select.option" />)</html:option>
				</html:select>
			</div>
		</div>
	</div>
	<div class="buttonBar">
		<html:link action="/back" styleClass="buttonBack">
			<bean:message key="button.back" />
		</html:link>
		
		<html:link action="/osb/massnahme/save" onclick="dataChanged=false;$('massnahmeForm').submit();return false;" styleClass="buttonSave">
			<bean:message key="button.save" />
		</html:link>
	</div>
</html:form>

<script type="text/javascript">
	var dataChanged = false;
	initDataChangeListener('massnahmeForm');
	window.onbeforeunload = changeChecker;
	function changeChecker(){
		if (dataChanged)
			return '<bean:message key="common.confirmChange" />';
	}

	function initializeGenehmigungsanforderung(){
		var phase = $('phaseId');
		var checkbox = $('genehmigungsanforderungTemp');
		var hidden = $('genehmigungsanforderung');
		
		// Ist keine Auswahl getroffen, muss Checkbox frei waehlbar sein
		if(phase.value == 0 || phase.value == ""){
			checkbox.disabled = false;
		}
		else if(phase.value > 0 && phase.value < 5){
			checkbox.checked = true;
			checkbox.disabled = true;
			hidden.value = true;
		}
		// Bei C=6 immer negativ
		else if(phase.value == 6){
			checkbox.checked = false;
			checkbox.disabled = true;
			hidden.value = false;
		}
		// Bei B=5 hat der Benutzer die Wahl
		else if(phase.value == 5){
			checkbox.disabled = false;
			if(checkbox.checked == true)
				hidden.value = true;
			else
				hidden.value = false;
		}
	}
	// Initialer Aufruf
	initializeGenehmigungsanforderung();
	
	function setHiddenGenehmigungsanforderung(checkbox){
		var phase = $('phaseId');
		var hidden = $('genehmigungsanforderung');
		if(phase.value == 5){
			if(checkbox.checked == true)
				hidden.value = true;
			else
				hidden.value = false;
		}		
	}
	
	Calendar.setup({
		inputField  : "datumAnmeldung",
		ifFormat    : "%d.%m.%Y",
		button      : "buttonDatumAnmeldung"
	});
	Calendar.setup({
    	inputField  : "beginn",
    	ifFormat    : "%d.%m.%Y %H:%M",
    	button      : "buttonBeginn",
       	showsTime	: true
	});
	Calendar.setup({
    	inputField  : "ende",
    	ifFormat    : "%d.%m.%Y %H:%M",
    	button      : "buttonEnde",
    	showsTime	: true
	});
	Calendar.setup({
    	inputField  : "folgeNichtausfuehrungBeginn",
    	ifFormat    : "%d.%m.%Y",
    	button      : "buttonFolgeNichtausfuehrungBeginn"
	});
</script>

<jsp:include page="/pages/main_footer.jsp" />