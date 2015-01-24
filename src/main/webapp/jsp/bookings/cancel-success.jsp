<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <div class="page-header">
            <h1><fmt:message key="CancelBooking"/></h1>
        </div>

        <div class="alert alert-success" role="alert"><fmt:message key="BookingCancellationSuccessMessage"><fmt:param value="${Booking.player.email}"/></fmt:message></div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
