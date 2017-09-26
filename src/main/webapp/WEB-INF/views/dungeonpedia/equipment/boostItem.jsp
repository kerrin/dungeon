<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Equipment - Boost Item</title>
  <script>
	$(function() {
		$( "#equipmentSubMenuEquipment" ).removeClass("menuOptionSelected");
		$( "#equipmentSubMenuAttributes" ).removeClass("menuOptionSelected");
		$( "#equipmentSubMenuEnchanting" ).removeClass("menuOptionSelected");
		$( "#equipmentSubMenuStash" ).removeClass("menuOptionSelected");
		$( "#equipmentSubMenuSalvaging" ).removeClass("menuOptionSelected");
		$( "#equipmentSubMenuBoostItem" ).addClass("menuOptionSelected");
	});
	</script>
</head>
<body>
	<span id="dungeonpediaContentInnerFrame" class="dungeonpediaContentInner scroll">
	<h1>Boost Items</h1>
	<p>
	Boost Items are items that have a rare chance of dropping as bonus items in successful Dungeons and Raids.<br />
	Each character in a dungeon has a ${boostItemDropChance/10}% chance of a boost item dropping for each item found in the Dungeon or Raid.<br />
	Boost items can also be purchased in the shop for dungeon tokens.<br />
	<br />
	The following types of boost items can be found (values shown for level 60 boost items):
	<table>
	<tr>
		<th>Name</th>
		<th data-qtiptitle="The minimum level the dungeon must be for this to drop">Minimum Level</th>
		<th data-qtiptitle="Percentage chance it will be this boost item if one drops">% Chance</th>
		<th>Description of Boost Item</th>
	</tr>
	<c:forEach var="boostItemType" items="${boostItemTypes}">
	<tr data-qtiptitle="${boostItemType.maxLevelUsageInstructions}">
		<td align="right">${boostItemType.niceName}</td>
		<td align="center">${boostItemType.minimumLevel}</td>
		<td align="center">${boostItemType.dropChance/boostItemType.totalChance*100}</td>
		<td align="left">${boostItemType.maxLevelDescription}</td>
	</tr>
	</c:forEach>
	</table>
	</p>
	</span>
</body>
</html>
