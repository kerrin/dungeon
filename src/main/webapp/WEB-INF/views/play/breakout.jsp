<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Dungeon</title>
<script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery-ui.min.js"></script>
<script src="${pageContext.request.contextPath}/js/play-manipulate-dom.js"></script>
<script lang="javascript">
var charId=${charId};
var dungeonId=${dungeonId};
</script>
</head>
<body onload="reloadPage('${pageContext.request.contextPath}/play?hardcore=${hardcore}&ironborn=${ironborn}&noBreakout=true');">
<a href="${pageContext.request.contextPath}/play?hardcore=${hardcore}&ironborn=${ironborn}&noBreakout=true">Please Press Here if the page doesn't reload</a>
</body>
</html>
