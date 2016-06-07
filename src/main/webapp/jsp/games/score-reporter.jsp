<%@include file="/jsp/include/include.jsp"%>
<c:if test="${Game.scoreReporter ne null}">
    <div><fmt:message key="ScoreReportedBy"><fmt:param value="${contextPath}/players/player/${Game.scoreReporter.UUID}"/><fmt:param value="${Game.scoreReporter}"/></fmt:message></div>
</c:if>