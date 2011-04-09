<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

		<div class="textcontent_left" style="width:100%;">
			<div class="box">
				<div class="label" style="width:185px;"><label for="konstruktionsEinschraenkung"><bean:message key="baumassnahme.konstruktionseinschraenkung" /></label></div>
				<div class="input"><html:text property="konstruktionsEinschraenkung" styleId="konstruktionsEinschraenkung" styleClass="date" maxlength="10" /></div>
			</div>
			<div class="box">
				<div class="label" style="width:185px;"><label for="abstimmungFfz"><bean:message key="baumassnahme.abstimmungffz" /></label></div>
				<div class="input"><html:text property="abstimmungFfz" styleId="abstimmungFfz" styleClass="date" maxlength="10" /></div>
			</div>
			<div class="box">
				<div class="label" style="width:185px;"><label for="antragAufhebungDienstruhe"><bean:message key="baumassnahme.antragaufhebungdienstruhe" /></label></div>
				<div class="input"><html:text property="antragAufhebungDienstruhe" styleId="antragAufhebungDienstruhe" styleClass="date" maxlength="10" /></div>
			</div>
			<div class="box">
				<div class="label" style="width:185px;"><label for="genehmigungAufhebungDienstruhe"><bean:message key="baumassnahme.genehmigungaufhebungdienstruhe" /></label></div>
				<div class="input"><html:text property="genehmigungAufhebungDienstruhe" styleId="genehmigungAufhebungDienstruhe" styleClass="date" maxlength="10" /></div>
			</div>
			<!-- 
				<div class="box">
					<div class="label" style="width:185px;"><label for="biUeNr"><bean:message key="baumassnahme.biuenr" /></label></div>
					<div class="input"><html:text property="biUeNr" styleId="biUeNr" maxlength="20"/></div>
				</div>
			 -->
			 <div class="box">
				<div class="label" style="width:185px;"><label for="betraNr"><bean:message key="baumassnahme.betranr" /></label></div>
				<div class="input"><html:text property="betraNr" styleId="betraNr" /></div>
			</div>
		</div>
