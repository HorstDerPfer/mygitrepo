<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

			<div class="textcontent_left">
				<div class="box">
					<div class="label"><bean:message key="baumassnahme.eskalationsbeginn" /></div>
					<div class="input"><html:text property="eskalationsBeginn" styleId="eskalationsBeginn" styleClass="date" maxlength="10" /></div>
				</div>
				
				<div class="box">
					<div class="label"><bean:message key="baumassnahme.eskalationsentscheidung" /></div>
					<div class="input"><html:text property="eskalationsEntscheidung" styleId="eskalationsEntscheidung" styleClass="date" maxlength="10" /></div>
				</div>
					
				<div class="box">
					<div class="label"><bean:message key="baumassnahme.eskalationveto" /></div>
					<div class="input">
						<html:radio property="eskalationVeto" value="true" styleClass="checkbox" />
						<bean:message key="baumassnahme.eskalationveto.true" />
						<html:radio property="eskalationVeto" value="false" styleClass="checkbox" />
						<bean:message key="baumassnahme.eskalationveto.false" />
					</div>
				</div>				
			
				<div class="box">
					<div class="label"><bean:message key="baumassnahme.ausfalldatum" /></div>
					<div class="input"><html:text property="ausfallDatum" styleId="ausfallDatum" styleClass="date" maxlength="10" /></div>
				</div>

				<div class="box">
					<div class="label"><bean:message key="baumassnahme.ausfallgrund" /></div>
					<div class="input">
						<html:select property="ausfallGrund" styleId="ausfallGrund" >
							<html:option value=""><bean:message key="common.select.option" /></html:option>
							<html:optionsCollection name="gruende" value="id" label="name"/>
						</html:select>
					</div>
				</div>

				<div class="box">
					<div class="label">
						<bean:message key="baumassnahme.bisherigeraufwand" /><br />
						<bean:message key="common.unit.time.hhmm" />
					</div>
					<div class="input">
						<html:text property="bisherigerAufwand" styleId="bisherigerAufwand" styleClass="time" maxlength="9"/>
					</div>
				</div>
			</div>
