<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>
<html:xhtml />

<jsp:include page="/pages/main_head.jsp" />
<jsp:include page="/pages/main_path.jsp" />
<jsp:include page="/pages/main_menu.jsp" />

<%-- Öffnet Punkt in Startmenü --%>
<script type="text/javascript">
    openMainMenu('navLink_osb_workflow-buendel');
</script>

<html:form action="/osb/saveBuendel">
	<html:hidden property="buendelId" />
	<html:hidden property="fahrplanjahr" />
	<html:hidden property="gleissperrungIds" />

	<div class="textcontent_head">
		<span style="float:left"><bean:message key="buendel" /></span>
		<c:if test="${buendel != null}">
			<span style="float:right;">
				<c:if test="${buendel.deleted == true}">
					<bean:message key="common.deleted" />&nbsp;-&nbsp;
				</c:if>
				<bean:message key="common.lastchangedate" />:&nbsp;
				<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${buendel.lastChangeDate}" />
			</span>	
		</c:if>
	</div>
	<div class="textcontent">
		<jsp:include page="editBuendelAllgemein.jsp" />
	</div>
	
	<div class="buttonBar">
		<html:link action="/back" styleClass="buttonBack"><bean:message key="button.back" /></html:link>
		
		<c:if test="${buendel != null && buendel.id != null}">
			<bean:define id="gruppenlfwUrl">
				<bean:message  bundle="configResources" key="gruppenlfw.${session_fahrplanjahr}" arg0="${buendel.buendelId}"/>
			</bean:define>			
			<html:link href="#" onclick="javascript:window.open('${gruppenlfwUrl}')" styleClass="buttonFolder">
				<bean:message key="gruppenlfw" />
			</html:link>
		</c:if>
		
		<c:if test="${buendel != null}">
			<easy:hasAuthorization authorization="ROLE_BUENDEL_LOESCHEN" model="${buendel}">
				<c:choose>
					<c:when test="${buendel.deleted == true}">
						<html:link action="/osb/deleteBuendel" styleClass="buttonReload">
							<html:param name="buendelId" value="${buendel.id}" />
							<html:param name="delete" value="false" />
							<bean:message key="button.undelete" />
						</html:link>
					</c:when>
					<c:when test="${buendel.deleted == false}">
						<bean:define id="confirmTextDelete" toScope="page"><bean:message key="confirm.buendel.delete" /></bean:define>
						<html:link action="/osb/deleteBuendel" styleClass="buttonDelete" onclick="return confirmLink(this.href, '${confirmTextDelete}');">
							<html:param name="buendelId" value="${buendel.id}" />
							<html:param name="delete" value="true" />
							<bean:message key="button.delete" />
						</html:link>
					</c:when>
				</c:choose>
			</easy:hasAuthorization>

			<easy:hasAuthorization model="${buendel}" authorization="ROLE_BUENDEL_ANLEGEN">
				<bean:define id="confirmTextCopy" toScope="page"><bean:message key="confirm.buendel.copy" /></bean:define>
				<html:link action="/osb/copyBuendel" styleClass="buttonAdd" onclick="return confirmLink(this.href, '${confirmTextCopy}');">
					<html:param name="buendelId" value="${buendel.id}" />
					<bean:message key="button.copy" />
				</html:link>
			</easy:hasAuthorization>

			<easy:hasAuthorization model="${buendel}" authorization="ROLE_BUENDEL_ANLEGEN, ROLE_BUENDEL_BEARBEITEN">
				<html:link href="#" onclick="document.forms[0].submit();"
					styleClass="buttonSave" styleId="buttonSave">
					<bean:message key="button.save" />
				</html:link>
			</easy:hasAuthorization>
		</c:if>
	</div>

	<br/>

	<%-- Karteireiter ------------------------------------------------------------------------------------------- --%>
	<html:link href="#" onclick="javascript:showTabDiv('Gleissperrungen');" styleId="tabLinkGleissperrungen" styleClass="tab_act">
		<bean:message key="buendel.tab.gleissperrungen" />
	</html:link>
	<html:link href="#" onclick="javascript:showTabDiv('Buendelungsgrad');" styleId="tabLinkBuendelungsgrad" styleClass="tab_ina">
		<bean:message key="buendel.tab.buendelungsgrad" />
	</html:link>
	<html:link href="#" onclick="javascript:showTabDiv('Fahrplanregelungen');" styleId="tabLinkFahrplanregelungen" styleClass="tab_ina">
		<bean:message key="fahrplanregelungen" />
	</html:link>

	<div class="textcontent center" id="tabDivGleissperrungen">

		<c:if test="${buendelForm.buendelId != null && buendelForm.buendelId != 0 && buendelForm.fixiert == false}">
			<div class="box" style="height:auto;" id="vzgStrecken">
				<div class="label bold left">
					<bean:message key="buendel.tab.gleissperrungen.legend.vzgStrecken" /><br/>
					(${fn:length(vzgStrecken)})
				</div>
				<div class="scroll left">
					<c:choose>
						<c:when test="${fn:length(vzgStrecken) > 0}">
							<logic:iterate id="current" name="vzgStrecken">
								<html:link action="/osb/addGleissperrungToBuendel" styleClass="list" style="margin:0px;">
									<bean:write name="current" property="caption" />
									<html:param name="buendelId" value="${buendelForm.buendelId}" />
									<html:param name="vzgStreckeId" value="${current.id}" />
								</html:link>
								<br/>
							</logic:iterate>
						</c:when>
						<c:otherwise>
							<bean:message key="buendel.message.vzgStreckenEmpty" />
						</c:otherwise>
					</c:choose>
				</div>
				<div class="label left" style="margin-left:20px;">
					<html:link href="#" onclick="showAllVzgStrecken();" styleClass="list" style="margin:0px;">
						<bean:message key="buendel.tab.gleissperrungen.legend.vzgStrecken.all" />
					</html:link>
				</div>
				<img src="<c:url value='/static/img/indicator.gif' />" id="vzgStreckenIndicator" style="float:left;display:none;" />
			</div>

			<div class="box" style="height:auto;display:none;" id="vzgStreckenAlle" >
				<jsp:include page="editBuendelVzgStreckenAll.jsp" />
			</div>

			<hr/>
		</c:if>

		<jsp:include page="editBuendelGleissperrungen.jsp" />
	</div>
	<div class="textcontent" id="tabDivBuendelungsgrad" style="display: none;">
		<jsp:include page="editBuendelBuendelungsgrad.jsp" />
	</div>
	
	<div class="textcontent center" id="tabDivFahrplanregelungen" style="display: none;">
		<jsp:include page="editBuendelFahrplanregelungen.jsp" />
	</div>
</html:form>

<br/>

<bean:define id="urlRefreshBuendelVzgStrecken"><c:url value='/refreshBuendelVzgStrecken.do'/></bean:define>

<script type="text/javascript">
	function showAllVzgStrecken(){
		refreshBuendelVzgStrecken('${urlRefreshBuendelVzgStrecken}','${buendel.id}');
	}

	function showAnstossendeVzgStrecken(){
		$('vzgStreckenAlle').hide();
		$('vzgStrecken').show();
	}
	
	showTabDiv("${ ((tab != null) ? tab : 'Gleissperrungen') }");
</script>

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
	
							var bsVon = $j("#startBahnhofId");
							bsVon.removeOption(/.*/);
							bsVon.addOption(defaultValue,defaultMessage);
							bsVon.selectOptions(defaultValue);
							new Effect.Highlight('startBahnhofId', { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });
							
							var bsBis = $j("#endeBahnhofId");
							bsBis.removeOption(/.*/);
							bsBis.addOption(defaultValue,defaultMessage);
							bsBis.selectOptions(defaultValue);
							new Effect.Highlight('endeBahnhofId', { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });
						}
					});
				},
			minLength: 2,
			select: 
				function(){
					var vzgNr = $j("#hauptStrecke").val();
					var bsVon = $j("#startBahnhofId");
	
					var defaultValue = "";
					var defaultMessage = '(<bean:message key="common.select.option" />)';
	
					bsVon.removeOption(/.*/);
					bsVon.addOption(defaultValue,defaultMessage);
					bsVon.ajaxAddOption("<c:url value='/AutoCompleteBetriebstellenByStrecke.view'/>",
						{ "vzgStrecke" : vzgNr, "onlyZugmeldestellen" : true, "fahrplanjahr" : ${buendel.fahrplanjahr} },
						false,
						function(){
							var bsBis = $j("#endeBahnhofId");
							bsBis.removeOption(/.*/);
							bsBis.addOption(defaultValue,defaultMessage);
							$j(this).copyOptions(bsBis,"all");
							bsBis.selectOptions(defaultValue);
							new Effect.Highlight('endeBahnhofId', { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });
						}
					);
					bsVon.selectOptions(defaultValue);
					new Effect.Highlight('startBahnhofId', { startcolor: '#ff0000', endcolor: '#ffffff', restorecolor: '#ffffff' });
				}
		});
	});
</script>

<jsp:include page="/pages/main_footer.jsp" />