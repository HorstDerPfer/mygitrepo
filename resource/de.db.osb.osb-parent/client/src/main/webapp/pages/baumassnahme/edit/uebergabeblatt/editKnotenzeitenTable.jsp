<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
							
			<div id="divKnotenzeiten">
				<div id="divKnotenzeitenTabelle">
					<bean:define id="urlKnotenzeiten" toScope="page"><c:url value="refreshKnotenzeiten.do" /></bean:define>
					<table class="colored" style="text-align:center;">
						<thead>
							<tr>
								<th>
									<div style="text-align:left;"><bean:message key="ueb.zug.knotenzeiten.bahnhof" /></div>
								</th>
								<th>
									<div style="text-align:left;"><bean:message key="ueb.zug.knotenzeiten.haltart" /></div>
								</th>
								<th>
									<div style="text-align:left;"><bean:message key="ueb.zug.knotenzeiten.an" /></div>
								</th>
								<th>
									<div style="text-align:left;"><bean:message key="ueb.zug.knotenzeiten.ab" /></div>
								</th>
								<th>
									<div style="text-align:left;"><bean:message key="ueb.zug.knotenzeiten.relativlage" /></div>
								</th>
								<th>
								</th>
							</tr>
						</thead>
						<tbody>
							<%--<jsp:include page="editKnotenzeitenTable.jsp"/>--%>
							<logic:iterate id="currentKnoten" name="knotenzeiten" indexId="i">
							<bean:define toScope="page" id="styleClass" value="${styleClass=='evenrow'?'oddrow':'evenrow'}"/>
								<tr class="${styleClass}">
									<td>
										<% String property1Name = "bahnhof(" + i + ")"; %>
										<html:text name="knotenzeitenForm" property="<%=property1Name %>" styleClass="date" maxlength="5"/>
									</td>
									<td>
										<% String property2Name = "haltart(" + i + ")"; %>
										<html:text name="knotenzeitenForm" property="<%=property2Name %>" styleClass="date" maxlength="1"/>
									</td>
									<td>
										<% String property3Name = "an(" + i + ")"; %>
										<html:text name="knotenzeitenForm" property="<%=property3Name %>" styleClass="time" maxlength="5" />
									</td>
									<td>
										<% String property4Name = "ab(" + i + ")"; %>
										<html:text name="knotenzeitenForm" property="<%=property4Name %>" styleClass="time" maxlength="5" />
									</td>
									<td>
										<% String property5Name = "relativlage(" + i + ")"; %>
										<html:text name="knotenzeitenForm" property="<%=property5Name %>" styleClass="date" />
									</td>
									<td>
										<bean:define id="confirmText" toScope="page"><bean:message key="confirm.knotenzeit.delete" /></bean:define>
										<html:link href="javascript:if(confirmLink(this.href, '${confirmText}')) removeKnotenzeiten('${urlKnotenzeiten}','${currentKnoten.id}','${knotenzeitenForm.zugIds}');" styleClass="delete" titleKey="button.delete">&nbsp;</html:link>
									</td>		
								</tr>
							</logic:iterate>
						</tbody>
					</table>
				</div>
			</div>