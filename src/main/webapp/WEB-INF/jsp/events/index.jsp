<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>
        <jsp:include page="/WEB-INF/jsp/include/module-description.jsp"/>
     
        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="currentEvents"/></h4>
            </div>
            <div class="panel-body">
                <c:choose>
                    <c:when test="${empty CurrentEvents}">
                        <div class="alert alert-info">
                            <fmt:message key="NoCurrentEvents"/>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="list-group">
                            <c:forEach var="Event" items="${CurrentEvents}">
                                <a href="/events/event/${Event.id}" class="list-group-item">
                                    <div class="list-item-text">
                                        ${Event.name}
                                    </div>
                                    <div class="text-right"><joda:format value="${Event.startDate}" pattern="EE, dd. MMM. yyyy"/></div>
                                </a>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
