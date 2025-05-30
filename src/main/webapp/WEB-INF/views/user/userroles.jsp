<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt"%>
<h2>User Roles</h2>

<!-- Set required URLs with context path -->
 <c:url var="updateUrl" value="/ems/userroles/update" context="${pageContext.request.contextPath}" />
 <c:url var="addUrl" value="/ems/userroles/add" context="${pageContext.request.contextPath}" /> 
 
 <!-- All form validation errors and Success message -->	     
   	<spring:hasBindErrors name="userRolesForm">
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
<center>
<form:form id="roleAddForm" method="post" action="${addUrl}" modelAttribute="userRolesForm"> 
	<div style="display: inline;float: centre">
		<form:label path="roleType">Enter New RoleName</form:label>
		<form:input path="roleType" htmlEscape="true" maxlength="50"/>	
		<p><input type="submit" class="addrole" value="Add Role"></p>
	</div>    
</form:form>
</hr> 

<form:form id="roleManageForm" method="post" action="${updateUrl}" modelAttribute="userRolesForm">

<table border="1">
	<!-- Print the roles name only once  -->
		<tr>
			<th rowspan="2">Page Details</th>
			<th colspan="${existingUserRoles.size()}">Roles</th>
		</tr>
		<tr>
			<c:forEach items="${existingUserRoles}" var="role" varStatus="roleIndex">
				<td>${role.roleType}</td>
				<input type="hidden" name="roleId[${roleIndex.count - 1}]" value="${role.uniqueId}" >			
			</c:forEach>
		</tr>


	<c:forEach items="${pageAccessDetails}" var="page" varStatus="index">
		<tr>
		<td>${page.resourceName}</td> 
		<c:forEach items="${existingUserRoles}" var="role" varStatus="roleIndex">
			<td>
			<input type="hidden" name="config[${roleIndex.count - 1}].bitPosition[${index.count - 1}]" value="${page.bitPosition}"/>  
 			<c:choose>
				<c:when test="${role.isBitPositionSet(role.privileges,page.bitPosition)}">
					<center><input  type="checkbox" name="config[${roleIndex.count - 1}].status[${index.count - 1}]" checked="checked"/></center>								
				</c:when>
				<c:otherwise>
					<center><input  type="checkbox" name="config[${roleIndex.count - 1}].status[${index.count - 1}]"/></center>
				</c:otherwise>
			</c:choose>
			</td>
		</c:forEach>
		</tr>
	 </c:forEach>
	 
	 
 </table> 
 
 </br>
	<div style="display: inline;float: left;width: 30%">
		<input type="submit" class="updaterole" value="Update Role">
	</div>
</form:form>
</center> 
