<%@ page session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<h3>Reports Configuration</h3>

<form>
	<fieldset>
		<c:if test="${not empty message}">
			<div class="${message.type.cssClass}">${message.text}</div>
		</c:if>	
	</fieldset>
</form>

<form:form id="reportManagementForm" method="post" action="${roleManageUrl}" modelAttribute="reportManagementForm">
	<table border="1">
		<!-- Print the roles name only once  -->
			<tr>
				<th rowspan="2">Device Name</th>
				<th colspan="${reportTypes.size()}">Report Type</th>
			</tr>
			<tr>
				<c:forEach items="${reportTypes}" var="reportType">
					<td><b>${reportType.desc}</b></td>			
				</c:forEach>
			</tr>
			
			<c:forEach items="${activeDevices}" var="device" varStatus="index">
				<tr>
				
				<td>${device.deviceName}</td> 
				
				<c:forEach items="${reportTypes}" var="type" varStatus="rptIndex">
					<td>  
			 			<c:choose>
							<c:when test="${device.reportMaster.isBitSet(device.reportMaster.type,type.bitPosition)}">
								<center><input  type="checkbox" name="bitPosition[${page.bitPosition}]" value="${page.bitPosition}" checked="checked"/></center>								
							</c:when>
							<c:otherwise>
								<center><input  type="checkbox" name="bitPosition[${page.bitPosition}]" value="${page.bitPosition}"/></center>
							</c:otherwise>
						</c:choose>
					</td>
				</c:forEach>
				</tr>
			 </c:forEach>
	 </table>
	 <br> 
	<div style="display: inline;float: left;width: 100%;">
		<p align="center">
			<input type="submit" class="addUpdatedevice" value="  Update  ">
		</p>
	</div>
</form:form>