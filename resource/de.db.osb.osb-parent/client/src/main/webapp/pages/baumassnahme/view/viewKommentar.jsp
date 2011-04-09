<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

<div id="divKommentar">
	<c:if test="${tab=='Kommentar'}">
		<div class="show" style="width:99%;height:99%;">
			<logic:empty name="baumassnahme" property="kommentar">
				<bean:message key="common.empty" />
			</logic:empty>
			<logic:notEmpty name="baumassnahme" property="kommentar">
				<bean:write name="baumassnahme" property="kommentar" />
			</logic:notEmpty>
		</div>
	</c:if>
</div>			