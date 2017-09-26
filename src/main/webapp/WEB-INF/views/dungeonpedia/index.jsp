<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>Dungeon-Pedia</title>
	<script>
	$(function() {
		$( "#dungeonpediaContentInner").load("${pageContext.request.contextPath}/dungeonpedia/account");
		$( ".dungeonpediaContentFrame" ).draggable();		
		$( ".dungeonpediaContentFrame" ).resizable({
			  minWidth: 675,
			  minHeight: 250,
			  handles: "n, e, s, w, se"
		});
	});
	</script>
</head>
<body>
	<%@ include file="common.jsp" %>
	<span id="dungeonpediaContentInner" class="dungeonpediaContentInnerFrame"></span>
</body>
</html>
