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
<html:xhtml/>

<jsp:include page="../../main_head.jsp"/>
<jsp:include page="../../main_path.jsp"/>
<jsp:include page="../../main_menu.jsp"/>

<%-- Öffnet Punkt in Startmenü --%>
<script type="text/javascript">
    openMainMenu('navLink_bob');
</script>

<div class="textcontent_head"><bean:message key="baumassnahme.stammdaten" /></div>
<div class="textcontent" style="border-bottom:0px;">
	<jsp:include page="viewBaumassnahmeStammdaten.jsp"/>
</div>
<div class="buttonBar">
	<%-- BEARBEITEN-BUTTON --%>
	<easy:hasAuthorization model="${baumassnahme}" authorization="ROLE_BAUMASSNAHME_GRUNDDATEN_BEARBEITEN,ROLE_BAUMASSNAHME_BENCHMARK_BEARBEITEN,ROLE_FAVORIT_BEARBEITEN">
		<%-- <html:form action="/editBaumassnahme?sp=true" style="display:inline"> --%>
		<html:form action="/editBaumassnahme?id=${baumassnahme.id}" style="display:inline">
			<input type="hidden" id="tab" name="tab" value="" />
			<html:hidden property="id" value="${baumassnahme.id }" />
			<html:link href="#" onclick="$('baumassnahmeForm').submit();" styleClass="buttonEdit"><bean:message key="button.edit" /></html:link>
		</html:form>
	</easy:hasAuthorization>
	<%-- LÖSCHEN-BUTTON --%>
	<easy:hasAuthorization model="${baumassnahme}" authorization="ROLE_BAUMASSNAHME_GRUNDDATEN_LOESCHEN">
		<bean:define id="confirmText" toScope="page"><bean:message key="confirm.baumassnahme.delete" /></bean:define>
		<html:link action="/deleteBaumassnahme" paramId="id" paramName="baumassnahme" paramProperty="id" styleClass="buttonDelete" onclick="return confirmLink(this.href, '${confirmText}');"><bean:message key="button.delete" /></html:link>
	</easy:hasAuthorization>
	<%-- ZURÜCK-BUTTON --%>
	<html:link action="/suche" styleClass="buttonBack">
		<bean:message key="button.back" />
		<html:param name="method" value="web" ></html:param>
		<html:param name="fromViewBaumassnahmeToList" value="true" ></html:param>
	</html:link>
</div>
<br>

<%-- Karteireiter -----------------------------------------------------------------------------------------------------%>
<bean:define id="urlViewTab" toScope="page"><c:url value="viewBaumassnahme.do" /></bean:define>

<easy:hasAuthorization model="${baumassnahme}" authorization="ROLE_BAUMASSNAHME_GRUNDDATEN_LESEN">
	<div style="width: 100%;">
	    <html:link href="#" onclick="showTabDivBM('JBB');refreshJbb('${urlViewTab}',${baumassnahme.id }, 'JBB', true);" styleId="tabLinkJBB" styleClass="tab_ina">
	    	<bean:message key="baumassnahme.tab.jbb" />
	    </html:link>
	    
   	    <html:link href="#" onclick="showTabDivBM('BBP');" styleId="tabLinkBBP" styleClass="tab_ina">
	    	<bean:message key="baumassnahme.tab.bbp" />
	    </html:link>
	    
	    <html:link href="#" onclick="showTabDivBM('StatusAktivitaeten');refreshStatusAktivitaeten('${urlViewTab}',${baumassnahme.id },'StatusAktivitaeten', true);" styleId="tabLinkStatusAktivitaeten" styleClass="tab_ina">
	    	<bean:message key="baumassnahme.tab.statusaktivitaeten" />
	    </html:link>
	    
	    <html:link href="#" onclick="showTabDivBM('VerzichtQTrasse');refreshVerzichtQTrasse('${urlViewTab}',${baumassnahme.id }, 'VerzichtQTrasse', true);" styleId="tabLinkVerzichtQTrasse" styleClass="tab_ina">
	    	<bean:message key="baumassnahme.tab.verzichtqtrasse" />
	    </html:link>
	    
	    <html:link href="#" onclick="showTabDivBM('AbstimmungNachbarbahn');refreshAbstimmungNachbarbahn('${urlViewTab}',${baumassnahme.id }, 'AbstimmungNachbarbahn', true);" styleId="tabLinkAbstimmungNachbarbahn" styleClass="tab_ina">
	    	<bean:message key="baumassnahme.tab.abstimmungnachbarbahn" />
	    </html:link>
	    
	    <html:link href="#" onclick="showTabDivBM('Benchmark');refreshBenchmark('${urlViewTab}',${baumassnahme.id },'Benchmark' , true);" styleId="tabLinkBenchmark" styleClass="tab_ina">
	    	<bean:message key="baumassnahme.tab.benchmark" />
	    </html:link>
	    
	    <html:link href="#" onclick="showTabDivBM('Aenderungsdokumentation');refreshAenderungsdokumentation('${urlViewTab}',${baumassnahme.id }, 'Aenderungsdokumentation', true);" styleId="tabLinkAenderungsdokumentation" styleClass="tab_ina">
	    	<bean:message key="baumassnahme.tab.aenderungsdokumenation" />
	    </html:link>
		
		<html:link href="#" onclick="showTabDivBM('Kommentar');refreshKommentar('${urlViewTab}',${baumassnahme.id },'Kommentar', true);" styleId="tabLinkKommentar" styleClass="tab_ina">
			<bean:message key="baumassnahme.tab.kommentar" />
		</html:link>
		
		<html:link href="#" onclick="showTabDivBM('EskalationAusfall');refreshEskalationAusfall('${urlViewTab}',${baumassnahme.id }, 'EskalationAusfall', true);" styleId="tabLinkEskalationAusfall" styleClass="tab_ina">
			<bean:message key="baumassnahme.tab.eskalationausfall" />
		</html:link>
		
		<html:link href="#" onclick="showTabDivBM('Uebergabeblatt');refreshUebergabeblatt('${urlViewTab}',${baumassnahme.id }, false, 'Uebergabeblatt', true);" styleId="tabLinkUebergabeblatt" styleClass="tab_ina">
			<bean:message key="baumassnahme.tab.uebergabeblatt" />
		</html:link>			
		
		<html:link href="#" onclick="showTabDivBM('Zvf');refreshZvf1('${urlViewTab}',${baumassnahme.id },'Zvf', false, true);" styleId="tabLinkZvf" styleClass="tab_ina">
			<bean:message key="baumassnahme.tab.zvf" />
		</html:link>			
		
		<html:link href="#" onclick="showTabDivBM('Fahrplan');refreshFahrplan('${urlViewTab}',${baumassnahme.id }, 'Fahrplan', true);" styleId="tabLinkFahrplan" styleClass="tab_ina">
			<bean:message key="baumassnahme.tab.fahrplan" />
		</html:link>
		
		<html:link href="#" onclick="showTabDivBM('Bearbeiter');refreshBearbeiter('${urlViewTab}',${baumassnahme.id }, 'Bearbeiter', true);" styleId="tabLinkBearbeiter" styleClass="tab_ina">
			<bean:message key="baumassnahme.tab.bearbeiter" />
		</html:link>
		
	</div>
</easy:hasAuthorization>
<easy:hasNotAuthorization model="${baumassnahme}" authorization="ROLE_BAUMASSNAHME_GRUNDDATEN_LESEN">
	<div style="width: 100%;">
		<html:link href="#" onclick="showTabDivBM('Benchmark');refreshBenchmark('${urlViewTab}',${baumassnahme.id },'Benchmark' , true);" styleId="tabLinkBenchmark" styleClass="tab_ina">
	    	<bean:message key="baumassnahme.tab.benchmark" />
	    </html:link>
		
		<html:link href="#" onclick="showTabDivBM('EskalationAusfall');refreshEskalationAusfall('${urlViewTab}',${baumassnahme.id }, 'EskalationAusfall', true);" styleId="tabLinkEskalationAusfall" styleClass="tab_ina">
			<bean:message key="baumassnahme.tab.eskalationausfall" />
		</html:link>
		
		<html:link href="#" onclick="showTabDivBM('Uebergabeblatt');refreshUebergabeblatt('${urlViewTab}',${baumassnahme.id }, false, 'Uebergabeblatt', true);" styleId="tabLinkUebergabeblatt" styleClass="tab_ina">
			<bean:message key="baumassnahme.tab.uebergabeblatt" />
		</html:link>			
		
		<html:link href="#" onclick="showTabDivBM('Zvf');refreshZvf1('${urlViewTab}',${baumassnahme.id },'Zvf', false, true);" styleId="tabLinkZvf" styleClass="tab_ina">
			<bean:message key="baumassnahme.tab.zvf" />
		</html:link>			
		
		<html:link href="#" onclick="showTabDivBM('Bearbeiter');refreshBearbeiter('${urlViewTab}',${baumassnahme.id }, 'Bearbeiter', true);" styleId="tabLinkBearbeiter" styleClass="tab_ina">
			<bean:message key="baumassnahme.tab.bearbeiter" />
		</html:link>
	</div>
</easy:hasNotAuthorization>

<div id="tabDivJBB" style="display:none;">
	<jsp:include page="viewJbb.jsp"/>
</div>

<div id="tabDivBBP" style="display:none;">
	<jsp:include page="viewBBPmassnahme.jsp"/>
</div>

<div class="textcontent" id="tabDivStatusAktivitaeten" style="display:none;">
	<jsp:include page="viewStatusAktivitaeten.jsp"/>
</div>

<div class="textcontent" id="tabDivEskalationAusfall" style="display:none;">
	<jsp:include page="viewEskalationAusfall.jsp"/>
</div>

<div class="textcontent" id="tabDivKommentar" style="display:none;" >
	<jsp:include page="viewKommentar.jsp"/>
</div>

<div class="textcontent" id="tabDivAenderungsdokumentation" style="display:none;">
	<jsp:include page="viewAenderungsdokumentation.jsp"/>
</div>

<div class="textcontent" id="tabDivVerzichtQTrasse" style="display:none;">
	<jsp:include page="viewVerzichtQTrasse.jsp"/>
</div>

<div class="textcontent" id="tabDivBenchmark" style="display:none;">
	<jsp:include page="viewBenchmark.jsp"/>
</div>

<div class="textcontent" id="tabDivAbstimmungNachbarbahn" style="display:none;">
	<jsp:include page="viewAbstimmungNachbarbahn.jsp"/>
</div>

<div id="tabDivUebergabeblatt" style="display:none;">
	<jsp:include page="uebergabeblatt/viewUebergabeblatt.jsp"/>
</div>

<div id="tabDivZvf" style="display:none;">
	<jsp:include page="zvf/viewZvf.jsp"/>
</div>

<div id="tabDivFahrplan" class="textcontent" style="display:none;">
	<jsp:include page="viewFahrplan.jsp"/>
	<br>
	<jsp:include page="viewFahrplanDetails.jsp"/>
</div>

<div id="tabDivBearbeiter" class="textcontent" style="display:none;">
	<jsp:include page="viewBearbeiter.jsp"/>
</div>

<script type="text/javascript">
	<easy:hasAuthorization model="${baumassnahme}" authorization="ROLE_BAUMASSNAHME_GRUNDDATEN_LESEN">
		showTabDivBM("${ ((tab != null) ? tab : 'JBB') }");
	</easy:hasAuthorization>
	<easy:hasNotAuthorization model="${baumassnahme}" authorization="ROLE_BAUMASSNAHME_GRUNDDATEN_LESEN">
		showTabDivBM("${ ((tab != null && (tab == 'EskalationAusfall' || tab == 'Uebergabeblatt' || tab == 'Zvf' || tab == 'Bearbeiter')) ? tab : 'Benchmark') }");
	</easy:hasNotAuthorization>
</script>

<jsp:include page="../../main_footer.jsp"/>