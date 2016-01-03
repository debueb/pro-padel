<%@ page pageEncoding="UTF-8" contentType="text/html" %> 
<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-col-xs-12 col-sm-8 col-sm-offset-2">
        <div class="page-header"></div>

        <div class="panel panel-info unit">
            <div class="panel-heading"><h4><fmt:message key="BookCourt"/></h4></div>
            <div class="panel-body">
                <spf:form method="POST" class="form-signin" role="form" modelAttribute="Booking">
                    <div class="alert alert-danger">${error}</div>
                    <h4><fmt:message key="BookingData"/></h4>

                    <jsp:include page="/jsp/bookings/include/booking-data.jsp"/>

                    <a class="btn btn-primary btn-block unit ajaxify" href="/bookings/booking/${Booking.UUID}/cancel"><fmt:message key="CancelBooking2"/></a>
                </spf:form>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>
