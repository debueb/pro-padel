<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>
<script src="${contextPath}/static/js/noconcat/tournament.js"></script>

<div class="page-header"></div>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/WEB-INF/jsp/events/include/info.jsp"/>
    </div>
</div>

<div class="tournament-container">

    <canvas id="canvas"></canvas>
    <div class="region">

        <c:forEach var="RoundGameMapEntry" items="${RoundGameMap}">
            <section class="round">
                <c:forEach var="Game" items="${RoundGameMapEntry.value}">

                    <article class="game">
                        <c:if test="${fn:length(Game.participants) == 2}"><a href="${contextPath}/games/game/${Game.id}/edit?redirectUrl=events/event/${Model.id}/knockoutgames"></c:if>
                            <c:forEach var="Participant" items="${Game.participants}">
                                <div class="team team-${Participant.id}" data-team="${Participant.id}">
                                    <span class="team-name">${Participant}</span>
                                    <span class="team-score">
                                        <c:forEach var="GameSet" items="${ParticipantGameGameSetMap[Participant][Game]}">
                                            ${GameSet.setGames}
                                        </c:forEach>
                                    </span>
                                </div>
                            </c:forEach>
                            <c:choose>
                                <c:when test="${RoundGameMapEntry.key == 0 and fn:length(Game.participants) == 1}">
                                    <div class="team" data-team="-1">
                                        <span class="team-name">- Bye -</span>
                                        <span class="team-score"></span>
                                    </div>
                                </c:when>
                                <c:when test="${fn:length(Game.participants) == 1}">
                                    <div class="team" data-team="-1">
                                        <span class="team-name"></span>
                                        <span class="team-score"></span>
                                    </div>
                                </c:when>
                                <c:when test="${fn:length(Game.participants) == 0}">
                                    <div class="team" data-team="-1">
                                        <span class="team-name"></span>
                                        <span class="team-score"></span>
                                    </div>
                                    <div class="team" data-team="-1">
                                        <span class="team-name"></span>
                                        <span class="team-score"></span>
                                    </div>
                                </c:when>
                            </c:choose>
                        <c:if test="${fn:length(Game.participants) == 2}"></a></c:if>
                    </article>
                </c:forEach>
            </section>
        </c:forEach>    
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>


