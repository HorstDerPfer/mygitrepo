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
	    <title><bean:message bundle="configResources" key="application.title"/></title>
    
	    <meta http-equiv="Content-Type"       content="text/html; charset=utf-8" />
	    <meta http-equiv="Content-Style-Type" content="text/css" />
	    <meta http-equiv="pragma"             content="no-cache" />
	    <meta http-equiv="Cache-Control"      content="no-cache" />
		
	    <link href="<c:url value='/static/css/main.css'/>"       type="text/css" rel="stylesheet" />
		<link href="<c:url value='/static/css/common.css'/>"     type="text/css" rel="stylesheet" />
	    <link href="<c:url value='/static/css/links.css'/>"      type="text/css" rel="stylesheet" />
	    <link href="<c:url value='/static/css/table.css'/>"      type="text/css" rel="stylesheet" />
	    <%--<link href="<c:url value='/static/css/navigation.css'/>" type="text/css" rel="stylesheet" />--%>
	    <link href="<c:url value='/static/css/tab.css'/>"        type="text/css" rel="stylesheet" />
	    <link href="<c:url value='/static/css/ajax.css'/>"       type="text/css" rel="stylesheet" />
	    <script src="<c:url value='/static/js/navigation.js'/>"    type="text/javascript"></script>
	    <script src="<c:url value='/static/js/tab.js'/>"           type="text/javascript"></script>
	    <script src="<c:url value='/static/js/changes.js'/>"       type="text/javascript"></script>
	    <script src="<c:url value='/static/js/utils.js'/>"         type="text/javascript"></script>
	    <script src="<c:url value='/static/js/ajax.js'/>"          type="text/javascript"></script>
	    
	    <script src="<c:url value='/static/js/calendar.js'/>"          type="text/javascript"></script>
		<script src="<c:url value='/static/js/lang/calendar-en.js'/>"  type="text/javascript"></script>
		<script src="<c:url value='/static/js/lang/calendar-de.js'/>"  type="text/javascript"></script>
		<script src="<c:url value='/static/js/calendar-setup.js'/>"    type="text/javascript"></script>
		<script src="<c:url value='/static/js/calendar-helper.js'/>"   type="text/javascript"></script>
	    <link href="<c:url value='/static/css/calendar-dbag.css'/>" type="text/css" rel="stylesheet" />
	
		<!-- Other external libs -->
	    <script type="text/javascript" src="<c:url value='/static/js/prototype.js'/>"></script>
	    <script type="text/javascript" src="<c:url value='/static/js/scriptaculous/scriptaculous.js'/>"></script>
	    <script type="text/javascript" src="<c:url value='/static/js/prototip.js'/>"></script>
	    <link href="<c:url value='/static/css/prototip/prototip.css'/>" type="text/css" rel="stylesheet" />
	    
	    <link href="<c:url value='/static/css/bob.css'/>" type="text/css" rel="stylesheet" />
	    
	    <style type="text/css">
	    body {
	    	background-image:none;
	    }
	    
	    div.buttonBar {
	    	width:inherit;
	    }
	    
	    div.textcontent {
	    	width:;
	    	padding:0px;
	    }
	    
	    div.textcontent_head {
	    	width:inherit;
	    }
	    
	    table.colored {
	    	background-color: #cccccc;
	    }
	    
	    table.colored td.nodata {
	    	background-color: #F7EFDE;
	    	color: black;
	    	font-style: italic;
	    }
	    </style>
	</head>
	
	<body>
		<%--
		<div class="textcontent_head"> 
			B&uuml;ndelplanung auf Streckenband 
		</div>
	--%>
		<html:errors/>
	    <html:messages id="message" message="true" header="success.messages.header" footer="success.messages.footer">
	        <li><bean:write name="message"/></li>
	    </html:messages>
		
		<%--<div class="buttonBar">
			
		</div>--%>
	</body>
</html>