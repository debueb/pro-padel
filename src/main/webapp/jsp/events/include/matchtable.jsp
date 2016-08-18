<%@include file="/jsp/include/include.jsp"%>
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
                    <c:when test="${Participant eq Opponent}">
                        -
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="GameResultMapEntry" items="${GameResultMap}">
                            <c:set var="Game" value="${GameResultMapEntry.key}"/>
                            <c:set var="Result" value="${GameResultMapEntry.value}"/>
                            <c:if test="${fn:contains(Game.participants, Participant) and fn:contains(Game.participants, Opponent)}">
                                <a href="/games/game/${Game.id}/edit?redirectUrl=${redirectUrl}" class="ajaxify">
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