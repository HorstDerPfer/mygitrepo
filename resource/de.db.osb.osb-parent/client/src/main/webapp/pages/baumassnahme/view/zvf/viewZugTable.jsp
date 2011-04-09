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

<div id="zugliste">
	<display:table
		id="currentZug" 
		name="zugCollection" 
		export="false"
		class="colored"
		style="margin-top:5px;width: 963px;" 
		requestURI="${requestURI}"
		defaultsort="1"
		defaultorder="ascending">
	
			<display:column property="laufendeNr" titleKey="ueb.zug.lfdnr.short" sortable="true" style="text-align:right;width: 27px;"/>
			<display:column property="zugnr" titleKey="ueb.zug.zugnr.short" sortable="true" style="width: 55px;"/>
			<display:column titleKey="ueb.zug.datum" sortable="true" style="width: 60px;">
				<bean:write name="currentZug" property="verkehrstag" format="dd.MM.yyyy" />
			</display:column>
			<c:if test="${abweichungstyp==artUmleitung }">
				<display:column property="tageswechsel" titleKey="ueb.zug.tageswechsel" sortable="true" style="width: 45px;"/>
			</c:if>
			<display:column property="zugbez" titleKey="ueb.zug.zuggattung.short" sortable="true" style="width: 55px;"/>
			<c:if test="${abweichungstyp==artUmleitung }">
				<display:column titleKey="ueb.zug.bedarf" sortable="true" sortProperty="bedarf" style="width: 45px;">
					<logic:notEmpty name="currentZug" property="bedarf">
						<bean:message key="ueb.zug.bedarf.${currentZug.bedarf}" />
					</logic:notEmpty>
				</display:column>
			</c:if>
			<display:column titleKey="ueb.zug.abgangsbhf" sortable="true" sortProperty="regelweg.abgangsbahnhof.langName" style="width: 120px;">
				<logic:notEmpty name="currentZug" property="regelweg.abgangsbahnhof">
					<bean:write name="currentZug" property="regelweg.abgangsbahnhof.langName"  />
				</logic:notEmpty>
			</display:column>
			<display:column titleKey="ueb.zug.zielbhf" sortable="true" sortProperty="regelweg.zielbahnhof.langName" style="width: 120px;">
				<logic:notEmpty name="currentZug" property="regelweg.zielbahnhof">
					<bean:write name="currentZug" property="regelweg.zielbahnhof.langName"  />
				</logic:notEmpty>
			</display:column>
			<display:column titleKey="ueb.zug.richtung.short" sortable="true" sortProperty="richtung" style="width: 27px;">
				<bean:message key="ueb.zug.richtung.${currentZug.richtung}" />
			</display:column>
			
			<c:if test="${abweichungstyp==artUmleitung }">
				<display:column property="abweichung.umleitung" titleKey="ueb.zug.umleitungsstrecke" sortable="true" style="width: 160px;"/>
				<display:column property="abweichung.verspaetung" titleKey="ueb.zug.verspaetung.short" sortable="true" style="width: 35px;"/>
			</c:if>
			
			<c:if test="${abweichungstyp==artVerspaetung }">
				<display:column property="abweichung.verspaetung" titleKey="ueb.zug.verspaetung" sortable="true" style="width: 59px;"/>
				<display:column property="bemerkung" titleKey="ueb.zug.bemerkung" sortable="true" style="width: 355px;"/>
			</c:if>
			
			<c:if test="${abweichungstyp==artAusfall }">
				<display:column titleKey="ueb.zug.ausfallab" sortable="true" sortProperty="abweichung.ausfallvon.langName" style="width: 120px;">
					<logic:notEmpty name="currentZug" property="abweichung.ausfallvon">
						<bean:write name="currentZug" property="abweichung.ausfallvon.langName" />
					</logic:notEmpty>
				</display:column>
				<display:column titleKey="ueb.zug.ausfallbis" sortable="true" sortProperty="abweichung.ausfallbis.langName" style="width: 120px;">
					<logic:notEmpty name="currentZug" property="abweichung.ausfallbis">
						<bean:write name="currentZug" property="abweichung.ausfallbis.langName" />
					</logic:notEmpty>
				</display:column>
				<display:column property="bemerkung" titleKey="ueb.zug.bemerkung" sortable="true" style="width: 164px;"/>
			</c:if>
			
			<c:if test="${abweichungstyp==artVorplan }">
				<display:column property="abweichung.verspaetung" titleKey="ueb.zug.zeitvorplan.short" sortable="true" style="width: 55px;"/>
				<display:column titleKey="ueb.zug.vorplanab" sortable="true" sortProperty="abweichung.vorplanab.langName" style="width: 120px;">
					<logic:notEmpty name="currentZug" property="abweichung.vorplanab">
						<bean:write name="currentZug" property="abweichung.vorplanab.langName" />
					</logic:notEmpty>
				</display:column>
				<display:column property="bemerkung" titleKey="ueb.zug.bemerkung" sortable="true" style="width: 229px;"/>
			</c:if>
			
			<c:if test="${abweichungstyp==artErsatzhalte }">
				<display:column titleKey="ueb.zug.ausfallverkehrshalt" style="width: 120px;">
					<table>
						<tbody>
							<logic:iterate id="currentHalt" name="currentZug" property="abweichung.halt" indexId="haltId">
								<tr>
									<td>
										<logic:notEmpty name="currentHalt" property="ausfall">
											<logic:notEmpty name="currentHalt" property="ausfall.langName">	
												<div style="text-align:center;"><bean:write name="currentHalt" property="ausfall.langName" /></div>
											</logic:notEmpty>
										</logic:notEmpty>
										<logic:empty name="currentHalt" property="ausfall.langName">
											<div>&#160;</div>
										</logic:empty>
									</td>
								</tr>
							</logic:iterate>
						</tbody>
					</table>
				</display:column>
				<display:column titleKey="ueb.zug.moeglicherersatzhalt" style="width: 120px;">
					<table>
						<tbody>
							<logic:iterate id="currentHalt" name="currentZug" property="abweichung.halt" indexId="haltId">
								<tr>
									<td>
										<logic:notEmpty name="currentHalt" property="ersatz">
											<logic:notEmpty name="currentHalt" property="ersatz.langName">
												<div style="text-align:center;"><bean:write name="currentHalt" property="ersatz.langName" /></div>
											</logic:notEmpty>
											<logic:empty name="currentHalt" property="ersatz.langName">
												<div style="text-align:center;">&#160;</div>
											</logic:empty>
										</logic:notEmpty>
										<logic:empty name="currentHalt" property="ersatz">
											<div style="text-align:center;">&#160;</div>
										</logic:empty>
									</td>
								</tr>
							</logic:iterate>
						</tbody>
					</table>
				</display:column>
				<display:column property="bemerkung" titleKey="ueb.zug.bemerkung" sortable="true" style="width: 164px;"/>
			</c:if>
			
			<c:if test="${abweichungstyp==artRegelung }">
				<display:column titleKey="ueb.zug.regelungsart" style="width: 120px;">
					<table>
						<tbody>
							<logic:iterate id="currentRegelung" name="currentZug" property="abweichung.regelungen" indexId="regId">
								<tr>
									<td>
										<div style="text-align:center;"><bean:write name="currentRegelung" property="art" /></div>
									</td>
								</tr>
							</logic:iterate>
						</tbody>
					</table>
				</display:column>
				<display:column titleKey="ueb.zug.inbst" style="width: 60px;">
					<table>
						<tbody>
							<logic:iterate id="currentRegelung" name="currentZug" property="abweichung.regelungen" indexId="regId">
								<tr>
									<td>
										<logic:notEmpty name="currentRegelung" property="giltIn">
											<logic:notEmpty name="currentRegelung" property="giltIn.langName">
												<div style="text-align:center;"><bean:write name="currentRegelung" property="giltIn.langName" /></div>
											</logic:notEmpty>
											<logic:empty name="currentRegelung" property="giltIn.langName">
												<div>&#160;</div>
											</logic:empty>
										</logic:notEmpty>
										<logic:empty name="currentRegelung" property="giltIn">
											<div>&#160;</div>
										</logic:empty>
									</td>
								</tr>
							</logic:iterate>
						</tbody>
					</table>
				</display:column>
				<display:column titleKey="ueb.zug.text" style="width: 60px;">
					<table>
						<tbody>
							<logic:iterate id="currentRegelung" name="currentZug" property="abweichung.regelungen" indexId="regId">
								<tr>
									<td>
										<div style="text-align:center;"><bean:write name="currentRegelung" property="text" /></div>
									</td>
								</tr>
							</logic:iterate>
						</tbody>
					</table>
				</display:column>
				<display:column property="bemerkung" titleKey="ueb.zug.bemerkung" sortable="true" style="width: 164px;"/>
			</c:if>
	
			<c:if test="${abweichungstyp==artGesperrt }">
				<display:column property="bemerkung" titleKey="ueb.zug.bemerkung" sortable="true" style="width: 424px;"/>
			</c:if>
			
			<c:if test="${abweichungstyp==artUmleitung }">
				<display:column titleKey="ueb.zug.qsks" style="width: 29px;">
					<logic:notEmpty name="currentZug" property="qs_ks">
						<div style="text-align:center"><bean:message key="ueb.zug.qsks.${currentZug.qs_ks}" /></div>
					</logic:notEmpty>
				</display:column>			
				<display:column property="bemerkung" titleKey="ueb.zug.bemerkung" sortable="true" style="width: 90px;"/>
			</c:if>
	</display:table>
</div>

