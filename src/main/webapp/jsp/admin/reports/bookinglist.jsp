<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1">
        <jsp:include page="/jsp/include/back.jsp"/>
        
        <div class="page-header">
            <h1><fmt:message key="BookingList"/></h1>
        </div>
      
        <jsp:include page="/jsp/admin/reports/include/daterange.jsp"/>
        
        <div style="margin-top: 40px;">
            <table class="table table-striped table-bordered table-centered datatable">
                <thead>
                    <th><fmt:message key="GameDate"/></th>
                    <th><fmt:message key="Day"/></th>
                    <th><fmt:message key="Time"/></th>
                    <th><fmt:message key="BookingDate"/></th>
                    <th><fmt:message key="Offer"/></th>
                    <th><fmt:message key="Player"/></th>
                    <th><fmt:message key="PaymentMethod"/></th>
                    <th><fmt:message key="Price"/></th>
                </thead>
                <tbody>
                <c:forEach items="${Bookings}" var="Booking">
                    <tr>
                        <td><joda:format value="${Booking.bookingDate}" pattern="yyyy-MM-dd"/></td>
                        <td><joda:format value="${Booking.bookingDate}" pattern="EE"/></td>
                        <td><joda:format value="${Booking.bookingTime}" pattern="HH:mm"/> - <joda:format value="${Booking.bookingEndTime}" pattern="HH:mm"/></td>
                        <td><joda:format value="${Booking.blockingTime}" pattern="yyyy-MM-dd"/></td>
                        <td>${Booking.offer}</td>
                        <td>${Booking.player}</td>
                        <td>${Booking.paymentMethod}</td>
                        <td>${Booking.amount} ${Booking.currency.symbol}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="unit">
            <fmt:message key="TotalAmount">
                <fmt:param value="${Total}"/>
            </fmt:message>
        </div>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>
