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
						<div class="label"><bean:message key="baumassnahme.verzichtqtrassebeantragt" /></div>
						<div class="input"><html:text property="verzichtQTrasseBeantragt" styleId="verzichtQTrasseBeantragt" styleClass="date" maxlength="10" /></div>
					</div>
					
					<div class="box">
						<div class="label"><bean:message key="baumassnahme.verzichtqtrasseabgestimmt" /></div>
						<div class="input"><html:text property="verzichtQTrasseAbgestimmt" styleId="verzichtQTrasseAbgestimmt" styleClass="date" maxlength="10" /></div>
					</div>
					<div class="box">
						<div class="label"><bean:message key="baumassnahme.verzichtqtrassegenehmigt" /></div>
						<div class="input">
							<html:radio property="verzichtQTrasseGenehmigt" value="true" styleClass="checkbox" />
							<bean:message key="baumassnahme.verzichtqtrassegenehmigt.true" />
							<html:radio property="verzichtQTrasseGenehmigt" value="false" styleClass="checkbox" />
							<bean:message key="baumassnahme.verzichtqtrassegenehmigt.false" />
						</div>
					</div>				
				</div>
