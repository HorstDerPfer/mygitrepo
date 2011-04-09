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

				<jsp:useBean id="urls" class="java.util.HashMap" />

				<display:table id="current"
					 name="sqlQueriesDatenauszug" 
					 export="false"
					 requestURI="listSqlQueries.do"
					 pagesize="20"
					 sort="list"
					 class="colored"
					 decorator="db.training.easy.util.displaytag.decorators.AddRowLink">
					 
					<easy:hasAuthorization model="${current}" authorization="ROLE_SQLQUERY_AUSFUEHREN">
						<c:set target="${urls}" property="${current.id}">
							<c:url value="executeSqlQuery.do?sqlQueryId=${current.id}" />
						</c:set>
					</easy:hasAuthorization>
					
					<display:column property="name" titleKey="osb.sqlQuery.name" sortable="true" />
					<display:column property="beschreibung" titleKey="osb.sqlQuery.beschreibung" sortable="true" />
					<display:column property="gueltigVon" titleKey="osb.sqlQuery.gueltigVon" sortable="true" format="{0,date,dd.MM.yyyy}" />
					<display:column property="gueltigBis" titleKey="osb.sqlQuery.gueltigBis" sortable="true" format="{0,date,dd.MM.yyyy}" />
					<display:column style="width:20px;" class="right">
						<easy:hasAuthorization model="${current}" authorization="ROLE_SQLQUERY_AUSFUEHREN">
							<html:link action="/executeSqlQuery" styleClass="show" titleKey="button.view">
								<html:param name="sqlQueryId" value="${current.id}" />
								&nbsp;
							</html:link>
						</easy:hasAuthorization>
					</display:column>
					<display:column style="width:20px;" class="right">
						<easy:hasAuthorization model="${current}" authorization="ROLE_SQLQUERY_BEARBEITEN">
							<html:link action="/editSqlQuery" styleClass="edit" titleKey="button.edit">
								<html:param name="sqlQueryId" value="${current.id}" />
								&nbsp;
							</html:link>
						</easy:hasAuthorization>
					</display:column>
					<display:column style="width:20px;" class="right">
						<easy:hasAuthorization model="${current}" authorization="ROLE_SQLQUERY_LOESCHEN">
							<c:set var="confirmText" scope="page"><bean:message key="confirm.sqlQuery.delete" /></c:set>
							<html:link action="/deleteSqlQuery" styleClass="delete" onclick="return confirmLink(this.href, '${confirmText}');">
								<html:param name="sqlQueryId" value="${current.id}"></html:param>
								&nbsp;
							</html:link>
						</easy:hasAuthorization>
					</display:column>
					
					<display:setProperty name="basic.empty.showtable" value="true" />
					<display:setProperty name="paging.banner.item_name"><bean:message key="osb.sqlQuery" /></display:setProperty>
					<display:setProperty name="paging.banner.items_name"><bean:message key="osb.sqlQueries" /></display:setProperty>
				</display:table>
