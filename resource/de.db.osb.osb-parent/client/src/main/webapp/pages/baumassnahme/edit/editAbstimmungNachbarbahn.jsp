<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

				<div class="textcontent_left">
					<div class="box">
						<div class="label"><bean:message key="baumassnahme.abstimmungnberforderlich" /></div>
						<div class="input">
							<html:radio property="abstimmungNbErforderlich" value="true" styleClass="checkbox" />
							<bean:message key="baumassnahme.abstimmungnberforderlich.true" />
							<html:radio property="abstimmungNbErforderlich" value="false" styleClass="checkbox" />
							<bean:message key="baumassnahme.abstimmungnberforderlich.false" />
						</div>
					</div>
	
					<div class="box">
						<div class="label"><bean:message key="baumassnahme.nachbarbahn" /></div>
						<div class="input">
							<html:select property="nachbarbahn" styleId="nachbarbahnName" >
								<html:option value=""><bean:message key="common.select.option" /></html:option>
								<html:optionsCollection name="nachbarbahnen" value="id" label="name"/>
							</html:select>
						</div>
					</div>
				
					<div class="box">
						<div class="label"><bean:message key="baumassnahme.abstimmungnberfolgtam" /></div>
						<div class="input"><html:text property="abstimmungNbErfolgtAm" styleId="abstimmungNbErfolgtAm" styleClass="date" maxlength="10" /></div>
					</div>
				</div>
				
