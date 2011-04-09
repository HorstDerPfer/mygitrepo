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
				
					<%-- Massnahmendaten --%>	
					<logic:iterate id="currentMassnahme" name="baumassnahme" property="aktuelleZvf.massnahmen" indexId="index">
						<c:if test='${index == 0}'>		
							<div class="textcontent_left">
								<div class="box">
									<div class="label"><bean:message key="ueb.massnahme.bbp" /></div>
									<div class="input"><html:text property="zvfBbpStrecke" styleId="bbp" maxlength="5" titleKey="ueb.tip.bbp"/></div>
								</div>
								<div class="box">
									<div class="label"><bean:message key="ueb.massnahme.formularkennung" /></div>
									<div class="input">
										<html:select property="zvfFormularkennung" styleId="formularkennung" >
											<logic:iterate id="currentFormularkennung" name="formularkennungen">
												<html:option value="${currentFormularkennung}">
													${currentFormularkennung}
												</html:option>
											</logic:iterate>
										</html:select>
									</div>
								</div>
								<div class="box">
									<div class="label"><bean:message key="ueb.massnahme.version" /></div>
									<div class="input">
										<html:text property="zvfVersionMajor" styleId="major" styleClass="time" maxlength="2"/>.
										<html:text property="zvfVersionMinor" styleId="minor" styleClass="time" maxlength="2"/>.
										<html:text property="zvfVersionSub" styleId="sub" styleClass="time" maxlength="2"/>
									</div>
								</div>
								<div class="box">
									<div class="label"><label for="uebBaumassnahmenart"><bean:message key="ueb.massnahme.baumassnahmenart" /></label></div>
									<div class="input">
										<html:select property="zvfBaumassnahmenart" styleId="uebBaumassnahmenart" >
											<logic:iterate id="currentArt" name="uebBaumassnahmenarten">
												<html:option value="${currentArt}">
													<bean:message key="ueb.massnahme.art.${currentArt}" />
												</html:option>
											</logic:iterate>
										</html:select>
									</div>
								</div>
								<div class="box">
									<div class="label"><bean:message key="ueb.massnahme.kennung" /></div>
									<div class="input"><html:text property="zvfKennung" styleId="kennung"  /></div>
								</div>
							</div>
							<div class="textcontent_right">
								<div class="box">
									<div class="label"><bean:message key="ueb.massnahme.kigbau" /></div>
									<div class="input"><html:text property="zvfLisbaKigbau" styleId="lisbaKigbau" maxlength="10" /></div>
								</div>
								<div class="box">
									<div class="label"><bean:message key="ueb.massnahme.qs_ks_ves" /></div>
									<div class="input"><html:text property="zvfQsKsVes" styleId="qsKsVes"  /></div>
								</div>
								<div class="box">
									<div class="label"><bean:message key="ueb.massnahme.korridor" /></div>
									<div class="input"><html:text property="zvfKorridor" styleId="korridor" maxlength="5" /></div>
								</div>
								
								<div class="label"><bean:message key="ueb.massnahme.festgelegt" /></div>
								<div class="box">
									<div class="label"><bean:message key="ueb.massnahme.spfv" /></div>
									<div class="input">
										<html:radio property="zvfFestgelegtSPFV" value="true" styleClass="checkbox" />
										<bean:message key="ueb.massnahme.festgelegt.true" />
										<html:radio property="zvfFestgelegtSPFV" value="false" styleClass="checkbox" />
										<bean:message key="ueb.massnahme.festgelegt.false" />
									</div>
								</div>
								<div class="box">
									<div class="label"><bean:message key="ueb.massnahme.spnv" /></div>
									<div class="input">
										<html:radio property="zvfFestgelegtSPNV" value="true" styleClass="checkbox" />
										<bean:message key="ueb.massnahme.festgelegt.true" />
										<html:radio property="zvfFestgelegtSPNV" value="false" styleClass="checkbox" />
										<bean:message key="ueb.massnahme.festgelegt.false" />
									</div>
								</div>
								<div class="box">
									<div class="input">
									<div class="label"><bean:message key="ueb.massnahme.sgv" /></div>
										<html:radio property="zvfFestgelegtSGV" value="true" styleClass="checkbox" />
										<bean:message key="ueb.massnahme.festgelegt.true" />
										<html:radio property="zvfFestgelegtSGV" value="false" styleClass="checkbox" />
										<bean:message key="ueb.massnahme.festgelegt.false" />
									</div>
								</div>
							</div>
							<br>
						</c:if>
					</logic:iterate>