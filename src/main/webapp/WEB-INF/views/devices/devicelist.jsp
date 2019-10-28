<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<h2>Device Manager</h2>

<!-- Set required urls with context path -->
 <c:url var="postUrl" value="/ems/device/add" context="${pageContext.request.contextPath}" />
 <c:url var="testUrl" value="/devicemanagement/connection/test" context="${pageContext.request.contextPath}" />
 <c:url var="updateDeviceUrl" value="/ems/device/update" context="${pageContext.request.contextPath}" />
 
 <input type="hidden" id="testUrl" value="${testUrl}">
 <input type="hidden" id="memorymappingcount" min="1" max="3">
 <c:set var="accordionIndex" value="${param.accordionIndex}" />
 
 <!-- Accordion index to keep open always -->
 <c:choose>
	<c:when test="${accordionIndex != null}">
		<input type="hidden" id="showaccordion" value="${accordionIndex}">
	</c:when>
	<c:otherwise>
		<input type="hidden" id="showaccordion" value="${deviceDetailsForm.accordionIndex}">
	</c:otherwise>
</c:choose>
 
 
 
 <!-- All form validation errors and Success message -->	     
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
 
 <hr>

<div id="accordion">

	<!-- First accordion with Form to Test and Add new device -->
  <div class="group">
    <h3>Add New Device</h3>
    <div>
    	 
	     <form:form id="addDeviceForm" method="post" action="${postUrl}" modelAttribute="deviceDetailsForm">
	     
	     	<!-- Keep accordion index to show same accordion post form submission -->
	     	<input type="hidden" name="accordionIndex" value="0">
	     
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
						<form:label path="enabled">Enabled</form:label>
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
  
  <c:forEach items="${existingDeviceList}" var="device" varStatus="index">
  
  	 <div class="group">
	    <h3>${device.deviceName}</h3>
	    <div>
	      <form:form id="addDeviceForm${index.count}" method="post" action="${updateDeviceUrl}" modelAttribute="deviceDetailsForm">
				
		     	<!-- Keep accordion index to show same accordion post form submission -->
		     	<input type="hidden" name="accordionIndex" value="${index.count}">
		     	<form:hidden path="uniqueId" value="${device.uniqueId}"/>
		     
				<fieldset>
					<div style="display: inline;width: 100%">
						<div style="display: inline;float: left;width: 60%">
							<form:label path="deviceName">Device Name / Description</form:label>
							<form:input path="deviceName" value="${device.deviceName}" disabled="true" htmlEscape="true" maxlength="30" />
						</div>
						
						<div style="display: inline;float: left;width: 30%">
							<form:label path="deviceId">Device Address / ID</form:label>
							<form:input path="deviceId" value="${device.deviceId}" disabled="true" title="Unique device identifier in given Group(Maximum can be 214)" maxlength="3"/>
						</div>
						
					</div>	
					
					<div style="display: inline;width: 100%">
						<div style="display: inline;float: left;width: 30%">
							<form:label path="baudRate">Baudrate</form:label>
							<form:input path="baudRate" value="${device.baudRate}" disabled="true" title="Data transfer rate of Device" />
						</div>
						
						<div style="display: inline;float: left;width: 30%">
							<form:label path="wordLength">Word Length</form:label>
							<form:input path="wordLength" value="${device.wordLength}" disabled="true" title="Word Length of Device" />
						</div>
						
						<div style="display: inline;float: left;width: 30%">
							<form:label path="stopbit">Stop Bit</form:label>
							<form:input path="stopbit" value="${device.stopbit}" disabled="true" title="Stop Bit of Device" />
						</div>
					</div>
					
					<div style="display: inline;width: 100%">
						<div style="display: inline;float: left;width: 30%">
							<form:label path="parity">Parity</form:label>
							<form:input path="parity" value="${device.parity}" disabled="true" title="Parity type of Device" />
						</div>
						
						<div style="display: inline;float: left;width: 30%">
							<form:label path="method">Method</form:label>
							<form:input path="method" value="${device.method}" disabled="true" title="Method of Device" />
						</div>
						
						<div style="display: inline;float: left;width: 30%">
							<form:label path="registerMapping">Register Data Order</form:label>
							<form:input path="registerMapping" value="${device.registerMapping}" disabled="true" title="Register Data Order of Device" />
						</div>
					</div>
					
					
					<div style="display: inline;width: 100%">
						<div style="display: inline;float: left;width: 30%">
							<form:label path="encoding">Encoding</form:label>
							<form:input path="encoding" value="${device.encoding}" disabled="true" title="Encoding type of Device" />
						</div>
						
						<div style="display: inline;float: left;width: 50%">
							<form:label path="port">COM Port</form:label>
							<form:input path="port" value="${device.port}" disabled="true" title="COM Port in which Device connected" />
						</div>
						
					</div>
					
					<div style="display: inline;width: 100%">
						<div style="display: inline;float: left;width: 30%">
							<form:label path="enabled">Enabled</form:label>
							<c:choose>
								<c:when test="${device.status == 1}">
									<form:checkbox path="enabled" title="Enable / Disable Device" value="${status}" checked="checked"/>								
								</c:when>
								<c:otherwise>
									<form:checkbox path="enabled" title="Enable / Disable Device"/>
								</c:otherwise>
							</c:choose>
						</div>
						
						<div style="display: inline;float: left;width: 30%">
							<form:label path="deleted">Delete</form:label>
							<form:checkbox path="deleted" class="deleteDevice" title="Delete Device"/>		
						</div>		
					</div>
					
					<div style="display: inline;width: 100%">
						<div style="display: inline;float: left;width: 100%">
							<label for="memorymapping" style="display: inline;float: left;width: 50%" >Memory Mapping Details</label>
						</div>
						
						<c:set var="memoryList" value="${device.memoryMappings}"/>
							
						<div id="mappingDetails"  style="display: inline;float: left;width: 100%">
						
							<c:forEach items="${memoryList}" var="memory"  varStatus="memoryIndex">
								<form:textarea class="memoryMapping" placeholder="${memory.memoryMapping}" disabled="true" rows="10" cols="50" path="memoryMappings[${memoryIndex.count - 1}].memoryMapping" />
							</c:forEach>
							
						</div>
					</div>	
					
					<div style="display: inline;width: 100%">
						<div style="display: inline;float: left;width: 30%">
							<input type="submit" class="updatedevice" value="Update Device">
						</div>
					</div>
			
				</fieldset>
				
			</form:form>
	    </div>
	  </div>
  
  </c:forEach>
  
</div>
