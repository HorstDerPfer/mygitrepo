<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
			
<div class="box">
	<bean:define toScope="request" id="zvfStyleClassZug" value="oddrow"/>
	<div id="divZugZvf">
		<jsp:useBean id="addZugParamMap" class="java.util.HashMap">
			<c:set target="${addZugParamMap}" property="id" value="${baumassnahme.id}" />
			<c:set target="${addZugParamMap}" property="type" value="ZVF" />
		</jsp:useBean>
		<c:if test="${baumassnahmeForm.showZuegeZvf == true}">
			<fieldset>
		    	<legend><bean:message key="zvf.umleitzuege" /></legend>
				<bean:define toScope="request" id="zvfnr" value="${0}"/>
				<bean:define id="abweichungstyp" toScope="request"><bean:message key="ueb.zug.abweichung.umleitung"/></bean:define>
				<c:set target="${addZugParamMap}" property="abweichung" value="${abweichungstyp}" />
				<jsp:include page="editZuege.jsp"/>
				<html:link action="/addZug" name="addZugParamMap" styleClass="plusBig" titleKey="button.create">&nbsp;</html:link>
			</fieldset>
			<br>
			<fieldset>
		    	<legend><bean:message key="zvf.verspaetung" /></legend>
				<bean:define toScope="request" id="zvfnr" value="${0}"/>
				<bean:define id="abweichungstyp" toScope="request"><bean:message key="ueb.zug.abweichung.verspaetung"/></bean:define>
				<c:set target="${addZugParamMap}" property="abweichung" value="${abweichungstyp}" />
				<jsp:include page="editZuege.jsp"/>
				<html:link action="/addZug" name="addZugParamMap" styleClass="plusBig" titleKey="button.create">&nbsp;</html:link>
			</fieldset>
			<br>
			<fieldset>
				<legend><bean:message key="zvf.ausfall" /></legend>
				<bean:define toScope="request" id="zvfnr" value="${0}"/>
				<bean:define id="abweichungstyp" toScope="request"><bean:message key="ueb.zug.abweichung.ausfall"/></bean:define>
				<c:set target="${addZugParamMap}" property="abweichung" value="${abweichungstyp}" />
				<jsp:include page="editZuege.jsp"/>
				<html:link action="/addZug" name="addZugParamMap" styleClass="plusBig" titleKey="button.create">&nbsp;</html:link>
			</fieldset>
			<br>
			<fieldset>
				<legend><bean:message key="zvf.vorplan" /></legend>
				<bean:define toScope="request" id="zvfnr" value="${0}"/>
				<bean:define id="abweichungstyp" toScope="request"><bean:message key="ueb.zug.abweichung.vorplan"/></bean:define>
				<c:set target="${addZugParamMap}" property="abweichung" value="${abweichungstyp}" />
				<jsp:include page="editZuege.jsp"/>
				<html:link action="/addZug" name="addZugParamMap" styleClass="plusBig" titleKey="button.create">&nbsp;</html:link>
			</fieldset>
			<br>
			<fieldset>
				<legend><bean:message key="zvf.ersatzhalte" /></legend>
				<bean:define toScope="request" id="zvfnr" value="${0}"/>
				<bean:define id="abweichungstyp" toScope="request"><bean:message key="ueb.zug.abweichung.ersatzhalte"/></bean:define>
				<c:set target="${addZugParamMap}" property="abweichung" value="${abweichungstyp}" />
				<jsp:include page="editZuege.jsp"/>
				<html:link action="/addZug" name="addZugParamMap" styleClass="plusBig" titleKey="button.create">&nbsp;</html:link>
			</fieldset>
			<br>
			<fieldset>
				<legend><bean:message key="zvf.gesperrt" /></legend>
				<bean:define toScope="request" id="zvfnr" value="${0}"/>
				<bean:define id="abweichungstyp" toScope="request"><bean:message key="ueb.zug.abweichung.gesperrt"/></bean:define>
				<c:set target="${addZugParamMap}" property="abweichung" value="${abweichungstyp}" />
				<jsp:include page="editZuege.jsp"/>
				<html:link action="/addZug" name="addZugParamMap" styleClass="plusBig" titleKey="button.create">&nbsp;</html:link>
			</fieldset>
			<br>
			<fieldset>
				<legend><bean:message key="zvf.regelung" /></legend>
				<bean:define toScope="request" id="zvfnr" value="${0}"/>
				<bean:define id="abweichungstyp" toScope="request"><bean:message key="ueb.zug.abweichung.regelung"/></bean:define>
				<c:set target="${addZugParamMap}" property="abweichung" value="${abweichungstyp}" />
				<jsp:include page="editZuege.jsp"/>
				<html:link action="/addZug" name="addZugParamMap" styleClass="plusBig" titleKey="button.create">&nbsp;</html:link>
			</fieldset>
		</c:if>
		<br>
	</div>
</div>