<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

			<div id="divBearbeitungsbereich"> 
				<bean:define id="urlBearbeitungsbereich" toScope="page"><c:url value="refreshBearbeitungsbereich.do" /></bean:define>
				<div class="box">
					<div class="label"><label for="bearbeitungsbereich"><bean:message key="baumassnahme.bearbeitungsbereich" /></label></div>
					<div class="input">
						<html:select name="StatisticsFilterForm" property="bearbeitungsbereich" styleId="bearbeitungsbereich">
							<html:option value=""><bean:message key="common.select.option" /></html:option>
							<logic:iterate id="currentBearbeitungsbereich" name="bearbeitungsbereiche" indexId="index">
								<html:option value="${currentBearbeitungsbereich.id}" >${currentBearbeitungsbereich.name}</html:option>
							</logic:iterate>
						</html:select>
					</div>
				</div>
			</div>