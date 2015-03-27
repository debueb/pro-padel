<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <div class="page-header">
            <h1><fmt:message key="Events"/></h1>
        </div>

        <div class="list-group">
            <c:choose>
                <c:when test="${empty Models}">
                    <fmt:message key="NoActiveEvents"/>
                </c:when>
                <c:otherwise>
                    <c:forEach var="Event" items="${Models}">
                        <a href="/scores/event/${Event.id}" class="list-group-item ajaxify">
                            <div class="list-item-text">${Event.name}</div>
                            <p class="list-group-item-text"><joda:format value="${Event.startDate}" pattern="dd. MMM. yyyy"/> - <joda:format value="${Event.endDate}" pattern="dd. MMM. yyyy"/></p>
                        </a>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>
