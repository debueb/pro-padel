<%@include file="/jsp/include/include.jsp"%>
<c:set var="first" value="true"/>
<c:forEach var="CustomerModule" items="${customerModules[sessionScope.customer.name]}" varStatus="status">
    <c:if test="${CustomerModule.showInMenu}">        
        <c:if test="${not first}">
            <div class="menu-separator"></div>
        </c:if>
        <c:set var="first" value="false"/>
        <c:set var="CustomerModule" value="${CustomerModule}" scope="request"/>
        <jsp:include page="/jsp/include/list-menu-module.jsp"/>
    </c:if>
</c:forEach> 
<c:if test="${not empty privileges}">
    <div class="menu-separator"></div>
    <fmt:message key="Administration" var="title"/>
    <jsp:include page="/jsp/include/list-menu-item.jsp">
        <jsp:param name="image" value="gear"/>
        <jsp:param name="title" value="${title}"/>
        <jsp:param name="url" value="/admin"/>
    </jsp:include>
</c:if>
<c:choose>
    <c:when test="${not empty user}">
        <div class="menu-separator"></div>
        <fmt:message key="Account" var="title"/>
        <jsp:include page="/jsp/include/list-menu-item.jsp">
            <jsp:param name="image" value="child"/>
            <jsp:param name="title" value="${title}"/>
            <jsp:param name="url" value="/account"/>
        </jsp:include>
        <div class="menu-separator"></div>
        <fmt:message key="Logout" var="title"/>
        <jsp:include page="/jsp/include/list-menu-item.jsp">
            <jsp:param name="image" value="sign-out"/>
            <jsp:param name="title" value="${title}"/>
            <jsp:param name="url" value="/logout"/>
        </jsp:include>
    </c:when>
    <c:otherwise>
        <div class="menu-separator"></div>
        <fmt:message key="LoginSignup" var="title"/>
        <jsp:include page="/jsp/include/list-menu-item.jsp">
            <jsp:param name="image" value="sign-in"/>
            <jsp:param name="title" value="${title}"/>
            <jsp:param name="url" value="/login"/>
        </jsp:include>
    </c:otherwise>
</c:choose>