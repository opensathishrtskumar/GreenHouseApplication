<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:importAttribute name="scripts"/>
<tiles:importAttribute name="styles"/>

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html>
<html>
<head>
	<title><tiles:insertAttribute name="title" defaultValue="EMS" /></title>
	<meta http-equiv="content-type" content="text/html;charset=utf-8" />	
	<link rel="stylesheet" href="<c:url value="/resources/page.css" />" type="text/css" media="screen" />
	<c:forEach var="style" items="${styles}">
		<link rel="stylesheet" href="<c:url value="${style}" />" type="text/css" media="all" />
	</c:forEach>
	<c:forEach var="meta" items="${metadata}">
		<meta name="${meta.key}" content="${meta.value}"/> 
	</c:forEach>
	<script type="text/javascript" src="<c:url value="/resources/jquery/3.3.1/jquery-3.4.1.min.js" />"></script>
	<script type="text/javascript" src="<c:url value="/resources/jquery-cookie/1.0/jquery-cookie.js" />"></script>
</head>
<body>
  	<div id="header">
		<tiles:insertAttribute name="header" />
	</div>
	<div id="content-container">
		
		<c:set var="contentVar"><tiles:getAsString name="content" defaultValue="empty"/></c:set>
		<c:set var="extendedcontentVar"><tiles:getAsString name="extendedcontent" defaultValue="empty"/></c:set>
		
		<c:if test="${contentVar != null && contentVar != 'empty'}">
			<div id="content">
				<tiles:insertAttribute name="content" ignore="true" />
			</div>
		</c:if>		
	
		<c:if test="${extendedcontentVar != null && extendedcontentVar != 'empty'}">
			<div id="extendedcontent">
				<tiles:insertAttribute name="extendedcontent"  ignore="true"/>
			</div>		
		</c:if>
		
		<div id="footer">
			<tiles:insertAttribute name="footer" />
		</div>
	</div>
	
	<c:forEach var="script" items="${scripts}">
		<script type="text/javascript" src="<c:url value="${script}" />"></script>	
	</c:forEach>
	<script type="text/javascript">
		$.cookie('ems.timeZoneOffset', new Date().getTimezoneOffset() * 60000);
	</script>
</body>
</html>