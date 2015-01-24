<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        
        <div class="page-header">
            <h1><fmt:message key="BookingReports"/></h1>
        </div>

        <div class="list-group">
            <a href="/admin/bookings/reports/bookinglist" class="list-group-item ajaxify">
                <div class="list-item-text"><fmt:message key="BookingList"/></div>
            </a>
            <a href="/admin/bookings/reports/players" class="list-group-item disabled ajaxify">
                <div class="list-item-text"><fmt:message key="TopPlayer"/></div>
            </a>
            <a href="/admin/bookings/reports/paymentmethods" class="list-group-item disabled ajaxify">
                <div class="list-item-text"><fmt:message key="TopPaymentMethod"/></div>
            </a>
            <a href="/admin/bookings/reports/weekdays" class="list-group-item disabled ajaxify">
                <div class="list-item-text"><fmt:message key="TopWeekDay"/></div>
            </a>
            <a href="/admin/bookings/reports/times" class="list-group-item disabled ajaxify">
                <div class="list-item-text"><fmt:message key="TopTimes"/></div>
            </a>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
