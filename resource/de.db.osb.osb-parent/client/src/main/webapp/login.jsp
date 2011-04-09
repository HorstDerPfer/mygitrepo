<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html:xhtml />

<jsp:include page="/pages/main_head.jsp" />
<jsp:include page="/pages/main_path.jsp" />

<div id="master_limited">
	<div id="master_navigation">
		<div class="navTitle">
			<bean:message bundle="configResources" key="application.title" />
		</div>
		<html:link action="login">
			<img src="<c:url value='/static/img/text_login.gif'/>" style="margin: 60px;" alt="Login" />
		</html:link>
	</div>
	<div id="master_content">
		<c:choose>
			<c:when test="${not empty param.credentialsExpired}">
				<div class="error" style="width:413px;">
					<ul>
						<c:if test="${ ((loginErrors != null) && (fn:length(loginErrors) > 0)) }">
							<logic:iterate id="currentError" name="loginErrors">
								<li>
									<bean:message key="${currentError}" />
								</li>
							</logic:iterate>
						</c:if>
						<c:if test="${ ((loginErrors == null) || ( loginErrors != null && (fn:length(loginErrors) == 0))) }">
							<li>
								<bean:message key="error.login.expired" />
							</li>
						</c:if>
					</ul>
				</div>
			</c:when>
			<c:otherwise>
				<c:if test="${not empty param.loginfailure}">
					<div class="error" style="width:413px;">
						<ul>
							<c:if test="${ ((loginErrors != null) && (fn:length(loginErrors) > 0)) }">
								<logic:iterate id="currentError" name="loginErrors">
									<li>
										<bean:message key="${currentError}" />
									</li>
								</logic:iterate>
							</c:if>
							<c:if test="${ ((loginErrors == null) || ( loginErrors != null && (fn:length(loginErrors) == 0))) }">
								<li>
									<bean:message key="error.login.failed" />
								</li>
							</c:if>
						</ul>
					</div>
				</c:if>
			</c:otherwise>
		</c:choose>


		<form id="loginForm" action="<c:url value='/j_acegi_security_check'/>" method="post">
			<div class="textcontent_head" style="width:435px;">
				<bean:message key="login" />
			</div>
			<div class="textcontent" style="width:435px;">
				<table class="form">
					<tr>
						<td class="label"><label for="loginName"><bean:message key="user.loginName" /></label></td>
						<td>
							<c:if test="${empty ACEGI_SECURITY_LAST_USERNAME}"><input type='text' name='j_username' id="loginName" style="width: 323px;" /></c:if>
							<c:if test="${not empty ACEGI_SECURITY_LAST_USERNAME}"><input type='text' name='j_username' id="loginName" value="${ ACEGI_SECURITY_LAST_USERNAME }" style="width: 323px;" /></c:if>
						</td>
					</tr>
					<tr>
						<td class="label">
							<label for="password">
								<c:if test="${empty param.credentialsExpired}"><bean:message key="user.password" /></c:if>
								<c:if test="${not empty param.credentialsExpired}"><bean:message key="user.oldPassword" /></c:if>
							</label>
						</td>
						<td><input type='password' name='j_password' id="password" style="width: 323px;" /></td>
					</tr>
					<c:if test="${empty param.credentialsExpired}">
						<tr>
							<td class="label"><label for="fahrplanjahr"><bean:message key="common.fahrplanjahr" /></label></td>
							<td>
								<select name="fahrplanjahr" id="fahrplanjahr" style="width:55px">
									<option value="2011">2011</option>
									<option value="2012" selected="selected">2012</option>
									<option value="2013">2013</option>
								</select>
							</td>
						</tr>
					</c:if>
					<c:if test="${not empty param.credentialsExpired}">
						<tr>
							<td class="label"><label for="newpassword"><bean:message key="user.newPassword" /></label></td>
							<td><input type='password' name='newpassword' id="password" style="width: 323px;" /></td>
						</tr>
						<tr>
							<td class="label"><label for="confirmpassword"><bean:message key="user.confirmPassword" /></label></td>
							<td><input type='password' name='confirmpassword' id="password" style="width: 323px;" /></td>
						</tr>
					</c:if>
				</table>
				
				<c:if test="${empty param.credentialsExpired}">
					<br/>
					<html:link action="/passReminder" styleClass="list" style="margin: 15px 0px 0px 102px;">
						<bean:message key="password.reminder" />
					</html:link>
				</c:if>
			</div>
			<div class="buttonBar" style="width:435px;">
				<input type="submit" class="hiddenSubmit" />
				<html:link href="javascript:document.getElementById('loginForm').submit();" styleClass="buttonOk">
					<bean:message key="button.login" />
				</html:link>
			</div>
		</form>

		<script type="text/javascript">
			document.getElementById('loginName').focus();
		</script>
	
<%-- main_footer schließt master_contend-div usw. --%>
<jsp:include page="pages/main_footer.jsp" />