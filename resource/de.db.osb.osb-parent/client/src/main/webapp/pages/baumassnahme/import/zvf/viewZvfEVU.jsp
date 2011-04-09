<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>


	<div class="box">
		<logic:notEmpty name="newEVUList">
			<display:table
				id="currentEVU" 
				name="newEVUList" 
				export="false"
				requestURI="${requestURI}"
				sort="external"
				class="colored">
			
				<display:column titleKey="baumassnahme.termine.pevutitel.short" sortable="false">
					<input type="checkbox" name="checkPEVU" value="${currentEVU.id}" class="checkbox">
				</display:column>
				<display:column titleKey="baumassnahme.termine.gevutitel.short" sortable="false">
					<input type="checkbox" name="checkGEVU" value="${currentEVU.id}" class="checkbox">
				</display:column>
				<display:column titleKey="baumassnahme.termine.evu" sortable="false" style="width: 80%;">
					${currentEVU.name}
				</display:column>
			
			</display:table>
		</logic:notEmpty>
	</div>
