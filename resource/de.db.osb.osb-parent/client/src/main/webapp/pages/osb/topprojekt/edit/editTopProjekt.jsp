<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
<html:xhtml/>

<jsp:include page="/pages/main_head.jsp"/>
<jsp:include page="/pages/main_path.jsp"/>
<jsp:include page="/pages/main_menu.jsp"/>
			
<script type="text/javascript">
    openMainMenu('navLink_osb_workflow-topprojekt');
</script>

<html:form action="/osb/saveTopProjekt?sp=true" focus="name">

	<div class="textcontent_head">
		<span style="float:left"><bean:message key="topprojekt" /></span>
		<span style="float:right;">
			<c:if test="${projekt.deleted == true}">
				<bean:message key="common.deleted" />&nbsp;-&nbsp;
			</c:if>
			<bean:message key="common.lastchangedate" />:&nbsp;
			<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${projekt.lastChangeDate}" />
		</span>
	</div>
	<div class="textcontent">
		<div class="textcontent_left">
			<div class="box">
				<div class="label"><label for="name"><bean:message key="topprojekt.name" /></label></div>
				<div class="input"><html:text property="name" styleId="name" /></div>
			</div>	
			<div class="box">
				<div class="label"><label for="anmelder"><bean:message key="topprojekt.anmelder" /></label></div>
				<%--<div class="input"><html:text property="anmelder" styleId="anmelder" /></div>--%>
				<html:select property="anmelderId" errorStyle="${errorStyle}">
					<html:option value="0"><bean:message key="common.select.option" /></html:option>
					<html:optionsCollection name="anmelderList" value="id" label="caption"/>
				</html:select>
			</div>
			<div class="box">
				<div class="label"><label for="sapProjektNummer"><bean:message key="topprojekt.sapprojektnummer" /></label></div>
				<div class="input"><html:text property="sapProjektNummer" styleId="sapProjektNummer" /></div>
			</div>
			<div class="box">
				<div class="label"><label for="regionalbereichId"><bean:message key="topprojekt.regionalbereich" /></label></div>
				<div class="input">
					<html:select property="regionalbereichId" styleId="regionalbereichId">
						<html:option value="0"><bean:message key="common.select.option" /></html:option>
						<html:optionsCollection name="listRegionalbereiche" value="id" label="name"/>
					</html:select>
				</div>
			</div>	
			<div class="box">
				<div class="label"><label for="baukosten"><bean:message key="topprojekt.baukosten" /></label></div>
				<div class="input"><html:text property="baukosten" styleId="baukosten" /></div>
			</div>		
		</div>
	</div>

	<div class="buttonBar">
		<html:link action="/back.do" styleClass="buttonBack"><bean:message key="button.back" /></html:link>
		<c:if test="${projekt != null}">
			<easy:hasAuthorization authorization="ROLE_TOPPROJEKT_LOESCHEN" model="${projekt}">
				<c:choose>
					<c:when test="${projekt.deleted == true}">
						<html:link action="/osb/deleteTopProjekt" styleClass="buttonReload">
							<html:param name="topProjektId" value="${projekt.id}" />
							<html:param name="delete" value="false" />
							<bean:message key="button.undelete" />
						</html:link>
					</c:when>
					<c:when test="${projekt.deleted == false}">
						<bean:define id="confirmText" toScope="page"><bean:message key="confirm.topprojekt.delete" /></bean:define>
						<html:link action="/osb/deleteTopProjekt" styleClass="buttonDelete" onclick="return confirmLink(this.href, '${confirmText}');">
							<html:param name="topProjektId" value="${projekt.id}" />
							<html:param name="delete" value="true" />
							<bean:message key="button.delete" />
						</html:link>
					</c:when>
				</c:choose>
			</easy:hasAuthorization>
			
			<easy:hasAuthorization model="${projekt}" authorization="ROLE_TOPPROJEKT_ANLEGEN, ROLE_TOPPROJEKT_BEARBEITEN">
				<html:link href="#" onclick="$('topProjektForm').submit();" styleClass="buttonSave" styleId="buttonSave">
					<bean:message key="button.save" />
				</html:link>
			</easy:hasAuthorization>
		</c:if>
		
		<c:if test="${projekt == null}">
			<authz:authorize ifAnyGranted="ROLE_TOPPROJEKT_ANLEGEN_REGIONALBEREICH, ROLE_TOPPROJEKT_ANLEGEN_ALLE">
				<html:link href="#" onclick="$('topProjektForm').submit();" styleClass="buttonSave" styleId="buttonSave">
					<bean:message key="button.save" />
				</html:link>
			</authz:authorize>
		</c:if>
	</div>
	
	<br/>
	<logic:present name="projekt">
		<jsp:include page="../view/viewTopProjektMassnahmen.jsp" />
	</logic:present>
	<easy:hasAuthorization model="${projekt}" authorization="ROLE_TOPPROJEKT_BEARBEITEN">
		<c:if test="${topProjektForm.topProjektId != null && topProjektForm.topProjektId != 0}">
			<div class="buttonBar">
				<html:link action="/osb/topProjekt/massnahmen/edit" styleClass="buttonAdd" style="margin:0px;">
					<html:param name="topProjektId" value="${projekt.id}" />
					<bean:message key="button.create" />
				</html:link>
			</div>
		</c:if>
	</easy:hasAuthorization>
</html:form>
				
<jsp:include page="/pages/main_footer.jsp"/>