<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/easy.tld" prefix="easy"  %>

<div id="divAddMeilenstein">
	<html:select name="editableMeilensteinListItemForm" property="insertMeilensteinBezeichnung" styleId="insertMeilensteinBezeichnungSelect"  styleClass="middle2" errorStyle="${errorStyle }">
		<html:optionsCollection name="meilensteinBezeichnungenList" value="id" label="text"/>
	</html:select>
</div>
