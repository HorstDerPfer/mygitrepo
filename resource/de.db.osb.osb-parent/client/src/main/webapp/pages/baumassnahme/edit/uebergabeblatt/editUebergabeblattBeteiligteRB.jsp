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

					<div class="textcontent_left">
						<br>
						<logic:iterate id="currentMassnahme" name="baumassnahme" property="uebergabeblatt.massnahmen" indexId="index">
							<c:if test='${index == 0}'>
								<table class="colored">
									<thead>
										<tr>
											<th>
												<bean:message key="ueb.regionalbereich" />
											</th>
											<th>
												<bean:message key="ueb.beteiligt" />
											</th>
											<th>
												<bean:message key="ueb.fplonr" />
											</th>
										</tr>
									</thead>
									<tbody>
										<logic:iterate id="currentNL" name="currentMassnahme" property="fplonr.niederlassungen" indexId="ind">
											<bean:define toScope="page" id="styleClass" value="${styleClass=='evenrow'?'oddrow':'evenrow'}"/>
											<tr class="${styleClass}">
												<td>
													<div class="label">${currentNL.regionalbereich.name}</div>
												</td>
												
												<td>
													<div class="box">
														<% String property14Name = "uebRb" + ind; %>
														<html:checkbox name="baumassnahmeForm" property="<%=property14Name %>" styleClass="checkbox" />
													</div>
												</td>
												<td>
													<div class="box">
														<% String property15Name = "uebFplo" + ind; %>
														<html:text property="<%=property15Name %>" style="width:50px;" maxlength="5"/>
													</div>
												</td>
											</tr>
										</logic:iterate>
									</tbody>
								</table>
							</c:if>
						</logic:iterate>
					</div>