<%@include file="/jsp/include/include.jsp"%>
<ul class="nav navbar-nav">
    <c:forEach var="Module" items="${menuLinks[sessionScope.customer.name]}" varStatus="status">      
        <jsp:include page="/jsp/include/list-menu-item.jsp">
            <jsp:param name="image" value="${Module.iconName}"/>
            <jsp:param name="title" value="${Module.title}"/>
            <jsp:param name="url" value="${Module.url}"/>
        </jsp:include>
    </c:forEach>
    <c:if test="${not empty privileges}">
        <fmt:message key="Administration" var="title"/>
        <jsp:include page="/jsp/include/list-menu-item.jsp">
            <jsp:param name="image" value="gear"/>
            <jsp:param name="title" value="${title}"/>
            <jsp:param name="url" value="/admin"/>
        </jsp:include>
    </c:if>
    <c:choose>
        <c:when test="${not empty user}">
            <fmt:message key="Account" var="title"/>
            <jsp:include page="/jsp/include/list-menu-item.jsp">
                <jsp:param name="image" value="child"/>
                <jsp:param name="title" value="${title}"/>
                <jsp:param name="url" value="/account"/>
            </jsp:include>
            <fmt:message key="Logout" var="title"/>
            <jsp:include page="/jsp/include/list-menu-item.jsp">
                <jsp:param name="image" value="sign-out"/>
                <jsp:param name="title" value="${title}"/>
                <jsp:param name="url" value="/logout"/>
            </jsp:include>
        </c:when>
        <c:otherwise>
            <fmt:message key="Login" var="title"/>
            <jsp:include page="/jsp/include/list-menu-item.jsp">
                <jsp:param name="image" value="sign-in"/>
                <jsp:param name="title" value="${title}"/>
                <jsp:param name="url" value="/login"/>
            </jsp:include>
        </c:otherwise>
    </c:choose>
</ul>