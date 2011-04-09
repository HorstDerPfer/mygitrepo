<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

<jsp:include page="../../main_head.jsp"/>
<jsp:include page="../../main_path.jsp"/>
<jsp:include page="../../main_menu.jsp"/>

<%-- Öffnet Punkt in Startmenü --%>
<script type="text/javascript">
    openMainMenu('navLink_bob');
</script>
<html:form action="/saveRegelung">
	
	<html:hidden property="baumassnahmeId" styleId="baumassnahmeId" />
	
	<div class="textcontent_head"><bean:message key="bbpmassnahme.stammdatenregelung" arg0="${regelung.regelungId}" /></div>
	<div class="textcontent" style="border-bottom:0px;">
		
		<div class="textcontent_left">
			<div class="box">
				<div class="label"><bean:message key="bbpmassnahme.regid" /></div>
				<div class="show"><bean:write name="regelung" property="regelungId" /></div>
			</div>
			
			<div class="box">
				<div class="label"><bean:message key="bbpmassnahme.bstvon" /></div>						
				<div class="show"><bean:write name="regelung" property="betriebsStelleVon" /></div>
			</div>
			
			<div class="box">
				<div class="label"><bean:message key="bbpmassnahme.bstbis" /></div>						
				<div class="show"><bean:write name="regelung" property="betriebsStelleBis" /></div>
			</div>
			
			<div class="box">
				<div class="label"><bean:message key="bbpmassnahme.streckebbp" /></div>
				<div class="show"><bean:write name="regelung" property="streckeBBP" /></div>
			</div>
			
			<div class="box">
				<div class="label"><bean:message key="bbpmassnahme.streckevzg" /></div>
				<div class="show"><bean:write name="regelung" property="streckeVZG" /></div>
			</div>
			
			<div class="box">
				<div class="label"><bean:message key="bbpmassnahme.beginn" /></div>
				<div class="input">
					<html:text property="beginn" styleId="beginn" styleClass="datetime" maxlength="16" errorStyle="${errorStyle}" />
					<img src="<c:url value='/static/img/calendar.gif' />" id="buttonBeginn" />
				</div>
			</div>
			
			<div class="box">
				<div class="label"><bean:message key="bbpmassnahme.ende" /></div>
				<div class="input">
					<html:text property="ende" styleId="ende" styleClass="datetime" errorStyle="${errorStyle}" />
					<img src="<c:url value='/static/img/calendar.gif' />" id="buttonEnde" />
				</div>
			</div>
		</div>
		
		<div class="textcontent_right">
			<div class="box">
				<div class="label"><bean:message key="bbpmassnahme.lisba" /></div>
				<div class="show"><bean:write name="regelung" property="lisbaNr" /></div>
			</div>
			
			<div class="box">
				<div class="label"><bean:message key="bbpmassnahme.regelvts" /></div>
				<div class="show"><bean:write name="regelung" property="regelVTS" /></div>
			</div>
			
			<div class="box">
				<div class="label"><bean:message key="bbpmassnahme.betriebsweise" /></div>
				<div class="show"><bean:write name="regelung" property="betriebsweise" /></div>
			</div>	
			
			<div class="box">
				<div class="label"><bean:message key="bbpmassnahme.bplarttext" /></div>
				<div class="show"><bean:write name="regelung" property="bplArtText" /></div>
			</div>
			
			<div class="box">
				<div class="label"><bean:message key="bbpmassnahme.sperrkz" /></div>
				<div class="show"><bean:write name="regelung" property="sperrKz" /></div>
			</div>
				
			<div class="box">
				<div class="label"><bean:message key="baumassnahme.beginn.terminberechnung" /></div>
				<c:if test="${regelung.beginnFuerTerminberechnung == true}">
					<div class="input">
						<html:radio property="beginnFuerTerminberechnung" disabled="true" value="true" styleClass="checkbox" />
							<bean:message key="common.true" />
						<html:radio property="beginnFuerTerminberechnung" disabled="true" value="false" styleClass="checkbox" />
							<bean:message key="common.false" />
					</div>
				</c:if>
				<c:if test="${regelung.beginnFuerTerminberechnung == false}">
					<div class="input">
						<html:radio property="beginnFuerTerminberechnung" value="true" styleClass="checkbox" />
							<bean:message key="common.true" />
						<html:radio property="beginnFuerTerminberechnung" value="false" styleClass="checkbox" />
							<bean:message key="common.false" />
					</div>
				</c:if>
			</div>
		</div>
	</div>
	<div class="buttonBar">		
		<html:link href="#" onclick="$(regelungForm).submit();" styleClass="buttonSave" styleId="buttonSave">
			<bean:message key="button.save" />
		</html:link>
		<%-- ZURÜCK-BUTTON --%>
		<html:link action="/back" styleClass="buttonBack">
			<bean:message key="button.back" />
		</html:link>
	</div>
	
	<script type="text/javascript">
		Calendar.setup({
	    	inputField  : "ende",
	    	ifFormat    : "%d.%m.%Y %H:%M",
	    	button      : "buttonEnde",
	    	showsTime	: true
		});
	</script>
			
	<script type="text/javascript">
		Calendar.setup({
	    	inputField  : "beginn",
	    	ifFormat    : "%d.%m.%Y %H:%M",
	    	button      : "buttonBeginn",
	       	showsTime	: true
		});
	</script>
</html:form>