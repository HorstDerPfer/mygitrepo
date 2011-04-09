<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

<div id="divVerzichtQTrasse">
	<c:if test="${tab=='VerzichtQTrasse'}">
		<div class="textcontent_left">
			<div class="box">
				<div class="label"><bean:message key="baumassnahme.verzichtqtrassebeantragt" /></div>
				<div class="show"><bean:write name="baumassnahme" property="verzichtQTrasseBeantragt" format="dd.MM.yyyy" /></div>
			</div>
				
			<div class="box">
				<div class="label"><bean:message key="baumassnahme.verzichtqtrasseabgestimmt" /></div>
				<div class="show"><bean:write name="baumassnahme" property="verzichtQTrasseAbgestimmt" format="dd.MM.yyyy" /></div>
			</div>
				
			<div class="box">
				<div class="label"><bean:message key="baumassnahme.verzichtqtrassegenehmigt" /></div>
				<div class="show">
					<logic:notEmpty name="baumassnahme" property="verzichtQTrasseGenehmigt">						
						<bean:message key="baumassnahme.verzichtqtrassegenehmigt.${baumassnahme.verzichtQTrasseGenehmigt}" />
					</logic:notEmpty>
				</div>
			</div>
		</div>
</c:if>
</div>