<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-md-10 col-md-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <jsp:include page="/jsp/include/module-description.jsp"/>
        
        <div class="panel panel-info unit">
            <div class="panel-heading"><h4><fmt:message key="Date"/></h4></div>
            <div class="panel-body">

                <form method="GET" class="ajaxify" action="${contextPath}/bookings">
                    <div class="datepicker-container ">
                        <div class="datepicker-text-container ${empty RangeMap or empty Facilities ? '' : 'form-top-element'}">
                            <div class="datepicker-label"><fmt:message key="Date"/></div>
                            <span class="fa fa-calendar datepicker-icon"></span>
                            <div class="datepicker-text"></div>
                        </div>
                        <input type="hidden" name="date" class="datepicker-input auto-submit" class="form-control" value="${Day}" />
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
    <div class="col-xs-12 col-md-10 col-md-offset-1 col-lg-8 col-lg-offset-2">
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
                    <c:forEach var="TimeRange" items="${RangeMap}">
                        <joda:format value="${TimeRange.startTime}" pattern="HH:mm" var="startTime"/>
                        <div>${startTime}</div>
                    </c:forEach>
                </div>
                <div class="booking-slick">

                    <c:forEach var="WeekDay" items="${WeekDays}">
                        <div>
                            <div class="booking-gallery-day">
                                <fmt:message key="DayShort-${WeekDay.dayOfWeek}"/>, <joda:format value="${WeekDay}" pattern="dd.MM."/>
                            </div>
                            <table class="table table-booking table-fixed">
                                <thead>
                                    <tr>
                                        <c:forEach var="Offer" items="${SelectedOffers}">
                                            <c:if test="${Offer.showInCalendar}">
                                                <th style="background-color: ${Offer.hexColor};">
                                                    <div    data-toggle="tooltip" 
                                                            data-placement="top"
                                                            title="${Offer}">
                                                        ${Offer.shortName}
                                                    </div>
                                                </th>
                                            </c:if>
                                        </c:forEach>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="TimeRange" items="${RangeMap}">
                                        <tr>
                                            <c:forEach var="Offer" items="${SelectedOffers}">
                                                <c:if test="${Offer.showInCalendar}">
                                                    <td>
                                                        <c:set var="containsTimeSlot" value="false"/>
                                                        <c:forEach var="TimeSlot" items="${TimeRange.timeSlots}">
                                                            <c:if test="${TimeSlot.date.dayOfWeek eq WeekDay.dayOfWeek}">
                                                                <c:set var="containsTimeSlot" value="true"/>
                                                                <c:choose>
                                                                    <c:when test="${fn:contains(TimeSlot.availableOffers, Offer)}">
                                                                        <joda:format value="${TimeRange.startTime}" pattern="HH:mm" var="startTime"/>
                                                                        <c:set var="urlDetail" value="/bookings/${TimeSlot.date}/${startTime}"/>
                                                                        <a class="ajaxify booking-gallery-offer" href="${urlDetail}/offer/${Offer.id}" title="${Offer.name} ${startTime}" style="background-color: ${Offer.hexColor};">
                                                                            ${TimeSlot.pricePerMinDuration}
                                                                        </a>
                                                                    </c:when>
                                                                    <c:when test="${not empty TimeSlot.bookings}">
                                                                        <c:forEach var="Booking" items="${TimeSlot.bookings}">
                                                                            <c:if test="${Booking.publicBooking and Booking.offer eq Offer and TimeSlot.startTime ge Booking.bookingTime}">
                                                                                <i class="fa fa-info-circle text-center" data-toggle="tooltip" data-placement="top" data-container="body" title="${not empty Booking.comment ? Booking.comment : Booking.player}<br /> <joda:format value="${Booking.bookingTime}" pattern="HH:mm"/> - <joda:format value="${Booking.bookingEndTime}" pattern="HH:mm"/>${Booking.confirmed ? '' : BookingPendingMsg}"></i>
                                                                            </c:if>
                                                                        </c:forEach>
                                                                    </c:when>
                                                                </c:choose>
                                                            </c:if>
                                                        </c:forEach>
                                                    </td>
                                                </c:if>
                                            </c:forEach>
                                        </tr>
                                    </c:forEach>
                                    <tr>
                                        <c:forEach var="Offer" items="${SelectedOffers}">
                                            <c:if test="${Offer.showInCalendar}">
                                                <td style="background-color: ${Offer.hexColor};">
                                                    <div    data-toggle="tooltip" 
                                                            data-placement="bottom"
                                                            title="${Offer}">
                                                        ${Offer.shortName}
                                                    </div>
                                                </td>
                                            </c:if>
                                        </c:forEach>
                                    </tr>
                                </tbody>
                            </table>
                            <div class="booking-gallery-day booking-gallery-day-bottom">
                                <fmt:message key="DayShort-${WeekDay.dayOfWeek}"/>, <joda:format value="${WeekDay}" pattern="dd.MM."/>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <div class="unit-2">
                <jsp:include page="/jsp/bookings/include/leyenda.jsp"/>
            </div>

            <joda:parseDateTime var="jodaDate" pattern="yyyy-MM-dd" value="${Day}"/>
            <script type="text/javascript">
                $(document).ready(function () {
                    $('.booking-slick').slick({
                        infinite: false,
                        mobileFirst: true,
                        arrows: true,
                        adaptiveHeight: true,
                        initialSlide: ${jodaDate.dayOfWeek-1},
                        responsive: [
                            {
                                breakpoint: 1600,
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
<jsp:include page="/jsp/include/footer.jsp"/>
