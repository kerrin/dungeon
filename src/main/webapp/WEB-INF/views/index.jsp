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
	<span class="loginSpan">
	<h1>
	Log In
	</h1>
	<span>
	<fblogin:facebookLogin facebook="${facebook}" characterId="${charId}" dungeonId="${dungeonId}" />
	<p>
		<form:form method="POST" action="${pageContext.request.contextPath}/login" modelAttribute="loginForm">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			<input type="hidden" name="touchScreen" value="javascript: isTouchDevice()" />
			<input type="hidden" name="hardcore" value="${hardcore}" />
			<input type="hidden" name="ironborn" value="${ironborn}" />
			<input type="hidden" name="charId" value="${charId}" />
			<input type="hidden" name="dungeonId" value="${dungeonId}" />
			<form:label path="username">Email Username</form:label>
			<form:input id="username" name="username" path="username" type="email" /><br>
			<form:label path="password">Password</form:label>
			<form:password id="password" name="password" path="password" /><br>
			<input type="submit" value="Login" />
		</form:form>
		<br />
		<h2>I forgot my password</h2>
		<form method="POST" action="${pageContext.request.contextPath}/login/forgotpassword">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			<label path="username">Email Username</label>
			<input id="username" name="username" path="username" type="email" /><br>			
			<input type="submit" value="Request reset password email" />
		</form>
	</p>
	</span>
	</span>
	<span class="newAccountSpan">
	<h1>Create New Account</h1>
	<span>
	<table>
    	<form:form method="POST" modelAttribute="accountCreateForm" action="${pageContext.request.contextPath}/createAccount">
    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <tr data-qtiptitle="We will only email you if you forget your password or setup email notifications">
        	<td>Email Address</td>
        	<td><form:input path="username" type="email" id="username" /></td>
        	<td><form:errors path="username" /></td>
        </tr>
        <tr>
        	<td>Password</td>
        	<td><form:input path="password" type="text" id="password" min="6" /></td>
        	<td><form:errors path="password" /></td>
   		</tr>
        <tr>
        	<td>Display Name</td>
        	<td><form:input path="displayName" type="text" id="displayName" /></td>
        	<td><form:errors path="displayName" /></td>
   		</tr>
   		<tr><td>&nbsp;</td><td colspan="2"><input name="add" type="submit" value="Register" /></td></tr>
        </form:form>
    </table>
    Or
    <fblogin:facebookLogin facebook="${facebook}" buttonText="Register" characterId="${charId}" dungeonId="${dungeonId}" />	
    </span>
    </span>
    </span>
</body>
</html>
