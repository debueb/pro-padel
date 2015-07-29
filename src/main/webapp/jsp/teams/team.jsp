<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4>${Team.name}</h4>
            </div>
            <div class="panel-body">


                <h4><fmt:message key="Players"/></h4>
                <div class="list-group">
                    <c:forEach var="Player" items="${Team.players}">
                        <jsp:include page="/jsp/include/list-badge-item.jsp">
                            <jsp:param name="msg" value="${Player}"/>
                            <jsp:param name="url" value="/players/player/${Player.id}"/>
                        </jsp:include>
                    </c:forEach>
                </div>

                <hr>
                <h4><fmt:message key="Games"/></h4>
                <c:choose>
                    <c:when test="${not empty EventGameMap}">
                        <div class="list-group">
                            <c:forEach var="EventGameMapEntry" items="${EventGameMap}">
                                <c:set var="Event" value="${EventGameMapEntry.key}"/>
                                <fmt:message key="AllGamesWithTeamInEvent" var="msg"><fmt:param value="${Team}"/><fmt:param value="${Event.name}"/></fmt:message>
                                <jsp:include page="/jsp/include/list-badge-item.jsp">
                                    <jsp:param name="msg" value="${msg}"/>
                                    <jsp:param name="url" value="/games/team/${Team.id}/event/${Event.id}"/>
                                    <jsp:param name="badge" value="${fn:length(EventGameMapEntry.value)}"/>
                                </jsp:include>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-info">
                            <fmt:message key="NoActiveGames"><fmt:param value="${Team}"/></fmt:message>
                        </div>
                    </c:otherwise>
                </c:choose>
                
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
