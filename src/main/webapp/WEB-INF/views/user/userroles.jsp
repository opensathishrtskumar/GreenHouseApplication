<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt"%>
<h2>User Roles</h2>

<!-- Set required URLs with context path -->
 <c:url var="postUrl" value="/ems/userroles/manage" context="${pageContext.request.contextPath}" />
 
 <input type="hidden" id="testUrl" value="${testUrl}">
 
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
 
 <!--Iterate existing devices in form, that can be updated/deleted(Soft delete)  -->    
 
<form:form id="roleManageForm" method="post" action="${roleManageUrl}" modelAttribute="userRolesForm">
<table border="1">
	<!-- Print the roles name only once  -->
		<tr>
			<th rowspan="2">Page Details</th>
			<c:forEach items="${existingUserRoles}" var="role" varStatus="roleIndex">
				<c:set var = "roleCount" value = "${roleIndex.count}"/>
			</c:forEach>
			<th colspan="${roleCount}">Roles</th>
		</tr>
		<tr>
		<c:forEach items="${existingUserRoles}" var="role" varStatus="roleIndex">
			<td>${role.roleType}</td>			
		</c:forEach>
		</tr>
<%-- 	</c:if> --%>
	<c:forEach items="${pageAccessDetails}" var="page" varStatus="index">
		<tr>
		<td>${page.resourceName}</td> 
		<c:forEach items="${existingUserRoles}" var="role" varStatus="roleIndex">
			<td> <input type="hidden" path="uniqueId[${page.uniqueId}]" value="${page.uniqueId}"/> 
 			<c:choose>
				<c:when test="${role.isBitPositionSet(role.privileges,page.bitPosition) == true}">
					<center><input  type="checkbox" path="bitPosition[${page.bitPosition}]" value="${page.bitPosition}" checked="checked"/></center>								
				</c:when>
				<c:otherwise>
					<center><input type="checkbox" path="bitPosition[${page.bitPosition}]" value="${page.bitPosition}" /></center>
				</c:otherwise>
			</c:choose>
			</td>
		</c:forEach>
		</tr>
	 </c:forEach>
 </table> 
 <br>
		<div style="display: inline;float: left;width: 30%">
			<input type="submit" class="addUpdatedevice" value="Add/Update Device">
		</div>
</form:form>
 
