<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        
        <div class="page-header">
            <h1><fmt:message key="MyOffers"/></h1>
        </div>
        
        <div class="list-group unit">
            <c:forEach var="Model" items="${Models}">
                <c:set var="Model" value="${Model}" scope="request"/>
                <c:set var="OfferURL" value="/matchmaker/offers/${Model.id}/edit" scope="request"/>
                <jsp:include page="/jsp/matchmaker/offers/include/offer-list-item.jsp"/>
            </c:forEach>
        </div>
        <a class="btn btn-primary btn-block unit" href="/matchmaker/offers/add"><fmt:message key="NewMatchOffer"/></a>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>