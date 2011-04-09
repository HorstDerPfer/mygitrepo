<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="de-DE">

<head>
    <title><bean:message bundle="configResources" key="application.title"/></title>
    
    <meta http-equiv="Content-Type"       content="text/html; charset=UTF-8" />
    <meta http-equiv="Content-Style-Type" content="text/css" />
    <meta http-equiv="pragma"             content="no-cache" />
    <meta http-equiv="Cache-Control"      content="no-cache" />
	
    <link href="<c:url value='/static/css/main.css'/>"       type="text/css" rel="stylesheet" />
	<link href="<c:url value='/static/css/common.css'/>"     type="text/css" rel="stylesheet" />
    <link href="<c:url value='/static/css/links.css'/>"      type="text/css" rel="stylesheet" />
    <link href="<c:url value='/static/css/table.css'/>"      type="text/css" rel="stylesheet" />
    <link href="<c:url value='/static/css/navigation.css'/>" type="text/css" rel="stylesheet" />
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
    <script type="text/javascript" src="<c:url value='/static/js/modalbox.js'/>"></script>
    
    <link href="<c:url value='/static/css/prototip/prototip.css'/>" type="text/css" rel="stylesheet" />
    <link href="<c:url value='/static/css/modalbox.css'/>" type="text/css" rel="stylesheet" media="screen" />
    <link href="<c:url value='/static/css/bob.css'/>" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="<c:url value='/static/js/baumassnahme.js'/>"></script>

	<!--  Query Styles und Libs -->
	<link href="<c:url value='/static/css/jquery/jquery-ui-1.8.1.bahn.css'/>" type="text/css" rel="stylesheet" />
	<!-- 
	<link href="<c:url value='/static/css/jquery/jquery.ui.all.css'/>" type="text/css" rel="stylesheet" />
	<link href="<c:url value='/static/css/jquery/jquery.ui.autocomplete.css'/>" type="text/css" rel="stylesheet" />
	<link href="<c:url value='/static/css/jquery/jquery.ui.base.css'/>" type="text/css" rel="stylesheet" />
	<link href="<c:url value='/static/css/jquery/jquery.ui.core.css'/>" type="text/css" rel="stylesheet" />
	<link href="<c:url value='/static/css/jquery/jquery.ui.datepicker.css'/>" type="text/css" rel="stylesheet" />
	<link href="<c:url value='/static/css/jquery/jquery.ui.dialog.css'/>" type="text/css" rel="stylesheet" />
	<link href="<c:url value='/static/css/jquery/jquery.ui.tabs.css'/>" type="text/css" rel="stylesheet" />
	<link href="<c:url value='/static/css/jquery/jquery.ui.theme.css'/>" type="text/css" rel="stylesheet" />
	<link href="<c:url value='/static/css/jquery/jquery-ui-1.8.custom.css'/>" type="text/css" rel="stylesheet" />
	-->
	 
	<!--<script type="text/javascript" src="<c:url value='/static/js/jquery/jquery-1.4.1.min.js'/>"></script>-->
	<script type="text/javascript" src="<c:url value='/static/js/jquery/jquery-1.4.2.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/static/js/jquery/jquery-ui-1.8.custom.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/static/js/jquery/jquery.livequery.js'/>"></script>
	<script type="text/javascript">
		var $j = jQuery.noConflict();
	</script>
	<script type="text/javascript" src="<c:url value='/static/js/jquery/texotela/jquery.selectboxes.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/static/js/ajaxprogress.js'/>"></script>
	
	<script type="text/javascript">
		function setClientProperties(){
			new Ajax.Request("<c:url value='/RefreshClientPropertiesServlet.view'/>", {
				method: "get",
				parameters : {
					clientWidth : document.documentElement.clientWidth,
					clientHeight : document.documentElement.clientHeight
				}
			});
		}

		function setDisplaytagExportTargets() {
			$j("div.export_banner a").each(function(index, element) {$j(element).attr("target", "_blank");});
		}

		function activateCheckboxen() {
			if ($("chkAlleAuswaehlen").checked == true){
				$j("tbody.zvfImportSelectZug input[type=checkbox]").each(function(index, element) {$j(element).attr("checked", "checked");});
			}else{
				$j("tbody.zvfImportSelectZug input[type=checkbox]").each(function(index, element) {$j(element).attr("checked", "");});
			}
		}
		
		$j(window).ready(setClientProperties);
		$j(window).ready(setDisplaytagExportTargets);
		$j(window).bind('beforeunload', changeChecker);
	</script>
</head>

<body>

<div id="saveDialog" title="<bean:message key="common.loading" />" style="display:none;">
	<p class="center">
		<bean:message key="common.wait" /><br /><br />
		<img src="<c:url value='/static/img/ajax-loader.gif'/>"  />
	</p>
</div>
<script type="text/javascript">
<%-- Laden-Dialog solange anzeigen, bis die Seite fertig geladen wurde --%>
showSaveDialog();
$j(window).ready(function() {
	hideSaveDialog = true;
	showSaveDialog();
});
</script>

<div id="master"><!-- wird in main_footer.jsp geschlossen -->
	
    <!-- Dokumentenkopf -->
    <div id="seitenkopf">
	    <div id="seitenkopf_links">
	    	<a href="http://www.bahn-net.db.de/" target="_blank">
	    		<img src="<c:url value='/static/img/netze_logo.gif'/>" title="DB-Net Startseite in neuem Fenster &ouml;ffnen" alt="DB-net Startseite in neuem Fenster &ouml;ffnen" style="margin-top:8px"/>
	    	</a>
	    </div>
	    <div id="seitenkopf_bogen"></div>
	    <div id="seitenkopf_rechts"><p><bean:message bundle="configResources" key="application.title"/></p></div>
	</div>
	
	<bean:define id="errorStyle" value="background-color:#CC9999;" toScope="session" />
	