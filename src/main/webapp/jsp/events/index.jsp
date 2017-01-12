<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>
        <jsp:include page="/jsp/include/module-description.jsp"/>
     
        <c:if test="${empty Models}">
            <div class="alert alert-info">
                <fmt:message key="NoActiveEvents"/>
            </div>
        </c:if>
        
        <div class="list-group">
            <c:forEach var="Event" items="${Models}">
                <a href="/events/event/${Event.id}" class="list-group-item ajaxify">
                    <div class="list-item-text">
                        ${Event.name}
                    </div>
                    <div class="text-right"><joda:format value="${Event.startDate}" pattern="EE, dd. MMMM yyyy"/></div>
                </a>
            </c:forEach>
        </div>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>
