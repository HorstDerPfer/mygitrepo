<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

			<bean:define id="urlAenderung" toScope="page"><c:url value="refreshAenderung.do" /></bean:define>
			<table class="colored" style="border-top:0px;">
				<colgroup>
					<col width="*" />
					<col style="width:100px;" />
					<col style="width:50px;" />
					<col style="width:50px;" />
				</colgroup>
				<tbody>
					<tr style="border-bottom:0px;">
						<td>
							<div class="box">
								<bean:message key="baumassnahme.grund" />&#58;
								<select name="grund" size="1" id="grund">
									<option value="0"><bean:message key="common.select.option" /></option>
									<c:forEach var="grund" items="${gruende}" >
										<option value="${grund.id}">${grund.name}</option>
									</c:forEach>
								</select> 
							</div>
						</td>
						<td>
							<bean:message key="baumassnahme.aufwand"/>&#58;<br />
							<bean:message key="common.unit.time.hhmm" />
						</td>
						<td>
							<input type="text" name="aufwand" id="aufwand" style="vertical-align:middle;" class="time" maxlength="9" />
						</td>
					 	<td style="text-align:right;">
					 		<html:link  onclick="javascript:addAenderung('${urlAenderung}');" href="#" styleClass="plusBig" titleKey="button.create">&nbsp;</html:link>
						</td>
					</tr>
				</tbody>
			</table>