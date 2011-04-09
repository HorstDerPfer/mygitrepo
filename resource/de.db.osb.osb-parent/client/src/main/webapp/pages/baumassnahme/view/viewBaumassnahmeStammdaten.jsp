<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

				<div class="textcontent_left">
					<div class="box">
						<div class="label"><bean:message key="baumassnahme.fahrplanjahr" /></div>
						<div class="show">${baumassnahme.fahrplanjahr}</div>
					</div>
					<div class="box">
						<div class="label"><bean:message key="baumassnahme.art" /></div>
						<div class="show"><bean:write name="baumassnahme" property="art" /></div>
					</div>
					
					<div class="box">
						<div class="label"><bean:message key="baumassnahme.kigbau" /></div>						
						<div class="show"><bean:message key="baumassnahme.kigbau.${baumassnahme.kigBau}" /></div>
					</div>
					
					<div class="box">
						<div class="label"><bean:message key="baumassnahme.streckebbp" /></div>
						<div class="show"><bean:write name="baumassnahme" property="streckeBBP" /></div>
					</div>
					
					<div class="box">
						<div class="label"><bean:message key="baumassnahme.streckevzg" /></div>
						<div class="show"><bean:write name="baumassnahme" property="streckeVZG" /></div>
					</div>
					
					<div class="box">
						<div class="label"><bean:message key="baumassnahme.streckenabschnitt" /></div>
						<div class="show"><bean:write name="baumassnahme" property="streckenAbschnitt" /></div>
					</div>
					
					<div class="box">
						<div class="label"><bean:message key="baumassnahme.artdermassnahme" /></div>
						<div class="showMultiLines"><bean:write name="baumassnahme" property="artDerMassnahme" /></div>
					</div>
					
					<div class="box">
						<div class="label"><bean:message key="baumassnahme.betriebsweise" /></div>
						<div class="show"><bean:write name="baumassnahme" property="betriebsweise" /></div>
					</div>
					
					<div class="box">
						<div class="label"><bean:message key="baumassnahme.zeitraum" /></div>
						<div class="show">
							<bean:write name="baumassnahme" property="beginnDatum" format="dd.MM.yyyy" />
							 - 
							<bean:write name="baumassnahme" property="endDatum" format="dd.MM.yyyy" />
						</div>
					</div>
					
					<div class="box">
						<div class="label"><bean:message key="baumassnahme.beginn" /></div>
						<div class="show"><bean:write name="baumassnahme" property="beginnFuerTerminberechnung" format="dd.MM.yyyy" /></div>
					</div>
				</div>
				
				<div class="textcontent_right">
					<div class="box">
						<div class="label"><bean:message key="baumassnahme.regionalbereichbm" /></div>
						<div class="show">
							<logic:notEmpty name="baumassnahme" property="regionalbereichBM">
								<bean:write name="baumassnahme" property="regionalbereichBM" />
							</logic:notEmpty>
						</div>
					</div>
						
					<div class="box">
						<div class="label"><bean:message key="baumassnahme.regionalbereichfpl" /></div>
						<div class="show">
							<logic:notEmpty name="baumassnahme" property="regionalBereichFpl">
								<bean:write name="baumassnahme" property="regionalBereichFpl.name" />
							</logic:notEmpty>
						</div>
					</div>
						
					<div class="box">
						<div class="label"><bean:message key="baumassnahme.bearbeitungsbereich" /></div>
						<div class="show">
							<logic:notEmpty name="baumassnahme" property="bearbeitungsbereich">
								<bean:write name="baumassnahme" property="bearbeitungsbereich.name" />
							</logic:notEmpty>
						</div>
					</div>
						
					<div class="box">
						<div class="label"><bean:message key="baumassnahme.fplonr" /></div>
						<div class="show"><bean:write name="baumassnahme" property="fploNr" /></div>
					</div>
						
					<div class="box">
						<div class="label"><bean:message key="baumassnahme.vorgangsnr" /></div>
						<div class="show"><bean:write name="baumassnahme" property="vorgangsNr" /></div>
					</div>
						
					<div class="box">
						<div class="label"><bean:message key="baumassnahme.prioritaet" /></div>
						<div class="show">
							<logic:notEmpty name="baumassnahme" property="prioritaet">
								<bean:message key="baumassnahme.prioritaet.${baumassnahme.prioritaet}" />
							</logic:notEmpty>
						</div>
					</div>
					
				</div>
				<div class="box"></div>