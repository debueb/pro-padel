<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="blog-content">
    <c:forEach var="PageEntry" items="${PageEntries}">
        <c:set var="PageEntry" value="${PageEntry}" scope="request"/>
        <jsp:include page="/jsp/blog/blogentry.jsp"/>
    </c:forEach>
    <div class="row pageentry" id="blog-next">
        <c:if test="${not Page.last}">
        <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2 relative">
            <a class="btn btn-primary btn-block" href="?page=${Page.number + 1}" data-content=".blog-content" data-replace="#blog-next"><fmt:message key="LoadMore"/></a>
        </div>
        </c:if>
    </div>
</div>
<div class="unit-4"></div>
<jsp:include page="/jsp/include/footer.jsp"/>