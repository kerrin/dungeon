<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Search Dungeons</title>
  <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js"></script>
  <script>
	$(function() {
		$( "#adminMenuAccount" ).removeClass("menuOptionSelected");
		$( "#adminMenuCharacters" ).removeClass("menuOptionSelected");
		$( "#adminMenuEquipment" ).removeClass("menuOptionSelected");
		$( "#adminMenuBoostItem" ).removeClass("menuOptionSelected");
		$( "#adminMenuDungeons" ).addClass("menuOptionSelected");
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
	<form:form method="POST" modelAttribute="dungeonForm" action="${pageContext.request.contextPath}/admin/dungeon">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		<input name="add" type="submit" value="Add New" />
	</form:form>
	<form:form method="GET" action="${pageContext.request.contextPath}/admin/dungeon" modelAttribute="dungeonSearchForm">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		<label for="id">Dungeon Id</label>
		<form:input type="number" path="id" />
		<input type="submit" name="find" value="Find" />
	</form:form>
	<hr />
	<table border="1">
	<tr>
        <th>Dungeon ID</th>
        <th>Account ID</th>
        <th>Hardcore</th>
        <th>Ironborn</th>
        <th>Type</th>  
        <th>Level</th>
        <th>XP</th>
        <th>Monsters</th>
        <th>Started</th>
        <th>Expires</th>
        <th>Party Size</th>
        <th>&nbsp;</th>
    </tr>
    <tr>
    	<form:form method="GET" modelAttribute="dungeonSearchForm" action="${pageContext.request.contextPath}/admin/dungeon">
    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <td><b>Search</b></td>
        <td><form:input path="accountId" type="number" min="-1" /><form:errors path="accountId" /></td>
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
        <td>
        	<form:select path="type">
	   			<form:options items="${dungeonTypes}" itemLabel="niceName" />
   			</form:select>
   		</td>
        <td><form:input path="greaterThanLevel" type="number" min="-1" max="9999" maxlength="4" /><form:errors path="greaterThanLevel" /> to <form:input path="lessThanLevel" type="number" min="-1" max="9999" maxlength="4" /><form:errors path="lessThanLevel" /></td>
        <td><form:input path="greaterThanXpReward" type="number" min="-1" max="9999" maxlength="4" /><form:errors path="greaterThanXpReward" /> to <form:input path="lessThanXpReward" type="number" min="-1" max="9999" maxlength="4" /><form:errors path="lessThanXpReward" /></td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
		<td><form:input path="greaterThanPartySize" type="number" min="-1" max="99" maxlength="2" /><form:errors path="greaterThanPartySize" /> to <form:input path="lessThanPartySize" type="number" min="-1" max="99" maxlength="2" /><form:errors path="lessThanPartySize" /></td>
        <td><input name="search" type="submit" value="Search" /></td>
        </form:form>
    </tr>
	<c:forEach items="${dungeons}" var="dungeon">
        <tr>
            <td><a href="${pageContext.request.contextPath}/admin/dungeon/${dungeon.id}">${dungeon.id}</a></td>
            <td><a href="${pageContext.request.contextPath}/admin/account/${dungeon.account.id}">${dungeon.account.id}</a></td>
            <td>${dungeon.hardcore}</td>
            <td>${dungeon.ironborn}</td>  
            <td>${dungeon.type.niceName}</td>
            <td>${dungeon.level}</td>
            <td>${dungeon.xpReward}</td>
            <td>Monsters</td>
            <td><fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${dungeon.started}" /></td>
            <td><fmt:formatDate type="time" value="${dungeon.expires}" /></td>
            <td>${dungeon.partySize}</td>
            <td>
            	<form method="GET" action="${pageContext.request.contextPath}/admin/dungeon/${dungeon.id}">
            		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            		<input type="submit" name="update" value="Update" />
            	</form>
            </td>
        </tr>
    </c:forEach>
    </table>
    </span>
</body>
</html>
