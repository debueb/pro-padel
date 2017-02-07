<%@include file="/jsp/include/include.jsp"%>
<a href="${param.url}" class="menu-item">${param.title}
    <c:if test="${not empty param.image}">
        <span class="list-menu-item-icon">
            <div class="fa fa-${param.image}"></div>
        </span> 
    </c:if>
</a>