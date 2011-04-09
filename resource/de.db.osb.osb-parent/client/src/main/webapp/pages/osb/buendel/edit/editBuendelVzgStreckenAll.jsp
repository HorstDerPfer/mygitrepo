<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html:xhtml />

<div class="label bold left">
	<bean:message key="buendel.tab.gleissperrungen.legend.vzgStrecken" /><br/>
	(${fn:length(vzgStreckenAlle)})
</div>
<div class="scroll left">
	<c:choose>
		<c:when test="${fn:length(vzgStreckenAlle) > 0}">
			<logic:iterate id="current" name="vzgStreckenAlle">
				<html:link action="/osb/addGleissperrungToBuendel" styleClass="list" style="margin:0px;">
					<bean:write name="current" property="caption" />
					<html:param name="buendelId" value="${buendelForm.buendelId}" />
					<html:param name="vzgStreckeId" value="${current.id}" />
					<html:param name="showAllVzgStrecken" value="true" />
				</html:link>
				<br/>
			</logic:iterate>
		</c:when>
		<c:otherwise>
			<bean:message key="buendel.message.vzgStreckenEmpty" />
		</c:otherwise>
	</c:choose>
</div>
<div class="label left" style="width:200px;margin-left:20px;">
	<html:link href="#" onclick="showAnstossendeVzgStrecken();" styleClass="list" style="margin:0px;">
		<bean:message key="buendel.tab.gleissperrungen.legend.vzgStrecken.abzweigende" />
	</html:link>
</div>
