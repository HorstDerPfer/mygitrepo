<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

<html:xhtml/>

<jsp:include page="../../../main_head.jsp"/>
	<jsp:include page="../../../main_path.jsp"/>
		<jsp:include page="../../../main_menu.jsp"/>

			<%-- öffnet Punkt in Startmenü --%>
			<script type="text/javascript">
			    openMainMenu('navLink_bob');
			</script>

			<div class="textcontent_head" >
				<bean:message key="ueb.zug.knotenzeiten.edit" />
			</div>
			<html:form action="/saveKnotenzeiten" >
				<%--<html:hidden property="zugIds"/>--%>
				<html:hidden property="ersterVerkehrstag"/>
				<html:hidden property="letzterVerkehrstag"/>
				<html:hidden property="add" />
				
				<div class="textcontent">
					<logic:empty name="zug">
						<jsp:include page="../../view/uebergabeblatt/viewKnotenzeitenVerkehrszeitraum.jsp"/>
					</logic:empty>
					<logic:notEmpty name="zug">
						<jsp:include page="../../view/uebergabeblatt/viewKnotenzeitenZugdaten.jsp"/>
					</logic:notEmpty>
					<jsp:include page="editKnotenzeitenTable.jsp"/>
					<jsp:include page="editKnotenzeitenEingabe.jsp"/>
				</div>
				<div class="buttonBar">
					<bean:define id="bmid" value="${baumassnahme.id}"/>
					<html:link action="/editBaumassnahme" styleId="backButton" styleClass="buttonBack">
						<html:param name="id" value="${knotenzeitenForm.baumassnahmeId}" />
						<html:param name="tab" value="Uebergabeblatt" />
						<html:param name="showZuegeUeb" value="true" />
						<bean:message key="button.back" />						
					</html:link>
					<html:link href="#" onclick="document.forms[0].submit();" styleClass="buttonSave" styleId="buttonSave"><bean:message key="button.save" /></html:link>
				</div>
			</html:form>

<jsp:include page="../../../main_footer.jsp"/>											
											

