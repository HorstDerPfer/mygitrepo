<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>
<html:xhtml />

<jsp:include page="../../main_head.jsp" />
<jsp:include page="../../main_path.jsp" />
<jsp:include page="../../main_menu.jsp" />
		
<%-- Öffnet Punkt in Startmenü --%>
<script type="text/javascript">
    openMainMenu('navDiv_osb_workflow-streckenband');
    
    function loadStreckenband() {
        var idCtrl = $j("#vzgStreckeId");
        var captionCtrl = $j("#vzgStreckeCaption");
        var id;
        
        if(idCtrl.val() == null || idCtrl.val() == "") {
            id = captionCtrl.val();
        } else {
            id = idCtrl.val();
        }
		var link = "<c:url value='/osb/streckenband/show.do?buendelId=&vzgStreckeId=' />" + id;
		window.location.href = link;
		//window.open(link,'_blank','status=no,dependent=yes,scrollbars=no,height=714,width=1016,resizable=yes');
		return false;
	}
</script>

<html:form action="/osb/streckenband/show?buendelId=">
	<div class="textcontent_head">Bündelplanung auf Streckenband</div>
	<div class="textcontent">
		<div class="box">
			<div class="label"><bean:message key="baumassnahme.streckevzg" /></div>
			<div class="input">
				<html:hidden property="vzgStreckeId" styleId="vzgStreckeId" />
				<input type="text" id="vzgStreckeCaption" />
				<img src="<c:url value='/static/img/indicator.gif' />" id="streckeVzgIndicator" style="display:none;" />
				<div id="streckeVzgSelect" class="autocomplete"></div>
			</div>
		</div>
	</div>
	<div class="buttonBar">
		<authz:authorize ifAnyGranted="ROLE_STRECKENBAND_LESEN_REGIONALBEREICH, ROLE_STRECKENBAND_LESEN_ALLE">
			<a href="#" onclick="loadStreckenband();" class="buttonSearch">Streckenband anzeigen</a>
		</authz:authorize>
	</div>
</html:form>

<script type="text/javascript">
	new Ajax.Autocompleter(
			"vzgStreckeCaption", 
			"streckeVzgSelect", 
			"<c:url value='/AutoCompleteStreckeVzg.view'/>", 
			{
				indicator: 'streckeVzgIndicator', 
				minChars: 1, 
				paramName: 'nummer', 
				afterUpdateElement: function(text, li) {
					$j('#vzgStreckeId').val(li.id);
				}
			}
	);
</script>

<jsp:include page="../../main_footer.jsp" />