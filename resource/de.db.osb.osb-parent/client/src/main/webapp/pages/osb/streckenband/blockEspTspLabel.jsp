<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

	<c:choose>
		<c:when test="${currentGleissperrung.richtungsKennzahl == '2'}">&lt;&nbsp;</c:when>
		<c:when test="${currentGleissperrung.richtungsKennzahl == '0'}">&lt;&nbsp;</c:when>
	</c:choose>
	${currentGleissperrung.sperrpausenbedarf}
	<c:choose>
		<c:when test="${currentGleissperrung.richtungsKennzahl == '0'}">&nbsp;&gt;&nbsp;</c:when>
		<c:when test="${currentGleissperrung.richtungsKennzahl == '1'}">&nbsp;&gt;&nbsp;</c:when>
	</c:choose>
	