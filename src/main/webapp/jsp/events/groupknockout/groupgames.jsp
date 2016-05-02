<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>
        
        <div class="panel panel-info unit">
            
            <div class="panel-heading">
                <h4><fmt:message key="Group"/> 1</h4>
            </div>
            <div class="panel-body">
                <div class="list-group">
                    <c:forEach var="Game" items="${Model.games}">
                        <c:set var="Game" value="${Game}" scope="request"/>
                        <a href="/games/game/${Game.id}/edit?redirectUrl=events/${Model.id}/groupgames" class="list-group-item ajaxify">
                            <jsp:include page="/jsp/games/game-result.jsp"/>
                        </a>
                    </c:forEach>
                </div>
            </div>
        </div>
            
            
        <a class="btn btn-primary btn-block unit" href="/events/${Model.id}/groupgamesend"><fmt:message key="EndGroupGames"/></a>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
