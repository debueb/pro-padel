<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="MyBookings"/></h4>
            </div>
        </div>


        <c:choose>
            <c:when test="${not empty Bookings}">
                <div class="list-group">
                    <c:forEach items="${Bookings}" var="Booking">
                        <a href="/account/bookings/booking/${Booking.UUID}" class="list-group-item ajaxify">
                            <div class="list-item-text"><joda:format value="${Booking.bookingDate}" pattern="EE, dd.MM.YYYY"/> - <joda:format value="${Booking.bookingTime}" pattern="HH:mm"/> - <joda:format value="${Booking.bookingEndTime}" pattern="HH:mm"/></div>
                        </a>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info">
                    <fmt:message key="NoBookingsYet"/>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>
