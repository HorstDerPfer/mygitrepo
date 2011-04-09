<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="db.training.bob.model.Status"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>
<%@ taglib uri="/META-INF/bob.tld" prefix="bob" %>

<div class="textcontent_head">
	Arbeitssteuerung: Meilensteinansicht
</div>
<div class="textcontent center">

	<jsp:useBean id="urls" class="java.util.HashMap"/>
	<c:set var="datePattern" value="(dd.MM.)" />   
	<display:table
		id="currentBaumassnahme" 
		name="baumassnahmen" 
		export="false"
		requestURI="suche.do?method=web" 
		pagesize="20" 
		sort="list"
		class="colored"
		decorator="db.training.easy.util.displaytag.decorators.AddRowLink">

		<%-- URL wird erzeugt, ggf. inkl. sessionId --%>
		<c:set target="${urls}" property="${currentBaumassnahme.id}" value="#" />
		
		<c:set target="${urls}" property="${currentBaumassnahme.id}">
			<c:url value="viewBaumassnahme.do?id=${currentBaumassnahme.id}" />
		</c:set>
		
		<c:set var="ausfallStyle">
			<c:choose>
				<c:when test="${!empty currentBaumassnahme.ausfallDatum}">background-color:#F4F4F4;color:#4f4f4f;</c:when>
				<c:otherwise></c:otherwise>
			</c:choose>
		</c:set>
		
		<display:column property="streckenAbschnitt" titleKey="baumassnahme.streckenabschnitt" sortable="true" media="html excel" style="${ausfallStyle}" />
		<display:column property="vorgangsNr" titleKey="baumassnahme.vorgangsnr.short" sortable="true" media="html excel" style="${ausfallStyle}" />
		<display:column property="art" titleKey="baumassnahme.art.short" sortable="true" media="html excel" style="${ausfallStyle}" />
		
		<display:column titleKey="baumassnahme.termine.anforderungbbzr.short" sortable="true" sortProperty="baubetriebsplanung.anforderungBBZRSoll" media="html" style="${ausfallStyle}">
			<div style="padding-left:5px;text-align:center;">
				<bob:statusIcon status="${currentBaumassnahme.gesamtStatus.anforderungBBZRStatus}"/>
				<c:if test="${currentBaumassnahme.baubetriebsplanung.anforderungBBZRStatus == 'COUNTDOWN14'}">
					${currentBaumassnahme.baubetriebsplanung.verbleibendeTage_AnforderungBBZR}
				</c:if>
				<br /><bean:write name="currentBaumassnahme" property="baubetriebsplanung.anforderungBBZRSoll" bundle="configResources" formatKey="dateFormat.short" />
			</div>
		</display:column>
		
		<display:column titleKey="baumassnahme.termine.bbp.biueentwurf" sortable="true" sortProperty="baubetriebsplanung.biUeEntwurfSoll" media="html" style="${ausfallStyle}">
			<div style="padding-left:5px;text-align:center;">
				<c:choose>
					<c:when test="${currentBaumassnahme.art != 'B'}">
						<bob:statusIcon status="${currentBaumassnahme.gesamtStatus.biUeEntwurfStatus}" />
						<c:if test="${currentBaumassnahme.baubetriebsplanung.biUeEntwurfStatus == 'COUNTDOWN14'}">
							${currentBaumassnahme.baubetriebsplanung.verbleibendeTage_BiUeEntwurf}
						</c:if>
						<br /><bean:write name="currentBaumassnahme" property="baubetriebsplanung.biUeEntwurfSoll" bundle="configResources" formatKey="dateFormat.short" />
					</c:when>
					<c:otherwise>
						<bob:statusIcon status="NEUTRAL" />
					</c:otherwise>
				</c:choose>
			</div>
		</display:column>

		<display:column titleKey="baumassnahme.termine.bbp.zvfentwurf" sortable="true" sortProperty="baubetriebsplanung.zvfEntwurfSoll" media="html" style="${ausfallStyle}">
			<div style="padding-left:5px;text-align:center;">
				<bob:statusIcon status="${currentBaumassnahme.gesamtStatus.zvfEntwurfStatus}"/>
				<c:if test="${currentBaumassnahme.baubetriebsplanung.zvfEntwurfStatus == 'COUNTDOWN14'}">
					${currentBaumassnahme.baubetriebsplanung.verbleibendeTage_ZvfEntwurf}
				</c:if>
				<br /><bean:write name="currentBaumassnahme" property="baubetriebsplanung.zvfEntwurfSoll" bundle="configResources" formatKey="dateFormat.short" />
			</div>
		</display:column>
		
		<display:column titleKey="baumassnahme.termine.stellungnahmeevu.short" media="html" style="${ausfallStyle}">
			<div style="padding-left:5px;text-align:center;">
				<bob:statusIcon status="${currentBaumassnahme.gesamtStatus.stellungnahmeEVUStatus}" />
				<c:choose>
					<c:when test="${(currentBaumassnahme.pevusStatus.verbleibendeTage_stellungnahmeEVU != null) && (currentBaumassnahme.pevusStatus.stellungnahmeEVUStatus == 'COUNTDOWN14' || currentBaumassnahme.gevusStatus.stellungnahmeEVUStatus == 'COUNTDOWN14')}">
						${currentBaumassnahme.pevusStatus.verbleibendeTage_stellungnahmeEVU}
					</c:when>
					<c:when test="${(currentBaumassnahme.gevusStatus.verbleibendeTage_stellungnahmeEVU != null) && (currentBaumassnahme.pevusStatus.stellungnahmeEVUStatus == 'COUNTDOWN14' || currentBaumassnahme.gevusStatus.stellungnahmeEVUStatus == 'COUNTDOWN14')}">
						${currentBaumassnahme.gevusStatus.verbleibendeTage_stellungnahmeEVU}
					</c:when>
				</c:choose>
				<br>
				<c:choose>
					<c:when test="${currentBaumassnahme.pevusStatus.fploSoll != null}">
						<bean:write name="currentBaumassnahme" property="pevusStatus.stellungnahmeEVUSoll" bundle="configResources" formatKey="dateFormat.short" />
					</c:when>
					<c:otherwise>
						<bean:write name="currentBaumassnahme" property="gevusStatus.stellungnahmeEVUSoll" bundle="configResources" formatKey="dateFormat.short" />
					</c:otherwise>
				</c:choose>
			</div>
		</display:column>

		<display:column titleKey="baumassnahme.termine.bbp.zvf" sortable="true" sortProperty="baubetriebsplanung.zvfSoll" media="html" style="${ausfallStyle}">
			<div style="padding-left:5px;text-align:center;">
				<bob:statusIcon status="${currentBaumassnahme.gesamtStatus.zvfStatus}" />
				<c:if test="${currentBaumassnahme.baubetriebsplanung.zvfStatus == 'COUNTDOWN14' || currentBaumassnahme.pevusStatus.zvFStatus == 'COUNTDOWN14' || currentBaumassnahme.gevusStatus.zvFStatus == 'COUNTDOWN14'}">
					${currentBaumassnahme.baubetriebsplanung.verbleibendeTage_Zvf}
				</c:if>
				<br /><bean:write name="currentBaumassnahme" property="baubetriebsplanung.zvfSoll" bundle="configResources" formatKey="dateFormat.short" />
			</div>
		</display:column>
		
		<display:column titleKey="baumassnahme.termine.masteruebergabeblattpv.short" media="html" style="${ausfallStyle}">
			<div style="padding-left:5px;text-align:center;">
				<bob:statusIcon status="${currentBaumassnahme.gesamtStatus.masterUebergabeblattPVStatus}" />
				<c:if test="${currentBaumassnahme.pevusStatus.masterUebergabeblattPVErforderlich == true && currentBaumassnahme.pevusStatus.masterUebergabeblattPVStatus == 'COUNTDOWN14'}">
					${currentBaumassnahme.pevusStatus.verbleibendeTage_masterUebergabeblattPV}
				</c:if>
				<br /><bean:write name="currentBaumassnahme" property="pevusStatus.masterUebergabeblattPVSoll" bundle="configResources" formatKey="dateFormat.short" />
			</div>
		</display:column>
		
		<display:column titleKey="baumassnahme.termine.masteruebergabeblattgv.short" media="html" style="${ausfallStyle}">
			<div style="padding-left:5px;text-align:center;">
				<bob:statusIcon status="${currentBaumassnahme.gesamtStatus.masterUebergabeblattGVStatus}" />
				<c:if test="${currentBaumassnahme.gevusStatus.masterUebergabeblattGVErforderlich == true && currentBaumassnahme.gevusStatus.masterUebergabeblattGVStatus == 'COUNTDOWN14'}">
					${currentBaumassnahme.gevusStatus.verbleibendeTage_masterUebergabeblattGV}
				</c:if>
				<br /><bean:write name="currentBaumassnahme" property="gevusStatus.masterUebergabeblattGVSoll" bundle="configResources" formatKey="dateFormat.short" />
			</div>
		</display:column>

		<display:column titleKey="baumassnahme.termine.uebergabeblattpv" media="html" style="${ausfallStyle}">
			<div style="padding-left:5px;text-align:center;">
				<bob:statusIcon status="${currentBaumassnahme.gesamtStatus.uebergabeblattPVStatus}" />
				<c:if test="${currentBaumassnahme.pevusStatus.uebergabeblattPVErforderlich == true && currentBaumassnahme.pevusStatus.uebergabeblattPVStatus == 'COUNTDOWN14'}">
					${currentBaumassnahme.pevusStatus.verbleibendeTage_uebergabeblattPV}
				</c:if>
				<br /><bean:write name="currentBaumassnahme" property="pevusStatus.uebergabeblattPVSoll" bundle="configResources" formatKey="dateFormat.short" />
			</div>
		</display:column>
		
		<display:column titleKey="baumassnahme.termine.uebergabeblattgv" media="html" style="${ausfallStyle}">
			<div style="padding-left:5px;text-align:center;">
				<bob:statusIcon status="${currentBaumassnahme.gesamtStatus.uebergabeblattGVStatus}" />
				<c:if test="${currentBaumassnahme.gevusStatus.uebergabeblattGVErforderlich == true && currentBaumassnahme.gevusStatus.uebergabeblattGVStatus == 'COUNTDOWN14'}">
					${currentBaumassnahme.gevusStatus.verbleibendeTage_uebergabeblattGV}
				</c:if>
				<br /><bean:write name="currentBaumassnahme" property="gevusStatus.uebergabeblattGVSoll" bundle="configResources" formatKey="dateFormat.short" />
			</div>
		</display:column>

		<display:column titleKey="baumassnahme.termine.fplo" media="html" style="${ausfallStyle}">
			<div style="padding-left:5px;text-align:center;">
				<bob:statusIcon status="${currentBaumassnahme.gesamtStatus.fploStatus}" />
				<c:choose>
					<c:when test="${(currentBaumassnahme.gevusStatus.verbleibendeTage_fplo != null) && (currentBaumassnahme.pevusStatus.fploStatus == 'COUNTDOWN14' || currentBaumassnahme.gevusStatus.fploStatus == 'COUNTDOWN14')}">
						${currentBaumassnahme.gevusStatus.verbleibendeTage_fplo}
					</c:when>
					<c:when test="${(currentBaumassnahme.pevusStatus.verbleibendeTage_fplo != null) && (currentBaumassnahme.pevusStatus.fploStatus == 'COUNTDOWN14' || currentBaumassnahme.gevusStatus.fploStatus == 'COUNTDOWN14')}">
						${currentBaumassnahme.pevusStatus.verbleibendeTage_fplo}
					</c:when>
				</c:choose>
				<br />
				<c:choose>
					<c:when test="${currentBaumassnahme.pevusStatus.fploSoll != null}">
						<bean:write name="currentBaumassnahme" property="pevusStatus.fploSoll" bundle="configResources" formatKey="dateFormat.short" />
					</c:when>
					<c:otherwise>
						<bean:write name="currentBaumassnahme" property="gevusStatus.fploSoll" bundle="configResources" formatKey="dateFormat.short" />
					</c:otherwise>
				</c:choose>
			</div>
		</display:column>

		<display:column titleKey="baumassnahme.termine.eingabegfd_z.short" media="html" style="${ausfallStyle}">
			<div style="padding-left:5px;text-align:center;">
				<bob:statusIcon status="${currentBaumassnahme.gesamtStatus.eingabeGFD_ZStatus}" />
				<c:choose>
					<c:when test="${(currentBaumassnahme.gevusStatus.verbleibendeTage_eingabeGFD_Z != null) && (currentBaumassnahme.pevusStatus.eingabeGFD_ZStatus == 'COUNTDOWN14' || currentBaumassnahme.gevusStatus.eingabeGFD_ZStatus == 'COUNTDOWN14')}">
						${currentBaumassnahme.gevusStatus.verbleibendeTage_eingabeGFD_Z}
					</c:when>
					<c:when test="${(currentBaumassnahme.pevusStatus.verbleibendeTage_eingabeGFD_Z != null) && (currentBaumassnahme.pevusStatus.eingabeGFD_ZStatus == 'COUNTDOWN14' || currentBaumassnahme.gevusStatus.eingabeGFD_ZStatus == 'COUNTDOWN14')}">
						${currentBaumassnahme.pevusStatus.verbleibendeTage_eingabeGFD_Z}
					</c:when>
				</c:choose>
				<br />
				<c:choose>
					<c:when test="${currentBaumassnahme.pevusStatus.eingabeGFD_ZSoll != null}">
						<bean:write name="currentBaumassnahme" property="pevusStatus.eingabeGFD_ZSoll" bundle="configResources" formatKey="dateFormat.short" />
					</c:when>
					<c:otherwise>
						<bean:write name="currentBaumassnahme" property="gevusStatus.eingabeGFD_ZSoll" bundle="configResources" formatKey="dateFormat.short" />
					</c:otherwise>
				</c:choose>
			</div>
		</display:column>

		<display:column media="html" style="text-align:center;${ausfallStyle}">
			<span id="tip_${currentBaumassnahme.id}">
				<img src="<c:url value='static/img/icon_s_info_small.gif' />" id="tip_${currentBaumassnahme.id}" />
			</span>
		</display:column>

		<%-- Excel Ausgabe (formatiert!)########################################################################################## --%>
								
		<display:column titleKey="baumassnahme.termine.anforderungbbzr.long" media="excel">
			<bob:statusIcon showIcon='false' showLabel='true' status='${currentBaumassnahme.baubetriebsplanung.anforderungBBZRStatus}' /><c:if test="${currentBaumassnahme.baubetriebsplanung.anforderungBBZRStatus == 'COUNTDOWN14'}">(${currentBaumassnahme.baubetriebsplanung.verbleibendeTage_AnforderungBBZR})</c:if>
		</display:column>
		
		<display:column titleKey="baumassnahme.termine.bbp.biueentwurf.long" media="excel">
			<bob:statusIcon showIcon="false" showLabel="true" status="${currentBaumassnahme.baubetriebsplanung.biUeEntwurfStatus}" /><c:if test="${currentBaumassnahme.baubetriebsplanung.biUeEntwurfStatus == 'COUNTDOWN14'}">(${currentBaumassnahme.baubetriebsplanung.verbleibendeTage_BiUeEntwurf})</c:if>
		</display:column>
		
		<display:column titleKey="baumassnahme.termine.bbp.zvfentwurf.long" media="excel">
			<bob:statusIcon showIcon="false" showLabel="true" status="${currentBaumassnahme.baubetriebsplanung.zvfEntwurfStatus}">
				<bob:param value="${currentBaumassnahme.pevusStatus.zvfEntwurfStatus}"></bob:param>
				<bob:param value="${currentBaumassnahme.gevusStatus.zvfEntwurfStatus}"></bob:param>
			</bob:statusIcon><c:if test="${currentBaumassnahme.baubetriebsplanung.zvfEntwurfStatus == 'COUNTDOWN14' || currentBaumassnahme.pevusStatus.zvfEntwurfStatus == 'COUNTDOWN14' || currentBaumassnahme.gevusStatus.zvfEntwurfStatus == 'COUNTDOWN14'}">(${currentBaumassnahme.baubetriebsplanung.verbleibendeTage_ZvfEntwurf})</c:if>
		</display:column>
		<display:column titleKey="baumassnahme.termine.stellungnahmeevu.nobr" media="excel">
			<bob:statusIcon showIcon="false" showLabel="true">
				<bob:param value="${currentBaumassnahme.pevusStatus.stellungnahmeEVUStatus}"></bob:param>
				<bob:param value="${currentBaumassnahme.gevusStatus.stellungnahmeEVUStatus}"></bob:param>
			</bob:statusIcon><c:choose>
				<c:when test="${(currentBaumassnahme.pevusStatus.verbleibendeTage_stellungnahmeEVU != null) && (currentBaumassnahme.pevusStatus.stellungnahmeEVUStatus == 'COUNTDOWN14' || currentBaumassnahme.gevusStatus.stellungnahmeEVUStatus == 'COUNTDOWN14')}">(${currentBaumassnahme.pevusStatus.verbleibendeTage_stellungnahmeEVU})</c:when>
				<c:when test="${(currentBaumassnahme.gevusStatus.verbleibendeTage_stellungnahmeEVU != null) && (currentBaumassnahme.pevusStatus.stellungnahmeEVUStatus == 'COUNTDOWN14' || currentBaumassnahme.gevusStatus.stellungnahmeEVUStatus == 'COUNTDOWN14')}">(${currentBaumassnahme.gevusStatus.verbleibendeTage_stellungnahmeEVU})</c:when>
			</c:choose>
		</display:column>
		
		<display:column titleKey="baumassnahme.termine.bbp.zvf" media="excel">
			<bob:statusIcon showIcon="false" showLabel="true" status="${currentBaumassnahme.baubetriebsplanung.zvfStatus}">
				<bob:param value="${currentBaumassnahme.pevusStatus.zvFStatus}"></bob:param>
				<bob:param value="${currentBaumassnahme.gevusStatus.zvFStatus}"></bob:param>
			</bob:statusIcon><c:if test="${currentBaumassnahme.baubetriebsplanung.zvfStatus == 'COUNTDOWN14' || currentBaumassnahme.pevusStatus.zvFStatus == 'COUNTDOWN14' || currentBaumassnahme.gevusStatus.zvFStatus == 'COUNTDOWN14'}">(${currentBaumassnahme.baubetriebsplanung.verbleibendeTage_Zvf})</c:if>
		</display:column>
		
		<display:column titleKey="baumassnahme.termine.masteruebergabeblattpv.nobr" media="excel">
			<bob:statusIcon showIcon="false" showLabel="true" status="${currentBaumassnahme.pevusStatus.masterUebergabeblattPVStatus}" /><c:if test="${currentBaumassnahme.pevusStatus.masterUebergabeblattPVStatus == 'COUNTDOWN14'}">(${currentBaumassnahme.pevusStatus.verbleibendeTage_masterUebergabeblattPV})</c:if>
		</display:column>
		
		<display:column titleKey="baumassnahme.termine.masteruebergabeblattgv.nobr" media="excel">
			<bob:statusIcon showIcon="false" showLabel="true" status="${currentBaumassnahme.gevusStatus.masterUebergabeblattGVStatus}" /><c:if test="${currentBaumassnahme.gevusStatus.masterUebergabeblattGVStatus == 'COUNTDOWN14'}">(${currentBaumassnahme.gevusStatus.verbleibendeTage_masterUebergabeblattGV})</c:if>
		</display:column>
		
		<display:column titleKey="baumassnahme.termine.uebergabeblattpv" media="excel">
			<bob:statusIcon showIcon="false" showLabel="true" status="${currentBaumassnahme.pevusStatus.uebergabeblattPVStatus}" /><c:if test="${currentBaumassnahme.pevusStatus.uebergabeblattPVStatus == 'COUNTDOWN14'}">(${currentBaumassnahme.pevusStatus.verbleibendeTage_uebergabeblattPV})</c:if>
		</display:column>
		
		<display:column titleKey="baumassnahme.termine.uebergabeblattgv" media="excel">
			<bob:statusIcon showIcon="false" showLabel="true" status="${currentBaumassnahme.gevusStatus.uebergabeblattGVStatus}" /><c:if test="${currentBaumassnahme.gevusStatus.uebergabeblattGVStatus == 'COUNTDOWN14'}">(${currentBaumassnahme.gevusStatus.verbleibendeTage_uebergabeblattGV})</c:if>
		</display:column>
		
		<display:column titleKey="baumassnahme.termine.fplo" media="excel">
			<bob:statusIcon showIcon="false" showLabel="true">
				<bob:param value="${currentBaumassnahme.pevusStatus.fploStatus}"></bob:param>
				<bob:param value="${currentBaumassnahme.gevusStatus.fploStatus}"></bob:param>
			</bob:statusIcon>
			<c:choose>
				<c:when test="${(currentBaumassnahme.pevusStatus.verbleibendeTage_fplo != null) && (currentBaumassnahme.pevusStatus.fploStatus == 'COUNTDOWN14' || currentBaumassnahme.gevusStatus.fploStatus == 'COUNTDOWN14')}">(${currentBaumassnahme.pevusStatus.verbleibendeTage_fplo})</c:when>
				<c:when test="${(currentBaumassnahme.gevusStatus.verbleibendeTage_fplo != null) && (currentBaumassnahme.pevusStatus.fploStatus == 'COUNTDOWN14' || currentBaumassnahme.gevusStatus.fploStatus == 'COUNTDOWN14')}">(${currentBaumassnahme.gevusStatus.verbleibendeTage_fplo})</c:when>
			</c:choose>
		</display:column>
		
		<display:column titleKey="baumassnahme.termine.eingabegfd_z" media="excel">
			<bob:statusIcon showIcon="false" showLabel="true">
				<bob:param value="${currentBaumassnahme.pevusStatus.eingabeGFD_ZStatus}"></bob:param>
				<bob:param value="${currentBaumassnahme.gevusStatus.eingabeGFD_ZStatus}"></bob:param>
			</bob:statusIcon>
			<c:choose>
				<c:when test="${(currentBaumassnahme.pevusStatus.verbleibendeTage_eingabeGFD_Z != null) && (currentBaumassnahme.pevusStatus.eingabeGFD_ZStatus == 'COUNTDOWN14' || currentBaumassnahme.pevusStatus.eingabeGFD_ZStatus == 'COUNTDOWN14')}">(${currentBaumassnahme.pevusStatus.verbleibendeTage_eingabeGFD_Z})</c:when>
				<c:when test="${(currentBaumassnahme.gevusStatus.verbleibendeTage_eingabeGFD_Z != null) && (currentBaumassnahme.pevusStatus.eingabeGFD_ZStatus == 'COUNTDOWN14' || currentBaumassnahme.pevusStatus.eingabeGFD_ZStatus == 'COUNTDOWN14')}">(${currentBaumassnahme.gevusStatus.verbleibendeTage_eingabeGFD_Z})</c:when>
			</c:choose>
		</display:column>
	</display:table>
				
</div>

<div class="buttonBar">
	<html:link action="/suche" target="_blank" styleClass="buttonXls">
		<html:param name="method" value="xlsSummary" />
		<bean:message key="button.xls.summary" />
	</html:link>
	<html:link action="/suche" target="_blank" styleClass="buttonXls">
		<html:param name="method" value="xls" />
		<bean:message key="button.xls" />
	</html:link>
</div>

<%-- Tooltips --%>
<jsp:directive.include file="../baumassnahme/tooltips.jsp" />