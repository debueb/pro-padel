<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <div class="panel panel-info unit">
            <div class="panel-heading"><h4><fmt:message key="BookingData"/></h4></div>
            <div class="panel-body">

                <c:choose>
                    <c:when test="${not empty error}">
                        <div class="alert alert-danger">${error}</div>
                    </c:when>
                    <c:otherwise>
                        <spf:form method="POST" class="form-signin" role="form" modelAttribute="Booking">
                            <spf:input type="hidden" path="bookingType"/>
                            <spf:input type="hidden" path="publicBooking" value="true"/>
                            
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
                                    <spf:select path="offer" class="form-control select-simple" id="booking-court" data-container="body">
                                        <c:choose>
                                            <c:when test="${not empty SelectedOffer and SelectedOffer.id == OfferDurationPrice.offer.id}">
                                                <spf:option selected="true" value="${OfferDurationPrice.offer.id}" data-target="#booking-duration-container-${OfferDurationPrice.offer.id}">${OfferDurationPrice.offer}</spf:option>
                                            </c:when>
                                            <c:otherwise>
                                                <spf:option value="${OfferDurationPrice.offer.id}" data-target="#booking-duration-container-${OfferDurationPrice.offer.id}">${OfferDurationPrice.offer}</spf:option>
                                            </c:otherwise>
                                        </c:choose>
                                    </spf:select>
                                </div>
                            </div>

                            <div id="booking-duration-container-${OfferDurationPrice.offer.id}" class="booking-duration-container">
                                <div class="row unit">
                                    <div class="col-xs-4 booking-cell"><fmt:message key="GameDuration"/>:</div>
                                    <div class="col-xs-8">
                                        <spf:select path="duration" class="form-control select-simple booking-duration" data-container="body">
                                            <c:forEach var="DurationPrice" items="${OfferDurationPrice.durationPriceMap}">
                                                <fmt:formatNumber var="price" value="${DurationPrice['value']}" minFractionDigits="2" maxFractionDigits="2"/>
                                                <spf:option value="${DurationPrice['key']}" data-price="${price} ${OfferDurationPrice.config.currency.symbol}">${DurationPrice['key']} <fmt:message key="Minutes"/></spf:option>
                                            </c:forEach>
                                        </spf:select>
                                    </div>
                                </div>
                                    
                                <c:if test="${not empty SelectedOffer.offerOptions}">   
                                    <div class="row unit">
                                        <div class="col-xs-4 booking-cell"><fmt:message key="Options"/>:</div>
                                        <div class="col-xs-8">
                                            <fmt:message key="None" var="None"/>
                                            <spf:select 
                                                path="offerOptions" 
                                                class="form-control select-simple" 
                                                data-container="body"
                                                multiple="${fn:length(SelectedOffer.offerOptions)>1 ? 'multiple' : 'false'}">
                                                <spf:option value="" label="${None}"/>
                                                <spf:options items="${SelectedOffer.offerOptions}" itemLabel="description" itemValue="id"/>
                                            </spf:select>
                                        </div>
                                    </div>
                                </c:if>

                                <div class="row unit">
                                    <div class="col-xs-4 booking-cell"><fmt:message key="PaymentMethod"/>:</div>
                                    <div class="col-xs-8">
                                        <spf:select path="paymentMethod" class="form-control select-simple" data-container="body">
                                            <c:forEach var="PaymentMethod" items="${OfferDurationPrice.config.paymentMethods}">
                                                <spf:option value="${PaymentMethod}"><fmt:message key="${PaymentMethod}"/></spf:option>
                                            </c:forEach>
                                        </spf:select>
                                    </div>
                                </div>
                            </div>


                            <div class="row">
                                <div class="col-xs-4 booking-cell"><fmt:message key="Price"/>:</div>
                                <div class="col-xs-8 booking-cell"><span id="booking-price"><fmt:formatNumber value="${Booking.amount}" maxFractionDigits="2" minFractionDigits="2"/></span> ${Booking.currency}</div>
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
                                        <a class="btn btn-primary btn-block unit ajaxify" href="/bookings"><fmt:message key="Cancel"/></a>
                                    </c:otherwise>
                                </c:choose>

                            </div>
                        </spf:form>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>
