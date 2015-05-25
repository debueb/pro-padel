<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="MatchOffer"/></h4>
            </div>
            <div class="panel-body">
                <div class="alert alert-danger"><fmt:message key="MatchOfferExpired"/></div>

                <jsp:include page="/jsp/matchoffers/include/offer-details.jsp"/>

                <a class="btn btn-primary btn-block" href="/matchoffers/add"><fmt:message key="NewMatchOffer"/></a>
                <a class="btn btn-primary btn-block" href="/matchoffers"><fmt:message key="OtherMatchOffers"/></a>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>
