<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Add Character</title>
  <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js"></script>
  <script>
	$(function() {
		$( "#adminMenuAccount" ).removeClass("menuOptionSelected");
		$( "#adminMenuCharacters" ).addClass("menuOptionSelected");
		$( "#adminMenuEquipment" ).removeClass("menuOptionSelected");
		$( "#adminMenuBoostItem" ).removeClass("menuOptionSelected");
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
	<h1>Add Character</h1>
	<table>
    	<form:form method="POST" modelAttribute="characterForm" action="${pageContext.request.contextPath}/admin/character">
    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <tr>
        	<td>Account Id</td>
        	<td><form:input path="accountId" type="number" id="accountId" min="1" /></td>
        	<td><form:errors path="accountId" /></td>
        </tr>
        <tr>
        	<td>Hardcore</td>
        	<td><form:checkbox  path="hardcore" value="true" id="hardcore" /></td>
        	<td><form:errors path="hardcore" /></td>
        </tr>
        <tr>
        	<td>Ironborn</td>
        	<td><form:checkbox  path="ironborn" value="true" id="ironborn" /></td>
        	<td><form:errors path="ironborn" /></td>
        </tr>
        <tr>
        	<td>Name</td>
        	<td><form:input path="name" type="text" id="name" /></td>
        	<td><form:errors path="name" /></td>
        </tr>
        <tr>
        	<td>Class</td>
        	<td>
	        	<form:select path="charClass">
		   			<form:options items="${characterClasses}" itemLabel="name" />
	   			</form:select>
   			</td>
   			<td><form:errors path="charClass" /></td>
   		</tr>
        <tr>
        	<td>Level</td>
        	<td><form:input path="level" type="number" value="1" min="1" /></td>
        	<td><form:errors path="level" /></td>
        </tr>
        <tr>
        	<td>XP</td>
        	<td><form:input path="xp" type="number" value="0" min="0" /></td>
        	<td><form:errors path="xp" /></td>
        </tr>
        <tr>
        	<td>Prestige Level</td>
        	<td><form:input path="prestigeLevel" type="number" value="0" min="0" /></td>
        	<td><form:errors path="prestigeLevel" /></td>
        </tr>
        <sec:authorize access="hasRole('Modify')">
        <tr><td>&nbsp;</td><td colspan="2"><input name="add" type="submit" value="Add" /></td></tr>
        </sec:authorize>
        </form:form>
    </table>
    <form method="GET" action="${pageContext.request.contextPath}/admin/character">
    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      	<input name="list" type="submit" value="Back"/>
    </form>
    </span>
</body>
</html>
