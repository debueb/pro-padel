<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>
        
        <jsp:include page="/jsp/events/include/info.jsp"/>
        
        <c:forEach var="GroupParticipantGameResultMapEntry" items="${GroupParticipantGameResultMap}">
            <c:set var="GroupNumber" value="${GroupParticipantGameResultMapEntry.key}"/>
            <c:set var="ParticipantGameResultMap" value="${GroupParticipantGameResultMapEntry.value}" scope="request"/>
            <div class="panel panel-info unit">
                <div class="panel-heading">
                    <h4><fmt:message key="Group"/> ${GroupNumber+1}</h4>
                </div>
                <div class="panel-body">
                    <div class="list-group">
                        <c:set var="redirectUrl" value="events/event/${Model.id}/groupgames" scope="request"/>
                        <jsp:include page="/jsp/events/include/matchtable.jsp"/>
                    </div>
                </div>
            </div>
        </c:forEach>
        
        <c:if test="${empty RoundGameMap}">
            <a class="btn btn-primary btn-block unit ajaxify" href="/admin/events/event/${Model.id}/groupgamesend"><fmt:message key="EndGroupGames"/></a>
        </c:if>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
