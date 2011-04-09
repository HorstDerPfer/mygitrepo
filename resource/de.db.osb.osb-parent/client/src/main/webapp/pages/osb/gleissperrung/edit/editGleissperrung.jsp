<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
<html:xhtml/>

<jsp:include page="/pages/main_head.jsp"/>
<jsp:include page="/pages/main_path.jsp"/>
<jsp:include page="/pages/main_menu.jsp"/>

<c:set var="urlVzgStrecke"><c:url value='/AutoCompleteStreckeVzg.view'/></c:set>
<c:set var="urlBetriebsstellen"><c:url value='/AutoCompleteBetriebstellenByStrecke.view'/></c:set>

<script type="text/javascript">
    openMainMenu('navLink_osb_workflow-gleissperrung');

	<%-- JQuery UI Autocomplete für Strecke registrieren --%>
	$j(function() {
		$j("#vzgStrecke").autocomplete({
			source:
				function(request, response) {
					$j.ajax({
						url: "${urlVzgStrecke}",
						datatype: "html",
						data: {
							<%-- Parameter, die bei AJAX Request übergeben werden --%>
							nummer: request.term
							<%-- ,regionalbereichId : "${loginUser.regionalbereich.id}" --%>
						},
						success: function(data) {
							<%-- Ergebnis des Requests in Autocomplete-Response-Objekt umbauen --%>
							<%-- erwartet: <ul><li>Wert</li>...</ul> --%>
							response($j.map($j("li", data), function(item) {
								var t = $j(item).text();
								return {label: t, value:t};
							}));
						},
						error: function(){
							var defaultValue = "";
							var defaultMessage = '(<bean:message key="common.select.option" />)';

							var bsVon = $j("#bstVonId");
							bsVon.removeOption(/.*/);
							bsVon.addOption(defaultValue,defaultMessage);
							bsVon.selectOptions(defaultValue);
							new Effect.Highlight('bstVonId', { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });
							
							var bsBis = $j("#bstBisId");
							bsBis.removeOption(/.*/);
							bsBis.addOption(defaultValue,defaultMessage);
							bsBis.selectOptions(defaultValue);
							new Effect.Highlight('bstBisId', { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });

							var bsVonKo = $j("#bstVonKoordiniertId");
							bsVonKo.removeOption(/.*/);
							bsVonKo.addOption(defaultValue,defaultMessage);
							bsVonKo.selectOptions(defaultValue);
							new Effect.Highlight('bstVonKoordiniertId', { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });
							
							var bsBisKo = $j("#bstBisKoordiniertId");
							bsBisKo.removeOption(/.*/);
							bsBisKo.addOption(defaultValue,defaultMessage);
							bsBisKo.selectOptions(defaultValue);
							new Effect.Highlight('bstBisKoordiniertId', { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });
						}
					});
				},
			minLength: 2,
			select: 
				function(){
					var vzgNr = $j("#vzgStrecke").val();
					var bsVon = $j("#bstVonId");

					var defaultValue = "";
					var defaultMessage = '(<bean:message key="common.select.option" />)';

					bsVon.removeOption(/.*/);
					bsVon.addOption(defaultValue,defaultMessage);
					bsVon.ajaxAddOption("${urlBetriebsstellen}",
						{ "vzgStrecke" : vzgNr, "onlyZugmeldestellen" : true },
						false,
						function(){
							var bsBis = $j("#bstBisId");
							bsBis.removeOption(/.*/);
							bsBis.addOption(defaultValue,defaultMessage);
							$j(this).copyOptions(bsBis,"all");
							bsBis.selectOptions(defaultValue);
							new Effect.Highlight('bstBisId', { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });

							var bsVonKo = $j("#bstVonKoordiniertId");
							bsVonKo.removeOption(/.*/);
							bsVonKo.addOption(defaultValue,defaultMessage);
							$j(this).copyOptions(bsVonKo,"all");
							bsVonKo.selectOptions(defaultValue);
							new Effect.Highlight('bstVonKoordiniertId', { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });

							var bsBisKo = $j("#bstBisKoordiniertId");
							bsBisKo.removeOption(/.*/);
							bsBisKo.addOption(defaultValue,defaultMessage);
							$j(this).copyOptions(bsBisKo,"all");
							bsBisKo.selectOptions(defaultValue);
							new Effect.Highlight('bstBisKoordiniertId', { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });
						}
					);
					bsVon.selectOptions(defaultValue);
					new Effect.Highlight('bstVonId', { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });
				}
		});
	});    
</script>

<html:form action="/osb/saveGleissperrung" acceptCharset="UTF-8">
	<html:hidden property="gleissperrungId" />
	
	<div class="textcontent_head">
		<span style="float:left"><bean:message key="gleissperrung" /></span>
		<c:if test="${gleissperrung != null}">
			<c:if test="${gleissperrung.bezeichnung != null}">
				<span style="float:left">: ${gleissperrung.bezeichnung}</span>
			</c:if>
			<span style="float:right;">
				<c:if test="${gleissperrung.deleted == true}">
					<bean:message key="common.deleted" />&nbsp;-&nbsp;
				</c:if>
				<bean:message key="common.lastchangedate" />:&nbsp;
				<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${gleissperrung.lastChangeDate}" />
			</span>
		</c:if>
	</div>

	<div class="textcontent">
		<div class="textcontent_left">
			<div class="box">
				<div class="label"><bean:message key="gleissperrung.lfd" /></div>
				<div class="show">${gleissperrung.lfdNr}</div>
			</div>
			<div class="box">
				<div class="label"><bean:message key="regelung.fahrplanjahr" />*</div>
				<div class="input"><html:text styleClass="year" property="fahrplanjahr" styleId="fahrplanjahr" errorStyle="${errorStyle}" /></div>
			</div>
			<div class="box">
				<div class="label"><bean:message key="gleissperrung.betriebsweise" /></div>
				<div class="input">
					<html:select property="betriebsweiseId">
						<html:option value=""><bean:message key="common.select.option" /></html:option>
						<html:optionsCollection name="betriebsweiseList" label="name" value="id" />
					</html:select>
				</div>
			</div>
			<div class="box">
				<div class="label"><bean:message key="gleissperrung.sperrpausenbedarf" /></div>
				<div class="input">
					<html:text property="sperrpausenbedarf" styleId="sperrpausenbedarf" style="width:35px;" errorStyle="width:35px;${errorStyle}" maxlength="6" />
					<html:select property="sperrpausenbedarfArt" style="width:105px;" errorStyle="width:105px;${errorStyle}">
						<html:option value=""><bean:message key="common.select.option" /></html:option>
						<html:optionsCollection name="sperrpausenbedarfArten" />
					</html:select>
				</div>
			</div>
			<div class="box">
				<div class="label"><bean:message key="gleissperrung.auswirkung" /></div>
				<div class="input">
					<html:select property="auswirkung">
						<html:option value=""><bean:message key="common.select.option" /></html:option>
						<html:option value="RUHE"><bean:message key="sperrung.auswirkung.RUHE" /></html:option>
						<html:option value="UMLEITUNG"><bean:message key="sperrung.auswirkung.UMLEITUNG" /></html:option>
						<html:option value="SEV"><bean:message key="sperrung.auswirkung.SEV" /></html:option>
					</html:select>
				</div>
			</div>
			<div class="box">
				<div class="label"><bean:message key="gleissperrung.bauLue" /></div>
				<div class="input"><html:checkbox styleClass="checkbox" property="bauLue" styleId="bauLue" /></div>
			</div>
			<div class="box">
				<div class="label"><bean:message key="regelung.kommentar" /></div>
				<div class="input"><html:textarea property="kommentar" styleId="kommentar" style="height:92px;" /></div>
			</div>
		</div>
		
		<div class="textcontent_right">
			<div class="box">
				<div class="label">
					<label for="vzgStrecke"><bean:message key="regelung.vzgStrecke" />*</label>
				</div>
				<div class="input"><html:text property="vzgStrecke" styleId="vzgStrecke" errorStyle="${errorStyle}" /></div>
			</div>
			
			<div class="box">
				<div class="label"><bean:message key="gleissperrung.richtungsKennzahl" /></div>
				<div class="input">
					<html:select property="richtungsKennzahl">
						<html:option value="0"><bean:message key="gleissperrung.richtungsKennzahl.0" /></html:option>
						<html:option value="1"><bean:message key="gleissperrung.richtungsKennzahl.1" /></html:option>
						<html:option value="2"><bean:message key="gleissperrung.richtungsKennzahl.2" /></html:option>
					</html:select>
				</div>
			</div>
			
			<div class="box">
				<div class="label"><label for="bstVonId"><bean:message key="regelung.bstVon" />*</label></div>
				<div class="input">
					<html:select property="bstVonId" styleId="bstVonId" errorStyle="${errorStyle}">
						<html:option value="">(<bean:message key="common.select.option" />)</html:option>
						<html:optionsCollection name="betriebsstellen" value="id" label="caption"/>
					</html:select>
				</div>
			</div>
			<div class="box">
				<div class="label"><label for="bstBisId"><bean:message key="regelung.bstBis" />*</label></div>
				<div class="input">
					<html:select property="bstBisId" styleId="bstBisId" errorStyle="${errorStyle}">
						<html:option value="">(<bean:message key="common.select.option" />)</html:option>
						<html:optionsCollection name="betriebsstellen" value="id" label="caption"/>
					</html:select>
				</div>
			</div>
			
			<div class="box">
				<div class="label"><bean:message key="gleissperrung.kmVon" /></div>
				<div class="input"><html:text styleClass="small" property="kmVon" styleId="kmVon" errorStyle="${errorStyle}" /></div>
			</div>
			<div class="box">
				<div class="label"><bean:message key="gleissperrung.kmBis" /></div>
				<div class="input"><html:text styleClass="small" property="kmBis" styleId="kmBis" errorStyle="${errorStyle}" /></div>
			</div>
			<div class="box">
				<div class="label"><bean:message key="gleissperrung.sigWeicheVon" /></div>
				<div class="input"><html:text property="sigWeicheVon" styleId="sigWeicheVon" errorStyle="${errorStyle}" /></div>
			</div>
			<div class="box">
				<div class="label"><bean:message key="gleissperrung.sigWeicheBis" /></div>
				<div class="input"><html:text property="sigWeicheBis" styleId="sigWeicheBis" errorStyle="${errorStyle}" /></div>
			</div>

			<div class="box">
				<div class="label"><label for="bstVonKoordiniertId"><bean:message key="regelung.bstVonKoordiniert" /></label></div>
				<div class="input">
					<html:select property="bstVonKoordiniertId" styleId="bstVonKoordiniertId" errorStyle="${errorStyle}">
						<html:option value="">(<bean:message key="common.select.option" />)</html:option>
						<html:optionsCollection name="betriebsstellen" value="id" label="caption"/>
					</html:select>
				</div>
			</div>
			<div class="box">
				<div class="label"><label for="bstBisKoordiniertId"><bean:message key="regelung.bstBisKoordiniert" /></label></div>
				<div class="input">
					<html:select property="bstBisKoordiniertId" styleId="bstBisKoordiniertId" errorStyle="${errorStyle}">
						<html:option value="">(<bean:message key="common.select.option" />)</html:option>
						<html:optionsCollection name="betriebsstellen" value="id" label="caption"/>
					</html:select>
				</div>
			</div>
		</div>
	</div>
	<div class="buttonBar">
		<html:link action="/back.do" styleClass="buttonBack"><bean:message key="button.back" /></html:link>

		<c:if test="${gleissperrung != null}">
			<easy:hasAuthorization authorization="ROLE_GLEISSPERRUNG_LOESCHEN" model="${gleissperrung}">
				<c:choose>
					<c:when test="${gleissperrung.deleted == true}">
						<html:link action="/osb/deleteGleissperrung" styleClass="buttonReload">
							<html:param name="gleissperrungId" value="${gleissperrung.id}" />
							<html:param name="delete" value="false" />
							<bean:message key="button.undelete" />
						</html:link>
					</c:when>
					<c:when test="${gleissperrung.deleted == false}">
						<bean:define id="confirmText" toScope="page"><bean:message key="confirm.gleissperrung.delete" /></bean:define>
						<html:link action="/osb/deleteGleissperrung" styleClass="buttonDelete" onclick="return confirmLink(this.href, '${confirmText}');">
							<html:param name="gleissperrungId" value="${gleissperrung.id}" />
							<html:param name="delete" value="true" />
							<bean:message key="button.delete" />
						</html:link>
					</c:when>
				</c:choose>
			</easy:hasAuthorization>
		</c:if>
		
		<html:link href="#" onclick="$('gleissperrungForm').submit();" styleClass="buttonSave">
			<bean:message key="button.save" />
		</html:link>
	</div>
	
	<br/>
	
	<%-- Karteireiter --------------------------------------------------------------------------------------------------- --%>
	<input type="hidden" id="tab" name="tab"
		value="${tab == null ? 'Terminplanung' : tab}" />
	<c:set var="backButtonLink" value="/osb/viewGleissperrung.do?gleissperrungId=${gleissperrung.id}" />
	
	<html:link href="#" onclick="javascript:showTabDiv('Terminplanung');setBackButtonLink('${backButtonLink}', 'Terminplanung');" styleId="tabLinkTerminplanung" styleClass="tab_act"><bean:message key="gleissperrung.tab.terminplanung" /></html:link>
	<html:link href="#" onclick="javascript:showTabDiv('Baustelle');setBackButtonLink('${backButtonLink}', 'Baustelle');" styleId="tabLinkBaustelle" styleClass="tab_ina"><bean:message key="gleissperrung.tab.baustelle" /></html:link>
	<html:link href="#" onclick="javascript:showTabDiv('Buendel');setBackButtonLink('${backButtonLink}', 'Buendel');" styleId="tabLinkBuendel" styleClass="tab_ina"><bean:message key="gleissperrung.tab.buendel" /></html:link>
	<html:link href="#" onclick="javascript:showTabDiv('Massnahmen');setBackButtonLink('${backButtonLink}', 'Massnahmen');" styleId="tabLinkMassnahmen" styleClass="tab_ina"><bean:message key="gleissperrung.tab.massnahmen" /></html:link>

	<div class="textcontent" id="tabDivTerminplanung" style="display: none;">
		<jsp:include page="editGleissperrungTerminplanung.jsp" />
	</div>
	
	<div class="textcontent center" id="tabDivBaustelle" style="display:none;">
		<jsp:include page="editGleissperrungBaustelle.jsp" />
	</div>
	
	<div class="textcontent center" id="tabDivBuendel" style="display:none;">
		<jsp:include page="editGleissperrungBuendel.jsp" />
	</div>
	
	<div class="textcontent center" id="tabDivMassnahmen" style="display:none;">
		<jsp:include page="editGleissperrungMassnahmen.jsp" />
	</div>
</html:form>

<script type="text/javascript">
	function setBackButtonLink(link, tab) {
		var e = $('backButton');
		if(e != null) {
			e.href = link + "&tab=" + tab;
		}
	}
	showTabDiv("${ ((tab != null) ? tab : 'Terminplanung') }");
</script>

<jsp:include page="/pages/main_footer.jsp"/>