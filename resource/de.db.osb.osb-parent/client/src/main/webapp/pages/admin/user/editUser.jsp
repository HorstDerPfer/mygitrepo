<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
<html:xhtml/>

<jsp:include page="../../main_head.jsp"/>
<jsp:include page="../../main_path.jsp"/>
<jsp:include page="../../main_menu.jsp"/>

<%-- Bindet Autovervollständigungs-Funktionen ein. --%>
<script src="<c:url value='/static/js/user.js'/>" type="text/javascript"></script>

<%-- Öffnet Punkt in Startmenü --%>
<script type="text/javascript">
    openMainMenu('navLink_admin-users');
</script>

<div class="textcontent_head">
    <bean:message key="menu.admin.user" />
</div>

<html:form action="/admin/saveUser" focus="firstName">
	<div class="textcontent">
		<html:hidden property="userId" styleId="userId" />
		
		<div class="textcontent_left">
			<div class="box">
				<div class="label"><label for="firstName"><bean:message key="user.firstName" />*</label></div>
				<div class="input">
					<c:if test="${ (userForm.userId == 0) }">
						<html:text property="firstName" styleId="firstName" onchange="$('firstName').value = toUpperFirst($('firstName').value); autoFillOut();" />
					</c:if>
					<c:if test="${ (userForm.userId > 0) }">
						<html:text property="firstName" styleId="firstName" />
					</c:if>
				</div>
			</div>
			<div class="box">
				<div class="label"><label for="middleInitial"><bean:message key="user.middleInitial" /></label></div>
				<div class="input">
					<input type="text" maxlength="3" name="middleInitial" style="width:30px" id="middleInitial" onchange="$('middleInitial').value = $('middleInitial').value.toUpperCase(); autoFillOut()"/>
					<bean:message key="user.middleInitial.info" />
				</div>
			</div>
			<div class="box">
				<div class="label"><label for="lastName"><bean:message key="user.name" />*</label></div>
				<div class="input">
					<c:if test="${ (userForm.userId == 0) }">
						<html:text property="name" styleId="lastName" onchange="$('lastName').value = toUpperFirst($('lastName').value); autoFillOut()" />
					</c:if>
					<c:if test="${ (userForm.userId > 0) }">
						<html:text property="name" styleId="lastName" />
					</c:if>
				</div>
			</div>
			<div class="box">
				<div class="label"><label for="email"><bean:message key="user.email" />*</label></div>
				<div class="input"><html:text property="email" styleId="email" /></div>
			</div>
			<bean:define id="urlBearbeitungsbereichUser" toScope="page"><c:url value="refreshBearbeitungsbereichUser.do" /></bean:define>
			<div class="box">
				<div class="label"><label for="regionalbereichId"><bean:message key="baumassnahme.regionalbereichfpl" />*</label></div>
				<div class="input">
					<html:select property="regionalbereichId" styleId="regionalbereichId" onchange="refreshBearbeitungsbereichUser('${urlBearbeitungsbereichUser}', this.value, 'USER');">
						<html:option value=""><bean:message key="common.select.option" /></html:option>
						<html:optionsCollection name="regionalbereiche" value="id" label="name"/>
					</html:select>
				</div>
			</div>
			<jsp:include page="editBearbeitungsbereichUser.jsp"></jsp:include>
			
			<hr/>
			
			<%-- Security-User anlegen --%>
			<logic:notPresent name="secUser">
				<div class="box">
					<div class="label" style="width:130px;"><bean:message key="user.login.create" /></div>
					<div class="input"><html:checkbox property="changeLogin" styleId="changeLogin" styleClass="checkbox" value="true" onclick="changeLoginFields();autoFillOut()"/></div>
				</div>
				<div class="box">
					<div class="label"><label for="loginName"><bean:message key="user.loginName" /></label></div>
					<div class="input"><html:text property="loginName" styleId="loginName" onfocus="$('name').value = toUpperFirst($('name').value);" /></div>
				</div>
			</logic:notPresent>
			
			<c:if test="${ (user != null) && (secUser != null)}">
				<c:if test="${ (loginUser.loginName != user.loginName) }">
					<div class="box">
						<div class="label" style="width:130px;"><bean:message key="user.login.change" /></div>
						<div class="input"><html:checkbox property="changeLogin" styleId="changeLogin" styleClass="checkbox" value="true" onclick="changeLoginFields();autoFillOut()"/></div>
					</div>
					<div class="box">
						<div class="label" style="width:130px;"><label for="loginName"><bean:message key="user.loginName" /></label></div>
						<div class="input"><html:text property="loginName" styleId="loginName" style="width:209px;" /></div>
					</div>
				</c:if>
				<c:if test="${ (loginUser.loginName == user.loginName) }">
					<div class="box">
						<div class="label"><bean:message key="user.loginName" /></div>
						<div class="show"><bean:write name="userForm" property="loginName" /></div>
					</div>
				</c:if>
				
				<hr/>

				<div class="box">
					<div class="label" style="width:130px;"><bean:message key="user.password.reset" /></div>
					<div class="input"><html:checkbox property="resetPassword" styleClass="checkbox" value="true" /></div>
				</div>
				<div class="box">
					<div class="label" style="width:130px;"><label for="generatePassword"><bean:message key="user.password.generate" /></label></div>
					<div class="input"><html:checkbox property="generatePassword" styleId="generatePassword" styleClass="checkbox" value="true" /></div>
				</div>
			</c:if>
		</div>
		
		<div class="textcontent_right">
			<%-- Security-User bearbeiten --%>
			<c:if test="${ (user != null) && (secUser != null)}">
				<div class="box">
					<div class="label" style="width:130px;"><label for="disabled"><bean:message key="user.disabled" /></label></div>
					<div class="input"><html:checkbox property="disabled" styleId="disabled" styleClass="checkbox" value="true" /></div>
				</div>
				<div class="box">
					<div class="label" style="width:130px;"><label for="locked"><bean:message key="user.locked" /></label></div>
					<div class="input"><html:checkbox property="locked" styleId="locked" styleClass="checkbox" value="true" /></div>
				</div>
				<div class="box">
					<div class="label" style="width:130px;"><bean:message key="user.loginAttempts" /></div>
					<div class="show" style="width:20px;text-align:right;"><bean:write name="secUser" property="loginAttempts" /></div>
				</div>						
			
				<hr/>
			
				<easy:hasAuthorization authorization="ROLE_BENUTZER_ROLLEN_VERGEBEN" model="${user}">
					<div class="box" style="height:auto;">
						<div class="label" style="width:130px;margin-top:13px;"><bean:message key="user.role" /></div>
						<div class="input">
							<table style="width:100%;">
								<tr>
									<td></td>
									<td style="width:20px;"><bean:message key="user.role.true" /></td>
									<td style="width:20px;"><bean:message key="user.role.false" /></td>
								</tr>
								<logic:iterate id="currentRole" name="roles">
									<logic:notEmpty name="role_${currentRole.id}">
										<logic:equal name="role_${currentRole.id}" value="true">
											<bean:define id="checkedTrue" value=" checked='checked'"/>
											<bean:define id="checkedFalse" value="" />
										</logic:equal>
										<logic:equal name="role_${currentRole.id}" value="false">
											<bean:define id="checkedTrue" value="" />
											<bean:define id="checkedFalse" value=" checked='checked'" />
										</logic:equal>
										<bean:define id="readonly" value="" />
										<easy:hasNotAuthorization authorization="ROLE_BENUTZER_ROLLEN_VERGEBEN" model="${currentRole}">
											<bean:define id="readonly" value=" disabled='disabled'" />
										</easy:hasNotAuthorization>														
									</logic:notEmpty>
									<tr>
										<td style="vertical-align:middle;"><bean:write name="currentRole" property="name" /></td>
										<td class="center">
											<input type="radio" name="role_${currentRole.id}" value="true" class="checkbox" ${checkedTrue} ${readonly} />
										</td>
										<td class="center">
											<input type="radio" name="role_${currentRole.id}" value="false" class="checkbox" ${checkedFalse} ${readonly} />
										</td>
									</tr>
								</logic:iterate>
							</table>
						</div>
					</div>
				</easy:hasAuthorization>

				<easy:hasNotAuthorization authorization="ROLE_BENUTZER_ROLLEN_VERGEBEN" model="${user}">
					<div class="box" style="height:auto;">
						<div class="label"><bean:message key="user.role" />:</div>
						<div class="input">
							<ul>
								<logic:iterate id="currentRole" name="secUser" property="roles">
									<li><bean:write name="currentRole" property="name" /></li>
								</logic:iterate>
							</ul>
						</div>
					</div>
				</easy:hasNotAuthorization>
			</c:if>
		</div>
	</div>
	
	<div class="buttonBar">
		<html:link action="/admin/listUsers" styleClass="buttonBack"><bean:message key="button.backToList" /></html:link>
		<a href="https://evi.bahn-net.db.de/evi2/sucheForm.do?actionID=search&tab=Mitarbeiter&attribute=Email&filter=${user.eviMail}" class="buttonSearch" target="_blank" title="<bean:message key='user.evi.title' />"><bean:message key="user.evi" /></a>
		<logic:notEqual name="userForm" property="userId" value="0">
			<easy:hasAuthorization authorization="ROLE_BENUTZER_LESEN" model="${user}">
				<jsp:useBean id="paramMap" class="java.util.HashMap"/>
				<c:set target="${paramMap}" property="userId" value="${user.id}"/>
				<c:set target="${paramMap}" property="history" value="edit"/>
				<html:link action="/admin/viewUser" name="paramMap" styleClass="buttonSearch"><bean:message key="button.view" /></html:link>
			</easy:hasAuthorization>
			<easy:hasAuthorization authorization="ROLE_BENUTZER_LOESCHEN" model="${user}">
				<bean:define id="confirmText" toScope="page"><bean:message key="confirm.user.delete" /></bean:define>
				<html:link action="/admin/deleteUser" paramId="userId" paramName="userForm" paramProperty="userId" styleClass="buttonDelete" onclick="return confirmLink(this.href, '${confirmText}');"><bean:message key="button.delete" /></html:link>
			</easy:hasAuthorization>
		</logic:notEqual>
		<html:link href="javascript:document.getElementById('userForm').reset();" styleClass="buttonCancel"><bean:message key="button.cancel" /></html:link>
		<html:link href="javascript:document.getElementById('userForm').submit();" styleClass="buttonSave"><bean:message key="button.save" /></html:link>
	</div>
</html:form>

<script type="text/javascript">
	function changeLoginFields(){
		disableField('loginName');
	}
	if(document.getElementById("changeLogin") != null && document.getElementById("changeLogin").checked == false){
		changeLoginFields();
	}
</script>

<jsp:include page="../../main_footer.jsp"/>
