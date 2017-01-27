<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>
        
        <ol class="unit-2 breadcrumb">
            <li><a class="ajaxify" href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a class="ajaxify" href="/admin/bookings"><fmt:message key="Bookings"/></a></li>
            <li><a class="ajaxify" href="/admin/bookings/reservations"><fmt:message key="BookingsAndReservations"/></a></li>
            <li class="active"><fmt:message key="EditBooking"/></li>
        </ol>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="EditBooking"/></h4>
            </div>
            <div class="panel-body">
                <div class="alert alert-danger"><fmt:message key="BookingDoesNotExist"/></div>
                <a href="/admin/bookings/reservations" class="btn btn-primary btn-block unit ajaxify"><fmt:message key="Cancel"/></a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
