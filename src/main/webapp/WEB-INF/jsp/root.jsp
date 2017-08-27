<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>
<c:forEach var="PageEntry" items="${PageEntries}">
    <c:set var="PageEntry" value="${PageEntry}" scope="request"/>
    <jsp:include page="/WEB-INF/jsp/page/pageentry.jsp"/>
</c:forEach>
<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>