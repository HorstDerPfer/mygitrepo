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

<logic:notEmpty name="baumassnahme" property="uebergabeblatt">
	<%--<easy:hasAuthorization model="${baumassnahme.uebergabeblatt}" authorization="ROLE_BBZR_BEARBEITEN">--%>
	
		<div class="textcontent">
			<jsp:include page="editUebergabeblattHeader.jsp"/>
			<jsp:include page="editUebergabeblattBeteiligteRB.jsp"/>
			<div class="box"> 
				<br>
				<jsp:include page="editStrecken.jsp"/>
				<jsp:include page="editStreckeEingabe.jsp"/>
				<br>
				<jsp:include page="editUebMassnahme.jsp"/>
			</div> 
			<br>
			
			<div class="box">
				<c:if test="${baumassnahmeForm.showZuegeUeb == true}">
					<jsp:include page="editZuege.jsp"/>
					<jsp:include page="editZugEingabe.jsp"/>
				</c:if>
			</div>

			<%-- DROPDOWNLISTEN FÜR MAILVERSAND --%>	
			<logic:equal value="true" name="bearbeitet">
				<div class="box">
					<bean:define id="urlMailEmpfaenger" toScope="page"><c:url value="refreshMailEmpfaenger.do" /></bean:define>
					<div class="label"><label for="uebEmpfaengerRB"><bean:message key="ueb.empfaenger_rb" /></label></div>
					<div class="input">
						<html:select property="uebEmpfaengerRB" styleId="uebEmpfaengerRB" onchange="refreshMailEmpfaenger('${urlMailEmpfaenger}', this.value);" >
							<html:option value="0"><bean:message key="common.select.option" /></html:option>
							<html:optionsCollection name="beteiligteRB" value="id" label="name"/>
						</html:select>
					</div>
					
					<jsp:include page="editMailEmpfaenger.jsp"></jsp:include>
					<%-- <jsp:include page="editMailEmpfaengerError.jsp"></jsp:include> --%>
				</div>
			</logic:equal>
		</div>

		<c:if test="${baumassnahmeForm.showZuegeUeb == false}">
			<div class="buttonBar">
				<html:link action="/editBaumassnahme?sp=true&showZuegeUeb=true&tab=Uebergabeblatt" paramId="id" paramName="baumassnahme" paramProperty="id" styleClass="buttonNext"><bean:message key="zvf.showzuege" /></html:link>
			</div>
		</c:if>
	
		<div class="buttonBar">
			<%-- FERTIG-BUTTON --%>
			<logic:empty name="baumassnahme" property="uebergabeblatt.fertigStellungsdatum">
				<html:link action="/uebergabeblattFertigstellen" paramId="id" paramName="baumassnahme" paramProperty="id" styleClass="buttonOk" styleId="buttonOk" ><bean:message key="button.ueb.fertig" /></html:link>
			</logic:empty>
		
			<%-- EMAIL-ÜB-BUTTON --%>
			<logic:equal value="true" name="bearbeitet">
					<bean:define id="urlMailEmpfaengerValue" toScope="page"><c:url value="refreshMailEmpfaengerValue.do" /></bean:define>
					<html:link href="javascript:loopSelected();" styleClass="buttonMail" styleId="buttonMail" ><bean:message key="button.ueb.mail" /></html:link>
			</logic:equal>

			<jsp:useBean id="exportButtonParamMap" class="java.util.HashMap">
				<c:set target="${exportButtonParamMap}" property="baumassnahmeId" value="${baumassnahme.id}" />
				<c:set target="${exportButtonParamMap}" property="tab" value="Uebergabeblatt" />
				<c:set target="${exportButtonParamMap}" property="zvfId" value="${baumassnahme.uebergabeblatt.id}" />
			</jsp:useBean>
			
			<%-- EXPORT-BUTTONS --%>
			<html:link action="/uebergabeblattExportXml" name="exportButtonParamMap"  styleClass="buttonNext" styleId="buttonAdd" ><bean:message key="button.export.xml" /></html:link>
			<html:link action="/uebergabeblattExportExcel" name="exportButtonParamMap"  styleClass="buttonXls" styleId="buttonXls" ><bean:message key="button.export.xls" /></html:link>
			<html:link target="_blank" action="/uebergabeblattExportPdf" name="exportButtonParamMap"  styleClass="buttonPdf" styleId="buttonPdf" ><bean:message key="button.export.pdf" /></html:link>

			<%-- IMPORT-BUTTON FÜR WEITERE XML-DATEI --%>
			<authz:authorize ifAnyGranted="ROLE_BBZR_ANLEGEN_ALLE,ROLE_BBZR_ANLEGEN_REGIONALBEREICH">
				<jsp:useBean id="uebImportParamMap2" class="java.util.HashMap">
					<c:set target="${uebImportParamMap2}" property="baumassnahmeId" value="${baumassnahme.id}" />
					<c:set target="${uebImportParamMap2}" property="type" value="UEB" />
					<c:set target="${uebImportParamMap2}" property="zvfId" value="${baumassnahme.uebergabeblatt.id}" />
				</jsp:useBean>
				<html:link action="/zvfXmlSelectFile" name="uebImportParamMap2" styleClass="buttonNext" styleId="buttonAdd" ><bean:message key="button.import" /></html:link>
			</authz:authorize>
		</div>

		<div class="buttonBar">
			<%-- LÖSCHEN-BUTTON --%>
			<bean:define id="confirmText" toScope="page"><bean:message key="confirm.uebergabeblatt.delete" /></bean:define>
			<html:link action="/deleteUebergabeblatt" paramId="id" paramName="baumassnahme" paramProperty="id"  styleClass="buttonDelete" onclick="return confirmLink(this.href, '${confirmText}');"><bean:message key="button.uebergabeblatt.delete" /></html:link>
		</div>
	<%--</easy:hasAuthorization>--%>
</logic:notEmpty>