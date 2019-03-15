<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>
        <jsp:include page="/WEB-INF/jsp/include/module-description.jsp"/>

        <c:if test="${Module.showEventScores}">
            <div class="panel panel-info">
                <div class="panel-heading">
                    <h4><fmt:message key="ResultsIn"><fmt:param>${Model.name}</fmt:param></fmt:message></h4>
                </div>
                <div class="panel-body">
                    <div class="table-responsive">
                        <table class="table table-bordered table-scores">
                            <thead>
                            <th class="text-center">#</th>
                            <th><fmt:message key="Team"/></th>
                            <th class="text-center"><fmt:message key="Matches"/></th>
                            <th class="text-center"><fmt:message key="Sets"/></th>
                            <th class="text-center"><fmt:message key="Games"/></th>
                            </thead>
                            <tbody>
                                <c:forEach var="ScoreEntry" items="${ScoreEntries}" varStatus="status">
                                    <c:set var="UrlParticipant" value="${ScoreEntry.participant.discriminatorValue == 'Player' ? '/players/player/' : '/teams/team/'}${ScoreEntry.participant.UUID}"/>
                                    <tr>
                                        <td class="text-center">${status.index+1}</td>
                                        <td><a href="${UrlParticipant}">${ScoreEntry.participant}</a></td>
                                        <td class="text-center">${ScoreEntry.matchesWon}:${ScoreEntry.matchesPlayed-ScoreEntry.matchesWon}</td>
                                        <td class="text-center">${ScoreEntry.setsWon}:${ScoreEntry.setsPlayed-ScoreEntry.setsWon}</td>
                                        <td class="text-center">${ScoreEntry.gamesWon}:${ScoreEntry.gamesPlayed-ScoreEntry.gamesWon}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </c:if>
     
        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="currentEvents"/></h4>
            </div>
            <div class="panel-body">
                <c:choose>
                    <c:when test="${empty CurrentEvents}">
                        <div class="alert alert-info">
                            <fmt:message key="NoCurrentEvents"/>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="list-group">
                            <c:forEach var="Event" items="${CurrentEvents}">
                                <a href="/events/event/${Event.id}" class="list-group-item">
                                    <div class="list-item-text">
                                        ${Event.name}
                                    </div>
                                    <div class="text-right"><joda:format value="${Event.startDate}" pattern="EE, dd. MMM. yyyy"/></div>
                                </a>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
