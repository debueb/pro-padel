<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <div class="panel panel-info unit">
            <div class="panel-heading"><h4><fmt:message key="BookCourt"/></h4></div>
            <div class="panel-body">
                <spf:form method="POST" class="form-signin" role="form" modelAttribute="Booking">
                    <div class="alert alert-danger">${error}</div>
                    <h4><fmt:message key="BookingData"/></h4>

                    <div class="row">
                        <div class="col-xs-4 booking-cell"><fmt:message key="Offer"/>:</div>
                        <div class="col-xs-8 booking-cell">${Booking.event}</div>
                    </div>                   
                    <div class="row">
                        <div class="col-xs-4 booking-cell"><fmt:message key="GameDate"/>:</div>
                        <div class="col-xs-8 booking-cell"><joda:format value="${Booking.bookingDate}" pattern="EEEE, dd. MMMM yyyy"/></div>
                    </div>
                    <div class="row">
                        <div class="col-xs-4 booking-cell"><fmt:message key="GameStart"/>:</div>
                        <div class="col-xs-8 booking-cell"><joda:format value="${Booking.bookingTime}" pattern="HH:mm"/></div>
                    </div>
                    <div class="row">
                        <div class="col-xs-4 booking-cell"><fmt:message key="Price"/>:</div>
                        <div class="col-xs-8 booking-cell"><fmt:formatNumber value="${Booking.amount}" minFractionDigits="2" maxFractionDigits="2"/> ${Booking.currency.symbol}</div>
                    </div>
                    <div class="row">
                        <div class="col-xs-4 booking-cell"><fmt:message key="PaymentMethod"/>:</div>
                        <div class="col-xs-8 booking-cell"><fmt:message key="${Booking.paymentMethod}"/></div>
                    </div>

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
                    <label class="checkbox" for="accept-cancellation-policy"><small><fmt:message key="BookingEventCancellationPolicy"/></small></label>
                    </div>
                    <button class="btn btn-primary btn-block unit" type="submit"><fmt:message key="Confirm"/></button>
                    <a class="btn btn-primary btn-block unit" href="/events/event/${Booking.event.id}"><fmt:message key="Cancel"/></a>
                </spf:form>
            </div>
        </div>

    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
