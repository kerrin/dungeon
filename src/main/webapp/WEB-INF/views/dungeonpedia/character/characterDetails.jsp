<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Character Details</title>
  <script>
	$(function() {
		$( "#characterSubMenuCharacters" ).removeClass("menuOptionSelected");
		$( "#characterSubMenuCreateCharacter" ).removeClass("menuOptionSelected");
		$( "#characterSubMenuCharacterDetails" ).addClass("menuOptionSelected");
		$( "#characterSubMenuCharacterClasses" ).removeClass("menuOptionSelected");
	});
	</script>
</head>
<body>
	<span id="dungeonpediaContentInnerTab" class="dungeonpediaContentInner scroll">
	<h1>Character Details</h1>
	<p>
	Selecting a character from all the characters in the 'Characters' panel will change the 'Character Details' panel to show the details of the selected character.<br />
	<br />
	Each character has:<br />
	<ul>
	<li>A <a href="javascript: loadSpan('dungeonpediaContentCharacter','${pageContext.request.contextPath}/dungeonpedia/character/characterClasses');">Class</a> - Depicted by its class symbol</li>
	<li>A name - That you gave it when you created the character</li>
	<li>A level - This is a rough guide to how strong they are</li>
	<li>XP - How much experience the character has towards the next level</li>
	<li>Actions you can perform - These change depending on the character you are looking at</li>
	<li><a href="javascript: loadSpan('dungeonpediaContentInner','${pageContext.request.contextPath}/dungeonpedia/equipment');">Equipment</a> - 13 slots (12 unique equipment types, as there are 2 ring slots)</li>
	<li>Attributes Summary - A summary of all the attributes on all the equipment the character is wearing</li>
	</ul>
	The following actions are available:<br />
	<ul>
	<li>Level Up - Increase the level of the character at a cost. The cost is equal to the level the character will be. Not available on max level characters.</li>
	<li>Resurrect - If the character is dead, you can resurrect them at a cost equal to the level of the character. Dead characters can not run dungeons.</li>
	<li>Reset Level For Prestige - Returns the character to level 1, but increases the characters prestige level. The amount of prestige level awarded depends on the character level. Note: It is more efficient to wait for later character levels before reseting. For example a level 10 character awards only 1 prestige level, but a level 60 awards 10 prestige levels.</li>
	<li>Rename - If you have a valid Rename <a href="javascript: loadSpansInOrder('dungeonpediaContentInner','${pageContext.request.contextPath}/dungeonpedia/equipment', 'dungeonpediaContentEquipment','${pageContext.request.contextPath}/dungeonpedia/equipment/boostItem');">Boost Item</a>, you can rename the character.</li>
	</ul>
	All <a href="javascript: loadSpan('dungeonpediaContentInner','${pageContext.request.contextPath}/dungeonpedia/equipment');">Equipment</a> has a type (e.g. Head) that determines which equipment slot it can be put in. A character also needs to be at least as higher level as the equipments level requirement level.<br/>
	Once a day all dead characters, that are not currently in a dungeon, will get a free resurrection. If all your characters are dead, the lowest level character will get a free resurrection.
	</p>
	</span>
</body>
</html>
