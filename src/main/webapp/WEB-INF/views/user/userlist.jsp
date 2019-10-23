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

			<c:forEach items="${userList}" var="user" varStatus="index">
	     <form:form class="updateUserForm" id="updateUser${index.count}" method="post" action="${postUrl}" modelAttribute="userDetailsForm">
				<form:hidden value="" path="uniqueid" />
				<form:textarea class="memoryMapping" value="${user.name}" htmlEscape="true" path="name" />
				<form:input class="memoryMapping" value="${user.email}" htmlEscape="true" path="email" />
				<form:input class="memoryMapping" value="${user.mobile}" htmlEscape="true" path="mobile" />
				<form:select class="memoryMapping" value="${user.mobile}" htmlEscape="true" path="role" />
			<p>
				<input type="submit" value="Update User">
			</p>
		</form:form>
			</c:forEach>

    </div>
  </div>
  
</div>
