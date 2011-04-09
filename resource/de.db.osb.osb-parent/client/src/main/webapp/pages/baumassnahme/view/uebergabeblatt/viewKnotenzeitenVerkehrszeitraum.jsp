<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

<%--Zugdaten --%>
<table width="100%">
	<%-- Daten --%>
	<tr><td>
		<table class="colored" style="text-align:center" >
			<thead>
				<tr>
					<th>
						<div style="text-align:left;"><bean:message key="ueb.zug.verkehrstag.first" /></div>
					</th>
					<th>
						<div style="text-align:left;"><bean:message key="ueb.zug.verkehrstag.last" /></div>
					</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>
						<div style="text-align:left;"><bean:write name="knotenzeitenForm" property="ersterVerkehrstag" format="dd.MM.yyyy" /></div>
					</td>
					<td>
						<div style="text-align:left;"><bean:write name="knotenzeitenForm" property="letzterVerkehrstag" format="dd.MM.yyyy" /></div>
					</td>
				</tr>
			</tbody>
		</table></td>
	</tr>
</table>