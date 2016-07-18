<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>
        <c:forEach var="PageEntry" items="${PageEntries}">
            <c:set var="PageEntry" value="${PageEntry}" scope="request"/>
            <jsp:include page="/jsp/page/pageentry.jsp"/>
            <hr>
        </c:forEach>
            
        <div class="container-flex flex-wrap stretch" id="container-home">
            <c:forEach var="Module" items="${menuLinks[sessionScope.customer.name]}" varStatus="status">   
                <jsp:include page="/jsp/include/flip-item.jsp">
                    <jsp:param name="url" value="${Module.url}"/>
                    <jsp:param name="title" value="${Module.title}"/>
                    <jsp:param name="iconName" value="${Module.iconName}"/>
                    <jsp:param name="desc" value="${Module.description}"/>
                </jsp:include>
            </c:forEach>
            <c:if test="${not empty user}">
                <fmt:message key="AccountDesc" var="desc"/>
                <jsp:include page="/jsp/include/flip-item.jsp">
                    <jsp:param name="url" value="/account"/>
                    <jsp:param name="title" value="Account"/>
                    <jsp:param name="iconName" value="child"/>
                    <jsp:param name="desc" value="${desc}"/>
                </jsp:include>
            </c:if>
            <c:if test="${not empty privileges}">
                <fmt:message key="AdminDesc" var="desc"/>
                <jsp:include page="/jsp/include/flip-item.jsp">
                    <jsp:param name="url" value="/admin"/>
                    <jsp:param name="title" value="Administration"/>
                    <jsp:param name="iconName" value="gear"/>
                    <jsp:param name="desc" value="${desc}"/>
                </jsp:include>
            </c:if>
            <c:if test="${empty user}">
                <fmt:message key="LoginDesc" var="desc"/>
                <jsp:include page="/jsp/include/flip-item.jsp">
                    <jsp:param name="url" value="/login"/>
                    <jsp:param name="title" value="Login"/>
                    <jsp:param name="iconName" value="sign-in"/>
                    <jsp:param name="desc" value="${desc}"/>
                </jsp:include>
            </c:if>
        </div>
    </div>
</div>
<script src="/js/noconcat/99_addtohomescreen.min.js"></script>
<script defer type="text/javascript">
    if (!("standalone" in window.navigator) || !window.navigator.standalone) {
        addToHomescreen({
            skipFirstVisit: true,
            maxDisplayCount: 1
        });
    }
</script>

<jsp:include page="/jsp/include/footer.jsp"/>