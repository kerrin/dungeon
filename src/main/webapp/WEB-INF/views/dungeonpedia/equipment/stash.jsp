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
		$( "#equipmentSubMenuEquipment" ).removeClass("menuOptionSelected");
		$( "#equipmentSubMenuAttributes" ).removeClass("menuOptionSelected");
		$( "#equipmentSubMenuEnchanting" ).removeClass("menuOptionSelected");
		$( "#equipmentSubMenuStash" ).addClass("menuOptionSelected");
		$( "#equipmentSubMenuSalvaging" ).removeClass("menuOptionSelected");
		$( "#equipmentSubMenuBoostItem" ).removeClass("menuOptionSelected");
	});
	</script>
</head>
<body>
	<span id="dungeonpediaContentInnerFrame" class="dungeonpediaContentInner">
	<h1>Stash</h1>
	<p>
	The stash panel is a place were you can place items to store them when they are not being worn by a character.<br />
	<br />
	If you want additional stash space, you can purchase 1 more slot at a time at a cost in Dungeon Tokens <img src="${pageContext.request.contextPath}/images/token.png" alt="Dungeon Tokens" data-qtiptitle="Dungeon Tokens" class="dungeonToken" /><br />
	<br />
	Items in the stash can be compared to items on the currently selected character in the Character Panel by hovering the mouse over it (on desktop browsers) or pressing it (on touch screen browsers)
	</p>
	</span>
</body>
</html>
