<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
<html:xhtml/>

<div class="textcontent_right" style="margin-top:4px;">
	<fieldset style="margin-left:-10px;width:461px;">
		<legend><bean:message key="fahrplanregelung.legend.fahrplanregelung" /></legend>
	
		<table class="colored" style="border:0px;">
			<tr>
				<td></td>
				<th class="right" style="border:1px solid white;"><bean:message key="fahrplanregelung.legend.fahrplanregelung.spfv" /></th>
				<th class="right" style="border:1px solid white;"><bean:message key="fahrplanregelung.legend.fahrplanregelung.spnv" /></th>
				<th class="right" style="border:1px solid white;"><bean:message key="fahrplanregelung.legend.fahrplanregelung.sgv" /></th>
			</tr>
			<tr class="evenrow">
				<th style="border:1px solid white;"><bean:message key="fahrplanregelung.legend.fahrplanregelung.ausfaelle" /></th>
				<td class="right" style="border:1px;"><bean:write name="fahrplanregelung" property="totalAusfallSpfv" /></td>
				<td class="right" style="border:1px;"><bean:write name="fahrplanregelung" property="totalAusfallSpnv" /></td>
				<td class="right" style="border:1px;"><bean:write name="fahrplanregelung" property="totalAusfallSgv" /></td>
			</tr>
			<tr class="oddrow">
				<th style="border:1px solid white;"><bean:message key="fahrplanregelung.legend.fahrplanregelung.umleitungen" /></th>
				<td class="right" style="border:1px;"><bean:write name="fahrplanregelung" property="anzahlSpfv" format="#,##0" /></td>
				<td class="right" style="border:1px;"><bean:write name="fahrplanregelung" property="anzahlSpnv" format="#,##0" /></td>
				<td class="right" style="border:1px;"><bean:write name="fahrplanregelung" property="anzahlSgv" format="#,##0" /></td>
			</tr>
		</table>
		
		<br/>
		
		<div class="box">
			<div class="label"><bean:message key="fahrplanregelung.legend.fahrplanregelung.umleitungswege" /></div>
			<div class="show right" style="width:50px"><bean:write name="fahrplanregelung" property="anzahlUmleitungswege" format="#,##0" /></div>
		</div>
	</fieldset>
</div>
