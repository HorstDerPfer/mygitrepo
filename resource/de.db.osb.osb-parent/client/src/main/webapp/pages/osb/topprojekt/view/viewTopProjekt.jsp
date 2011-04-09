<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
<html:xhtml/>

<jsp:include page="/pages/main_head.jsp"/>
<jsp:include page="/pages/main_path.jsp"/>
<jsp:include page="/pages/main_menu.jsp"/>
			
<script type="text/javascript">
    openMainMenu('navLink_osb_workflow-topprojekt');
</script>

<div class="textcontent_head">
	<span style="float:left"><bean:message key="topprojekt" /></span>
	<span style="float:right;">
		<c:if test="${projekt.deleted == true}">
			<bean:message key="common.deleted" />&nbsp;-&nbsp;
		</c:if>
		<bean:message key="common.lastchangedate" />:&nbsp;
		<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${projekt.lastChangeDate}" />
	</span>
</div>
<div class="textcontent" style="border-bottom:0px;">
	<div class="textcontent_left">
		<div class="box">
			<div class="label"><bean:message key="topprojekt.name" /></div>
			<div class="show">${projekt.name}</div>
		</div>	
		<div class="box">
			<div class="label"><bean:message key="topprojekt.anmelder" /></div>
			<div class="show"><c:if test="${!empty projekt.anmelder}">${projekt.anmelder.caption}</c:if></div>
		</div>
		<div class="box">
			<div class="label"><bean:message key="topprojekt.sapprojektnummer" /></div>
			<div class="show">${projekt.sapProjektNummer}</div>
		</div>
		<div class="box">
			<div class="label"><bean:message key="topprojekt.regionalbereich" /></div>
			<div class="show">${projekt.regionalbereich.name}</div>
		</div>
		<div class="box">
			<div class="label"><bean:message key="topprojekt.baukosten" /></div>
			<div class="show"><fmt:formatNumber maxFractionDigits="10" value="${projekt.baukosten}" /></div>
		</div>		
	</div>
</div>

<div class="buttonBar">
	<html:link action="/back.do" styleClass="buttonBack"><bean:message key="button.back" /></html:link>
	
	<easy:hasAuthorization model="${projekt}" authorization="ROLE_TOPPROJEKT_BEARBEITEN">
		<html:link action="/osb/editTopProjekt.do" styleClass="buttonEdit">
			<html:param name="topProjektId" value="${projekt.id}" />
			<bean:message key="button.edit" />
		</html:link>
	</easy:hasAuthorization>
</div>

<br/>

<jsp:include page="viewTopProjektMassnahmen.jsp" />
				
<jsp:include page="/pages/main_footer.jsp"/>