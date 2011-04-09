<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

			
			<div id="divMailEmpfaenger"> 
				<html:select name="baumassnahmeForm" property="uebMailEmpfaenger" styleId="uebEmpfaenger" multiple="multiple" size="5">
					<logic:iterate name="baumassnahmeForm" property="uebMailEmpfaengerList" id="currentEmp" indexId="index" >
						<html:option value="${currentEmp.id}" >${currentEmp.name}, ${currentEmp.firstName}</html:option>
					</logic:iterate>
				</html:select>
						
				<div style="color:red;font-weight:bold;width:100%;">
					<logic:notEmpty name="errorKey">
						<bean:message key="${errorKey}" />
					</logic:notEmpty>
				</div>
				<div style="color:white;font-weight:bold;width:100%;">
					<logic:notEmpty name="messageKey">
						<bean:message key="${messageKey}" />
					</logic:notEmpty>
				</div>
			</div>

			
			
			
			
			