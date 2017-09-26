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
  <title>View Account Inventory</title>
  <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js"></script>
  <script>
	$(function() {
		$( "#adminMenuAccount" ).removeClass("menuOptionSelected");
		$( "#adminMenuCharacters" ).removeClass("menuOptionSelected");
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
	<h1>Inventory</h1>
	<span>Account: <a href="${pageContext.request.contextPath}/admin/account/${inventory.accountId}">${inventory.accountId}</a></span>
	<span>Hardcore: 
	<c:if test="${inventory.hardcore}">Yes</c:if>
	<c:if test="${!inventory.hardcore}">No</c:if>
	</span>
	<span>Ironborn: 
	<c:if test="${inventory.ironborn}">Yes</c:if>
	<c:if test="${!inventory.ironborn}">No</c:if>
	</span>
	<br />
	<table border="1">
	<c:forEach begin="0" end="${inventory.size-1}" varStatus="loop">
	<c:if test="${inventory.inventorySlots.containsKey(loop.index)}">
		<equip:equipmentDisplay equipment="${inventory.inventorySlots[loop.index]}" 
			        	isTouchScreen="${account.touchScreen}"
			        			/>
	</c:if>
	<c:if test="${!inventory.inventorySlots.containsKey(loop.index)}">
		<span class="item">
			&nbsp;
		</span>
	</c:if>
	</c:forEach>
    </table>
    </span>
</body>
</html>
