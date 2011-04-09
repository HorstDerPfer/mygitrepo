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
				    openMainMenu('navLink_admin-meilensteine');
				</script>
			</authz:authorize>
			<authz:authorize ifAllGranted="ROLE_OSB_USER">
				<script type="text/javascript">
				    openMainMenu('navLink_osb_stammdaten-meilensteine');
				</script>
			</authz:authorize>
			
			<div class="textcontent_head">
			    <bean:message key="admin.stammdaten" />
			</div>
			
			<div class="textcontent" style="text-align:center;">
				<html:form action="/admin/listMeilensteine">
					<html:hidden property="method" value="list" />
					<c:forEach var="displaytagParamMapEntry" items="${displaytagParamMap}">
						<input type="hidden" id="${displaytagParamMapEntry.key}" name="${displaytagParamMapEntry.key}" value="${displaytagParamMapEntry.value}" />
					</c:forEach>
					<script type="text/javascript">
						function setDispatch(s) {
							$('editableMeilensteinListItemForm').method.value = s;
						}
					</script>
					<jsp:useBean id="urls" class="java.util.HashMap"/>
					<display:table id="currentItem"
						 name="editableListItems" 
						 export="false" 
						 requestURI="/admin/listMeilensteine.do?method=list"
						 excludedParams="*"
						 pagesize="20"
						 sort="list"
						 defaultsort="1"
						 class="colored"
						 decorator="db.training.easy.util.displaytag.decorators.AddRowLink">
						
						<c:choose>
							<c:when test="${(currentItem.readonly == false) && (currentItem.id == editableMeilensteinListItemForm.id) }">
								<%-- Edit --%>
								<display:column titleKey="title.meilensteine.art" property="art" sortable="true" sortName="art"/>
								<display:column titleKey="title.meilensteine.schnittstelle" property="schnittstelle" sortable="true" sortName="schnittstelle"/>
								<display:column titleKey="title.meilensteine.meilenstein" property="meilensteinBezeichnung" sortable="true" sortName="meilensteinBezeichnung"/>
								<display:column titleKey="title.meilensteine.wochen" sortable="false">
									<html:hidden property="id" value="${currentItem.id }" />
									<html:text property="updateWochenVorBaubeginn" styleClass="year" value="${currentItem.wochenVorBaubeginn }" errorStyle="${errorStyle }" ></html:text>
								</display:column>
								<display:column titleKey="title.meilensteine.wochentag" sortable="false">
									<html:select property="updateWochentag" styleId="wochentagSelect"  styleClass="small" errorStyle="${errorStyle }">
										<html:optionsCollection name="wochentagList" value="id" label="text"/>
									</html:select>
								</display:column>
								<display:column titleKey="title.meilensteine.mindestfrist" sortable="false">
									<html:radio property="updateMindestfrist" value="true" styleClass="checkbox" errorStyle="${errorStyle }"/>
									<bean:message key="mindestfrist.true" />
									<html:radio property="updateMindestfrist" value="false" styleClass="checkbox" errorStyle="${errorStyle }"/>
									<bean:message key="mindestfrist.false" />
								</display:column>
								<logic:empty name="currentItem" property="gueltigAb">
									<display:column titleKey="title.meilensteine.gueltigab" property="gueltigAb" sortable="true" sortName="gueltigAb"/>
								</logic:empty>
								<logic:notEmpty name="currentItem" property="gueltigAb">
									<display:column titleKey="title.meilensteine.gueltigab" sortable="true" sortName="gueltigAb">
										<html:text property="updateGueltigAb" styleClass="date" value="${currentItem.gueltigAb }" errorStyle="${errorStyle }" ></html:text>
									</display:column>
								</logic:notEmpty>
								<display:column style="text-align:right;width:15px;" media="html">
									<html:link href="#" onclick="setDispatch('update');$('editableMeilensteinListItemForm').submit();" styleClass="buttonSave" styleId="buttonSave"><bean:message key="button.save" /></html:link>
								</display:column>
								<display:column style="text-align:right;width:15px;" media="html">
									<html:link action="/admin/listMeilensteine.do?method=list&id=" name="displaytagParamMap" styleClass="cancelBig">&nbsp;</html:link>
								</display:column>
							</c:when>
							<c:otherwise>
								<%-- View --%>
								<display:column titleKey="title.meilensteine.art" property="art" sortable="true" sortName="art"/>
								<display:column titleKey="title.meilensteine.schnittstelle" property="schnittstelle" sortable="true" sortName="schnittstelle"/>
								<display:column titleKey="title.meilensteine.meilenstein" property="meilensteinBezeichnung" sortable="true" sortName="meilensteinBezeichnung"/>
								<display:column titleKey="title.meilensteine.wochen" property="wochenVorBaubeginn" sortable="false" />
								<display:column titleKey="title.meilensteine.wochentag" property="wochentag" sortable="false" />
								<display:column titleKey="title.meilensteine.mindestfrist" sortable="false">
									<bean:message key="mindestfrist.${currentItem.mindestfrist}" />
								</display:column>
								<display:column titleKey="title.meilensteine.gueltigab" property="gueltigAb" sortable="true" sortName="gueltigAb"/>
								<display:column style="text-align:right;width:15px;" media="html">
									<html:link action="/admin/listMeilensteine.do?method=list&id=${currentItem.id}" name="displaytagParamMap" styleClass="edit">&nbsp;</html:link>
								</display:column>
								<logic:empty name="currentItem" property="gueltigAb">
									<display:column style="text-align:right;width:15px;" media="html"/>
								</logic:empty>
								<logic:notEmpty name="currentItem" property="gueltigAb">
									<display:column style="text-align:right;width:15px;" media="html">
										<html:link action="/admin/listMeilensteine.do?method=delete&id=${currentItem.id}" name="displaytagParamMap" styleClass="delete">&nbsp;</html:link>
									</display:column>
								</logic:notEmpty>
							</c:otherwise>
						</c:choose>
						
						<display:setProperty name="basic.empty.showtable" value="true" />
						<display:setProperty name="paging.banner.item_name"><bean:message key="menu.admin.meilenstein" /></display:setProperty>
						<display:setProperty name="paging.banner.items_name"><bean:message key="menu.admin.meilensteine" /></display:setProperty>
					</display:table>
					<br />
					
					<%-- Insert --%>
					<bean:define id="urlAddMeilenstein" toScope="page"><c:url value="/refreshMeilensteinSelect.do" /></bean:define>
					<fieldset>
						<legend><bean:message key="insert.new.entry" /></legend>
						<table class="colored" style="text-align:center;">
							<thead>
								<tr>
									<th><bean:message key="title.meilensteine.art" /></th>
									<th><bean:message key="title.meilensteine.schnittstelle" /></th>
									<th><bean:message key="title.meilensteine.meilenstein" /></th>
									<th><bean:message key="title.meilensteine.wochen" /></th>
									<th><bean:message key="title.meilensteine.wochentag" /></th>
									<th><bean:message key="title.meilensteine.mindestfrist" /></th>
									<th><bean:message key="title.meilensteine.gueltigab" /></th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<tr>									
									<td>
										<html:select property="insertArt" styleId="insertArtSelect"  styleClass="date" errorStyle="${errorStyle }" onchange="refreshAddMeilenstein('${urlAddMeilenstein}', this.value, $F('insertSchnittstelleSelect'));">
											<html:optionsCollection name="artenList" value="id" label="text" />
										</html:select>
									</td>
									<td>
										<html:select property="insertSchnittstelle" styleId="insertSchnittstelleSelect"  styleClass="date" errorStyle="${errorStyle }" onchange="refreshAddMeilenstein('${urlAddMeilenstein}', $F('insertArtSelect'), this.value);">
											<html:optionsCollection name="schnittstellenList" value="id" label="text"/>
										</html:select>
									</td>
									<td>
										<jsp:include page="editableMeilensteinListInsert.jsp" />
									</td>
									<td>
										<html:text property="insertWochenVorBaubeginn" styleClass="date" errorStyle="${errorStyle }" ></html:text>
									</td>
									<td>
										<html:select property="insertWochentag" styleId="insertWochentagSelect"  styleClass="small" errorStyle="${errorStyle }">
											<html:optionsCollection name="wochentagList" value="id" label="text"/>
										</html:select>
									</td>
									<td>
										<html:radio property="insertMindestfrist" value="true" styleClass="checkbox" errorStyle="${errorStyle }"/>
										<bean:message key="mindestfrist.true" />
										<html:radio property="insertMindestfrist" value="false" styleClass="checkbox" errorStyle="${errorStyle }"/>
										<bean:message key="mindestfrist.false" />
									</td>
									<td>
										<html:text property="insertGueltigAb" styleClass="date" errorStyle="${errorStyle }" ></html:text>
									</td>
									<td style="text-align:right;">
										<html:link href="#" onclick="setDispatch('insert');$('editableMeilensteinListItemForm').submit();" styleClass="plusBig" styleId="buttonAdd" titleKey="button.create">
											&nbsp;
										</html:link>
									</td>
								</tr>
							</tbody>
						</table>
					</fieldset>
				</html:form>
			</div>
			
			<div class="buttonBar">
			<bean:define id="confirmText" toScope="page"><bean:message key="confirm.qualitygates.calc" /></bean:define>
				<html:link action="/admin/calculateQualityGates" onclick="return confirmLink(this.href, '${confirmText}');" styleClass="buttonReload" styleId="buttonCalc">
					<bean:message key="button.meilensteine.berechnen" />
					<html:param name="execute" value="true" />
				</html:link>
			</div>
			
			<div id="divProgressbar">
			</div>
			
<jsp:include page="../main_footer.jsp"/>