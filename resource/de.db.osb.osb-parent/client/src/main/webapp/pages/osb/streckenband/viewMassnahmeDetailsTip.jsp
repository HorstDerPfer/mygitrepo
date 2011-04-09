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

<div style="border: 0px; padding: 0px 0px 0px -3px;width:480px;">

	<c:choose>
		<c:when test="${!empty popupBaumassnahme}">
			<div class="box">
				<div class="label"><label for="gewerk"><bean:message key="sperrpausenbedarf.gewerk" /></label></div>
				<div class="show"><bean:write name="popupBaumassnahme" property="gewerk"/></div>
			</div>
			<div class="box">
				<div class="label"><label for="untergewerk"><bean:message key="sperrpausenbedarf.untergewerk" /></label></div>
				<div class="show"><bean:write name="popupBaumassnahme" property="untergewerk"/></div>
			</div>
			<div class="box">
				<div class="label"><label for="projektDefinitionDbBez"><bean:message key="sperrpausenbedarf.bezeichnung" /></label></div>
				<div class="showTextarea" style="height: 28px;"><bean:write name="popupBaumassnahme" property="projektDefinitionDbBez" /></div>
			</div>
			
			<%--
			<div class="box">
				<div class="label">
					<bean:message key="gleissperrung.artDerArbeiten" />
				</div>
				<div class="show">${popupGleissperrung.artDerArbeiten}</div>
			</div>
			<div class="box">
				<div class="label"><label for="sapProjektNummer"><bean:message key="sperrpausenbedarf.sapProjektNummer" /></label></div>
				<div class="show"><bean:write name="popupBaumassnahme" property="sapProjektNummer" /></div>
			</div>
  			--%>
			<div class="box">
				<div class="label"><label for="pspElement"><bean:message key="sperrpausenbedarf.pspElement" /></label></div>
				<div class="show"><bean:write name="popupBaumassnahme" property="pspElement" /></div>
			</div>
		
			<div class="box">
				<div class="label">
					<label for="betriebsstelleVon">
						<bean:message key="sperrpausenbedarf.bsBuendelDS100Von" />
					</label>
				</div>
				<div class="show">
					<logic:present name="popupGleissperrung" property="bstVon">
						<bean:write name="popupGleissperrung" property="bstVon.caption" />
					</logic:present>
				</div>
			</div>
			<div class="box">
				<div class="label">
					<label for="betriebsstelleBis">
						<bean:message key="sperrpausenbedarf.bsBuendelDS100Bis" />
					</label>
				</div>
				<div class="show">
					<logic:present name="popupGleissperrung" property="bstBis">
						<bean:write name="popupGleissperrung" property="bstBis.caption" />
					</logic:present>
				</div>
			</div>
	
			<div class="box">
				<div class="label"><label for="kommentar"><bean:message key="sperrpausenbedarf.kommentar" /></label></div>
				<div class="showTextarea" style="height: 28px;"><bean:write name="popupBaumassnahme" property="kommentar" /></div>
			</div>
	
			<div class="box">
				<div class="label"><label for="buendel"><bean:message key="sperrpausenbedarf.buendel" /></label></div>
				<div class="show">
					<logic:iterate id="currentBuendel" name="popupGleissperrung" property="buendel" indexId="index">
						[<bean:write name="currentBuendel" property="buendelId" />]
						<bean:write name="currentBuendel" property="buendelName" />
						<c:if test="${ (fn:length(popupGleissperrung.buendel) > (index+1)) }">,</c:if>
					</logic:iterate>
				</div>
			</div>
		</c:when>
		<c:otherwise>
			<bean:message key="common.empty" />
		</c:otherwise>
	</c:choose>
</div>
