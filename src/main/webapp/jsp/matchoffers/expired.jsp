<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header">
            <h1><fmt:message key="MatchOffer"/></h1>
        </div>
        
        <div class="alert alert-danger"><fmt:message key="MatchOfferExpired"/></div>
        
        <jsp:include page="/jsp/matchoffers/include/offer-details.jsp"/>

        <a class="btn btn-primary btn-block" href="/matchoffers/new"><fmt:message key="NewMatchOffer"/></a>
        <a class="btn btn-primary btn-block" href="/matchoffers"><fmt:message key="OtherMatchOffers"/></a>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
