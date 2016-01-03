<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Games"/>${Event.name}</h4>
            </div>
        </div>


        <div class="list-group">
            <fmt:message key="AllGames" var="msg"/>
            <jsp:include page="/jsp/include/list-badge-item.jsp">
                <jsp:param name="msg" value="${msg}"/>
                <jsp:param name="url" value="/games/event/${Event.id}/all"/>
                <jsp:param name="badge" value="${fn:length(Event.games)}"/>
            </jsp:include>
            <c:set var="TeamCount" value="${fn:length(Event.teams)}"/>
            <c:forEach var="Team" items="${Event.teams}">
                <fmt:message key="GamesWith" var="msg"><fmt:param>${Team}</fmt:param></fmt:message>
                <jsp:include page="/jsp/include/list-badge-item.jsp">
                    <jsp:param name="msg" value="${msg}"/>
                    <jsp:param name="url" value="/games/event/${Event.id}/team/${Team.id}"/>
                    <jsp:param name="badge" value="${TeamCount-1}"/>
                </jsp:include>
            </c:forEach>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
