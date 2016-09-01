<%@include file="/jsp/include/include.jsp"%>
<c:if test="${CustomerModule.showInMenu}">
    <a href="${CustomerModule.url}" class="menu-item ${empty CustomerModule.subModules ? 'ajaxify' : 'hasSubmodules'}" ${CustomerModule.moduleType == "Link" ? 'target="blank"' : ''} id="Module-${CustomerModule.id}">${CustomerModule.title}
    </a>
    <c:if test="${not empty CustomerModule.subModules}">
        <span class="subModules Module-${CustomerModule.id}-subModules">
            <c:set var="firstSubmodule" value="true"/>
            <c:forEach var="SubModule" items="${CustomerModule.subModules}">
                <c:if test="${not firstSubmodule}">
                    <div class="menu-separator"></div>
                </c:if>
                <c:set var="firstSubmodule" value="false"/>
                <c:set var="CustomerModule" value="${SubModule}" scope="request"/>
                <div class="subModule" style="position: relative">
                    <jsp:include page="/jsp/include/list-menu-module.jsp"/>
                </div>
            </c:forEach>
        </span>
    </c:if>
</c:if>