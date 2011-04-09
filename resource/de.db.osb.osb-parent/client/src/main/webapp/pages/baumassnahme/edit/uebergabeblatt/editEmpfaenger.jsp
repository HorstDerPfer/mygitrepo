<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

				<div id="divEmpfaenger">
					<div id="divEmpfaengerTabelle">
						<bean:define id="urlEmpfaenger" toScope="page"><c:url value="refreshEmpfaenger.do" /></bean:define>
						<display:table
							id="currentEmpfaenger" 
							name="baumassnahme.uebergabeblatt.header.empfaenger" 
							export="false"
							class="colored"
							style="margin-top:5px;" 
							sort="page"
							defaultsort="1">

							<display:column titleKey="ueb.empfaenger" sortable="false" style="">${currentEmpfaenger}</display:column>
							<display:column style="text-align:right;width:30px" sortable="false">
								<bean:define id="confirmText" toScope="page"><bean:message key="confirm.empfaenger.delete" /></bean:define>
								<html:link href="javascript:if(confirmLink(this.href, '${confirmText}')) removeEmpfaenger('${urlEmpfaenger}','${currentEmpfaenger}', 'ueb');" styleClass="delete" titleKey="button.delete">&nbsp;</html:link>
							</display:column>
						</display:table>
					</div>
				</div>