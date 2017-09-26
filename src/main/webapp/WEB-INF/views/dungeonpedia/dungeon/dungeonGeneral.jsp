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
		$( "#dungeonTabGeneral" ).addClass("dungeonpediaTabSelected");
		$( "#dungeonTabAdventures" ).removeClass("dungeonpediaTabSelected");
		$( "#dungeonTabDungeons" ).removeClass("dungeonpediaTabSelected");
		$( "#dungeonTabRaids" ).removeClass("dungeonpediaTabSelected");
	});
	</script>
</head>
<body>
	<span class="scroll">
	<h1>Dungeons - General</h1>
	<p>
	Dungeons come in three types:
	</p>
	<ul>
	<li><a href="javascript: loadSpan('dungeonpediaContentInnerTab','${pageContext.request.contextPath}/dungeonpedia/dungeon/dungeon/adventures');"><font color="green">Adventures</font></a></li>
	<li><a href="javascript: loadSpan('dungeonpediaContentInnerTab','${pageContext.request.contextPath}/dungeonpedia/dungeon/dungeon/dungeons');"><font color="yellow">Dungeons</font></a></li>
	<li><a href="javascript: loadSpan('dungeonpediaContentInnerTab','${pageContext.request.contextPath}/dungeonpedia/dungeon/dungeon/raids');"><font color="red">Raids</font></a></li>
	</ul>
	<p>
	From level 4 you will always have at least the same number of dungeons available equal to your highest level character, this includes any dungeons that are being run. For example if your highest level character is level 10, and you have 4 dungeons active (adventures, dungeons or raids) you will have 6 pending dungeons available to assign characters to. If you don't run a dungeon for 24 hours, it will expire, if that means you no longer have enough dungeons, a new pending one will be created to replace it.<br />
	<br />
	All dungeons have a chance at additional items being rewarded on successful completion.<br />
	<br />
	If you kill all the monsters in a dungeon, you are successful and can claim the reward items, Xp and Dungeon Tokens <img src="${pageContext.request.contextPath}/images/token.png" alt="Tokens" title="Dungeon Tokens" class="dungeonToken" />.<br />
	Dungeon reward items are always the same level as the dungeon up to max character level (60), dungeons above max character level have an better chance at having higher quality items, but will always be max level items.<br />
	Dungeon Tokens <img src="${pageContext.request.contextPath}/images/token.png" 
		alt="Dungeon Tokens" data-qtiptitle="Dungeon Tokens" class="dungeonToken" /> are awarded based on the level of the dungeon.<br />
	<br />
	Monsters are the same level as the dungeon, even above max character level.<br />
	<br />
	Dungeons take 1 minute per level to complete. You can pay dungeon tokens to finish a dungeon immediately, it costs 1 token for every minute, or part thereof, remaining on the dungeon.<br />
	<br />
	Dungeons and Raids are created every 24 hours, or when a space becomes available (you can have a total number of dungeons equal to your highest level character).<br />
	<br />
	Dungeons and Raids have a difficulty displayed on their details page in the top right. This number is a rough guide how hard the dungeon will be.<br />
	You can adjust the level of the dungeon here to by up to 20% either up or down. This will change the item and monster levels, the number of Dungeon Tokens 
	<img src="${pageContext.request.contextPath}/images/token.png" 
		alt="Dungeon Tokens" data-qtiptitle="Dungeon Tokens" class="dungeonToken" /> and XP awarded.<br />
	If you adjust the level of a dungeon above level 60 this could affect the item reward quality and will affect the difficulty.<br />
	Increasing the level of a dungeon costs Dungeon Tokens 
	<img src="${pageContext.request.contextPath}/images/token.png" 
		alt="Dungeon Tokens" data-qtiptitle="Dungeon Tokens" class="dungeonToken" />, but reducing it is free.<br /> 
	<br />
	If the dungeons you have do not suit you, you can remove all the dungeon you are not currently running with characters by spending Dungeon Tokens 
	<img src="${pageContext.request.contextPath}/images/token.png" alt="Dungeon Tokens" data-qtiptitle="Dungeon Tokens" class="dungeonToken" /> equal to your highest character level. The empty slots will be replaced with new random dungeons (and raids if you are level 10 or higher.) 
	</p>
	<p>
	<h2>Dungeon Type Summary:</h2>
	<table class="center">
	<tr><th>Type</th><th>Party Size</th><th>Awarded XP (% of Character Level)</th><th>Monsters</th><th>Can Die</th><th>Level Unlocks</th></tr>
	<tr>
		<td style="text-align: right;" title="Level of adventures is always equal to the level of the lowest character in the party"><a href="javascript: loadSpan('dungeonpediaContentInnerTab','${pageContext.request.contextPath}/dungeonpedia/dungeon/dungeon/adventures');"><font color="green">Adventure</font></a></td>
		<td>${dungeonAdventure.minCharacters} to ${dungeonAdventure.maxCharacters}</td>
		<td>25%</td>
		<td>Trash<br />Elites</td>
		<td><font color="green">No</font></td>
		<td>${dungeonAdventure.minLevel}</td>
	</tr>
	<tr><td style="text-align: right;"><a href="javascript: loadSpan('dungeonpediaContentInnerTab','${pageContext.request.contextPath}/dungeonpedia/dungeon/dungeon/dungeons');"><font color="yellow">Dungeon</font></a></td><td>${dungeonDungeon.minCharacters} to ${dungeonDungeon.maxCharacters}</td><td>100%</td><td>Trash<br />Elites<br />1 Boss</td><td><font color="red">Yes</font></td><td>${dungeonDungeon.minLevel}</td></tr>
	<tr><td style="text-align: right;"><a href="javascript: loadSpan('dungeonpediaContentInnerTab','${pageContext.request.contextPath}/dungeonpedia/dungeon/dungeon/raids');"><font color="red">Raid</font></a></td><td>${dungeonRaid.minCharacters} to ${dungeonRaid.maxCharacters}</td><td>200%</td><td>Trash<br />Elites<br />2 Bosses</td><td><font color="red">Yes</font></td><td>${dungeonRaid.minLevel}</td></tr>
	</table>
	<br />
	<h2>Dungeon Type Item Reward Chances:</h2>
	<table class="center">
	<tr>
		<th>&nbsp;</th>
		<c:forEach var="quality" items="${equipmentQualities}">
		<c:if test="${quality.id > 1}">
		<th><font color="${quality.htmlColour}">${quality.niceName}</font></th>
		</c:if>
		</c:forEach>
	</tr>
	<tr><td style="text-align: right;"><a href="javascript: loadSpan('dungeonpediaContentInnerTab','${pageContext.request.contextPath}/dungeonpedia/dungeon/dungeon/adventures');"><font color="green">Adventure</font></a></td><td>24%</td><td><font color="green">40%</font></td><td>21%</td><td>11%</td><td>2%</td><td><font color="red">0%</font></td></tr>
	<tr><td style="text-align: right;"><a href="javascript: loadSpan('dungeonpediaContentInnerTab','${pageContext.request.contextPath}/dungeonpedia/dungeon/dungeon/dungeons');"><font color="yellow">Dungeon</font></a></td><td>3%</td><td>21%</td><td><font color="green">40%</font></td><td>21%</td><td>11%</td><td>2%</td></tr>
	<tr><td style="text-align: right;"><a href="javascript: loadSpan('dungeonpediaContentInnerTab','${pageContext.request.contextPath}/dungeonpedia/dungeon/dungeon/raids');"><font color="red">Raid</font></a></td><td>2%</td><td>12%</td><td><font color="green">30%</font></td><td><font color="green">30%</font></td><td>16%</td><td>8%</td></tr>
	</table>
	Note: If the dungeon is above maximum level, the chance of better quality of items is increased.<br />
	Successfully completing a <a href="javascript: loadSpan('dungeonpediaContentInnerTab','${pageContext.request.contextPath}/dungeonpedia/dungeon/dungeon/dungeons');"><font color="yellow">Dungeon</font></a> or 
	<a href="javascript: loadSpan('dungeonpediaContentInnerTab','${pageContext.request.contextPath}/dungeonpedia/dungeon/dungeon/raids');"><font color="red">Raid</font></a>
	has a chance of additional items being found, based on your magic find of characters running the dungeon. Characters have a basic magic find of <fmt:formatNumber value="${baseMagicFind/10}" pattern="#"></fmt:formatNumber>%. 
	There can be up to ${foundItemChances} additional items found.<br />
	Additionally, there is a ${boostItemDropChance/10}% chance of there being a rare <a href="javascript: loadSpansInOrder('dungeonpediaContentInner','${pageContext.request.contextPath}/dungeonpedia/equipment', 'dungeonpediaContentEquipment','${pageContext.request.contextPath}/dungeonpedia/equipment/boostItem');">Boost Item</a>.
	</p>
	</span>
</body>
</html>
