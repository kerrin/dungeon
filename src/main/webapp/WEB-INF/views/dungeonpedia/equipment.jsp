<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Equipment</title>
  <script>
	$(function() {
		$( "#menuAccount" ).removeClass("menuOptionSelected");
		$( "#menuCharacters" ).removeClass("menuOptionSelected");
		$( "#menuDungeons" ).removeClass("menuOptionSelected");
		$( "#menuEquipment" ).addClass("menuOptionSelected");
		$( "#menuSpells" ).removeClass("menuOptionSelected");
	});
	</script>
</head>
<body>
	<span class="dungeonpediaSubMenu">
		<span id="equipmentSubMenuEquipment" class="menuOption"><a href="javascript: loadSpan('dungeonpediaContentEquipment','${pageContext.request.contextPath}/dungeonpedia/equipment/equipment');">Equipment</a></span>
		<span id="equipmentSubMenuAttributes" class="menuOption"><a href="javascript: loadSpan('dungeonpediaContentEquipment','${pageContext.request.contextPath}/dungeonpedia/equipment/attributes');">Attributes</a></span>
		<span id="equipmentSubMenuEnchanting" class="menuOption"><a href="javascript: loadSpan('dungeonpediaContentEquipment','${pageContext.request.contextPath}/dungeonpedia/equipment/enchanting');">Enchanting</a></span>
		<span id="equipmentSubMenuStash" class="menuOption"><a href="javascript: loadSpan('dungeonpediaContentEquipment','${pageContext.request.contextPath}/dungeonpedia/equipment/stash');">Stash</a></span>
		<span id="equipmentSubMenuSalvaging" class="menuOption"><a href="javascript: loadSpan('dungeonpediaContentEquipment','${pageContext.request.contextPath}/dungeonpedia/equipment/salvaging');">Salvaging</a></span>
		<span id="equipmentSubMenuBoostItem" class="menuOption"><a href="javascript: loadSpan('dungeonpediaContentEquipment','${pageContext.request.contextPath}/dungeonpedia/equipment/boostItem');">Boost Items</a></span>
	</span>
	<span id="dungeonpediaContentEquipment" class="dungeonpediaContentInnerWithSubMenu"></span>
	<script>
	$(function() {
		 loadSpan('dungeonpediaContentEquipment','${pageContext.request.contextPath}/dungeonpedia/equipment/equipment');
	 });
	</script>
</body>
</html>
