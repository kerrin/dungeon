<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Modify Character</title>
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
	<h1>Modify Character</h1>
	<table>
	    <form:form method="POST" action="${pageContext.request.contextPath}/admin/character/${characterForm.id}" modelAttribute="characterForm" >
	    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			<tr><td>Character Id</td><td><form:input path="id" type="hidden" /><c:out value="${characterForm.id}"/></td><td></td></tr>
		    <tr><td>Account Id</td><td><form:input path="accountId" type="number" /></td><td><form:errors path="accountId" /></td></tr>
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
		    <tr><td>Name</td><td><form:input path="name" type="text" /></td><td><form:errors path="name" /></td></tr>
		    <tr>
		    	<td>Class</td>
		    	<td>
		    		<form:select path="charClass">
			   			<form:options items="${characterClasses}" itemLabel="name" />
		   			</form:select>
		    	</td>
		    	<td><form:errors path="charClass" /></td>
		    </tr>
		    <tr><td>Level</td><td><form:input path="level" type="number" min="1" /></td><td><form:errors path="level" /></td></tr>
		    <tr><td>XP</td><td><form:input path="xp" type="number" min="0" /></td><td><form:errors path="xp" /></td></tr>
		    <tr><td>Prestige Level</td><td><form:input path="prestigeLevel" type="number" min="0" /></td><td><form:errors path="prestigeLevel" /></td></tr>
		    <tr><td>Dead</td><td><form:checkbox  path="currentlyDead" value="true" id="currentlyDead" /></td><td><form:errors path="currentlyDead" /></td></tr>
		    <tr><td>Dungeon Id</td><td><form:input path="dungeonId" id="dungeonId" type="number" /></td><td><form:errors path="dungeonId" /></td></tr>
		    <sec:authorize access="hasRole('Modify')">
		    <tr><td>&nbsp;</td><td colspan="2"><input name="update" type="submit" value="Update"/></td></tr>
		    </sec:authorize>
	    </form:form>
    </table>
    <form method="GET" action="${pageContext.request.contextPath}/admin/character">
    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      	<input name="id" type="hidden" value="${characterForm.id}" /><br /> 
        <input name="list" type="submit" value="Search Character"/><br />
        <a href="${pageContext.request.contextPath}/admin/character/${characterForm.id}/equipment">Equipment</a>
    </form>
    </span>
</body>
</html>
