<%@ page session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<h3>Reports Configuration</h3>

<form>
	<fieldset>
		<c:if test="${not empty message}">
			<div class="${message.type.cssClass}">${message.text}</div>
		</c:if>	
	</fieldset>
</form>

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