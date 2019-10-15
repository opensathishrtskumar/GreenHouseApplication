<%@ page session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h1>
	<a title="EMS - Hello ${account.getName()}" href="<c:url value="/" />"><img
		src="<c:url value="/resources/logo-header.png" />" alt="EMS" /></a>
</h1>

<!-- TODO : make username displayed in header -->

<div id="nav">
	<ul>
		<li><a href="<c:url value="/ems/dashboard/show" />">Dashboard</a></li>
		<li><a href="<c:url value="/ems/devices/show" />">Device Mangement</a></li>
		<li><a href="<c:url value="/ems/reports" />">Reports</a></li>
		<li><a href="#">User Mangement</a></li>
		<li><a href="<c:url value="/ems/settings" />">Settings</a></li>
		<li><a href="<c:url value="/signout" />">Sign out</a></li>
	</ul>
</div>