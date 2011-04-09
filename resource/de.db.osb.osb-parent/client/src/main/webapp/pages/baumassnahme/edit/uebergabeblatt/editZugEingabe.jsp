<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>


					<bean:define id="urlZug" toScope="page"><c:url value="refreshZug.do" /></bean:define>
					<table class="colored" style="border-top:0px;">
						<colgroup>
							<col width="*" />
						</colgroup>
						<tbody>
							<tr style="border-bottom:0px;">
								<td style="text-align:left;">
									<jsp:useBean id="addZugParamMap" class="java.util.HashMap">
										<c:set target="${addZugParamMap}" property="id" value="${baumassnahme.id}" />
										<c:set target="${addZugParamMap}" property="type" value="UEB" />
									</jsp:useBean>
									<html:link action="/addZug" name="addZugParamMap" styleClass="plusBig" titleKey="button.create">
									&nbsp;
										<html:param name="showZuegeUeb" value="true" />
									</html:link>
									<%--<html:link href="javascript:addZug('${urlZug}');" styleClass="plusBig" titleKey="button.create">&nbsp;</html:link>--%>
								</td>
							</tr>
						</tbody>
					</table>
