<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="blog-content">
    <c:forEach var="PageEntry" items="${PageEntries}">
        <c:set var="PageEntry" value="${PageEntry}" scope="request"/>
        <jsp:include page="/jsp/blog/blogentry.jsp"/>
    </c:forEach>
    <div class="row pageentry" id="blog-next">
        <div>${Page.number}</div>
        <div>${Page.last}</div>
        <div>${Page}</div>
        <div>${Page.number + 1}</div>
    </div>
</div>
<div class="unit-4"></div>
<jsp:include page="/jsp/include/footer.jsp"/>