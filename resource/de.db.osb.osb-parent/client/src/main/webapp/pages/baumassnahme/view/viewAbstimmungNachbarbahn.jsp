<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

<div id="divAbstimmungNachbarbahn">
	<c:if test="${tab=='AbstimmungNachbarbahn'}">
		<div class="textcontent_left">
			<div class="box">
				<div class="label"><bean:message key="baumassnahme.abstimmungnberforderlich" /></div>
				<div class="show"><bean:message key="baumassnahme.abstimmungnberforderlich.${baumassnahme.abstimmungNbErforderlich}" /></div>
			</div>
			
			<div class="box">
				<div class="label"><bean:message key="baumassnahme.nachbarbahn" /></div>
				<div class="show">
					<logic:notEmpty name="baumassnahme" property="nachbarbahn">
						<bean:write name="baumassnahme" property="nachbarbahn.name" />
					</logic:notEmpty>
				</div>
			</div>
				
			<div class="box">
				<div class="label"><bean:message key="baumassnahme.abstimmungnberfolgtam" /></div>
				<div class="show"><bean:write name="baumassnahme" property="abstimmungNbErfolgtAm" format="dd.MM.yyyy" /></div>
			</div>
		</div>
	</c:if>
</div>
				