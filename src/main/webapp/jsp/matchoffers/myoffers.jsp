<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">

        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="MyOffers"/></h4>
            </div>
        </div>


        <div class="list-group unit">
            <c:forEach var="Model" items="${Models}">
                <c:set var="Model" value="${Model}" scope="request"/>
                <c:set var="OfferURL" value="/matchoffers/${Model.id}/edit" scope="request"/>
                <jsp:include page="/jsp/matchoffers/include/offer-list-item.jsp"/>
            </c:forEach>
        </div>
        <a class="btn btn-primary btn-block unit" href="/matchoffers/add"><fmt:message key="NewMatchOffer"/></a>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>