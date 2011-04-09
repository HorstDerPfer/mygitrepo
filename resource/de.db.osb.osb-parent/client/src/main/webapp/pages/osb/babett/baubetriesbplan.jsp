<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--
	Created by IntelliJ IDEA.
	User: hennebrueder
	Date: 24.02.2010
	Time: 12:55:00
	To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="../../main_head.jsp"/>
<jsp:include page="../../main_path.jsp"/>
<jsp:include page="../../main_menu.jsp"/>

<%-- Öffnet Punkt in Startmenü --%>
<script type="text/javascript">
    openMainMenu('navLink_osb_baubetriebsplan');
</script>

<html:errors/>
<h1>Maßnahme</h1>

<table id="baubetriebsplan" class="colored">
    <thead>
    <tr>
        <th><bean:message key="baubetriebsplan-a-label"/></th>
        <th><bean:message key="baubetriebsplan-b-label"/></th>
        <th><bean:message key="baubetriebsplan-c-label"/></th>
        <th><bean:message key="baubetriebsplan-d-label"/><br>
            <html:image src="/bob_osb/osb/baubetriebsplan.do?task=renderChart&header=true" alt="Gant image"/>
        </th>
        <th><div style="float:right;"><bean:message key="baubetriebsplan-e-label-right"/></div><bean:message key="baubetriebsplan-e-label-left"/>
			
			  <div style="text-align:center;"><bean:message key="baubetriebsplan-e-label-2"/></div>
			  <div style="text-align:center;"><bean:message key="baubetriebsplan-e-label-3"/></div></th>
        <th style="text-align:center;"><bean:message key="baubetriebsplan-f-label"/></th>
        <th style="text-align:center;"><bean:message key="baubetriebsplan-g-label"/></th>
        <th><bean:message key="baubetriebsplan-h-label"/></th>

    </tr>
    </thead>
    <tbody>
		<tr>
			<td>SSSSSSSS</td>
			<td>SSSSSSSSSSSS</td>
			<td>SSSSSS</td>
			<td><img height="10px" src="/bob_osb/static/img/spacer.gif" width="366"> </td>
			<td>0000.0SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS</td>
			<td>00.00.0000,00:00-00.00.0000,00:00;SSSSSSSSSSSSSSSS</td>
			<td>SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS</td>
			<td>SSSS</td>
		</tr>
    <c:forEach items="${report.massnahmeReportList}" var="m" varStatus="mi">
        <tr>

            <td rowspan="${fn:length(m.regelungReportList)+1}">${mi.index + 1}<br>
                    ${m.labelA}<br>
                (${m.betraNummer})(${m.fploKennzeichnung})(${m.bildlicheUebersicht})
            </td>
            <td>
                    ${m.labelB}
            </td>
            <td>
                    ${m.labelC}
                <c:if test="${m.statusBbzr == 'NICHT_ERFORDERLICH'}"><html:img
                        src="/bob_osb/static/img/baubetriebsplan/nichtErforderlich.png" title="Nicht erforderlich"
                        alt="Nicht erforderlich icon"/> </c:if>
                <c:if test="${m.statusBbzr == 'ERFORDERLICH'}"><html:img
                        src="/bob_osb/static/img/baubetriebsplan/erforderlich.png" title="erforderlich"
                        alt="erforderlich icon"/> </c:if>
                <c:if test="${m.statusBbzr == 'BEAUFTRAGT'}"><html:img
                        src="/bob_osb/static/img/baubetriebsplan/beauftragt.png" title="Beauftragt" alt="Beauftragt icon"/>
                </c:if>
                <c:if test="${m.statusBbzr == 'ENTWURF_GELIEFERT'}"><html:img
                        src="/bob_osb/static/img/baubetriebsplan/entwurfGeliefert.png" title="Entwurf geliefert"
                        alt="Entwurf geliefert icon"/> </c:if>
                <c:if test="${m.statusBbzr == 'ERLEDIGT'}"><html:img
                        src="/bob_osb/static/img/baubetriebsplan/erledigt.png" title="Erledigt" alt="Erledigt icon"/>
                </c:if>
                <br>
                 <c:choose>
                        <c:when test="${!m.schichtweise && m.durchgehend}">
                            <html:img src="/bob_osb/static/img/baubetriebsplan/nichtErforderlich.png" title="Nicht erforderlich"
                                      alt="Nicht erforderlich icon"/>
                        </c:when>
                        <c:otherwise>
                            <c:if test="${m.schichtweise}"><html:img
                                    src="/bob_osb/static/img/baubetriebsplan/schichten.png" title="Schichten"
                                    alt="Schichten icon"/> </c:if>
                            <c:if test="${!m.durchgehend}"><html:img
                                    src="/bob_osb/static/img/baubetriebsplan/unterbrochen.png" title="Unterbrochene Arbeit"
                                    alt="Unterbrochene Arbeit icon"/> </c:if>
                        </c:otherwise>
                    </c:choose>
            </td>
            <td></td>
            <td><div style="float:right;">${m.finanzInfo}</div>${m.massnahmeId}
						<div>${m.strecke}</div>
						</td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <c:forEach items="${m.regelungReportList}" var="r" varStatus="ri">
            <tr>
							  <td>${r.betroffen}</td>
                <td><c:choose>
                        <c:when test="${!r.schicht && r.durchgehend}">
                            <html:img src="/bob_osb/static/img/baubetriebsplan/nichtErforderlich.png" title="Nicht erforderlich"
                                      alt="Nicht erforderlich icon"/>
                        </c:when>
                        <c:otherwise>
                            <c:if test="${r.schicht}"><html:img src="/bob_osb/static/img/baubetriebsplan/schichten.png"
                                                                title="Schichten" alt="Schichten icon"/> </c:if>
                            <c:if test="${!r.durchgehend}"><html:img src="/bob_osb/static/img/baubetriebsplan/unterbrochen.png"
                                                                     title="Unterbrochene Arbeit"
                                                                     alt="Unterbrochene Arbeit icon"/> </c:if>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <html:image src="/bob_osb/osb/baubetriebsplan.do?task=renderChart&massnahme=${mi.index}&row=${ri.index}"
                                alt="Gant image"/>
                </td>
                <td>${r.strecke}</td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
        </c:forEach>
    </c:forEach>
    </tbody>
</table>

<jsp:include page="../../main_footer.jsp"/>