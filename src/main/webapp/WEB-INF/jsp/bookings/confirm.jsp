<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <div class="panel panel-info unit">
            <div class="panel-heading"><h4><fmt:message key="ConfirmBooking"/></h4></div>
            <div class="panel-body">
                <spf:form method="POST" class="${Booking.paymentMethod == 'PayPal' ? 'no-ajaxify' : ''}" modelAttribute="Booking">
                    <div class="alert alert-danger">${error}</div>
                    <c:if test="${empty error}">
                        <div class="alert alert-danger"><fmt:message key="AcceptPolicyToConfirmBooking"/></div>
                    </c:if>
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

                    <div>
                    <input type="checkbox" name="accept-cancellation-policy" id="accept-cancellation-policy"/>
                    <label class="checkbox" for="accept-cancellation-policy"><small><fmt:message key="BookingCancellationPolicy"><fmt:param value="${CancellationPolicyDeadline}"/></fmt:message></small></label>
                    </div>
                    <div>
                        <input type="checkbox" name="public-booking" id="public-booking" ${Booking.publicBooking ? 'checked' : ''}/>
                        <label class="checkbox" for="public-booking"><small><fmt:message key="PublicBooking"/></small></label>
                    </div>
                    <button class="btn btn-primary btn-block unit" type="submit"><fmt:message key="Confirm"/></button>
                    <a class="btn btn-primary btn-block unit" href="/bookings"><fmt:message key="Cancel"/></a>
                </spf:form>
            </div>
        </div>

    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
