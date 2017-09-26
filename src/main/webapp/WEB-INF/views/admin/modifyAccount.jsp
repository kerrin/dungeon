<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Modify Account</title>
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
	<%@ include file="../checkError.jsp" %>
	<%@ include file="adminMenu.jsp" %>
	<span class="adminContent">
	<span style="float: left; width: 400px;">
	<h1>Modify Account</h1>
	<table>
	    <form:form method="POST" action="${pageContext.request.contextPath}/admin/account/${accountForm.id}" modelAttribute="accountForm" >
	    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			<tr><td>Account Id</td><td><form:input path="id" type="hidden" /><c:out value="${accountForm.id}"/></td><td></td></tr>
		    <tr><td>Username</td><td><form:input path="username" type="text" /></td><td><form:errors path="username" /></td></tr>
		    <tr><td>Password</td><td><form:input path="password" type="password" /></td><td><form:errors path="password" /></td></tr>
		    <tr><td>Display Name</td><td><form:input path="displayName" type="text" /></td><td><form:errors path="displayName" /></td></tr>
		    <tr><td>Login Tokens Adjustment (${account.loginTokens})</td><td><form:input path="modifyTokens" type="text" /></td><td><form:errors path="modifyTokens" /></td></tr>
		    <tr><td>Standard Currency (${accountCurrencyStandard.currency})</td><td><form:input path="modifyCurrencyStandard" type="text" /></td><td><form:errors path="modifyCurrencyStandard" /></td></tr>
		    <tr><td>Hardcore Currency (${accountCurrencyHardcore.currency})</td><td><form:input path="modifyCurrencyHardcore" type="text" /></td><td><form:errors path="modifyCurrencyHardcore" /></td></tr>
		    <tr><td>Ironborn Currency (${accountCurrencyIronborn.currency})</td><td><form:input path="modifyCurrencyIronborn" type="text" /></td><td><form:errors path="modifyCurrencyIronborn" /></td></tr>
		    <tr><td>Extreme Currency (${accountCurrencyExtreme.currency})</td><td><form:input path="modifyCurrencyExtreme" type="text" /></td><td><form:errors path="modifyCurrencyExtreme" /></td></tr>
		    <tr><td>On Holiday</td><td><form:checkbox  path="onHoliday" value="${onHoliday}" /></td><td><form:errors path="onHoliday" /></td></tr>
		    <tr><td>Debug Mode</td><td><form:checkbox  path="debugMode" value="${debugMode}" /></td><td><form:errors path="debugMode" /></td></tr>
		    <tr><td>Touch Screen</td><td>${account.touchScreen}</td><td></td></tr>
		    <tr>
	        	<td>Privileges</td>
	        	<td>
		        	<form:checkbox path="privView" value="true" id="privView" /><label for="privView">View</label>
		        	<form:checkbox path="privModify" value="true" id="privModify" /><label for="privModify">Modify</label>
		        	<form:checkbox path="privDelete" value="true" id="privDelete" /><label for="privDelete">Delete</label>
	   			</td>
	   			<td></td>
	   		</tr>
	   		<sec:authorize access="hasRole('Modify')">
		    <tr><td>&nbsp;</td><td colspan="2"><input name="update" type="submit" value="Update"/></td></tr>
		    </sec:authorize>
	    </form:form>
    </table>
    <span>
    <a href="${pageContext.request.contextPath}/admin/account/${accountForm.id}/characters">Characters</a>
     &nbsp;|&nbsp;
    <a href="${pageContext.request.contextPath}/admin/account/${accountForm.id}/dungeons">Dungeons</a>
     &nbsp;|&nbsp;
    <a href="${pageContext.request.contextPath}/admin/account/currencyaudit/${accountForm.id}">Currency Audit</a>
     &nbsp;|&nbsp;
     <form method="GET" action="${pageContext.request.contextPath}/admin/account/${accountForm.id}/inventory">
    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      	<input name="hardcore" type="checkbox" value="true" /><label for="hardcore">Hardcore</label>  
        <input name="ironborn" type="checkbox" value="true"/><label for="ironborn">Ironborn</label>
        <input name="submit" type="submit" value="Inventory"/>
    </form>
    </span>
    <span>
    <form method="GET" action="${pageContext.request.contextPath}/admin/account">
    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      	<input name="id" type="hidden" value="${accountForm.id}" /><br />  
        <input name="list" type="submit" value="Back"/>
    </form>
    </span>
    </span>
    <span style="float: left;">
    	<h1>Send Message</h1>
    	<label for="message">Message:</label>
		<form method="POST" action="${pageContext.request.contextPath}/admin/account/${accountForm.id}/message">
	    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	      	<textarea name="message" rows="20" cols="40" maxlength="1024"></textarea>
	      	<br />  
	        <input name="sendMessage" type="submit" value="Send Message"/>
	    </form>
    </span>
    <span style="float: left;">
    	Tokens that get replaced:<br />
    	&lt;DISPLAYNAME&gt;<br />
    	&lt;USERNAME&gt;<br />
    	&lt;EMAIL&gt;<br />
    	<br />
    	Accepts HTML<br />
    	Note: Use &lt;br /&gt; for new line
    </span>
    </span>
</body>
</html>
