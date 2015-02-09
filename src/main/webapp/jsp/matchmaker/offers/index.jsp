<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        
        <div class="page-header">
            <h1><fmt:message key="MyOffers"/></h1>
        </div>
        
        <div class="list-group unit">
            <c:forEach var="Model" items="${Models}">
                <a href="/matchmaker/offers/edit/${Model.id}" class="list-group-item ajaxify">
                    <div class="list-item-text">${Model}</div>
                </a>
            </c:forEach>
        </div>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>