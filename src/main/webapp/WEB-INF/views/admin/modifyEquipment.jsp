<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="equipmentTagLib" prefix="equip"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Modify Equipment</title>
  	<script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/jquery-ui.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/jquery.qtip.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/imagesloaded.pkg.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/shared.js"></script>
  <script>
	$(function() {
		$( "#adminMenuAccount" ).removeClass("menuOptionSelected");
		$( "#adminMenuCharacters" ).removeClass("menuOptionSelected");
		$( "#adminMenuEquipment" ).addClass("menuOptionSelected");
		$( "#adminMenuBoostItem" ).removeClass("menuOptionSelected");
		$( "#adminMenuDungeons" ).removeClass("menuOptionSelected");
		$( "#adminMenuCurrencyAudit" ).removeClass("menuOptionSelected");
		$( "#adminMenuTestDungeon" ).removeClass("menuOptionSelected");
		$( "#adminMenuMessageAll" ).removeClass("menuOptionSelected");
		$( "#adminMenuLogout" ).removeClass("menuOptionSelected");
	});
	</script>
</head>
<body onLoad="equipment_showAttributes()">
	<%@ include file="adminMenu.jsp" %>
	<span class="adminContent">
	<h1>Modify Equipment</h1>
	<table style="float: left">
	    <form:form method="POST" action="${pageContext.request.contextPath}/admin/equipment/${equipmentForm.id}" modelAttribute="equipmentForm" >
	    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			<tr><td><label for="id">Equipment Id</label></td><td><form:input path="id" type="hidden" /><c:out value="${equipmentForm.id}"/></td></tr>
	        <tr>
	        	<td><label for="equipmentType">Equipment Type</label></td>
	        	<td>
	        		<form:select path="equipmentType">
			   			<form:options items="${equipmentType}" itemLabel="niceName" />
		   			</form:select>
	   			</td>
	   			<td>&nbsp;</td>
	   		</tr>
		    <tr>
	        	<td><label for="level">Level</label></td>
	        	<td><form:input path="level" type="number" min="1" /></td>
	        	<td><form:errors path="level" /></td>
	        </tr>
			<tr>
				<td>Hardcore</td>
				<td><form:checkbox path="hardcore" value="true" id="hardcore" /></td>
				<td><form:errors path="hardcore" /></td>
			</tr>
			<tr>
				<td>Ironborn</td>
				<td><form:checkbox  path="ironborn" value="true" id="ironborn" /></td>
				<td><form:errors path="ironborn" /></td>
			</tr>
	        <tr>
	        	<td>Equipment Location</td>
	        	<td>
		        	${equipmentForm.equipmentLocation}: ${equipmentForm.equipmentLocationId}
	   			</td>
	   			<td>&nbsp;</td>
	   		</tr>
	   		<tr>
	        	<td><label for="quality">Quality</label></td>
	        	<td>
		        	<form:select path="quality" onchange="equipment_showAttributes()">
			   			<form:options items="${quality}" itemLabel="niceName" />
		   			</form:select>
	   			</td>
	   			<td><form:errors path="quality" /></td>
	   		</tr>
	   		
	        <tr id="baseAttribute">
	        	<td><label for="baseAttributeType">Base Attribute</label></td>
	        	<td>
	        		<form:select path="baseAttributeType">
			   			<form:options items="${attributes}" itemLabel="niceName" />
		   			</form:select>        		
	        		<form:input path="baseAttributeValue" type="number" min="0" />
	        	</td>
	        	<td><form:errors path="baseAttributeType" /><form:errors path="baseAttributeValue" /></td>
	        </tr>
	        <tr id="defenceAttribute">
	        	<td><label for="defenceAttributeType">Defence Attribute</label></td>
	        	<td>
	        		<form:select path="defenceAttributeType">
			   			<form:options items="${attributes}" itemLabel="niceName" />
		   			</form:select>        		
	        		<form:input path="defenceAttributeValue" type="number" min="0" />
	        	</td>
	        	<td><form:errors path="defenceAttributeType" /><form:errors path="defenceAttributeValue" /></td>
	        </tr>
	        <tr id="attribute0">
	        	<td><label for="attributeType0">Attribute 1</label></td>
	        	<td>
	        		<form:select path="attributeType0">
			   			<form:options items="${attributes}" itemLabel="niceName" />
		   			</form:select>        		
	        		<form:input path="attributeValue0" type="number" min="0" />
	        	</td>
	        	<td><form:errors path="attributeType0" /><form:errors path="attributeValue0" /></td>
	        </tr>
	        <tr id="attribute1">
	        	<td><label for="attributeType1">Attribute 2</label></td>
	        	<td>
	        		<form:select path="attributeType1">
			   			<form:options items="${attributes}" itemLabel="niceName" />
		   			</form:select>        		
	        		<form:input path="attributeValue1" type="number" min="0" />
	        	</td>
	        	<td><form:errors path="attributeType1" /><form:errors path="attributeValue1" /></td>
	        </tr>
	        <tr id="attribute2">
	        	<td><label for="attributeType2">Attribute 3</label></td>
	        	<td>
	        		<form:select path="attributeType2">
			   			<form:options items="${attributes}" itemLabel="niceName" />
		   			</form:select>        		
	        		<form:input path="attributeValue2" type="number" min="0" />
	        	</td>
	        	<td><form:errors path="attributeType2" /><form:errors path="attributeValue2" /></td>
	        </tr>
	        <tr id="attribute3">
	        	<td><label for="attributeType3">Attribute 4</label></td>
	        	<td>
	        		<form:select path="attributeType3">
			   			<form:options items="${attributes}" itemLabel="niceName" />
		   			</form:select>        		
	        		<form:input path="attributeValue3" type="number" min="0" />
	        	</td>
	        	<td><form:errors path="attributeType3" /><form:errors path="attributeValue3" /></td>
	        </tr>
	        <tr id="attribute4">
	        	<td><label for="attributeType4">Attribute 5</label></td>
	        	<td>
	        		<form:select path="attributeType4">
			   			<form:options items="${attributes}" itemLabel="niceName" />
		   			</form:select>        		
	        		<form:input path="attributeValue4" type="number" min="0" />
	        	</td>
	        	<td><form:errors path="attributeType4" /><form:errors path="attributeValue4" /></td>
	        </tr>
	        <tr id="ancientAttribute"> <!-- also only shown for ancient -->
	        	<td><label for="ancientAttribute">Ancient Attribute</label></td>
	        	<td>
	        		<form:select path="ancientAttributeType">
			   			<form:options items="${attributes}" itemLabel="niceName" />
		   			</form:select>        		
	        		<form:input path="ancientAttributeValue" type="number" min="0" />
	        	</td>
	        	<td><form:errors path="ancientAttributeType" /><form:errors path="ancientAttributeValue" /></td>
	        </tr>
	        <tr id="sendToAccountId">
	        	<td><label for="sendToAccountId">Send To Account ID</label></td>
	        	<td>     		
	        		<form:input path="sendToAccountId" type="number" min="-1" data-qtiptitle="-1 means don't send" />
	        	</td>
	        	<td><form:errors path="sendToAccountId" /></td>
	        </tr>	        
		    <sec:authorize access="hasRole('Modify')">
		    <tr><td>&nbsp;</td><td colspan="2"><input name="update" type="submit" value="Update"/></td></tr>
		    </sec:authorize>
	    </form:form>
    </table>
    <%@ include file="equipmentNotes.jsp" %>
    <form method="GET" action="${pageContext.request.contextPath}/admin/equipment">
    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      	<input name="id" type="hidden" value="${equipmentForm.id}" /><br /> 
        <input name="list" type="submit" value="Search Equipment"/><br />
        <!-- a href="${pageContext.request.contextPath}/admin/character/${characterId}/equipment">Character Equipment</a-->
    </form>
    <br />
    <equip:equipmentDisplay equipment="${equipment}"
								adminView="true"
								compare="false" 
			        			dragable="false" 
			        			dropable="false" 
			        			hoverAbove="false"
			        			accountApiKey="${account.apiKey}"
			        			isTouchScreen="${account.touchScreen}"
			        			/>
    </span>
</body>
</html>
