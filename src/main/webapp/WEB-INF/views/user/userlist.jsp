<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<h2>User Management</h2>

<div id="accordion">
	
	<!-- First accordion with Form to Test and Add new device -->
  <div class="group">
    <h3>Add New Device</h3>
    <div>
    	 <c:url var="postUrl" value="/ems/user/add" context="${pageContext.request.contextPath}" />
	     <form:form id="addUser" method="post" action="${postUrl}" modelAttribute="userDetailsForm">
			<fieldset>
				<form:label path="name">Name of the User<form:errors path="name" cssClass="error" /></form:label>
				<form:input path="name" htmlEscape="true" maxlength="50" />
				
				<form:label path="userId">User ID<form:errors path="userId" cssClass="error" /></form:label>
				<form:input path="userId" title="Unique user identifier in given Group(Maximum can be 214)" htmlEscape="true" maxlength="3"/>
				
				<form:label path="emailID">Email ID<form:errors path="emailID" cssClass="error" /></form:label>
				<form:input path="emailID" 	htmlEscape="true" maxlength="50"/>
				
				<form:label path="password">Password<form:errors path="encoding" cssClass="error" /></form:label>
				<form:input path="password" htmlEscape="true" maxlength="50"/>
				
				<form:label path="roleType">Role Type<form:errors path="roleType" cssClass="error" /></form:label>
				<form:select path="role" items="${formDetails.comRoleList}" title="Available Role Type listed" ></form:select>
				
				<form:label path="mobileNumber">Mobile Number<form:errors path="enabled" cssClass="error" /></form:label>
				<form:input path="mobileNumber" htmlEscape="true" maxlength="50"/>
				
			</fieldset>
			<p>
				<input type="submit" value="Add User">
			</p>
		</form:form>
    </div>
  </div>
  
  <!--Iterate existing devices in form, that can be updated/deleted(Soft delete)  -->
  <div class="group">
    <h3>View and Edit User</h3>
    <div>
    	 <c:url var="postUrl" value="/ems/user/edit" context="${pageContext.request.contextPath}" />
	     <form:form id="editUser" method="post" action="${postUrl}" modelAttribute="userDetailsForm">
			<fieldset>
				<form:label path="name">Name of the User<form:errors path="name" cssClass="error" /></form:label>
				<form:input path="name" htmlEscape="true" maxlength="50" />
				
				<form:label path="userId">User ID<form:errors path="userId" cssClass="error" /></form:label>
				<form:input path="userId" title="Unique user identifier in given Group(Maximum can be 214)" htmlEscape="true" maxlength="3"/>
				
				<form:label path="emailID">Email ID<form:errors path="emailID" cssClass="error" /></form:label>
				<form:input path="emailID" 	htmlEscape="true" maxlength="50"/>
				
				<form:label path="password">Password<form:errors path="encoding" cssClass="error" /></form:label>
				<form:input path="password" htmlEscape="true" maxlength="50"/>
				
				<form:label path="roleType">Role Type<form:errors path="roleType" cssClass="error" /></form:label>
				<form:select path="role" items="${formDetails.comRoleList}" title="Available Role Type listed" ></form:select>
				
				<form:label path="mobileNumber">Mobile Number<form:errors path="enabled" cssClass="error" /></form:label>
				<form:input path="mobileNumber" htmlEscape="true" maxlength="50"/>
				
			</fieldset>
			<p>
				<input type="submit" value="Edit User">
			</p>
		</form:form>
    </div>
    <div>
<%--     	 <c:url var="postUrl" value="/ems/user/view" context="${pageContext.request.contextPath}" />
	     <form:form id="viewUser" method="post" action="${postUrl}" modelAttribute="userDetailsShow">
			<fieldset>
				<form:label path="name">Name of the User<form:errors path="name" cssClass="error" /></form:label>
				<form:input path="name" htmlEscape="true" maxlength="50" />
				
				<form:label path="userId">User ID<form:errors path="userId" cssClass="error" /></form:label>
				<form:input path="userId" title="Unique user identifier in given Group(Maximum can be 214)" htmlEscape="true" maxlength="3"/>
				
				<form:label path="emailID">Email ID<form:errors path="emailID" cssClass="error" /></form:label>
				<form:input path="emailID" 	htmlEscape="true" maxlength="50"/>
				
				<form:label path="password">Password<form:errors path="encoding" cssClass="error" /></form:label>
				<form:input path="password" htmlEscape="true" maxlength="50"/>
				
				<form:label path="roleType">Role Type<form:errors path="roleType" cssClass="error" /></form:label>
				<form:select path="role" items="${formDetails.comRoleList}" title="Available Role Type listed" ></form:select>
				
				<form:label path="mobileNumber">Mobile Number<form:errors path="enabled" cssClass="error" /></form:label>
				<form:input path="mobileNumber" htmlEscape="true" maxlength="50"/>
				
			</fieldset>
			<p>
				<input type="submit" value="Edit User">
			</p>
		</form:form> --%>
    </div>    
  </div>
  
</div>
