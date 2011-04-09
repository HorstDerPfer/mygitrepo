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

			<%-- Öffnet Punkt in Startmenü --%>
			<script type="text/javascript">
			    openMainMenu('navLink_admin-users');
			</script>
			
			<div class="textcontent_head">
			    <bean:message key="menu.admin.user" />
			</div>
			
			<div class="textcontent" style="text-align:center;">
			
				<jsp:useBean id="urls" class="java.util.HashMap"/>
				
				<display:table id="currentUser"
					 name="users" 
					 export="false" 
					 requestURI="listUsers.do"
					 pagesize="20"
					 sort="list"
					 class="colored"
					 decorator="db.training.easy.util.displaytag.decorators.AddRowLink">
					 
					<%-- URL wird erzeugt, ggf. inkl. sessionId --%>
					<c:set target="${urls}" property="${currentUser.id}" value="#" />
					
					<c:if test="${currentUser != null}">
						<easy:hasAuthorization authorization="ROLE_BENUTZER_LESEN" model="${currentUser}">
							<c:set target="${urls}" property="${currentUser.id}">
								<c:url value="viewUser.do?userId=${currentUser.id}"/>
							</c:set>
						</easy:hasAuthorization>
					</c:if>
					
					<display:column property="caption" titleKey="user.name" sortable="true" />
					<display:column property="loginName" titleKey="user.loginName" sortable="true" />
					<display:column property="email" titleKey="user.email" sortable="true" autolink="true"/>
					<display:column titleKey="user.evi">
						<a href="https://evi.bahn-net.db.de/evi2/sucheForm.do?actionID=search&tab=Mitarbeiter&attribute=Email&filter=${currentUser.eviMail}" class="list" target="_blank" title="<bean:message key='user.evi.title' />"><bean:message key="user.evi.link" /></a>
					</display:column>
					<display:column style="width:20px;" class="right">
						<easy:hasAuthorization authorization="ROLE_BENUTZER_BEARBEITEN" model="${currentUser}">
							<html:link action="/admin/editUser" paramId="userId" paramName="currentUser" paramProperty="id" styleClass="edit" titleKey="button.edit">&nbsp;</html:link>
						</easy:hasAuthorization>
					</display:column>
					
					<display:setProperty name="basic.empty.showtable" value="true" />
					<display:setProperty name="paging.banner.item_name"><bean:message key="user" /></display:setProperty>
					<display:setProperty name="paging.banner.items_name"><bean:message key="user" /></display:setProperty>
				</display:table>
				
			</div>
			
			<div class="buttonBar">
				<easy:hasRole role="ADMINISTRATOR_ZENTRAL">
					<c:choose>
						<c:when test="${!empty regionalrechtEnabled && regionalrechtEnabled == true}">
							<html:link action="/admin/toggleRegionalPermissions" styleClass="buttonLock"><bean:message key="button.regionalPermissions.disable" /></html:link>
						</c:when>
						<c:otherwise>
							<html:link action="/admin/toggleRegionalPermissions" styleClass="buttonUnlock" title="test"><bean:message key="button.regionalPermissions.enable" /></html:link>
						</c:otherwise>
					</c:choose>
				</easy:hasRole>

				<authz:authorize ifAnyGranted="ROLE_BENUTZER_ANLEGEN_ALLE,ROLE_BENUTZER_ANLEGEN_BUSINESS_AREA,ROLE_BENUTZER_ANLEGEN_LOCATION">
					<bean:define id="userId" value="0"/>
		    		<html:link action="/admin/editUser" paramId="userId" paramName="userId" styleClass="buttonAdd"><bean:message key="button.user.add" /></html:link>
				</authz:authorize>
			</div>

<jsp:include page="../../main_footer.jsp"/>
