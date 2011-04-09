<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
					
<div id="divUebZuege">
	<div class="box">
		<c:if test="${baumassnahmeForm.showZuegeUeb == true}">
			<fieldset>
			<%--name="currentMassnahme.zug"  --%>
			    <legend><bean:message key="ueb.zuege" /></legend>
				<logic:iterate id="currentMassnahme" name="baumassnahme" property="uebergabeblatt.massnahmen" indexId="index">
					<bean:define toScope="request" id="nr" value="${0}"/>
					<display:table
						id="currentZug" 
						name="uebZuege" 
						requestURI="/refreshUebergabeblatt.do?id=${baumassnahme.id}&showZuege=true"
						export="false"
						class="colored"
						style="margin-top:5px;" 
						sort="external">
						
							<bean:define toScope="page" id="nr" value="${nr+1}"/>
							<display:column property="laufendeNr" titleKey="ueb.zug.lfdnr.short" sortable="true" />
							<display:column property="zugnr" titleKey="ueb.zug.zugnr.short" sortable="true" />
							<display:column property="verkehrstag" titleKey="ueb.zug.datum" sortable="true" />
							<display:column property="zugbez" titleKey="ueb.zug.zuggattung.short" sortable="true" />
							<display:column titleKey="ueb.zug.abgangsbhf" sortable="true" sortProperty="regelweg.abgangsbahnhof.langName" >
								<logic:notEmpty name="currentZug" property="regelweg.abgangsbahnhof">
									<bean:write name="currentZug" property="regelweg.abgangsbahnhof.langName"  />
								</logic:notEmpty>
							</display:column>
							<display:column titleKey="ueb.zug.zielbhf" sortable="true" sortProperty="regelweg.zielbahnhof.langName">
								<logic:notEmpty name="currentZug" property="regelweg.zielbahnhof">
									<bean:write name="currentZug" property="regelweg.zielbahnhof.langName"  />
								</logic:notEmpty>
							</display:column>
							<display:column titleKey="ueb.zug.richtung.short" sortable="true" sortProperty="richtung">
								<bean:message key="ueb.zug.richtung.${currentZug.richtung}" />
							</display:column>
							<display:column titleKey="ueb.zug.qsks" sortable="true" sortProperty="qs_ks">
								<bean:message key="ueb.zug.qsks.${currentZug.qs_ks}" />
							</display:column>
							
							<display:column titleKey="common.more" sortable="false" style="">
								<span id="tip_${currentZug.id}">
									<img src="<c:url value='static/img/icon_s_info_small.gif' />" id="tip_${currentZug.id}" />
								</span>
						</display:column>
					</display:table>
				</logic:iterate>
			</fieldset>
		</c:if>
	</div>
</div>
