<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>


<script language="JavaScript">
<!--
function toggleCheckbox(boxname, trigger) {
	f=document.getElementById('baumassnahmeSearchForm');
	if(f==null) f=document.getElementById('StatisticsFilterForm');
	for( i=0 ; i<f.elements.length ; i++) {
		if (f.elements[i].name==boxname) {
			f.elements[i].checked=trigger.checked;
		}
	}
}

//-->
</script>
 
<%-- %><div class="textcontent_head">
	Arbeitssteuerung
</div>
<div class="textcontent">
	<div class="textcontent_left">--%>
		<div class="box">
				<table border="0">
					<tr>
						<td rowspan="2" style="vertical-align: middle; padding-right: 10px">
							<html:radio property="optionDatumZeitraum" styleClass="checkbox" value="datum"/>
						</td>
						<td style="vertical-align: baseline; padding-right: 5px; text-align: right;">
							<bean:message key="controlling.beginndatum" />
						</td>
						<td>
							<html:text property="controllingBeginnDatum" styleId="controllingBeginnDatum" maxlength="10" styleClass="date" />
							<img src="<c:url value='/static/img/calendar.gif' />" id="buttonControllingBeginnDatum" />
						</td>
						<td rowspan="2" style="vertical-align: middle; padding-left: 20px; padding-right: 10px; text-align: center;"> oder
						</td>
						<td rowspan="2" style="vertical-align: middle;">
							<html:radio property="optionDatumZeitraum" styleClass="checkbox" value="zeitraum"/>
						</td>
						<td style="vertical-align: baseline; padding-right: 5px; padding-left: 20px; text-align: right;">
							<bean:message key="controlling.letzte" />
						</td>
						<td>
							<html:text property="letzteXWochen" styleId="letzteXWochen" maxlength="3" styleClass="date" />
						</td>
						<td style="vertical-align: baseline; padding-right: 5px; padding-left: 5px;">
							<bean:message key="controlling.wochen" />
						</td>
					</tr>
					<tr>
						<td style="vertical-align: baseline; padding-right: 5px; padding-left: 5px;  text-align: right;">
							<bean:message key="controlling.enddatum" />
						</td>
						<td>
							<html:text property="controllingEndDatum" styleId="controllingEndDatum" maxlength="10" styleClass="date" />
							<img src="<c:url value='/static/img/calendar.gif' />" id="buttonControllingEndDatum" />
						</td>
						<td style="vertical-align: baseline; padding-right: 5px; padding-left: 5px; text-align: right;">
							<bean:message key="controlling.naechste" />
						</td>
						<td>
							<html:text property="naechsteXWochen" styleId="naechsteXWochen" maxlength="3" styleClass="date" />
						</td>
						<td style="vertical-align: baseline; padding-right: 5px; padding-left: 5px;">
							<bean:message key="controlling.wochen" />
						</td>
					</tr>
				</table>			
		</div>

		<script type="text/javascript">
		  Calendar.setup(
		    {
		      inputField  : "controllingBeginnDatum",    		     // ID of the input field
		      ifFormat    : "%d.%m.%Y",   							 // the date format
		      button      : "buttonControllingBeginnDatum",       // ID of the button
		      eventName	  : "click"
		    }
		  );
		  Calendar.setup(
		    {
		      inputField  : "controllingEndDatum",       		  // ID of the input field
		      ifFormat    : "%d.%m.%Y",    						// the date format
		      button      : "buttonControllingEndDatum",       // ID of the button
		      eventName   : "click"
		    }
		  );
		</script>
	<%--></div>--%>
	<div class="box">
		<table class="colored" style="margin-top:10px;width:100%;">
			<thead>
				<tr>
					<%--<th class="controlling" title="<bean:message key='baumassnahme.termine.studie.long' />">
						<bean:message key="baumassnahme.termine.studie.short" />
					</th>--%>
					<th class="controlling">
						<bean:message key="baumassnahme.termine.anforderungbbzr.short" />
					</th>
					<th class="controlling">
						<bean:message key="baumassnahme.termine.bbp.biueentwurf" />
					</th>
					<th class="controlling">
						<bean:message key="baumassnahme.termine.bbp.zvfentwurf" />
					</th>
					<%--<th class="controlling">
						<bean:message key="baumassnahme.termine.koordinationsergebnis.short" />
					</th>--%>
					<th class="controlling">
						<bean:message key="baumassnahme.termine.stellungnahmeevu.short" />
					</th>
					<th class="controlling">
						<bean:message key="baumassnahme.termine.gesamtkonzeptbbzr.short" />
					</th>
					<%--<th class="controlling">
						<bean:message key="baumassnahme.termine.biue" />
					</th>--%>
					<th class="controlling">
						<bean:message key="baumassnahme.termine.bbp.zvf" />
					</th>
					<th class="controlling">
						<bean:message key="baumassnahme.termine.masteruebergabeblattpv.short" />
					</th>
					<th class="controlling">
						<bean:message key="baumassnahme.termine.masteruebergabeblattgv.short" />
					</th>
					<th class="controlling">
						<bean:message key="baumassnahme.termine.uebergabeblattpv" />
					</th>
					<th class="controlling">
						<bean:message key="baumassnahme.termine.uebergabeblattgv" />
					</th>
					<th class="controlling">
						<bean:message key="baumassnahme.termine.fplo" />
					</th>
					<th class="controlling">
						<bean:message key="baumassnahme.termine.eingabegfd_z.short" />
					</th>
					<%--<th class="controlling">
						<bean:message key="baumassnahme.termine.ausfaellesev" />
					</th>--%>
					<%--<th class="controlling">
						<bean:message key="baumassnahme.termine.bkonzeptevu" />
					</th>--%>
				</tr>
			</thead>
			<tbody>
				<colgroup>
					<col class="odd" />
					<col class="even" />
					<col class="odd" />
					<col class="even" />
					<col class="odd" />
					<col class="even" />
					<col class="odd" />
					<col class="even" />
					<col class="odd" />
					<col class="even" />
					<col class="odd" />
					<col class="even" />
					<col class="odd" />
					<col class="even" />
					<col class="odd" />
					<%--<col class="even" />
					<col class="odd" />--%>
				</colgroup>
				<tr>
					<%--Studie Grobkonzept: <td class="controlling">
						<html:multibox property="milestones" value="studie" style="width:30px;border:0px;" />
					</td>--%>
					<td class="controlling">
						<html:multibox property="milestones" value="anforderungbbzr" style="width:30px;border:0px;" />
					</td>
					<td class="controlling">
						<html:multibox property="milestones" value="biueentwurf" style="width:30px;border:0px;" />
					</td>
					<td class="controlling">
						<html:multibox property="milestones" value="zvfentwurf" style="width:30px;border:0px;" />
					</td>
					<%--<td class="controlling">
						<html:multibox property="milestones" value="koordinationsergebnis" style="width:30px;border:0px;" />
					</td>--%>
					<td class="controlling">
						<html:multibox property="milestones" value="stellungnahmeevu" style="width:30px;border:0px;" />
					</td>
					<td class="controlling">
						<html:multibox property="milestones" value="gesamtkonzeptbbzr" style="width:30px;border:0px;" />
					</td>
					<%--<td class="controlling">
						<html:multibox property="milestones" value="biue" style="width:30px;border:0px;" />
					</td>--%>
					<td class="controlling">
						<html:multibox property="milestones" value="zvf" style="width:30px;border:0px;" />
					</td>
					<td class="controlling">
						<html:multibox property="milestones" value="masteruebergabeblattpv" style="width:30px;border:0px;" />
					</td>
					<td class="controlling">
						<html:multibox property="milestones" value="masteruebergabeblattgv" style="width:30px;border:0px;" />
					</td>
					<td class="controlling">
						<html:multibox property="milestones" value="uebergabeblattpv" style="width:30px;border:0px;" />
					</td>
					<td class="controlling">
						<html:multibox property="milestones" value="uebergabeblattgv" style="width:30px;border:0px;" />
					</td>
					<td class="controlling">
						<html:multibox property="milestones" value="fplo" style="width:30px;border:0px;" />
					</td>
					<td class="controlling">
						<html:multibox property="milestones" value="eingabegfd_z" style="width:30px;border:0px;" />
					</td>
					<%--<td class="controlling">
						<html:multibox property="milestones" value="ausfaellesev" style="width:30px;border:0px;" />
					</td>--%>
					<%--<td class="controlling">
						<html:multibox property="milestones" value="bkonzeptevu" style="width:30px;border:0px;" />
					</td>--%>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="checkbox">
		<input type="checkbox" class="checkbox" onclick="toggleCheckbox('milestones', this);return true;" /> alle
	</div>
<%--</div>--%>