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
                <jsp:include page="/jsp/games/game-result.jsp"/>
                <jsp:include page="/jsp/games/score-reporter.jsp"/>
                <div class="list-group unit">
                    <a href="/games/game/${Game.id}/edit" class="list-group-item ajaxify" >
                        <div class="list-item-text"><fmt:message key="EditResult"/></div>
                    </a>
                    <c:forEach var="Team" items="${Game.participants}">
                        <a class="list-group-item ajaxify" href="/teams/team/${Team.UUID}">
                            <div class="list-item-text"><fmt:message key="AllInfosAbout"><fmt:param value="${Team}"/></fmt:message></div>
                            </a>
                    </c:forEach>
                    <c:forEach var="Participant" items="${Game.participants}">
                        <a class="list-group-item ajaxify" href="/games/team/${Participant.UUID}/event/${Game.event.id}">
                            <div class="list-item-text"><fmt:message key="AllGamesWithTeamInEvent"><fmt:param value="${Participant}"/><fmt:param value="${Game.event.name}"/></fmt:message></div>
                            </a>
                    </c:forEach>
                    <a class="list-group-item ajaxify" href="/games/event/${Game.event.id}/all">
                        <div class="list-item-text"><fmt:message key="AllGamesIn"><fmt:param value="${Game.event.name}"/></fmt:message></div>
                        </a>
                        <a class="list-group-item ajaxify" href="/scores/event/${Game.event.id}">
                        <div class="list-item-text"><fmt:message key="ScoresOfEvent"><fmt:param value="${Game.event.name}"/></fmt:message></div>
                        </a>

                    <c:if test="${sessionScope.accessLevel == 'loggedInAndParticipant'}">
                        <a class="list-group-item private-data" data-fake="${Game.obfuscatedMailTo}" data-prefix="mailto:">
                            <div class="list-item-text"><fmt:message key="MailAllPlayers"/></div>
                        </a>
                    </c:if>
                </div>

                <c:if test="${empty sessionScope.accessLevel}">
                    <div class="alert alert-info unit">
                        <fmt:message key="LogInToMailAllPlayers"/>
                    </div>
                    <c:url value="/games/game/${Game.id}" var="gameURL"/>
                    <a class="btn btn-primary btn-block unit" href="/login?redirect=${gameURL}"><fmt:message key="Login"/></a>
                    <a class="btn btn-primary btn-block" href="/login/register?redirect=${gameURL}"><fmt:message key="Register"/></a>
                </c:if>

                <c:if test="${sessionScope.accessLevel == 'loggedIn'}">
                    <div class="alert alert-info unit">
                        <fmt:message key="NeedToParticipateToMailAllPlayers"><fmt:param value="${Player}"/></fmt:message>
                        </div>
                </c:if>
            </div></div>
    </div>

</div>

<jsp:include page="/jsp/include/footer.jsp"/>
