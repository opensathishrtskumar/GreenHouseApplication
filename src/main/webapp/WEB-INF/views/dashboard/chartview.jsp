<%@ page session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<h3>Monitor Devices</h3>

<c:if test="${not empty message}">
	<div class="${message.type.cssClass}">${message.text}</div>
</c:if>

<form:form id="daterangereport" method="post"
	modelAttribute="reportForm" >
	<div class="formInfo">
		<h2>Choose Device to Monitor</h2>
		<s:bind path="*">
		</s:bind>
	</div>
	<fieldset>
	
		<span class="oneline">
			<form:label path="deviceName">Device Name <form:errors
					path="deviceName" cssClass="error" />
			</form:label>
			<form:select path="deviceName" id="deviceNames" multiple="multiple" class="3col active">
				<c:forEach items="${deviceNames}" var="device">
					<form:option value="${device.uniqueId}" id="${device.uniqueId}">${device}</form:option>
				</c:forEach>
			</form:select>
		</span>
		
		<span class="oneline">
			<form:label path="memoryMappingDetails">Memory Mappings <form:errors
					path="memoryMappingDetails" cssClass="error" />
			</form:label>
			
			<form:select path="memoryMappingDetails" id="memoryMappings" multiple="multiple" class="3col active">
			</form:select>
		</span>
		
	</fieldset>

	<p align="center">
		<input type="button" id="renderchart" value="Monitor">
	</p>
</form:form>