<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header">
        </div>
        <c:forEach var="PageEntry" items="${PageEntries}" varStatus="status" >
            <c:set var="PageEntry" value="${PageEntry}" scope="request"/>
            <jsp:include page="/jsp/page/pageentry.jsp"/>
            <c:if test="${not status.last}"><hr/></c:if>
        </c:forEach>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>