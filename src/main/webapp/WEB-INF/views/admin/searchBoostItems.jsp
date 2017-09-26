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
	<form:form method="POST" modelAttribute="boostItemForm" action="${pageContext.request.contextPath}/admin/boostitem">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		<input name="add" type="submit" value="Add New" />
	</form:form>
	<form:form method="GET" action="${pageContext.request.contextPath}/admin/boostitem" modelAttribute="boostItemSearchForm">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		<label for="id">BoostItem Id</label>
		<form:input type="number" path="id" />
		<input type="submit" name="find" value="Find" />
	</form:form>
	<hr />
	<table border="1">
	<tr>
        <th>Boost Item ID</th>
        <th>Account ID</th> 
        <th>Level</th>
        <th>Type</th>  
        <th>Hardcore</th>
        <th>Ironborn</th>
        <th>Stash</th>
        <th>Dungeon</th>
        <th>Message</th>
        <th>Action</th>
    </tr>
    <tr>
    	<form:form method="GET" modelAttribute="boostItemSearchForm" action="${pageContext.request.contextPath}/admin/boostitem">
    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <td><b>Search</b></td>
        <td><form:input path="accountId" type="number" min="-1" /><form:errors path="accountId" /></td>
        <td><form:input path="greaterThanLevel" type="number" min="-1" max="9999" maxlength="4" /><form:errors path="greaterThanLevel" /> to <form:input path="lessThanLevel" type="number" min="-1" max="9999" maxlength="4" /><form:errors path="lessThanLevel" /></td>
        <td>
        	<form:select path="boostItemType">
        		<option value="">All</option>
	   			<form:options items="${boostItemType}" itemLabel="niceName" />
   			</form:select>
   		</td>
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
        <td>Stash</td>
        <td>Dungeon</td>
   		<td>&nbsp;</td>
        <td><input name="search" type="submit" value="Search" /></td>
        </form:form>
    </tr>
	<c:forEach items="${boostItems}" var="boostItem">
        <tr>
            <td><a href="${pageContext.request.contextPath}/admin/boostitem/${boostItem.id}">${boostItem.id}</a></td>
            <td><a href="${pageContext.request.contextPath}/admin/account/${boostItem.account.id}">${boostItem.account.id}</a></td>
            <td>${boostItem.level}</td>
            <td>${boostItem.boostItemType.niceName}</td>
            <td>${boostItem.hardcore}</td>
            <td>${boostItem.ironborn}</td>
            <td>${boostItem.stashSlotId}</td>
            <td>
            <c:if test="${boostItem.dungeonId <= 0}">&nbsp;</c:if>
            <c:if test="${boostItem.dungeonId > 0}">
            <a href="${pageContext.request.contextPath}/admin/dungeon/${boostItem.dungeonId}">${boostItem.dungeonId}</a>
            </c:if>
            </td>
            <td>${boostItem.messageId}</td>
            <td>
            	<form method="GET" action="${pageContext.request.contextPath}/admin/boostitem/${boostItem.id}">
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
