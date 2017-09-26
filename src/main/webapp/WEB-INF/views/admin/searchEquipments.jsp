<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Search Characters</title>
  <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js"></script>
  <script>
	$(function() {
		$( "#adminMenuAccount" ).removeClass("menuOptionSelected");
		$( "#adminMenuCharacters" ).removeClass("menuOptionSelected");
		$( "#adminMenuEquipment" ).addClass("menuOptionSelected");
		$( "#adminMenuBoostItem" ).removeClass("menuOptionSelected");
		$( "#adminMenuDungeons" ).removeClass("menuOptionSelected");
		$( "#adminMenuCurrencyAudit" ).removeClass("menuOptionSelected");
		$( "#adminMenuTestDungeon" ).removeClass("menuOptionSelected");
		$( "#adminMenuMessageAll" ).removeClass("menuOptionSelected");
		$( "#adminMenuLogout" ).removeClass("menuOptionSelected");
	});
	</script>
</head>
<body>
	<%@ include file="adminMenu.jsp" %>
	<span class="adminContent">
	<form:form method="POST" modelAttribute="equipmentForm" action="${pageContext.request.contextPath}/admin/equipment">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		<input name="add" type="submit" value="Add New" />
	</form:form>
	<form:form method="GET" action="${pageContext.request.contextPath}/admin/equipment" modelAttribute="equipmentSearchForm">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		<label for="id">Equipment Id</label>
		<form:input type="number" path="id" />
		<input type="submit" name="find" value="Find" />
	</form:form>
	<hr />
	<table border="1">
	<tr>
        <th>Equipment ID</th>
        <th>Level</th>
        <th>Hardcore</th>
        <th>Ironborn</th>
        <th>Quality</th>  
        <th>Attributes</th>
        <th>Equipment Type</th>
        <th>Action</th>
    </tr>
    <tr>
    	<form:form method="GET" modelAttribute="equipmentSearchForm" action="${pageContext.request.contextPath}/admin/equipment">
    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <td><b>Search</b></td>
        <td><form:input path="greaterThanLevel" type="number" min="-1" max="9999" maxlength="4" /><form:errors path="greaterThanLevel" /> to <form:input path="lessThanLevel" type="number" min="-1" max="9999" maxlength="4" /><form:errors path="lessThanLevel" /></td>
        <td>
        	<form:select path="hardcore">
	   			<form:options items="${booleanOptions}" itemLabel="name" />
   			</form:select>
   		</td>
        <td>
        	<form:select path="ironborn">
	   			<form:options items="${booleanOptions}" itemLabel="name" />
   			</form:select>
   		</td>
        <td>
        	<form:select path="quality">
	   			<form:options items="${equipmentQualities}" itemLabel="niceName" />
   			</form:select>
   		</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td><input name="search" type="submit" value="Search" /></td>
        </form:form>
    </tr>
	<c:forEach items="${equipments}" var="equipment">
        <tr>
            <td><a href="${pageContext.request.contextPath}/admin/equipment/${equipment.id}">${equipment.id}</a></td>
            <td>${equipment.level}</td>
            <td>${equipment.hardcore}</td>
            <td>${equipment.ironborn}</td>
            <td>${equipment.quality.niceName}</td>
            <td>Attributes</td>
            <td>${equipment.equipmentType.niceName}</td>
            <td>
            	<form method="GET" action="${pageContext.request.contextPath}/admin/equipment/${equipment.id}">
            		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            		<input type="submit" name="update" value="Update" />
            	</form>
            </td>
        </tr>
    </c:forEach>
    </table>
    </span>
</body>
</html>
