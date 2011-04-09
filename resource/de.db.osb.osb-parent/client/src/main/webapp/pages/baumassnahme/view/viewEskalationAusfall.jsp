<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

<div id="divEskalationAusfall">
	<c:if test="${tab=='EskalationAusfall' or currentUser != null}">
		<div class="textcontent_left">
			<div class="box">
				<div class="label"><bean:message key="baumassnahme.eskalationsbeginn" /></div>
				<div class="show"><bean:write name="baumassnahme" property="eskalationsBeginn" format="dd.MM.yyyy" /></div>
			</div>
			
			<div class="box">
				<div class="label"><bean:message key="baumassnahme.eskalationsentscheidung" /></div>
				<div class="show"><bean:write name="baumassnahme" property="eskalationsEntscheidung" format="dd.MM.yyyy" /></div>
			</div>
			
			<div class="box">
				<div class="label"><bean:message key="baumassnahme.eskalationveto" /></div>						
				<div class="show"><bean:message key="baumassnahme.eskalationveto.${baumassnahme.eskalationVeto}" /></div>
			</div>
			
			<div class="box">
				<div class="label"><bean:message key="baumassnahme.ausfalldatum" /></div>
				<div class="show"><bean:write name="baumassnahme" property="ausfallDatum" format="dd.MM.yyyy" /></div>
			</div>
			
			<div class="box">
				<div class="label"><bean:message key="baumassnahme.ausfallgrund" /></div>
				<div class="show">
					<logic:notEmpty name="baumassnahme" property="ausfallGrund">
						<bean:write name="baumassnahme" property="ausfallGrund.name" />
					</logic:notEmpty>
				</div>
			</div>
	
			<div class="box">
				<div class="label">
					<bean:message key="baumassnahme.bisherigeraufwand" /><br />
					<bean:message key="common.unit.time.hhmm" />
				</div>
				<div class="showMultiLines">
					<bean:write name="baumassnahme" property="bisherigerAufwandTimeString" /><br />&nbsp;
				</div>
			</div>
		</div>
	</c:if>
</div>			