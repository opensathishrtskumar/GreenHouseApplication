<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<h2>Management</h2>

<ul id="menu" class="listings">
	<li class="listing">
		<a href="<c:url value="/ems/user" />">Mange Users / Roles</a>
	</li>
	<li class="listing">
		<a href="<c:url value="/ems/devices/show" />">Mange Devices</a>
	</li>
	
	<li class="listing">
		<a href="<c:url value="/ems/devices/show" />">Mange Schedules</a>
	</li>
	
</ul>
