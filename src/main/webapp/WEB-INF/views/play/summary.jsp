<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:if test="${!empty account}">
<c:if test="${!empty accountCurrency}">
<span style="vertical-align: text-top;">
<span style="display: inline-block; vertical-align: middle; text-align: center; float: left;">${account.displayName}</span>
<span style="display: inline-block; vertical-align: middle; text-align: center; float: left; width: 5%;">&nbsp;&nbsp;</span>
<span id="playMenuLogin" class="menuOption"><a href="${pageContext.request.contextPath}/logout">Logout</a></span>
<span style="display: inline-block; vertical-align: middle; text-align: center; float: left; width: 5%;">&nbsp;&nbsp;</span>
<a href="${pageContext.request.contextPath}/play/account/addTokens" class="menuLink">
<label>
	<img src="${pageContext.request.contextPath}/images/token.png" alt="Dungeon Tokens" data-qtiptitle="Dungeon Tokens" class="dungeonToken" />
</label>
<span style="display: inline-block; vertical-align: middle;">${accountCurrency.currency}</span>
</a>
</span>
</c:if>
<c:if test="${account.onHoliday}"><span id="playMenuLogin" class="menuOption"><a style="color: red;" class="menuLink" href="${pageContext.request.contextPath}/play/account/offHoliday">Stop Holiday</a></span></c:if>
</c:if>