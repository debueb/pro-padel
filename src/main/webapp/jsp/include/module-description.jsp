<%@include file="/jsp/include/include.jsp"%>
<c:if test="${not empty Module}">
    <div class="relative">
        <c:if test="${fn:contains(sessionScope.privileges,'ManageGeneral')}">
           <a class="ajaxify edit-page" href="/admin/general/modules/edit/${Module.id}"><i class="fa fa-edit"></i></a>
        </c:if>
        ${Module.description}
    </div>
</c:if>
