<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<c:forEach var="PageEntry" items="${PageEntries}">
    <c:set var="PageEntry" value="${PageEntry}" scope="request"/>
    <jsp:include page="/jsp/page/pageentry.jsp"/>
</c:forEach>
<jsp:include page="/jsp/include/footer.jsp"/>