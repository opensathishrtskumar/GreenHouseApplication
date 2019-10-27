<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<h2>User Management</h2>

<!-- <div id="accordion">
	First accordion with Form to Test and Add new device
  <div class="group">
 -->
<h3>Add New User</h3>
<div style="width: 100%">
 <c:url var="postUrl" value="/ems/user/add" context="${pageContext.request.contextPath}" />
 <c:url var="updateUrl" value="/ems/user/update" context="${pageContext.request.contextPath}" />
    <form:form  id="addUser" method="post" action="${postUrl}" modelAttribute="userDetailsForm">
    	<!-- devicemanagement check for error -->
    	<spring:hasBindErrors name="userDetailsForm">
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
	<fieldset>
		<form:label path="name">Name of the User</form:label>
		<form:input path="name" htmlEscape="true" maxlength="50" />
		
		<form:label path="emailID">Email ID</form:label>
		<form:input path="emailID" 	htmlEscape="true" maxlength="50"/>
		
		<form:label path="password">Password</form:label>
		<form:input path="password" type="password" htmlEscape="true" maxlength="50"/>
		
		<form:label path="roleID">Role Type</form:label>
		<form:select path="roleID" items="${roleList}" title="Available Role Type listed" ></form:select>
		
		<form:label path="mobileNumber">Mobile Number</form:label>
		<form:input path="mobileNumber" htmlEscape="true" maxlength="50"/>
		
	</fieldset>
	<p>
		<input type="submit" value="Add User">
	</p>
</form:form>
</div>
<hr>
	<c:forEach items="${existingUserDetails}" var="user" varStatus="index">				     
	     <form:form class="" id="updateUser${index.count}" method="post" action="${updateUrl}" modelAttribute="userDetailsForm">
		<!-- All form validation errors and Success message -->	     
				<form:hidden value="${user.id}" path="uniqueId" />
<div style="width: 100%;">
	<fieldset>
			<div style="display: inline;width: 100%">
				<div style="display: inline;float: left;width: 20%">
					<form:input class="sameline" value="${user.name}" htmlEscape="true" path="name" />
				</div>
				<div style="display: inline;float: left;width: 30%">
					<form:input class="sameline" disabled="true" value="${user.emailId}" htmlEscape="true" path="emailID" />
				</div>
				<div style="display: inline;float: left;width: 20%">
					<form:input class="sameline" value="${user.mobileNumber}" htmlEscape="true" path="mobileNumber" />
				</div>
				<div style="display: inline;float: left;width: 20%">	
				<form:select class="sameline" path="roleID">
				    <c:forEach items="${existingUserRoles}" var="roles" varStatus="status">
				        <c:choose>
				            <c:when test="${roles.uniqueId eq user.roleId}">
				                <option value="${roles.uniqueId}" selected="true">${roles.roleType}</option>
				            </c:when>
				            <c:otherwise>
				                <option value="${roles.uniqueId}">${roles.roleType}</option>
				            </c:otherwise>
				        </c:choose> 
				    </c:forEach>
				</form:select>
				</div>
				<div style="display: inline;float: left;width: 10%">
					<input class = "sameline" type="submit" value="Update User">
				</div>
			</div>
			</fieldset>
			</div>
		</form:form>
	</c:forEach>

<!--   </div>
</div>
 -->