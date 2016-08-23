<%@include file="/jsp/include/include.jsp"%>
<c:if test="${Module.showInMenu}">
    <a href="${Module.url}" class="menu-item ${empty Module.subModules ? 'ajaxify' : 'hasSubmodules'}" id="Module-${Module.id}">${Module.title}
        <c:if test="${not empty Module.iconName}">
            <span class="list-menu-item-icon">
                <div class="fa fa-${Module.iconName}"></div>
            </span> 
        </c:if>
    </a>
    <c:if test="${not empty Module.subModules}">
        <span class="subModules Module-${Module.id}-subModules">
            <c:forEach var="SubModule" items="${Module.subModules}">
                <c:set var="Module" value="${SubModule}" scope="request"/>
                <div class="subModule" style="position: relative">
                    <jsp:include page="/jsp/include/list-menu-module.jsp"/>
                </div>
            </c:forEach>
        </span>
    </c:if>
</c:if>