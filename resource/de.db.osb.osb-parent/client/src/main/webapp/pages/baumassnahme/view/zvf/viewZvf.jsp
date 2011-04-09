<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

<bean:define id="urlZvf" toScope="page"><c:url value="viewBaumassnahme.do" /></bean:define>
<div id="divZvf">
	<c:if test="${tab=='Zvf'}">
		<div class="textcontent">
			<logic:present name="viewZvf">
				<div class="box">
					<div class="label"><bean:message key="bbzr.versionen" /></div>
					<div class="input">
						<html:select name="baumassnahmeForm" property="zvf" styleId="zvfSelect" onchange="refreshZvf2('${urlZvf}', ${baumassnahme.id}, this.value, 'Zvf', false, true);">
							<html:optionsCollection name="baumassnahme" property="zvf" value="id" label="version"/>
						</html:select>
					</div>
				</div>
				${zvfSelect}
			
				<jsp:include page="viewZvfHeader.jsp"/>
				<jsp:include page="viewZvfMassnahme.jsp"/>
				<jsp:include page="viewZvfZuege.jsp"/>
				
			</logic:present>
			<logic:notPresent name="viewZvf">		
				<bean:message key="info.zvf.none" />
			</logic:notPresent>
		</div>	
	</c:if>
	
	<logic:present name="viewZvf">
		<c:if test="${tab=='Zvf'}">
			<c:if test="${baumassnahmeForm.showZuegeZvf == false}">
				<div class="buttonBar" id="showZuegeButtonZvf" style="display:block;">
					<html:link href="#" styleClass="buttonNext" onclick="refreshZvf2('${urlZvf}', ${baumassnahme.id }, $F('zvfSelect'), 'Zvf', true, true);">
						<bean:message key="zvf.showzuege" />
					</html:link>
				</div>
			</c:if>
		</c:if>
	</logic:present>
</div>