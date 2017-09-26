<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Search Accounts</title>
  <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js"></script>
  <script>
	$(function() {
		$( "#adminMenuAccount" ).addClass("menuOptionSelected");
		$( "#adminMenuCharacters" ).removeClass("menuOptionSelected");
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
	<form:form method="POST" modelAttribute="accountForm" action="${pageContext.request.contextPath}/admin/account">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		<input name="add" type="submit" value="Add New" />
	</form:form>
	<form:form method="GET" action="${pageContext.request.contextPath}/admin/account" modelAttribute="accountSearchForm">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		<label for="id">Account Id</label>
		<form:input type="number" path="id" />
		<input type="submit" name="find" value="Find" />
	</form:form>
	<hr />
	<table>
	<tr>
        <th>Account Id</th>
        <th>Username</th>  
        <th>Display Name</th>
        <th>Previous Login</th>
        <th>Last Login</th>
        <th>On Holiday</th>
        <th>Login Tokens</th>
        <th colspan="2">Action</th>
    </tr>
    <tr>
    	<form:form method="GET" modelAttribute="accountSearchForm" action="${pageContext.request.contextPath}/admin/account">
    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <td><b>Search</b></td>
        <td><form:input path="username" type="text" /><form:errors path="username" /></td>
        <td><form:input path="displayName" type="text" /><form:errors path="displayName" /></td>
		<td colspan="5">&nbsp;</td>
        <td colspan="2"><input name="search" type="submit" value="Search" /></td>
        </form:form>
    </tr>
	<c:forEach items="${accounts}" var="account">
        <tr>
            <td><a href="${pageContext.request.contextPath}/admin/account/${account.id}">${account.id}</a></td>
            <td>${account.username}</td>
            <td>${account.displayName}</td>
            <td><fmt:formatDate value="${account.previousLogin}" pattern="yyyy-MM-dd hh:mm:ss"/></td> 
            <td><fmt:formatDate value="${account.lastLogin}" pattern="yyyy-MM-dd hh:mm:ss"/></td>  
            <td>${account.onHoliday}</td>
            <td>${account.loginTokens}</td>
            <td>
            	<form method="GET" action="${pageContext.request.contextPath}/admin/account/${account.id}">
            		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            		<input type="submit" name="update" value="Update" />
            	</form>
            	<form method="GET" action="${pageContext.request.contextPath}/admin/account/${account.id}/process">
            		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            		<input type="submit" name="update" value="Process" />
            	</form>
            </td>
	        <td>
	        	<a href="${pageContext.request.contextPath}/admin/account/${account.id}/characters">Characters</a>
	        	&nbsp;|&nbsp;
	        	<a href="${pageContext.request.contextPath}/admin/account/${account.id}/dungeons">Dungeons</a>
				&nbsp;|&nbsp;
				<a href="${pageContext.request.contextPath}/admin/account/currencyaudit/${account.id}">Currency Audit</a>
	        	&nbsp;|&nbsp;				
			     <form method="GET" action="${pageContext.request.contextPath}/admin/account/${account.id}/inventory">
			    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			      	<input name="hardcore" type="checkbox" value="true" /><label>Hardcore</label>  
			        <input name="ironborn" type="checkbox" value="true"/><label>Ironborn</label>
			        <input name="submit" type="submit" value="Inventory"/>
			    </form>
	        </td>
        </tr>
    </c:forEach>
    </table>
    </span>
</body>
</html>
