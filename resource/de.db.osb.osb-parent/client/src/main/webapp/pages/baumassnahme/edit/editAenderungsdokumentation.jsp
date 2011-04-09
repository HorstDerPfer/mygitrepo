<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

		<div id="divAenderung">
			<div id="divAenderungTabelle">
				<bean:define id="urlAenderung" toScope="page"><c:url value="refreshAenderung.do" /></bean:define>
				<display:table
					id="currentAenderung" 
					name="baumassnahme.aenderungen" 
					export="false"
					pagesize="20"
					class="colored"
					style="margin-top:5px;" 
					sort="page"
					defaultsort="1">
					
					<c:set var="aufwandTimeStringTitle"><bean:message key="baumassnahme.aufwand" />&nbsp;<bean:message key="common.unit.time.hhmm" /></c:set>
					
					<display:column property="aenderungsNr" titleKey="baumassnahme.aenderungsNr" sortable="true" style="width:15%;"/>
					<display:column property="grund.name" titleKey="baumassnahme.grund" sortable="false" style="width:70%;"/>
					<display:column property="aufwandTimeString" title="${aufwandTimeStringTitle}" sortable="false" style="text-align:right;"/>
					<display:column style="text-align:right;width:30px" sortable="false">
						<bean:define id="confirmText" toScope="page"><bean:message key="confirm.aenderung.delete" /></bean:define>
						<html:link href="#" onclick="javascript:if(confirmLink(this.href, '${confirmText}')) removeAenderung('${urlAenderung}','${currentAenderung.id}');" styleClass="delete" titleKey="button.delete">&nbsp;</html:link>
					</display:column>
				
					<display:setProperty name="basic.empty.showtable" value="true" />
					<display:setProperty name="paging.banner.item_name"><bean:message key="baumassnahme.aenderung" /></display:setProperty>
					<display:setProperty name="paging.banner.items_name"><bean:message key="baumassnahme.aenderungen" /></display:setProperty>
				</display:table>
			</div>
			<div style="color:red;font-weight:bold;">
				<logic:notEmpty name="errorKey">
					<bean:message key="${errorKey}" />
				</logic:notEmpty>
			</div>
		</div>	
		
		