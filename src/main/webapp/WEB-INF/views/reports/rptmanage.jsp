<%@ page session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<h3>Reports Configuration</h3>

<form>
	 <fieldset>
	 <!-- All form validation errors and Success message -->	     
	   	<spring:hasBindErrors name="reportManagementForm">
	  	 	<div class="error">
				<core:forEach var="error" items="${errors.allErrors}">
					<b><spring:message message="${error}" /></b>
					<br />
				</core:forEach>
			</div>
	   </spring:hasBindErrors>
	
		<core:if test="${param.msg != null}">
			<div style="display: inline;float: left;width: 100%;align-content: center;" class="success">${param.msg}</div>
		</core:if>
	 </fieldset>
 </form>

 <core:url var="manageReportUrl" value="/ems/reports/management" context="${pageContext.request.contextPath}" />

<form:form id="reportManagementForm" method="POST" action="${manageReportUrl}" modelAttribute="reportManagementForm">
	<table border="1">
		<!-- Print the roles name only once  -->
			<tr>
				<th rowspan="2">Device Name</th>
				<th colspan="${reportTypes.size()}">Report Type</th>
			</tr>
			<tr>
				<core:forEach items="${reportTypes}" var="reportType">
					<td><b>${reportType.desc}</b></td>			
				</core:forEach>
			</tr>
			
			<core:forEach items="${activeDevices}" var="device" varStatus="deviceIndex">
				<tr>
				
				<td>${device.deviceName}</td>
				<input type="hidden" name="uniqueId[${deviceIndex.count - 1}]" value="${device.uniqueId}"/> 
				<core:forEach items="${reportTypes}" var="type" varStatus="rptIndex">
					<td>
						<input type="hidden" name="config[${deviceIndex.count - 1}].bitPosition[${rptIndex.count - 1}]" value="${type.bitPosition}"/>
			 			<core:choose>
							<core:when test="${device.reportMaster.isBitSet(device.reportMaster.type,type.bitPosition)}">
								<center><input  type="checkbox" name="config[${deviceIndex.count - 1}].status[${rptIndex.count - 1}]" checked="checked"/></center>								
							</core:when>
							<core:otherwise>
								<center><input  type="checkbox" name="config[${deviceIndex.count - 1}].status[${rptIndex.count - 1}]"/></center>
							</core:otherwise>
						</core:choose>
					</td>
				</core:forEach>
				</tr>
			 </core:forEach>
	 </table>
	 <br> 
	<div style="display: inline;float: left;width: 100%;">
		<p align="center">
			<input type="submit" class="addUpdatedevice" value="  Update  ">
		</p>
	</div>
</form:form>