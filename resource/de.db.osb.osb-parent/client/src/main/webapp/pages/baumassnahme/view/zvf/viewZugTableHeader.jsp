<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>								

<thead>
	<tr>
		<th rowspan="2">
			<div style="text-align:center;"><bean:message key="ueb.zug.lfdnr" /></div>
		</th>
		<th rowspan="2">
			<div style="text-align:center;"><bean:message key="ueb.zug.datum" /></div>
		</th>
		<c:if test="${abweichungstyp==artUmleitung }">
			<th rowspan="2">
				<div style="text-align:center;"><bean:message key="ueb.zug.tageswechsel" /></div>
			</th>
		</c:if>
		<th rowspan="2">
			<div style="text-align:center;"><bean:message key="ueb.zug.zuggattung" /></div>
		</th>
		<th rowspan="2">
			<div style="text-align:center;"><bean:message key="ueb.zug.zugnr" /></div>
		</th>
		<c:if test="${abweichungstyp==artUmleitung }">
			<th rowspan="2">
				<div style="text-align:center;"><bean:message key="ueb.zug.bedarf" /></div>
			</th>
		</c:if>
		<th rowspan="2">
			<div style="text-align:center;"><bean:message key="ueb.zug.abgangsbhf" /></div>
		</th>
		<th rowspan="2">
			<div style="text-align:center;"><bean:message key="ueb.zug.zielbhf" /></div>
		</th>
		<th>
			<div style="text-align:center;"><bean:message key="common.more" /></div>
		</th>
	</tr>
</thead>