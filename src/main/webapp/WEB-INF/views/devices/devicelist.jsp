<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<h2>Device Manager</h2>

<div id="accordion">
	
	<!-- First accordion with Form to Test and Add new device -->
  <div class="group">
    <h3>Add New Device</h3>
    <div>
	     <form:form id="addDevice" method="post"  modelAttribute="deviceDetailsForm">
			<fieldset>
				<form:label path="deviceName">Device Name / Description<form:errors path="deviceName" cssClass="error" /></form:label>
				<form:input path="deviceName" htmlEscape="true" maxlength="50" />
				
				<form:label path="deviceId">Device Address / ID<form:errors path="deviceId" cssClass="error" /></form:label>
				<form:input path="deviceId" title="Unique device identifier in given Group(Maximum can be 214)" htmlEscape="true" maxlength="3"/>
				
				<form:label path="baudRate">Baudrate<form:errors path="baudRate" cssClass="error" /></form:label>
				<form:select path="baudRate" items="${formDetails.baudRateList}" title="Data transfer rate of Device" ></form:select>
				
				<form:label path="wordLength">Word Length<form:errors path="wordLength" cssClass="error" /></form:label>
				<form:select path="wordLength" items="${formDetails.wordlengthList}" title="Word Length of Device" ></form:select>
				
				<form:label path="stopbit">Stop Bit<form:errors path="stopbit" cssClass="error" /></form:label>
				<form:select path="stopbit" items="${formDetails.stopbitList}" title="Stop Bit of Device" ></form:select>
				
				<form:label path="parity">Parity<form:errors path="parity" cssClass="error" /></form:label>
				<form:select path="parity" items="${formDetails.parityList}" title="Parity type of Device" ></form:select>
				
				<form:label path="method">Method<form:errors path="method" cssClass="error" /></form:label>
				<form:select path="method" items="${formDetails.readMethodList}" title="Method of Device" ></form:select>
				
				<form:label path="registerMapping">Register Data Order<form:errors path="registerMapping" cssClass="error" /></form:label>
				<form:select path="registerMapping" items="${formDetails.registerMappingList}" title="Register Data Order of Device" ></form:select>
				
				<form:label path="encoding">Encoding<form:errors path="encoding" cssClass="error" /></form:label>
				<form:select path="encoding" items="${formDetails.encodingList}" title="Encoding type of Device" ></form:select>
				
				<form:label path="enabled">Enable/Disable<form:errors path="enabled" cssClass="error" /></form:label>
				<form:checkbox path="enabled" title="Enable / Disable Device"/>
				
				
				<label for="memorymapping">Memory Mapping Details</label>
				<div id="memorymapping" name="memorymapping">
					<form:textarea  title="Memory Mappings in Detail" rows="10" cols="50" path="memoryMappings[0].memoryMapping"/>
					<form:textarea  title="Memory Mappings in Detail" rows="10" cols="50" path="memoryMappings[1].memoryMapping"/>
				</div>
		
			</fieldset>
			<p>
				<input type="submit" value="Add Device">
				<input type="button" value="Test Connection">
			</p>
		</form:form>
    </div>
  </div>
  
  <!--Iterate existing devices in form, that can be updated/deleted(Soft delete)  -->
  <div class="group">
    <h3>Section 2</h3>
    <div>
      <p>Sed non urna. Donec et ante. Phasellus eu ligula. Vestibulum sit amet purus. Vivamus hendrerit, dolor at aliquet laoreet, mauris turpis porttitor velit, faucibus interdum tellus libero ac justo. Vivamus non quam. In suscipit faucibus urna. </p>
    </div>
  </div>
  
</div>
