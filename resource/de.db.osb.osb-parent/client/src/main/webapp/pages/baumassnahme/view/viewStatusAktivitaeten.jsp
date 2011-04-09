<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

<div id="divStatusAktivitaeten">
	<c:if test="${tab=='StatusAktivitaeten'}">
		<div class="textcontent_left" style="width:100%;">
			<div class="box">
				<div class="label" style="width:185px;"><bean:message key="baumassnahme.konstruktionseinschraenkung" /></div>
				<div class="show"><bean:write name="baumassnahme" property="konstruktionsEinschraenkung" format="dd.MM.yyyy" /></div>
			</div>
			<div class="box">
				<div class="label" style="width:185px;"><bean:message key="baumassnahme.abstimmungffz" /></div>
				<div class="show"><bean:write name="baumassnahme" property="abstimmungFfz" format="dd.MM.yyyy" /></div>
			</div>
			<div class="box">
				<div class="label" style="width:185px;"><bean:message key="baumassnahme.antragaufhebungdienstruhe" /></div>
				<div class="show"><bean:write name="baumassnahme" property="antragAufhebungDienstruhe" format="dd.MM.yyyy" /></div>
			</div>
			<div class="box">
				<div class="label" style="width:185px;"><bean:message key="baumassnahme.genehmigungaufhebungdienstruhe" /></div>
				<div class="show"><bean:write name="baumassnahme" property="genehmigungAufhebungDienstruhe" format="dd.MM.yyyy" /></div>
			</div>
			<div class="box">
				<div class="label" style="width:185px;"><bean:message key="baumassnahme.betranr" /></div>
				<div class="show"><bean:write name="baumassnahme" property="betraNr" /></div>
			</div>
		</div>
	</c:if>
</div>