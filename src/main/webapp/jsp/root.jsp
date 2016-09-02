<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<c:forEach var="PageEntry" items="${PageEntries}">
    <c:set var="PageEntry" value="${PageEntry}" scope="request"/>
    <jsp:include page="/jsp/page/pageentry.jsp"/>
</c:forEach>
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