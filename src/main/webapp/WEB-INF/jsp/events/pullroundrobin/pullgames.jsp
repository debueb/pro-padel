<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <jsp:include page="/WEB-INF/jsp/events/include/info.jsp"/>

        <c:set var="redirectUrl" value="events/event/${Model.id}/pullgames"/>
        <div class="panel panel-info unit">
            <div class="panel-heading">
                <h4><fmt:message key="Games"/></h4>
            </div>
            <div class="panel-body" style="padding: 0;">
                <div style="padding: 10px;">
                    <c:choose>
                        <c:when test="${Model.eventType eq 'PullRoundRobin'}">
                            <a href="/events/edit/${Model.id}/addpullgame?redirectUrl=${redirectUrl}" class="btn btn-primary btn-block unit-2"><fmt:message key="AddGame"/></a>
                        </c:when>
                        <c:when test="${Model.eventType eq 'FriendlyGames'}">
                            <a href="/events/edit/${Model.id}/addfriendlygame?redirectUrl=${redirectUrl}" class="btn btn-primary btn-block unit-2"><fmt:message key="AddGame"/></a>
                        </c:when>
                    </c:choose>
                </div>
                <table class="table table-responsive table-striped table-fixed unit-2">
                    <tbody>
                    <c:forEach var="GameResult" items="${GameResultMap}">
                        <c:set var="Game" value="${GameResult.key}"/>
                        <c:set var="Result" value="${GameResult.value}"/>
                        <tr>
                            <td class="text-right vertical-align-middle">${Game}</td>
                            <td class="vertical-align-middle">
                                <c:choose>
                                    <c:when test="${empty Result}">
                                        <a href="/games/game/${Game.id}/edit?redirectUrl=${redirectUrl}"><fmt:message key="AddGameResult"/></a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="/games/game/${Game.id}/edit?redirectUrl=${redirectUrl}">${Result}</a>
                                    </c:otherwise>
                                </c:choose>
                                    |
                                    <a href="/admin/events/${Model.id}/game/${Game.id}/delete?redirectUrl=${redirectUrl}"><fmt:message key="Delete"/></a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
