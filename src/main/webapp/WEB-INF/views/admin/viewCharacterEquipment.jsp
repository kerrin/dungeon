<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="equipmentTagLib" prefix="equip"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>View Character Equipment</title>
  <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js"></script>
  <script>
	$(function() {
		$( "#adminMenuAccount" ).removeClass("menuOptionSelected");
		$( "#adminMenuCharacters" ).addClass("menuOptionSelected");
		$( "#adminMenuEquipment" ).removeClass("menuOptionSelected");
		$( "#adminMenuBoostItem" ).removeClass("menuOptionSelected");
		$( "#adminMenuDungeons" ).removeClass("menuOptionSelected");
		$( "#adminMenuCurrencyAudit" ).removeClass("menuOptionSelected");
		$( "#adminMenuTestDungeon" ).removeClass("menuOptionSelected");
		$( "#adminMenuMessageAll" ).removeClass("menuOptionSelected");
		$( "#adminMenuLogout" ).removeClass("menuOptionSelected");
		
		$("#outerSpan_adminFrame").empty();
	});
	</script>
</head>
<body>
	<%@ include file="adminMenu.jsp" %>
	<span class="adminContent">
	<h1>Character Id: <a href="${pageContext.request.contextPath}/admin/character/${characterId}">${characterId}</a></h1>
	<span>
	Hardcore:
	<c:if test="${hardcore}">Yes</c:if>
	<c:if test="${!hardcore}">No</c:if>
	</span>
	<span>
	Ironborn:
	<c:if test="${ironborn}">Yes</c:if>
	<c:if test="${!ironborn}">No</c:if>
	</span>
	<table>
	    <tr>
	    	<td>
	    		<label>SHOULDERS</label><br />
	    		<equip:equipmentDisplay 
	    			adminView="true" 
	    			equipment="${characterEquipment['SHOULDERS']}"
			        	isTouchScreen="${account.touchScreen}"
	    			/>
	    	</td>
	    	<td>
	    		<label>HEAD</label><br />
	    		<equip:equipmentDisplay 
	    			adminView="true" 
	    			equipment="${characterEquipment['HEAD']}"
			        	isTouchScreen="${account.touchScreen}"
	    			/>
	    	</td>
	    	<td>&nbsp;</td>
	    </tr>
	    <tr>
	    	<td>&nbsp;</td>
	    	<td>
	    		<label>AMULET</label><br />
	    		<equip:equipmentDisplay 
	    			adminView="true" 
	    			equipment="${characterEquipment['AMULET']}"
			        	isTouchScreen="${account.touchScreen}"
	    			/>
	    	</td>
	    	<td>
	    		<label>BROACH</label><br />
	    		<equip:equipmentDisplay  
	    			adminView="true" 
	    			equipment="${characterEquipment['BROACH']}"
			        	isTouchScreen="${account.touchScreen}"
	    			/>
	    	</td>
	    </tr>
	    <tr>
	    	<td>
	    		<label>RING</label><br />
	    		<equip:equipmentDisplay  
	    			adminView="true" 
	    			equipment="${characterEquipment['RING_LEFT']}"
			        	isTouchScreen="${account.touchScreen}"
	    			/>
	    	</td>
	    	<td>
	    		<label>CHEST</label><br />
	    		<equip:equipmentDisplay  
	    			adminView="true" 
	    			equipment="${characterEquipment['CHEST']}"
			        	isTouchScreen="${account.touchScreen}"
	    			/>
	    	</td>
	    	<td>
	    		<label>RING</label><br />
	    		<equip:equipmentDisplay  
	    			adminView="true" 
	    			equipment="${characterEquipment['RING_RIGHT']}"
			        	isTouchScreen="${account.touchScreen}"
	    			/>
	    	</td>
	    </tr>	    
	    <tr>
	    	<td>
	    		<label>BRACERS</label><br />
	    		<equip:equipmentDisplay  
	    			adminView="true" 
	    			equipment="${characterEquipment['BRACERS']}"
			        	isTouchScreen="${account.touchScreen}"
	    			/>
	    	</td>
	    	<td>
	    		<label>LEGS</label><br />
	    		<equip:equipmentDisplay  
	    			adminView="true" 
	    			equipment="${characterEquipment['LEGS']}"
			        	isTouchScreen="${account.touchScreen}"
	    			/>
	    	</td>
	    	<td>
	    		<label>HANDS</label><br />
	    		<equip:equipmentDisplay  
	    			adminView="true" 
	    			equipment="${characterEquipment['HANDS']}"
			        	isTouchScreen="${account.touchScreen}"
	    			/>
	    	</td>
	    </tr>    
	    <tr>
	    	<td>
	    		<label>WEAPON</label><br />
	    		<equip:equipmentDisplay  
	    			adminView="true" 
	    			equipment="${characterEquipment['MAIN_WEAPON']}"
			        	isTouchScreen="${account.touchScreen}"
	    			/>
	    	</td>
	    	<td>
	    		<label>FEET</label><br />
	    		<equip:equipmentDisplay  
	    			adminView="true" 
	    			equipment="${characterEquipment['FEET']}"
			        	isTouchScreen="${account.touchScreen}"
	    			/>
	    	</td>
	    	<td>
	    		<label>OFF-HAND</label><br />
	    		<equip:equipmentDisplay  
	    			adminView="true" 
	    			equipment="${characterEquipment['OFF_HAND']}"
			        	isTouchScreen="${account.touchScreen}"
	    			/>
	    	</td>
	    </tr>	        
    </table>
    <form method="GET" action="${pageContext.request.contextPath}/admin/character/${characterId}">
    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      	<input name="list" type="submit" value="Back"/>
    </form>
    </span>
</body>
</html>
