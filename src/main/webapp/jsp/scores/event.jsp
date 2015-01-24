<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header">
            <h1><fmt:message key="ResultsIn"><fmt:param>${Event.name}</fmt:param></fmt:message></h1>
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
                            <td><a href="${UrlTeam}" class="ajaxify">${ScoreEntry.participant.displayName}</a></td>
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
                <jsp:param name="badge" value="${fn:length(Event.games)}"/>
            </jsp:include>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
