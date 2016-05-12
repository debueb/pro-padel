<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>
        
        <jsp:include page="/jsp/events/include/info.jsp"/>
        
        <c:forEach var="GroupMapEntry" items="${GroupGameMap}">
            <c:set var="GroupNumber" value="${GroupMapEntry.key}"/>
            <c:set var="GameList" value="${GroupMapEntry.value}"/>
            <div class="panel panel-info unit">
                <div class="panel-heading">
                    <h4><fmt:message key="Group"/> ${GroupNumber+1}</h4>
                </div>
                <div class="panel-body">
                    <div class="list-group">
                        <c:forEach var="Game" items="${GameList}">
                            <c:set var="Game" value="${Game}" scope="request"/>
                            <a href="/games/game/${Game.id}/edit?redirectUrl=events/event/${Model.id}/groupgames" class="list-group-item ajaxify">
                                <jsp:include page="/jsp/games/game-result.jsp"/>
                            </a>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </c:forEach>
        
        <c:choose>
            <c:when test="${empty RoundGameMap}">
                <a class="btn btn-primary btn-block unit" href="/admin/events/event/${Model.id}/groupgamesend"><fmt:message key="EndGroupGames"/></a>
            </c:when>
            <c:otherwise>
                <div class="list-group">
                    <jsp:include page="/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/events/event/${Model.id}/knockoutgames"/>
                        <jsp:param name="key" value="KnockoutGames"/>
                        <jsp:param name="icon" value="list-ol"/>
                    </jsp:include>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
