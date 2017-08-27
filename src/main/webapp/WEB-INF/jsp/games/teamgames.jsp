<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>
        <jsp:include page="/WEB-INF/jsp/events/include/info.jsp"/>
        <div class="panel panel-info">
            <div class="panel-heading">
                <h4>${title}</h4>
                <c:if test="${not empty subtitle}">
                    <h4>${subtitle}</h4>
                </c:if>
            </div>
            <div class="panel-body" style="padding: 0;">
                <div class="table-responsive">
                    <table class="table table-fixed table-striped table-hover table-condensed">
                        <thead>
                            <th></th>
                            <c:forEach var="ParticipantGameResultMapEntry" items="${ParticipantGameResultMap}">
                                <c:set var="Participant" value="${ParticipantGameResultMapEntry.key}"/>
                                <th class="normal">${Participant}</th>
                            </c:forEach>
                        </thead>
                        <tbody>
                            <c:forEach var="ParticipantGameResultMapEntry" items="${ParticipantGameResultMap}">
                                <c:set var="Participant" value="${ParticipantGameResultMapEntry.key}"/>
                                <c:set var="GameResultMap" value="${ParticipantGameResultMapEntry.value}"/>

                                <c:forEach var="ParticipantGameResultMapEntry" items="${ParticipantGameResultMap}">
                                    <c:forEach var="GameResultMapEntry" items="${GameResultMap}">
                                        <c:set var="Game" value="${GameResultMapEntry.key}"/>
                                        <c:set var="Result" value="${GameResultMapEntry.value}"/>
                                        <tr>
                                            <c:forEach var="GameParticipant" items="${Game.participants}">
                                                <c:if test="${GameParticipant ne Participant}">
                                                    <td class="text-right vertical-align-middle">${GameParticipant}</td>
                                                </c:if>
                                            </c:forEach>
                                            <td class="vertical-align-middle">
                                                <a href="/games/game/${Game.id}/edit?redirectUrl=games/event/${Model.id}">
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
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:forEach>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
