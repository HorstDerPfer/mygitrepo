<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
<html:xhtml/>

<jsp:include page="../../main_head.jsp"/>
<jsp:include page="../../main_path.jsp"/>
<jsp:include page="../../main_menu.jsp"/>

<%-- Öffnet Punkt in Startmenü --%>
<script type="text/javascript">
    openMainMenu('navLink_osb_workflow-fahrplanregelung');
</script>

<div class="textcontent_head">
	<span style="float:left"><bean:message key="fahrplanregelung" /></span>
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
				<div class="label"><bean:message key="baumassnahme.regionalbereich" /></div>
				<div class="show">
					<c:if test="${fahrplanregelung.regionalbereich != null}">
						<bean:write name="fahrplanregelung" property="regionalbereich.name" />
					</c:if>
				</div>
			</div>
			<div class="box">
				<div class="label"><bean:message key="fahrplanregelung.name" /></div>
				<div class="show"><bean:write name="fahrplanregelung" property="name" /></div>
			</div>
			<div class="box">
				<div class="label"><bean:message key="fahrplanregelung.betriebsstelleVon" /></div>
				<div class="show">
					<c:if test="${fahrplanregelung.betriebsstelleVon != null}">
						<bean:write name="fahrplanregelung" property="betriebsstelleVon.caption" />
					</c:if>
				</div>
			</div>
			<div class="box">
				<div class="label"><bean:message key="fahrplanregelung.betriebsstelleBis" /></div>
				<div class="show">
					<c:if test="${fahrplanregelung.betriebsstelleBis != null}">
						<bean:write name="fahrplanregelung" property="betriebsstelleBis.caption" />
					</c:if>
				</div>
			</div>
			<div class="box">
				<div class="label"><bean:message key="fahrplanregelung.fixiert" /></div>
				<div class="show" style="width:25px;"><bean:message key="common.${fahrplanregelung.fixiert}" /></div>
				<div class="label" style="margin-left:144px;width:119px;"><bean:message key="fahrplanregelung.importiert" /></div>
				<div class="show" style="width:25px;"><bean:message key="common.${fahrplanregelung.importiert}" /></div>
			</div>
		</div>
		<div class="textcontent_right">
			<div class="box">
				<div class="label"><bean:message key="fahrplanregelung.nummer" /></div>
				<div class="show" style="width:80px;"><bean:write name="fahrplanregelung" property="fahrplanregelungId" /></div>
			</div>
			<div class="box">
				<div class="label"><bean:message key="fahrplanregelung.verkehrstage" /></div>
				<div class="show">${fahrplanregelung.verkehrstage}</div>
			</div>
			<div class="box">
				<div class="label"><bean:message key="baumassnahme.betriebsweise" /></div>
				<div class="show">
					<c:if test="${fahrplanregelung.betriebsweise != null}">
						<bean:write name="fahrplanregelung" property="betriebsweise.name" />
					</c:if>
				</div>
			</div>
			<div class="box">
				<div class="label"><bean:message key="fahrplanregelung.planStart" /></div>
				<div class="show center" style="width:60px;"><bean:write name="fahrplanregelung" property="planStart" format="dd.MM.yyyy" /></div>
				<div class="label" style="margin-left:93px;width:100px;"><bean:message key="fahrplanregelung.planEnde" /></div>
				<div class="show center" style="width:60px;"><bean:write name="fahrplanregelung" property="planEnde" format="dd.MM.yyyy" /></div>
			</div>
			<div class="box">
				<div class="label"><bean:message key="fahrplanregelung.start" /></div>
				<div class="show center" style="width:60px;"><bean:write name="fahrplanregelung" property="start" format="dd.MM.yyyy" /></div>
				<div class="label" style="margin-left:93px;width:100px;"><bean:message key="fahrplanregelung.ende" /></div>
				<div class="show center" style="width:60px;"><bean:write name="fahrplanregelung" property="ende" format="dd.MM.yyyy" /></div>
			</div>
		</div>
	</fieldset>

	<br/>

	<div class="textcontent_left">
		<fieldset>
			<legend><bean:message key="fahrplanregelung.legend.instrumente" /></legend>
			
			<div class="box">
				<div class="label"><bean:message key="fahrplanregelung.aufnahmeNfpVorschlag" /></div>
				<div class="show" style="width:25px;">
					<c:if test="${fahrplanregelung.aufnahmeNfpVorschlag != null}">
						<bean:message key="fahrplanregelung.aufnahmeArt.${fahrplanregelung.aufnahmeNfpVorschlag}" />
					</c:if>
				</div>
			</div>
			<div class="box">
				<div class="label"><bean:message key="fahrplanregelung.aufnahmeNfp" /></div>
				<div class="show" style="width:25px;">
					<c:if test="${fahrplanregelung.aufnahmeNfp != null}">
						<bean:message key="fahrplanregelung.aufnahmeArt.${fahrplanregelung.aufnahmeNfp}" />
					</c:if>
				</div>
			</div>
			<div class="box">
				<div class="label"><bean:message key="fahrplanregelung.behandlungKS" /></div>
				<div class="show" style="width:25px;"><bean:message key="common.${fahrplanregelung.behandlungKS}" /></div>
			</div>
			<div class="box">
				<div class="label"><bean:message key="fahrplanregelung.relevanzBzu" /></div>
				<div class="show" style="width:25px;"><bean:message key="common.${fahrplanregelung.relevanzBzu}" /></div>
			</div>
			<div class="box">
				<div class="label"><bean:message key="fahrplanregelung.nachtsperrpause" /></div>
				<div class="show" style="width:25px;"><bean:message key="common.${fahrplanregelung.nachtsperrpause}" /></div>
			</div>
		</fieldset>			
	</div>

	<jsp:include page="viewFahrplanregelungFahrplanregelung.jsp" />
</div>

<div class="buttonBar">
	<html:link action="/back" styleClass="buttonBack"><bean:message key="button.back" /></html:link>
	
	<easy:hasAuthorization model="${fahrplanregelung}" authorization="ROLE_FAHRPLANREGELUNG_BEARBEITEN">
		<bean:define id="confirmTextCopy" toScope="page"><bean:message key="confirm.fahrplanregelung.copy" /></bean:define>
		<html:link action="/osb/fahrplanregelung/copy" styleClass="buttonAdd" onclick="return confirmLink(this.href, '${confirmTextCopy}');">
			<html:param name="fahrplanregelungId" value="${fahrplanregelung.id}" />
			<bean:message key="button.copy" />
			<html:param name="sp" value="true" />
		</html:link>

		<html:link action="/osb/fahrplanregelung/edit" styleClass="buttonEdit">
			<html:param name="fahrplanregelungId" value="${fahrplanregelung.id }" />
			<bean:message key="button.edit" />
		</html:link>
	</easy:hasAuthorization>
</div>

<br/>

<%-- Karteireiter --------------------------------------------------------------------------------------------------- --%>	
<html:link href="#" onclick="javascript:showTabDiv('Umleitungen');" styleId="tabLinkUmleitungen" styleClass="tab_act"><bean:message key="fahrplanregelung.tab.umleitungen" /></html:link>
<html:link href="#" onclick="javascript:showTabDiv('Gleissperrungen');" styleId="tabLinkGleissperrungen" styleClass="tab_ina"><bean:message key="fahrplanregelung.tab.gleissperrungen" /></html:link>

<div class="textcontent" id="tabDivUmleitungen">
	<jsp:include page="editFahrplanregelungUmleitungen.jsp"/>
</div>

<div class="textcontent" id="tabDivGleissperrungen" style="display:none;">
	<jsp:include page="viewFahrplanregelungGleissperrungen.jsp"/>
</div>

<br><jsp:include page="../../main_footer.jsp"/>