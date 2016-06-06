<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>
        
        <jsp:include page="/jsp/events/include/info.jsp"/>
        
        <c:forEach var="GroupParticipantGameResultMapEntry" items="${GroupParticipantGameResultMap}">
            <c:set var="GroupNumber" value="${GroupParticipantGameResultMapEntry.key}"/>
            <c:set var="ParticipantGameResultMap" value="${GroupParticipantGameResultMapEntry.value}"/>
            <div class="panel panel-info unit">
                <div class="panel-heading">
                    <h4><fmt:message key="Group"/> ${GroupNumber+1}</h4>
                </div>
                <div class="panel-body">
                    <div class="list-group">
                        <div class="table-responsive">
                            <table class="table table-striped table-hover table-condensed">
                                <thead>
                                    <th></th>
                                    <c:forEach var="ParticipantGameResultMapEntry" items="${ParticipantGameResultMap}">
                                        <c:set var="Participant" value="${ParticipantGameResultMapEntry.key}"/>
                                    <th class="text-center normal">${Participant}</th>
                                    </c:forEach>
                                </thead>
                                <tbody>
                                    <c:forEach var="ParticipantGameResultMapEntry" items="${ParticipantGameResultMap}">
                                        <c:set var="Participant" value="${ParticipantGameResultMapEntry.key}"/>
                                        <c:set var="GameResultMap" value="${ParticipantGameResultMapEntry.value}"/>
                                        <tr>
                                            <td class="vertical-align-middle">${Participant}</td>
                                            <c:forEach var="ParticipantGameResultMapEntry" items="${ParticipantGameResultMap}">
                                                <c:set var="Opponent" value="${ParticipantGameResultMapEntry.key}"/>
                                                <td class="text-center vertical-align-middle">
                                                    <c:choose>
                                                        <c:when test="${Participant == Opponent}">
                                                            -
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:forEach var="GameResultMapEntry" items="${GameResultMap}">
                                                                <c:set var="Game" value="${GameResultMapEntry.key}"/>
                                                                <c:set var="Result" value="${GameResultMapEntry.value}"/>
                                                                <c:if test="${fn:contains(Game.participants, Participant) and fn:contains(Game.participants, Opponent)}">
                                                                    <a href="/games/game/${Game.id}/edit?redirectUrl=events/event/${Model.id}/groupgames" class="ajaxify">
                                                                    <c:choose>
                                                                        <c:when test="${not empty Result}">
                                                                            ${Result}
                                                                        </c:when>
                                                                        <c:when test="${Game.startTime ne null}">
                                                                            <c:if test="${Game.startDate ne Model.startDate}">
                                                                                <joda:format value="${Game.startDate}" pattern="dd. MMM" /> 
                                                                            </c:if>
                                                                            <joda:format value="${Game.startTime}" pattern="HH:mm" /> <fmt:message key="oClock"/>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            -:- -:-
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </c:if>
                                                                </a>
                                                            </c:forEach>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </c:forEach>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
        
        <c:if test="${empty RoundGameMap}">
            <a class="btn btn-primary btn-block unit ajaxify" href="/admin/events/event/${Model.id}/groupgamesend"><fmt:message key="EndGroupGames"/></a>
        </c:if>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
