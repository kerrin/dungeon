<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Account</title>
  <script>
	$(function() {
		$( "#menuAccount" ).addClass("menuOptionSelected");
		$( "#menuCharacters" ).removeClass("menuOptionSelected");
		$( "#menuDungeons" ).removeClass("menuOptionSelected");
		$( "#menuEquipment" ).removeClass("menuOptionSelected");
		$( "#menuSpells" ).removeClass("menuOptionSelected");
	});
	</script>
</head>
<body>
	<span id="dungeonpediaContentAccount" class="dungeonpediaContentInner">
	<h1>Account</h1>
	<p>
	You can view your account by selecting "Account" on the main menu at the top of the page<br/>
	On the account page you can: <ul>
	<li>See how many Dungeon Tokens <img src="${pageContext.request.contextPath}/images/token.png" alt="Dungeon Tokens" data-qtiptitle="Dungeon Tokens" class="dungeonToken" /> you have in each mode</li>
	<li>See how many Dungeon Tokens you will get if you login tomorrow.</li>
	<li>Take A Holiday - So your Daily Dungeon Tokens will not reduce each day you don't login.</li>
	<li>View your Dungeon Token history (gained and spent).</li>
	<li>View your achievements.</li>
	<li>View your boost items.</li>
	<li>View your position and other players positions on the hiscore tables.</li>
	</ul>
	Daily Dungeon Tokens are awarded each day you login. The first day you login you get awarded 1 Dungeon Token.<br />
	Each consecutive day you login, the number of tokens goes up.<br />
	<br />
	If you fail to login on a day, the number of Daily Dungeon Tokens will go down, unless you have set your account as 'On Holiday'.<br />
	Setting your account as 'On Holiday' will stop the Daily Dungeon Tokens to remain unchanged.<br />
	Note that you will not receive any tokens when you login until you turn off 'On Holiday'. You will then receive Daily Dungeon Tokens when you login the following day.<br />
	The first time you login each day, you have a chance of being awarded additional items. 
	These items may be <a href="javascript: loadSpansInOrder('dungeonpediaContentInner','${pageContext.request.contextPath}/dungeonpedia/equipment', 'dungeonpediaContentEquipment','${pageContext.request.contextPath}/dungeonpedia/equipment/boostItem');">Boost Items</a> or 
	<a href="javascript: loadSpan('dungeonpediaContentInner','${pageContext.request.contextPath}/dungeonpedia/equipment');">Equipment</a>. You will be sent the item via a message if you are awarded one. 
	</p>
	</span>
</body>
</html>
