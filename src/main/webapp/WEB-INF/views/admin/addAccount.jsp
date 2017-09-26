<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Add Account</title>
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
	<h1>Add Account</h1>
	<table>
    	<form:form method="POST" modelAttribute="accountForm" action="${pageContext.request.contextPath}/admin/account">
    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <tr>
        	<td>Username</td>
        	<td><form:input path="username" type="text" id="username" /></td>
        	<td><form:errors path="username" /></td>
        </tr>
        <tr>
        	<td>Password</td>
        	<td><form:input path="password" type="text" id="password" /></td>
        	<td><form:errors path="password" /></td>
   		</tr>
        <tr>
        	<td>Display Name</td>
        	<td><form:input path="displayName" type="text" id="displayName" /></td>
        	<td><form:errors path="displayName" /></td>
   		</tr>
   		<sec:authorize access="hasRole('Modify')">
        <tr><td>&nbsp;</td><td colspan="2"><input name="add" type="submit" value="Add" /></td></tr>
        </sec:authorize>
        </form:form>
    </table>
    <form method="GET" action="${pageContext.request.contextPath}/admin/account">
    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      	<input name="list" type="submit" value="Back"/>
    </form>
    </span>
</body>
</html>
