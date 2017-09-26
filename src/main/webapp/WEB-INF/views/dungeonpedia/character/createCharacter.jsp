<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Create Character</title>
  <script>
	$(function() {
		$( "#characterSubMenuCharacters" ).removeClass("menuOptionSelected");
		$( "#characterSubMenuCreateCharacter" ).addClass("menuOptionSelected");
		$( "#characterSubMenuCharacterDetails" ).removeClass("menuOptionSelected");
		$( "#characterSubMenuCharacterClasses" ).removeClass("menuOptionSelected");
	});
	</script>
</head>
<body>
	<span id="dungeonpediaContentInnerTab" class="dungeonpediaContentInner scroll">
	<h1>Create Character</h1>
	<p>
	You can create a new character at any time. On the characters panel press the "Create New Level 1 Character" link at the bottom of the panel.<br />
	<br />
	The 'Character Details' panel will change to a 'Create Character' panel.<br />
	<br />
	In the 'Create Character' panel you will be told the Dungeon Token <img src="${pageContext.request.contextPath}/images/token.png" 
		alt="Tokens" data-qtiptitle="Dungeon Tokens" class="dungeonToken" /> cost to create a new character (you need to pay 1 token for each character you already have.)<br />
	<br />
	You will give the character a name.<br />
	<br />
	You will lastly select the character class you want the new character to be.<br />
	<br />
	Press the 'Create' button and the new character is created.
	</p>
	</span>
</body>
</html>
