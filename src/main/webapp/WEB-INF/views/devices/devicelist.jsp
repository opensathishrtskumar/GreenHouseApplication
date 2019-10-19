<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<h2>Device Manager</h2>

<div id="accordion">

	
	<!-- First accordion with Form to Test and Add new device -->
  <div class="group">
    <h3>Add New Device</h3>
    <div>
    	 <!-- Set required urls with context path -->
    	 <c:url var="postUrl" value="/ems/device/add" context="${pageContext.request.contextPath}" />
    	 <c:url var="testUrl" value="/devicemanagement/connection/test" context="${pageContext.request.contextPath}" />
    	 <input type="hidden" id="testUrl" value="${testUrl}"> 
    	 <input type="hidden" id="memorymappingcount" min="1" max="3">
    	 
    	 <!-- All form validation errors -->
		    	 
	     <form:form id="addDeviceForm" method="post" action="${postUrl}" modelAttribute="deviceDetailsForm">
	     
	     	<spring:hasBindErrors name="deviceDetailsForm">
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
				<div style="display: inline;width: 100%">
					<div style="display: inline;float: left;width: 60%">
						<form:label path="deviceName">Device Name / Description</form:label>
						<form:input path="deviceName" htmlEscape="true" maxlength="30" />
					</div>
					
					<div style="display: inline;float: left;width: 30%">
						<form:label path="deviceId">Device Address / ID</form:label>
						<form:input path="deviceId" title="Unique device identifier in given Group(Maximum can be 214)" htmlEscape="true" maxlength="3"/>
					</div>
					
				</div>	
				
				<div style="display: inline;width: 100%">
					<div style="display: inline;float: left;width: 30%">
						<form:label path="baudRate">Baudrate</form:label>
						<form:select path="baudRate" items="${formDetails.baudRateList}" title="Data transfer rate of Device" ></form:select>
					</div>
					
					<div style="display: inline;float: left;width: 30%">
						<form:label path="wordLength">Word Length</form:label>
						<form:select path="wordLength" items="${formDetails.wordlengthList}" title="Word Length of Device" ></form:select>
					</div>
					
					<div style="display: inline;float: left;width: 30%">
						<form:label path="stopbit">Stop Bit</form:label>
						<form:select path="stopbit" items="${formDetails.stopbitList}" title="Stop Bit of Device" ></form:select>
					</div>
				</div>
				
				<div style="display: inline;width: 100%">
					<div style="display: inline;float: left;width: 30%">
						<form:label path="parity">Parity</form:label>
						<form:select path="parity" items="${formDetails.parityList}" title="Parity type of Device" ></form:select>
					</div>
					
					<div style="display: inline;float: left;width: 30%">
						<form:label path="method">Method</form:label>
						<form:select path="method" items="${formDetails.readMethodList}" title="Method of Device" ></form:select>
					</div>
					
					<div style="display: inline;float: left;width: 30%">
						<form:label path="registerMapping">Register Data Order</form:label>
						<form:select path="registerMapping" items="${formDetails.registerMappingList}" title="Register Data Order of Device" ></form:select>
					</div>
				</div>
				
				
				<div style="display: inline;width: 100%">
					<div style="display: inline;float: left;width: 30%">
						<form:label path="encoding">Encoding</form:label>
						<form:select path="encoding" items="${formDetails.encodingList}" title="Encoding type of Device" ></form:select>
					</div>
					
					<div style="display: inline;float: left;width: 30%">
						<form:label path="port">COM Port</form:label>
						<form:select path="port" items="${formDetails.comPortList}" title="COM Port in which Device connected" ></form:select>
					</div>
					
					<div style="display: inline;float: left;width: 30%">
						<form:label path="enabled">Enable/Disable</form:label>
						<form:checkbox path="enabled" title="Enable / Disable Device"/>		
					</div>
				</div>	
				
				
				<div style="display: inline;width: 100%">
					<div style="display: inline;float: left;width: 100%">
						<label for="memorymapping" style="display: inline;float: left;width: 50%" >Memory Mapping Details</label>
						<input type="button" value="+" class="addmemorymapping" title="Add memory mapping" style="display: inline;float: left;width: 10%">
						<input type="button" value="-" class="removememorymapping" title="Remove memory mapping" style="display: inline;float: left;width: 10%">
					</div>
					
					<div id="mappingDetails"  style="display: inline;float: left;width: 100%">
						<c:choose>
							<c:when test="${memoryMappings == null || empty memoryMappings}">
								<form:textarea class="memoryMapping" htmlEscape="true"  title="Memory Mappings in Detail" rows="10" cols="50" path="memoryMappings[0].memoryMapping"/>
							</c:when>
							<c:otherwise>
								<c:forEach items="${memoryMappings}" varStatus="index" var="memory">
									<form:textarea class="memoryMapping" value="${memory.memoryMapping}" htmlEscape="true" title="Memory Mappings in Detail" rows="10" cols="50" path="memoryMappings[${index.count - 1}].memoryMapping" />
								</c:forEach>
							</c:otherwise>
						</c:choose>
						
					</div>
				</div>	
				
				<div style="display: inline;width: 100%">
					<div style="display: inline;float: left;width: 30%">
						<input type="submit" id="adddevice" value="Add Device" disabled="disabled">
					</div>
					<div style="display: inline;float: left;width: 30%">	
						<input type="button" id="testconnection" value="Test Connection">
					</div>	
				</div>
		
			</fieldset>
			
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
