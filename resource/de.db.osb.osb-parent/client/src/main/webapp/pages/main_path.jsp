<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy" %>
<html:xhtml/>

    <!-- graue zeile fuer Hauptnavigation-->
    <div id="navigation_oben">
	   	<div id="navigation_oben_links">
	    	<p></p>
	    </div>
	    <div id="navigation_oben_rechts">
    		<p>
		    	<logic:present name="loginUser">
	    			<bean:message key="common.loginUser" />:
	    			<bean:write name="loginUser" property="caption" />
	    			(<bean:write name="loginUser" property="regionalbereich.name" />)
					&nbsp;-&nbsp;
					<bean:message key="common.fahrplanjahr" />:
	        		<bean:write name="session_fahrplanjahr" />
	        		<easy:hasRole role="ADMINISTRATOR_ZENTRAL">
	        			[Regionale Berechtigungen:
	        			<c:choose><c:when test="${!empty regionalrechtEnabled && regionalrechtEnabled == true}"><b>an</b></c:when><c:otherwise>aus</c:otherwise></c:choose>.]
	        		</easy:hasRole>
		    	</logic:present>
    		</p>
    	</div>
    </div>
    
    <!-- Pfadanzeige -->
    <div id="pfadanzeige">
        <div id="pfadanzeige_icon"></div>
        <div id="pfadanzeige_datum">
       		<jsp:useBean id="dateToday" class="java.util.Date"/>
        	<p><bean:write format="dd.MM.yyyy" name="dateToday"/></p>
        </div>
		<div id="pfadanzeige_text"></div>
	</div>