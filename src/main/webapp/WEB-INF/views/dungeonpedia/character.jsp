<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Character</title>
  <script>
	$(function() {
		$( "#menuAccount" ).removeClass("menuOptionSelected");
		$( "#menuCharacters" ).addClass("menuOptionSelected");
		$( "#menuDungeons" ).removeClass("menuOptionSelected");
		$( "#menuEquipment" ).removeClass("menuOptionSelected");
		$( "#menuSpells" ).removeClass("menuOptionSelected");
	});
	</script>
</head>
<body>
	<span class="dungeonpediaSubMenu">
		<span id="characterSubMenuCharacters" class="menuOption"><a href="javascript: loadSpan('dungeonpediaContentCharacter','${pageContext.request.contextPath}/dungeonpedia/character/characters');">Characters</a></span>
		<span id="characterSubMenuCreateCharacter" class="menuOption"><a href="javascript: loadSpan('dungeonpediaContentCharacter','${pageContext.request.contextPath}/dungeonpedia/character/createCharacter');">Create Character</a></span>
		<span id="characterSubMenuCharacterDetails" class="menuOption"><a href="javascript: loadSpan('dungeonpediaContentCharacter','${pageContext.request.contextPath}/dungeonpedia/character/characterDetails');">Character Details</a></span>
		<span id="characterSubMenuCharacterClasses" class="menuOption"><a href="javascript: loadSpan('dungeonpediaContentCharacter','${pageContext.request.contextPath}/dungeonpedia/character/characterClasses');">Character Classes</a></span>
	</span>
	<span id="dungeonpediaContentCharacter" class="dungeonpediaContentInnerWithSubMenu"></span>
	<script>
	$(function() {
		 loadSpan('dungeonpediaContentCharacter','${pageContext.request.contextPath}/dungeonpedia/character/characters');
	 });
	</script>
</body>
</html>
