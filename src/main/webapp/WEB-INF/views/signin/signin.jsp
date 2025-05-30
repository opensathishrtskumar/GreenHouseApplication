<%@ page session="false"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${not empty message}">
	<div class="${message.type.cssClass}">${message.text}</div>
</c:if>

<form id="signin" action="<c:url value="/signin/authenticate" />"
	method="post">
	
	<div class="formInfo">
		<h2>Sign In</h2>
		<c:if test="${not empty param['error']}">
			<div class="error">Your sign in information was incorrect. Please try again.</div>
		</c:if>
	</div>
	
	<fieldset>
		<label for="login">Email ID</label> 
		<input id="login" name="j_username" type="text" size="25" autocorrect="off"	autocapitalize="off" />
		<label for="password">Password</label> 
		<input id="password" name="j_password" type="password" size="25" />
		
		<p><button type="submit">Sign In</button></p>
		
		<p><a href="#">Forgot your password?</a></p>
			
	</fieldset>
	
</form>
