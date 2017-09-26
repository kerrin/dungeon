<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Enchanting</title>
  <script>
	$(function() {
		$( "#equipmentSubMenuEquipment" ).removeClass("menuOptionSelected");
		$( "#equipmentSubMenuAttributes" ).removeClass("menuOptionSelected");
		$( "#equipmentSubMenuEnchanting" ).addClass("menuOptionSelected");
		$( "#equipmentSubMenuStash" ).removeClass("menuOptionSelected");
		$( "#equipmentSubMenuSalvaging" ).removeClass("menuOptionSelected");
		$( "#equipmentSubMenuBoostItem" ).removeClass("menuOptionSelected");
	});
	</script>
</head>
<body>
	<span id="dungeonpediaContentInnerFrame" class="dungeonpediaContentInner scroll">
	<h1>Enchanting</h1>
	<p>
	If you wish to modify an items attribute you can do so by dropping the item on the Enchant Item box at the bottom left of the page<br />
	<br />
	This will open a pop up with the full details of the item and buttons that allow you to change an attribute. Note: These buttons will not always appear on an attribute.<br />
	<br />
	The attribute types are listed, along with their current value and the possible range (in brackets)<br />
	<br />
	You can re-roll the attribute type (cost in Dungeon Tokens 
		<img src="${pageContext.request.contextPath}/images/token.png" alt="Dungeon Tokens" data-qtiptitle="Dungeon Tokens" class="dungeonToken" /> equal to level, note cursed attributes cost 5 time the level to reroll)
	This will completely re-roll the attribute, probably replacing it with a new attribute. This is all you are allowed to do if the attribute is cursed.<br />
	<br />
	You can re-roll the attribute range (cost in Dungeon Tokens 
		<img src="${pageContext.request.contextPath}/images/token.png" alt="Dungeon Tokens" data-qtiptitle="Dungeon Tokens" class="dungeonToken" /> equal to 10 times the level)
	This will re-roll the attribute value range. This is not possible if the attribute is cursed. Note: This could return a cursed value, making it impossible to re-roll the range again.
	<br />
	Artifact items get an additional attribute that rolls double value, and is never cursed. This attribute can not be enchanted.<br />
	<br />
	If the item is of <font color="Orange">Legendary</font> quality or less, and you have a valid Quality <a href="javascript: loadSpansInOrder('dungeonpediaContentInner','${pageContext.request.contextPath}/dungeonpedia/equipment', 'dungeonpediaContentEquipment','${pageContext.request.contextPath}/dungeonpedia/equipment/boostItem');">Boost Item</a>, you will be able to increase the item quality by one quality.<br />
	</p>
	</span>
</body>
</html>
