<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Search Characters</title>
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
	<form:form method="POST" modelAttribute="characterForm" action="${pageContext.request.contextPath}/admin/character">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		<input name="add" type="submit" value="Add New" />
	</form:form>
	<form:form method="GET" action="${pageContext.request.contextPath}/admin/character" modelAttribute="characterSearchForm">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		<label for="id">Character Id</label>
		<form:input type="number" path="id" />
		<input type="submit" name="find" value="Find" />
	</form:form>
	<hr />
	<table border="1">
	<tr>
        <th>Character ID</th>
        <th>Account ID</th>
        <th>Hardcore</th>
        <th>Ironborn</th>
        <th>Name</th>  
        <th>Class</th>
        <th>Level</th>
        <th>XP</th>
        <th>Prestige Level</th>
        <th>Dungeon</th>
        <th colspan="2">Action</th>
    </tr>
    <tr>
    	<form:form method="GET" modelAttribute="characterSearchForm" action="${pageContext.request.contextPath}/admin/character">
    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <td><b>Search</b></td>
        <td><form:input path="accountId" type="number" /><form:errors path="accountId" /></td> 
        <td>
        	<form:select path="hardcore">
	   			<form:options items="${booleanOptions}" itemLabel="name" />
   			</form:select>
   		</td>
        <td>
        	<form:select path="ironborn">
	   			<form:options items="${booleanOptions}" itemLabel="name" />
   			</form:select>
   		</td>
        <td><form:input path="name" type="text" /><form:errors path="name" /></td>  
        <td>
        	<form:select path="charClass">
	   			<form:options items="${characterClasses}" itemLabel="name" />
   			</form:select>
   		</td>
        <td><form:input path="greaterThanLevel" type="number" min="-1" max="9999" maxlength="4" /><form:errors path="greaterThanLevel" /> to <form:input path="lessThanLevel" type="number" min="-1" max="9999" maxlength="4" /><form:errors path="lessThanLevel" /></td>
        <td><form:input path="greaterThanXp" type="number" min="-1" max="99999999" /><form:errors path="greaterThanXp" /> to <form:input path="lessThanXp" type="number" min="-1" max="99999999" /><form:errors path="lessThanXp" /></td>
        <td><form:input path="greaterThanPrestigeLevel" type="number" min="-1" max="9999" maxlength="4" /><form:errors path="greaterThanPrestigeLevel" /> to <form:input path="lessThanPrestigeLevel" type="number" min="-1" max="9999" maxlength="4" /><form:errors path="lessThanPrestigeLevel" /></td>
        <td><form:input path="dungeonId" type="number" min="-1" max="99999999" /><form:errors path="dungeonId" /></td>
        <td colspan="2"><input name="search" type="submit" value="Search" /></td>        
        </form:form>
    </tr>
	<c:forEach items="${characters}" var="character">
        <tr>
            <td><a href="${pageContext.request.contextPath}/admin/character/${character.id}">${character.id}</a></td>
            <td><a href="${pageContext.request.contextPath}/admin/account/${character.account.id}">${character.account.id}</a></td>
            <td>${character.hardcore}</td>
            <td>${character.ironborn}</td>  
            <td>${character.name}</td>
            <td>${character.charClass.name}</td>
            <td style="text-align: center;">${character.level}</td>
            <td style="text-align: center;">${character.xp}</td>
            <td style="text-align: center;">${character.prestigeLevel}</td>
            <td style="text-align: center;">
            	<c:if test="${!empty character.dungeon}">
            	<a href="${pageContext.request.contextPath}/admin/dungeon/${character.dungeon.id}">
            		${character.dungeon.id}
            	</a>
            	</c:if>
            </td>
            <td>
            	<form method="GET" action="${pageContext.request.contextPath}/admin/character/${character.id}">
            		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            		<input type="submit" name="update" value="Update" />
            	</form>
            </td>
            <td><a href="${pageContext.request.contextPath}/admin/character/${character.id}/equipment">Equipment</a></td>
        </tr>
    </c:forEach>
    </table>
    </span>
</body>
</html>
