<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<c:if test="${activeTab == 'editMassnahmeGleissperrung'}">
	<display:column property="lfdNr" title="lfdNr" sortable="false" />
</c:if>
<display:column property="vzgStrecke.nummer" titleKey="regelung.vzgStrecke" sortable="false" />
<display:column property="bstVon.caption" titleKey="regelung.bstVon" sortable="false" />
<display:column property="bstVon.caption" titleKey="regelung.bstBis" sortable="false" />
<display:column property="zeitVon" titleKey="regelung.zeitVon" sortable="false" format="{0,date,dd.MM.yyyy HH:mm}" />
<display:column property="zeitBis" titleKey="regelung.zeitBis" sortable="false" format="{0,date,dd.MM.yyyy HH:mm}" />
