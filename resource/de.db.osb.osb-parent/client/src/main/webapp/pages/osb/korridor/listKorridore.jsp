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
			    openMainMenu('navLink_osb-korridore');
			</script>
			
			<div class="textcontent_head">
			    <bean:message key="menu.korridor.list" />
			</div>
			
			<div class="textcontent" style="text-align:center;">
			
				<jsp:useBean id="urls" class="java.util.HashMap"/>
				
				<display:table id="currentKorridor"
					 name="korridore" 
					 export="false" 
					 requestURI="listKorridore.do"
					 pagesize="20"
					 sort="list"
					 class="colored"
					 decorator="db.training.easy.util.displaytag.decorators.AddRowLink">
					 
					<%-- URL wird erzeugt, ggf. inkl. sessionId --%>
					<c:set target="${urls}" property="${currentKorridor.id}" value="#" />
					
					<c:if test="${currentKorridor != null}">
						<c:set target="${urls}" property="${currentKorridor.id}">
							<c:url value="viewKorridor.do?korridorId=${currentKorridor.id}"/>
						</c:set>
					</c:if>
					
					<display:column property="id" titleKey="korridor.id" sortable="true" />
					<display:column property="name" titleKey="korridor.name" sortable="true" />
					<display:column style="width:20px;" class="right">
						<html:link action="/editKorridor" paramId="korridorId" paramName="currentKorridor" paramProperty="id" styleClass="edit" titleKey="button.edit">&nbsp;</html:link>
					</display:column>
					
					<display:setProperty name="basic.empty.showtable" value="true" />
					<display:setProperty name="paging.banner.item_name"><bean:message key="user" /></display:setProperty>
					<display:setProperty name="paging.banner.items_name"><bean:message key="user" /></display:setProperty>
				</display:table>
				
			</div>
			
			<div class="buttonBar">
				<bean:define id="korridorId" value="0"/>
	    		<html:link action="/editKorridor" paramId="korridorId" paramName="korridorId" styleClass="buttonAdd"><bean:message key="button.korridor.add" /></html:link>
			</div>

<jsp:include page="../../main_footer.jsp"/>