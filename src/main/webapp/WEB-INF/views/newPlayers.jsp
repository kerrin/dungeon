<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>New Players</title>
</head>
<body>
	<%@ include file="play/common.jsp" %>
	<span class="infoSpan">
	<h1>New Players</h1>
	<p>
	Hello, and thank you for helping me test my game.<br />
	<br />
	<a href="https://docs.google.com/document/d/1mwQlURvnd4waODEEebXCkRGLhUYf1yUtzyDTfxvkE8E/edit?usp=sharing">You can view a google document that details the game here</a><br/>
	<br/>
	As you can see, this is still in development. <br />
	So the interface is functional, and requires a lot of work and usability fixes. That said, please provide feedback on all the issues you are having that aren't outlined below (bugs or usability), or if you have ideas for a feature I could add to improve the game.<br />
	<br />
	To get started, <a href="${pageContext.request.contextPath}/">create you're self an account</a><br />
	<font style="background-color: green; text-decorate: bold;">Green</font> boxes can be dragged, <font style="background-color: #6090B0;  text-decorate: bold;">Blue</font> boxes can have things dropped on them<br />
	The basic game idea is, you are equipping characters and sending them to dungeons to get XP, more equipment and Dungeon Tokens.<br />
	<br />
	You get more dungeon tokens daily; The first day you log on you get 1, second day you'll get 2, and so on, but if you don't login one day, the amount you get the next time will reduce by the number of days missed.<br />
	You also get dungeon tokens from successfully completing adventures, and from dungeon and raids once they become available to you.<br />
	<br />
	If a character dies, it can be resurrected, but that costs dungeon tokens equal to their level.<br />
	<br />
	You can decide how many characters to send to a dungeon. There is a minimum number required to run a dungeon. A red border signifies the minimum number of characters, a yellow signifies optional additional characters allowed.<br />
	<br />
	There are two modes you can toggle on the main menu called "Hardcore" and "Ironborn". You can toggle these by selecting them, and they will change you to a new account using the following rules:<ul>
	<li>Hardcore - When a character dies, you can not resurrect it, but must instead be deleted!</li>
	<li>Ironborn - Once equipment has been equiped on a character, you can only remove it by salvaging it! You can not level up characters with Dungeon Tokens</li>
	</ul>
	You can play any combination of these modes, which means you can play four different modes simultaneously.<br />
	<br />
	Note: Accounts were reset on March 27th when Hardcore and Ironborn modes were added. This was unavoidable. I will try not to require a reset again.<br />
	<br />
	I will keep the following lists up to date, so check back for updates.<br />
	Recent Updates:<br />
	<ul>
	<li>Toggle on/off buttons above stash to improve performance</li>
	<li>You get awarded an item per day, per mode. These items are either equipment or boost items.</li>
	<li>You can redeem and use all Boost Items</li>
	<li>Administrators can send equipment and Boost Items in messages</li>
	<li>Notifications on achievements, characters levelling up, daily tokens</li>
	<li>Achievements</li>
	</ul>
	<br />
	To Do:<br />
	<ol>
	<li>Add shop to purchase items: Improve item quality (level based on amount paid, need 100% chance of success), Additional Tokens, Free Level Up, Free Resurrect, Improve item attribute range, Reduce Dungeon Time (based on amount paid), Increase XP for 1 day, Increase Magic Find for 1 day, Improve item level, Character Name Change</li>
	<li>Better show the characters that are in the party for the pending dungeon and move characters when a duplicate character is dragged in to the party</li>
	<li>Add '?' buttons that open dungeon-pedia at relevant place</li>
	<li>Allow party members to be drag out of a pending dungeon</li>
	<li>Email Opt-out</li>
	<li>Emails to get people to come and play - You've lost ranks on the hiscore, new features added since you last played, etc</li>
	<li>Tweets - Award a dungeon token for tweeting</li>
	<li>Purchase features - e.g. Purchase dungeon difficulty for 24 hours, resurrect all button, salvage all buttons, badly equipped finder, etc</li>
	<li>Make messages more interesting, e.g. icons for achievements, level up plays a sound and lists new skills</li>
	<li>Monster details need icons for damage types (e.g. green cloud for poison damage, a weapon for melee).</li>
	<li>Automatically stash items still in deleted messages (make sure message ID is cleared when item taken from message)</li>
	<li>Facebook Posts - Award a dungeon token for all shares</li>
	<li>Facebook Likes</li>
	<li>Change dungeon action log to page that shows the action, with controls to skip ahead, speed up, pause, or rewind the action.</li>
	<li>
		Implement Advanced Boost/Purchase Items:
		<ul>
			<li>Run Raids (assigns characters to raids)</li>
			<li>Run Dungeons (assigns characters to dungeons)</li>
			<li>Run dungeons for me (Once every 15 minutes, assigns all idle characters to raids, dungeons and adventures)</li>
			<li>Optimise my equipment (Equips all the equipment as optimally as possible)</li>
			<li>Resurrect me(Dead characters get resurrected)</li>
			<li>Don't let me die (Characters don't die, they just quit)</li>
			<li>Salvage crap (salvage anything that is not Legendary, artifact or boost item in stash or dungeon rewards)</li>
			<li>Close my dungeons (closes all dungeons only keeping the best gear)</li>
		</ul>
	</li>
	<li>Tutorial</li>
	</ol>	
	<br />
	Known Bugs:<br />
	<ul>
	<li>Adventure, Dungeon, Raid achievements awarding incorrectly from adventures (sequentially).</li>
	<li>Putting the same character in a party multiple times causes database rollback error.</li>
	<li>Moving boost items doesn't update qtip pop ups with the move.</li>
	<li>Enchanting items should not roll an attribute already on the item.</li>
	<li>Common armour should roll, base, def and armour only always.</li>
	<li>Higher level dungeons need to be harder. Level 72 raids should require perfect equipment and 10 characters.</li>
	<li>Dungeon-pedia spell page not working on live, but works fine locally</li>
	<li>Icons for boost items currently using mostly textual place holder graphics.</li>
	<li>Completed dungeons sometimes reload as incomplete dungeons, meaning you can't move equipment or close them (particularly on slower live server).</li>
	<li>Moving items off of or on to characters needs to update the summary numbers on the item details</li>
	<li>No scroll bars appear on touch screen devices</li>
	<li>Expired dungeons show the "Stash & Close" button on the dungeon list page when they change from pending dungeons</li>
	<li>Touch screen devices particularly slow with compare items enabled</li>
	<li>Ironborn not moving items to salvage to replace (with confirm) or pop up saying why.</li>
	<li>Changing between desktop and touch screen doesn't always swap correctly, causing equipment pop ups to not appear.</li>
	<li>Dungeons don't always add found loot before auto reload of dungeon panel (especially on slow live server)</li>
	<li>Some concurrency issues, like moving items too quick causing one to disappear, or closing a dungeon twice causing an error page</li>
	</ul>
	<br />
	Things I'm considering add/removing/changing:<br />
	<ol>
		<li>Sort Dungeon List by Type, Level/XP,Finish, Finished type (New, Pending, Dead characters) (jQuery plugin)</li>
		<li>Sort stash by usage level, instead of actual level</li>
		<li>Button to sort stash by item type first</li>
		<li>Closing dungeon goes to next finished dungeon instead of dungeon list</li>
		<li>Display count down timer until free resurrections and daily tokens</li>
		<li>Salvage All option for rarity types of equipment - Includes items in finished dungeons</li>
		<li>Maybe add a way to quickly salvage multiple items (eg enter salvage mode, and click on item salvages without confirmation)</li>
		<li>Craft items - Select character slot and level - bad chance at high quality</li>
		<li>Show bad equipment highlights equipment not for that character</li>
		<li>Allow account reset for each mode - New Highscores</li>
		<li>Allow players to set a skill rotation for each character, instead of the random skill usage they currently do.</li>
		<li>Google login</li>
		<li>Make graphical (instead of text) representations of Dungeon Types.</li>
		<li>Make the following links graphical buttons: Level Up, Resurrect, Create New Character, Run Adventure, View Dungeon, Purchase Dungeon Tokens (needs adding to menu bar)</li>
		<li>Better Graphics</li>
		<li>Make all the panels draggable, resizable (on desktop only unless I can fix the issue on iOS)</li>
		<li>Player tool to message other players</li>
		<li>Make it easier to Run Adventure if you are already in dungeon details</li>
	</ul>
	<br />
	Questions, and feedback to <a href="mailto:kerrin.hardy@gmail.com?subject=Dungeon Game">kerrin.hardy@gmail.com</a>
	</p>
	</span>
</body>
</html>
