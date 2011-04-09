<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"%>

<html:xhtml />
<jsp:include page="/pages/main_head.jsp" />
<jsp:include page="/pages/main_path.jsp" />
<jsp:include page="/pages/main_menu.jsp" />

<table class="colored">
	<thead>
		<tr>
			<th style="width: 88px;">
				MN-Zeile
				<br />
				MN-Art
				<br />
				B F Ü
			</th>
			<th style="width: 132px;">
				Folge N/A
				<br />
				Großmaschine
				<br />
				betroffen
			</th>
			<th style="width: 66px;">
				Gen ZR
				<br />
				Wirk
				<br />
				Wirk
			</th>
			<th style="width: 390px;padding:0">
				<img src="<c:url value='/bbpChart.view?axis' />" />
			</th>
			<th style="width: 390px;">
				Maßnahme ID -- La Lü Finanz....
				<br />
				Maßnahme: Strecke, Ri, Abschnitt
				<br />
				Regelung: Strecke, Ri, Abschnitt
			</th>
			<th style="width: 376px;">
				Spalte F
			</th>
			<th style="width: 352px;">
				Spalte G
			</th>
			<th style="width: 44px;">
				Spalte H
			</th>
		</tr>
	</thead>
	<tbody>
	<tr>
	<td>1</td>
	<td>Sperr</td>
	<td>GZ</td>
	<td>
	</td>
	<td>0101234.6152.10&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;La&nbsp;&nbsp;&nbsp;&nbsp;Invest</td>
	<td>G.016027230</td>
	<td>GE: Umbau Streckengleis</td>
	<td></td>
	</tr>
	<tr>
	<td>A (EI)</td>
	<td></td>
	<td></td>
	<td><img src="<c:url value='/bbpChart.view?mn' />" /></td>
	<td>6153.1 Bln-Rummelsb Vnk - Bln Ostgbf(B7)</td>
	<td>08.01.2010,18:30-18.01.2010,05:30;Fr-Mo</td>
	<td></td>
	<td>X</td>
	</tr>
	<tr>
	<td></td>
	<td>FG</td>
	<td></td>
	<td><img src="<c:url value='/bbpChart.view?gl' />" /></td>
	<td>6153.2 Bln-Rummelsb Vnk - Bln Ostgbf(B7)</td>
	<td>08.01.2010,18:30-18.01.2010,05:30;Fr-Mo</td>
	<td>Ggl Zs 6; Fzv: 2,2 min</td>
	<td></td>
	</tr>
	<tr>
	<td>(&nbsp;)&nbsp;(&nbsp;)&nbsp;(&nbsp;)</td>
	<td>G</td>
	<td></td>
	<td><img src="<c:url value='/bbpChart.view?lf' />" /></td>
	<td>6153.3 Bln-Rummelsb Vnk - Bln Ostgbf(B7)</td>
	<td>08.01.2010,18:30-18.01.2010,05:30;Fr-Mo</td>
	<td>Tsp; Fzv: 0,0 min</td>
	<td>X</td>
	</tr>
	</tbody>
	<tfoot>
		<tr>
			<td>
				SSSSSSSS
			</td>
			<td>
				SSSSSSSSSSSS
			</td>
			<td>
				SSSSSS
			</td>
			<td></td>
			<td>
				0000.0SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
			</td>
			<td>
				.00.0000,00:00-00.00.0000,00:00;SSSSSSSSSSSSSSSS
			</td>
			<td>
				SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
			</td>
			<td>
				SSSS
			</td>
		</tr>
	</tfoot>
</table>

<jsp:include page="/pages/main_footer.jsp" />