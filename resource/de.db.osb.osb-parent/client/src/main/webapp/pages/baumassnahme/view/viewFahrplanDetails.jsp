<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
				
<div id="divFahrplanDetails" style="text-align:center;">
	<display:table
		id="currentFahrplan" 
		name="fahrplan" 
		export="false"
		requestURI="${requestURI}?tab=Fahrplan"
		pagesize="20"
		sort="list"
		class="colored" >
	
		<display:column property="lfd" titleKey="fahrplan.lfd" sortable="true"  />
		<display:column property="bst" titleKey="fahrplan.bst" sortable="true" />
		<display:column property="ankunft" titleKey="fahrplan.ankunft" sortable="true" />
		<display:column property="abfahrt" titleKey="fahrplan.abfahrt" sortable="true" />
		<display:column property="haltart" titleKey="fahrplan.haltart" sortable="true" />
		<display:column property="vzg" titleKey="fahrplan.vzg" sortable="true" />
		<display:column property="zugfolge" titleKey="fahrplan.zugfolge" sortable="true" />
		
		<display:setProperty name="basic.empty.showtable" value="true" />
	</display:table>
</div>
				