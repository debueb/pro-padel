<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <div class="panel panel-info unit">
            <div class="panel-heading"><h4><fmt:message key="BookCourt"/></h4></div>
            <div class="panel-body">
                <spf:form method="POST" class="form-signin" role="form" modelAttribute="Booking">
                    <div class="alert alert-danger">${error}</div>
                    <h4><fmt:message key="BookingData"/></h4>

                    <jsp:include page="/jsp/bookings/include/booking-data.jsp"/>
                    <div class="row">
                        <div class="col-xs-4 booking-cell"><fmt:message key="Invoice"/>:</div>
                        <div class="col-xs-8 booking-cell"><a href="/invoices/booking/${Booking.UUID}"><i class="fa fa-file-text"></i></a></div>
                    </div>

                    <a class="btn btn-primary btn-block unit ajaxify" href="/bookings/booking/${Booking.UUID}/cancel"><fmt:message key="CancelBooking2"/></a>
                </spf:form>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>
