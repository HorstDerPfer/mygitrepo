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
			    openMainMenu('navLink_osb_daten-sqlquery');
			</script>
			
			<div class="textcontent_head">
			    <bean:message key="osb.sqlQuery.edit" />
			</div>
			
			<html:form action="/saveSqlQuery" focus="name">
			
				<div class="textcontent">
					<html:hidden property="sqlQueryId" />
					<div class="textcontent_left">
						<div class="box">
							<div class="label"><label for="name"><bean:message key="osb.sqlQuery.name" />*</label></div>
							<div class="input"><html:text property="name" styleId="name" errorStyle="${errorStyle}" /></div>
						</div>
						<div class="box">
							<div class="label"><label for="beschreibung"><bean:message key="osb.sqlQuery.beschreibung" /></label></div>
							<div class="input"><html:textarea property="beschreibung" styleId="beschreibung" style="height:65px"/></div>
						</div>
					</div>
					<div class="textcontent_right">						
						<div class="box">
							<div class="label"><label for="gueltigVon"><bean:message key="osb.sqlQuery.gueltigVon" /></label></div>
							<div class="input">
								<html:text property="gueltigVon" styleId="gueltigVon" maxlength="10" styleClass="date" />
								<img src="<c:url value='/static/img/calendar.gif' />" id="buttonGueltigVon" />
							</div>
						</div>
						<div class="box">
							<div class="label"><label for="gueltigBis"><bean:message key="osb.sqlQuery.gueltigBis" /></label></div>
							<div class="input">
								<html:text property="gueltigBis" styleId="gueltigBis" maxlength="10" styleClass="date" />
								<img src="<c:url value='/static/img/calendar.gif' />" id="buttonGueltigBis" />
							</div>
						</div>
						<div class="box">
							<div class="label"><label for="modul"><bean:message key="osb.sqlQuery.modul" /></label></div>
							<div class="input">
								<html:select name="sqlQueryForm" property="modul" styleId="modul">
									<logic:iterate id="current" name="modulListe">
										<html:option value="${current}">${current}</html:option>
									</logic:iterate>
								</html:select>
							</div>
						</div>
						<div class="box">
							<div class="label"><label for="cluster"><bean:message key="osb.sqlQuery.cluster" /></label></div>
							<div class="input">
								<html:select name="sqlQueryForm" property="cluster" styleId="cluster">
									<logic:iterate id="current" name="clusterListe">
										<html:option value="${current}"><bean:message key="osb.sqlQueries.${current}"/></html:option>
									</logic:iterate>
								</html:select>
							</div>
						</div>
					</div>
					<div class="box" style="margin-top:-10px">
						<div class="label"><label for="query"><bean:message key="osb.sqlQuery.query" />*</label></div>
						<div class="input"><html:textarea property="query" styleId="query" style="width:617px;height:50px" errorStyle="${errorStyle}width:617px;height:50px" /></div>
					</div>
				</div>
				<div class="buttonBar">
					<html:link action="/listSqlQueries.do?activeTabDiv=${sqlQueryForm.cluster}" styleClass="buttonBack"><bean:message key="button.backToList" /></html:link>
					<html:link action="/executeSqlQuery.do?sqlQueryId=${sqlQueryForm.sqlQueryId}" target="_blank" styleClass="buttonSearch"><bean:message key="osb.sqlQuery.execute" /></html:link>
					<logic:notEqual name="sqlQueryForm" property="sqlQueryId" value="0">
						<bean:define id="confirmText" toScope="page"><bean:message key="confirm.sqlQuery.delete" /></bean:define>
						<html:link action="/deleteSqlQuery" paramId="sqlQueryId" paramName="sqlQueryForm" paramProperty="sqlQueryId" styleClass="buttonDelete" onclick="return confirmLink(this.href, '${confirmText}');"><bean:message key="button.delete" /></html:link>
					</logic:notEqual>

					<html:link href="javascript:document.getElementById('sqlQueryForm').reset();" styleClass="buttonCancel"><bean:message key="button.cancel" /></html:link>
					<html:link href="javascript:document.getElementById('sqlQueryForm').submit();" styleClass="buttonSave"><bean:message key="button.save" /></html:link>
				</div>
				
			</html:form>

			<script type="text/javascript">
			  Calendar.setup(
			    {
			      inputField  : "gueltigVon",
			      button      : "buttonGueltigVon"
			    }
			  );
			  Calendar.setup(
			    {
			      inputField  : "gueltigBis",
			      button      : "buttonGueltigBis"
			    }
			  );
			</script>

<jsp:include page="../../main_footer.jsp"/>