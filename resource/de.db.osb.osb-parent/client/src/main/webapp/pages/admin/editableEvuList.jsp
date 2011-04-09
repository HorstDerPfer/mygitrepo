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
	    openMainMenu('navLink_admin-evuList');
	</script>
</authz:authorize>
<authz:authorize ifAllGranted="ROLE_OSB_USER">
	<script type="text/javascript">
	    openMainMenu('navLink_osb_stammdaten-evuList');
	</script>
</authz:authorize>

<div class="textcontent_head">
    Stammdaten verwalten
</div>

<div class="textcontent" style="text-align:center;">
	<html:form action="/admin/listEvu">
		<html:hidden property="method" value="list" />
		<c:forEach var="displaytagParamMapEntry" items="${displaytagParamMap}">
			<input type="hidden" id="${displaytagParamMapEntry.key}" name="${displaytagParamMapEntry.key}" value="${displaytagParamMapEntry.value}" />
		</c:forEach>
		<script type="text/javascript">
			function setDispatch(s) {
				$('editableEvuListItemForm').method.value = s;
			}
		</script>
		
		<bean:define id="titleEdit"><bean:message key="button.edit" /></bean:define>
		<bean:define id="titleDelete"><bean:message key="button.delete" /></bean:define>
		<bean:define id="confirmText" toScope="page"><bean:message key="confirm.evu.delete" /></bean:define>
		
		<jsp:useBean id="urls" class="java.util.HashMap"/>
		
		<display:table id="currentItem"
			 name="editableListItems" 
			 export="false" 
			 requestURI="/admin/listEvu.do?method=list"
			 excludedParams="*"
			 pagesize="20"
			 sort="list"
			 defaultsort="1"
			 class="colored"
			 decorator="db.training.easy.util.displaytag.decorators.AddRowLink">
			
			<c:choose>
				<c:when test="${(currentItem.id == editableEvuListItemForm.id) }">
					<display:column titleKey="title.evu.kundennummer" sortable="true" media="html" style="width:80px;">
						<html:hidden property="id" value="${currentItem.id }" />
						<html:text  property="updateKundenNr" styleClass="input" value="${currentItem.kundenNr }" errorStyle="${errorStyle }" style="width:80px;"></html:text>
					</display:column>
					<display:column titleKey="title.evu.name" sortable="true" media="html">
						<html:text property="updateName" style="width:250px;" styleClass="input" value="${currentItem.name }" errorStyle="${errorStyle }" ></html:text>
					</display:column>
					<display:column titleKey="title.evu.kurzbezeichnung" sortable="true" media="html">
						<html:text property="updateKurzbezeichnung" style="width:200px;" styleClass="input" value="${currentItem.kurzbezeichnung }" errorStyle="${errorStyle }" ></html:text>
					</display:column>
					<display:column titleKey="title.evu.kundengruppe" sortable="true" media="html">
						<html:hidden property="updateEvugruppeId" styleId="updateEvugruppeId" />
						<html:text property="updateEvugruppe" style="width:250px;" styleId="updateEvugruppe" styleClass="input" value="${currentItem.evugruppe.name }" errorStyle="${errorStyle }" ></html:text>
						<img src="<c:url value='/static/img/indicator.gif' />" id="updateEvugruppeIndicator" style="display:none;" />
						<div id="updateEvugruppeSelect" class="autocomplete"></div>
					</display:column>
					<display:column style="text-align:right;width:15px;" media="html">
						<%--<html:link action="/admin/listEvu.do?method=update" styleClass="saveBig">&nbsp;</html:link>--%>
						<html:link href="#" onclick="setDispatch('update');$('editableEvuListItemForm').submit();" styleClass="buttonSave" styleId="buttonSave"><bean:message key="button.save" /></html:link>
					</display:column>
					<display:column style="text-align:right;width:15px;" media="html">
						<html:link action="/admin/listEvu.do?method=list&id=" name="displaytagParamMap" styleClass="cancelBig">&nbsp;</html:link>
					</display:column>
				</c:when>
				<c:otherwise>
					<display:column titleKey="title.evu.kundennummer" property="kundenNr" sortable="true" style="width:50px;" />
					<display:column titleKey="title.evu.name" property="text" sortable="true" />
					<display:column titleKey="title.evu.kurzbezeichnung" property="kurzbezeichnung" sortable="true" />
					<display:column titleKey="title.evu.kundengruppe" property="evugruppe.name" sortable="true" />
					<display:column style="text-align:right;width:15px;" media="html">
						<html:link action="/admin/listEvu?method=list&id=${currentItem.id}" name="displaytagParamMap" styleClass="edit" title="${titleEdit}">&nbsp;</html:link>
					</display:column>
					<display:column style="text-align:right;width:15px;" media="html">
						<html:link action="/admin/listEvu?method=delete&id=${currentItem.id}" name="displaytagParamMap" styleClass="delete" title="${titleDelete}" onclick="return confirmLink(this.href, '${confirmText}');">&nbsp;</html:link>
					</display:column>
				</c:otherwise>
			</c:choose>
			
			<display:setProperty name="basic.empty.showtable" value="true" />
			<display:setProperty name="paging.banner.item_name"><bean:message key="menu.admin.evu" /></display:setProperty>
			<display:setProperty name="paging.banner.items_name"><bean:message key="menu.admin.evu" /></display:setProperty>
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
							Kundennummer:
						</td>
						<td style="width:20px;">&nbsp;</td>
						<td>
							<html:text property="insertKundenNr" styleClass="price" errorStyle="${errorStyle }"></html:text>
						</td>
						<td style="width:30px;">&nbsp;</td>
						<td>
							Kundengruppe:
						</td>
						<td style="width:20px;">&nbsp;</td>
						<td>
							<html:hidden property="insertEvugruppeId" styleId="insertEvugruppeId" />
							<html:text property="insertEvugruppe" style="width:250px;"  styleId="insertEvugruppe" styleClass="input" errorStyle="${errorStyle }"></html:text>
							<img src="<c:url value='/static/img/indicator.gif' />" id="insertEvugruppeIndicator" style="display:none;" />
							<div id="insertEvugruppeSelect" class="autocomplete"></div>
						</td>
					</tr>
					<tr>
						<td>
							Name:
						</td>
						<td style="width:20px;">&nbsp;</td>
						<td>
							<html:text property="insertName" style="width:250px;"  styleClass="input" errorStyle="${errorStyle }"></html:text>
						</td>
						<td style="width:30px;">&nbsp;</td>
						<td>
							Kurzbezeichnung:
						</td>
						<td style="width:20px;">&nbsp;</td>
						<td>
							<html:text property="insertKurzbezeichnung" style="width:250px;"  styleClass="input" errorStyle="${errorStyle }"></html:text>
						</td>
						<td style="text-align:right;">
							<html:link href="#" onclick="setDispatch('insert');$('editableEvuListItemForm').submit();" styleClass="plusBig" styleId="buttonAdd" titleKey="button.create">
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

<script type="text/javascript">
	new Ajax.Autocompleter(
			"updateEvugruppe", 
			"updateEvugruppeSelect", 
			"<c:url value='/AutoCompleteEvuGruppe.view'/>", 
			{
				indicator: 'updateEvugruppeIndicator', 
				minChars: 1, 
				paramName: 'keyword',
				afterUpdateElement: function(text, li) {
					$j('#updateEvugruppeId').val(li.id);
				}
			}
	);
</script>

<script type="text/javascript">
	new Ajax.Autocompleter(
			"insertEvugruppe", 
			"insertEvugruppeSelect", 
			"<c:url value='/AutoCompleteEvuGruppe.view'/>", 
			{
				indicator: 'insertEvugruppeIndicator', 
				minChars: 1, 
				paramName: 'keyword',
				afterUpdateElement: function(text, li) {
					$j('#insertEvugruppeId').val(li.id);
				}
			}
	);
</script>

<jsp:include page="../main_footer.jsp"/>