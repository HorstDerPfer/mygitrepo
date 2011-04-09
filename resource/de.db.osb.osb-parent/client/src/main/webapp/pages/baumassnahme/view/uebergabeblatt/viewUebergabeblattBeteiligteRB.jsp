<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

<br>
<table class="colored" style="width:100%;">
	<thead>
		<th><bean:message key="ueb.regionalbereich" /></th>
		<th><bean:message key="ueb.beteiligt" /></th>
		<th><bean:message key="ueb.fplonr" /></th>
		<th><bean:message key="ueb.rb.bearbeitungsstatus.short" /></th>
	</thead>
	<tbody>
		<logic:iterate id="currentMassnahme" name="baumassnahme" property="uebergabeblatt.massnahmen" indexId="index">
			<c:if test='${index == 0}'>
				<logic:iterate id="currentNL" name="currentMassnahme" property="fplonr.niederlassungen" indexId="ind">
					<bean:define toScope="page" id="styleClass" value="${styleClass=='evenrow'?'oddrow':'evenrow'}"/>
					<tr class="${styleClass}">
						<td>${currentNL.regionalbereich.name}</td>
						<td>
							<bean:message key="ueb.beteiligteRB.${currentNL.beteiligt}" />
						</td>
						<td>${currentNL.fplonr}</td>
						<td>
							<c:if test='${currentNL.beteiligt}'>
								<c:if test='${currentNL.bearbeitungsStatus != null}'>
									${currentNL.bearbeitungsStatus} %
								</c:if>
								<c:if test='${currentNL.bearbeitungsStatus == null}'>
									0 %											
								</c:if>
							</c:if>
						</td>
					</tr>
				</logic:iterate>
			</c:if>
		</logic:iterate>
	</tbody>
</table>
