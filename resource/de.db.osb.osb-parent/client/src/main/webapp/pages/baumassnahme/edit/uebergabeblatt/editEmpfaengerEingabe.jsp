<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

			<bean:define id="urlEmpfaenger" toScope="page"><c:url value="refreshEmpfaenger.do" /></bean:define>
			<table class="colored" style="border-top:0px;">
				<colgroup>
					<col width="*" />
					<col style="width:50px;" />
				</colgroup>
				<tbody>
					<tr style="border-bottom:0px;">
					 	<td style="text-align:left;">
							<input type="text" name="empfaengerName" id="empfaengerName" maxlength="255" />
						</td>
						<td style="text-align:right;">
							<html:link href="javascript:addEmpfaenger('${urlEmpfaenger}', 'ueb');" styleClass="plusBig" titleKey="button.create">&nbsp;</html:link>
						</td>
					</tr>
				</tbody>
			</table>