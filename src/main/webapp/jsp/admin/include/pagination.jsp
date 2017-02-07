<%@include file="/jsp/include/include.jsp"%>
<c:set var="currentElements" value="${as:min((Page.number+1) * Page.size, Page.totalElements)}"/>
<c:if test="${not empty Page.size and Page.size > 0 and (param.page > 0 || currentElements < Page.totalElements)}">
    <nav class="unit">
        <div>
            <fmt:message key="OneToTenOfFifteenTotal">
                <fmt:param value="${(Page.number+1) * Page.size - Page.size + 1}"/>
                <fmt:param value="${currentElements}"/>
                <fmt:param value="${Page.totalElements}"/>
            </fmt:message>
        </div>
        <ul class="pagination">
            <c:forEach begin="0" end="${Page.totalElements-1}" step="${Page.size}" var="count" varStatus="status">
                <li class="${Page.number == status.count-1 ? 'active' : ''}"><a href="?page=${status.count-1}">${status.count}</a></li>
                </c:forEach>
        </ul>
    </nav>
</c:if>