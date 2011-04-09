<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

			<bean:define id="urlKnotenzeiten" toScope="page"><c:url value="refreshKnotenzeiten.do" /></bean:define>
			<table class="colored" style="border-top:0px;">
				<colgroup>
					<col width="*" />
				</colgroup>
				<tbody>
					<tr style="border-bottom:0px;">
						<td style="text-align:left;">
							<%--<html:link href="javascript:addKnotenzeiten('${urlKnotenzeiten}','${knotenzeitenForm.zugIds}');" styleClass="plusBig" titleKey="button.create">&nbsp;</html:link>--%>
							<bean:define id="add" value="true"></bean:define>
							<html:link href="#" onclick="callSubmit();"  styleClass="plusBig">&nbsp;</html:link>
						</td>
					</tr>
				</tbody>
			</table>
			
			<script type="text/javascript" language="JavaScript">
					function callSubmit(){
						document.forms[0].add.value = true;
						document.forms[0].submit();
					}
				</script>
			