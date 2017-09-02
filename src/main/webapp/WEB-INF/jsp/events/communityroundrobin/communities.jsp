<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <jsp:include page="/WEB-INF/jsp/events/include/info.jsp"/>
        <div class="panel panel-info" style="background: #f2f2f2">
            <div class="panel-heading">
                <h4><fmt:message key="Communities"/></h4>
            </div>
            <div class="panel-body">
                <c:choose>
                    <c:when test="${empty CommunityMap}">
                        <div class="panel-body">
                            <div class="list-group">
                                <div class="alert alert-info"><fmt:message key="NoParticipants"/></div>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="CommunityMapEntry" items="${CommunityMap}">
                            <c:set var="Community" value="${CommunityMapEntry.key}"/>
                            <div class="panel panel-info">
                                <div class="panel-heading">
                                    <h4>${Community}</h4>
                                </div>
                                <div class="panel-body">
                                    <div class="list-group">
                                        <c:set var="RankedParticipantMap" value="${CommunityMapEntry.value}"/>
                                        <c:forEach var="RankedParticipantEntry" items="${RankedParticipantMap}">
                                            <c:set var="Participant" value="${RankedParticipantEntry.participant}"/>
                                            <jsp:include page="/WEB-INF/jsp/include/list-badge-item.jsp">
                                                <jsp:param name="url" value="${Participant.discriminatorValue == 'Player' ? '/players/player/' : '/teams/team/'}${Participant.UUID}"/>
                                                <jsp:param name="msg" value="${Participant}"/>
                                                <jsp:param name="badge" value="${RankedParticipantEntry.value}"/>
                                            </jsp:include>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
