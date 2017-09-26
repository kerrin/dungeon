<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Characters</title>
  <script>
	$(function() {
		$( "#characterSubMenuCharacters" ).addClass("menuOptionSelected");
		$( "#characterSubMenuCreateCharacter" ).removeClass("menuOptionSelected");
		$( "#characterSubMenuCharacterDetails" ).removeClass("menuOptionSelected");
		$( "#characterSubMenuCharacterClasses" ).removeClass("menuOptionSelected");
	});
	</script>
</head>
<body>
	<span id="dungeonpediaContentInnerFrame" class="dungeonpediaContentInner scroll">
	<h1>Characters</h1>
	<p>
	Characters have:<br />
	<ul>
	<li>A level - A rough guide to how strong they are</li>
	<li>A character class - Affects what attributes are strong for them and what spells/skills they can use</li>
	<li>Equipment - Boosts attributes. There are 13 slots for equipment on each character.</li>
	<li>XP - How much experience the character has earnt towards the next level. When the XP bar is full, they gain a level and the XP resets to 0. Note: Max level characters can no longer earn XP.</li>
	</ul>
	<br />
	While characters are running a dungeon they also have:
	<ul>
	<li>Health - When they take damage, this depletes, healing replenishes, if it drops to 0 they die.</li>
	<li>Mana - Used to cast spells and use skills. Using a skill reduces this amount. They must have enough mana to use the spell/skill.</li>
	<li>Buffs - Spells cast on the character that last a few rounds that temporarily increase a players attributes.</li>
	<li>De-buffs - Same as a buff, but cast by monsters and reduces attributes.</li>
	</ul>
	<br />
	You can filter the classes of characters shown in the Characters panel by toggling the small class icons at the top of the Characters panel.<br />
	<br />
	You can drag characters to the party boxes of the Dungeon Details or Run Adventure panels to add them to the party that will run the dungeon.
	</p>
	</span>
</body>
</html>
