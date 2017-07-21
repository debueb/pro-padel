<%@include file="/jsp/include/include.jsp"%>
        
<div class="panel panel-info" id="games">
    <div class="panel-heading">
        <h4>${fn:length(GameResultMap)} <fmt:message key="GamesWith"><fmt:param value="${Player}"/></fmt:message></h4>
    </div>
    <div class="panel-body" style="padding: 0;">
        <div class="table-responsive">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th><a href="?sortBy=participants"><fmt:message key="Players"/></a></th>
                        <th><fmt:message key="GameResult"/></th>
                        <th><a href="?sortBy=event"><fmt:message key="Event"/></a></th>
                        <th><a href="?sortBy=date"><fmt:message key="Date"/></a></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="GameResult" items="${GameResultMap}">
                        <c:set var="Game" value="${GameResult.key}"/>
                        <c:set var="Result" value="${GameResult.value}"/>
                        <c:set var="Result" value="${empty Result ? '- : -' : Result}"/>
                        <tr>
                            <td>
                                <c:forEach var="P" items="${Game.participants}" varStatus="status">
                                    <a href="${P.discriminatorValue == 'Player' ? '/players/player/' : '/teams/team/'}${P.UUID}">${P}</a>
                                    <c:if test="${status.first}"><span style="display: block;"> - vs. - </span></c:if>
                                </c:forEach>
                            </td>
                            <td><a href="/games/game/${Game.id}/edit?redirectUrl=players/player/${Player.UUID}/games">${Result}</a></td>
                            <td><a href="/events/event/${Game.event.id}">${Game.event}</a></td>
                            <td><a href="/games/game/${Game.id}/edit?redirectUrl=players/player/${Player.UUID}/games"><joda:format value="${Game.startDate}" pattern="EEE, dd. MMM. YYYY"/></a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>