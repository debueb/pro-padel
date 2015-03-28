<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        
        <div class="page-header">
            <h1><fmt:message key="Scores"/></h1>
        </div>

        <c:choose>
            <c:when test="${empty Events}">
                <fmt:message key="NoActiveEvents"/>
            </c:when>
            <c:otherwise>
                <div class="list-group">
                    <c:forEach var="Event" items="${Events}">
                        <a href="/scores/event/${Event.id}" class="list-group-item ajaxify">
                            <div class="list-item-text"><fmt:message key="ScoresOfEvent"><fmt:param>${Event.name}</fmt:param></fmt:message></div>
                        </a>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
