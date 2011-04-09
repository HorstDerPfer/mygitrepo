<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>
					
<%-- Zugtabellen --%>
<div id="divZvfZuege">
	<c:if test="${tab=='Zvf'}">
		<logic:present name="viewZvf">

			<c:if test="${baumassnahmeForm.showZuegeZvf == true}">
				
				<bean:define id="artUmleitung" toScope="request"><bean:message key="ueb.zug.abweichung.umleitung"/></bean:define>
				<bean:define id="artVerspaetung" toScope="request"><bean:message key="ueb.zug.abweichung.verspaetung"/></bean:define>
				<bean:define id="artAusfall" toScope="request"><bean:message key="ueb.zug.abweichung.ausfall"/></bean:define>
				<bean:define id="artVorplan" toScope="request"><bean:message key="ueb.zug.abweichung.vorplan"/></bean:define>
				<bean:define id="artGesperrt" toScope="request"><bean:message key="ueb.zug.abweichung.gesperrt"/></bean:define>
				<bean:define id="artErsatzhalte" toScope="request"><bean:message key="ueb.zug.abweichung.ersatzhalte"/></bean:define>
				<bean:define id="artRegelung" toScope="request"><bean:message key="ueb.zug.abweichung.regelung"/></bean:define>
				
				<%-- Umzuleitende Züge --%>
				<div class="box"> 
					<bean:define id="zugCollection" toScope="request" name="UMLEITUNG"></bean:define>
					<c:if test="${!empty zugCollection}">
						<fieldset>
					    	<legend><bean:message key="zvf.umleitzuege" /></legend>
							<bean:define toScope="request" id="bbzrnr" value="${0}"/>
							<bean:define id="abweichungstyp" toScope="request"><bean:message key="ueb.zug.abweichung.umleitung"/></bean:define>
							<jsp:include page="viewZugTable.jsp"/>
						</fieldset>
						<br></br>
					</c:if>
				</div>
			
				<%-- Verspätungen --%>
				<div class="box">
					<bean:define id="zugCollection" toScope="request" name="VERSPAETUNG"></bean:define>
					<c:if test="${!empty zugCollection}">
						<fieldset>
					    	<legend><bean:message key="zvf.verspaetung" /></legend>
							<bean:define toScope="request" id="bbzrnr" value="${0}"/>
							<bean:define id="abweichungstyp" toScope="request"><bean:message key="ueb.zug.abweichung.verspaetung"/></bean:define>
							<jsp:include page="viewZugTable.jsp"/>
						</fieldset>
						<br></br>
					</c:if>
				</div>
			
				<%-- Ausfälle --%>
				<div class="box">
					<bean:define id="zugCollection" toScope="request" name="AUSFALL"></bean:define>
					<c:if test="${!empty zugCollection}">
						<fieldset>
					    	<legend><bean:message key="zvf.ausfall" /></legend>
							<bean:define toScope="request" id="bbzrnr" value="${0}"/>
							<bean:define id="abweichungstyp" toScope="request"><bean:message key="ueb.zug.abweichung.ausfall"/></bean:define>
							<jsp:include page="viewZugTable.jsp"/>
						</fieldset>
						<br></br>
					</c:if>
				</div>
			
				<%-- Vorplanfahrten --%>
				<div class="box">
					<bean:define id="zugCollection" toScope="request" name="VORPLAN"></bean:define>
					<c:if test="${!empty zugCollection}">
						<fieldset>
					    	<legend><bean:message key="zvf.vorplan" /></legend>
							<bean:define toScope="request" id="bbzrnr" value="${0}"/>
							<bean:define id="abweichungstyp" toScope="request"><bean:message key="ueb.zug.abweichung.vorplan"/></bean:define>
							<jsp:include page="viewZugTable.jsp"/>
						</fieldset>
						<br></br>
					</c:if>
				</div>
			
				<%-- Ausfall von Verhehrshalten --%>
				<div class="box">
					<bean:define id="zugCollection" toScope="request" name="ERSATZHALTE"></bean:define>
					<c:if test="${!empty zugCollection}">
						<fieldset>
					    	<legend><bean:message key="zvf.ersatzhalte" /></legend>
							<bean:define toScope="request" id="bbzrnr" value="${0}"/>
							<bean:define id="abweichungstyp" toScope="request"><bean:message key="ueb.zug.abweichung.ersatzhalte"/></bean:define>
							<jsp:include page="viewZugTable.jsp"/>
						</fieldset>
						<br></br>
					</c:if>
				</div>
			
				<%-- Sperren von Bedarfsplänen --%>
				<div class="box">
					<bean:define id="zugCollection" toScope="request" name="GESPERRT"></bean:define>
					<c:if test="${!empty zugCollection}">
						<fieldset>
					    	<legend><bean:message key="zvf.gesperrt" /></legend>
							<bean:define toScope="request" id="bbzrnr" value="${0}"/>
							<bean:define id="abweichungstyp" toScope="request"><bean:message key="ueb.zug.abweichung.gesperrt"/></bean:define>
							<jsp:include page="viewZugTable.jsp"/>
						</fieldset>
						<br></br>
					</c:if>
				</div>
				
			<%-- Andere zugbezogene Regelungen --%>
				<div class="box">
					<bean:define id="zugCollection" toScope="request" name="REGELUNG"></bean:define>
					<c:if test="${!empty zugCollection}">
						<fieldset>
					    	<legend><bean:message key="zvf.regelung" /></legend>
							<bean:define toScope="request" id="bbzrnr" value="${0}"/>
							<bean:define id="abweichungstyp" toScope="request"><bean:message key="ueb.zug.abweichung.regelung"/></bean:define>
							<jsp:include page="viewZugTable.jsp"/>
						</fieldset>
						<br></br>
					</c:if>
				</div>
			</c:if>
			

		</logic:present>
	</c:if>
</div>