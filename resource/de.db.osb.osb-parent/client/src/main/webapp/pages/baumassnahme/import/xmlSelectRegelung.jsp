<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="db.training.bob.model.Regelung"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

<html:xhtml/>

<jsp:include page="../../main_head.jsp"/>
	<jsp:include page="../../main_path.jsp"/>
		<jsp:include page="../../main_menu.jsp"/>

			<%-- oeffnet Punkt in Startmenue --%>
			<script type="text/javascript">
			    openMainMenu('navLink_bob_baumassnahme');
			    
			    function hideRegelungen() {
		    	    $j("tr.evenrow3").each(function(index, element) {$j(element).attr("style", "visibility: collapse;");});
		    	    $j("tr.oddrow3").each(function(index, element) {$j(element).attr("style", "visibility: collapse");});
		    	    $j("div.invisible").each(function(index, element) {$j(element).attr("style", "display: none");});
				}
				
			    function showRegelungen() {
			    	if ($("chkEinblenden").checked == false){
						hideRegelungen();
			    	} else {
			    		$j("tr.evenrow3").each(function(index, element) {$j(element).attr("style", "visibility: visible;");});
			    	    $j("tr.oddrow3").each(function(index, element) {$j(element).attr("style", "visibility: visible;");});
			    	    $j("div.invisible").each(function(index, element) {$j(element).attr("style", "display: block");});
			    	}
				}
			</script>
			
			<html:form action="/xmlSelectRegelung" enctype="multipart/form-data">
				
				<html:hidden name="xmlImportForm" property="fileContent"/>
				<html:hidden name="xmlImportForm" property="baumassnahmeId"/>
				
				<bean:define id="baumassnahmeId" name="xmlImportForm" property="baumassnahmeId"></bean:define>
			 	
			 	<div class="textcontent_head">
				    <bean:message key="baumassnahme.xml.auswahlregelung" />
				</div>
				<div class="textcontent">
					<div>
						<bean:message key="baumassnahme.xml.selectregelung" />
					</div>
					<input type="checkbox" id="chkEinblenden" onclick="showRegelungen()" class="checkbox">
						<bean:message key="baumassnahme.xml.schonimportiert" />
					</input>
				</div>
				
				<br />
				
				<logic:iterate id="currentbbpMassnahme" name="bbpmassnahmen" indexId="index">
					<div class="${currentbbpMassnahme.allRegs==true?'invisible':''}">
						<div class="textcontent_head" style="margin-top: 20px;">
						    <bean:message key="baumassnahme.xml.bbpmassnahme" />
						</div>
						<bean:define id="masId" scope="page" name="currentbbpMassnahme" property="masId"></bean:define>
	
						<div class="textcontent">
						
							<script type="text/javascript">
								function activateCheckboxen_${currentbbpMassnahme.masId }() {
									if ($("chkAlleAuswaehlen_${currentbbpMassnahme.masId }").checked == true){
										$j("tbody.zvfImportSelectZug_${currentbbpMassnahme.masId } input[type=checkbox]").each(function(index, element) {$j(element).attr("checked", "checked");});
									}else{
										$j("tbody.zvfImportSelectZug_${currentbbpMassnahme.masId } input[type=checkbox]").each(function(index, element) {$j(element).attr("checked", "");});
									}
								}
							</script>
							
							<input type="checkbox" id="chkAlleAuswaehlen_${currentbbpMassnahme.masId }" onclick="activateCheckboxen_${currentbbpMassnahme.masId }()" class="checkbox">
								<bean:message key="zvf.import.alleauswaehlen" />
							</input>
							
							
							<br /><br />
							
							<table class="colored" style="text-align:center;">
								<thead>
									<tr>
										<th><bean:message key="baumassnahme.xml.invorganguebernehmen" /></th>
										<c:if test="${baumassnahmeId==0}">
											<th><bean:message key="baumassnahme.xml.hauptbbp" /></th>
										</c:if>
										<th><bean:message key="bbpmassnahme.masid" /></th>
										<th><bean:message key="bbpmassnahme.streckebbpvzg" /></th>
										<th><bean:message key="bbpmassnahme.arbeiten" /></th>
										<th><bean:message key="bbpmassnahme.bstvon" /></th>
										<th><bean:message key="bbpmassnahme.bstbis" /></th>
										<th><bean:message key="bbpmassnahme.beginn" /></th>
										<th><bean:message key="bbpmassnahme.ende" /></th>
										<th><bean:message key="bbpmassnahme.regionalbereich" /></th>
										<th></th>
										<th></th>
									</tr>
								</thead>
								<tbody class="zvfImportSelectZug_${currentbbpMassnahme.masId }">
									<tr class="${currentbbpMassnahme.allRegs==true?'evenrow3':'evenrow'}">
										<td>
											<c:if test="${index==0}">
												<input type="checkbox" name="checkBBPMassnahme" value="${currentbbpMassnahme.masId}" class="checkbox"/>
											</c:if>
											<c:if test="${index>0}">
												<input type="checkbox" name="checkBBPMassnahme" value="${currentbbpMassnahme.masId}" class="checkbox"/>
											</c:if>
										</td>
										<c:if test="${baumassnahmeId==0}">
											<td>
												<c:if test="${index==0}">
													<input type="radio" name="radioBBPMassnahme" value="${currentbbpMassnahme.masId}" class="checkbox"/>
												</c:if>
												<c:if test="${index>0}">
													<input type="radio" name="radioBBPMassnahme" value="${currentbbpMassnahme.masId}" class="checkbox"/>
												</c:if>
											</td>
										</c:if>
										<td><bean:write name="currentbbpMassnahme" property="masId" /></td>
										<td><bean:write name="currentbbpMassnahme" property="streckeBBP" />/<bean:write name="currentbbpMassnahme" property="streckeVZG" /></td>
										<td><bean:write name="currentbbpMassnahme" property="arbeiten" /></td>
										<td><bean:write name="currentbbpMassnahme" property="bstVonLang" /></td>
										<td><bean:write name="currentbbpMassnahme" property="bstBisLang" /></td>
										<td><bean:write name="currentbbpMassnahme" property="beginn" format="dd.MM.yy" /></td>
										<td><bean:write name="currentbbpMassnahme" property="ende" format="dd.MM.yy" /></td>
										<td><bean:write name="currentbbpMassnahme" property="regionalbereich" /></td>
										<td></td>
										<td></td>
									</tr>
								</tbody>
							</table>
							
							<br/>
							
							<table class="colored" style="text-align:center;">
								<thead>
									<tr style="background-color: #999999;">
										<th><bean:message key="baumassnahme.xml.invorganguebernehmen" /></th>
										<c:if test="${baumassnahmeId==0}">
											<th><bean:message key="baumassnahme.xml.fuerterminberechnung" /></th>
										</c:if>
										<th><bean:message key="baumassnahme.vorgangsnr" /></th>
										<th><bean:message key="bbpmassnahme.regid" /></th>
										<th><bean:message key="bbpmassnahme.streckebbpvzg" /></th>
										<th><bean:message key="bbpmassnahme.bstvon" /></th>
										<th><bean:message key="bbpmassnahme.bstbis" /></th>
										<th><bean:message key="bbpmassnahme.beginn" /></th>
										<th><bean:message key="bbpmassnahme.ende" /></th>
										<th><bean:message key="bbpmassnahme.betriebsweise" /></th>
										<th><bean:message key="bbpmassnahme.bplarttext" /></th>
									</tr>
								</thead>
								<tbody class="zvfImportSelectZug_${currentbbpMassnahme.masId }">
								    <% Boolean visibleRowEven = false; 
								       Boolean invisibleRowEven = false; %>
									<logic:iterate id="currentRegelung" name="currentbbpMassnahme" property="regelungen" indexId="indexReg">
										<% String styleClassString = null; 
											if(((Regelung)currentRegelung).getVorgangsnr()==null){
												if (visibleRowEven==true){
													visibleRowEven=false;
													styleClassString= "oddrow";
												} else {
													visibleRowEven=true;
													styleClassString= "evenrow";
												}
											} else {
												if (invisibleRowEven==true){
													invisibleRowEven=false;
													styleClassString= "oddrow3";
												} else {
													invisibleRowEven=true;
													styleClassString= "evenrow3";
												}
											}
										%>
										<bean:define toScope="page" id="styleClass" value="<%=styleClassString %>"/>
										<tr class="${styleClass}">
											<td>
												<c:if test="${indexReg==0}">
													<input type="checkbox" name="checkRegelung" value="${currentRegelung.regelungId}" class="checkbox"/>
												</c:if>
												<c:if test="${indexReg>0}">
													<input type="checkbox" name="checkRegelung" value="${currentRegelung.regelungId}" class="checkbox"/>
												</c:if>
											</td>
											<c:if test="${baumassnahmeId==0}">
												<td>
													<c:if test="${indexReg==0}">
														<input type="radio" name="radioRegelung" value="${currentRegelung.regelungId}" class="checkbox"/>
													</c:if>
													<c:if test="${indexReg>0}">
														<input type="radio" name="radioRegelung" value="${currentRegelung.regelungId}" class="checkbox"/>
													</c:if>
												</td>	
											</c:if>
											<td>
												<logic:notEmpty name="currentRegelung" property="vorgangsnr">
													<logic:iterate id="currentVorgangsnr" name="currentRegelung" property="vorgangsnr" indexId="indexVorgang">
														<div>
															${currentVorgangsnr}
															<c:if test="${ (fn:length(currentRegelung.vorgangsnr) > (indexVorgang+1)) }">,</c:if>														
														</div>
													</logic:iterate>
												</logic:notEmpty>
											</td>
											<td>
												<bean:write name="currentRegelung" property="regelungId"/>
											</td>
											<td>
												<bean:write name="currentRegelung" property="streckeBBP"/>
												/
												<bean:write name="currentRegelung" property="streckeVZG"/>
											</td>
											<td><bean:write name="currentRegelung" property="betriebsStelleVon"/></td>
											<td><bean:write name="currentRegelung" property="betriebsStelleBis"/></td>
											<td><bean:write name="currentRegelung" property="beginn" format="dd.MM.yy"/></td>
											<td><bean:write name="currentRegelung" property="ende" format="dd.MM.yy"/></td>
											<td><bean:write name="currentRegelung" property="betriebsweise"/></td>
											<td><bean:write name="currentRegelung" property="bplArtText"/></td>
										</tr>
									</logic:iterate>
								</tbody>
							</table>
						</div>
					</div>
				</logic:iterate>
			</html:form>
			
			<div class="buttonBar">
				<html:link href="#" onclick="document.forms[0].submit();" styleClass="buttonSave" styleId="buttonSave">
					<bean:message key="button.save" />
				</html:link>
				<html:link action="/xmlImport1" styleClass="buttonBack">
					<bean:message key="button.back" />
					<html:param name="baumassnahmeId" value="0" />
				</html:link>
			</div> 
		
		
<jsp:include page="../../main_footer.jsp"/>