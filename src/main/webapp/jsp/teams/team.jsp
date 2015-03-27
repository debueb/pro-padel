<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header">
            <h1>${Team.name}</h1>
        </div>

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
        <div class="list-group">
            <fmt:message key="AllGames" var="msg"/>
            <jsp:include page="/jsp/include/list-badge-item.jsp">
                <jsp:param name="msg" value="${msg}"/>
                <jsp:param name="url" value="/games/team/${Team.id}"/>
                <jsp:param name="badge" value="${fn:length(Games)}"/>
            </jsp:include>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
