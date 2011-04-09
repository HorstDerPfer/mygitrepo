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
			    <bean:message key="menu.osb.korridor" />
			</div>
			
			<html:form action="/saveKorridor" focus="name">
			
				<div class="textcontent" style="height:280px;">
					<html:hidden property="korridorId" />
					<div class="textcontent_left">
						<div class="box">
							<div class="label"><label for="name"><bean:message key="korridor.name" />*</label></div>
							<div class="input">
								<html:text property="name" styleId="name" />
							</div>
						</div>
					</div>
				</div>
				
				<div class="buttonBar">
					<html:link action="/listKorridore" styleClass="buttonBack"><bean:message key="button.backToList" /></html:link>
					<%-- Loeschen noch nicht implementiert
					<logic:notEqual name="korridorForm" property="korridorId" value="0">
						<bean:define id="confirmText" toScope="page"><bean:message key="confirm.korridor.delete" /></bean:define>
						<html:link action="/deleteKorridor" paramId="korridorId" paramName="korridorForm" paramProperty="korridorId" styleClass="buttonDelete" onclick="return confirmLink(this.href, '${confirmText}');"><bean:message key="button.delete" /></html:link>
					</logic:notEqual>
					--%>
					<html:link href="javascript:document.getElementById('korridorForm').reset();" styleClass="buttonCancel"><bean:message key="button.cancel" /></html:link>
					<html:link href="javascript:document.getElementById('korridorForm').submit();" styleClass="buttonSave"><bean:message key="button.save" /></html:link>
				</div>
				
			</html:form>

<jsp:include page="../../main_footer.jsp"/>