<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

				<div class="textcontent_left">
					<jsp:include page="editEmpfaenger.jsp"/>
					<jsp:include page="editEmpfaengerEingabe.jsp"/>
				</div>

				<div class="textcontent_right">
					<div class="box">
						<div class="label"><label for="name"><bean:message key="ueb.sender.name" /></label></div>
						<div class="input"><html:text property="senderName" styleId="name" maxlength="255"/></div>
					</div>
					<div class="box">
						<div class="label"><label for="vorname"><bean:message key="ueb.sender.vorname" /></label></div>
						<div class="input"><html:text property="senderVorname" styleId="vorname" maxlength="255"/></div>
					</div>
					<div class="box">
						<div class="label"><label for="strasse"><bean:message key="ueb.sender.strasse" /></label></div>
						<div class="input"><html:text property="senderStrasse" styleId="strasse" maxlength="255"/></div>
					</div>
					<div class="box">
						<div class="label"><label for="plz"><bean:message key="ueb.sender.plz" /></label></div>
						<div class="input"><html:text property="senderPLZ" styleId="plz" maxlength="5"/></div>
					</div>
					<div class="box">
						<div class="label"><label for="ort"><bean:message key="ueb.sender.ort" /></label></div>
						<div class="input"><html:text property="senderOrt" styleId="ort" maxlength="255"/></div>
					</div>
					<div class="box">
						<div class="label"><label for="telefon"><bean:message key="ueb.sender.tel.extern" /></label></div>
						<div class="input"><html:text property="senderTelefon" styleId="telefon" maxlength="255"/></div>
					</div>
					<div class="box">
						<div class="label"><label for="telefonIntern"><bean:message key="ueb.sender.tel.intern" /></label></div>
						<div class="input"><html:text property="senderTelefonIntern" styleId="telefonIntern" maxlength="255"/></div>
					</div>
					<div class="box">
						<div class="label"><label for="email"><bean:message key="ueb.sender.email" /></label></div>
						<div class="input"><html:text property="senderEmail" styleId="email" maxlength="255"/></div>
					</div>
				</div>