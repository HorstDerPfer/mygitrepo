<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
<html:xhtml/>

<html>
<head>
    <title><bean:message bundle="configResources" key="application.title"/> - <bean:message key="osb.sqlQuery.execute"/></title>
    
    <meta http-equiv="Content-Type"       content="text/html; charset=utf-8" />
    <meta http-equiv="Content-Style-Type" content="text/css" />
    <meta http-equiv="pragma"             content="no-cache" />
    <meta http-equiv="Cache-Control"      content="no-cache" />
	
	<link href="<c:url value='/static/css/common.css'/>"     type="text/css" rel="stylesheet" />
    <link href="<c:url value='/static/css/main.css'/>"       type="text/css" rel="stylesheet" />
    <link href="<c:url value='/static/css/links.css'/>"      type="text/css" rel="stylesheet" />
    <link href="<c:url value='/static/css/table.css'/>"      type="text/css" rel="stylesheet" />
    <link href="<c:url value='/static/css/navigation.css'/>" type="text/css" rel="stylesheet" />
    <link href="<c:url value='/static/css/tab.css'/>"        type="text/css" rel="stylesheet" />
    <link href="<c:url value='/static/css/baumassnahme.css'/>"       type="text/css" rel="stylesheet" />

	<!-- Other external libs -->
    <script type="text/javascript" src="<c:url value='/static/js/prototype.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/static/js/scriptaculous/scriptaculous.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/static/js/prototip.js'/>"></script>
    <link href="<c:url value='/static/css/prototip/prototip.css'/>" type="text/css" rel="stylesheet" />
    
	<style type="text/css">
		body {
		    margin: 10px;
		    font-family: Arial, Helvetica, sans-serif;
		    background-image: url();
		    background-color: #ffffff;
		}
		
		div.textcontent_head{
			position:relative;
		    width: 729px;
		    background-color:#666666;
		    color:white;
		    font-weight:bold;
		    padding:5px 12px 5px 10px;
			clear:both;
		}
		
		div.textcontent{
			position:relative;
		    width: 729px;
		    background-color: #cccccc;
		    border:1px solid #666666;
		    border-top:0px;
		    padding: 10px 10px 10px 10px;
		    clear:both;
		    /* padding: 12px 12px 20px 12px; */    
		}
	</style>
	
</head>

<jsp:useBean id="dateToday" class="java.util.Date"/>
<bean:define id="time"><bean:write format="yyyyMMddhhmm" name="dateToday"/></bean:define>

<body>
	<html:errors/>
	<logic:notPresent name="org.apache.struts.action.ERROR">
	
<%--
<bean:define id="cols" name="results" property="dynaProperties"/>
<table border="2">
	<tr>
		<logic:iterate id="col" name="cols">
			<th><bean:write name="col" property="name"/></th>
		</logic:iterate>
	</tr>
	<logic:iterate id="row" name="results" property="rows">
		<tr>
			<logic:iterate id="col" name="cols">
				<td>
					<bean:write name="row" property="<%=((org.apache.commons.beanutils.DynaProperty)col).getName( )%>"/>
				</td>
			</logic:iterate>
		</tr>
	</logic:iterate>
</table>
--%>
		<bean:message key="osb.sqlQuery.execute.title" />: <b><i>${sqlQuery.query}</i></b><br/><br/>
		<display:table	name="results.rows" 
		               	export="true" 
		               	requestURI="executeSqlQuery.do"
		               	sort="list"
		               	class="colored">
			<display:setProperty name="export.decorated" value="true" />
			<display:setProperty name="export.csv" value="true" />
			<display:setProperty name="export.csv.filename" value="${time}_${sqlQuery.name}_${loginUser.loginName}.csv" />
			<display:setProperty name="export.excel.filename" value="${time}_${sqlQuery.name}_${loginUser.loginName}.xls" />
		</display:table>
	</logic:notPresent>		    
</body>
</html>