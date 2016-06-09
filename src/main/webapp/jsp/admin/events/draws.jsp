<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<script src="${contextPath}/js/noconcat/tournament.js"></script>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Draws"/> ${Model.name}</h4>
            </div>
            <div class="panel-body">
                <c:choose>
                    <c:when test="${empty RoundGameMap}">
                        <div class="alert alert-danger">${error}</div>
                        <div class="alert alert-info"><fmt:message key="DrawsDoNotExist"/></div>
                        <form method="POST">
                            <button class="btn btn-primary btn-block unit"><fmt:message key="CreateDraws"/></button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <fmt:message key="EditDrawsDesc"/>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
<c:if test="${not empty RoundGameMap}">
    <div class="tournament-container">
        <canvas id="canvas"></canvas>
        <div class="region">
            <c:forEach var="RoundGameMapEntry" items="${RoundGameMap}">
                <section class="round">
                    <c:forEach var="Game" items="${RoundGameMapEntry.value}">

                        <article class="game">
                            <c:if test="${RoundGameMapEntry.key == 0}"><a class="ajaxify" href="${contextPath}/admin/events/edit/${Model.id}/draws/game/${Game.id}"></c:if>
                                <c:forEach var="Participant" items="${Game.participants}">
                                    <div class="team" data-team="${Participant.id}">

                                        <span class="team-name">${Participant.name}</span>
                                        <span class="team-score"></span>
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

                                <c:if test="${RoundGameMapEntry.key == 0}"></a></c:if>
                            </article>
                    </c:forEach>
                </section>
            </c:forEach>
        </div>
    </div>
</c:if>

<div class="row unit">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="list-group">
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="key" value="AllEvents"/>
                <jsp:param name="href" value="/admin/events"/>
            </jsp:include>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="key" value="PlayerViewOfThisEvent"/>
                <jsp:param name="href" value="/events/event/${Model.id}"/>
            </jsp:include>
        </div>
    </div>
</div>


<jsp:include page="/jsp/include/footer.jsp"/>     


