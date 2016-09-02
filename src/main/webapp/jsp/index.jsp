<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<c:forEach var="PageEntry" items="${PageEntries}">
    <c:set var="PageEntry" value="${PageEntry}" scope="request"/>
    <jsp:include page="/jsp/page/pageentry.jsp"/>
</c:forEach>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="container-flex stretch" id="container-home">
            <c:forEach var="Module" items="${customerModules[sessionScope.customer.name]}" varStatus="status">
                <c:if test="${Module.showOnHomepage}">
                    <jsp:include page="/jsp/include/flip-item.jsp">
                        <jsp:param name="url" value="${Module.url}"/>
                        <jsp:param name="title" value="${Module.title}"/>
                        <jsp:param name="iconName" value="${Module.iconName}"/>
                        <jsp:param name="desc" value="${Module.shortDescription}"/>
                    </jsp:include>
                </c:if>
            </c:forEach>
        </div>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>
<script src="/js/noconcat/99_addtohomescreen.min.js"></script>
<script defer type="text/javascript">
    if (!("standalone" in window.navigator) || !window.navigator.standalone) {
        addToHomescreen({
            skipFirstVisit: true,
            maxDisplayCount: 1
        });
    }
</script>