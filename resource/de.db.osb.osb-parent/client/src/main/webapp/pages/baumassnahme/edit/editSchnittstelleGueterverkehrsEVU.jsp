<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

<div id="divGEVU">
	<bean:define id="urlGEVU" toScope="page"><c:url value="refreshGEVU.do" /></bean:define>
	<table class="colored" style="text-align:center;">
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
				<c:if test='${(baumassnahme.art == "A") || (baumassnahme.art == "QS") || (baumassnahme.art == "KS")}'>
					<th class="center"><bean:message key="baumassnahme.termine.masteruebergabeblattgv" /></th>
				</c:if>
				<th class="center"><bean:message key="baumassnahme.termine.uebergabeblattgv" /></th>
				<%--<c:if test='${baumassnahme.art != "A"}'>--%>
					<th class="center"><bean:message key="baumassnahme.termine.fplo" /></th>
					<th class="center"><bean:message key="baumassnahme.termine.eingabegfd_z" /></th>
				<%--</c:if>--%>
				<th></th>
			</tr>
		</thead>
		<tbody id="tbody_gevu">
			<%-- Ist-Termine --%>
			<logic:iterate id="currentEvuGruppe" name="baumassnahme" property="gevus" indexId="index">
				<bean:define id="name" scope="page" name="currentEvuGruppe" property="evuGruppe.name"></bean:define>
			 	<bean:define id="evuId" scope="page" name="currentEvuGruppe" property="evuGruppe.id"></bean:define>

				<bean:define toScope="page" id="styleClass" value="${styleClass=='evenrow'?'oddrow':'evenrow'}"/>
				<tr id="gevu__${evuId}" class="${styleClass}">
					<td style="overflow:hidden;max-width:120px;min-width:80px;">
						<div class="box" style="text-align:left"><%= name %></div>
					</td>
			    	<c:if test='${baumassnahme.art == "KS"}'>
						<td>
							<% String property1Name = "gevuStudieGrobkonzept(" + evuId + ")"; %>
							<html:text name="baumassnahmeForm" property="<%=property1Name %>" styleId="gevuStudieGrobkonzept__${evuId}" styleClass="dateShort" maxlength="10"/>
						</td>
					</c:if>
					<td>
						<% String property2Name = "gevuZvfEntwurf(" + evuId + ")"; %>
						<html:text name="baumassnahmeForm" property="<%=property2Name %>" styleId="gevuZvfEntwurf__${evuId}" styleClass="dateShort" maxlength="10"/>
						<% String property2_1_name = "gevuIsZvFEntwurfErforderlich(" + evuId + ")"; %>
						<br /><html:checkbox name="baumassnahmeForm" property="<%=property2_1_name %>" styleId="chk_gevuZvfEntwurf__${evuId}" styleClass="checkbox" titleKey="baumassnahme.termine.required" />
						<bean:message key="baumassnahme.termine.required.short" />
					</td>
					<td>
						<% String property3Name = "gevuStellungnahmeEVU(" + evuId + ")"; %>
						<html:text name="baumassnahmeForm" property="<%=property3Name %>" styleId="gevuStellungnahmeEVU__${evuId}" styleClass="dateShort" maxlength="10"/>
						<% String property3_1_name = "gevuIsStellungnahmeEVUErforderlich(" + evuId + ")"; %>
						<br /><html:checkbox name="baumassnahmeForm" property="<%=property3_1_name %>" styleId="chk_gevuStellungnahmeEVU__${evuId}" styleClass="checkbox" titleKey="baumassnahme.termine.required.short" />
						<bean:message key="baumassnahme.termine.required.short" />
					</td>
					<td>
						<% String property4Name = "gevuZvF(" + evuId + ")"; %>
						<html:text name="baumassnahmeForm" property="<%=property4Name %>" styleId="gevuZvf__${evuId}" styleClass="dateShort" maxlength="10"/>
						<% String property4_1_name = "gevuIsZvFErforderlich(" + evuId + ")"; %>
						<br /><html:checkbox name="baumassnahmeForm" property="<%=property4_1_name %>" styleId="chk_gevuZvf__${evuId}" styleClass="checkbox" titleKey="baumassnahme.termine.required.short" />
						<bean:message key="baumassnahme.termine.required.short" />
					</td>
					<c:if test='${(baumassnahme.art == "A") || (baumassnahme.art == "QS") || (baumassnahme.art == "KS")}'>
						<td>
							<% String property5Name = "gevuMasterUebergabeblattGV(" + evuId + ")"; %>
							<html:text name="baumassnahmeForm" property="<%=property5Name %>" styleId="gevuMasterUebergabeblattGV__${evuId}" styleClass="dateShort" maxlength="10"/>
							<% String property5_1_name = "gevuIsMasterUebergabeblattGVErforderlich(" + evuId + ")"; %>
							<br /><html:checkbox name="baumassnahmeForm" property="<%=property5_1_name %>" styleId="chk_gevuMasterUebergabeblattGV__${evuId}" styleClass="checkbox" titleKey="baumassnahme.termine.required.short" />
							<bean:message key="baumassnahme.termine.required.short" />
						</td>
					</c:if>
					<td>
						<% String property6Name = "gevuUebergabeblattGV(" + evuId + ")"; %>
						<html:text name="baumassnahmeForm" property="<%=property6Name %>"styleId="gevuUebergabeblattGV__${evuId}" styleClass="dateShort" maxlength="10"/>
						<% String property6_1_name = "gevuIsUebergabeblattGVErforderlich(" + evuId + ")"; %>
						<br /><html:checkbox name="baumassnahmeForm" property="<%=property6_1_name %>" styleId="chk_gevuUebergabeblattGV__${evuId}" styleClass="checkbox" titleKey="baumassnahme.termine.required.short" />
						<bean:message key="baumassnahme.termine.required.short" />
					</td>
					<%--<c:if test='${baumassnahme.art != "A"}'>--%>
						<td>
							<% String property7Name = "gevuFplo(" + evuId + ")"; %>
							<html:text name="baumassnahmeForm" property="<%=property7Name %>" styleId="gevuFplo__${evuId}" styleClass="dateasdfShort" maxlength="10"/>
							<% String property7_1_name = "gevuIsFploErforderlich(" + evuId + ")"; %>
							<br /><html:checkbox name="baumassnahmeForm" property="<%=property7_1_name %>" styleId="chk_gevuFplo__${evuId}" styleClass="checkbox" titleKey="baumassnahme.termine.required.short" />
							<bean:message key="baumassnahme.termine.required.short" />
						</td>
						<td>
							<% String property8Name = "gevuEingabeGFD_Z(" + evuId + ")"; %>
							<html:text name="baumassnahmeForm" property="<%=property8Name %>" styleId="gevuEingabeGFD_Z__${evuId}" styleClass="datasdfeShort" maxlength="10"/>
							<% String property8_1_name = "gevuIsEingabeGFD_ZErforderlich(" + evuId + ")"; %>
							<br /><html:checkbox name="baumassnahmeForm" property="<%=property8_1_name %>" styleId="chk_gevuEingabeGFD_Z__${evuId}" styleClass="checkbox" titleKey="baumassnahme.termine.required.short" />
							<bean:message key="baumassnahme.termine.required.short" />
						</td>
					<%--</c:if>--%>
					<td>
						<bean:define id="confirmText" toScope="page"><bean:message key="confirm.evu.delete" /></bean:define>
						<a href="#" onclick="if(confirmLink(this.href, '${confirmText}')) removeEvuRow('tbody_gevu', '${evuId}');return false;" class="delete">&nbsp;</a>
					 </td> 
				</tr>
			</logic:iterate> 
		</tbody>
	</table>
	<div id="gevuError" style="color:red;font-weight:bold;width:100%;display:none;">
		<bean:message key="error.evu.doubleinsert" />
	</div>
</div>