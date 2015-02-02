<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        
        <div class="page-header">
            <h1><fmt:message key="Bookings"/></h1>
        </div>

        <div class="list-group">
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/admin/bookings/paypal"/>
                <jsp:param name="key" value="PayPal"/>
                <jsp:param name="icon" value="paypal"/>
            </jsp:include>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/admin/bookings/paymill"/>
                <jsp:param name="key" value="PayMill"/>
                <jsp:param name="icon" value="credit-card"/>
            </jsp:include>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/admin/bookings/voucher"/>
                <jsp:param name="key" value="Vouchers"/>
                <jsp:param name="icon" value="gift"/>
            </jsp:include>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/admin/bookings/settings"/>
                <jsp:param name="key" value="CalendarSettings"/>
                <jsp:param name="icon" value="calendar"/>
            </jsp:include>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/admin/bookings/reservations"/>
                <jsp:param name="key" value="CourtReservations"/>
                <jsp:param name="icon" value="cube"/>
            </jsp:include>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/admin/bookings/reports"/>
                <jsp:param name="key" value="Reports"/>
                <jsp:param name="icon" value="pie-chart"/>
            </jsp:include>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
