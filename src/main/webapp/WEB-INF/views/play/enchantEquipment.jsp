<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="equipmentTagLib" prefix="equip"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Enchant</title>
<style>
</style>
</head>
<body>
	<span class="enchantPopUp">
		<span style="position: absolute; right: 2px; top: 2px;">
			<a href="${pageContext.request.contextPath}/play?hardcore=${hardcore}&ironborn=${ironborn}"
				onclick="location.href=this.href+'&charId='+charId+'&dungeonId='+dungeonId; return false;"
				style='text-decoration: none; font-weight: bold; color: red;'>X</a>
		</span>
		<span style="align: center; float: left">
			<%@ include file="../checkError.jsp" %>
			<h3>Enchant Equipment</h3>
			<span>
			<equip:equipmentDisplay 
			        			enchantView="true"
			        			equipment="${equipment}" 
			        			usableTypeBoost="${usableTypeBoost}"
			        			usableRangeBoost="${usableRangeBoost}"
			        			usableImproveRangeBoost="${usableImproveRangeBoost}"
			        			usableCurseBoost="${usableCurseBoost}"
			        			usableQualityBoost="${usableQualityBoost}"
        						accountApiKey="${account.apiKey}"
			        			dragable="false" 
			        			dropable="false"
			        			isTouchScreen="${account.touchScreen}"
			        			/>	
			</span>			
		</span>
	</span>
</body>
</html>
