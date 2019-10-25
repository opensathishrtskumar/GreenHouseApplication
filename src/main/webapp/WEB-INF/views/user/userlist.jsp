<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<h2>User Management</h2>

<div id="accordion">
	
	<!-- First accordion with Form to Test and Add new device -->
  <div class="group">
    <h3>Add New User</h3>
    <div>
    	 <c:url var="postUrl" value="/ems/user/add" context="${pageContext.request.contextPath}" />
	     <form:form id="addUser" method="post" action="${postUrl}" modelAttribute="userDetailsForm">
	     	<!-- devicemanagem check for error -->
	     
			<c:if test="${param.msg != null}">
				<div style="display: inline;float: left;width: 100%;align-content: center;" class="success">${param.msg}</div>
			</c:if>			
			<fieldset>
				<form:label path="name">Name of the User<form:errors path="name" cssClass="error" /></form:label>
				<form:input path="name" htmlEscape="true" maxlength="50" />
				
				<form:label path="emailID">Email ID<form:errors path="emailID" cssClass="error" /></form:label>
				<form:input path="emailID" 	htmlEscape="true" maxlength="50"/>
				
				<form:label path="password">Password<form:errors path="password" cssClass="error" /></form:label>
				<form:input path="password" type="password" htmlEscape="true" maxlength="50"/>
				
				<form:label path="roleID">Role Type<form:errors path="roleID" cssClass="error" /></form:label>
				<form:select path="roleID" items="${roleList}" title="Available Role Type listed" ></form:select>
				
				<form:label path="mobileNumber">Mobile Number<form:errors path="mobileNumber" cssClass="error" /></form:label>
				<form:input path="mobileNumber" htmlEscape="true" maxlength="50"/>
				
			</fieldset>
			<p>
				<input type="submit" value="Add User">
			</p>
		</form:form>
		<hr>

		<c:forEach items="${existingUserDetails}" var="user" varStatus="index">
		     <form:form class="" id="updateUser${index.count}" method="post" action="${postUrl}" modelAttribute="userDetailsForm">
			<!-- All form validation errors and Success message -->	     
	     	<spring:hasBindErrors name="userDetailsForm">
	    	 	<div class="error">
					<c:forEach var="error" items="${errors.allErrors}">
						<b><spring:message message="${error}" /></b>
						<br />
					</c:forEach>
				</div>
		    </spring:hasBindErrors>				     
					<form:hidden value="${user.id}" path="uniqueId" />
				<fieldset>
					<div style="display: inline;width: 100%">
						<div style="display: inline;float: left;width: 40%">
							<form:input class="user" value="${user.name}" htmlEscape="true" path="name" />
						</div>
						<div style="display: inline;float: left;width: 50%">
							<form:input class="email" disabled="true" value="${user.emailId}" htmlEscape="true" path="emailID" />
						</div>
					</div>
					<div style="display: inline;width: 100%">
						<div style="display: inline;float: left;width: 50%">
							<form:input class="mobile" value="${user.mobileNumber}" htmlEscape="true" path="mobileNumber" />
						</div>
						<div style="display: inline;float: left;width: 40%">	
							<form:input class="role" value="${user.roleId}" htmlEscape="true" path="roleID" />
						</div>
					</div>
					<div>
						<input type="submit" value="Update User">
					</div>
				</fieldset>
			</form:form>
		</c:forEach>

    </div>
  </div>
  
</div>
