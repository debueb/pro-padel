<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>
        <div class="panel panel-info">
            <div class="panel-heading">
                <h4>${Team.name}</h4>
            </div>
            <div class="panel-body">


                <h4><fmt:message key="Players"/></h4>
                <div class="list-group">
                    <c:forEach var="Player" items="${Team.players}">
                        <jsp:include page="/WEB-INF/jsp/include/list-badge-item.jsp">
                            <jsp:param name="msg" value="${Player}"/>
                            <jsp:param name="url" value="/players/player/${Player.UUID}"/>
                        </jsp:include>
                    </c:forEach>
                </div>

                <hr>
                <h4><fmt:message key="ActiveEventParticipations"/></h4>
                <c:choose>
                    <c:when test="${empty Events}">
                        <div class="alert alert-info">
                            <fmt:message key="NoActiveGames"><fmt:param value="${Team}"/></fmt:message>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="list-group">
                            <c:forEach var="Event" items="${Events}">
                                <jsp:include page="/WEB-INF/jsp/include/list-badge-item.jsp">
                                    <jsp:param name="msg" value="${Event.name} / ${Event.startDate}"/>
                                    <jsp:param name="url" value="/events/event/${Event.id}"/>
                                </jsp:include>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
                
                <hr>
                <h4><fmt:message key="PastEventParticipations"/></h4>
                <c:choose>
                    <c:when test="${empty PastEvents}">
                        <div class="alert alert-info">
                            <fmt:message key="NoPastGames"><fmt:param value="${Team}"/></fmt:message>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="list-group">
                            <c:forEach var="Event" items="${PastEvents}">
                                <jsp:include page="/WEB-INF/jsp/include/list-badge-item.jsp">
                                    <jsp:param name="msg" value="${Event.name} / ${Event.startDate}"/>
                                    <jsp:param name="url" value="/events/event/${Event.id}"/>
                                </jsp:include>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
                
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
