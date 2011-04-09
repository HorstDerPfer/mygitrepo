<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>

<script type="text/javascript">
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
							beginn: $j("#beginn").val(),
							ende: $j("#ende").val()
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
			select: initializeDropdowns
		});
	});
	
	function initializeDropdowns(){
		var vzgNr = $j("#strecke").val();
		var bsVon = $j("#betriebsstelleVonId");

		var defaultValue = "";
		var defaultMessage = '(<bean:message key="common.select.option" />)';

		bsVon.removeOption(/.*/);
		bsVon.addOption(defaultValue,defaultMessage);
		bsVon.ajaxAddOption("<c:url value='/AutoCompleteBetriebstellenByStrecke.view'/>",
			{ "vzgStrecke" : vzgNr, 
			  "beginn" : $j("#beginn").val(), 
			  "ende" : $j("#ende").val(),
			  "onlyZugmeldestellen" : "true"
			},
			false,
			function(){
				var bsBis = $j("#betriebsstelleBisId");

				bsBis.removeOption(/.*/);
				bsBis.addOption(defaultValue,defaultMessage);
				$j(this).copyOptions(bsBis,"all");
				bsBis.selectOptions(defaultValue);
				new Effect.Highlight('betriebsstelleBisId', { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });
			}
		);
		bsVon.selectOptions(defaultValue);
		new Effect.Highlight('betriebsstelleVonId', { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });
	}
</script>

<html:hidden property="method" />
<html:hidden property="massnahmeId" />
<html:hidden property="regelungId" />

<div class="box">
	<div class="label"><label for="ungueltig"><bean:message key="regelung.ungueltig" /></label></div>
	<div class="input"><html:checkbox property="ungueltig" styleId="ungueltig" styleClass="checkbox" errorStyle="${errorStyle}" /></div>
</div>

<div class="box">
	<div class="label"><label for="strecke"><bean:message key="regelung.vzgStrecke" />*</label></div>
	<div class="input"><html:text property="strecke" styleId="strecke" styleClass="long" errorStyle="${errorStyle}"></html:text></div>
</div>

<div class="textcontent_left">
	<div class="box">
		<div class="label"><label for="betriebsstelleVonId"><bean:message key="regelung.bstVon" />*</label></div>
		<div class="input">
			<html:select property="betriebsstelleVonId" styleId="betriebsstelleVonId" errorStyle="${errorStyle}">
				<html:option value="">(<bean:message key="common.select.option" />)</html:option>
				<html:optionsCollection name="betriebsstellen" value="id" label="caption"/>
			</html:select>
		</div>
	</div>
	<div class="box">
		<div class="label"><label for="betriebsstelleBisId"><bean:message key="regelung.bstBis" />*</label></div>
		<div class="input">
			<html:select property="betriebsstelleBisId" styleId="betriebsstelleBisId" errorStyle="${errorStyle}">
				<html:option value="">(<bean:message key="common.select.option" />)</html:option>
				<html:optionsCollection name="betriebsstellen" value="id" label="caption"/>
			</html:select>
		</div>
	</div>
</div>

<div class="textcontent_right">
	<c:if test="${activeTab == 'editMassnahmeGleissperrung' || activeTab == 'editMassnahmeLangsamfahrstelle'}">
		<div class="box">
			<div class="label">
				<label for="kmVon">
					<c:if test="${activeTab == 'editMassnahmeGleissperrung'}">
						<bean:message key="gleissperrung.kmVon" />
					</c:if>
					<c:if test="${activeTab == 'editMassnahmeLangsamfahrstelle'}">
						<bean:message key="langsamfahrstelle.kmVon" />*
					</c:if>
				</label>
			</div>
			<div class="input"><html:text property="kmVon" styleId="kmVon" styleClass="small" errorStyle="${errorStyle}" /></div>
		</div>
		<div class="box">
			<div class="label">
				<label for="kmBis">
					<c:if test="${activeTab == 'editMassnahmeGleissperrung'}">
						<bean:message key="gleissperrung.kmBis" />
					</c:if>
					<c:if test="${activeTab == 'editMassnahmeLangsamfahrstelle'}">
						<bean:message key="langsamfahrstelle.kmBis" />*
					</c:if>
				</label>
			</div>
			<div class="input"><html:text property="kmBis" styleId="kmBis" styleClass="small" errorStyle="${errorStyle}" /></div>
		</div>
	</c:if>
</div>
	
<div class="box" style="margin-top:-2px;"></div>

<div class="textcontent_left">
	<div class="box">
		<div class="label"><label for="beginn"><bean:message key="regelung.zeitVon" />*</label></div>
		<div class="input">
			<html:text property="beginn" styleId="beginn" styleClass="datetime" maxlength="16" errorStyle="${errorStyle}" />
			<img src="<c:url value='/static/img/calendar.gif' />" id="buttonBeginn" />
		</div>

		<script type="text/javascript">
			Calendar.setup({
		    	inputField  : "beginn",
		    	ifFormat    : "%d.%m.%Y %H:%M",
		    	button      : "buttonBeginn",
		       	showsTime	: true
			});
		</script>

		<div class="input" style="margin-left:40px;">
			<html:checkbox styleId="durchgehend" property="durchgehend" styleClass="checkbox" errorStyle="${errorStyle}"></html:checkbox>
			<label for="durchgehend"><bean:message key="regelung.durchgehend.short" />*</label>
		</div>
	</div>
	
	<div class="box">
		<div class="label"><label for="ende"><bean:message key="regelung.zeitBis" />*</label></div>
		<div class="input">
			<html:text property="ende" styleId="ende" styleClass="datetime" errorStyle="${errorStyle}" />
			<img src="<c:url value='/static/img/calendar.gif' />" id="buttonEnde" />
		</div>

		<script type="text/javascript">
			Calendar.setup({
		    	inputField  : "ende",
		    	ifFormat    : "%d.%m.%Y %H:%M",
		    	button      : "buttonEnde",
		    	showsTime	: true
			});
		</script>

		<div class="input" style="margin-left:40px;">
			<html:checkbox styleId="schichtweise" property="schichtweise" styleClass="checkbox" errorStyle="${errorStyle}"></html:checkbox>
			<label for="schichtweise"><bean:message key="regelung.schichtweise.short" />*</label>
		</div>
	</div>
</div>

<div class="textcontent_right">
	<div class="box">
		<div class="label" style="width:95px;"><bean:message key="regelung.betroffen" /></div>
		<div class="input" style="width:72px;"><html:checkbox property="betroffenSpfv" styleClass="checkbox" /><bean:message key="regelung.betroffen.spfv" /></div>
		<div class="input" style="width:72px;"><html:checkbox property="betroffenSpnv" styleClass="checkbox" /><bean:message key="regelung.betroffen.spnv" /></div>
		<div class="input" style="width:72px;"><html:checkbox property="betroffenSgv" styleClass="checkbox" /><bean:message key="regelung.betroffen.sgv" /></div>
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

<c:if test="${activeTab == 'editMassnahmeGleissperrung'}">
	<div class="box">
		<div class="label"></div>
		<div class="label" style="width:90px;">
			<html:radio property="richtungskennzahl" value="1" styleClass="checkbox" errorStyle="${errorStyle}"><bean:message key="gleissperrung.richtungsKennzahl.1" /></html:radio>
		</div>
		<div class="label" style="width:125px;">
			<html:radio property="richtungskennzahl" value="2" styleClass="checkbox" errorStyle="${errorStyle}"><bean:message key="gleissperrung.richtungsKennzahl.2" /></html:radio>
		</div>
		<div class="label" style="width:150px;">
			<html:radio property="richtungskennzahl" value="0" styleClass="checkbox" errorStyle="${errorStyle}"><bean:message key="gleissperrung.richtungsKennzahl.0" /></html:radio>
		</div>
	</div>
</c:if>

<hr/>
