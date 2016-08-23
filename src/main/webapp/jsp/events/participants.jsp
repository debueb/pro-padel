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
                <c:if test="${empty Model.participants}">
                    <div class="alert alert-info"><fmt:message key="NoParticipants"/></div>
                </c:if>
                    <c:forEach var="Participant" items="${Model.participants}">
                        <jsp:include page="/jsp/include/list-badge-item.jsp">
                            <jsp:param name="url" value="${Participant.discriminatorValue == 'Player' ? '/players/player/' : '/teams/team/'}${Participant.UUID}"/>
                            <jsp:param name="msg" value="${Participant}"/>
                        </jsp:include>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>
