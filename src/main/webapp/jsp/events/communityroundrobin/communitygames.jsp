<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <jsp:include page="/jsp/events/include/info.jsp"/>


        <div class="panel panel-info unit">
            <div class="panel-heading">
                <h4><fmt:message key="Games"/></h4>
            </div>
            <div class="panel-body">
                <div class="list-group">
                    <div class="table-responsive">
                        <table class="table table-striped table-hover table-condensed">
                            <thead>
                            <th></th>
                                <c:forEach var="GroupParticipantGameResultMapEntry" items="${GroupParticipantGameResultMap}" begin="1" end="1">
                                    <c:forEach var="ParticipantGameResultMapEntry" items="${GroupParticipantGameResultMapEntry.value}">
                                    <th class="text-center normal">
                                        <div class="small">${GroupParticipantGameResultMapEntry.key}</div>
                                        <div>${ParticipantGameResultMapEntry.key}</div>
                                    </th>
                                </c:forEach>
                            </c:forEach>
                            </thead>
                            <tbody>
                                <c:forEach var="GroupParticipantGameResultMapEntry" items="${GroupParticipantGameResultMap}" begin="0" end="0">
                                    <c:forEach var="ParticipantGameResultMapEntry" items="${GroupParticipantGameResultMapEntry.value}">
                                        <c:set var="Participant" value="${ParticipantGameResultMapEntry.key}"/>
                                        <c:set var="GameResultMap" value="${ParticipantGameResultMapEntry.value}"/>
                                        <tr>
                                            <td class="vertical-align-middle">
                                                <div class="small">${GroupParticipantGameResultMapEntry.key}</div>
                                                <div>${Participant}</div>
                                            </td>
                                            <c:forEach var="GroupParticipantGameResultMapEntry2" items="${GroupParticipantGameResultMap}" begin="1" end="1">
                                                <c:forEach var="ParticipantGameResultMapEntry2" items="${GroupParticipantGameResultMapEntry2.value}">
                                                    <c:set var="Opponent" value="${ParticipantGameResultMapEntry2.key}"/>
                                                    <c:if test="${Participant.community ne Opponent.community}">
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
                                                                            <a href="/games/game/${Game.id}/edit?redirectUrl=events/event/${Model.id}/communitygames" class="ajaxify">
                                                                                <c:choose>
                                                                                    <c:when test="${not empty Result}">
                                                                                        ${Result}
                                                                                    </c:when>
                                                                                    <c:when test="${Game.startTime ne null}">
                                                                                        <joda:format value="${Game.startDate}" pattern="dd. MMM" /><joda:format value="${Game.startTime}" pattern="HH:mm" /> <fmt:message key="oClock"/>
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
                                                    </c:if>
                                                </c:forEach>
                                            </c:forEach>
                                        </tr>
                                    </c:forEach>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
