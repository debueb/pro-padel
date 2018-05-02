<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>
<fmt:message key="NonPublicBooking" var="BookingMsg"/>
<fmt:message key="NotifyWhenAvailable" var="NotifyWhenAvailable"/>
<div class="row">
    <div class="col-xs-12 col-lg-10 col-lg-offset-1">
        <div class="page-header"></div>

        <c:if test="${fn:contains(sessionScope.privileges,'ManageBookings')}">
            <div class="text-center"><a class="btn btn-primary unit" href="/admin/bookings/reservations?startDate=${Day}" style="width: 300px"><fmt:message key="AdministerBookingsAndReservations"/></a></div>
            <div class="text-center"><a class="btn btn-primary unit" href="/admin/bookings/reservations/add?date=${Day}" style="width: 300px"><fmt:message key="AddReservation"/></a></div>
        </c:if>
        <jsp:include page="/WEB-INF/jsp/include/module-description.jsp"/>

        <div class="panel panel-info unit-2">
            <div class="panel-heading"><h4><fmt:message key="Date"/></h4></div>
            <div class="panel-body">

                <form method="GET" action="${contextPath}/bookings" data-anchor="#date" id="date">
                    <div class="datepicker-container ">
                        <div class="datepicker-text-container ${empty RangeMap or empty Facilities ? '' : 'form-top-element'}">
                            <div class="datepicker-label"><fmt:message key="Date"/></div>
                            <span class="fa fa-calendar datepicker-icon"></span>
                            <div class="datepicker-text"></div>
                        </div>
                        <input type="hidden" name="date" class="datepicker-input auto-submit" class="form-control" value="${Day}"/>
                        <div class="datepicker" data-show-on-init="false" data-redirect-on-select="/bookings/{date}/{time}" data-day-config='${dayConfigs}' data-max-date='${maxDate}'></div>
                    </div>
                    <c:if test="${not empty RangeMap}">
                        <c:if test="${not empty Facilities}">
                            <div class="relative">
                                <select name="facilities" class="select-multiple form-control form-bottom-element auto-submit" multiple="true" data-container="body">
                                    <c:forEach var="Facility" items="${Facilities}">
                                        <option value="${Facility.id}" ${as:contains(SelectedFacilities, Facility) ? 'selected="selected"' : ''}>
                                            ${Facility.name}
                                        </option>
                                    </c:forEach>
                                </select>
                                <span class="explanation-select"><fmt:message key="Offers"/></span>
                            </div>
                        </c:if>
                    </c:if>
                </form>
            </div>
        </div>
    </div>
</div>
<div class="row">
    <div class="col-xs-12 col-lg-10 col-lg-offset-1">
    <c:choose>
        <c:when test="${empty RangeMap}">
            <div class="alert alert-info">
                <fmt:message key="NoBookableTimeSlotsAvailable"/>
            </div>
        </c:when>
        <c:otherwise>
            <fmt:message key="BookingPending" var="BookingPendingMsg"/>
            <c:set var="BookingPendingMsg" value="<br />${BookingPendingMsg}"/>
            <div class="booking-gallery">
                <div class="booking-gallery-time">
                    <c:forEach var="TimeRange" items="${RangeMap}" varStatus="status">
                        <div><joda:format value="${TimeRange.startTime}" pattern="HH:mm" /></div>
                    </c:forEach>
                </div>
                <div class="booking-slick">
                    <c:forEach var="WeekDay" items="${WeekDays}">
                        <div>
                            <div class="booking-gallery-day">
                                <fmt:message key="DayShort-${WeekDay.dayOfWeek}"/>, <joda:format value="${WeekDay}" pattern="dd.MM."/>
                            </div>
                            <div class="booking-gallery-offers">
                                <c:forEach var="Offer" items="${SelectedOffers}">
                                    <fmt:formatNumber var="rowWidth" value="${100 / fn:length(SelectedOffers)}"/>
                                    <div class="booking-gallery-offer-row" style="width: ${rowWidth}%;">
                                        <div    class="booking-gallery-header"
                                                style="background-color: ${Offer.hexColor};"
                                                data-toggle="tooltip"
                                                data-placement="top"
                                                data-content="${Offer}">
                                            ${Offer.shortName}
                                        </div>
                                        <c:forEach var="TimeRange" items="${RangeMap}">
                                            <c:set var="hourFilled" value="false" />
                                            <c:forEach var="TimeSlot" items="${TimeRange.timeSlots}">
                                                <c:if test="${TimeSlot.date.dayOfWeek eq WeekDay.dayOfWeek}">
                                                    <c:set var="bgColor" value="${TimeSlot.past ? '#e7e7e7' : Offer.hexColor}"/>
                                                    <c:choose>
                                                        <c:when test="${fn:contains(TimeSlot.availableOffers, Offer)}">
                                                            <joda:format value="${TimeRange.startTime}" pattern="HH:mm" var="startTime"/>
                                                            <c:set var="urlDetail" value="/bookings/${TimeSlot.date}/${startTime}"/>
                                                            <div class="booking-gallery-offer">
                                                                <a href="${urlDetail}/offer/${Offer.id}" title="${Offer.name} ${startTime}" style="background-color: ${bgColor};${TimeSlot.past ? 'color: #ccc;' : ''}">
                                                                    ${TimeSlot.pricePerMinDuration}
                                                                </a>
                                                            </div>
                                                            <c:set var="hourFilled" value="true" />
                                                        </c:when>
                                                        <c:when test="${not empty TimeSlot.bookings}">
                                                            <c:set var="timeSlotFilled" value="false"/>
                                                            <c:forEach var="Booking" items="${TimeSlot.bookings}">
                                                                <c:if test="${Booking.offer eq Offer}">
                                                                    <c:choose>
                                                                        <c:when test="${TimeSlot.endTime eq Booking.bookingEndTime}">
                                                                            <fmt:formatNumber var="factor" value="${Booking.duration / TimeSlot.config.minInterval}"/>
                                                                            <div class="booking-gallery-offer-taken" style="height: ${factor * 40}px;">
                                                                                <div data-toggle="booking-tooltip"
                                                                                     data-booking="${Booking.UUID}"
                                                                                     data-placement="top"
                                                                                     data-container="body"
                                                                                     data-title="<joda:format value="${Booking.bookingTime}" pattern="HH:mm"/> - <joda:format value="${Booking.bookingEndTime}" pattern="HH:mm"/>">
                                                                                    ${Booking.publicBooking ? empty Booking.comment ? Booking.player : Booking.comment : BookingMsg}
                                                                                    <br/>
                                                                                    <i class="fa fa-info-circle text-center"></i>
                                                                                </div>
                                                                            </div>
                                                                            <c:set var="timeSlotFilled" value="true"/>
                                                                        </c:when>
                                                                        <c:when test="${TimeSlot.startTime ge Booking.bookingTime}">
                                                                            <c:set var="timeSlotFilled" value="true"/>
                                                                        </c:when>
                                                                        <c:when test="${not timeSlotFilled}">
                                                                            <div class="booking-gallery-offer" style="background-color: ${bgColor};"></div>
                                                                            <c:set var="timeSlotFilled" value="true"/>
                                                                        </c:when>
                                                                    </c:choose>
                                                                </c:if>
                                                                <c:set var="hourFilled" value="${timeSlotFilled}" />
                                                            </c:forEach>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <div class="booking-gallery-offer" style="background-color: ${bgColor};"></div>
                                                            <c:set var="hourFilled" value="true" />
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:if>
                                            </c:forEach>
                                         <c:if test="${not hourFilled}">
                                            <div class="booking-gallery-offer booking-gallery-offer" style="background-color: ${Offer.hexColor};"></div>
                                         </c:if>
                                    </c:forEach>
                                    <div class="booking-gallery-header" style="background-color: ${Offer.hexColor};">
                                        <div    data-toggle="tooltip"
                                                data-placement="bottom"
                                                data-content="${Offer}">
                                            ${Offer.shortName}
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                            </div>
                            <div class="booking-gallery-day booking-gallery-day-bottom">
                                <fmt:message key="DayShort-${WeekDay.dayOfWeek}"/>, <joda:format value="${WeekDay}" pattern="dd.MM."/>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <div class="unit-2">
                <jsp:include page="/WEB-INF/jsp/bookings/include/leyenda.jsp"/>
            </div>

            <joda:parseDateTime var="jodaDate" pattern="yyyy-MM-dd" value="${Day}"/>
            <script type="text/javascript">
                $(document).ready(function () {
                    $('.booking-slick').on('init', function(event, slick, currentSlide){
                        var prevEnabled = false,
                            nextEnabled = false,
                            checkSlideStatus = function(slick, currentSlide){
                                var lastSlide = currentSlide + slick.slickGetOption('slidesToShow');
                                if (currentSlide === 0){
                                    prevEnabled = true;
                                } else if (lastSlide === 7){
                                    nextEnabled = true;
                                } else {
                                    prevEnabled = false;
                                    nextEnabled = false;
                                }
                            };
                        checkSlideStatus(slick, slick.slickCurrentSlide());
                        
                        $('.booking-slick').on('afterChange', function(event, slick, currentSlide){
                            checkSlideStatus(slick, currentSlide);
                        });
        
                        $('.slick-next').on('click tap', function(){
                            if (nextEnabled){
                                nextEnabled = false;
                                $('input[name="date"]').val('${NextMonday}').trigger('change');
                            }
                        });
                        $('.slick-prev').on('click tap', function(){
                            if (prevEnabled){
                                prevEnabled = false;
                                $('input[name="date"]').val('${PrevSunday}').trigger('change');
                            }
                        });
                    });
        
                    
                    $('.booking-slick').slick({
                        infinite: false,
                        mobileFirst: true,
                        arrows: true,
                        adaptiveHeight: true,
                        initialSlide: ${jodaDate.dayOfWeek-1},
                        responsive: [
                            {
                                breakpoint: 1679,
                                settings: {
                                    slidesToShow: 3
                                }
                            }, {
                                breakpoint: 992,
                                settings: {
                                    slidesToShow: 2
                                }
                            }, {
                                breakpoint: 480,
                                settings: {
                                    slidesToShow: 1
                                }
                            }]
                    });
                });
            </script>
            <div class="unit-2"></div>
        </c:otherwise>
    </c:choose>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
