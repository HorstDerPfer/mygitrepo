<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
<html:xhtml/>

<jsp:include page="../../main_head.jsp"/>
<jsp:include page="../../main_path.jsp"/>
<jsp:include page="../../main_menu.jsp"/>

<%-- Öffnet Punkt in Startmenü --%>
<script type="text/javascript">
    openMainMenu('navLink_osb_workflow-fahrplanregelung');
</script>

<html:form action="/osb/fahrplanregelung/save">
	<html:hidden property="fahrplanregelungId" />
	<html:hidden property="fahrplanjahr" />
	<html:hidden property="copy" />

	<div class="textcontent_head">
		<span style="float:left">
			<logic:notEqual name="fahrplanregelungForm" property="fahrplanregelungId" value="0">
				<bean:message key="fahrplanregelung" />&nbsp;
				<bean:message key="common.edit" />
			</logic:notEqual>
			<logic:equal name="fahrplanregelungForm" property="fahrplanregelungId" value="0">
				<bean:message key="fahrplanregelung" />&nbsp;
				<bean:message key="common.edit" />
			</logic:equal>
		</span>
		<c:if test="${fahrplanregelung != null}">
			<span style="float:right;">
				<c:if test="${fahrplanregelung.deleted == true}">
					<bean:message key="common.deleted" />&nbsp;-&nbsp;
				</c:if>
				<bean:message key="common.lastchangedate" />:&nbsp;
				<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${fahrplanregelung.lastChangeDate}" />
			</span>
		</c:if>
	</div>
	<div class="textcontent">
		<fieldset>
			<legend><bean:message key="fahrplanregelung.legend.abschnitt" /></legend>

			<div class="textcontent_left">
				<div class="box">
					<div class="label"><bean:message key="fahrplanregelung.regionalbereich" />*</div>
					<c:if test="${fahrplanregelungForm.copy == false || fahrplanregelung.fixiert == true}">
						<div class="show">
							<c:if test="${fahrplanregelung.regionalbereich != null}">
								<bean:write name="fahrplanregelung" property="regionalbereich.name" />
								<html:hidden property="regionalbereichId" />
							</c:if>
						</div>
					</c:if>
					<c:if test="${fahrplanregelungForm.copy == true && fahrplanregelung.fixiert == false}">
						<authz:authorize ifAnyGranted="ROLE_FAHRPLANREGELUNG_ANLEGEN_ALLE,ROLE_FAHRPLANREGELUNG_BEARBEITEN_ALLE">
							<div class="input">
								<html:select property="regionalbereichId" styleId="regionalbereichId" errorStyle="${errorStyle}">
									<html:option value="0"><bean:message key="common.select.option" /></html:option>
									<html:optionsCollection name="regionalbereiche" value="id" label="name" />
								</html:select>
							</div>
						</authz:authorize>
						<authz:authorize ifNotGranted="ROLE_FAHRPLANREGELUNG_ANLEGEN_ALLE,ROLE_FAHRPLANREGELUNG_BEARBEITEN_ALLE">
							<div class="show">
								<bean:write name="loginUser" property="regionalbereich.name" />
								<html:hidden property="regionabereichId" value="${loginUser.regionalbereich.id}" />
							</div>
						</authz:authorize>
					</c:if>
				</div>
				<div class="box">
					<div class="label"><bean:message key="fahrplanregelung.name" />*</div>
					<c:if test="${fahrplanregelungForm.copy == false || fahrplanregelung.fixiert == true}">
						<div class="show"><bean:write name="fahrplanregelung" property="name" /></div>
						<html:hidden property="name" />
					</c:if>
					<c:if test="${fahrplanregelungForm.copy == true && fahrplanregelung.fixiert == false}">
						<div class="edit"><html:text property="name" styleId="name" maxlength="250" errorStyle="${errorStyle}" /></div>
					</c:if>
				</div>
				<div class="box">
					<div class="label">
						<label for="betriebsstelleVon"><bean:message key="fahrplanregelung.betriebsstelleVon" /></label>
						<img src="<c:url value='/static/img/indicator.gif' />" id="betriebsstelleVonIndicator" style="display:none;" />
					</div>
					<c:if test="${fahrplanregelung.fixiert == false}">
						<div class="input">
							<html:text property="betriebsstelleVon" styleId="betriebsstelleVon" maxlength="30" errorStyle="${errorStyle}" />
							<div id="betriebsstelleVonSelect" class="autocomplete"></div>
						</div>
					</c:if>
					<c:if test="${fahrplanregelung.fixiert == true}">
						<div class="show">
							<html:hidden property="betriebsstelleVon" />
							<bean:write name="fahrplanregelungForm" property="betriebsstelleVon" />
						</div>
					</c:if>
				</div>
				<div class="box">
					<div class="label">
						<label for="betriebsstelleBis"><bean:message key="fahrplanregelung.betriebsstelleBis" /></label>
						<img src="<c:url value='/static/img/indicator.gif' />" id="betriebsstelleBisIndicator" style="display:none;" />
					</div>
					<c:if test="${fahrplanregelung.fixiert == false}">
						<div class="input">
							<html:text property="betriebsstelleBis" styleId="betriebsstelleBis" maxlength="30" errorStyle="${errorStyle}" />
							<div id="betriebsstelleBisSelect" class="autocomplete"></div>
						</div>
					</c:if>
					<c:if test="${fahrplanregelung.fixiert == true}">
						<div class="show">
							<html:hidden property="betriebsstelleBis" />
							<bean:write name="fahrplanregelungForm" property="betriebsstelleBis" />
						</div>
					</c:if>
				</div>
				<div class="box">
					<div class="label"><label for="fixiert"><bean:message key="fahrplanregelung.fixiert" /></label></div>
					<div class="input"><html:checkbox property="fixiert" styleId="fixiert" styleClass="checkbox" /></div>
					<c:if test="${fahrplanregelung.fixiert == false}">
						<div class="label" style="margin-left:158px;"><label for="importiert"><bean:message key="fahrplanregelung.importiert" /></label></div>
						<div class="input"><html:checkbox property="importiert" styleId="importiert" styleClass="checkbox" /></div>
					</c:if>
					<c:if test="${fahrplanregelung.fixiert == true}">
						<div class="label" style="margin-left:158px;width:112px;"><label for="importiert"><bean:message key="fahrplanregelung.importiert" /></label></div>
						<div class="show" style="width:25px;"><bean:message key="common.${fahrplanregelung.importiert}" /></div>
					</c:if>
				</div>
			</div>
			<div class="textcontent_right">
				<div class="box">
					<div class="label"><label for="nummer"><bean:message key="fahrplanregelung.nummer" /></label></div>
					<div class="show" style="width:80px;"><bean:write name="fahrplanregelung" property="fahrplanregelungId" /></div>
				</div>
				<div class="box">
					<div class="label"><bean:message key="fahrplanregelung.verkehrstage" /></div>
					<c:if test="${fahrplanregelung.fixiert == false}">
						<div class="input" style="width:38px;"><html:checkbox property="verkehrstag_mo" styleClass="checkbox" /><bean:message key="vtr.montag.short" /></div>
						<div class="input" style="width:37px;"><html:checkbox property="verkehrstag_di" styleClass="checkbox" /><bean:message key="vtr.dienstag.short" /></div>
						<div class="input" style="width:37px;"><html:checkbox property="verkehrstag_mi" styleClass="checkbox" /><bean:message key="vtr.mittwoch.short" /></div>
						<div class="input" style="width:38px;"><html:checkbox property="verkehrstag_do" styleClass="checkbox" /><bean:message key="vtr.donnerstag.short" /></div>
						<div class="input" style="width:37px;"><html:checkbox property="verkehrstag_fr" styleClass="checkbox" /><bean:message key="vtr.freitag.short" /></div>
						<div class="input" style="width:38px;"><html:checkbox property="verkehrstag_sa" styleClass="checkbox" /><bean:message key="vtr.samstag.short" /></div>
						<div class="input"><html:checkbox property="verkehrstag_so" styleClass="checkbox" /><bean:message key="vtr.sonntag.short" /></div>
					</c:if>
					<c:if test="${fahrplanregelung.fixiert == true}">
						<div class="input" style="width:38px;"><html:checkbox property="verkehrstag_mo" styleClass="checkbox" disabled="true" /><bean:message key="vtr.montag.short" /></div>
						<div class="input" style="width:37px;"><html:checkbox property="verkehrstag_di" styleClass="checkbox" disabled="true" /><bean:message key="vtr.dienstag.short" /></div>
						<div class="input" style="width:37px;"><html:checkbox property="verkehrstag_mi" styleClass="checkbox" disabled="true" /><bean:message key="vtr.mittwoch.short" /></div>
						<div class="input" style="width:38px;"><html:checkbox property="verkehrstag_do" styleClass="checkbox" disabled="true" /><bean:message key="vtr.donnerstag.short" /></div>
						<div class="input" style="width:37px;"><html:checkbox property="verkehrstag_fr" styleClass="checkbox" disabled="true" /><bean:message key="vtr.freitag.short" /></div>
						<div class="input" style="width:38px;"><html:checkbox property="verkehrstag_sa" styleClass="checkbox" disabled="true" /><bean:message key="vtr.samstag.short" /></div>
						<div class="input"><html:checkbox property="verkehrstag_so" styleClass="checkbox" disabled="true" /><bean:message key="vtr.sonntag.short" /></div>
						<html:hidden property="verkehrstag_mo" />
						<html:hidden property="verkehrstag_di" />
						<html:hidden property="verkehrstag_mi" />
						<html:hidden property="verkehrstag_do" />
						<html:hidden property="verkehrstag_fr" />
						<html:hidden property="verkehrstag_sa" />
						<html:hidden property="verkehrstag_so" />
					</c:if>
				</div>
				<div class="box">
					<div class="label"><label for="betriebsweiseId"><bean:message key="baumassnahme.betriebsweise" /></label></div>
					<c:if test="${fahrplanregelung.fixiert == false}">
						<div class="input">
							<html:select property="betriebsweiseId">
								<html:option key="common.select.option" value=""></html:option>
								<html:optionsCollection name="betriebsweisen" label="name" value="id" />
							</html:select>
						</div>
					</c:if>
					<c:if test="${fahrplanregelung.fixiert == true}">
						<div class="show">
							<html:hidden property="betriebsweiseId" />
							<c:forEach var="bw" items="${betriebsweisen}">
								<c:if test="${bw.id == fahrplanregelungForm.betriebsweiseId}">
									${bw.name }
								</c:if>
							</c:forEach>
						</div>
					</c:if>
				</div>

				<div class="box">
					<div class="label"><bean:message key="fahrplanregelung.planStart" /></div>
					<c:if test="${fahrplanregelung.fixiert == false}">
						<div class="input" style="width: 82px;">
							<html:text property="planStart" styleId="planStart" maxlength="10" styleClass="date" errorStyle="${errorStyle}" />
							<img src="<c:url value='/static/img/calendar.gif' />" id="buttonPlanStart" />
						</div>
						<div class="label" style="margin-left: 35px; width: 124px;""><bean:message key="fahrplanregelung.planEnde" /></div>
						<div class="input" style="width: 82px;">
							<html:text property="planEnde" styleId="planEnde" maxlength="10" styleClass="date" errorStyle="${errorStyle}" />
							<img src="<c:url value='/static/img/calendar.gif' />" id="buttonPlanEnde" />
						</div>
					</c:if>
					<c:if test="${fahrplanregelung.fixiert == true}">
						<div class="show center" style="width:60px;"><bean:write name="fahrplanregelung" property="planStart" format="dd.MM.yyyy" /></div>
						<div class="label" style="margin-left:93px;width:100px;"><bean:message key="fahrplanregelung.planEnde" /></div>
						<div class="show center" style="width:60px;"><bean:write name="fahrplanregelung" property="planEnde" format="dd.MM.yyyy" /></div>
						<html:hidden property="planStart" />
						<html:hidden property="planEnde" />
					</c:if>
				</div>

				<div class="box">
					<div class="label"><bean:message key="fahrplanregelung.start" />*</div>
					<c:if test="${fahrplanregelung.fixiert == false}">
						<div class="input" style="width: 82px;">
							<html:text property="start" styleId="start" maxlength="10" styleClass="date" errorStyle="${errorStyle}" />
							<img src="<c:url value='/static/img/calendar.gif' />" id="buttonStart" />
						</div>
						<div class="label" style="margin-left: 35px; width: 124px;"><bean:message key="fahrplanregelung.ende" /></div>
						<div class="input" style="width: 82px;">
							<html:text property="ende" styleId="ende" maxlength="10" styleClass="date" errorStyle="${errorStyle}" />
							<img src="<c:url value='/static/img/calendar.gif' />" id="buttonEnde" />
						</div>
					</c:if>
					<c:if test="${fahrplanregelung.fixiert == true}">
						<div class="show center" style="width:60px;"><bean:write name="fahrplanregelung" property="start" format="dd.MM.yyyy" /></div>
						<div class="label" style="margin-left:93px;width:100px;"><bean:message key="fahrplanregelung.ende" /></div>
						<div class="show center" style="width:60px;"><bean:write name="fahrplanregelung" property="ende" format="dd.MM.yyyy" /></div>
						<html:hidden property="start" />
						<html:hidden property="ende" />
					</c:if>
				</div>
			</div>
		</fieldset>

		<br/>

		<div class="textcontent_left">
			<fieldset>
				<legend><bean:message key="fahrplanregelung.legend.instrumente" /></legend>
				
				<div class="box">
					<div class="label"><bean:message key="fahrplanregelung.aufnahmeNfpVorschlag" />*</div>
					<c:if test="${fahrplanregelung.fixiert == false}">
						<div class="input"><html:radio property="aufnahmeNfpVorschlag" value="FALSE" styleClass="checkbox" errorStyle="${errorStyle}" /></div>
						<div class="label" style="width:50px;"><bean:message key="fahrplanregelung.aufnahmeArt.FALSE" /></div>
						<div class="input"><html:radio property="aufnahmeNfpVorschlag" value="GE" styleClass="checkbox" errorStyle="${errorStyle}" /></div>
						<div class="label" style="width:50px;"><bean:message key="fahrplanregelung.aufnahmeArt.GE" /></div>
						<div class="input"><html:radio property="aufnahmeNfpVorschlag" value="AR" styleClass="checkbox" errorStyle="${errorStyle}" /></div>
						<div class="label" style="width:50px;"><bean:message key="fahrplanregelung.aufnahmeArt.AR" /></div>
					</c:if>
					<c:if test="${fahrplanregelung.fixiert == true}">
						<div class="show" style="width:25px;">
							<html:hidden property="aufnahmeNfpVorschlag" />
							<c:if test="${fahrplanregelung.aufnahmeNfpVorschlag != null}">
								<bean:message key="fahrplanregelung.aufnahmeArt.${fahrplanregelung.aufnahmeNfpVorschlag}" />
							</c:if>
						</div>
					</c:if>
				</div>
				<div class="box">
					<div class="label"><bean:message key="fahrplanregelung.aufnahmeNfp" />*</div>
					<c:if test="${fahrplanregelung.fixiert == false}">
						<div class="input"><html:radio property="aufnahmeNfp" value="FALSE" styleClass="checkbox" errorStyle="${errorStyle}" /></div>
						<div class="label" style="width:50px;"><bean:message key="fahrplanregelung.aufnahmeArt.FALSE" /></div>
						<div class="input"><html:radio property="aufnahmeNfp" value="GE" styleClass="checkbox" errorStyle="${errorStyle}" /></div>
						<div class="label" style="width:50px;"><bean:message key="fahrplanregelung.aufnahmeArt.GE" /></div>
						<div class="input"><html:radio property="aufnahmeNfp" value="AR" styleClass="checkbox" errorStyle="${errorStyle}" /></div>
						<div class="label" style="width:50px;"><bean:message key="fahrplanregelung.aufnahmeArt.AR" /></div>
					</c:if>
					<c:if test="${fahrplanregelung.fixiert == true}">
						<div class="show" style="width:25px;">
							<html:hidden property="aufnahmeNfp" />
							<c:if test="${fahrplanregelung.aufnahmeNfp != null}">
								<bean:message key="fahrplanregelung.aufnahmeArt.${fahrplanregelung.aufnahmeNfp}" />
							</c:if>
						</div>
					</c:if>
				</div>
				<div class="box">
					<div class="label"><label for="behandlungKS"><bean:message key="fahrplanregelung.behandlungKS" /></label></div>
					<c:if test="${fahrplanregelung.fixiert == false}">
					<div class="input"><html:checkbox property="behandlungKS" styleId="behandlungKS" styleClass="checkbox" /></div>
					</c:if>
					<c:if test="${fahrplanregelung.fixiert == true}">
						<div class="show" style="width:25px;">
							<html:hidden property="behandlungKS" />
							<logic:equal name="fahrplanregelungForm" property="behandlungKS" value="false">nein</logic:equal>
							<logic:equal name="fahrplanregelungForm" property="behandlungKS" value="true">ja</logic:equal>
						</div>
					</c:if>
				</div>
				<div class="box">
					<div class="label"><label for="relevanzBzu"><bean:message key="fahrplanregelung.relevanzBzu" /></label></div>
					<c:if test="${fahrplanregelung.fixiert == false}">
						<div class="input"><html:checkbox property="relevanzBzu" styleId="relevanzBzu" styleClass="checkbox" /></div>
					</c:if>
					<c:if test="${fahrplanregelung.fixiert == true}">
						<div class="show" style="width:25px;">
							<html:hidden property="relevanzBzu" />
							<logic:equal name="fahrplanregelungForm" property="relevanzBzu" value="false">nein</logic:equal>
							<logic:equal name="fahrplanregelungForm" property="relevanzBzu" value="true">ja</logic:equal>
						</div>
					</c:if>
				</div>
				<div class="box">
					<div class="label"><bean:message key="fahrplanregelung.nachtsperrpause" /></div>
					<c:if test="${fahrplanregelung.fixiert == false}">
						<div class="input"><html:checkbox property="nachtsperrpause" styleId="nachtsperrpause" styleClass="checkbox" /></div>
					</c:if>
					<c:if test="${fahrplanregelung.fixiert == true}">
						<div class="show" style="width:25px;">
							<html:hidden property="nachtsperrpause" />
							<logic:equal name="fahrplanregelungForm" property="nachtsperrpause" value="false">nein</logic:equal>
							<logic:equal name="fahrplanregelungForm" property="nachtsperrpause" value="true">ja</logic:equal>
						</div>
					</c:if>
				</div>
			</fieldset>			
		</div>

		<c:if test="${fahrplanregelungForm.fahrplanregelungId != 0}">
			<jsp:include page="viewFahrplanregelungFahrplanregelung.jsp" />
		</c:if>
	</div>
	
	<div class="buttonBar">
		<html:link action="/back" styleClass="buttonBack"><bean:message key="button.back" /></html:link>
		
		<c:if test="${fahrplanregelung != null && fahrplanregelungForm.fahrplanregelungId != 0 && fahrplanregelungForm.fixiert == false}">
			<easy:hasAuthorization authorization="ROLE_FAHRPLANREGELUNG_LOESCHEN" model="${fahrplanregelung}">
				<c:choose>
					<c:when test="${fahrplanregelung.deleted == true}">
						<html:link action="/osb/fahrplanregelung/delete" styleClass="buttonReload">
							<html:param name="fahrplanregelungId" value="${fahrplanregelung.id}" />
							<html:param name="delete" value="false" />
							<bean:message key="button.undelete" />
						</html:link>
					</c:when>
					<c:when test="${fahrplanregelung.deleted == false}">
						<bean:define id="confirmText" toScope="page"><bean:message key="confirm.fahrplanregelung.delete" /></bean:define>
						<html:link action="/osb/fahrplanregelung/delete" styleClass="buttonDelete" onclick="return confirmLink(this.href, '${confirmText}');">
							<html:param name="fahrplanregelungId" value="${fahrplanregelung.id}" />
							<html:param name="delete" value="true" />
							<bean:message key="button.delete" />
						</html:link>
					</c:when>
				</c:choose>
			</easy:hasAuthorization>
		</c:if>
		
		<html:link action="/osb/fahrplanregelung/edit" styleClass="buttonReload">
			<html:param name="fahrplanregelungId" value="${fahrplanregelungForm.fahrplanregelungId}" />
			<bean:message key="button.reset" />
		</html:link>

		<c:if test="${fahrplanregelungForm.fahrplanregelungId != 0}">
			<bean:define id="confirmTextCopy" toScope="page"><bean:message key="confirm.fahrplanregelung.copy" /></bean:define>
			<html:link action="/osb/fahrplanregelung/copy" styleClass="buttonAdd" onclick="return confirmLink(this.href, '${confirmTextCopy}');">
				<html:param name="fahrplanregelungId" value="${fahrplanregelung.id}" />
				<bean:message key="button.copy" />
				<html:param name="sp" value="true" />
			</html:link>
		</c:if>
		
		<html:link href="javascript:$('fahrplanregelungForm').submit();" styleClass="buttonSave"><bean:message key="button.save" /></html:link>		
	</div>
</html:form>

<logic:notEqual name="fahrplanregelungForm" property="fahrplanregelungId" value="0">
	<br/>

	<%-- Karteireiter --------------------------------------------------------------------------------------------------- --%>	
	<html:link href="#" onclick="javascript:showTabDiv('Umleitungen');" styleId="tabLinkUmleitungen" styleClass="tab_act"><bean:message key="fahrplanregelung.tab.umleitungen" /></html:link>
	<html:link href="#" onclick="javascript:showTabDiv('Gleissperrungen');" styleId="tabLinkGleissperrungen" styleClass="tab_ina"><bean:message key="fahrplanregelung.tab.gleissperrungen" /></html:link>
		
	<div class="textcontent" id="tabDivUmleitungen">
		<jsp:include page="editFahrplanregelungUmleitungen.jsp"/>
 	</div>

	<div id="tabDivGleissperrungen" style="display:none;">
		<div class="textcontent" >
			<jsp:include page="editFahrplanregelungGleissperrungen.jsp"/>
		</div>
		
		<easy:hasAuthorization model="${fahrplanregelung}" authorization="ROLE_FAHRPLANREGELUNG_GLEISSPERRUNG_ZUORDNEN">
			<div class="buttonBar">
				<html:link action="/osb/fahrplanregelung/gleissperrung/edit" styleClass="buttonPlus" style="margin-left:0px;">
					<html:param name="fahrplanregelungId" value="${fahrplanregelung.id}" />
					<bean:message key="button.create" />
				</html:link>
			</div>
		</easy:hasAuthorization>
	</div>
</logic:notEqual>

<c:if test="${fahrplanregelung.fixiert == false}">
	<c:set var="urlAutocomplete" scope="request"><c:url value='/autocompleteOrtsbezeichnungen.view'/></c:set>
	
	<script type="text/javascript">
		new Ajax.Autocompleter("betriebsstelleVon", "betriebsstelleVonSelect", "${urlAutocomplete}", { indicator: 'betriebsstelleVonIndicator', minChars: 2, paramName: 'keyword' });
		new Ajax.Autocompleter("betriebsstelleBis", "betriebsstelleBisSelect", "${urlAutocomplete}", { indicator: 'betriebsstelleBisIndicator', minChars: 2, paramName: 'keyword' });					  
		  Calendar.setup({
		      inputField  : "planStart",
		      ifFormat    : "%d.%m.%Y",
		      button      : "buttonPlanStart"
		  });
		  Calendar.setup({
		      inputField  : "planEnde",
		      ifFormat    : "%d.%m.%Y",
		      button      : "buttonPlanEnde"
		  });
		  Calendar.setup({
		      inputField  : "start",
		      ifFormat    : "%d.%m.%Y",
		      button      : "buttonStart"
		  });
		  Calendar.setup({
		      inputField  : "ende",
		      ifFormat    : "%d.%m.%Y",
		      button      : "buttonEnde"
		  });
	</script>
</c:if>

<jsp:include page="../../main_footer.jsp"/>