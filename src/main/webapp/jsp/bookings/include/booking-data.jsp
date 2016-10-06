<%@include file="/jsp/include/include.jsp"%>
<div class="row">
    <div class="col-xs-4 booking-cell"><fmt:message key="Offer"/>:</div>
    <div class="col-xs-8 booking-cell">${Booking.name}</div>
</div>                   
<div class="row">
    <div class="col-xs-4 booking-cell"><fmt:message key="GameDate"/>:</div>
    <div class="col-xs-8 booking-cell"><joda:format value="${Booking.bookingDate}" pattern="EEEE, dd. MMMM yyyy"/></div>
</div>
<div class="row">
    <div class="col-xs-4 booking-cell"><fmt:message key="GameStart"/>:</div>
    <div class="col-xs-8 booking-cell"><joda:format value="${Booking.bookingTime}" pattern="HH:mm"/></div>
</div>
<c:if test="${not empty Booking.duration}">
    <div class="row">
        <div class="col-xs-4 booking-cell"><fmt:message key="GameDuration"/>:</div>
        <div class="col-xs-8 booking-cell">${Booking.duration} <fmt:message key="Minutes"/></div>
    </div>
</c:if>
<div class="row">
    <div class="col-xs-4 booking-cell"><fmt:message key="Price"/>:</div>
    <div class="col-xs-8 booking-cell"><fmt:formatNumber value="${Booking.amount}" minFractionDigits="2" maxFractionDigits="2"/> ${Booking.currency.symbol}</div>
</div>
<div class="row">
    <div class="col-xs-4 booking-cell"><fmt:message key="PaymentMethod"/>:</div>
    <div class="col-xs-8 booking-cell"><fmt:message key="${Booking.paymentMethod}"/></div>
</div>