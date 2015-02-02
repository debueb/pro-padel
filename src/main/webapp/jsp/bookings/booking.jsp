<%@ page pageEncoding="UTF-8" contentType="text/html" %> 
<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-6 col-sm-offset-3">
        <div class="page-header">
            <h1><fmt:message key="BookCourt"/></h1>
        </div>
        <c:choose>
            <c:when test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:when>
            <c:otherwise>
                <spf:form method="POST" class="form-signin" role="form" modelAttribute="Booking">
                    <spf:input type="hidden" path="bookingType"/>
                    <h4><fmt:message key="BookingData"/></h4>

                    <div class="row">
                        <div class="col-xs-4 booking-cell"><fmt:message key="GameDate"/>:</div>
                        <div class="col-xs-8 booking-cell"><joda:format value="${Booking.bookingDate}" pattern="EEEE, dd. MMMM yyyy"/></div>
                    </div>
                    <div class="row">
                        <div class="col-xs-4 booking-cell"><fmt:message key="GameStart"/>:</div>
                        <div class="col-xs-8 booking-cell"><joda:format  value="${Booking.bookingTime}" pattern="HH:mm"/></div>
                    </div>
                    
                    <div class="row">
                        <div class="col-xs-4 booking-cell"><fmt:message key="Offer"/>:</div>
                        <div class="col-xs-8">
                            <spf:select path="offer" class="form-control select-simple" id="booking-court">
                                <c:forEach items="${OfferDurations}" var="Offer">
                                    <spf:option value="${Offer['key'].id}" data-target="#booking-duration-container-${Offer['key'].id}">${Offer['key']}</spf:option>
                                </c:forEach>
                            </spf:select>
                        </div>
                    </div>
                   
                    <c:forEach items="${OfferDurations}" var="Offer">
                        <div class="row unit booking-duration-container" id="booking-duration-container-${Offer['key'].id}">
                            <div class="col-xs-4 booking-cell"><fmt:message key="GameDuration"/>:</div>
                            <div class="col-xs-8">
                                <spf:select path="duration" class="form-control select-simple booking-duration">
                                    <c:forEach var="Duration" items="${Offer['value']}">
                                        <spf:option value="${Duration}">${Duration} <fmt:message key="Minutes"/></spf:option>
                                    </c:forEach>
                                </spf:select>
                            </div>
                        </div>
                    </c:forEach>

                    <div class="row unit">
                        <div class="col-xs-4 booking-cell"><fmt:message key="PaymentMethod"/>:</div>
                        <div class="col-xs-8">
                            <spf:select path="paymentMethod" class="form-control select-simple">
                                <c:forEach var="PaymentMethod" items="${PaymentMethods}">
                                    <div>
                                        <span class="input-group-addon">
                                            <spf:option value="${PaymentMethod}"><fmt:message key="${PaymentMethod}"/></spf:option>
                                            </span>
                                        </div>
                                </c:forEach>
                            </spf:select>
                        </div>
                    </div>
                    
                    <div class="row">
                        <div class="col-xs-4 booking-cell"><fmt:message key="Price"/>:</div>
                        <div class="col-xs-8 booking-cell"><span id="booking-price" data-base-price="${Config.basePrice}" data-min-duration="${Config.minDuration}"><fmt:formatNumber value="${Booking.amount}" maxFractionDigits="2" minFractionDigits="2"/></span> ${Booking.currency}</div>
                    </div>

                    <div class="unit">
                        <c:choose>
                            <c:when test="${empty sessionScope.user}">
                                <div class="accordion unit">
                                    <div><fmt:message key="BookWithoutAccount"/></div>
                                    <div>
                                        <div><fmt:message key="BookWithoutAccountMessage"/></div>
                                        <button class="btn btn-primary unit btn-booking-submit unit" data-booking-type="nologin"><fmt:message key="BookWithoutAccount"/></button>
                                    </div>
                                    <div><fmt:message key="BookWithLogin"/></div>
                                    <div>
                                        <div><fmt:message key="BookWithLoginMessage"/></div>
                                        <button class="btn btn-primary btn-booking-submit unit" type="submit" data-booking-type="login"><fmt:message key="BookWithLogin"/></button>
                                    </div>
                                    <div><fmt:message key="BookWithRegistration"/></div>
                                    <div>
                                        <div><fmt:message key="BookWithRegistrationMessage"/></div>
                                        <button class="btn btn-primary unit btn-booking-submit unit" data-booking-type="register"><fmt:message key="BookWithRegistration"/></button>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <button class="btn btn-primary btn-block unit btn-booking-submit" data-booking-type="loggedIn"><fmt:message key="Book"/></button>
                                <a class="btn btn-primary btn-block unit ajaxify" href="/bookings/${Booking.bookingDate}"><fmt:message key="ChangeBooking"/></a>
                                <a class="btn btn-primary btn-block unit ajaxify" href="/bookings"><fmt:message key="Cancel"/></a>
                            </c:otherwise>
                        </c:choose>

                    </div>
                </spf:form>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>
