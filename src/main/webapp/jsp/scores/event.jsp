<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="ResultsIn"><fmt:param>${Event.name}</fmt:param></fmt:message></h4>
                    </div>
                </div>


                <div class="table-responsive">
                    <table class="table table-bordered table-scores">
                        <thead>
                        <th class="text-center">#</th>
                        <th>Team</th>
                        <th class="text-center"><fmt:message key="Points"/></th>
                        <th class="text-center"><fmt:message key="Matches"/></th>
                        <th class="text-center"><fmt:message key="Sets"/></th>
                        <th class="text-center"><fmt:message key="Games"/></th>
                </thead>
                <tbody>
                    <c:forEach var="ScoreEntry" items="${ScoreEntries}" varStatus="status">
                        <c:set var="UrlTeam" value="/teams/team/${ScoreEntry.participant.id}"/>
                        <c:set var="UrlTeamGames" value="/games/team/${ScoreEntry.participant.id}"/>
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
        <div class="list-group unit">
            <fmt:message key="AllGamesIn" var="msg"><fmt:param value="${Event.name}"/></fmt:message>
            <jsp:include page="/jsp/include/list-badge-item.jsp">
                <jsp:param name="msg" value="${msg}"/>
                <jsp:param name="url" value="/games/event/${Event.id}/all"/>
                <jsp:param name="badge" value=""/>
            </jsp:include>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
