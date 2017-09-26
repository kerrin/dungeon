<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="equipmentTagLib" prefix="equip"%>
<%@ taglib uri="dungeonEventsTagLib" prefix="dungeonEvents"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Modify Dungeon</title>
  <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js"></script>
  <script>
	$(function() {
		$( "#adminMenuAccount" ).removeClass("menuOptionSelected");
		$( "#adminMenuCharacters" ).removeClass("menuOptionSelected");
		$( "#adminMenuEquipment" ).removeClass("menuOptionSelected");
		$( "#adminMenuBoostItem" ).removeClass("menuOptionSelected");
		$( "#adminMenuDungeons" ).addClass("menuOptionSelected");
		$( "#adminMenuCurrencyAudit" ).removeClass("menuOptionSelected");
		$( "#adminMenuTestDungeon" ).removeClass("menuOptionSelected");
		$( "#adminMenuMessageAll" ).removeClass("menuOptionSelected");
		$( "#adminMenuLogout" ).removeClass("menuOptionSelected");
	});
	</script>
</head>
<body onLoad="dungeon_showMonsters()">
	<%@ include file="adminMenu.jsp" %>
	<span class="adminContent">
	<h1>Modify Dungeon</h1>
	<table style="float: left">
	    <form:form method="POST" action="${pageContext.request.contextPath}/admin/dungeon/${dungeonForm.id}" modelAttribute="dungeonForm" >
	    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			<tr><td><label for="id">Dungeon Id</label></td><td><form:input path="id" type="hidden" /><c:out value="${dungeonForm.id}"/></td></tr>
		    <tr>
	        	<td>Account Id</td>
	        	<td><form:input path="accountId" type="number" min="1" /></td>
	        	<td><form:errors path="accountId" /></td>
	        </tr>
			<tr>
				<td>Hardcore</td>
				<td><form:checkbox  path="hardcore" value="true" id="hardcore" /></td>
				<td><form:errors path="hardcore" /></td>
			</tr>
			<tr>
				<td>Ironborn</td>
				<td><form:checkbox  path="ironborn" value="true" id="ironborn" /></td>
				<td><form:errors path="ironborn" /></td>
			</tr>
	        <tr>
	        	<td>Type</td>
	        	<td>
		        	<form:select path="type" onchange="dungeon_showMonsters()">
			   			<form:options items="${dungeonTypes}" itemLabel="niceName" />
		   			</form:select>
	   			</td>
	   			<td><form:errors path="type" /></td>
	   		</tr>
	   		<tr>
	        	<td>Level</td>
	        	<td><form:input path="level" type="number" min="1" /></td>
	        	<td><form:errors path="level" /></td>
	        </tr>
	   		<tr>
	        	<td>XP Reward</td>
	        	<td><form:input path="xpReward" type="number" min="1" /></td>
	        	<td><form:errors path="xpReward" /></td>
	        </tr>
	   		<tr>
	   			<td>Started</td>
	   			<td>
	   			<fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${dungeon.started}" />
	   			<c:if test="${!empty dungeon.started}">
	   			<br />
	        	<div class="link" id="lb_link_logs">View Action</div>
	        	<script lang="javascript">registerShowDiv('lb_link_logs', 'lb_logs',{});</script>
	        	<dungeonEvents:dungeonEventsDisplay 
	        			dungeon="${dungeon}" 
	        			dungeonEvents="${dungeonEvents}"
	        			parentSpan="adminFrame"
	        			/>
	        	<c:if test="${dungeon.finished}">Finished</c:if>
	        	</c:if>
	   			</td>
            </tr>
	   		<tr>
	   			<td>Expires</td>
	   			<td><fmt:formatDate type="time" value="${dungeon.expires}" /></td>
	   		</tr>
	   		<tr>
	        	<td>Party Size</td>
	        	<td><form:input path="partySize" type="number" min="1" /></td>
	        	<td><form:errors path="partySize" /></td>
	        </tr>
	        <tr id="monster0">
	        	<td>Monster 1</td>
	        	<td>
	        		<form:select path="monsterType0">
			   			<form:options items="${monsterTypes}" itemLabel="niceName" />
		   			</form:select>
	        		<form:select path="monster0">
			   			<form:options items="${monsters}" itemLabel="niceName" />
		   			</form:select>
	        	</td>
	        	<td><form:errors path="monsterType0" /><form:errors path="monster0" /></td>
	        </tr>
	        <tr id="monster1">
	        	<td>Monster 2</td>
	        	<td>
	        		<form:select path="monsterType1">
			   			<form:options items="${monsterTypes}" itemLabel="niceName" />
		   			</form:select>
	        		<form:select path="monster1">
			   			<form:options items="${monsters}" itemLabel="niceName" />
		   			</form:select>
	        	</td>
	        	<td><form:errors path="monsterType1" /><form:errors path="monster1" /></td>
	        </tr>
	        <tr id="monster2">
	        	<td>Monster 3</td>
	        	<td>
	        		<form:select path="monsterType2">
			   			<form:options items="${monsterTypes}" itemLabel="niceName" />
		   			</form:select>
	        		<form:select path="monster2">
			   			<form:options items="${monsters}" itemLabel="niceName" />
		   			</form:select>
	        	</td>
	        	<td><form:errors path="monsterType2" /><form:errors path="monster2" /></td>
	        </tr>
	        <tr id="monster3">
	        	<td>Monster 4</td>
	        	<td>
	        		<form:select path="monsterType3">
			   			<form:options items="${monsterTypes}" itemLabel="niceName" />
		   			</form:select>
	        		<form:select path="monster3">
			   			<form:options items="${monsters}" itemLabel="niceName" />
		   			</form:select>
	        	</td>
	        	<td><form:errors path="monsterType3" /><form:errors path="monster3" /></td>
	        </tr>
	        <tr id="monster4">
	        	<td>Monster 5</td>
	        	<td>
	        		<form:select path="monsterType4">
			   			<form:options items="${monsterTypes}" itemLabel="niceName" />
		   			</form:select>
	        		<form:select path="monster4">
			   			<form:options items="${monsters}" itemLabel="niceName" />
		   			</form:select>
	        	</td>
	        	<td><form:errors path="monsterType4" /><form:errors path="monster4" /></td>
	        </tr>
	        <tr id="monster5">
	        	<td>Monster 6</td>
	        	<td>
	        		<form:select path="monsterType5">
			   			<form:options items="${monsterTypes}" itemLabel="niceName" />
		   			</form:select>
	        		<form:select path="monster5">
			   			<form:options items="${monsters}" itemLabel="niceName" />
		   			</form:select>
	        	</td>
	        	<td><form:errors path="monsterType5" /><form:errors path="monster5" /></td>
	        </tr>
	        <tr id="monster6">
	        	<td>Monster 7</td>
	        	<td>
	        		<form:select path="monsterType6">
			   			<form:options items="${monsterTypes}" itemLabel="niceName" />
		   			</form:select>
	        		<form:select path="monster6">
			   			<form:options items="${monsters}" itemLabel="niceName" />
		   			</form:select>
	        	</td>
	        	<td><form:errors path="monsterType6" /><form:errors path="monster6" /></td>
	        </tr>
	        <tr id="monster7">
	        	<td>Monster 8</td>
	        	<td>
	        		<form:select path="monsterType7">
			   			<form:options items="${monsterTypes}" itemLabel="niceName" />
		   			</form:select>
	        		<form:select path="monster7">
			   			<form:options items="${monsters}" itemLabel="niceName" />
		   			</form:select>
	        	</td>
	        	<td><form:errors path="monsterType7" /><form:errors path="monster7" /></td>
	        </tr>
	        <tr id="monster8">
	        	<td>Monster 9</td>
	        	<td>
	        		<form:select path="monsterType8">
			   			<form:options items="${monsterTypes}" itemLabel="niceName" />
		   			</form:select>
	        		<form:select path="monster8">
			   			<form:options items="${monsters}" itemLabel="niceName" />
		   			</form:select>
	        	</td>
	        	<td><form:errors path="monsterType8" /><form:errors path="monster8" /></td>
	        </tr>
	        <tr id="monster9">
	        	<td>Monster 10</td>
	        	<td>
	        		<form:select path="monsterType9">
			   			<form:options items="${monsterTypes}" itemLabel="niceName" />
		   			</form:select>
	        		<form:select path="monster9">
			   			<form:options items="${monsters}" itemLabel="niceName" />
		   			</form:select>
	        	</td>
	        	<td><form:errors path="monsterType9" /><form:errors path="monster9" /></td>
	        </tr>
	        <!-- End Item Rewards -->
		    <sec:authorize access="hasRole('Modify')">
		    <tr><td>&nbsp;</td><td colspan="2"><input name="update" type="submit" value="Update"/></td></tr>
		    </sec:authorize>
		    
		    <!-- Item Rewards -->
		    <tr>
	        	<td colspan="3">
		        	<c:forEach items="${dungeon.itemRewardsAsSet}" var="itemReward">
		        		<span style="float: left;">		        
			        		<c:if test="${dungeon.itemRewards[itemReward]}"><font color="purple">Found</font></c:if>
			        		<equip:equipmentDisplay 
			        			adminView="true" 
			        			equipment="${itemReward}"
			        			isTouchScreen="${account.touchScreen}"
	    						/>
			        	</span>
		        	</c:forEach>
	        	</td>
	        </tr>	        
	    </form:form>
    </table>
    <br style="clear:both;" />
    <form method="GET" action="${pageContext.request.contextPath}/admin/dungeon">
    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      	<input name="id" type="hidden" value="${dungeonForm.id}" /><br /> 
        <input name="list" type="submit" value="Search Dungeon"/><br />
    </form>
    </span>
</body>
</html>
