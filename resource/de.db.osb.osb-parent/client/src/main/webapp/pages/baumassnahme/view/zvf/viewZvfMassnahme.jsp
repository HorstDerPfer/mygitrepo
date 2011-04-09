<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
					
<div class="box">
	<br></br>
	<fieldset>
		<%-- Tabelle Streckenabschnitte--%>
    	<legend><bean:message key="ueb.betroffenerbereich" /></legend>
		<table class="colored" style="text-align:center;" >
			<thead>
				<tr>
					<th>
						<div style="text-align:center;"><bean:message key="ueb.strecke.vzg" /></div>
					</th>
					<th>
						<div style="text-align:center;"><bean:message key="ueb.strecke.massnahme" /></div>
					</th>
					<th>
						<div style="text-align:center;"><bean:message key="ueb.strecke.baubeginn" /></div>
					</th>
					<th>
						<div style="text-align:center;"><bean:message key="ueb.strecke.bauende" /></div>
					</th>
					<th>
						<div style="text-align:center;"><bean:message key="ueb.strecke.unterbrochen" /></div>
					</th>
					<th>
						<div style="text-align:center;"><bean:message key="ueb.strecke.grund" /></div>
					</th>
					<th>
						<div style="text-align:center;"><bean:message key="ueb.strecke.betriebsweise" /></div>
					</th>
				</tr>
			</thead>
			<tbody>
				<logic:iterate id="currentMassnahme" name="viewZvf" property="massnahmen" indexId="index">
					<logic:iterate id="currentStrecke" name="currentMassnahme" property="strecke" indexId="ind">
						<bean:define toScope="page" id="styleClass" value="${styleClass=='evenrow'?'oddrow':'evenrow'}"/>
						<tr class="${styleClass}">
							<td>
								<logic:iterate id="currentVzg" name="currentStrecke" property="streckeVZG" indexId="indexVzg">
									<logic:equal name="indexVzg" value="0">
										<div style="text-align:center"><bean:write name="currentVzg" property="nummer"  /></div>
									</logic:equal>
								</logic:iterate>
							</td>
							<td>
								<div style="text-align:center"><bean:write name="currentStrecke" property="massnahme"  /></div>
							</td>
							<td>
								<div style="text-align:center"><bean:write name="currentStrecke" property="baubeginn" format="dd.MM.yyyy HH:mm" /></div>
							</td>
							<td>
								<div style="text-align:center"><bean:write name="currentStrecke" property="bauende" format="dd.MM.yyyy HH:mm" /></div>
							</td>
							<td>
								<div style="text-align:center"><bean:message key="ueb.strecke.unterbrochen.${currentStrecke.zeitraumUnterbrochen}" /></div>
								<%--<div style="text-align:center">${currentStrecke.zeitraumUnterbrochen}</div>--%>
							</td>
							<td>
								<div style="text-align:center"><bean:write name="currentStrecke" property="grund"  /></div>
							</td>
							<td>
								<div style="text-align:center"><bean:write name="currentStrecke" property="betriebsweise"  /></div>
							</td>
						</tr>
					</logic:iterate>
				</logic:iterate>
			</tbody>
		</table>
	</fieldset>
</div>		

<%-- Massnahmendaten --%>	
<div class="box">	
	<br></br>
	<logic:iterate id="currentMassnahme" name="viewZvf" property="massnahmen" indexId="index">
		<c:if test='${index == 0}'>	
			<div class="textcontent_left">
				<div class="box">
					<div class="label"><bean:message key="ueb.massnahme.bbp" /></div>
					<div class="show">
						<logic:iterate id="currentBBP" name="currentMassnahme" property="bbp" indexId="indexBBP">
							<bean:write name="currentBBP" property="nummer"  />
							<c:if test="${ (fn:length(currentMassnahme.bbp) > (indexBBP+1)) }">, </c:if>
						</logic:iterate>
					</div>
				</div>
				<div class="box">
					<div class="label"><bean:message key="ueb.massnahme.formularkennung" /></div>
					<div class="show"><bean:write name="currentMassnahme" property="version.formular"  /></div>
				</div>
				<div class="box">
					<div class="label"><bean:message key="ueb.massnahme.version" /></div>
					<div class="show">
						<bean:write name="currentMassnahme" property="version.major"  />.
						<bean:write name="currentMassnahme" property="version.minor"  />.
						<bean:write name="currentMassnahme" property="version.sub"  />
					</div>
				</div>
				<div class="box">
					<div class="label"><bean:message key="ueb.massnahme.baumassnahmenart" /></div>
					<div class="show"><bean:write name="currentMassnahme" property="baumassnahmenart"  /></div>
				</div>
				<div class="box">
					<div class="label"><bean:message key="ueb.massnahme.kennung" /></div>
					<div class="show"><bean:write name="currentMassnahme" property="kennung"  /></div>
				</div>
			</div>
			<div class="textcontent_right">
				<div class="box">
					<div class="label"><bean:message key="ueb.massnahme.kigbau" /></div>
					<div class="show"><bean:write name="currentMassnahme" property="kigbau"  /></div>
				</div>
				<div class="box">
					<div class="label"><bean:message key="ueb.massnahme.qs_ks_ves" /></div>
					<div class="show"><bean:write name="currentMassnahme" property="qsKsVesNr"  /></div>
				</div>
				<div class="box">
					<div class="label"><bean:message key="ueb.massnahme.korridor" /></div>
					<div class="show"><bean:write name="currentMassnahme" property="korridor"  /></div>
				</div>
				
				<div class="label"><bean:message key="ueb.massnahme.festgelegt" /></div>
				<div class="box">
					<div class="label"><bean:message key="ueb.massnahme.spfv" /></div>
				 	<div class="show"><bean:message key="ueb.massnahme.festgelegt.${currentMassnahme.festgelegtSPFV}" /></div>
				</div>
				<div class="box">
					<div class="label"><bean:message key="ueb.massnahme.spnv" /></div>
				 	<div class="show"><bean:message key="ueb.massnahme.festgelegt.${currentMassnahme.festgelegtSPNV}" /></div>
				</div>
				<div class="box">
					<div class="label"><bean:message key="ueb.massnahme.sgv" /></div>
				 	<div class="show"><bean:message key="ueb.massnahme.festgelegt.${currentMassnahme.festgelegtSGV}" /></div>
				</div>
			</div>
		</c:if>
	</logic:iterate>
	<br></br>
</div>


