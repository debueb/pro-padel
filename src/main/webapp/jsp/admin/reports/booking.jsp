<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Booking"/></h4>
            </div>
            <div class="panel-body">
                <div class="container-fluid">
                    <spf:form modelAttribute="Booking">
                        <div class="alert alert-danger"><spf:errors path="*"/></div>
                        <div class="row">
                            <div class="col-xs-4 booking-cell"><fmt:message key="Player"/>:</div>
                            <div class="col-xs-8 booking-cell">${Booking.player}</div>
                        </div>  
                        <jsp:include page="/jsp/bookings/include/booking-data.jsp"/>
                        <div class="row">
                            <div class="col-xs-4 booking-cell">
                                <fmt:message key="Comment"/>:
                            </div>
                            <div class="col-xs-8 booking-cell">
                                <spf:textarea path="comment" class="form-control" maxlength="8000"/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-4 booking-cell">
                                <fmt:message key="Paid"/>:
                            </div>
                            <div class="col-xs-8 booking-cell">
                                <c:choose>
                                    <c:when test="${Booking.paymentMethod == 'Cash' or Booking.paymentMethod == 'Reservation'}">
                                        <spf:checkbox path="paymentConfirmed"/><label for="paymentConfirmed1"></label>
                                    </c:when>
                                    <c:otherwise>
                                        <i class="fa fa-${Booking.paymentConfirmed ? 'check' : 'close'}"></i>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-12">
                                <button type="submit" class="btn btn-primary btn-block"><fmt:message key="Save"/></button>
                            </div>
                            <div class="col-xs-12 unit-2">
                                <a href="/admin/reports/booking/${Booking.id}/delete" class="btn btn-danger btn-block"><fmt:message key="Delete"/></a>
                            </div>
                        </div>
                    </spf:form>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>
