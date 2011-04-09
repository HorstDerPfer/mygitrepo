<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
				
<div id="divFahrplan">				
	<c:if test="${tab=='Fahrplan'}">		
		<div style="text-align:center;">
			<jsp:useBean id="urls" class="java.util.HashMap"/>
			<bean:define id="urlFahrplanDetails" toScope="page"><c:url value="/refreshFahrplanDetails.do" /></bean:define>
			
			<display:table
				id="currentZugcharakteristik" 
				name="baumassnahme.zugcharakteristik" 
				export="false"
				requestURI="${requestURI}?tab=Fahrplan"
				pagesize="10"
				sort="list"
				class="colored"
				decorator="db.training.easy.util.displaytag.decorators.AddRowLink" >
			
				<%-- URL wird erzeugt --%>
				<c:set target="${urls}" property="${currentZugcharakteristik.id}" value="#" />
				<c:set target="${urls}" property="${currentZugcharakteristik.id}" >
					<c:url value="refreshFahrplanDetails('${urlFahrplanDetails}','${currentZugcharakteristik.id}')" />
				</c:set>
				
				<display:column property="verkehrstag" titleKey="zugcharakteristik.verkehrstag" sortable="true" format="{0,date,dd.MM.yyyy}" />
				<display:column titleKey="zugcharakteristik.tagwechsel" sortable="true" >
					<logic:equal name="currentZugcharakteristik" property="tagwechsel" value="1">
						${currentZugcharakteristik.tagwechsel }
					</logic:equal>
				</display:column>
				<display:column property="kundennummer" titleKey="zugcharakteristik.kundennummer" sortable="true" />
				<display:column property="zugnr" titleKey="zugcharakteristik.zugnummer" sortable="true" />
				<display:column titleKey="zugcharakteristik.zuggattung" sortable="true" >
					<logic:notEmpty name="currentZugcharakteristik" property="gattungsbezeichnung">
						${currentZugcharakteristik.gattungsbezeichnung} (${currentZugcharakteristik.gattungsnr})
					</logic:notEmpty>
				</display:column>
				<display:column property="startBf" titleKey="zugcharakteristik.startbf" sortable="true" />
				<display:column property="zielBf" titleKey="zugcharakteristik.zielbf" sortable="true" />
	
				<display:setProperty name="basic.empty.showtable" value="true" />
			</display:table>
		</div>				
	</c:if>
</div>