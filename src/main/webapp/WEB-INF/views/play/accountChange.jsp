<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Account Update</title>
</head>
<body onload="javascript: initSummary('${pageContext.request.contextPath}','?hardcore='+(hardcore?'1':'0')+'&ironborn='+(ironborn?'1':'0'))">
	<%@ include file="common.jsp" %>
	<span class="accountSpan">
	<span class="accountTitle">
	<h1>Update Account</h1>
	</span>
	<span class="accountDetails">
	<table>
	    <form:form method="POST" action="${pageContext.request.contextPath}/play/account/modify" modelAttribute="accountPublicForm" >
	    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			<tr><td>Modify Username/Email</td><td><form:input path="username" type="text" /></td><td><form:errors path="username" /></td></tr>
		    <tr><td>Set New Password</td><td><form:input path="newPassword1" type="password" /></td><td><form:errors path="newPassword1" /></td></tr>
		    <tr><td>Repeat New Password</td><td><form:input path="newPassword2" type="password" /></td><td><form:errors path="newPassword2" /></td></tr>
		    <tr><td>Display Name</td><td><form:input path="displayName" type="text" /></td><td><form:errors path="displayName" /></td></tr>
		    <tr><td>Password <font style="color: red;">(Required for all modifications)</font></td><td><form:input path="currentPassword" type="password" /></td><td><form:errors path="currentPassword" /></td></tr>
		    <tr><td>&nbsp;</td><td colspan="2"><input name="update" type="submit" value="Modify"/></td></tr>
		</form:form>
	</table>
	</span>
	</span>

<c:if test="${accountConfigToolTips.value > 0}">	
<script>
$('[title]').qtip();
$('[data-qtiptitle]').each(function( index) {
	var thisTitle = $(this).attr('data-qtiptitle');
	$(this).attr('title', thisTitle);
	$(this).qtip({show: { <c:if test="${account.touchScreen}">event: 'touchend', </c:if>solo: true }, hide: { event: 'unfocus', distance: 100 }});
});
</script>
</c:if>
</body>
</html>
