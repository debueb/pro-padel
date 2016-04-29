<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<link href="/css/noconcat/tournament.css" rel="stylesheet">
<script src="${contextPath}/js/noconcat/tournament.js"></script>

<div class="tournament-container">

    <canvas id="canvas"></canvas>
    <div class="region">

        <c:forEach var="RoundGameMapEntry" items="${RoundGameMap}">
            <section class="round">
                <c:forEach var="Game" items="${RoundGameMapEntry.value}">

                    <article class="game">
                        <c:forEach var="Participant" items="${Game.participants}">
                            <div class="team" data-team="${Participant.id}">
                                <c:if test="${RoundGameMapEntry.key == 0}"><a class="ajaxify" href="${contextPath}/admin/events/edit/${Model.id}/draws/game/${Game.id}"></c:if>
                                    <span class="team-name">${Participant.name}</span>
                                    <span class="team-score"></span>
                                <c:if test="${RoundGameMapEntry.key == 0}"></a></c:if>
                            </div>
                        </c:forEach>
                        <c:choose>
                            <c:when test="${RoundGameMapEntry.key == 0 and fn:length(Game.participants) == 1}">
                                <div class="team" data-team="-1">
                                    <a class="ajaxify" href="${contextPath}/admin/events/edit/${Model.id}/draws/game/${Game.id}">
                                        <span class="team-name">- Bye -</span>
                                        <span class="team-score"></span>
                                    </a>
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
                    </article>
                </c:forEach>
            </section>
        </c:forEach>    

    </div>
    
</div>
<div class="row unit">
    <div class="col-xs-4 col-xs-offset-4 text-center">
        <a class="btn btn-primary btn-block" href="/admin/events"><fmt:message key="AllEvents"/></a>
    </div>
</div>


<jsp:include page="/jsp/include/footer.jsp"/>     


