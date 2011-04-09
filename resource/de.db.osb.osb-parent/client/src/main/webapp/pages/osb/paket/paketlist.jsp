<%@page import="db.training.osb.model.SAPMassnahme"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>


<%-- Öffnet Punkt in Startmenü --%>
		<script type="text/javascript">
				    openMainMenu('navLink_osb_workflow-pakete');
		</script>

<div class="textcontent_head">
	<bean:message key="menu.paket.list" />
</div>

<div class="textcontent" style="text-align: center;">

	<jsp:useBean id="urls" class="java.util.HashMap" />
	<display:table id="currentPaket" name="pakete"
		export="true" requestURI="${requestURI}" pagesize="${pakete.objectsPerPage}" sort="external"
		class="colored"
		decorator="db.training.easy.util.displaytag.decorators.AddRowLink">

		<%-- URL wird erzeugt, ggf. inkl. sessionId --%>
		<c:set target="${urls}" property="${currentPaket.id}" value="#" />

		<easy:hasAuthorization model="${currentPaket}" authorization="ROLE_PAKET_LESEN">
			<c:set target="${urls}" property="${currentPaket.id}">
				<c:url value="/paketView.do?id=${currentPaket.id}" />
			</c:set>
		</easy:hasAuthorization>
		
		<display:column property="paketId" titleKey="paket.rb_jahr_lfdnr" sortable="false" />
		
		<display:column property="kurzname" titleKey="paket.name" sortable="true" />
		
		<display:column property="esp" titleKey="paket.esp" sortable="true" />
		
		<display:column property="tsp" titleKey="paket.tsp" sortable="true" />
		
		<display:column property="kommentar" titleKey="paket.kommentar" sortable="true" />	
		
		<display:column property="massnahmenCount" titleKey="paket.massnahmenCount" sortable="false" class="text-align:right" />
		
		<display:column style="text-align:right;width:15px;" media="html">
			<easy:hasAuthorization model="${currentPaket}" authorization="ROLE_PAKET_LESEN">
		 		<html:link action="/paketView.do?id=${currentPaket.id}" styleClass="show">&nbsp;</html:link>
		 	</easy:hasAuthorization>
		</display:column>
	</display:table>
</div>