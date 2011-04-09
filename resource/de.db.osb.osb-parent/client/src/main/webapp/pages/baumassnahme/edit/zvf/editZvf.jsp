<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

<div class="textcontent">
	<jsp:include page="editZvfHeader.jsp"/>
	<div class="box"> 
		<br>
		<jsp:include page="editStrecken.jsp"/>
		<jsp:include page="editStreckeEingabe.jsp"/>
		<br>
		<jsp:include page="editZvfMassnahme.jsp"/>
	</div>
	<div class="box">
		<jsp:include page="editZvfZug.jsp" />
	</div>
</div>
<c:if test="${baumassnahmeForm.showZuegeZvf == false}">
	<div class="buttonBar">
		<html:link action="/editBaumassnahme" styleClass="buttonNext">
			<html:param name="id" value="${baumassnahme.id}" />
			<html:param name="tab" value="Zvf" />
			<html:param name="sp" value="true" />
			<html:param name="showZuegeZvf" value="true" />
			<bean:message key="zvf.showzuege" />
		</html:link>
	</div>
</c:if>
<div class="buttonBar">
	<%-- IMPORT-BUTTON --%>
	<authz:authorize ifAnyGranted="ROLE_BBZR_ANLEGEN_ALLE,ROLE_BBZR_ANLEGEN_REGIONALBEREICH">
		<jsp:useBean id="zvfImportParamMap2" class="java.util.HashMap">
			<c:set target="${zvfImportParamMap2}" property="baumassnahmeId" value="${baumassnahme.id}" />
			<c:set target="${zvfImportParamMap2}" property="type" value="ZVF" />
			<c:set target="${zvfImportParamMap2}" property="zvfId" value="${baumassnahme.aktuelleZvf.id}" />
		</jsp:useBean>
		<html:link action="/zvfXmlSelectFile" name="zvfImportParamMap2" styleClass="buttonNext" styleId="buttonAdd" ><bean:message key="button.import" /></html:link>
	</authz:authorize>

	<%-- LÖSCHEN-BUTTON --%>
	<easy:hasAuthorization model="${baumassnahme.aktuelleZvf}" authorization="ROLE_BBZR_LOESCHEN">
		<bean:define id="confirmText" toScope="page"><bean:message key="confirm.zvf.delete" /></bean:define>
		<html:link action="/deleteZvf" paramId="id" paramName="baumassnahme" paramProperty="id"  styleClass="buttonDelete" onclick="return confirmLink(this.href, '${confirmText}');"><bean:message key="button.zvf.delete" /></html:link>
	</easy:hasAuthorization>
</div>