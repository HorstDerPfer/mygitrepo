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

<jsp:include page="../../../main_head.jsp"/>
<jsp:include page="../../../main_path.jsp"/>
<jsp:include page="../../../main_menu.jsp"/>

<%-- Öffnet Punkt in Startmenü --%>
<script type="text/javascript">
    openMainMenu('navLink_osb_workflow-sperrbedarf');
</script>

<html:form action="${saveAction}">
	<html:hidden property="sperrpausenbedarfId" value="${baumassnahme.id}" />
	<html:hidden property="fahrplanjahr" />

	<div class="textcontent_head">
		<span style="float:left"><bean:message key="sperrpausenbedarf.id" />: ${baumassnahme.massnahmeId}</span>
		<span style="float:right;">
			<bean:message key="common.lastchangedate" />:&nbsp;
			<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${baumassnahme.lastChangeDate}" />
		</span>
	</div>
	<div class="textcontent">
		<jsp:include page="../view/viewSperrpausenbedarfTopProjekte.jsp"/>

		<c:choose>
			<%-- Anzeige fuer Administratoren --%>
			<c:when test="${isAdmin}">
				<div class="textcontent_left">
					<div class="box">
						<div class="label"><bean:message key="sperrpausenbedarf.anmelder" /></div>
						<div class="show">
							<c:if test="${baumassnahme.anmelder != null}">
								<bean:write name="baumassnahme" property="anmelder.caption" />
							</c:if>
						</div>
					</div>
					<div class="box">
						<div class="label"><label for="auftraggeberId"><bean:message key="sperrpausenbedarf.auftraggeber" /></label></div>
						<div class="input">
							<html:select property="auftraggeberId" errorStyle="${errorStyle}" styleId="auftraggeberId">
								<html:option value=""><bean:message key="common.select.option" /></html:option>
								<html:optionsCollection name="auftraggeber" value="id" label="caption"/>
							</html:select>
						</div>
					</div>
					<div class="box">
						<div class="label"><label for="fixiert"><bean:message key="sperrpausenbedarf.fixiert" /></label></div>
						<div class="input"><html:checkbox property="fixiert" styleId="fixiert" styleClass="checkbox" errorStyle="${errorStyle}" /></div>
						<div class="label" style="margin-left:120px;"><bean:message key="sperrpausenbedarf.genehmigungsDatum" /></div>
						<div class="show center" style="width:60px;"><bean:write name="baumassnahme" property="genehmigungsDatum" format="dd.MM.yyyy" /></div>
					</div>
				</div>
				<div class="textcontent_right">
					<div class="box">
						<div class="label"><label for="regionalbereichId"><bean:message key="sperrpausenbedarf.regionalbereich" />*</label></div>
						<div class="input">
							<html:select property="regionalbereichId" errorStyle="${errorStyle}" styleId="regionalbereichId">
								<html:option value=""><bean:message key="common.select.option" /></html:option>
								<html:optionsCollection name="regionalbereiche" value="id" label="name"/>
							</html:select>
						</div>
					</div>
					<div class="box">
						<div class="label"><label for="finanztypId"><bean:message key="sperrpausenbedarf.finanztyp" />*</label></div>
						<div class="input">
							<html:select property="finanztypId" errorStyle="${errorStyle}" styleId="finanztypId">
								<html:option value=""><bean:message key="common.select.option" /></html:option>
								<html:optionsCollection name="finanztypen" value="id" label="name"/>
							</html:select>
						</div>
					</div>
					<div class="box">
						<div class="label"><bean:message key="sperrpausenbedarf.ankermassnahmeArt" /></div>
						<div class="input">
							<html:select property="ankermassnahmeArtId" styleId="ankermassnahmeArtId" errorStyle="${errorStyle}">
								<html:option value="">(<bean:message key="common.keine" />)</html:option>
								<html:optionsCollection name="ankermassnahmeArten" value="id" label="langname"/>
							</html:select>
						</div>
					</div>
				</div>
			</c:when>
			<%-- Anzeige fuer Bearbeiter --%>
			<c:otherwise>
				<div class="textcontent_left">
					<div class="box">
						<div class="label"><bean:message key="sperrpausenbedarf.anmelder" /></div>
						<div class="show">
							<c:if test="${baumassnahme.anmelder != null}">
								<bean:write name="baumassnahme" property="anmelder.caption" />
							</c:if>
						</div>
					</div>
					<div class="box">
						<div class="label"><bean:message key="sperrpausenbedarf.auftraggeber" /></div>
						<div class="show">
							<c:if test="${baumassnahme.auftraggeber != null}">
								<bean:write name="baumassnahme" property="auftraggeber.caption" />
							</c:if>
						</div>
					</div>
					<div class="box">
						<div class="label"><label for="fixiert"><bean:message key="sperrpausenbedarf.fixiert" /></label></div>
						<%-- Fixierung darf nur von einem zentralen Administrator entfernt werden, zum Setzen benoetigt man das Recht --%>
						<c:choose>
							<c:when test="${baumassnahme.genehmiger != null}">
								<div class="show" style="width:22px;"><bean:message key="common.ja" /></div>
								<div class="label" style="margin-left:109px;"><bean:message key="sperrpausenbedarf.genehmigungsDatum" /></div>
							</c:when>
							<c:otherwise>
								<easy:hasAuthorization model="${baumassnahme}" authorization="ROLE_MASSNAHME_FIXIEREN">
									<div class="input"><html:checkbox property="fixiert" styleId="fixiert" styleClass="checkbox" errorStyle="${errorStyle}" /></div>
									<div class="label" style="margin-left:120px;"><bean:message key="sperrpausenbedarf.genehmigungsDatum" /></div>
								</easy:hasAuthorization>
								<easy:hasNotAuthorization model="${baumassnahme}" authorization="ROLE_MASSNAHME_FIXIEREN">
									<div class="show" style="width:22px;"><bean:message key="common.nein" /></div>
									<div class="label" style="margin-left:109px;"><bean:message key="sperrpausenbedarf.genehmigungsDatum" /></div>
								</easy:hasNotAuthorization>
							</c:otherwise>
						</c:choose>
						<div class="show center" style="width:60px;"><bean:write name="baumassnahme" property="genehmigungsDatum" format="dd.MM.yyyy" /></div>
					</div>
				</div>
				<div class="textcontent_right">
					<div class="box">
						<div class="label"><bean:message key="sperrpausenbedarf.regionalbereich" />*</div>
						<div class="show">
							<c:if test="${baumassnahme.regionalbereich != null}">
								<bean:write name="baumassnahme" property="regionalbereich.name" />
							</c:if>
						</div>
					</div>
					<div class="box">
						<div class="label"><bean:message key="sperrpausenbedarf.finanztyp" />*</div>
						<div class="show">
							<c:if test="${baumassnahme.finanztyp != null}">
								<bean:write name="baumassnahme" property="finanztyp.name" />
							</c:if>
						</div>
					</div>
					<div class="box">
						<div class="label"><bean:message key="sperrpausenbedarf.ankermassnahmeArt" /></div>
						<div class="input">
							<html:select property="ankermassnahmeArtId" styleId="ankermassnahmeArtId" errorStyle="${errorStyle}">
								<html:option value="">(<bean:message key="common.keine" />)</html:option>
								<html:optionsCollection name="ankermassnahmeArten" value="id" label="langname"/>
							</html:select>
						</div>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="buttonBar">
		<html:link action="/back" styleClass="buttonBack"><bean:message key="button.back" /></html:link>
		<html:link href="#" onclick="document.forms[0].submit();" styleClass="buttonSave">
			<bean:message key="button.save" />
		</html:link>
	</div>
	
	<br>
	
	<%-- Karteireiter -----------------------------------------------------------------------------------------------------%>
	<html:link href="#" onclick="javascript:showTabDiv('Massnahme');" styleId="tabLinkMassnahme" styleClass="tab_act"><bean:message key="sperrpausenbedarf" /></html:link>
	<html:link href="#" onclick="javascript:showTabDiv('Gleissperrungen');" styleId="tabLinkGleissperrungen" styleClass="tab_ina"><bean:message key="gleissperrungen" /></html:link>
	
	<div id="tabDivMassnahme">
		<div class="textcontent" id="tabMassnahme">
			<jsp:include page="../view/viewSperrpausenbedarfBuendel.jsp"/>
			<c:choose>
				<c:when test="${isAdmin}">
					<jsp:include page="editSperrpausenbedarfTop.jsp"/>
					<jsp:include page="editSperrpausenbedarfMiddle.jsp"/>
					<jsp:include page="editSperrpausenbedarfBottom.jsp"/>
				</c:when>
				<c:otherwise>
					<jsp:include page="../view/viewSperrpausenbedarfTop.jsp"/>
					<jsp:include page="editSperrpausenbedarfMiddle.jsp"/>
					<jsp:include page="../view/viewSperrpausenbedarfBottom.jsp"/>
				</c:otherwise>
			</c:choose>
		</div>	
	</div>
	<div id="tabDivGleissperrungen" style="display:none;">
		<div class="textcontent center" id="tabGleissperrungen">
			<c:choose>
				<c:when test="${isAdmin}">
					<jsp:include page="editSperrpausenbedarfGleissperrungen.jsp"/>
				</c:when>
				<c:otherwise>
					<jsp:include page="../view/viewSperrpausenbedarfGleissperrungen.jsp"/>
				</c:otherwise>
			</c:choose>
		</div>	
		<div class="buttonBar">
			<html:link action="/osb/addGleissperrungenToMassnahme" styleClass="buttonAdd">
				<html:param name="sperrpausenbedarfId" value="${baumassnahme.id}" />
				<bean:message key="button.create" />
			</html:link>
		</div>
	</div>
</html:form>

<script type="text/javascript">
	<%-- JQuery UI Autocomplete für Strecke registrieren --%>
	$j(function() {
		$j("#hauptStrecke").autocomplete({
			source:
				function(request, response) {
					$j.ajax({
						url:"<c:url value='/AutoCompleteStreckeVzg.view'/>",
						datatype: "html",
						data: {
							<%-- Parameter, die bei AJAX Request übergeben werden --%>
							nummer: request.term
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
	
							var bsVon = $j("#betriebsstelleVonId");
							bsVon.removeOption(/.*/);
							bsVon.addOption(defaultValue,defaultMessage);
							bsVon.selectOptions(defaultValue);
							new Effect.Highlight('betriebsstelleVonId', { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });
							
							var bsBis = $j("#betriebsstelleBisId");
							bsBis.removeOption(/.*/);
							bsBis.addOption(defaultValue,defaultMessage);
							bsBis.selectOptions(defaultValue);
							new Effect.Highlight('betriebsstelleBisId', { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });
	
							var bsVonKo = $j("#betriebsstelleVonKoordiniertId");
							bsVonKo.removeOption(/.*/);
							bsVonKo.addOption(defaultValue,defaultMessage);
							bsVonKo.selectOptions(defaultValue);
							new Effect.Highlight('betriebsstelleVonKoordiniertId', { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });
							
							var bsBisKo = $j("#betriebsstelleBisKoordiniertId");
							bsBisKo.removeOption(/.*/);
							bsBisKo.addOption(defaultValue,defaultMessage);
							bsBisKo.selectOptions(defaultValue);
							new Effect.Highlight('betriebsstelleBisKoordiniertId', { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });
						}
					});
				},
			minLength: 2,
			select: 
				function(){
					var vzgNr = $j("#hauptStrecke").val();
					var bsVon = $j("#betriebsstelleVonId");
	
					var defaultValue = "";
					var defaultMessage = '(<bean:message key="common.select.option" />)';
	
					bsVon.removeOption(/.*/);
					bsVon.addOption(defaultValue,defaultMessage);
					bsVon.ajaxAddOption("<c:url value='/AutoCompleteBetriebstellenByStrecke.view'/>",
						{ "vzgStrecke" : vzgNr, "onlyZugmeldestellen" : true, "fahrplanjahr" : ${baumassnahme.fahrplanjahr} },
						false,
						function(){
							var bsBis = $j("#betriebsstelleBisId");
							bsBis.removeOption(/.*/);
							bsBis.addOption(defaultValue,defaultMessage);
							$j(this).copyOptions(bsBis,"all");
							bsBis.selectOptions(defaultValue);
							new Effect.Highlight('betriebsstelleBisId', { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });
	
							var bsVonKo = $j("#betriebsstelleVonKoordiniertId");
							bsVonKo.removeOption(/.*/);
							bsVonKo.addOption(defaultValue,defaultMessage);
							$j(this).copyOptions(bsVonKo,"all");
							bsVonKo.selectOptions(defaultValue);
							new Effect.Highlight('betriebsstelleVonKoordiniertId', { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });
	
							var bsBisKo = $j("#betriebsstelleBisKoordiniertId");
							bsBisKo.removeOption(/.*/);
							bsBisKo.addOption(defaultValue,defaultMessage);
							$j(this).copyOptions(bsBisKo,"all");
							bsBisKo.selectOptions(defaultValue);
							new Effect.Highlight('betriebsstelleBisKoordiniertId', { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });
						}
					);
					bsVon.selectOptions(defaultValue);
					new Effect.Highlight('betriebsstelleVonId', { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });
				}
		});
	});    
</script>

<jsp:include page="../../../main_footer.jsp"/>