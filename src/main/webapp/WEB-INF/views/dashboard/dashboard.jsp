<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<h2>Dashboard</h2>

<h3>TODO : Displays number of devices, failed devices, company name, address and so on</h3>

<ul id="connectedAccounts" class="listings">
	<li class="listing">
		<a href="<c:url value="/ems/dashboard/chartview" />">Live Monitor</a>	
	</li>
</ul>
