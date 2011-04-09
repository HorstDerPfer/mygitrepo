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
<html:xhtml/>

<jsp:include page="../../main_head.jsp"/>
	<jsp:include page="../../main_path.jsp"/>
		<jsp:include page="../../main_menu.jsp"/>

			<%-- Öffnet Punkt in Startmenü --%>
			<script type="text/javascript">
			    openMainMenu('navLink_admin-users');
			</script>
			
			<div class="textcontent_head">
			    <bean:message key="menu.admin.user" />
			</div>
			
			<div class="textcontent">
				<div class="textcontent_left">
					<div class="box">
						<div class="label"><bean:message key="user.firstName" />*:</div>
						<div class="show"><bean:write name="user" property="firstName" /></div>
					</div>
					<div class="box">
						<div class="label"><bean:message key="user.name" />*:</div>
						<div class="show"><bean:write name="user" property="name" /></div>
					</div>
					<div class="box">
						<div class="label"><bean:message key="user.email" />*:</div>
						<div class="show"><bean:write name="user" property="email" /></div>
					</div>
					<div class="box">
						<div class="label"><bean:message key="baumassnahme.regionalbereichfpl" />*:</div>
						<div class="show">
							<logic:notEmpty name="user" property="regionalbereich">
								<bean:write name="user" property="regionalbereich.name" />
							</logic:notEmpty>
						</div>
					</div>
					<div class="box">
						<div class="label"><bean:message key="baumassnahme.bearbeitungsbereich" />*:</div>
						<div class="show">
							<logic:notEmpty name="user" property="bearbeitungsbereich">
								<bean:write name="user" property="bearbeitungsbereich.name" />
							</logic:notEmpty>
						</div>
					</div>

					<hr/>

					<%-- Security-User bearbeiten --%>
					<logic:present name="secUser">
						<div class="box">
							<div class="label"><bean:message key="user.loginName" />:</div>
							<div class="show"><bean:write name="user" property="loginName" /></div>
						</div>
					</logic:present>
				</div>
				<div class="textcontent_right">
					<logic:present name="secUser">
						<div class="box">
							<div class="label"><bean:message key="user.role" />:</div>
							<div class="input">
								<ul>
									<logic:iterate id="currentRole" name="secUser" property="roles">
										<li><bean:write name="currentRole" property="name" /></li>
									</logic:iterate>
								</ul>
							</div>
						</div>
					</logic:present>
				</div>
			</div>
			
			<div class="buttonBar">
				<c:choose>
					<c:when test="${ (param.history != null) && (param.history == 'edit') }">
						<easy:hasAuthorization authorization="ROLE_BENUTZER_BEARBEITEN" model="${user}">
							<html:link action="/admin/editUser" paramId="userId" paramName="user" paramProperty="id" styleClass="buttonBack"><bean:message key="button.back" /></html:link>
						</easy:hasAuthorization>
						<easy:hasNotAuthorization authorization="ROLE_BENUTZER_BEARBEITEN" model="${user}">
							<html:link action="/admin/listUsers" styleClass="buttonBack"><bean:message key="button.back" /></html:link>
						</easy:hasNotAuthorization>
					</c:when>
					<c:otherwise>
						<html:link action="/admin/listUsers" styleClass="buttonBack"><bean:message key="button.back" /></html:link>
					</c:otherwise>
				</c:choose>

				<a href="https://evi.bahn-net.db.de/evi2/sucheForm.do?actionID=search&tab=Mitarbeiter&attribute=Email&filter=${user.eviMail}" class="buttonSearch" target="_blank" title="<bean:message key='user.evi.title' />"><bean:message key="user.evi" /></a>
				<easy:hasAuthorization authorization="ROLE_BENUTZER_BEARBEITEN" model="${user}">
					<html:link action="/admin/editUser" paramId="userId" paramName="user" paramProperty="id" styleClass="buttonEdit"><bean:message key="button.edit" /></html:link>
				</easy:hasAuthorization>
			</div>
		
<jsp:include page="../../main_footer.jsp"/>