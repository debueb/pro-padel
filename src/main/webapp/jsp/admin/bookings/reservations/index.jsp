<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header">
            <h1><fmt:message key="CourtReservations"/></h1>
        </div>

        <div class="table-responsive">
            <table class="table table-striped table-bordered unit">
                <thead>
                    <th><fmt:message key="Date"/></th>
                    <th><fmt:message key="Time"/></th>
                    <th><fmt:message key="Duration"/></th>
                    <th><fmt:message key="Offer"/></th>
                    <th><fmt:message key="Comment"/></th>
                    <th><fmt:message key="ReservedBy"/></th>
                    <th class="text-center"><fmt:message key="Delete"/></th>
                </thead>
                <tbody>
                <c:forEach items="${Reservations}" var="Booking">
                    <tr>
                        <td>${Booking.bookingDate}</td>
                        <td><joda:format value="${Booking.bookingTime}" pattern="HH:mm"/></td>
                        <td>${Booking.duration}</td>
                        <td>${Booking.offer}</td>
                        <td>${Booking.comment}</td>
                        <td><a href="${contextPath}/players/player/${Booking.player.id}" class="ajaxify">${Booking.player}</a></td>
                        <td class="text-center"><a class="ajaxify fa fa-minus-circle" href="/admin/bookings/reservations/${Booking.id}/delete"></a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="unit">
            <a href="${contextPath}/admin/bookings/reservations/add" class="btn btn-primary ajaxify"><fmt:message key="Add"/></a>
        </div>
    </div>
</div>


<jsp:include page="/jsp/include/footer.jsp"/>
