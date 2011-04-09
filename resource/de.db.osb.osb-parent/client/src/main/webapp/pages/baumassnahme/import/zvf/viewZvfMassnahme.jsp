<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
			
	<div class="textcontent">	
		<jsp:include page="viewZvfHeader.jsp"/>
		
		<div class="box">
			<br>
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
						<logic:iterate id="currentMassnahme" name="zvfFile" property="massnahmen" indexId="index">
							<logic:iterate id="currentStrecke" name="currentMassnahme" property="strecke" indexId="ind">
								<bean:define toScope="page" id="styleClass" value="${styleClass=='evenrow'?'oddrow':'evenrow'}"/>
								<tr class="${styleClass}">
									<td>
										<logic:iterate id="currentVzg" name="currentStrecke" property="streckeVZG" indexId="indexVzg">
											<logic:equal name="indexVzg" value="0">
												<logic:notEmpty name="currentVzg">
													<div style="text-align:center"><bean:write name="currentVzg" property="nummer"  /></div>
												</logic:notEmpty>
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
			<br>
			<logic:iterate id="currentMassnahme" name="zvfFile" property="massnahmen" indexId="index">
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
			<br>
		</div>
		
		<div class="box">
		    <bean:message key="zvf.import.neueversion" />
			<c:if test="${zvfId==-1}"><%-- erste ZvF einlesen --%>
				<input type="radio" name="radioHeader" value="true" class="checkbox" checked="checked"><bean:message key="zvf.ja" />
				<input type="radio" name="radioHeader" value="false" class="checkbox" disabled="disabled"><bean:message key="zvf.nein" />
				
			</c:if>
			<c:if test="${zvfId>-1}"><%-- weitere Daten importieren --%>
				<c:if test="${neueVersion==true}">
					<input type="radio" name="radioHeader" value="true" class="checkbox" checked="checked"><bean:message key="zvf.ja" />
					<input type="radio" name="radioHeader" value="false" class="checkbox"><bean:message key="zvf.nein" />
				</c:if>
				<c:if test="${neueVersion==false}">
					<input type="radio" name="radioHeader" value="true" class="checkbox"><bean:message key="zvf.ja" />
					<input type="radio" name="radioHeader" value="false" class="checkbox" checked="checked"><bean:message key="zvf.nein" />
				</c:if>
			</c:if>
		</div>
	</div>
	<br>
		
	<%-- Auswahl EVU --%>	
	<div class="textcontent_head">
		<bean:message key="zvf.import.meilenstein" />
	</div>

	<div class="textcontent">
		<select name="zvfmeilenstein" size="3">
			<logic:iterate id="currentMeilenstein" name="zvfMeilensteinList" indexId="index">
				<c:if test="${selectedMeilenstein==currentMeilenstein.id}">
					<option value="${currentMeilenstein.id}" selected="selected">${currentMeilenstein.bezeichnung}</option>
				</c:if>
				<c:if test="${selectedMeilenstein!=currentMeilenstein.id}">
					<option value="${currentMeilenstein.id}">${currentMeilenstein.bezeichnung}</option>
				</c:if>
			</logic:iterate>
	    </select>
	</div>
	<br>
		
	<div class="textcontent_head">
		<bean:message key="zvf.import.evu" />
	</div>
		
	<div class="textcontent">
		<jsp:include page="viewZvfEVU.jsp"/>
	</div>	
	<br>
		
	<%-- Zuege--%>
	<div class="textcontent_head">
		<bean:message key="zvf.import.zugdaten" />
	</div>
		
	<div class="textcontent">
		<input type="checkbox" id="chkAlleAuswaehlen" onClick="activateCheckboxen()" class="checkbox"><bean:message key="zvf.import.alleauswaehlen"/>

		<%-- Zugtabellen --%>
		<bean:define id="artUmleitung" toScope="request"><bean:message key="ueb.zug.abweichung.umleitung"/></bean:define>
		<bean:define id="artVerspaetung" toScope="request"><bean:message key="ueb.zug.abweichung.verspaetung"/></bean:define>
		<bean:define id="artAusfall" toScope="request"><bean:message key="ueb.zug.abweichung.ausfall"/></bean:define>
		<bean:define id="artVorplan" toScope="request"><bean:message key="ueb.zug.abweichung.vorplan"/></bean:define>
		<bean:define id="artGesperrt" toScope="request"><bean:message key="ueb.zug.abweichung.gesperrt"/></bean:define>
		<bean:define id="artErsatzhalte" toScope="request"><bean:message key="ueb.zug.abweichung.ersatzhalte"/></bean:define>
		<bean:define id="artRegelung" toScope="request"><bean:message key="ueb.zug.abweichung.regelung"/></bean:define>
		
		
		<%-- Umzuleitende Zuege --%>
		<div class="box">
			<fieldset>
		    	<legend><bean:message key="zvf.umleitzuege" /></legend>
				<bean:define toScope="request" id="zvfnr" value="${0}"/>
				<bean:define id="abweichungstyp" toScope="request"><bean:message key="ueb.zug.abweichung.umleitung"/></bean:define>
				<table class="colored" style="table-layout: fixed; width: 963px;text-align:center;" >
					<colgroup>
				        <col width="10"><col width="27"><col width="60"><col width="50"><col width="55">
				        <col width="55"><col width="46"><col width="120"><col width="120"><col width="160">
				        <col width="59"><col width="53"><col width="16">
				    </colgroup>
					
					<jsp:include page="viewZugTableHeader.jsp"/>
					<tbody class="zvfImportSelectZug">
						<jsp:include page="viewZugTable.jsp"/>
					</tbody>
				</table>
				<c:if test="${countZuege<=1000 }">
					<jsp:include page="viewZugDetailsTip.jsp"/>
				</c:if>
			</fieldset>
			<br>
		</div>
	
		<%-- Verspaetungen --%>
		<div class="box">
			<fieldset>
		    	<legend><bean:message key="zvf.verspaetung" /></legend>
				<bean:define toScope="request" id="zvfnr" value="${0}"/>
				<bean:define id="abweichungstyp" toScope="request"><bean:message key="ueb.zug.abweichung.verspaetung"/></bean:define>
				<table class="colored" style="table-layout: fixed; width: 963px;text-align:center;" >
					<colgroup>
				        <col width="10"><col width="27"><col width="60"><col width="55"><col width="55">
				        <col width="120"><col width="120"><col width="59"><col width="355">
				    </colgroup>
					<jsp:include page="viewZugTableHeader.jsp"/>
					<tbody class="zvfImportSelectZug">
						<jsp:include page="viewZugTable.jsp"/>
					</tbody>
				</table>
			</fieldset>
			<br>
		</div>
	
		<%-- Ausfaelle --%>
		<div class="box">
			<fieldset>
		    	<legend><bean:message key="zvf.ausfall" /></legend>
				<bean:define toScope="request" id="zvfnr" value="${0}"/>
				<bean:define id="abweichungstyp" toScope="request"><bean:message key="ueb.zug.abweichung.ausfall"/></bean:define>
				<table class="colored" style="table-layout: fixed; width: 963px;text-align:center;" >
					<colgroup>
				        <col width="10"><col width="27"><col width="60"><col width="55"><col width="55">
				        <col width="120"><col width="120"><col width="120"><col width="120"><col width="164">
				    </colgroup>
					<jsp:include page="viewZugTableHeader.jsp"/>
					<tbody class="zvfImportSelectZug">
						<jsp:include page="viewZugTable.jsp"/>
					</tbody>
				</table>
			</fieldset>
			<br>
		</div>
	
		<%-- Vorplanfahrten --%>
		<div class="box">
			<fieldset>
		    	<legend><bean:message key="zvf.vorplan" /></legend>
				<bean:define toScope="request" id="zvfnr" value="${0}"/>
				<bean:define id="abweichungstyp" toScope="request"><bean:message key="ueb.zug.abweichung.vorplan"/></bean:define>
				<table class="colored" style="table-layout: fixed; width: 963px;text-align:center;" >
					<colgroup>
				        <col width="10"><col width="27"><col width="60"><col width="55"><col width="55">
				        <col width="120"><col width="120"><col width="55"><col width="120"><col width="229">
				    </colgroup>
					<jsp:include page="viewZugTableHeader.jsp"/>
					<tbody class="zvfImportSelectZug">
						<jsp:include page="viewZugTable.jsp"/>
					</tbody>
				</table>
			</fieldset>
			<br>
		</div>
	
		<%-- Ausfall von Verhehrshalten --%>
		<div class="box">
			<fieldset>
		    	<legend><bean:message key="zvf.ersatzhalte" /></legend>
				<bean:define toScope="request" id="zvfnr" value="${0}"/>
				<bean:define id="abweichungstyp" toScope="request"><bean:message key="ueb.zug.abweichung.ersatzhalte"/></bean:define>
				<table class="colored" style="table-layout: fixed; width: 963px;text-align:center;" >
					<colgroup>
				        <col width="10"><col width="27"><col width="60"><col width="55"><col width="55">
				        <col width="120"><col width="120"><col width="120"><col width="120"><col width="164">
				    </colgroup>
					<jsp:include page="viewZugTableHeader.jsp"/>
					<tbody class="zvfImportSelectZug">
						<jsp:include page="viewZugTable.jsp"/>
					</tbody>
				</table>
			</fieldset>
			<br>
		</div>
	
		<%-- Sperren von Bedarfsplänen --%>
		<div class="box">
			<fieldset>
		    	<legend><bean:message key="zvf.gesperrt" /></legend>
				<bean:define toScope="request" id="zvfnr" value="${0}"/>
				<bean:define id="abweichungstyp" toScope="request"><bean:message key="ueb.zug.abweichung.gesperrt"/></bean:define>
				<table class="colored" style="table-layout: fixed; width: 963px;text-align:center;" >
					<colgroup>
						<col width="10"><col width="27"><col width="60"><col width="55"><col width="55">
				        <col width="120"><col width="120"><col width="424">
				    </colgroup>
					<jsp:include page="viewZugTableHeader.jsp"/>
					<tbody class="zvfImportSelectZug">
						<jsp:include page="viewZugTable.jsp"/>
					</tbody>
				</table>
			</fieldset>
			<br>
		</div>
		
		<%-- Andere zugbezogene Regelungen --%>
		<div class="box">
			<fieldset>
		    	<legend><bean:message key="zvf.regelung" /></legend>
				<bean:define toScope="request" id="zvfnr" value="${0}"/>
				<bean:define id="abweichungstyp" toScope="request"><bean:message key="ueb.zug.abweichung.regelung"/></bean:define>
				<table class="colored" style="table-layout: fixed; width: 963px;text-align:center;" >
					<colgroup>
				        <col width="10"><col width="27"><col width="60"><col width="55"><col width="55">
				        <col width="120"><col width="120"><col width="120"><col width="120"><col width="164">
				    </colgroup>
					<jsp:include page="viewZugTableHeader.jsp"/>
					<tbody class="zvfImportSelectZug">
						<jsp:include page="viewZugTable.jsp"/>
					</tbody>
				</table>
			</fieldset>
			<br>
		</div>
	</div>