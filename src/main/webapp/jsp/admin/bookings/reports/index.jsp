<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        
        <div class="page-header">
            <h1><fmt:message key="BookingReports"/></h1>
        </div>

        <div class="list-group">
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/admin/bookings/reports/allocations"/>
                <jsp:param name="key" value="Allocations"/>
                <jsp:param name="icon" value="cubes"/>
            </jsp:include>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/admin/bookings/reports/bookinglist"/>
                <jsp:param name="key" value="BookingList"/>
                <jsp:param name="icon" value="bars"/>
            </jsp:include>
<%--
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/admin/bookings/reports/players"/>
                <jsp:param name="key" value="TopPlayer"/>
                <jsp:param name="icon" value="user"/>
            </jsp:include>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/admin/bookings/reports/paymentmethods"/>
                <jsp:param name="key" value="TopPaymentMethod"/>
                <jsp:param name="icon" value="credit-card"/>
            </jsp:include>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/admin/bookings/reports/weekdays"/>
                <jsp:param name="key" value="TopWeekDay"/>
                <jsp:param name="icon" value="calendar"/>
            </jsp:include>
             <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/admin/bookings/reports/times"/>
                <jsp:param name="key" value="TopTimes"/>
                <jsp:param name="icon" value="calendar-o"/>
            </jsp:include>
--%>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
