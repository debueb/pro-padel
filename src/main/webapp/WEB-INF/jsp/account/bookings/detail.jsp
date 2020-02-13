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

                    <jsp:include page="/WEB-INF/jsp/bookings/include/booking-data.jsp"/>

                    <c:choose>
                        <c:when test="${empty Booking.offer}">
                            <div class="alert alert-warning text-center"><fmt:message key="BookingCannotBeCancelled"/></div>
                        </c:when>
                        <c:otherwise>
                            <a class="btn btn-primary btn-block unit" href="/bookings/booking/${Booking.UUID}/cancel"><fmt:message key="CancelBooking2"/></a>
                        </c:otherwise>
                    </c:choose>
                </spf:form>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
