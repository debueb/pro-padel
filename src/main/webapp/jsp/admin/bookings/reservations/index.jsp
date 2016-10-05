<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="CourtReservations"/></h4>
            </div>
            <div class="panel-body">
                <jsp:include page="/jsp/admin/include/daterange.jsp"/>
                
                <div class="table-responsive unit-2">
                    <table class="table table-striped table-bordered unit">
                        <thead>
                        <th><fmt:message key="Date"/></th>
                        <th><fmt:message key="Day"/></th>
                        <th><fmt:message key="Time"/></th>
                        <th><fmt:message key="Offer"/></th>
                        <th><fmt:message key="Price"/></th>
                        <th><fmt:message key="Comment"/></th>
                        <th><fmt:message key="ReservedBy"/></th>
                        <th class="text-center"><fmt:message key="Delete"/></th>
                        <th class="text-center"><fmt:message key="DeleteAll"/></th>
                        </thead>
                        <tbody>
                            <c:forEach items="${Reservations}" var="Booking">
                                <tr>
                                    <td>${Booking.bookingDate}</td>
                                    <td><joda:format value="${Booking.bookingDate}" pattern="EE"/></td>
                                    <td><joda:format value="${Booking.bookingTime}" pattern="HH:mm"/> - <joda:format value="${Booking.bookingEndTime}" pattern="HH:mm"/></td>
                                    <td>${Booking.offer}</td>
                                    <td>${Booking.amount}</td>
                                    <td>${Booking.comment}</td>
                                    <td><a href="${contextPath}/players/player/${Booking.player.UUID}" class="ajaxify">${Booking.player}</a></td>
                                    <td class="text-center"><a class="ajaxify fa fa-minus-circle" href="/admin/bookings/reservations/${Booking.id}/delete"></a></td>
                                    <td class="text-center"><a class="ajaxify fa fa-minus-circle" href="/admin/bookings/reservations/${Booking.id}/deleteall"></a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="unit">
                    <a href="${contextPath}/admin/bookings/reservations/add" class="btn btn-primary btn-block ajaxify"><fmt:message key="Add"/></a>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
