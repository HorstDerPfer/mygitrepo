<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="de-DE">

<head>
    <title><bean:message key="common.importMessage"/></title>
    
    <meta http-equiv="Content-Type"       content="text/html; charset=utf-8" />
    <meta http-equiv="Content-Style-Type" content="text/css" />
    <meta http-equiv="pragma"             content="no-cache" />
    <meta http-equiv="Cache-Control"      content="no-cache" />
	
    <link href="<c:url value='/static/css/table.css'/>"      type="text/css" rel="stylesheet" />
    <link href="<c:url value='/static/css/navigation.css'/>" type="text/css" rel="stylesheet" />
    <link href="<c:url value='/static/css/tab.css'/>"        type="text/css" rel="stylesheet" />
    <link href="<c:url value='/static/css/ajax.css'/>"       type="text/css" rel="stylesheet" />
    <link href="<c:url value='/static/css/baumassnahme.css'/>"       type="text/css" rel="stylesheet" />
</head>

<body>

	<div class="textcontent">
		<logic:notEmpty name="importMessages">
			<table class="colored">
				<tr>
					<th></th>
					<th><bean:message key="common.importMessage.level" /></th>
					<th><bean:message key="common.importMessage.reason" /></th>
				</tr>
				<bean:define toScope="page" id="rowNumber" value="0" />
				<logic:iterate id="currentMessage" name="importMessages">
					<tr class="${ (rowNumber % 2) == 0 ? "evenrow" : "oddrow" }">
						<td>
							<logic:equal name="currentMessage" property="level" value="WARNING">
								<html:img src="static/img/warning.png" alt="warning" />
							</logic:equal>
							<logic:equal name="currentMessage" property="level" value="ERROR">
								<html:img src="static/img/error.png" alt="error" />
							</logic:equal>
						</td>
						<td>
							<logic:equal name="currentMessage" property="level" value="WARNING">
								<bean:message key="common.importMessage.warning" />
							</logic:equal>
							<logic:equal name="currentMessage" property="level" value="ERROR">
								<bean:message key="common.importMessage.error" />
							</logic:equal>
						</td>
						<td><bean:write name="currentMessage" property="reason" /></td>
					</tr>
					<bean:define toScope="page" id="rowNumber" value="${rowNumber+1}"/>
				</logic:iterate>
			</table>
		</logic:notEmpty>
	</div>
</body>
</html>