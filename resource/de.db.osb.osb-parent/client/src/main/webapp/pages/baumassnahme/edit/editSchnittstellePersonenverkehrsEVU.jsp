<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

<div id="divPEVU">
	<bean:define id="urlPEVU" toScope="page"><c:url value="refreshPEVU.do" /></bean:define>
	<table class="colored">
		<%-- Spaltenüberschriften --%>
		<thead>
			<tr>
				<th class="left"><bean:message key="baumassnahme.termine.evu" /></th>
				<c:if test='${baumassnahme.art == "KS"}'>
					<th class="center"><bean:message key="baumassnahme.termine.studie" /></th>
				</c:if>
				<th class="center"><bean:message key="baumassnahme.termine.zvfentwurf.nobr" /></th>
				<th class="center"><bean:message key="baumassnahme.termine.stellungnahmeevu" /></th>
				<th class="center"><bean:message key="baumassnahme.termine.zvf" /></th>
				<c:if test='${(baumassnahme.art == "A") || (baumassnahme.art == "QS" )|| (baumassnahme.art == "KS")}'>
					<th class="center"><bean:message key="baumassnahme.termine.masteruebergabeblattpv" /></th>
				</c:if>
				<th class="center"><bean:message key="baumassnahme.termine.uebergabeblattpv" /></th>
				<%--<c:if test='${baumassnahme.art != "A"}'>--%>
					<th class="center"><bean:message key="baumassnahme.termine.fplo" /></th>
					<th class="center"><bean:message key="baumassnahme.termine.eingabegfd_z" /></th>
				<%--</c:if>--%>
				<th class="center"><bean:message key="baumassnahme.termine.ausfaellesev" /></th>
				<th class="center"><bean:message key="baumassnahme.termine.bkonzeptevu" /></th>
				<th></th>
			</tr>
		</thead>
		<tbody id="tbody_pevu">
			<%-- Ist-Termine --%>
			<logic:iterate id="currentEvuGruppe" name="baumassnahme" property="pevus" indexId="index">
				<bean:define id="name" scope="page" name="currentEvuGruppe" property="evuGruppe.name"></bean:define>
				<bean:define id="evuId" scope="page" name="currentEvuGruppe" property="evuGruppe.id"></bean:define>

				<bean:define toScope="page" id="styleClass" value="${styleClass=='evenrow'?'oddrow':'evenrow'}"/>
				<tr id="pevu__${evuId}" class="${styleClass}">
					<td style="overflow:hidden;width:120px;" class="left"><%= name %></td>
					<c:if test='${baumassnahme.art == "KS"}'>
						<td>
							<% String property1Name = "pevuStudieGrobkonzept(" + evuId + ")"; %>
							<html:text name="baumassnahmeForm" property="<%=property1Name %>" styleId="pevuStudieGrobkonzept__${evuId}" styleClass="dateShort" maxlength="10"/>
						</td>
					</c:if>
					<td>
						<% String property2Name = "pevuZvfEntwurf(" + evuId + ")"; %>
						<html:text name="baumassnahmeForm" property="<%=property2Name %>" styleId="pevuZvfEntwurf__${evuId}" styleClass="dateShort" maxlength="10"/>
						<% String property2_1_name = "pevuIsZvfEntwurfErforderlich(" + evuId + ")"; %>
						<br /><html:checkbox name="baumassnahmeForm" property="<%=property2_1_name %>" styleId="chk_pevuZvfEntwurf__${evuId}" styleClass="checkbox" titleKey="baumassnahme.termine.required.short" />
						<bean:message key="baumassnahme.termine.required.short" />
					</td>
					<td>
						<% String property3Name = "pevuStellungnahmeEVU(" + evuId + ")"; %>
						<html:text name="baumassnahmeForm" property="<%=property3Name %>" styleId="pevuStellungnahmeEVU__${evuId}" styleClass="dateShort" maxlength="10"/>
						<% String property3_1_name = "pevuIsStellungnahmeEVUErforderlich(" + evuId + ")"; %>
						<br /><html:checkbox name="baumassnahmeForm" property="<%=property3_1_name %>" styleId="chk_pevuStellungnahmeEVU__${evuId}" styleClass="checkbox" titleKey="baumassnahme.termine.required.short" />
						<bean:message key="baumassnahme.termine.required.short" />
					</td>
					<td>
						<% String property4Name = "pevuZvF(" + evuId + ")"; %>
						<html:text name="baumassnahmeForm" property="<%=property4Name %>" styleId="pevuZvf__${evuId}" styleClass="dateShort" maxlength="10"/>
						<% String property4_1_name = "pevuIsZvFErforderlich(" + evuId + ")"; %>
						<br /><html:checkbox name="baumassnahmeForm" property="<%=property4_1_name %>" styleId="chk_pevuZvf__${evuId}" styleClass="checkbox" titleKey="baumassnahme.termine.required.short" />
						<bean:message key="baumassnahme.termine.required.short" />
					</td>
					<c:if test='${(baumassnahme.art == "A") || (baumassnahme.art == "QS") || (baumassnahme.art == "KS")}'>
						<td>
							<% String property5Name = "pevuMasterUebergabeblattPV(" + evuId + ")"; %>
							<html:text name="baumassnahmeForm" property="<%=property5Name %>" styleId="pevuMasterUebergabeblattPV__${evuId}" styleClass="dateShort" maxlength="10"/>
							<% String property5_1_name = "pevuIsMasterUebergabeblattPVErforderlich(" + evuId + ")";  %>
							<br /><html:checkbox name="baumassnahmeForm" property="<%=property5_1_name %>" styleId="chk_pevuMasterUebergabeblattPV__${evuId}" styleClass="checkbox" titleKey="baumassnahme.termine.required.short" />
							<bean:message key="baumassnahme.termine.required.short" />
						</td>
					</c:if>
					<td>
						<% String property6Name = "pevuUebergabeblattPV(" + evuId + ")"; %>
						<html:text name="baumassnahmeForm" property="<%=property6Name %>" styleId="pevuUebergabeblattPV__${evuId}" styleClass="dateShort" maxlength="10"/>
						<% String property6_1_name = "pevuIsUebergabeblattPVErforderlich(" + evuId + ")"; %>
						<br /><html:checkbox name="baumassnahmeForm" property="<%=property6_1_name %>" styleId="chk_pevuUebergabeblattPV__${evuId}" styleClass="checkbox" titleKey="baumassnahme.termine.required.short" />
						<bean:message key="baumassnahme.termine.required.short" />
					</td>
					<%--<c:if test='${baumassnahme.art != "A"}'>--%>
						<td>
							<% String property7Name = "pevuFplo(" + evuId + ")"; %>
							<html:text name="baumassnahmeForm" property="<%=property7Name %>" styleId="pevuFplo__${evuId}" styleClass="dateShort" maxlength="10"/>
							<% String property7_1_name = "pevuIsFploErforderlich(" + evuId + ")"; %>
							<br /><html:checkbox name="baumassnahmeForm" property="<%=property7_1_name %>" styleId="chk_pevuFplo__${evuId}" styleClass="checkbox" titleKey="baumassnahme.termine.required.short" />
							<bean:message key="baumassnahme.termine.required.short" />
						</td>
						<td>
							<% String property8Name = "pevuEingabeGFD_Z(" + evuId + ")"; %>
							<html:text name="baumassnahmeForm" property="<%=property8Name %>" styleId="pevuEingabeGFD_Z__${evuId}" styleClass="dateShort" maxlength="10"/>
							<% String property8_1_name = "pevuIsEingabeGFD_ZErforderlich(" + evuId + ")"; %>
							<br /><html:checkbox name="baumassnahmeForm" property="<%=property8_1_name %>" styleId="chk_pevuEingabeGFD_Z__${evuId}" styleClass="checkbox" titleKey="baumassnahme.termine.required.short" />
							<bean:message key="baumassnahme.termine.required.short" />
						</td>
					<%--</c:if>--%>
					<td>
						<% String property9Name = "pevuAusfaelleSEV(" + evuId + ")"; %>
						<html:radio name="baumassnahmeForm" property="<%=property9Name %>" value="true" styleClass="checkbox" />
						<bean:message key="baumassnahme.termine.ausfaellesev.true" />
						<br/>
						<html:radio name="baumassnahmeForm" property="<%=property9Name %>" value="false" styleClass="checkbox" />
						<bean:message key="baumassnahme.termine.ausfaellesev.false" />
					</td>
					<td>
						<% String property10Name = "pevuBKonzeptEVU(" + evuId + ")"; %>
						<html:text name="baumassnahmeForm" property="<%=property10Name %>" styleId="pevuBKonzeptEVU__${evuId}" styleClass="dateShort" maxlength="10"/>
						<% String property10_1_name = "pevuIsBKonzeptEVUErforderlich(" + evuId + ")"; %>
						<br /><html:checkbox name="baumassnahmeForm" property="<%=property10_1_name %>" styleId="chk_pevuBKonzeptEVU__${evuId}" styleClass="checkbox" titleKey="baumassnahme.termine.required.short" />
						<bean:message key="baumassnahme.termine.required.short" />
					</td>
					<td>
						<bean:define id="confirmText" toScope="page"><bean:message key="confirm.evu.delete" /></bean:define>
						<a href="#" onclick="if(confirmLink(this.href, '${confirmText}')) removeEvuRow('tbody_pevu', '${evuId}');return false;" class="delete">&nbsp;</a>
					 </td> 
				</tr>								
			</logic:iterate>
		</tbody>
	</table>
	<div id="pevuError" style="color:red;font-weight:bold;width:100%;display:none;">
		<bean:message key="error.evu.doubleinsert" />
	</div>
</div>