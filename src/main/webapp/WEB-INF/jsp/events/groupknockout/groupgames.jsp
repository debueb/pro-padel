<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <jsp:include page="/WEB-INF/jsp/events/include/info.jsp"/>

        <div class="panel panel-info unit">
            <div class="panel-heading">
                <h4><fmt:message key="GroupGames"/>
                    <c:if test="${not empty Round}">
                        <fmt:message key="Round"/> ${Round+1}
                    </c:if>
                </h4>
            </div>
            <div class="panel-body">
                <c:forEach var="GroupParticipantGameResultMapEntry" items="${GroupParticipantGameResultMap}" varStatus="status">
                    <c:set var="GroupNumber" value="${GroupParticipantGameResultMapEntry.key}"/>
                    <c:set var="ParticipantGameResultMap" value="${GroupParticipantGameResultMapEntry.value}" scope="request"/>
                    <div class="panel panel-default">
                        <div class="panel-heading text-center">
                            <h4><fmt:message key="Group"/> ${GroupNumber+1}</h4>
                        </div>
                        <div class="panel-body" style="padding: 0;">
                            <div class="list-group">
                                <c:set var="redirectUrl" value="events/event/${Model.id}/groupgames" scope="request"/>
                                <c:if test="${not empty Round}">
                                    <c:set var="redirectUrl" value="${redirectUrl}/${Round}" scope="request"/>
                                </c:if>
                                <jsp:include page="/WEB-INF/jsp/events/include/matchtable.jsp"/>
                            </div>
                        </div>
                    </div>
                    <c:if test="${not status.last}">
                        <div style="padding-top: 20px"></div>
                    </c:if>
                </c:forEach>
                <c:if test="${empty RoundGameMap}">
                    <c:set var="endUrl" value="/admin/events/event/${Model.id}/groupgamesend"/>
                    <c:if test="${not empty Round}">
                        <c:set var="endUrl" value="${endUrl}/${Round}"/>
                    </c:if>
                    <c:if test="${empty Round or Round lt 1}">
                        <a class="btn btn-primary btn-block unit" href="${endUrl}"><fmt:message key="EndGroupGames"/></a>
                    </c:if>
                </c:if>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>