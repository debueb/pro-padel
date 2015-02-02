<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <div class="unit"></div>
        <c:forEach var="News" items="${AllNews}">
            <div class="panel panel-info">
                <div class="panel-heading"><h4>${News.title}</h4></div>
                <div class="panel-body">${News.message}</div>
            </div>
            <hr>
        </c:forEach>
        
        <div class="list-group">
             <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/news"/>
                <jsp:param name="key" value="News"/>
                <jsp:param name="icon" value="rss"/>
            </jsp:include>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/events"/>
                <jsp:param name="key" value="Events"/>
                <jsp:param name="icon" value="sitemap"/>
            </jsp:include>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/players"/>
                <jsp:param name="key" value="Players"/>
                <jsp:param name="icon" value="user"/>
            </jsp:include>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/teams"/>
                <jsp:param name="key" value="Teams"/>
                <jsp:param name="icon" value="users"/>
            </jsp:include>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/bookings"/>
                <jsp:param name="key" value="BookCourt"/>
                <jsp:param name="icon" value="calendar"/>
            </jsp:include>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/contact"/>
                <jsp:param name="key" value="Contact"/>
                <jsp:param name="icon" value="envelope"/>
            </jsp:include>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/links"/>
                <jsp:param name="key" value="Links"/>
                <jsp:param name="icon" value="link"/>
            </jsp:include>

            <c:if test="${client == 'iOS'}">
                <jsp:include page="/jsp/include/list-group-item.jsp">
                    <jsp:param name="href" value="/app/ios"/>
                    <jsp:param name="key" value="iOSApp"/>
                    <jsp:param name="icon" value="apple"/>
                </jsp:include>
            </c:if>
            <c:if test="${client == 'Android'}">
                <jsp:include page="/jsp/include/list-group-item.jsp">
                    <jsp:param name="href" value="/app/android"/>
                    <jsp:param name="key" value="AndroidApp"/>
                    <jsp:param name="icon" value="android"/>
                </jsp:include>
            </c:if>
            <c:if test="${not empty user}">
                <jsp:include page="/jsp/include/list-group-item.jsp">
                    <jsp:param name="href" value="/account"/>
                    <jsp:param name="key" value="Account"/>
                    <jsp:param name="icon" value="child"/>
                </jsp:include>
            </c:if>
            <c:if test="${not empty privileges}">
                <jsp:include page="/jsp/include/list-group-item.jsp">
                    <jsp:param name="href" value="/admin"/>
                    <jsp:param name="key" value="Administration"/>
                    <jsp:param name="icon" value="gear"/>
                </jsp:include>
            </c:if>
            <c:choose>
                <c:when test="${not empty user}">
                    <jsp:include page="/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/logout"/>
                        <jsp:param name="key" value="Logout"/>
                        <jsp:param name="icon" value="sign-out"/>
                    </jsp:include>
                </c:when>
                <c:otherwise>
                    <jsp:include page="/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/login"/>
                        <jsp:param name="key" value="Login"/>
                        <jsp:param name="icon" value="sign-in"/>
                    </jsp:include>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
<script src="/js/noconcat/99_addtohomescreen.min.js"></script>
<script defer type="text/javascript">
    
    if (("standalone" in window.navigator) && window.navigator.standalone) {
        $("#iOSApp").hide();
    } else {
        addToHomescreen({
            skipFirstVisit: true,
            maxDisplayCount: 1
        });
    }
</script>

<jsp:include page="/jsp/include/footer.jsp"/>