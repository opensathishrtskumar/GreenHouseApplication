<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<h2>Device Manager</h2>
<%-- 
<form:form id="resetPassword" method="post" modelAttribute="changePasswordForm">
	<div class="formInfo">
		<h2>Add / Remove / Update - Devices</h2>
	</div>
	<fieldset>
		<form:label path="oldPassword">Current Password<form:errors
				path="oldPassword" cssClass="error" />
		</form:label>
		<form:password path="oldPassword" />

		<form:label path="password">Password (at least 6 characters) <form:errors
				path="password" cssClass="error" />
		</form:label>
		<form:password path="password" />
		<form:label path="confirmPassword">Confirm Password<form:errors
				path="confirmPassword" cssClass="error" /></form:label>
		<form:password path="confirmPassword" />
		<input type="hidden" value="${token}" />
	</fieldset>
	<p>
		<button type="submit">Reset</button>
	</p>
</form:form> --%>

<div id="accordion">
  <div class="group">
    <h3>Section 1</h3>
    <div>
      <p>Mauris mauris ante, blandit et, ultrices a, suscipit eget, quam. Integer ut neque. Vivamus nisi metus, molestie vel, gravida in, condimentum sit amet, nunc. Nam a nibh. Donec suscipit eros. Nam mi. Proin viverra leo ut odio. Curabitur malesuada. Vestibulum a velit eu ante scelerisque vulputate.</p>
    </div>
  </div>
  <div class="group">
    <h3>Section 2</h3>
    <div>
      <p>Sed non urna. Donec et ante. Phasellus eu ligula. Vestibulum sit amet purus. Vivamus hendrerit, dolor at aliquet laoreet, mauris turpis porttitor velit, faucibus interdum tellus libero ac justo. Vivamus non quam. In suscipit faucibus urna. </p>
    </div>
  </div>
  <div class="group">
    <h3>Section 3</h3>
    <div>
      <p>Nam enim risus, molestie et, porta ac, aliquam ac, risus. Quisque lobortis. Phasellus pellentesque purus in massa. Aenean in pede. Phasellus ac libero ac tellus pellentesque semper. Sed ac felis. Sed commodo, magna quis lacinia ornare, quam ante aliquam nisi, eu iaculis leo purus venenatis dui. </p>
      <ul>
        <li>List item one</li>
        <li>List item two</li>
        <li>List item three</li>
      </ul>
    </div>
  </div>
  <div class="group">
    <h3>Section 4</h3>
    <div>
      <p>Cras dictum. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Aenean lacinia mauris vel est. </p><p>Suspendisse eu nisl. Nullam ut libero. Integer dignissim consequat lectus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. </p>
    </div>
  </div>
</div>
