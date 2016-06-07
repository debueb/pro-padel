<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="GameResultIn"><fmt:param value="${Game.event.name}"/></fmt:message></h4>
                </div>
                <div class="panel-body">
                <jsp:include page="/jsp/games/score-reporter.jsp"/>
                <div class="alert alert-danger">${error}</div>
                <form method="POST">
                    <table style="width: 100%;" class="table-editgame table-fixed">
                        <thead>
                        <th></th>
                            <c:forEach var="Participant" items="${Game.participants}">
                            <th class="text-center">
                                <a href="${Participant.discriminatorValue == 'Player' ? '/players/player/' : '/teams/team/'}${Participant.UUID}">${Participant}</a>
                            </th>
                            </c:forEach>
                        </thead>
                        <tbody>
                            <c:forEach begin="1" end="${Game.event.numberOfSets}" step="1" var="set" varStatus="status">
                                <tr>
                                    <td>${set}. <fmt:message key="Set"/></td>
                                    <c:forEach var="Participant" items="${Game.participants}">
                                        <td>
                                            <c:set var="key" value="game-${Game.id}-participant-${Participant.id}-set-${set}"/>
                                            <select name="${key}" class="select-simple form-control" data-container="body">
                                                <option value="-1">-</option>
                                                <c:choose>
                                                    <c:when test="${status.last}">
                                                        <c:set var="end" value="${Game.event.numberOfGamesInFinalSet}"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:set var="end" value="${Game.event.numberOfGamesPerSet}"/>
                                                    </c:otherwise>
                                                </c:choose>
                                                <c:forEach begin="0" end="${end}" step="1" var="current">
                                                    <option value="${current}" ${GamesMap[key].setGames == current ? 'selected' : ''}>${current}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </c:forEach>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <button type="submit" class="btn btn-primary btn-block unit"><fmt:message key="Save"/></button>
                    <c:choose>
                        <c:when test="${empty param.redirectUrl}">
                            <a class="btn btn-primary btn-block unit ajaxify" href="/games/game/${Game.id}"><fmt:message key="Cancel"/></a>
                        </c:when>
                        <c:otherwise>
                            <a class="btn btn-primary btn-block unit ajaxify" href="/${param.redirectUrl}"><fmt:message key="Cancel"/></a>
                        </c:otherwise>
                    </c:choose>
                </form>
            </div></div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
