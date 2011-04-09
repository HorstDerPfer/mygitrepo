<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
		
<div id="divUebergabeblatt">
	<logic:empty name="baumassnahme" property="uebergabeblatt">
		<div class="textcontent">
			<p><bean:message key="info.ueb.none" /></p>
		</div>
	</logic:empty>
	
	<logic:notEmpty name="baumassnahme" property="uebergabeblatt">
		<bean:define id="urlUeb" toScope="page"><c:url value="viewBaumassnahme.do" /></bean:define>
		<div id="divUeb">
			<c:if test="${tab=='Uebergabeblatt'}">
				<div class="textcontent">
					<jsp:include page="viewUebergabeblattHeader.jsp"/>
					<jsp:include page="viewUebMassnahme.jsp"/>
					<jsp:include page="viewUebergabeblattZuege.jsp"/>
				</div>
				<c:if test="${baumassnahmeForm.showZuegeUeb == true}">
					<jsp:directive.include file="tooltip.jsp" />
				</c:if>
				<c:if test="${baumassnahmeForm.showZuegeUeb == false}">
					<div class="buttonBar" id="showZuegeButtonUEB" style="display:block;">
						<html:link href="#" styleClass="buttonNext" onclick="hideZuegeButtonUeb();"><bean:message key="zvf.showzuege" /></html:link>
					</div>
				</c:if>
			</c:if>
		</div>
	</logic:notEmpty>
</div>
<script type="text/javascript">
	function hideZuegeButtonUeb() {
		$("showZuegeButtonUEB").hide();
		refreshUebergabeblatt('${urlUeb}', ${baumassnahme.id }, true, 'Uebergabeblatt', true);	
	}
</script>