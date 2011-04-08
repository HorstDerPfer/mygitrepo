<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<html>
<head><title><bean:write name="helloStrutsForm" property="message" /></title></head>
<body><H2><bean:write name="helloStrutsForm" property="message" /></H2>
</body>
</html>