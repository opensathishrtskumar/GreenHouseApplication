<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<h2>Manage Dashboard displayed devices</h2>


<form>
	 <fieldset>
	 <!-- All form validation errors and Success message -->	     
	   	<spring:hasBindErrors name="dashboardMngmtForm">
	  	 	<div class="error">
				<c:forEach var="error" items="${errors.allErrors}">
					<b><spring:message message="${error}" /></b>
					<br />
				</c:forEach>
			</div>
	   </spring:hasBindErrors>
	
		<c:if test="${param.msg != null}">
			<div style="display: inline;float: left;width: 100%;align-content: center;" class="success">${param.msg}</div>
		</c:if>
	 </fieldset>
 </form>

<c:url var="updateDashboardUrl" value="/ems/dashboardmngmt/update" context="${pageContext.request.contextPath}" />

<form:form id="dashboardMngmtForm" method="post" action="${updateDashboardUrl}" modelAttribute="dashboardMngmtForm">

	<c:forEach items="${deviceList}" var="device" >
	
		<div><c:out value="${device.deviceName}"></c:out></div>
	
	</c:forEach>

</form:form>


