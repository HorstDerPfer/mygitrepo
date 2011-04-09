<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html:xhtml/>

<br>
<table class="colored">
	<tr>
		<td style="width:230px;">
			<bean:define id="urlBearbeiter" toScope="page"><c:url value="addBearbeiter.do" /></bean:define>
			<html:select name="baumassnahmeForm" property="insertBearbeiter" styleId="insertBearbeiterSelect"  styleClass="long" errorStyle="${errorStyle }">
				<html:optionsCollection name="possibleBearbeiterList" value="id" label="nameAndFirstname" />
			</html:select>
		</td>
		<td style="width:25px;">
			<html:link onclick="javascript:addBearbeiter('${urlBearbeiter}', ${baumassnahme.id }, $F('insertBearbeiterSelect'));" href="#" styleClass="plusBig" titleKey="button.create">&nbsp;</html:link>
		</td>
		<td style="color:red;font-weight:bold;">
			<logic:notEmpty name="errorKey">
				<bean:message key="${errorKey}" />
			</logic:notEmpty>
		</td>
	</tr>
</table>

