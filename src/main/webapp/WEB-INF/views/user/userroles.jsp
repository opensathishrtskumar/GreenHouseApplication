<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

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
 <c:forEach items="${existingUserRoles}" var="role" varStatus="index">
	<form:hidden path="uniqueId" value="${role.uniqueId}"/>
<%-- 	<form:hidden path="pageName" value="${page.name}"/>
	<form:hidden path="pageURL" value="${page.url}"/>
	<form:hidden path="pageBitPosition" value="${page.bitPosition}"/> --%>
	<div style="display: inline;width: 100%">
		<form:label path="roleType">Enter/Select Role Name</form:label>
		<form:input path="roleType" value="${role.roleType}" disabled="true" htmlEscape="true" maxlength="30" />
	</div>	   
	<fieldset>
		<div style="display: inline;width: 100%">
<%-- 			<div style="display: inline;float: left;width: 30%">
				<!-- TODO: fetch page details to append -->
				<form:label path="uniqueId">S.No.</form:label>
				<form:input path="uniqueId" value="${role.uniqueID}" disabled="true" />
			</div> --%>
			
			<div style="display: inline;float: left;width: 30%">
				<!-- TODO: fetch page details to append -->
				<form:label path="privileges">Page Name</form:label>
				<form:input path="privileges" value="${role.privileges}" disabled="true" title="Page Privileges" />
			</div>
		</div>
		
<%-- 		<div style="display: inline;width: 100%">
			<div style="display: inline;float: left;width: 30%">
				<form:label path="enabled">Enabled</form:label>
				<c:choose>
					<c:when test="${role.status == 1}">
						<form:checkbox path="enabled" title="Enabled Page Access" value="${status}" checked="checked"/>								
					</c:when>
					<c:otherwise>
						<form:checkbox path="enabled" title="Enable Page Access"/>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
 --%>	</fieldset>
 </c:forEach>
<div style="display: inline;width: 100%">
	<div style="display: inline;float: left;width: 30%">
		<input type="submit" class="addUpdatedevice" value="Add/Update Device">
	</div>
</div> 
</form:form>
 
