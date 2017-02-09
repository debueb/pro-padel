<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-md-10 col-md-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>
        
        <ol class="unit-2 breadcrumb">
            <li><a class="ajaxify" href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a class="ajaxify" href="/admin/reports"><fmt:message key="Reports"/></a></li>
            <li class="active"><fmt:message key="Allocations"/></li>
        </ol>

        <jsp:include page="/jsp/include/module-description.jsp"/>
        
        <div class="panel panel-info unit">
            <div class="panel-heading"><h4><fmt:message key="Allocations"/></h4></div>
            <div class="panel-body">

                <form method="GET" class="ajaxify" action="">
                    <div class="datepicker-container ">
                        <div class="datepicker-text-container ${empty RangeMap or empty Facilities ? '' : 'form-top-element'}">
                            <div class="datepicker-label"><fmt:message key="Date"/></div>
                            <span class="fa fa-calendar datepicker-icon"></span>
                            <div class="datepicker-text"></div>
                        </div>
                        <input type="hidden" name="date" class="datepicker-input auto-submit" class="form-control" value="${Day}"/>
                        <div class="datepicker" data-show-on-init="false" data-redirect-on-select="/admin/bookings/allocations/?date={date}" data-day-config='${dayConfigs}' data-max-date='${maxDate}' data-allow-past="true"></div>
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
                        <div>${status.index} ${startTime}</div>
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
                                            <th style="background-color: ${Offer.hexColor};">
                                                <div    data-toggle="tooltip" 
                                                        data-placement="top"
                                                        data-content="${Offer}">
                                                    ${Offer.shortName}
                                                </div>
                                            </th>
                                        </c:forEach>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="TimeRange" items="${RangeMap}">
                                        <tr>
                                            <c:forEach var="Offer" items="${SelectedOffers}">
                                                <td>
                                                    <c:set var="containsTimeSlot" value="false"/>
                                                    <c:forEach var="TimeSlot" items="${TimeRange.timeSlots}">
                                                        <c:if test="${TimeSlot.date.dayOfWeek eq WeekDay.dayOfWeek}">
                                                            <c:set var="containsTimeSlot" value="true"/>
                                                            <c:choose>
                                                                <c:when test="${fn:contains(TimeSlot.availableOffers, Offer)}">
                                                                    -
                                                                </c:when>
                                                                <c:when test="${not empty TimeSlot.bookings}">
                                                                    <c:set var="timeSlotFilled" value="false"/>
                                                                    <c:forEach var="Booking" items="${TimeSlot.bookings}">
                                                                        <c:if test="${Booking.offer eq Offer}">
                                                                            <c:choose>
                                                                                <c:when test="${TimeSlot.startTime ge Booking.bookingTime}">
                                                                                    <a class="ajaxify booking-gallery-allocation" style="background-color: ${Offer.hexColor};" href="/admin/bookings/reservations/booking/${Booking.id}">
                                                                                        ${empty Booking.comment ? Booking.player : Booking.comment}
                                                                                    </a>
                                                                                    <c:set var="timeSlotFilled" value="true"/>
                                                                                </c:when>
                                                                                <c:when test="${not timeSlotFilled}">
                                                                                    <div class="booking-gallery-offer" style="background-color: ${Offer.hexColor};"></div>
                                                                                </c:when>
                                                                            </c:choose>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                </c:when>
                                                            </c:choose>
                                                        </c:if>
                                                    </c:forEach>
                                                </td>
                                            </c:forEach>
                                        </tr>
                                    </c:forEach>
                                    <tr>
                                        <c:forEach var="Offer" items="${SelectedOffers}">
                                            <td style="background-color: ${Offer.hexColor};">
                                                <div    data-toggle="tooltip" 
                                                        data-placement="bottom"
                                                        data-content="${Offer}">
                                                    ${Offer.shortName}
                                                </div>
                                            </td>
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
                                $('input[name="date"]').val('${NextMonday}');
                            }
                        });
                        $('.slick-prev').on('click tap', function(){
                            if (prevEnabled){
                                prevEnabled = false;
                                $('input[name="date"]').val('${PrevSunday}');
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
