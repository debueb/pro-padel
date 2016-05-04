<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
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
                <h4><fmt:message key="EventParticipations"/></h4>
                <c:choose>
                    <c:when test="${not empty Events}">
                        <div class="list-group">
                            <c:forEach var="Event" items="${Events}">
                                <jsp:include page="/jsp/include/list-badge-item.jsp">
                                    <jsp:param name="msg" value="${Event.name}"/>
                                    <jsp:param name="url" value="/events/${Event.id}"/>
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
