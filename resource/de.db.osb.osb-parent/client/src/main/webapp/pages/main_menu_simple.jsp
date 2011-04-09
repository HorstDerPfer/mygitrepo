<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html:xhtml />

<div id="master_limited">
		<div id="master_navigation">
			<div class="navTitle"><bean:message bundle="configResources" key="application.title" /></div>
		<html:link action="login">
			<img src="<c:url value='/static/img/text_login.gif'/>" style="margin: 60px;" alt="Login" />
		</html:link>
	</div>
	<div id="master_content">
		<logic:present name="loginErrors">
			<c:if test="${ (fn:length(loginErrors) > 0) }">
				<div class="error">
					<ul>
						<logic:iterate id="currentError" name="loginErrors">
							<li><bean:message key="${currentError}" /></li>
						</logic:iterate>
					</ul>
				</div>
			</c:if>
		</logic:present>

		<html:errors />
		<html:messages id="message" message="true" header="success.messages.header" footer="success.messages.footer">
			<li><bean:write name="message" /></li>
		</html:messages>

		<c:choose>
			<c:when test="${not empty param.credentialsExpired}">
				<div class="error">
					<ul><bean:message key="error.login.expired" /></ul>
				</div>
			</c:when>
			<c:otherwise>
				<c:if test="${not empty param.loginfailure}">
					<div class="error">
						<ul><bean:message key="error.login.failed" /></ul>
					</div>
				</c:if>
			</c:otherwise>
		</c:choose>
