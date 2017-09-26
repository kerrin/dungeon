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
	Forgot Password
	</h1>
	<span>
	${message}
	</span>
	</span>
</body>
</html>