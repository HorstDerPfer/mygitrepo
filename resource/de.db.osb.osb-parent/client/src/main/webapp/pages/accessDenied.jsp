<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<html:xhtml/>

<jsp:include page="main_head.jsp"/>
	<jsp:include page="main_path.jsp"/>
		<jsp:include page="main_menu.jsp"/>

		<div class="textcontent_head">Zugriff verweigert!</div>
		<div class="textcontent">Sie haben für diesen Vorgang keine Berechtigung!</div>

<jsp:include page="main_footer.jsp"/>