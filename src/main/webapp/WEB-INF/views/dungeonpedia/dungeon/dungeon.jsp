<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Dungeon</title>
  <script>
	$(function() {
		$( "#dungeonSubMenuDungeons" ).addClass("menuOptionSelected");
		$( "#dungeonSubMenuMonsters" ).removeClass("menuOptionSelected");
	});
	</script>
</head>
<body>
	<span class="dungeonpediaTab">
	<ul>
		<li id="dungeonTabGeneral" class="dungeonpediaTabSelected"><a href="javascript: loadSpan('dungeonpediaContentInnerTab','${pageContext.request.contextPath}/dungeonpedia/dungeon/dungeon/general');">General</a></li>
		<li id="dungeonTabAdventures"><a href="javascript: loadSpan('dungeonpediaContentInnerTab','${pageContext.request.contextPath}/dungeonpedia/dungeon/dungeon/adventures');">Adventures</a></li>
		<li id="dungeonTabDungeons"><a href="javascript: loadSpan('dungeonpediaContentInnerTab','${pageContext.request.contextPath}/dungeonpedia/dungeon/dungeon/dungeons');">Dungeons</a></li>
		<li id="dungeonTabRaids"><a href="javascript: loadSpan('dungeonpediaContentInnerTab','${pageContext.request.contextPath}/dungeonpedia/dungeon/dungeon/raids');">Raids</a></li>
	</ul>
	</span>
	<span id="dungeonpediaContentInnerTab" class="dungeonpediaContentInnerTab"></span>
	<script>
	 $(function() {
		 loadSpan('dungeonpediaContentInnerTab','${pageContext.request.contextPath}/dungeonpedia/dungeon/dungeon/general');
	 });
	 </script>
</body>
</html>
