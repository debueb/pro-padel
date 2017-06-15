<%@include file="/jsp/include/include.jsp"%>
<c:set var="currentElements" value="${Page.size == 0 ? Page.totalElements : as:min((Page.number+1) * Page.size, Page.totalElements)}"/>
<c:set var="numPrev" value="1"/>
<c:set var="numNext" value="1"/>
<%-- jstl does not have Math.ceil, so we always add 0.5 if it is less than 0.5 --%>
<fmt:formatNumber value="${Page.totalElements / Page.size + (Page.totalElements / Page.size % 1 == 0 ? 0 : 0.5)}" var="totalPages" maxFractionDigits="0"/>
<div class="text-center unit">
    <div>
        <fmt:message key="OneToTenOfFifteenTotal">
            <fmt:param value="${(Page.number+1) * Page.size - Page.size + 1}"/>
            <fmt:param value="${currentElements}"/>
            <fmt:param value="${Page.totalElements}"/>
        </fmt:message>
    </div>
    <c:if test="${not empty Page.size and Page.size > 0 and (param.page > 0 || currentElements < Page.totalElements)}">
        <ul class="pagination">
            <%-- always display first page --%>
            <li class="${empty param.page or param.page eq 0 ? 'active' : ''}"><a class="btn btn-pagination btn-primary" href="?page=0&size=${Page.size}">1</a></li>
            <c:choose>
                <%-- 1 2 --%>
                <c:when test="${totalPages-2 le 0}">
                </c:when>
                <%-- 1 2 3 4 5 --%>
                <c:when test="${totalPages-2 <= numPrev+numNext+1}">
                    <c:forEach begin="1" end="${as:min(totalPages-2, 2)}" var="current">
                         <li class="${param.page eq current ? 'active' : ''}">
                             <a class="btn btn-pagination btn-primary" href="?page=${current}&size=${Page.size}">${current+1}</a>
                         </li>
                    </c:forEach>
                </c:when>
                <%-- 1 ... 4 5 6 ... 100 --%>
                <c:when test="${Page.number > numPrev+numNext and Page.number < totalPages-1-numPrev-numNext}">
                    <li><a class="btn">...</a></li>
                    <c:forEach var="prev" begin="${Page.number-numPrev}" end="${Page.number-1}">
                        <li><a class="btn btn-pagination btn-primary" href="?page=${prev}&size=${Page.size}">${prev+1}</a></li>
                    </c:forEach>
                    <li class="active"><a class="btn btn-pagination btn-primary" href="?page=${Page.number}&size=${Page.size}">${Page.number+1}</a></li>
                    <c:forEach var="next" begin="1" end="${numNext}">
                        <li><a class="btn btn-pagination btn-primary" href="?page=${Page.number+next}&size=${Page.size}">${Page.number+next+1}</a></li>
                    </c:forEach>
                    <li><a class="btn">...</a></li>
                </c:when>
                <%-- 1 2 3 4 ... 100 --%>
                <c:when test="${Page.number <= 1+numPrev+numNext}">
                    <c:forEach var="next" begin="1" end="${1+numPrev+numNext}">
                        <li class="${param.page eq next ? 'active' : ''}"><a class="btn btn-pagination btn-primary" href="?page=${next}&size=${Page.size}">${next+1}</a></li>
                    </c:forEach>
                    <li><a class="btn">...</a></li>
                </c:when>
                <%-- 1 ... 97 98 99 100 --%>
                <c:otherwise>
                    <li><a class="btn">...</a></li>
                    <c:forEach var="prev" begin="${totalPages-2-numPrev-numNext}" end="${totalPages-2}">
                        <li class="${param.page eq prev ? 'active' : ''}"><a class="btn btn-pagination btn-primary" href="?page=${prev}&size=${Page.size}">${prev+1}</a></li>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            <%-- always display last page --%>
            <li class="${param.page eq totalPages-1 ? 'active' : ''}"><a class="btn btn-pagination btn-primary" href="?page=${totalPages-1}&size=${Page.size}">${totalPages}</a></li>
        </ul>
    </c:if>
</div>