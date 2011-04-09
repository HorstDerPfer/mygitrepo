<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

			<div class="textcontent_left">
				<display:table
					id="currentEmpfaenger" 
					name="baumassnahme.uebergabeblatt.header.empfaenger" 
					export="false"
					pagesize="10"
					class="colored"
					style="margin-top:5px;" 
					sort="page"
					defaultsort="1">
					<display:column titleKey="ueb.empfaenger" sortable="false" style="">${currentEmpfaenger}</display:column>
				</display:table>
				
				<jsp:include page="viewUebergabeblattBeteiligteRB.jsp"/>
			</div>

			<div class="textcontent_right">
				<div class="box">
					<div class="label"><bean:message key="ueb.sender.name" /></div>
					<div class="show"><bean:write name="baumassnahme" property="uebergabeblatt.header.sender.name" /></div>
				</div>
				<div class="box">
					<div class="label"><bean:message key="ueb.sender.vorname" /></div>
					<div class="show"><bean:write name="baumassnahme" property="uebergabeblatt.header.sender.vorname" /></div>
				</div>
				<div class="box">
					<div class="label"><bean:message key="ueb.sender.strasse" /></div>
					<div class="show"><bean:write name="baumassnahme" property="uebergabeblatt.header.sender.strasse" /></div>
				</div>
				<div class="box">
					<div class="label"><bean:message key="ueb.sender.plz" /></div>
					<div class="show"><bean:write name="baumassnahme" property="uebergabeblatt.header.sender.plz" /></div>
				</div>
				<div class="box">
					<div class="label"><bean:message key="ueb.sender.ort" /></div>
					<div class="show"><bean:write name="baumassnahme" property="uebergabeblatt.header.sender.ort" /></div>
				</div>
				<div class="box">
					<div class="label"><bean:message key="ueb.sender.tel.intern" /></div>
					<div class="show"><bean:write name="baumassnahme" property="uebergabeblatt.header.sender.telefonIntern" /></div>
				</div>
				<div class="box">
					<div class="label"><bean:message key="ueb.sender.tel.extern" /></div>
					<div class="show"><bean:write name="baumassnahme" property="uebergabeblatt.header.sender.telefon" /></div>
				</div>
				<div class="box">
					<div class="label"><bean:message key="ueb.sender.email" /></div>
					<div class="show"><bean:write name="baumassnahme" property="uebergabeblatt.header.sender.email" /></div>
				</div>

			</div>