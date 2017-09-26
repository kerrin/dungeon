<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Equipment - Equipment</title>
  <script>
	$(function() {
		$( "#equipmentSubMenuEquipment" ).addClass("menuOptionSelected");
		$( "#equipmentSubMenuAttributes" ).removeClass("menuOptionSelected");
		$( "#equipmentSubMenuEnchanting" ).removeClass("menuOptionSelected");
		$( "#equipmentSubMenuStash" ).removeClass("menuOptionSelected");
		$( "#equipmentSubMenuSalvaging" ).removeClass("menuOptionSelected");
		$( "#equipmentSubMenuBoostItem" ).removeClass("menuOptionSelected");
	});
	</script>
</head>
<body>
	<span id="dungeonpediaContentInnerFrame" class="dungeonpediaContentInner scroll">
	<h1>Equipment</h1>
	<p>
	Equipment has:
	<ul>
	<li>A level - Which determines what level character can wear it (this can be modified by an attribute) and how strong the attributes are.</li>
	<li>A type - Which determines which item slot on the character it can be placed in</li> 
	<li>A quality - Which determines how many attributes it has</li>
	<li><a href="javascript: loadSpan('dungeonpediaContentEquipment','${pageContext.request.contextPath}/dungeonpedia/equipment/attributes');">Attributes</a> - Randomly generated attributes with random ranges. Note: Equipment will always have a guaranteed attribute type. e.g. A weapon has a damage attribute and armour has armour, etc</li>
	<li>Power Summary - How much Attack (A), Defence (D) and Recovery (R) power the item gives. If the item is on a character, these values are calculated at the level of the character, otherwise they are at the item level. Note: If a item is not on a character the values may have a '+' in, the number before the plus affects all characters, and the number after only effects specific classes (e.g. Strength only affects the attack value of Barbarians, Paladins and Hard Bards). Also note, that items worn by characters have this value combined to a single value if it is the correct class.</li>
	<li><a href="javascript: loadSpan('dungeonpediaContentEquipment','${pageContext.request.contextPath}/dungeonpedia/equipment/salvaging');">Salvage</a> Value - How many Dungeon Tokens <img src="${pageContext.request.contextPath}/images/token.png" alt="Dungeon Tokens" data-qtiptitle="Dungeon Tokens" class="dungeonToken" /> salvaging would give you</li>
	</ul>
	Equipment quality can be:
	<ul>
	<c:forEach var="quality" items="${equipmentQualities}">
	<li><font color="${quality.htmlColour}">${quality.niceName}</font> - ${quality.id} Attributes<c:if test="${quality.id == 7}"><font color="DarkGreen"> Plus 1 Double Power Attribute</font></c:if></li>
	</c:forEach>
	</ul>
	<br />
	Artifact items get an additional attribute that rolls double value. This attribute can not be enchanted.<br />
	Equipment on successfully completed dungeons, in your stash or on a character can be dragged and dropped to:
	<ul>
		<li>The <a href="javascript: loadSpan('dungeonpediaContentEquipment','${pageContext.request.contextPath}/dungeonpedia/equipment/stash');">Stash</a> - If the Stash is not an empty slot the item will swap places with the dragged item, if the item in the Stash can be put where the dragged item was.</li>
		<li>A valid character details slot for the equipment - If the slot was not empty the items will swap places. Dragging items from a completed dungeon to an non-empty character slot will put the existing item in your stash (Ironborn mode will salvage the existing item instead)</li>
		<li><a href="javascript: loadSpan('dungeonpediaContentEquipment','${pageContext.request.contextPath}/dungeonpedia/equipment/salvaging');">Salvage</a> - The item will be converted to Dungeon Tokens <img src="${pageContext.request.contextPath}/images/token.png" alt="Dungeon Tokens" data-qtiptitle="Dungeon Tokens" class="dungeonToken" /> after confirmation. Higher level and higher quality items salvage for more tokens.</li>
		<li><a href="javascript: loadSpan('dungeonpediaContentEquipment','${pageContext.request.contextPath}/dungeonpedia/equipment/enchanting');">Enchanting</a> - A pop up to enchant the equipment will appear.</li>		
	</ul>
	</p>
	</span>
</body>
</html>
