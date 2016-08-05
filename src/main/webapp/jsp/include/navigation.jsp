<%@include file="/jsp/include/include.jsp"%>
<c:forEach var="Module" items="${customerModules[sessionScope.customer.name]}" varStatus="status">
    <c:if test="${Module.showInMenu}">
        <jsp:include page="/jsp/include/list-menu-item.jsp">
            <jsp:param name="image" value="${Module.iconName}"/>
            <jsp:param name="title" value="${Module.title}"/>
            <jsp:param name="url" value="${Module.url}"/>
        </jsp:include>
    </c:if>
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
        <a href="/logout" class="menu-item"><fmt:message key="Logout"/>
            <span class="list-menu-item-icon">
                <div class="fa fa-sign-out"></div>
            </span>
        </a>
    </c:when>
    <c:otherwise>
        <fmt:message key="LoginSignup" var="title"/>
        <jsp:include page="/jsp/include/list-menu-item.jsp">
            <jsp:param name="image" value="sign-in"/>
            <jsp:param name="title" value="${title}"/>
            <jsp:param name="url" value="/login"/>
        </jsp:include>
    </c:otherwise>
</c:choose>