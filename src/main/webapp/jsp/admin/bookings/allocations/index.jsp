<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header">
            <h1><fmt:message key="CourtAllocations"/></h1>
        </div>

        <div class="datepicker-container">
            <div class="datepicker-text-container">
                <span class="fa fa-calendar datepicker-icon"></span>
                <div class="datepicker-text"></div>
            </div>
            <input type="hidden" class="datepicker-input" class="form-control" value="${Day}" />
            <div class="datepicker" data-show-on-init="false" data-redirect-on-select="/admin/bookings/allocations/{date}"></div>
        </div>

        <c:set var="width" value="${10/CourtCount}"/>
        <fmt:formatNumber var="width" value="${width}" maxFractionDigits="0"/>

        <c:if test="${not empty CourtCount}">
            <div class="table-responsive table-responsive-booking unit">
                <div class="container-fluid">
                    <div class="row-fluid reservation-row">
                        <span class="col-xs-2 text-center"><fmt:message key="Time"/></span>
                        <c:forEach var="courtNumber" begin="1" end="${CourtCount}">
                            <span class="col-xs-${width} text-center"><fmt:message key="Court"/> ${courtNumber}</span>
                        </c:forEach>
                    </div>
                </div>
                <c:forEach var="TimeSlot" items="${TimeSlots}">
                    <div class="container-fluid">
                        <div class="row-fluid reservation-row">
                            <span class="col-xs-2 text-center"><joda:format value="${TimeSlot.startTime}" pattern="HH:mm" /></span>
                            <c:forEach var="courtNumber" begin="0" end="${CourtCount+1}">
                                <c:forEach var="Booking" items="${TimeSlot.bookings}">
                                    <c:choose>
                                        <c:when test="${TimeSlot.startTime == Booking.bookingTime and Booking.courtNumber == courtNumber}">
                                            <c:set var="height" value="${Booking.duration / TimeSlot.config.minInterval * 35}"/>
                                            <fmt:formatNumber var="height" value="${height}" maxFractionDigits="0"/>
                                            <div class="col-xs-${width} col-xs-offset-${width*courtNumber} relative" style="min-height: 0;">
                                                <div class="text-center booked" style="height: ${height}px;">
                                                    <div><a class="ajaxify" href="/players/player/${Booking.player.id}">${Booking.player.displayName}</a></div>
                                                    <div><joda:format value="${Booking.bookingTime}" pattern="HH:mm" /> - <joda:format value="${Booking.bookingEndTime}" pattern="HH:mm" /></div>
                                                    <div>${Booking.duration} <fmt:message key="Minutes"/></div>
                                                </div>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="col-xs-10" style="min-height: 0;"></span>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </c:forEach>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>
        <div class="error unit">${error}</div>
    </div>
</div>


<jsp:include page="/jsp/include/footer.jsp"/>
