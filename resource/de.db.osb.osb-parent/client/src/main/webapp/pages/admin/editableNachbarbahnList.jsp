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

<jsp:include page="../main_head.jsp"/>
<jsp:include page="../main_path.jsp"/>
<jsp:include page="../main_menu.jsp"/>

<%-- Öffnet Punkt in Startmenü --%>
<authz:authorize ifAllGranted="ROLE_BOB_USER">
	<script type="text/javascript">
	    openMainMenu('navLink_admin-nachbarbahnList');
	</script>
</authz:authorize>
<authz:authorize ifAllGranted="ROLE_OSB_USER">
	<script type="text/javascript">
	    openMainMenu('navLink_osb_stammdaten-nachbarbahnList');
	</script>
</authz:authorize>

<div class="textcontent_head">
    Stammdaten verwalten
</div>

<div class="textcontent" style="text-align:center;">
	<html:form action="/admin/listNachbarbahn">
		<html:hidden property="method" value="list" />
		<c:forEach var="displaytagParamMapEntry" items="${displaytagParamMap}">
			<input type="hidden" id="${displaytagParamMapEntry.key}" name="${displaytagParamMapEntry.key}" value="${displaytagParamMapEntry.value}" />
		</c:forEach>
		<script type="text/javascript">
			function setDispatch(s) {
				$('editableNachbarbahnListItemForm').method.value = s;
			}
		</script>
		
		<bean:define id="titleEdit"><bean:message key="button.edit" /></bean:define>
		<bean:define id="titleDelete"><bean:message key="button.delete" /></bean:define>
		<bean:define id="confirmText" toScope="page"><bean:message key="confirm.nachbarbahn.delete" /></bean:define>
		
		<jsp:useBean id="urls" class="java.util.HashMap"/>
		
		<display:table id="currentItem"
			 name="editableListItems" 
			 export="false" 
			 requestURI="/admin/listNachbarbahn.do?method=list"
			 excludedParams="*"
			 pagesize="20"
			 sort="list"
			 class="colored"
			 decorator="db.training.easy.util.displaytag.decorators.AddRowLink">
			
			<c:choose>
				<c:when test="${currentItem.readonly == true }">
					<display:column titleKey="title.nachbarbahn.name" property="text" sortable="true" style="color:#a0a0a0;font-style:italic;" />
					<display:column style="text-align:right;width:15px;" media="html">
						&nbsp;
					</display:column>
					<display:column style="text-align:right;width:15px;" media="html">
						&nbsp;
					</display:column>
				</c:when>
				<c:when test="${(currentItem.readonly == false) && (currentItem.id == editableNachbarbahnListItemForm.id) }">
					<display:column titleKey="title.nachbarbahn.name" sortable="true" media="html">
						<html:hidden property="id" value="${currentItem.id }" />
						<html:text property="updateName" styleClass="input" value="${currentItem.name }" errorStyle="${errorStyle }" ></html:text>
					</display:column>
					<display:column style="text-align:right;width:15px;" media="html">
						<html:link href="#" onclick="setDispatch('update');$('editableNachbarbahnListItemForm').submit();" styleClass="buttonSave" styleId="buttonSave"><bean:message key="button.save" /></html:link>
					</display:column>
					<display:column style="text-align:right;width:15px;" media="html">
						<html:link action="/admin/listNachbarbahn.do?method=list&id=" name="displaytagParamMap" styleClass="cancelBig">&nbsp;</html:link>
					</display:column>
				</c:when>
				<c:otherwise>
					<display:column titleKey="title.nachbarbahn.name" property="text" sortable="true" />
					<display:column style="text-align:right;width:15px;" media="html">
						<html:link action="/admin/listNachbarbahn?method=list&id=${currentItem.id}" name="displaytagParamMap" styleClass="edit" title="${titleEdit}">&nbsp;</html:link>
					</display:column>
					<display:column style="text-align:right;width:15px;" media="html">
						<html:link action="/admin/listNachbarbahn?method=delete&id=${currentItem.id}" name="displaytagParamMap" styleClass="delete" title="${titleDelete}" onclick="return confirmLink(this.href, '${confirmText}');">&nbsp;</html:link>
					</display:column>
				</c:otherwise>
			</c:choose>
			
			<display:setProperty name="basic.empty.showtable" value="true" />
			<display:setProperty name="paging.banner.item_name"><bean:message key="title.nachbarbahn.name" /></display:setProperty>
			<display:setProperty name="paging.banner.items_name"><bean:message key="title.nachbarbahn.name" /></display:setProperty>
		</display:table>
		<br />
		<fieldset>
			<legend>
				<bean:message key="insert.new.entry" />
			</legend>
			<table style="text-align:center;">
				<colgroup>
				</colgroup>
				<tbody>
					<tr>
						<td>
							Text:
						</td>
						<td style="width:30px;">&nbsp;</td>
						<td>
							<html:text property="insertName" styleClass="input" errorStyle="${errorStyle }"></html:text>
						</td>
						<td style="text-align:right;">
							<html:link href="#" onclick="setDispatch('insert');$('editableNachbarbahnListItemForm').submit();" styleClass="plusBig" styleId="buttonAdd" titleKey="button.create">
								&nbsp;
							</html:link>
						</td>
					</tr>
				</tbody>
			</table>
		</fieldset>
	</div>
</html:form>

<div class="buttonBar">
	&nbsp;
</div>

<jsp:include page="../main_footer.jsp"/>