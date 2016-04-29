<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Bookings"/></h4>
            </div>
        </div>


        <div class="list-group">
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/admin/bookings/offers"/>
                <jsp:param name="key" value="Offers"/>
                <jsp:param name="icon" value="dribbble"/>
            </jsp:include>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/admin/bookings/facilities"/>
                <jsp:param name="key" value="Facilities"/>
                <jsp:param name="icon" value="building"/>
            </jsp:include>
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
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
