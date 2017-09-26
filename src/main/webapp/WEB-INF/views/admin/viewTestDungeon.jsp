<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="characterTagLib" prefix="character"%>
<%@ taglib uri="monsterTagLib" prefix="monster"%>
<%@ taglib uri="dungeonEventsTagLib" prefix="dungeonEvents"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>View Test Dungeon</title>
  <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js"></script>
  <script>
	$(function() {
		$( "#adminMenuAccount" ).removeClass("menuOptionSelected");
		$( "#adminMenuCharacters" ).removeClass("menuOptionSelected");
		$( "#adminMenuEquipment" ).removeClass("menuOptionSelected");
		$( "#adminMenuBoostItem" ).removeClass("menuOptionSelected");
		$( "#adminMenuDungeons" ).removeClass("menuOptionSelected");
		$( "#adminMenuCurrencyAudit" ).removeClass("menuOptionSelected");
		$( "#adminMenuTestDungeon" ).addClass("menuOptionSelected");
		$( "#adminMenuMessageAll" ).removeClass("menuOptionSelected");
		$( "#adminMenuLogout" ).removeClass("menuOptionSelected");
	});
	</script>
</head>
<body>
	<%@ include file="adminMenu.jsp" %>
	<span class="adminContent">
	<h1>Test Dungeon Details</h1>
	<table style="float: left">
		<tr>
        	<td colspan="2">
        	Level ${dungeon.level} ${dungeon.type}
        	<div class="link" id="lb_link_logs">View Action</div>
        	<script lang="javascript">registerShowDiv('lb_link_logs', 'lb_logs',{});</script>
        	<div id="lb_logs" class="lightbox dungeonLogs">
        		<div style="float: left;">
        		<dungeonEvents:dungeonEventsDisplay 
        				dungeon="${dungeon}" 
        				dungeonEvents="${dungeonEvents}"
	        			parentSpan="adminFrame" />
        		</div>
        		<div style="float: right;">
        			<a href="javascript: hideDivById('lb_logs');" style="text-decoration: none; font-weight: bold; color: red;">X</a>
        		</div>
        	</div>
        	</td>
        </tr>
   		<tr>
        	<td>XP Reward</td>
        	<td>${dungeon.xpReward}</td>
        </tr>
        <tr>
        	<td colspan="2"><b>Party</b></td>
        </tr>
        <tr>
        	<td colspan="2">
        	<c:forEach items="${dungeon.characters}" var="character">
        		<character:characterDisplay 
		          	character="${character}"
			        dragable="false"
			        	isTouchScreen="${account.touchScreen}"
			        />
        	</c:forEach>
			</td>
        </tr>
        <tr>
        	<td colspan="2"><b>Monsters</b></td>
        </tr>
        <tr>
        	<td colspan="2">
	        <c:forEach items="${dungeon.monstersAsList}" var="monster">
        		<monster:monsterDisplay 
        			spanId="${monster.name}"
        			monster="${monster}" 
        			monsterType="${dungeon.monsters[monster]}" 
        			level="${dungeon.level}" />
	        </c:forEach>
        	</td>
       	</tr>
    </table>
    </span>
</body>
</html>
