<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="fbloginTagLib" prefix="fblogin"%>
<html lang="en">
<head>
  	<meta charset="utf-8">
  	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>Dungeon</title>
</head>
<body>
	<%@ include file="play/common.jsp" %>
	<%@ include file="checkError.jsp" %>
	<span class="infoSpan">
	<h1>
	Reset Password for ${displayName} (${username})
	</h1>
	<span>
		<form:form method="POST" action="${pageContext.request.contextPath}/login/resetpassword" modelAttribute="resetPasswordForm">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			<form:hidden id="key" name="key" path="key" />
			<form:label path="password1">New Password</form:label>
			<form:password id="password1" name="password1" path="password1" /><br>
			<form:label path="password2">Re-enter Password</form:label>
			<form:password id="password2" name="password2" path="password2" /><br>
			<input type="submit" value="Change Password" />
		</form:form>
	</span>
	</span>
</body>
</html>