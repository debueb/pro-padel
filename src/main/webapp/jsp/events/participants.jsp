<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>
        
        <jsp:include page="/jsp/events/include/info.jsp"/>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="ParticipantsIn"><fmt:param value="${Model}"/></fmt:message></h4>
            </div>
            <div class="panel-body">
                <div class="list-group">
                    <c:if test="${empty RankingMap}">
                        <div class="alert alert-info"><fmt:message key="NoParticipants"/></div>
                    </c:if>
                    <c:forEach var="RankingMapEntry" items="${RankingMap}">
                        <c:set var="Participant" value="${RankingMapEntry.key}"/>
                        <c:set var="Ranking" value="${RankingMapEntry.value}"/>
                        <a href="${Participant.discriminatorValue == 'Player' ? '/players/player/' : '/teams/team/'}${Participant.UUID}" class="list-group-item ajaxify">
                            <div class="list-item-text">${Participant}</div>
                            <div class="list-group-item-icon">
                                <c:choose>
                                    <c:when test="${Participant.discriminatorValue == 'Player' and not empty Participant.profileImage}">
                                        <img src="/images/image/${Participant.profileImage.sha256}"/>
                                    </c:when>
                                    <c:when test="${Participant.discriminatorValue == 'Player'}">
                                        <i class="fa fa-user"></i>
                                    </c:when>
                                </c:choose>
                                <span class="player-ranking">${Ranking}</span>
                            </div>
                        </a>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>
