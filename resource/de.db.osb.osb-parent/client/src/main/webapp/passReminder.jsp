<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html:xhtml />

<jsp:include page="/pages/main_head.jsp"/>
<jsp:include page="/pages/main_path.jsp"/>
<jsp:include page="/pages/main_menu_simple.jsp"/>
	
<html:form action="/passReset" focus="email">
	<div class="textcontent_head">
    	<bean:message key="password.reminder.title"/>
    </div>
	<div class="textcontent">
		<bean:message key="password.reminder.description"/>
		<br/><br/>
		<div class="box">
			<div class="label"><label for="email"><bean:message key="user.email"/>*:</label></div>
			<div class="input"><html:text property="email" errorStyle="${errorStyle}" /></div>
		</div>
	</div>
	<div class="buttonBar">
  		<html:link action="login" styleClass="buttonBack"><bean:message key="password.reminder.back"/></html:link>
		<input type="submit" class="hiddenSubmit" />
  		<html:link href="javascript:document.forms.passResetForm.submit();" styleClass="buttonReload"><bean:message key="button.reset"/></html:link>
	</div>
</html:form>

<%-- main_footer schließt master_contend-div usw. --%>
<jsp:include page="/pages/main_footer.jsp" />