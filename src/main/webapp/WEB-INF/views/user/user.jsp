<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<h2>User Management</h2>

<ul id="connectedAccounts" class="listings">
	<li class="listing">
		<a href="<c:url value="/ems/user/show" />">User Management</a>
	</li>
	<li class="listing">		
		<a href="<c:url value="/ems/userroles/show" />">Role Management</a>	
	</li>
</ul>
