<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Search Account Currency Audits</title>
  <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js"></script>
  <script>
	$(function() {
		$( "#adminMenuAccount" ).removeClass("menuOptionSelected");
		$( "#adminMenuCharacters" ).removeClass("menuOptionSelected");
		$( "#adminMenuEquipment" ).removeClass("menuOptionSelected");
		$( "#adminMenuBoostItem" ).removeClass("menuOptionSelected");
		$( "#adminMenuDungeons" ).removeClass("menuOptionSelected");
		$( "#adminMenuCurrencyAudit" ).addClass("menuOptionSelected");
		$( "#adminMenuTestDungeon" ).removeClass("menuOptionSelected");
		$( "#adminMenuMessageAll" ).removeClass("menuOptionSelected");
		$( "#adminMenuLogout" ).removeClass("menuOptionSelected");
	});
	</script>
</head>
<body>
	<%@ include file="adminMenu.jsp" %>
	<span class="adminContent">
	<table>
	<tr>
        <th>Account Id</th>
        <th>Hardcore</th>
        <th>Ironborn</th>
        <th colspan="2">Date</th>  
        <th>Currency Change</th>
        <th>Reference</th>
    </tr>
    <tr>
		<form:form method="GET" modelAttribute="accountCurrencyAuditSearchForm" action="${pageContext.request.contextPath}/admin/account/currencyaudit">
    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <td><form:input path="accountId" type="number" /><form:errors path="accountId" /></td>
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
        <td><form:input path="startDate" type="date" /><form:errors path="startDate" /></td>
        <td><form:input path="endDate" type="date" /><form:errors path="endDate" /></td>
		<td>&nbsp;</td>
        <td><input name="search" type="submit" value="Search" /></td>
        </form:form>
    </tr>
	<c:forEach items="${audits}" var="audit">
        <tr>
            <td><a href="${pageContext.request.contextPath}/admin/account/${audit.accountId}">${audit.accountId}</a></td>
            <td>${audit.hardcore}</td>
            <td>${audit.ironborn}</td>
            <td colspan="2"><fmt:formatDate value="${audit.timestamp}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
            <td>
            	<c:if test="${audit.currency > 0}"><font color="green"></c:if>
            	<c:if test="${audit.currency < 0}"><font color="red"></c:if>
            	${audit.currency}
            	</font>
            </td>
            <td>${audit.reference}</td>
        </tr>
    </c:forEach>
    </table>
    </span>
</body>
</html>
