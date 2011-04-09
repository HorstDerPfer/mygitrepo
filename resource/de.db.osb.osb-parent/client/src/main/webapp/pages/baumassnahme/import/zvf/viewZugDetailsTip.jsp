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
		
<bean:define toScope="page" id="evenOdd" value="${evenOdd=='oddrow'?'evenrow':'oddrow'}"/>

<logic:iterate id="currentMassnahme" name="zvfFile" property="massnahmen" indexId="index">
	<logic:iterate id="currentZug" name="currentMassnahme" property="zug" indexId="i">
		<c:if test="${currentZug.abweichung.art ==abweichungstyp }">
			<div id="tooltipp_${i}" style="width:730px;display: none;">
				<table class="colored">
					<thead>
						<tr>
							<th>
								<div style="text-align:center;"><bean:message key="ueb.zug.bemerkung" /></div>
							</th> 
						</tr>
					</thead>
					<tbody>
						<tr class="${evenOdd}">
							<td>
								<div style="text-align:center"><bean:write name="currentZug" property="bemerkung"  /></div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<script type="text/javascript">
				var content = $('tooltipp_${i}').cloneNode(true).show();
				new Tip('tipp_${i}',
					content,
					{
						title: '<bean:message key="ueb.zug.zugnrhead" arg0="${currentZug.zugnr}" />',
						viewport: true 
					}); 
			</script>
		</c:if>
	</logic:iterate>
</logic:iterate>
									