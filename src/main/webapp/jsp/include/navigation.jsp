<%@include file="/jsp/include/include.jsp"%>
<ul class="nav navbar-nav">
    <jsp:include page="/jsp/include/list-menu-item.jsp">
        <jsp:param name="image" value="${not empty param.image ? 'rss' : ''}"/>
        <jsp:param name="key" value="News"/>
        <jsp:param name="url" value="/news"/>
    </jsp:include>
    <jsp:include page="/jsp/include/list-menu-item.jsp">
        <jsp:param name="image" value="${not empty param.image ? 'sitemap' : ''}"/>
        <jsp:param name="key" value="Events"/>
        <jsp:param name="url" value="/events"/>
    </jsp:include>
    <jsp:include page="/jsp/include/list-menu-item.jsp">
        <jsp:param name="image" value="${not empty param.image ? 'user' : ''}"/>
        <jsp:param name="key" value="Players"/>
        <jsp:param name="url" value="/players"/>
    </jsp:include>
    <jsp:include page="/jsp/include/list-menu-item.jsp">
        <jsp:param name="image" value="${not empty param.image ? 'users' : ''}"/>
        <jsp:param name="key" value="Teams"/>
        <jsp:param name="url" value="/teams"/>
    </jsp:include>
    <jsp:include page="/jsp/include/list-menu-item.jsp">
        <jsp:param name="image" value="${not empty param.image ? 'calendar' : ''}"/>
        <jsp:param name="key" value="BookCourt"/>
        <jsp:param name="url" value="/bookings"/>
    </jsp:include>
    <jsp:include page="/jsp/include/list-menu-item.jsp">
        <jsp:param name="image" value="${not empty param.image ? 'futbol-o' : ''}"/>
        <jsp:param name="key" value="MatchOffers"/>
        <jsp:param name="url" value="/matchoffers"/>
    </jsp:include>
    <jsp:include page="/jsp/include/list-menu-item.jsp">
        <jsp:param name="image" value="${not empty param.image ? 'envelope' : ''}"/>
        <jsp:param name="key" value="Contact"/>
        <jsp:param name="url" value="/contact"/>
    </jsp:include>
    <jsp:include page="/jsp/include/list-menu-item.jsp">
        <jsp:param name="image" value="${not empty param.image ? 'link' : ''}"/>
        <jsp:param name="key" value="Links"/>
        <jsp:param name="url" value="/links"/>
    </jsp:include>
    <c:if test="${not empty user}">
        <jsp:include page="/jsp/include/list-menu-item.jsp">
            <jsp:param name="image" value="${not empty param.image ? 'child' : ''}"/>
            <jsp:param name="key" value="Account"/>
            <jsp:param name="url" value="/account"/>
        </jsp:include>
    </c:if>
    <c:if test="${not empty privileges}">
        <jsp:include page="/jsp/include/list-menu-item.jsp">
            <jsp:param name="image" value="${not empty param.image ? 'gear' : ''}"/>
            <jsp:param name="key" value="Administration"/>
            <jsp:param name="url" value="/admin"/>
        </jsp:include>
    </c:if>
    <c:choose>
        <c:when test="${not empty user}">
            <jsp:include page="/jsp/include/list-menu-item.jsp">
                <jsp:param name="image" value="${not empty param.image ? 'sign-out' : ''}"/>
                <jsp:param name="key" value="Logout"/>
                <jsp:param name="url" value="/logout"/>
            </jsp:include>
    </c:when>
        <c:otherwise>
            <jsp:include page="/jsp/include/list-menu-item.jsp">
                <jsp:param name="image" value="${not empty param.image ? 'sign-in' : ''}"/>
                <jsp:param name="key" value="Login"/>
                <jsp:param name="url" value="/login"/>
            </jsp:include>
        </c:otherwise>
    </c:choose>
</ul>