<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

<div id="divJbb">
	<c:if test="${tab=='JBB'}">
		<div class="textcontent" id="tabJBBStammdaten">
			<jsp:include page="viewJbbStammdaten.jsp"/>
		</div>
		<br/>
	    <div class="textcontent_head"><bean:message key="baumassnahme.schnittstellen" /></div>
	    <div class="textcontent">
	    	<fieldset>
			    <legend><bean:message key="baumassnahme.termine.bbptitel" /></legend>
				<jsp:include page="viewSchnittstelleBBP.jsp"/>
			</fieldset>
			<br>
		    <fieldset>
			    <legend><bean:message key="baumassnahme.termine.pevutitel" /></legend>
				<jsp:include page="viewSchnittstellePEVU.jsp"/>
			</fieldset>
			<br>
		    <fieldset>
			    <legend><bean:message key="baumassnahme.termine.gevutitel" /></legend>
				<jsp:include page="viewSchnittstelleGEVU.jsp"/>
			</fieldset>
		</div>
	</c:if>
</div>

			
