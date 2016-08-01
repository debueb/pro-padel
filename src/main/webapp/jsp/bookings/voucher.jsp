<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <div class="panel panel-info unit">
            <div class="panel-heading"><h4><fmt:message key="Voucher"/></h4></div>
            <div class="panel-body">
                <form class="form-signin paymill-form" method="POST" data-payment-type="directdebit">
                    <div id="error" class="alert alert-danger">${error}</div>

                    <div class="relative">
                        <input name="voucherUUID" class="form-control" type="text" />
                        <div class="explanation"><fmt:message key="Voucher"/></div>
                    </div>

                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Book"/></button>
                    <a class="btn btn-primary btn-block unit ajaxify" href="/bookings/booking/${Booking.UUID}/abort"><fmt:message key="Cancel"/></a>
                </form>
            </div>
        </div>

    </div>
</div>

<jsp:include page="/jsp/bookings/include/paymill.jsp"/>
<jsp:include page="/jsp/include/footer.jsp"/>