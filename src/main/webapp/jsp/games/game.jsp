<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header">
            <h1><fmt:message key="GameResult"/></h1>
        </div>
        <c:set var="showScoreReporter" value="true" scope="request"/>
        <jsp:include page="/jsp/games/game-result.jsp"/>
        <div class="list-group unit">
            <a href="/games/game/${Game.id}/edit" class="list-group-item ajaxify" >
                <div class="list-item-text"><fmt:message key="EditResult"/></div>
            </a>
            <a class="list-group-item private-data" data-fake="${Game.obfuscatedMailTo}" data-prefix="mailto:">
                <div class="list-item-text"><fmt:message key="MailAllPlayers"/></div>
            </a>
            <c:forEach var="Participant" items="${Game.participants}">
                <a class="list-group-item ajaxify" href="/games/team/${Participant.id}">
                    <div class="list-item-text"><fmt:message key="AllGamesWith"><fmt:param value="${Participant.displayName}"/></fmt:message></div>
                </a>
            </c:forEach>
            <a class="list-group-item ajaxify" href="/games/event/${Game.event.id}/all">
                <div class="list-item-text"><fmt:message key="AllGamesIn"><fmt:param value="${Game.event.name}"/></fmt:message></div>
            </a>
            <a class="list-group-item ajaxify" href="/scores/event/${Game.event.id}">
                <div class="list-item-text"><fmt:message key="ScoresOfEvent"><fmt:param value="${Game.event.name}"/></fmt:message></div>
            </a>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
