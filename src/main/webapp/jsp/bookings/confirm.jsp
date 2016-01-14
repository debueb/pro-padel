<%@ page pageEncoding="UTF-8" contentType="text/html" %> 
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

                    <jsp:include page="include/booking-data.jsp"/>

                    <hr>

                    <h4><fmt:message key="PersonalData"/>:</h4>
                    <div class="row">
                        <div class="col-xs-4 booking-cell"><fmt:message key="FirstName"/>:</div>
                        <div class="col-xs-8 booking-cell">${Booking.player.firstName}</div>
                    </div>
                    <div class="row">
                        <div class="col-xs-4 booking-cell"><fmt:message key="LastName"/>:</div>
                        <div class="col-xs-8 booking-cell">${Booking.player.lastName}</div>
                    </div>
                    <div class="row">
                        <div class="col-xs-4 booking-cell"><fmt:message key="EmailAddress"/>:</div>
                        <div class="col-xs-8 booking-cell">${Booking.player.email}</div>
                    </div>
                    <div class="row">
                        <div class="col-xs-4 booking-cell"><fmt:message key="PhoneNumber"/>:</div>
                        <div class="col-xs-8 booking-cell">${Booking.player.phone}</div>
                    </div>

                    <input type="checkbox" name="accept-cancellation-policy" id="accept-cancellation-policy"/>
                    <label class="checkbox" for="accept-cancellation-policy"><small><fmt:message key="BookingCancellationPolicy"><fmt:param value="${CancellationPolicyDeadline}"/></fmt:message></small></label>

                    <button class="btn btn-primary btn-block unit" type="submit"><fmt:message key="Confirm"/></button>
                    <a class="btn btn-primary btn-block unit ajaxify" href="/bookings/?date=${Booking.bookingDate}"><fmt:message key="ChangeBooking"/></a>
                    <a class="btn btn-primary btn-block unit ajaxify" href="/bookings"><fmt:message key="Cancel"/></a>
                </spf:form>
            </div>
        </div>

    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>
