<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<html:xhtml/>

<jsp:include page="/pages/main_head.jsp" />
<jsp:include page="/pages/main_path.jsp" />
<jsp:include page="/pages/main_menu_simple.jsp" />
	    
<c:if test="${not empty param.loginfailure}">
	<h2>bad login</h2>
</c:if>
<div class="textcontent_head" style="width:435px;"><bean:message key="logout" /></div>
<div class="textcontent" style="width:435px;">
  	Sie wurden erfolgreich abgemeldet.
	<p><a href="<c:url value='/'/>" class="list">Erneut anmelden</a><p/>
</div>

<%-- main_footer schließt master_contend-div usw. --%>
<jsp:include page="pages/main_footer.jsp" />