<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

<bean:define id="urlGEVU" toScope="page"><c:url value="refreshGEVU.do" /></bean:define>
<table>
	<tbody>
		<tr style="border-bottom:0px;">
			<td style="width:70%;">
				<div class="box">
					<div class="label">
						<label for="gevuKundengruppe">
							<bean:message key="baumassnahme.termine.evu" />
						</label>
						<img src="<c:url value='./static/img/indicator.gif' />" id="gevuIndicator" style="display:none;" />
					</div>
					<div class="input">
						<input type="text" name="gevuKundengruppe" id="gevuKundengruppe" maxlength="30" />
						<input type="hidden" name="gevuId" id="gevuId" />
						<html:hidden property="gevuLinkedIds" styleId="gevuLinkedIds" />
						<div id="gevuSelect" class="autocomplete"></div>
						<%-- <a href="javascript:addGEVU('${urlGEVU}');" class="plusBig">&nbsp;</a> --%>
						<c:set var="confirmDelete"><bean:message key="confirm.evu.delete" /></c:set>
						<c:set var="requiredMessage"><bean:message key="baumassnahme.termine.required" /></c:set>
						<a href="#" onclick="insertEVURow('tbody_gevu', $('gevuId').value, $('gevuKundengruppe').value, '${baumassnahme.art}', '${requiredMessage}', '${confirmDelete}');$('gevuKundengruppe').value='';return false;" class="plusBig">&nbsp;</a>
					</div>
				</div>
				
				<script type="text/javascript">
					var urlAutocomplete = "<c:url value='/AutoCompleteEvuGruppe.view'/>";
					new Ajax.Autocompleter("gevuKundengruppe", "gevuSelect", urlAutocomplete, { indicator: 'gevuIndicator', minChars: 2, paramName: 'keyword', afterUpdateElement: getGevuId });
				</script>
			</td>
		</tr>
	</tbody>
</table>