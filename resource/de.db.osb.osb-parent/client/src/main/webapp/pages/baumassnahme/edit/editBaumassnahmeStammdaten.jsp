<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
			
		    	<div class="textcontent_left">
					<html:hidden property="id" styleId="id"/>
					
					<div class="box">
						<div class="label"><bean:message key="baumassnahme.fahrplanjahr" /></div>
						<div class="show">
							<c:if test="${baumassnahme.fahrplanjahr!=0}">
								${baumassnahme.fahrplanjahr}
							</c:if>
						</div>
					</div>
					
					<div class="box">
						<div class="label"><label for="art"><bean:message key="baumassnahme.art" /></label></div>
						<div class="input">
							<html:select property="art" styleId="art" >
								<logic:iterate id="currentArt" name="arten">
									<html:option value="${currentArt}">
										<bean:message key="baumassnahme.art.${currentArt}" />
									</html:option>
								</logic:iterate>
							</html:select>
						</div>
					</div>
					
					<div class="box">
						<div class="label"><label for="kigBau"><bean:message key="baumassnahme.kigbau" /></label></div>
						<div class="input">
							<html:radio property="kigBau" value="true" styleClass="checkbox" />
							<bean:message key="baumassnahme.kigbau.true" />
							<html:radio property="kigBau" value="false" styleClass="checkbox" />
							<bean:message key="baumassnahme.kigbau.false" />
						</div>
					</div>
					
					<div class="box">
						<div class="label"><label for="streckeBBP"><bean:message key="baumassnahme.streckebbp" /></label></div>
						<div class="input"><html:text property="streckeBBP" styleId="streckeBBP" maxlength="5"/></div>
					</div>
					
					<div class="box">
						<div class="label"><label for="streckeVZG"><bean:message key="baumassnahme.streckevzg" /></label></div>
						<div class="input"><html:text property="streckeVZG" styleId="streckeVZG" maxlength="4"/></div>
					</div>
					
					<div class="box">
						<div class="label"><label for="streckenAbschnitt"><bean:message key="baumassnahme.streckenabschnitt" /></label></div>
						<div class="input"><html:text property="streckenAbschnitt" styleId="streckenAbschnitt" maxlength="255"/></div>
					</div>
					
					<div class="box">
						<div class="label"><label for="artDerMassnahme"><bean:message key="baumassnahme.artdermassnahme" /></label></div>
						<div class="input"><html:text property="artDerMassnahme" styleId="artDerMassnahme" maxlength="255"/></div>
					</div>
					
					<div class="box">
						<div class="label"><label for="betriebsweise"><bean:message key="baumassnahme.betriebsweise" /></label></div>
						<div class="input"><html:text property="betriebsweise" styleId="betriebsweise" maxlength="255"/></div>
					</div>
				
					<div class="box">
						<div class="label"><label for="beginnFuerTerminberechnung"><bean:message key="baumassnahme.beginn" /></label></div>
						<div class="input"><html:text property="beginnFuerTerminberechnung" styleId="beginnFuerTerminberechnung" styleClass="date" maxlength="10" /></div>
					</div>
					
					<div class="box">
						<div class="label"><label for="beginnDatum"><bean:message key="baumassnahme.beginndatum" /></label></div>
						<html:text property="beginnDatum" styleId="beginnDatum" styleClass="date" maxlength="10" />
					
						<label for="endDatum"><bean:message key="baumassnahme.enddatum" /></label>
						<html:text property="endDatum" styleId="endDatum" styleClass="date" maxlength="10" />
					</div>
				</div>
				
				<div class="textcontent_right">
					<div class="box">
						<div class="label">
							<label for="regionalbereichBM"><bean:message key="baumassnahme.regionalbereichbm" /></label>
							<img src="<c:url value='/static/img/indicator.gif' />" id="regionalbereichBMIndicator" style="display:none;" />
						</div>
						<div class="input">
							<html:text property="regionalbereichBM" styleId="regionalbereichBM" maxlength="255" />
							<div id="regionalbereichBMSelect" class="autocomplete"></div>
						</div>
					</div>
					
					
					<div class="box">
						<bean:define id="urlBearbeitungsbereich" toScope="page"><c:url value="refreshBearbeitungsbereich.do" /></bean:define>
						<div class="label"><label for="regionalBereichFpl"><bean:message key="baumassnahme.regionalbereichfpl" /></label></div>
						<div class="input">
							<html:select property="regionalBereichFpl" styleId="regionalBereichFpl" onchange="refreshBearbeitungsbereich('${urlBearbeitungsbereich}', this.value, 'BAUMASSNAHME');">
								<html:optionsCollection name="regionalbereiche" value="id" label="name"/>
							</html:select>
						</div>
					</div>
					
					<jsp:include page="editBearbeitungsbereich.jsp"></jsp:include>
					
					<div class="box">
						<div class="label"><label for="fploNr"><bean:message key="baumassnahme.fplonr" /></label></div>
						<div class="input"><html:text property="fploNr" styleId="fploNr" maxlength="10"/></div>
					</div>

					<div class="box">
						<div class="label"><label for="vorgangsNr"><bean:message key="baumassnahme.vorgangsnr" /></label></div>
						<div class="show">
							<c:if test="${baumassnahme.vorgangsNr!=null}">
								${baumassnahme.vorgangsNr}
							</c:if>
						</div>
						<%--<div class="input"><html:text property="vorgangsNr" styleId="vorgangsNr" maxlength="10" disabled="true"/></div> --%>
					</div>

					<div class="box">
						<div class="label"><label for="prioritaet"><bean:message key="baumassnahme.prioritaet" /></label></div>
						<div class="input">
							<html:select property="prioritaet" styleId="prioritaet">
								<html:option value=""><bean:message key="common.select.option" /></html:option>
								<logic:iterate id="currentPrioritaet" name="prioritaeten">
									<html:option value="${currentPrioritaet}">
										<bean:message key="baumassnahme.prioritaet.${currentPrioritaet}" />
									</html:option>
								</logic:iterate>
							</html:select>
						</div>
					</div>
				</div>
			
				<script type="text/javascript">
					new Ajax.Autocompleter(
							"regionalbereichBM", 
							"regionalbereichBMSelect", 
							"<c:url value='/AutoCompleteRegionalbereichBM.view'/>", 
							{
								indicator: 'regionalbereichBMIndicator', 
								minChars: 1, 
								paramName: 'keyword'
							}
					);
				</script>