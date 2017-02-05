<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <jsp:include page="/jsp/events/include/info.jsp"/>

        <c:set var="redirectUrl" value="events/event/${Model.id}/pullgames"/>
        <div class="panel panel-info unit">
            <div class="panel-heading">
                <h4><fmt:message key="Games"/></h4>
            </div>
            <div class="panel-body" style="padding: 0;">
                <table class="table table-responsive table-striped table-fixed">
                    <tbody>
                    <c:forEach var="GameResult" items="${GameResultMap}">
                        <c:set var="Game" value="${GameResult.key}"/>
                        <c:set var="Result" value="${GameResult.value}"/>
                        <tr>
                            <td class="text-right vertical-align-middle">${Game}</td>
                            <td class="vertical-align-middle">
                                <c:choose>
                                    <c:when test="${not empty Game.startDate and not empty Game.startTime}">
                                        <a href="/games/game/${Game.id}/edit?redirectUrl=${redirectUrl}" class="ajaxify"><joda:format value="${Game.startDate}" pattern="dd. MMM" /> <joda:format value="${Game.startTime}" pattern="HH:mm" /> <fmt:message key="oClock"/></a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="/admin/events/edit/${Model.id}/gameschedule"><fmt:message key="AddGameDate"/></a>
                                    </c:otherwise>
                                </c:choose>
                                    |
                                <c:choose>
                                    <c:when test="${empty Result}">
                                        <a href="/games/game/${Game.id}/edit?redirectUrl=${redirectUrl}" class="ajaxify"><fmt:message key="AddGameResult"/></a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="/games/game/${Game.id}/edit?redirectUrl=${redirectUrl}" class="ajaxify">${Result}</a>
                                    </c:otherwise>
                                </c:choose>
                                    |
                                    <a href="/admin/events/${Model.id}/game/${Game.id}/delete?redirectUrl=${redirectUrl}" class="ajaxify"><fmt:message key="Delete"/></a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <div style="padding: 10px;">
                    <c:choose>
                        <c:when test="${Model.eventType eq 'PullRoundRobin'}">
                            <a href="/admin/events/edit/${Model.id}/addpullgame?redirectUrl=${redirectUrl}" class="btn btn-primary btn-block ajaxify unit-2"><fmt:message key="AddGame"/></a>
                        </c:when>
                        <c:when test="${Model.eventType eq 'FriendlyGames'}">
                            <a href="/admin/events/edit/${Model.id}/addfriendlygame?redirectUrl=${redirectUrl}" class="btn btn-primary btn-block ajaxify unit-2"><fmt:message key="AddGame"/></a>
                        </c:when>
                    </c:choose>
                    
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
