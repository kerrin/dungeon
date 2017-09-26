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
		$( "#menuAccount" ).removeClass("menuOptionSelected");
		$( "#menuCharacters" ).removeClass("menuOptionSelected");
		$( "#menuDungeons" ).addClass("menuOptionSelected");
		$( "#menuEquipment" ).removeClass("menuOptionSelected");
		$( "#menuSpells" ).removeClass("menuOptionSelected");
	});
	</script>
</head>
<body>
	<span class="dungeonpediaSubMenu">
	<span id="dungeonSubMenuDungeons" class="menuOption"><a href="javascript: loadSpan('dungeonpediaContentDungeon','${pageContext.request.contextPath}/dungeonpedia/dungeon/dungeon');">Dungeons</a></span>
	<span id="dungeonSubMenuMonsters" class="menuOption"><a href="javascript: loadSpan('dungeonpediaContentDungeon','${pageContext.request.contextPath}/dungeonpedia/dungeon/monsters');">Monsters</a></span>
	</span>
	<span id="dungeonpediaContentDungeon" class="dungeonpediaContentInnerWithSubMenu"></span>
	<script>
	 $(function() {
		 loadSpan('dungeonpediaContentDungeon','${pageContext.request.contextPath}/dungeonpedia/dungeon/dungeon');
	 });
	 </script>
</body>
</html>
