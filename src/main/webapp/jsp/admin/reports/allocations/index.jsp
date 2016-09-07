<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Allocations"/></h4>
            </div>
            <div class="panel-body">
                <form method="GET" class="ajaxify" action="${contextPath}/admin/reports/allocations">
                    <div class="datepicker-container">
                        <div class="datepicker-text-container">
                            <span class="fa fa-calendar datepicker-icon"></span>
                            <div class="datepicker-text"></div>
                        </div>
                        <input type="hidden" name="date" class="datepicker-input auto-submit" class="form-control" value="${Day}" />
                        <div class="datepicker" data-show-on-init="false" data-day-config='${dayConfigs}' data-max-date='${maxDate}'></div>
                    </div>
                </form>

                <div class="unit-2">
                    <c:choose>
                        <c:when test="${empty RangeMap}">
                            <div class="alert alert-danger unit"><fmt:message key="NoBookingsForSelectedDate"/></div>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive unit">
                                <table class="table table-bordered table-round table-booking table-fixed">
                                    <thead>
                                        <tr>
                                            <th class="text-center"><fmt:message key="TimeShort"/></th>
                                                <c:forEach var="WeekDay" items="${WeekDays}">
                                                <th class="text-center ${Day == WeekDay ? 'booking-selected-date' : ''}">
                                                    <fmt:message key="DayShort-${WeekDay.dayOfWeek}"/>
                                                    <br /><joda:format value="${WeekDay}" pattern="dd.MM."/>
                                                </th>
                                            </c:forEach>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="TimeRange" items="${RangeMap}">
                                            <tr>
                                                <joda:format value="${TimeRange.startTime}" pattern="HH:mm" var="startTime"/>
                                                <joda:format value="${TimeRange.endTime}" pattern="HH:mm" var="endTime"/>
                                                <td class="booking-time">${startTime}</td>

                                                <c:forEach var="WeekDay" items="${WeekDays}">
                                                    <c:set var="offerCount" value="0"/>

                                                    <c:forEach var="TimeSlot" items="${TimeRange.timeSlots}">
                                                        <c:if test="${TimeSlot.date.dayOfWeek == WeekDay.dayOfWeek}">
                                                            <c:set var="offerCount" value="${offerCount + fn:length(TimeSlot.config.offers)}"/>
                                                        </c:if> 
                                                    </c:forEach>

                                                    <c:choose>
                                                        <c:when test="${offerCount>0}">
                                                            <td class="booking-bookable">
                                                                <div class="booking-offer-container">
                                                                    <c:forEach var="TimeSlot" items="${TimeRange.timeSlots}">
                                                                        <c:if test="${TimeSlot.date.dayOfWeek == WeekDay.dayOfWeek}">
                                                                            <div class="booking-offer-row">
                                                                                <c:set var="urlDetail" value="/admin/reports/bookinglist/${TimeSlot.date}"/>
                                                                                <c:choose>
                                                                                    <c:when test="${empty TimeSlot.bookings}">
                                                                                        -
                                                                                    </c:when>
                                                                                    <c:otherwise>
                                                                                        <c:forEach var="Booking" items="${TimeSlot.bookings}">
                                                                                            <a class="ajaxify booking-gallery-offer" href="${urlDetail}" style="background-color: ${Booking.offer.hexColor};">
                                                                                                <i class="fa fa-info-circle text-center" data-toggle="tooltip" data-placement="top" data-content="${Booking.player}<br /> ${Booking.duration} min<br />${Booking.comment}"></i>
                                                                                            </a>
                                                                                        </c:forEach>
                                                                                    </c:otherwise>
                                                                                </c:choose>
                                                                            </div>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                </div>
                                                            </td>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <td class="booking-disabled"></td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:forEach>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>
