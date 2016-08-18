<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>
        <jsp:include page="/jsp/events/include/info.jsp"/>
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
                                <th class="text-center"><fmt:message key="Points"/></th>
                        <th class="text-center"><fmt:message key="Matches"/></th>
                        <th class="text-center"><fmt:message key="Sets"/></th>
                        <th class="text-center"><fmt:message key="Games"/></th>
                        </thead>
                        <tbody>
                            <c:forEach var="ScoreEntry" items="${ScoreEntries}" varStatus="status">
                                <c:set var="UrlTeam" value="/teams/team/${ScoreEntry.participant.UUID}"/>
                                <c:set var="UrlTeamGames" value="/games/team/${ScoreEntry.participant.UUID}"/>
                                <tr>
                                    <td class="text-center">${status.index+1}</td>
                                    <td><a href="${UrlTeam}" class="ajaxify">${ScoreEntry.participant}</a></td>
                                    <td class="text-center"><a href="${UrlTeamGames}" class="ajaxify">${ScoreEntry.totalPoints}</a></td>
                                    <td class="text-center"><a href="${UrlTeamGames}" class="ajaxify">${ScoreEntry.matchesWon}:${ScoreEntry.matchesPlayed-ScoreEntry.matchesWon}</a></td>
                                    <td class="text-center"><a href="${UrlTeamGames}" class="ajaxify">${ScoreEntry.setsWon}:${ScoreEntry.setsPlayed-ScoreEntry.setsWon}</a></td>
                                    <td class="text-center"><a href="${UrlTeamGames}" class="ajaxify">${ScoreEntry.gamesWon}:${ScoreEntry.gamesPlayed-ScoreEntry.gamesWon}</a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
