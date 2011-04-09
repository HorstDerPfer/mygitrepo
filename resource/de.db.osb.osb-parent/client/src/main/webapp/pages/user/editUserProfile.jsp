<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<html:xhtml/>

<jsp:include page="../main_head.jsp"/>
	<jsp:include page="../main_path.jsp"/>
		<jsp:include page="../main_menu.jsp"/>

			<%-- Öffnet Punkt in Startmenü --%>
			<script type="text/javascript">
			    openMainMenu('navLink_userprofile');
			</script>

			<div class="textcontent_head">
				<bean:message key="menu.profile" />
			</div>

			<html:form action="/saveUserProfile" focus="password">
				<div class="textcontent">
					<div class="textcontent_left">
						<div class="box">
							<div class="label"><bean:message key="user.loginName" /></div>
							<div class="show"><bean:write name="secUser" property="username" /></div>
						</div>
						<div class="box">
							<div class="label"><bean:message key="user.password.change" /></div>
							<div class="input"><html:checkbox property="changePassword" styleId="changePassword" styleClass="checkbox" value="true" onclick="changePasswordFields();"/></div>
						</div>
						<div class="box">
							<div class="label"><label for="oldPassword"><bean:message key="user.oldPassword" />*</label></div>
							<div class="input"><html:password property="oldPassword" styleId="oldPassword"/></div>
						</div>
						<div class="box">
							<div class="label"><label for="password"><bean:message key="user.newPassword" />*</label></div>
							<div class="input"><html:password property="password" styleId="password"/></div>
						</div>
						<div class="box">
							<div class="label"><label for="password2"><bean:message key="user.password2" />*</label></div>
							<div class="input"><html:password property="password2" styleId="password2"/></div>
						</div>
					</div>
				</div>
				<div class="buttonBar">
					<html:link href="javascript:document.getElementById('userProfileForm').reset();" styleClass="buttonCancel"><bean:message key="button.cancel" /></html:link>
					<html:link href="javascript:document.getElementById('userProfileForm').submit();" styleClass="buttonSave"><bean:message key="button.save" /></html:link>
				</div>
			</html:form>

			<script type="text/javascript">
				function changePasswordFields(){
				    disableField('oldPassword');
				    disableField('password');
				    disableField('password2');
				}
				if(document.getElementById('changePassword').checked == false)
					changePasswordFields();
			</script>

<jsp:include page="../main_footer.jsp"/>