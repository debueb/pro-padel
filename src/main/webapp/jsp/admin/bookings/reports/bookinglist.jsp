<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        
        <div class="page-header">
            <h1><fmt:message key="BookingList"/></h1>
        </div>
      
        <spf:form method="POST" modelAttribute="DateRange" action="/admin/bookings/reports/bookinglist">
            <%-- Start Datum --%>
            <div class="datepicker-container">
                <div class="datepicker-text-container form-top-element">
                    <div class="datepicker-label"><fmt:message key="Start"/></div>
                    <span class="fa fa-calendar datepicker-icon"></span>
                    <div class="datepicker-text"></div>
                </div>
                <spf:input type="hidden" path="startDate" class="datepicker-input form-control" value="${DateRange.startDate}"/>
                <div class="datepicker" data-show-on-init="false" data-allow-past="true"></div>
            </div>

            <%-- End Datum --%>
            <div class="datepicker-container">
                <div class="datepicker-text-container form-bottom-element">
                    <div class="datepicker-label"><fmt:message key="End"/></div>
                    <span class="fa fa-calendar datepicker-icon"></span>
                    <div class="datepicker-text"></div>
                </div>
                <spf:input type="hidden" path="endDate" class="datepicker-input form-control"/>
                <div class="datepicker" data-show-on-init="false" data-allow-past="true" value="${DateRange.endDate}"></div>
            </div>
            <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Select"/></button>
        </spf:form>
        
        <div style="margin-top: 40px;">
            <table class="table table-striped table-bordered table-centered datatable">
                <thead>
                    <th><fmt:message key="BookingDate"/></th>
                    <th><fmt:message key="GameDate"/></th>
                    <th><fmt:message key="Day"/></th>
                    <th><fmt:message key="Time"/></th>
                    <th><fmt:message key="Offer"/></th>
                    <th><fmt:message key="Player"/></th>
                    <th><fmt:message key="PaymentMethod"/></th>
                    <th><fmt:message key="Price"/></th>
                </thead>
                <tbody>
                <c:forEach items="${Bookings}" var="Booking">
                    <tr>
                        <td><joda:format value="${Booking.blockingTime}" pattern="yyyy-MM-dd"/></td>
                        <td><joda:format value="${Booking.bookingDate}" pattern="yyyy-MM-dd"/></td>
                        <td><joda:format value="${Booking.bookingDate}" pattern="EE"/></td>
                        <td><joda:format value="${Booking.bookingTime}" pattern="HH:mm"/> - <joda:format value="${Booking.bookingEndTime}" pattern="HH:mm"/></td>
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
<jsp:include page="/jsp/include/datatables.jsp"/>
<jsp:include page="/jsp/include/footer.jsp"/>
