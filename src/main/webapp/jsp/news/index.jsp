<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <div class="page-header">
            <h1>${Module.title}</h1>
        </div>
        <c:forEach var="News" items="${AllNews}" varStatus="status">
            <div class="panel panel-info">
                <div class="panel-heading"><h4>${News.title}</h4></div>
                <div class="panel-body">${News.message}</div>
            </div>
            <c:if test="${not status.last}"><hr/></c:if>
        </c:forEach>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>