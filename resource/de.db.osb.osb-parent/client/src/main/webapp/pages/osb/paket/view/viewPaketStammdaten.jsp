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
			<div class="label"><bean:message key="paket.regionalbereich" /></div>
			<div class="show"><bean:write name="paket" property="regionalbereich.name" /></div>
		</div>	
		<div class="box">
			<div class="label"><bean:message key="paket.name" /></div>
			<div class="show"><bean:write name="paket" property="kurzname" /></div>
		</div>		
	</div>
	
	<div class="textcontent_right">
		<div class="box">
			<div class="label"><bean:message key="paket.lfd" /></div>
			<div class="show"><bean:write name="paket" property="lfdNr" /></div>
		</div>		
	</div>
