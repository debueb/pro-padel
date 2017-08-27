<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>
        <jsp:include page="/WEB-INF/jsp/events/include/info.jsp"/>
        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Games"/> ${Model.name}</h4>
            </div>
            <div class="panel-body">
                <div class="list-group">
                    <fmt:message key="AllGames" var="msg"/>
                    <jsp:include page="/WEB-INF/jsp/include/list-badge-item.jsp">
                        <jsp:param name="msg" value="${msg}"/>
                        <jsp:param name="url" value="/games/event/${Model.id}/all"/>
                        <jsp:param name="badge" value=""/>
                    </jsp:include>
                    <c:set var="TeamCount" value="${fn:length(Model.teams)}"/>
                    <c:forEach var="Team" items="${Model.teams}">
                        <fmt:message key="GamesWith" var="msg"><fmt:param>${Team}</fmt:param></fmt:message>
                        <jsp:include page="/WEB-INF/jsp/include/list-badge-item.jsp">
                            <jsp:param name="msg" value="${msg}"/>
                            <jsp:param name="url" value="/games/event/${Model.id}/team/${Team.UUID}"/>
                            <jsp:param name="badge" value=""/>
                        </jsp:include>
                    </c:forEach>
                </div>  
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
