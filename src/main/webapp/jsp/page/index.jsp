<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <div class="page-header">
        </div>
        <c:forEach var="Entry" items="${PageEntries}" varStatus="status">
            <div class="panel panel-info">
                <div class="panel-heading"><h4>${Entry.title}</h4></div>
                <div class="panel-body">${Entry.message}</div>
            </div>
            <c:if test="${not status.last}"><hr/></c:if>
        </c:forEach>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>