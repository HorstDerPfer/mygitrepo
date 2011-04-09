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

<jsp:include page="../../../main_head.jsp"/>
<jsp:include page="../../../main_path.jsp"/>
<jsp:include page="../../../main_menu.jsp"/>

<%-- Öffnet Punkt in Startmenü --%>
<script type="text/javascript">
    openMainMenu('navLink_osb_workflow-sperrbedarf');
</script>

<div class="textcontent_head">
	<span style="float:left"><bean:message key="sperrpausenbedarf.id" />: ${baumassnahme.massnahmeId}</span>
	<span style="float:right;">
		<bean:message key="common.lastchangedate" />:&nbsp;
		<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${baumassnahme.lastChangeDate}" />
	</span>
</div>
<div class="textcontent">
	<jsp:include page="viewSperrpausenbedarfTopProjekte.jsp"/>

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
			<div class="show" style="width:22px;">
				<c:choose>
					<c:when test="${baumassnahme.genehmiger != null}"><bean:message key="common.ja" /></c:when>
					<c:otherwise><bean:message key="common.nein" /></c:otherwise>
				</c:choose>
			</div>
			<div class="label" style="margin-left:109px;"><bean:message key="sperrpausenbedarf.genehmigungsDatum" /></div>
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
			<div class="show">
				<c:choose>
					<c:when test="${baumassnahme.ankermassnahmeArt != null}">
						<bean:write name="baumassnahme" property="ankermassnahmeArt.langname" />
					</c:when>
					<c:otherwise><bean:message key="common.keine" /></c:otherwise>				
				</c:choose>
			</div>
		</div>
	</div>
</div>
<div class="buttonBar">
	<html:link action="/back" styleClass="buttonBack"><bean:message key="button.back" /></html:link>
	
	<easy:hasAuthorization model="${baumassnahme}" authorization="ROLE_MASSNAHME_BEARBEITEN">
		<html:link action="/osb/editSperrpausenbedarf" styleClass="buttonEdit">
			<html:param name="sperrpausenbedarfId" value="${baumassnahme.id }" />
			<bean:message key="button.edit" />
		</html:link>
	</easy:hasAuthorization>
</div>

<br>

<%-- Karteireiter -----------------------------------------------------------------------------------------------------%>
<html:link href="#" onclick="javascript:showTabDiv('Massnahme');" styleId="tabLinkMassnahme" styleClass="tab_act"><bean:message key="sperrpausenbedarf" /></html:link>
<html:link href="#" onclick="javascript:showTabDiv('Gleissperrungen');" styleId="tabLinkGleissperrungen" styleClass="tab_ina"><bean:message key="gleissperrungen" /></html:link>

<div id="tabDivMassnahme">
	<div class="textcontent" id="tabMassnahme">
		<jsp:include page="../view/viewSperrpausenbedarfBuendel.jsp"/>
		<jsp:include page="viewSperrpausenbedarfTop.jsp"/>
		<jsp:include page="viewSperrpausenbedarfMiddle.jsp"/>
		<jsp:include page="viewSperrpausenbedarfBottom.jsp"/>
	</div>	
</div>
<div id="tabDivGleissperrungen" style="display:none;">
	<div class="textcontent" id="tabGleissperrungen">
		<jsp:include page="viewSperrpausenbedarfGleissperrungen.jsp"/>
	</div>	
</div>

<jsp:include page="../../../main_footer.jsp"/>