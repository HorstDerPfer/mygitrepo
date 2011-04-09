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
		<logic:equal name="baumassnahme" property="kigBau" value="true">
			<div class="label"><bean:message key="baumassnahme.kigbaunr" /></div>						
			<div class="show">
				<logic:iterate id="currentKigBauNr" name="baumassnahme" property="kigBauNr" indexId="index">
					${currentKigBauNr}<c:if test="${ (fn:length(baumassnahme.kigBauNr) > (index+1)) }">, </c:if>
				</logic:iterate>
			</div>
		</logic:equal>
	</div>	
	
	<logic:equal name="baumassnahme" property="art" value="KS">
		<div class="box">				
			<div class="label"><bean:message key="baumassnahme.korridornr" /></div>
			<div class="show"><bean:write name="baumassnahme" property="korridorNr" /></div>
		</div>
	
		<div class="box">
			<div class="label"><bean:message key="baumassnahme.korridorzeitfenster" /></div>
			<div class="show">
				<logic:iterate id="currentKorridorZeitfenster" name="baumassnahme" property="korridorZeitfenster" indexId="index">
					${currentKorridorZeitfenster}<c:if test="${ (fn:length(baumassnahme.korridorZeitfenster) > (index+1)) }">, </c:if>
				</logic:iterate>
			</div>
		</div>
	</logic:equal>
		
	<logic:equal name="baumassnahme" property="art" value="QS">
		<div class="box">
			<div class="label"><bean:message key="baumassnahme.qsnr" /></div>
			<div class="show" style="height:auto; max-height: 70px;">
				<logic:iterate id="currentQsNr" name="baumassnahme" property="qsNr" indexId="index">
					${currentQsNr}<c:if test="${ (fn:length(baumassnahme.qsNr) > (index+1)) }">, </c:if>
				</logic:iterate>
			</div>
		</div>
	
		<div class="box">	
			<div class="label"><bean:message key="baumassnahme.qsspfv" /></div>						
			<div class="show"><bean:message key="baumassnahme.qsspfv.${baumassnahme.qsSPFV}" /></div>
		</div>
	
		<div class="box">	
			<div class="label"><bean:message key="baumassnahme.qsspnv" /></div>						
			<div class="show"><bean:message key="baumassnahme.qsspnv.${baumassnahme.qsSPNV}" /></div>
		</div>

		<div class="box">	
			<div class="label"><bean:message key="baumassnahme.qssgv" /></div>						
			<div class="show"><bean:message key="baumassnahme.qssgv.${baumassnahme.qsSGV}" /></div>
		</div>
	
	</logic:equal>
</div>

<div class="textcontent_right">
	<div class="box">
		
	</div>
</div>
<div class="box"></div>
			
