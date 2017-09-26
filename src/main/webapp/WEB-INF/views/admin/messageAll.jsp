<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Message ALL Accounts</title>
  <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js"></script>
  <script>
	$(function() {
		$( "#adminMenuAccount" ).removeClass("menuOptionSelected");
		$( "#adminMenuCharacters" ).removeClass("menuOptionSelected");
		$( "#adminMenuEquipment" ).removeClass("menuOptionSelected");
		$( "#adminMenuBoostItem" ).removeClass("menuOptionSelected");
		$( "#adminMenuDungeons" ).removeClass("menuOptionSelected");
		$( "#adminMenuCurrencyAudit" ).removeClass("menuOptionSelected");
		$( "#adminMenuTestDungeon" ).removeClass("menuOptionSelected");
		$( "#adminMenuMessageAll" ).addClass("menuOptionSelected");
		$( "#adminMenuLogout" ).removeClass("menuOptionSelected");
	});
	</script>
</head>
<body>
	<%@ include file="adminMenu.jsp" %>
	<span class="adminContent">
	<span style="float: left;">
    	<h1>Send Message To ALL Accounts</h1>
    	<label for="message">Message:</label>
		<form method="POST" action="${pageContext.request.contextPath}/admin/messageAll">
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
    	Accepts HTML
    	Note: Use &lt;br /&gt; for new line
    </span>
    </span>
</body>
</html>
