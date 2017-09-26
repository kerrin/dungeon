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
		$( "#dungeonTabGeneral" ).removeClass("dungeonpediaTabSelected");
		$( "#dungeonTabAdventures" ).removeClass("dungeonpediaTabSelected");
		$( "#dungeonTabDungeons" ).addClass("dungeonpediaTabSelected");
		$( "#dungeonTabRaids" ).removeClass("dungeonpediaTabSelected");
	});
	</script>
</head>
<body>
	<span class="scroll">
	<h1>Dungeons - Dungeon</h1>
	<ul>
		<li>Are generated randomly once you have at least one character at level ${dungeonDungeon.minLevel} or higher.</li>
		<li>Can be run by ${dungeonDungeon.minCharacters} to ${dungeonDungeon.maxCharacters} characters.</li>
		<li>The level of the dungeon is decided at the time it is created.</li>
		<li>You can see what item rewards and monsters are in the dungeon before you assign characters and start the dungeon.</li>
		<li>Dungeons have a medium chance at rewarding better items.</li>
	</ul>
	<p>
	If you kill all the monsters in a dungeon you are successful and you can claim the item rewards. Drag the items to an empty slot in your stash, on a character, or you can drag it directly to salvage. Once all the items are assigned somewhere, you will be able to close the dungeon and claim the XP, dungeon tokens, and the characters will be able to be assigned to another dungeon.<br />
	<br />
	If all the characters die before all the monsters die in a dungeon, the dungeon has failed and you only get the XP for monsters killed, all other rewards are lost.<br />
	<br />
	If any characters die in a dungeon, you will need to resurrect them on their character details panel.
	</p>
	</span>
</body>
</html>
