<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<html:xhtml/>

<jsp:include page="main_head.jsp"/>
<jsp:include page="main_path.jsp"/>
<jsp:include page="main_menu.jsp"/>

<script type="text/javascript">
    openMainMenu('navLink_contact');
</script>

<div class="textcontent_head">Kontakt unter:</div>
<div class="textcontent">
    <p>Wenn Sie Fragen, Anregungen oder Kritik zur Anwendung haben, freuen wir uns auf Ihre <br/>E-Mail. In dringenden Fällen können Sie uns auch telefonisch erreichen.</p>
    
    <table>
        <tr>
            <td width="120"><b>Ansprechpartner:</b></td>
            <td>
            	DB Netz AG<br/>
            	Fahrplan & Kapazitätsmanagement<br/>
            	Prozesse, IT-Landschaft und Projekte Fahrplan (I.NMF 1)<br />
            </td>
        </tr>
        <tr>
            <td><b>E-Mail:</b></td>
            <td><html:link href="mailto:dbnetz-fahrplan-bfportal@deutschebahn.com">dbnetz-fahrplan-bfportal@deutschebahn.com</html:link></td>
        </tr>
        <tr>
            <td><b>Tel.:</b></td>
            <td>069-265-32154</td>
        </tr>
        <tr>
            <td></td>
            <td>(Basa) 955-32154</td>
        </tr>
    </table>
</div>

<jsp:include page="main_footer.jsp"/>