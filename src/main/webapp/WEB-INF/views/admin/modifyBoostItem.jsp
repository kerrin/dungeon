<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Modify BoostItem</title>
  <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js"></script>
  <script>
	$(function() {
		$( "#adminMenuAccount" ).removeClass("menuOptionSelected");
		$( "#adminMenuCharacters" ).removeClass("menuOptionSelected");
		$( "#adminMenuEquipment" ).removeClass("menuOptionSelected");
		$( "#adminMenuBoostItem" ).addClass("menuOptionSelected");
		$( "#adminMenuDungeons" ).removeClass("menuOptionSelected");
		$( "#adminMenuCurrencyAudit" ).removeClass("menuOptionSelected");
		$( "#adminMenuTestDungeon" ).removeClass("menuOptionSelected");
		$( "#adminMenuMessageAll" ).removeClass("menuOptionSelected");
		$( "#adminMenuLogout" ).removeClass("menuOptionSelected");
	});
	</script>
</head>
<body>
	<%@ include file="adminMenu.jsp" %>
	<span class="adminContent">
	<h1>Modify Boost Item</h1>
	<table style="float: left">
	    <form:form method="POST" action="${pageContext.request.contextPath}/admin/boostitem/${boostItemForm.id}" modelAttribute="boostItemForm" >
	    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			<tr><td><label for="id">BoostItem Id</label></td><td><form:input path="id" type="hidden" /><c:out value="${boostItemForm.id}"/></td></tr>
	        <tr id="accountId">
	        	<td><label for="accountId">Account ID</label></td>
	        	<td>
	        		<form:input path="accountId" type="number" min="1" />
	        	</td>
	        	<td><form:errors path="accountId" /></td>
	        </tr>
	   		<tr>
	        	<td><label for="boostItemType">BoostItem Type</label></td>
	        	<td>
	        		<form:select path="boostItemType">
			   			<form:options items="${boostItemType}" itemLabel="niceName" />
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
	   		<tr id="stashSlotId">
	        	<td><label for="stashSlotId">Stash Slot ID</label></td>
	        	<td>
	        		<form:input path="stashSlotId" type="hidden" />
	        		${boostItemForm.stashSlotId}
	        	</td>
	        	<td><form:errors path="stashSlotId" /></td>
	        </tr>
	   		<tr id="dungeonId">
	        	<td><label for="dungeonId">Dungeon ID</label></td>
	        	<td>
	        		<form:input path="dungeonId" type="hidden" />
	        		${boostItemForm.dungeonId}
	        	</td>
	        	<td><form:errors path="dungeonId" /></td>
	        </tr>
	   		<tr id="messageId">
	        	<td><label for="messageId">Message ID</label></td>
	        	<td>
	        		<form:input path="messageId" type="hidden" />
	        		${boostItemForm.messageId}
	        	</td>
	        	<td><form:errors path="messageId" /></td>
	        </tr>
	        <tr id="sendToAccountId">
	        	<td><label for="sendToAccount">Send To Account</label></td>
        		<td><form:checkbox path="sendToAccount" value="true" id="sendToAccount" /></td>
	        	<td><form:errors path="sendToAccount" /></td>
	        </tr>	        
		    <sec:authorize access="hasRole('Modify')">
		    <tr><td>&nbsp;</td><td colspan="2"><input name="update" type="submit" value="Update"/></td></tr>
		    </sec:authorize>
	    </form:form>
    </table>
    <form method="GET" action="${pageContext.request.contextPath}/admin/boostitem">
    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      	<input name="id" type="hidden" value="${boostItemForm.id}" /><br /> 
        <input name="list" type="submit" value="Search Boost Item"/><br />
    </form>
    </span>
</body>
</html>
