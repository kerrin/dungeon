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
		$( "#dungeonTabAdventures" ).addClass("dungeonpediaTabSelected");
		$( "#dungeonTabDungeons" ).removeClass("dungeonpediaTabSelected");
		$( "#dungeonTabRaids" ).removeClass("dungeonpediaTabSelected");
	});
	</script>
</head>
<body>
	<span class="scroll">
	<h1>Dungeons - Adventures</h1>
	<ul>
		<li>Can be started at anytime by selecting "Run Adventure" at the top of the dungeons list and selecting specific characters to send.</li>
		<li>You can send all your idle characters to Adventures by selecting "Run Adventures" at the top of the characters list.</li>
		<li>Can be run by ${dungeonAdventure.minCharacters} to ${dungeonAdventure.maxCharacters} characters.</li>
		<li>The level of the dungeon is determined by the lowest level character in the party.</li>
		<li>The dungeon monsters and rewards are generated once the dungeon is started and is determined by the number of party members running the dungeon.</li>
		<li>You will get at least one item per party member.</li>
		<li>Adventures have the lowest chance at rewarding better items. It is not possible to get an Artifact item in an adventure.</li>
		<li>Only trash and elite mobs can appear in adventures.</li>
		<li>It is not possible to fail an adventure, but characters only get XP for the monsters killed.</li>
	</ul>
	<p>
	When an adventure finishes, it is always successful and you can claim the item rewards. Drag the items to an empty slot in your stash, on a character, or you can drag it directly to salvage. Once all the items are assigned somewhere, you will be able to close the dungeon and claim the XP, dungeon tokens, and the characters will be able to be assigned to another dungeon.<br />
	<br />
	Characters that die in adventures are resurrected for free.<br />
	<br />
	Adventure dungeons will count as a dungeon when the game is deciding if it needs to generate a new dungeon based on your highest level character.
	</p>
	</span>
</body>
</html>
