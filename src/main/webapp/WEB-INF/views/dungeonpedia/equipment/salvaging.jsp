<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Attributes</title>
  <script>
	$(function() {
		$( "#equipmentSubMenuEquipment" ).removeClass("menuOptionSelected");
		$( "#equipmentSubMenuAttributes" ).removeClass("menuOptionSelected");
		$( "#equipmentSubMenuEnchanting" ).removeClass("menuOptionSelected");
		$( "#equipmentSubMenuStash" ).removeClass("menuOptionSelected");
		$( "#equipmentSubMenuSalvaging" ).addClass("menuOptionSelected");
		$( "#equipmentSubMenuBoostItem" ).removeClass("menuOptionSelected");
	});
	</script>
</head>
<body>
	<span id="dungeonpediaContentInnerFrame" class="dungeonpediaContentInner scroll">
	<h1>Salvaging</h1>
	<p>
	If you need more Dungeon Tokens <img src="${pageContext.request.contextPath}/images/token.png" alt="Dungeon Tokens" data-qtiptitle="Dungeon Tokens" class="dungeonToken" /> or just have equipment that you no longer want to keep you can salvage it.<br />
	<br />
	To salvage an item, simply drag it to the Salvage Item box at the bottom right of the screen. You will be prompted with a confirmation box that tells you how many Dungeon Tokens <img src="${pageContext.request.contextPath}/images/token.png" alt="Dungeon Tokens" data-qtiptitle="Dungeon Tokens" class="dungeonToken" /> you will get and asking you to confirm you want to salvage the item.<br />
	<br />
	If you changed your mind press 'Cancel', otherwise press 'Ok' and the item is destroyed and you are awarded the tokens for its value.<br />
	<br />
	An items salvage value is dependent on the level and quality. The better quality items salvage for more, and worse quality for less.<br />
	<br />
	Note: Once an item is salvaged it is irreversibly destroyed!
	</p>
	</span>
</body>
</html>
